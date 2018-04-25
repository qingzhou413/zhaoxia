package cn.zhaoblog.zhaoxia.dto;/**
 * Created by 16204 on 2017/11/5.
 */

import java.io.Serializable;

/**
 * 订单里单个商品数据
 *
 * @author qingzhou
 *         2017-11-05 21:11
 */
public class OrderItemDto implements Serializable{
    private static final long serialVersionUID = 2365054636229305891L;

    private Long cartId;
    private Integer count;

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "OrderItemDto{" +
                "cartId=" + cartId +
                ", count=" + count +
                '}';
    }
}
