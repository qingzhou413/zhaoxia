package cn.zhaoblog.zhaoxia.controller;

import cn.zhaoblog.zhaoxia.biz.PackStyleBiz;
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
@RequestMapping("packStyle")
public class PackStyleController extends BaseController {

    @Autowired
    private PackStyleBiz biz;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ReturnMsg list(HttpServletRequest request){
        ReturnMsg msg = new ReturnMsg();
        msg.setData(biz.findAll());
        msg.setErrorCode(0);
        return msg;
    }
}
