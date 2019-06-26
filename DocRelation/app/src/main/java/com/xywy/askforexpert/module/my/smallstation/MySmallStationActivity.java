package com.xywy.askforexpert.module.my.smallstation;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.widget.view.SlidingMenu;

/**
 * 我的小站
 *
 * @author 王鹏
 * @2015-5-9下午12:27:07
 */

public class MySmallStationActivity extends FragmentActivity {

    private SlidingMenu mSlidingMenu;
    private FragmentTransaction mTransaction;
    private Menu_right_fragment rightFragment;
    private MySmallStationFragment centerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        MobclickAgent.openActivityDurationTrack(false);
        setContentView(R.layout.consult_main_frag);
        ((TextView) findViewById(R.id.con_tv_title)).setText("我的小站");
        init();
    }

    private void init() {
        mSlidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
        mSlidingMenu.setCanSliding(false);
        mSlidingMenu.setRightView(getLayoutInflater().inflate(
                R.layout.right_frame, null));
        mSlidingMenu.setCenterView(getLayoutInflater().inflate(
                R.layout.center_frame, null));
        mTransaction = this.getSupportFragmentManager().beginTransaction();
        rightFragment = new Menu_right_fragment();
        mTransaction.replace(R.id.right_frame, rightFragment);
        centerFragment = new MySmallStationFragment();

        mTransaction.replace(R.id.center_frame, centerFragment);
        mTransaction.commit();

    }

    public void onClickBack(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.btn2_add:
                mSlidingMenu.showRightView();
                break;
            default:
                break;
        }
    }

    public void onResume() {
        super.onResume();
        StatisticalTools.onResume(this);
    }

    public void onPause() {
        super.onPause();
        StatisticalTools.onPause(this);
    }
}
