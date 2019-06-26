package com.xywy.retrofit.net;

import com.xywy.util.ReflectUtils;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/11/18 15:35
 */

public class RetrofitCache {
    public static final String  CacheStr= "NetResponseCache";
    /**
     * 判断网络回调是否属于缓存
     * @param data
     * @return
     */

    public static boolean isResponseFromCache(Object data){
        if (CacheStr.equals(ReflectUtils.getStringField(data,"msg"))){
            return true;
        }
        return false;
    }

    public static boolean isResponseFromCache(String msg){
        if (CacheStr.equals(msg)){
            return true;
        }
        return false;
    }

}
