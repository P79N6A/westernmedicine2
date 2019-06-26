package com.xywy.askforexpert.module.discovery.medicine.module.appupdate;

import com.xywy.askforexpert.module.discovery.medicine.common.MyRetrofitClient;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity.AppVersion;
import com.xywy.retrofit.net.BaseData;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by xgxg on 2017/11/9.
 * 更新app版本
 */

public class UpdateAppRequest {
    private static UpdateAppRequest instance;
    private UpdateAppApi api;

    private UpdateAppRequest() {
//        api = RetrofitClient.getRetrofit().create(PersonCardApi.class);
        api = MyRetrofitClient.getMyRetrofit().create(UpdateAppApi.class);
    }

    static public UpdateAppRequest getInstance() {
        if (instance == null) {
            instance = new UpdateAppRequest();
        }
        return instance;
    }

    public Observable<BaseData<AppVersion>> checkVersion(final String version) {
//        getParamMap.put("sign", RequestTool.getSign(getParamMap, null));
        Map<String, String> getParams = new HashMap<>();
//        source传
//        yyzs_doctor_online 线上版
//        yyzs_doctor_offline_cq 线下重庆
//        yyzs_doctor_offline_xt 线下邢台

        getParams.put("source", "yyzs_doctor_offline_cq");
        getParams.put("app_version", version);
        return api.checkVersion(getParams);
    }
}
