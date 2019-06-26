package com.xywy.retrofit.cache;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Response;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/9/7 16:41
 */
public interface ICache {

    String get(Request request);

    boolean put(Response response) throws IOException;

    boolean remove(Request req);

    void clear();

}
