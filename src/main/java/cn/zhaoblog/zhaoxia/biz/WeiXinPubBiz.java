package cn.zhaoblog.zhaoxia.biz;/**
 * Created by 16204 on 2017/10/29.
 */

import cn.zhaoblog.util.Md5Util;
import cn.zhaoblog.zhaoxia.entity.WeiXinPub;
import cn.zhaoblog.zhaoxia.mapper.WeiXinPubMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 公众号服务
 *
 * @author qingzhou
 *         2017-10-29 22:01
 */
@Service
public class WeiXinPubBiz {

    @Autowired
    private WeiXinPubMapper mapper;

    public List<WeiXinPub> findAll(){
        return mapper.selectAll();
    }

    public WeiXinPub login(String username, String password) {
        WeiXinPub dbUser = mapper.selectByLoginName(username);
        if (dbUser != null) {
            String thePass = Md5Util.md5(Md5Util.md5(password));
            if (dbUser.getLoginPass().equals(thePass)) {
                dbUser.setLoginPass(null);
                return dbUser;
            }
        }
        return null;
    }
}
