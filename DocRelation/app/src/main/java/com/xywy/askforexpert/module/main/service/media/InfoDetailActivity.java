package com.xywy.askforexpert.module.main.service.media;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.UMShareAPI;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.WebViewUtils2;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.BookBaseInfo;
import com.xywy.askforexpert.model.followList.IsFollowData;
import com.xywy.askforexpert.model.relatedNews.Media;
import com.xywy.askforexpert.model.relatedNews.RelatedNewsItem;
import com.xywy.askforexpert.model.relatedNews.RelatedNewsRootData;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;
import com.xywy.askforexpert.module.doctorcircle.AnonymousNameIntroActivity;
import com.xywy.askforexpert.module.main.media.MediaDetailActivity;
import com.xywy.askforexpert.module.main.service.linchuang.CommentInfoActivity1;
import com.xywy.askforexpert.module.main.service.linchuang.utils.HtmlRegexpUtil;
import com.xywy.askforexpert.module.main.service.person.PersonDetailActivity;
import com.xywy.askforexpert.module.message.share.ShareUtil;
import com.xywy.askforexpert.module.my.account.LoginActivity;
import com.xywy.askforexpert.widget.CircleImageView;
import com.xywy.askforexpert.widget.PasteEditText;
import com.xywy.base.view.ProgressDialog;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * 资讯详情 stone
 */
public class InfoDetailActivity extends YMBaseActivity {

    private static final String TAG = "InfoDetailActivity";
    private SharedPreferences sp;
    private String url;
    private String id = "";
    private String title;
    private String imageUrl;
    private int type;

    private WebView webview;

    private BookBaseInfo info;
    private static final String APP_CACAHE_DIRNAME = "/webcache";

    private String iscollection = "0";
    private String ispraise;
    private ImageButton btn2;

    public PasteEditText et_sendmmot_tiz;
    public LinearLayout rl_bottom_tiez;
    public LinearLayout rl_menu;

    private InputMethodManager manager;

    private TextView tv_max;
    private TextView tv_min;
//    private UMSocialService mController;

    private TextView unread_address_number;
    private Map<String, String> map;

    private android.support.v7.app.AlertDialog.Builder builder;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {

                case 400:
                    if (map != null && map.get("code").equals("0")) {
                        ToastUtils.shortToast(
                                map.get("msg"));
                        Intent intents = new Intent(InfoDetailActivity.this,
                                CommentInfoActivity1.class);
                        intents.putExtra("id", id);
                        intents.putExtra("type", "consult");
                        intents.putExtra("title", title);
                        intents.putExtra("url", url);
                        intents.putExtra("imageUrl", imageUrl);
                        startActivityForResult(intents, 1228);
                    } else {
                        ToastUtils.shortToast("评论失败");
                    }
                    et_sendmmot_tiz.setText("");
                    et_sendmmot_tiz.setHint("请输入你的评论");
                    hideKeyboard();
                    hideKey();
                    break;

                default:
                    break;
            }
        }
    };
    private CircleImageView infoDetailAvatar;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private int model;
    private String uuid;
    public static boolean shouldBack = false;
    private String mediaid;
    public static boolean isShouldReload = false;
    private String preId;
    private Button tv_com_menu;
    private ProgressBar newsDetailProgress;
    private ImageView praiseImg;


    public void onClick_back(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.news_detail_comment:
                intent = new Intent(InfoDetailActivity.this, CommentInfoActivity1.class);
                intent.putExtra("id", id);
                intent.putExtra("type", "consult");
                intent.putExtra("title", title);
                intent.putExtra("url", url);
                intent.putExtra("imageUrl", imageUrl);
//                startActivityForResult(intent, 1228);
                startActivity(intent);
                break;

            case R.id.praise_imgbtn:
                //改为点赞入口
                StatisticalTools.eventCount(this, "InPraise");
                if (!YMUserService.isGuest()) {
                    if (!TextUtils.isEmpty(id)) {
                        praiseAdd(id);
                    }
                } else {
                    goToLogin(builder);
                }
                break;

            case R.id.unread_address_number:
                intent = new Intent(InfoDetailActivity.this, CommentInfoActivity1.class);
                intent.putExtra("id", id);
                intent.putExtra("type", "consult");
                intent.putExtra("title", title);
                intent.putExtra("url", url);
                intent.putExtra("imageUrl", imageUrl);
                startActivityForResult(intent, 1228);
                break;

            case R.id.btn_share:
                StatisticalTools.eventCount(InfoDetailActivity.this, "zxshare");


                new ShareUtil.Builder()
                        .setTitle("医学资讯")
                        .setText(title)
                        .setTargetUrl(url)
                        .setImageUrl(imageUrl)
                        .setShareId(id)
                        .setShareSource("1")
                        .build(this).innerShare();

                break;
            case R.id.btn1:
                hideKeyboard();
                if (shouldBack) {
                    Intent intent1 = new Intent(InfoDetailActivity.this, MediaDetailActivity.class);
                    intent1.putExtra("mediaId", mediaid);
                    InfoDetailActivity.this.startActivity(intent1);
                } else {
                    finish();
                }
                break;
            case R.id.btn2:
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(InfoDetailActivity.this).context);
                } else {
                    if (!NetworkUtil.isNetWorkConnected()) {

                        ToastUtils.shortToast("网络连接失败");
                        return;
                    }
                    String userid1 = YMApplication.getLoginInfo().getData()
                            .getPid();
                    String sign1 = MD5Util.MD5(userid1 + Constants.MD5_KEY);
                    AjaxParams params = new AjaxParams();
                    params.put("a", "actionAdd");
                    params.put("collecid", id);
                    params.put("userid", userid1);
                    params.put("c", "collection");
                    params.put("sign", sign1);
                    FinalHttp ft = new FinalHttp();
                    ft.post(CommonUrl.Consulting_Url, params, new AjaxCallBack() {
                        @Override
                        public void onSuccess(String t) {
                            DLog.d(TAG, "info detail favorite = " + t.toString());
                            map = ResolveJson.R_Action(t.toString());
                            // handler.sendEmptyMessage(200);
                            if (map == null) {
                                return;
                            }
                            String code = map.get("code");
                            if (!TextUtils.isEmpty(code)) {
                                if ("1".equals(code)) { // 取消收藏"
                                    btn2.setBackgroundResource(R.drawable.collected_btn_sector);
                                    ToastUtils.shortToast(
                                            "取消收藏");
                                } else if ("0".equals(code)) {// "收藏成功"
                                    // T.showNoRepeatShort(
                                    // getApplicationContext(), "收藏成功");
                                    ToastUtils.shortToast(
                                            map.get("msg"));
                                    btn2.setBackgroundResource(R.drawable.collect_nobtn_sector_);
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
                break;
            case R.id.btn_send_real:// 实名

                //判断认证 stone
                DialogUtil.showUserCenterCertifyDialog(InfoDetailActivity.this, new MyCallBack() {
                    @Override
                    public void onClick(Object data) {

                        String userid = YMApplication.getLoginInfo().getData().getPid();
                        String toUserid = "0";
                        String themeid = id;
                        String sign = MD5Util.MD5(userid + toUserid + themeid
                                + Constants.MD5_KEY);
                        String str_content = HtmlRegexpUtil.filterHtml(et_sendmmot_tiz
                                .getText().toString().trim());
                        AjaxParams params = new AjaxParams();
                        params.put("c", "comment");
                        params.put("a", "comment");
                        params.put("userid", userid);
                        params.put("content", str_content);
                        params.put("toUserid", "0");
                        params.put("pid", "0");
                        params.put("level", "0");
                        params.put("themeid", themeid);
                        params.put("sign", sign);
                        // YMApplication.Trace("空   "
                        // + TextUtils.isEmpty(et_sendmmot.getText()
                        // .toString().trim()));
                        if (!TextUtils.isEmpty(et_sendmmot_tiz.getText().toString().trim())
                                && !str_content.equals("")) {
                            if (str_content.length() > 150) {
                                ToastUtils.shortToast(
                                        "字数限制在150字以内");
                            } else {
                                if (NetworkUtil
                                        .isNetWorkConnected()) {
                                    hideKey();
                                    hideKeyboard();
                                    setData(params);
                                } else {
                                    ToastUtils.shortToast("网络连接失败");
                                }
                            }

                        } else {
                            ToastUtils.shortToast("评论内容不能为空");
                            et_sendmmot_tiz.setText("");
                        }

                    }

                }, null, null);

                break;
            case R.id.btn_send_anony: // 匿名

                //判断认证 stone
                DialogUtil.showUserCenterCertifyDialog(InfoDetailActivity.this, new MyCallBack() {
                    @Override
                    public void onClick(Object data) {
                        String userid = YMApplication.getLoginInfo().getData().getPid();
                        String toUserid = "0";
                        String themeid = id;
                        String sign = MD5Util.MD5(userid + toUserid + themeid + Constants.MD5_KEY);
                        String str_content = HtmlRegexpUtil.filterHtml(et_sendmmot_tiz.getText().toString().trim());
                        AjaxParams params = new AjaxParams();
                        params.put("c", "comment");
                        params.put("a", "comment");
                        params.put("userid", userid);
                        params.put("content", str_content);
                        params.put("toUserid", "0");
                        params.put("pid", "0");
                        params.put("level", "2");
                        params.put("themeid", themeid);
                        params.put("sign", sign);
                        // YMApplication.Trace("空   "
                        // + TextUtils.isEmpty(et_sendmmot.getText()
                        // .toString().trim()));
                        if (!TextUtils.isEmpty(et_sendmmot_tiz.getText().toString().trim())
                                && !str_content.equals("")) {
                            if (str_content.length() > 150) {
                                ToastUtils.shortToast("字数限制在150字以内");
                            } else {
                                hideKey();
                                hideKeyboard();
                                setData(params);
                            }
                        } else {
                            ToastUtils.shortToast("评论内容不能为空");
                            et_sendmmot_tiz.setText("");
                        }
                    }
                }, null, null);

                break;
            case R.id.tv_com_menu:
//                if (model == 1) {
//                    Intent intent1 = new Intent(InfoDetailActivity.this, CommentInfoActivity1.class);
//                    intent1.putExtra("id", id);
//                    intent1.putExtra("type", "consult");
//                    intent1.putExtra("title", title);
//                    intent1.putExtra("url", url);
//                    intent1.putExtra("imageUrl", imageUrl);
//                    startActivityForResult(intent1, 1228);
//                } else {
                rl_menu.setVisibility(View.GONE);
                rl_bottom_tiez.setVisibility(View.VISIBLE);
                et_sendmmot_tiz.requestFocus();
                et_sendmmot_tiz.setFocusableInTouchMode(true);
                et_sendmmot_tiz.setFocusable(true);
                ShowKeyboard(et_sendmmot_tiz);
//                }
                break;
            default:
                break;
        }
    }

    private void praiseAdd(String id) {
        AjaxParams params = new AjaxParams();
        String userid = YMApplication.getLoginInfo().getData().getPid();

        final String photo = YMApplication.getLoginInfo().getData().getPhoto();

        String sign = MD5Util.MD5(userid + id + Constants.MD5_KEY);
        params.put("a", "actionAdd");
        params.put("id", id);
        params.put("type", "1");
        params.put("userid", userid);
        params.put("c", "praise");
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();

        fh.post(CommonUrl.Consulting_Url, params, new AjaxCallBack() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                DLog.i(TAG, "----" + s);

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("msg");
                    switch (code) {
                        case -1:
                            Toast.makeText(InfoDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                            break;
                        case 0:
                            webview.loadUrl("javascript:addLike('" + photo + "')");
                            praiseImg.setImageResource(R.drawable.info_praised);
                            Toast.makeText(InfoDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 发送评论
     *
     * @param params
     */
    public void setData(AjaxParams params) {
        final ProgressDialog dialog = new ProgressDialog(this, "加载中。。。");
        dialog.setCanceledOnTouchOutside(false);
        dialog.showProgersssDialog();
        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.Consulting_Url, params, new AjaxCallBack() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                dialog.dismiss();
                super.onFailure(t, errorNo, strMsg);

            }

            @Override
            public void onSuccess(String t) {
                dialog.dismiss();
                DLog.i(TAG, "评论返回数据。" + t.toString());
                map = ResolveJson.R_Action(t.toString());
                if (rl_bottom_tiez != null & rl_menu != null) {
//					hideKey();
                    hideKeyboard();
                    handler.sendEmptyMessage(400);

                    rl_bottom_tiez.setVisibility(View.GONE);
                    rl_menu.setVisibility(View.VISIBLE);
//					hideKeyboard();

                }
                super.onSuccess(t);
            }
        });
    }

    public void initview() {

        if (!TextUtils.isEmpty(iscollection)) {
            if ("1".equals(iscollection)) {
                btn2.setBackgroundResource(R.drawable.collect_nobtn_sector_);
            } else {
                btn2.setBackgroundResource(R.drawable.collected_btn_sector);
            }
        }
//        if (!TextUtils.isEmpty(info.getList().getCommNum())) {
//            if (!info.getList().getCommNum().equals("0")
//                    & Integer.valueOf(info.getList().getCommNum()) > 10) {
//                unread_address_number.setVisibility(View.VISIBLE);
//                if (Integer.valueOf(info.getList().getCommNum()) > 99) {
//                    unread_address_number.setText(99 + "+");
//                } else {
//                    unread_address_number.setText(info.getList().getCommNum());
//                }
//            }
//
//        }

        // if ("1".equals(iscollection)) {
        // // tv_collect.setText("已收藏");
        // } else {
        // // tv_collect.setText("收藏");
        // }
        // if ("1".equals(ispraise)) {
        // // tv_praise.setText("已点赞");
        // } else {
        // // tv_praise.setText("点赞");
        // }
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private void init() {
        webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        webview.getSettings().setJavaScriptEnabled(true);
        WebViewUtils2.safeEnhance(webview);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(false);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // webview.setWebChromeClient(new ChromewebViewClient());

        webview.getSettings().setRenderPriority(RenderPriority.HIGH);
        if (NetworkUtil.isNetWorkConnected()) {
            webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // 设置缓存模式 
        } else {
            webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); // 设置缓存模式 
        }

        // 开启 DOM storage API 功能 
        webview.getSettings().setDomStorageEnabled(true);
        // 开启 database storage API 功能 
        webview.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME;
        //       String cacheDirPath =
        // getCacheDir().getAbsolutePath()+Constant.APP_DB_DIRNAME; 

        // 设置数据库缓存路径 
        webview.getSettings().setDatabasePath(cacheDirPath);
        // 设置  Application Caches 缓存目录 
        webview.getSettings().setAppCachePath(cacheDirPath);
        // 开启 Application Caches
        // 功能
        webview.getSettings().setAppCacheEnabled(true);

        webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        webview.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress < 100) {
                    newsDetailProgress.setVisibility(View.VISIBLE);
                    newsDetailProgress.setProgress(newProgress);
                } else {
                    newsDetailProgress.setVisibility(View.GONE);
                }
            }
        });
        webview.setWebViewClient(new HelloWebViewClient());

        // webview.setWebChromeClient(new WebChromeClient()
        // {
        // public void onProgressChanged(WebView view, int progress)
        // {
        // // Activity和Webview根据加载程度决定进度条的进度大小
        // // 当加载到100%的时候 进度条自动消失
        // YMApplication.Trace("进度条"+progress);
        // InfoDetailActivity.this.setProgress(progress);
        // }
        //
        // });

//        webview.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public boolean onConsoleMessage(ConsoleMessage cm) {
//                DLog.d(TAG, cm.message() + " -- From line "
//                        + cm.lineNumber() + " of "
//                        + cm.sourceId());
//                return true;
//            }
//
//            @Override
//            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
//                Toast.makeText(InfoDetailActivity.this, message, Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });

        DLog.d(TAG, "web url = " + (url + "&t_mobile=android&yimaifrom=yimai&userid=" + uuid));
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie((url + "&t_mobile=android&yimaifrom=yimai&userid=" + uuid),
                "Yimai-Request=" + AppUtils.getAPPInfo());
        DLog.d(TAG, "info detail load url = " + (url + "&t_mobile=android&yimaifrom=yimai&userid=" + uuid));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webview.loadUrl(url + "&t_mobile=android&yimaifrom=yimai&userid=" + uuid);
//        webview.addJavascriptInterface(new ClickPraise(this), "addLike");

    }

    /**
     * 获取收藏 点赞状态
     */
    public void getData() {
        AjaxParams params = new AjaxParams();
        params.put("c", "article");
        params.put("a", "msg");
        String sign = MD5Util.MD5(id + uuid + Constants.MD5_KEY);
        params.put("userid", uuid);
        DLog.i(TAG, "当前文章id=" + id);
        params.put("id", id);
        params.put("type", "1");
        params.put("sign", sign);
        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.Consulting_Url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                DLog.i(TAG, "colloct parise" + t.toString());
                Gson gson = new Gson();
                info = ResolveJson.R_Consult_Book(t.toString());
                if (info == null) {
                    ToastUtils.shortToast("数据获取失败");
                    return;
                }
                if ("0".equals(info.getCode())) {
                    iscollection = info.getList().getIscollection();
                    ispraise = info.getList().getIspraise();
                    initview();

                    if (ispraise.equals("1")) {
                        praiseImg.setImageResource(R.drawable.info_praised);
                    }

                }
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    private void requestNewsInfo(final boolean isShould) {
        FinalHttp finalHttp = new FinalHttp();
        AjaxParams params = new AjaxParams();
        params.put("a", "row");
        params.put("userid", uuid);
        params.put("c", "article");
        params.put("id", id);
        params.put("sign", MD5Util.MD5(id + Constants.MD5_KEY));
        DLog.d(TAG, "related news url = " + CommonUrl.NEWS_LIST_URL + "?" + params.toString());
        finalHttp.post(CommonUrl.NEWS_LIST_URL, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                DLog.d(TAG, "related news string = " + id + ", " + s);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (jsonObject == null) {
                    if (isShould) {
                        Toast.makeText(InfoDetailActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    RelatedNewsRootData rootData = new RelatedNewsRootData();
                    rootData.parseJson(jsonObject);

                    if (rootData.getCode() == 0) {
                        RelatedNewsItem item = rootData.getItem();
                        title = item.getTitle();
                        DLog.d(TAG, "item url = " + item.getUrl());
                        url = item.getUrl();
                        model = item.getModel();
                        imageUrl = item.getImage();
                        DLog.d(TAG, "news detail model = " + model);
                        if (model == 3) {
                            findViewById(R.id.news_detail_comment).setVisibility(View.VISIBLE);
                        } else {
                            findViewById(R.id.news_detail_comment).setVisibility(View.GONE);
                        }
                        if (item.getUrl() == null || "".equals(item.getUrl())) {
                            if (isShould) {
                                Toast.makeText(InfoDetailActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            DLog.d(TAG, "related url = " + item.getUrl());
                            if (item.getPraise() == 1) {
                                praiseImg.setImageResource(R.drawable.info_praised);
                            } else {
                                praiseImg.setImageResource(R.drawable.info_detail_img);
                            }
                            DLog.d(TAG, "type = " + item.getType() + ", model = " + item.getModel());
                            if (item.getType() == 1 && (item.getModel() == 1 || item.getModel() == 0)) { // 媒体号、多图
                                if (item.getModel() == 1) {
                                    rl_menu.setBackgroundColor(Color.parseColor("#DD000000"));
                                    tv_com_menu.setBackgroundResource(R.drawable.news_pics_bottom_shape);
                                }
                                final Media media = item.getMedia();
                                infoDetailAvatar.setVisibility(View.VISIBLE);
                                imageLoader.displayImage(media.getImg(), infoDetailAvatar, options);
                                infoDetailAvatar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(InfoDetailActivity.this, MediaDetailActivity.class);
                                        mediaid = media.getMediaid();
                                        intent.putExtra("mediaId", mediaid);
                                        InfoDetailActivity.this.startActivity(intent);
                                    }
                                });
                            } else {
                                rl_menu.setBackgroundColor(Color.parseColor("#F6F6F6"));
                                tv_com_menu.setBackgroundResource(R.drawable.edit_frame_sytle);
                                infoDetailAvatar.setVisibility(View.GONE);
                            }

                            if (isShould) {
                                webview.loadUrl(item.getUrl() + "&t_mobile=android&yimaifrom=yimai&userid=" + uuid);
                            }
                        }
                    } else {
                        if (isShould) {
                            Toast.makeText(InfoDetailActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);

                if (isShould) {
                    Toast.makeText(InfoDetailActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            DLog.i(TAG, "webview==" + url);
//            图像和名字是user://id相关评论是comment://相关阅读relatedReading://id
            if (url.contains("relatedreading://")) {
                id = url.replaceAll("relatedreading://", "");
//                webview.loadUrl("http://club.xywy.com/zixun/d" + id + ".html?xywyfrom=h5&t_mobile=android");
                requestNewsInfo(true);
                return true;
            }
            if (url.contains("comment://")) {
                id = url.replaceAll("comment://", "");
                //去评论列表
                Intent intent = new Intent(InfoDetailActivity.this, CommentInfoActivity1.class);
                intent.putExtra("id", id);
                intent.putExtra("type", "consult");
                intent.putExtra("title", title);
                intent.putExtra("url", url);
                intent.putExtra("imageUrl", imageUrl);
                startActivityForResult(intent, 1228);
                return true;
            }
            if (url.contains("user://")) {
                id = url.replaceAll("user://", "");
                //去个人主页用户
                Intent intent = new Intent(InfoDetailActivity.this, PersonDetailActivity.class);
                intent.putExtra("uuid", id);
                intent.putExtra("isDoctor", "2");
                startActivity(intent);
                return true;
            }
            if (url.contains("anonymous://")) {
                String anonymousName = url.replaceAll("anonymous://", "");
                Intent intent = new Intent(InfoDetailActivity.this, AnonymousNameIntroActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("anonymousName", anonymousName);
                startActivity(intent);
                return true;
            }
            if (url.contains("mediainfo://")) {
                mediaid = url.replace("mediainfo://", "");
                Intent intent = new Intent(InfoDetailActivity.this, MediaDetailActivity.class);
                intent.putExtra("mediaId", mediaid);
                startActivity(intent);
                return true;
            }
            if (url.contains("subscribemediabtn://")) {
                // 订阅
                mediaid = url.replace("subscribemediabtn://", "");
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(InfoDetailActivity.this).context);
                } else {
                    addFollow(YMApplication.getPID(), mediaid);
                }
                return true;
            }
            if (url.contains("invitation://")) {

                new ShareUtil.Builder()
                        .setTitle(getString(R.string.invite_money_share_title))
                        .setText(getString(R.string.invite_money_share_content))
                        .setTargetUrl(url.replace("invitation://", ""))
                        .setImageUrl(ShareUtil.DEFAULT_SHARE_IMG_ULR)
                        .build(InfoDetailActivity.this).outerShare();

                return true;
            }

            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            CookieManager cookieManager = CookieManager.getInstance();
            String CookieStr = cookieManager.getCookie(url);
            LogUtils.d("Cookies = " + CookieStr);
            getData();
        }
    }

    // 显示虚拟键盘
    public static void ShowKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }

    private void hideKey() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }

    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_sendmmot_tiz.getWindowToken(), 0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        url = intent.getStringExtra("url");
        id = intent.getStringExtra("ids");
        title = intent.getStringExtra("title");
        imageUrl = intent.getStringExtra("imageurl");
    }

    @Override
    public void onResume() {
        super.onResume();
        StatisticalTools.onPause(InfoDetailActivity.this);

        DLog.i(TAG, "onResume");

        if (sp != null & et_sendmmot_tiz != null) {
            et_sendmmot_tiz.setText(sp.getString("tiezi" + id, ""));
        }
        if (NetworkUtil.isNetWorkConnected()) {
            DLog.d(TAG, "preId = " + preId + ", id = " + id);
            if (preId != null && !preId.equals(id)) {
                init();
            }

            if (isShouldReload) {
                webview.stopLoading();
                webview.reload();
                isShouldReload = false;
            }

            getData();
        } else {
            ToastUtils.shortToast("网络连接失败");
        }
    }

    @Override
    public void onPause() {

        StatisticalTools.onPause(InfoDetailActivity.this);
        super.onPause();
//        mController.getConfig().removePlatform(SHARE_MEDIA.EMAIL);
    }

    @Override
    protected void initView() {
        hideCommonBaseTitle();

        sp = getSharedPreferences("save_user", MODE_PRIVATE);

        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnFail(R.drawable.media_smail_icon)
                .showImageOnLoading(R.drawable.media_smail_icon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        if (YMUserService.isGuest()) {
            uuid = "0";
        } else {
            uuid = YMApplication.getPID();
        }

        builder = new android.support.v7.app.AlertDialog.Builder(this);

        url = getIntent().getStringExtra("url");
        id = getIntent().getStringExtra("ids");
        title = getIntent().getStringExtra("title");
        imageUrl = getIntent().getStringExtra("imageurl");
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        unread_address_number = (TextView) findViewById(R.id.unread_address_number);
        tv_max = (TextView) findViewById(R.id.tv_max);
        tv_min = (TextView) findViewById(R.id.tv_min);
        rl_menu = (LinearLayout) findViewById(R.id.rl_menu);
        rl_bottom_tiez = (LinearLayout) findViewById(R.id.rl_bottom_tiez);
        et_sendmmot_tiz = (PasteEditText) findViewById(R.id.et_sendmmot_tiz);
        infoDetailAvatar = (CircleImageView) findViewById(R.id.info_detail_avatar);
        btn2 = (ImageButton) findViewById(R.id.btn2);
        tv_com_menu = (Button) findViewById(R.id.tv_com_menu);

        praiseImg = (ImageView) findViewById(R.id.praise_imgbtn);

        newsDetailProgress = (ProgressBar) findViewById(R.id.news_detail_progress);
        DLog.i(TAG, "detail_info" + url);
        webview = (WebView) findViewById(R.id.webview);
        // 初始化分享平台
//        initSocialSDK(title, url, imageUrl);
        requestNewsInfo(false);
        init();

        if (NetworkUtil.isNetWorkConnected()) {
            getData();
        } else {
            ToastUtils.shortToast("网络连接失败");
        }
        et_sendmmot_tiz.setText(sp.getString("tiezi" + id, ""));
        et_sendmmot_tiz.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                tv_min.setText(String.valueOf(arg0.length()));
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                DLog.i(TAG, "帖子评论保存" + arg0.toString());
                sp.edit().putString("tiezi" + id, arg0.toString()).apply();

            }
        });
        webview.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (rl_bottom_tiez.getVisibility() == View.VISIBLE) {
                            rl_bottom_tiez.setVisibility(View.GONE);
                            rl_menu.setVisibility(View.VISIBLE);
                            hideKeyboard();

                        }
                        break;
                    case MotionEvent.ACTION_UP:

                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    default:
                        break;
                }

                return false;

            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onStop() {

        StatisticalTools.onPause(InfoDetailActivity.this);

        shouldBack = false;
        preId = id;
        super.onStop();
    }

    @Override
    protected void onDestroy() {


        DLog.d(TAG, TAG + " onDestroy");

        webview.stopLoading();
        webview.loadUrl("");
        webview.reload();
        webview = null;

        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_info_detail;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1228) {
            if (resultCode == 1228) {
                if (webview != null) {
                    webview.setWebChromeClient(new WebChromeClient() {
                        @Override
                        public boolean onConsoleMessage(ConsoleMessage cm) {
                            DLog.d(TAG, cm.message() + " -- From line "
                                    + cm.lineNumber() + " of "
                                    + cm.sourceId());
                            return true;
                        }
                    });
                    if (model == 0) { // 普通资讯
                        webview.stopLoading();
                        webview.reload();
                    }
                }
            }
        }
    }

    protected void goToLogin(AlertDialog.Builder builder2) {
        builder2.setTitle("您还没有登录")
                .setMessage("请登录以后使用")
                .setPositiveButton("去登录",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Intent intent = new Intent(YMApplication.getAppContext(),
                                        LoginActivity.class);
                                intent.putExtra("guest", true);
                                startActivity(intent);
                            }
                        }).setNegativeButton("取消", null).create().show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (webview != null && webview.canGoBack()) {
                webview.goBack();
            } else {
                if (shouldBack) {
                    Intent intent1 = new Intent(InfoDetailActivity.this, MediaDetailActivity.class);
                    intent1.putExtra("mediaId", mediaid);
                    InfoDetailActivity.this.startActivity(intent1);
                } else {
                    this.finish();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
                        Toast.makeText(InfoDetailActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                        YMApplication.isrefresh = true;
                        webview.reload();
                    } else {
                        Toast.makeText(InfoDetailActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(InfoDetailActivity.this,
                            InfoDetailActivity.this.getString(R.string.server_error),
                            Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(InfoDetailActivity.this, MediaDetailActivity.class);
                intent.putExtra("mediaId", touserid);
                startActivity(intent);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);

                Toast.makeText(InfoDetailActivity.this, "关注失败", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(InfoDetailActivity.this, MediaDetailActivity.class);
                intent.putExtra("mediaId", touserid);
                startActivity(intent);
            }
        });
    }
}
