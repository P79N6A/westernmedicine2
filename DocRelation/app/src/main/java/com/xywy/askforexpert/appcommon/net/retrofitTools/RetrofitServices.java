package com.xywy.askforexpert.appcommon.net.retrofitTools;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/6/7 10:46
 */

import com.xywy.askforexpert.appcommon.net.retrofitWrapper.ApiConstants;
import com.xywy.askforexpert.model.AddressBook;
import com.xywy.askforexpert.model.base.BaseBean;
import com.xywy.askforexpert.model.base.BaseBean2;
import com.xywy.askforexpert.model.followList.FollowListData;
import com.xywy.askforexpert.model.healthy.HealthyDocBean;
import com.xywy.askforexpert.model.healthy.HealthyUserInfoDetailBean;
import com.xywy.askforexpert.model.media.ServicesMediasListBean;
import com.xywy.askforexpert.model.update.VersionUpdateData;
import com.xywy.askforexpert.model.videoNews.VideoNewsBean;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Retrofit 请求 service
 */
@Deprecated
public class RetrofitServices {

    /**
     * 健康居民 患者详情
     */
    public interface HealthyUserInfoDetailService {
        @FormUrlEncoded
        @POST(ApiConstants.COMMON_URL)
        Call<BaseBean<HealthyUserInfoDetailBean>> getData(@FieldMap Map<String, String> map);
    }

    /**
     * 健康档案
     */
    public interface HealthyDocService {
        @FormUrlEncoded
        @POST(ApiConstants.COMMON_URL)
        Call<HealthyDocBean> getData(@FieldMap Map<String, String> map);
    }

    /**
     * 签约居民列表
     */
    public interface HealthyUserListService {
        @FormUrlEncoded
        @POST(ApiConstants.COMMON_URL)
        Call<AddressBook> getData(@FieldMap Map<String, String> map);
    }

    /**
     * 获取服务号/媒体号列表
     */
    public interface ServicesMediasListService {
        @FormUrlEncoded
        @POST(ApiConstants.COMMON_URL)
        Call<BaseBean<List<ServicesMediasListBean>>> getData(@FieldMap Map<String, Object> map);
    }

    /**
     * 搜索媒体号/服务号
     */
    public interface SearchMediaService {
        @FormUrlEncoded
        @POST(ApiConstants.COMMON_URL)
        Call<BaseBean<List<ServicesMediasListBean>>> getData(@FieldMap Map<String, Object> map);
    }

    /**
     * 发送资讯评论
     */
    public interface SendNewsCommentService {
        @FormUrlEncoded
        @POST(ApiConstants.ZIXUN_URL)
        Call<BaseBean2> getData(@FieldMap Map<String, String> map);
    }

    /**
     * 资讯点赞
     */
    public interface PraiseNewsService {
        @FormUrlEncoded
        @POST(ApiConstants.ZIXUN_URL)
        Call<BaseBean2> getData(@FieldMap Map<String, String> map);
    }

    /**
     * 资讯收藏
     */
    public interface NewsFavoriteService {
        @FormUrlEncoded
        @POST(ApiConstants.ZIXUN_URL)
        Call<BaseBean2> getData(@FieldMap Map<String, String> map);
    }

    /**
     * 订阅/取消订阅
     */
    public interface MediaFollowService {
        @FormUrlEncoded
        @POST(ApiConstants.COMMON_URL)
        Call<BaseBean2> getData(@FieldMap Map<String, String> map);
    }

    /**
     * 原生视频资讯
     */
    public interface VideoNewsService {
        @FormUrlEncoded
        @POST(ApiConstants.ZIXUN_URL)
        Call<BaseBean<VideoNewsBean>> getData(@FieldMap Map<String, String> map);
    }

    public interface FriendListService {
        @GET(ApiConstants.COMMON_URL)
        Call<AddressBook> getData(@QueryMap Map<String, Object> map);
    }

    public interface MediaSubscribePushService {
        @FormUrlEncoded
        @POST(ApiConstants.COMMON_URL)
        Call<BaseBean2> getData(@FieldMap Map<String, Object> map);
    }

    public interface FollowListService {
        @GET(ApiConstants.COMMON_URL)
        Call<BaseBean<List<FollowListData>>> getData(@QueryMap Map<String, Object> map);
    }

    public interface VersionUpdateService {
        @GET(ApiConstants.OTHER_HOST + "?a=sys&m=version&type=1")
        Call<BaseBean<VersionUpdateData>> getData(@Query("sign") String sign);
    }

}
