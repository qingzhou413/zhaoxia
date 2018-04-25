package cn.zhaoblog.zhaoxia.weixin.dto;

import java.io.Serializable;

/**
 * @author qingzhou
 */
public class WxJsTicket implements Serializable{

    private static final long serialVersionUID = -1208440639840252044L;

    private int errcode;
	private String errmsg;
	private String ticket;
	private int expires_in;
	public WxJsTicket() {
	}
	public int getErrcode() {
		return errcode;
	}
	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public int getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}

    @Override
    public String toString() {
        return "WxJsTicket{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", ticket='" + ticket + '\'' +
                ", expires_in=" + expires_in +
                '}';
    }
}
