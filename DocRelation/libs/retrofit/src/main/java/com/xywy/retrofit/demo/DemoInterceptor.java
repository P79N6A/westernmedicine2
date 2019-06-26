package com.xywy.retrofit.demo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/3/21 10:22
 */
public class DemoInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        final Map<String, String> headerMap = getHeaderMapWhenUse();
        for (String headerKey : headerMap.keySet()) {
            builder.header(headerKey, headerMap.get(headerKey));
        }
        //修改请求为只从网络中读数据
        Request request = builder
                .cacheControl(CacheControl.FORCE_NETWORK).build();
        return chain.proceed(request);
    }
    private static Map<String, String> headerMap = new HashMap<>();
    private static Map<String, String> getHeaderMapWhenUse() {
        headerMap.put("Accept-Encoding", "gzip");
        headerMap.put("Accept-Encoding", "identity");
        return headerMap;
    }
}
