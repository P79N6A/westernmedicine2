package com.xywy.askforexpert.module.message.healthrecord.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.xywy.retrofit.net.BaseData;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by bailiangjin on 16/6/18.
 */
public class DataUtils {

    @NonNull
    public static BaseData parseData(Object obj) {
        BaseData res = new BaseData();

        if (null == obj || !(obj instanceof String)) {
            res.setCode(Errors.BASE_PARSER_ERROR);
            res.setMsg(Errors.ERROR_MSG);
        } else {
            String response = "";
            response = (String) obj;
            Type mType = null;
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
                    if (mType != null) {
                        Gson gson = new Gson();
                        res.setData(gson.fromJson(response.toString(), mType));
                    } else {
                        res.setData(response.toString());
                    }
                }
            } catch (JSONException e) {
                res.setCode(Errors.BASE_PARSER_ERROR);
                res.setMsg(Errors.ERROR_MSG);
            } catch (JsonSyntaxException e) {
                res.setCode(Errors.BASE_GSON_ERROR);
                res.setMsg(Errors.ERROR_MSG);
            } catch (Exception e) {
                res.setCode(Errors.BASE_PARSER_ERROR);
                res.setMsg(Errors.ERROR_MSG);
            } finally {
                Log.d("NETWORK_TAG", String.format("response builder : code = %d, msg = %s", res.getCode(), res.getMsg()));
            }


        }
        return res;
    }
}
