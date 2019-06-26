package com.xywy.askforexpert.module.main.service.que.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.QueData;

import java.util.List;

/**
 * 项目名称：D_Platform
 * 类名称：QueDpartAdapter
 * 类描述：科室选择适配器
 * 创建人：shihao
 * 创建时间：2015-6-24 下午3:52:56
 * 修改备注：
 */
public class QueDpartAdapter extends BaseAdapter {

    private Context context;

    private List<QueData> queDatas;

    private int currentPos;

    private int type;

    public QueDpartAdapter(Context context, int type) {
        this.context = context;
        this.type = type;
    }

    public void bindData(List<QueData> queDatas) {
        this.queDatas = queDatas;
        notifyDataSetChanged();
    }

    public void isSelector(int position) {
        this.currentPos = position;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return queDatas.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return queDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (type == 1) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.baseschool_item, null);
                holder.textView = (TextView) convertView
                        .findViewById(R.id.tv_text);
                holder.re_item = (RelativeLayout) convertView
                        .findViewById(R.id.re_item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView.setText(queDatas.get(position).getName());

            if (currentPos == position) {
                holder.re_item.setBackgroundColor(context.getResources()
                        .getColor(R.color.white));
                holder.textView.setTextColor(context.getResources().getColor(
                        R.color.purse_blue));
                if (type == 2) {
                    holder.ivSelector.setVisibility(View.VISIBLE);
                }
            } else {
                holder.re_item.setBackgroundColor(context.getResources()
                        .getColor(R.color.huise));
                holder.textView.setTextColor(
                        Color.BLACK);
                if (type == 2) {
                    holder.ivSelector.setVisibility(View.GONE);
                }
            }
        }

        if (type == 2) {
            ViewHolder xholder;
            if (convertView == null) {
                xholder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.erji_item, null);
                xholder.tvEjView = (TextView) convertView
                        .findViewById(R.id.tv_name);
                xholder.reBgItem = (RelativeLayout) convertView
                        .findViewById(R.id.re_item_bg);
                xholder.ivSelector = (ImageView) convertView
                        .findViewById(R.id.iv_selector);
                convertView.setTag(xholder);
            } else {
                xholder = (ViewHolder) convertView.getTag();
            }

            xholder.tvEjView.setText(queDatas.get(position).getName());

            if (currentPos == position) {
                xholder.tvEjView.setTextColor(context.getResources().getColor(
                        R.color.purse_blue));
                xholder.ivSelector.setVisibility(View.VISIBLE);
            } else {
                xholder.tvEjView.setTextColor(Color.BLACK);
                xholder.ivSelector.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    private class ViewHolder {
        TextView textView, tvEjView;
        RelativeLayout re_item, reBgItem;
        ImageView ivSelector;
    }

}
