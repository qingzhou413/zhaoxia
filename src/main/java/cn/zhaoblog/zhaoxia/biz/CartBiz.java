package cn.zhaoblog.zhaoxia.biz;

import cn.zhaoblog.zhaoxia.dto.AddCartDto;
import cn.zhaoblog.zhaoxia.dto.OrderItemDto;
import cn.zhaoblog.zhaoxia.entity.Cart;
import cn.zhaoblog.zhaoxia.entity.Goods;
import cn.zhaoblog.zhaoxia.entity.GoodsSpecDet;
import cn.zhaoblog.zhaoxia.entity.WeiXinUser;
import cn.zhaoblog.zhaoxia.exception.GoodsException;
import cn.zhaoblog.zhaoxia.mapper.CartMapper;
import com.joysuch.core.util.BigDecimalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 购物车业务类
 *
 * @author qingzhou
 *         2017-10-29 16:22
 */
@Service
public class CartBiz {

    private GoodsBiz goodsBiz;
    private CartMapper mapper;

    @Autowired
    public CartBiz(GoodsBiz goodsBiz, CartMapper mapper) {
        this.mapper = mapper;
        this.goodsBiz = goodsBiz;
    }

    private Cart findByUserIdGoodsIdSpecId(long userId, long goodsId, long specId) {
        return mapper.selectByUserIdGoodsIdSpecId(userId, goodsId, specId);
    }

    /**
     * 添加到购物车接口
     *
     * @param user
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = {Exception.class})
    public void addCart(WeiXinUser user, AddCartDto dto) {
        long goodsId = dto.getId();
        Goods goods = goodsBiz.findById(goodsId);
        if (goods == null) {
            throw GoodsException.GOODS_NOT_FOUND;
        }
        long userId = user.getId();
        long specId = dto.getSpecId();
        int count = dto.getCount();
        synchronized ((userId + "_" + goodsId).intern()) {
            Cart dbCartGoodsItem = findByUserIdGoodsIdSpecId(userId, goodsId, specId);
            if (dbCartGoodsItem != null) {
                //添加数量
                int totalCount = dbCartGoodsItem.getCount() + count;
                mapper.updateCartItemCount(dbCartGoodsItem.getId(), dbCartGoodsItem.getCount() + count);
            } else {
                //添加CartItem
                Cart newCart = new Cart(userId, goodsId, specId, count);
                mapper.insert(newCart);
            }
        }
    }

    public int findCartCount(WeiXinUser loginUser) {
        int count = 0;
        if (loginUser != null) {
            List<Cart> items = mapper.selectByUserId(loginUser.getId());
            if (items != null) {
                for (Cart item : items) {
                    count += item.getCount();
                }
            }
        }
        return count;
    }

    public List<Cart> findByUserId(long id) {
        List<Cart> list = mapper.selectByUserId(id);
        if (list != null) {
            Iterator<Cart> iter = list.iterator();
            while (iter.hasNext()) {
                Cart item = iter.next();
                boolean fillRes = fillCartGoodsAndSpec(item);
                if (!fillRes) {
                    iter.remove();
                    mapper.deleteByUserIdAndCartId(item.getUserId(), item.getId());
                }
            }
        }
        return list;
    }

    /**
     * 填写商品及规格，如果找不到则返回false
     *
     * @param item
     * @return
     */
    private boolean fillCartGoodsAndSpec(Cart item) {
        Long goodsId = item.getGoodsId();
        Goods goods = goodsBiz.findById(goodsId);
        if (goods == null) {
            return false;
        }
        item.setGoods(goods);
        Long specId = item.getSpecDetId();
        GoodsSpecDet det = goodsBiz.findGoodsSpecById(specId);
        if (det == null) {
            return false;
        }
        item.setSpecDet(det);
        return true;
    }

    @Transactional(rollbackFor = {Exception.class})
    public void remove(WeiXinUser loginUser, Long id) {
        mapper.deleteByUserIdAndCartId(loginUser.getId(), id);
    }

    public Cart findByUserAndId(WeiXinUser loginUser, Long cartId) {
        Cart dbCart = mapper.selectByUserIdAndId(loginUser.getId(), cartId);
        if (dbCart != null) {
            fillCartGoodsAndSpec(dbCart);
        }
        return dbCart;
    }

    public List<Cart> findCartsDetails(List<OrderItemDto> items, WeiXinUser loginUser) {
        List<Cart> list = new ArrayList<>(items.size());
        for (OrderItemDto item : items) {
            Cart cart = this.findByUserAndId(loginUser, item.getCartId());
            if (cart != null) {
                //购物车里的数量没刷新到数据库，这里用缓存覆盖
                Integer itemCount = item.getCount();
                cart.setCount(itemCount);
                list.add(cart);
            }
        }
        return list;
    }

    @Transactional(rollbackFor = {Exception.class})
    public void removeCart(Cart cart) {
        mapper.deleteByUserIdAndCartId(cart.getUserId(), cart.getId());
    }
}
