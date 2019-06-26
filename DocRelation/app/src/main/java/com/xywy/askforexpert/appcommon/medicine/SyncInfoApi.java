package com.xywy.askforexpert.appcommon.medicine;

import com.xywy.retrofit.net.BaseData;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by xgxg on 2017/10/26.
 */

public interface SyncInfoApi {
    @GET("api.php/wkysym/wkys/sync_info")
    Observable<BaseData> syncInfo(@QueryMap Map<String, String> getParams);
}
