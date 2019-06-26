package com.xywy.askforexpert.module.main.service.linchuang.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.CommentFistInfo;

import java.util.List;

/**
 * 学校城市 适配器
 *
 * @author 王鹏
 * @2015-5-7下午5:48:59
 */
@SuppressLint("ResourceAsColor")
public class BaseCommentSecondAdapter extends BaseAdapter {

    List<CommentFistInfo> list;
    private Context context;
    private LayoutInflater inflater;

    public BaseCommentSecondAdapter(Context context, List<CommentFistInfo> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
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
            convertView = inflater.inflate(R.layout.comment_list_item_second,
                    null);
            holder.tv_content = (TextView) convertView
                    .findViewById(R.id.tv_content);
            holder.tv_redate = (TextView) convertView
                    .findViewById(R.id.tv_redate);
            holder.tv_docname = (TextView) convertView
                    .findViewById(R.id.tv_docname);
            holder.lin_main = (LinearLayout) convertView.findViewById(R.id.lin_main);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            holder.lin_main.setBackgroundResource(R.drawable.commt_item_1);
        } else {
            holder.lin_main.setBackgroundResource(R.drawable.commt_item_2);

        }
        if (list != null) {
            holder.tv_content.setText(list.get(position).getContent());
            String docname = list.get(position).getDoc().getName() + " 对 " + list.get(position).getTodoc().getName() + "的回复";
            holder.tv_docname.setText(docname);
            holder.tv_redate.setText(list.get(position).getNomaldate());
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tv_content;
        LinearLayout lin_main;

        TextView tv_redate;
        TextView tv_docname;
    }

}
