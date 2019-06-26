package com.xywy.askforexpert.appcommon.interfaces.listener;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.DownloadListener;

import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;

/**
 * webView 下载事件监听
 * Created by bailiangjin on 2016/12/9.
 */
public class MyWebViewDownLoadListener implements DownloadListener {
    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

        LogUtils.d("download file url:" + url);
        if (TextUtils.isEmpty(url) || !url.contains("appdl.xywy.com")) {
            LogUtils.e("无效的下载链接:" + url);
            return;
        }
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppUtils.getAppContext().startActivity(intent);
    }

//

}
