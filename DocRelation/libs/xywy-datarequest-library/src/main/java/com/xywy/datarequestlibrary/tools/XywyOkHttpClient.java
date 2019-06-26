package com.xywy.datarequestlibrary.tools;

import com.xywy.datarequestlibrary.XywyDataRequestApi;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 最底层的 OKHttpClient
 * Created by bailiangjin on 2017/2/16.
 */

public enum XywyOkHttpClient {

    INSTANCE;

    private OkHttpClient okHttpClient;

    XywyOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        //TODO stone 测试与线上要修改
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        logging.setLevel(HttpLoggingInterceptor.Level.NONE);

        okHttpClient = new OkHttpClient.Builder()
                //.cache(cache)  //禁用okhttp自身的的缓存
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new MyInterceptor())
                .addInterceptor(logging)
                .build();
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    static class MyInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            //添加header

            if (null != XywyDataRequestApi.getInstance().getHelper()) {
                XywyDataRequestApi.getInstance().getHelper().addHeader(builder);
            }

            //修改请求为只从网络中读数据
            Request request = builder
                    .cacheControl(CacheControl.FORCE_NETWORK).build();
            return chain.proceed(request);
        }
    }
}
