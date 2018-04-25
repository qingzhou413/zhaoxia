package cn.zhaoblog.zhaoxia.biz;

import cn.zhaoblog.zhaoxia.dto.GoodsDto;
import cn.zhaoblog.zhaoxia.entity.Address;
import cn.zhaoblog.zhaoxia.entity.Cart;
import cn.zhaoblog.zhaoxia.entity.Goods;
import cn.zhaoblog.zhaoxia.entity.Order;
import cn.zhaoblog.zhaoxia.entity.WeiXinUser;
import cn.zhaoblog.zhaoxia.exception.OrderException;
import cn.zhaoblog.zhaoxia.mapper.OrderMapper;
import cn.zhaoblog.zhaoxia.weixin.WeiXinService;
import cn.zhaoblog.zhaoxia.weixin.dto.JsPayReqDto;
import cn.zhaoblog.zhaoxia.weixin.dto.UnifiedOrderDto;
import cn.zhaoblog.zhaoxia.weixin.dto.UnifiedOrderResultDTO;
import cn.zhaoblog.zhaoxia.weixin.dto.WeiXinPayCallbackDto;
import com.joysuch.core.pager.PageBean;
import com.joysuch.core.pager.PageParam;
import com.joysuch.core.util.BigDecimalUtil;
import com.joysuch.core.util.DateUtil;
import com.joysuch.core.util.JsonUtil;
import com.joysuch.core.util.MapParam;
import com.joysuch.core.util.StringUtil;
import com.joysuch.core.util.UuidUtil;
import com.sun.tools.corba.se.idl.constExpr.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单业务类
 *
 * @author qingzhou
 *         2017-11-08 1:12
 */
@Service
public class OrderBiz {

    private WeiXinService weiXinService;
    private OrderMapper mapper;
    private CartBiz cartBiz;
    private GoodsBiz goodsBiz;

    @Autowired
    public OrderBiz(WeiXinService weiXinService, OrderMapper mapper, CartBiz cartBiz, GoodsBiz goodsBiz) {
        this.weiXinService = weiXinService;
        this.mapper = mapper;
        this.cartBiz = cartBiz;
        this.goodsBiz = goodsBiz;
    }

    public JsPayReqDto unifiedOrder(WeiXinUser loginUser, List<Cart> list, String ip, double total, Address address) {
        String appid = loginUser.getAppid();
        String openId = loginUser.getOpenid();
        BigDecimal money = new BigDecimal(BigDecimalUtil.mul(total, 100));
        String detail = JsonUtil.toJson(list);
        StringBuilder sb = new StringBuilder();
        for (Cart cart : list) {
            String thisGoods = cart.getGoods().getName() + "(" + cart.getSpecDet().getSpecName() +
                    ") * " + cart.getCount();
            if (sb.length() + thisGoods.length() >= 100) {
                sb.append("等");
            } else {
                sb.append(thisGoods);
            }
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);

        UnifiedOrderDto orderDto = new UnifiedOrderDto(appid, "attach_demo", sb.toString(),
                openId, ip, money.intValue());
        UnifiedOrderResultDTO preOrderRes = weiXinService.unifiedOrder(orderDto);
        if (preOrderRes == null) {
            throw OrderException.WEI_XIN_PRE_ORDER_ERROR;
        }
        JsPayReqDto rest = new JsPayReqDto(loginUser.getAppid(), preOrderRes.getPrepay_id(), weiXinService.getMchKey(loginUser.getAppid()));

        Order order = new Order();
        order.setAppid(appid);
        order.setMchId(orderDto.getMch_id());
        order.setUserId(loginUser.getId());
        order.setOpenId(openId);
        order.setAddress(JsonUtil.toJson(address));
        order.setNickname(loginUser.getNickname());
        order.setHeadimgurl(loginUser.getHeadimgurl());
        order.setTradeNum(orderDto.getOut_trade_no());
        order.setTradeType(orderDto.getTrade_type());
        order.setDetails(detail);
        order.setTotalMoney(money);
        order.setCreateTime(new Date());
        order.setStatus(0);
        order.setRetPrepayId(preOrderRes.getPrepay_id());
        order.setDeliverStatus(0);
        mapper.insert(order);
        //删除该订单对应的购物车列表，并扣库存
        String details = order.getDetails();
        if (StringUtil.notEmpty(details)) {
            List<Cart> carts = JsonUtil.fromListJson(details, Cart.class);
            if(carts != null){
                for (Cart cart : carts) {
                    cartBiz.removeCart(cart);
                    goodsBiz.reduceLeftCount(cart.getSpecDetId(), cart.getCount());
                }
            }
        }

        return rest;
    }

    public Order findByWeiXinPayCallback(String mchId, String openid, String tradeNum) {
        return mapper.selectByWeiXinPayCallback(mchId, openid, tradeNum);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void orderSuccess(Order dbOrder, String timeend, String bankType, String transactionId) {
        //更新订单状态
        long id = dbOrder.getId();
        Date timeEnd = DateUtil.parseDate(timeend, "yyyyMMddHHmmss");
        mapper.updateOrderSuccess(id, timeEnd, bankType, transactionId);
        List<Cart> list = JsonUtil.fromListJson(dbOrder.getDetails(), Cart.class);
        if (list != null && list.size() > 0) {
            for (Cart cat : list) {
                goodsBiz.addSaleCount(cat.getGoodsId(), cat.getCount());
            }
        }
    }

    public PageBean<Order> list(String appid, PageParam page) {
        Map<String, Object> paramMap = page.getParamMap();
        paramMap.put("appid", appid);
        int count = mapper.selectCountByCondition(paramMap);
        List<Order> list = mapper.selectByCondition(paramMap);
        return new PageBean<>(page.getPageNum(), page.getNumPerPage(), count, list);
    }

    /**
     * 设置订单为已经配货
     */
    public void deliverOrder(Long id) {
        mapper.deliver(id);
    }

    public List<Order> unpayedOrders() {
        return mapper.unpayedOrders();
    }

    @Transactional(rollbackFor = {Exception.class})
    public void orderClosed(String mchId, String openid, String transactionId, String tradeNum) {
        mapper.closeOrder(mchId, openid, transactionId, tradeNum);
        Order order = mapper.selectByMchIdAndTradeNum(mchId, tradeNum);
        if (order != null) {
            //回退库存
            String details = order.getDetails();
            if (StringUtil.notEmpty(details)) {
                List<Cart> carts = JsonUtil.fromListJson(details, Cart.class);
                if(carts != null){
                    for (Cart cart : carts) {
                        goodsBiz.addLeftCount(cart.getSpecDetId(), cart.getCount());
                    }
                }
            }
        }
    }

    public PageBean<Order> userOrders(PageParam page, WeiXinUser wxUser) {
        Map<String, Object> paramMap = page.getParamMap();
        paramMap.put("appid", wxUser.getAppid());
        paramMap.put("openid", wxUser.getOpenid());
        int totalCount = mapper.selectCountByCondition(paramMap);
        List<Order> list = mapper.selectByCondition(paramMap);
        return new PageBean<>(page.getPageNum(), page.getNumPerPage(), totalCount, list);
    }

    public Order findById(long userId, Long orderId) {
        return mapper.selectByUserIdAndOrderId(userId, orderId);
    }

    public Order findById(Long id) {
        return mapper.selectById(id);
    }
}
