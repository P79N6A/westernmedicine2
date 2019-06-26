package com.xywy.askforexpert.module.discovery.medicine.module.medical.request;

import com.xywy.askforexpert.module.discovery.medicine.common.MyConstant;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.PharmacyRecordEntityList;
import com.xywy.datarequestlibrary.XywyDataRequestApi;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by xgxg on 2017/5/4.
 */

public class DataService {
    public static void getPharmacyRecord(int doctor_id, String keyword, int page, int pagesize, Subscriber<PharmacyRecordEntityList> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("doctor_id", doctor_id+"");
        getParams.put("keyword", keyword);
        getParams.put("page", page+"");
        getParams.put("pagesize", pagesize+"");
        //新的医脉加用药的这个app,不需要sevice_id
//        int service_id = BuildConfig.service_id;
//        getParams.put("service_id", service_id+"");
        String partUrl = "api.php/wkys/wkysDoctor/prescri";
        XywyDataRequestApi.getInstance().getRequest(MyConstant.SERVER_URL,partUrl,getParams,"1618","1.0",PharmacyRecordEntityList.class, subscriber);

    }

}
