package com.xywy.askforexpert.module.main.service.codex.adapter;

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
import com.xywy.askforexpert.model.CodexSecondInfo;

import java.util.List;

/**
 * 学校城市 适配器
 *
 * @author 王鹏
 * @2015-5-7下午5:48:59
 */
@SuppressLint("ResourceAsColor")
public class BaseCodexSecondAdapter extends BaseAdapter {

    public SparseBooleanArray selectionMap;
    List<CodexSecondInfo> list;
    //	private Context context;
    private LayoutInflater inflater;

    public BaseCodexSecondAdapter(Context context) {
//		this.context = context;

        inflater = LayoutInflater.from(context);
    }

    public void setData(List<CodexSecondInfo> list) {
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
            convertView = inflater.inflate(R.layout.baseschool_item, null);
            holder.textView = (TextView) convertView.findViewById(R.id.tv_text);
            holder.re_item = (RelativeLayout) convertView
                    .findViewById(R.id.re_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (list != null) {
            holder.textView.setText(list.get(position).getTitle());
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
    }
}
