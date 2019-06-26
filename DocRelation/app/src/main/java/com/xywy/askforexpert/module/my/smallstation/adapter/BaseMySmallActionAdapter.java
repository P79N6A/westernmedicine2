package com.xywy.askforexpert.module.my.smallstation.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.MySmallActionInfo;

import java.util.List;

/**
 * 学校城市 适配器
 *
 * @author 王鹏
 * @2015-5-7下午5:48:59
 */
@SuppressLint("ResourceAsColor")
public class BaseMySmallActionAdapter extends BaseAdapter {

    public SparseBooleanArray selectionMap;
    List<MySmallActionInfo> list;
    private Context context;
    private LayoutInflater inflater;

    public BaseMySmallActionAdapter(Context context,
                                    List<MySmallActionInfo> list) {
        this.context = context;
        this.list = list;
        if (context != null) {
            inflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.small_action_item, null);
            holder.tv_content = (TextView) convertView
                    .findViewById(R.id.tv_content);
            holder.tv_vip_name = (TextView) convertView
                    .findViewById(R.id.tv_vip_name);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.rating = (RatingBar) convertView
                    .findViewById(R.id.ratingbar);
            holder.tv_points = (TextView) convertView
                    .findViewById(R.id.tv_points);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (list != null) {
//            holder.tv_content.setText(list.get(position).getG_cons());
//            holder.tv_date.setText(list.get(position).getG_date());
//            holder.tv_vip_name.setText(list.get(position).getG_uid());
//            holder.tv_points.setText(list.get(position).getG_stat() + "分");
//            holder.rating.setClickable(false);
//            float start = Float.parseFloat(list.get(position).getG_stat());
//            holder.rating.setRating(start);

        }
        return convertView;
    }

    public void init() {
        for (int i = 0; i < list.size(); i++) {
            selectionMap = new SparseBooleanArray();
            selectionMap.put(i, false);
        }
    }

    private class ViewHolder {
        TextView textView;
        RelativeLayout re_item;
        TextView tv_content;
        RatingBar rating;
        TextView tv_vip_name;
        TextView tv_date;
        TextView tv_points;
    }
}
