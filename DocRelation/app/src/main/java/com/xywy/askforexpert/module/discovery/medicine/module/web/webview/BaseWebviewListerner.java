package com.xywy.askforexpert.module.discovery.medicine.module.web.webview;

public interface BaseWebviewListerner {



    void onStatusChanged(WebviewStatus status, String url);

    boolean onUrlChanged(String url);
}
