package cn.zhaoblog.zhaoxia.entity;/**
 * Created by 16204 on 2017/10/29.
 */

import com.joysuch.core.util.JsonUtil;

import java.io.Serializable;
import java.util.List;

/**
 * 微信用户信息
 *
 * @author qingzhou
 *         2017-10-29 21:56
 */
public class WeiXinUser implements Serializable {
    private static final long serialVersionUID = 546673357327571018L;

    private long id;
    private String appid;
    private String openid;
    private String nickname;
    /**
     * 性别。1男2女0未知
     */
    private String sex;
    private String province;
    private String city;
    private String country;
    private String headimgurl;
    private List<String> privilege;
    private String privilegeStr;

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

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public List<String> getPrivilege() {
        return privilege;
    }

    public void setPrivilege(List<String> privilege) {
        this.privilege = privilege;
        if(privilege != null){
            this.privilegeStr = JsonUtil.toJson(privilege);
        }
    }

    public String getPrivilegeStr() {
        return privilegeStr;
    }

    public void setPrivilegeStr(String privilegeStr) {
        this.privilegeStr = privilegeStr;
    }

    @Override
    public String toString() {
        return "WeiXinUser{" +
                "appid='" + appid + '\'' +
                ", openid='" + openid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", privilegeStr=" + privilegeStr +
                '}';
    }
}
