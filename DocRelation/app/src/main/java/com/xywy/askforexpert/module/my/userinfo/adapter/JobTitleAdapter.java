package com.xywy.askforexpert.module.my.userinfo.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.IdNameBean;

import java.util.List;

/**
 * 临床职称 adapter
 * stone
 */
public class JobTitleAdapter extends BaseAdapter {

    private SparseBooleanArray selectionMap;
    private List<IdNameBean> list;
    private Context context;
    private LayoutInflater inflater;

    public JobTitleAdapter(Context context, List<IdNameBean>  list,int pos) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        selectionMap = new SparseBooleanArray();
        initSelecteStatus();
        selectionMap.put(pos,true);
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
            convertView = inflater.inflate(R.layout.item_jobtitle, null);
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
                    R.color.color_333333));
        }
        holder.textView.setText(list.get(position).name);

        return convertView;
    }

    public void initSelecteStatus() {
        for (int i = 0; i < list.size(); i++) {
            selectionMap.put(i, false);
        }
    }

    private class ViewHolder {
        TextView textView;
        ImageView imageView;
    }

    public SparseBooleanArray getSelectionMap() {
        return selectionMap;
    }
}
