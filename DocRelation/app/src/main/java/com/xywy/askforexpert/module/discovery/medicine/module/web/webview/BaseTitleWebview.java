package com.xywy.askforexpert.module.discovery.medicine.module.web.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.module.discovery.medicine.module.web.WebActivity;
import com.xywy.base.view.CircleProgressBar;
import com.xywy.util.ContextUtil;
import com.xywy.util.DensityUtil;
import com.xywy.util.NetUtils;


/**
 * 封装的带标题的webview控件
 */
public class BaseTitleWebview extends LinearLayout implements BaseWebviewListerner {

    private BaseWebview webview;

    private View baseWebFrame;

    private CircleProgressBar loading_progressbar;

    private TextView titleName;

    private ImageView backBtn;

    private CommonLoadingView loadingView;

    private View title;

    private View title_1;

    private boolean isShowTitle = true;

    private boolean isShowTitleWithAnim = false;

    private AttributeSet mAttributeSet;

    private int mDefStyle;

    private Context mContext;

    private Activity mActivity;

    WebviewType webviewType = WebviewType.WebviewTypeMutiple;

    //初始url
    private String currentUrl;

    public BaseTitleWebview(Context context) {
        super(context);
        mAttributeSet = null;
        mDefStyle = 0;
        mContext = context;
    }

    public BaseTitleWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
        mAttributeSet = attrs;
        mDefStyle = 0;
        mContext = context;
    }

    public void setXYWebviewScrollCallback(XYWebview.XYWebviewScrollCallback callback) {
        webview.setXYWebviewScrollCallback(callback);
    }

    public void setWebViewClient(WebViewClient client) {
        webview.setWebViewClient(client);
    }

    public void setXyWebClient(){
        XYWebviewClient client=new XYWebviewClient(mActivity);
        client.setListerner(this);
        webview.setWebViewClient(client);
    }

    public void setWebChromeClient(WebChromeClient client) {
        webview.setWebChromeClient(client);
    }

    public void init() {
        webview.init();
        initWebview();
    }

    public void init(boolean isShowTitle, boolean isShowTitleWithAnim) {
        this.isShowTitle = isShowTitle;
        this.isShowTitleWithAnim = isShowTitleWithAnim;
        initWebview();
    }


    public void setViewType(WebviewType type) {
        webviewType = type;
        switch (type) {
            case WebviewTypeSingle:
                backBtn.setVisibility(GONE);
                break;
            case WebviewTypeMutiple:
                backBtn.setVisibility(VISIBLE);
                break;
            default:
                break;
        }
    }

    //清空webview的方法
    public void clear() {
        if (webview != null) {
            webview.clear();
        }
    }

    public void loadUrl(String url) {
        if (webview != null) {
            currentUrl = url;
            webview.loadUrl(url);
            if (titleName != null) {
                titleName.setText(TitleHelper.getTitle(currentUrl));
            }
        }
    }

    public void onActivityResult(int resultCode, Intent data, Activity act) {
        if (webview != null) {
            webview.onActivityResult(resultCode, data, act);
        }
    }

    /**
     * 是否返回
     *
     * @return
     */
    public boolean goBack() {
        if (webview.canGoBack()) {
            webview.goBack();
            return false;
        } else {
            Activity act = ContextUtil.currentActivity();
            if (act != null && !act.isFinishing()) {
                act.finish();
            }
            return true;
        }
    }

    public void ShowOrHideBackBtn(boolean isShow) {
        if (backBtn != null) {
            if (isShow) {
                backBtn.setVisibility(VISIBLE);
            } else {
                backBtn.setVisibility(GONE);
            }
        }
    }

    private void initWebview() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.view_base_title_webview, this, true);

        webview = (BaseWebview) findViewById(R.id.base_webview);
        webview.setActivity(mActivity);
        baseWebFrame = (View) findViewById(R.id.base_web_frame);
        initTitle();
        setViewType(WebviewType.WebviewTypeMutiple);
        loading_progressbar = (CircleProgressBar) findViewById(R.id.loading_progressbar);
        webview.setListerner(this);
        loadingView = (CommonLoadingView) findViewById(R.id.loading_view);

        loading_progressbar.setVisibility(GONE);

        setXYWebviewScrollCallback(new XYWebview.XYWebviewScrollCallback() {
            @Override
            public void onScroll(int l, int t, int oldl, int oldt, int scrollY) {
                ////TODO
                if (isShowTitleWithAnim == true) {
                    if (DensityUtil.px2dip(mContext, scrollY) >= 80) {
                        title.setVisibility(View.VISIBLE);
                    } else {
                        title.setVisibility(View.GONE);
                    }
                }
            }
        });

    }

    private void initTitle() {
        if (isShowTitle == false) {
            backBtn = (ImageView) findViewById(R.id.ibn_back_1);
            title = (View) findViewById(R.id.commonTitle_1);
            titleName = (TextView) findViewById(R.id.title_name_1);
            title.setVisibility(View.GONE);
        } else if (isShowTitle == true && isShowTitleWithAnim == false) {
            backBtn = (ImageView) findViewById(R.id.ibn_back_1);
            title = (View) findViewById(R.id.commonTitle_1);
            titleName = (TextView) findViewById(R.id.title_name_1);
        } else if (isShowTitleWithAnim == true) {
            backBtn = (ImageView) findViewById(R.id.ibn_back);
            title = (View) findViewById(R.id.commonTitle);
            title_1 = (View) findViewById(R.id.commonTitle_1);
            titleName = (TextView) findViewById(R.id.title_name);
            title_1.setVisibility(View.GONE);
            title.setVisibility(View.GONE);
        }

        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }

    public void loadData() {
        if (!NetUtils.isConnected(getContext())) {
            Toast.makeText(getContext(), "没有网络", Toast.LENGTH_SHORT);
        }
        loadUrl(currentUrl);
    }


    @Override
    public void onStatusChanged(WebviewStatus status, String url) {
        if (url != null && !url.isEmpty()) {
            currentUrl = url;
            if (titleName != null) {
                titleName.setText(webview.getWebTitle());
            }
        }
        switch (status) {
            case WebviewDonloading:
                loadingView.setLoadingState(CommonLoadingView.LoadingState.LoadingStateLoading);
                break;
            case WebviewLoadFailed:
                loadingView.setLoadingState(CommonLoadingView.LoadingState.LoadingStateFailed);
                break;
            case WebviewLoadSucessed:
                loadingView.setLoadingState(CommonLoadingView.LoadingState.LoadingStateSuccess);
                if(mActivity instanceof WebActivity){
                    ((WebActivity)mActivity).loadWebViewDataSuccess();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onUrlChanged(String url) {
        boolean consumed = false;
        {
            switch (webviewType) {
                case WebviewTypeSingle:
                    currentUrl = url;
                    consumed = true;
                    Activity act = ContextUtil.currentActivity();
                    if (act != null && !act.isFinishing()) {
                        WebActivity.startActivity(act, url);
                    }
                    break;
                case WebviewTypeMutiple:
                    currentUrl = url;
                    if (titleName != null) {
                        titleName.setText(webview.getWebTitle());
                    }
                    break;
            }
        }

        return consumed;
    }

    public void hideTitle() {
        title.setVisibility(View.GONE);
    }

    public void showTitle() {
        title.setVisibility(View.VISIBLE);
    }

    public void updateTitle(String title) {
        titleName.setText(title);
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

}
