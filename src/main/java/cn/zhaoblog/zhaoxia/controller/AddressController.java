package cn.zhaoblog.zhaoxia.controller;/**
 * Created by 16204 on 2017/11/5.
 */

import cn.zhaoblog.zhaoxia.biz.AddressBiz;
import cn.zhaoblog.zhaoxia.common.Contants;
import cn.zhaoblog.zhaoxia.entity.Address;
import cn.zhaoblog.zhaoxia.entity.WeiXinUser;
import com.joysuch.core.web.ReturnMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 地址管理
 *
 * @author qingzhou
 *         2017-11-05 16:00
 */
@RestController
@RequestMapping("address")
public class AddressController extends MarketBaseController {

    private AddressBiz biz;

    @Autowired
    public AddressController(AddressBiz biz) {
        this.biz = biz;
    }

    /**
     * 从订单页面跳转到地址管理页面时标记一下缓存，用于选择地址后跳回订单页面
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "orderToAddrFlag", method = RequestMethod.POST)
    public ReturnMsg orderToAddrFlag(HttpServletRequest request) {
        ReturnMsg msg = new ReturnMsg();
        checkMobileLogin(request);
        request.getSession().setAttribute(Contants.ORDER_TO_ADDRESS_FLAG_KEY, true);
        msg.setData("标记成功");
        msg.setErrorCode(0);
        return msg;
    }

    /**
     * 从我的->地址管理页面跳转到地址管理页面时
     * 清除标记缓存，用于选择地址后不要跳回订单页面
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "clearOrderToAddrFlag", method = RequestMethod.POST)
    public ReturnMsg clearOrderToAddrFlag(HttpServletRequest request) {
        ReturnMsg msg = new ReturnMsg();
        checkMobileLogin(request);
        request.getSession().removeAttribute(Contants.ORDER_TO_ADDRESS_FLAG_KEY);
        msg.setData("清除标记成功");
        msg.setErrorCode(0);
        return msg;
    }

    /**
     * 判断是否有标记
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "isOrderToAddr", method = RequestMethod.GET)
    public ReturnMsg isOrderToAddr(HttpServletRequest request) {
        ReturnMsg msg = new ReturnMsg();
        checkMobileLogin(request);
        Boolean flag = (Boolean) request.getSession().getAttribute(Contants.ORDER_TO_ADDRESS_FLAG_KEY);
        msg.setData(flag == null ? false : flag.booleanValue());
        msg.setErrorCode(0);
        return msg;
    }

    /**
     * 前端在地址列表里选中了一个地址后存储到session里
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "saveAddrCache", method = RequestMethod.POST)
    public ReturnMsg saveAddrCache(HttpServletRequest request) {
        ReturnMsg msg = new ReturnMsg();
        checkMobileLogin(request);
        WeiXinUser loginUser = getMobileLoginUser(request);
        Long id = getParaToLong(request, "id");
        Address addr = biz.findAddress(loginUser, id);
        if (addr != null) {
            request.getSession().setAttribute(Contants.ADDRESS_CACHE_KEY, addr);
        } else {
            msg.addErrorMsg("所选地址不存在");
            return msg;
        }
        msg.setData("保存缓存成功");
        msg.setErrorCode(0);
        return msg;
    }

    @RequestMapping(value = "load", method = RequestMethod.GET)
    public ReturnMsg load(HttpServletRequest request) {
        ReturnMsg msg = new ReturnMsg();
        checkMobileLogin(request);
        WeiXinUser loginUser = getMobileLoginUser(request);
        Long id = getParaToLong(request, "id");
        Address addr = (Address) request.getSession().getAttribute(Contants.ADDRESS_CACHE_KEY);
        if (addr == null) {
            addr = biz.defaultAddr(loginUser);
        }
        msg.setData(addr);
        msg.setErrorCode(0);
        return msg;
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ReturnMsg add(HttpServletRequest request) {
        ReturnMsg msg = new ReturnMsg();
        checkMobileLogin(request);
        WeiXinUser loginUser = getMobileLoginUser(request);
        String name = getPara(request, "name");
        String phone = getPara(request, "phone");
        String area = getPara(request, "area");
        String address = getPara(request, "address");
        Address newAddr = new Address(loginUser.getAppid(), loginUser.getId(), name, phone, area, address);
        biz.save(newAddr);
        msg.setErrorCode(0);
        return msg;
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ReturnMsg list(HttpServletRequest request) {
        ReturnMsg msg = new ReturnMsg();
        checkMobileLogin(request);
        WeiXinUser loginUser = getMobileLoginUser(request);
        List<Address> list = biz.findUserAddress(loginUser);
        msg.setData(list);
        msg.setErrorCode(0);
        return msg;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public ReturnMsg delete(HttpServletRequest request) {
        ReturnMsg msg = new ReturnMsg();
        checkMobileLogin(request);
        WeiXinUser loginUser = getMobileLoginUser(request);
        biz.deleteByUserIdAndId(loginUser.getId(), getParaToLong(request, "id"));
        msg.setErrorCode(0);
        return msg;
    }
}
