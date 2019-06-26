package com.xywy.askforexpert.module.main.service.que.utils;

import android.text.TextUtils;

import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.SPUtils;
import com.xywy.askforexpert.model.api.BaseResultBean;

/**
 * 问题广场 缓存工具类
 * Created by bailiangjin on 2017/1/12.
 */
public class QuestionSquareCacheUtils {

    private static final String QUESTION_SQUARE_SP_KEY = "QUESTION_SQUARE_SP_KEY";


    protected static String IS_RECOGNIZE_MEDICINE_GUIDE_SHOULD_SHOW_KEY = "IS_RECOGNIZE_MEDICINE_GUIDE_SHOULD_SHOW_KEY";

    private static SPUtils getSP() {
        return SPUtils.createInstance(QUESTION_SQUARE_SP_KEY);
    }

    public static <T extends BaseResultBean> void cacheData(String spKey, T data) {

        if (null == data) {
            LogUtils.e("缓存的数据为null");
            return;
        }
        String jsonStr = GsonUtils.toJson(data);

        LogUtils.d("缓存数据 key=" + spKey + "  json=" + jsonStr);

        getSP().putString(spKey, jsonStr);

    }

    public static String getStringCache(String spKey) {

        if (TextUtils.isEmpty(spKey)) {
            LogUtils.e("缓存的key为null");
            return null;
        }

        LogUtils.d("取缓存key=" + spKey);
        String jsonStr = getSP().getString(spKey);

        LogUtils.d("取缓存k=" + spKey + " value=" + jsonStr);

        return jsonStr;
    }

    public static void removeSp(String spKey) {
        getSP().remove(spKey);
    }

    public static boolean isRecognizeMedicineGuideShouldShow() {
        return getSP().getBoolean(IS_RECOGNIZE_MEDICINE_GUIDE_SHOULD_SHOW_KEY + YMUserService.getCurUserId(),true);
    }

    public static void setRecognizeMedicineGuideShouldShow(boolean isShouldShow) {
        getSP().putBoolean(IS_RECOGNIZE_MEDICINE_GUIDE_SHOULD_SHOW_KEY +YMUserService.getCurUserId(), isShouldShow);
    }


}
