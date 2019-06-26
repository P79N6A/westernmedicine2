package com.xywy.askforexpert.module.main.home.request;

import com.xywy.askforexpert.module.discovery.medicine.common.MyRetrofitClient;
import com.xywy.askforexpert.module.discovery.medicine.common.RequestTool;
import com.xywy.retrofit.net.BaseData;

import java.util.Map;

import rx.Observable;

/**
 * Created by xugan on 2018/4/19.
 */

public class ApplyClubOrImwdStateRequest {
    private ApplyClubOrImwdStateApi api;
    private static ApplyClubOrImwdStateRequest instance;
    private ApplyClubOrImwdStateRequest() {
        api = MyRetrofitClient.getMyRetrofit().create(ApplyClubOrImwdStateApi.class);
    }

    static public ApplyClubOrImwdStateRequest getInstance() {
        if(instance == null) {
            instance = new ApplyClubOrImwdStateRequest();
        }
        return instance;
    }

    public Observable<BaseData> getClubAssignState(String uid) {
        Map<String, String> getParams = RequestTool.getCommonParams("1816");
        getParams.put("uid",uid);
        getParams.put("sign",RequestTool.getSign(getParams));
        return api.getClubAssignState(getParams);
    }

    public Observable<BaseData> getImwdState(String did,int type) {
        Map<String, String> getParams = RequestTool.getCommonParams("1824");
        getParams.put("did",did);
        getParams.put("type",type+"");
        getParams.put("sign",RequestTool.getSign(getParams));
        return api.getImwdState(getParams);
    }

}
