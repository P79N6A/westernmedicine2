package com.xywy.askforexpert.module.main.service.que;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog.Builder;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.umeng.analytics.social.UMSocialService;
import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.interfaces.listener.MyWebViewDownLoadListener;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.WebViewUtils2;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.module.discovery.medicine.common.MyConstant;
import com.xywy.askforexpert.module.message.share.ShareUtil;
import com.xywy.base.view.ProgressDialog;
import com.xywy.uilibrary.titlebar.ItemClickListener;

/**
 * webview 展示信息 （答题绩效，服务协议，关于医脉，积分规则，其他）
 *
 * @author shr
 *         created at 16/5/6 上午9:37
 */
public class QuePerActivity extends YMBaseActivity {

    private WebView webView;

    private String webUrl;

    private String title;

//    private TextView tvTitle;

    private ProgressDialog dialog;

    private boolean isShare;

//    private ImageButton btnShare;

    private UMSocialService mController;

    private String description = "";

    private String imageUrl;

    private String id;

    private ValueCallback<Uri> mUploadMessage;

    private Builder builder;

    private final static int FILECHOOSER_RESULTCODE = 1;

    private static final String TAG = "QuePerActivity";

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        CommonUtils.initSystemBar(this);
//        builder = new Builder(this);
//        title = getIntent().getStringExtra("isfrom");
//        if (title.equals("答题绩效")) {
//            String userid = YMApplication.getLoginInfo().getData().getPid();
//            webUrl = CommonUrl.QUE_JIXIAO + "&userid=" + userid;
//        } else if (title.equals("服务协议")) {
//            if(YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)){
//                webUrl = MyConstant.H5_BASE_URL + "protocol";
//            }else {
//                webUrl = CommonUrl.ModleUrl + "club/register.html";
//            }
//        } else if (title.equals("关于医脉")) {
//            webUrl = CommonUrl.ModleUrl + "club/introduce.html";
//        }else if(title.equals("关于闻康医生助手")){
//            webUrl = MyConstant.H5_BASE_URL + "introduce";
//        } else if ("积分规则".equals(title)) {
//            String userid = YMApplication.getLoginInfo().getData().getPid();
//            String sign = MD5Util.MD5(userid + Constants.MD5_KEY);
//            webUrl = CommonUrl.ModleUrl + "club/pointApp.interface.php?command=pointShow&userid=" + userid + "&sign=" + sign;
//        } else {
//            webUrl = getIntent().getStringExtra("content_url");
//            description = getIntent().getStringExtra("description");
//            imageUrl = getIntent().getStringExtra("imageUrl");
//            id = getIntent().getStringExtra("id");
//            isShare = true;
//        }
//
//        setContentView(R.layout.activity_que_per);
//        dialog = new ProgressDialog(this, "正在加载，请稍后...");
//        dialog.showProgersssDialog();
//        webView = (WebView) findViewById(R.id.wv_per);
//        tvTitle = (TextView) findViewById(R.id.tv_title);
//        btnShare = (ImageButton) findViewById(R.id.btn_share);
//        if (isShare) {
//            btnShare.setVisibility(View.VISIBLE);
//            // 初始化分享平台
////			initSocialSDK(description, description, webUrl, imageUrl);
//            DLog.i(TAG, "share=" + isShare + "weburl=" + webUrl + "id=" + id
//                    + "description=" + description + "imageUrl=" + imageUrl);
//        } else {
//            btnShare.setVisibility(View.GONE);
//        }
//        tvTitle.setText(title);
//        initWebView();
//    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_que_per;
    }

    @Override
    protected void initView() {
        builder = new Builder(this);
        title = getIntent().getStringExtra("isfrom");
        titleBarBuilder.setTitleText(title);
        if (title.equals("答题绩效")) {
            String userid = YMApplication.getLoginInfo().getData().getPid();
            webUrl = CommonUrl.QUE_JIXIAO + "&userid=" + userid;
        } else if (title.equals("服务协议")) {
            if(YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)){
                webUrl = MyConstant.H5_BASE_URL + "protocol";
            }else {
                webUrl = CommonUrl.ModleUrl + "club/register.html";
            }
        } else if (title.equals("关于医脉")) {
            webUrl = CommonUrl.ModleUrl + "club/introduce.html";
        }else if(title.equals("关于闻康医生助手")){
            webUrl = MyConstant.H5_BASE_URL + "introduce";
        } else if ("积分规则".equals(title)) {
            String userid = YMApplication.getLoginInfo().getData().getPid();
            String sign = MD5Util.MD5(userid + Constants.MD5_KEY);
            webUrl = CommonUrl.ModleUrl + "club/pointApp.interface.php?command=pointShow&userid=" + userid + "&sign=" + sign;
        } else {
            webUrl = getIntent().getStringExtra("content_url");
            description = getIntent().getStringExtra("description");
            imageUrl = getIntent().getStringExtra("imageUrl");
            id = getIntent().getStringExtra("id");
            isShare = true;
        }

        dialog = new ProgressDialog(this, "正在加载，请稍后...");
        dialog.showProgersssDialog();
        webView = (WebView) findViewById(R.id.wv_per);
//        tvTitle = (TextView) findViewById(R.id.tv_title);
//        btnShare = (ImageButton) findViewById(R.id.btn_share);
        if (isShare) {
//            btnShare.setVisibility(View.VISIBLE);
            titleBarBuilder.addItem("分享", new ItemClickListener() {
                @Override
                public void onClick() {
                    DLog.i(TAG, "btn_share weburl=" + webUrl + "id=" + id
                            + "description=" + description + "imageUrl=" + imageUrl);
                    new ShareUtil.Builder().setTitle(title)
                            .setText(description)
                            .setTargetUrl(webUrl)
                            .setImageUrl(imageUrl)
                            .setShareId(id)
                            .setShareSource("")
                            .build(QuePerActivity.this).innerShare();
                }
            }).build();
            // 初始化分享平台
            DLog.i(TAG, "share=" + isShare + "weburl=" + webUrl + "id=" + id
                    + "description=" + description + "imageUrl=" + imageUrl);
        } else {
//            btnShare.setVisibility(View.GONE);
        }
//        tvTitle.setText(title);
        initWebView();
    }

    @Override
    protected void initData() {
    }

//    public void onPerClickListener(View v) {
//        if (v.getId() == R.id.btn_per_back) {
//            finish();
//        }
//        if (v.getId() == R.id.btn_share) {
//            DLog.i(TAG, "btn_share weburl=" + webUrl + "id=" + id
//                    + "description=" + description + "imageUrl=" + imageUrl);
//            new ShareUtil.Builder().setTitle(title)
//                    .setText(description)
//                    .setTargetUrl(webUrl)
//                    .setImageUrl(imageUrl)
//                    .setShareId(id)
//                    .setShareSource("")
//                    .build(this).innerShare();
//
//        }
//    }

    private void initWebView() {
        if (Build.VERSION.SDK_INT > 10 && Build.VERSION.SDK_INT < 17) {
            fixWebView();
        }
        if (Build.VERSION.SDK_INT >= 19) {
            webView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            webView.getSettings().setLoadsImagesAutomatically(false);
        }
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

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setHorizontalScrollbarOverlay(true);
        webView.setHorizontalScrollBarEnabled(true);
        webView.requestFocus();

        webView.setWebViewClient(new MyWebViewClient());

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url,
                                       String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message,
                                      String defaultValue, JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue,
                        result);
            }

            // The undocumented magic method override
            // Eclipse will swear at you if you try to put @Override here
            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {

                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                QuePerActivity.this.startActivityForResult(
                        Intent.createChooser(i, "File Chooser"),
                        FILECHOOSER_RESULTCODE);

            }

            // For Android 3.0+
            public void openFileChooser(ValueCallback uploadMsg,
                                        String acceptType) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                QuePerActivity.this.startActivityForResult(
                        Intent.createChooser(i, "File Browser"),
                        FILECHOOSER_RESULTCODE);
            }

            // For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                        String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                QuePerActivity.this.startActivityForResult(
                        Intent.createChooser(i, "File Chooser"),
                        QuePerActivity.FILECHOOSER_RESULTCODE);

            }

        });
        Log.i(TAG, "WEBURL=" + webUrl);
        CookieManager cookieManager = CookieManager.getInstance();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            cookieManager.setAcceptThirdPartyCookies(webView,true);
        } else {
            cookieManager.setAcceptCookie(true);
        }
        cookieManager.setCookie(webUrl, "Yimai-Request=" + AppUtils.getAPPInfo());
        // 开启 localStorage
        webView.getSettings().setDomStorageEnabled(true);

        webView.setDownloadListener(new MyWebViewDownLoadListener());
        webView.loadUrl(webUrl);
    }

    private void fixWebView() {
        webView.removeJavascriptInterface("searchBoxJavaBridge_");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String urlconnection) {
            webView.loadUrl(urlconnection);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!webView.getSettings().getLoadsImagesAutomatically()) {
                webView.getSettings().setLoadsImagesAutomatically(true);
            }
            dialog.closeProgersssDialog();
            String js = "var sharediv = document.getElementById(\"div_share\");document.body.removeChild(sharediv);";
            webView.loadUrl("javascript:" + js);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

    }

    public void onResume() {
        super.onResume();
        StatisticalTools.onResume(this);
    }

    public void onPause() {
        StatisticalTools.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
