package com.xywy.askforexpert.module.discovery.medicine.module.web;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.activity.WebViewActivity;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.util.CommonUtil;
import com.xywy.askforexpert.module.discovery.medicine.module.web.webview.BaseTitleWebview;
import com.xywy.askforexpert.module.discovery.medicine.module.web.webview.BaseWebview;
import com.xywy.askforexpert.module.discovery.medicine.module.web.webview.CommonLoadingView;
import com.xywy.askforexpert.module.discovery.medicine.module.web.webview.WebviewType;
import com.xywy.askforexpert.module.doctorcircle.DiscussDetailActivity;
import com.xywy.askforexpert.module.message.share.ShareUtil;
import com.xywy.base.XywyBaseActivity;


public class WebActivity extends XywyBaseActivity {

    private static final String INTENT_KEY_ACTIVITY_URL = "TARGET_URL";

    private static final String INTENT_KEY_PRODUCT_ID = "TARGET_PRODUCT_ID";

    private static final String INTENT_KEY_ACTIVITY_TITLE = "TARGET_TITLE";
    private static final String INTENT_KEY_ACTIVITY_SHARE_TITLE = "SHARE_TITLE";
    private static final String INTENT_KEY_ACTIVITY_SHARE_CONTENT = "SHARE_CONTENT";

    private BaseTitleWebview mWebview;

    private String mBaseUrl;

    private String mTitle;

    private ImageButton ibBack;

    private CommonLoadingView clvLoading;
    private String mShareTitle;
    private String mShareContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        //初始化统一bar，将系统栏的背景设置成透明色
        CommonUtils.initSystemBar(this);
        checkIntent();
        initView();
        initListener();


//        //随便给一个requestCode就行，因为当前activity不关心回调函数
//        PermissionManager.getIns()
//                .requestPermissions(WebActivity.this
//                        , permission.CAMERA
//                        , 0,null);
//        PermissionManager.getIns()
//                .requestPermissions(WebActivity.this
//                        , permission.READ_EXTERNAL_STORAGE
//                        , 0,null);
//        PermissionManager.getIns()
//                .requestPermissions(WebActivity.this
//                        , permission.WRITE_EXTERNAL_STORAGE
//                        , 0,null);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            mWebview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void checkIntent() {
        Intent intent = getIntent();
        mBaseUrl = intent.getStringExtra(INTENT_KEY_ACTIVITY_URL);
        mTitle = intent.getStringExtra(INTENT_KEY_ACTIVITY_TITLE);
        mShareTitle = intent.getStringExtra(INTENT_KEY_ACTIVITY_SHARE_TITLE);
        mShareContent = intent.getStringExtra(INTENT_KEY_ACTIVITY_SHARE_CONTENT);

    }

    public static void startActivity(Context context, String url) {
        Intent i = new Intent();
        i.setClass(context, WebActivity.class);
        i.putExtra(INTENT_KEY_ACTIVITY_URL, url);
        context.startActivity(i);
    }

//    public static void startActivity(Context context, String baseUrl, String id) {
//        String url = baseUrl + "?doctorid=" + RSAUtils.encodeUserid(id);
//        Intent i = new Intent();
//        i.setClass(context, WebActivity.class);
//        i.putExtra(INTENT_KEY_ACTIVITY_URL, url);
//        i.putExtra(INTENT_KEY_PRODUCT_ID, id);
//        context.startActivity(i);
//    }

//    public static void startActivity(Context context, String baseUrl, String recipeId,String doctor_id,String title) {
//        String url = baseUrl + "cfdetail-"+recipeId+".htm?doctorid=" + RSAUtils.encodeUserid(doctor_id);
//        LogUtils.i("url="+url);
//        Intent i = new Intent();
//        i.setClass(context, WebActivity.class);
//        i.putExtra(INTENT_KEY_ACTIVITY_URL, url);
//        i.putExtra(INTENT_KEY_PRODUCT_ID, doctor_id);
//        i.putExtra(INTENT_KEY_ACTIVITY_TITLE, title);
//        context.startActivity(i);
//    }

    public static void startActivity(Context context, String url,String title) {
        Intent i = new Intent();
        i.setClass(context, WebActivity.class);
        i.putExtra(INTENT_KEY_ACTIVITY_URL, url);
        i.putExtra(INTENT_KEY_ACTIVITY_TITLE, title);
        context.startActivity(i);
    }

    public static void startActivity(Context context, String url,String title,String shareTitle,String shareContent) {
        Intent i = new Intent();
        i.setClass(context, WebActivity.class);
        i.putExtra(INTENT_KEY_ACTIVITY_URL, url);
        i.putExtra(INTENT_KEY_ACTIVITY_TITLE, title);
        i.putExtra(INTENT_KEY_ACTIVITY_SHARE_TITLE, shareTitle);
        i.putExtra(INTENT_KEY_ACTIVITY_SHARE_CONTENT, shareContent);
        context.startActivity(i);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BaseWebview.FILECHOOSER_REQUESTCODE) {
            mWebview.onActivityResult(resultCode, data, this);

        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void initView() {


        mWebview = (BaseTitleWebview) findViewById(R.id.webview);
        mWebview.init(true, false);
        mWebview.setActivity(this);
        mWebview.setViewType(WebviewType.WebviewTypeMutiple);
//        XYWebviewClient c = new XYWebviewClient(this);
//        mWebview.setWebViewClient(c);
        mWebview.setXyWebClient();
        mWebview.loadUrl(mBaseUrl);

        ibBack = (ImageButton) findViewById(R.id.ibn_back_1);

        clvLoading = (CommonLoadingView) findViewById(R.id.clv_loading);
        View view = clvLoading.findViewById(R.id.fl_loading_parent);
        if (view != null) {
            view.setBackgroundColor(0x0);
        }
        mWebview.init();
        hideTitle();
        initTitle();
    }


    protected void initListener() {
        //返回后退按钮点击事件
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivity.this.finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void hideTitle() {
        mWebview.hideTitle();
    }

    private void showTitle() {
        mWebview.showTitle();
    }

    private void updateTitle(String title) {
        showTitle();
        mWebview.updateTitle(title);
    }

    private void initTitle() {
        titleBarBuilder.setTitleText((mTitle==null)?"":mTitle);
        titleBarBuilder.setBackGround(R.drawable.toolbar_bg_no_alpha_new);
        titleBarBuilder.setBackIcon("", R.drawable.fanhui, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (TextUtils.isEmpty(mShareTitle)){
            titleBarBuilder.setRight("", R.drawable.icon_refresh, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mWebview.loadUrl(mBaseUrl);
                }
            });
        }else{
            titleBarBuilder.setRight("", R.drawable.share_com, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new ShareUtil.Builder()
                            .setTitle(mShareTitle)
                            .setText(mShareContent)
                            .setTargetUrl(mBaseUrl)
                            .setImageUrl(ShareUtil.DEFAULT_SHARE_IMG_ULR)
                            .build(WebActivity.this).outerShare();
                }
            });
        }
        titleBarBuilder.setBackIconClickEvent(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.actionKey(KeyEvent.KEYCODE_BACK);
            }
        });
    }

    public void loadWebViewDataSuccess(){
        YmRxBus.notifyWebViewLoadingStateSuccess(mBaseUrl);
    }

}
















































































