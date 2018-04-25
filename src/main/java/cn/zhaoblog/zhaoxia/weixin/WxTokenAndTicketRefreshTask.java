package cn.zhaoblog.zhaoxia.weixin;

import cn.zhaoblog.zhaoxia.biz.WeiXinPubBiz;
import cn.zhaoblog.zhaoxia.entity.WeiXinPub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公众号AccessToken和JS-TICKET刷新任务
 *
 * @author qingzhou
 *         2017-11-09 1:17
 */
@Component
public class WxTokenAndTicketRefreshTask {

    private static final Logger logger = LoggerFactory.getLogger(WxTokenAndTicketRefreshTask.class);

    private Map<String, String> tokenMap = new HashMap<>();
    private Map<String, String> ticketMap = new HashMap<>();

    private WeiXinService weiXinService;
    private WeiXinPubBiz weiXinPubBiz;
    @Autowired
    public WxTokenAndTicketRefreshTask(WeiXinService weiXinService, WeiXinPubBiz weiXinPubBiz) {
        this.weiXinService = weiXinService;
        this.weiXinPubBiz = weiXinPubBiz;
        refresh();
    }

    private void refresh() {
        List<WeiXinPub> all = weiXinPubBiz.findAll();
        if(all != null && all.size() > 0){
            for (WeiXinPub pub : all) {
                try {
                    String accessToken = weiXinService.getAccessToken(pub.getAppid(), pub.getSecret());
                    if(accessToken != null){
                        tokenMap.put(pub.getAppid(), accessToken);
                        String jsTicket = weiXinService.getJsTicket(accessToken);
                        if(jsTicket != null){
                            ticketMap.put(pub.getAppid(), jsTicket);
                        }
                    }
                } catch (Exception e) {
                    logger.error("刷新Token和Ticket失败:" + pub.toString(), e);
                }
            }
        }
    }

    /**
     * 20分钟自动刷新
     */
    @Scheduled(initialDelay = 20 * 60 * 1000, fixedDelay = 20 * 60 * 1000)
    public void autoRefresh(){
        try {
            refresh();
        } catch (Exception e) {
            logger.error("定时刷新Token和Ticket失败:", e);
        }
    }

    public String getTicket(String appid) {
        return ticketMap.get(appid);
    }
}
