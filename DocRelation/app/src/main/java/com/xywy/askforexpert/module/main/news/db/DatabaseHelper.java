package com.xywy.askforexpert.module.main.news.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/1/14 16:09
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "news_data.db";
    public static final int VERSION = 1;

    // news list data
    public static final String NEWS_TABLE_NAME = "news_list";
    public static final String NEWS_COLUMN_ID = "_id";
    public static final String NEWS_TITLE = "title";
    public static final String NEWS_SUMMARY = "summary";
    public static final String NEWS_IMAGE = "image";
    public static final String NEWS_CREATE_TIME = "create_time";
    public static final String NEWS_READ_COUNT = "read_count";

    public static final String NEWS_LIST_TABLE_CREATE = "CREATE TABLE " + NEWS_TABLE_NAME
            + "(" + NEWS_COLUMN_ID + "INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NEWS_TITLE + " TEXT NOT NULL, "
            + NEWS_SUMMARY + " TEXT NOT NULL, "
            + NEWS_IMAGE + " TEXT, "
            + NEWS_CREATE_TIME + " TEXT NOT NULL, "
            + NEWS_READ_COUNT + " TEXT);";

    // read news data
    public static final String READ_NEWS_TABLE_NAME = "read_news_list";
    public static final String READ_COLUMN_ID = "_id";
    public static final String READ_NEWS_ID = "read_news_id";

    public static final String READ_TABLE_CREATE = "CREATE TABLE " + READ_NEWS_TABLE_NAME
            + "(" + READ_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + READ_NEWS_ID + " CHAR(256) UNIQUE);";

    private volatile static DatabaseHelper mDBHelper;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (mDBHelper == null) {
            synchronized (DatabaseHelper.class) {
                if (mDBHelper == null) {
                    mDBHelper = new DatabaseHelper(context);
                }
            }
        }

        return mDBHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(NEWS_LIST_TABLE_CREATE);
        db.execSQL(READ_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + NEWS_LIST_TABLE_CREATE);
        db.execSQL("DROP TABLE IF EXISTS " + READ_NEWS_TABLE_NAME);

        onCreate(db);
    }
}
