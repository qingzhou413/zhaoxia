package cn.zhaoblog.zhaoxia.exception;/**
 * Created by 16204 on 2017/10/29.
 */

import cn.zhaoblog.zhaoxia.entity.WeiXinPub;
import com.joysuch.core.exception.BizException;

/**
 * 微信异常类
 *
 * @author qingzhou
 *         2017-10-29 22:12
 */
public class WeiXinException extends BizException {

    public WeiXinException(int code, String msg){
        super(code, msg, new Object[]{});
    }
    public WeiXinException(int code, String msgFormat, Object... args) {
        super(code, msgFormat, args);
    }

    public static final WeiXinException APPID_PUB_NOT_EXISTS = new WeiXinException(BaseErrorCode.WEIXIN_BASE_CODE + 1, "未查找到公众号");

    public static final WeiXinException USER_INFO_ERROR = new WeiXinException(BaseErrorCode.WEIXIN_BASE_CODE + 2, "查询用户信息失败，请退出页面重试");

}
