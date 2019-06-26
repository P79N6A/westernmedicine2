/**
 * @author shihao
 * @date 2015-5-8
 */
package com.xywy.askforexpert.module.main.service.service;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.model.QueData;
import com.xywy.askforexpert.module.my.clinic.PhoneDoctorSettingActivity;
import com.xywy.askforexpert.widget.view.TitleIndicator;
import com.xywy.askforexpert.widget.view.TitleIndicator.OnTitleIndicatorListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 电话医生 stone
 * @author shihao
 * 2015-5-13
 */
public class ServiceTelDoctor extends FragmentActivity implements
        OnTitleIndicatorListener {

    private ViewPager pager;

    private RelativeLayout layout;

    private TabPagerAdapter adapter;

    private List<Fragment> pagerItemList = new ArrayList<Fragment>();

    private List<QueData> mLables = null;

    /**
     * view title
     */
    private TitleIndicator mTitleIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        MobclickAgent.openActivityDurationTrack(false);
        setContentView(R.layout.activity_service_teldoctor);

        initView();
    }

    private void initView() {

        mLables = new ArrayList<QueData>();

        mLables.add(new QueData("待确定时间"));

        mLables.add(new QueData("待通话"));

        mLables.add(new QueData("通话完成"));

        mLables.add(new QueData("订单完成"));

        layout = (RelativeLayout) findViewById(R.id.rl_title_bar);

        pager = (ViewPager) findViewById(R.id.tel_pager);

        mTitleIndicator = new TitleIndicator(ServiceTelDoctor.this, mLables);

        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        layout.addView(mTitleIndicator, params);

        mTitleIndicator.setOnTitleIndicatorListener(this);

        for (int i = 0; i < mLables.size(); i++) {
//			TelItemFragment fragment = new TelItemFragment(i+1);
            TelItemFragment fragment = TelItemFragment.newInstance(i + 1);
            pagerItemList.add(fragment);
        }

        adapter = new TabPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);

        pager.setOffscreenPageLimit(mLables.size());

        pager.invalidate();

        adapter.notifyDataSetChanged();

    }

    private class TabPagerAdapter extends FragmentStatePagerAdapter implements
            ViewPager.OnPageChangeListener {

        public TabPagerAdapter(FragmentManager fm) {
            super(fm);
            pager.setOnPageChangeListener(this);
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int position) {

//			 if (myPageChangeListener != null)
//			 myPageChangeListener.onPageSelected(position);

            mTitleIndicator.setTabsDisplay(ServiceTelDoctor.this, position);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position < mLables.size()) {
                fragment = pagerItemList.get(position);
            } else {
                fragment = pagerItemList.get(0);
            }

            return fragment;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mLables.size();
        }

    }

    @Override
    public void onIndicatorSelected(int index) {
        pager.setCurrentItem(index);
    }

    public void onTelClickListener(View v) {
        switch (v.getId()) {
            case R.id.btn_tel_back:
                finish();

                break;

            case R.id.btn_tel_setting:
                Intent intent = new Intent(ServiceTelDoctor.this, PhoneDoctorSettingActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        StatisticalTools.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatisticalTools.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
