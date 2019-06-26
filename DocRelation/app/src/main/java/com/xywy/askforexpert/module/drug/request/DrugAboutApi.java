package com.xywy.askforexpert.module.drug.request;

import com.xywy.askforexpert.appcommon.net.retrofitWrapper.ApiConstants;
import com.xywy.askforexpert.module.drug.bean.BasisDoctorDiagnose;
import com.xywy.askforexpert.module.drug.bean.DocQues;
import com.xywy.askforexpert.module.drug.bean.DoctorPrice;
import com.xywy.askforexpert.module.drug.bean.DrugBean;
import com.xywy.askforexpert.module.drug.bean.PrescriptionDetailBean;
import com.xywy.askforexpert.module.drug.bean.PrescriptionMsg;
import com.xywy.retrofit.net.BaseData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;


/**
 * 药品相关api 医脉 IM诊室
 * stone
 */
public interface DrugAboutApi {

    //常用药品列表
    @Headers(ApiConstants.FORCE_NETWORK)
    @GET("/api.php/imask/getDoctor/doc_ques")
    Observable<BaseData<List<DocQues>>> getDocQues(@QueryMap Map<String, String> getParams);

    @Headers(ApiConstants.FORCE_NETWORK)
    @GET("/imask/getDoctor/acceptQues")
    Observable<BaseData<List<DocQues>>> getVisitingDocQues(@QueryMap Map<String, String> getParams);

    //常用药品列表
    @Headers(ApiConstants.FORCE_NETWORK)
    @GET("api.php/wkysym/yao/druglist")
    Observable<BaseData<List<DrugBean>>> getCommonDrugList(@QueryMap Map<String, String> getParams);

    //常用药品添加
    @Headers(ApiConstants.FORCE_NETWORK)
    @GET("api.php/wkysym/yao/drugadd")
    Observable<BaseData> getCommonDrugAdd(@QueryMap Map<String, String> getParams);

    //常用药品移除
    @Headers(ApiConstants.FORCE_NETWORK)
    @GET("api.php/wkysym/yao/drugdel")
    Observable<BaseData> getCommonDrugRemove(@QueryMap Map<String, String> getParams);

    //商品列表
    @Headers(ApiConstants.FORCE_NETWORK)
    @GET("api.php/wkysym/yao/goodslist")
    Observable<BaseData<List<DrugBean>>> getGoodsList(@QueryMap Map<String, String> getParams);

    //处方列表
    @Headers(ApiConstants.FORCE_NETWORK)
    @GET("api.php/wkysym/cf/cflist")
    Observable<BaseData<List<DocQues>>> getPrescriptionList(@QueryMap Map<String, String> getParams);

    //处方详情
    @Headers(ApiConstants.FORCE_NETWORK)
    @GET("api.php/wkysym/cf/cfdetail")
    Observable<BaseData<PrescriptionDetailBean>> getPrescriptionDetail(@QueryMap Map<String, String> getParams);

    //取消处方
    @Headers(ApiConstants.FORCE_NETWORK)
    @GET("api.php/wkysym/cf/cfcancel")
    Observable<BaseData> getPrescriptionCancel(@QueryMap Map<String, String> getParams);

    //添加处方
    @FormUrlEncoded
    @Headers(ApiConstants.FORCE_NETWORK)
    @POST("api.php/wkysym/cf/cfadd")
    Observable<BaseData<PrescriptionMsg>> postPrescriptionAdd(@QueryMap Map<String, String> getParams, @FieldMap Map<String, String> postparams);

    //设置医事服务费
    @Headers(ApiConstants.FORCE_NETWORK)
    @GET("api.php/imask/doctor/upadte_amount")
    Observable<BaseData> setPrice(@QueryMap Map<String, String> getParams);

    //获取基础诊断疾病数据
    @Headers(ApiConstants.FORCE_NETWORK)
    @GET("api.php/wkysym/wkys/doctor_diagnose")
    Observable<BaseData<ArrayList<BasisDoctorDiagnose>>> getDoctorDiagnose(@QueryMap Map<String, String> getParams);

    //获取搜索诊断疾病数据
    @Headers(ApiConstants.FORCE_NETWORK)
    @GET("api.php/wkysym/wkys/diagnose_list")
    Observable<BaseData<ArrayList<BasisDoctorDiagnose>>> getDiagnoseSearchList(@QueryMap Map<String, String> getParams);

    //设置医事服务费
    @Headers(ApiConstants.FORCE_NETWORK)
    @GET("api.php/imask/getDoctor/detail")
    Observable<BaseData<DoctorPrice>> getPrice(@QueryMap Map<String, String> getParams);
}
