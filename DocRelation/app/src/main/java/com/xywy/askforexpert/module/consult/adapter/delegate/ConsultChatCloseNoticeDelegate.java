package com.xywy.askforexpert.module.consult.adapter.delegate;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DateUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.model.certification.MessageBoardBean;
import com.xywy.askforexpert.model.consultentity.ConsultChatEntity;
import com.xywy.askforexpert.module.discovery.medicine.CertificationAboutRequest;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import static com.xywy.askforexpert.appcommon.utils.CtxtUtils.getString;

/**
 * 关闭留言提醒 stone
 */

public class ConsultChatCloseNoticeDelegate implements ItemViewDelegate<ConsultChatEntity> {

    private SpannableString ss;

    private MyCallBack callBack;

    public ConsultChatCloseNoticeDelegate(MyCallBack myCallBack) {
        this.callBack = myCallBack;
    }


    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_chat_closenotice;
    }

    @Override
    public boolean isForViewType(ConsultChatEntity item, int position) {
        return item.getType() == ConsultChatEntity.TYPE_CLOSE_NOTICE;
    }

    @Override
    public void convert(ViewHolder holder, ConsultChatEntity consultChatEntity, int position) {

        //创建SpannableString对象,内容不可修改
        ss = new SpannableString(getString(R.string.messagesetting_notice1));

        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#3300ff")), 25, getString(R.string.messagesetting_notice1).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new TextClick(), 25, getString(R.string.messagesetting_notice1).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((TextView) holder.getView(R.id.tv1)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) holder.getView(R.id.tv1)).setText(ss);
    }


    private class TextClick extends ClickableSpan {
        @Override
        public void onClick(View widget) {
            //在此处理点击事件
//            ToastUtils.shortToast("关闭吧");

            //关闭留言 stone
            StatisticalTools.eventCount(widget.getContext(), Constants.CLOSETHEMESSAGE);

            //关闭留言 不在夜间留言时间不关闭夜间留言
            if (!DateUtils.isCurrentInTimeScope(6, 0, 22, 0)) {
                requestSetMessageBoard(Constants.NIGHT, YMApplication.mNightMessage);
            }
            requestSetMessageBoard(Constants.OFFLINE, YMApplication.mOfflineMessage);

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            // ds.setColor(Color.parseColor("#00c8aa")); //文字的颜色
            // ds.setColor(ds.linkColor); //文字的颜色
            ds.setUnderlineText(true); //是否设置下划线，true表示设置。
        }


        //请求设置留言板
        private void requestSetMessageBoard(final String type, String message) {
            CertificationAboutRequest.getInstance().getSetMessageBoard(YMApplication.getUUid(), type, Constants.CLOSEED, message).subscribe(new BaseRetrofitResponse<BaseData<MessageBoardBean>>() {
                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    ToastUtils.shortToast("关闭留言失败");
                }

                @Override
                public void onNext(BaseData<MessageBoardBean> listBaseData) {
                    super.onNext(listBaseData);
                    if (Constants.OFFLINE.equals(type)) {
                        ToastUtils.shortToast("关闭留言成功");
                        YMApplication.mIsNoticeOpen = false;
                        //通知
                        YmRxBus.notifyNightModeChanged(false);
                        if (callBack != null) {
                            callBack.onClick(null);
                        }
                    }
                }
            });
        }
    }

}
