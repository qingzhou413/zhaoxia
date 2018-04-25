package cn.zhaoblog.zhaoxia.controller;

import cn.zhaoblog.util.DateUtil;
import cn.zhaoblog.zhaoxia.biz.GoodsBiz;
import cn.zhaoblog.zhaoxia.common.PathUtil;
import cn.zhaoblog.zhaoxia.controller.excel.ExcelGoodsReader;
import cn.zhaoblog.zhaoxia.dto.GoodsDto;
import cn.zhaoblog.zhaoxia.entity.Goods;
import cn.zhaoblog.zhaoxia.entity.GoodsSpecDet;
import com.joysuch.core.pager.PageBean;
import com.joysuch.core.pager.PageParam;
import com.joysuch.core.util.FileUtil;
import com.joysuch.core.util.JsonUtil;
import com.joysuch.core.util.MapParam;
import com.joysuch.core.web.ReturnMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by 16204 on 2017/10/8.
 */
@RestController
@RequestMapping("goods")
public class GoodsController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private GoodsBiz biz;
    @Autowired
    private ExcelGoodsReader reader;

    @RequestMapping("uploadCvs")
    @ResponseBody
    public ReturnMsg uploadCvs(HttpServletRequest request) {
        ReturnMsg msg = new ReturnMsg();
        // 创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        // 判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {
            // 从request中取出上传文件
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            // 取得request中的所有文件名
            Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
            if (fileMap != null && fileMap.size() > 0) {
                MultipartFile binFile = fileMap.get("csvFile");
                if (binFile == null) {
                    msg.addErrorMsg("上传文件不能为空");
                } else {
                    if (binFile.getSize() > 8 * 1024 * 1024) {
                        msg.addErrorMsg("上传文件不能大于8M");
                    } else {
                        try {
                            String filePath = PathUtil.getUploadGoodsCsvPath() + DateUtil.formatDateTime(new Date()) + "_" + new Random().nextInt(1000) + binFile.getOriginalFilename();
                            byte[] data = binFile.getBytes();
                            boolean eRes = FileUtil.writeToFile(filePath, data);
                            if (eRes) {
                                List<GoodsSpecDet> list = reader.read(msg, new File(filePath));
                                if (list == null) {
                                    msg.addErrorMsg("Excel解析失败，请确认数据是否正确");
                                }
                                if (!msg.isValid()) {
                                    return msg;
                                }
                                String result = biz.uploadGoodsExcel(list);
                                msg.setData(result);
                                msg.setErrorCode(0);
                            } else {
                                msg.addErrorMsg("上传文件失败");
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            } else {
                msg.addErrorMsg("上传文件不能为空");
            }
        } else {
            msg.addErrorMsg("上传文件不能为空");
        }
        return msg;
    }


    @RequestMapping(value = "hot", method = RequestMethod.GET)
    public ReturnMsg hot(HttpServletRequest request) {
        ReturnMsg msg = new ReturnMsg();

        List<Goods> list = biz.hot();
        msg.setData(list);
        msg.setErrorCode(0);
        return msg;
    }

    @RequestMapping(value = "home", method = RequestMethod.GET)
    public ReturnMsg home(HttpServletRequest request) {
        ReturnMsg msg = new ReturnMsg();

        List<Goods> list = biz.home();
        msg.setData(list);
        msg.setErrorCode(0);
        return msg;
    }

    @RequestMapping(value = "setactive", method = RequestMethod.POST)
    public ReturnMsg setactive(HttpServletRequest request) {
        ReturnMsg msg = new ReturnMsg();
        Long id = getParaToLong(request, "id");
        Integer act = getParaToInt(request, "active");
        biz.setActive(id, act);
        msg.setErrorCode(0);
        return msg;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public ReturnMsg delete(HttpServletRequest request) {
        ReturnMsg msg = new ReturnMsg();
        Long id = getParaToLong(request, "id");

        biz.deleteById(id);
        msg.setErrorCode(0);
        return msg;
    }

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public ReturnMsg detail(HttpServletRequest request) {
        ReturnMsg msg = new ReturnMsg();
        Long id = getParaToLong(request, "id");

        Goods goods = biz.findById(id);
        if (goods == null) {
            msg.addErrorMsg("未查询到商品");
            return msg;
        }
        biz.addViewCount(id);
        msg.setData(goods);
        msg.setErrorCode(0);
        return msg;
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ReturnMsg list(HttpServletRequest request) {
        ReturnMsg msg = new ReturnMsg();

        PageParam page = getPageParam(request);
        MapParam pars = new MapParam();
        Map<String, Object> params = pars.build();
        PageBean<GoodsDto> data = biz.list(page, params);

        msg.setErrorCode(0);
        msg.setData(data);
        return msg;
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ReturnMsg add(HttpServletRequest request, @RequestBody String json) {
        ReturnMsg msg = new ReturnMsg();
        logger.info("add goods: " + json);
        GoodsDto dto = JsonUtil.fromJson(json, GoodsDto.class);
        if (dto == null) {
            msg.addErrorMsg("填写数据有误");
            return msg;
        }
        if (dto.getSpecDets() == null || dto.getSpecDets().size() == 0) {
            msg.addErrorMsg("规格不能为空");
            return msg;
        }
        Goods g = convDto2Goods(dto);
        biz.save(g);
        msg.setErrorCode(0);
        return msg;
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public ReturnMsg edit(HttpServletRequest request, @RequestBody String json) {
        ReturnMsg msg = new ReturnMsg();
        logger.info("edit goods: " + json);
        GoodsDto dto = JsonUtil.fromJson(json, GoodsDto.class);
        if (dto == null) {
            msg.addErrorMsg("填写数据有误");
            return msg;
        }
        if (dto.getSpecDets() == null || dto.getSpecDets().size() == 0) {
            msg.addErrorMsg("规格不能为空");
            return msg;
        }
        Goods g = convDto2Goods(dto);
        biz.edit(g);
        msg.setErrorCode(0);
        return msg;
    }

    @RequestMapping(value = "concern", method = RequestMethod.POST)
    public ReturnMsg concern(HttpServletRequest request) {
        ReturnMsg msg = new ReturnMsg();
        Long id = getParaToLong(request, "id");
        Boolean flag = getParaToBoolean(request, "flag");
        if (id == null || flag == null) {
            msg.addErrorMsg("关注参数错误");
            return msg;
        }
        //TODO
        msg.setErrorCode(0);
        msg.setData("关注接口调用成功");
        return msg;
    }

    private Goods convDto2Goods(GoodsDto dto) {
        Goods g = new Goods();
        g.setId(dto.getId());
        g.setName(dto.getName());
        g.setBrandId(dto.getBrandId());
        g.setDesc(dto.getDesc());
        g.setDetImgsStr(dto.getDetImgsStr());
        g.setHeadImg(dto.getHeadImg());
        g.setPackId(dto.getPackStyleId());
        g.setRootCatId(dto.getRootCatId());
        g.setSubCatId(dto.getSubCatId());
        g.setSpecDets(dto.getSpecDets());
        return g;
    }
}
