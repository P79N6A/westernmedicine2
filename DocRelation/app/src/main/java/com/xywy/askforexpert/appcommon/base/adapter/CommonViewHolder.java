package com.xywy.askforexpert.appcommon.base.adapter;

import android.util.SparseArray;
import android.view.View;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2015/11/27 10:57
 */
@Deprecated
public class CommonViewHolder {
    private CommonViewHolder() {
        throw new AssertionError();
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T getView(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<>();
            view.setTag(viewHolder);
        }

        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }

        return (T) childView;
    }
}
