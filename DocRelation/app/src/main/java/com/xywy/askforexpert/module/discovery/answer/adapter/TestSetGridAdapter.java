package com.xywy.askforexpert.module.discovery.answer.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseAdapter;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.model.answer.show.TestSet;

import java.util.List;


/**
 * 答题模块 首页 试题集 GridView条目 Adapter
 * Created by bailiangjin on 16/3/21.
 */
public class TestSetGridAdapter extends YMBaseAdapter<TestSet> {

    public TestSetGridAdapter(Activity context, List<TestSet> dataList) {
        super(context, dataList);
    }


    @Override
    public int getItemLayoutResId() {
        return R.layout.item_testset_gridview;
    }

    @Override
    public BaseViewHolder getViewHolder(View rootView) {
        return new ViewHolder(rootView);
    }


    public class ViewHolder extends BaseViewHolder<TestSet> {
        private final ImageView iv_paper_image;

        public ViewHolder(View rootView) {
            super(rootView);
            iv_paper_image = (ImageView) this.rootView.findViewById(R.id.iv_paper_image);
        }

        @Override
        public void show(int position, TestSet dataItem) {
            if (!TextUtils.isEmpty(dataItem.getImgUrl())) {
                ImageLoadUtils.INSTANCE.loadImageView(iv_paper_image, dataItem.getImgUrl());

            } else {
             iv_paper_image.setImageResource(R.drawable.test_paper_bg_03);

            }
        }
    }
}
