package cn.zhaoblog.zhaoxia.entity;

import java.io.Serializable;

/**
 * Created by 16204 on 2017/10/9.
 */
public class Brand implements Serializable{

    private static final long serialVersionUID = 6786400149659717663L;

    /**
     * 品牌ID
     */
    private long id;
    /**
     * 品牌名称
     */
    private String name;

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

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
