package com.xywy.askforexpert.module.my.integral;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.MyScoresInfo;

import java.util.List;

/**
 * 我的积分
 *
 * @author 王鹏
 * @2015-10-22上午11:19:42
 */
@SuppressLint("ResourceAsColor")
public class BaseMyScoresAdapter extends BaseAdapter {

    List<MyScoresInfo> list;
    private Context context;
    private LayoutInflater inflater;

    public BaseMyScoresAdapter(Context context, List<MyScoresInfo> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;

    }

    public void setData(List<MyScoresInfo> list) {
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
            convertView = inflater.inflate(R.layout.my_scores_item, null);
            holder.tv_point = (TextView) convertView
                    .findViewById(R.id.tv_point);
            holder.tv_title = (TextView) convertView
                    .findViewById(R.id.tv_title);

            holder.tv_gone_title = (TextView) convertView
                    .findViewById(R.id.tv_gone_title);

            holder.tv_creattime = (TextView) convertView
                    .findViewById(R.id.tv_creattime);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (list.size() > 0) {
            holder.tv_title.setText(list.get(position).getDemo());
            holder.tv_creattime.setText(list.get(position).getCreatetime());
            holder.tv_point.setText(list.get(position).getPoint());

            if ("1".equals(list.get(position).getType())) {
                holder.tv_gone_title.setVisibility(View.VISIBLE);

            } else {
                holder.tv_gone_title.setVisibility(View.GONE);

            }

        }

        return convertView;
    }

    private class ViewHolder {
        TextView tv_point;
        TextView tv_title;
        TextView tv_creattime;
        TextView tv_gone_title;
    }

}
