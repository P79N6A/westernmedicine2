package com.xywy.askforexpert.appcommon.medicine;

import com.xywy.askforexpert.module.discovery.medicine.common.MyRetrofitClient;
import com.xywy.askforexpert.module.discovery.medicine.common.RequestTool;
import com.xywy.retrofit.net.BaseData;

import java.util.Map;

import rx.Observable;

/**
 * Created by xgxg on 2017/10/26.
 */

public class SyncInfoRequest {
    private SyncInfoApi api;
    private static SyncInfoRequest instance;
    private SyncInfoRequest() {
        api = MyRetrofitClient.getMyRetrofit().create(SyncInfoApi.class);

    }

    static public SyncInfoRequest getInstance() {
        if(instance == null) {
            instance = new SyncInfoRequest();
        }
        return instance;
    }

    public Observable<BaseData> syncInfo(long doctor_id) {
        Map<String, String> getParams = RequestTool.getCommonParams("1757");
        getParams.put("id",doctor_id+"");
        getParams.put("sign",RequestTool.getSign(getParams));
        return api.syncInfo(getParams);
    }
}
