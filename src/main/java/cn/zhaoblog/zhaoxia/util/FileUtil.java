package cn.zhaoblog.zhaoxia.util;

import cn.zhaoblog.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtil extends HttpServlet {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static String getFileNameByFullPath(String fileFullPath) {
        String fileName = null;
        if (StringUtil.notEmpty(fileFullPath)) {
            int index = fileFullPath.lastIndexOf(File.separator);
            if (index >= 0 && index < (fileFullPath.length() - 1)) {
                return fileFullPath.substring(index + 1);
            }
        }
        return fileName;
    }

    public static void generateFolder(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 读取文件，返回byte[]
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static byte[] getContent(String filePath) throws IOException {
        File file = new File(filePath);
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            return null;
        }
        FileInputStream fi = new FileInputStream(file);
        byte[] buffer = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取
        if (offset != buffer.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        fi.close();
        return buffer;
    }

}
