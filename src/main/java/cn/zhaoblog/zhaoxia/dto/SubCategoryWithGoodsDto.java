package cn.zhaoblog.zhaoxia.dto;/**
 * Created by 16204 on 2017/10/28.
 */

import cn.zhaoblog.zhaoxia.entity.Goods;

import java.io.Serializable;
import java.util.List;

/**
 * APP加载子分类及其下面的商品列表
 *
 * @author qingzhou
 *         2017-10-28 11:30
 */
public class SubCategoryWithGoodsDto implements Serializable {
    private static final long serialVersionUID = 1328203153496080326L;

    private long id;
    private long parentId;
    private String name;
    private List<Goods> goods;

    public SubCategoryWithGoodsDto() {
    }

    public SubCategoryWithGoodsDto(long id, long parentId, String name, List<Goods> goods) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.goods = goods;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Goods> getGoods() {
        return goods;
    }

    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }

    @Override
    public String toString() {
        return "SubCategoryWithGoodsDto{" +
                "id=" + id +
                ", parentId='" + parentId + '\'' +
                ", name='" + name + '\'' +
                ", goods=" + goods +
                '}';
    }
}
