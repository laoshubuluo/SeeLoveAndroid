package com.tianyu.seelove.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tianyu.seelove.common.Constant;
import com.tianyu.seelove.dao.BibleDao;
import com.tianyu.seelove.utils.AppUtils;
import com.tianyu.seelove.utils.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    private String DB_PATH = Constant.DB_PATH;
    private String DB_NAME = Constant.DB_NAME;
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    private int dbVersion = Constant.DB_BIBLE_VERSION;

    public DataBaseHelper(Context context) {
        super(context, Constant.DB_NAME, null, Constant.DB_BIBLE_VERSION);
        this.myContext = context;
    }

    /**
     * 创建数据库
     *
     * @throws IOException
     */
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            SQLiteDatabase checkDB = null;
            try {
                String myPath = DB_PATH + DB_NAME;
                checkDB = SQLiteDatabase.openDatabase(myPath, null,
                        SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
            int oldVersion = 0;
            if (checkDB != null) {
                oldVersion = checkDB.getVersion();
                checkDB.close();
            }
            if (oldVersion != dbVersion)
                updateDataBase(this.getWritableDatabase(), oldVersion, dbVersion);
        } else {
            Log.v("DataBase Not Exist", ">>>>>>>");
            try {
                this.getReadableDatabase();
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * 检查数据库是否存在
     */
    private boolean checkDataBase() {
        SQLiteDatabase checkDB;
        try {
            String myPath = DB_PATH + DB_NAME;
            File file = new File(myPath);
            if (file.exists()) {
                checkDB = SQLiteDatabase.openDatabase(myPath, null,
                        SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
                if (checkDB != null) {
                    checkDB.close();
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 拷贝数据库
     *
     * @throws IOException
     */
    private void copyDataBase() throws IOException {
        try {
            InputStream myInput = myContext.getAssets().open(DB_NAME);
            String outFileName = DB_PATH + DB_NAME;
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
            SQLiteDatabase checkDB = SQLiteDatabase.openDatabase(outFileName,
                    null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
            checkDB.setVersion(dbVersion);
            checkDB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开数据库
     *
     * @return
     * @throws SQLException
     */
    public SQLiteDatabase openDataBase() throws SQLException {
        try {
            String myPath = DB_PATH + DB_NAME;
            // SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE 解决部分手机在open数据库的时候崩溃问题
            myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myDataBase;
    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /**
     * 升级数据库
     */
    public void updateDataBase(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtil.i("db for bible update begin:oldVersion:" + oldVersion + ",newVersion:" + newVersion);
    }
}
