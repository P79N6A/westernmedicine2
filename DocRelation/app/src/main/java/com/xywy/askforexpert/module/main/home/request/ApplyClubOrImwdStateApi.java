package com.xywy.askforexpert.module.main.home.request;

import com.xywy.retrofit.net.BaseData;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by xugan on 2018/4/19.
 */
public interface ApplyClubOrImwdStateApi {
    @GET("api.php/club/doctorPayAudit/index")
    Observable<BaseData> getClubAssignState(@QueryMap Map<String, String> getParams);

    @GET("api.php/imask/doctor/audit_service")
    Observable<BaseData> getImwdState(@QueryMap Map<String, String> getParams);
}
