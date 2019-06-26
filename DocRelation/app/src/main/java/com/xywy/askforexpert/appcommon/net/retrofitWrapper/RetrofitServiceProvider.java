package com.xywy.askforexpert.appcommon.net.retrofitWrapper;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.commonerror.CommonIntegerRstErrors;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.commonerror.CommonStringRstErrors;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
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
import com.xywy.askforexpert.model.main.NewsListPageBean;
import com.xywy.askforexpert.model.main.SubscribeTitleListBean;
import com.xywy.askforexpert.model.main.promotion.PromotionPageBean;
import com.xywy.askforexpert.model.media.MediaFirstItemBean;
import com.xywy.askforexpert.model.media.MediaNumberBean;
import com.xywy.askforexpert.model.media.TodayRecommendBean;
import com.xywy.askforexpert.model.newdoctorcircle.CommentResultBean;
import com.xywy.askforexpert.model.newdoctorcircle.PraiseResultBean;
import com.xywy.askforexpert.model.topics.TopicDetailData;
import com.xywy.retrofit.RetrofitClient;
import com.xywy.retrofit.net.BaseData;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by bobby on 16/6/15.
 */
public class RetrofitServiceProvider {
    //中间层域名
//    public static final String REQUEST_URL = "http://test.api.wws.xywy.com";

    //医脉域名
    public static final String REQUEST_URL = CommonUrl.BASE_HOST;

    private static final int DEFAULT_TIMEOUT = 15;

    private static Map<String, String> headerMap = new HashMap<>();

    private Retrofit retrofit;
    private ApiService api;

    //构造方法私有
    private RetrofitServiceProvider() {
//        //手动创建一个OkHttpClient并设置超时时间
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
//        CommonNetUtils.addHeader(headerMap, builder);
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        builder.addInterceptor(loggingInterceptor);
//
//        retrofit = new Retrofit.Builder()
//                .client(builder.build())
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .baseUrl(REQUEST_URL)
//                .build();
//
//        api = retrofit.create(ApiService.class);

        //替换为带缓存的Retrofit
        retrofit = RetrofitClient.getRetrofit();
        api = retrofit.create(ApiService.class);
    }


    //获取单例
    public static RetrofitServiceProvider getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 生成请求sign
     *
     * @param signKey
     * @return
     */
    public static String generateSign(String signKey) {
        return MD5Util.MD5(signKey + Constants.MD5_KEY);
    }

    private <T> void toSubscribe(rx.Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }


    //=============================================开始写接口===============================================================

    public Observable yimaiLogin(Subscriber<BaseData<UserData>> subscriber, String username, String password, String reg_id) {

        Map<String, String> bundle = new HashMap<>();
        bundle.put("password", password);
        bundle.put("phone", username);
        bundle.put("sign", generateSign(username + Constants.MD5_KEY));
        bundle.put("command", "login");
        bundle.put("reg_id", reg_id);
        rx.Observable<BaseData<UserData>> observable = api.yimaiLogin(bundle)
                .map(new CommonErrors<UserData>());

        toSubscribe(observable, subscriber);
        return observable;
    }

    public Observable getGroupList(Subscriber<BaseData<List<GroupModel>>> subscriber, String userId) {
//        userId = "18732252";//for test
        //a=huanxingroup&m=grouplist&userid=18732252&bind=18732252&sign=34c8e82ef40d8f05613955bbd4e67bc4
        Map<String, String> bundle = new HashMap<>();
        bundle.put("a", "huanxingroup");
        bundle.put("m", "grouplist");
        bundle.put("userid", userId);
        bundle.put("bind", userId);
        addSign(bundle, userId);
        rx.Observable<BaseData<List<GroupModel>>> observable = api.getGroupList(bundle)
                .map(new CommonErrors<List<GroupModel>>());

        toSubscribe(observable, subscriber);
        return observable;
    }


    public Observable getAddressBookList(Subscriber<BaseData<List<HxUserEntity>>> subscriber) {


//        String bind = YMApplication.getLoginInfo().getData()
//                .getHuanxin_username();
//        Long st = System.currentTimeMillis();
//
//        String sign = MD5Util.MD5(st + bind + Constants.MD5_KEY);
//        AjaxParams params = new AjaxParams();
//
//        params.put("timestamp", st + "");
//        params.put("bind", bind);
//        params.put("a", "chat");
//        params.put("m", "getRelation");
//        params.put("username", bind);
//        params.put("sign", sign);

//        String userHxId = CommonAppUtils.getCurUserId();
        String userHxId = YMApplication.getLoginInfo().getData().getHuanxin_username();
        String timestamp = System.currentTimeMillis() + "";
        String signKey = timestamp + userHxId;
        Map<String, String> bundle = new HashMap<>();
        bundle.put("timestamp", timestamp);
        bundle.put("a", "chat");
        bundle.put("m", "getRelation");
        bundle.put("username", userHxId);
        bundle.put("bind", userHxId);
        addSign(bundle, signKey);
        rx.Observable<BaseData<List<HxUserEntity>>> observable = api.getContactList(bundle)
                .map(new CommonErrors<List<HxUserEntity>>());

        toSubscribe(observable, subscriber);
        return observable;
    }


    public Observable inviteToGroup(String groupId, String[] members, Subscriber<BaseData<JsonObject>> subscriber) {
        //members=12529772&a=huanxingroup&m=inviteinfogroup&groupid=207106433559298476&userid=18732252&bind=18732252&sign=34c8e82ef40d8f05613955bbd4e67bc4
        //String member = "12529772";
        //groupId = "207106433559298476";
        //userId = "18732252";
        String member = getMemberStr(members);
        String userId = YMUserService.getCurUserId();

        Map<String, String> bundle = new HashMap<>();
        bundle.put("members", member);
        bundle.put("a", "huanxingroup");
        bundle.put("m", "inviteinfogroup");
        bundle.put("groupid", groupId);
        bundle.put("userid", userId);
        bundle.put("bind", userId);
        addSign(bundle, userId);
        rx.Observable<BaseData<JsonObject>> observable = api.dealWithGroup(bundle)
                .map(new CommonErrors<JsonObject>());

        toSubscribe(observable, subscriber);
        return observable;
    }

    public Observable<BaseData<GroupModel>> createGroup(Subscriber<BaseData<GroupModel>> subscriber, String[] members, String userId, String groupName) {
        //a=huanxingroup&m=group_add&userid=20437&members=12529772|18732252&bind=20437&groupname=医脉讨论&public=1&desc=医脉讨论组&approval=1&sign=9985a028b1e5b882be1a389165de2d47
//        String member = "12529772|18732252";
        String member = getMemberStr(members);
        Map<String, String> bundle = new HashMap<>();
        bundle.put("members", member);
        bundle.put("a", "huanxingroup");
        bundle.put("m", "group_add");
        bundle.put("groupname", groupName);
        bundle.put("public", "1");
        bundle.put("desc", groupName);
        bundle.put("approval", "1");
        bundle.put("userid", userId);
        bundle.put("bind", userId);
        addSign(bundle, userId);
        rx.Observable<BaseData<GroupModel>> observable = api.createGroup(bundle)
                .map(new CommonErrors<GroupModel>());

        toSubscribe(observable, subscriber);
        return observable;
    }

    @NonNull
    private String getMemberStr(String[] members) {
        String member = "";
        for (String str : members) {
            member += str + "|";
        }
        if (member.length() > 0) {
            member = member.substring(0, member.length() - 1);
        }
        return member;
    }

    public Observable setGroupNoDisturb(Subscriber<BaseData<JsonObject>> subscriber, String groupId, String userId, int flag) {
        //is_disturb=1&a=huanxingroup&m=nodisturb&userid=20437&bind=20437&groupid=207106433559298476&sign=9985a028b1e5b882be1a389165de2d47
//        flag = 1;
//        userId = "20437";
//        groupId = "207106433559298476";

        Map<String, String> bundle = new HashMap<>();
        bundle.put("is_disturb", String.valueOf(flag));
        bundle.put("a", "huanxingroup");
        bundle.put("m", "nodisturb");
        bundle.put("userid", userId);
        bundle.put("bind", userId);
        bundle.put("groupid", groupId);
        addSign(bundle, userId);
        rx.Observable<BaseData<JsonObject>> observable = api.dealWithGroup(bundle)
                .map(new CommonErrors<JsonObject>());

        toSubscribe(observable, subscriber);
        return observable;
    }

    public Observable changeGroupName(Subscriber<BaseData<JsonObject>> subscriber, String groupId, String name, String userId) {
        //a=huanxingroup&m=modifygroupname&groupid=207106433559298476&userid=20437&bind=20437&groupname=癌症讨论&sign=9985a028b1e5b882be1a389165de2d47

        Map<String, String> bundle = new HashMap<>();
        bundle.put("a", "huanxingroup");
        bundle.put("m", "modifygroupname");
        bundle.put("userid", userId);
        bundle.put("bind", userId);
        bundle.put("groupid", groupId);
        bundle.put("groupname", name);
        addSign(bundle, userId);
        rx.Observable<BaseData<JsonObject>> observable = api.dealWithGroup(bundle)
                .map(new CommonErrors<JsonObject>());

        toSubscribe(observable, subscriber);
        return observable;
    }


    /**
     * 删除群组接口 1279
     *
     * @param groupId
     * @param masterId   群主id
     * @param subscriber
     * @return
     */
    public Observable deleteGroup(String groupId, String masterId, Subscriber<BaseData<JsonObject>> subscriber) {
        //a=huanxingroup&groupid=207106433559298476&userid=20437&bind=20437&tuserid=12529772&m=exitgroup&sign=9985a028b1e5b882be1a389165de2d47
//        tuserId = "12529772";
//        userId = "20437";
//        groupId = "207106433559298476";

        Map<String, String> bundle = new HashMap<>();
        bundle.put("a", "huanxingroup");
        bundle.put("m", "deletegroup");
        bundle.put("userid", masterId);
        bundle.put("bind", masterId);
        bundle.put("groupid", groupId);
        addSign(bundle, masterId);
        rx.Observable<BaseData<JsonObject>> observable = api.dealWithGroup(bundle)
                .map(new CommonErrors<JsonObject>());

        toSubscribe(observable, subscriber);
        return observable;
    }


    /**
     * 退群 接口 1274
     *
     * @param groupId
     * @param userId     群主id
     * @param subscriber
     * @return
     */
    public Observable existGroup(String groupId, String userId, String newOwner, Subscriber<BaseData<JsonObject>> subscriber) {

        //参数转换 null 转为""
        newOwner = TextUtils.isEmpty(newOwner) ? "" : newOwner;

        Map<String, String> bundle = new HashMap<>();
        bundle.put("a", "huanxingroup");
        bundle.put("m", "exitgroup");
        bundle.put("userid", userId);
        bundle.put("bind", userId);
        bundle.put("groupid", groupId);
        bundle.put("newowner", newOwner);
        addSign(bundle, userId);
        rx.Observable<BaseData<JsonObject>> observable = api.dealWithGroup(bundle)
                .map(new CommonErrors<JsonObject>());


        toSubscribe(observable, subscriber);
        return observable;
    }


    /**
     * 退群 踢人 接口 1331
     *
     * @param groupId
     * @param masterId   群主id
     * @param tuserId
     * @param subscriber
     * @return
     */
    public Observable dealWithGroupMember(String groupId, String masterId, String tuserId, Subscriber<BaseData<JsonObject>> subscriber) {
        //a=huanxingroup&groupid=207106433559298476&userid=20437&bind=20437&tuserid=12529772&m=exitgroup&sign=9985a028b1e5b882be1a389165de2d47
//        tuserId = "12529772";
//        userId = "20437";
//        groupId = "207106433559298476";

        Map<String, String> bundle = new HashMap<>();
        bundle.put("a", "huanxingroup");
        bundle.put("m", "delmember");
        bundle.put("userid", masterId);
        bundle.put("bind", masterId);
        bundle.put("groupid", groupId);
        bundle.put("targetuser", tuserId);
        addSign(bundle, masterId);
        rx.Observable<BaseData<JsonObject>> observable = api.dealWithGroup(bundle)
                .map(new CommonErrors<JsonObject>());


        toSubscribe(observable, subscriber);
        return observable;
    }

    /**
     * 更换新群主
     *
     * @param groupId     群id
     * @param oldMasterId 老群主id
     * @param newMasterId 新群主id
     * @param subscriber  回调
     * @return
     */
    public Observable transferGroupMaster(String groupId, String oldMasterId, String newMasterId, Subscriber<BaseData<JsonObject>> subscriber) {
        //groupid=207106433559298476&a=huanxingroup&m=changeowner&userid=20437&bind=20437&owerid=12529772&sign=9985a028b1e5b882be1a389165de2d47

//        groupId = "207106433559298476";
//        ownerId = "12529772";
//        toUserId = "20437";

        Map<String, String> bundle = new HashMap<>();
        bundle.put("groupid", groupId);
        bundle.put("a", "huanxingroup");
        bundle.put("m", "changeowner");
        bundle.put("userid", oldMasterId);
        bundle.put("bind", oldMasterId);
        bundle.put("owerid", newMasterId);
        addSign(bundle, oldMasterId);
        rx.Observable<BaseData<JsonObject>> observable = api.dealWithGroup(bundle)
                .map(new CommonErrors<JsonObject>());

        toSubscribe(observable, subscriber);
        return observable;
    }

    public Observable getGroupMembers(Subscriber<BaseData<List<ContactModel>>> subscriber, String groupId) {
        //a=huanxingroup&m=groupmemberlist&groupid=206752980584628668&bind=206752980584628668&sign=b494c4fb511c7ee2c41978d01535f39e
//        groupId = "206752980584628668";

        Map<String, String> bundle = new HashMap<>();
        bundle.put("a", "huanxingroup");
        bundle.put("m", "groupmemberlist");
        bundle.put("bind", groupId);
        bundle.put("groupid", groupId);
        addSign(bundle, groupId);
        rx.Observable<BaseData<List<ContactModel>>> observable = api.getGroupMembers(bundle)
                .map(new CommonErrors<List<ContactModel>>());


        toSubscribe(observable, subscriber);
        return observable;
    }

    public Observable getGroupDetail(Subscriber<BaseData<GroupModel>> subscriber, String groupId, String userId) {
        //a=huanxingroup&m=groupinfo&userid=20437&bind=20437&groupid=207106433559298476&sign=9985a028b1e5b882be1a389165de2d47
//        groupId = "207106433559298476";
//        userId = "20437";

        Map<String, String> bundle = new HashMap<>();
        bundle.put("a", "huanxingroup");
        bundle.put("m", "groupinfo");
        bundle.put("bind", userId);
        bundle.put("userid", userId);
        bundle.put("groupid", groupId);
        addSign(bundle, userId);
        rx.Observable<BaseData<GroupModel>> observable = api.getGroupDetail(bundle)
                .map(new CommonErrors<GroupModel>());


        toSubscribe(observable, subscriber);
        return observable;
    }


    /**
     * 修改群头像方法
     *
     * @param filePath
     * @param groupId
     * @param masterId
     * @param subscriber
     * @return
     */
    public Observable modifyGroupImage(String filePath, String groupId, String masterId, Subscriber<BaseData<JsonObject>> subscriber) {
        File file = new File(filePath);
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("img", "clip.png", photoRequestBody);

        rx.Observable<BaseData<JsonObject>> observable = api.uploadImage(photo,
                RequestBody.create(null, "huanxingroup"),
                RequestBody.create(null, "modifyimg"),
                RequestBody.create(null, groupId),
                RequestBody.create(null, masterId),
                RequestBody.create(null, generateSign(masterId)))
                .map(new CommonErrors<JsonObject>());
        toSubscribe(observable, subscriber);
        return observable;

    }

    /**
     *
     * 获取某人的实名动态
     * @param loginUserId 登录用户的Id
     * @param page
     * @param userid  被查看用户Id
     * @param type
     * @return
     */
    public  Observable<DoctorCircleRealNameBean> getMydynamicMsg(String loginUserId, String page, String userid,
                                                                 String type
    ) {

        Map<String, String> params = new HashMap<>();
        addSign(params,userid + type);
        params.put("m", "my_dynamic");
        params.put("a", "dynamic");
        params.put("userid", userid);
        params.put("touserid", loginUserId);
        params.put("bind", userid + type);
        params.put("page", page);
        params.put("type", type);
        return RetrofitClient.getRetrofit().create(ApiService.class).getRealMsg(params);
    }
    /**
     * 获取某人的匿名动态
     *
     */
    public  Observable<DoctorCirclrNotNameBean> getMyAnonymoDynamicMsg(String touserid, String page, String userid,
                                                                 String type
    ) {

        Map<String, String> params = new HashMap<>();
        addSign(params,userid + type);
        params.put("m", "my_dynamic");
        params.put("a", "dynamic");
        params.put("userid", userid);
        params.put("touserid", touserid);
        params.put("bind", userid + type);
        params.put("page", page);
        params.put("type", type);
        return RetrofitClient.getRetrofit().create(ApiService.class).getAnonymoMsg(params);
    }
    /**
     * 获取匿名消息
     * @param page 当前页面
     * @param userid  userid
     * @param oldid
     * @param noldid
     * @param type
     */
    public Observable<DoctorCirclrNotNameBean> getAnonymoMsg(String page, String userid, String oldid, String noldid, String type) {

        Map<String, String> params = new HashMap<>();
        params.put("m", "dynamic_list");
        params.put("a", "dynamic");
        params.put("userid", userid);
        addSign(params,userid + type);
        if (noldid!=null) {
            params.put("noldid", noldid);
        }
        params.put("oldid", oldid);
        params.put("bind", userid + type);
        params.put("page", page + "");
        params.put("type", type + "");
        return RetrofitClient.getRetrofit().create(ApiService.class).getAnonymoMsg(params)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                 ;

    }
    /**
     * 获取主题消息
     * @param topicId
     * @param userID
     * @param currentPage
     */
    public Observable<TopicDetailData> getTopicMsg(String topicId, String userID, int currentPage) {

        Map<String, String> params = new HashMap<>();
        addSign(params,topicId);
        params.put("m", "theme_detail");
        params.put("a", "theme");
        params.put("bind", topicId);
        params.put("userid", userID);
        params.put("id", topicId);
        params.put("page", currentPage+"");
        params.put("pagesize", "20");
        return RetrofitClient.getRetrofit().create(ApiService.class).getTopicMsg(params);

    }
    /**
     * 获取实名消息
     * @param page 当前页面
     * @param userid  userid
     * @param oldid
     * @param noldid
     * @param type
     */
    public Observable<DoctorCircleRealNameBean> getRealMsg(String page, String userid, String oldid, String noldid, String type) {

        Map<String, String> params = new HashMap<>();
        params.put("m", "dynamic_list");
        params.put("a", "dynamic");
        params.put("userid", userid);
        addSign(params,userid + type);
        if (noldid!=null) {
            params.put("noldid", noldid);
        }
        params.put("oldid", oldid);
        params.put("bind", userid + type);
        params.put("page", page + "");
        params.put("type", type + "");
        return RetrofitClient.getRetrofit().create(ApiService.class).getRealMsg(params);
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())


    }
    //试题部分接口

    /**
     * 试题首页 试题集 列表接口 1383
     *
     * @param userId
     * @param subscriber
     * @return
     */
    public Observable getSetList(String userId, Subscriber<SetPageBean> subscriber) {
        String tempStamp = "" + System.currentTimeMillis();
        Map<String, String> bundle = new HashMap<>();
        bundle.put("a", "medicine");
        bundle.put("m", "paperClass");
        bundle.put("bind", userId);
        bundle.put("userId", userId);
        bundle.put("timestamp", tempStamp);
        addAnswerSign(bundle, tempStamp, userId);
        rx.Observable<SetPageBean> observable = api.getSetList(bundle)
                .map(new CommonIntegerRstErrors<SetPageBean>());

        toSubscribe(observable, subscriber);
        return observable;
    }

    /**
     * 试题列表接口 1384
     *
     * @param userId
     * @param classId    试题集id
     * @param subscriber
     * @return
     */
    public Observable getPaperList(String userId, String classId, Subscriber<PaperListPageBean> subscriber) {
        String tempStamp = "" + System.currentTimeMillis();
        Map<String, String> bundle = new HashMap<>();
        bundle.put("a", "medicine");
        bundle.put("m", "paperList");
        bundle.put("bind", userId);
        bundle.put("userId", userId);
        bundle.put("classId", classId);
        bundle.put("timestamp", tempStamp);
        addAnswerSign(bundle, tempStamp, userId);
        rx.Observable<PaperListPageBean> observable = api.getPaperList(bundle)
                .map(new CommonIntegerRstErrors<PaperListPageBean>());

        toSubscribe(observable, subscriber);
        return observable;
    }

    public Observable getPaperContent(String userId, String paperId, Subscriber<ExamPaper> subscriber) {
        String tempStamp = "" + System.currentTimeMillis();
        Map<String, String> bundle = new HashMap<>();
        bundle.put("a", "medicine");
        bundle.put("m", "paperInfo");
        bundle.put("bind", userId);
        bundle.put("userId", userId);
        bundle.put("paperId", paperId);
        bundle.put("timestamp", tempStamp);
        addAnswerSign(bundle, tempStamp, userId);
        rx.Observable<ExamPaper> observable = api.getPaperContent(bundle)
                .map(new CommonIntegerRstErrors<ExamPaper>());

        toSubscribe(observable, subscriber);
        return observable;
    }

    /**
     * 提交答题结果 1386
     *
     * @param userId
     * @param paperId
     * @param score
     * @param paper
     * @param subscriber
     * @return
     */
    public Observable submitForScore(String userId, String paperId, int score, String paper, Subscriber<ScoreBean> subscriber) {
        String tempStamp = "" + System.currentTimeMillis();
        Map<String, String> bundle = new HashMap<>();
        bundle.put("a", "medicine");
        bundle.put("m", "paperRecord");
        bundle.put("bind", userId);
        bundle.put("userId", userId);
        bundle.put("paperId", paperId);
        bundle.put("score", "" + score);
        bundle.put("paper", paper);
        bundle.put("timestamp", tempStamp);
        addAnswerSign(bundle, tempStamp, userId);
        rx.Observable<ScoreBean> observable = api.submitForScore(bundle)
                .map(new CommonIntegerRstErrors<ScoreBean>());

        toSubscribe(observable, subscriber);
        return observable;
    }

    /**
     * 获取已答试题记录 1387
     *
     * @param userId
     * @param subscriber
     * @return
     */
    public Observable getAnsweredPaperList(String userId, Subscriber<AnsweredListPageBean> subscriber) {
        String tempStamp = "" + System.currentTimeMillis();
        Map<String, String> bundle = new HashMap<>();
        bundle.put("a", "medicine");
        bundle.put("m", "paperRecordList");
        bundle.put("bind", userId);
        bundle.put("userId", userId);
        bundle.put("timestamp", tempStamp);
        addAnswerSign(bundle, tempStamp, userId);
        rx.Observable<AnsweredListPageBean> observable = api.getAnsweredPaperList(bundle)
                .map(new CommonIntegerRstErrors<AnsweredListPageBean>());

        toSubscribe(observable, subscriber);
        return observable;
    }


    /**
     * 获取错题页详情 1388
     *
     * @param userId
     * @param paperId
     * @param subscriber
     * @return
     */
    public Observable getPaperWrongQuestionList(String userId, String paperId, Subscriber<WrongPaperPageBean> subscriber) {
        String tempStamp = "" + System.currentTimeMillis();
        Map<String, String> bundle = new HashMap<>();
        bundle.put("a", "medicine");
        bundle.put("m", "paperWrongInfo");
        bundle.put("paperId", paperId);
        bundle.put("bind", userId);
        bundle.put("userId", userId);
        bundle.put("timestamp", tempStamp);
        addAnswerSign(bundle, tempStamp, userId);
        rx.Observable<WrongPaperPageBean> observable = api.getPaperWrongQuestionList(bundle)
                .map(new CommonIntegerRstErrors<WrongPaperPageBean>());

        toSubscribe(observable, subscriber);
        return observable;
    }


    /**
     * 删除错题集 1389
     *
     * @param userId
     * @param paperId
     * @param subscriber
     * @return
     */
    public Observable deleteWrongQuestionSet(String userId, String paperId, String action, Subscriber<DeleteWrongSetResultBean> subscriber) {
        String tempStamp = "" + System.currentTimeMillis();
        Map<String, String> bundle = new HashMap<>();
        bundle.put("a", "medicine");
        bundle.put("m", "paperRecordDel");
        bundle.put("paperId", paperId);
        bundle.put("action", action);
        bundle.put("bind", userId);
        bundle.put("userId", userId);
        bundle.put("timestamp", tempStamp);
        addAnswerSign(bundle, tempStamp, userId);
        rx.Observable<DeleteWrongSetResultBean> observable = api.deleteWrongQuestionSet(bundle)
                .map(new CommonIntegerRstErrors<DeleteWrongSetResultBean>());

        toSubscribe(observable, subscriber);
        return observable;
    }

    /**
     * 删除单个错题 1390
     *
     * @param userId
     * @param paperId
     * @param subscriber
     * @param questionId
     * @return
     */
    public Observable deleteWrongQuestion(String userId, String paperId, String questionId, Subscriber<DeleteWrongQuestionResultBean> subscriber) {
        String tempStamp = "" + System.currentTimeMillis();
        Map<String, String> bundle = new HashMap<>();
        bundle.put("a", "medicine");
        bundle.put("m", "paperWrongDel");
        bundle.put("paperId", paperId);
        bundle.put("questionId", questionId);
        bundle.put("bind", userId);
        bundle.put("userId", userId);
        bundle.put("timestamp", tempStamp);
        addAnswerSign(bundle, tempStamp, userId);
        rx.Observable<DeleteWrongQuestionResultBean> observable = api.deleteWrongQuestion(bundle)
                .map(new CommonIntegerRstErrors<DeleteWrongQuestionResultBean>());

        toSubscribe(observable, subscriber);
        return observable;
    }

    /**
     * 获取用户晋级信息
     *
     * @param userId
     * @param subscriber
     * @return
     */
    public Observable getPromotionInfo(String userId, Subscriber<PromotionPageBean> subscriber) {
        String tempStamp = "" + System.currentTimeMillis();
        Map<String, String> bundle = new HashMap<>();
        bundle.put("command", "jinjiInfo");
        bundle.put("userid", userId);
        addSign(bundle, userId);
        rx.Observable<PromotionPageBean> observable = api.getPromotionInfo(bundle)
                .map(new CommonIntegerRstErrors<PromotionPageBean>());

        toSubscribe(observable, subscriber);
        return observable;
    }

    /**
     * 医圈点赞接口 290
     *
     * @param userId
     * @param msgId
     * @param commentId
     * @param toUserId
     * @param type
     * @param subscriber
     * @return
     */
    public Observable praise(String userId, String msgId, String commentId, String toUserId, String type, Subscriber<PraiseResultBean> subscriber) {
        Map<String, String> bundle = new HashMap<>();

        commentId = TextUtils.isEmpty(commentId) ? "" : commentId;
        toUserId = TextUtils.isEmpty(toUserId) ? "" : toUserId;

        bundle.put("m", "praise");
        bundle.put("a", "praise");
        bundle.put("type", type);
        bundle.put("userid", userId);
        bundle.put("dynamicid", msgId);
        bundle.put("commentid", commentId);// 评论ID
        String bind = userId + toUserId + commentId + msgId;
        bundle.put("bind", bind);// userid+touserid+commentid+dynamicid
        bundle.put("touserid", toUserId);// 被评论用户ID
        addSign(bundle, bind);
        rx.Observable<PraiseResultBean> observable = api.praise(bundle)
                .map(new CommonStringRstErrors<PraiseResultBean>());

        toSubscribe(observable, subscriber);
        return observable;
    }


    /**
     * 医圈评论接口 286
     *
     * @param userId
     * @param msgId
     * @param commentId
     * @param toUserId
     * @param content
     * @param subscriber
     * @return
     */
    public Observable comment(String userId, String msgId, String commentId, String toUserId, String content, String type, Subscriber<CommentResultBean> subscriber) {
        Map<String, String> bundle = new HashMap<>();

        commentId = TextUtils.isEmpty(commentId) ? "" : commentId;
        toUserId = TextUtils.isEmpty(toUserId) ? "" : toUserId;

        bundle.put("m", "comment");
        bundle.put("a", "comment");
        bundle.put("type", type);
        bundle.put("userid", userId);
        bundle.put("touserid", toUserId);// 被评论用户ID
        bundle.put("dynamicid", msgId);
        bundle.put("commentid", commentId);// 评论ID
        bundle.put("content", content);// 评论ID
        String bind = userId + toUserId + msgId;
        bundle.put("bind", bind);
        addSign(bundle, bind);
        rx.Observable<CommentResultBean> observable = api.comment(bundle)
                .map(new CommonStringRstErrors<CommentResultBean>());
        toSubscribe(observable, subscriber);
        return observable;
    }


    /**
     * 删除评论接口 287
     *
     * @param userId
     * @param commentId
     * @param subscriber
     * @return
     */
    public Observable deleteComment(String userId, String commentId, Subscriber<BaseResultBean> subscriber) {
        Map<String, String> bundle = new HashMap<>();

        commentId = TextUtils.isEmpty(commentId) ? "" : commentId;

        bundle.put("a", "comment");
        bundle.put("m", "comment_del");
        bundle.put("userid", userId);
        bundle.put("id", commentId);// 评论ID
        String bind = userId + commentId;
        bundle.put("bind", bind);
        addSign(bundle, bind);
        rx.Observable<BaseResultBean> observable = api.deleteComment(bundle)
                .map(new CommonIntegerRstErrors<BaseResultBean>());

        toSubscribe(observable, subscriber);
        return observable;
    }

    /**
     * 删除医圈动态接口 283
     *
     * @param userId
     * @param msgId
     * @param subscriber
     * @return
     */
    public Observable deleteCircleMsg(String userId, String msgId, Subscriber<BaseResultBean> subscriber) {
        Map<String, String> bundle = new HashMap<>();
        String bind = userId + msgId;
        bundle.put("m", "dynamic_del");
        bundle.put("a", "dynamic");
        bundle.put("userid", userId);
        bundle.put("bind", bind);
        bundle.put("dynamicid", msgId);
        bundle.put("bind", bind);
        addSign(bundle, bind);
        rx.Observable<BaseResultBean> observable = api.deleteCircleMsg(bundle)
                .map(new CommonIntegerRstErrors<BaseResultBean>());

        toSubscribe(observable, subscriber);
        return observable;
    }


    /**
     * 获取实名动态详情页 信息 和尾页评论
     *
     * @param userId
     * @param msgId
     * @param subscriber
     * @return
     */
    public Observable getDynamicDetailPageInfoWithLastPage(String userId, String msgId, int page, Subscriber<DynamicDtaile> subscriber) {

        return getDynamicDetailPageInfo(userId, msgId, page, subscriber);
    }


    /**
     * 获取实名动态详情页 信息 和首页评论
     *
     * @param userId
     * @param msgId
     * @param subscriber
     * @return
     */
    public Observable getDynamicDetailPageInfo(String userId, String msgId, Subscriber<DynamicDtaile> subscriber) {
        return getDynamicDetailPageInfo(userId, msgId, 1, subscriber);
    }

    /**
     * 获取实名动态详情页 信息
     *
     * @param userId
     * @param msgId
     * @param subscriber
     * @return
     */
    private Observable getDynamicDetailPageInfo(String userId, String msgId, int page, Subscriber<DynamicDtaile> subscriber) {
        Map<String, String> bundle = new HashMap<>();
        String bind = userId + msgId;
        bundle.put("m", "dynamic_row");
        bundle.put("a", "dynamic");
        bundle.put("userid", userId);
        bundle.put("dynamicid", msgId);
        bundle.put("bind", bind);
        bundle.put("page", "" + page);//首次 获取第一页数据
        addSign(bundle, bind);
        rx.Observable<DynamicDtaile> observable = api.getDynamicDetailPageInfo(bundle)
                .map(new CommonStringRstErrors<DynamicDtaile>());
        toSubscribe(observable, subscriber);
        return observable;
    }
//
//    public static void getMoreConment(String page, String dynamicid,
//                                      String bind, String sign, AjaxCallBack<? extends Object> callBack) {
//        AjaxParams params = new AjaxParams();
//        params.put("m", "comment_page");
//        params.put("page", page);
//        params.put("a", "comment");
//        params.put("dynamicid", dynamicid);
//        params.put("bind", bind);// /userid+id
//        params.put("sign", sign);
//        DLog.d(TAG, "more comment url = " + url + "?" + params.toString());
//        fh.post(url, params, callBack);
//    }


    /**
     * 获取实名动态详情页 信息
     *
     * @param msgId
     * @return
     */
    public Observable getDynamicDetailMoreComment(String msgId, int page, Subscriber<MoreMessage> subscriber) {
        Map<String, String> bundle = new HashMap<>();
        String bind = msgId;

        bundle.put("m", "comment_page");
        bundle.put("a", "comment");
        bundle.put("dynamicid", msgId);
        bundle.put("page", "" + page);
        bundle.put("bind", bind);// /userid+id

        addSign(bundle, bind);
        rx.Observable<MoreMessage> observable = api.getDynamicDetailMoreComment(bundle)
                .map(new CommonStringRstErrors<MoreMessage>());
        toSubscribe(observable, subscriber);
        return observable;
    }


    /**
     * 试题模块 添加sign参数
     *
     * @param paramMap
     * @param tempStamp
     * @param bind
     */
    private void addAnswerSign(Map<String, String> paramMap, String tempStamp, String bind) {
        addSign(paramMap, tempStamp + bind);
    }


    /**
     * 添加sign参数
     *
     * @param signKey
     * @param paramMap
     */
    public static void addSign(Map<String, String> paramMap, String signKey) {
        String sign = generateSign(signKey);
        paramMap.put("sign", sign);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final RetrofitServiceProvider INSTANCE = new RetrofitServiceProvider();
    }

    /**
     * 获取发现 页服务提醒数接口
     *
     * @param userId
     * @param subscriber
     * @return
     */
    public Observable getDiscoverNoticeNumber(String userId,Subscriber<DiscoverNoticeNumberBean> subscriber) {
        Map<String, String> bundle = new HashMap<>();
        bundle.put("command", "quesMenu");
        bundle.put("userid", userId);
        bundle.put("getCount", "1");
        addSign(bundle, userId);
        rx.Observable<DiscoverNoticeNumberBean> observable = api.getDiscoverServiceNoticeNumber(bundle)
                .map(new CommonIntegerRstErrors<DiscoverNoticeNumberBean>());

        toSubscribe(observable, subscriber);
        return observable;
    }

    /**
     * 获取今日推荐媒体号
     * @param userid
     */
    public  Observable<TodayRecommendBean> getToadayRecommendMedia(String userid) {

        Map<String, String> params = new HashMap<>();
        addSign(params,userid);
        params.put("m", "recommend");
        params.put("a", "media");
        params.put("userid", userid);
        params.put("bind", userid );
        return api.getToadayRecommendMedia(params);
    }

    /**
     * 获取媒体号科室列表
     * @param userid
     */
    public  Observable<BaseData<MediaFirstItemBean>> getMediaOrders(String userid) {

        Map<String, String> params = new HashMap<>();
        addSign(params,userid);
        params.put("m", "mediasubject");
        params.put("a", "media");
        params.put("userid", userid);
        params.put("bind", userid );
        return api.getMediaOrders(params);
    }
    /**
     * 根据科室id获取媒体号列表
     * @param userid
     * @param id 科室id
     * @param page
     */
    public Observable<BaseData<List<MediaNumberBean>>> getMediaByDepartId(String userid, String id, int page) {

        Map<String, String> params = new HashMap<>();
        addSign(params,userid+id);
        params.put("m", "mediagenre");
        params.put("a", "media");
        params.put("id", id);
        params.put("page", page+"");
        params.put("userid", userid);
        params.put("bind", userid+id);
        return api.getMediaByDepartId(params);
    }

    /**
     * 获取首页订阅标题列表
     *
     * @param userId
     * @param subscriber
     * @return
     */
    public Observable getSubscribeTitleList(String userId,String identityType,Subscriber<SubscribeTitleListBean> subscriber) {
        Map<String, String> bundle = new HashMap<>();
        bundle.put("a", "list");
        bundle.put("userid", userId);
        bundle.put("c", "subscribe");
        bundle.put("type", identityType);
        addSign(bundle, userId);
        rx.Observable<SubscribeTitleListBean> observable = api.getSubscribeTitleList(bundle)
                .map(new CommonIntegerRstErrors<SubscribeTitleListBean>());

        toSubscribe(observable, subscriber);
        return observable;
    }


    /**
     * 获取 首页具体一个tab媒体列表信息
     *
     * @param userId
     * @param subscriber
     * @return
     */
    public Observable getNewsListPageBeanList(String userId,String id,int pageNumber,int pageSize,Subscriber<NewsListPageBean> subscriber) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("a", "list");
        paramMap.put("c", "article");
        paramMap.put("userid", userId);
        paramMap.put("id", id);
        paramMap.put("page", String.valueOf(pageNumber));
        paramMap.put("pagesize", ""+pageSize);
        addSign(paramMap, id);
        rx.Observable<NewsListPageBean> observable = api.getNewsListPageBeanList(paramMap)
                .map(new CommonIntegerRstErrors<NewsListPageBean>());

        toSubscribe(observable, subscriber);
        return observable;
    }



}
