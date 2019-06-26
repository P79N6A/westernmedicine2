package com.xywy.datarequestlibrary.downloadandupload.listener;

/**
 * blj
 */
public interface ProgressListener {
    void onProgress(long currentBytes, long contentLength, boolean done);
}