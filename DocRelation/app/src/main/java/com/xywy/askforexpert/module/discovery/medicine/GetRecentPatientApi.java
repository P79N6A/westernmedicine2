package com.xywy.askforexpert.module.discovery.medicine;

import com.xywy.askforexpert.module.discovery.medicine.module.patient.entity.Patient;
import com.xywy.retrofit.net.BaseData;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by xgxg on 2017/11/1.
 */

public interface GetRecentPatientApi {
    @GET("api.php/wkysym/wkys/get_recent_patient")
    Observable<BaseData<List<Patient>>> getRecentPatient(@QueryMap Map<String, String> getParams);
}
