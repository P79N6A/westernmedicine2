package com.xywy.askforexpert.module.doctorcircle.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseAdapter;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.model.doctor.PraiseBean;

import java.util.List;

/**
 * Compiler: Android Studio
 * Project: D_Platform
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2015/12/25 17:31
 *
 * 2016-11-17 白良锦重构
 */
public class PraiseHeadAdapter extends YMBaseAdapter<PraiseBean> {


    public PraiseHeadAdapter(Activity activity, List<PraiseBean> dataList) {
        super(activity, dataList);
    }

    public void clearData(){
        dataList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemLayoutResId() {
        return  R.layout.praise_avatar_layout;
    }

    @Override
    protected BaseViewHolder getViewHolder(View rootView) {
        return new ViewHolder(rootView);
    }

    class ViewHolder extends BaseViewHolder<PraiseBean>{

        final ImageView iv_head;

        public ViewHolder(View rootView) {
            super(rootView);
            iv_head= (ImageView) rootView.findViewById(R.id.praise_avatar);
        }

        @Override
        public void show(int position, PraiseBean data) {
            ImageLoadUtils.INSTANCE.loadImageView(iv_head,data.getPhoto());

        }
    }
}
