package com.xywy.askforexpert.module.consult.adapter.delegate;

import android.widget.ImageView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.consultentity.ConsultChatEntity;
import com.xywy.util.XYImageLoader;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by zhangzheng on 2017/4/28.
 */

public class ConsultChatQuestionDescDelegate implements ItemViewDelegate<ConsultChatEntity> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_chat_question_desc;
    }

    @Override
    public boolean isForViewType(ConsultChatEntity item, int position) {
        return item.getType() == ConsultChatEntity.TYPE_QUESTION_DESC;
    }

    @Override
    public void convert(ViewHolder holder, ConsultChatEntity consultChatEntity, int position) {
        holder.setText(R.id.tv_chat_patient_info, consultChatEntity.getPatientInfo());
        holder.setText(R.id.tv_chat_title, consultChatEntity.getTitle());
//        holder.setText(R.id.tv_chat_question_desc, consultChatEntity.getDesc());
//        XYImageLoader.getInstance().displayImage(consultChatEntity.getImgUrl(), (ImageView) holder.getView(R.id.iv_patient_head));
    }
}
