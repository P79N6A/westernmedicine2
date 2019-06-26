package com.xywy.livevideo.common_interface;

/**
 * Created by bobby on 17/2/24.
 */

public interface IDataResponse<T> {


    public void onReceived(T t);

    public void onError(Throwable e);
}
