package com.xywy.askforexpert.module.drug;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.model.consultentity.ConsultPagerTabBean;
import com.xywy.askforexpert.model.consultentity.ConsultPagerTabItemBean;
import com.xywy.uilibrary.fragment.pagerfragment.adapter.XywyFragmentPagerAdapter;
import com.xywy.uilibrary.fragment.pagerfragment.fragment.XYWYTabPagerFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


/**
 * 在线诊室诊疗记录 stone
 */
public class OnlineRoomRecordListPagerFragment extends XYWYTabPagerFragment<ConsultPagerTabBean> {

    private XywyFragmentPagerAdapter<ConsultPagerTabBean> pagerAdapter;
    private List<Fragment> fragments;
    private List<ConsultPagerTabBean> consultPagerTabBeanList;

    private static final String ALL = "全部";
    private static final String END = "已结束";
    private static final String EXIT = "已退费";

    @Override
    protected void beforeViewBind() {
        //去分割线
        setHasDivider(false);
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
        viewPager.setOffscreenPageLimit(2);
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
        ConsultPagerTabItemBean itemBean = new ConsultPagerTabItemBean(0, ALL);
        consultPagerTabBeanList.add(new ConsultPagerTabBean(ALL, itemBean));
        itemBean = new ConsultPagerTabItemBean(1, END);
        consultPagerTabBeanList.add(new ConsultPagerTabBean(END, itemBean));
        itemBean = new ConsultPagerTabItemBean(2, EXIT);
        consultPagerTabBeanList.add(new ConsultPagerTabBean(EXIT, itemBean));

        fragments = new ArrayList<>();
        fragments.add(OnlineRoomItemFragment.newInstance(2,0, null));
        fragments.add(OnlineRoomItemFragment.newInstance(2,1, null));
        fragments.add(OnlineRoomItemFragment.newInstance(2,2 ,null));
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


}
