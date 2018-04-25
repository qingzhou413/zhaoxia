package cn.zhaoblog.zhaoxia.controller;

import cn.zhaoblog.zhaoxia.biz.CategoryBiz;
import cn.zhaoblog.zhaoxia.biz.GoodsBiz;
import cn.zhaoblog.zhaoxia.dto.SubCategoryWithGoodsDto;
import cn.zhaoblog.zhaoxia.entity.Category;
import cn.zhaoblog.zhaoxia.entity.Goods;
import com.joysuch.core.util.MapParam;
import com.joysuch.core.web.ReturnMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 16204 on 2017/10/10.
 */
@RestController
@RequestMapping("category")
public class CategoryController extends BaseController {

    @Autowired
    private CategoryBiz biz;
    @Autowired
    private GoodsBiz goodsBiz;

    @RequestMapping("roots")
    public ReturnMsg roots(){
        ReturnMsg msg = new ReturnMsg();
        List<Category> roots = biz.findRoots();
        msg.setData(roots);
        msg.setErrorCode(0);
        return msg;
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public ReturnMsg edit(HttpServletRequest request){
        ReturnMsg msg = new ReturnMsg();

        Long id = getParaToLong(request, "id");
        String category = getPara(request, "category");
        biz.edit(id, category);
        msg.setErrorCode(0);
        return msg;
    }

    @RequestMapping(value = "get", method = RequestMethod.POST)
    public ReturnMsg get(HttpServletRequest request){
        ReturnMsg msg = new ReturnMsg();

        Long id = getParaToLong(request, "id");
        Category cate = biz.findById(id);
        if (cate == null) {
            msg.addErrorMsg("所选类目不存在");
            return msg;
        }
        msg.setData(cate);
        msg.setErrorCode(0);
        return msg;
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ReturnMsg add(HttpServletRequest request){
        ReturnMsg msg = new ReturnMsg();

        Long parentId = getParaToLong(request, "parentId");
        String category = getPara(request, "category");
        Category cat = new Category();
        cat.setParentId(parentId);
        cat.setName(category);

        biz.save(cat);

        msg.setErrorCode(0);
        return msg;
    }

    @RequestMapping(value = "subCats", method = RequestMethod.GET)
    public ReturnMsg subCats(HttpServletRequest request){
        ReturnMsg msg = new ReturnMsg();
        Long catId = getParaToLong(request, "catId");
        Category dbCat = biz.findById(catId);
        List<Category> subs = biz.findByParentId(catId);
        MapParam ret = new MapParam();
        ret.add("id", catId);
        ret.add("subs", subs);
        ret.add("name", dbCat.getName());
        msg.setData(ret.build());
        msg.setErrorCode(0);
        return msg;
    }

    @RequestMapping(value = "del", method = RequestMethod.POST)
    public ReturnMsg del(HttpServletRequest request){
        ReturnMsg msg = new ReturnMsg();
        Long id = getParaToLong(request, "id");
        biz.delete(id);
        msg.setErrorCode(0);
        return msg;
    }

    @RequestMapping(value = "loadRootCatData", method = RequestMethod.GET)
    public ReturnMsg loadRootCatData(HttpServletRequest request){
        ReturnMsg msg = new ReturnMsg();
        Long rootCatId = getParaToLong(request, "catId");
        List<Category> subs = biz.findByParentId(rootCatId);
        List<SubCategoryWithGoodsDto> data = new ArrayList<>();
        if(subs != null && subs.size() > 0){
            for(Category sub : subs){
                long catId = sub.getId();
                List<Goods> subCatGoods = goodsBiz.getBySubCatId(catId);
                SubCategoryWithGoodsDto subData = new SubCategoryWithGoodsDto(sub.getId(), sub.getParentId(), sub.getName(), subCatGoods == null ? new ArrayList<Goods>(0) : subCatGoods);
                data.add(subData);
            }
        }
        msg.setData(data);
        msg.setErrorCode(0);
        return msg;
    }
}
