package com.xywy.askforexpert.module.main.service.que;

import android.text.TextUtils;

import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.interfaces.callback.HttpCallback;
import com.xywy.askforexpert.appcommon.net.BaseHttpUtils;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.ApiConstants;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.RetrofitServiceProvider;
import com.xywy.askforexpert.model.questionsquare.SendMedicineResultBean;
import com.xywy.datarequestlibrary.XywyDataRequestApi;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;


/**
 * Created by bailiangjin on 2016/12/12.
 */

public class QuestionService {

    public static void skip(String id, String type, final HttpCallback httpCallback) {

        String userId = YMApplication.getLoginInfo().getData().getPid();
        Map params = new HashMap();
        params.put("userid", userId);
        params.put("id", id);
        params.put("act", type);
        String signKey = userId + id + type;

        BaseHttpUtils.requestByPost(CommonUrl.QUE_SKIP, params, signKey, new HttpCallback() {
            @Override
            public void onSuccess(Object obj) {
                httpCallback.onSuccess(obj);
            }

            @Override
            public void onFailed(Throwable throwable, int errorNo, String strMsg) {
                httpCallback.onFailed(throwable, errorNo, strMsg);
            }
        });


    }

    /**
     * /**
     * <p>
     * test.yimai.api.xywy.com/app/1.6/club/doctorApp.interface.php?command=yaoInfo
     * 推送药品接口
     * post参数
     * <p>
     * userid   医生id
     * qid  帖子id
     * yaoid   药id
     * yaoimg   药品图片地址
     * yaotitle  药品标题
     * <p>
     * sign   验证规则同其他的相同
     * <p>
     * md5(userid+qid+yaoid  +  key)
     *
     * @param userId
     * @param qid
     * @param yaoid
     * @param yaotitle
     * @param yaoimg
     * @param subscriber
     */
    public static void sendMedicineToUser(String userId, String qid, String yaoid, String yaotitle, String yaoimg, Subscriber<SendMedicineResultBean> subscriber) {

        Map<String, String> getParam = new HashMap<>();
        getParam.put("command", "yaoInfo");

        Map<String, String> postParam = new HashMap<>();
        postParam.put("userid", userId);
        postParam.put("qid", TextUtils.isEmpty(qid) ? "" : qid);
        postParam.put("yaoid", yaoid);
        postParam.put("yaotitle", yaotitle);
        postParam.put("yaoimg", yaoimg);
        RetrofitServiceProvider.addSign(postParam, userId + qid + yaoid);

        XywyDataRequestApi.getInstance().postRequestCommon(BuildConfig.APP_BASE_URL_YM, ApiConstants.CLUB_URL, getParam, postParam, SendMedicineResultBean.class, subscriber);
    }


}
