package cn.zhaoblog.zhaoxia.biz;

import cn.zhaoblog.zhaoxia.entity.Category;
import cn.zhaoblog.zhaoxia.exception.CategoryException;
import cn.zhaoblog.zhaoxia.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qingzhou
 */
@Service
public class CategoryBiz {

    @Autowired
    private CategoryMapper mapper;

    public List<Category> findByParentId(long parentId){
        Map<String, Object> param = new HashMap<>();
        param.put("parentId", parentId);
        return mapper.selectByCondition(param);
    }

    public List<Category> findRoots(){
        Map<String, Object> param = new HashMap<>();
        param.put("parentId", -1);
        return mapper.selectByCondition(param);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void save(Category cat){
        long parentId = cat.getParentId();
        String name = cat.getName();
        if(parentId != -1){
            //not root category
            Map<String, Object> idParam = new HashMap<>();
            idParam.put("id", parentId);
            List<Category> dbCat = mapper.selectByCondition(idParam);
            if(dbCat == null || dbCat.size() == 0){
                throw CategoryException.CATEGORY_NOT_EXISTS;
            }
        }
        Map<String, Object> nameParam = new HashMap<>();
        nameParam.put("parentId", parentId);
        nameParam.put("name", name);
        List<Category> dbCat = mapper.selectByCondition(nameParam);
        if(dbCat != null && dbCat.size() > 0){
            throw CategoryException.CATEGORY_ALREADY_EXISTS;
        }
        mapper.insert(cat);
    }

    public Category findById(long id) {
        return mapper.selectById(id);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void delete(Long id) {
        List<Category> subs = this.findByParentId(id);
        if(subs != null && subs.size() > 0){
            throw CategoryException.STILL_HAVE_SUBS_CANT_DELETE;
        }
        Category dbCat = this.findById(id);
        if(dbCat == null){
            throw CategoryException.CATEGORY_NOT_EXISTS;
        }
        mapper.deleteByPK(id);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void edit(Long id, String category) {
        mapper.updateName(id, category);
    }
}
