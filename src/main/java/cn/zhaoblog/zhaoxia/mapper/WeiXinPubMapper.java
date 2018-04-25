package cn.zhaoblog.zhaoxia.mapper;/**
 * Created by 16204 on 2017/10/29.
 */

import cn.zhaoblog.zhaoxia.entity.WeiXinPub;

import java.util.List;

/**
 * 公众号DAO
 *
 * @author qingzhou
 *         2017-10-29 22:02
 */
public interface WeiXinPubMapper {

    /**
     * 查询所有公众号信息
     * @return
     */
    List<WeiXinPub> selectAll();

    /**
     * 根据登录名查询
     * @param username
     * @return
     */
    WeiXinPub selectByLoginName(String username);
}
