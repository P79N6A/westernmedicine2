package com.xywy.askforexpert.module.discovery.medicine.module.appupdate;

import com.xywy.askforexpert.module.discovery.medicine.common.ApiConstants;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity.AppVersion;
import com.xywy.retrofit.net.BaseData;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by xgxg on 2017/11/9.
 */

public interface UpdateAppApi {
    @GET(ApiConstants.CHECK_VERSIONS)
    Observable<BaseData<AppVersion>> checkVersion(@QueryMap Map<String, String> getParams);
}
