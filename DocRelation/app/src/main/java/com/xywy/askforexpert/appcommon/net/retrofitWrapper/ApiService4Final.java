package com.xywy.askforexpert.appcommon.net.retrofitWrapper;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by bobby on 16/6/15.
 */
public interface ApiService4Final {
    @GET()
    @Headers(ApiConstants.FORCE_NETWORK)
     Observable<ResponseBody> get(
            @Url String url,
            @QueryMap Map<String, String> maps);

    @FormUrlEncoded
    @Headers(ApiConstants.FORCE_NETWORK)
    @POST()
    Observable<ResponseBody> post(
            @Url String url,
            @FieldMap Map<String, String> maps);

    @FormUrlEncoded
    @Headers(ApiConstants.FORCE_NETWORK)
    @POST()
    Observable<ResponseBody> postLive(
            @Url String url,
            @QueryMap Map<String, String> getParams,
            @FieldMap Map<String, String> postparams);

}
