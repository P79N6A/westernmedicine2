package com.xywy.askforexpert.appcommon.interfaces.callback;

/**
 * Created by bailiangjin on 16/5/9.
 */
public interface HttpCallback {
    public void onSuccess(final Object obj);

    public void onFailed(final Throwable throwable, final int errorNo, final String strMsg);
}
