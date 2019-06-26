package com.xywy.askforexpert.appcommon.utils;

import android.webkit.WebView;

/**
 * Created by bailiangjin on 16/9/19.
 */
public class WebViewUtils2 {

    public static void safeEnhance(WebView webview) {
        webview.removeJavascriptInterface("accessibility");
        webview.removeJavascriptInterface("accessibilityTraversal");
        webview.removeJavascriptInterface("searchBoxJavaBridge_");
    }
}
