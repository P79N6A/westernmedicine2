package com.xywy.askforexpert.module.my.pause.request;

import com.xywy.askforexpert.model.MyPurse.BillInfo;
import com.xywy.askforexpert.model.MyPurse.MyPurseBean;
import com.xywy.askforexpert.model.bankCard.BankCard;
import com.xywy.askforexpert.module.discovery.medicine.common.MyRetrofitClient;
import com.xywy.askforexpert.module.discovery.medicine.common.RequestTool;
import com.xywy.retrofit.net.BaseData;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by xugan on 2018/5/25.
 */

public class PurseRequest {
    private PurseApi api;
    private static PurseRequest instance = new PurseRequest();
    private PurseRequest(){
        api = MyRetrofitClient.getMyRetrofit().create(PurseApi.class);
    }
    public static PurseRequest getInstance(){
        return instance;
    }

    public Observable<BaseData> bindBankCard(String card_id,String card_name,String card_dress,String card_num,String userid){
        Map<String, String> getApiParams = RequestTool.getCommonParams("1843");
        Map<String, String> postApiParams = new HashMap<>();
        postApiParams.put("card_id",card_id);
        postApiParams.put("card_name",card_name);
        postApiParams.put("card_dress",card_dress);
        postApiParams.put("card_num", card_num);
        postApiParams.put("userid", userid);
        getApiParams.put("sign",RequestTool.getSign(getApiParams,postApiParams));
        return api.bindBankCard(getApiParams,postApiParams);
    }

    public Observable<BaseData<BankCard>> getBankCardState(String userid){
        Map<String, String> getApiParams = RequestTool.getCommonParams("1842");
        Map<String, String> postApiParams = new HashMap<>();
        postApiParams.put("userid", userid);
        getApiParams.put("sign",RequestTool.getSign(getApiParams,postApiParams));
        return api.getBankCardState(getApiParams,postApiParams);
    }

    public Observable<BaseData<MyPurseBean>> getBillSummaryDetail(String doctor_id, int month){
        Map<String, String> getApiParams = RequestTool.getCommonParams("1874");
        getApiParams.put("doctor_id", doctor_id);
        if(0!=month){
            getApiParams.put("month", month+"");
        }
        getApiParams.put("sign",RequestTool.getSign(getApiParams));
        return api.getBillSummaryDetail(getApiParams);
    }

    public Observable<BaseData<BillInfo>> getBillDetail(String doctor_id, int month, int type, int page, int pagesize){
        Map<String, String> getApiParams = RequestTool.getCommonParams("1876");
        getApiParams.put("doctor_id", doctor_id);
        getApiParams.put("month", month+"");
        getApiParams.put("type", type+"");
        getApiParams.put("page", page+"");
        getApiParams.put("pagesize", pagesize+"");
        getApiParams.put("sign",RequestTool.getSign(getApiParams));
        return api.getBillDetail(getApiParams);
    }
}
