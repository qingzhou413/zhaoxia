package cn.zhaoblog.zhaoxia.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 阿里云域名解析
 *
 * @author qingzhou
 *         2017-11-14 0:05
 */
public class AliDomainUtil {

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(URLEncoder.encode("a=b+1", "UTF-8"));

    }
}
