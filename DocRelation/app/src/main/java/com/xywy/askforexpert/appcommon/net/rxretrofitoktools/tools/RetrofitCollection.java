package com.xywy.askforexpert.appcommon.net.rxretrofitoktools.tools;


import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.datarequestlibrary.tools.XywyOkHttpClient;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit 集合 可以通过添加枚举元素的方式 方便地添加 不同 root url的 retrofit
 * @author bailiangjin 2017-02-16
 */
public enum RetrofitCollection {
    /**
     * 中间层 BaseUrl
     */
    WWS_XYWY_INSTANCE(CommonUrl.WWS_XYWY_BASE_URL);

    private Retrofit retrofit;

    RetrofitCollection(String baseUrl) {
        retrofit = new Retrofit.Builder()
                .client(XywyOkHttpClient.INSTANCE.getOkHttpClient())
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                //也可以添加自定义的RxJavaCallAdapterFactory
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}

