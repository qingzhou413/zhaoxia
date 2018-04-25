package cn.zhaoblog.zhaoxia.entity;/**
 * Created by 16204 on 2017/10/29.
 */

import java.io.Serializable;
import java.util.Date;

/**
 * 购物车
 *
 * @author qingzhou
 *         2017-10-29 16:21
 */
public class Cart implements Serializable {
    private static final long serialVersionUID = -509944792277261698L;

    private Long id;
    private Long userId;
    private Long goodsId;
    private Long specDetId;
    private Integer count;
    private Date addTime;

    private Goods goods;
    private GoodsSpecDet specDet;
    private Boolean active;
    public Cart() {
    }

    public Cart(Long userId, Long goodsId, Long specDetId, Integer count) {
        this.userId = userId;
        this.goodsId = goodsId;
        this.specDetId = specDetId;
        this.count = count;
        this.addTime = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getSpecDetId() {
        return specDetId;
    }

    public void setSpecDetId(Long specDetId) {
        this.specDetId = specDetId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public GoodsSpecDet getSpecDet() {
        return specDet;
    }

    public void setSpecDet(GoodsSpecDet specDet) {
        this.specDet = specDet;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", goodsId=" + goodsId +
                ", specDetId=" + specDetId +
                ", count=" + count +
                ", addTime=" + addTime +
                ", goods=" + goods +
                ", specDet=" + specDet +
                ", active=" + active +
                '}';
    }
}
