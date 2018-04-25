package cn.zhaoblog.zhaoxia.weixin.dto;

import java.io.Serializable;

/**
 * 微信访问Token
 *
 * @author qingzhou
 *         2017-11-09 1:10
 */
public class WxAccessToken implements Serializable{
    private static final long serialVersionUID = 8636692925356980133L;

    private int errcode;
    private String errmsg;
    private String access_token;
    private int expires_in;
    public WxAccessToken() {
    }
    public String getAccess_token() {
        return access_token;
    }
    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
    public int getExpires_in() {
        return expires_in;
    }
    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }
    public String getErrmsg() {
        return errmsg;
    }
    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
    public int getErrcode() {
        return errcode;
    }
    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }
    @Override
    public String toString() {
        return "AccessToken [errcode=" + errcode + ", errmsg=" + errmsg + ", access_token=" + access_token + ", expires_in=" + expires_in + "]";
    }
}
