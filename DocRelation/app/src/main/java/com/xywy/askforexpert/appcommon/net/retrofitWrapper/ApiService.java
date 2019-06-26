package com.xywy.askforexpert.appcommon.net.retrofitWrapper;

import com.google.gson.JsonObject;
import com.xywy.askforexpert.appcommon.YMConfig;
import com.xywy.askforexpert.model.answer.api.ScoreBean;
import com.xywy.askforexpert.model.answer.api.answered.AnsweredListPageBean;
import com.xywy.askforexpert.model.answer.api.deletewrong.DeleteWrongQuestionResultBean;
import com.xywy.askforexpert.model.answer.api.deletewrong.DeleteWrongSetResultBean;
import com.xywy.askforexpert.model.answer.api.paperlist.PaperListPageBean;
import com.xywy.askforexpert.model.answer.api.set.SetPageBean;
import com.xywy.askforexpert.model.answer.api.wrongquestion.WrongPaperPageBean;
import com.xywy.askforexpert.model.answer.local.ExamPaper;
import com.xywy.askforexpert.model.api.BaseResultBean;
import com.xywy.askforexpert.model.discover.DiscoverNoticeNumberBean;
import com.xywy.askforexpert.model.doctor.DoctorCircleRealNameBean;
import com.xywy.askforexpert.model.doctor.DoctorCirclrNotNameBean;
import com.xywy.askforexpert.model.doctor.DynamicDtaile;
import com.xywy.askforexpert.model.doctor.MoreMessage;
import com.xywy.askforexpert.model.im.group.ContactModel;
import com.xywy.askforexpert.model.im.group.GroupModel;
import com.xywy.askforexpert.model.im.hxuser.HxUserEntity;
import com.xywy.askforexpert.model.liveshow.AlipayResultBean;
import com.xywy.askforexpert.model.liveshow.FollowUNFollowResultBean;
import com.xywy.askforexpert.model.liveshow.HealthCoinOrderBean;
import com.xywy.askforexpert.model.liveshow.LiveShowHostInfo;
import com.xywy.askforexpert.model.liveshow.LiveShowListPageBean;
import com.xywy.askforexpert.model.liveshow.LiveShowStateInfoBean;
import com.xywy.askforexpert.model.liveshow.MyFansPageBean;
import com.xywy.askforexpert.model.liveshow.MyFollowedPageBean;
import com.xywy.askforexpert.model.main.NewsListPageBean;
import com.xywy.askforexpert.model.main.SubscribeTitleListBean;
import com.xywy.askforexpert.model.main.promotion.PromotionPageBean;
import com.xywy.askforexpert.model.media.MediaFirstItemBean;
import com.xywy.askforexpert.model.media.MediaNumberBean;
import com.xywy.askforexpert.model.media.TodayRecommendBean;
import com.xywy.askforexpert.model.newdoctorcircle.CommentResultBean;
import com.xywy.askforexpert.model.newdoctorcircle.PraiseResultBean;
import com.xywy.askforexpert.model.topics.TopicDetailData;
import com.xywy.retrofit.net.BaseData;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by bobby on 16/6/15.
 */
public interface ApiService {
    public static final String MIDDLE_WIRE_COMMON_GET_PARAM_STR = "source="+ YMConfig.SOURCE+"&pro="+YMConfig.PRO+"&os="+YMConfig.OS;



    @FormUrlEncoded
    @POST(ApiConstants.loginMethod)
    Observable<BaseData<UserData>> rxCacheDemo(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiConstants.loginMethod)
    @Headers(ApiConstants.FORCE_NETWORK)
    Observable<BaseData<UserData>> noCacheDemo(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiConstants.loginMethod)
    Call<BaseData<UserData>> retrofitDemo(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiConstants.bookingMethod)
    Observable<BaseData<BookingEntity>> getBookingEntity(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiConstants.loginMethod)
    Observable<BaseData<UserData>> yimaiLogin(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiConstants.contactMethod)
    Observable<BaseData<List<GroupModel>>> getGroupList(@FieldMap Map<String, String> map);

    @GET(ApiConstants.contactMethod)
    Observable<BaseData<List<HxUserEntity>>> getContactList(@QueryMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiConstants.contactMethod)
    Observable<BaseData<JsonObject>> dealWithGroup(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiConstants.contactMethod)
    Observable<BaseData<GroupModel>> createGroup(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiConstants.contactMethod)
    Observable<BaseData<List<ContactModel>>> getGroupMembers(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiConstants.contactMethod)
    Observable<BaseData<GroupModel>> getGroupDetail(@FieldMap Map<String, String> map);

    @Multipart
    @POST(ApiConstants.contactMethod)
    Observable<BaseData<JsonObject>> uploadImage(@Part MultipartBody.Part image,
                                                 @Part("a") RequestBody a,
                                                 @Part("m") RequestBody m,
                                                 @Part("groupid") RequestBody groupid,
                                                 @Part("userid") RequestBody userid,
                                                 @Part("bind") RequestBody bind);

    //匿名消息刷新
    @FormUrlEncoded
    @POST(ApiConstants.doctorCircleMethod)
    Observable<DoctorCirclrNotNameBean> getAnonymoMsg(@FieldMap Map<String, String> map);

    //实名消息刷新
    @FormUrlEncoded
    @POST(ApiConstants.doctorCircleMethod)
    Observable<DoctorCircleRealNameBean> getRealMsg(@FieldMap Map<String, String> map);

    //话题消息刷新
    @FormUrlEncoded
    @POST(ApiConstants.FOLLOW_LIST)
    Observable<TopicDetailData> getTopicMsg(@FieldMap Map<String, String> map);
    //试题类接口

    @FormUrlEncoded
    @POST(ApiConstants.answerMethod)
    Observable<SetPageBean> getSetList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiConstants.answerMethod)
    Observable<PaperListPageBean> getPaperList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiConstants.answerMethod)
    Observable<ExamPaper> getPaperContent(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiConstants.answerMethod)
    Observable<ScoreBean> submitForScore(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiConstants.answerMethod)
    Observable<AnsweredListPageBean> getAnsweredPaperList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiConstants.answerMethod)
    Observable<WrongPaperPageBean> getPaperWrongQuestionList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiConstants.answerMethod)
    Observable<DeleteWrongSetResultBean> deleteWrongQuestionSet(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiConstants.answerMethod)
    Observable<DeleteWrongQuestionResultBean> deleteWrongQuestion(@FieldMap Map<String, String> map);

    //晋级接口

    @FormUrlEncoded
    @POST(ApiConstants.promotionInfoMethod)
    Observable<PromotionPageBean> getPromotionInfo(@FieldMap Map<String, String> map);

    //医圈接口

    @FormUrlEncoded
    @POST(ApiConstants.doctorCircleMethod)
    @Headers(ApiConstants.FORCE_NETWORK)
    Observable<PraiseResultBean> praise(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiConstants.doctorCircleMethod)
    Observable<CommentResultBean> comment(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiConstants.doctorCircleMethod)
    Observable<BaseResultBean> deleteComment(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiConstants.doctorCircleMethod)
    Observable<BaseResultBean> deleteCircleMsg(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiConstants.doctorCircleMethod)
    Observable<DynamicDtaile> getDynamicDetailPageInfo(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiConstants.doctorCircleMethod)
    Observable<MoreMessage> getDynamicDetailMoreComment(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST(ApiConstants.CLUB_URL)
    Observable<DiscoverNoticeNumberBean> getDiscoverServiceNoticeNumber(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST()
    Observable<DiscoverNoticeNumberBean> getDiscoverServiceNoticeNumber2(@Url String url, @FieldMap Map<String, String> postPramMap);

    @FormUrlEncoded
    @POST(ApiConstants.ZIXUN_URL)
    Observable<SubscribeTitleListBean> getSubscribeTitleList(@FieldMap Map<String, String> map);

    //获取今日订阅推荐
    @FormUrlEncoded
    @POST(ApiConstants.COMMON_URL)
    Observable<TodayRecommendBean> getToadayRecommendMedia(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiConstants.ZIXUN_URL)
    Observable<NewsListPageBean> getNewsListPageBeanList(@FieldMap Map<String, String> map);

    //获取媒体号订阅科室列表
    @FormUrlEncoded
    @POST(ApiConstants.COMMON_URL)
    Observable<BaseData<MediaFirstItemBean>> getMediaOrders(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ApiConstants.COMMON_URL)
    Observable<BaseData<List<MediaNumberBean>>> getMediaByDepartId(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("/user/userCreateOrder/index?" + MIDDLE_WIRE_COMMON_GET_PARAM_STR + "&api=1516&version=1.0")
    Observable<HealthCoinOrderBean> generateOrder(@Query("sign") String sign, @FieldMap Map<String, String> map);

    @GET("pay/AppAliPay/index?" + MIDDLE_WIRE_COMMON_GET_PARAM_STR + "&api=1062&version=1.0&service_code=yimai_xywy_radio")
    Observable<AlipayResultBean> payByAliPay(@Query("user_id") String userId, @Query("order") String orderId, @Query("sign") String sign);

    @GET("live/user/attenliveList?" + MIDDLE_WIRE_COMMON_GET_PARAM_STR + "&api=1552&version=1.0")
    Observable<MyFollowedPageBean> getMyFollowedLiveShowPageInfo(@Query("user_id") String userId, @Query("page") int page, @Query("sign") String sign);

    @GET("live/user/fansList?" + MIDDLE_WIRE_COMMON_GET_PARAM_STR + "&api=1553&version=1.0")
    Observable<MyFansPageBean> getMyFansPageInfo(@Query("user_id") String user_id, @Query("sign") String sign);

    @FormUrlEncoded
    @POST("live/concern/index?" + MIDDLE_WIRE_COMMON_GET_PARAM_STR + "&api=1551&version=1.0")
    Observable<FollowUNFollowResultBean> followOrUnFollow(@FieldMap Map<String, String> map, @Query("sign") String sign);


    @GET("live/room/getLiveList?" + MIDDLE_WIRE_COMMON_GET_PARAM_STR + "&api=1562&version=1.0")
    Observable<LiveShowListPageBean> getLiveShowList(@Query("state") String state, @Query("cate_id") String cate_id, @Query("page") int page, @Query("page_size") int page_size, @Query("sign") String sign);

    @GET("live/room/liveList?" + MIDDLE_WIRE_COMMON_GET_PARAM_STR + "&api=1568&version=1.0")
    Observable<LiveShowHostInfo> getLiveShowHostInfo(@Query("user_id") String user_id, @Query("id") String id, @Query("page") int page, @Query("is_list") int is_list, @Query("sign") String sign);

    @GET("live/user/liveStatus?" + MIDDLE_WIRE_COMMON_GET_PARAM_STR + "&api=1574&version=1.0")
    Observable<LiveShowStateInfoBean> getliveStatusInfo(@Query("user_id") String user_id, @Query("sign") String sign);

}
