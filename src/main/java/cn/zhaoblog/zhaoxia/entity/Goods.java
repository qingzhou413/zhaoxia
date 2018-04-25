package cn.zhaoblog.zhaoxia.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 16204 on 2017/10/4.
 */
public class Goods implements Serializable {

    private static final long serialVersionUID = -7061347110664751843L;

    /**
     * 商品ID
     */
    private long id;
    /**
     * 主类目ID
     */
    private long rootCatId;
    /**
     * 子类目ID
     */
    private long subCatId;
    /**
     * 品牌ID
     */
    private long brandId;
    /**
     * 包装方式ID
     */
    private long packId;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 售卖数量
     */
    private Integer saleCount;
    /**
     * 不同规格相应明细
     */
    private List<GoodsSpecDet> specDets;
    /**
     * 主图
     */
    private String headImg;
    /**
     * 其他图片
     */
    private String detImgsStr;
    /**
     * 商品详细描述
     */
    private String desc;
    /**
     * 查看次数
     */
    private Integer viewCount;
    /**
     * 是否上架中
     */
    private Boolean active;

    public Goods() {
        this.saleCount = 0;
        this.viewCount = 0;
        this.active = true;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRootCatId() {
        return rootCatId;
    }

    public void setRootCatId(long rootCatId) {
        this.rootCatId = rootCatId;
    }

    public long getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(long subCatId) {
        this.subCatId = subCatId;
    }

    public long getBrandId() {
        return brandId;
    }

    public void setBrandId(long brandId) {
        this.brandId = brandId;
    }

    public long getPackId() {
        return packId;
    }

    public void setPackId(long packId) {
        this.packId = packId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GoodsSpecDet> getSpecDets() {
        return specDets;
    }

    public void setSpecDets(List<GoodsSpecDet> specDets) {
        this.specDets = specDets;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(Integer saleCount) {
        this.saleCount = saleCount;
    }

    public String getDetImgsStr() {
        return detImgsStr;
    }

    public void setDetImgsStr(String detImgsStr) {
        this.detImgsStr = detImgsStr;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", rootCatId=" + rootCatId +
                ", subCatId=" + subCatId +
                ", brandId=" + brandId +
                ", packId=" + packId +
                ", name='" + name + '\'' +
                ", saleCount=" + saleCount +
                ", specDets=" + specDets +
                ", headImg='" + headImg + '\'' +
                ", detImgsStr='" + detImgsStr + '\'' +
                ", desc='" + desc + '\'' +
                ", viewCount='" + viewCount + '\'' +
                ", active='" + active + '\'' +
                '}';
    }
}
