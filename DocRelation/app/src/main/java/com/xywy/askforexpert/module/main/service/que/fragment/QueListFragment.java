package com.xywy.askforexpert.module.main.service.que.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.old.BaseFragment;
import com.xywy.askforexpert.appcommon.old.BasePage;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.QueData;
import com.xywy.askforexpert.module.main.service.que.QueListPage;
import com.xywy.askforexpert.module.main.service.que.RedNumChangListener;
import com.xywy.askforexpert.widget.view.TitleIndicator;
import com.xywy.askforexpert.widget.view.TitleIndicator.OnTitleIndicatorListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：D_Platform 类名称：QueListFragment 类描述：负责整个页面 创建人：shihao 创建时间：2015-8-10
 * 下午4:11:16 修改备注：
 */
public class QueListFragment extends BaseFragment implements
        OnTitleIndicatorListener, RedNumChangListener {

    private ViewPager viewPager;

    private TabPageAdapter tabPageAdapter;

    private List<BasePage> pageList = new ArrayList<BasePage>();
    // 标签包含内容
    private List<QueData> mLables = null;

    private int pageNum;

    private int first;

    private TitleIndicator mTitleIndicator;

    private RelativeLayout layout;

    private static final String TAG = "QueListFragment";

    private static final String WAITREPLY = "waitreply";//处理中
    private static final String RUNLIST = "runlist";//runlist

    private MyBroadCastReceiver receiver;

    private boolean isPush = false;

    private int jpushFrom = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle budle = getArguments();
        first = budle.getInt("first");
        isPush = budle.getBoolean("jpush");
        jpushFrom = budle.getInt("from");
        IntentFilter filter = new IntentFilter("com.refresh.list");
        receiver = new MyBroadCastReceiver();
        context.registerReceiver(receiver, filter);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {

        DLog.i(TAG, "QueListFragment initView");

        View view = inflater.inflate(R.layout.fragment_question_content,
                container, false);

        viewPager = (ViewPager) view.findViewById(R.id.pager);

        layout = (RelativeLayout) view.findViewById(R.id.ll_title_bar);

        return view;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

        DLog.i(TAG, "QueListFragment initData");
        if (mLables == null) {
            mLables = new ArrayList<QueData>();
        }
        mLables.clear();
        mLables = YMApplication.getInstance().getmLables();
        if (mLables == null || mLables.size() == 0) {
            return;
        }
        mTitleIndicator = new TitleIndicator(getActivity(), mLables, 0);
        // pager.setOffscreenPageLimit(mLables.size());
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        layout.addView(mTitleIndicator, params);
        mTitleIndicator.setOnTitleIndicatorListener(this);
        pageNum = mLables.size();

        for (int i = 0; i < pageNum; i++) {

            String url = mLables.get(i).getUrl();

            QueListPage queListPage = new QueListPage(getActivity(),url, "quelist", i);

            if (WAITREPLY.equals(url) || RUNLIST.equals(url)) {
                queListPage.setmRedNumChangListener(QueListFragment.this);
            }

            pageList.add(queListPage);
        }

        if (tabPageAdapter == null) {
            tabPageAdapter = new TabPageAdapter();
            viewPager.setAdapter(tabPageAdapter);
        } else {
            tabPageAdapter.notifyDataSetChanged();
        }

        if (isPush) {
            updateItemList(first);
        } else {
            viewPager.setCurrentItem(first);
            pageList.get(first).initData();
            mTitleIndicator.setTabsDisplay(getActivity(), first);
        }

        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {

                viewPager.setCurrentItem(position);

                pageList.get(position).initData();//刷新当前页面的数据

                if (myPageChangeListener != null) {
                    myPageChangeListener.onPageSelected(position);
                }

                mTitleIndicator.setTabsDisplay(getActivity(), position);

            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i(TAG, "state==" + state);
            }
        });

    }

    class TabPageAdapter extends PagerAdapter {

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
            
            container.addView(pageList.get(position)
                    .getRootView());
            return pageList.get(position).getRootView();
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            
            // super.destroyItem(container, position, object);
            ((ViewPager) container).removeView((View) object);
        }
    }

    public QueListPage getQueListPage(int index) {
        return (QueListPage) pageList.get(index);
    }

    public boolean isFirst() {
        return viewPager.getCurrentItem() == 0;
    }

    public boolean isEnd() {
        return viewPager.getCurrentItem() == pageList.size() - 1;
    }

    /**
     * 接口回调 获取当前的page信息
     */
    private MyPageChangeListener myPageChangeListener;

    public void setMyPageChangeListener(MyPageChangeListener l) {
        myPageChangeListener = l;
    }

    public interface MyPageChangeListener {
        void onPageSelected(int position);
    }

    /**
     * 指针指示器
     */
    @Override
    public void onIndicatorSelected(int index) {
        //tab项打点统计
        tabStatisticsEvent(index);
        viewPager.setCurrentItem(index);
    }

    /**
     * tab 项打点事件
     *
     * @param index
     */
    private void tabStatisticsEvent(int index) {
//        if (0 < index && index < mLables.size()) {
        if (0 <=index && index < mLables.size()) {

            QueData queData = mLables.get(index);


            if (null != queData && !TextUtils.isEmpty(queData.getUrl())) {

                String tabStasticTag = null;
                switch (queData.getUrl()) {
                    case "xinli":
                    case "unreply":
                        tabStasticTag = "PCQuestion";
                        break;
                    case "mobileDetail":
                        tabStasticTag = "PhoneQueation";
                        break;
                    case "mward":
                        tabStasticTag = "RewardQuestion";
                        break;
                    case "asktome":
                        tabStasticTag = "AskMeQuestion";
                        break;
                    case "zhuiwen":
                        tabStasticTag = "AskMeAgain";
                        break;
                    case "ques":
                        tabStasticTag = "ques";
                        break;
                    case "waitreply":
                        tabStasticTag = "Waitingforreply";
                        break;
                    case "runlist":
                        tabStasticTag = "consultinglistquestion";
                        break;
                    default:
                        break;
                }

                if (!TextUtils.isEmpty(tabStasticTag)) {
                    StatisticalTools.eventCount(getActivity(), tabStasticTag);
                }
            }
        }
    }

    @Override
    public void isShowRedPoint(int num, String tag) {
        Log.i(TAG, "TAG==" + tag + "num==" + num);
        mTitleIndicator.setRedPointShow(tag, num);
    }

    @Override
    public void onDestroy() {
        
        super.onDestroy();
        context.unregisterReceiver(receiver);
    }

    public class MyBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int position = intent.getExtras().getInt("index");
            // viewPager.setCurrentItem(position);
            pageList.get(position).initData();
        }

    }

    public void updateItemList(int type) {
        // 1 对我提问 2 对我追问 3 驳回 zhuiwen

        // 0 1 2asktome 3zhuiwen =2

        if (mLables != null && mLables.size() > 0) {
            switch (type) {
                case 1:
                    viewPager.setCurrentItem(mLables.size() - 2);
                    pageList.get(mLables.size() - 2).initData();
                    mTitleIndicator.setTabsDisplay(getActivity(), mLables.size() - 2);
                    break;
                case 2:
                    viewPager.setCurrentItem(mLables.size() - 1);
                    pageList.get(mLables.size() - 1).initData();
                    mTitleIndicator.setTabsDisplay(getActivity(), mLables.size() - 1);
                    break;

                default:
                    break;
            }
        }

    }
}
