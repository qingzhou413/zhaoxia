package cn.zhaoblog.zhaoxia.exception;/**
 * Created by 16204 on 2017/11/5.
 */

import com.joysuch.core.exception.BizException;

/**
 * 用户账户异常
 *
 * @author qingzhou
 *         2017-11-05 0:49
 */
public class UserException extends BizException {
    public UserException(int code, String msg){
        super(code, msg, new Object[]{});
    }
    public UserException(int code, String msgFormat, Object... args) {
        super(code, msgFormat, args);
    }

    public static UserException NOT_LOGIN = new UserException(-1, "未登录");
}
