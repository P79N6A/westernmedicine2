package com.xywy.askforexpert.appcommon.base.webview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.umeng.socialize.UMShareAPI;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.WebViewUtils2;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.module.message.share.ShareUtil;
import com.xywy.askforexpert.module.my.account.LoginActivity;

/**
 * 一般活动 H5 页面
 */
public class CommonWebViewActivity extends AppCompatActivity {
    public static final String ID_INTENT_KEY = "id";
    public static final String IMAGE_URL_INTENT_KEY = "imageUrl";
    public static final String IS_FROM_INTENT_KEY = "isfrom";
    public static final String CONTENT_URL_INTENT_KEY = "content_url";
    public static final String DESCRIPTION_INTENT_KEY = "description";
    public static final String SHARE_VISIBLE_INTENT_KEY = "shareVisible";
    private static final String LOG_TAG = CommonWebViewActivity.class.getSimpleName();
    private WebView webView;
    private String id;
    private String imageUrl;
    private String isFrom;
    private String contentUrl;
    private String description;
    private ProgressBar progressBar;
    private TextView toolbarTitle;
    private boolean shareVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);

        setContentView(R.layout.activity_new_year);

        initView();
        getParams();
        loadData();
    }

    private void getParams() {
        id = getIntent().getStringExtra(ID_INTENT_KEY);
        imageUrl = getIntent().getStringExtra(IMAGE_URL_INTENT_KEY);
        isFrom = getIntent().getStringExtra(IS_FROM_INTENT_KEY);
        contentUrl = getIntent().getStringExtra(CONTENT_URL_INTENT_KEY);
        description = getIntent().getStringExtra(DESCRIPTION_INTENT_KEY);
        shareVisible = getIntent().getBooleanExtra(SHARE_VISIBLE_INTENT_KEY, true);
    }

    private void loadData() {
        toolbarTitle.setText(isFrom);

        webView.setWebViewClient(new MyWebViewClient());
        WebViewUtils2.safeEnhance(webView);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                if (newProgress < 100) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(contentUrl, "Yimai-Request=" + AppUtils.getAPPInfo());
        DLog.d(LOG_TAG, "contentUrl = " + contentUrl);
        webView.loadUrl(contentUrl);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CommonUtils.setToolbar(this, toolbar);

        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        webView = (WebView) findViewById(R.id.new_year_web);
        CommonUtils.setWebView(webView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_year_share_menu, menu);
        menu.getItem(0).setVisible(shareVisible);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    this.finish();
                }
                break;

            // 分享
            case R.id.new_year_share:
                DLog.d(LOG_TAG, LOG_TAG + " share");
                if (imageUrl == null || imageUrl.equals("")) {
                    imageUrl = ShareUtil.DEFAULT_SHARE_IMG_ULR;
                }
                DLog.d(LOG_TAG, "share_title = " + isFrom);
                DLog.d(LOG_TAG, "share_content = " + description);
                DLog.d(LOG_TAG, "share_url = " + contentUrl + "&param=1&from=android");
                DLog.d(LOG_TAG, "share_img = " + imageUrl);

                new ShareUtil.Builder().setTitle(isFrom)
                        .setText(description)
                        .setTargetUrl(contentUrl)
                        .setImageUrl(imageUrl)
                        .setShareId("")
                        .setShareSource("3")
                        .build(this).innerShare();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                this.finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            DLog.d(LOG_TAG, "Override URL = " + url);

            if (url.contains("login://")) {
                startActivity(new Intent(CommonWebViewActivity.this, LoginActivity.class));
                YMApplication.getInstance().appExit();
                return true;
            } else {
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(CommonWebViewActivity.this).context);
                } else {
                    webView.loadUrl(url);
                }
                return true;
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            DLog.d(LOG_TAG, "isCommon webview error");
        }
    }
}
