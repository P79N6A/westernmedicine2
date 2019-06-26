package com.xywy.askforexpert.module.message.healthrecord;

import com.xywy.askforexpert.appcommon.interfaces.callback.HttpCallback;
import com.xywy.askforexpert.appcommon.net.BaseHttpUtils;
import com.xywy.askforexpert.appcommon.net.CommonUrl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bailiangjin on 16/6/18.
 */
public class HealthService {
    /**
     * 获取 患者血压数据 1240
     *
     * @param callback
     */
    public static void requestBloodPressure(String patientId, String startTime, String endTime, HttpCallback callback) {
        Map map = new HashMap();
        String curTimeMillisStr = "" + System.currentTimeMillis();
        map.put("timestamp", curTimeMillisStr);
        map.put("end", endTime);
        map.put("start", startTime);
        map.put("bind", patientId);
        map.put("a", "areamedical");
        map.put("m", "patientbloodpressure");
        map.put("patientid", patientId);
        String signKey = curTimeMillisStr + patientId;
        BaseHttpUtils.requestByPost(CommonUrl.CALL_URL, map, signKey, callback);
    }

    /**
     * 获取 患者血糖数据 1241
     *
     * @param callback
     */
    public static void requestBloodSugar(String patientId, String startTime, String endTime, HttpCallback callback) {
        Map map = new HashMap();
        String curTimeMillisStr = "" + System.currentTimeMillis();
        map.put("timestamp", curTimeMillisStr);
        map.put("end", endTime);
        map.put("start", startTime);
        map.put("bind", patientId);
        map.put("a", "areamedical");
        map.put("m", "patientbloodsugar");
        map.put("patientid", patientId);
        String signKey = curTimeMillisStr + patientId;
        BaseHttpUtils.requestByPost(CommonUrl.CALL_URL, map, signKey, callback);
    }

}
