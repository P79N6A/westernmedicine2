package com.xywy.livevideo.debug.requestservice;

import com.xywy.livevideo.debug.requestservice.interfaze.RequestCallback;
import com.xywy.livevideo.debug.requestservice.interfaze.RequestInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bailiangjin on 2017/2/24.
 */

public class RequestService {

    public static RequestInterface getRequestInterface(){
        return new RequestInterface() {
            @Override
            public <T> void request(String requestUrl, Map<String, String> paramMap, RequestCallback<T> requestCallback) {
                //TODO：医脉、寻医问药分别实现自己的请求方法 回调结果
            }
        };

    }





    public static void startShowRequest(){
        Map<String,String> paramMap= new HashMap<>();
        getRequestInterface().request("http://www.baidu.com",paramMap, new RequestCallback<String>() {
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onError(String errorStr) {

            }
        });
    }
}
