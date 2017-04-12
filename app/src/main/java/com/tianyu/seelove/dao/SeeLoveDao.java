package com.tianyu.seelove.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.tianyu.seelove.database.DataBaseHelper;
import com.tianyu.seelove.model.entity.message.ChatEmoji;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shisheng.zhao
 * @Description: 内置数据库实现类
 * @date 2017-04-12 15:28
 */
public class SeeLoveDao {
    private String sqlSelectEmojiList = "SELECT * FROM emoji";
    private Context context;
    private SQLiteDatabase database = null;

    public SeeLoveDao(Context context) {
        super();
        this.context = context;
    }

    /**
     * 创建一个数据库对象， 用于操作保存在本地的数据库
     * @return 返回一个数据库实例
     */
    private SQLiteDatabase getDataBase() {
        SQLiteDatabase dicData = null;
        DataBaseHelper myDbHelper = new DataBaseHelper(context);
        try {
            myDbHelper.createDataBase();
            dicData = myDbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dicData;
    }

    /**
     * 获取表情列表
     * @return
     */
    public List<ChatEmoji> getDBEmojiList() {
        List<ChatEmoji> list = new ArrayList<ChatEmoji>();
        Cursor cursor = null;
        try {
            SQLiteDatabase database = getDataBase();
            cursor = database.rawQuery(sqlSelectEmojiList, null);
            while (cursor.moveToNext()) {
                list.add(getChatEmojiByCursor(cursor));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    public ChatEmoji getChatEmojiByCursor(Cursor cursor) {
        ChatEmoji chatEmoji = new ChatEmoji();
        chatEmoji.setCharacter(cursor.getString(cursor
                .getColumnIndex("utf8")));
        chatEmoji.setFaceName(cursor.getString(cursor
                .getColumnIndex("filename")));
        chatEmoji.setMapKey(cursor.getString(cursor
                .getColumnIndex("utf16")));
        return chatEmoji;
    }
}
