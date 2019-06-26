package com.xywy.askforexpert.appcommon.base;

import android.os.Bundle;

/**
 *
 * YMBaseFragment 使用示例
 * Created by bailiangjin on 2016/10/24.
 */

public class DemoFragment extends YMBaseFragment {
    @Override
    protected int getLayoutResId() {
        //TODO:返回 fragment layout resId
        return 0;
    }

    @Override
    protected void initView() {
        //TODO:View初始化

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        //TODO:数据初始化
    }

    @Override
    public String getStatisticalPageName() {
        return null;
    }
}
