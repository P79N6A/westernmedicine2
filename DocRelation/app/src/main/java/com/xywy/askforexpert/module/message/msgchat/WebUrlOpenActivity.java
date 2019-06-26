package com.xywy.askforexpert.module.message.msgchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.umeng.socialize.UMShareAPI;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.WebViewUtils2;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.module.message.share.ShareUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by shihao on 16/4/8.
 */
public class WebUrlOpenActivity extends AppCompatActivity {

    private static final String TAG = WebUrlOpenActivity.class.getSimpleName();
    @Bind(R.id.url_back)
    ImageButton urlBack;
    @Bind(R.id.rl_url_bar)
    RelativeLayout rlUrlBar;
    @Bind(R.id.invite_money_progressBar)
    ProgressBar progressBar;
    @Bind(R.id.invite_money_web)
    WebView urlOpen;
    private String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web_url);
        ButterKnife.bind(this);
        CommonUtils.initSystemBar(this);
        CommonUtils.setWebView(urlOpen);

        url = getIntent().getStringExtra("url");
        DLog.i(TAG, "web url" + url);
        if (!url.equals("")) {
            if (url.contains("www.baidu.com")) {
                url = "https://www.baidu.com";
            }
            urlOpen.setWebChromeClient(new WebChromeClient() {
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
            urlOpen.setWebViewClient(new MyWebViewClient());
            WebViewUtils2.safeEnhance(urlOpen);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setCookie(url,
                    "Yimai-Request=" + AppUtils.getAPPInfo());
            urlOpen.loadUrl(url);
        }

    }

    @OnClick(R.id.url_back)
    public void onClick() {
        finish();
    }

    @Override
    public void onBackPressed() {
        if (urlOpen.canGoBack()) {
            urlOpen.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();

        StatisticalTools.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        StatisticalTools.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
//        UMSsoHandler ssoHandler = SharedUtils.getInstence().getmController().getConfig().getSsoHandler(requestCode);
//        if (ssoHandler != null) {
//            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
//        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            DLog.d("tag", "invite_money: " + url);
            if (url.contains("invitation://")) {

                new ShareUtil.Builder()
                        .setTitle(getString(R.string.invite_money_share_title))
                        .setText(getString(R.string.invite_money_share_content))
                        .setTargetUrl(url.replace("invitation://", ""))
                        .setImageUrl(ShareUtil.DEFAULT_SHARE_IMG_ULR)
                        .build(WebUrlOpenActivity.this).outerShare();
                return true;
            }
            return false;
        }
    }
}
