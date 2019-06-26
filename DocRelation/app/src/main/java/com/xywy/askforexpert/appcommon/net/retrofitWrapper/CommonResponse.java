package com.xywy.askforexpert.appcommon.net.retrofitWrapper;

import android.content.Context;
import android.widget.Toast;

import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.retrofit.RetrofitClient;

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
        contextRef = new WeakReference<>(YMApplication.getInstance().getApplicationContext());
    }


    public CommonResponse(Context con) {
        contextRef = new WeakReference<>(con);
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
//        if (contextRef.get() == null || !(contextRef.get() instanceof Context)) {
//            return;
//        }
        LogUtils.ex(e);
//        LogUtils.e("操作异常:" + e.getMessage());
//        LogUtils.e("操作异常:" + e.getClass());
        //先处理系统异常，再处理自己构造的ApiException
        if (e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof HttpException) {
//            Toast.makeText(contextRef.get().getApplicationContext(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(contextRef.get().getApplicationContext(), "异常提示:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Toast.makeText(contextRef.get().getApplicationContext(), "网络开了个小差，请返回重试一下吧", Toast.LENGTH_SHORT).show();
//            ToastUtils.shortToast("异常提示:" + e.getMessage());
        }

    }

}
