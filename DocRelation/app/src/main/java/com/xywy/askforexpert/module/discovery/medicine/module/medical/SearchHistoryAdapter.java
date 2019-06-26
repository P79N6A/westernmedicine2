package com.xywy.askforexpert.module.discovery.medicine.module.medical;

import android.content.Context;

import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.SearchResultEntity;
import com.xywy.uilibrary.recyclerview.adapter.XYWYRVMultiTypeBaseAdapter;

/**
 * Created by xgxg on 17/5/9.
 */

public class SearchHistoryAdapter extends XYWYRVMultiTypeBaseAdapter<SearchResultEntity> {
    public SearchHistoryAdapter(Context context) {
        super(context);
        addItemViewDelegate(new SearchHistoryDelegate());
    }
}
