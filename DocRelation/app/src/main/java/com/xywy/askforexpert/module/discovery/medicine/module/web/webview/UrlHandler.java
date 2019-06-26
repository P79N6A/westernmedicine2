package com.xywy.askforexpert.module.discovery.medicine.module.web.webview;

import android.net.Uri;

/**
 * Created by bobby on 16/6/20.
 */
public abstract class UrlHandler {
    public static String HYBRID_SCHEME = "mobilehospital";
    public static final String HTTP = "http";
    public static final String HTTPS = "https";
    public static final String TEL = "tel";
    public static final String SMSTO = "smsto";
    public static final String MAILTO = "mailto";

    public static void setScheme(String scheme) {
        HYBRID_SCHEME = scheme;
    }

    /***
     * 返回Host
     *
     * @return
     */
    abstract public String getHandledUrlHost();

    /***
     * 处理URI
     *
     * @param uri
     * @return 已处理返回true, 否则返回false
     */
    abstract public boolean handleUrl(Uri uri);
}
