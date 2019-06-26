package com.xywy.askforexpert.module.discovery.medicine;

import com.xywy.askforexpert.appcommon.net.retrofitWrapper.ApiConstants;
import com.xywy.askforexpert.model.IdNameBean;
import com.xywy.askforexpert.model.LoginInfo_New;
import com.xywy.askforexpert.model.certification.MessageBoardBean;
import com.xywy.retrofit.RetrofitClient;
import com.xywy.retrofit.net.BaseData;

import java.util.List;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;


/**
 * 认证相关/留言板 api
 * stone
 * 2018/2/5 上午11:38
 */
public interface CertificationAboutApi {

    //通过关键字搜索获取医院列表
    @GET("api.php/doctor/commonData/getHospital")
    Observable<BaseData<List<IdNameBean>>> getHospitalList(@QueryMap Map<String, String> getParams);

    //获取用户信息
    @GET("api.php/doctor/doctorinfo/index")
    @Headers(RetrofitClient.FORCE_NETWORK)
    Observable<BaseData<LoginInfo_New>> getDoctorInfo(@QueryMap Map<String, String> getParams);

    //认证用户信息
    @FormUrlEncoded
    @Headers(ApiConstants.FORCE_NETWORK)
    @POST("api.php/doctor/doctorCommon/perfectDoctor")
    Observable<BaseData> postCertifyUserInfo(@QueryMap Map<String, String> getParams,@FieldMap Map<String, String> postparams);


    //获取留言板
    @Headers(ApiConstants.FORCE_NETWORK)
    @GET("api.php/imask/doctor/get_messageboard")
    Observable<BaseData<List<MessageBoardBean>>> getMessageBoard(@QueryMap Map<String, String> getParams);

    //设置留言板
    @FormUrlEncoded
    @Headers(ApiConstants.FORCE_NETWORK)
    @POST("api.php/imask/doctor/set_messageboard")
    Observable<BaseData<MessageBoardBean>> getSetMessageBoard(@QueryMap Map<String, String> getParams,@FieldMap Map<String, String> postparams);
}
