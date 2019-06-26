package com.xywy.askforexpert.module.message.notice.request;

import com.xywy.askforexpert.model.notice.Notice;
import com.xywy.askforexpert.module.discovery.medicine.common.MyRetrofitClient;
import com.xywy.askforexpert.module.discovery.medicine.common.RequestTool;
import com.xywy.retrofit.net.BaseData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by xugan on 2018/5/25.
 */

public class GetNoticeListRequest {
    private GetNoticeListApi api;
    private static GetNoticeListRequest instance = new GetNoticeListRequest();
    private GetNoticeListRequest(){
        api = MyRetrofitClient.getMyRetrofit().create(GetNoticeListApi.class);
    }
    public static GetNoticeListRequest getInstance(){
        return instance;
    }

    public Observable<BaseData<List<Notice>>> getNoticeList(String userid){
        Map<String, String> getApiParams = RequestTool.getCommonParams("1840");
        Map<String, String> postApiParams = new HashMap<>();
        postApiParams.put("userid", userid);
        getApiParams.put("sign",RequestTool.getSign(getApiParams,postApiParams));
        return api.getNoticeList(getApiParams,postApiParams);
    }

    public Observable<BaseData<Notice>> getNewNotice(String userid){
        Map<String, String> getApiParams = RequestTool.getCommonParams("1841");
        Map<String, String> postApiParams = new HashMap<>();
        postApiParams.put("userid", userid);
        getApiParams.put("sign",RequestTool.getSign(getApiParams,postApiParams));
        return api.getNewNotice(getApiParams,postApiParams);
    }
}
