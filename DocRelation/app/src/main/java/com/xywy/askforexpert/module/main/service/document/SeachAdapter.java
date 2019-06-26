package com.xywy.askforexpert.module.main.service.document;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.wenxian.ItemRecords;

import java.util.List;

/**
 * 搜索适配器
 *
 * @author apple
 */
public class SeachAdapter extends BaseAdapter {

    Context context;
    private List<ItemRecords> mRecords;

    public SeachAdapter(Context context) {
        this.context = context;
    }

    public SeachAdapter(Activity context2, List<ItemRecords> mRecords) {
        // TODO Auto-generated constructor stub
        this.context = context2;
        this.mRecords = mRecords;

    }

    public void addData(List<ItemRecords> mRecords) {
        this.mRecords.addAll(mRecords);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mRecords.size();
    }

    public List<ItemRecords> getData() {
        return mRecords;
    }

    @Override
    public ItemRecords getItem(int arg0) {
        // TODO Auto-generated method stub
        return mRecords.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int arg0, View view, ViewGroup arg2) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (view == null) {

            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_seach, null);
            holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            holder.tv_author = (TextView) view.findViewById(R.id.tv_author);
            holder.tv_come_fram = (TextView) view.findViewById(R.id.tv_come_fram);
            holder.tv_linchpin = (TextView) view.findViewById(R.id.tv_linchpin);
            holder.tv_send_time = (TextView) view.findViewById(R.id.tv_send_time);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        ItemRecords itemRecords = mRecords.get(arg0);

        if (TextUtils.isEmpty(itemRecords.getYear())) {

            holder.tv_send_time.setVisibility(View.GONE);
        } else {
            holder.tv_send_time.setVisibility(View.VISIBLE);
            holder.tv_send_time.setText("发表年份：" + itemRecords.getYear());
        }

        if (TextUtils.isEmpty(itemRecords.getTitle().trim())) {

            holder.tv_title.setVisibility(View.GONE);
        } else {
            holder.tv_title.setVisibility(View.VISIBLE);
            holder.tv_title.setText(itemRecords.getTitle());
        }
        if (TextUtils.isEmpty(itemRecords.getCreator())) {

            holder.tv_author.setVisibility(View.GONE);
        } else {
            holder.tv_author.setVisibility(View.VISIBLE);
            holder.tv_author.setText("作者：" + itemRecords.getCreator());
        }
        if (TextUtils.isEmpty(itemRecords.getSource())) {

            holder.tv_come_fram.setVisibility(View.GONE);
        } else {
            holder.tv_come_fram.setVisibility(View.VISIBLE);
            holder.tv_come_fram.setText("来源：" + itemRecords.getSource());
        }
        if (TextUtils.isEmpty(itemRecords.getKeyWords())) {

            holder.tv_linchpin.setVisibility(View.GONE);
        } else {
            holder.tv_linchpin.setVisibility(View.VISIBLE);
            holder.tv_linchpin.setText("关键词：" + itemRecords.getKeyWords());
        }

        return view;
    }


    static class ViewHolder {
        TextView tv_title, tv_author, tv_come_fram, tv_linchpin, tv_send_time;
    }

}
