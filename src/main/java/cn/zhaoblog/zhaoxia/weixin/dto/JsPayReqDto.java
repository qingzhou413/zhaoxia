package cn.zhaoblog.zhaoxia.weixin.dto;

import com.joysuch.core.util.Md5Util;
import com.joysuch.core.util.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * JSAPI调用支付时的参数
 *
 * @author qingzhou
 *         2017-11-08 23:34
 */
public class JsPayReqDto implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(JsPayReqDto.class);
    private static final long serialVersionUID = -2211764263169959591L;

    private String appid;
    private long timestamp;
    private String nonceStr;
    private String prepayId;
    private String signType;
    private String paySign;

    public JsPayReqDto(String appid, String prepayId, String mchKey) {
        this.appid = appid;
        this.timestamp = System.currentTimeMillis() / 1000;
        this.nonceStr = UuidUtil.getUuid();
        this.prepayId = prepayId;
        this.signType = "MD5";
        StringBuilder sb = new StringBuilder();
        sb.append("appId=" + appid);
        sb.append("&nonceStr=" + this.nonceStr);
        sb.append("&package=" + "prepay_id=" + this.prepayId);
        sb.append("&signType=" + this.signType);
        sb.append("&timeStamp=" + this.timestamp);
        sb.append("&key=" + mchKey);
        logger.info("JSAPI支付获取签名字符串:" + sb.toString());
        String sign = Md5Util.md5(sb.toString()).toUpperCase();
        logger.info("JSAPI支付获取签名:" + sign);
        this.paySign = sign;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }

    @Override
    public String toString() {
        return "JsPayReqDto{" +
                "appid=" + appid +
                "timestamp=" + timestamp +
                ", nonceStr='" + nonceStr + '\'' +
                ", prepayId='" + prepayId + '\'' +
                ", signType='" + signType + '\'' +
                ", paySign='" + paySign + '\'' +
                '}';
    }
}
