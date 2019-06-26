package com.xywy.askforexpert.module.main.news.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/1/14 16:07
 */

/**
 * 已读新闻记录
 */
public class NewsReadDataSource extends BaseDataSource {
    private SQLiteDatabase database;
    private String[] allColumns = {DatabaseHelper.READ_COLUMN_ID, DatabaseHelper.READ_NEWS_ID};

    public NewsReadDataSource(DatabaseHelper databaseHelper) {
        database = databaseHelper.getWritableDatabase();
    }

    private void insert(String newsId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.READ_NEWS_ID, newsId);
        database.insert(DatabaseHelper.READ_NEWS_TABLE_NAME, null, contentValues);
    }

    /**
     * 设置为已读
     */
    public boolean readNews(String newsId) {
        if (TextUtils.isEmpty(newsId)) {
            return false;
        }

        if (!isNewsRead(newsId)) {
            insert(newsId);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断新闻是否已读
     */
    private boolean isNewsRead(String newsId) {
        boolean result = false;

        Cursor cursor = database.query(DatabaseHelper.READ_NEWS_TABLE_NAME, allColumns,
                DatabaseHelper.READ_NEWS_ID + " = '" + newsId + "'",
                null, null, null, null);

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            result = true;
        }

        if (cursor != null) {
            cursor.close();
        }

        return result;
    }


    /**
     * 获取所有已读新闻
     */
    public List<String> getAllReadNews() {
        ArrayList<String> readNewsList = new ArrayList<>();

        Cursor cursor = database.query(DatabaseHelper.READ_NEWS_TABLE_NAME, allColumns, null, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String newsID = cursor.getString(cursor.getColumnIndex(DatabaseHelper.READ_NEWS_ID));
                readNewsList.add(newsID);

                cursor.moveToNext();
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return readNewsList;
    }
}
