package com.xywy.askforexpert.module.main.patient.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 2018/8/21.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList = new ArrayList<>();
    private String [] title;
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragments(List<Fragment> fragments,String [] title) {
        mFragmentList.addAll(fragments);
        this.title = title;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
