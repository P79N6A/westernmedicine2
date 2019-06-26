package com.xywy.askforexpert.module.main.service.service;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.WebViewUtils2;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;

public class TelDetailActivity extends Activity {

    private WebView webView;

    private String webUrl;

    private String state;

    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        //http://test.api.app.club.xywy.com/app/1.0/club/phoneApp.interface.php?
//		command=orderdetail&userid=23088840&order_id=4509&md5=1
        String url = CommonUrl.ModleUrl + "club/phoneApp.interface.php?command=orderdetail";
        String userid = YMApplication.getLoginInfo().getData().getPid();
        String orderId = getIntent().getStringExtra("orderId");
        state = getIntent().getStringExtra("state");
        webUrl = CommonUrl.QUE_TEL_DETAIL + "&userid=" + userid + "&order_id=" + orderId + "&sign=" + MD5Util.MD5(userid + orderId + Constants.MD5_KEY);
        setContentView(R.layout.activity_tel_detail);

        tvTitle = (TextView) findViewById(R.id.tv_tel_title);
        webView = (WebView) findViewById(R.id.wv_per);
        switch (Integer.parseInt(state)) {
            case 1:
                tvTitle.setText("待确定时间");
                break;

            case 2:
                tvTitle.setText("待通话");
                break;
            case 3:
                tvTitle.setText("通话完成");
                break;
            case 4:
                tvTitle.setText("订单完成");
                break;
        }
        initWebView();
    }

    public void onPerClickListener(View v) {
        if (v.getId() == R.id.btn_per_back) {
            finish();
        }
    }

    private void initWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        WebViewUtils2.safeEnhance(webView);
        webView.getSettings().setRenderPriority(RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath() + "/webcache";
        webView.getSettings().setDatabasePath(cacheDirPath);
        webView.getSettings().setAppCachePath(cacheDirPath);
        webView.getSettings().setAppCacheEnabled(true);

        webView.setWebViewClient(new MyWebViewClient());

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                // TODO Auto-generated method stub
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url,
                                       String message, JsResult result) {
                // TODO Auto-generated method stub
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message,
                                      String defaultValue, JsPromptResult result) {
                // TODO Auto-generated method stub
                return super.onJsPrompt(view, url, message, defaultValue,
                        result);
            }

        });

        webView.loadUrl(webUrl);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        StatisticalTools.onPause(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        StatisticalTools.onResume(this);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view,
                                                String urlconnection) {
            webView.loadUrl(urlconnection);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            String js = "var sharediv = document.getElementById(\"div_share\");document.body.removeChild(sharediv);";
            webView.loadUrl("javascript:" + js);
            // loading_bar.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            // TODO Auto-generated method stub
            super.onReceivedError(view, errorCode, description, failingUrl);
            // loading_bar.setVisibility(View.GONE);
            // noWifiView.setVisibility(View.VISIBLE);
        }

    }
}
