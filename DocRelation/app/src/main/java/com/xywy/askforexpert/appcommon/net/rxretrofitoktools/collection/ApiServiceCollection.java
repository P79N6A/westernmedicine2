package com.xywy.askforexpert.appcommon.net.rxretrofitoktools.collection;

import com.xywy.askforexpert.appcommon.net.rxretrofitoktools.tools.RetrofitCollection;

import retrofit2.Retrofit;

/**
 * ApiService 集合
 * Created by bailiangjin on 2017/2/16.
 */
public class ApiServiceCollection {


    public static  <T extends Object> T getApiService(Class<T> service) {
        Retrofit retrofit = RetrofitCollection.WWS_XYWY_INSTANCE.getRetrofit();
        T apiService = retrofit.create(service);
        return apiService;
    }

    public static String getBaseUrl() {
        return  RetrofitCollection.WWS_XYWY_INSTANCE.getRetrofit().baseUrl().toString();
    }
}
