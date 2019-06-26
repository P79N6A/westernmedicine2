package com.xywy.askforexpert.module.discovery.medicine.module.medical.request;

import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.MedicineEntity;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.PharmacyEntity;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.PharmacyRecordEntity;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.RecipeEntity;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.SearchResultEntity;
import com.xywy.retrofit.net.BaseData;

import java.util.List;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by bobby on 17/3/29.
 */

public interface MedicineApi {
    @GET("api.php/wkys/wkysDrug/cateList")
    Observable<BaseData<List<PharmacyEntity>>> getMedicineCategoryNew(@QueryMap Map<String, String> getParams);

    @GET("api.php/wkys/wkysDrug/drugList")
    Observable<BaseData<List<MedicineEntity>>> getMedicineListForCateIdNew(@QueryMap Map<String, String> getParams);

    @GET("api.php/wkys/wkysDrug/drugList")
    Observable<BaseData<List<MedicineEntity>>> getMedicineListForDrugIdNew(@QueryMap Map<String, String> getParams);

    @GET("api.php/wkys/wkysDrug/search")
    Observable<BaseData<List<SearchResultEntity>>> searchMedicineNew(@QueryMap Map<String, String> getParams);

    @FormUrlEncoded
    @POST("api.php/wkys/wkysPrescri/send?")
    Observable<BaseData<RecipeEntity>> sendRecipe(@QueryMap Map<String, String> getParams, @FieldMap Map<String, String> postParams);


    @GET("api.php/wkys/wkysDoctor/prescri")
    Observable<BaseData<List<PharmacyRecordEntity>>> getPharmacyRecordNew(@QueryMap Map<String, String> getParams);

    @FormUrlEncoded
    @POST("api.php/wkys/wkysDoctor/docrep?")
    Observable<BaseData> bindPharmaceuticalAgentsOffline(@QueryMap Map<String, String> getParams, @FieldMap Map<String, String> postParams);

    @FormUrlEncoded
    @POST("api.php/wkys/wkysDoctor/deldrug?")
    Observable<BaseData> deleteDrug(@QueryMap Map<String, String> getParams, @FieldMap Map<String, String> postParams);
}
