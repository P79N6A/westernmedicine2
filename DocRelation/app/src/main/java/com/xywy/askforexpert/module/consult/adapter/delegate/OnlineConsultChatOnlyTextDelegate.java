package com.xywy.askforexpert.module.consult.adapter.delegate;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.model.consultentity.ConsultChatEntity;
import com.xywy.askforexpert.model.consultentity.OnlineConsultChatEntity;
import com.xywy.askforexpert.widget.promptView.ChatPromptViewManager;
import com.xywy.askforexpert.widget.promptView.Location;
import com.xywy.askforexpert.widget.promptView.PromptViewHelper;
import com.xywy.util.XYImageLoader;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by zhangzheng on 2017/4/28.
 */

public class OnlineConsultChatOnlyTextDelegate implements ItemViewDelegate<OnlineConsultChatEntity> {
    private Activity mActivity;
    private ClipboardManager mClipboardManager;


    public OnlineConsultChatOnlyTextDelegate(Context context) {
        mActivity = (Activity) context;
        mClipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_chat_only_text;
    }

    @Override
    public boolean isForViewType(OnlineConsultChatEntity item, int position) {
        return item.getType() == OnlineConsultChatEntity.TYPE_ONLY_TEXT;
    }

    @Override
    public void convert(final ViewHolder holder, OnlineConsultChatEntity consultChatEntity, int position) {
        PromptViewHelper pvHelper = new PromptViewHelper(mActivity);
        pvHelper.setPromptViewManager(new ChatPromptViewManager(mActivity, Location.TOP_CENTER));

        if (consultChatEntity.getMsg_type() == ConsultChatEntity.MSG_TYPE_RECV) {
            pvHelper.addPrompt(holder.getView(R.id.tv_chat_recv_text));
            pvHelper.setOnItemClickListener(new PromptViewHelper.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    mClipboardManager.setText(((TextView) holder.getView(R.id.tv_chat_recv_text)).getText().toString());
                    ToastUtils.shortToast("复制成功");
                }
            });

            holder.getView(R.id.rl_chat_recv).setVisibility(View.VISIBLE);
            holder.getView(R.id.rl_chat_send).setVisibility(View.GONE);
            holder.setText(R.id.tv_chat_recv_text, consultChatEntity.getText());
            if (consultChatEntity.getDataBean() != null && consultChatEntity.getDataBean().getUser_photo() != null) {
                XYImageLoader.getInstance().displayImage(consultChatEntity.getDataBean().getUser_photo(), (ImageView) holder.getView(R.id.iv_chat_patient_head));
            }
        } else {
            pvHelper.addPrompt(holder.getView(R.id.tv_chat_send_text));
            pvHelper.setOnItemClickListener(new PromptViewHelper.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    mClipboardManager.setText(((TextView) holder.getView(R.id.tv_chat_send_text)).getText().toString());
                    ToastUtils.shortToast("复制成功");
                }
            });

            holder.getView(R.id.rl_chat_recv).setVisibility(View.GONE);
            holder.getView(R.id.rl_chat_send).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_chat_send_text, consultChatEntity.getText());
            XYImageLoader.getInstance().displayImage(YMApplication.getLoginInfo().getData().getPhoto(), (ImageView) holder.getView(R.id.iv_chat_doctor_head));
            switch (consultChatEntity.getSendState()) {
                case ConsultChatEntity.SEND_STATE_IN_SENDING:
                    holder.getView(R.id.tv_chat_send_tip_sending).setVisibility(View.VISIBLE);
                    holder.getView(R.id.tv_chat_send_tip_failed).setVisibility(View.INVISIBLE);
                    break;
                case ConsultChatEntity.SEND_STATE_FAILED:
                    holder.getView(R.id.tv_chat_send_tip_sending).setVisibility(View.INVISIBLE);
                    holder.getView(R.id.tv_chat_send_tip_failed).setVisibility(View.VISIBLE);
                    break;
                case ConsultChatEntity.SEND_STATE_SUCCESS:
                    holder.getView(R.id.tv_chat_send_tip_sending).setVisibility(View.INVISIBLE);
                    holder.getView(R.id.tv_chat_send_tip_failed).setVisibility(View.INVISIBLE);
                    break;
            }
        }


    }

}
