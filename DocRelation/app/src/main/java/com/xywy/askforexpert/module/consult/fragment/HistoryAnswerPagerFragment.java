package com.xywy.askforexpert.module.consult.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.consultentity.ConsultPagerTabBean;
import com.xywy.askforexpert.model.consultentity.ConsultPagerTabItemBean;
import com.xywy.uilibrary.fragment.pagerfragment.adapter.XywyFragmentPagerAdapter;
import com.xywy.uilibrary.fragment.pagerfragment.fragment.XYWYTabPagerFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 历史回复 切换tab stone 总结+未总结
 */
public class HistoryAnswerPagerFragment extends XYWYTabPagerFragment<ConsultPagerTabBean> implements ConsultItemFragment.IModifyMsgTip {

    private XywyFragmentPagerAdapter<ConsultPagerTabBean> pagerAdapter;
    private List<Fragment> fragments;
    private List<ConsultPagerTabBean> consultPagerTabBeanList;
    private static final String NOT_SUMUP = "未总结";
    private static final String ALL = "全部";

    private View[] tabViewArray;
    private ITip iTip;

    @Override
    protected void beforeViewBind() {
    }

    @Override
    protected void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    protected void unBindView() {
        ButterKnife.unbind(this);
    }

    @Override
    protected void initListener() {

    }

    @Override
    public XywyFragmentPagerAdapter<ConsultPagerTabBean> getAdapter() {
        if (pagerAdapter == null) {
            initData();
            pagerAdapter = new XywyFragmentPagerAdapter<ConsultPagerTabBean>(getChildFragmentManager(), consultPagerTabBeanList) {
                @Override
                protected Fragment getItemFragment(int position, ConsultPagerTabBean tabData) {
                    return fragments.get(position);
                }
            };
        }
        return pagerAdapter;
    }

    private void initData() {
        consultPagerTabBeanList = new ArrayList<>();
        ConsultPagerTabItemBean itemBean = new ConsultPagerTabItemBean(ConsultPagerTabItemBean.TYPE_ANSWERED, NOT_SUMUP);
        consultPagerTabBeanList.add(new ConsultPagerTabBean(NOT_SUMUP, itemBean));
        itemBean = new ConsultPagerTabItemBean(ConsultPagerTabItemBean.TYPE_ANSWERED, ALL);
        consultPagerTabBeanList.add(new ConsultPagerTabBean(ALL, itemBean));

        fragments = new ArrayList<>();
        fragments.add(ConsultItemFragment.newInstance(consultPagerTabBeanList.get(0), this, true,1));
        fragments.add(ConsultItemFragment.newInstance(consultPagerTabBeanList.get(1), this, true,2));
        addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (iTip != null) {
                    switch (position) {
                        case 0:
                            iTip.showTip(getResources().getString(R.string.consult_bottom_tip));
                            break;
                        case 1:
                            iTip.showTip(getResources().getString(R.string.question_pool_tip));
                            break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public static interface ITip {
        void showTip(String text);
    }

    public ITip getiTip() {
        return iTip;
    }

    public void setiTip(ITip iTip) {
        this.iTip = iTip;
    }

    /**
     * tablayout mode
     *
     * @return
     */
    protected int getTabMode() {
        return TabLayout.MODE_FIXED;
    }

    @Override
    protected int getTabLayoutResId() {
        return R.layout.item_tab_online_consult;
    }

    @Override
    protected int getTabTitleTextViewResId() {
        return R.id.tv_title;
    }

    @Override
    protected int getSelectedTabIndicatorColor() {
        return R.color.color_00c8aa;
    }

    @Override
    protected void onTabInit(View[] tabViewArray) {
        super.onTabInit(tabViewArray);
        this.tabViewArray = tabViewArray;
    }

    @Override
    public void updateMsgCount(int tab, String count) {
        if (tabViewArray == null || tabViewArray[tab] == null) {
            return;
        }
        TextView tvCount = (TextView) tabViewArray[tab].findViewById(R.id.tv_msg_number);
        if (tvCount != null) {
            if (count.equals("0")) {
                tvCount.setVisibility(View.INVISIBLE);
            } else {
                tvCount.setVisibility(View.VISIBLE);
                tvCount.setText(count);
            }
        }
    }

    @Override
    public void loadProcessingData() {

    }


}
