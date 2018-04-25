package cn.zhaoblog.zhaoxia.mapper;/**
 * Created by 16204 on 2017/11/5.
 */

import cn.zhaoblog.zhaoxia.entity.Address;
import cn.zhaoblog.zhaoxia.entity.WeiXinUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 地址DAO
 *
 * @author qingzhou
 *         2017-11-05 15:59
 */
public interface AddressMapper {

    /**
     * 查询用户默认地址
     * @param loginUser
     * @return
     */
    Address selectDefaultAddress(WeiXinUser loginUser);

    /**
     * 添加地址
     * @param newAddr
     */
    void insert(Address newAddr);

    /**
     * 查找用户地址列表
     * @param loginUser
     * @return
     */
    List<Address> selectByUser(WeiXinUser loginUser);

    /**
     * 查询某个地址
     * @param appid
     * @param userId
     * @param id
     * @return
     */
    Address selectByUserAndId(@Param("appid") String appid, @Param("userId") long userId, @Param("id") Long id);

    /**
     * 删除地址
     * @param userId
     * @param id
     */
    void deleteByUserIdAndId(@Param("userId") Long userId, @Param("id") Long id);
}
