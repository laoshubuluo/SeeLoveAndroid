package com.tianyu.seelove.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.tianyu.seelove.common.Constant;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.utils.LogUtil;

/**
 * 数据库操作工具类
 * @author shisheng.zhao
 * @date 2017-04-05 07:53
 */
public class DbOpenHelper extends SQLiteOpenHelper {
    // 用户表
    private final static String sqlCreateUserInfo = "CREATE TABLE USERINFO(_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "UserId,HeadUrl,NickName,AccountType DEFAULT '0',Age DEFAULT '18',Sex DEFAULT '0',BigImg,CityCode DEFAULT '01'," +
            "CityName,WorkCode DEFAULT '01',WorkName,EducationCode DEFAULT '0',EducationName,HouseCode DEFAULT '0'," +
            "HouseName,MarriageCode DEFAULT '0',MarriageName,Introduce,Remark,VideoCount DEFAULT '0',FollowCount DEFAULT '0'," +
            "FollowedCount DEFAULT '0')";
    private final static String sqlDropUserInfo = "DROP TABLE IF EXISTS USERINFO";
    // 短视频表
    private final static String sqlCreateVideoInfo = "CREATE TABLE VIDEOINFO(_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "VideoId,UserId,VideoTitle,VideoTime,IsDefault DEFAULT '0',VideoImg,VideoUrl,Remark)";
    private final static String sqlDropVideoInfo = "DROP TABLE IF EXISTS VIDEOINFO";
    // 用户和短视频关系表
//    private final static String sqlCreateUserVideoInfo = "CREATE TABLE USER_VIDEO(_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
//            "UserId,VideoId)";
//    private final static String sqlDropUserVideoInfo = "DROP TABLE IF EXISTS USER_VIDEO";
    // 消息表
    private final static String sqlCreateMessageInfo = "CREATE TABLE MESSAGEINFO(_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "MessageId,UserFrom,UserTo,MessageContent,TimeStamp,IsRead DEFAULT '0',IsVisable DEFAULT '0',IsDelay DEFALUT '1'," +
            "SendStatue DEFAULT '0',MessageType,ThumUrl,AudioLength,Lng,Lat,Address)";
    private final static String sqlDropMessageInfo = "DROP TABLE IF EXISTS MESSAGEINFO";
    // 会话表
    private final static String sqlCreateSessionInfo = "CREATE TABLE SESSIONINFO(_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "TargetId,SessionType,LastMessageId,Priority,SessionIcon,MessageType,SessionName,SessionIsRead DEFAULT '0',SessionContent)";
    private final static String sqlDropSessionInfo = "DROP TABLE IF EXISTS SESSIONINFO";

    private static DbOpenHelper helper;
    private static Context context;

    private DbOpenHelper(Context context) {
        super(context, String.valueOf(AppUtils.getInstance().getUserId()), null, Constant.DB_COMMON_VERSION);
        DbOpenHelper.context = context;
    }

    private DbOpenHelper() {
        super(context, String.valueOf(AppUtils.getInstance().getUserId()), null, Constant.DB_COMMON_VERSION);
    }

    public static DbOpenHelper getInstance(Context context) {
        if (helper == null) {
            synchronized (DbOpenHelper.class) {
                helper = new DbOpenHelper(context);
            }
        }
        return helper;
    }

    public static void reload() {
        if (helper != null) {
            try {
                helper.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        helper = new DbOpenHelper();
    }

    public static void reset() {
        if (helper != null) {
            helper.close();
        }
        helper = null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreateUserInfo);// 用户表
        db.execSQL(sqlCreateVideoInfo);// 短视频表
//        db.execSQL(sqlCreateUserVideoInfo);// 短视频和用户关系表
        db.execSQL(sqlCreateMessageInfo);// 消息表
        db.execSQL(sqlCreateSessionInfo);// 会话表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtil.i("db update begin:userId:" + AppUtils.getInstance().getUserId() + ",oldVersion:" + oldVersion + ",newVersion:" + newVersion);
    }

    public void closeDb() {
        this.close();
    }
}