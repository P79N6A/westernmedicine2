package com.xywy.askforexpert.module.liveshow.adapter.liveshowlist;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.xywy.askforexpert.module.liveshow.bean.LiveShowListTabBean;
import com.xywy.askforexpert.module.liveshow.fragment.LiveShowListFragment;

import java.util.List;

/**
 * Created by bailiangjin on 2017/02/23.
 */

public class LiveShowListTabAdapter extends FragmentPagerAdapter {
    private List<LiveShowListTabBean> liveShowListTabBeanList;

    public LiveShowListTabAdapter(FragmentManager fm, List<LiveShowListTabBean> liveShowListTabBeanList) {
        super(fm);
        this.liveShowListTabBeanList = liveShowListTabBeanList;
    }

    @Override
    public int getCount() {
        return liveShowListTabBeanList.size();
    }

    @Override
    public Fragment getItem(final int position) {
        return LiveShowListFragment.getInstance(getDataItem(position).getState(),getDataItem(position).getCate_id());
    }

    @Override
    public CharSequence getPageTitle(final int position) {
        return getDataItem(position).getTitleStr();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    private LiveShowListTabBean getDataItem(int position ){
        return liveShowListTabBeanList.get(position);
    }
}
