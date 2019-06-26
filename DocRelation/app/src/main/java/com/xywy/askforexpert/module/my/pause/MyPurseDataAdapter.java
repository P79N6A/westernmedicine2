package com.xywy.askforexpert.module.my.pause;

import android.content.Context;

import com.xywy.askforexpert.appcommon.base.adapter.better.YMRVMultiTypeBaseAdapter;
import com.xywy.askforexpert.model.MyPurse.MyPurseItemBean;

/**
 *  Created by xgxg on 2018/6/20.
 */
public class MyPurseDataAdapter extends YMRVMultiTypeBaseAdapter<MyPurseItemBean> {
    public MyPurseDataAdapter(Context context) {
        super(context);
        addItemViewDelegate(new MyPurseRoomServiceOuterDelegate(context));
        addItemViewDelegate(new MyPurseRoomServiceMoreDelegate(context));
        addItemViewDelegate(new MyPurseHalfYearAnalysisDelegate(context));
    }
}
