package com.xywy.askforexpert.module.main.media.newpart;

import android.content.Context;
import android.widget.ImageView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.adapter.better.YMRVSingleTypeBaseAdapter;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.model.media.MediaNumberBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by bailiangjin on 2016/12/20.
 */

public class RightBaseAdapter extends YMRVSingleTypeBaseAdapter<MediaNumberBean> {

    public RightBaseAdapter(Context context) {
        super(context);
    }


    @Override
    protected int getItemLayoutResId() {
        return  R.layout.item_media_content_rv_list;
    }

    @Override
    protected void convert(ViewHolder holder, final MediaNumberBean item, int position) {
        ImageView ivHead=holder.getView(R.id.iv_head);
        ImageLoadUtils.INSTANCE.loadImageView(ivHead,item.getImg());
        holder.setText(R.id.tv_title,getDatas().get(position).getName());
        holder.setText(R.id.tv_content,getDatas().get(position).getIntroduce());
    }

}
