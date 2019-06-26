package com.xywy.askforexpert.module.consult.adapter.delegate;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.model.consultentity.ConsultChatEntity;
import com.xywy.askforexpert.module.consult.adapter.ChatImgsAdapter;
import com.xywy.util.XYImageLoader;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by zhangzheng on 2017/4/28. stone 移植yimai
 */

public class ConsultChatOnlyImgDelegate implements ItemViewDelegate<ConsultChatEntity> {
    private boolean mIsFromWTKOrHistory;
    public ConsultChatOnlyImgDelegate(boolean mIsFromWTKOrHistory) {
        this.mIsFromWTKOrHistory = mIsFromWTKOrHistory;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_chat_only_img;
    }

    @Override
    public boolean isForViewType(ConsultChatEntity item, int position) {
        return item.getType() == ConsultChatEntity.TYPE_ONLY_IMG;
    }

    @Override
    public void convert(ViewHolder holder, ConsultChatEntity consultChatEntity, int position) {
        ChatImgsAdapter adapter = new ChatImgsAdapter(consultChatEntity.getMsg_type(),mIsFromWTKOrHistory,holder.getConvertView().getContext());
        adapter.setData(consultChatEntity.getImgUrls());
        if (consultChatEntity.getMsg_type() == ConsultChatEntity.MSG_TYPE_RECV) {
            holder.getView(R.id.rl_chat_recv).setVisibility(View.VISIBLE);
            holder.getView(R.id.rl_chat_send).setVisibility(View.GONE);
            if (null!=consultChatEntity.getDataBean()&&
                    !TextUtils.isEmpty(consultChatEntity.getDataBean().getUser_photo())) {
                XYImageLoader.getInstance().displayImage(consultChatEntity.getDataBean().getUser_photo(), (ImageView) holder.getView(R.id.iv_chat_patient_head));
//            if (null==consultChatEntity.getDataBean()){
//                XYImageLoader.getInstance().displayImage(ImFileCacheUtil.getInstance().userphoto.get(YMUserService.getCurUserId()), (ImageView) holder.getView(R.id.iv_chat_patient_head));
//            }else{
//
//            }
            }
            RecyclerView rlvImgs = holder.getView(R.id.rlv_chat_recv_imgs);
           //stone 修改处
            rlvImgs.setLayoutManager(new LinearLayoutManager(holder.getConvertView().getContext()));
            rlvImgs.setAdapter(adapter);
        } else {
            holder.getView(R.id.rl_chat_recv).setVisibility(View.GONE);
            holder.getView(R.id.rl_chat_send).setVisibility(View.VISIBLE);
            XYImageLoader.getInstance().displayImage(YMApplication.getLoginInfo().getData().getPhoto(), (ImageView) holder.getView(R.id.iv_chat_doctor_head));
            RecyclerView rlvImgs = holder.getView(R.id.rlv_chat_send_imgs);
            //stone 修改处
            rlvImgs.setLayoutManager(new LinearLayoutManager(holder.getConvertView().getContext()));
            rlvImgs.setAdapter(adapter);
        }
    }
}
