package com.xywy.component.datarequest.network;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;

import java.util.Collections;
import java.util.Map;

/**
 * 简单的post网络请求
 */
public class PostRequest extends SimpleRequest {

    private Map<String, String> mParams = Collections.EMPTY_MAP;

    public PostRequest(String url, Response.Listener<SimpleResponse> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, listener, errorListener);
    }

    @Override
    public String getCacheKey() {
        String cacheKey = getUrl();
        try {
            cacheKey += new String(getBody());
        } catch (AuthFailureError e) {
            e.printStackTrace();
        }
        return cacheKey;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    /**
     * 给网络请求配置参数 用于post包体
     *
     * @param params 在post包体中的数据
     */
    public void setParams(Map<String, String> params) {
        if (params != null && params.size() > 0) {
            mParams = params;
        }
    }
}
