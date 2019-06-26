package com.xywy.askforexpert.module.main.service.recruit;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.WebViewUtils2;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.model.RecruitWebInfo;
import com.xywy.askforexpert.widget.view.ProgressWebView;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.HashMap;
import java.util.Map;

/**
 * webview (职位详情+企业详情)
 * @author 王鹏
 * @2015-5-11上午8:52:07
 */

public class RecruitWebMianActivity extends Activity {
    public ProgressWebView webview;
    private String url;
    private String deliver;
    private Button btn_add_newfriend;
    private String id;
    private String coll;
    private ImageButton btn2;
    RecruitWebInfo recrweb;
    TextView tv_title;
    private Map<String, String> map = new HashMap<String, String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);

        setContentView(R.layout.webview_recruitcenter);

        url = getIntent().getStringExtra("url");
        // deliver = getIntent().getStringExtra("deliver");
        id = getIntent().getStringExtra("id");
        // coll = getIntent().getStringExtra("coll");
        btn2 = (ImageButton) findViewById(R.id.btn2);
        getDeliver(id);

        webview = (ProgressWebView) findViewById(R.id.webview);
        init();

    }

    public void initview() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        if (!TextUtils.isEmpty(coll)) {
            if ("0".equals(coll)) {
                btn2.setBackgroundResource(R.drawable.collect_nobtn_sector_);
            } else {
                btn2.setBackgroundResource(R.drawable.collected_btn_sector);
            }

        }
        btn_add_newfriend = (Button) findViewById(R.id.btn_add_newfriend);
        if (deliver.equals("0")) {
            btn_add_newfriend.setText("已投递");
            btn_add_newfriend.setBackgroundResource(R.drawable.huise_select);
        } else {
            btn_add_newfriend.setText("投递简历");
            btn_add_newfriend
                    .setBackgroundResource(R.drawable.blue_btn_selector);

            btn_add_newfriend.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    StatisticalTools.eventCount(RecruitWebMianActivity.this, "resume");
                    if (YMUserService.isGuest()) {
                        DialogUtil.LoginDialog(new YMOtherUtils(RecruitWebMianActivity.this).context);
                    } else {
                        sendData(id);
                    }

                }
            });

        }

    }

    public void sendData(String id) {
        AjaxParams params = new AjaxParams();
        String uid = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(uid + Constants.MD5_KEY);
        params.put("id", id);
        params.put("uid", uid);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.Recruit_toudi_Url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                map = ResolveJson.R_Action(t.toString());
                if (map.get("code").equals("0")) {
                    deliver = "0";
                    btn_add_newfriend.setText("已投递");
                    btn_add_newfriend
                            .setBackgroundResource(R.drawable.huise_select);
                }
                ToastUtils.shortToast(map.get("msg"));
                // TODO Auto-generated method stub
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                super.onFailure(t, errorNo, strMsg);
            }
        });

    }

    public void sendDatas(final String type) {
        AjaxParams params = new AjaxParams();
        String uid = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(id + Constants.MD5_KEY);
        params.put("id", id);
        params.put("uid", uid);
        params.put("type", type);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.Recruit_Coll_Url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                map = ResolveJson.R_Action(t.toString());
                if (map.get("code").equals("0")) {
                    if (type.equals("ins")) {
                        coll = "0";
                        btn2.setBackgroundResource(R.drawable.collect_nobtn_sector_);
                    } else {
                        coll = "-1";
                        btn2.setBackgroundResource(R.drawable.collected_btn_sector);

                    }
                }
                ToastUtils.shortToast(map.get("msg"));
                // TODO Auto-generated method stub
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                super.onFailure(t, errorNo, strMsg);
            }
        });

    }

    public void getDeliver(String id) {
        AjaxParams params = new AjaxParams();
        String sign = MD5Util.MD5(id + Constants.MD5_KEY);
        params.put("id", id);
        if (!YMUserService.isGuest()) {
            String uid = YMApplication.getLoginInfo().getData().getPid();
            params.put("uid", uid);
        }
        params.put("sign", sign);
        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.ModleUrl + "zhaopin/detailenter.interface.php",
                params, new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        Gson gson = new Gson();
                        recrweb = gson.fromJson(t.toString(),
                                RecruitWebInfo.class);
                        if (recrweb != null) {
                            if (recrweb.getCode().equals("0")) {
                                deliver = recrweb.getList().getDeliver();
                                coll = recrweb.getList().getColl();
                                initview();
                            } else {
                                deliver = -1 + "";
                                coll = -1 + "";
                            }
                        }

                        super.onSuccess(t);
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        // TODO Auto-generated method stub
                        super.onFailure(t, errorNo, strMsg);
                    }
                });

        // zhaopin/detailenter.interface.php?
    }

    private void init() {
        webview.getSettings().setJavaScriptEnabled(true);
        WebViewUtils2.safeEnhance(webview);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(false);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        // webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        // webview.setWebChromeClient(new ChromewebViewClient());
        webview.loadUrl(url);
        webview.setWebViewClient(new HelloWebViewClient());
    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            String str = "http://test.api.app.club.xywy.com/app/1.0/zhaopin/enterpriseList.interface.php?"
                    + "id=" + "&enter";

            try {
                id = url.substring(url.lastIndexOf("=") + 1,
                        url.lastIndexOf("&"));
            } catch (Exception e) {
                // TODO: handle exception
            }
            if (url.contains("enterpriseList.interface.php?")) {
                tv_title.setText("企业详情");
                btn2.setVisibility(View.GONE);
                btn_add_newfriend.setVisibility(View.GONE);

            } else {
                tv_title.setText("职位详情");
                btn2.setVisibility(View.VISIBLE);
                btn_add_newfriend.setVisibility(View.VISIBLE);
            }
            getDeliver(id);
            webview.loadUrl(url);

            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            // TODO Auto-generated method stub
            super.onReceivedSslError(view, handler, error);
        }
    }

    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                if (webview.canGoBack()) {
                    try {
                        id = webview.getUrl().substring(
                                webview.getUrl().lastIndexOf("=") + 1,
                                webview.getUrl().lastIndexOf("&"));
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    if (webview.getUrl().contains("enterpriseList.interface.php?")) {

//					try
//					{
//						id = webview.getUrl().substring(
//								webview.getUrl().lastIndexOf("=") + 1,
//								webview.getUrl().lastIndexOf("&"));
//					} catch (Exception e)
//					{
//						// TODO: handle exception
//					}
                        tv_title.setText("职位详情");
                        btn2.setVisibility(View.VISIBLE);
                        btn_add_newfriend.setVisibility(View.VISIBLE);

                    } else {
                        tv_title.setText("企业详情");
                        btn2.setVisibility(View.GONE);
                        btn_add_newfriend.setVisibility(View.GONE);
                    }
                    webview.goBack();
                    getDeliver(id);
                } else {
                    finish();
                }

                break;
            case R.id.btn2:
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(RecruitWebMianActivity.this).context);

                } else {
                    if (NetworkUtil.isNetWorkConnected()) {
                        if (!TextUtils.isEmpty(coll)) {

                            if ("0".equals(coll)) {
                                sendDatas("del");
                            } else {
                                sendDatas("ins");
                            }

                        }
                    } else {
                        ToastUtils.shortToast("网络连接失败");
                    }
                }
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            try {
                id = webview.getUrl().substring(
                        webview.getUrl().lastIndexOf("=") + 1,
                        webview.getUrl().lastIndexOf("&"));
            } catch (Exception e) {
                // TODO: handle exception
            }
            if (webview.canGoBack()) {

                if (webview.getUrl().contains("enterpriseList.interface.php?")) {

                    tv_title.setText("职位详情");
                    btn2.setVisibility(View.VISIBLE);
                    btn_add_newfriend.setVisibility(View.VISIBLE);

                } else {
                    tv_title.setText("企业详情");
                    btn2.setVisibility(View.GONE);
                    btn_add_newfriend.setVisibility(View.GONE);
                }
                webview.goBack();
                getDeliver(id);
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatisticalTools.onResume(RecruitWebMianActivity.this);

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        StatisticalTools.onPause(RecruitWebMianActivity.this);
        super.onPause();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }
}
