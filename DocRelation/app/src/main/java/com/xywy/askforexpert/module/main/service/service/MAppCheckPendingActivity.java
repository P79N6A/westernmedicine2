package com.xywy.askforexpert.module.main.service.service;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.WebViewUtils2;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <p>
 * 功能：服务-预约加号子页面的item
 * </p>
 *
 * @author liuxuejiao
 * @2015-5-14 上午 9:27:30
 */
public class MAppCheckPendingActivity extends Activity implements
        OnClickListener {
    /**
     * mBtnBack: 返回按钮
     */
    private View mBtnBack;
    /**
     * mBtnRefuse: 拒绝按钮
     */
    private Button mBtnRefuse;
    /**
     * mBtnAgree: 同意按钮
     */
    private Button mBtnAgree;
    /**
     * mTxtTitle: 标题文字
     */
    private TextView mTxtTitle;
    /**
     * mTxtBasicInfos: 姓名、性别、年龄
     */
    private TextView mTxtBasicInfos;
    /**
     * mTxtAddress:所在地
     */
    private TextView mTxtAddress;
    /**
     * mTxtIdentify: 患者身份证号
     */
    private TextView mTxtIdentify;
    /**
     * mTxtPhonenum: 手机号码
     */
    private TextView mTxtPhonenum;
    /**
     * mTxtSick: 所患疾病
     */
    private TextView mTxtSick;
    /**
     * mTxtTreathistory: 曾经得到某专家诊治
     */
    private TextView mTxtTreathistory;
    /**
     * mTxtPurpose: 预约目的
     */
    private TextView mTxtPurpose;
    /**
     * mTxtLasthospital: 最后一次就诊医院
     */
    private TextView mTxtLasthospital;
    /**
     * mTxtJobContent: 岗位职责
     */
    private TextView mTxtJobContent;
    /**
     * mTextViewOrderNum: 预约订单号
     */
    private TextView mTextViewOrderNum;
    /**
     * mTextViewTime: 预约订单时间
     */
    private TextView mTextViewTime;
    /**
     * mImageViewTop: 订单状态
     */
    private ImageView mImageViewTop;

    private WebView webView;

    private String webUrl, id, time, show, curState, audit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        webUrl = getIntent().getStringExtra("url");
        id = getIntent().getStringExtra("id");
        time = getIntent().getStringExtra("time");
        curState = getIntent().getStringExtra("state");            //状态 1，2，3，4
        show = getIntent().getStringExtra("show");
        audit = getIntent().getStringExtra("audit");
        setContentView(R.layout.activity_make_app_checkpending);
        initview();
        initUtils();
        initListener();

    }

    private void initListener() {
        // TODO Auto-generated method stub
        mBtnBack.setOnClickListener(this);
        mBtnRefuse.setOnClickListener(this);
        mBtnAgree.setOnClickListener(this);
    }

    private void initUtils() {
        // TODO Auto-generated method stub

    }

    private void initview() {

        webView = (WebView) findViewById(R.id.web_view);
        initWebView();
        mBtnBack =  findViewById(R.id.btn1);
        mTxtTitle = (TextView) findViewById(R.id.tv_title);
        mBtnRefuse = (Button) findViewById(R.id.btn_checkpending_left);
        mBtnAgree = (Button) findViewById(R.id.btn_checkpending_right);

        mTextViewOrderNum = (TextView) findViewById(R.id.textView_checkpending_item_order_num);
        mTextViewTime = (TextView) findViewById(R.id.textView_checkpending_item_order_time);
        mImageViewTop = (ImageView) findViewById(R.id.imagView_checkpending_item_top);
//		mTxtTitle.setText(R.string.make_app_txt_checkpending);
        mTextViewOrderNum.setText("预约单号：" + id);
        mTextViewTime.setText("预约门诊时间：" + time);
        changeButton(Integer.parseInt(curState));
    }

    private void changeButton(int index) {
        switch (index) {
            case 1:
                mTxtTitle.setText("等待确认");
                mImageViewTop.setImageResource(R.drawable.yuyue_wait_confirm);
                if (audit.equals("0")) {
                    mBtnRefuse.setVisibility(View.GONE);
                    mBtnAgree.setVisibility(View.GONE);
                } else {
                    mBtnRefuse.setVisibility(View.VISIBLE);
                    mBtnAgree.setVisibility(View.VISIBLE);
                    mBtnAgree.setText(getResources().getString(
                            R.string.make_app_txt_btn_agree));
                    mBtnRefuse.setText(getResources().getString(
                            R.string.make_app_txt_btn_refuse));
                }

                break;
            case 2:
                // 根据数据的状态改变
                if (show.equals("0")) {
                    mTxtTitle.setText("等待就诊");
                    mBtnRefuse.setVisibility(View.GONE);
                    mBtnAgree.setVisibility(View.GONE);
                    mImageViewTop.setImageResource(R.drawable.yuyue_shenghe);
                } else {
                    mTxtTitle.setText("已领取加号单");
                    mBtnRefuse.setVisibility(View.VISIBLE);
                    mBtnAgree.setVisibility(View.VISIBLE);
                    mBtnAgree.setText("患者爽约");
                    mBtnRefuse.setText("确认就诊");
                    mImageViewTop.setImageResource(R.drawable.yuyue_yilingqu);
                }
                break;
            case 3:
                mTxtTitle.setText("成功就诊");
                mImageViewTop.setImageResource(R.drawable.yuyue_success);
                // if (show.equals("1")) {
                mBtnRefuse.setVisibility(View.GONE);
                mBtnAgree.setVisibility(View.GONE);
                // }else{
                // mBtnRefuse.setVisibility(View.VISIBLE);
                // mBtnAgree.setVisibility(View.VISIBLE);
                // }

                break;
            case 4:
                mTxtTitle.setText("爽约");
                mImageViewTop.setImageResource(R.drawable.yuyue_shuangyue);
                // if (show.equals("1")) {
                mBtnRefuse.setVisibility(View.GONE);
                mBtnAgree.setVisibility(View.GONE);
                // }else{
                // mBtnRefuse.setVisibility(View.VISIBLE);
                // mBtnAgree.setVisibility(View.VISIBLE);
                // }
                break;

            default:
                break;
        }
    }

    //	 1:同意预约 2：不同意预约 3：成功就诊 4：爽约
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                this.finish();
                break;
            case R.id.btn_checkpending_left:
                if (curState.equals("1")) {
                    getReq("2");
                }
                if (curState.equals("2")) {
                    getReq("3");
                }
                break;
            case R.id.btn_checkpending_right:
                if (curState.equals("1")) {
                    getReq("1");
                }
                if (curState.equals("2")) {
                    getReq("4");
                }
                break;
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

    private void getReq(final String state) {
        AjaxParams params = new AjaxParams();
        params.put("userid", YMApplication.getLoginInfo().getData().getPid());
        params.put("state", state);
        params.put("order_id", id);
        params.put(
                "sign",
                MD5Util.MD5(YMApplication.getLoginInfo().getData().getPid()
                        + Constants.MD5_KEY));
        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.MAKE_ADD_NUM_SUB, params, new AjaxCallBack() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                super.onFailure(t, errorNo, strMsg);
            }

            @Override
            public void onLoading(long count, long current) {
                // TODO Auto-generated method stub
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(String t) {
                // TODO Auto-generated method stub
                super.onSuccess(t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("msg");
                    if (code == 0) {
                        SharedPreferences.Editor edit = getSharedPreferences(
                                "isskip", MODE_PRIVATE).edit();
                        edit.putBoolean("skip", true);
                        edit.putString("state", curState);
                        edit.commit();
                        finish();
                    }
                    ToastUtils.shortToast(msg);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        });
    }

    public void onResume() {
        super.onResume();
        StatisticalTools.onResume(this);
    }

    public void onPause() {

        StatisticalTools.onPause(this);
        super.onPause();
    }
}
