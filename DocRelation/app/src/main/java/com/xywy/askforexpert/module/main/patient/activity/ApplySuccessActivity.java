package com.xywy.askforexpert.module.main.patient.activity;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;

/**
 * Created by jason on 2018/11/16.
 */

public class ApplySuccessActivity extends YMBaseActivity {
    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("申请成功");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.apply_success_layout;
    }
}
