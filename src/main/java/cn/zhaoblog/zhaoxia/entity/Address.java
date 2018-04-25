package cn.zhaoblog.zhaoxia.entity;/**
 * Created by 16204 on 2017/10/29.
 */

import com.joysuch.core.util.JsonUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 地址信息
 *
 * @author qingzhou
 *         2017-11-05 15:58
 */
public class Address implements Serializable {

    private static final long serialVersionUID = 2152638592480782465L;
    private Long id;
    private String appid;
    private Long userId;
    private String name;
    private String phone;
    private String area;
    private String address;
    private Date activeTime;

    public Address() {
    }

    public Address(String appid, Long userId, String name, String phone, String area, String address) {
        this.appid = appid;
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.area = area;
        this.address = address;
        this.activeTime = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Date activeTime) {
        this.activeTime = activeTime;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", appid='" + appid + '\'' +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", area='" + area + '\'' +
                ", address='" + address + '\'' +
                ", activeTime=" + activeTime +
                '}';
    }
}
