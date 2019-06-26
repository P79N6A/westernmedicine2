package com.xywy.askforexpert.module.discovery.medicine.common;


import com.xywy.askforexpert.BuildConfig;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/3/29 13:09
 */

public class MyConstant {
//    medicine_uid_68258005,medicine_did_68258005
    public static final String HX_ID_PREFIX_DOC = "medicine_did_";
    public static final String HX_ID_PREFIX_USER = "medicine_uid_";
    public static final String HX_SYSTEM_ID="medicine_assistant";//系统消息的环信Id

    public static final String NOT_DEFINED="-100";//未定义字段

    public static final String H5_FORMAL_BASE_URL="http://wkyszs.xywy.com/";
    public static final String SERVER_FORMAL_URL = "http://api.wws.xywy.com";
    public static final String H5_TEST_BASE_URL="http://test.wkyszs.xywy.com/";
    public static final String SERVER_TEST_URL = "http://test.api.wws.xywy.com";

    public static  String H5_BASE_URL;
    public static  String SERVER_URL;

//    public static final int ONLINE_SERVICE_ID = 1;
//    public static final int OFFLINE_SERVICE_ID = 2;
//    public static int SERVICE_ID;
    static {
        if (BuildConfig.isTestServer){
            H5_BASE_URL=H5_TEST_BASE_URL;
            SERVER_URL = SERVER_TEST_URL;
        }else{
            H5_BASE_URL=H5_FORMAL_BASE_URL;
            SERVER_URL = SERVER_FORMAL_URL;
        }

//        if(OffLineService.isOffLine()){
//            SERVICE_ID=OFFLINE_SERVICE_ID;
//        }else{
//            SERVICE_ID=ONLINE_SERVICE_ID;
//        }
    }

//    public static final String REQUEST_SOURCE = "yyzs_doctor";
//    public static final String REQUEST_OS = "android";
//    public static final String REQUEST_PRO = "xywyf32l24WmcqquqqTdhXaMkQ";
//    public static final String MD5_SIGN_KEY_VAULE = "g1eN.uatPE7rHoiC";
}
