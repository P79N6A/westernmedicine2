package com.xywy.askforexpert.module.information;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseFragment;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.model.main.SubscribeTitleListBean;
import com.xywy.askforexpert.module.main.home.HomeNewsTabAdapter;
import com.xywy.askforexpert.module.main.home.HomePageCacheUtils;
import com.xywy.askforexpert.module.main.home.HomeService;
import com.xywy.askforexpert.module.main.subscribe.SubscribeListActivity;
import com.xywy.askforexpert.widget.homeWidget.tabs.com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * 资讯
 * Created by xg on 2018/3/30.
 */

public class InfoFragment extends YMBaseFragment {
    private long lastUpDateTime = 0;
    private long REFRESH_TIME_GAP = 15 * 60 * 100;//15分钟

    @Bind(R.id.tabs)
    PagerSlidingTabStrip pagerSlidingTabStrip;

    @Bind(R.id.viewPager)
    ViewPager viewPager;

    private HomeNewsTabAdapter mAdapter;

    /**
     * 订阅 Title List
     */
    private List<SubscribeTitleListBean.SubscribeBean> subscribeTitleList = new ArrayList<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_info;
    }

    @Override
    protected void initView() {
        CommonUtils.initSystemBar(getActivity());
        hideStatusBarHight();
        showCommonBaseTitle();
        titleBarBuilder.setBackGround(R.drawable.toolbar_bg_no_alpha_new);
        titleBarBuilder.setTitleText("资讯");
        titleBarBuilder.hideBackIcon();
        pagerSlidingTabStrip.setTextColor(Color.parseColor("#333333"));
        pagerSlidingTabStrip.setTypeface(null, Typeface.NORMAL);
    }


    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public String getStatisticalPageName() {
        return "InfoFragment";
    }


    //stone 加载过,刷新
    public void refresh() {
        long curTimeMillis = System.currentTimeMillis();
        if (curTimeMillis - lastUpDateTime > REFRESH_TIME_GAP) {
            //刷新数据
            requestServeNewsList();
        }else {
            //读取缓存的数据
            getNewsListInFile();
        }
    }


    /**
     * 获取本地定制服务与订阅列表 stone
     */
    public void getNewsListInFile() {
        //取本地缓存
        SubscribeTitleListBean subscribeTitleListBean = HomePageCacheUtils.getSubscribeTitleListBean(YMUserService.getCurUserId());

        initOrUpdateTabTitleInfo(subscribeTitleListBean);
    }

    /**
     * 请求定制服务与订阅列表
     */
    public void requestServeNewsList() {
        lastUpDateTime = System.currentTimeMillis();

        //请求网络最新数据
        HomeService.getSubscribeTitleListInfo(new CommonResponse<SubscribeTitleListBean>() {
            @Override
            public void onNext(SubscribeTitleListBean subscribeTitleListBean) {
                initOrUpdateTabTitleInfo(subscribeTitleListBean);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    private void initOrUpdateTabTitleInfo(SubscribeTitleListBean subscribeTitleListBean) {
        if (null != subscribeTitleListBean) {
            List<SubscribeTitleListBean.SubscribeBean> subscribeBeanList = subscribeTitleListBean.getSubscribe();
            if (null != subscribeBeanList && !subscribeBeanList.isEmpty()) {
                //清空数据
                subscribeTitleList.clear();
                for (SubscribeTitleListBean.SubscribeBean subscribeBean : subscribeBeanList) {
                    if (2 == subscribeBean.getType()) {
                        // 固定的服务
                        subscribeTitleList.add(subscribeBean);
                    }
                }
                for (SubscribeTitleListBean.SubscribeBean subscribeBean : subscribeBeanList) {
                    if (1 == subscribeBean.getType()) {
                        // 订制的服务
                        subscribeTitleList.add(subscribeBean);
                    }
                }

                if (null == mAdapter) {
                    // TODO: 2017/10/24 stone 此处应为getChildFragmentManager
//                    mAdapter = new HomeNewsTabAdapter(getFragmentManager(), subscribeTitleList);
                    mAdapter = new HomeNewsTabAdapter(getChildFragmentManager(), subscribeTitleList);
                    viewPager.setAdapter(mAdapter);
                    pagerSlidingTabStrip.setViewPager(viewPager);
                } else {
                    mAdapter.setSubscribeBeanList(subscribeTitleList);
                    mAdapter.notifyDataSetChanged();
                    pagerSlidingTabStrip.notifyDataSetChanged();
                }
            }
        }
    }

    @OnClick({R.id.tv_subscribe})
    public void onClick(View v) {
        //点击事件 事件分发处理
        switch (v.getId()) {
            case R.id.tv_subscribe:
                if (YMUserService.isLoginUser(getActivity())) {
                    StatisticalTools.eventCount(getActivity(), "Subscribe");
                    startActivity(new Intent(getActivity(), SubscribeListActivity.class).putExtra("isShouldShowAnim", true));
                    getActivity().overridePendingTransition(R.anim.bottom_in, R.anim.stable);
                }
                break;

            default:
                break;
        }
    }
}
