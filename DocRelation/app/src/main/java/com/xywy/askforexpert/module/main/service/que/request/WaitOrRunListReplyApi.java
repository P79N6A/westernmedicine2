package com.xywy.askforexpert.module.main.service.que.request;

import com.xywy.askforexpert.appcommon.net.retrofitWrapper.ApiConstants;
import com.xywy.askforexpert.model.base.BaseBean;
import com.xywy.askforexpert.model.questionsquare.QuestionDetailPageBean;
import com.xywy.askforexpert.module.main.service.que.model.QuestionSquareBean;
import com.xywy.askforexpert.module.main.service.que.model.QuestionTieBreak;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by xugan on 2017/12/25.
 */

public interface WaitOrRunListReplyApi {
    @FormUrlEncoded
    @POST("app/1.7/club/doctorApp.interface.php")
    Observable<BaseBean<QuestionSquareBean>> getWaitReplyApi(@QueryMap Map<String,String> getParams, @FieldMap Map<String,String> postParams);

    @FormUrlEncoded
    @POST("app/1.7/club/doctorApp.interface.php")
    Observable<BaseBean<QuestionTieBreak>> getRunListApi(@QueryMap Map<String,String> getParams, @FieldMap Map<String,String> postParams);

}
