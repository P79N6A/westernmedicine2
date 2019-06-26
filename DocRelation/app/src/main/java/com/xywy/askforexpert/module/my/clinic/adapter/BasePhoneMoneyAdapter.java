package com.xywy.askforexpert.module.my.clinic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.PhoneMoneyInfo;

import java.util.List;

/**
 * 执业类型
 *
 * @author 王鹏
 * @2015-5-7下午5:48:59
 */
@SuppressLint("ResourceAsColor")
public class BasePhoneMoneyAdapter extends BaseAdapter {

    public SparseBooleanArray selectionMap;
    List<PhoneMoneyInfo> list;
    private Context context;
    private LayoutInflater inflater;

    public BasePhoneMoneyAdapter(Context context) {
        this.context = context;

        inflater = LayoutInflater.from(context);
    }

    public void setData(List<PhoneMoneyInfo> list) {
        this.list = list;
        if (list != null) {
            init();
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
            convertView = inflater.inflate(R.layout.my_job_type_item, null);
            holder.textView = (TextView) convertView
                    .findViewById(R.id.tv_job_name);
            holder.imageView = (ImageView) convertView
                    .findViewById(R.id.img_sure);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        boolean ischeck = selectionMap.get(position);
        if (ischeck) {
            holder.imageView.setVisibility(View.VISIBLE);
            holder.textView.setTextColor(context.getResources().getColor(
                    R.color.purse_blue));
        } else {

            holder.imageView.setVisibility(View.GONE);
            holder.textView.setTextColor(context.getResources().getColor(
                    R.color.my_textcolor));
        }
        if (list != null) {
            holder.textView.setText(list.get(position).getMoney() + "元 ／" + list.get(position).getTime() + " 分钟");
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
        ImageView imageView;
    }
}
