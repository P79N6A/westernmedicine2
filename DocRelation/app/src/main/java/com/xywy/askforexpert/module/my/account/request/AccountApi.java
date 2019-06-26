package com.xywy.askforexpert.module.my.account.request;


import com.xywy.askforexpert.model.LoginInfo_New;
import com.xywy.askforexpert.model.RegisterInfo;
import com.xywy.retrofit.net.BaseData;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by xugan on 2018/2/2.
 * 登录注册相关的接口
 */

public interface AccountApi {
    @FormUrlEncoded
    @POST("api.php/xiaochengxu/user/userSmsCode")
    Observable<BaseData> getTextMsgCode(@QueryMap Map<String, String>  getParams, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api.php/doctor/doctorCommon/register")
    Observable<BaseData<RegisterInfo>> register(@QueryMap Map<String, String>  getParams, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api.php/doctor/doctorCommon/login")
    Observable<BaseData<LoginInfo_New>> login(@QueryMap Map<String, String>  getParams, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api.php/doctor/doctorCommon/editPassword")
    Observable<BaseData> updatePwd(@QueryMap Map<String, String>  getParams, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api.php/doctor/doctorCommon/forgetPassword")
    Observable<BaseData> resetPwd(@QueryMap Map<String, String>  getParams, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api.php/doctor/doctorCommon/bindPhone")
    Observable<BaseData> bindPhone(@QueryMap Map<String, String>  getParams, @FieldMap Map<String, String> map);
}
