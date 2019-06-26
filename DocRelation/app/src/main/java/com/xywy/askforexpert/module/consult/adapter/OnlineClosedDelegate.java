package com.xywy.askforexpert.module.consult.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.model.consultentity.ConsultChatEntity;
import com.xywy.askforexpert.model.consultentity.OnlineConsultChatEntity;
import com.xywy.uilibrary.recyclerview.adapter.XYWYRVMultiTypeBaseAdapter;
import com.xywy.util.XYImageLoader;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by jason on 2018/7/9.
 */

public class OnlineClosedDelegate implements ItemViewDelegate<OnlineConsultChatEntity> {
    public OnlineClosedDelegate(Context context) {
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.online_im_closed;
    }


    @Override
    public boolean isForViewType(OnlineConsultChatEntity item, int position) {
        return item.getType() == ConsultChatEntity.TYPE_TIP;
    }

    @Override
    public void convert(ViewHolder holder, OnlineConsultChatEntity consultChatEntity, int position) {
        TextView comment_tv = holder.getView(R.id.comment_tv);
        comment_tv.setText(consultChatEntity.getComment());
        YMOtherUtils.onlineHandleStar((ImageView) holder.getView(R.id.score_img),consultChatEntity.getSatisfied());
    }
}
