package com.xywy.askforexpert.module.discovery.medicine.module.account.data;

import com.xywy.askforexpert.module.discovery.medicine.common.ApiConstants;
import com.xywy.askforexpert.module.discovery.medicine.module.account.beans.LoginServerBean;
import com.xywy.askforexpert.module.discovery.medicine.module.account.beans.UserInfoBean;
import com.xywy.retrofit.net.BaseData;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/3/30 10:20
 */

public interface UserApi {

    @FormUrlEncoded
    @POST(ApiConstants.loginMethod)
    Observable<BaseData<LoginServerBean>> login(@QueryMap Map<String, String> getParams, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiConstants.registerMethod)
    Observable<BaseData<LoginServerBean>> register(@QueryMap Map<String, String> getParams, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiConstants.getTxtMethod)
    Observable<BaseData> getTextMsgCode(@QueryMap Map<String, String> getParams, @FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST(ApiConstants.FIND_PASSWORD_METHOD)
    Observable<BaseData> findPassWord(@QueryMap Map<String, String> getParams, @FieldMap Map<String, String> map);

    @GET(ApiConstants.GET_USER_INFO_METHOD)
    Observable<BaseData<UserInfoBean>> getUserInfo(@QueryMap Map<String, String> getParams);

    @FormUrlEncoded
    @POST(ApiConstants.CODE_LOGIN)
    Observable<BaseData<LoginServerBean>> loginByRandomCode(@QueryMap Map<String, String> getParams, @FieldMap Map<String, String> map);
}
