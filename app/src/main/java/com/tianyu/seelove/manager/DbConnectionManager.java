package com.tianyu.seelove.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.tianyu.seelove.database.DbOpenHelper;
import com.tianyu.seelove.utils.LogUtil;

/**
 * 数据库连接管理 负责监听和管理数据库连接
 * @author shisheng.zhao
 * @date 2015-09-07 18:56:43
 */
public class DbConnectionManager {
    private static DbConnectionManager dbConnectionManager = new DbConnectionManager();
    private static Context context;
    private static SQLiteDatabase database;

    private DbConnectionManager() {
    }

    public static DbConnectionManager getInstance() {
        return dbConnectionManager;
    }

    public void reload() {
        DbOpenHelper.reload();
        DbOpenHelper.getInstance(DbConnectionManager.context);
    }

    public void reset() {
        DbOpenHelper.reset();
    }

    public static void init(Context context) {
        DbConnectionManager.context = context;
        DbOpenHelper.getInstance(DbConnectionManager.context);
    }

    public synchronized SQLiteDatabase getConnection() {
        try {
            if (database != null && database.isOpen()) {
                return database;
            } else {
                DbOpenHelper.reload();
                database = DbOpenHelper
                        .getInstance(DbConnectionManager.context)
                        .getWritableDatabase();
                return database;
            }
        } catch (Exception ex) {
            LogUtil.e("获取数据库连接失败", ex);
            ex.printStackTrace();
            try {
                DbOpenHelper.reload();
                DbOpenHelper.getInstance(DbConnectionManager.context);
                return DbOpenHelper.getInstance(context).getWritableDatabase();
            } catch (Exception exception) {
                LogUtil.e("获取数据库连接失败", exception);
                exception.printStackTrace();
                return null;
            }
        }
    }
}
