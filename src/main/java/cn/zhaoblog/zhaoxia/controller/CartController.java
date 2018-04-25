package cn.zhaoblog.zhaoxia.controller;/**
 * Created by 16204 on 2017/10/29.
 */

import cn.zhaoblog.zhaoxia.biz.CartBiz;
import cn.zhaoblog.zhaoxia.dto.AddCartDto;
import cn.zhaoblog.zhaoxia.entity.Cart;
import cn.zhaoblog.zhaoxia.entity.WeiXinUser;
import com.joysuch.core.web.ReturnMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 购物车
 *
 * @author qingzhou
 *         2017-10-29 16:16
 */
@RestController
@RequestMapping("cart")
public class CartController extends MarketBaseController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    private CartBiz biz;

    @Autowired
    public CartController(CartBiz biz) {
        this.biz = biz;
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ReturnMsg list(HttpServletRequest request){
        ReturnMsg msg = new ReturnMsg();

        checkMobileLogin(request);
        WeiXinUser loginUser = getMobileLoginUser(request);
        List<Cart> list = biz.findByUserId(loginUser.getId());
        msg.setData(list);
        msg.setErrorCode(0);
        return msg;
    }

    @RequestMapping(value = "cartCount", method = RequestMethod.GET)
    public ReturnMsg cartCount(HttpServletRequest request){
        ReturnMsg msg = new ReturnMsg();

        checkMobileLogin(request);
        WeiXinUser loginUser = getMobileLoginUser(request);
        int cartCount = biz.findCartCount(loginUser);
        msg.setErrorCode(0);
        msg.setData(cartCount);
        return msg;
    }

    @RequestMapping(value = "addCart", method = RequestMethod.POST)
    public ReturnMsg addCart(HttpServletRequest request) {
        ReturnMsg msg = new ReturnMsg();
        Long id = getParaToLong(request, "id");
        Long specId = getParaToLong(request, "specId");
        Integer count = getParaToInt(request, "count");
        if(id == null || specId == null || count == null){
            msg.addErrorMsg("添加购物车失败，请刷新页面重试");
            return msg;
        }
        AddCartDto dto = new AddCartDto(id, specId, count);
        checkMobileLogin(request);
        WeiXinUser loginUser = getMobileLoginUser(request);
        biz.addCart(loginUser, dto);

        int cartCount = biz.findCartCount(loginUser);
        msg.setErrorCode(0);
        msg.setData(cartCount);
        return msg;
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public ReturnMsg remove(HttpServletRequest request) {
        ReturnMsg msg = new ReturnMsg();
        Long id = getParaToLong(request, "id");
        checkMobileLogin(request);
        WeiXinUser loginUser = getMobileLoginUser(request);
        biz.remove(loginUser, id);

        int cartCount = biz.findCartCount(loginUser);
        msg.setErrorCode(0);
        msg.setData(cartCount);
        return msg;
    }
}
