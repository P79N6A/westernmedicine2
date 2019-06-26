package com.xywy.askforexpert.module.message.share;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.model.BookBaseInfo;
import com.xywy.askforexpert.model.followList.IsFollowData;
import com.xywy.askforexpert.module.doctorcircle.AnonymousNameIntroActivity;
import com.xywy.askforexpert.module.main.media.MediaDetailActivity;
import com.xywy.askforexpert.module.main.service.linchuang.CommentInfoActivity1;
import com.xywy.askforexpert.module.main.service.person.PersonDetailActivity;
import com.xywy.askforexpert.widget.view.ProgressWebView;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.List;
import java.util.Map;

/**
 * 分享展示 web页面
 *
 * @author 王鹏
 * @2015-8-5上午11:09:19
 */
public class ShareWebActivity extends Activity {

    String url;
    String title;
    String imageUrl;
    ProgressWebView webview;
    String newTitle;
    String id;
    private List<SHARE_MEDIA> platformList;
    private Activity context;
    private boolean sharedQQ;
    //    private UMSocialService mController;
    private Dialog mDialog;
    private String iscollection = "0";
    private String ispraise;

    private BookBaseInfo info;

    // private UMSocialService mController;

    private SharedPreferences sp;

    private String uuid;
    private String[] title1 = {};
    private int[] pic1 = new int[]{R.drawable.icon_wxin_nor,
            R.drawable.icon_pyq_wx_nor, R.drawable.icon_qq_nor,
            R.drawable.icon_qq_qz_nor, R.drawable.icon_sinlang_nor,
            R.drawable.icon_mpj_nor, R.drawable.icon_yquan_nor,
            R.drawable.icon_share_collec_no};
    private String[] title2 = {};
    private int[] pic2 = new int[]{R.drawable.icon_wxin_nor,
            R.drawable.icon_pyq_wx_nor, R.drawable.icon_qq_nor,
            R.drawable.icon_qq_qz_nor, R.drawable.icon_sinlang_nor,
            R.drawable.icon_mpj_nor, R.drawable.icon_yquan_nor,
            R.drawable.ico_share_colle_ye};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.share_web);

        if (!YMUserService.isGuest()) {
            uuid = YMApplication.getPID();
        } else {
            uuid = "0";
        }

        CommonUtils.initSystemBar(this);
        context = ShareWebActivity.this;
        sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        sp.edit().putString("mustUpdata", "").apply();
//        mController = UMServiceFactory
//                .getUMSocialService("com.umeng.share");
        webview = (ProgressWebView) findViewById(R.id.webView);

        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        imageUrl = getIntent().getStringExtra("imageUrl");

        id = getIntent().getStringExtra("posts_id");
        if (!TextUtils.isEmpty(title) & title.length() > 8) {
            newTitle = title.substring(0, 8) + "...";
        } else if (!TextUtils.isEmpty(title) & title.length() <= 8) {
            newTitle = title;
        }

        if (TextUtils.isEmpty(id)) {
            title1 = new String[]{"微信", "朋友圈", "QQ", "QQ空间", "微博",
                    "名片夹", "医圈"};
            title2 = new String[]{"微信", "朋友圈", "QQ", "QQ空间", "微博",
                    "名片夹", "医圈"};
        } else {
            title1 = new String[]{"微信", "朋友圈", "QQ", "QQ空间", "微博",
                    "名片夹", "医圈", "收藏"};
            title2 = new String[]{"微信", "朋友圈", "QQ", "QQ空间", "微博",
                    "名片夹", "医圈", "已收藏"};
        }
        ((TextView) findViewById(R.id.tv_title)).setText(newTitle);
        init();
        if (NetworkUtil.isNetWorkConnected()) {
            getData();
        } else {
            ToastUtils.shortToast("网络连接失败");
        }

    }

    private void init() {
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(false);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        // webview.setWebChromeClient(new ChromewebViewClient());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url + "&t_mobile=android&yimaifrom=yimai&userid=" + uuid, "Yimai-Request=" + AppUtils.getAPPInfo());
        if (url.contains(CommonUrl.H5_EXAM)) {
            webview.loadUrl(CommonUrl.H5_EXAM + uuid);
        } else {
            webview.loadUrl(url + "&t_mobile=android&yimaifrom=yimai&userid=" + uuid);
        }
        webview.setWebViewClient(new HelloWebViewClient());
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

            case R.id.btn2:


                break;
            default:
                break;
        }

    }



    public void setCollection() {
        if (YMUserService.isGuest()) {
            DialogUtil.LoginDialog(new YMOtherUtils(ShareWebActivity.this).context);
        } else {
            if (!NetworkUtil.isNetWorkConnected()) {

                ToastUtils.shortToast("网络连接失败");
                return;
            }
            String userid1 = YMApplication.getLoginInfo().getData().getPid();
            String sign1 = MD5Util.MD5(userid1 + Constants.MD5_KEY);
            AjaxParams params = new AjaxParams();
            params.put("a", "actionAdd");
            params.put("collecid", id);
            params.put("userid", userid1);
            params.put("c", "collection");
            params.put("sign", sign1);
            FinalHttp ft = new FinalHttp();
            ft.post(CommonUrl.Consulting_Url, params,
                    new AjaxCallBack() {
                        @Override
                        public void onSuccess(String t) {
                            Map<String, String> map = ResolveJson
                                    .R_Action(t.toString());
                            // handler.sendEmptyMessage(200);
                            if (map == null) {
                                return;
                            }
                            String code = map.get("code");
                            if (!TextUtils.isEmpty(code)) {
                                if ("1".equals(code)) { // 取消收藏"
                                    iscollection = "0";
                                    ToastUtils.shortToast(
                                            "取消收藏");
                                } else if ("0".equals(code)) {// "收藏成功"
                                    // T.showNoRepeatShort(
                                    // getApplicationContext(), "收藏成功");
                                    ToastUtils.shortToast(
                                            map.get("msg"));
                                    iscollection = "1";
                                }
                            }
                            super.onSuccess(t);
                        }

                        @Override
                        public void onFailure(Throwable t, int errorNo,
                                              String strMsg) {
                            super.onFailure(t, errorNo, strMsg);
                        }
                    });
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

    /**
     * 获取收藏 点赞状态
     */
    public void getData() {
        String userid;
        AjaxParams params = new AjaxParams();
        params.put("c", "article");
        params.put("a", "msg");

        if (YMUserService.isGuest()) {
            userid = "0";
        } else {
            userid = YMApplication.getLoginInfo().getData().getPid();
        }
        String sign = MD5Util.MD5(id + userid + Constants.MD5_KEY);
        params.put("userid", userid);
        params.put("id", id);
        params.put("type", "1");
        params.put("sign", sign);
        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.Consulting_Url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                Gson gson = new Gson();
                info = ResolveJson.R_Consult_Book(t.toString());
                if ("0".equals(info.getCode())) {
                    iscollection = info.getList().getIscollection();
                    ispraise = info.getList().getIspraise();
                    // initview();
                }
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    private void showDailog(View view, int gravity) {
        if (context != null && context.isFinishing()) {
            return;
        }
        mDialog = new Dialog(context, R.style.MyDialogStyle);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);
        Window window = mDialog.getWindow();
        window.setGravity(gravity);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setWindowAnimations(R.style.PopupAnimation);
        WindowManager.LayoutParams mParams = mDialog.getWindow()
                .getAttributes();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        mParams.width = (int) (display.getWidth() * 1.0);
        mDialog.getWindow().setAttributes(mParams);
        mDialog.show();
    }



    @Override
    protected void onDestroy() {
        webview.stopLoading();
        webview.loadUrl("");
        webview.reload();
        webview = null;

        super.onDestroy();
    }

    /**
     * 订阅
     */
    private void addFollow(String userid, final String touserid) {
        String bind = userid + touserid;
        String sign = MD5Util.MD5(bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("a", "dcFriend");
        params.put("m", "friend_add");
        params.put("userid", userid);
        params.put("touserid", touserid);
        params.put("bind", bind);
        params.put("sign", sign);

        FinalHttp request = new FinalHttp();
        request.post(CommonUrl.doctor_circo_url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);

                Gson gson = new Gson();
                IsFollowData data = gson.fromJson(s, IsFollowData.class);

                if (data != null) {
                    if (data.getCode().equals("0")) {
                        Toast.makeText(ShareWebActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                        webview.reload();
                    } else {
                        Toast.makeText(ShareWebActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ShareWebActivity.this,
                            ShareWebActivity.this.getString(R.string.server_error),
                            Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(ShareWebActivity.this, MediaDetailActivity.class);
                intent.putExtra("mediaId", touserid);
                startActivity(intent);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);

                Toast.makeText(ShareWebActivity.this, "关注失败", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ShareWebActivity.this, MediaDetailActivity.class);
                intent.putExtra("mediaId", touserid);
                startActivity(intent);
            }
        });
    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("mediainfo://")) {
                String mediaId = url.replace("mediainfo://", "");
                Intent intent = new Intent(ShareWebActivity.this, MediaDetailActivity.class);
                intent.putExtra("mediaId", mediaId);
                startActivity(intent);
            } else if (url.contains("subscribemediabtn://")) {
                // 订阅
                String mediaId = url.replace("subscribemediabtn://", "");
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(ShareWebActivity.this).context);
                } else {
                    addFollow(YMApplication.getPID(), mediaId);
                }
            } else if (url.contains("comment://")) {
                String id = url.replaceAll("comment://", "");
                //去评论列表
                Intent intent = new Intent(ShareWebActivity.this, CommentInfoActivity1.class);
                intent.putExtra("id", id);
                intent.putExtra("type", "consult");
                intent.putExtra("title", title);
                intent.putExtra("url", url);
                intent.putExtra("imageUrl", imageUrl);
                startActivityForResult(intent, 1228);
            } else if (url.contains("user://")) {
                String id = url.replaceAll("user://", "");
                //去个人主页用户
                Intent intent = new Intent(ShareWebActivity.this, PersonDetailActivity.class);
                intent.putExtra("uuid", id);
                intent.putExtra("isDoctor", "2");
                startActivity(intent);
            } else if (url.contains("anonymous://")) {
                String anonymousName = url.replaceAll("anonymous://", "");
                Intent intent = new Intent(ShareWebActivity.this, AnonymousNameIntroActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("anonymousName", anonymousName);
                startActivity(intent);
            } else {
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(ShareWebActivity.this).context);
                } else {
                    webview.loadUrl(url);
                }
            }
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
}
