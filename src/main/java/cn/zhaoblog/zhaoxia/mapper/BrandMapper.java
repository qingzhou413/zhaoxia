package cn.zhaoblog.zhaoxia.mapper;

import cn.zhaoblog.zhaoxia.entity.Brand;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 16204 on 2017/10/9.
 */
public interface BrandMapper {

    Brand selectById(long id);

    void insert(Brand brand);

    List<Brand> selectAll();

    Brand selectByName(@Param("name") String name);

    void deleteById(@Param("id") Long id);
}
