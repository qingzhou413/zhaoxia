package cn.zhaoblog.zhaoxia.biz;

import cn.zhaoblog.zhaoxia.entity.Brand;
import cn.zhaoblog.zhaoxia.exception.BrandException;
import cn.zhaoblog.zhaoxia.mapper.BrandMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author qingzhou
 */
@Service
public class BrandBiz {

    @Autowired
    private BrandMapper mapper;

    public Brand findById(long id){
        return mapper.selectById(id);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void save(Brand brand){
        String name = brand.getName();
        Brand dbBrand = mapper.selectByName(name);
        if(dbBrand != null){
            throw BrandException.BRAND_NAME_EXISTS_ERROR;
        }
        mapper.insert(brand);
    }

    public List<Brand> list() {
        return mapper.selectAll();
    }

    @Transactional(rollbackFor = {Exception.class})
    public void del(Long id) {
        Brand dbBrand = mapper.selectById(id);
        if(dbBrand == null){
            throw BrandException.BRAND_ID_NOT_EXISTS_ERROR;
        }
        mapper.deleteById(id);
    }
}
