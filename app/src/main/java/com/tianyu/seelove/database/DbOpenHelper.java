package com.tianyu.seelove.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Settings;

import com.tianyu.seelove.common.Constant;
import com.tianyu.seelove.dao.impl.MessageDaoImpl;
import com.tianyu.seelove.dao.impl.SessionDaoImpl;
import com.tianyu.seelove.model.entity.message.SLSession;
import com.tianyu.seelove.model.entity.message.SLTextMessage;
import com.tianyu.seelove.model.enums.MessageType;
import com.tianyu.seelove.model.enums.SessionType;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.utils.LogUtil;

/**
 * 数据库操作工具类
 *
 * @author shisheng.zhao
 * @date 2017-04-05 07:53
 */
public class DbOpenHelper extends SQLiteOpenHelper {
    // 用户表
    private final static String sqlCreateUserInfo = "CREATE TABLE USERINFO(_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "UserId,HeadUrl,NickName,AccountType DEFAULT '0',Age DEFAULT '0',Sex DEFAULT '0',BigImg,CityCode DEFAULT '0'," +
            "CityName,WorkCode DEFAULT '0',WorkName,EducationCode DEFAULT '0',EducationName,HouseCode DEFAULT '0'," +
            "HouseName,MarriageCode DEFAULT '0',MarriageName,Introduce,Remark,VideoCount DEFAULT '0',FollowCount DEFAULT '0'," +
            "FollowedCount DEFAULT '0')";
    private final static String sqlDropUserInfo = "DROP TABLE IF EXISTS USERINFO";
    // 短视频表
    private final static String sqlCreateVideoInfo = "CREATE TABLE VIDEOINFO(_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "VideoId,VideoTitle,VideoTime,IsDefault DEFAULT '0',VideoImg,VideoUrl,Remark)";
    private final static String sqlDropVideoInfo = "DROP TABLE IF EXISTS VIDEOINFO";
    // 用户和短视频关系表
    private final static String sqlCreateUserVideoInfo = "CREATE TABLE USER_VIDEO(_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "UserId,VideoId)";
    private final static String sqlDropUserVideoInfo = "DROP TABLE IF EXISTS USER_VIDEO";
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
        super(context, AppUtils.getInstance().getUserId(), null, Constant.DB_COMMON_VERSION);
        DbOpenHelper.context = context;
    }

    private DbOpenHelper() {
        super(context, AppUtils.getInstance().getUserId(), null, Constant.DB_COMMON_VERSION);
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
        db.execSQL(sqlCreateUserVideoInfo);// 短视频和用户关系表
        db.execSQL(sqlCreateMessageInfo);// 消息表
        db.execSQL(sqlCreateSessionInfo);// 会话表
        initData(db);
    }

    private void initData(SQLiteDatabase db) {
        String insertUserInfo = "INSERT INTO USERINFO(UserId,HeadUrl,NickName,AccountType,Age,Sex,BigImg,CityCode,CityName," +
                "WorkCode,WorkName,EducationCode,EducationName,HouseCode,HouseName,MarriageCode,MarriageName,Introduce," +
                "Remark,VideoCount,FollowCount,FollowedCount) VALUES('1001','https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=673651839,1464649612&fm=111&gp=0.jpg'" +
                ",'天宇','1','26','1','https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=673651839,1464649612&fm=111&gp=0.jpg'," +
                "'01','北京市','001','软件工程师','001','本科','001','已购房','001','未婚','活波开朗的大男孩!','','10','20','33')";
        db.execSQL(insertUserInfo);
        String insertUserInfo2 = "INSERT INTO USERINFO(UserId,HeadUrl,NickName,AccountType,Age,Sex,BigImg,CityCode,CityName," +
                "WorkCode,WorkName,EducationCode,EducationName,HouseCode,HouseName,MarriageCode,MarriageName,Introduce," +
                "Remark,VideoCount,FollowCount,FollowedCount) VALUES('1002','https://ss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/image/h%3D200/sign=fd90a83e900a304e4d22a7fae1c9a7c3/d01373f082025aafa480a2f1fcedab64034f1a5d.jpg'" +
                ",'天涯','1','26','1','https://ss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/image/h%3D200/sign=fd90a83e900a304e4d22a7fae1c9a7c3/d01373f082025aafa480a2f1fcedab64034f1a5d.jpg'," +
                "'01','北京市','001','软件工程师','001','本科','001','已购房','001','未婚','活波开朗的大男孩!','','10','20','33')";
        db.execSQL(insertUserInfo2);
        String insertVideoInfo = "INSERT INTO VIDEOINFO(VideoId,VideoTitle,VideoTime,IsDefault,VideoImg,VideoUrl,Remark) VALUES('1000','爱情观'," +
                "'10','1','https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=673651839,1464649612&fm=111&gp=0.jpg','','')";
        db.execSQL(insertVideoInfo);
        String insertUserVideo = "INSERT INTO USER_VIDEO(UserId,VideoId) VALUES('1000','1000')";
        db.execSQL(insertUserVideo);
        String insertMessageInfo = "INSERT INTO MESSAGEINFO(MessageId,UserFrom,UserTo,MessageContent,TimeStamp,IsRead,IsVisable," +
                "IsDelay,SendStatue,MessageType) VALUES('1000','1001','1000','我是测试消息!'," + System.currentTimeMillis() + ",'0','0','1','0','TEXT')";
        db.execSQL(insertMessageInfo);
        String insertMessageInfo2 = "INSERT INTO MESSAGEINFO(MessageId,UserFrom,UserTo,MessageContent,TimeStamp,IsRead,IsVisable," +
                "IsDelay,SendStatue,MessageType) VALUES('1001','1002','1000','我是天涯消息!'," + System.currentTimeMillis() + ",'0','0','1','0','TEXT')";
        db.execSQL(insertMessageInfo2);
        String insertSessionInfo = "INSERT INTO SESSIONINFO(TargetId,LastMessageId,Priority,SessionIcon,SessionName," +
                "SessionIsRead,SessionContent) VALUES('1001','1000'," + System.currentTimeMillis() + ",'https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=673651839,1464649612&fm=111&gp=0.jpg','天宇','0','我是测试消息!')";
        db.execSQL(insertSessionInfo);
        String insertSessionInfo2 = "INSERT INTO SESSIONINFO(TargetId,LastMessageId,Priority,SessionIcon,SessionName," +
                "SessionIsRead,SessionContent) VALUES('1002','1001'," + System.currentTimeMillis() + ",'https://ss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/image/h%3D200/sign=fd90a83e900a304e4d22a7fae1c9a7c3/d01373f082025aafa480a2f1fcedab64034f1a5d.jpg','天涯','0','我是天涯消息!')";
        db.execSQL(insertSessionInfo2);

        SLTextMessage slTextMessage = new SLTextMessage();
        slTextMessage.setMessageId("1000");
        slTextMessage.setUserFrom("1001");
        slTextMessage.setUserTo("1000");
        slTextMessage.setMessageContent("我是测试消息!");
        slTextMessage.setTimestamp(System.currentTimeMillis());
        slTextMessage.setIsRead(0);
        slTextMessage.setIsVisible(0);
        slTextMessage.setIsDelay(1);
        slTextMessage.setSendStatue(0);
//        new MessageDaoImpl().addMessage(slTextMessage);

        SLSession slSession = new SLSession();
        slSession.setTargetId("1000");
//        slSession.setSessionType(SessionType.CHAT);
        slSession.setLastMessageId("1000");
        slSession.setPriority(System.currentTimeMillis());
        slSession.setSessionIcon("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=673651839,1464649612&fm=111&gp=0.jpg");
//        slSession.setMessageType(MessageType.TEXT);
        slSession.setSessionName("天宇");
        slSession.setSessionIsRead(0);
        slSession.setSessionContent("我是测试消息!");
//        new SessionDaoImpl().addSession(slSession);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtil.i("db update begin:userId:" + AppUtils.getInstance().getUserId() + ",oldVersion:" + oldVersion + ",newVersion:" + newVersion);
    }

    public void closeDb() {
        this.close();
    }
}