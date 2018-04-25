package cn.zhaoblog.zhaoxia.controller;

import cn.zhaoblog.zhaoxia.biz.OrderBiz;
import cn.zhaoblog.zhaoxia.entity.Order;
import cn.zhaoblog.zhaoxia.weixin.dto.ToWeiXinResp;
import cn.zhaoblog.zhaoxia.weixin.dto.WeiXinPayCallbackDto;
import cn.zhaoblog.zhaoxia.weixin.util.XmlUtil;
import com.joysuch.core.util.BigDecimalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

/**
 * 微信回调接口
 *
 * @author qingzhou
 *         2017-11-09 0:11
 */
@Controller
@RequestMapping("wxcallback")
public class WxCallbackController extends MarketBaseController {

    private static final Logger logger = LoggerFactory.getLogger(WxCallbackController.class);

    private OrderBiz orderBiz;

    @Autowired
    public WxCallbackController(OrderBiz orderBiz) {
        this.orderBiz = orderBiz;
    }

    @RequestMapping(value = "pay", method = RequestMethod.POST)
    @ResponseBody
    public String pay(HttpServletRequest request, HttpServletResponse response, @RequestBody String xml) {
        logger.info("wxcallback pay : " + xml);
        ToWeiXinResp resp = new ToWeiXinResp();
        synchronized (WxCallbackController.class){
            try {
                XmlUtil dto = new XmlUtil(WeiXinPayCallbackDto.class);
                WeiXinPayCallbackDto callback = dto.fromXml(xml);
                if (callback != null && "SUCCESS".equals(callback.getReturn_code())) {
                    synchronized (callback.getOut_trade_no().intern()) {
                        //订单成功
                        Order dbOrder = orderBiz.findByWeiXinPayCallback(callback.getMch_id(), callback.getOpenid(), callback.getOut_trade_no());
                        if (dbOrder == null) {
                            resp.setReturn_code("FAIL");
                            resp.setReturn_msg("查询不到该订单");
                        } else {
                            Integer totalFee = callback.getTotal_fee();
                            BigDecimal totalMoney = dbOrder.getTotalMoney();
                            if(dbOrder.getStatus() == 0) {
                                if (BigDecimalUtil.sub(totalFee.doubleValue(), totalMoney.doubleValue()) == 0.0) {
                                    orderBiz.orderSuccess(dbOrder, callback.getTime_end(), callback.getBank_type(), callback.getTransaction_id());
                                    resp.setReturn_code("SUCCESS");
                                    resp.setReturn_msg("OK");
                                }else{
                                    //支付金额不同
                                    resp.setReturn_code("FAIL");
                                    resp.setReturn_msg("total fee not match");
                                }
                            } else {
                                //已经支付过
                                resp.setReturn_code("SUCCESS");
                                resp.setReturn_msg("OK");
                            }
                        }
                    }
                } else {
                    resp.setReturn_code("FAIL");
                    resp.setReturn_msg("wx response not success");
                }
            } catch (Exception e) {
                resp.setReturn_code("FAIL");
                resp.setReturn_msg("error");
            }
        }
        logger.info("回复微信:" + resp.toString());
        return new XmlUtil(ToWeiXinResp.class).toXml(resp, "UTF-8");
    }
}
