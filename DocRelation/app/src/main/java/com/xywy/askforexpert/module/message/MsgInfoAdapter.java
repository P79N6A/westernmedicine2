package com.xywy.askforexpert.module.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MsgInfoAdapter extends BaseAdapter {

    private Context context;

    private List<Message> msgLists;

    private SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");

    public MsgInfoAdapter(Context context) {
        this.context = context;
    }

    public void bindData(List<Message> msgLists) {
        this.msgLists = msgLists;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return msgLists.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return msgLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.msg_item_adapter, null);
            holder.title = (TextView) convertView.findViewById(R.id.name);
            holder.unreadLabel = (TextView) convertView
                    .findViewById(R.id.unread_msg_number);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.content = (TextView) convertView.findViewById(R.id.message);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (msgLists.get(position) != null) {
            String createtime = msgLists.get(position).getCreateTime();
            String msgNum = msgLists.get(position).getMsgNum();
            holder.title.setText(msgLists.get(position).getTitle());
            holder.content.setText(msgLists.get(position).getDetail());
            if (!"".equals(createtime)) {
                String sd = sdf.format(new Date(
                        Long.parseLong(createtime) * 1000L));
                holder.time.setText(sd);
            }
            if (!"".equals(msgNum)) {
                holder.unreadLabel.setVisibility(View.VISIBLE);
                if (Integer.parseInt(msgNum) > 99) {
                    holder.unreadLabel.setText("99+");
                } else {
                    holder.unreadLabel.setText(msgNum);
                }
            } else {
                holder.unreadLabel.setVisibility(View.GONE);
            }


            switch (msgLists.get(position).getType()) {

                case 1:// 问答消息
                    holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.msg_zhushou_icon));
                    break;
                case 2:// 预约转诊
                    holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.yuyuzhuanzhen));
                    break;
                case 3:// 电话医生
                    holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.dianhuayisheng));
                    break;
                case 4:// 医学资讯
                    holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_xixun));
                    break;
                case 5:// 中国医生的一天
                    holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_doctor_oneday));
                    break;
                case 6:// 招聘中心
                    holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_zhaopin));
                    break;
                case 7:// 临床指南
                    holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_lingchuang));
                    break;
                case 8:// 家庭医生
                    holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_home_doctor_lg));
                    break;
            }

        }

        return convertView;
    }

    class ViewHolder {
        /**
         * 和谁的聊天记录
         */
        TextView title;
        /**
         * 消息未读数
         */
        TextView unreadLabel;
        /**
         * 最后一条消息的内容
         */
        TextView content;
        /**
         * 最后一条消息的时间
         */
        TextView time;
        /**
         * 用户头像
         */
        ImageView avatar;
        /**
         * 最后一条消息的发送状态
         */
        View msgState;
        /** 整个list中每一行总布局 */
    }
}