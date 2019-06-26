package com.xywy.askforexpert.module.discovery.medicine;

import android.text.TextUtils;

import com.xywy.askforexpert.model.IdNameBean;
import com.xywy.askforexpert.model.LoginInfo_New;
import com.xywy.askforexpert.model.certification.ApproveBean;
import com.xywy.askforexpert.model.certification.MessageBoardBean;
import com.xywy.askforexpert.module.discovery.medicine.common.MyRetrofitClient;
import com.xywy.askforexpert.module.discovery.medicine.common.RequestTool;
import com.xywy.retrofit.net.BaseData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * 认证相关请求
 * stone
 * 2018/2/5 上午11:38
 */
public class CertificationAboutRequest {
    private CertificationAboutApi api;
    private static CertificationAboutRequest instance;

    private CertificationAboutRequest() {
        api = MyRetrofitClient.getMyRetrofit().create(CertificationAboutApi.class);
    }

    static public CertificationAboutRequest getInstance() {
        if (instance == null) {
            instance = new CertificationAboutRequest();
        }
        return instance;
    }

    public Observable<BaseData<List<IdNameBean>>> getHospitalList(String name) {
        Map<String, String> getParams = RequestTool.getCommonParams("1608");
        getParams.put("name", name);
        getParams.put("sign", RequestTool.getSign(getParams));
        return api.getHospitalList(getParams);
    }

    public Observable<BaseData<LoginInfo_New>> getDoctorInfo(String doctor_id) {
        Map<String, String> getParams = RequestTool.getCommonParams("1601");
        getParams.put("mode", "1");
        getParams.put("doctor_id", doctor_id);
        getParams.put("sign", RequestTool.getSign(getParams));
        return api.getDoctorInfo(getParams);
    }


    public Observable<BaseData> postCertifyUserInfo(ApproveBean approveBean) {
        Map<String, String> getParams = RequestTool.getCommonParams("1617");
        Map<String, String> postParams = new HashMap<>();

        postParams.put("doctor_id", approveBean.doctor_id);
        postParams.put("real_name", approveBean.real_name);
        postParams.put("sex", approveBean.sex);
        postParams.put("photo", approveBean.photo);
        postParams.put("user_type", "1"); //固定为医务工作者
        postParams.put("province", approveBean.province);
        postParams.put("city", approveBean.city);
        postParams.put("shf_image", approveBean.shf_image);
        postParams.put("zhy_image", approveBean.zhy_image);
        postParams.put("hospital_id", approveBean.hospital_id);
        postParams.put("hospital_name", approveBean.hospital_name);
        postParams.put("clinic", approveBean.clinic);
        postParams.put("introduce", approveBean.introduce);
        postParams.put("be_good_at", approveBean.be_good_at);
        postParams.put("subject_first", approveBean.subject_first);
        postParams.put("subject_second", approveBean.subject_second);
        //以下两个不为空就传
        if (!TextUtils.isEmpty(approveBean.subject_phone)) {
            postParams.put("subject_phone", approveBean.subject_phone);
        }
        if (!TextUtils.isEmpty(approveBean.zhch_image)) {
            postParams.put("zhch_image", approveBean.zhch_image);
        }

        getParams.put("sign", RequestTool.getSign(getParams, postParams));
        return api.postCertifyUserInfo(getParams, postParams);
    }

    public Observable<BaseData<List<MessageBoardBean>>> getMessageBoard(String did) {
        Map<String, String> getParams = RequestTool.getCommonParams("1814");
        getParams.put("did", did);
        getParams.put("sign", RequestTool.getSign(getParams));
        return api.getMessageBoard(getParams);
    }

    public Observable<BaseData<MessageBoardBean>> getSetMessageBoard(String did,String type,String isopen,String message) {
        Map<String, String> getParams = RequestTool.getCommonParams("1815");
        Map<String, String> postParams = new HashMap<>();
        postParams.put("did", did);
        postParams.put("type", type);
        postParams.put("isopen", isopen);
        postParams.put("message", message);

        getParams.put("sign", RequestTool.getSign(getParams,postParams));
        return api.getSetMessageBoard(getParams,postParams);
    }
}
