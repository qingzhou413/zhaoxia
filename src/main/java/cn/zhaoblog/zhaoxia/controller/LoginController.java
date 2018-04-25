package cn.zhaoblog.zhaoxia.controller;

import cn.zhaoblog.util.bean.ReturnMsg;
import cn.zhaoblog.zhaoxia.biz.WeiXinPubBiz;
import cn.zhaoblog.zhaoxia.entity.WeiXinPub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController extends MarketBaseController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private WeiXinPubBiz weiXinPubBiz;

    @RequestMapping(value = "doLogin")
    public String login(HttpServletRequest request) {
        boolean hasError = true;

        String username = getPara(request, "username");
        String password = getPara(request, "password");
        logger.info("=== login. username:" + username + " ===");

        WeiXinPub pub = weiXinPubBiz.login(username, password);
        int errorCode = 0;
        if (pub != null) {
            // 成功登陆
            setWebLoginUser(request, pub);
            hasError = false;
        } else {
            // 用户名密码错误
            errorCode = 1;
        }
        if (hasError) {
            return "redirect:/login.html?login_error=" + errorCode;
        } else {
            return "redirect:/index.html";
        }
    }

    @RequestMapping(value = "pubInfo")
    @ResponseBody
    public ReturnMsg pubInfo(HttpServletRequest request) {
        ReturnMsg msg = new ReturnMsg();

        WeiXinPub user = getWebLoginUser(request);

        Map<String, Object> data = new HashMap<>();
        data.put("name", user.getName());

        msg.setData(data);
        msg.setErrorCode(0);
        return msg;
    }

    /**
     * 登出
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "doLogout")
    @ResponseBody
    public ReturnMsg logout(HttpServletRequest request) {
        ReturnMsg returnMsg = new ReturnMsg();
        WeiXinPub user = getWebLoginUser(request);
        if (user != null) {
            logger.info("=== logout. username:" + user.getName() + " ===");
            request.getSession().invalidate();
        }
        returnMsg.setErrorCode(0);
        return returnMsg;
    }

}
