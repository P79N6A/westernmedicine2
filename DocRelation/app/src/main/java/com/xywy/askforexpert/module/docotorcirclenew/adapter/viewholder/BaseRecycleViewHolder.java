package com.xywy.askforexpert.module.docotorcirclenew.adapter.viewholder;

import android.content.Context;
import android.view.View;

import com.xywy.askforexpert.widget.view.ultimaterecycleview.UltimateRecyclerviewViewHolder;

import java.lang.reflect.Field;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/11/16 17:30
 * 一个列表中存在多种Item类型时，创建多种DataBinder进行数据绑定
 */

public  class BaseRecycleViewHolder<T> extends UltimateRecyclerviewViewHolder<T>{

    public BaseRecycleViewHolder(View rootView) {
        super(rootView);
    }
    public void updateView(Context context, T item) {
        itemView.setVisibility(View.VISIBLE);
        itemView.setTag(item);
        try {
            setTagForSubView(item);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void setTagForSubView(T item) throws IllegalAccessException {
        Field[] views=getClass().getDeclaredFields();
        for (Field view:views){
            view.setAccessible(true);
            Object value=view.get(this);
            if (value!=null&&value instanceof View){
                ((View) value).setTag(item);
            }
        }
    }

    public void dismiss(){
        itemView.setVisibility(View.GONE);
    }
}
