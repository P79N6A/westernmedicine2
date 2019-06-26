package com.xywy.askforexpert.module.main.service.downFile;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import okhttp3.Call;


/**
 * DownloadFile
 *
 * @author 王鹏
 * @2015-6-5下午4:31:22
 */
public class DownloadFile {

    //	private static final long serialVersionUID = 1L;
    private boolean isStop;
    @SuppressWarnings("rawtypes")
    private Call mCall;

    /**
     * @param url          下载地址
     * @param toPath       存储的路
     * @param downCallBack 下载过程的回下载进度,与成功之后的回调
     * @return void
     */

    public DownloadFile startDownloadFileByUrl(String url, String toPath,
                                               AjaxCallBack downCallBack) {

        if (downCallBack == null) {
            throw new RuntimeException("AjaxCallBack对象不能为null");
        } else {
            FinalHttp down = new FinalHttp();
            // 支持断点续传
            mCall = down.download(url, toPath, true, downCallBack);
            // mHttpHandler.
        }
        return this;
    }

    public void stopDownload() {
        if (mCall != null) {
            mCall.cancel();
            if (!mCall.isCanceled()) {
                mCall.cancel();
            }
        }
    }

    public boolean isStop() {
        isStop = mCall.isCanceled();
        return isStop;
    }

    public void setStop(boolean isStop) {
        this.isStop = isStop;
    }
}
