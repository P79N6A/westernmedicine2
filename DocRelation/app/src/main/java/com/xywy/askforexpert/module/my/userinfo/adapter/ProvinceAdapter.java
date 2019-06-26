package com.xywy.askforexpert.module.my.userinfo.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.IdNameBean;

import java.util.List;

/**
 * 城市适配器
 * stone
 * 2018/2/7 下午1:39
 */
public class ProvinceAdapter extends BaseAdapter {

    public SparseBooleanArray selectionMap;
    private List<IdNameBean> list;
    private Context context;
    private LayoutInflater inflater;

    public ProvinceAdapter(Context context, List<IdNameBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        selectionMap = new SparseBooleanArray();
        init();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
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
            convertView = inflater.inflate(R.layout.baseschool_item, null);
            holder.img_enter = convertView.findViewById(R.id.img_enter);
            holder.textView = (TextView) convertView.findViewById(R.id.tv_text);
            holder.re_item = (RelativeLayout) convertView.findViewById(R.id.re_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.re_item.setBackgroundColor(selectionMap.get(position) ? context.getResources().getColor(R.color.white) : context.getResources().getColor(R.color.huise));
        holder.textView.setSelected(selectionMap.get(position));
        holder.img_enter.setVisibility(selectionMap.get(position) ? View.INVISIBLE : View.VISIBLE);
        holder.textView.setText(list.get(position).name);

        return convertView;
    }

    public void init() {
        for (int i = 0; i < list.size(); i++) {
            selectionMap.put(i, false);
        }
    }

    private class ViewHolder {
        TextView textView;
        RelativeLayout re_item;
        View img_enter;
    }
}
