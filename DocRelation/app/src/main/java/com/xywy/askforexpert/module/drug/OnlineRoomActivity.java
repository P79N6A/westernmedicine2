package com.xywy.askforexpert.module.drug;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.widget.view.SelectBasePopupWindow;
import com.xywy.uilibrary.titlebar.ItemClickListener;

/**
 * 在线诊室主页 stone
 */
public class OnlineRoomActivity extends YMBaseActivity {

    private View lay1;
    private View lay2;
    private View lay3;
    private View popRoot;

    private OnlineRoomPagerFragment pagerFragment;

    private SelectBasePopupWindow mPopupWindow;
    private int pageItem;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, OnlineRoomActivity.class));
    }

    @Override
    protected void initView() {
        initTitleBar();
        if (pagerFragment == null) {
            pagerFragment = new OnlineRoomPagerFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("pageItem", pageItem);
            pagerFragment.setArguments(bundle);
            displayFragment(pagerFragment);

        }

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_online_room;
    }

    private void displayFragment(Fragment fragment) {
        if (!fragment.isAdded()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fl_container, fragment);
            transaction.commitAllowingStateLoss();
        }
    }

    private void initTitleBar() {
        titleBarBuilder.setTitleText("问诊用药");

        titleBarBuilder.addItem("设置", new ItemClickListener() {
            @Override
            public void onClick() {
                startActivity(new Intent(OnlineRoomActivity.this, OnlineRoomFeeEditActivity.class));
//                showPop();
            }
        }).build();
        pageItem = getIntent().getIntExtra("pageItem",0);
    }

    private void showPop() {
        if (mPopupWindow == null) {
            mPopupWindow = new SelectBasePopupWindow(true, this);
            popRoot = View.inflate(getBaseContext(), R.layout.pop_layout_onlineroom2, null);
            lay1 = popRoot.findViewById(R.id.lay1);
            lay2 = popRoot.findViewById(R.id.lay2);
            lay3 = popRoot.findViewById(R.id.lay3);
            lay1.setOnClickListener(mPopOnClickListener);
            lay2.setOnClickListener(mPopOnClickListener);
            lay3.setOnClickListener(mPopOnClickListener);
        }
        if (!mPopupWindow.isShowing()) {
//            mPopupWindow.init(popRoot).showAtLocation(this.getWindow().getDecorView(), Gravity.TOP | Gravity.RIGHT, (AppUtils.dpToPx(48, getResources()) - 24) / 2 + AppUtils.dpToPx(5, getResources()) - 30 - 27, AppUtils.dpToPx(48, getResources()) + YMApplication.getStatusBarHeight() - 30);
            mPopupWindow.init(popRoot).showAtLocation(this.getWindow().getDecorView(), Gravity.TOP | Gravity.RIGHT, AppUtils.dpToPx(5, getResources()), AppUtils.dpToPx(48, getResources()) + YMApplication.getStatusBarHeight());
        }
    }


    /**
     * pop监听器
     */
    private View.OnClickListener mPopOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mPopupWindow.dismiss();
            switch (view.getId()) {
                case R.id.lay1:
                    startActivity(new Intent(OnlineRoomActivity.this, OnlineRoomRecordListActivity.class));
                    break;
                case R.id.lay2:
                    startActivity(new Intent(OnlineRoomActivity.this, PrescriptionRecordListActivity.class));
                    break;
                case R.id.lay3:
                    startActivity(new Intent(OnlineRoomActivity.this, OnlineRoomSettingActivity.class));
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus && YMApplication.getIsFirstShowOnlineNotice()) {
//            YMApplication.setIsFirstShowOnlineNotice(false);
//            showPop();
//            new AddMessageSettingGuideDialogFragment().show(getSupportFragmentManager(), "ConsultOnlineActivity");
//        }
    }

}
