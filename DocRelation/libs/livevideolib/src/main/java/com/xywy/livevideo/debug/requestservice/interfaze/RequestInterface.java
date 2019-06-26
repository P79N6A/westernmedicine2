package com.xywy.livevideo.debug.requestservice.interfaze;

import java.util.Map;

/**
 * Created by bailiangjin on 2017/2/24.
 */

public interface RequestInterface {
     <T>void request(String requestUrl, Map<String, String> paramMap, RequestCallback<T> requestCallback);
}
