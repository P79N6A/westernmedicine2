package com.xywy.askforexpert.module.main.home;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseDialogFragment;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 新增留言设置弹出层 stone
 */
public class AddMessageSettingGuideDialogFragment extends YMBaseDialogFragment {


    @Bind(R.id.root_guide)
    View root_guide;

    @Bind(R.id.iv_guide)
    View iv_guide;

    private boolean mDismiss;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.add_message_setting_guide_dialog;
    }

    @Override
    public void initView() {
        CommonUtils.initSystemBar(getActivity());


        Window window = getDialog().getWindow();
//        window.setGravity(Gravity.BOTTOM); //可设置dialog的位置
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
        //无标题
//        window.requestFeature(Window.FEATURE_NO_TITLE);//必须放在setContextView之前调用
        //透明状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //设置背景颜色,只有设置了这个属性,宽度才能全屏MATCH_PARENT
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);


    }

    @Override
    public void initData(Bundle savedInstanceState) {
        YMApplication.applicationHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mDismiss) {
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) iv_guide.getLayoutParams();
                    layoutParams.topMargin = AppUtils.dpToPx(100, getResources()) + (YMApplication.getStatusBarHeight() > 0 ? YMApplication.getStatusBarHeight() : AppUtils.dpToPx(15, getResources()));
                    iv_guide.setLayoutParams(layoutParams);
                }
            }
        }, 100);
    }

    @OnClick({R.id.root_guide})
    public void onClick(View v) {
        //点击事件 事件分发处理
        switch (v.getId()) {

            case R.id.root_guide:
                getDialog().dismiss();
                break;

            default:
                break;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mDismiss = true;
    }
}
