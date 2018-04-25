package cn.zhaoblog.zhaoxia.controller;

import cn.zhaoblog.zhaoxia.util.RequestUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 辅助API工具
 *
 * @author qingzhou
 *         2017-11-13 23:24
 */
@RestController
@RequestMapping("util")
public class UtilController extends BaseController {

    @RequestMapping(value = "ip", method = RequestMethod.GET)
    @ResponseBody
    public String ip(HttpServletRequest request) {
        return RequestUtil.getIpAddress(request);
    }
}
