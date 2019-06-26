package com.xywy.askforexpert.appcommon.net.rxretrofitoktools.collection;

import com.xywy.askforexpert.appcommon.net.retrofitWrapper.ApiService;
import com.xywy.askforexpert.appcommon.utils.LogUtils;

/**
 * Created by bailiangjin on 2017/3/1.
 */

public class CommonServiceProvider {


    public static ApiService getWWSXYWYApiService() {
        LogUtils.d("requestBaseUrl:" + ApiServiceCollection.getBaseUrl());
        return ApiServiceCollection.getApiService(ApiService.class);
    }

}