package com.xywy.askforexpert.appcommon.net.retrofitWrapper;

import com.xywy.askforexpert.appcommon.net.utils.CommonNetUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/3/21 10:22
 */
public class XywyInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        CommonNetUtils.addHeader(builder);
        //修改请求为只从网络中读数据
        Request request = builder
                .cacheControl(CacheControl.FORCE_NETWORK).build();
        return chain.proceed(request);
    }
}
