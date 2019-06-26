package com.xywy.datarequestlibrary.downloadandupload.listener;

/**
 * Created by bailiangjin on 2017/5/8.
 */

public interface OkDownloadListener {

    void onProgress(long totalBytes, long currentBytes);

    void onSuccess(String absoluteFilePath);

    void onFailure(Exception e);
}
