package cn.zhaoblog.zhaoxia.entity;

import java.io.Serializable;

/**
 * Created by 16204 on 2017/10/9.
 */
public class PackStyle implements Serializable {

    private static final long serialVersionUID = 1696636385855700624L;

    /**
     * 打包方式ID
     */
    private long id;
    /**
     * 打包方式
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
        return "PackStyle{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
