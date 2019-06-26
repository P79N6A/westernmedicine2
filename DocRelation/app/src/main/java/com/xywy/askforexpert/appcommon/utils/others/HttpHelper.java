package com.xywy.askforexpert.appcommon.utils.others;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * 废弃的网络请求方法
 */
@Deprecated
public class HttpHelper {

    /**
     * 从网络获取数据 （get）
     *
     * @param url
     * @return
     * @throws IOException
     * @throws IllegalStateException
     */

    public static String doGet(String url) {
        String result = null;
        try {
            result = request(url);
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    private static String request(String url) throws IllegalStateException,
            IOException {
        return parseStringFromEntity(getStream(url));
    }

    public static String parseStringFromEntity(InputStream input)
            throws IllegalStateException, IOException {
        String result = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line = null;
        StringBuffer sb = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        result = sb.toString();
        return result;
    }

    public static String doPost(String url, List<BasicNameValuePair> pairs) {
        // 生成一个HttpPost对象
        HttpPost postRequest = new HttpPost(url);
        HttpEntity entity = null;
        HttpResponse response = null;
        String result = "";
        try {
            entity = new UrlEncodedFormEntity(pairs, "UTF_8");
            postRequest.setEntity(entity);
            response = new DefaultHttpClient().execute(postRequest);
            result = getResponseText(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getResponseText(HttpResponse response) {
        HttpEntity responseEntity = response.getEntity();
        InputStream input = null;
        String result = null;
        try {
            input = responseEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    input));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public static String getNetworkType(Context cxt) {
        ConnectivityManager connectivityManager = (ConnectivityManager) cxt
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobNetInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobNetInfo != null && mobNetInfo.getExtraInfo() != null) {
            return mobNetInfo.getExtraInfo();
        }
        return "";
    }

    /***
     * 得到inputStream
     *
     * @param url
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    public static InputStream getStream(String url)
            throws IllegalStateException, IOException {
        HttpResponse response = null;

        int timeoutConnection = 10000;
        int timeoutSocket = 5000;

        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters,
                timeoutConnection);
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
        httpClient.getParams().setParameter("http.socket.timeout", new Integer(30000));
        HttpGet getRequest = new HttpGet(url);
        response = httpClient.execute(getRequest);

        return response.getEntity().getContent();
    }

    /***
     * 得到HttpEntity
     *
     * @param url
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    public static HttpEntity getHttpEntity(String url)
            throws IllegalStateException, IOException {
        HttpResponse response = null;

        int timeoutConnection = 10000;
        int timeoutSocket = 5000;

        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters,
                timeoutConnection);
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
        HttpGet getRequest = new HttpGet(url);
        response = httpClient.execute(getRequest);
        return response.getEntity();
    }
}
