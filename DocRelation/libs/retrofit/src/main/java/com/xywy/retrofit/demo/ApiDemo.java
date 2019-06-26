package com.xywy.retrofit.demo;

import com.xywy.retrofit.RetrofitClient;
import com.xywy.retrofit.net.BaseData;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by bobby on 16/6/15.
 */
public interface ApiDemo {


    @FormUrlEncoded
    @POST("app/1.5/club/doctorApp.interface.php")
    Observable<BaseData<UserData>> rxCacheDemo(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("app/1.5/club/doctorApp.interface.php")
    @Headers(RetrofitClient.FORCE_NETWORK)
    Observable<BaseData<UserData>> noCacheDemo(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("app/1.5/club/doctorApp.interface.php")
    Observable<BaseData<UserData>> retrofitDemo(@FieldMap Map<String, String> map);
}
