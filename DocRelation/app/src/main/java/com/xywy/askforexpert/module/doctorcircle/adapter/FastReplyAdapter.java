package com.xywy.askforexpert.module.doctorcircle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.QuestionSquareMsgItem;

import java.util.List;

/**
 * 项目名称：D_Platform 类名称：FastReplyAdapter 类描述：快捷回复适配器 创建人：shihao 创建时间：2015-5-25
 * 上午9:23:40 修改备注：
 */
public class FastReplyAdapter extends BaseAdapter {

    private Context context;
    private List<QuestionSquareMsgItem> mLists;

    public FastReplyAdapter(Context context) {
        this.context = context;
    }

    public void bindData(List<QuestionSquareMsgItem> mLists) {
        this.mLists = mLists;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mLists.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.fast_reply_item, null);
            holder.tvContent = (TextView) convertView
                    .findViewById(R.id.tv_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mLists.get(position).getTitle() != null) {
            holder.tvContent.setText(mLists.get(position).getTitle());
        }

        return convertView;
    }

    class ViewHolder {
        TextView tvContent;
    }

}
