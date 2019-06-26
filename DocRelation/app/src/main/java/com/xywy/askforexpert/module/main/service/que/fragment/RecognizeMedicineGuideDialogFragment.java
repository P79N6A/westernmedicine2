package com.xywy.askforexpert.module.main.service.que.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.RelativeLayout;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseDialogFragment;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 推荐用药 导引 弹出层
 * Author: blj
 * <p>
 * Date: 2017/05/05
 */
public class RecognizeMedicineGuideDialogFragment extends YMBaseDialogFragment {


    @Bind(R.id.rl_recognize_medicine_cover)
    RelativeLayout rl_recognize_medicine_cover;

    @Bind(R.id.v_bottom)
    View v_bottom;

    private boolean isDoubleLine = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
    }

    @Override
    public void beforeViewBind() {
        super.beforeViewBind();
        Bundle bundle = getArguments();
        isDoubleLine = bundle.getBoolean("isDoubleLine", false);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.dialog_recognize_medicine_cover;
    }

    @Override
    public void initView() {
        v_bottom.setVisibility(isDoubleLine ? View.VISIBLE : View.GONE);

    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @OnClick({R.id.rl_recognize_medicine_cover})
    public void onClick(View v) {
        //点击事件 事件分发处理
        switch (v.getId()) {

            case R.id.rl_recognize_medicine_cover:
                getDialog().dismiss();
                break;

            default:
                break;
        }

    }
}
