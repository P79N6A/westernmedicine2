package com.xywy.askforexpert.module.main.service.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.umeng.analytics.MobclickAgent;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.module.main.service.HistoryReplyActivity;
import com.xywy.askforexpert.module.main.service.que.fragment.QueListFragment;
import com.xywy.askforexpert.widget.view.SelectBasePopupWindow;
import com.xywy.retrofit.rxbus.Event;
import com.xywy.retrofit.rxbus.EventSubscriber;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 服务创窗问题广场
 *
 * @author shihao 2015-5-8
 */
public class ServiceQueActivity extends YMBaseActivity {

    private static final String TAG = "ServiceQueActivity";

    /**
     * 抽屉菜单对象
     */
//    private SlidingMenu mSlidingMenu;

    private ImageView iBGudie;

//    private RightGuideFragment rightFragment;

    // private QueViewPagerFragment contentViewPagerFragment;

    private QueListFragment queListFragment;

    private FragmentTransaction mTransaction;

    // private List<QueData> mLables = null;

    private int first;

    private int isJump;

    private int backNum;
    private int hisnum; //标识历史回复是否有可修改的回复 ，是否有红点的标记

    private int jpushFrom = -1;

    private boolean isFirst = true;

    private ImageView redView;

    private QueReceiver queReceiver;

    private boolean isPush = false;

    private View lay1;
    private View lay2;
    private View popRoot;

    private SelectBasePopupWindow mPopupWindow;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_service_que;
    }

    @Override
    protected void beforeViewBind() {

        MobclickAgent.openActivityDurationTrack(false);
        // 在此处进行加入广播监听状态是否发生改变
        registerQuestionReceiver();
        DLog.i(TAG, "oncreate");
        initTitleIntentData();
    }

    @Override
    protected void initView() {
        hideCommonBaseTitle();
        YmRxBus.registerHistoryReplyUpdateSucess(new EventSubscriber<String>() {
            @Override
            public void onNext(Event<String> stringEvent) {
                getBackNum();
            }
        },this);
//        mSlidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
        iBGudie = (ImageView) findViewById(R.id.btn_question_rguide);
        redView = (ImageView) findViewById(R.id.iv_img_red);
        if (hisnum > 0) {
            redView.setVisibility(View.VISIBLE);
        } else {
            redView.setVisibility(View.GONE);
        }
//        mSlidingMenu.setRightView(getLayoutInflater().inflate(
//                R.layout.right_frame, null));
//        mSlidingMenu.setCenterView(getLayoutInflater().inflate(
//                R.layout.center_frame, null));

        mTransaction = this.getSupportFragmentManager().beginTransaction();

//        rightFragment = new RightGuideFragment();
//        Bundle bundle_right = new Bundle();
//        bundle_right.putInt("backNum", backNum);
//        rightFragment.setArguments(bundle_right);
//        mTransaction.replace(R.id.layout_container, queListFragment);

        // contentViewPagerFragment = new QueViewPagerFragment();
        queListFragment = new QueListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("first", first);
        bundle.putBoolean("jpush", isPush);
        bundle.putInt("isJump", isJump);
        bundle.putInt("from", jpushFrom);
        queListFragment.setArguments(bundle);

        mTransaction.replace(R.id.layout_container, queListFragment);
        mTransaction.commit();

        iBGudie.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                mSlidingMenu.showRightView();
                showPop();
            }
        });

    }

    @Override
    protected void initData() {
        initListener();
    }


    private void initTitleIntentData() {
        // mLables = new ArrayList<QueData>();
        Bundle bundle = getIntent().getBundleExtra("data");
        first = bundle.getInt("first");
        if (first < 0) {
            first = 0;                //修正first为负情况
        }
        isJump = bundle.getInt("isJump");
        jpushFrom = bundle.getInt("from");
        isPush = bundle.getBoolean("jpush", false);
        backNum = bundle.getInt("backNum");
        hisnum = bundle.getInt("hisnum");
        DLog.i(TAG, "=====first==" + first + "=====jpush====" + isPush);
    }


    public void onQuestionListener(View v) {
        switch (v.getId()) {
            case R.id.btn_question_back:
                unLock();
                finish();
                break;
        }
    }

    private void registerQuestionReceiver() {
        IntentFilter filter = new IntentFilter("com.questate.change");
        queReceiver = new QueReceiver();
        registerReceiver(queReceiver, filter);
    }


    /**
     * 解锁帖子
     */
    private void unLock() {
        AjaxParams params = new AjaxParams();
        String qId = queListFragment.getQueListPage(0).articleId;
        params.put("userid", YMApplication.getLoginInfo().getData().getPid());
        params.put("qid", qId);
        params.put("sign", MD5Util.MD5(YMApplication.getLoginInfo().getData().getPid()
                + qId + Constants.MD5_KEY));
        FinalHttp hp = new FinalHttp();
        DLog.i(TAG, "帖子解锁url" + CommonUrl.QUE_UNLOCK + params.toString());
        hp.post(CommonUrl.QUE_UNLOCK, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                DLog.i(TAG, "帖子解锁" + s);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    private void initListener() {

//        queListFragment.setMyPageChangeListener(new QueListFragment.MyPageChangeListener() {
//
//            @Override
//            public void onPageSelected(int position) {
//                if (queListFragment.isFirst()) {
//                    mSlidingMenu.setCanSliding(false);
//                } else if (queListFragment.isEnd()) {
//                    mSlidingMenu.setCanSliding(true);
//                } else {
//                    mSlidingMenu.setCanSliding(false);
//                }
//            }
//        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFirst = false;
//        mSlidingMenu.close();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
//        if (!isFirst) {
//            getBackNum();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        unregisterReceiver(queReceiver);

    }


    public void getBackNum() {
        AjaxParams params = new AjaxParams();
        params.put("userid", YMApplication.getLoginInfo().getData().getPid());
        params.put(
                "sign",
                MD5Util.MD5(YMApplication.getLoginInfo().getData().getPid()
                        + Constants.MD5_KEY));
        FinalHttp fh = new FinalHttp();

        fh.post(CommonUrl.DP_COMMON + "command=backNum", params,
                new AjaxCallBack() {

                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                    }

                    @Override
                    public void onLoading(long count, long current) {
                        super.onLoading(count, current);
                    }

                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        Log.i(TAG, "json=" + t);
                        try {
                            JSONObject jsonObject = new JSONObject(t);
                            int code = jsonObject.getInt("code");
                            String msg = jsonObject.getString("msg");
                            JSONObject jsonElement = jsonObject
                                    .getJSONObject("data");
                            if (code == 0) {
                                backNum = jsonElement.getInt("num");
//                                rightFragment.changeNum(backNum);
                                if (backNum > 0) {
                                    redView.setVisibility(View.VISIBLE);
                                } else {
                                    redView.setVisibility(View.GONE);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    public class QueReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 接收到广播，改变状态操作
            shortToast("接收到状态改变广播");
            int type = 0;
            if (intent != null) {
                type = intent.getIntExtra("questate", 0);
            }

            if (queListFragment != null) {
                queListFragment.updateItemList(type);
            }
        }
    }

    private void showPop() {
        if (mPopupWindow == null) {
            mPopupWindow = new SelectBasePopupWindow(true, this);
            popRoot = View.inflate(getBaseContext(), R.layout.pop_layout_serviceque, null);
            lay1 = popRoot.findViewById(R.id.lay1);
            lay1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                    StatisticalTools.eventCount(ServiceQueActivity.this, "myreply");
//                intent.setClass(getActivity(), QueMyReplyActivity.class);
//                intent.putExtra("backNum", backNum);
                    Intent intent = new Intent(ServiceQueActivity.this, HistoryReplyActivity.class);
                    intent.putExtra("backNum", backNum);
                    startActivity(intent);
                }
            });
        }
        if (!mPopupWindow.isShowing()) {
//            mPopupWindow.init(popRoot).showAtLocation(this.getWindow().getDecorView(), Gravity.TOP | Gravity.RIGHT, (AppUtils.dpToPx(48, getResources()) - 24) / 2 + AppUtils.dpToPx(5, getResources()) - 30 - 27, AppUtils.dpToPx(48, getResources()) + YMApplication.getStatusBarHeight() - 30);
            mPopupWindow.init(popRoot).showAtLocation(this.getWindow().getDecorView(), Gravity.TOP | Gravity.RIGHT, AppUtils.dpToPx(15, getResources()), AppUtils.dpToPx(48 + 5, getResources()) + YMApplication.getStatusBarHeight());
        }
    }

}
