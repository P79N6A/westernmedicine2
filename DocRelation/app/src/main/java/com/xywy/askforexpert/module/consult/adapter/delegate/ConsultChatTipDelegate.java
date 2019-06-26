package com.xywy.askforexpert.module.consult.adapter.delegate;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.consultentity.ConsultChatEntity;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by zhangzheng on 2017/4/28.
 */

public class ConsultChatTipDelegate implements ItemViewDelegate<ConsultChatEntity> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_chat_tip;
    }

    @Override
    public boolean isForViewType(ConsultChatEntity item, int position) {
        return item.getType() == ConsultChatEntity.TYPE_TIP;
    }

    @Override
    public void convert(ViewHolder holder, ConsultChatEntity consultChatEntity, int position) {
        holder.setText(R.id.tv_chat_tip, consultChatEntity.getTipText());
    }
}
