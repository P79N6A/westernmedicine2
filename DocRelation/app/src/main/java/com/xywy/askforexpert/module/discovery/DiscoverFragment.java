package com.xywy.askforexpert.module.discovery;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.fragment.YMListFragment;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.module.discovery.adapter.DiscoverBaseAdapter;
import com.xywy.askforexpert.module.discovery.adapter.discovermain.bean.DiscoverItemBean;
import com.xywy.askforexpert.module.discovery.adapter.discovermain.bean.DiscoverItemType;
import com.xywy.askforexpert.module.discovery.adapter.discovermain.bean.DiscoverServiceInnerItem;
import com.xywy.askforexpert.module.discovery.adapter.discovermain.bean.DiscoverServiceItem;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 发现 fragment stone
 * <p>
 * author bailiangjin 2016-12-28
 */
public class DiscoverFragment extends YMListFragment {

    private DiscoverBaseAdapter discoverItemAdapter;

    private List<DiscoverItemBean> itemList = new ArrayList<>();

    @Override
    protected int getNoDataLayoutResId() {
        return R.layout.activity_bind_phone;
    }

    @Override
    protected DiscoverBaseAdapter getListRvAdapter() {

        showCommonBaseTitle();
        if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
            titleBarBuilder.setTitleText("首页");
        } else {
            titleBarBuilder.setTitleText("发现");
        }
        titleBarBuilder.hideBackIcon();

        discoverItemAdapter = new DiscoverBaseAdapter(getActivity());
        discoverItemAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                DiscoverItemBean item = discoverItemAdapter.getItem(position);

                switch (item.getType()) {
                    case DiscoverItemBean.TYPE_LIST:
                        break;
                    case DiscoverItemBean.TYPE_SERVICE:
                    case DiscoverItemBean.TYPE_LIVE_SHOW:
                        //do nothing
                        break;
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        return discoverItemAdapter;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        itemList.clear();

        //医生助手特有的药品助手模块 stone
        if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
            itemList.add(new DiscoverItemBean(DiscoverItemBean.TYPE_MEDICINE));
        }

        //添加统一的服务入口，不管是何种身份
        addServiceHeader();

        discoverItemAdapter.setData(itemList);
        discoverItemAdapter.notifyDataSetChanged();
    }

    private void addServiceHeader() {
        List<DiscoverServiceInnerItem> innerItemList = new ArrayList<>();
        innerItemList.add(0,new DiscoverServiceInnerItem(DiscoverItemType.EXAM_PAPER, "医学试题", R.drawable.icon_discover_exam));
        innerItemList.add(1,new DiscoverServiceInnerItem(DiscoverItemType.CLINIC_BOOK, "临床指南",R.drawable.discover_guide));
        innerItemList.add(2,new DiscoverServiceInnerItem(DiscoverItemType.CHECK_BOOK, "检查手册", R.drawable.discover_docs));
        innerItemList.add(3,new DiscoverServiceInnerItem(DiscoverItemType.MEDICINE_HELPER, "用药助手", R.drawable.discover_yaodian));
        innerItemList.add(4,new DiscoverServiceInnerItem(DiscoverItemType.FIND_JOB, "找工作", R.drawable.discover_recruit));
        DiscoverServiceItem discoverServiceItem = new DiscoverServiceItem(innerItemList);
        itemList.add(new DiscoverItemBean(discoverServiceItem));
    }

    @Override
    public void onResume() {
        super.onResume();
        StatisticalTools.fragmentOnResume("DiscoverFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        StatisticalTools.fragmentOnPause("DiscoverFragment");
    }

    @Override
    public String getStatisticsId() {
        return null;
    }


    @Override
    protected int getDividerColorResId() {
        return R.color.app_common_divider_color;
    }

}
