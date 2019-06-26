package com.xywy.askforexpert.module.my.invite;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.umeng.socialize.UMShareAPI;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.WebViewUtils2;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.module.message.share.ShareUtil;

import butterknife.Bind;

/**
 * 邀请活动/文献活动
 *
 * @author Jack Fang
 */
public class InviteMoneyActivity extends YMBaseActivity {
    private static final String LOG_TAG = InviteMoneyActivity.class.getSimpleName();
//    @Bind(R.id.invite_money_toolbar)
//    Toolbar toolbar;
//    @Bind(R.id.invite_money_toolbar_title)
//    TextView toolbarTitle;
    @Bind(R.id.invite_money_progressBar)
    ProgressBar progressBar;
    @Bind(R.id.invite_money_web)
    WebView inviteMoneyWeb;
    private String uuid;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_invite_money);
//
//        ButterKnife.bind(this);
//
//        if (YMUserService.isGuest()) {
//            uuid = "0";
//        } else {
//            uuid = YMApplication.getPID();
//        }
//
//        CommonUtils.initSystemBar(this);
//        CommonUtils.setToolbar(this, toolbar);
//        CommonUtils.setWebView(inviteMoneyWeb);
//        loadData();
//    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_invite_money;
    }
    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("邀请活动");
        if (YMUserService.isGuest()) {
            uuid = "0";
        } else {
            uuid = YMApplication.getPID();
        }
        CommonUtils.setWebView(inviteMoneyWeb);

    }

    @Override
    protected void initData() {
        loadData();
    }

    private void loadData() {
        inviteMoneyWeb.setWebChromeClient(new WebChromeClient() {
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
        inviteMoneyWeb.setWebViewClient(new MyWebViewClient());
        WebViewUtils2.safeEnhance(inviteMoneyWeb);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(CommonUrl.INVITE_MONEY + uuid,
                "Yimai-Request=" + AppUtils.getAPPInfo());
        inviteMoneyWeb.loadUrl(CommonUrl.INVITE_MONEY + uuid);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (inviteMoneyWeb.canGoBack()) {
                    inviteMoneyWeb.goBack();
                    return true;
                }
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (inviteMoneyWeb.canGoBack()) {
            inviteMoneyWeb.goBack();
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

        StatisticalTools.onPause(this);


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
            DLog.d(LOG_TAG, "invite_money: " + url);
            if (url.contains("invitation://")) {
                new ShareUtil.Builder()
                        .setTitle(getResources().getString(R.string.invite_money_share_title))
                        .setText( getResources().getString(R.string.invite_money_share_content))
                        .setTargetUrl( url.replace("invitation://", ""))
                        .setImageUrl(ShareUtil.DEFAULT_SHARE_IMG_ULR)
                        .build(InviteMoneyActivity.this).outerShare();
            }
            return true;
        }
    }
}
