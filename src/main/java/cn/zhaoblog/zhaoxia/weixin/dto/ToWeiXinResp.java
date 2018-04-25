package cn.zhaoblog.zhaoxia.weixin.dto;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * 回复微信回调接口的值
 *
 * @author qingzhou
 *         2017-11-09 0:31
 */
@XmlRootElement(name = "xml")
public class ToWeiXinResp implements Serializable {

    private static final long serialVersionUID = 7164192802309003373L;

    private String return_code;
    private String return_msg;

    public ToWeiXinResp() {
    }

    public ToWeiXinResp(String return_code, String return_msg) {
        this.return_code = return_code;
        this.return_msg = return_msg;
    }

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    @Override
    public String toString() {
        return "ToWeiXinResp{" +
                "return_code='" + return_code + '\'' +
                ", return_msg='" + return_msg + '\'' +
                '}';
    }
}
