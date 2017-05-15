package com.tianyu.seelove.common;

import android.os.Handler;

/**
 * @author shisheng.zhao
 * @Description: 定义一些常量
 * @date 2017-04-01 10:29
 */
public class Constant {
    // 数据库[通用]常量
    public final static int DB_COMMON_VERSION = 10;

    //数据库[圣经]常量
    public final static String DB_NAME = "seelove.db"; // 数据库名称
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
    public static final String QQ_APP_ID = "1105974837";
    public static final String QQ_APP_KEY = "y4e1eVHJ0fuo90T0";
    //微信开放平台
    public static final String WEIXIN_APP_ID = "wxb3509f0204b8d3cb";
    public static final String WEIXIN_APP_SECRET = "457fdf85cd950d433626fec52f9c610d";

    //微信开放平台url
    public static String WEIXIN_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
    public static String WEIXIN_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo";

    public static final int corners = 100;

    //登录界面的handler（自有登录和第三方开放平台登录共用）
    public static Handler loginHandler;
    //是否进行第三方登录中
    public static boolean loginOpenPlatformIng = false;
    //引导登录界面是否存在
    public static boolean loginActivityIng = false;
    // 七牛存储
    public static String videosNameSpace = "http://videos.shiai360.com/";

    // 视频拍摄参数设置
    public static int cbrBufSize = 166; // CBRMode BufSize 保持不变
    public static int cbrBitrate = 600; // CBRMode Bitrate 固定码率值(越大越清晰，但是视频大小会增加）
    public static String velocity = "ultrafast"; // 视频压缩速度，极速压缩
    public static int videoWidth = 480; // 视频拍摄宽度
    public static int videHeight = 600; // 视频拍摄高度
    public static int maxRecordTime = 8 * 1000; // 拍摄视频最长时长
    public static int minRecordTime = 1 * 1000; // 拍摄视频最短时长
    public static int maxFrameRate = 18; // 视频拍摄码率
}
