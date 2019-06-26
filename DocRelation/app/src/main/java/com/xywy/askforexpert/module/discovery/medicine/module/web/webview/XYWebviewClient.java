package com.xywy.askforexpert.module.discovery.medicine.module.web.webview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bobby on 16/6/20.
 */
public class XYWebviewClient extends WebViewClient {
    private Activity mActivityRef;
    private Map<String, UrlHandler> mHandlerMap;

    private BaseWebviewListerner listerner;

    public XYWebviewClient(Activity activity) {
        super();
        this.mActivityRef = activity;
        mHandlerMap = new HashMap<>();
    }

    public Map<String, UrlHandler> addHyBridUrlHandler(UrlHandler handler) {
        mHandlerMap.put(handler.getHandledUrlHost(), handler);
        return mHandlerMap;
    }

    public static void setUrlScheme(String scheme) {
        UrlHandler.setScheme(scheme);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if(listerner!=null){
            listerner.onStatusChanged(WebviewStatus.WebviewDonloading,url);
        }
    }

    //// TODO: 16/6/20 异常状态监听
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (mActivityRef != null  && !mActivityRef.isFinishing()) {
            mActivityRef.setTitle(view.getTitle());
        }
        if(listerner!=null){
            listerner.onStatusChanged(WebviewStatus.WebviewLoadSucessed,url);
        }
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    @Override
    public boolean shouldOverrideUrlLoading(final WebView view, String url) {
        Uri uri = Uri.parse(url);
        //Uri uri = Uri.parse("mobilehospital://payresult?type=2&order=123&doctorid=60&doctorname=高大全");
        String scheme = uri.getScheme();


        if (UrlHandler.HYBRID_SCHEME.equalsIgnoreCase(scheme)) {

            return true;
        } else if (UrlHandler.HTTP.equalsIgnoreCase(scheme) || UrlHandler.HTTPS.equalsIgnoreCase(scheme)) {

            view.loadUrl(url);
            return true;
        }
        return super.shouldOverrideUrlLoading(view, url);
    }

    /**
     * 在OverrideUrl中处理各种自定义scheme的URLhandler
     */
    public UrlHandler getUrlHandler(String host) {
        return mHandlerMap.get(host);
    }

    public BaseWebviewListerner getListerner() {
        return listerner;
    }

    public void setListerner(BaseWebviewListerner listerner) {
        this.listerner = listerner;
    }
}
