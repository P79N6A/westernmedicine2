package com.xywy.askforexpert.module.main.service.que.request;

import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.model.base.BaseBean;
import com.xywy.askforexpert.model.questionsquare.QuestionDetailPageBean;
import com.xywy.askforexpert.module.main.service.que.model.QuestionSquareBean;
import com.xywy.askforexpert.module.main.service.que.model.QuestionTieBreak;
import com.xywy.retrofit.RetrofitClient;
import com.xywy.util.MD5Util;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by xugan on 2017/12/25.
 */

public class WaitOrRunListReplyRequest {
    private WaitOrRunListReplyApi api;
    public static WaitOrRunListReplyRequest instance = new WaitOrRunListReplyRequest();
    private WaitOrRunListReplyRequest(){
        api = RetrofitClient.getRetrofit().create(WaitOrRunListReplyApi.class);
    }

    public static WaitOrRunListReplyRequest getInstance(){
        return instance;
    }

    public Observable<BaseBean<QuestionSquareBean>> getWaitReply(String command,String tag,String userId){
        Map<String, String> getParams = new HashMap<>();
        getParams.put("command",command);
        Map<String, String> postParams = new HashMap<>();
        postParams.put("tag",tag);
        postParams.put("userid",userId);
        getParams.put("sign", MD5Util.MD5(userId+tag + Constants.MD5_KEY));
        return api.getWaitReplyApi(getParams,postParams);
    }

    public Observable<BaseBean<QuestionTieBreak>> getRunList(String command, String userId){
        Map<String, String> getParams = new HashMap<>();
        getParams.put("command",command);
        Map<String, String> postParams = new HashMap<>();
        postParams.put("userid",userId);
        getParams.put("sign", MD5Util.MD5(userId + Constants.MD5_KEY));
        return api.getRunListApi(getParams,postParams);
    }
}
