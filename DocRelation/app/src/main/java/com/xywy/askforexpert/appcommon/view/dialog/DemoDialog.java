package com.xywy.askforexpert.appcommon.view.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.uilibrary.dialog.base.AbsTitleDialog;

/**
 * Created by bailiangjin on 2016/10/25.
 */

public class DemoDialog extends AbsTitleDialog {

    TextView tv_test;

    public DemoDialog(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.dialog_demo;
    }

    @Override
    protected boolean isNoTitle() {
        return false;
    }

    @Override
    protected void bindSubView(View view) {
        tv_test= (TextView) view.findViewById(R.id.tv_test);
        LogUtils.d("测试dialog");

        tv_test.setText("测试动态");

    }
}
