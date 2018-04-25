package cn.zhaoblog.zhaoxia.entity;/**
 * Created by 16204 on 2017/11/8.
 */

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单
 *
 * @author qingzhou
 *         2017-11-08 0:40
 */
public class Order implements Serializable {
    private static final long serialVersionUID = -2766554203133106187L;

    private Long id;
    private String appid;
    private String mchId;
    private Long userId;
    private String openId;
    private String address;
    private String nickname;
    private String headimgurl;
    private String tradeNum;
    private String tradeType;
    private String details;
    private BigDecimal totalMoney;
    private Date createTime;
    private String bankType;
    private Date payTime;
    private String transactionId;
    private Integer status;
    private String remark;
    private String retPrepayId;
    private Integer deliverStatus;

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

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getTradeNum() {
        return tradeNum;
    }

    public void setTradeNum(String tradeNum) {
        this.tradeNum = tradeNum;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRetPrepayId() {
        return retPrepayId;
    }

    public void setRetPrepayId(String retPrepayId) {
        this.retPrepayId = retPrepayId;
    }

    public Integer getDeliverStatus() {
        return deliverStatus;
    }

    public void setDeliverStatus(Integer deliverStatus) {
        this.deliverStatus = deliverStatus;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", appid='" + appid + '\'' +
                ", mchId='" + mchId + '\'' +
                ", userId=" + userId +
                ", openId='" + openId + '\'' +
                ", address='" + address + '\'' +
                ", nickname='" + nickname + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", tradeNum='" + tradeNum + '\'' +
                ", tradeType='" + tradeType + '\'' +
                ", details='" + details + '\'' +
                ", totalMoney=" + totalMoney +
                ", createTime=" + createTime +
                ", bankType='" + bankType + '\'' +
                ", payTime=" + payTime +
                ", transactionId='" + transactionId + '\'' +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                ", retPrepayId='" + retPrepayId + '\'' +
                ", deliverStatus='" + deliverStatus + '\'' +
                '}';
    }
}
