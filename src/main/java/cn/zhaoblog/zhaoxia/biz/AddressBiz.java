package cn.zhaoblog.zhaoxia.biz;/**
 * Created by 16204 on 2017/11/5.
 */

import cn.zhaoblog.zhaoxia.entity.Address;
import cn.zhaoblog.zhaoxia.entity.WeiXinUser;
import cn.zhaoblog.zhaoxia.mapper.AddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 地址业务类
 *
 * @author qingzhou
 *         2017-11-05 15:59
 */
@Service
public class AddressBiz {

    private AddressMapper mapper;
    @Autowired
    public AddressBiz(AddressMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 获取默认地址
     * @param loginUser
     * @return
     */
    public Address defaultAddr(WeiXinUser loginUser) {
        return mapper.selectDefaultAddress(loginUser);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void save(Address newAddr) {
        mapper.insert(newAddr);
    }

    public List<Address> findUserAddress(WeiXinUser loginUser) {
        return mapper.selectByUser(loginUser);
    }

    public Address findAddress(WeiXinUser loginUser, Long id) {
        return mapper.selectByUserAndId(loginUser.getAppid(), loginUser.getId(), id);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void deleteByUserIdAndId(Long userId, Long id) {
        mapper.deleteByUserIdAndId(userId, id);
    }
}
