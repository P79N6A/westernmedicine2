package com.xywy.askforexpert.module.my.request;

import com.xywy.askforexpert.model.my.Income;
import com.xywy.askforexpert.module.discovery.medicine.common.MyRetrofitClient;
import com.xywy.askforexpert.module.discovery.medicine.common.RequestTool;
import com.xywy.retrofit.net.BaseData;

import java.util.Map;

import rx.Observable;

/**
 * Created by xugan on 2018/6/21.
 */

public class UserCenterFragmentRequest {
    private UserCenterFragmentApi api;
    private static UserCenterFragmentRequest instance = new UserCenterFragmentRequest();
    private UserCenterFragmentRequest(){
        api = MyRetrofitClient.getMyRetrofit().create(UserCenterFragmentApi.class);
    }
    public static UserCenterFragmentRequest getInstance(){
        return instance;
    }

    public Observable<BaseData<Income>> getNewIncome(String doctor_id){
        Map<String, String> getApiParams = RequestTool.getCommonParams("1873");
        getApiParams.put("doctor_id", doctor_id);
        getApiParams.put("sign",RequestTool.getSign(getApiParams));
        return api.getNewIncome(getApiParams);
    }

}
