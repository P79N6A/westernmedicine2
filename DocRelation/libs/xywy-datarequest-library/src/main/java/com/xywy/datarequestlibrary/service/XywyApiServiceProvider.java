package com.xywy.datarequestlibrary.service;

import com.xywy.datarequestlibrary.tools.XywyRetrofit;

import retrofit2.Retrofit;

/**
 * ApiService 集合
 * Created by bailiangjin on 2017/2/16.
 */
public class XywyApiServiceProvider {

    public static  <T extends Object> T getApiService(Class<T> service) {
        Retrofit retrofit = XywyRetrofit.getInstance().getRetrofit();
        T apiService = retrofit.create(service);
        return apiService;
    }

}
