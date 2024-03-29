/**
 * Copyright (c) 2012-2013, Michael Yang 杨福海 (www.yangfuhai.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.tsz.afinal.http;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 参数包裹工具类 stone
 * <p>
 * 使用方法:
 * <p>
 * <pre>
 * AjaxParams params = new AjaxParams();
 * params.put("username", "michael");
 * params.put("password", "123456");
 * params.put("email", "test@tsz.net");
 * params.put("profile_picture", new File("/mnt/sdcard/pic.jpg")); // 上传文件
 * params.put("profile_picture2", inputStream); // 上传数据流
 * params.put("profile_picture3", new ByteArrayInputStream(bytes)); // 提交字节流
 *
 * FinalHttp fh = new FinalHttp();
 * fh.post("http://www.yangfuhai.com", params, new AjaxCallBack(){
 * 		@Override
 *		public void onLoading(long count, long current) {
 *				textView.setText(current+"/"+count);
 *		}
 *
 *		@Override
 *		public void onSuccess(String t) {
 *			textView.setText(t==null?"null":t);
 *		}
 * });
 * </pre>
 */
public class AjaxParams {
    private static String ENCODING = "UTF-8";

    public ConcurrentHashMap<String, String> getUrlParams() {
        return urlParams;
    }

    protected ConcurrentHashMap<String, String> urlParams;

    public ConcurrentHashMap<String, File> getFileParams() {
        return fileParams;
    }

    protected ConcurrentHashMap<String, File> fileParams;

    public AjaxParams() {
        init();
    }

    public AjaxParams(Map<String, String> source) {
        init();

        for(Map.Entry<String, String> entry : source.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    public AjaxParams(String key, String value) {
        init();
        put(key, value);
    }

    public AjaxParams(Object... keysAndValues) {
      init();
      int len = keysAndValues.length;
      if (len % 2 != 0)
        throw new IllegalArgumentException("Supplied arguments must be even");
      for (int i = 0; i < len; i += 2) {
        String key = String.valueOf(keysAndValues[i]);
        String val = String.valueOf(keysAndValues[i + 1]);
        put(key, val);
      }
    }

    public void put(String key, String value){
        if(key != null && value != null) {
            urlParams.put(key, value);
        }
    }

    public void put(String key, File file) throws FileNotFoundException {
        if(key != null && file != null) {
            fileParams.put(key, file);
        }
    }



    public void remove(String key){
        urlParams.remove(key);
        fileParams.remove(key);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for(ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
            if(result.length() > 0)
                result.append("&");

            result.append(entry.getKey());
            result.append("=");
            result.append(entry.getValue());
        }

        for(ConcurrentHashMap.Entry<String, File> entry : fileParams.entrySet()) {
            if(result.length() > 0)
                result.append("&");

            result.append(entry.getKey());
            result.append("=");
            result.append("FILE");
        }

        return result.toString();
    }

    private void init(){
        urlParams = new ConcurrentHashMap<String, String>();
        fileParams = new ConcurrentHashMap<String, File>();
    }

    protected List<BasicNameValuePair> getParamsList() {
        List<BasicNameValuePair> lparams = new LinkedList<BasicNameValuePair>();

        for(ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
            lparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        return lparams;
    }

    public String getParamString() {
        return URLEncodedUtils.format(getParamsList(), ENCODING);
    }

}