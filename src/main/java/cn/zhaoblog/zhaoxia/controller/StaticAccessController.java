package cn.zhaoblog.zhaoxia.controller;

import cn.zhaoblog.zhaoxia.common.PathUtil;
import com.joysuch.core.util.FileUtil;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by 16204 on 2017/10/15.
 */
@RestController
@RequestMapping("staticAccess")
public class StaticAccessController extends BaseController {

    @RequestMapping("img/{fileName:.+}")
    public void img(HttpServletRequest request, HttpServletResponse response, @PathVariable String fileName) {
        String contentType = "image";
        if(fileName.endsWith("jpg")){
            contentType = "image/jpeg;charset=UTF-8";
        }else if(fileName.endsWith("png")){
            contentType = "image/png;charset=UTF-8";
        }
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
        response.setDateHeader("Expires", 0);
        response.setContentType(contentType);

        InputStream is = null;
        try {
        byte[] data = FileUtil.readAsByteArray(new File(PathUtil.getUploadImgPath() + fileName));
            is = new ByteArrayInputStream(data);
            OutputStream out = response.getOutputStream();

            byte[] buf = new byte[1024];
            int len = -1;
            while ((len = is.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
