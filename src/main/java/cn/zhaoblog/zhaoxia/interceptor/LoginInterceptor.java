package cn.zhaoblog.zhaoxia.interceptor;

import cn.zhaoblog.util.JsonUtil;
import cn.zhaoblog.util.bean.ReturnMsg;
import cn.zhaoblog.zhaoxia.controller.MarketBaseController;
import cn.zhaoblog.zhaoxia.entity.WeiXinPub;
import cn.zhaoblog.zhaoxia.entity.WeiXinUser;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 在DispatcherServlet完全处理完请求后被调用(可以在该方法进行一些资源的清理操作)
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handle, Exception arg3)
            throws Exception {

    }

    /**
     * 在业务处理完成请求后，在DispatcherServlet向客户端返回响应前被调用
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handle, ModelAndView arg3)
            throws Exception {

    }

    /**
     * 业务处理器处理之前被调用，被拦截返回false，反之能正常到Controller层
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handle) throws Exception {
        try {
            HttpSession session = request.getSession();
            if (session != null) {
                WeiXinUser mobileUser = (WeiXinUser) session.getAttribute(MarketBaseController.MOBILE_USER_SESSION_KEY);
                if (mobileUser != null) {
                    return true;
                }

                WeiXinPub webUser = (WeiXinPub) session.getAttribute(MarketBaseController.WEB_USER_SESSION_KEY);
                if (webUser != null) {
                    return true;
                }
            }

            // 最后的情况就是进入登录页面
            ReturnMsg msg = new ReturnMsg();
            msg.setErrorCode(-1);
            response.getOutputStream().write(JsonUtil.toJson(msg).getBytes());

        } catch (Exception e) {
            e.printStackTrace();
            ReturnMsg msg = new ReturnMsg();
            msg.addErrorMsg("操作失败");
            response.getOutputStream().write(JsonUtil.toJson(msg).getBytes());
        }

        return false;
    }

}
