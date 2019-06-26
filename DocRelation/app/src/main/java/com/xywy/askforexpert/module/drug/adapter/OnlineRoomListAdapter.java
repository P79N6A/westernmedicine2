package com.xywy.askforexpert.module.drug.adapter;

import android.content.Context;

import com.xywy.askforexpert.module.drug.DocQuesDelegate;
import com.xywy.askforexpert.module.drug.OnlineRoomItemFragment;
import com.xywy.askforexpert.module.drug.bean.DocQues;
import com.xywy.uilibrary.recyclerview.adapter.XYWYRVMultiTypeBaseAdapter;

/**
 * 药品列表的Adapter stone
 */
public class OnlineRoomListAdapter extends XYWYRVMultiTypeBaseAdapter<DocQues> {
    public OnlineRoomListAdapter(OnlineRoomItemFragment onlineRoomItemFragment, Context context, int type) {
        super(context);
        if (type == 4) {
            addItemViewDelegate(new PrescriptionRecordListMainDelegate(context,type));
        }else{
            addItemViewDelegate(new DocQuesDelegate(onlineRoomItemFragment,type,context));
        }

    }
}
