package com.xywy.askforexpert.module.drug;

import android.support.v7.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;

/**
 * Created by jason on 2018/7/31.
 */

public class PhotoInfoActivity extends YMBaseActivity {
    @Override
    protected void initView() {
        AppCompatImageView imageView = (AppCompatImageView) findViewById(R.id.img_view);
        String url = getIntent().getStringExtra("url");
        Glide.with(this).load(url).into(imageView);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.photo_info_layout;
    }
}
