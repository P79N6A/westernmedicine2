package com.xywy.askforexpert.module.discovery.medicine.common.net;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by bailiangjin on 2017/4/24.
 */

public interface ApiService {

    /**
     *
     * 示例
     * @param url 全url
     * @param postPramMap post param
     * @return
     */
    @FormUrlEncoded
    @POST()
    Observable<String> test(@Url String url, @FieldMap Map<String, String> postPramMap);


}
