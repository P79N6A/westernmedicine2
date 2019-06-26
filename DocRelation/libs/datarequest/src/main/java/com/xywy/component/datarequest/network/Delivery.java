package com.xywy.component.datarequest.network;

import android.os.Handler;

import com.android.volley.ExecutorDelivery;
import com.android.volley.Request;
import com.android.volley.Response;

import java.util.concurrent.ExecutorService;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/8/5 11:07
 * 改写responsedelivery传递intermediate的值
 */
public class Delivery extends ExecutorDelivery {
    public Delivery(Handler handler) {
        super(handler);
    }

    public Delivery(ExecutorService executorService) {
        super(executorService);
    }

    @Override
    public void postResponse(Request<?> request, Response<?> response, Runnable runnable) {
        Object result = response.result;
        if (result instanceof  SimpleResponse){
            ((SimpleResponse) result).intermediate=response.intermediate;
        }
        super.postResponse(request, response, runnable);
    }
}
