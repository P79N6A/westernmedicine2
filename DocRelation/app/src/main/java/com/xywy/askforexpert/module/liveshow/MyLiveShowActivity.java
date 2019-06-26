package com.xywy.askforexpert.module.liveshow;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.activity.WebViewActivity;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.uilibrary.titlebar.ItemClickListener;
import com.xywy.askforexpert.module.liveshow.adapter.MyLiveShowTabAdapter;
import com.xywy.askforexpert.module.liveshow.bean.LiveShowTypeBean;
import com.xywy.askforexpert.module.liveshow.constants.H5PageUrl;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 我的直播页面
 * Created by bailiangjin on 2017/2/23.
 */

public class MyLiveShowActivity extends YMBaseActivity {

    @Bind(R.id.viewPager)
    ViewPager viewPager;

    @Bind(R.id.tabLayout)
    TabLayout tabLayout;

    @Bind(R.id.iv_start_broadcast)
    ImageView iv_start_broadcast;

    MyLiveShowTabAdapter myLiveShowTabAdapter;
    List<LiveShowTypeBean> liveShowTypeBeanList = new ArrayList<>();

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, MyLiveShowActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_my_live_show;
    }

    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("我的直播");

        titleBarBuilder.addItem("设置", new ItemClickListener() {
            @Override
            public void onClick() {
                //  跳转 h5 页面
                WebViewActivity.start(MyLiveShowActivity.this,"直播信息修改", H5PageUrl.APPLY_FOR_LIVE_SHOW_URL);
            }
        }).build();

    }

    @Override
    protected void initData() {
        liveShowTypeBeanList.clear();
        liveShowTypeBeanList.add(new LiveShowTypeBean("1"));
        liveShowTypeBeanList.add(new LiveShowTypeBean("2"));

        myLiveShowTabAdapter = new MyLiveShowTabAdapter(getSupportFragmentManager(), liveShowTypeBeanList);
        viewPager.setAdapter(myLiveShowTabAdapter);

        tabLayout.setupWithViewPager(viewPager);

    }

    @OnClick({R.id.iv_start_broadcast})
    public void onClick(View v) {
        //点击事件 事件分发处理
        switch (v.getId()) {
            case R.id.iv_start_broadcast:
                if(YMUserService.checkLiveShowState(MyLiveShowActivity.this)){
                   // LiveManager.getInstance().startBroadcast(MyLiveShowActivity.this);
                }
                break;
        }
    }


}
