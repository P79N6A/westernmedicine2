package com.xywy.datarequestlibrary.tools;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit 实例
 *
 * @author bailiangjin 2017-02-16
 */
public class XywyRetrofit {

    private static volatile XywyRetrofit instance = null;

    private Retrofit retrofit;

    public static XywyRetrofit getInstance() {
        if (instance == null) {
            synchronized (XywyRetrofit.class) {
                if (instance == null) {
                    instance = new XywyRetrofit();
                }
            }
        }
        return instance;
    }


    XywyRetrofit() {
        retrofit = new Retrofit.Builder()
                .client(XywyOkHttpClient.INSTANCE.getOkHttpClient())
                .baseUrl("http://www.baidu.com")//假的baseurl 实际
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                //也可以添加自定义的RxJavaCallAdapterFactory
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

}

