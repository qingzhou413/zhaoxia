package cn.zhaoblog.zhaoxia.mapper;

import cn.zhaoblog.zhaoxia.entity.PackStyle;

import java.util.List;

/**
 * Created by 16204 on 2017/10/9.
 */
public interface PackStyleMapper {

    List<PackStyle> selectAll();

    PackStyle selectById(long id);

    void insert(PackStyle packStyle);
}
