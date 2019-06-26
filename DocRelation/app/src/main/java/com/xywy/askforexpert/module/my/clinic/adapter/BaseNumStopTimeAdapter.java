package com.xywy.askforexpert.module.my.clinic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.NumStopTimeListInfo;

import java.util.List;

/**
 * 停诊服务时间
 *
 * @author 王鹏
 * @2015-5-29上午8:59:48
 */
@SuppressLint("ResourceAsColor")
public class BaseNumStopTimeAdapter extends BaseAdapter {

    public SparseBooleanArray selectionMap;
    List<NumStopTimeListInfo> list;
    private Context context;
    private LayoutInflater inflater;

    public BaseNumStopTimeAdapter(Context context) {
        this.context = context;

        inflater = LayoutInflater.from(context);
    }

    public void setData(List<NumStopTimeListInfo> list) {
        this.list = list;
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
            convertView = inflater.inflate(R.layout.num_stop_time_list_item,
                    null);
            holder.tv_stop_time = (TextView) convertView.findViewById(R.id.tv_stop_time);
            holder.tv_stop_info = (TextView) convertView.findViewById(R.id.tv_stop_info);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (list != null) {
            holder.tv_stop_time.setText(list.get(position).getStartdate() + "-" + list.get(position).getEnddate());
            holder.tv_stop_info.setText(list.get(position).getReason());
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
        TextView tv_stop_time;
        TextView tv_stop_info;
        RelativeLayout re_item;
    }
}
