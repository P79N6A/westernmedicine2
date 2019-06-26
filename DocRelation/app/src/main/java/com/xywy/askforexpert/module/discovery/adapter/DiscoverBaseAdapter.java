package com.xywy.askforexpert.module.discovery.adapter;

import android.content.Context;

import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.adapter.better.YMRVMultiTypeBaseAdapter;
import com.xywy.askforexpert.module.discovery.adapter.discovermain.DiscoverListMedicineDelegate;
import com.xywy.askforexpert.module.discovery.adapter.discovermain.DiscoverServiceOuterDelegate;
import com.xywy.askforexpert.module.discovery.adapter.discovermain.bean.DiscoverItemBean;

/**
 * Created by bailiangjin on 2016/12/28.
 */

public class DiscoverBaseAdapter extends YMRVMultiTypeBaseAdapter<DiscoverItemBean> {
    public DiscoverBaseAdapter(Context context) {
        super(context);
        if(YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)){
            //添加药品助手入口
            addItemViewDelegate(new DiscoverListMedicineDelegate(context));
        }

        addItemViewDelegate(new DiscoverServiceOuterDelegate(context));

    }
}
