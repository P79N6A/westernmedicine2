package com.xywy.component.datarequest.network;

import android.graphics.Bitmap;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Map;

/**
 * 简单网络请求，是所有网络请求的基类
 * 传递url，返回String
 */
public class SimpleRequest extends Request<SimpleResponse> {

    private static final int SIMPLE_TIMEOUT_TIMER = 10000;
    protected Response.Listener<SimpleResponse> mListener;
    private boolean mCache = false;
    protected Map mHttpHeader = Collections.emptyMap();
    private Response mResponse;

    public SimpleRequest(int method, String url, Response.Listener<SimpleResponse> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;

        setRetryPolicy(new DefaultRetryPolicy(
                SIMPLE_TIMEOUT_TIMER,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public static Cache.Entry makeCacheHeaders(NetworkResponse response) {
        Cache.Entry entry = new Cache.Entry();
        entry.data = response.data;
        entry.ttl = Long.MAX_VALUE;
        entry.responseHeaders = response.headers;

        return entry;
    }

    /**
     * 设置当前request是否缓存
     *
     * @param cache 1、每次网络请求结果都缓存
     *              2、发送request前，检测本次网络请求是否需要缓存，如果不需要，先清缓存数据
     *              3、onresponse的时候，对比mCache和mResponse.intermediate的值来判断是否回调
     */
    public void setCacheEntity(boolean cache) {
        mCache = cache;
        if (!cache) {
            RequestManager.clearCache(this);
        }

    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        SimpleResponse parsed;
        try {
            parsed = new SimpleResponse(new String(response.data, HttpHeaderParser.parseCharset(response.headers,"UTF-8")));
        } catch (UnsupportedEncodingException e) {
            parsed = new SimpleResponse(new String(response.data));
        }
        Cache.Entry entry;

//        if(mCache) {
//            entry = makeCacheHeaders(response);
//        } else {
//            entry = HttpHeaderParser.parseCacheHeaders(response);
//        }
        entry = makeCacheHeaders(response);
        mResponse = Response.success(parsed, entry);
        return mResponse;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHttpHeader;
    }

    @Override
    protected void deliverResponse(SimpleResponse response) {
        if (mListener != null) {
            //网络请求不需要缓存，不返回中间数据
            if (mResponse != null && !mCache && mResponse.intermediate) {
                return;
            }
            mListener.onResponse(response);
        }
    }

    /**
     * 给网络请求配置参数 用于post包体
     *
     * @param params 在post包体中的数据
     */
    public void setParams(Map<String, String> params) {

    }

    /**
     * 给网络请求配置文件
     */
    public void setFile(String name, File file) {

    }

    /**
     * 给网络请求配置图片
     */
    public void setBitmap(String name, Bitmap bitmap) {

    }

    /**
     * 给网络请求配置二进制流数据,gzip可以压缩该数据
     */
    public void setBytes(byte[] data) {

    }

    public Map getmHttpHeader() {
        return mHttpHeader;
    }

    public void setmHttpHeader(Map mHttpHeader) {
        this.mHttpHeader = mHttpHeader == null ? Collections.emptyMap() : mHttpHeader;
    }
}
