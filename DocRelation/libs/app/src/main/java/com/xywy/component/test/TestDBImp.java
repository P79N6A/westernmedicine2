package com.xywy.component.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.xywy.component.datarequest.database.UriFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijiazi on 15/12/26.
 */
public final class TestDBImp {
    private static TestDBImp instance = new TestDBImp();

    private TestDBImp() {
    }

    public static TestDBImp getInstance() {
        return instance;
    }

    public List<TestModel> queryAll(Context context) {
        Cursor cursor = context.getContentResolver().query(
                UriFactory.getUri(TestSQLProvider.class, TestTable.class), null, null,
                null, null);
        if (cursor == null) {
            return null;
        }
        return parseTestQueryAll(cursor);
    }

    public void save(Context context, TestModel model) {
        if (model == null) {
            return;
        }
        ContentValues contactsInfo = buildContentValue(model);
        context.getContentResolver()
                .insert(UriFactory.getUri(TestSQLProvider.class, TestTable.class),
                        contactsInfo);
    }

    public void save(Context context, List<TestModel> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        final int count = list.size();
        ContentValues[] contentValues = new ContentValues[count];
        for (int i = 0; i < count; i++) {
            contentValues[i] = buildContentValue(list.get(i));
        }
        context.getContentResolver().bulkInsert(
                UriFactory.getUri(TestSQLProvider.class, TestTable.class),
                contentValues);
        synchronized (list) {
            if (list != null) {
                list.clear();
            }
        }
    }

    public void deleteAll(Context context) {
        context.getContentResolver().delete(
                UriFactory.getUri(TestSQLProvider.class, TestTable.class), null, null);
    }

    public void deleteByTime(Context context, long time) {
        context.getContentResolver().delete(
                UriFactory.getUri(TestSQLProvider.class, TestTable.class),
                TestTable.TEST_COLUMN_TIME + "=?", new String[]{Long.toString(time)});
    }

    public int getCount(Context context) {
        Cursor cursor = context.getContentResolver().query(
                UriFactory.getUri(TestSQLProvider.class, TestTable.class),
                new String[]{"count(*) AS count"}, null, null, null);
        if (cursor == null) {
            return 0;
        }
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    private ContentValues buildContentValue(TestModel mode) {
        long time = mode.getTime();
        long uid = mode.getUid();
        ContentValues contactsInfo = new ContentValues();
        contactsInfo.put(TestTable.TEST_COLUMN_TIME, time);
        contactsInfo.put(TestTable.TEST_COLUMN_UID, uid);
        return contactsInfo;
    }

    private List<TestModel> parseTestQueryAll(Cursor cursor) {
        final int count = cursor.getCount();
        List<TestModel> list = new ArrayList<>(count);
        TestModel data = null;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            data = new TestModel();
            data.setTime(cursor.getLong(cursor
                    .getColumnIndexOrThrow(TestTable.TEST_COLUMN_TIME)));
            data.setUid(cursor.getLong(cursor
                    .getColumnIndexOrThrow(TestTable.TEST_COLUMN_UID)));
            list.add(data);
        }
        cursor.close();
        return list;
    }
}
