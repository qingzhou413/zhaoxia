package cn.zhaoblog.zhaoxia.common;

import com.joysuch.core.util.FileUtil;

import java.io.File;

public class PathUtil {

    private static final String BASE_PATH = System.getProperty("user.home") + File.separator + "zhaoxia-data/";
    private static final String UPLOAD_PATH = BASE_PATH + File.separator + "upload";

    public static final String getUploadImgPath(){
        String path = UPLOAD_PATH + File.separator + "img" + File.separator;
        FileUtil.generateFolder(path);
        return path;
    }

    public static String getUploadGoodsCsvPath(){
        String path = BASE_PATH + "goodsCsv" + File.separator;
        cn.zhaoblog.zhaoxia.util.FileUtil.generateFolder(path);
        return path;
    }

}
