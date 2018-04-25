package cn.zhaoblog.zhaoxia.task;

import cn.zhaoblog.zhaoxia.biz.OrderBiz;
import cn.zhaoblog.zhaoxia.entity.Order;
import cn.zhaoblog.zhaoxia.weixin.WeiXinService;
import cn.zhaoblog.zhaoxia.weixin.dto.QueryOrderDto;
import cn.zhaoblog.zhaoxia.weixin.dto.QueryOrderResultDTO;
import com.joysuch.core.util.BigDecimalUtil;
import com.sun.tools.corba.se.idl.constExpr.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 定期查询任务
 *
 * @author qingzhou
 *         2017-12-16 12:55
 */
@Component
public class OrderQueryTask {

    private static final Logger logger = LoggerFactory.getLogger(OrderQueryTask.class);

    private OrderBiz orderBiz;
    private WeiXinService weiXinService;

    @Autowired
    public OrderQueryTask(OrderBiz orderBiz, WeiXinService service) {
        this.orderBiz = orderBiz;
        this.weiXinService = service;
    }

    @Scheduled(initialDelay = 60 * 1000, fixedDelay = 600 * 1000)
    public void refreshUnPayedOrders() {

        List<Order> orders = orderBiz.unpayedOrders();
        for (Order order : orders) {
            try {
                refreshStatus(order);
            } catch (Exception e) {
                logger.info("refresh order[" + order.toString() + "] exception: ", e);
            }
        }
    }

    private void refreshStatus(Order order) {
        logger.info("refreshing order: " + order.getId());
        synchronized (order.getTradeNum().intern()) {
            QueryOrderDto query = new QueryOrderDto();
            query.setAppid(order.getAppid());
            query.setOut_trade_no(order.getTradeNum());

            QueryOrderResultDTO queryResult = weiXinService.queryOrder(query);
            if (queryResult.getOut_trade_no().equals(order.getTradeNum())) {
                if ("SUCCESS".equals(queryResult.getTrade_state())) {
                    //订单成功
                    Order dbOrder = orderBiz.findByWeiXinPayCallback(queryResult.getMch_id(), queryResult.getOpenid(),
                            queryResult.getOut_trade_no());
                    if (dbOrder != null) {
                        Integer totalFee = queryResult.getTotal_fee();
                        BigDecimal totalMoney = dbOrder.getTotalMoney();
                        if(dbOrder.getStatus() == 0) {
                            if (BigDecimalUtil.sub(totalFee.doubleValue(), totalMoney.doubleValue()) == 0.0) {
                                orderBiz.orderSuccess(dbOrder, queryResult.getTime_end(), queryResult.getBank_type(), queryResult.getTransaction_id());
                            }else{
                                //支付金额不同
                                logger.error("order[" + order.toString() + "] total fee not match");
                            }
                        } else {
                            //已经支付过
                            logger.info("order[" + order.toString() + "] already payed");
                        }
                    }
                } else if ("CLOSED".equals(queryResult.getTrade_state())) {
                    //订单关闭
                    orderBiz.orderClosed(queryResult.getMch_id(), queryResult.getOpenid(), queryResult.getTransaction_id(),
                            queryResult.getOut_trade_no());
                } else {
                    //其他状态，判断订单是否过期
                    if (System.currentTimeMillis() - 3600 * 1000 > order.getCreateTime().getTime()) {
                        //微信实际是2小时过期，此处提前1小时余量
                        orderBiz.orderClosed(queryResult.getMch_id(), queryResult.getOpenid(), queryResult.getTransaction_id(),
                                queryResult.getOut_trade_no());
                    }
                }
            } else {
                logger.error("query order param TransactionId not match return TransactionId\n" +
                        "order: " + order.toString() + "\n" +
                        "return: " + queryResult.toString() );
            }


        }
    }
}
