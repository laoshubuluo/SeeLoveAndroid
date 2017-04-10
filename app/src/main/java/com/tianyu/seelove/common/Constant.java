package com.tianyu.seelove.common;

/**
 * @author shisheng.zhao
 * @Description: 定义一些常量
 * @date 2017-04-01 10:29
 */
public class Constant {
    // 数据库[通用]常量
    public final static int DB_COMMON_VERSION = 10;

    //数据库[圣经]常量
    public final static String DB_NAME = "bible.db"; // 数据库名称
    public final static String DB_PATH = "/data/data/com.tianyu.seelove/databases/";// 数据库存储路径
    public final static int DB_BIBLE_VERSION = 5;// 数据库版本号

    public static int screenWidth;
    public static int screenHeight;
    public static int IO_BUFFER_SIZE = 2 * 1024;
    public static int imageCount = 4; // 图片最多可以选择张数

    public static int childCount = 2;
    public static int processDepth = 1;

    public static long tempMill = 0;
    public static long currentMill = 0;

    public static int[] deviceWidthHeight;

    //QQ开放平台
    public static final String QQ_APP_ID = "1104784484";
    public static final String QQ_APP_KEY = "ASJIYRxo7CfkLPy9";
    //微信开放平台
    public static final String WEIXIN_APP_ID = "wxb3509f0204b8d3cb";
    public static final String WEIXIN_APP_SECRET = "457fdf85cd950d433626fec52f9c610d";
}
