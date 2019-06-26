package com.xywy.askforexpert.module.main.service.que;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.old.BasePage;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.model.QueData;
import com.xywy.askforexpert.widget.view.TitleIndicator;
import com.xywy.askforexpert.widget.view.TitleIndicator.OnTitleIndicatorListener;

import java.util.ArrayList;
import java.util.List;

public class QueMyReplyActivity extends FragmentActivity implements
        OnTitleIndicatorListener, RedNumChangListener {

    private ViewPager replyViewPager;

    private TabPagerAdapter adapter;

    private TitleIndicator mTitleIndicator;

    private RelativeLayout layout;

    private List<BasePage> pageList = new ArrayList<BasePage>();

    private List<QueData> mLables;

    private ReplyRefreshReceiver receiver;

    private int backNum;

    private static final String TAG = "QueMyReplyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CommonUtils.initSystemBar(this);
        backNum = getIntent().getIntExtra("backNum", 0);

        // 在此处进行加入广播监听状态是否发生改变
        IntentFilter filter = new IntentFilter("com.refresh.list");
        receiver = new ReplyRefreshReceiver();
        registerReceiver(receiver, filter);

        MobclickAgent.openActivityDurationTrack(false);
        setContentView(R.layout.activity_que_myreply);
        mLables = new ArrayList<QueData>();
        if (!YMApplication.getLoginInfo().getData().getIsjob().equals("2")) {
            mLables.add(new QueData("被驳回", "edit", backNum));
            mLables.add(new QueData("被采纳", "agree", 0));
            mLables.add(new QueData("已回复", "reply", 0));
            mLables.add(new QueData("已删除", "del", 0));
        } else {
            mLables.add(new QueData("myreply", "myreply", 0));
        }
        initView();
    }

    private void initView() {
        replyViewPager = (ViewPager) findViewById(R.id.reply_viewpager);
        layout = (RelativeLayout) findViewById(R.id.rl_title_bar);
        if (mLables.size() != 1) {
            layout.setVisibility(View.VISIBLE);
            mTitleIndicator = new TitleIndicator(this, mLables, 1);
            LayoutParams params = new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            layout.addView(mTitleIndicator, params);

            mTitleIndicator.setOnTitleIndicatorListener(this);

        } else {
            layout.setVisibility(View.GONE);
        }
        for (int i = 0; i < mLables.size(); i++) {
            QueListPage queListPage = new QueListPage(this, mLables.get(i)
                    .getUrl(), "myreply", i);
            if (mLables.get(i).getUrl().equals("edit")) {
                queListPage.setmRedNumChangListener(this);
            }
            pageList.add(queListPage);
        }

        if (adapter == null) {
            adapter = new TabPagerAdapter();
            replyViewPager.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

        replyViewPager.setCurrentItem(0);

        pageList.get(0).initData();

        replyViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {

                replyViewPager.setCurrentItem(position);

                pageList.get(position).initData();

                mTitleIndicator.setTabsDisplay(QueMyReplyActivity.this,
                        position);

            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void onReplyClickListener(View v) {
        switch (v.getId()) {
            case R.id.btn_reply_back:
                finish();
                break;
        }
    }

    class TabPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pageList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(pageList.get(position).getRootView());
            return pageList.get(position).getRootView();
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            // TODO Auto-generated method stub
            // super.destroyItem(container, position, object);
            ((ViewPager) container).removeView((View) object);
        }
    }

    @Override
    public void onIndicatorSelected(int index) {
        replyViewPager.setCurrentItem(index);
    }

    public void onResume() {
        super.onResume();
        StatisticalTools.onResume(this);
    }

    public void onPause() {
        super.onPause();
        StatisticalTools.onPause(this);
    }

    @Override
    public void isShowRedPoint(int num, String tag) {
        mTitleIndicator.setRedPointShow(tag, num);
    }

    class ReplyRefreshReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int position = intent.getExtras().getInt("index");
            replyViewPager.setCurrentItem(position);
            pageList.get(position).initData();
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();


        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }
}
