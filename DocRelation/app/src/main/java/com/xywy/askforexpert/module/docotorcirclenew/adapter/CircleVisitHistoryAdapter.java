package com.xywy.askforexpert.module.docotorcirclenew.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.model.DoctorUnReadMessageBean;
import com.xywy.askforexpert.module.docotorcirclenew.adapter.viewholder.BaseRecycleViewHolder;
import com.xywy.askforexpert.widget.view.ultimaterecycleview.UltimateRecyclerviewViewHolder;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/12/21 14:32
 */

public class CircleVisitHistoryAdapter extends BaseUltimateRecycleAdapter<DoctorUnReadMessageBean.ListItem> {

    public CircleVisitHistoryAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_unreadmessage, parent, false);
        RecyclerView.ViewHolder vh = new ItemViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder mViewHolder, int position) {
        ((UltimateRecyclerviewViewHolder) mViewHolder).onBindView(getItem(position));
    }

    class ItemViewHolder extends BaseRecycleViewHolder<DoctorUnReadMessageBean.ListItem> {
        TextView tv_username, tv_user_time, tv_user_parises, tv_unread_content;
        ImageView iv_usrer_pic;

        public ItemViewHolder(View convertView) {
            super(convertView);
            //话题列表
            tv_unread_content = (TextView) convertView.findViewById(R.id.tv_unread_content);
            tv_user_time = (TextView) convertView.findViewById(R.id.tv_user_time);
            tv_user_parises = (TextView) convertView.findViewById(R.id.tv_user_parises);
            iv_usrer_pic = (ImageView) convertView.findViewById(R.id.iv_usrer_pic);
            tv_username = (TextView) convertView.findViewById(R.id.tv_username);

        }

        public void updateView(final Context context, DoctorUnReadMessageBean.ListItem item) {
            super.updateView(context,item);
            DoctorUnReadMessageBean.ListItem.Userrow userrow = item.userrow;
            ImageLoadUtils.INSTANCE.loadImageView(iv_usrer_pic, userrow.userphoto);
            tv_unread_content.setText(item.dynamic);
            tv_username.setText(userrow.nickname);
            tv_user_parises.setText(item.content);
            tv_user_time.setText(item.nomalDate);
            itemView.setOnClickListener(uiListener);
        }
    }
}
