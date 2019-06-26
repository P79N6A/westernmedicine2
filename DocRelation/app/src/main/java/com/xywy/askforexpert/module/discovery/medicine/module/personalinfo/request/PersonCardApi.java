package com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.request;

import com.xywy.askforexpert.module.discovery.medicine.common.ApiConstants;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity.AppVersion;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity.ImageBean;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity.KeyValueNode;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity.PersonCard;
import com.xywy.retrofit.RetrofitClient;
import com.xywy.retrofit.net.BaseData;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface PersonCardApi {
    @GET("api.php/doctor/doctorinfo/index")
    @Headers(RetrofitClient.FORCE_NETWORK)
    Observable<BaseData<PersonCard>> getDoctorCardNew(@QueryMap Map<String, String> getParams);

    @GET("api.php/wkysym/wkys/get_docewm")
    @Headers(RetrofitClient.FORCE_NETWORK)
    Observable<BaseData> getDoctorEWM(@QueryMap Map<String, String> getParams);

    @POST(ApiConstants.UPLOAD_IMAGE_METHOD)
    @Headers(RetrofitClient.FORCE_NETWORK)
    Observable<BaseData<ImageBean>> uploadImg(@QueryMap Map<String, String> getParams, @Body MultipartBody body);

    @GET(ApiConstants.GET_HOSP_METHOD)
    Observable<BaseData<List<KeyValueNode>>> getHosp(@QueryMap Map<String, String> getParams);

    @FormUrlEncoded
    @POST(ApiConstants.UPDATE_INFO_METHOD)
    Observable<BaseData> updatePersonalInfo(@QueryMap Map<String, String> getParams, @FieldMap Map<String, String> map);

    @GET(ApiConstants.CHECK_VERSIONS)
    Observable<BaseData<AppVersion>> checkVersion(@QueryMap Map<String, String> getParams);
}
