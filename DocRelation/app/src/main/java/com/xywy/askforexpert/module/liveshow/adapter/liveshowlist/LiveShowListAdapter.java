package com.xywy.askforexpert.module.liveshow.adapter.liveshowlist;

import android.content.Context;

import com.xywy.askforexpert.appcommon.base.adapter.better.YMRVMultiTypeBaseAdapter;

/**
 *  直播列表Adapter
 * Created by bailiangjin on 2017/3/4.
 */

public class LiveShowListAdapter extends YMRVMultiTypeBaseAdapter<LiveShowItem>{
    public LiveShowListAdapter(Context context) {
        super(context);
        addItemViewDelegate(new BigItemDelegate());
        addItemViewDelegate(new FiveItemDelegate());
    }

}
