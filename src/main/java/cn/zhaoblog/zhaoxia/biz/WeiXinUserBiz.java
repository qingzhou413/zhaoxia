package cn.zhaoblog.zhaoxia.biz;/**
 * Created by 16204 on 2017/10/29.
 */

import cn.zhaoblog.zhaoxia.entity.WeiXinUser;
import cn.zhaoblog.zhaoxia.mapper.WeiXinUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务
 *
 * @author qingzhou
 *         2017-10-29 21:48
 */
@Service
public class WeiXinUserBiz {

    @Autowired
    private WeiXinUserMapper mapper;

    @Transactional(rollbackFor = {Exception.class})
    public void checkUser(WeiXinUser user) {
        synchronized (user.getAppid().intern()) {
            WeiXinUser dbUser = mapper.selectByAppidAndOpenid(user.getAppid(), user.getOpenid());
            if (dbUser == null) {
                mapper.insert(user);
            }else{
                if(!user.toString().equals(dbUser.toString())){
                    dbUser.setNickname(user.getNickname());
                    dbUser.setSex(user.getSex());
                    dbUser.setProvince(user.getProvince());
                    dbUser.setCity(user.getCity());
                    dbUser.setCountry(user.getCountry());
                    dbUser.setHeadimgurl(user.getHeadimgurl());
                    dbUser.setPrivilege(user.getPrivilege());
                    mapper.update(dbUser);
                }
            }
        }
    }
}
