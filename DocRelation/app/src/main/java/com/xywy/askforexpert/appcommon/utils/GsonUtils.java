package com.xywy.askforexpert.appcommon.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Gson 工具类 stone
 *
 * @author blj
 */
public class GsonUtils {
    private static Gson gson = new Gson();

    private GsonUtils() {

    }

//    public static <T> T toObj(String json) {
//        if (TextUtils.isEmpty(json) ) {
//            return null;
//        }
//            Type type = new TypeToken<T>() {}.getType();
//        return (T) gson.fromJson(json, type.getClass());
//    }

    /**
     * 将字符串转成对象
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T toObj(String json, Class<T> clazz) {

        if (TextUtils.isEmpty(json) || null == clazz) {
            return null;
        }
        try {
            return gson.fromJson(json, clazz);
        }catch (Exception e){
            e.printStackTrace();
            LogUtils.e("服务端接口json数据格式异常:"+e.getMessage());
            return null;
        }

    }

    /**
     * 将对象转成json字符串
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        if (null == obj) {
            return null;
        }
        return gson.toJson(obj);
    }



    /**
     * 将json array 字符串转换为  List stone
     *
     * @param jsonString json array 字符串
     * @return List<T>
     */
    public static <T> List<T> parsJsonArrayStr2List(String jsonString,Class<T> cls){
        List<T> list = new ArrayList<T>();
        try {
            Gson gson = new Gson();
            JsonArray arry = new JsonParser().parse(jsonString).getAsJsonArray();
            for (JsonElement jsonElement : arry) {
                list.add(gson.fromJson(jsonElement, cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Gson getGson() {
        return gson;
    }


}
