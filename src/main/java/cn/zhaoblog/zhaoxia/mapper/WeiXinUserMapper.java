package cn.zhaoblog.zhaoxia.mapper;/**
 * Created by 16204 on 2017/10/29.
 */

import cn.zhaoblog.zhaoxia.entity.WeiXinUser;
import org.apache.ibatis.annotations.Param;

/**
 * 微信用户DAO
 *
 * @author qingzhou
 *         2017-10-29 22:33
 */
public interface WeiXinUserMapper {

    /**
     * 根据appid openid查询
     * @param appid
     * @param openid
     * @return
     */
    WeiXinUser selectByAppidAndOpenid(@Param("appid") String appid, @Param("openid") String openid);

    /**
     * 插入
     * @param user
     */
    void insert(WeiXinUser user);

    /**
     * 更新
     * @param dbUser
     */
    void update(WeiXinUser dbUser);
}
