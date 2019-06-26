package com.xywy.askforexpert.module.main.service.codex;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.WebViewUtils2;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.model.BookBaseInfo;
import com.xywy.askforexpert.widget.view.ProgressWebView;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 药典 web页面 stone
 *
 * @author 王鹏
 * @2015-5-13下午8:01:09
 */
public class BookWebActivity extends YMBaseActivity {
    private ProgressWebView webview;
    private String url;
    private String msg;
    private String iscollection = "0";
    private ImageView btn2;
    private FinalHttp fh;
    private String collecid;
    private String uuid;
    private String channel;
    private BookBaseInfo info;
    private LinearLayout no_data;
    private TextView tv_nodata_title;
    private ImageView img_nodata;


    public void updateCollector() {
        if (!TextUtils.isEmpty(iscollection)) {
            if ("1".equals(iscollection)) {
                btn2.setImageResource(R.drawable.collect_nobtn_sector_);
            } else {
                btn2.setImageResource(R.drawable.collected_btn_sector);
            }
        }
    }

    @SuppressLint("NewApi")
    private void init() {

        fh = new FinalHttp();
        webview.getSettings().setJavaScriptEnabled(true);
        WebViewUtils2.safeEnhance(webview);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(false);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        // webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        // webview.setWebChromeClient(new ChromewebViewClient());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, "Yimai-Request=" + AppUtils.getAPPInfo());
        webview.loadUrl(url);
        webview.setWebViewClient(new HelloWebViewClient());
    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            webview.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            super.onReceivedSslError(view, handler, error);
        }
    }

    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                if (webview.canGoBack()) {
                    webview.goBack();
                } else {
                    finish();
                }

                break;
            case R.id.btn2:// 收藏
                if ("1".equals(channel)) {
                    StatisticalTools.eventCount(this, "ydcollection");
                } else if ("2".equals(channel)) {
                    StatisticalTools.eventCount(this, "sccollection");
                }

                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(BookWebActivity.this).context);
                } else {

                    AjaxParams param = new AjaxParams();
                    uuid = YMApplication.getLoginInfo().getData().getPid();
                    param.put("a", "collection");
                    param.put("c", "collection");
                    param.put("userid", uuid);
                    param.put("collecid", collecid);
                    param.put("channel", channel);
                    param.put("sign", MD5Util.MD5(collecid + Constants.MD5_KEY));
                    fh.post(CommonUrl.Codex_Url, param, new AjaxCallBack() {
                        public void onFailure(Throwable t, int errorNo,
                                              String strMsg) {
                        }

                        @SuppressLint("NewApi")
                        public void onSuccess(String t) {

                            try {
                                JSONObject jsonObject = new JSONObject(t.toString());
                                String code = jsonObject.getString("code");
                                if (!TextUtils.isEmpty(code)) {
                                    if ("2".equals(code)) { // 取消收藏"
                                        btn2.setImageResource(R.drawable.collected_btn_sector);
                                        ToastUtils.shortToast("取消收藏");
                                    } else if ("0".equals(code)) {// "收藏成功"
                                        ToastUtils.shortToast("收藏成功");
                                        btn2.setImageResource(R.drawable.collect_nobtn_sector_);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    });
                }

                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (webview.canGoBack()) {
                webview.goBack();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void getData() {
        String userid;
        String sign = MD5Util.MD5(collecid + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("c", "status");
        params.put("a", "status");
        if (YMUserService.isGuest()) {
            userid = "0";
        } else {
            userid = YMApplication.getLoginInfo().getData().getPid();
        }
        params.put("userid", userid);
        params.put("id", collecid);
        params.put("channel", channel);
        params.put("sign", sign);
        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.Codex_Url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                Gson gson = new Gson();
                info = gson.fromJson(t.toString(), BookBaseInfo.class);
                if ("0".equals(info.getCode())) {
                    iscollection = info.getList().getIscollection();
                    updateCollector();
                }
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }


    @Override
    protected void initView() {

        hideCommonBaseTitle();
//        ((TextView) findViewById(R.id.tv_title)).setText(mStrTitle);
        btn2= (ImageView) findViewById(R.id.btn2);

//        btn2.setImageResource(R.drawable.service_topque_right_btn);


        no_data = (LinearLayout) findViewById(R.id.lin_nodata);
        tv_nodata_title = (TextView) findViewById(R.id.tv_nodata_title);
        tv_nodata_title.setText("页面加载失败");
        img_nodata = (ImageView) findViewById(R.id.img_nodate);
        img_nodata.setBackgroundResource(R.drawable.service_more_none);
//        btn2 = (ImageButton) findViewById(R.id.btn2);
        webview = (ProgressWebView) findViewById(R.id.webview);
        url = getIntent().getStringExtra("url");
        msg = getIntent().getStringExtra("msg");
        collecid = getIntent().getStringExtra("collecid");
        channel = getIntent().getStringExtra("channel");
        // iscollection = getIntent().getStringExtra("iscollection");
        String title = getIntent().getStringExtra("title");
        if ("我的简历".equals(title)) {
            btn2.setVisibility(View.GONE);

        }else{
            btn2.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(url)) {
            tv_nodata_title.setText("您还没有完整简历，手机编辑简历太费劲，还是去电脑上编辑吧");
        }
        ((TextView) findViewById(R.id.tv_title)).setText(title);
        if (NetworkUtil.isNetWorkConnected()) {
            getData();
        } else {
            ToastUtils.shortToast("网络连接失败");
            no_data.setVisibility(View.VISIBLE);
            tv_nodata_title.setText("网络连接失败");
        }

        if (!TextUtils.isEmpty(url)) {
            init();
        } else {
            no_data.setVisibility(View.VISIBLE);
        }

        updateCollector();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.book_webview;
    }
}
