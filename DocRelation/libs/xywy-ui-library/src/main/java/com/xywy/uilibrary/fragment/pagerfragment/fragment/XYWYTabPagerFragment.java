package com.xywy.uilibrary.fragment.pagerfragment.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xywy.uilibrary.R;
import com.xywy.uilibrary.fragment.XywySuperBaseFragment;
import com.xywy.uilibrary.fragment.pagerfragment.adapter.BaseTabPageBean;
import com.xywy.uilibrary.fragment.pagerfragment.adapter.XywyFragmentPagerAdapter;
import com.xywy.uilibrary.view.SupportNoScrollViewpager;

import java.util.List;


/**
 * Created by bailiangjin on 2017/3/22.
 */

public abstract class XYWYTabPagerFragment<T extends BaseTabPageBean> extends XywySuperBaseFragment {

    protected boolean hasDivider = true;//分割线

    protected SupportNoScrollViewpager viewPager;

    TabLayout tabLayout;

    Fragment curFragment;

    View[] tabViewArray;

    private static int DEFAULT_NORMAL_TEXT_COLOR = R.color.tv_black_333;
    private static int DEFAULT_SELECTED_TEXT_COLOR = R.color.color_00c8aa;
    private static int DEFAULT_BACKGROUND_RESID = R.color.white;
    private static int DEFAULT_TAB_INDICATOR_COLOR = R.color.color_00c8aa;

    protected XywyFragmentPagerAdapter fragmentPagerAdapter;

    public XywyFragmentPagerAdapter<T> getFragmentPagerAdapter() {
        return fragmentPagerAdapter;
    }

    public void setFragmentPagerAdapter(XywyFragmentPagerAdapter<T> fragmentPagerAdapter) {
        initViewPagerAndTab(fragmentPagerAdapter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_with_fragment_viewpager;
    }


    @Override
    protected void initView() {
        viewPager = (SupportNoScrollViewpager) rootView.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);

        if (hasDivider) {
            //stone 添加分割线
            LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
            linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            linearLayout.setDividerDrawable(ContextCompat.getDrawable(getActivity(),
                    R.drawable.layout_divider_vertical));
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        initViewPagerAndTab(getAdapter());
    }

    private void initViewPagerAndTab(XywyFragmentPagerAdapter fragmentPagerAdapter) {
        XYWYTabPagerFragment.this.fragmentPagerAdapter = fragmentPagerAdapter;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                curFragment = XYWYTabPagerFragment.this.fragmentPagerAdapter.getItem(position);
                if (isSpecialTab()) {
                    setTabSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        curFragment = fragmentPagerAdapter.getItem(0);
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        setSelectedTabIndicatorColor(getSelectedTabIndicatorColor() <= 0 ? DEFAULT_TAB_INDICATOR_COLOR : getSelectedTabIndicatorColor());
        setTabBackground(getTabBackgroundResId() <= 0 ? DEFAULT_BACKGROUND_RESID : getTabBackgroundResId());
        setTabMode(getTabMode());

        if (isSpecialTab()) {
            int tabSize = fragmentPagerAdapter.getTabList().size();
            List<BaseTabPageBean> tabPageBeanList = fragmentPagerAdapter.getTabList();
            tabViewArray = new View[tabSize];
            for (int i = 0; i < tabSize; i++) {
                View curTabView = setTabView(i, getTabLayoutResId());
                if (getTabTitleTextViewResId() > 0) {
                    ((TextView) curTabView.findViewById(getTabTitleTextViewResId())).setText(tabPageBeanList.get(i).getTitle());
                }
                tabViewArray[i] = curTabView;
            }
            setTabSelected(0);
            onTabInit(tabViewArray);
        } else {
            setTabTextColors(getNormalTabTextColor() <= 0 ? DEFAULT_NORMAL_TEXT_COLOR : getNormalTabTextColor(), getSelectedTabTextColor() <= 0 ? DEFAULT_SELECTED_TEXT_COLOR : getSelectedTabTextColor());
        }
    }

    private void setTabSelected(int curIndex) {
        for (int i = 0; i < tabViewArray.length; i++) {
            tabLayout.getTabAt(i).getCustomView().setSelected(curIndex == i ? true : false);
        }
    }

    public void setScroll(boolean isCanScroll) {
        if (null != viewPager) {
            viewPager.setNoScroll(!isCanScroll);
        }
    }


    /**
     * 隐藏 tab layout
     */
    public void hideTab() {
        if (null != tabLayout) {
            tabLayout.setVisibility(View.GONE);
        }

    }

    /**
     * 显示 tab layout
     */
    public void showTab() {
        if (null != tabLayout) {
            tabLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setTabTextColors(int normalColor, int selectedColor) {
        tabLayout.setTabTextColors(getActivity().getResources().getColor(normalColor), getActivity().getResources().getColor(selectedColor));

    }

    private void setSelectedTabIndicatorColor(int selectedColor) {
        tabLayout.setSelectedTabIndicatorColor(getActivity().getResources().getColor(selectedColor));
    }


    private void setTabMode(int mode) {
        tabLayout.setTabMode(mode);
    }


    private void setTabBackground(int bgResId) {
        tabLayout.setBackgroundResource(bgResId);

    }


    /**
     * 未选中文字颜色
     *
     * @return
     */
    protected int getNormalTabTextColor() {
        return 0;
    }

    /**
     * 选中文字颜色
     *
     * @return
     */
    protected int getSelectedTabTextColor() {
        return 0;
    }

    /**
     * 选中状态下划线颜色
     *
     * @return
     */
    protected int getSelectedTabIndicatorColor() {
        return 0;
    }

    /**
     * tablayout 背景 ResId
     *
     * @return
     */
    protected int getTabBackgroundResId() {
        return 0;
    }


    /**
     * 返回自定义Tab中 标题所使用的 TextView的ResId
     *
     * @return
     */
    protected int getTabTitleTextViewResId() {
        return 0;
    }


    /**
     * 自定义tab 回调 tabView数组
     *
     * @param tabViewArray
     */
    protected void onTabInit(View[] tabViewArray) {

    }

    /**
     * tablayout mode
     *
     * @return
     */
    protected int getTabMode() {
        return TabLayout.MODE_SCROLLABLE;
    }

    //自定义Tab

    /**
     * 自定义tab item layoutId 不覆盖即不是自定义tab
     *
     * @return
     */
    protected int getTabLayoutResId() {
        return 0;
    }


    /**
     * 是否为自定义tab
     *
     * @return
     */
    private boolean isSpecialTab() {
        return getTabLayoutResId() > 0;
    }


    /**
     * 获取当前显示的fragment
     *
     * @return
     */
    public Fragment getCurFragment() {
        return curFragment;
    }


    public abstract XywyFragmentPagerAdapter<T> getAdapter();


    public void backToFirstIndex() {
        if (null != viewPager && null != fragmentPagerAdapter && null != fragmentPagerAdapter.getTabList() && !fragmentPagerAdapter.getTabList().isEmpty()) {
            viewPager.setCurrentItem(0);
        }

    }

    /**
     * 添加tab切换监听
     *
     * @param onPageChangeListener
     */
    protected void addOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        if (null == onPageChangeListener) {
            throw new RuntimeException("onPageChangeListener can not be null");
        }
        viewPager.addOnPageChangeListener(onPageChangeListener);
    }


    private View setTabView(int index, int layoutResId) {
        int tabCount = tabLayout.getTabCount();
        if (index > tabCount - 1) {
            Log.e(getClass().getName(), "tab index out of tab size");
            throw new RuntimeException("tab index out of tab size");
        }
        TabLayout.Tab tab = tabLayout.getTabAt(index);
        tab.setCustomView(layoutResId);
        return tab.getCustomView();
    }

    public void setHasDivider(boolean hasDivider) {
        this.hasDivider = hasDivider;
    }
}
