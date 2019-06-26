package com.xywy.askforexpert.module.liveshow.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.xywy.askforexpert.module.liveshow.bean.LiveShowTypeBean;
import com.xywy.askforexpert.module.liveshow.fragment.LiveShowRecordListFragment;
import com.xywy.askforexpert.module.liveshow.fragment.MyFansListFragment;

import java.util.List;

/**
 * Created by bailiangjin on 2017/02/23.
 */

public class MyLiveShowTabAdapter extends FragmentPagerAdapter {
    private List<LiveShowTypeBean> liveShowTypeBeanList;

    private LiveShowRecordListFragment liveShowRecordListFragment;

    private MyFansListFragment myFansListFragment;


    public MyLiveShowTabAdapter(FragmentManager fm, List<LiveShowTypeBean> liveShowTypeBeanList) {
        super(fm);
        this.liveShowTypeBeanList = liveShowTypeBeanList;

        liveShowRecordListFragment = new LiveShowRecordListFragment();
        myFansListFragment = new MyFansListFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(final int position) {

        switch (position) {
            case 0:
                return liveShowRecordListFragment;
            case 1:
                return myFansListFragment;

        }
        return liveShowRecordListFragment;
    }

    @Override
    public CharSequence getPageTitle(final int position) {

        switch (position) {
            case 0:
                return "直播记录";
            case 1:
                return "我的粉丝";
            default:
                return null;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    public List<LiveShowTypeBean> getLiveShowTypeBeanList() {
        return liveShowTypeBeanList;
    }

    public void setLiveShowTypeBeanList(List<LiveShowTypeBean> liveShowTypeBeanList) {
        this.liveShowTypeBeanList = liveShowTypeBeanList;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
