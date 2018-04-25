package cn.zhaoblog.zhaoxia.dto;

import cn.zhaoblog.zhaoxia.entity.Goods;
import cn.zhaoblog.zhaoxia.entity.GoodsSpecDet;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 16204 on 2017/10/8.
 */
public class GoodsDto implements Serializable {

    private static final long serialVersionUID = -7717009532448655338L;

    private long id;
    private String name;
    private String category;
    private long rootCatId;
    private long subCatId;
    private List<GoodsSpecDet> specDets;
    private int saleCount;
    private String headImg;
    private String detImgsStr;
    private String desc;
    private String brand;
    private long brandId;
    private long packStyleId;
    private String packStyle;
    private boolean active;

    public GoodsDto(Goods goods, String category, String brand, String packStyle) {
        this.id = goods.getId();
        this.name = goods.getName();
        this.category = category;
        this.specDets = goods.getSpecDets();
        this.saleCount = goods.getSaleCount();
        this.headImg = goods.getHeadImg();
        this.detImgsStr = goods.getDetImgsStr();
        this.desc = goods.getDesc();
        this.brand = brand;
        this.packStyle = packStyle;
        this.active = goods.getActive();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(int saleCount) {
        this.saleCount = saleCount;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getDetImgsStr() {
        return detImgsStr;
    }

    public void setDetImgsStr(String detImgsStr) {
        this.detImgsStr = detImgsStr;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPackStyle() {
        return packStyle;
    }

    public void setPackStyle(String packStyle) {
        this.packStyle = packStyle;
    }
    public List<GoodsSpecDet> getSpecDets() {
        return specDets;
    }
    public void setSpecDets(List<GoodsSpecDet> specDets) {
        this.specDets = specDets;
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

    public long getPackStyleId() {
        return packStyleId;
    }

    public void setPackStyleId(long packStyleId) {
        this.packStyleId = packStyleId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "GoodsDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", rootCatId=" + rootCatId +
                ", subCatId=" + subCatId +
                ", specDets=" + specDets +
                ", saleCount=" + saleCount +
                ", headImg='" + headImg + '\'' +
                ", detImgsStr='" + detImgsStr + '\'' +
                ", desc='" + desc + '\'' +
                ", brand='" + brand + '\'' +
                ", brandId=" + brandId +
                ", packStyleId=" + packStyleId +
                ", packStyle='" + packStyle + '\'' +
                ", active='" + active + '\'' +
                '}';
    }
}
