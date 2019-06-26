package com.xywy.component.datarequest.neworkWrapper;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.xywy.component.datarequest.common.Constants;
import com.xywy.component.datarequest.network.GzipRequest;
import com.xywy.component.datarequest.network.MLRequest;
import com.xywy.component.datarequest.network.MultipartRequest;
import com.xywy.component.datarequest.network.PostRequest;
import com.xywy.component.datarequest.network.RequestManager;
import com.xywy.component.datarequest.network.SimpleRequest;
import com.xywy.component.datarequest.network.SimpleResponse;
import com.xywy.component.datarequest.tool.JSONTools;
import com.xywy.component.datarequest.uploadLog.UploadLogClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据请求的封装
 */
public class DataRequestWrapper {

    public SimpleRequest getRequest() {
        return mRequest;
    }

    //最后生成的那个request
    private SimpleRequest mRequest;
    //错误情况监听，最后转换成BaseData
    private ErrorListener mErrorListener;
    //成功情况监听，最后转换成BaseData
    private SuccessListener mSuccessListener;
    //以下是可以配置的信息
    //暴露给上层的监听
    private String mUrl;
    private IDataResponse mDataResponse;
    private Object mTag;
    private Type mType;

    private DataRequestWrapper(Builder b) {
        buildUrl(b);
        buildRequest(b);
        if (b.mResponse != null) {
            mDataResponse = (b.mResponse);
        }
        if (mRequest != null) {
            mRequest.setParams(b.mParams);

            if (b.mFiles != null && b.mFiles.size() > 0) {
                Map<String, File> files = b.mFiles;
                for (String key : files.keySet()) {
                    mRequest.setFile(key, files.get(key));
                }
            }

            if (b.mBitmap != null && b.mBitmap.size() > 0) {
                Map<String, Bitmap> bitmap = b.mBitmap;
                for (String key : bitmap.keySet()) {
                    mRequest.setBitmap(key, bitmap.get(key));
                }
            }

            if (b.mData != null) {
                mRequest.setBytes(b.mData);
            }

            mRequest.setCacheEntity(b.mCache);
        }

        mType = b.mType;
        mTag = b.mTag;
        RequestManager.addRequest(mRequest, mTag);
    }

    private SimpleRequest buildRequest(Builder b) {
        mSuccessListener = new SuccessListener();
        mErrorListener = new ErrorListener();
        switch (b.mMethod) {
            case GET:
                mRequest = new SimpleRequest(Request.Method.GET, mUrl, mSuccessListener, mErrorListener);
                break;
            case POST:
                mRequest = new PostRequest(mUrl, mSuccessListener, mErrorListener);
                break;
            case MULTIPART:
                mRequest = new MultipartRequest(mUrl, mSuccessListener, mErrorListener);
                break;
            case Gzip:
                mRequest = new GzipRequest(mUrl, mSuccessListener, mErrorListener);
                break;
            case MAXLEAP:
                mRequest = new MLRequest(mUrl, mSuccessListener, mErrorListener);
                break;
            default:
                mRequest = new SimpleRequest(Request.Method.GET, mUrl, mSuccessListener, mErrorListener);
                break;
        }

        //设置header
        if (b.mHTTPHeader != null) {
            mRequest.setmHttpHeader(b.mHTTPHeader);
        }

        Log.d(Constants.NetworkTag, "url request : " + mUrl);
        Log.d(Constants.NetworkTag, "post params : " + b.mParams);
        Log.d(Constants.NetworkTag, "files : " + b.mFiles);

        return mRequest;
    }

    private String buildUrl(Builder b) {
        StringBuilder sb = new StringBuilder(b.mUrl);
        if (!TextUtils.isEmpty(b.mUrlMethod)) {
            if (sb.length() > 0 && sb.charAt(sb.length() - 1) != '/') {
                sb.append('/');
            }
            sb.append(b.mUrlMethod);
            if (sb.length() > 0 && sb.charAt(sb.length() - 1) != '?') {
                sb.append('?');
            }
        }


        if (b.mHeader != null && b.mHeader.size() > 0) {

            try {

                Map<String, String> param = b.mHeader;
                for (String key : param.keySet()) {
                    sb.append(key);
                    sb.append('=');
                    //编码
                    String encodeValue = "";
                    encodeValue = URLEncoder.encode(param.get(key), "utf-8");

                    sb.append(encodeValue);
                    sb.append('&');
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            int length = sb.length();
            if (length > 0) {
                sb.deleteCharAt(length - 1);
            }
        }
        mUrl = sb.toString();

        return mUrl;
    }

    public enum DataRequestMethod {
        GET,
        POST,
        MULTIPART,
        Gzip,
        MAXLEAP
    }

    public static class Builder {

        private DataRequestMethod mMethod;
        private String mUrl;
        private String mUrlMethod;
        private IDataResponse mResponse;
        private Map<String, String> mHeader;
        private Map<String, String> mParams;
        private Map<String, String> mHTTPHeader;
        private Map<String, File> mFiles;
        private Map<String, Bitmap> mBitmap;
        private Type mType;
        private boolean mCache;
        private Object mTag;
        private byte[] mData;

        public Builder(DataRequestMethod method, String url, IDataResponse response) {
            this.mMethod = method;
            this.mUrl = url;
            this.mResponse = response;
        }

        public DataRequestWrapper build() { // 构建，返回一个新对象
            return new DataRequestWrapper(this);
        }

        public Builder urlMethod(String val) {
            this.mUrlMethod = val;
            return this;
        }

        public Builder header(Map<String, String> val) {
            this.mHeader = val;
            return this;
        }

        public Builder param(Map<String, String> val) {
            this.mParams = val;
            return this;
        }

        public Builder httpHeader(Map<String, String> val) {
            this.mHTTPHeader = val;
            return this;
        }

        public Builder file(Map<String, File> val) {
            this.mFiles = val;
            return this;
        }

        public Builder bitmap(Map<String, Bitmap> val) {
            this.mBitmap = val;
            return this;
        }

        public Builder gzipData(byte[] data) {
            this.mData = data;
            return this;
        }

        public Builder response(IDataResponse res) {
            this.mResponse = res;
            return this;
        }

        public Builder type(Type val) {
            this.mType = val;
            return this;
        }

        public Builder cache(boolean val) {
            this.mCache = val;
            return this;
        }

        public Builder tag(Object val) {
            this.mTag = val;
            return this;
        }

    }

    public class ErrorListener implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError error) {

            Log.d(Constants.NetworkTag, "url response error: " + error.toString());
            BaseData res = new BaseData();
            res.setCode(-1);
            res.setMsg("network error");
            if (mTag instanceof String) {
                res.setTag((String) mTag);//临时用于判断回传值用的tag
            }
            UploadLogClient.net(mRequest.getUrl(), res.getCode() + "");
            if (mDataResponse != null) {
                mDataResponse.onResponse(res);
            }
        }
    }

    public class SuccessListener implements Response.Listener {

        @Override
        public void onResponse(Object response) {

            Log.d(Constants.NetworkTag, "url response: " + response.toString());
            BaseData res = new BaseData();
            if (mTag instanceof String) {
                res.setTag((String) mTag);//临时用于判断回传值用的tag
            }
            if (response instanceof SimpleResponse) {
                SimpleResponse sres = (SimpleResponse) response;
                if (sres != null) {
                    res.setIntermediate(sres.intermediate);
                }
            }
            try {
                JSONObject root = JSONTools.buildJsonObject(response.toString());
                if (root != null) {
                    int code = root.getInt("code");
                    String msg = root.getString("msg");
                    if (root.has("total")) {
                        int total = root.getInt("total");
                        res.setTotal(total);
                    }
                    res.setCode(code);
                    if (TextUtils.isEmpty(msg)) {
                        res.setMsg(Errors.BASE_NO_MSG);
                    } else {
                        res.setMsg(msg);
                    }
                    if (mRequest.getTag() != null && mRequest.getTag() instanceof String) {
                        res.setTag(mRequest.getTag().toString());
                    }
                    if (mType != null) {
                        Gson gson = new Gson();
                        res.setData(gson.fromJson(response.toString(), mType));
                    } else {
                        res.setData(response.toString());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                res.setCode(Errors.BASE_PARSER_ERROR);
                res.setMsg(Errors.ERROR_MSG);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                res.setCode(Errors.BASE_GSON_ERROR);
                res.setMsg(Errors.ERROR_MSG);
            } catch (Exception e) {
                e.printStackTrace();
                res.setCode(Errors.BASE_PARSER_ERROR);
                res.setMsg(Errors.ERROR_MSG);
            } finally {

                Log.d(Constants.NetworkTag, String.format(" response builder : code = %d, msg = %s", res.getCode(), res.getMsg()));
                if (!res.isIntermediate() && res.getCode() != Errors.BASE_NO_ERROR) {
                    UploadLogClient.net(mRequest.getUrl(), res.getCode() + "");
                }
                if (mDataResponse != null) {
                    if (RequestManager.isDebug()) {
                        mDataResponse.onResponse(res);
                    } else {
                        try {
                            mDataResponse.onResponse(res);
                        } catch (Exception e) {
                            UploadLogClient.netCrash(mRequest.getUrl(), res.getCode() + "", e);
                        }
                    }
                }
            }
        }
    }
}
