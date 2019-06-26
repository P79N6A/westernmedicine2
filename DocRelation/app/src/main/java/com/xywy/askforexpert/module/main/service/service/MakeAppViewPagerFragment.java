package com.xywy.askforexpert.module.main.service.service;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.model.QueData;
import com.xywy.askforexpert.widget.view.TitleIndicator;
import com.xywy.askforexpert.widget.view.TitleIndicator.OnTitleIndicatorListener;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 功能：服务-预约加号
 * </p>
 *
 * @author liuxuejiao
 * @2015-5-13 上午 11:27:30
 */
public class MakeAppViewPagerFragment extends Fragment implements
        OnTitleIndicatorListener {

    private ViewPager pager;

    private RelativeLayout layout;

    private TabPagerAdapter adapter;

    private List<Fragment> pagerItemList = new ArrayList<Fragment>();

    private List<QueData> mLables;

    private int first;
    private int isJump;

    private int updatePosition;

    /**
     * view title
     */
    private TitleIndicator mTitleIndicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        MobclickAgent.openActivityDurationTrack(false);
        Bundle budle = getArguments();
        ArrayList<QueData> list = (ArrayList<QueData>) budle
                .getSerializable("mLables");
        // list.add(new QueData("等待确认", "1", 5));
        // list.add(new QueData("等待就诊", "2", 5));
        // list.add(new QueData("成功就诊", "3", 5));
        // list.add(new QueData("爽约", "4", 5));
        mLables = list;
        first = budle.getInt("first");
        isJump = budle.getInt("isJump");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_make_app_content,
                container, false);

        pager = (ViewPager) view.findViewById(R.id.make_app_pager);
        layout = (RelativeLayout) view.findViewById(R.id.make_app_title_bar);
        mTitleIndicator = new TitleIndicator(getActivity(), mLables);
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        layout.addView(mTitleIndicator, params);
        mTitleIndicator.setOnTitleIndicatorListener(this);

        for (int i = 0; i < mLables.size(); i++) {
//			MakeAppointFragment fragment = new MakeAppointFragment(mLables.get(i).getStatus(), i);
            MakeAppointFragment fragment = MakeAppointFragment.newInstance(mLables.get(i).getStatus(), i);
            fragment.getTag();
            pagerItemList.add(fragment);
        }

        pager.setOffscreenPageLimit(mLables.size());

        adapter = new TabPagerAdapter(getFragmentManager());

        pager.setAdapter(adapter);

        pager.invalidate();

        adapter.notifyDataSetChanged();

        return view;
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

            if (myPageChangeListener != null) {
                myPageChangeListener.onPageSelected(position);
            }

            mTitleIndicator.setTabsDisplay(getActivity(), position);
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

        @Override
        public Object instantiateItem(View container, int position) {
            // TODO Auto-generated method stub

            return super.instantiateItem(container, position);
        }
    }

    public boolean isFirst() {
        return pager.getCurrentItem() == 0;
    }

    public boolean isEnd() {
        return pager.getCurrentItem() == pagerItemList.size() - 1;
    }

    private MyPageChangeListener myPageChangeListener;

    public void setMyPageChangeListener(MyPageChangeListener l) {

        myPageChangeListener = l;

    }

    public interface MyPageChangeListener {
        void onPageSelected(int position);
    }

    @Override
    public void onIndicatorSelected(int index) {
        // TODO Auto-generated method stub
        pager.setCurrentItem(index);

    }

    public void onResume() {
        super.onResume();
        StatisticalTools.onResume(getActivity());
    }

    public void onPause() {
        super.onPause();
        StatisticalTools.onPause(getActivity());
    }

}
