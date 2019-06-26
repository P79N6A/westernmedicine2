package com.xywy.askforexpert.module.discovery.medicine.module.patient.request;

import com.xywy.askforexpert.module.discovery.medicine.common.MyRetrofitClient;
import com.xywy.askforexpert.module.discovery.medicine.common.RequestTool;
import com.xywy.askforexpert.module.discovery.medicine.module.patient.entity.Patient;
import com.xywy.retrofit.net.BaseData;

import java.util.List;
import java.util.Map;

import rx.Observable;

public class PatientListRequest {

    private PatientListApi api;
    private static PatientListRequest instance;
    private PatientListRequest() {
//        api = RetrofitClient.getRetrofit().create(PatientListApi.class);
        api = MyRetrofitClient.getMyRetrofit().create(PatientListApi.class);
    }

    static public PatientListRequest getInstance() {
        if(instance == null) {
            instance = new PatientListRequest();
        }
        return instance;
    }

    public Observable<BaseData<List<Patient>>> getPatientList(int doctor_id) {
        Map<String, String> getParams = RequestTool.getCommonParams("1613");
        getParams.put("doctor_id",doctor_id+"");
        getParams.put("sign",RequestTool.getSign(getParams));
        return api.getPatientsNew(getParams);
    }

    public Observable<BaseData<List<Patient>>> getPatientInfo(int doctor_id,String patientId) {
        Map<String, String> getParams = RequestTool.getCommonParams("1774");
        getParams.put("doctor_id",doctor_id+"");
        getParams.put("user_ids",patientId);
        getParams.put("sign",RequestTool.getSign(getParams));
        return api.getPatientInfo(getParams);
    }

    public Observable<BaseData<List<Patient>>> removePatient(String doctor_id,String patient_id) {
        Map<String, String> getParams = RequestTool.getCommonParams("1773");
        getParams.put("version", "1.1");
        getParams.put("doctor_id",doctor_id);
        getParams.put("user_id",patient_id);
        getParams.put("sign",RequestTool.getSign(getParams));
        return api.removePatient(getParams);
    }

}
