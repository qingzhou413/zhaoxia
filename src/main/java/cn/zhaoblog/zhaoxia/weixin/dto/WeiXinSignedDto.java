package cn.zhaoblog.zhaoxia.weixin.dto;/**
 * Created by 16204 on 2017/11/7.
 */

import java.io.Serializable;

/**
 * 带签名接口公共父类
 *
 * @author qingzhou
 *         2017-11-07 23:42
 */
public abstract class WeiXinSignedDto implements Serializable{
    private String sign;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "WeiXinSignedDto{" +
                "sign='" + sign + '\'' +
                '}';
    }
}
