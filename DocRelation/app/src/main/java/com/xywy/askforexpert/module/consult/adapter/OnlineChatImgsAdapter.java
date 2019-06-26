package com.xywy.askforexpert.module.consult.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.consultentity.ConsultChatEntity;
import com.xywy.askforexpert.model.consultentity.OnlineConsultChatEntity;
import com.xywy.component.uimodules.photoPicker.PhotoPreviewActivity;
import com.xywy.component.uimodules.photoPicker.model.PhotoInfo;
import com.xywy.uilibrary.recyclerview.adapter.XYWYRVSingleTypeBaseAdapter;
import com.xywy.util.XYImageLoader;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzheng on 2017/5/6.
 */

public class OnlineChatImgsAdapter extends XYWYRVSingleTypeBaseAdapter<String> {


    //stone
    private ArrayList<PhotoInfo> photoInfos;
    private OnlineConsultChatEntity consultChatEntity;

    public OnlineChatImgsAdapter(Context context, OnlineConsultChatEntity consultChatEntity) {
        super(context);
        this.consultChatEntity = consultChatEntity;
    }

    @Override
    public void setData(List<String> dataList) {
        super.setData(dataList);
        photoInfos = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
            PhotoInfo info = new PhotoInfo();
            info.setNetWorkUrl(mDatas.get(i));
            photoInfos.add(info);
        }
    }

    @Override
    protected int getItemLayoutResId() {
        return R.layout.item_chat_imgs;
    }

    @Override
    protected void convert(ViewHolder holder, final String item, final int position) {
        XYImageLoader.getInstance().displayImage(item, (ImageView) holder.getView(R.id.iv_img));
        if (consultChatEntity.getMsg_type()== ConsultChatEntity.MSG_TYPE_RECV){
            holder.getView(R.id.line1).setVisibility(View.GONE);
            holder.getView(R.id.line2).setVisibility(View.VISIBLE);
        }else{
            holder.getView(R.id.line1).setVisibility(View.VISIBLE);
            holder.getView(R.id.line2).setVisibility(View.GONE);
        }
        holder.getView(R.id.iv_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPreviewActivity.startActivity(mContext, photoInfos, position,true);
            }
        });
    }
}
