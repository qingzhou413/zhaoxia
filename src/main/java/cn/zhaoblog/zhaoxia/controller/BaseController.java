package cn.zhaoblog.zhaoxia.controller;

import cn.zhaoblog.zhaoxia.common.Contants;
import com.joysuch.core.exception.BizException;
import com.joysuch.core.pager.PageParam;
import com.joysuch.core.util.DateUtil;
import com.joysuch.core.util.StringUtil;
import com.joysuch.core.web.ReturnMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected static final String REDIRECT = "redirect:";
    protected static final String LANGUAGE_PROP = "mess";

    @ExceptionHandler
    @ResponseBody
    public ReturnMsg exp(HttpServletRequest request, HttpServletResponse response, Exception e) {
        setAllowAllAccessControlHeader(response);

        ReturnMsg msg = new ReturnMsg();
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Content-Type", "application/json;charset=UTF-8");

        if (e instanceof BizException) {
            BizException be = (BizException) e;
            msg.addErrorMsg(be.getMsg());
            msg.setErrorCode(be.getCode());
        } else if (e instanceof HttpMessageNotReadableException) {
            msg.addErrorMsg("参数错误");
        } else {
            msg.addErrorMsg("服务器繁忙");
            logger.error("catch exception: ", e);
        }

        return msg;
    }

    protected void setAllowAllAccessControlHeader(HttpServletResponse response) {
        if (response != null) {
            response.setHeader("Access-Control-Allow-Headers", "*");
            response.setHeader("Access-Control-Allow-Methods", "*");
            String origin = response.getHeader("Access-Control-Allow-Origin");
            if (StringUtil.isEmpty(origin)) {
                logger.info("set Access-Control-Allow-Origin headers...");
                response.setHeader("Access-Control-Allow-Origin", "*");
            }
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Content-Type", "text/plain;charset=UTF-8");
        }
    }

    protected PageParam getPageParam(HttpServletRequest request) {
        return new PageParam(getPageNum(request), getNumPerPage(request));
    }

    private int getNumPerPage(HttpServletRequest request) {
        int numPerPage = 10;
        String numPerPageStr = getValue(request, "numPerPage");
        if (StringUtil.notEmpty(numPerPageStr)) {
            numPerPage = Integer.parseInt(numPerPageStr);
        }
        return numPerPage;
    }

    private int getPageNum(HttpServletRequest request) {
        int pageNum = 1;
        String pageNumStr = getValue(request, "pageNum");
        if (StringUtil.notEmpty(pageNumStr)) {
            pageNum = Integer.parseInt(pageNumStr);
        }
        return pageNum;
    }

    public Map<String, String[]> getParaMap(HttpServletRequest request) {
        return request.getParameterMap();
    }

    private String getValue(HttpServletRequest request, String paramName) {
        return request.getParameter(paramName);
    }

    protected String getPara(HttpServletRequest request, String paramName) {
        return getValue(request, paramName);
    }

    protected Integer getParaToInt(HttpServletRequest request, String paramName) {
        String value = getValue(request, paramName);
        if (StringUtil.notEmpty(value)) {
            return Integer.parseInt(value);
        }
        return null;
    }

    protected Long getParaToLong(HttpServletRequest request, String paramName) {
        String value = getValue(request, paramName);
        if (StringUtil.notEmpty(value)) {
            return Long.parseLong(value);
        }
        return null;
    }

    protected Float getParaToFloat(HttpServletRequest request, String paramName) {
        String value = getValue(request, paramName);
        if (StringUtil.notEmpty(value)) {
            return Float.parseFloat(value);
        }
        return null;
    }

    protected Double getParaToDouble(HttpServletRequest request, String paramName) {
        String value = getValue(request, paramName);
        if (StringUtil.notEmpty(value)) {
            return Double.parseDouble(value);
        }
        return null;
    }

    protected Boolean getParaToBoolean(HttpServletRequest request, String paramName) {
        String value = getValue(request, paramName);
        if (StringUtil.notEmpty(value)) {
            return Boolean.parseBoolean(value);
        }
        return null;
    }

    protected Date getParaToDate(HttpServletRequest request, String paramName) {
        String value = getValue(request, paramName);
        if (StringUtil.notEmpty(value)) {
            return DateUtil.parseDate(request.getParameter(paramName));
        }
        return null;
    }

    protected Date getParaToDatetime(HttpServletRequest request, String paramName) {
        String value = getValue(request, paramName);
        if (StringUtil.notEmpty(value)) {
            return DateUtil.parseDateTime(request.getParameter(paramName));
        }
        return null;
    }

    protected Date getParaToDate(HttpServletRequest request, String paramName, String pattern) {
        String value = getValue(request, paramName);
        if (StringUtil.notEmpty(value)) {
            return DateUtil.parseDate(request.getParameter(paramName), pattern);
        }
        return null;
    }

    protected String[] getParaValues(HttpServletRequest request, String paramName) {
        return request.getParameterValues(paramName);
    }

}
