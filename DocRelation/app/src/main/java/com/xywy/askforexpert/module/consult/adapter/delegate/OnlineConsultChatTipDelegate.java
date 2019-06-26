package com.xywy.askforexpert.module.consult.adapter.delegate;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.consultentity.ConsultChatEntity;
import com.xywy.askforexpert.model.consultentity.OnlineConsultChatEntity;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by zhangzheng on 2017/4/28.
 */

public class OnlineConsultChatTipDelegate implements ItemViewDelegate<OnlineConsultChatEntity> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_chat_tip;
    }

    @Override
    public boolean isForViewType(OnlineConsultChatEntity item, int position) {
        return item.getType() == OnlineConsultChatEntity.TYPE_TIP;
    }

    @Override
    public void convert(ViewHolder holder, OnlineConsultChatEntity consultChatEntity, int position) {
        holder.setText(R.id.tv_chat_tip, consultChatEntity.getTipText());
    }
}
