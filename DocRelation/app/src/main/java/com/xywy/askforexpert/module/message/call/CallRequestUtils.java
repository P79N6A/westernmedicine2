package com.xywy.askforexpert.module.message.call;

import com.xywy.askforexpert.appcommon.interfaces.callback.HttpCallback;
import com.xywy.askforexpert.appcommon.net.BaseHttpUtils;
import com.xywy.askforexpert.appcommon.net.CommonUrl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bailiangjin on 16/5/9.
 */
public class CallRequestUtils {

    /**
     * 获取 试题分类列表 1261
     *
     * @param callback
     */
    public static void call(String patientId, HttpCallback callback) {
        Map map = new HashMap();
        String curTimemillisStr = "" + System.currentTimeMillis();
        map.put("timestamp", curTimemillisStr);
        map.put("bind", patientId);
        map.put("a", "areamedical");
        map.put("m", "patientphone");
        map.put("patientid", patientId);
        String signKey = curTimemillisStr + patientId;
        BaseHttpUtils.requestByPost(CommonUrl.CALL_URL, map, signKey, callback);
    }


}
