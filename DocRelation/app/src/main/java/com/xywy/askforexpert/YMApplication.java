package com.xywy.askforexpert;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.iflytek.cloud.SpeechUtility;
import com.lecloud.config.LeCloudPlayerConfig;
import com.letv.proxy.LeCloudProxy;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.umeng.socialize.PlatformConfig;
import com.xywy.askforexpert.appcommon.YMConfig;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.db.DBManager;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.XywyInterceptor;
import com.xywy.askforexpert.appcommon.net.utils.CommonNetUtils;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DensityUtils;
import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.appcommon.utils.ScreenUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.AddNumSettingInfo;
import com.xywy.askforexpert.model.ChannelItem;
import com.xywy.askforexpert.model.ClinicStatInfo;
import com.xywy.askforexpert.model.DownFileItemInfo;
import com.xywy.askforexpert.model.FamDocSettingInfo;
import com.xywy.askforexpert.model.LoginInfo;
import com.xywy.askforexpert.model.LoginInfo_New;
import com.xywy.askforexpert.model.Message;
import com.xywy.askforexpert.model.PhoneSettingInfo;
import com.xywy.askforexpert.model.QueData;
import com.xywy.askforexpert.model.Service;
import com.xywy.askforexpert.model.consultentity.DoctorInfoBean;
import com.xywy.askforexpert.model.liveshow.LiveShowStateInfoEntity;
import com.xywy.askforexpert.model.media.MediaList;
import com.xywy.askforexpert.model.notice.Notice;
import com.xywy.askforexpert.model.push.PushExtraBean;
import com.xywy.askforexpert.module.consult.ConsultConstants;
import com.xywy.askforexpert.module.consult.activity.ConsultOnlineActivity;
import com.xywy.askforexpert.module.drug.OnlineRoomActivity;
import com.xywy.askforexpert.module.main.news.db.DatabaseHelper;
import com.xywy.askforexpert.module.main.news.db.NewsReadDataSource;
import com.xywy.askforexpert.module.main.patient.activity.PatientManagerActivity;
import com.xywy.askforexpert.module.my.account.utils.ForceLogoutDialogUtils;
import com.xywy.askforexpert.module.websocket.WebSocketApi;
import com.xywy.base.XywyBaseApplication;
import com.xywy.component.datarequest.imageWrapper.ImageLoaderUtils;
import com.xywy.datarequestlibrary.XywyDataRequestApi;
import com.xywy.datarequestlibrary.paramtools.CommonRequestParam;
import com.xywy.datarequestlibrary.paramtools.XywyHeaderHelper;
import com.xywy.easeWrapper.HXSDKHelper;
import com.xywy.easeWrapper.domain.EaseUser;
import com.xywy.im.sdk.XywyIMService;
import com.xywy.retrofit.RetrofitClient;
import com.xywy.retrofit.net.ApiException;
import com.xywy.retrofit.net.RetrofitCache;
import com.xywy.retrofit.rxbus.RxBus;
import com.xywy.util.AppUtils;
import com.xywy.util.CrashHandler;
import com.xywy.util.L;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalDb.DbUpdateListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.org.bjca.sdk.core.kit.BJCASDK;
import cn.org.bjca.sdk.core.values.EnvType;
import okhttp3.Request;

public class YMApplication extends XywyBaseApplication {


    //stone
    public static String mOfflineMessage, mNightMessage;
    public static boolean mIsNoticeOpen;//留言提示是否是开启状态 stone

    //stone 新消息提醒(即时问答)
    private PopupWindow mPopupWindow;
    private HashMap<String, PopupWindow> mPopupWindowMap = new HashMap<>();

    private static SharedPreferences mSP4Pid;//保存信息

    private static int statusBarHeight;
    private static boolean firstUseKdxf = true;

    //stone 保存有登录相关信息
    private static LoginInfo_New sUserInfo;

    public static PushExtraBean sPushExtraBean;

    private Set<Integer> mDaySetClear = new HashSet<>();
    private Set<Integer> mDaySetAll = new HashSet<>();

    public static Handler applicationHandler;

    //stone 是否是医生助手
    public static boolean sIsYSZS = true;

    //stone IM即时问答问题id 用于推送判断 在当前页面并且问题id相同,点击通知栏不跳转
    public static List<String> sQuestionId = new ArrayList<>();
    public static List<String> sWTGCQuestionId = new ArrayList<>();

    private static final String TAG = "YMApplication";
    public static final String APPLICATIONID = "com.xywy.medicine_super_market";
    /**
     * 是否为认证的签约医生
     */
    public static int healthyFlag;
    //stone 保存有登录相关信息
    private static LoginInfo login1 = new LoginInfo();

    private static LiveShowStateInfoEntity liveShowStateInfoEntity;
    public static String deleteGroupId;
    /**
     * 服务开通
     */
    public static ClinicStatInfo cinfo;
    /**
     * 跳转返回 标示
     */
    public static String photoTag;
    /**
     * 是否支持多选 默认 多选
     */
    public static boolean isSelectMore;
    public static int SCR_W;
    public static int SCR_H;
    public static String JPUSH_ID = "";

    /**
     * 是否刷新
     */
    public static boolean isrefresh = false;

    public static final int AUDIT_STATUS_1 = 1;//认证通过

    /**
     * 当前用户nickname,为了苹果推送不是userid而是昵称
     */

    // public static String lastGid;
    public static HXSDKHelper hxSDKHelper = HXSDKHelper.getInstance();
    /**
     * 我的诊所预约加号
     */
    public static AddNumSettingInfo addnuminfo = new AddNumSettingInfo();
    /**
     * 我的诊所家庭医生
     */
    public static FamDocSettingInfo famdocinfo = new FamDocSettingInfo();
    /**
     * 我的诊所电话医生
     */
    public static PhoneSettingInfo phosetinfo = new PhoneSettingInfo();
    public static List<Message> msgLists = new ArrayList<Message>();
    public static String msgnum;
    public static String msgdetail;
    public static String msgcreatetime;
    public static String msgtotal;
    public static String addFriendNum = "";
    public static String addMsgDetail = "";
    public static String addMsgCreateTime = "";
    static SharedPreferences sp;
    private static YMApplication instance;
    private static NewsReadDataSource mNewsReadDataSource;
    // login user name
    private List<ChannelItem> channelItemList = new ArrayList<>();
    private List<QueData> mLables = new ArrayList<>();
    private DownFileItemInfo stopOrStartDownloadfileItem; // 需要停止下载的任务
    private List<DownFileItemInfo> downloadItems = new ArrayList<DownFileItemInfo>(); // 下载队列
    private List<DownFileItemInfo> newDownloadItems = new ArrayList<DownFileItemInfo>();
    private Map<Integer, List<Integer>> notificMap = new HashMap<Integer, List<Integer>>();
    private NotificationManager manger;
    private DatabaseHelper mDatabaseHelper;
    private MediaList mediaList = new MediaList();
    private List<Notice> noticeList = new ArrayList<Notice>();

    public int isOnLineConsultUser = -1;

    public WebSocketApi webSocketApi;
    public WebSocketApi getWebSocketApi() {
        return webSocketApi;
    }
    //在线诊室 stone
//    WSApi wsApi;

    //在线医生的信息实体
    private DoctorInfoBean doctorInfoBean;
    //用药助手添加
    private boolean mSyncInfoSuccess;//医生信息是否同步成功，true则表示同步成功，false表示同步失败
    private String mSyncInfoMsg;//医生信息是否同步接口返回的提示信息
    private boolean mCanSellDrug;//医生是否有售药的权限，true表示有，false表示没有
    private String mCanSellDrugMsg;//医生是否有售药的权限接口返回的提示信息
    private boolean mHasSyncInfo;//是否同步过医生信息,true表示同步过医生信息，false表示未同步过医生信息
    private boolean mHasSellDrug;//是否检查过医生是否有售药的权限，true表示检查过，false，表示未检查过
    private boolean hasNewNotice;
    //用药助手添加

    public static String getPID() {
        return getLogin1().getData().getPid();
    }

    public static LiveShowStateInfoEntity getLiveShowStateInfoEntity() {
        return liveShowStateInfoEntity;
    }

    public static void setLiveShowStateInfoEntity(LiveShowStateInfoEntity liveShowStateInfoEntity) {
//        YMApplication.liveShowStateInfoEntity = liveShowStateInfoEntity;
//        LiveConfig config= new LiveConfig.Builder()
//                .setRequest(new YmLiveDataRequest())
//                .setUserId(login1.getData().getPid())
//                .setNickName(login1.getData().getRealname())
//                .setMyHeadImageUrl(login1.getData().getPhoto())
//                .setAnchorUrl(liveShowStateInfoEntity.getCover())
//                .setHealthIcon(liveShowStateInfoEntity.getBalance())
////                .setHxUserName(login1.getData().getHuanxin_username())
////                .setHxPassword(login1.getData().getHuanxin_password())
//                .setUserType(1)
//                .createLiveConfig();
//        LiveManager.getInstance().init(YMApplication.instance,config);
    }

    //stone 获取用户登录信息 如:用户名 用户头像 环信用户名 环信密码
    public static LoginInfo getLoginInfo() {
//        LoginInfo logins;
//        if (getLogin1() == null || getLogin1().getData() == null || TextUtils.isEmpty(getLogin1().getData().getPid())) {
//            //stone 可能返回的logins为null 直接引用会导致空指针bug
//            if (!TextUtils.isEmpty(sp.getString("login_info", ""))) {
//                logins = ResolveJson.R_logininfo(sp.getString("login_info", ""));
//                if (logins == null) {
//                    logins = new LoginInfo();
//                }
//            } else {
//                logins = new LoginInfo();
//            }
//        } else {
//            logins = getLogin1();
//        }
//
//        return logins;


        if (getLogin1() == null || getLogin1().getData() == null || TextUtils.isEmpty(getLogin1().getData().getPid())) {
            //stone 可能返回的logins为null 直接引用会导致空指针bug
            if (!TextUtils.isEmpty(sp.getString("login_info", ""))) {
                LoginInfo logins = ResolveJson.R_logininfo(sp.getString("login_info", ""));
                if (logins == null) {
                    return new LoginInfo();
                }
                return logins;
            } else {
                return new LoginInfo();
            }
        } else {
            return getLogin1();
        }
    }


    public static YMApplication getInstance() {
        return instance;
    }

    public boolean mAppIsForeGround = false;//是否在前台
    private int mStartedActivityCount = 0; //活动的Activity个数

    /**
     * 获取内存中好友user list
     *
     * @return
     */
    public static Map<String, EaseUser> getContactList() {
        return hxSDKHelper.getContactList();
    }

    public static String getUUid() {
        if (!YMUserService.isGuest()) {
            return getLoginInfo().getData().getPid();
        } else {
            return "0";
        }
    }

    public static LoginInfo getLogin1() {
        return login1;
    }

    public static void setLogin1(LoginInfo login1) {
        YMApplication.login1 = login1;
    }

    /**
     * 设置好友user list到内存中
     *
     * @param contactList
     */
    public void setContactList(Map<String, EaseUser> contactList) {
        hxSDKHelper.setContactList(contactList);
    }

    /**
     * 是否是医生 登录接口返回的isDoctor非空并且不为12,13,14 stone
     *
     * @return
     */
    public static boolean isDoctor() {
//        String isDoctor = getLoginInfo().getData().getIsdoctor();
//        return isDoctor != null && !isDoctor.equals("12") && !isDoctor.equals("13") && !isDoctor.equals("14");
        return true;//去掉了医学生这个类型,所以这里直接返回true
    }

    /**
     * 个人资料是否完善
     *
     * @return
     */
    public static boolean isInfoComplete() {
        //是否完善信息
        LoginInfo.UserData userData = getLoginInfo().getData();
        if (!isDoctor()) {
            return !(TextUtils.isEmpty(userData.getRealname())
                    || TextUtils.isEmpty(userData.getProfession())
                    || TextUtils.isEmpty(userData.getSchool()));
        } else {
            return !(TextUtils.isEmpty(userData.getRealname())
                    || TextUtils.isEmpty(userData.getJob())
                    || TextUtils.isEmpty(userData.getHospital())
                    || TextUtils.isEmpty(userData.getSubject())
            );
        }
    }

    /**
     * 0 未认证 1 认证中 2 认证
     *
     * @return
     */
//    public static int isPerCheckInfo() {
//
//        String isjob = getLoginInfo().getData().getIsjob();
//        String approveid = getLoginInfo().getData().getApproveid();
//        String isDoctor = getLoginInfo().getData().getIsdoctor();
//        if (isjob == null | approveid == null | isDoctor == null) {
//            return 0;
//        }
//        if (isDoctor()) {
//            if ("-1".equals(getLoginInfo().getData().getXiaozhan().getDati())) {
//                if (sp.getBoolean(getLoginInfo().getData().getPid()
//                        + "expertapp", false)) {
//                    return 1;
//                } else {
//                    return 0;
//                }
//            }
//            if ("0".equals(isjob) & "0".equals(isDoctor)
//                    && approveid.equals("0")) {
//                return 1;
//            } else if ("2".equals(isjob) || "1".equals(isjob)) {
//                return 2;
//            } else {
//                return 0;
//            }
//        } else {
//
//            return 0;
//        }
//    }

    /**
     * 是否是认证医生 首先判断是医生+登录接口返回isjob,非空,数值为0未认证,为1,2为认证,1是普通认证医生,2为专家医生 stone
     *
     * @return
     */
    public static boolean isDoctorApprove() {
        LoginInfo loginInfo = getLoginInfo();
        if (null != loginInfo.getData()) {
//            -1 => '审核中', -2 => '驳回',
//                    0 => '待审核', 1 => '通过', 2 => '不通过',
//                    3 => '待跟踪', 4 => '暂不开通',
            return loginInfo.getData().getAudit_status() == AUDIT_STATUS_1;
        }
        return false;
    }

    /**
     * 医生 身份  stone 新版本不这么返回数据,统一走医生逻辑
     *
     * @return 1 普通医生 2 专家医生 stone
     */
    public static int DoctorType() {

//        String isjob = getLoginInfo().getData().getIsjob();
//        if (isjob == null) {
//            return -1;
//        }
//        if (isDoctorApprove()) {
//            if ("1".equals(isjob)) {
//                return 1;
//            } else if ("2".equals(isjob)) {
//                return 2;
//            }
//        }

        return 1;
    }

    /**
     * 认证状态
     */
    public static int DoctorApproveType() {
        LoginInfo loginInfo = getLoginInfo();
        if (null != loginInfo.getData()) {
//            -1 => '审核中', -2 => '驳回',
//                    0 => '待审核', 1 => '通过', 2 => '不通过',
//                    3 => '待跟踪', 4 => '暂不开通',
            return loginInfo.getData().getAudit_status();
        }
        return -3;
    }

    /**
     * 学生是否认证
     *
     * @return
     */
//    public static boolean isStudentAppove() {
//        String isDoctor = getLoginInfo().getData().getIsdoctor();
//        return "14".equals(isDoctor);
//    }

    /**
     * 学生认证状态
     *
     * @return 1 认证中 2认证失败 0 未认证
     */
//    public static int StudentAppoveType() {
//        String isDoctor = getLoginInfo().getData().getIsdoctor();
//        String approveid = getLoginInfo().getData().getApproveid();
//
//        if ("13".equals(isDoctor) && "0".equals(approveid)) {
//            return 1;
//        } else if ("13".equals(isDoctor) && "1".equals(approveid)) {
//            return 2;
//        } else {
//            return 0;
//        }
//
//    }
    public static Map<String, Service> getServiceMark() {
        Map<String, Service> map = new HashMap<String, Service>();
        LoginInfo loginInfo = YMApplication.getLoginInfo();
        if (null != loginInfo) {
            LoginInfo.UserData userData = loginInfo.getData();
            if (null != userData) {
                ClinicStatInfo xiaozhan = userData.getXiaozhan();
                if (null != xiaozhan) {
                    List<Service> services = xiaozhan.getServices();
                    if (null != services) {
                        Service service = null;
                        for (int i = 0; i < services.size(); i++) {
                            service = services.get(i);
                            if (Constants.CLUB_EN.equals(service.work_sign)) { //问题广场
                                map.put(Constants.CLUB_CN, service);
                            } else if (Constants.FAMILYDOCTOR_EN.equals(service.work_sign)) { //家庭医生
                                map.put(Constants.FAMILYDOCTOR_CN, service);
                            } else if (Constants.ZJZIXUN_EN.equals(service.work_sign)) { //专家咨询
                                map.put(Constants.ZJZIXUN_CN, service);
                            } else if (Constants.JIAHAO_EN.equals(service.work_sign)) { //预约转诊
                                map.put(Constants.JIAHAO_CN, service);
                            } else if (Constants.DHYS_EN.equals(service.work_sign)) { //电话医生
                                map.put(Constants.DHYS_CN, service);
                            } else if (Constants.XIANXIA_EN.equals(service.work_sign)) { //患者管理
                                map.put(Constants.XIANXIA_CN, service);
                            } else if (Constants.WKYS_EN.equals(service.work_sign)) { //闻康医生
                                map.put(Constants.WKYS_CN, service);
                            } else if (Constants.IMWD_EN.equals(service.work_sign)) { //即时问答
                                map.put(Constants.IMWD_CN, service);
                            } else if (Constants.CLUB_ASSIGN_EN.equals(service.work_sign)) { //问题广场(指定)
                                map.put(Constants.CLUB_ASSIGN_CN, service);
                            } else if (Constants.IMWD_REWARD_EN.equals(service.work_sign)) { //即时问答(悬赏)
                                map.put(Constants.IMWD_REWARD_CN, service);
                            } else if (Constants.IMWD_ASSIGN_EN.equals(service.work_sign)) { //即时问答(指定)
                                map.put(Constants.IMWD_ASSIGN_CN, service);
                            } else if (Constants.CLUB_REWARD_EN.equals(service.work_sign)) { //问题广场(悬赏)
                                map.put(Constants.CLUB_REWARD_CN, service);
                            } else if (Constants.MYGH_EN.equals(service.work_sign)) { //名医挂号
                                map.put(Constants.MYGH_CN, service);
                            }
                        }
                    }
                }
            }
        }
        return map;
    }

    /**
     * 家庭医生开通
     */
    public static Map<String, String> fam_map() {
        Map<String, String> map = new HashMap<String, String>();
//        map.put("1", "已开通");
//        map.put("0", "审核中");
//        map.put("2", "审核不通过");
//        map.put("5", "未开通");
//        map.put("-1", "不可开通");

//        map.put("-1", "未开通");
        map.put("-1", "申请开通");
//        map.put("0", "待审核");
        map.put("0", "审核中");
        map.put("1", "已开通");
//        map.put("2", "关闭");     //审核不通过也给2
        map.put("2", "未通过");     //审核不通过也给2
//        map.put("3", "暂时关闭");
        map.put("3", "未通过");
        return map;
    }

    /**
     * 预约加号开通
     */
    public static Map<String, String> yuyue_map() {
        Map<String, String> map = new HashMap<String, String>();
//        map.put("1", "审核中");
//        map.put("2", "已开通");
//        map.put("3", "审核不通过");
//        map.put("4", "暂时关闭");
//        map.put("5", "未开通");
//        map.put("-1", "不可开通");
//        map.put("-2", "不可开通");

//        map.put("-1", "未开通");
        map.put("-1", "申请开通");
//        map.put("0", "待审核");
        map.put("0", "审核中");
        map.put("1", "已开通");
//        map.put("2", "关闭");
        map.put("2", "未通过");
//        map.put("3", "暂时关闭");
        map.put("3", "未通过");
        return map;
    }

    /**
     * 电话医生开通
     */
    public static Map<String, String> phone_map() {
        Map<String, String> map = new HashMap<String, String>();
//        map.put("1", "审核中");
//        map.put("2", "已开通");
//        map.put("3", "审核不通过");
//        map.put("5", "未开通");
//        map.put("-1", "不可开通");
//        map.put("-2", "不可开通");

//        map.put("-1", "未开通");
        map.put("-1", "申请开通");
//        map.put("0", "待审核");
        map.put("0", "审核中");
        map.put("1", "已开通");
//        map.put("2", "关闭");
        map.put("2", "未通过");
//        map.put("3", "暂时关闭");
        map.put("3", "未通过");
        return map;

    }

    /**
     * 即时问答
     */
    public static Map<String, String> imwd_map() {
        Map<String, String> map = new HashMap<String, String>();
//        map.put("-1", "未开通");
        map.put("-1", "申请开通");
        map.put("1", "已开通");
//        map.put("0", "待审核");
        map.put("0", "审核中");
//        map.put("2", "关闭");
        map.put("2", "未通过");
        return map;
    }

    /**
     * 问题广场
     */
    public static Map<String, String> dati_map() {
        Map<String, String> map = new HashMap<String, String>();
//        map.put("1", "已开通");
//        map.put("5", "未开通");
//        map.put("-1", "不可开通");

//        map.put("-1", "未开通");
        map.put("-1", "申请开通");
//        map.put("0", "待审核");
        map.put("0", "审核中");
        map.put("1", "已开通");
//        map.put("2", "关闭");
        map.put("2", "未通过");
//        map.put("3", "暂时关闭");
        map.put("3", "未通过");
        return map;
    }

    //=============================================================//
    public static NewsReadDataSource getNewsReadDataSource() {
        return mNewsReadDataSource;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (!AppUtils.isMainProcess(this)) {
            //非主进程直接返回
            return;
        }
        // 拦截异常 目前暂时打开 看崩溃日志 stone
//        if (!BuildConfig.DEBUG) {
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(getApplicationContext());
//        }

        // 注意： appid 必须和下载的SDK保持一致，否则会出现10407错误
        SpeechUtility.createUtility(this, "appid=" + "5af10a4d");
        // 以下语句用于设置日志开关（默认开启），设置成false时关闭语音云SDK日志打印
//        Setting.setShowLog(BuildConfig.DEBUG);


//        String jpushId = getAppMetaData(this,"JPUSH_APPKEY");
//        LogUtils.i("jpushId="+jpushId);
        RetrofitClient.init(CommonUrl.BASE_HOST, YMApplication.getAppContext(), new RetrofitClient.ICommonRespHandler() {
            @Override
            public boolean handleSuccess(Object data) {
                return false;
            }

            @Override
            public boolean handleError(Throwable e) {
                if (e instanceof ApiException) {
                    int resultCode = ((ApiException) e).getCode();
                    String resultMsg = e.getMessage();
                    if (!RetrofitCache.isResponseFromCache(e.getMessage())) {
                        //非缓存
                        if (40001 == resultCode || 40000 == resultCode) {
                            //弹出强制退出提示框
                            ForceLogoutDialogUtils.showForceDialogWithLog(resultCode, resultMsg);
                            return true;
                        }
                    }
                }
                return false;
            }
        }, new XywyInterceptor());
        instance = this;

        XywyDataRequestApi.getInstance().init(YMApplication.getInstance().getApplicationContext(), new CommonRequestParam(YMConfig.SOURCE, YMConfig.PRO, YMConfig.MD5_SIGN_KEY), new XywyHeaderHelper() {
            @Override
            public void addHeader(Request.Builder requestBuilder) {
                CommonNetUtils.addHeader(requestBuilder);
            }
        });

        //初始化图片加载工具类
        ImageLoadUtils.INSTANCE.init(this);

        // letv //
//        String processName = getProcessName(this, android.os.Process.myPid());
//        if (getApplicationInfo().packageName.equals(processName)) {
//            LeCloudPlayerConfig.getInstance().setPrintSdcardLog(true).setIsApp().setUseLiveToVod(true);//setUseLiveToVod 使用直播转点播功能 (直播结束后按照点播方式播放)
//            LeCloudProxy.init(getApplicationContext());
//        }
        LeCloudPlayerConfig.getInstance().setPrintSdcardLog(true).setIsApp().setUseLiveToVod(true);//setUseLiveToVod 使用直播转点播功能 (直播结束后按照点播方式播放)
        LeCloudProxy.init(getApplicationContext());
        //////////////////////////////////

        //===============================================================//
        mDatabaseHelper = DatabaseHelper.getInstance(getApplicationContext());

        mNewsReadDataSource = new NewsReadDataSource(mDatabaseHelper);
        //===============================================================//


        if (APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
            //微信
            PlatformConfig.setWeixin(Constants.WEIXIN_S1_YSZS, Constants.WEIXIN_S2_YSZS);
            //新浪微博
            PlatformConfig.setSinaWeibo(Constants.WEIBO_S1_YSZS, Constants.WEIBO_S2_YSZS);
        } else {
            //微信
            PlatformConfig.setWeixin(Constants.WEIXIN_S1, Constants.WEIXIN_S2);
            //新浪微博
            PlatformConfig.setSinaWeibo(Constants.WEIBO_S1, Constants.WEIBO_S2);
        }
        PlatformConfig.setQQZone(Constants.QZONE_S1, Constants.QZONE_S2);

        CommonUtils.initMobileAgent(this);
        sp = getSharedPreferences("save_user", MODE_PRIVATE);


        initImageLoad();

        registerActivityLifecycle();//检测app是否在前台

//        JPUSH_ID = sp.getString("jpush_regis_id", "");
//
        DLog.i(TAG, "极光推送id" + JPushInterface.getRegistrationID(YMApplication.getInstance()));

        //TODO stone 测试与线上要修改 动态控制
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
//        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        // 初始化极光推送 并自定义简单的通知栏
        JPushInterface.init(this);

        // 指定定制的 Notification Layout
        CustomPushNotificationBuilder builder = new
                CustomPushNotificationBuilder(YMApplication.getInstance(),
                R.layout.customer_notitfication_layout,
                R.id.icon,
                R.id.title,
                R.id.text);
        // 指定最顶层状态栏小图标
        builder.statusBarDrawable = R.drawable.jpush_notification_icon;
        // 指定下拉状态栏时显示的通知图标
        builder.layoutIconDrawable = R.mipmap.dp_icon;
        builder.notificationFlags = Notification.FLAG_SHOW_LIGHTS;  //设置为呼吸灯闪烁
        // 设置为铃声、震动、呼吸灯闪烁都要
        builder.notificationDefaults = Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE
                | Notification.DEFAULT_LIGHTS;
        JPushInterface.setPushNotificationBuilder(1, builder);

//        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(YMApplication.getInstance());
//        builder.statusBarDrawable =R.drawable.jpush_notification_icon;
//        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
//                | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
//        builder.notificationDefaults = Notification.DEFAULT_SOUND
//                | Notification.DEFAULT_VIBRATE
//                | Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
//        JPushInterface.setPushNotificationBuilder(1, builder);

//        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(getAppContext());
//        builder.notificationDefaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
//        JPushInterface.setPushNotificationBuilder(1, builder);
        manger = (NotificationManager) this
                .getSystemService(NOTIFICATION_SERVICE);
        /**
         * this function will initialize the HuanXin SDK
         *
         * @return boolean true if caller can continue to call HuanXin related
         *         APIs after calling onInit, otherwise false.
         *
         *         环信初始化SDK帮助函数
         *         返回true如果正确初始化，否则false，如果返回为false，请在后续的调用中不要调用任何和环信相关的代码
         *
         *         for example: 例子：
         *
         *         public class HXSDKHelper extends HXSDKHelper
         *
         *         HXHelper = HXSDKHelper.getInstance();
         *         if(HXHelper.onInit(context)){ // do HuanXin related work }
         */
        hxSDKHelper.onInit(getAppContext());

        SCR_W = getResources().getDisplayMetrics().widthPixels;
        SCR_H = getResources().getDisplayMetrics().heightPixels;
        ArrayList<ChannelItem> list = (ArrayList<ChannelItem>) DBManager
                .getManager().getUserChannelData();
        setChannelItemList(list);

        FinalDb fb = FinalDb.create(getBaseContext(), "coupon.db", true, 2,
                new DbUpdateListener() {

                    @Override
                    public void onUpgrade(SQLiteDatabase arg0, int arg1,
                                          int arg2) {
                        // TODO Auto-generated method stub
                        String sql = "select count(*) as c from sqlite_master where type ='table' and name ='downloadtask';";
                        Cursor myCursor = arg0.rawQuery(sql,
                                null);
                        if (myCursor.moveToFirst()) {
                            DLog.i("sql", "数据库版本表存在" + arg1 + "新的" + arg2);
                            arg0.execSQL("ALTER TABLE downloadtask  ADD commed default '0'");
                        } else {
                            DLog.i("sql", "数据库版表不存在");
                        }
                        myCursor.close();
                    }
                });


        //initBugstags();

        //stone
        sIsYSZS = YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID);
        applicationHandler = new Handler(getApplicationContext().getMainLooper());
        //stone 新添加
        ImageLoaderUtils.getInstance().init(this);


        mDaySetAll.add(0);
        mDaySetAll.add(1);
        mDaySetAll.add(2);
        mDaySetAll.add(3);
        mDaySetAll.add(4);
        mDaySetAll.add(5);
        mDaySetAll.add(6);
        //医网签环境设置
        if (BuildConfig.BJCASDK_TAG){
            BJCASDK.getInstance().setServerUrl(EnvType.PUBLIC);
        }else{
            BJCASDK.getInstance().setServerUrl(EnvType.INTEGRATE);
        }

    }

    /**
     * 获取application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
//    public String getAppMetaData(Context ctx, String key) {
//        if (ctx == null || TextUtils.isEmpty(key)) {
//            return null;
//        }
//        String resultData = null;
//        try {
//            PackageManager packageManager = ctx.getPackageManager();
//            if (packageManager != null) {
//                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
//                if (applicationInfo != null) {
//                    if (applicationInfo.metaData != null) {
//                        resultData = applicationInfo.metaData.getString(key);
//                    }
//                }
//
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        return resultData;
//    }
    public void registerActivityLifecycle() {
        if (Build.VERSION.SDK_INT >= 14) {
            registerActivityLifecycleCallbacks(
                    new Application.ActivityLifecycleCallbacks() {
                        @Override
                        public void onActivityCreated(
                                Activity activity, Bundle bundle) {

                        }

                        @Override
                        public void onActivityStarted(Activity activity) {
                            mStartedActivityCount++;
                            if (!mAppIsForeGround) {
                                mAppIsForeGround = true;
                                JPushInterface.setPushTime(YMApplication.getInstance(), mDaySetClear, 0, 23);
                            }
                        }

                        @Override
                        public void onActivityResumed(final Activity activity) {
//                            //stone
                            if (mPopupWindowMap.containsKey(String.valueOf(activity.hashCode()))
                                    && (mPopupWindow = mPopupWindowMap.get(String.valueOf(activity.hashCode()))) != null) {
                                YMApplication.applicationHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (activity != null
                                                && !activity.isFinishing()
                                                && mPopupWindowMap.containsKey(String.valueOf(activity.hashCode()))
                                                && (mPopupWindow = mPopupWindowMap.get(String.valueOf(activity.hashCode()))) != null) {
                                            mPopupWindow.setAnimationStyle(R.style.PopupDownAnimationForNewMessage);
                                            mPopupWindow.update();
                                        }
                                    }
                                }, 200);
                            }
                        }

                        @Override
                        public void onActivityPaused(Activity activity) {
//                            //stone
                            if (activity != null
                                    && !activity.isFinishing()
                                    && mPopupWindowMap.containsKey(String.valueOf(activity.hashCode()))
                                    && (mPopupWindow = mPopupWindowMap.get(String.valueOf(activity.hashCode()))) != null) {
                                if (mPopupWindow.isShowing()) {
                                    mPopupWindow.setAnimationStyle(0);
                                    mPopupWindow.update();
                                }
                            }
                        }

                        @Override
                        public void onActivityStopped(Activity activity) {
                            mStartedActivityCount--;
                            if (mStartedActivityCount == 0) {
                                if (mAppIsForeGround) {
                                    mAppIsForeGround = false;
                                    JPushInterface.setPushTime(YMApplication.getInstance(), mDaySetAll, 0, 23);
                                }
                            }
                        }

                        @Override
                        public void onActivitySaveInstanceState(
                                Activity activity, Bundle bundle) {

                        }

                        @Override
                        public void onActivityDestroyed(Activity activity) {
                            //stone
                            if (mPopupWindowMap.containsKey(String.valueOf(activity.hashCode()))
                                    && (mPopupWindow = mPopupWindowMap.get(String.valueOf(activity.hashCode()))) != null
                                    && mPopupWindow.isShowing()) {
                                mPopupWindow.dismiss();
                                mPopupWindow = null;
                            }
                        }
                    });

        }
    }

    //    /**
//     * 初始化Bugstags
//     */
//    private void initBugstags() {
//        final String BUGTAGS_APP_KEY = "2ee6086adc2bc223e122e1abd3edb16a";
//        BugtagsOptions options = new BugtagsOptions.Builder().
//                trackingLocation(false).//是否获取位置
//                trackingCrashLog(true).//是否收集crash
//                trackingConsoleLog(true).//是否收集console log
//                trackingUserSteps(true).//是否收集用户操作步骤
//                build();
//
//        //BTGInvocationEventBubble(悬浮小球)
//        //BTGInvocationEventShake(摇一摇)
//        //BTGInvocationEventNone(静默)
//        Bugtags.start(BUGTAGS_APP_KEY, this, Bugtags.BTGInvocationEventNone, options);
//
//
//    }

    public MediaList getMediaList() {
        return mediaList;
    }

    public void setMediaList(MediaList mediaList) {
        if (mediaList == null) {
            return;
        }
        this.mediaList = mediaList;
    }

    public List<Notice> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(List<Notice> noticeList) {
        if (noticeList == null) {
            return;
        }
        this.noticeList = noticeList;
    }

    public void setHasNewNotice(boolean hasNewNotice) {
        this.hasNewNotice = hasNewNotice;
    }

    public boolean hasNewNotice() {
        return hasNewNotice;
    }

    public List<ChannelItem> getChannelItemList() {
        return channelItemList;
    }

    public void setChannelItemList(List<ChannelItem> channelItemList) {
        this.channelItemList = channelItemList;
    }

    public List<QueData> getmLables() {
        return mLables;
    }

    public void setmLables(List<QueData> mLables) {
        this.mLables = mLables;
    }

    /**
     * 获取当前登陆用户名
     *
     * @return
     */
    public String getUserName() {
        return hxSDKHelper.getCurrentUsernName();
    }

    /**
     * 设置用户名
     */
    public void setUserName(String username) {
        hxSDKHelper.setCurrentUserName(username);
    }

    /**
     * 获取密码
     *
     * @return
     */
    public String getPassword() {
        return hxSDKHelper.getPassword();
    }

    /**
     * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
     * 内部的自动登录需要的密码，已经加密存储了
     *
     * @param pwd
     */
    public void setPassword(String pwd) {
        hxSDKHelper.setPassword(pwd);
    }


    private void initImageLoad() {

        File cacheDir = getCacheDir();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .denyCacheImageMultipleSizesInMemory()
                .threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY - 1)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheExtraOptions(480, 800)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheSize(10 * 1024 * 1024)
                .diskCacheExtraOptions(480, 800, null)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .imageDownloader(new BaseImageDownloader(this))
                //TODO stone 测试与线上要修改
//                .writeDebugLogs()// Remove for release app
                .build();
        ImageLoader.getInstance().init(config);

    }

    public File getExternalStorageDirectory() {
        File externalStorageDirectory = Environment
                .getExternalStorageDirectory();
        int sdk = Integer.valueOf(Build.VERSION.SDK);
        if (sdk >= 8 && externalStorageDirectory == null) {
            externalStorageDirectory = getExternalFilesDir(null);
        }
        if (externalStorageDirectory != null) {
            externalStorageDirectory = new File(
                    externalStorageDirectory.getAbsolutePath(), "xywy");
            if (!externalStorageDirectory.exists()) {
                if (!externalStorageDirectory.mkdirs()) {
                    externalStorageDirectory = null;
                }
            }
        }
        return externalStorageDirectory;
    }

    public DownFileItemInfo getStopOrStartDownloadfileItem() {
        return stopOrStartDownloadfileItem;
    }

    public void setStopOrStartDownloadfileItem(
            DownFileItemInfo stopOrStartDownloadfileItem) {
        this.stopOrStartDownloadfileItem = stopOrStartDownloadfileItem;
    }

    public List<DownFileItemInfo> getDownloadItems() {
        return downloadItems;
    }

    public void setDownloadItems(List<DownFileItemInfo> downloadItems) {
        this.downloadItems = downloadItems;
    }

    public void addNotificationId(int type, int notiId) {
        List<Integer> notifList = null;
        if (notificMap.containsKey(type)) {
            notifList = notificMap.get(type);
            notifList.add(notiId);
        } else {
            notifList = new ArrayList<Integer>();
            notifList.add(notiId);
            notificMap.put(type, notifList);
        }

        int size = notificMap.get(type).size();

        DLog.i(TAG, "SIZE=" + size);

        for (int i = 0; i < size; i++) {
            DLog.i(TAG, "addnofificId=" + notificMap.get(type).get(i));
        }
    }

    public void remoNotification(int type) {
        if (!notificMap.containsKey(type)) {
            return;
        }
        int size = notificMap.get(type).size();

        for (int i = 0; i < size; i++) {
            DLog.i(TAG, "nofificId=" + notificMap.get(type).get(i));
            manger.cancel(notificMap.get(type).get(i));
        }
        notificMap.remove(type);
    }

    public List<DownFileItemInfo> getNewDownloadItems() {
        return newDownloadItems;
    }

    public void setNewDownloadItems(List<DownFileItemInfo> newDownloadItems) {
        this.newDownloadItems = newDownloadItems;
    }

    public void hxLogout(EMCallBack emCallBack) {
        hxSDKHelper.logout(emCallBack);
    }

    //stone 新添加
    public void hxLogout(boolean unbind, EMCallBack emCallBack) {
        hxSDKHelper.logout(unbind, emCallBack);
    }

    @Override
    protected void onAppExit() {
        YMUserService.setIsGuest(true);
        disconnectWebSocket();
    }


    @Override
    public void onActivityDestroyed(Activity activity) {
        super.onActivityDestroyed(activity);
        RxBus.getDefault().removeSubscriptions(activity);
    }

    @Override
    protected void onAppFrontStateChange(boolean isFront) {
        super.onAppFrontStateChange(isFront);
        if (isFront) {
            SharedPreferences sp = getSharedPreferences("save_user", MODE_PRIVATE);
            String passWord = sp.getString("pass_word", "").trim();
            String userId = sp.getString("userid", "").trim();
            if(!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(passWord)){
                XywyIMService.getInstance().connect(BuildConfig.WEBSOCKET_URL_ERLANG,Constants.BSID_RTQA,YMConfig.SOURCE,Long.parseLong(userId),passWord,2);
            }

            if (null == webSocketApi || !webSocketApi.isConnected()) {
                initWebSocket();
            }
//            //在线诊室 stone
//            if (null == wsApi || !wsApi.isConnected()) {
//                initWebSocketForOnline();
//            }
        } else {
            XywyIMService.getInstance().closeNew();

            if (null != webSocketApi && webSocketApi.isConnected()) {
                webSocketApi.disconnect();
            }
//            //在线诊室 stone
//            if (null != wsApi && wsApi.isConnected()) {
//                wsApi.disconnect();
//            }
        }
    }


    /**
     * 断开socket链接
     */
    public void disconnectWebSocket() {
        if (null != webSocketApi) {
            webSocketApi.disconnect();
        }
//        //在线诊室 stone
//        if (null != wsApi) {
//            wsApi.disconnect();
//        }
    }

    /**
     * 初始化socket
     */
    public void initWebSocket() {
        if (!YMUserService.isGuest() && 1 == YMApplication.getInstance().isOnLineConsultUser) {
            initWebSocket(ConsultConstants.WEBSOCKET_ADDRESS, YMUserService.getCurUserId());
        }
    }

//    /**
//     * 初始化socket 在线诊室 stone
//     */
//    public void initWebSocketForOnline() {
//        //TODO 在线诊室 stone 这里需要判断医生是否满足条件
//        initNewWebSocket("新的ws地址", YMUserService.getCurUserId());
//
//    }

    /**
     * 初始化socket
     *
     * @param webSocketUrl
     * @param curUserId
     */
    private void initWebSocket(String webSocketUrl, String curUserId) {
        webSocketApi = WebSocketApi.INSTANCE.startSocket(webSocketUrl, curUserId);
    }

//    /**
//     * 在线诊室初始化socket
//     *
//     * @param webSocketUrl
//     * @param curUserId
//     */
//    private void initNewWebSocket(String webSocketUrl, String curUserId) {
//        wsApi = WSApi.INSTANCE.startSocket(webSocketUrl, curUserId);
//    }

    public void setDoctorInfo(DoctorInfoBean doctorInfoBean) {
        this.doctorInfoBean = doctorInfoBean;
    }

    public DoctorInfoBean getDoctorInfo() {
        return this.doctorInfoBean;
    }

    //用药助手添加

    /**
     * 医生信息是否同步成功
     *
     * @param syncInfoSuccess
     */
    public void setSyncInfoResult(boolean syncInfoSuccess, String syncInfoMsg) {
        this.mSyncInfoSuccess = syncInfoSuccess;
        this.mSyncInfoMsg = syncInfoMsg;
    }

    public boolean getSyncInfoResult() {
        return mSyncInfoSuccess;
    }

    public String getSyncInfoMsg() {
        return mSyncInfoMsg;
    }

    public void setSellDrugResult(boolean canSellDrug, String canSellDrugMsg) {
        this.mCanSellDrug = canSellDrug;
        this.mCanSellDrugMsg = canSellDrugMsg;
    }

    //是否能售药
    public boolean getSellDrugResult() {
        return this.mCanSellDrug;
    }

    //获取是否能售药的提示信息
    public String getSellDrugMsg() {
        return this.mCanSellDrugMsg;
    }

    //设置是否同步过医生信息
    public void setHasSyncInfo(boolean hasSyncInfo) {
        this.mHasSyncInfo = hasSyncInfo;
    }

    public boolean getHasSyncInfo() {
        return this.mHasSyncInfo;
    }

    public boolean getHasSellDrug() {
        return this.mHasSellDrug;
    }

    public void setHasSellDrug(boolean hasSellDrug) {
        this.mHasSellDrug = hasSellDrug;
    }

    //用药助手添加


    /**
     * 获取用户信息 stone
     *
     * @return
     */
    public static LoginInfo_New getUserInfo() {
        if (sUserInfo == null || sUserInfo.user_id == 0) {
            //stone 共享文件名是 用户的唯一标志 pid
            if (mSP4Pid == null) {
                mSP4Pid = YMApplication.getInstance().getSharedPreferences(YMApplication.getLoginInfo().getData().getPid(), MODE_PRIVATE);
            }
            //stone 可能返回的logins为null 直接引用会导致空指针bug
            if (!TextUtils.isEmpty(mSP4Pid.getString("ym_user_info", ""))) {
                sUserInfo = GsonUtils.toObj(mSP4Pid.getString("ym_user_info", ""), LoginInfo_New.class);
            }
        }

        return sUserInfo;

    }

    public static void setUserInfo(LoginInfo_New userInfo) {
        //将用户信息保存到pid共享文件中 stone
        mSP4Pid = YMApplication.getInstance().getSharedPreferences(YMApplication.getLoginInfo().getData().getPid(), MODE_PRIVATE);
        mSP4Pid.edit().putString("ym_user_info", GsonUtils.toJson(userInfo)).apply();
        sUserInfo = userInfo;
    }


    //第一次使用科大讯飞语音提示
    public static boolean getFirstUseKdxf() {
        if (firstUseKdxf) {
            firstUseKdxf = sp.getBoolean("first_use_kdxf", true);
        }
        return firstUseKdxf;
    }

    public static void setFirstUseKdxf(boolean first_use_kdxf) {
        firstUseKdxf = first_use_kdxf;
        sp.edit().putBoolean("first_use_kdxf", firstUseKdxf).apply();
    }

    //给一个默认值
    public static int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            statusBarHeight = sp.getInt("status_bar_height", 0);
        }
        return statusBarHeight;
    }

    public static void setStatusBarHeight(int statusBarHeight) {
        if (YMApplication.statusBarHeight == 0) {
            YMApplication.statusBarHeight = statusBarHeight;
            sp.edit().putInt("status_bar_height", statusBarHeight).apply();
        }
    }


    //即时问答 留言设置的引导提示
    public static boolean getIsFirstShowOnlineNotice() {
        return sp.getBoolean("is_first_show_online_notice", true);
    }

    public static void setIsFirstShowOnlineNotice(boolean is_first_show_online_notice) {
        sp.edit().putBoolean("is_first_show_online_notice", is_first_show_online_notice).apply();
    }

    public void showPop4NewMessage(final String bsid, final int type) {
        final Activity activity = YMApplication.getTopActivity();
        if (activity != null && !activity.isFinishing()) {
            if (mPopupWindowMap.containsKey(String.valueOf(activity.hashCode()))
                    && (mPopupWindow = mPopupWindowMap.get(String.valueOf(activity.hashCode()))) != null) {
                L.d("来自map");
                if (mPopupWindow.isShowing()) {
                    return;
                }
            } else {
                View popRoot = View.inflate(activity, R.layout.pop_layout_new_message, null);
                TextView msgtv = (TextView) popRoot.findViewById(R.id.msg_tv);
                if (bsid.equals("hlwyy")){
                    msgtv.setText("您有新的问诊订单");
                }

                View lay_new_message = popRoot.findViewById(R.id.lay_new_message);

                mPopupWindow = new PopupWindow(popRoot, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                lay_new_message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mPopupWindow != null) {
                            mPopupWindow.dismiss();
                        }
                        if (bsid.equals(Constants.BSID_RTQA)){
                            ConsultOnlineActivity.startActivity(activity);
                            //新消息查看 stone
                            StatisticalTools.eventCount(activity, Constants.VIEWNEWMESSAGES);
                        } else if (bsid.equals("ylh")){
                            activity.startActivity(new Intent(activity,PatientManagerActivity.class));

                        }else if (bsid.equals("hlwyy")){
                                activity.startActivity(new Intent(activity, OnlineRoomActivity.class).putExtra("pageItem",1));
                        }

                    }
                });
                mPopupWindow.setAnimationStyle(R.style.PopupDownAnimationForNewMessage);
                //注意这三个属性必须同时设置
                mPopupWindow.setFocusable(false);//这里必须设置为true才能点击区域外或者消失
                mPopupWindow.setOutsideTouchable(false);//点击外部区域消失
//              mPopupWindow.setBackgroundDrawable(new PaintDrawable(Color.TRANSPARENT));
                mPopupWindow.setBackgroundDrawable(null);

                mPopupWindow.setTouchable(true);//这个控制PopupWindow内部控件的点击事件

                mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        mPopupWindowMap.remove(String.valueOf(activity.hashCode()));
                        mPopupWindow = null;
                    }
                });
            }

            if (activity != null && !activity.isFinishing() && !mPopupWindow.isShowing()) {
                activity.getWindow().getDecorView().post(new Runnable() {
                    @Override
                    public void run() {
                        if (activity != null && !activity.isFinishing() && !mPopupWindow.isShowing()) {
                            mPopupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.TOP | Gravity.LEFT, ScreenUtils.getScreenWidth(activity) - mPopupWindow.getWidth(), ScreenUtils.getScreenHeight(activity) - DensityUtils.dp2px(150));
                            mPopupWindowMap.put(String.valueOf(activity.hashCode()), mPopupWindow);
                            L.d("show");

                            YMApplication.applicationHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (activity != null && !activity.isFinishing() && mPopupWindow != null && mPopupWindow.isShowing()) {
                                        mPopupWindow.dismiss();
                                    }
                                }
                            }, 5000);
                        }
                    }
                });
            }
        }
    }
}
