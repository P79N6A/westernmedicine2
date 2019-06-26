package com.xywy.askforexpert.appcommon.base;

import android.app.Activity;

import com.xywy.askforexpert.appcommon.base.superbase.SuperBaseAdapter;

import java.util.List;

/**
 *
 * 已废弃 不建议使用 listview，建议使用 RecyclerView 配合 YMRVSingleTypeBaseAdapter 实现相应效果
 * 如必须使用ListView时 可使用该Adapter
 * Created by bailiangjin on 16/8/8.
 *
 */
@Deprecated
public abstract class YMBaseAdapter<T> extends SuperBaseAdapter<T> {
    public YMBaseAdapter(Activity activity, List dataList) {
        super(activity, dataList);
    }

}
