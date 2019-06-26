package com.xywy.askforexpert.module.consult.adapter;

import android.content.Context;

import com.xywy.askforexpert.model.consultentity.ConsultChatEntity;
import com.xywy.askforexpert.module.consult.activity.ConsultChatActivity;
import com.xywy.askforexpert.module.consult.adapter.delegate.ConsultChatCloseNoticeDelegate;
import com.xywy.askforexpert.module.consult.adapter.delegate.ConsultChatOnlyImgDelegate;
import com.xywy.askforexpert.module.consult.adapter.delegate.ConsultChatOnlyTextDelegate;
import com.xywy.askforexpert.module.consult.adapter.delegate.ConsultChatQuestionDescDelegate;
import com.xywy.askforexpert.module.consult.adapter.delegate.ConsultChatRewardDelegate;
import com.xywy.askforexpert.module.consult.adapter.delegate.ConsultChatTipDelegate;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;
import com.xywy.uilibrary.recyclerview.adapter.XYWYRVMultiTypeBaseAdapter;

import java.util.ArrayList;

/**
 * Created by zhangzheng on 2017/4/28. stone
 */

public class ConsultChatAdapter extends XYWYRVMultiTypeBaseAdapter<ConsultChatEntity> {
    public ConsultChatAdapter(boolean mIsFromWTKOrHistory,Context context, MyCallBack callBack, SendMsgError sendMsgError) {
        super(context);
        addItemViewDelegate(new ConsultChatOnlyImgDelegate(mIsFromWTKOrHistory));
        addItemViewDelegate(new ConsultChatOnlyTextDelegate(context,sendMsgError));
        addItemViewDelegate(new ConsultChatQuestionDescDelegate());
        addItemViewDelegate(new ConsultChatTipDelegate());
        //TODO 送心意 判断标准未知 stone
        addItemViewDelegate(new ConsultChatRewardDelegate());
        // TODO: 2018/4/16 留言关闭提醒
        addItemViewDelegate(new ConsultChatCloseNoticeDelegate(callBack));
    }




    public void addDataForPos(int pos, ConsultChatEntity dataItem) {
        if (null == mDatas) {
            mDatas = new ArrayList<>();
        }

        if (null != dataItem) {
            this.mDatas.add(pos, dataItem);
        }
    }


    public interface SendMsgError{
        void senMsgError(int position);
    }

}
