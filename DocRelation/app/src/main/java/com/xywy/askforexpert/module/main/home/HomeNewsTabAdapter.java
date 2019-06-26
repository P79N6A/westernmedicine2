package com.xywy.askforexpert.module.main.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.model.main.SubscribeTitleListBean;
import com.xywy.askforexpert.module.main.news.NewsListFragment;

import java.util.List;

/**
 * Created by bailiangjin on 2017/1/12.
 */

public class HomeNewsTabAdapter extends FragmentStatePagerAdapter {
    private List<SubscribeTitleListBean.SubscribeBean> subscribeBeanList;


    public HomeNewsTabAdapter(FragmentManager fm, List<SubscribeTitleListBean.SubscribeBean> subscribeBeanList) {
        super(fm);
        this.subscribeBeanList = subscribeBeanList;
    }

    @Override
    public int getCount() {
        return null == subscribeBeanList ? 0 : subscribeBeanList.size();
    }

    @Override
    public Fragment getItem(final int position) {

        int id = null == subscribeBeanList || position >= subscribeBeanList.size() ? 0 : subscribeBeanList.get(position).getId();
        LogUtils.d("tabId:" + id);
        return NewsListFragment.newInstance(id);
    }

    @Override
    public CharSequence getPageTitle(final int position) {

        String title = null == subscribeBeanList || position >= subscribeBeanList.size() ? "" : subscribeBeanList.get(position).getName();
        LogUtils.d("title:" + title);
        return title;
    }


    public List<SubscribeTitleListBean.SubscribeBean> getSubscribeBeanList() {
        return subscribeBeanList;
    }

    public void setSubscribeBeanList(List<SubscribeTitleListBean.SubscribeBean> subscribeBeanList) {
        this.subscribeBeanList = subscribeBeanList;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
