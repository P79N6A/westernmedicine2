package com.xywy.datarequestlibrary.paramtools;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bailiangjin on 2017/2/28.
 */

public class CommonParamUtils {

    /**
     * 生成getUrl后缀
     * <p>
     * 格式 ?key1=value1&key2=value2&key3=value3
     *
     * @param getParam
     * @return
     */
    public static String generateGetUrlString(Map<String, String> getParam) {

        if (null == getParam || getParam.isEmpty()) {
            return "";
        }

        String postfixGetUrl = "?";
        List<Map.Entry<String, String>> list
                = new ArrayList<Map.Entry<String, String>>(getParam.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            //升序排序
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                postfixGetUrl += list.get(i).getKey() + "=" + list.get(i).getValue();
            } else {
                postfixGetUrl += list.get(i).getKey() + "=" + list.get(i).getValue() +
                        "&";
            }
        }
        return postfixGetUrl;
    }

    /**
     * 计算sig,针对中间层    get形式
     * 30002
     *
     * @param params
     * @param secretKey
     * @return
     */
    public static String getSign(Map<String, String> params, String secretKey) {

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
        //带汉字的请求加密用
        sign = MD5.generateSign(sign + secretKey);
        return sign;
    }


    /**
     * 计算sig,针对中间层    post multipart 形式
     *
     * @param getParams
     * @param postParams
     * @param secretKey
     * @return
     */
    public static String getSign(Map<String, String> getParams, Map<String, String> postParams, String secretKey) {

        Map<String, String> newGetParams = new HashMap<>();
        newGetParams.putAll(getParams);
        if (postParams != null) newGetParams.putAll(postParams);
        return getSign(newGetParams, secretKey);
    }

}
