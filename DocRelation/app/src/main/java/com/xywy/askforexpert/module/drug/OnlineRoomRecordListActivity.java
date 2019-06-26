package com.xywy.askforexpert.module.drug;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;

/**
 * 在线诊室诊疗记录 stone
 */
public class OnlineRoomRecordListActivity extends YMBaseActivity {

    private OnlineRoomRecordListPagerFragment pagerFragment;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, OnlineRoomRecordListActivity.class));
    }

    @Override
    protected void initView() {
        initTitleBar();
        if (pagerFragment == null) {
            pagerFragment = new OnlineRoomRecordListPagerFragment();
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
        titleBarBuilder.setTitleText("接诊记录");
    }

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
