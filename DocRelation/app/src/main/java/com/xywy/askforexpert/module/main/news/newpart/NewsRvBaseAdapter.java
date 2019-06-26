package com.xywy.askforexpert.module.main.news.newpart;

import android.content.Context;

import com.xywy.askforexpert.appcommon.base.adapter.better.YMRVMultiTypeBaseAdapter;
import com.xywy.askforexpert.module.main.news.newpart.bean.MainTabListItemBean;

/**
 * Created by bailiangjin on 2017/1/3.
 */

public class NewsRvBaseAdapter extends YMRVMultiTypeBaseAdapter<MainTabListItemBean> {
    public NewsRvBaseAdapter(Context context) {
        super(context);
        addItemViewDelegate(new NewsListItemDelegate());
        addItemViewDelegate(new NewsListItemMultiPictureDelegate());
        addItemViewDelegate(new MediaEnterListItemDelegate());
        addItemViewDelegate(new MediaRecommendListItemDelegate());
    }
}
