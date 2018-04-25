package cn.zhaoblog.zhaoxia.exception;

import com.joysuch.core.exception.BizException;

/**
 * Created by 16204 on 2017/10/4.
 */
public class CategoryException extends BizException {

    public CategoryException(int code, String msg) {
        this(code, msg, new Object[0]);
    }

    public CategoryException(int code, String msgFormat, Object... args) {
        super(code, msgFormat, args);
    }

    public static final CategoryException CATEGORY_NOT_EXISTS = new CategoryException(BaseErrorCode.CATEGORY_BASE_CODE + 1, "类目不存在");
    public static final CategoryException CATEGORY_ALREADY_EXISTS = new CategoryException(BaseErrorCode.CATEGORY_BASE_CODE + 2, "该类目已经存在");
    public static final CategoryException STILL_HAVE_SUBS_CANT_DELETE = new CategoryException(BaseErrorCode.CATEGORY_BASE_CODE + 3, "该类目还有子类目，不能删除");

}
