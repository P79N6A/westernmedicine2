package com.xywy.component.datarequest.network;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.xywy.component.datarequest.tool.GzipUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 简单网络请求，是所有网络请求的基类
 * 传递url，返回String
 */
public class GzipRequest extends PostRequest {
    public GzipRequest(String url, Response.Listener<SimpleResponse> listener, Response.ErrorListener errorListener) {
        super( url, listener, errorListener);
        setShouldCache(false);
    }

    public Map<String, String> getHeaders() throws AuthFailureError
    {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Charset", "UTF-8");
        headers.put("Content-Type", "application/x-javascript");
//        headers.put("Accept-Encoding", "gzip,deflate");
        return headers;
    }

    private byte[] data;
    @Override
    public void setBytes(byte[] data) {
        this.data=GzipUtils.compressLog(data);
    }

    public byte[] getBody() throws AuthFailureError
    {
        return data;
    }
    private  String urlDecode(byte[] data){
        String result="";
        String[] params=new String(data, encoding).split("&");
        for (String param:params){
            String[] pair=param.split("=");
            if (pair.length==2){
                try {
                    result+=pair[0]+"="+URLDecoder.decode(pair[1],"UTF-8")+"&";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }

    private  static Charset encoding=Charset.forName("UTF-8");

}
