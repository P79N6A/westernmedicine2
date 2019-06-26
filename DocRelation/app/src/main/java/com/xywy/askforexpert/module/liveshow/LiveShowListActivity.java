package com.xywy.askforexpert.module.liveshow;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.module.liveshow.adapter.liveshowlist.LiveShowListTabAdapter;
import com.xywy.askforexpert.module.liveshow.bean.LiveShowListTabBean;
import com.xywy.askforexpert.widget.liveshow.ChannelLayout;

import net.tsz.afinal.core.Arrays;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 直播列表页面
 * Created by bailiangjin on 2017/2/23.
 */

public class LiveShowListActivity extends YMBaseActivity {

    @Bind(R.id.viewPager)
    ViewPager viewPager;

    @Bind(R.id.tabLayout)
    TabLayout tabLayout;

    @Bind(R.id.channel_layout)
    ChannelLayout mChannelLayout;

    @Bind(R.id.iv_arrow)
    ImageView btnChannel;


    public static final String[] titleArray = {"推荐", "直播", "关注", "名医直播", "医学美容", "肿瘤", "健身瑜伽", "心理科", "妇儿"};
    public static final String[] stateArray = {"1", null, "2", null, null, null, null, null, null};
    public static final String[] cateIdArray = {null, null, null, "1", "2", "3", "4", "5", "6"};

    private List<String> titleList = new ArrayList<>(Arrays.asList(titleArray));

    LiveShowListTabAdapter myLiveShowTabAdapter;
    List<LiveShowListTabBean> liveShowListTabBeanList = new ArrayList<>();

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, LiveShowListActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_live_show_list;
    }

    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("直播列表");
        initChannel();

//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

    }

    /**
     * 初始化频道页
     */
    private void initChannel() {
        btnChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChannelLayout.isLayoutOpen()) {
                    close();
                } else {
                    open();
                }
            }
        });
        //GridView 点击相应事件
        mChannelLayout.setOnDragViewClickListener(new ChannelLayout.OnDragViewClickListener() {

            @Override
            public void onClick(View view, int positon) {
                TabLayout.Tab tab = tabLayout.getTabAt(positon);
                if (tab != null) {
                    tab.select();
                }
                close();
            }
        });

        if (YMUserService.isGuest()) {
            titleList.remove(2);
        }
        mChannelLayout.setChannelData(titleList);

        //点击空白处弹窗消失
        mChannelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChannelLayout.isLayoutOpen()) {
                    close();
                }
            }
        });
    }


    private void close() {
        mChannelLayout.close();
        btnChannel.setImageResource(R.drawable.icon_arrow_down_gray);
    }


    private void open() {
        mChannelLayout.open(tabLayout.getSelectedTabPosition());
        btnChannel.setImageResource(R.drawable.arrow_up);
    }

    @Override
    protected void initData() {

        liveShowListTabBeanList.clear();

        for (int i = 0; i < titleArray.length; i++) {
            if (YMUserService.isGuest() && 2 == i) {
                //未登录状态不添加关注
            } else {
                liveShowListTabBeanList.add(new LiveShowListTabBean(titleArray[i], stateArray[i], cateIdArray[i]));
            }

        }

        myLiveShowTabAdapter = new LiveShowListTabAdapter(getSupportFragmentManager(), liveShowListTabBeanList);
        viewPager.setAdapter(myLiveShowTabAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
