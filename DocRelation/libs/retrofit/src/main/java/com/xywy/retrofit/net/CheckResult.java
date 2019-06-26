package com.xywy.retrofit.net;

import com.google.gson.Gson;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/3/7 18:10
 */
public class CheckResult {
    public static void checkNetReturn (String temp) throws ApiException{
        BaseData result = new Gson().fromJson(temp, BaseData.class);
        int code = result.getCode();
//            int code = Integer.valueOf(ReflectUtils.getStringField(data, "code"));
        //code=20000是否为正确返回值，待定
        if (code != 0 && code != 10000) {
            throw new ApiException(code, result.getMsg());
        }
    }
}
