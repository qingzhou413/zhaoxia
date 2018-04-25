package cn.zhaoblog.zhaoxia.controller;

import cn.zhaoblog.zhaoxia.biz.BrandBiz;
import cn.zhaoblog.zhaoxia.entity.Brand;
import com.joysuch.core.util.StringUtil;
import com.joysuch.core.web.ReturnMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 16204 on 2017/10/13.
 */
@RestController
@RequestMapping("brand")
public class BrandController extends BaseController {

    @Autowired
    private BrandBiz biz;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ReturnMsg list(HttpServletRequest request){
        ReturnMsg msg = new ReturnMsg();
        msg.setData(biz.list());
        msg.setErrorCode(0);
        return msg;
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ReturnMsg add(HttpServletRequest request){
        ReturnMsg msg = new ReturnMsg();
        String name = getPara(request, "name");
        if(StringUtil.isEmpty(name)){
            msg.addErrorMsg("品牌名称不能为空");
            return msg;
        }
        Brand brand = new Brand();
        brand.setName(name);
        biz.save(brand);
        msg.setErrorCode(0);
        return msg;
    }

    @RequestMapping(value = "del", method = RequestMethod.POST)
    public ReturnMsg del(HttpServletRequest request){
        ReturnMsg msg = new ReturnMsg();
        Long id = getParaToLong(request, "id");
        if(id == null){
            msg.addErrorMsg("请选择要删除的品牌");
            return msg;
        }
        biz.del(id);
        msg.setErrorCode(0);
        return msg;
    }
}
