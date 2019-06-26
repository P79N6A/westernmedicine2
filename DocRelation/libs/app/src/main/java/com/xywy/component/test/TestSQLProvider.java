package com.xywy.component.test;

import com.xywy.component.datarequest.database.BaseContentProvider;
import com.xywy.component.datarequest.database.BaseContentProviderNode;
import com.xywy.component.datarequest.database.BaseTableMode;

import java.util.List;

/**
 * Created by shijiazi on 15/12/26.
 */
@BaseContentProviderNode(authorities = "com.xywy.component.datarequest.testsql")
public class TestSQLProvider extends BaseContentProvider {

    public static final String TEST_DB_NAME = "test_db";

    @Override
    public String getDBName() {
        return TEST_DB_NAME;
    }

    @Override
    protected List<Class<? extends BaseTableMode>> onTableCreateRegist(
            List<Class<? extends BaseTableMode>> classTypes) {
        classTypes.add(TestTable.class);
        return classTypes;
    }
}
