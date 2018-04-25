package cn.zhaoblog.zhaoxia.biz;

import cn.zhaoblog.zhaoxia.entity.PackStyle;
import cn.zhaoblog.zhaoxia.mapper.PackStyleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 16204 on 2017/10/9.
 */
@Service
public class PackStyleBiz {

    @Autowired
    private PackStyleMapper mapper;

    public PackStyle findById(long id){
        return mapper.selectById(id);
    }

    public List<PackStyle> findAll(){
        return mapper.selectAll();
    }

    @Transactional(rollbackFor = {Exception.class})
    public void save(PackStyle packStyle){
        //TODO 检查是否重复
        mapper.insert(packStyle);
    }
}
