package com.xywy.askforexpert.module.discovery.medicine;

import com.xywy.askforexpert.module.discovery.medicine.common.MyRetrofitClient;
import com.xywy.askforexpert.module.discovery.medicine.common.RequestTool;
import com.xywy.askforexpert.module.discovery.medicine.module.patient.entity.Patient;
import com.xywy.retrofit.net.BaseData;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by xgxg on 2017/11/1.
 */

public class GetRecentPatientRequest {
    private GetRecentPatientApi api;
    private static GetRecentPatientRequest instance;
    private GetRecentPatientRequest() {
        api = MyRetrofitClient.getMyRetrofit().create(GetRecentPatientApi.class);

    }

    static public GetRecentPatientRequest getInstance() {
        if(instance == null) {
            instance = new GetRecentPatientRequest();
        }
        return instance;
    }

    public Observable<BaseData<List<Patient>>> getRecentPatient(long doctor_id, int pagesize) {
        Map<String, String> getParams = RequestTool.getCommonParams("1764");
        getParams.put("doctor_id",doctor_id+"");
        getParams.put("pagesize",pagesize+"");
        getParams.put("sign",RequestTool.getSign(getParams));
        return api.getRecentPatient(getParams);
    }
}
