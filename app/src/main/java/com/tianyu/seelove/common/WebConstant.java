package com.tianyu.seelove.common;

/**
 * 用于保存一些常量信息，web请求地址
 * @author shisheng.zhao
 * @date 2017-03-28 16:25
 */
public class WebConstant {
    // 测试环境地址（勿删除）
    public static String BASE_URL = "http://test.shiai360.com";
    // 生产环境地址（!!!此地址只有版本升级时才可使用！！！勿删除）
//    public static String BASE_URL = "http://real.shiai360.com";

    // 本地地址（勿删除）
//     public static String BASE_URL = "http://192.168.0.168:8080/seelove/request";

    public static int SPLASH_TIME = 3 * 1000; // splash加载时间

    public static String AccessKey = "8Pu-IcVd7xcMrLdLSubKgT8EWVfKtZOrCkQ0aSox";
    public static String SecretKey = "MVgYeiasQigSxm5pK0kM94O_1q_DKLoa1lQYlIQ6";
    public static final String MAC_NAME = "HmacSHA1";
    public static final String ENCODING = "UTF-8";
}
