package com.xywy.askforexpert.module.main.home;

import android.text.TextUtils;

import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.SPUtils;
import com.xywy.askforexpert.model.api.BaseResultBean;
import com.xywy.askforexpert.model.main.NewsListPageBean;
import com.xywy.askforexpert.model.main.SubscribeTitleListBean;

/**
 * Home 页缓存工具类
 * Created by bailiangjin on 2017/1/12.
 */
public class HomePageCacheUtils {

    private static final String HOME_TAB_SP_KEY = "HOME_TAB_SP_KEY";

    protected static String NEWS_LIST_INFO_CACHE_KEY = "NEWS_LIST_INFO_CACHE_KEY";

    protected static String TAB_TITLE_INFO_CACHE_KEY = "TAB_TITLE_INFO_CACHE_KEY";

    protected static String IS_MEDIA_GUIDE_SHOULD_SHOW_KEY = "IS_MEDIA_GUIDE_SHOULD_SHOW_KEY";

    private static SPUtils getHomeTabSP() {
        return SPUtils.createInstance(HOME_TAB_SP_KEY);
    }

    public static <T extends BaseResultBean> void cacheData(String spKey, T data) {

        if (null == data) {
            LogUtils.e("缓存的数据为null");
            return;
        }
        String jsonStr = GsonUtils.toJson(data);

        LogUtils.d("缓存数据 key=" + spKey + "  json=" + jsonStr);

        getHomeTabSP().putString(spKey, jsonStr);

    }


    public static String getStringCache(String spKey) {

        if (TextUtils.isEmpty(spKey)) {
            LogUtils.e("缓存的key为null");
            return null;
        }

        LogUtils.d("取缓存key=" + spKey);
        String jsonStr = getHomeTabSP().getString(spKey);

        LogUtils.d("取缓存k=" + spKey + " value=" + jsonStr);

        return jsonStr;
    }

    public static void removeSp(String spKey) {
        getHomeTabSP().remove(spKey);
    }

    public static void cacheNewsTabTitleListData(String userId, SubscribeTitleListBean subscribeTitleListBean) {
        cacheData(TAB_TITLE_INFO_CACHE_KEY + userId, subscribeTitleListBean);
    }

    public static SubscribeTitleListBean getSubscribeTitleListBean(String userId) {

        String jsonStr = getStringCache(TAB_TITLE_INFO_CACHE_KEY + userId);
        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }
        return GsonUtils.toObj(jsonStr, SubscribeTitleListBean.class);
    }


    public static void cacheNewsListData(String newsTypeId, NewsListPageBean newsListPageBean) {
        cacheData(NEWS_LIST_INFO_CACHE_KEY + newsTypeId, newsListPageBean);
    }

    public static NewsListPageBean getNewsListBean(String newsTypeId) {

        String cacheKey = NEWS_LIST_INFO_CACHE_KEY + newsTypeId;

        String jsonStr = getStringCache(cacheKey);

        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }

        return GsonUtils.toObj(jsonStr, NewsListPageBean.class);
    }


    public static boolean isMediaGuideShouldShow() {
        return getHomeTabSP().getBoolean(IS_MEDIA_GUIDE_SHOULD_SHOW_KEY+ YMUserService.getCurUserId(),true);
    }

    public static void setediaGuideShouldShow(boolean isShouldShow) {
        getHomeTabSP().putBoolean(IS_MEDIA_GUIDE_SHOULD_SHOW_KEY+YMUserService.getCurUserId(), isShouldShow);
    }


}
