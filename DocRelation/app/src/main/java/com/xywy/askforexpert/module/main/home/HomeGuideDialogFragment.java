package com.xywy.askforexpert.module.main.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ImageView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseDialogFragment;

import butterknife.Bind;
import butterknife.OnClick;

/**
 *
 * Home页 导引 弹出层
 * Author: blj
 *
 * Date: 2017/01/17
 */
public class HomeGuideDialogFragment extends YMBaseDialogFragment {


    @Bind(R.id.iv_media_guide)
    ImageView iv_media_guide;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_home_guide_dialog;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @OnClick({R.id.iv_media_guide})
    public void onClick(View v) {
        //点击事件 事件分发处理
        switch (v.getId()) {

            case R.id.iv_media_guide:
                getDialog().dismiss();
                break;

            default:
                break;
        }

    }
}
