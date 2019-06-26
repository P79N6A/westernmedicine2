package com.xywy.askforexpert.module.discovery.medicine.module.patient.request;

import com.xywy.askforexpert.module.discovery.medicine.module.patient.entity.Patient;
import com.xywy.retrofit.net.BaseData;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface PatientListApi {
    @GET("api.php/wkys/wkysDoctor/patient")
    Observable<BaseData<List<Patient>>> getPatientsNew(@QueryMap Map<String, String> getParams);

    @GET("api.php/wkys/wkysDoctor/group")
    Observable<BaseData<List<Patient>>> getPatientInfo(@QueryMap Map<String, String> getParams);

    @GET("api.php/wkys/wkysDoctor/remove_patient")
    Observable<BaseData<List<Patient>>> removePatient(@QueryMap Map<String, String> getParams);
}
