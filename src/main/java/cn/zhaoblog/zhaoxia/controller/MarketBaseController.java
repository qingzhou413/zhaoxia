package cn.zhaoblog.zhaoxia.controller;/**
 * Created by 16204 on 2017/11/5.
 */

import cn.zhaoblog.zhaoxia.entity.WeiXinPub;
import cn.zhaoblog.zhaoxia.entity.WeiXinUser;
import cn.zhaoblog.zhaoxia.exception.UserException;

import javax.servlet.http.HttpServletRequest;

/**
 * 项目基础controller
 *
 * @author qingzhou
 *         2017-11-05 0:45
 */
public class MarketBaseController extends BaseController {

    public static final String MOBILE_USER_SESSION_KEY = "mobileUser";
    public static final String WEB_USER_SESSION_KEY = "webUser";

    protected WeiXinUser getMobileLoginUser(HttpServletRequest request){
        return (WeiXinUser)request.getSession().getAttribute(MOBILE_USER_SESSION_KEY);
    }

    protected void setMobileLoginUser(HttpServletRequest request, WeiXinUser user){
        request.getSession().setAttribute(MOBILE_USER_SESSION_KEY, user);
    }

    protected void checkMobileLogin(HttpServletRequest request) {
        WeiXinUser user = getMobileLoginUser(request);
        if(user == null){
            throw UserException.NOT_LOGIN;
        }
    }

    protected void setWebLoginUser(HttpServletRequest request, WeiXinPub user) {
        request.getSession().setAttribute(WEB_USER_SESSION_KEY, user);
    }

    protected WeiXinPub getWebLoginUser(HttpServletRequest request){
        return (WeiXinPub)request.getSession().getAttribute(WEB_USER_SESSION_KEY);
    }


}
