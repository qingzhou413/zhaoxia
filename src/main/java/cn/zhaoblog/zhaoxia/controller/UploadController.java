package cn.zhaoblog.zhaoxia.controller;

import cn.zhaoblog.zhaoxia.common.PathUtil;
import com.joysuch.core.util.FileUtil;
import com.joysuch.core.util.UuidUtil;
import com.joysuch.core.web.ReturnMsg;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by 16204 on 2017/10/15.
 */
@RestController
@RequestMapping("upload")
public class UploadController extends BaseController {

    @RequestMapping(value = "img", method = RequestMethod.POST)
    public ReturnMsg img(HttpServletRequest request){
        ReturnMsg msg = new ReturnMsg();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

            Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
            if (fileMap != null && fileMap.size() > 0) {
                MultipartFile binFile = fileMap.get("file");
                if(binFile == null){
                    msg.addErrorMsg("未找到上传文件");
                }else{
                    //写入文件
                    String fileNameStr = binFile.getOriginalFilename();
                    int dotInd = fileNameStr.lastIndexOf(".");
                    String suffix = dotInd == -1 ? fileNameStr : fileNameStr.substring(dotInd);
                    String fileName = UuidUtil.getUuid() + suffix;

                    try {
                        boolean b = FileUtil.writeToFile(PathUtil.getUploadImgPath() + fileName, binFile.getBytes());
                        if(b){
                            msg.setData(fileName);
                            msg.setErrorCode(0);
                        }else{
                            msg.addErrorMsg("保存文件错误");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        msg.addErrorMsg("保存文件错误");
                    }
                }
            }
        }
        return msg;
    }
}
