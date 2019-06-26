package com.xywy.askforexpert.module.consult.adapter.delegate;

import android.content.Context;

import com.xywy.askforexpert.model.consultentity.OnlineConsultChatEntity;
import com.xywy.askforexpert.module.consult.adapter.OnlineClosedDelegate;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;
import com.xywy.uilibrary.recyclerview.adapter.XYWYRVMultiTypeBaseAdapter;

import java.util.ArrayList;

/**
 * Created by zhangzheng on 2017/4/28. stone
 */

public class OnlineConsultChatAdapter extends XYWYRVMultiTypeBaseAdapter<OnlineConsultChatEntity> {
    public OnlineConsultChatAdapter(Context context, boolean isPatient,MyCallBack callBack) {
        super(context);
        addItemViewDelegate(new OnlineConsultChatOnlyImgDelegate());
        addItemViewDelegate(new OnlineConsultChatOnlyTextDelegate(context));
        addItemViewDelegate(new OnlineConsultChatQuestionDescDelegate(context,isPatient));
        addItemViewDelegate(new OnlineClosedDelegate(context));
        addItemViewDelegate(new OnlineConsultChatPatientDelegate(context));

    }


    public void addDataForPos(int pos, OnlineConsultChatEntity dataItem) {
        if (null == mDatas) {
            mDatas = new ArrayList<>();
        }

        if (null != dataItem) {
            this.mDatas.add(pos, dataItem);
        }
    }


}
