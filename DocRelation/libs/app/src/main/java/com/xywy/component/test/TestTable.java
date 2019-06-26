package com.xywy.component.test;

import com.xywy.component.datarequest.database.BaseTableMode;

/**
 * Created by shijiazi on 15/12/26.
 * 测试表结构
 */
public class TestTable extends BaseTableMode {

    public static final String TEST_TABLE_NAME = "test";

    public static final String TEST_COLUMN_TIME = "time";

    public static final String TEST_COLUMN_UID = "uid";

    @Override
    public String createTableSql() {
        return "create table if not exists " + TEST_TABLE_NAME + "(" + TEST_COLUMN_TIME
                + " INTEGER PRIMARY KEY ON CONFLICT REPLACE," + TEST_COLUMN_UID + " INTEGER"
                + ");";
    }

    @Override
    public String deleteTableSql() {
        return "drop table if exists " + TEST_TABLE_NAME;
    }

    @Override
    public String[] getFieldAll() {
        return new String[]{TEST_COLUMN_TIME, TEST_COLUMN_UID};
    }

    @Override
    public String getTableName() {
        return TEST_TABLE_NAME;
    }

}
