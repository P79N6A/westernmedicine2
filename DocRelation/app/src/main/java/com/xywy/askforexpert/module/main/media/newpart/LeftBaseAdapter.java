package com.xywy.askforexpert.module.main.media.newpart;

import android.content.Context;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.adapter.better.YMRVSingleTypeBaseAdapter;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.model.media.MediaTypeBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by bailiangjin on 2016/12/20.
 */

public class LeftBaseAdapter extends YMRVSingleTypeBaseAdapter<MediaTypeBean> {

    public LeftBaseAdapter(Context context) {
        super(context);
    }



    int selectedPosition=0;
    /**
     * 设置选中item的位置
     *  @param position
     *
     */
    public void setSelectedItem(int position) {
        if (null == mDatas || mDatas.isEmpty()) {
            return;
        }
        selectedPosition=position;
        notifyDataSetChanged();
    }



    @Override
    protected int getItemLayoutResId() {
        return R.layout.item_media_index_rv_list;
    }

    @Override
    protected void convert(ViewHolder holder, MediaTypeBean mediaTypeBean, int position) {
        if (position==selectedPosition){
            holder.itemView.setSelected(true);
        }else {
            holder.itemView.setSelected(false);
        }
        holder.setText(R.id.tv_name, mediaTypeBean.getName());
        LogUtils.d("mediaTypeBean"+mediaTypeBean.getName());
    }
}
