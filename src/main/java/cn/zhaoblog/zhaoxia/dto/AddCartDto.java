package cn.zhaoblog.zhaoxia.dto;/**
 * Created by 16204 on 2017/11/5.
 */

import java.io.Serializable;

/**
 * 添加到购物车
 *
 * @author qingzhou
 *         2017-11-05 0:36
 */
public class AddCartDto implements Serializable {
    private static final long serialVersionUID = -1432219982512102507L;

    private Long id;
    private Long specId;
    private Integer count;

    public AddCartDto(Long id, Long specId, Integer count) {
        this.id = id;
        this.specId = specId;
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSpecId() {
        return specId;
    }

    public void setSpecId(Long specId) {
        this.specId = specId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "AddCartDto{" +
                "id=" + id +
                ", specId=" + specId +
                ", count=" + count +
                '}';
    }
}
