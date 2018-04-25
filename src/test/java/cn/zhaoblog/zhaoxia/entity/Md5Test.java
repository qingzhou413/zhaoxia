package cn.zhaoblog.zhaoxia.entity;

import com.joysuch.core.util.Md5Util;

/**
 * @author qingzhou
 *         2017-12-16 17:54
 */
public class Md5Test {
    public static void main(String[] args) {
        System.out.println(Md5Util.md5(Md5Util.md5("xinghuile")));
    }
}
