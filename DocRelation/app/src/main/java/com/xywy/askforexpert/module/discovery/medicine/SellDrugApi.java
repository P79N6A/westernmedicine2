package com.xywy.askforexpert.module.discovery.medicine;

import com.xywy.retrofit.net.BaseData;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by xgxg on 2017/10/26.
 */

public interface SellDrugApi {
    @GET("api.php/wkysym/wkys/sell_drug")
    Observable<BaseData> getSellDrug(@QueryMap Map<String, String> getParams);
}
