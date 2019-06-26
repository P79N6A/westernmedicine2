package com.xywy.livevideo.common_interface;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/3/7 11:54
 */

public class CommonLiveResponse<T> implements IDataResponse<T> {

    @Override
    public void onReceived(T t) {

    }

    @Override
    public void onError(Throwable e) {

    }
}
