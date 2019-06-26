package com.xywy.askforexpert.module.discovery.medicine.common;

import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.YMApplication;
import com.xywy.util.LogUtils;
import com.xywy.util.MD5Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bobby on 17/3/29.
 */

public class RequestTool {

//    public static final String REQUEST_SOURCE = "yyzs_doctor";
    public static final String REQUEST_SOURCE = (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID))?"yyzs_ym":"yimai";
    public static final String REQUEST_OS = "android";
//    public static final String REQUEST_PRO = "xywyf32l24WmcqquqqTdhXaMkQ";
    public static final String REQUEST_PRO = (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID))?"xywyf32l24WmcqquqqTdhXaWkQ":"xywyf32l24WmcqquqqTdhXZ4kg";
//    public static final String MD5_SIGN_KEY_VAULE = "g1eN.uatPE7rHoiC";
    public static final String MD5_SIGN_KEY_VAULE = (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID))?"6F#KwNCrg.2BDc#+":"D@oQ8A*wte.bHFfA";
    public static final String MIDDLE_WIRE_COMMON_GET_PARAM_STR = "source="+REQUEST_SOURCE+"&pro="+REQUEST_PRO+"&os=android";

    public static String getSign(Map<String, String> getpParams) {
        return getSign(getpParams, MD5_SIGN_KEY_VAULE);
    }

    public static String getSign(Map<String, String> getpParams,Map<String, String> postParams) {
        return getSign(getpParams,postParams,MD5_SIGN_KEY_VAULE);
    }

    /**
     * 计算sig,针对中间层    get形式
     * @param params
     * @param secretKey
     * @return
     */
    private static String getSign(Map<String, String> params, String secretKey) {
        addCommonGetParam(params);
        String sign = "";
        List<Map.Entry<String, String>> list
                = new ArrayList<Map.Entry<String, String>>(params.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            //升序排序
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                sign += list.get(i).getKey() + "=" + list.get(i).getValue();
            } else {
                sign += list.get(i).getKey() + "=" + list.get(i).getValue() +
                        "&";
            }
        }
        LogUtils.e("TAG_SUBMIT-sign:"+sign + secretKey);
        //带汉字的请求加密用
        sign = MD5Util.generateSign(sign + secretKey);
        return sign;
    }

    /**
     * 计算sig,针对中间层    post multipart 形式
     * @param getParams
     * @param postParams
     * @param secretKey
     * @return
     */
    private static String getSign(Map<String, String> getParams, Map<String, String> postParams, String secretKey) {
//        addCommonGetParam(getParams);
        Map<String, String> newGetParams = new HashMap<>();
        newGetParams.putAll(getParams);
        if (postParams != null){
            newGetParams.putAll(postParams);
        }
        return  getSign(newGetParams, secretKey);
    }

    private static Map<String, String> addCommonGetParam( Map<String, String> getParam) {
        getParam.put("source", REQUEST_SOURCE);
        getParam.put("os", REQUEST_OS);
        getParam.put("pro", REQUEST_PRO);
//        if(OffLineService.isOffLine()){
//            getParam.put("service_id", MyConstant.SERVICE_ID+"");
//        }
        //新的医脉加用药的这个app,不需要sevice_id
//        getParam.put("service_id", BuildConfig.service_id+"");
        return getParam;
    }

    public static Map<String, String> addServiceId( Map<String, String> getParam) {
//        getParam.put("service_id", MyConstant.SERVICE_ID+"");
        //新的医脉加用药的这个app,不需要sevice_id
//        getParam.put("service_id", BuildConfig.service_id+"");
        return getParam;
    }

    public static Map<String, String> getCommonParams(String api) {
        return getCommonParmas(api, "1.0");
    }

    private static Map<String, String> getCommonParmas(String api, String version) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("api", api);
        getParams.put("version", version);
        addCommonGetParam(getParams);
        return getParams;
    }


    public static String getUrlWithQueryString(String url, Map<String,String> params) {
        if (params != null) {
            String paramString ="";
            for(String s:params.keySet()){
                paramString=paramString+s+"="+params.get(s)+"&";
            }
            url += "?" + paramString;
        }
        return url;
    }
}
