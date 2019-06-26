package com.xywy.askforexpert.module.message.notice.request;

import com.xywy.askforexpert.model.notice.Notice;
import com.xywy.retrofit.net.BaseData;

import java.util.List;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by xugan on 2018/5/25.
 * 获取网站公告相关的接口
 */

public interface GetNoticeListApi {
    @FormUrlEncoded
    @POST("api.php/yimai/notice/getnoticelist")
    Observable<BaseData<List<Notice>>> getNoticeList(@QueryMap Map<String, String> getParams, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api.php/yimai/notice/getnoticesy")
    Observable<BaseData<Notice>> getNewNotice(@QueryMap Map<String, String> getParams, @FieldMap Map<String, String> map);

}
