package com.xywy.askforexpert.module.discovery.medicine;

import com.xywy.askforexpert.module.discovery.medicine.common.MyRetrofitClient;
import com.xywy.askforexpert.module.discovery.medicine.common.RequestTool;
import com.xywy.retrofit.net.BaseData;

import java.util.Map;

import rx.Observable;

/**
 * Created by xgxg on 2017/10/26.
 */

public class SellDrugRequest {
    private SellDrugApi api;
    private static SellDrugRequest instance;
    private SellDrugRequest() {
//        api = RetrofitClient.getRetrofit().create(MedicineApi.class);
        api = MyRetrofitClient.getMyRetrofit().create(SellDrugApi.class);

    }

    static public SellDrugRequest getInstance() {
        if(instance == null) {
            instance = new SellDrugRequest();
        }
        return instance;
    }

    public Observable<BaseData> getSellDrug(long doctor_id) {
        Map<String, String> getParams = RequestTool.getCommonParams("1758");
        getParams.put("doctor_id",doctor_id+"");
        getParams.put("sign",RequestTool.getSign(getParams));
        return api.getSellDrug(getParams);
    }
}
