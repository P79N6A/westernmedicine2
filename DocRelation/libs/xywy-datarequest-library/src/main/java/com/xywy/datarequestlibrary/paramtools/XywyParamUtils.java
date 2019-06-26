package com.xywy.datarequestlibrary.paramtools;


import com.xywy.datarequestlibrary.XywyDataRequestApi;

import java.util.Map;

/**
 * Created by bailiangjin on 2017/2/28.
 */

public class XywyParamUtils {



    public static Map<String, String> addMustParam(Map<String, String> getParamMap, String apiCode, String version) {
        XywyDataRequestApi.getInstance().checkAppParamInit();
        addCommonParams(getParamMap);
        addApiCodeAndVersion(getParamMap, apiCode, version);
        return getParamMap;
    }

    public static String getSign(Map<String, String> getParams) {
        return CommonParamUtils.getSign(getParams, XywyDataRequestApi.getInstance().getCommonRequestParam().getSignKey());
    }

    public static String getSign(Map<String, String> getParams, Map<String, String> postParams) {
        return CommonParamUtils.getSign(getParams, postParams, XywyDataRequestApi.getInstance().getCommonRequestParam().getSignKey());
    }

    private static Map<String, String> addApiCodeAndVersion(Map<String, String> getParamMap, String apiCode, String version) {
        getParamMap.put("api", apiCode);
        getParamMap.put("version", version);
        return getParamMap;
    }

    private static Map<String, String> addCommonParams(Map<String, String> getParamMap) {
        getParamMap.put("source", XywyDataRequestApi.getInstance().getCommonRequestParam().getSource());
        getParamMap.put("pro", XywyDataRequestApi.getInstance().getCommonRequestParam().getPro());
        getParamMap.put("os", XywyDataRequestApi.getInstance().getCommonRequestParam().getOs());
        return getParamMap;
    }




}
