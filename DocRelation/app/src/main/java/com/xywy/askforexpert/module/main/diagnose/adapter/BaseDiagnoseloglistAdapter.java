package com.xywy.askforexpert.module.main.diagnose.adapter;

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
import com.xywy.askforexpert.model.DiagnoseLogListInfo;

import java.util.List;

/**
 * 学校城市 适配器
 *
 * @author 王鹏
 * @2015-5-7下午5:48:59
 */
@SuppressLint("ResourceAsColor")
public class BaseDiagnoseloglistAdapter extends BaseAdapter {

    public SparseBooleanArray selectionMap;
    List<DiagnoseLogListInfo> list;
    private Context context;
    private LayoutInflater inflater;

    public BaseDiagnoseloglistAdapter(Context context) {
        this.context = context;

        inflater = LayoutInflater.from(context);
    }

    public void setData(List<DiagnoseLogListInfo> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.diagnose_log_list_item, null);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
//			holder.re_item = (RelativeLayout) convertView
//					.findViewById(R.id.re_item);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (list != null) {

            holder.tv_date.setText(list.get(position).getRealname());
            holder.tv_title.setText(list.get(position).getCotent());
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
        TextView tv_title;
        RelativeLayout re_item;
        TextView tv_date;
    }
}
