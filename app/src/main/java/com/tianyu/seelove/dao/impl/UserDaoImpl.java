package com.tianyu.seelove.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.tianyu.seelove.dao.UserDao;
import com.tianyu.seelove.manager.DbConnectionManager;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.model.enums.SexType;
import com.tianyu.seelove.utils.LogUtil;
import com.tianyu.seelove.utils.StringUtils;

import java.util.List;

/**
 * @author shisheng.zhao
 * @Description: 用户数据访问层实现类
 * @date 2017-04-10 17:40
 */
public class UserDaoImpl implements UserDao {
    // 插入用户数据
    public final static String sqlInsertUserInfo = "INSERT INTO USERINFO(UserId,HeadUrl,NickName,AccountType,Age,Sex,BigImg,CityCode,CityName,WorkCode," +
            "WorkName,EducationCode,EducationName,HouseCode,HouseName,MarriageCode,MarriageName,Introduce,Remark,VideoCount,FollowCount,FollowedCount) " +
            "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    // 根据userId更新用户信息
    public final static String sqlUpdateUserByUserId = "UPDATE USERINFO SET HeadUrl = ?,NickName = ?,AccountType = ?,Age = ?,Sex = ?," +
            "BigImg = ?,CityCode = ?,CityName = ?,WorkCode = ?,WorkName = ?,EducationCode = ?,EducationName = ?,HouseCode = ?,HouseName = ?," +
            "MarriageCode = ?,MarriageName = ?,Introduce = ?,Remark = ?,VideoCount = ?,FollowCount = ?,FollowedCount = ? WHERE UserId = ?";
    // 根据userId更新用户头像headUrl
    public final static String sqlUpdateHeadUrlByUserId = "UPDATE USERINFO SET HeadUrl = ? WHERE UserId = ?";
    // 根据userId查询用户详情
    public final static String sqlSelectUserByUserId = "SELECT * FROM USERINFO WHERE UserId = ?";
    // 根据userId删除当前用户
    public final static String sqlDeleteUserByUserId = "DELETE FROM USERINFO WHERE UserId = ?";

    @Override
    public void addUser(SLUser slUser) {
        if (null == slUser) {
            return;
        }
        if (null == getUserByUserId(slUser.getUserId())) {
            insert(slUser);
        } else {
            deleteUserByUserId(String.valueOf(slUser.getUserId()));
            insert(slUser);
        }
    }

    private boolean insert(SLUser user) {
        try {
            DbConnectionManager.getInstance().getConnection().execSQL(
                    sqlInsertUserInfo,
                    new String[]{String.valueOf(user.getUserId()), user.getHeadUrl(),
                            user.getNickName(), String.valueOf(user.getAccountType()),
                            String.valueOf(user.getAge()), user.getSex(),
                            user.getBigImg(), String.valueOf(user.getCityCode()), user.getCityName(),
                            String.valueOf(user.getWorkCode()), user.getWorkName(),
                            String.valueOf(user.getEducationCode()), user.getEducationName(),
                            String.valueOf(user.getHouseCode()),
                            user.getHouseName(),
                            String.valueOf(user.getMarriageCode()),
                            user.getMarriageName(), user.getIntroduce(),
                            user.getRemark(), String.valueOf(user.getVideoCount()), String.valueOf(user.getFollowCount()),
                            String.valueOf(user.getFollowedCount())});
            LogUtil.i("db execute sql success： " + sqlInsertUserInfo);
            return true;
        } catch (Exception ex) {
            LogUtil.e("db execute sql error： " + sqlInsertUserInfo, ex);
            return false;
        }
    }

    /**
     * 执行插入动作，将用户插入数据库
     *
     * @param userList
     * @return
     */
    private boolean insert(List<SLUser> userList) {
        if (userList == null || userList.isEmpty())
            return false;
        try {
            SQLiteDatabase db = DbConnectionManager.getInstance().getConnection();
            SQLiteStatement stat = db.compileStatement(sqlInsertUserInfo);
            for (SLUser user : userList) {
                bindData(stat, user);
                stat.executeInsert();
            }
            LogUtil.i("db execute sql success: insert userInfo: " + userList.size());
            return true;
        } catch (Exception ex) {
            LogUtil.e("db execute sql error: insert userInfo: " + userList.size(), ex);
            return false;
        }
    }

    /**
     * update User
     *
     * @param user
     * @return
     */
    @Override
    public boolean updateUserByUserId(SLUser user) {
        if (user == null || StringUtils.isNullOrBlank(user.getNickName()))
            return false;
        try {
            DbConnectionManager.getInstance().getConnection().execSQL(
                    sqlUpdateUserByUserId,
                    new String[]{user.getHeadUrl(),
                            user.getNickName(), String.valueOf(user.getAccountType()),
                            String.valueOf(user.getAge()), SexType.parseByMsg(user.getSex()).getResultCode(),
                            user.getBigImg(), String.valueOf(user.getCityCode()), user.getCityName(),
                            String.valueOf(user.getWorkCode()), user.getWorkName(),
                            String.valueOf(user.getEducationCode()), user.getEducationName(),
                            String.valueOf(user.getHouseCode()),
                            user.getHouseName(),
                            String.valueOf(user.getMarriageCode()),
                            user.getMarriageName(), user.getIntroduce(),
                            user.getRemark(), String.valueOf(user.getVideoCount()), String.valueOf(user.getFollowCount()),
                            String.valueOf(user.getFollowedCount()), String.valueOf(user.getUserId())});
            LogUtil.i("db execute sql success： " + sqlUpdateUserByUserId);
            return true;
        } catch (Exception ex) {
            LogUtil.e("db execute sql error： " + sqlUpdateUserByUserId, ex);
            return false;
        }
    }

    /**
     * update User
     *
     * @param userId
     * @return
     */
    @Override
    public boolean updateUserHeadUrlByUserId(String userId, String headUrl) {
        if (StringUtils.isNullOrBlank(userId))
            return false;
        try {
            DbConnectionManager.getInstance().getConnection().execSQL(
                    sqlUpdateHeadUrlByUserId,
                    new String[]{headUrl, userId});
            LogUtil.i("db execute sql success： " + sqlUpdateHeadUrlByUserId);
            return true;
        } catch (Exception ex) {
            LogUtil.e("db execute sql error： " + sqlUpdateHeadUrlByUserId, ex);
            return false;
        }
    }

    /**
     * 根据uid获取User
     *
     * @param userId
     * @return
     */
    @Override
    public SLUser getUserByUserId(long userId) {
        Cursor cursor = null;
        SLUser user = new SLUser();
        try {
            cursor = DbConnectionManager.getInstance().getConnection().rawQuery(sqlSelectUserByUserId, new String[]{String.valueOf(userId)});
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                return parseUserFromCursor(cursor);
            } else
                return user;
        } catch (Throwable ex) {
            LogUtil.e("db execute sql error： " + sqlSelectUserByUserId, ex);
            return user;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 删除用户信息
     *
     * @param userId
     * @return
     */
    @Override
    public boolean deleteUserByUserId(String userId) {
        try {
            DbConnectionManager.getInstance().getConnection().execSQL(
                    sqlDeleteUserByUserId,
                    new Object[]{String.valueOf(userId)});
            LogUtil.i("db execute sql success： " + sqlDeleteUserByUserId);
            return true;
        } catch (Exception ex) {
            LogUtil.e("db execute sql error： " + sqlDeleteUserByUserId, ex);
            return false;
        }
    }

    /**
     * 绑定sql的数据
     */
    public static SQLiteStatement bindData(SQLiteStatement stat, SLUser user) {
        stat.bindString(1, String.valueOf(user.getUserId()));
        stat.bindString(2, user.getHeadUrl());
        stat.bindString(3, user.getNickName());
        stat.bindString(4, String.valueOf(user.getAccountType()));
        stat.bindString(5, String.valueOf(user.getSex()));
        stat.bindString(6, String.valueOf(user.getAge()));
        stat.bindString(7, user.getBigImg());
        stat.bindString(8, String.valueOf(user.getCityCode()));
        stat.bindString(9, user.getCityName());
        stat.bindString(10, String.valueOf(user.getWorkCode()));
        stat.bindString(11, user.getWorkName());
        stat.bindString(12, String.valueOf(user.getEducationCode()));
        stat.bindString(13, user.getEducationName());
        stat.bindString(14, String.valueOf(user.getHouseCode()));
        stat.bindString(15, user.getHouseName());
        stat.bindString(16, String.valueOf(user.getMarriageCode()));
        stat.bindString(17, user.getMarriageName());
        stat.bindString(18, user.getIntroduce());
        stat.bindString(19, user.getRemark());
        stat.bindString(20, String.valueOf(user.getVideoCount()));
        stat.bindString(21, String.valueOf(user.getFollowCount()));
        stat.bindString(22, String.valueOf(user.getFollowedCount()));
        return stat;
    }

    /**
     * 从cursor中获取用户信息
     *
     * @param cursor
     */
    private SLUser parseUserFromCursor(Cursor cursor) {
        SLUser user = new SLUser();
        user.setUserId(cursor.getLong(cursor.getColumnIndexOrThrow("UserId")));
        user.setHeadUrl(cursor.getString(cursor.getColumnIndexOrThrow("HeadUrl")));
        user.setNickName(cursor.getString(cursor.getColumnIndexOrThrow("NickName")));
        user.setAccountType(cursor.getInt(cursor.getColumnIndexOrThrow("AccountType")));
        user.setAge(cursor.getInt(cursor.getColumnIndexOrThrow("Age")));
        user.setSex(cursor.getString(cursor.getColumnIndexOrThrow("Sex")));
        user.setBigImg(cursor.getString(cursor.getColumnIndexOrThrow("BigImg")));
        user.setCityCode(cursor.getInt(cursor.getColumnIndexOrThrow("CityCode")));
        user.setCityName(cursor.getString(cursor.getColumnIndexOrThrow("CityName")));
        user.setWorkCode(cursor.getInt(cursor.getColumnIndexOrThrow("WorkCode")));
        user.setWorkName(cursor.getString(cursor.getColumnIndexOrThrow("WorkName")));
        user.setEducationCode(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("EducationCode"))));
        user.setEducationName(cursor.getString(cursor.getColumnIndexOrThrow("EducationName")));
        user.setHouseCode(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("HouseCode"))));
        user.setHouseName(cursor.getString(cursor.getColumnIndexOrThrow("HouseName")));
        user.setMarriageCode(cursor.getInt(cursor.getColumnIndexOrThrow("MarriageCode")));
        user.setMarriageName(cursor.getString(cursor.getColumnIndexOrThrow("MarriageName")));
        user.setIntroduce(cursor.getString(cursor.getColumnIndexOrThrow("Introduce")));
        user.setRemark(cursor.getString(cursor.getColumnIndexOrThrow("Remark")));
        user.setVideoCount(cursor.getInt(cursor.getColumnIndexOrThrow("VideoCount")));
        user.setFollowCount(cursor.getInt(cursor.getColumnIndexOrThrow("FollowCount")));
        user.setFollowedCount(cursor.getInt(cursor.getColumnIndexOrThrow("FollowedCount")));
        return user;
    }
}
