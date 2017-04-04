package com.tianyu.seelove.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.tianyu.seelove.common.Constant;
import com.tianyu.seelove.dao.UserDao;
import com.tianyu.seelove.manager.DbConnectionManager;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.model.enums.SexType;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.utils.LogUtil;
import com.tianyu.seelove.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shisheng.zhao
 * @Description: 用户数据访问层实现类
 * @date 2015-09-07 下午15:00:24
 */
public class UserDaoImpl implements UserDao {
    // 插入用户数据
    public final static String sqlInsertUserInfo = "insert into userinfo(uid,nickname,noteName,amenId,sex,province,city,country,headUrl,phoneNum,birthday,marriage,age,level,signa,countryCode,status,km,timeAgo,dateTime,timestamp,uTime,remark,dnd,provinceId,cityId) " +
            "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    @Override
    public boolean add(List<SLUser> userList) {
        return false;
    }

    @Override
    public boolean add(SLUser user, String status) {
        return false;
    }

    @Override
    public boolean addIfNotExist(SLUser user) {
        return false;
    }

    @Override
    public boolean deleteOldFriendData(long timestampNew) {
        return false;
    }

    @Override
    public String getUserStatusById(long userId) {
        return null;
    }

    /**
     * 执行插入动作，将用户插入数据库
     *
     * @param user
     * @return
     */
    private boolean insert(SLUser user) {
        if (user == null)
            return false;
        try {
            DbConnectionManager.getInstance().getConnection().execSQL(
                    sqlInsertUserInfo,
                    new String[]{String.valueOf(user.getUserId()),
                            user.getNickName(), user.getHeadUrl(),
                            String.valueOf(user.getAccountType()), SexType.parseByMsg(user.getSex()).getResultCode(),
                            String.valueOf(user.getAge()), user.getBgImg(),
                            String.valueOf(user.getCityCode()), user.getCityName(),
                            String.valueOf(user.getWorkCode()), user.getWorkName(),
                            String.valueOf(user.getEducationCode()),
                            user.getEducationName(),
                            String.valueOf(user.getHouseCode()),
                            user.getHouseName(), String.valueOf(user.getMarriageCode()),
                            user.getMarriageName(), user.getIntroduce(),
                            user.getRemark()});
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
    public boolean updateUser(SLUser user) {
        if (user == null || StringUtils.isNullOrBlank(user.getNickName()))
            return false;
        String updateSql = "update userinfo set " +
                "uid,nickname,noteName,amenId,sex=?,province=?,city=?,country=?,headUrl=?,phoneNum=?,level=?," +
                "signa=?,countryCode=?,status=?,km=?,timeAgo=?,dateTime=?,timestamp=?,uTime=?,remark=?,dnd=?,provinceId,cityId where uid=? ";
        try {
            DbConnectionManager.getInstance().getConnection().execSQL(
                    updateSql,
                    new String[]{String.valueOf(user.getUserId()),
                            user.getNickName(), user.getHeadUrl(),
                            String.valueOf(user.getAccountType()), SexType.parseByMsg(user.getSex()).getResultCode(),
                            String.valueOf(user.getAge()), user.getBgImg(),
                            String.valueOf(user.getCityCode()), user.getCityName(),
                            String.valueOf(user.getWorkCode()), user.getWorkName(),
                            String.valueOf(user.getEducationCode()),
                            user.getEducationName(),
                            String.valueOf(user.getHouseCode()),
                            user.getHouseName(), String.valueOf(user.getMarriageCode()),
                            user.getMarriageName(), user.getIntroduce(),
                            user.getRemark()});
            LogUtil.i("db execute sql success： " + updateSql);
            return true;
        } catch (Exception ex) {
            LogUtil.e("db execute sql error： " + updateSql, ex);
            return false;
        }
    }

    /**
     * update User
     *
     * @param uid
     * @return
     */
    @Override
    public boolean updateUserHeadByUid(String uid, String headUrl) {
        if (StringUtils.isNullOrBlank(uid))
            return false;
        String updateSql = "update userinfo set headUrl=? where uid=?";
        try {
            DbConnectionManager.getInstance().getConnection().execSQL(
                    updateSql,
                    new String[]{headUrl, uid});
            LogUtil.i("db execute sql success： " + updateSql);
            return true;
        } catch (Exception ex) {
            LogUtil.e("db execute sql error： " + updateSql, ex);
            return false;
        }
    }


    /**
     * 根据status更新用户信仰等级
     *
     * @param status User.STATUS_*
     * @return
     */
    @Override
    public boolean updateFaithLevelByStatus(String status, int level) {
        String updateSql = "update userinfo set level=? where status=?";
        try {
            DbConnectionManager.getInstance().getConnection().execSQL(
                    updateSql,
                    new String[]{String.valueOf(level), status});
            LogUtil.i("db execute sql success： " + updateSql);
            return true;
        } catch (Exception ex) {
            LogUtil.e("db execute sql error： " + updateSql, ex);
            return false;
        }
    }

    /**
     * 根据uid更新用户状态
     *
     * @param status User.STATUS_*
     * @return
     */
    @Override
    public boolean updateStatusByUid(String uid, String status) {
        String updateSql = "update userinfo set status=? where uid=?";
        try {
            DbConnectionManager.getInstance().getConnection().execSQL(
                    updateSql,
                    new String[]{status, uid});
            LogUtil.i("db execute sql success： " + updateSql);
            return true;
        } catch (Exception ex) {
            LogUtil.e("db execute sql error： " + updateSql, ex);
            return false;
        }
    }

    /**
     * 根据uid更新免打扰状态
     *
     * @param dnd 免打扰状态
     * @return
     */
    @Override
    public boolean updateBnbByUid(String uid, String dnd) {
        String updateSql = "update userinfo set dnd=? where uid=?";
        try {
            DbConnectionManager.getInstance().getConnection().execSQL(
                    updateSql,
                    new String[]{dnd, uid});
            LogUtil.i("db execute sql success： " + updateSql);
            return true;
        } catch (Exception ex) {
            LogUtil.e("db execute sql error： " + updateSql, ex);
            return false;
        }
    }

    /**
     * 根据手机号查询用户是否存在
     *
     * @param phoneNo
     * @return
     */
    @Override
    public SLUser getUserByPhoneNo(String phoneNo) {
        String selectSql = "select * from userinfo where phoneNum=?";
        Cursor cursor = null;
        try {
            cursor = DbConnectionManager.getInstance().getConnection().rawQuery(selectSql, new String[]{phoneNo});
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                return parseUserFromCursor(cursor);
            }
        } catch (Exception ex) {
            LogUtil.e("db execute sql error： " + selectSql, ex);
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }


    /**
     * 根据状态查询用户
     *
     * @param status User.STATUS_*
     * @return
     */
    @Override
    public List<SLUser> getUserByStatus(String status) {
        String selectSql = "select * from userinfo where status=? order by uid";
        Cursor cursor = null;
        List<SLUser> userList = new ArrayList<SLUser>();
        try {
            cursor = DbConnectionManager.getInstance().getConnection().rawQuery(selectSql, new String[]{status});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
                userList.add(parseUserFromCursor(cursor));
            LogUtil.i("db execute sql success： " + selectSql + ", count: " + userList.size());
            return userList;
        } catch (Exception ex) {
            LogUtil.e("db execute sql error： " + selectSql, ex);
            return userList;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    /**
     * 获取好友列表
     *
     * @param
     * @return
     */
    @Override
    public List<SLUser> getFriendList() {
        String selectSql = "select * from userinfo where status in (?,?,?)";
        Cursor cursor = null;
        List<SLUser> userList = new ArrayList<SLUser>();
        try {
            cursor = DbConnectionManager.getInstance().getConnection().rawQuery(selectSql, new String[]{});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
                userList.add(parseUserFromCursor(cursor));
            LogUtil.i("db execute sql success： " + selectSql + ", count: " + userList.size());
            return userList;
        } catch (Exception ex) {
            LogUtil.e("db execute sql error： " + selectSql, ex);
            return userList;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    /**
     * 获取所有
     *
     * @param
     * @return
     */
    @Override
    public List<SLUser> getAll() {
        String selectSql = "select * from userinfo where uid!=?";
        Cursor cursor = null;
        List<SLUser> userList = new ArrayList<SLUser>();
        try {
            cursor = DbConnectionManager.getInstance().getConnection().rawQuery(selectSql, new String[]{""});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
                userList.add(parseUserFromCursor(cursor));
            LogUtil.i("db execute sql success： " + selectSql + ", count: " + userList.size());
            return userList;
        } catch (Exception ex) {
            LogUtil.e("db execute sql error： " + selectSql, ex);
            return userList;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    /**
     * 根据uid获取User
     *
     * @param uid
     * @return
     */
    @Override
    public SLUser getUserByUid(String uid) {
        String selectSql = "select * from userinfo where uid=?";
        Cursor cursor = null;
        SLUser user = new SLUser();
        try {
            cursor = DbConnectionManager.getInstance().getConnection().rawQuery(selectSql, new String[]{uid});
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                return parseUserFromCursor(cursor);
            } else
                return user;
        } catch (Throwable ex) {
            LogUtil.e("db execute sql error： " + selectSql, ex);
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

    public boolean deleteUser(long userId) {
        String deleteSql = "delete from userinfo where uid=?";
        try {
            DbConnectionManager.getInstance().getConnection().execSQL(
                    deleteSql,
                    new Object[]{String.valueOf(userId)});
            LogUtil.i("db execute sql success： " + deleteSql);
            return true;
        } catch (Exception ex) {
            LogUtil.e("db execute sql error： " + deleteSql, ex);
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
        stat.bindString(5, SexType.parseByMsg(user.getSex()).getResultCode());
        stat.bindString(6, String.valueOf(user.getAge()));
        stat.bindString(7, user.getBgImg());
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
        return stat;
    }

    /**
     * 从cursor中获取用户信息
     *
     * @param cursor
     */
    private SLUser parseUserFromCursor(Cursor cursor) {
        SLUser user = new SLUser();
        user.setUserId(cursor.getLong(cursor.getColumnIndexOrThrow("uid")));
        user.setHeadUrl(cursor.getString(cursor.getColumnIndexOrThrow("nickname")));
        user.setNickName(cursor.getString(cursor.getColumnIndexOrThrow("noteName")));
        user.setAccountType(cursor.getInt(cursor.getColumnIndexOrThrow("amenId")));
        user.setSex(cursor.getString(cursor.getColumnIndexOrThrow("sex")));
        user.setAge(cursor.getInt(cursor.getColumnIndexOrThrow("province")));
        user.setBgImg(cursor.getString(cursor.getColumnIndexOrThrow("city")));
        user.setCityCode(cursor.getInt(cursor.getColumnIndexOrThrow("country")));
        user.setCityName(cursor.getString(cursor.getColumnIndexOrThrow("headUrl")));
        user.setWorkCode(cursor.getInt(cursor.getColumnIndexOrThrow("phoneNum")));
        user.setWorkName(cursor.getString(cursor.getColumnIndexOrThrow("birthday")));
        user.setEducationCode(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("marriage"))));
        user.setEducationName(cursor.getString(cursor.getColumnIndexOrThrow("age")));
        user.setHouseCode(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("level"))));
        user.setHouseName(cursor.getString(cursor.getColumnIndexOrThrow("signa")));
        user.setMarriageCode(cursor.getInt(cursor.getColumnIndexOrThrow("countryCode")));
        user.setMarriageName(cursor.getString(cursor.getColumnIndexOrThrow("status")));
        user.setIntroduce(cursor.getString(cursor.getColumnIndexOrThrow("km")));
        user.setRemark(cursor.getString(cursor.getColumnIndexOrThrow("timeAgo")));
        return user;
    }
}
