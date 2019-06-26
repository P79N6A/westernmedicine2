package com.xywy.askforexpert.module.discovery.medicine.module.medical.request;

import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.module.discovery.medicine.common.MyRetrofitClient;
import com.xywy.askforexpert.module.discovery.medicine.common.RequestTool;
import com.xywy.askforexpert.module.discovery.medicine.config.OffLineService;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.MedicineEntity;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.PharmacyEntity;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.PharmacyRecordEntity;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.RecipeEntity;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.SearchResultEntity;
import com.xywy.retrofit.net.BaseData;
import com.xywy.util.LogUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by bobby on 17/3/29.
 */

public class MedicineRequest {

    private MedicineApi api;
    private static MedicineRequest instance;
    private MedicineRequest() {
//        api = RetrofitClient.getRetrofit().create(MedicineApi.class);
        api = MyRetrofitClient.getMyRetrofit().create(MedicineApi.class);

    }

    static public MedicineRequest getInstance() {
        if(instance == null) {
            instance = new MedicineRequest();
        }
        return instance;
    }

    public Observable<BaseData<List<PharmacyEntity>>> getMedicineCategory() {
        Map<String, String> getParams = RequestTool.getCommonParams("1610");
        getParams.put("sign",RequestTool.getSign(getParams));
        return api.getMedicineCategoryNew(getParams);
    }

    //order_by 排序（1 默认 2 价格 3 销量 4 分成指数）
    //sort 排序规则 1 正序 2 倒序
    public Observable<BaseData<List<MedicineEntity>>> getMedicineListForCateId(int cate_id, int order_by, int sort, int page, int pagesize) {
        Map<String, String> getParams = RequestTool.getCommonParams("1612");
        getParams.put("cate_id", ""+cate_id);
        getParams.put("order_by", ""+order_by);
        getParams.put("sort", ""+sort);
        getParams.put("page", ""+page);
        getParams.put("pagesize", ""+pagesize);
        getParams.put("version", "1.1");
        int doctor_id = Integer.parseInt(YMUserService.getCurUserId());
        getParams.put("doctor_id",""+doctor_id);
        if(-2==cate_id){//-2代表明星药品
            getParams.remove("cate_id");
            int star_cate = 1;
            getParams.put("star_cate",""+star_cate);
        }
        getParams.put("sign",RequestTool.getSign(getParams));
        return api.getMedicineListForCateIdNew(getParams);
    }

    //order_by 排序（1 默认 2 价格 3 销量 4 分成指数）
    //sort 排序规则 1 正序 2 倒序
    public Observable<BaseData<List<MedicineEntity>>> getMedicineListForDrugId(int drug_id, int order_by, int sort,int page,int pagesize) {
        Map<String, String> getParams = RequestTool.getCommonParams("1612");
        getParams.put("drug_id", ""+drug_id);
        getParams.put("order_by", ""+order_by);
        getParams.put("sort", ""+sort);
        getParams.put("page", ""+page);
        getParams.put("pagesize", ""+pagesize);
        if(OffLineService.isOffLine()){
            getParams.put("version", "1.1");
//            UserBean user = UserManager.getInstance().getCurrentLoginUser();
//            int doctor_id = Integer.parseInt(user.getLoginServerBean().getUser_id());
            //使用医脉的用户id
            int doctor_id = Integer.parseInt(YMUserService.getCurUserId());
            getParams.put("doctor_id",""+doctor_id);
        }
        getParams.put("sign",RequestTool.getSign(getParams));
        return api.getMedicineListForDrugIdNew(getParams);
    }

    public Observable<BaseData<List<SearchResultEntity>>> medicineSearch(String keyword) {
        Map<String, String> getParams = RequestTool.getCommonParams("1611");
        getParams.put("keyword", keyword);
        getParams.put("sign",RequestTool.getSign(getParams));
        return api.searchMedicineNew(getParams);
    }

    //发送处方
    public Observable<BaseData<RecipeEntity>> sendRecipe(int user_id, String user_name, int age, int sex,
                                                         int doctor_id, String doctor_name,
                                                         String diagnosis, int pov, String drugsJson) {
//        final Map<String, String> bundle = new HashMap<>();
        Map<String, String> getParams = RequestTool.getCommonParams("1614");
        getParams.put("version", "1.0");
        getParams.put("source", RequestTool.REQUEST_SOURCE);
        getParams.put("pro", RequestTool.REQUEST_PRO);
        getParams.put("os", "android");
//        Map<String, String> postParams = RequestTool.getCommonParams("1614");
        Map<String, String> postParams = new HashMap<>();
        postParams.put("user_id", user_id + "");
        postParams.put("user_name", user_name);
        postParams.put("age", age + "");
        if(-1 != sex){
            postParams.put("sex", sex + "");
        }
        postParams.put("doctor_id", doctor_id + "");
        postParams.put("doctor_name", doctor_name);
        postParams.put("diagnosis", diagnosis);
        postParams.put("pov", pov+"");
        postParams.put("drugs", drugsJson);
        LogUtils.e(""+postParams.toString());
        getParams.put("sign", RequestTool.getSign(getParams, postParams));
        return api.sendRecipe(getParams,postParams);
    }


    public Observable<BaseData<List<PharmacyRecordEntity>>> getPharmacyRecord(int doctor_id, String keyword, int page, int pagesize) {
        Map<String, String> getParams = RequestTool.getCommonParams("1618");
        getParams.put("doctor_id", doctor_id+"");
        getParams.put("keyword", keyword);
        getParams.put("page", page+"");
        getParams.put("pagesize", pagesize+"");
        getParams.put("sign",RequestTool.getSign(getParams));
        return api.getPharmacyRecordNew(getParams);

    }


    public Observable<BaseData> bindPharmaceuticalAgentsOffline(long doctor_id, long rep_id) {
        Map<String, String> getParams = RequestTool.getCommonParams("1644");
        getParams.put("version", "1.0");
        getParams.put("source", RequestTool.REQUEST_SOURCE);
        getParams.put("pro", RequestTool.REQUEST_PRO);
        getParams.put("os", "android");
        Map<String, String> postParams = RequestTool.getCommonParams("1644");
        postParams.put("doctor_id", doctor_id+"");
        postParams.put("rep_id", rep_id+"");
        //新的医脉加用药的这个app,不需要sevice_id
//        int service_id = BuildConfig.service_id;
//        postParams.put("service_id", ""+service_id);
        getParams.put("sign", RequestTool.getSign(getParams,postParams));
        return api.bindPharmaceuticalAgentsOffline(getParams,postParams);
    }

    public Observable<BaseData> deleteDrug(long doctor_id, int product_id) {
        Map<String, String> getParams = RequestTool.getCommonParams("1660");
        Map<String, String> postParams = RequestTool.getCommonParams("1660");
        postParams.put("doctor_id", doctor_id+"");
        postParams.put("product_id", product_id+"");
        getParams.put("sign", RequestTool.getSign(getParams,postParams));
        return api.deleteDrug(getParams,postParams);
    }
}
