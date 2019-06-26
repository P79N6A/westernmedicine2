package com.xywy.askforexpert.appcommon.net;

import com.xywy.askforexpert.appcommon.interfaces.callback.HttpCallback;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by bailiangjin on 16/5/9.
 */
@Deprecated
public class BaseHttpUtils {

    public static void requestByPost(String requestUrl, Map paramMap, String signKey, final HttpCallback callback) {

        AjaxParams params = new AjaxParams();

        Iterator iterator = paramMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            params.put(key, value);
        }
        //添加sign参数
        String sign = generateSign(signKey);
        params.put("sign", sign);

        LogUtils.d("param:" + params.toString());

        FinalHttp fh = new FinalHttp();
        LogUtils.d("answer url = " + requestUrl + "?" + params.toString());
        fh.post(requestUrl, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                LogUtils.e("responseData:" + t.toString());
                callback.onSuccess(t);
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                callback.onFailed(t, errorNo, strMsg);
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    public static String generateSign(String signKey) {
        return MD5Util.MD5(signKey + Constants.MD5_KEY);
    }


}
