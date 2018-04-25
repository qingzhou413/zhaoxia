package cn.zhaoblog.zhaoxia.exception;

import com.joysuch.core.exception.BizException;

/**
 * 订单异常
 *
 * @author qingzhou
 *         2017-11-08 23:50
 */
public class OrderException extends BizException {
    public OrderException(int code, String msg) {
        super(code, msg, new Object[]{});
    }
    public OrderException(int code, String msgFormat, Object... args) {
        super(code, msgFormat, args);
    }

    public static final OrderException WEI_XIN_PRE_ORDER_ERROR = new OrderException(BaseErrorCode.WEIXIN_ORDER_BASE_CODE + 1, "微信预下单失败");
}
