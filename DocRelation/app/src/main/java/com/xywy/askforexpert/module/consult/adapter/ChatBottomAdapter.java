package com.xywy.askforexpert.module.consult.adapter;

import android.content.Context;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.adapter.better.YMRVSingleTypeBaseAdapter;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.model.consultentity.ChatBottomItemEntity;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by zhangzheng on 2017/5/2.
 */

public class ChatBottomAdapter extends YMRVSingleTypeBaseAdapter<ChatBottomItemEntity> {

    private OnItemClickListener onItemClickListener;

    public ChatBottomAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutResId() {
        return R.layout.item_chat_bottom;
    }

    @Override
    protected void convert(ViewHolder holder, final ChatBottomItemEntity item, int position) {
        holder.setText(R.id.tv_text, item.getText());
        if (item.isEnable()) {
            holder.setImageResource(R.id.iv_img, item.getEnnabledImgSrc());
            holder.setOnClickListener(R.id.ll_root, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.isEnable() && onItemClickListener != null) {
                        onItemClickListener.onItemClick(item);
                    }
                }
            });
        } else {
            if (item.getText().equals("开处方")){
                holder.setOnClickListener(R.id.ll_root, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.longToast("患者为初诊患者，暂不可为其开具在线处方");
                    }
                });
            }
            holder.setImageResource(R.id.iv_img, item.getDisabledImgSrc());
        }

    }

    public static interface OnItemClickListener {
        void onItemClick(ChatBottomItemEntity entity);
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
