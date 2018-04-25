package cn.zhaoblog.zhaoxia.entity;

import java.io.Serializable;

/**
 * 公众号信息
 *
 * @author qingzhou
 *         2017-10-29 21:55
 */
public class WeiXinPub implements Serializable {
    private static final long serialVersionUID = 7686339362478462063L;

    private long id;
    private String appid;
    private String secret;
    private String name;
    private String headimgurl;
    private String realName;
    private String certPath;
    private String certPass;
    private String mchId;
    private String mchKey;
    private String loginName;
    private String loginPass;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    public String getCertPass() {
        return certPass;
    }

    public void setCertPass(String certPass) {
        this.certPass = certPass;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getMchKey() {
        return mchKey;
    }

    public void setMchKey(String mchKey) {
        this.mchKey = mchKey;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginPass() {
        return loginPass;
    }

    public void setLoginPass(String loginPass) {
        this.loginPass = loginPass;
    }

    @Override
    public String toString() {
        return "WeiXinPub{" +
                "id='" + id + '\'' +
                "appid='" + appid + '\'' +
                ", secret='" + secret + '\'' +
                ", name='" + name + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", realName='" + realName + '\'' +
                ", certPath='" + certPath + '\'' +
                ", certPass='" + certPass + '\'' +
                ", mchId='" + mchId + '\'' +
                ", mchKey='" + mchKey + '\'' +
                ", loginName='" + loginName + '\'' +
                ", loginPass='" + loginPass + '\'' +
                '}';
    }
}
