package com.xywy.livevideo.common_interface;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by bobby on 17/2/24.
 */

public interface IDataRequest {
    Map<String, String> getCommonGetParams(String apiCode);

    public   Object post(String url, String method, Map<String, String> getParms, Map<String, String> postParms, final IDataResponse iResp, final Type type, Object flag, boolean haveCache);
    public void get(String url, String method, Map<String, String> getParms, Map<String, String> postParms, final IDataResponse iResp, final Type type, Object flag, boolean haveCache);
    public void upload(String url, String method, Map<String, File> fileParams, Map<String, String> getParms, Map<String, String> postParms, final IDataResponse iResp, final Type type, Object flag, boolean haveCache);
    public void cancel(Object cancelObject);
}
