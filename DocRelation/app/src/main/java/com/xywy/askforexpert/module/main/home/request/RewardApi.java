package com.xywy.askforexpert.module.main.home.request;

import com.xywy.askforexpert.model.home.RewardItemBean;
import com.xywy.retrofit.net.BaseData;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by xugan on 2018/4/12.
 */

public interface RewardApi {
    @GET("api.php/imask/rewardList/index")
    Observable<BaseData<RewardItemBean>> getReward(@QueryMap Map<String, String> getParams);
}
