package com.xywy.askforexpert.module.docotorcirclenew.model;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.xywy.askforexpert.widget.view.ultimaterecycleview.UltimateRecyclerView;

import java.util.List;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/1/23 11:02
 */
public interface IRecycleViewModel<T> extends SwipeRefreshLayout.OnRefreshListener, UltimateRecyclerView.OnLoadMoreListener {
    List<T> getData();

    Object getExtra(Object key);

    void putExtra(Object key, Object value);

    void registerEvent(Object tag);

    View.OnClickListener getItemClickListener();

    void notifyDataChanged();

    void deleteItem(T contentItem);
}
