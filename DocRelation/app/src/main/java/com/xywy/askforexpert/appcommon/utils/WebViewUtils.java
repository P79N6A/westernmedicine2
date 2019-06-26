package com.xywy.askforexpert.appcommon.utils;

import android.app.Activity;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xywy.askforexpert.appcommon.interfaces.listener.MyWebViewDownLoadListener;

/**
 * Created by bailiangjin on 15/8/19.
 */
public class WebViewUtils {

    /**
     * 根据url加载Web界面
     * @param webView
     * @param url
     */
   public static void load(final WebView webView, String url ){

       webView.getSettings().setDomStorageEnabled(true);
       // 设置支持javascript
       webView.getSettings().setJavaScriptEnabled(true);
       webView.getSettings().setAllowFileAccess(true);
       webView.getSettings().setSupportZoom(true);
       //启动缓存
       webView.getSettings().setAppCacheEnabled(false);
       // 设置缓存模式
       webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
       //使用自定义的WebViewClient

       // Use WideViewport and Zoom out if there is no viewport defined
       webView.getSettings().setUseWideViewPort(true);
       webView.getSettings().setLoadWithOverviewMode(true);
       // Enable pinch to zoom without the zoom buttons
       webView.getSettings().setBuiltInZoomControls(true);
       if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
           // Hide the zoom controls for HONEYCOMB+
           webView.getSettings().setDisplayZoomControls(false);
       }
       // Enable remote debugging via chrome://inspect
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
           WebView.setWebContentsDebuggingEnabled(true);
       }

       webView.setWebViewClient(new WebViewClient() {
           //覆盖shouldOverrideUrlLoading 方法
           @Override
           public boolean shouldOverrideUrlLoading(WebView view, String url) {
               if (url.startsWith("yimaiapp://goback")){
                Activity activity=(Activity) webView.getContext();
                  activity.finish();
                   return true;
               }
               view.loadUrl(url);
               return true;
           }

           @Override
           public void onPageFinished(WebView view, String url) {
               super.onPageFinished(view, url);
               //打印加载完成时的Cookie
               CookieManager cookieManager = CookieManager.getInstance();
               String CookieStr = cookieManager.getCookie(url);
               LogUtils.d( "Cookies = " + CookieStr);
           }
       });

       webView.setDownloadListener(new MyWebViewDownLoadListener());

       webView.setWebChromeClient(new WebChromeClient(){

       });

       CookieManager cookieManager = CookieManager.getInstance();
       cookieManager.setAcceptCookie(true);
       cookieManager.setCookie(url, "Yimai-Request=" + AppUtils.getAPPInfo());

       LogUtils.d("Yimai-Request="+AppUtils.getAPPInfo());
       LogUtils.d("contentUrl = " + url);

       webView.loadUrl(url);
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
           webView.onResume();
       }
   }
}
