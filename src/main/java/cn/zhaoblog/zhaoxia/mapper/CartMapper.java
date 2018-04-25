package cn.zhaoblog.zhaoxia.mapper;/**
 * Created by 16204 on 2017/10/29.
 */

import cn.zhaoblog.zhaoxia.entity.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 购物车DAO
 *
 * @author qingzhou
 *         2017-10-29 16:23
 */
public interface CartMapper {
    /**
     * 查询是否之前加入过购物车
     * @param userId
     * @param goodsId
     * @param specId
     * @return
     */
    Cart selectByUserIdGoodsIdSpecId(@Param("userId") long userId, @Param("goodsId") long goodsId, @Param("specDetId") long specId);

    /**
     * 更新购物车里某个商品的数量
     * @param id
     * @param count
     */
    void updateCartItemCount(@Param("id") Long id, @Param("count") int count);

    /**
     * 添加进购物车
     * @param newCart
     */
    void insert(Cart newCart);

    /**
     * 查询用户购物车
     * @param id
     * @return
     */
    List<Cart> selectByUserId(@Param("userId") long id);

    /**
     * 删除购物车记录
     * @param userId
     * @param id
     */
    void deleteByUserIdAndCartId(@Param("userId")long userId, @Param("id")Long id);

    /**
     * 订单里根据ID查询商品信息
     * @param id
     * @param cartId
     * @return
     */
    Cart selectByUserIdAndId(@Param("userId") long id, @Param("id") Long cartId);
}
