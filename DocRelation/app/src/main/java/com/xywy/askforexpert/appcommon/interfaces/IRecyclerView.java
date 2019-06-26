package com.xywy.askforexpert.appcommon.interfaces;

import android.view.View;

/**
 * Compiler: Android Studio
 * Project: EndlessRecyclerView
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/6/16 17:49
 */
public interface IRecyclerView {

    interface OnLoadMoreListener {
        void onLoadMore();
    }

    interface OnItemClickListener {
        void OnItemClick(View view);
    }

    interface OnItemLongClickListener {
        void OnItemLongClick(View view);
    }

}
