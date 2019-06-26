package com.xywy.askforexpert.module.my.request;

import com.xywy.askforexpert.model.my.Income;
import com.xywy.retrofit.net.BaseData;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by xugan on 2018/6/21.
 * "我的"模块一级页面的所有相关的接口
 */

public interface UserCenterFragmentApi {
    @GET("/api.php/yimai/yimaiDoctor/bill")
    Observable<BaseData<Income>> getNewIncome(@QueryMap Map<String, String> getParams);

}
