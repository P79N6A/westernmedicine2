package com.xywy.askforexpert.module.my.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.ItemList;

import java.util.List;

public class MyConlectListAdapter extends BaseAdapter {

    Activity content;
    private List<ItemList> list1;
    private boolean isVisable = false;

    public MyConlectListAdapter(Activity content, List<ItemList> list) {
        this.list1 = list;
        this.content = content;
    }

    public void addData(List<ItemList> list) {
        this.list1.addAll(list);
        notifyDataSetChanged();
    }

    public void setAlldelte(boolean flag) {
        isVisable = flag;
        notifyDataSetChanged();
    }

    public List<ItemList> getList() {
        return list1;
    }

    public void selectChecbox(int a) {
        list1.get(a).isSeclect = !list1.get(a).isSeclect;
        notifyDataSetChanged();
    }

    public void deleteItem(List<ItemList> list) {
        list1 = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list1.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list1.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder1 mHolder;
        if (convertView == null) {
            mHolder = new ViewHolder1();
            convertView = View.inflate(content, R.layout.item_myconlect_list,
                    null);
            mHolder.cb = (CheckBox) convertView.findViewById(R.id.cb);
            mHolder.title = (TextView) convertView.findViewById(R.id.tv_title);
            mHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder1) convertView.getTag();
        }

        ItemList itemList = list1.get(position);

        if (isVisable) {
            mHolder.cb.setVisibility(View.VISIBLE);
        } else {
            mHolder.cb.setVisibility(View.GONE);
        }

        mHolder.title.setText(itemList.title);
        mHolder.tv_time.setText(itemList.collectiontime);
        if (mHolder.cb.getVisibility() == View.VISIBLE) {
            mHolder.cb.setChecked(itemList.isSeclect);
        } else {

        }


        return convertView;
    }

    static class ViewHolder1 {
        TextView title, tv_time;
        CheckBox cb;
    }

}
