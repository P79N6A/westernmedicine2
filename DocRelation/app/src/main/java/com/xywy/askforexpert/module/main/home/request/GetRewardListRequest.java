package com.xywy.askforexpert.module.main.home.request;

import com.xywy.askforexpert.model.home.RewardItemBean;
import com.xywy.askforexpert.module.discovery.medicine.common.MyRetrofitClient;
import com.xywy.askforexpert.module.discovery.medicine.common.RequestTool;
import com.xywy.retrofit.net.BaseData;

import java.util.Map;

import rx.Observable;

/**
 * Created by xugan on 2018/4/12.
 */

public class GetRewardListRequest {
    private RewardApi api;
    private static GetRewardListRequest instance;
    private GetRewardListRequest() {
//        api = RetrofitClient.getRetrofit().create(MedicineApi.class);
        api = MyRetrofitClient.getMyRetrofit().create(RewardApi.class);

    }

    static public GetRewardListRequest getInstance() {
        if(instance == null) {
            instance = new GetRewardListRequest();
        }
        return instance;
    }

    public Observable<BaseData<RewardItemBean>> getRewardList() {
        Map<String, String> getParams = RequestTool.getCommonParams("1785");
        getParams.put("sign",RequestTool.getSign(getParams));
        return api.getReward(getParams);
    }

}
