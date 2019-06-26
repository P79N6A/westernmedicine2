package com.xywy.askforexpert.module.discovery.medicine.module.medical;

import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.SearchResultEntity;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by xgxg on 17/5/9.
 */

public class SearchHistoryDelegate implements ItemViewDelegate<SearchResultEntity> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_search_history_item;
    }

    @Override
    public boolean isForViewType(SearchResultEntity item, int position) {
        return true;
    }

    @Override
    public void convert(ViewHolder holder, SearchResultEntity entity, int position) {
        if (entity != null) {
            ((TextView) holder.getView(R.id.search_history_title)).setText(entity.getName());
            holder.getConvertView().setTag(entity);
        }
    }
}
