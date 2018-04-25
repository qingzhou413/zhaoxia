package cn.zhaoblog.zhaoxia.mapper;

import cn.zhaoblog.zhaoxia.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单DAO
 *
 * @author qingzhou
 *         2017-11-08 1:13
 */
public interface OrderMapper {
    /**
     * 插入新订单
     * @param order
     */
    void insert(Order order);

    /**
     * 根据微信支付回调查询订单
     * @param mchId
     * @param openid
     * @param tradeNum
     * @return
     */
    Order selectByWeiXinPayCallback(@Param("mchId") String mchId, @Param("openId") String openid,
                                    @Param("tradeNum") String tradeNum);

    /**
     * 根据商户号和订单号查询订单
     */
    Order selectByMchIdAndTradeNum(@Param("mchId") String mchId, @Param("tradeNum") String tradeNum);

    /**
     * 订单支付成功
     * @param id
     * @param timeEnd
     * @param bankType
     * @param transactionId
     */
    void updateOrderSuccess(@Param("id") Long id, @Param("timeEnd") Date timeEnd, @Param("bankType") String bankType, @Param("transactionId") String transactionId);

    /**
     * 根据参数查列表
     */
    List<Order> selectByCondition(Map<String, Object> paramMap);

    /**
     * 查询未支付的订单
     */
    List<Order> unpayedOrders();

    /**
     * 关闭订单
     */
    void closeOrder(@Param("mchId") String mchId, @Param("openid") String openid,
                    @Param("transactionId") String transactionId, @Param("tradeNum") String tradeNum);

    /**
     * 查询结果数量
     */
    int selectCountByCondition(Map<String, Object> paramMap);

    /**
     * 根据用户ID和订单ID查询
     */
    Order selectByUserIdAndOrderId(@Param("userId") Long userId, @Param("orderId") Long orderId);

    /**
     * 根据ID查找
     * @param id
     * @return
     */
    Order selectById(@Param("id") Long id);

    /**
     * 设置订单为已配货
     * @param id
     */
    void deliver(Long id);
}
