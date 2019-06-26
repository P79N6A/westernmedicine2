package com.xywy.askforexpert.appcommon.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xywy.askforexpert.appcommon.utils.others.DLog;

/**
 * 数据库创建类
 *
 * @author SHR
 * @2015-5-5上午9:58:26
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "database.db";// 数据库名称
    public static final int VERSION = 2;
    public static final String TABLE_SERVICE = "service";// 数据表
    public static final String ID = "id";//
    public static final String NAME = "name";
    public static final String ORDERID = "orderId";
    public static final String SELECTED = "selected";
    public static final String DESCRIPTION = "description";
    public static final String IMGPATH = "imgPath";
    public static final String MOREPATH = "morePath";

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO 创建数据库后，对数据库的操作
        DLog.i("db", "创建数据库表");
        String sql = "create table if not exists " + TABLE_SERVICE + "(" + ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " varchar(500) , "
                + ORDERID + " INTEGER , " + SELECTED + " DEFAULT 1," + IMGPATH
                + " INTEGER , " + MOREPATH + " INTEGER , " + DESCRIPTION
                + " varchar(500))";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO 更改数据库版本的操作
        onCreate(db);
    }

}
