package com.xywy.component.datarequest.network;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.xywy.component.datarequest.common.Constants;

import java.util.Collections;
import java.util.Map;

/**
 * MAXLEAP REQUEST
 */
public class MLRequest extends SimpleRequest {

    private Map<String, String> mParams = Collections.EMPTY_MAP;

    public MLRequest(String url, Response.Listener<SimpleResponse> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, listener, errorListener);
    }

    @Override
    public String getCacheKey() {
        String cacheKey = getUrl();
        try {
            cacheKey += new String(getBody());
        } catch (Exception e) {
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

    @Override
    public byte[] getBody() {
        String str = "";
        if(mParams.containsKey(Constants.MAXLEAP_REQUEST_KEY)) {
            str = mParams.get(Constants.MAXLEAP_REQUEST_KEY);
        }
        return str.getBytes();
    }

    @Override
    public String getBodyContentType() {
        return "application/json";
    }
}
