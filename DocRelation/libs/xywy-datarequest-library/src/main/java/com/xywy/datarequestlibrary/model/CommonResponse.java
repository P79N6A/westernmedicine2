package com.xywy.datarequestlibrary.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.xywy.datarequestlibrary.XywyDataRequestApi;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by bobby on 16/6/15.
 * 异步消息回调的统一错误处理类，现在改为使用 SubscriberInterceptor 拦截返回的请求
 * 此处可改为空实现
 */
public abstract class CommonResponse<T> extends Subscriber<T> {

    private WeakReference<Context> contextRef;

    public CommonResponse() {
        contextRef = new WeakReference<>(XywyDataRequestApi.getInstance().getAppContext());
    }


    public CommonResponse(Context con) {
        contextRef = new WeakReference<>(con);
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        Log.e(this.getClass().getName(),"操作异常:"+e.getMessage());
        //先处理系统异常，再处理自己构造的ApiException
        if (e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof HttpException) {
            //Toast.makeText(XywyDataRequestApi.getInstance().getAppContext(),"网络中断，请检查您的网络状态",Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(XywyDataRequestApi.getInstance().getAppContext(),"异常提示:" + e.getMessage(),Toast.LENGTH_SHORT).show();
            Toast.makeText(contextRef.get().getApplicationContext(), "网络开了个小差，请返回重试一下吧", Toast.LENGTH_SHORT).show();
        }

    }

}
