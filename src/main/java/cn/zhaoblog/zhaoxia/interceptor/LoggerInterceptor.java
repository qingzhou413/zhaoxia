package cn.zhaoblog.zhaoxia.interceptor;

import cn.zhaoblog.zhaoxia.controller.MarketBaseController;
import cn.zhaoblog.zhaoxia.entity.WeiXinPub;
import cn.zhaoblog.zhaoxia.entity.WeiXinUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

public class LoggerInterceptor implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);
    private static final String format = "yyyy-MM-dd HH:mm:ss";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        StringBuilder sb = new StringBuilder("\nLogger action report -------- ").append(new SimpleDateFormat(format).format(new Date())).append(" ------------------------------\n");
        WeiXinUser mobileUser = (WeiXinUser) request.getSession().getAttribute(MarketBaseController.MOBILE_USER_SESSION_KEY);
        if (mobileUser != null) {
            sb.append("User: ").append(mobileUser.getNickname() + ", appid: " + mobileUser.getAppid() + " openid: " + mobileUser.getOpenid());
        } else {
            WeiXinPub webUser = (WeiXinPub) request.getSession().getAttribute(MarketBaseController.WEB_USER_SESSION_KEY);
            if (webUser != null) {
                sb.append("User: ").append(webUser.getName() + ", appid: " + webUser.getAppid());
            } else {
                sb.append("User: null");
            }
        }

        sb.append("\n");
        sb.append("UrI: ").append(uri);
        sb.append("\n");
        // print all parameters
        Enumeration<String> e = request.getParameterNames();
        if (e.hasMoreElements()) {
            sb.append("Parameter   : ");
            while (e.hasMoreElements()) {
                String name = e.nextElement();
                String[] values = request.getParameterValues(name);
                if (values.length == 1) {
                    sb.append(name).append("=").append(values[0]);
                } else {
                    sb.append(name).append("[]={");
                    for (int i = 0; i < values.length; i++) {
                        if (i > 0) {
                            sb.append(",");
                        }
                        sb.append(values[i]);
                    }
                    sb.append("}");
                }
                sb.append("  ");
            }
            sb.append("\n");
        }
        sb.append("--------------------------------------------------------------------------------\n");
        logger.info(sb.toString());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
