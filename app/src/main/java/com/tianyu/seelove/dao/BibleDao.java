package com.tianyu.seelove.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tianyu.seelove.database.DataBaseHelper;
import com.tianyu.seelove.model.entity.message.ChatEmoji;

import java.util.ArrayList;
import java.util.List;

public class BibleDao {
    private Context context;
    private SQLiteDatabase database = null;

    public BibleDao(Context context) {
        super();
        this.context = context;
    }

    /**
     * 创建一个数据库对象， 用于操作保存在本地的数据库
     *
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
     *
     * @return
     */
    public List<ChatEmoji> getDBEmojiList() {
        List<ChatEmoji> list = new ArrayList<ChatEmoji>();
        Cursor cursor = null;
        try {
            SQLiteDatabase database = getDataBase();
            String sql = "select * from ios_emoji";
            cursor = database.rawQuery(sql, null);
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
