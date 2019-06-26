package com.xywy.retrofit.demo;

import android.util.Log;
import android.widget.Toast;

import com.xywy.retrofit.RetrofitClient;
import com.xywy.util.L;
import com.xywy.util.LogUtils;
import com.xywy.util.T;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by bobby on 16/6/15.
 * Subscriber的base类和代理类
 */
public  class BaseRetrofitResponse<M> extends Subscriber<M> {

    Subscriber<? super M> subscriber;
    public BaseRetrofitResponse(Subscriber<? super M> subscriber) {
        super(subscriber);
        this.subscriber=subscriber;
    }

    public BaseRetrofitResponse() {
        this(null);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (subscriber!=null)
            subscriber.onStart();
    }

    @Override
    public void onCompleted() {
        if (subscriber!=null)
            subscriber.onCompleted();
    }

    @Override
    public void onNext(M m) {
        try{
            if (subscriber!=null)
                subscriber.onNext(m);
        }catch (Exception e){
            onError(e);
        }
    }

    @Override
    public void onError(Throwable e) {
        L.ex(e);
        //先处理系统异常，再处理自己构造的ApiException
        if (e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof HttpException) {
            Toast.makeText(RetrofitClient.getContext(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(RetrofitClient.getContext(), "异常提示:" + e.getMessage(), Toast.LENGTH_SHORT).show();
//            T.showShort(RetrofitClient.getContext(),"异常提示:" + e.getMessage());
            LogUtils.e(e.getMessage());
        }
        if (subscriber!=null)
            subscriber.onError(e);
    }

}
