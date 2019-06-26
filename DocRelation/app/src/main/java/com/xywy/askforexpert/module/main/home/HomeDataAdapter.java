package com.xywy.askforexpert.module.main.home;

import android.content.Context;

import com.xywy.askforexpert.appcommon.base.adapter.better.YMRVMultiTypeBaseAdapter;
import com.xywy.askforexpert.model.home.HomeItemBean;

/**
 *  Created by xgxg on 2018/4/4.
 */
public class HomeDataAdapter extends YMRVMultiTypeBaseAdapter<HomeItemBean> {
    public HomeDataAdapter(Context context) {
        super(context);
        addItemViewDelegate(new RoomServiceOuterDelegate(context));
        addItemViewDelegate(new RoomServiceMoreDelegate(context));
        addItemViewDelegate(new RewardListDelegate(context));
    }
}
