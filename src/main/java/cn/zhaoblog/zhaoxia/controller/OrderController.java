package cn.zhaoblog.zhaoxia.controller;

import cn.zhaoblog.zhaoxia.biz.AddressBiz;
import cn.zhaoblog.zhaoxia.biz.CartBiz;
import cn.zhaoblog.zhaoxia.biz.OrderBiz;
import cn.zhaoblog.zhaoxia.common.Contants;
import cn.zhaoblog.zhaoxia.dto.OrderItemDto;
import cn.zhaoblog.zhaoxia.entity.Address;
import cn.zhaoblog.zhaoxia.entity.Cart;
import cn.zhaoblog.zhaoxia.entity.Order;
import cn.zhaoblog.zhaoxia.entity.WeiXinUser;
import cn.zhaoblog.zhaoxia.util.RequestUtil;
import cn.zhaoblog.zhaoxia.weixin.WeiXinService;
import cn.zhaoblog.zhaoxia.weixin.dto.JsPayReqDto;
import com.joysuch.core.pager.PageBean;
import com.joysuch.core.util.BigDecimalUtil;
import com.joysuch.core.util.JsonUtil;
import com.joysuch.core.util.MapParam;
import com.joysuch.core.web.ReturnMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 订单业务
 *
 * @author qingzhou
 *         2017-11-05 21:08
 */
@RestController
@RequestMapping("order")
public class OrderController extends MarketBaseController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private CartBiz cartBiz;
    private AddressBiz addrBiz;
    private OrderBiz biz;
    private WeiXinService weiXinService;
    @Autowired
    public OrderController(CartBiz cartBiz, AddressBiz addrBiz, OrderBiz biz, WeiXinService weiXinService) {
        this.cartBiz = cartBiz;
        this.addrBiz = addrBiz;
        this.biz = biz;
        this.weiXinService = weiXinService;
    }

    @RequestMapping(value = "load", method = RequestMethod.POST)
    public ReturnMsg load(HttpServletRequest request) {
        ReturnMsg msg = new ReturnMsg();

        checkMobileLogin(request);
        WeiXinUser wxUser = getMobileLoginUser(request);
        Order order = biz.findById(wxUser.getId(), getParaToLong(request, "id"));
        if (order == null) {
            msg.addErrorMsg("订单不存在");
            return msg;
        }
        JsPayReqDto rest = new JsPayReqDto(wxUser.getAppid(), order.getRetPrepayId(), weiXinService.getMchKey(wxUser.getAppid()));
        msg.setData(rest);
        msg.setErrorCode(0);
        return msg;
    }

    @RequestMapping(value = "detail", method = RequestMethod.POST)
    public ReturnMsg detail(HttpServletRequest request) {
        ReturnMsg msg = new ReturnMsg();

        Order order = biz.findById(getParaToLong(request, "id"));
        if (order == null) {
            msg.addErrorMsg("订单不存在");
            return msg;
        }
        msg.setData(order);
        msg.setErrorCode(0);
        return msg;
    }

    @RequestMapping(value = "deliver", method = RequestMethod.POST)
    public ReturnMsg deliver(HttpServletRequest request) {
        ReturnMsg msg = new ReturnMsg();

        Long id = getParaToLong(request, "id");
        Order order = biz.findById(id);
        if (order == null) {
            msg.addErrorMsg("订单不存在");
            return msg;
        }
        biz.deliverOrder(id);
        msg.setErrorCode(0);
        return msg;
    }


    @RequestMapping(value = "mylist", method = RequestMethod.GET)
    public ReturnMsg mylist(HttpServletRequest request) {
        ReturnMsg msg = new ReturnMsg();

        checkMobileLogin(request);
        WeiXinUser wxUser = getMobileLoginUser(request);

        PageBean<Order> page = biz.userOrders(getPageParam(request), wxUser);
        msg.setData(page);
        msg.setErrorCode(0);
        return msg;
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ReturnMsg list(HttpServletRequest request) {
        ReturnMsg msg = new ReturnMsg();

        String appid = "wx2649254e500949cc";
        PageBean<Order> page = biz.list(appid, getPageParam(request));

        msg.setData(page);
        msg.setErrorCode(0);
        return msg;
    }

    @RequestMapping(value = "prePay", method = RequestMethod.POST)
    public ReturnMsg prePay(HttpServletRequest request) {
        ReturnMsg msg = new ReturnMsg();
        checkMobileLogin(request);

        List<OrderItemDto> items = (List<OrderItemDto>) request.getSession().getAttribute(Contants.ORDER_CACHE_KEY);
        if(items == null || items.size() == 0){
            msg.addErrorMsg("查询不到商品，请重新从购物车中选择");
            return msg;
        }

        Long addrId = getParaToLong(request, "addrId");
        if(addrId == null){
            msg.addErrorMsg("订单地址必须填写");
            return msg;
        }

        WeiXinUser loginUser = getMobileLoginUser(request);
        Address address = addrBiz.findAddress(loginUser, addrId);
        if(address == null){
            msg.addErrorMsg("所选地址不存在，请重新选择地址");
            return msg;
        }

        List<Cart> list = cartBiz.findCartsDetails(items, loginUser);
        double total = getCartsTotalFee(list);
        String details = JsonUtil.toJson(list);
        if(total < 20.0){
            msg.addErrorMsg("订单金额必须达到20元才能送货上门哦");
            return msg;
        }

        //预下单
        JsPayReqDto jsPayReqDto = biz.unifiedOrder(loginUser, list, RequestUtil.getIpAddress(request), total, address);
        msg.setData(jsPayReqDto);
        msg.setErrorCode(0);
        return msg;
    }

    @RequestMapping(value = "clearOrderCache", method = RequestMethod.POST)
    public ReturnMsg clearOrderCache(HttpServletRequest request){
        ReturnMsg msg = new ReturnMsg();
        checkMobileLogin(request);
        request.getSession().removeAttribute(Contants.ORDER_CACHE_KEY);
        msg.setData("清除订单缓存成功");
        logger.info("清除订单缓存:" + getMobileLoginUser(request).toString());
        msg.setErrorCode(0);
        return msg;
    }

    @RequestMapping(value = "saveOrderCache", method = RequestMethod.POST)
    public ReturnMsg saveOrderCache(HttpServletRequest request, @RequestBody String json){
        ReturnMsg msg = new ReturnMsg();
        checkMobileLogin(request);

        List<OrderItemDto> items = JsonUtil.fromListJson(json, OrderItemDto.class);
        if(items == null || items.size() == 0){
            msg.addErrorMsg("提交订单有误，请刷新重试!");
            return msg;
        }

        logger.info("保存订单到缓存：" + json);
        request.getSession().setAttribute(Contants.ORDER_CACHE_KEY, items);
        msg.setErrorCode(0);
        return msg;
    }

    @RequestMapping(value = "loadCache", method = RequestMethod.GET)
    public ReturnMsg loadCache(HttpServletRequest request){
        ReturnMsg msg = new ReturnMsg();
        checkMobileLogin(request);
        WeiXinUser loginUser = getMobileLoginUser(request);

        List<OrderItemDto> items = (List<OrderItemDto>) request.getSession().getAttribute(Contants.ORDER_CACHE_KEY);
        if(items == null || items.size() == 0){
            msg.addErrorMsg("查询不到商品，请重新从购物车中选择");
            return msg;
        }

        List<Cart> list = cartBiz.findCartsDetails(items, loginUser);
        double total = getCartsTotalFee(list);

        MapParam param = new MapParam()
                .add("list", list)
                .add("total", total);
        msg.setData(param.build());
        msg.setErrorCode(0);
        return msg;
    }

    private double getCartsTotalFee(List<Cart> list) {
        double total = 0.0;
        for(Cart cart : list){
            double itemPrice = cart.getSpecDet().getPrice().doubleValue();
            double itemPriceTotal = BigDecimalUtil.mul(itemPrice, cart.getCount().doubleValue());
            total = BigDecimalUtil.add(total, itemPriceTotal);
        }
        return total;
    }

}
