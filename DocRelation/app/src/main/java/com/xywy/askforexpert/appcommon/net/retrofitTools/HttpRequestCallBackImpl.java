package com.xywy.askforexpert.appcommon.net.retrofitTools;

import android.widget.Toast;

import com.xywy.askforexpert.YMApplication;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/6/27 9:23
 */

/**
 * Retrofit 请求回调实现类，在实际代码中可只实现 onSuccess 方法
 *
 * @author Jack Fang
 */
@Deprecated
public abstract class HttpRequestCallBackImpl<D> implements HttpRequestCallBack<D> {
    @Override
    public void onFailure(RequestState state, String msg) {
        Toast.makeText(YMApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }
}
