package com.xywy.askforexpert.module.discovery;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.WebViewUtils2;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.widget.view.SelectBasePopupWindow;
import com.xywy.base.view.ProgressDialog;
import com.xywy.uilibrary.titlebar.ItemClickListener;

/**
 * 医生的一天 stone
 *
 * @author LG
 */
public class DoctorOneDayActivity extends YMBaseActivity {

    private WebView web;
    private Activity context;
    //    private ImageView iv_slidmenu;
//    private SlidingMenu mSlidingMenu;
    private FragmentTransaction mTransaction;
    //    private View rightView;
    private String url;
    private String uuid;
    private String sign;
    private boolean isCurentPage = true;
    private ProgressDialog pro;
//    private ImageView iv_error;

    private View lay1;
    private View lay2;
    private View lay3;
    private View lay4;
    private View line4;
    private View popRoot;

    private boolean mNeedHide;//是否隐藏关注我们

    private SelectBasePopupWindow mPopupWindow;


    @SuppressWarnings("deprecation")
    private void intiView() {


//        iv_error = (ImageView) findViewById(R.id.iv_error);

//        iv_slidmenu = (ImageView) findViewById(R.id.iv_slidmenu);
//        iv_back = (ImageView) findViewById(R.id.iv_back);
//        mSlidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
//        mSlidingMenu.setCanSliding(false);
//        mSlidingMenu.setRightView(getLayoutInflater().inflate(R.layout.right_frame, null));

//        mSlidingMenu.setCenterView(getLayoutInflater().inflate(R.layout.center_frame, null));
        web = new WebView(this);
//        rightView = View.inflate(context, R.layout.doctor_oneday, null);
//        rightView.findViewById(R.id.ll_activty).setOnClickListener(this);
        if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
            mNeedHide = true;
        } else {
            mNeedHide = false;
        }

//        rightView.findViewById(R.id.ll_submit).setOnClickListener(this);
//        rightView.findViewById(R.id.home).setOnClickListener(this);

//        FrameLayout right_frame = ((FrameLayout) findViewById(R.id.right_frame));
        FrameLayout center_frame = ((FrameLayout) findViewById(R.id.layout_container));
//        right_frame.removeAllViews();
        center_frame.removeAllViews();
//        right_frame.addView(rightView);
        center_frame.addView(web);

        web.getSettings().setJavaScriptEnabled(true);
        WebViewUtils2.safeEnhance(web);
        web.getSettings().setRenderPriority(RenderPriority.HIGH);
        if (NetworkUtil.isNetWorkConnected()) {
            web.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        if (Build.VERSION.SDK_INT > 10 && Build.VERSION.SDK_INT < 17) {
            web.removeJavascriptInterface("searchBoxJavaBridge_");
        }

        web.getSettings().setDomStorageEnabled(true);
        web.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath() + "/webcache";
        web.getSettings().setDatabasePath(cacheDirPath);
        web.getSettings().setAppCachePath(cacheDirPath);
        web.getSettings().setAppCacheEnabled(true);


        web.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        web.setHorizontalScrollbarOverlay(true);
        web.setHorizontalScrollBarEnabled(true);
        web.requestFocus();

        sign = MD5Util.MD5(uuid + Constants.MD5_KEY);
        url = CommonUrl.doctorOneDay + "?from=ypt&userid=" + uuid + "&sign=" + sign;
        web.loadUrl(url);

        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // TODO Auto-generated method stub
                if (pro != null && pro.isShowing()) {
                    pro.closeProgersssDialog();
                }
                super.onReceivedError(view, errorCode, description, failingUrl);
//				iv_error.setVisibility(View.VISIBLE);

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pro = new ProgressDialog(context, getString(R.string.loadig_more));
                pro.showProgersssDialog();
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
//                iv_error.setVisibility(View.INVISIBLE);
                if (pro != null && pro.isShowing()) {
                    pro.closeProgersssDialog();
                }
                if (!web.getSettings().getLoadsImagesAutomatically()) {
                    web.getSettings().setLoadsImagesAutomatically(true);
                }
                super.onPageFinished(view, url);
            }

        });

    }


    private void setLsener() {

//        iv_back.setOnClickListener(this);
//        iv_slidmenu.setOnClickListener(this);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) {
            web.goBack();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (pro != null && pro.isShowing()) {
            pro.closeProgersssDialog();
        }

        super.onDestroy();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_doctor_oneday;
    }


    @Override
    protected void initView() {

        this.context = this;

        titleBarBuilder.setTitleText("中国医生的一天").addItem("", R.drawable.service_topque_right_btn, new ItemClickListener() {
            @Override
            public void onClick() {
                showPop();
            }
        }).setBackIconClickEvent(new ItemClickListener() {
            @Override
            public void onClick() {
                if (web.canGoBack()) {
                    web.goBack();
                    url = "";
                } else {
                    DoctorOneDayActivity.this.finish();
                }
            }
        }).build();

        if (YMUserService.isGuest()) {
            uuid = "0";
        } else {
            uuid = YMApplication.getLoginInfo().getData().getPid();
        }
        isCurentPage = false;

        intiView();
        initData();
        setLsener();
    }

    @Override
    protected void initData() {

    }

    private void showPop() {
        if (mPopupWindow == null) {
            mPopupWindow = new SelectBasePopupWindow(true, this);
            popRoot = View.inflate(getBaseContext(), R.layout.pop_layout_doctor_oneday, null);
            line4 = popRoot.findViewById(R.id.line4);
            lay1 = popRoot.findViewById(R.id.lay1);
            lay2 = popRoot.findViewById(R.id.lay2);
            lay3 = popRoot.findViewById(R.id.lay3);
            lay4 = popRoot.findViewById(R.id.lay4);
            lay1.setOnClickListener(mPopOnClickListener);
            lay2.setOnClickListener(mPopOnClickListener);
            lay3.setOnClickListener(mPopOnClickListener);
            lay4.setOnClickListener(mPopOnClickListener);

            if (mNeedHide) {
                lay4.setVisibility(View.GONE);
                line4.setVisibility(View.GONE);
            }
        }
        if (!mPopupWindow.isShowing()) {
//            mPopupWindow.init(popRoot).showAtLocation(this.getWindow().getDecorView(), Gravity.TOP | Gravity.RIGHT, (AppUtils.dpToPx(48, getResources()) - 24) / 2 + AppUtils.dpToPx(5, getResources()) - 30 - 27, AppUtils.dpToPx(48, getResources()) + YMApplication.getStatusBarHeight() - 30);
            mPopupWindow.init(popRoot).showAtLocation(this.getWindow().getDecorView(), Gravity.TOP | Gravity.RIGHT, AppUtils.dpToPx(15, getResources()), AppUtils.dpToPx(48 + 5, getResources()) + YMApplication.getStatusBarHeight());
        }
    }

    /**
     * pop监听器
     */
    private View.OnClickListener mPopOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mPopupWindow != null && mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
            switch (view.getId()) {
                case R.id.lay1:
                    //回到首页
                    if (url.equals(CommonUrl.doctorOneDay + "?from=ypt&userid=" + uuid + "&sign=" + sign)) {
                        return;
                    }
                    url = CommonUrl.doctorOneDay + "?from=ypt&userid=" + uuid + "&sign=" + sign;
                    web.loadUrl(url);

                    break;
                case R.id.lay2:
                    //我要投稿
                    StatisticalTools.eventCount(DoctorOneDayActivity.this, "Contributors");
                    if (url.equals(CommonUrl.doctorOneDay + "tou/?from=ypt")) {
                        return;
                    }
                    url = CommonUrl.doctorOneDay + "tou/?from=ypt";
                    web.loadUrl(url);
                    break;
                case R.id.lay3:
                    //活动介绍
                    StatisticalTools.eventCount(DoctorOneDayActivity.this, "introduction");
                    if (url.equals(CommonUrl.doctorOneDay + "active/?from=ypt")) {
                        return;
                    }
                    url = CommonUrl.doctorOneDay + "active/?from=ypt";
                    web.loadUrl(url);
                    break;
                case R.id.lay4:
                    //关注我们
                    if (url.equals(CommonUrl.doctorOneDay + "join/?from=ypt")) {
                        return;
                    }
                    url = CommonUrl.doctorOneDay + "join/?from=ypt";
                    web.loadUrl(url);
                    break;
                default:
                    break;
            }
        }

    };

}
