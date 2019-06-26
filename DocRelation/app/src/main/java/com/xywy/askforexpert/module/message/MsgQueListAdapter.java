package com.xywy.askforexpert.module.message;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.QuestionBean.Data.DataList;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

/**
 * 问答消息 适配器
 *
 * @author LG
 */
public class MsgQueListAdapter extends BaseAdapter {

    private Context context;

    private List<DataList> data;

    private FinalBitmap fb;

    public MsgQueListAdapter(Context context, List<DataList> data) {
        this.context = context;
        this.data = data;
        fb = FinalBitmap.create(context, false);
    }

    public void addData(List<DataList> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_msgquelist, null);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.message = (TextView) convertView.findViewById(R.id.message);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.time = (TextView) convertView.findViewById(R.id.time);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DataList dataList = data.get(position);
        holder.name.setText(dataList.title);
        holder.message.setText(dataList.content);
        holder.time.setText(dataList.createtime);

        return convertView;
    }

    class ViewHolder {
        TextView time, message, name;
        ImageView msg_state, avatar;
    }

}
