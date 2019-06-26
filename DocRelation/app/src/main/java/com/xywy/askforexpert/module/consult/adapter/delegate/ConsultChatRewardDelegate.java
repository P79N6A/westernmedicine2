package com.xywy.askforexpert.module.consult.adapter.delegate;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.consultentity.ConsultChatEntity;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * 送心意 stone
 */

public class ConsultChatRewardDelegate implements ItemViewDelegate<ConsultChatEntity> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_chat_reward;
    }

    @Override
    public boolean isForViewType(ConsultChatEntity item, int position) {
        return item.getType() == ConsultChatEntity.TYPE_REWARD;
    }

    @Override
    public void convert(ViewHolder holder, ConsultChatEntity consultChatEntity, int position) {
        holder.setText(R.id.tv_reward_count, consultChatEntity.getText()+"元");
    }
}
