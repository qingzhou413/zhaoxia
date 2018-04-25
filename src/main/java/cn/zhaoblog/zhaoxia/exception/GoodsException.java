package cn.zhaoblog.zhaoxia.exception;

import com.joysuch.core.exception.BizException;

/**
 * Created by 16204 on 2017/10/10.
 */
public class GoodsException extends BizException {
    public GoodsException(int code, String msg){
        super(code, msg, new Object[0]);
    }
    public GoodsException(int code, String msgFormat, Object... args) {
        super(code, msgFormat, args);
    }

    public static final GoodsException SPEC_DET_CANT_BE_EMPTY = new GoodsException(BaseErrorCode.GOODS_BASE_CODE + 1, "商品规格必须填写");
    public static final GoodsException GOODS_NOT_FOUND = new GoodsException(BaseErrorCode.GOODS_BASE_CODE + 2, "没找到该商品");
    public static final GoodsException LEFT_COUNT_LIMIT = new GoodsException(BaseErrorCode.GOODS_BASE_CODE + 3, "商品库存不足");

}
