package cn.zhaoblog.zhaoxia.entity;

import java.io.Serializable;

/**
 * Created by 16204 on 2017/10/4.
 */
public class Category implements Serializable {

    private static final long serialVersionUID = 3594035703753054249L;
    /**
     * 类目ID
     */
    private long id;
    /**
     * 父类目ID
     */
    private long parentId;
    /**
     * 类目名字
     */
    private String name;

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

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                '}';
    }
}
