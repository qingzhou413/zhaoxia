package cn.zhaoblog.zhaoxia.controller;

import cn.zhaoblog.zhaoxia.entity.WeiXinUser;
import cn.zhaoblog.zhaoxia.weixin.WxTokenAndTicketRefreshTask;
import cn.zhaoblog.zhaoxia.weixin.util.SHA1;
import cn.zhaoblog.zhaoxia.weixin.util.WeiXinHttpUtil;
import com.joysuch.core.util.StringUtil;
import com.joysuch.core.util.UuidUtil;
import com.joysuch.core.web.ReturnMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 微信认证控制器
 *
 * @author qingzhou
 *         2017-11-09 1:25
 */
@RestController
@RequestMapping("wxauth")
public class WxAuthController extends MarketBaseController {

    private static final Logger logger = LoggerFactory.getLogger(WxAuthController.class);

    private WxTokenAndTicketRefreshTask task;
    @Autowired
    public WxAuthController(WxTokenAndTicketRefreshTask task) {
        this.task = task;
    }

    @RequestMapping(value = "sign", method = RequestMethod.POST)
    public ReturnMsg sign(HttpServletRequest request){
        ReturnMsg msg = new ReturnMsg();
        checkMobileLogin(request);

        WeiXinUser loginUser = getMobileLoginUser(request);
        String url = getPara(request, "url");
        logger.info("======url[" + url + "]======");
        Map<String, Object> map = new HashMap<>();
        String ticket = task.getTicket(loginUser.getAppid());
        logger.info("======ticket[" + ticket + "]======");
        if (StringUtil.notEmpty(ticket)) {
            long timestamp = System.currentTimeMillis() / 1000;
            logger.info("======timestamp[" + timestamp + "]======");
            String nonceStr = UuidUtil.getUuid();
            logger.info("======nonceStr[" + nonceStr + "]======");
            String signature = getSignature(loginUser.getAppid(), nonceStr, timestamp, url, ticket);
            logger.info("======signature[" + signature + "]======");
            if (StringUtil.notEmpty(signature)) {
                map.put("appid", loginUser.getAppid());
                map.put("timestamp", timestamp);
                map.put("nonceStr", nonceStr);
                map.put("signature", signature);
                List<String> jsApiList = new ArrayList<String>();
                jsApiList.add("chooseWXPay");
                map.put("jsApiList", jsApiList);
                msg.setErrorCode(0);
                msg.setData(map);
            } else {
                map.put("error", "get signature failed");
            }
        } else {
            map.put("error", "get signature failed");
        }

        return msg;
    }

    private String getSignature(String appid, String nonceStr, long timestamp, String url, String ticket) {
        Map<String, Object> params = new TreeMap<String, Object>();
        params.put("noncestr", nonceStr);
        params.put("jsapi_ticket", ticket);
        params.put("timestamp", timestamp);
        params.put("url", url);
        String paramsString = WeiXinHttpUtil.contactParams(params);
        if (StringUtil.notEmpty(paramsString)) {
            paramsString = paramsString.substring(1);
        }
        logger.info("======paramsString[" + paramsString + "]======");
        if (StringUtil.notEmpty(paramsString)) {
            return SHA1.encode(paramsString);
        }
        return null;
    }
}
