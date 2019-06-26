package com.xywy.askforexpert.module.consult.adapter.delegate;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.model.consultentity.ConsultChatEntity;
import com.xywy.askforexpert.module.consult.adapter.ConsultChatAdapter;
import com.xywy.askforexpert.widget.promptView.ChatPromptViewManager;
import com.xywy.askforexpert.widget.promptView.Location;
import com.xywy.askforexpert.widget.promptView.PromptViewHelper;
import com.xywy.util.XYImageLoader;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhangzheng on 2017/4/28.
 */

public class ConsultChatOnlyTextDelegate implements ItemViewDelegate<ConsultChatEntity> {

    private Activity mActivity;
    private ClipboardManager mClipboardManager;
    private ConsultChatAdapter.SendMsgError callBack;

    public ConsultChatOnlyTextDelegate(Context context,ConsultChatAdapter.SendMsgError myCallBack) {
        mActivity = (Activity) context;
        mClipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        this.callBack = myCallBack;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_chat_only_text;
    }

    @Override
    public boolean isForViewType(ConsultChatEntity item, int position) {
        return item.getType() == ConsultChatEntity.TYPE_ONLY_TEXT;
    }

    @Override
    public void convert(final ViewHolder holder, ConsultChatEntity consultChatEntity, final int position) {
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
                    holder.getView(R.id.tv_chat_send_tip_failed).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showNormalDialog(position);
                        }
                    });
                    break;
                case ConsultChatEntity.SEND_STATE_SUCCESS:
                    holder.getView(R.id.tv_chat_send_tip_sending).setVisibility(View.INVISIBLE);
                    holder.getView(R.id.tv_chat_send_tip_failed).setVisibility(View.INVISIBLE);
                    break;
            }
        }
        if (consultChatEntity.isTitmeFlag()){
            holder.setVisible(R.id.msg_time_tv,true);
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
            String msgTime = sdf.format(new Date(Long.parseLong(consultChatEntity.getTime())));
            holder.setText(R.id.msg_time_tv,msgTime);
        } else {
            holder.setVisible(R.id.msg_time_tv,false);
        }

    }

    private void showNormalDialog(final int position){
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(mActivity);
        normalDialog.setMessage("您需要重发这条消息吗？");
        normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(null!=callBack) {
                    callBack.senMsgError(position);
                }
                dialog.dismiss();
            }
        });
        normalDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        normalDialog.show();
    }

}
