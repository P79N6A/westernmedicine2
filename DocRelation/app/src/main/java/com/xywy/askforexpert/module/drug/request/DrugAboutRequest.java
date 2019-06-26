package com.xywy.askforexpert.module.drug.request;

import android.text.TextUtils;

import com.xywy.askforexpert.module.discovery.medicine.common.MyRetrofitClient;
import com.xywy.askforexpert.module.discovery.medicine.common.RequestTool;
import com.xywy.askforexpert.module.drug.bean.BasisDoctorDiagnose;
import com.xywy.askforexpert.module.drug.bean.DocQues;
import com.xywy.askforexpert.module.drug.bean.DoctorPrice;
import com.xywy.askforexpert.module.drug.bean.DrugBean;
import com.xywy.askforexpert.module.drug.bean.PrescriptionDetailBean;
import com.xywy.askforexpert.module.drug.bean.PrescriptionMsg;
import com.xywy.retrofit.net.BaseData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * 药品相关请求 医脉 IM诊室
 * stone
 */
public class DrugAboutRequest {
    private DrugAboutApi api;
    private static DrugAboutRequest instance;

    private DrugAboutRequest() {
        api = MyRetrofitClient.getMyRetrofit().create(DrugAboutApi.class);
    }

    static public DrugAboutRequest getInstance() {
        if (instance == null) {
            instance = new DrugAboutRequest();
        }
        return instance;
    }

    public Observable<BaseData<List<DocQues>>> getDocQues(String doctor_id,int type,int status,int page) {
        Map<String, String> getParams = RequestTool.getCommonParams("1888");
        getParams.put("did", doctor_id);
        getParams.put("type", type+"");
        if(-1 != status){
            getParams.put("status", status+"");
        }
        getParams.put("page", String.valueOf(page));
        getParams.put("pagesize", "10");

        getParams.put("sign", RequestTool.getSign(getParams));
        return api.getDocQues(getParams);
    }

    public Observable<BaseData<List<DocQues>>> getVisitingDocQues(String doctor_id,int page) {
        Map<String, String> getParams = RequestTool.getCommonParams("2070");
        getParams.put("did", doctor_id);
        getParams.put("page", String.valueOf(page));
        getParams.put("pagesize", "10");

        getParams.put("sign", RequestTool.getSign(getParams));
        return api.getVisitingDocQues(getParams);
    }

    public Observable<BaseData<List<DrugBean>>> getCommonDrugList(String doctor_id, int page) {
        Map<String, String> getParams = RequestTool.getCommonParams("1864");
        getParams.put("doctor_id", doctor_id);
        getParams.put("page", String.valueOf(page));
        getParams.put("pagesize", "10");

        getParams.put("sign", RequestTool.getSign(getParams));
        return api.getCommonDrugList(getParams);
    }

    public Observable<BaseData> getCommonDrugAdd(String doctor_id, String pid) {
        Map<String, String> getParams = RequestTool.getCommonParams("1866");
        getParams.put("doctor_id", doctor_id);
        getParams.put("pid", pid);

        getParams.put("sign", RequestTool.getSign(getParams));
        return api.getCommonDrugAdd(getParams);
    }

    public Observable<BaseData> getCommonDrugRemove(String doctor_id, String id) {
        Map<String, String> getParams = RequestTool.getCommonParams("1865");
        getParams.put("doctor_id", doctor_id);
        getParams.put("id", id);

        getParams.put("sign", RequestTool.getSign(getParams));
        return api.getCommonDrugRemove(getParams);
    }

    public Observable<BaseData<List<DrugBean>>> getGoodsList(String doctor_id, String cateid, String drugid, int page) {
        Map<String, String> getParams = RequestTool.getCommonParams("1867");
//        仅在需要获取是否加入常用药标示时传入即可
        if (!TextUtils.isEmpty(doctor_id)) {
            getParams.put("doctor_id", doctor_id);
        }
        if (!TextUtils.isEmpty(drugid)) {
            getParams.put("drugid", drugid);
        }
        getParams.put("cateid", cateid);//分类id
//        getParams.put("drugid", drugid);//药品id

        getParams.put("page", String.valueOf(page));
        getParams.put("pagesize", "10");

        getParams.put("sign", RequestTool.getSign(getParams));
        return api.getGoodsList(getParams);
    }

    /**
     * 处方列表
     *
     * @param doctor_id
     * @param condition 搜索项 ：1.审核中 2.已审核 3.已购买 4.已失效 5.已退款
     * @param sort      排序 ：1.正序 2.倒序
     * @param page
     * @return
     */
    public Observable<BaseData<List<DocQues>>> getPrescriptionList(String doctor_id, String condition, String sort, int page) {
        Map<String, String> getParams = RequestTool.getCommonParams("1868");
        getParams.put("doctor_id", doctor_id);
        if(!TextUtils.isEmpty(condition)){
            getParams.put("condition", condition);
        }
        getParams.put("sort", sort);

        getParams.put("page", String.valueOf(page));
        getParams.put("pagesize", "10");

        getParams.put("sign", RequestTool.getSign(getParams));
        return api.getPrescriptionList(getParams);
    }

    /**
     * 处方详情
     *
     * @param doctor_id
     * @param pid
     * @return
     */
    public Observable<BaseData<PrescriptionDetailBean>> getPrescriptionDetail(String doctor_id, String pid) {
        Map<String, String> getParams = RequestTool.getCommonParams("1869");
        getParams.put("doctor_id", doctor_id);
        getParams.put("pid", pid);//处方id

        getParams.put("sign", RequestTool.getSign(getParams));
        return api.getPrescriptionDetail(getParams);
    }

    /**
     * 取消处方
     *
     * @param doctor_id
     * @param pid
     * @return
     */
    public Observable<BaseData> getPrescriptionCancel(String doctor_id, String pid) {
        Map<String, String> getParams = RequestTool.getCommonParams("1870");
        getParams.put("doctor_id", doctor_id);
        getParams.put("pid", pid);//处方id

        getParams.put("sign", RequestTool.getSign(getParams));
        return api.getPrescriptionCancel(getParams);
    }

    /**
     * 添加处方
     *
     */
    public Observable<BaseData<PrescriptionMsg>> postPrescriptionAdd(String did, String dname, String uid, String uname, String usersex,
                                                                     String age, String diagnosis, String drugs, String questionid,
                                                                     String age_month,String age_day) {
        Map<String, String> getParams = RequestTool.getCommonParams("1871");
        Map<String, String> postParams = new HashMap<>();
        postParams.put("did", did);
        postParams.put("dname", dname);
        postParams.put("uid", uid);
        postParams.put("uname", uname);
        postParams.put("usersex", usersex);
        postParams.put("age", age);
        postParams.put("age_month", age_month);
        postParams.put("age_day", age_day);
        postParams.put("diagnosis", diagnosis);
        postParams.put("drugs", drugs);
        postParams.put("usource", "1");
        postParams.put("questionid", questionid);
        getParams.put("sign", RequestTool.getSign(getParams,postParams));
        return api.postPrescriptionAdd(getParams,postParams);
    }

    /**
     * 基础诊断疾病数据
     *
     */
    public Observable<BaseData<ArrayList<BasisDoctorDiagnose>>> getDoctorDiagnose(String doctor_id) {
        Map<String, String> getParams = RequestTool.getCommonParams("2160");
        getParams.put("doctor_id", doctor_id);
        getParams.put("sign", RequestTool.getSign(getParams));
        return api.getDoctorDiagnose(getParams);
    }

    /**
     * 搜索诊断疾病数据
     *
     */
    public Observable<BaseData<ArrayList<BasisDoctorDiagnose>>> getDiagnoseSearchList(String diagnoseName) {
        Map<String, String> getParams = RequestTool.getCommonParams("2161");
        getParams.put("diagnoseName", diagnoseName);
        getParams.put("sign", RequestTool.getSign(getParams));
        return api.getDiagnoseSearchList(getParams);
    }

    public Observable<BaseData> setPrice(String did, String amount) {
        Map<String, String> getParams = RequestTool.getCommonParams("1887");
        getParams.put("did", did);
        getParams.put("amount", amount);
        getParams.put("type", "2");
        getParams.put("sign", RequestTool.getSign(getParams));
        return api.setPrice(getParams);
    }

    public Observable<BaseData<DoctorPrice>> getPrice(String id) {
        Map<String, String> getParams = RequestTool.getCommonParams("1714");
        getParams.put("id", id);
        getParams.put("sign", RequestTool.getSign(getParams));
        return api.getPrice(getParams);
    }

}
