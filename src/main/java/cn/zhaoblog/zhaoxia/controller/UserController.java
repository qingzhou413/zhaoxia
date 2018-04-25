package cn.zhaoblog.zhaoxia.controller;/**
 * Created by 16204 on 2017/10/29.
 */

import cn.zhaoblog.zhaoxia.biz.WeiXinUserBiz;
import cn.zhaoblog.zhaoxia.common.Contants;
import cn.zhaoblog.zhaoxia.entity.WeiXinUser;
import cn.zhaoblog.zhaoxia.weixin.WeiXinService;
import com.joysuch.core.util.StringUtil;
import com.joysuch.core.web.ReturnMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户
 *
 * @author qingzhou
 *         2017-10-29 21:47
 */
@RestController
@RequestMapping("user")
public class UserController extends MarketBaseController{

    @Autowired
    private WeiXinUserBiz biz;
    @Autowired
    private WeiXinService service;

    @RequestMapping(value = "loginInfo", method = RequestMethod.GET)
    public ReturnMsg loginInfo(HttpServletRequest request, HttpServletResponse response) {
        ReturnMsg msg = new ReturnMsg();
        checkMobileLogin(request);
        msg.setData(getMobileLoginUser(request));
        msg.setErrorCode(0);
        return msg;
    }

    @RequestMapping(value = "info", method = RequestMethod.GET)
    public ReturnMsg info(HttpServletRequest request, HttpServletResponse response){
        ReturnMsg msg = new ReturnMsg();
        String appid = getPara(request, Contants.WX_APPID_KEY);
        String code = getPara(request, Contants.WX_CODE);
        if(StringUtil.isEmpty(code) || StringUtil.isEmpty(appid)){
            msg.setData(Contants.WX_AUTH_PAGE);
            msg.setErrorCode(-1);
            return msg;
        }
        WeiXinUser user = getMobileLoginUser(request);
        if(user == null){
            user = service.info(appid, code);
            if(user == null){
                msg.setData(Contants.WX_AUTH_PAGE);
                msg.setErrorCode(-1);
                return msg;
            }else {
                biz.checkUser(user);
                setMobileLoginUser(request, user);
            }
        }
        msg.setData(user);
        msg.setErrorCode(0);
        return msg;
    }
}
