package com.xywy.askforexpert.appcommon.base.interfaze;

/**
 * Created by xugan on 2018/5/25.
 */

public interface IBaseView<T> {
    void onSuccessResultView(T t,String flag);
    void onErrorResultView(T t,String flag,Throwable e);
}
