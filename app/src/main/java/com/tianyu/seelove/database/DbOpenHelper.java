package com.tianyu.seelove.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.tianyu.seelove.common.Constant;
import com.tianyu.seelove.dao.impl.MessageDaoImpl;
import com.tianyu.seelove.model.enums.MessageType;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.utils.LogUtil;
import java.util.HashMap;

/**
 * 数据库操作工具类
 * @author shisheng.zhao
 * @date 2015-09-07 18:51:22
 */
public class DbOpenHelper extends SQLiteOpenHelper {
    // 用户表
    private final static String sqlCreateUserInfo = "create table userinfo(_id integer primary key autoincrement,uid,nickname,noteName,amenId,sex default '0'," +
            "province,city,country,headUrl,phoneNum,birthday,marriage default '0',age default '0',level default '1',signa,countryCode,status default '0',km,timeAgo,dateTime,timestamp default '0'," +
            "uTime default '0',dnd default '0',remark,provinceId,cityId)";
    private final static String sqlDropUserInfo = "drop table if exists userinfo";
    // 群组表
    private final static String sqlCreateGroupInfo = "create table groupinfo(_id integer primary key autoincrement,groupId,groupName,groupType default '0',uid,maxList,count,icon,addList default '0',dnd default '0',showName default '0',memberStatus default '0',checkJoin default '0')";
    private final static String sqlDropGroupInfo = "drop table if exists groupinfo";
    // 群组成员关系表
    private final static String sqlCreateGroupMember = "create table groupmember(_id integer primary key autoincrement,groupId,uid)";
    // 邻舍信息表
    private final static String sqlCreateNearbyInfo = "create table nearbyinfo(_id integer primary key autoincrement,nearbyId,nearbyType,remark)";
    private final static String sqlDropNearbyInfo = "drop table if exists nearbyinfo";
    // 消息表
    private final static String sqlCreateMessageInfo = "create table messageinfo(_id integer primary key autoincrement,messageId,userFrom,userTo,content,timestamp,groupId,isRead,isVisible default '0',isDelay,state,type," +
            "audiolength,lng,lat,address,volumeId,chapterId,sectionId,prayId,thumUrl,articleId,title,imageUrl,url,articleType,userId,userName,headUrl,userTemp)";
    private final static String sqlDropMessageInfo = "drop table if exists messageinfo";
    // 会话表
    private final static String sqlCreateSessionInfo = "create table sessioninfo(_id integer primary key autoincrement,targetId NOT NULL unique,sessionType,lastMessageId,priority,sessionIcon,messageType,sessionName,sessionContent,sessionIsRead,prayId,praySubject)";
    private final static String sqlDropSessionInfo = "drop table if exists sessioninfo";
    // 任务表
    private final static String sqlCreateTaskInfo = "create table taskinfo(" +
            "_id integer primary key autoincrement,taskId default '0',status default '0',time,message,isRead,type," + // 任务基础属性
            "userFrom default '0',userTo default '0',applySta default '0'," + // 好友任务属性
            "groupId default '0',groupShow default '0'," + // 群组任务属性
            "meetingId default '0',applyId default '0')"; // 聚会任务属性
    private final static String sqlDropTaskInfo = "drop table if exists taskinfo";
    // 系统新闻表
    private final static String sqlCreateNewsInfo = "create table newsinfo(_id integer primary key autoincrement,newsId,newsTitle,newsTotle,newsImgUrl,newsSummary,newsUrl,newsSource,timeLong)";
    // 支付订单表
    private final static String sqlCreatePayOrderInfo = "create table payorderinfo(_id integer primary key autoincrement,orderId,meetingId,userId,orderNo,orderName,orderType default '1',orderPrice,orderStatus default '1',orderCreateTime,orderUpdateTime,statusStr,orderIcon,remark)";
    // 聚会表
    private final static String sqlCreateMeetingInfo = "create table meetinginfo(_id integer primary key autoincrement,meetingId,userId,meetingType default '1',meetingName,meetingIcon,meetingBackgroud,meetingDetail,meetingLat,meetingLon," +
            "meetingAddress,meetingPhone,meetingWorshipTime,meetingMemberCount,meetingGroupCount,meetingMaxGroupCount,meetingCheckStatus default '1',meetingOptStatus default '1'," +
            "meetingCreateTime,meetingUpdateTime,meetingStatus default '1',payStatus default '0',memberStatus default '0',checkJoin default '0',payShow default '0',remark)";
    // 聚会群组映射关系表
    private final static String sqlCreateMeetingGroupInfo = "create table meetinggroupinfo(_id integer primary key autoincrement,meetingId,groupId,groupCreateTime,groupUpdateTime,remark)";
    // 聚会成员表
    private final static String sqlCreateMeetingMemberInfo = "create table meetingmemberinfo(_id integer primary key autoincrement,meetingId,userId,headUrl,memberStatus default '0',memberCheckStatus default '1',memberCreateTime,memberUpdateTime,remark)";
    // 系统配置表
    private final static String sqlCreateSysConfig = "create table sysconfig(_id integer primary key autoincrement,prayNotify default '1',newsNotify default '1',commentNotify default '1',interactNotify default '1')";
    private final static String sqlDropSysConfig = "drop table if exists sysconfig";
    // 启动配置表
    private final static String sqlCreateConfig = "create table configinfo(_id integer primary key autoincrement,childCount default '0',processDepth default '0',meetingId)";
    private final static String sqlDropConfig = "drop table if exists configinfo";
    // 收获地址表
    private final static String sqlCreateDeliveryAddressInfo = "create table deliveryaddressinfo(_id integer primary key autoincrement,addressId,userId,meetingId,provinceId,provinceName,cityId,cityName,districtId,districtName,name,phone,detail)";
    private static DbOpenHelper helper;
    private static Context context;
    private HashMap<MessageType, MessageDaoImpl> map = new HashMap<MessageType, MessageDaoImpl>();

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
        db.execSQL(sqlCreateGroupInfo);// 群组表
        db.execSQL(sqlCreateGroupMember);// 群组成员关系表
        db.execSQL(sqlCreateNearbyInfo);// 邻舍信息表
        db.execSQL(sqlCreateMessageInfo);// 消息表
        db.execSQL(sqlCreateSessionInfo);// 会话表
        db.execSQL(sqlCreateTaskInfo);// 任务表
        db.execSQL(sqlCreateNewsInfo);// 系统文章表
        db.execSQL(sqlCreatePayOrderInfo);// 支付订单表
        db.execSQL(sqlCreateMeetingInfo);// 聚会表
        db.execSQL(sqlCreateMeetingGroupInfo);// 聚会群组映射关系表
        db.execSQL(sqlCreateMeetingMemberInfo);// 聚会成员表
        db.execSQL(sqlCreateSysConfig);// 系统配置表
        db.execSQL(sqlCreateConfig);// 启动配置表
        db.execSQL(sqlCreateDeliveryAddressInfo);// 收货地址表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtil.i("db update begin:userId:" + AppUtils.getInstance().getUserId() + ",oldVersion:" + oldVersion + ",newVersion:" + newVersion);
    }

    public void closeDb() {
        this.close();
    }
}