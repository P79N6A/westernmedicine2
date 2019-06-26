package com.xywy.datarequestlibrary.service;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by bailiangjin on 2017/4/24.
 */

public interface XywyApiService {

    /**
     * 通用请求方法
     *
     * @param url         请求url getparam 请自行组拼到url中
     * @param postPramMap postParam Map
     * @return
     */
    @FormUrlEncoded
    @POST()
    Observable<ResponseBody> postRequest(@Url String url, @FieldMap Map<String, String> postPramMap);

    @GET
    Observable<ResponseBody> getRequest(@Url String url);


}
