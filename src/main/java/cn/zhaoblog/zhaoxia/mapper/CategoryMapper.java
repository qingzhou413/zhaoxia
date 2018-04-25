package cn.zhaoblog.zhaoxia.mapper;

import cn.zhaoblog.zhaoxia.entity.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by 16204 on 2017/10/4.
 */
public interface CategoryMapper {

    List<Category> selectByCondition(Map<String, Object> params);

    void insert(Category category);

    void deleteByPK(Long id);

    Category selectById(long id);

    /**
     * 更新名称
     */
    void updateName(@Param("id") Long id, @Param("name") String name);
}
