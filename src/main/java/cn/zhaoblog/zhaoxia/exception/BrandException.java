package cn.zhaoblog.zhaoxia.exception;/**
 * Created by 16204 on 2017/10/21.
 */

import com.joysuch.core.exception.BizException;

/**
 * 品牌异常
 *
 * @author qingzhou
 *         2017-10-21 23:13
 */
public class BrandException extends BizException {
    public BrandException(int code, String msg){
        super(code, msg, new Object[]{});
    }
    public BrandException(int code, String msgFormat, Object... args) {
        super(code, msgFormat, args);
    }

    public static final BrandException BRAND_NAME_EXISTS_ERROR = new BrandException(BaseErrorCode.BRAND_BASE_CODE + 1, "品牌名称已经存在");
    public static final BrandException BRAND_ID_NOT_EXISTS_ERROR = new BrandException(BaseErrorCode.GOODS_BASE_CODE + 2, "指定的品牌不存在，请刷新后重试");

}
