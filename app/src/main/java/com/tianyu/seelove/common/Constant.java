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
    public final static String DB_PATH = "/data/data/com.wmgj.amen/databases/";// 数据库存储路径
    public final static int DB_BIBLE_VERSION = 5;// 数据库版本号

    public static int screenWidth;
    public static int screenHeight;
    public static int IO_BUFFER_SIZE = 2 * 1024;
    public static int imageCount = 4; // 图片最多可以选择张数

    public static int[] deviceWidthHeight;
}
