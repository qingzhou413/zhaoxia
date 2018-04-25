package cn.zhaoblog.zhaoxia.weixin;/**
 * Created by 16204 on 2017/10/29.
 */

import cn.zhaoblog.util.HttpClientUtil;
import cn.zhaoblog.util.JsonUtil;
import cn.zhaoblog.zhaoxia.biz.WeiXinPubBiz;
import cn.zhaoblog.zhaoxia.entity.WeiXinPub;
import cn.zhaoblog.zhaoxia.entity.WeiXinUser;
import cn.zhaoblog.zhaoxia.exception.WeiXinException;
import cn.zhaoblog.zhaoxia.weixin.dto.QueryOrderDto;
import cn.zhaoblog.zhaoxia.weixin.dto.QueryOrderResultDTO;
import cn.zhaoblog.zhaoxia.weixin.dto.UnifiedOrderDto;
import cn.zhaoblog.zhaoxia.weixin.dto.UnifiedOrderResultDTO;
import cn.zhaoblog.zhaoxia.weixin.dto.WxAccessToken;
import cn.zhaoblog.zhaoxia.weixin.dto.WxJsTicket;
import cn.zhaoblog.zhaoxia.weixin.util.WeiXinHttpUtil;
import cn.zhaoblog.zhaoxia.weixin.util.XmlUtil;
import com.joysuch.core.util.DateUtil;
import com.joysuch.core.util.Md5Util;
import com.joysuch.core.util.StringUtil;
import com.joysuch.core.util.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信服务
 *
 * @author qingzhou
 *         2017-10-29 21:51
 */
@Component
public class WeiXinService {

    private static final Logger logger = LoggerFactory.getLogger(WeiXinService.class);

    private static final int API_LIMIT_TIME = 3;
    public static final String WEI_XIN_UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    public static final String WEI_XIN_QUERY_ORDER_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
    public static final String WEI_XIN_UNIFIED_ORDER_NOTIFY_URL = "http://market.515huitao.com/wxcallback/pay";
    public static final String WEI_XIN_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
    public static final String WEI_XIN_JS_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";


    private WeiXinPubBiz biz;
    private Map<String, WeiXinPub> pubs = new HashMap<>();

    @Autowired
    public WeiXinService(WeiXinPubBiz biz) {
        this.biz = biz;
        List<WeiXinPub> all = biz.findAll();
        if (all != null) {
            for (WeiXinPub p : all) {
                pubs.put(p.getAppid(), p);
            }
        }
    }

    public String getMchKey(String appid) {
        WeiXinPub pub = pubs.get(appid);
        if (pub == null) {
            return null;
        }
        return pub.getMchKey();
    }

    private String getSecret(String appid) {
        WeiXinPub weiXinPub = pubs.get(appid);
        if (weiXinPub == null) {
            throw WeiXinException.APPID_PUB_NOT_EXISTS;
        }
        return weiXinPub.getSecret();
    }


    /**
     * 获取AccessToken接口
     *
     * @param appid
     * @param secret
     * @return
     */
    public String getAccessToken(String appid, String secret) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("grant_type", "client_credential");
        params.put("appid", appid);
        params.put("secret", secret);

        String result = null;
        int i = 0;
        while (result == null && i < API_LIMIT_TIME) {
            result = HttpClientUtil.createInstance().doGetWithParams(WEI_XIN_ACCESS_TOKEN_URL, params);
            i++;
        }
        logger.info("result >>>>>> getAccessToken >>>>>>> " + result);
        if (StringUtil.notEmpty(result)) {
            if (result.contains("access_token")) {
                WxAccessToken token = JsonUtil.fromJson(result, WxAccessToken.class);
                if (token != null) {
                    return token.getAccess_token();
                }
            }
        }
        return null;
    }


    /**
     * 获取JS-TICKET
     *
     * @param accessToken
     * @return
     */
    public String getJsTicket(String accessToken) {
        if (!StringUtil.isEmpty(accessToken)) {
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("type", "jsapi");
            params.put("access_token", accessToken);
            String result = null;
            int i = 0;
            while (result == null && i < API_LIMIT_TIME) {
                result = HttpClientUtil.createInstance().doGetWithParams(WEI_XIN_JS_TICKET_URL, params);
                i++;
            }
            if (result.indexOf("ticket") >= 0) {
                WxJsTicket ticket = JsonUtil.fromJson(result, WxJsTicket.class);
                if (ticket != null) {
                    return ticket.getTicket();
                }
            }
        }
        return null;
    }

    /**
     * 查询订单状态
     *
     * @param query
     * @return
     */
    public QueryOrderResultDTO queryOrder(QueryOrderDto query) {
        String appid = query.getAppid();
        WeiXinPub pub = pubs.get(appid);
        if (pub == null) {
            return null;
        }
        String mchId = pub.getMchId();
        query.setMch_id(mchId);
        String nonceStr = UuidUtil.getUuid();
        query.setNonce_str(nonceStr);
        String mchKey = pub.getMchKey();
        String certPath = pub.getCertPath();
        String certPass = pub.getCertPass();

        String result = null;
        int i = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("appid=" + query.getAppid());
        sb.append("&mch_id=" + query.getMch_id());
        sb.append("&nonce_str=" + query.getNonce_str());
        sb.append("&out_trade_no=" + query.getOut_trade_no());
        sb.append("&key=" + mchKey);
        logger.info("查询订单状态签名字符串: " + sb.toString());
        query.setSign(Md5Util.md5(sb.toString()).toUpperCase());
        String xml = new XmlUtil(QueryOrderDto.class).toXml(query, "UTF-8");
        while (result == null && i < API_LIMIT_TIME) {
            result = WeiXinHttpUtil.postPay(WEI_XIN_QUERY_ORDER_URL, xml, certPath, certPass);
            i++;
        }
        logger.info(">>>>>>>>>>queryOrder result[" + result + "]<<<<<<<<<<");
        if (result != null && result.contains("SUCCESS")) {
            return new XmlUtil(QueryOrderResultDTO.class).fromXml(result);
        }
        return null;

    }

    /**
     * 统一下单接口
     *
     * @param order
     * @return
     */
    public UnifiedOrderResultDTO unifiedOrder(UnifiedOrderDto order) {
        String appid = order.getAppid();
        WeiXinPub pub = pubs.get(appid);
        if (pub == null) {
            return null;
        }

        order.setTrade_type("JSAPI");
        String tradeNum = getTradeNum();
        order.setOut_trade_no(tradeNum);
        order.setNotify_url(WEI_XIN_UNIFIED_ORDER_NOTIFY_URL);
        String nonceStr = UuidUtil.getUuid();
        order.setNonce_str(nonceStr);
        String mchId = pub.getMchId();
        order.setMch_id(mchId);
        String mchKey = pub.getMchKey();
        String certPath = pub.getCertPath();
        String certPass = pub.getCertPass();

        String result = null;
        int i = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("appid=" + order.getAppid());
        sb.append("&attach=" + order.getAttach());
        sb.append("&body=" + order.getBody());
        sb.append("&mch_id=" + order.getMch_id());
        sb.append("&nonce_str=" + order.getNonce_str());
        sb.append("&notify_url=" + order.getNotify_url());
        sb.append("&openid=" + order.getOpenid());
        sb.append("&out_trade_no=" + order.getOut_trade_no());
        sb.append("&spbill_create_ip=" + order.getSpbill_create_ip());
        sb.append("&total_fee=" + order.getTotal_fee());
        sb.append("&trade_type=" + order.getTrade_type());
        sb.append("&key=" + mchKey);
        logger.info("预下单签名字符串: " + sb.toString());
        order.setSign(Md5Util.md5(sb.toString()).toUpperCase());
        String xml = new XmlUtil(UnifiedOrderDto.class).toXml(order, "UTF-8");
        while (result == null && i < API_LIMIT_TIME) {
            result = WeiXinHttpUtil.postPay(WEI_XIN_UNIFIED_ORDER_URL, xml, certPath, certPass);
            i++;
        }
        logger.info(">>>>>>>>>>unifiedOrder result[" + result + "]<<<<<<<<<<");
        if (result != null && result.contains("SUCCESS")) {
            return new XmlUtil(UnifiedOrderResultDTO.class).fromXml(result);
        }
        return null;
    }

    private String getTradeNum() {
        return DateUtil.formatDate(new Date(), "yyyyMMddHHmmss") + (System.nanoTime() % 1000000);
    }

    /**
     * 获取微信授权用户信息
     *
     * @param appid
     * @param code
     * @return
     */
    public WeiXinUser info(String appid, String code) {
        String secret = getSecret(appid);
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret=" + secret + "&code=" + code + "&grant_type=authorization_code";
        String resp = HttpClientUtil.createInstance().doGet(url);
        if (StringUtil.notEmpty(resp) && resp.contains("access_token")) {
            WeiXinUserAccessToken token = JsonUtil.fromJson(resp, WeiXinUserAccessToken.class);
            WeiXinUser user = userInfo(token);
            if (user != null) {
                user.setAppid(appid);
                return user;
            }
        }
        return null;
    }

    private WeiXinUser userInfo(WeiXinUserAccessToken token) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + token.getAccess_token() + "&openid=" + token.getOpenid() + "&lang=zh_CN";
        String resp = HttpClientUtil.createInstance().doGet(url);
        if (StringUtil.notEmpty(resp) && resp.contains("openid")) {
            WeiXinUser user = JsonUtil.fromJson(resp, WeiXinUser.class);
            return user;
        }
        return null;
    }

}
