package com.xywy.askforexpert.module.main.service.service;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.adapter.CommonBaseAdapter;
import com.xywy.askforexpert.appcommon.base.adapter.CommonViewHolder;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;

import java.util.List;
import java.util.Map;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/1/5 13:19
 */
public class MyServicesGridAdapter extends CommonBaseAdapter<Integer> {
    public MyServicesGridAdapter(Context mContext, List<Integer> mDatas, int mItemLayoutId) {
        super(mContext, mDatas, mItemLayoutId);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(mItemLayoutId, parent, false);
        }

        RelativeLayout itemLayout = CommonViewHolder.getView(convertView, R.id.grid_item);
        TextView itemText = CommonViewHolder.getView(convertView, R.id.grid_item_text);

        Integer id = mDatas.get(position);
        Map<String, Object> serviceInfo = CommonUtils.getServiceInfo(id);

        if (serviceInfo != null) {
            itemLayout.setBackgroundColor(Color.parseColor((String) serviceInfo.get("bgColor")));
            itemText.setText((String) serviceInfo.get("name"));
            itemText.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable((Integer) serviceInfo.get("icon")),
                    null, null, null);
        }

        return convertView;
    }
}
