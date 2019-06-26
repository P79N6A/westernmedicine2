package com.xywy.askforexpert.module.my.pause.request;

import com.xywy.askforexpert.appcommon.net.retrofitWrapper.ApiConstants;
import com.xywy.askforexpert.model.MyPurse.BillInfo;
import com.xywy.askforexpert.model.MyPurse.MyPurseBean;
import com.xywy.askforexpert.model.bankCard.BankCard;
import com.xywy.retrofit.net.BaseData;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by xugan on 2018/5/25.
 * 绑定银行卡相关的接口
 */

public interface PurseApi {
    @FormUrlEncoded
    @POST("api.php/yimai/dcard/dcardadd")
    Observable<BaseData> bindBankCard(@QueryMap Map<String, String> getParams, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api.php/yimai/dcard/getdcard")
    Observable<BaseData<BankCard>> getBankCardState(@QueryMap Map<String, String> getParams, @FieldMap Map<String, String> map);

    @Headers(ApiConstants.FORCE_NETWORK)
    @GET("/api.php/yimai/yimaiDoctor/wallet")
    Observable<BaseData<MyPurseBean>> getBillSummaryDetail(@QueryMap Map<String, String> getParams);

    @Headers(ApiConstants.FORCE_NETWORK)
    @GET("/api.php/yimai/yimaiDoctor/servDetail")
    Observable<BaseData<BillInfo>> getBillDetail(@QueryMap Map<String, String> getParams);
}
