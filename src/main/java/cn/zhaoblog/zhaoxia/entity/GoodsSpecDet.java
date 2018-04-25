package cn.zhaoblog.zhaoxia.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by 16204 on 2017/10/4.
 */
public class GoodsSpecDet implements Serializable{

    private static final long serialVersionUID = 4052683979357860711L;

    /**
     * 明细ID
     */
    private long id;
    /**
     * 商品ID
     */
    private long goodsId;
    /**
     * 规格名称
     */
    private String specName;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 库存
     */
    private Integer leftCnt;
    /**
     * 条码
     */
    private String barCode;

    public GoodsSpecDet() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSpecName() {
        return specName;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getLeftCnt() {
        return leftCnt;
    }

    public void setLeftCnt(Integer leftCnt) {
        this.leftCnt = leftCnt;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    @Override
    public String toString() {
        return "GoodsSpecDet{" +
                "id=" + id +
                ", goodsId=" + goodsId +
                ", specName='" + specName + '\'' +
                ", price=" + price +
                ", leftCnt=" + leftCnt +
                ", barCode=" + barCode +
                '}';
    }
}
