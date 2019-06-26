package com.xywy.askforexpert.appcommon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.MainActivity;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.activity.WebViewActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.JudgeNetIsConnectedReceiver;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.ClinicStatInfo;
import com.xywy.askforexpert.model.LoginInfo;
import com.xywy.askforexpert.model.LoginInfo_New;
import com.xywy.askforexpert.model.PersonInfo;
import com.xywy.askforexpert.model.doctor.User;
import com.xywy.askforexpert.model.liveshow.LiveShowStateInfoEntity;
import com.xywy.askforexpert.module.discovery.medicine.CertificationAboutRequest;
import com.xywy.askforexpert.module.discovery.medicine.common.Callback;
import com.xywy.askforexpert.module.liveshow.constants.H5PageUrl;
import com.xywy.askforexpert.module.my.account.LoginActivity;
import com.xywy.askforexpert.module.my.account.accountconfig.AccountSPUtils;
import com.xywy.askforexpert.module.my.account.request.AccountRequest;
import com.xywy.askforexpert.module.websocket.WebSocketApi;
import com.xywy.base.view.ProgressDialog;
import com.xywy.easeWrapper.EMChatManager;
import com.xywy.easeWrapper.db.InviteMessage;
import com.xywy.easeWrapper.db.InviteMessgeDao;
import com.xywy.im.sdk.XywyIMService;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.xywy.sdk.stats.MobileAgent;
import com.xywy.util.SharedPreferencesHelper;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import me.leolin.shortcutbadger.ShortcutBadger;

import static android.content.Context.MODE_PRIVATE;
import static com.xywy.askforexpert.YMApplication.getLoginInfo;
import static com.xywy.askforexpert.appcommon.UserType.AllCheckAprove;
import static com.xywy.askforexpert.appcommon.UserType.AllCheckType;
import static com.xywy.askforexpert.appcommon.utils.ToastUtils.shortToast;

/**
 * 用户登录+环信登录 用户信息获取(用户id 名字 头像...) stone
 * Created by bailiangjin on 16/5/9.
 */
public class YMUserService {

    static int userType;
    private static PersonInfo perInfo;
    private static volatile boolean isGuest;
    private final static String ISFISTSHOWZHUANZHEN = "isFistShowZhuanZhen";
    private final static String ISFISTSHOWCHULIWANCHENG = "isFistShowChuLiWanCheng";

    public static PersonInfo getPerInfo() {
        return perInfo;
    }

    public static void setPerInfo(PersonInfo perInfo) {
        YMUserService.perInfo = perInfo;
    }

    public static boolean isGuest() {
        return isGuest;
    }

//    public static void initLiveShowData(final Activity activity, final ProgressDialog dialog) {
//        WWSXYWYService.getliveStatusInfo(YMUserService.getCurUserId(), new CommonResponse<LiveShowStateInfoBean>() {
//            @Override
//            public void onNext(LiveShowStateInfoBean liveShowStateInfoBean) {
//
//                if (null != liveShowStateInfoBean) {
//                    //YMApplication.setLiveShowStateInfoEntity(liveShowStateInfoBean.getData());
//                    toMainPage(activity, dialog);
//                    return;
//                }
//                dialog.closeProgersssDialog();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                super.onError(e);
//                dialog.closeProgersssDialog();
//            }
//        });
//    }

    private static void toMainPage(Activity activity, ProgressDialog dialog) {
        Intent mainIntent = new Intent(activity, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mainIntent.putExtra("jpush", true);
        activity.startActivity(mainIntent);

        if (dialog != null) {
            dialog.closeProgersssDialog();
        }

        //stone 去掉这些 现在的推送逻辑改变了
//        if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
//            //发广播
//            Intent intent = new Intent();
//            intent.setAction(BuildConfig.APPLICATION_ID);
//            intent.putExtra("jpush", true);
//            activity.sendBroadcast(intent);
//            dialog.closeProgersssDialog();
//            activity.finish();
//        } else {
//            //发广播
//            Intent intent = new Intent();
//            intent.setAction("com.main.pushchange");
//            intent.putExtra("jpush", true);
//            activity.sendBroadcast(intent);
//            dialog.closeProgersssDialog();
//            activity.finish();
//        }
    }

    public static boolean checkLiveShowState(Activity activity) {
        if (!isLoginUser(activity)) {
            return false;
        }
        if (isLiveShowUser()) {
            return true;
        }

        shortToast("您还未开通直播功能请先开通直播功能");
        // 2017/3/8 跳转到开通直播页 h5

        WebViewActivity.start(activity, "直播申请", H5PageUrl.APPLY_FOR_LIVE_SHOW_URL);

        return false;

    }

    public static boolean isLiveShowUser() {

        LiveShowStateInfoEntity liveShowStateInfoEntity = YMApplication.getLiveShowStateInfoEntity();
        if (null == liveShowStateInfoEntity) {
            return false;
        }
        return 1 == liveShowStateInfoEntity.getIs_Anchor();
    }

    public static void setIsGuest(boolean isGuest) {
        YMUserService.isGuest = isGuest;
        if (!isGuest) {
            getPersonInfo();
        }
    }

    public static boolean isStudent() {

        if (isGuest()) {
            return false;
        }
        if (YMApplication.isDoctor()) {
            return false;
        }
        return true;
    }

    /**
     * 获取用户token
     *
     * @return
     */
    public static String getUserToken() {
        String token;
        if (isGuest()) {
            token = "";
        } else {
            token = getLoginInfo().getData().getToken();
        }
        LogUtils.d("token:" + token);
        return token;
    }

    /**
     * 获取用户头像图片url
     *
     * @return
     */
    public static String getCurUserHeadUrl() {
        String headUrl;
        if (isGuest()) {
            headUrl = null;
        } else {
            headUrl = getLoginInfo().getData().getPhoto();
        }
        LogUtils.d("headUrl:" + headUrl);
        return headUrl;
    }

    /**
     * 获取用户id
     *
     * @return
     */
    public static String getCurUserId() {
        String userId;
        if (isGuest()) {
            userId = "0";
        } else {
            userId = getLoginInfo().getData().getPid();
        }
        LogUtils.d("answerUserId:" + userId);
        return userId;
    }

    /**
     * 获取用户名
     *
     * @return
     */
    public static String getCurUserName() {
        if (isGuest()) {
            return "未注册用户";
        }

        String curUserName = getLoginInfo().getData().getRealname();
        LogUtils.d("curUserName:" + curUserName);
        return curUserName;
    }

    public static User getNewCurUser() {

        User newUser = new User();
        newUser.userid = getCurUserId();
        newUser.nickname = getCurUserName();
        newUser.job = getLoginInfo().getData().getJob();
        newUser.department = getLoginInfo().getData().getSubjectName();
        newUser.photo = getLoginInfo().getData().getPhoto();

        return newUser;
    }

    /**
     * 检验当前用户是否为已登录用户 已注册用户为有效用户 Guest体验用户 为无效用户
     *
     * @param activity
     * @return true：正常用户 false：无效用户 弹框提示跳转登录页
     */
    public static boolean isLoginUser(Activity activity) {
        if (isGuest()) {
            DialogUtil.LoginDialog(activity);
            return false;
        } else {
            return true;
        }
    }

//    /**
//     * 校测当前用户 医生信息是否完整 工具类
//     *
//     * @param activity
//     * @param todoStr  提示内容为： "需要完善姓名、职称、医院、科室等信息，才能+todoStr" 传空时 todoStr 默认值为： "执行后续操作"
//     * @return true：用户信息完整 false：用户信息不完整：弹框跳转完善信息页
//     */
//    public static boolean isUserInfoComplete(final Activity activity, String todoStr) {
//
//        //检验是否登录 如果未登录 去登录
//        if (!isLoginUser(activity)) {
//            return false;
//        }
//        String msgInfo;
//        if (YMApplication.isDoctor()) {
//            msgInfo = "需要完善姓名、职称、医院、科室等信息，才能";
//        } else {
//            msgInfo = "需要完善姓名、学校、专业等信息，才能";
//        }
//        msgInfo += todoStr;
//
//        //已登录 检测信息完整性
//        if (YMApplication.isInfoComplete()) {
//            return true;
//        } else {
//            DialogUtil.showCompleteInfoDialog(null, activity, msgInfo);
////            new XywyPNDialog.Builder().setContent(noticeContent)
////                    .setPositiveStr("去完善")
////                    .setNegativeStr("取消")
////                    .create(activity, new PNDialogListener() {
////                        @Override
////                        public void onPositive() {
////                            Intent intent = new Intent(activity, PersonInfoActivity.class);
////                            intent.putExtra("doctorInfo", "doctorInfo");
////                            activity.startActivity(intent);
////                        }
////
////                        @Override
////                        public void onNegative() {
////
////                        }
////                    }).show();
//            return false;
//        }
//    }

//    /**
//     * 校测当前用户 医生是否认证过,是否是登录状态
//     */
//    public static boolean isUserApproved(final Activity activity) {
//
//        //检验是否登录 如果未登录 去登录
//        if (!isLoginUser(activity)) {
//            return false;
//        }
//
//        if (YMApplication.isDoctorApprove()) {
//            return true;
//        } else {
//            DialogUtil.showUserCenterCertifyDialog(activity, null, null, null);
//            return false;
//        }
//    }

    //用户类型 stone
    public static int getUserType() {
        userType = UserType.none;
        LoginInfo.UserData user = getLoginInfo().getData();
        SharedPreferences sp = YMApplication.getAppContext().getSharedPreferences("save_user", MODE_PRIVATE);
        if (!isGuest()) {//  登陆
            setUserType(UserType.login);
            if (YMApplication.isDoctor()) {//医生
                if (YMApplication.DoctorType() == 1) {//普通认证医生
                    setUserType(UserType.Approved | UserType.Doctor);
                } else if (YMApplication.DoctorType() == 2) {// 专家
                    if ("-1".equals(user.getXiaozhan().getDati())) {
                        if (sp.getBoolean(getLoginInfo().getData().getPid()
                                + "expertapp", false)) {
                            setUserType(UserType.UnApproved); //0 未认证 1 认证中 2 认证
                        } else {
                            setUserType(UserType.Approving);//0 未认证 1 认证中 2 认证
                        }
                    } else {//已认证专家
                        setUserType(UserType.Approved);
                    }
                    setUserType(UserType.Profession);
                } else {
                    setUserType(UserType.Doctor);
                    if ("0".equals(user.getIsjob()) & "0".equals(user.getIsdoctor()) && "0".equals(user.getApproveid())) {// 医生认证中
                        setUserType(UserType.Approving);
                    } else {//未认证
                        setUserType(UserType.UnApproved);
                    }
                }
            } else {// 医学生
                setUserType(UserType.Student);
                if ("14".equals(user.getIsdoctor())) {// 认证成功的医学生
                    setUserType(UserType.Approved);
                } else {
                    if ("13".equals(user.getIsdoctor()) && "0".equals(user.getApproveid())) {//认证中的医学生
                        setUserType(UserType.Approving);
                    } else if ("13".equals(user.getIsdoctor()) && "1".equals(user.getApproveid())) {//认证失败
                        setUserType(UserType.ApproveFailed);
                    } else {//未认证
                        setUserType(UserType.UnApproved);
                    }
                }
            }
        } else {//游客

        }
        return userType;
    }

    private static void setUserType(int i) {
        userType = userType | i;
    }

    //检查当前用户 stone
    public static boolean checkUserId(Context context, int alowedUser) {
        return checkUserId(context, alowedUser, true);
    }

    public static boolean checkUserId(Context context, int alowedUser, boolean showDialog) {
        int currentType = getUserType();

        //是否需要判断登录
        if ((alowedUser & UserType.login) == UserType.login) {
            if (isGuest()) {
                if (showDialog) {
                    DialogUtil.LoginDialog(context);
                }
                return false;
            }
        }
        //需要判断医生类型
        if ((alowedUser & AllCheckType) != UserType.none) {
            if (((alowedUser & AllCheckType) & currentType) == UserType.none) {
                if (showDialog) {
                    CommonUtils.showApproveDialog(context, "请先通过专业认证");
                }
                return false;
            }
        }
        //需要判断认证情况
        if ((alowedUser & AllCheckAprove) != UserType.none) {
            if (((alowedUser & AllCheckAprove) & currentType) == UserType.none) {
                if (showDialog) {
                    CommonUtils.showApproveDialog(context, "请先通过专业认证");
                }
                return false;
            }
        }
        return true;
    }

    public static boolean isQuestionSquareOpen() {
        LoginInfo login1 = YMApplication.getLogin1();
        if (null != login1) {
            LoginInfo.UserData userData = login1.getData();
            if (null != userData) {
                ClinicStatInfo clinicStatInfo = userData.getXiaozhan();
                if (null != clinicStatInfo) {
                    if ("1".equals(clinicStatInfo.getDati())) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean isImwdOpen() {
        LoginInfo login1 = YMApplication.getLogin1();
        if (null != login1) {
            LoginInfo.UserData userData = login1.getData();
            if (null != userData) {
                if (1 == userData.getImwd()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    public static boolean isRecognizedUser() {
//        String isJob = YMApplication.getLoginInfo().getData().getIsjob();
//
//        return "1".equals(isJob) || "2".equals(isJob);
        return true;//由于5.3.1版本不区分专家医生和普通医生,所以这里直接返回true
    }

    /**
     * 请求动态数和关注数
     */
    private static void getPersonInfo() {
        String status = "get";
        String userid = getCurUserId();
        String sign = CommonUtils.computeSign(userid + status);
        AjaxParams params = new AjaxParams();
        params.put("command", "info");
        params.put("status", status);
        params.put("userid", userid);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        DLog.i("info_follow", CommonUrl.DP_COMMON + params.toString());
        fh.post(CommonUrl.DP_COMMON, params, new AjaxCallBack() {

            @Override
            public void onSuccess(String t) {
                if (t != null) {
//                    DLog.i("info_follow", t);
                    perInfo = ResolveJson.R_personinfo(t);
                    super.onSuccess(t);
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }

        });
    }

    public static void autoLogin(final Activity context) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 如果用户名密码都有，直接进入主页面
                final SharedPreferences sp1 = context.getSharedPreferences("save_user", MODE_PRIVATE);
                String username = sp1.getString("user_name", "").trim();
                String password = sp1.getString("pass_word", "").trim();
                if (password.length() > 100) {   //这里选取长度为100，是因为加密后的密码的长度都是127的，一般未加密的密码不可能有这么长的长度
                    //老的接口保存到本地的加密后的密码，由于新的接口的密码不需要加密，所以，这里
                    //将保存到本地的经过加密后的密码清空，让用户自己重新输入一次密码
                    sp1.edit().putString("pass_word", "").commit();
                    startLoginPage(context);
                    return;
                }
                if (AccountSPUtils.isAccountLogined(username)) {
                    if (JudgeNetIsConnectedReceiver.judgeNetIsConnected(context)) {
//                        YMUserService.ymLogin(context, username, password);
                        YMUserService.ymLogin_New(context, username, password);
                        return;
                    }
                }
                //如果登录的是新账号，则将之前的通知清除
                ShortcutBadger.applyCount(context, 0);
                JPushInterface.clearAllNotifications(context);
                YMApplication.sPushExtraBean = null;
                startLoginPage(context);
            }
        });

    }

    /**
     * 环信登出 stone
     */
    public static void huanxinLogout() {
        //登陆过
        if (EMClient.getInstance().isLoggedInBefore()) {
            YMApplication.getInstance().hxLogout(new EMCallBack() {
                @Override
                public void onSuccess() {
                    LogUtils.d("环信登出成功 首次");
                }

                @Override
                public void onError(int i, String s) {
                    LogUtils.d("环信登出失败 重新登出");
                    YMApplication.applicationHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            YMApplication.getInstance().hxLogout(false, new EMCallBack() {
                                @Override
                                public void onSuccess() {
                                    LogUtils.d("环信登出成功 第二次");
                                }

                                @Override
                                public void onError(int i, String s) {
                                    LogUtils.d("环信登出失败 第二次");
                                }

                                @Override
                                public void onProgress(int i, String s) {
                                }
                            });
                        }
                    });
                }

                @Override
                public void onProgress(int i, String s) {
                }
            });
        }
    }

    /**
     * 退出登录,清空数据
     */
    public static void ymLogout() {
//
//        final ProgressDialog logoutProgressDialog = new ProgressDialog(activity,
//                "正在退出中...");
//        logoutProgressDialog.setCanceledOnTouchOutside(false);
//        logoutProgressDialog.showProgersssDialog();
        InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(
        );
        inviteMessgeDao.deleteAllMessage();
        List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
        if (!TextUtils.isEmpty(YMApplication.msgnum)) {
            YMApplication.msgnum = 0 + "";
        }
        if (!TextUtils.isEmpty(YMApplication.addFriendNum)) {
            YMApplication.addFriendNum = 0 + "";
        }
        for (int i = 0; i < msgs.size(); i++) {
            inviteMessgeDao.deleteMessage(msgs.get(i).getFrom());
        }

        //环信登出 old stone
//        YMApplication.getInstance().hxLogout(null);
        //环信登出 new stone
        huanxinLogout();

        //将java版本的websocket断开连接
        WebSocketApi webSocketApi = YMApplication.getInstance().getWebSocketApi();
        if (null != webSocketApi && webSocketApi.isConnected()) {
            webSocketApi.disconnect();
        }

        //将erlang版本的websocket断开连接
        XywyIMService.getInstance().closeNew();

        AccountSPUtils.logout();

        setIsGuest(true);//登出后，则是游客了
        //登出后，也要将极光的别名重置
        JPushInterface.setAliasAndTags(YMApplication.getInstance(), "", new LinkedHashSet<String>(), new

                TagAliasCallback() {

                    @Override
                    public void gotResult(int code, String alias, Set<String> tags) {
                        String logs;
                        switch (code) {
                            case 0:
                                logs = "Set tag and alias success";
                                LogUtils.i("Jpush" + logs);
                                break;

                            case 6002:
                                logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                                LogUtils.i("Jpush" + logs);
                                if (NetworkUtil.isNetWorkConnected()) {
                                    // mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS,
                                    // tags), 1000 * 60);
                                } else {
                                    LogUtils.i("Jpush" + "No network");
                                }
                                break;

                            default:
                                logs = "Failed with errorCode = " + code;
                                LogUtils.i("Jpush" + logs);
                        }

                        // ExampleUtil.showToast(logs, getApplicationContext());
                    }

                });
        //登出后，在次登录，仍然算是第一次进入app
        SharedPreferences.Editor login_sp = YMApplication.getInstance().getSharedPreferences("login", Context.MODE_PRIVATE).edit();
        login_sp.putBoolean(ISFISTSHOWZHUANZHEN, true);
        login_sp.putBoolean(ISFISTSHOWCHULIWANCHENG, true);
        login_sp.commit();
        //解决 登录没有开通在线咨询的账号，再次登录开通了在线咨询的账号，没有显示在线咨询
        YMApplication.getInstance().isOnLineConsultUser = -1;
        //药品助手添加,
        // 退出登录时，将是否调用过同步医生接口的标记重置为false
        YMApplication.getInstance().setHasSyncInfo(false);
        // 退出登录时，将设置的同步医生信息的结果的标记重置为false
        YMApplication.getInstance().setSyncInfoResult(false, "");
        // 退出登录时，将调用过检测医生是否具有售药权限接口的标记重置为false
        YMApplication.getInstance().setHasSellDrug(false);
        // 退出登录时，将设置的检测医生是否具有售药权限接口的结果的标记重置为false
        YMApplication.getInstance().setSellDrugResult(false, "");
        //清空用药助手中的sp的数据
        SharedPreferencesHelper.getIns(YMApplication.getInstance()).clear();
        //药品助手添加
        YMApplication.getInstance().appExit();

        // 重新显示登陆页面
        Intent intent = new Intent(YMApplication.getAppContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        YMApplication.getInstance().startActivity(intent);

    }

    public static void ymLogin_New(final Activity context, final String currentUsername, final String currentPassword) {
        //获取用于保存用户登录信息的save_user共享文件
        final SharedPreferences sp1 = context.getSharedPreferences("save_user", MODE_PRIVATE);
        //dialog
        final ProgressDialog dialog = new ProgressDialog(context, "正在登录中...");
        dialog.setCanceledOnTouchOutside(false);
        AccountRequest.getInstance().login(currentUsername, currentPassword).subscribe(new BaseRetrofitResponse<BaseData<LoginInfo_New>>() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.showProgersssDialog();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onNext(BaseData<LoginInfo_New> loginInfo_newBaseData) {
                super.onNext(loginInfo_newBaseData);
                if (null != loginInfo_newBaseData
                        && null != loginInfo_newBaseData.getData()
                        && loginInfo_newBaseData.getData().user_id!=0){
                    LoginInfo_New loginInfo_new = loginInfo_newBaseData.getData();
                    dealwithData_New(loginInfo_new, sp1, currentUsername, currentPassword, context, dialog);
                } else {
                    Toast.makeText(YMApplication.getAppContext(),"登录失败,请重新登录",0).show();
                    Intent intent = new Intent(YMApplication.getAppContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    YMApplication.getInstance().startActivity(intent);
                }

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                dialog.closeProgersssDialog();
                startLoginPage(context);
            }
        });
    }


    /**
     * 刷新用户信息并保存起来   stone
     *
     * @param context
     */
    public static void refreshUserInfo(final Activity context, final Callback callback) {
        //获取用于保存用户登录信息的save_user共享文件
        final SharedPreferences sp1 = context.getSharedPreferences("save_user", MODE_PRIVATE);

        CertificationAboutRequest.getInstance().getDoctorInfo(YMApplication.getLoginInfo().getData().getPid()).subscribe(new BaseRetrofitResponse<BaseData<LoginInfo_New>>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(BaseData<LoginInfo_New> info) {
                super.onNext(info);
                LoginInfo_New loginInfo_new = info.getData();
                if (null != info
                        && loginInfo_new != null) {
//                    && loginInfo_new.user_id == 0
                    //application中保存用户信息
                    YMApplication.setUserInfo(loginInfo_new);
                    LoginInfo login = setLoginInfo(loginInfo_new);
                    //将用户登录信息保存到save_user共享文件中
                    sp1.edit().putString("login_info", GsonUtils.toJson(login)).apply();


                    if (callback != null) {
                        callback.onSuccess(loginInfo_new);
                    }
                }
            }
        });
    }


//    /**
//     * 登录 stone
//     * stone
//     * 2017/10/26 下午5:59
//     */
//    public static void ymLogin(final Activity context, final String currentUsername, final String currentPassword) {
//        //获取用于保存用户登录信息的save_user共享文件
//        final SharedPreferences sp1 = context.getSharedPreferences("save_user", MODE_PRIVATE);
//
////        String registrationID = JPushInterface.getRegistrationID(YMApplication.getInstance());
////        String reg_id = sp1.getString("jpush_regis_id", "");
////        if (!TextUtils.isEmpty(registrationID) && !TextUtils.isEmpty(reg_id) && !reg_id.equals(registrationID)) {
////            reg_id = registrationID;
////            sp1.edit().putString("jpush_regis_id", registrationID).apply();
////        }
//
//        //dialog
//        final ProgressDialog dialog = new ProgressDialog(context, "正在登录中...");
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.showProgersssDialog();
//        //sign生成
//        String keyCode = MD5Util.MD5(currentUsername + Constants.MD5_KEY);
//        //获取极光注册id
//        FinalHttp ft = new FinalHttp();
//        ft.configTimeout(5000);
//        //参数
//        AjaxParams params = new AjaxParams();
//        params.put("command", "login");
//        if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
//            params.put("bus_source", 13 + "");
//        }
//        params.put("phone", currentUsername);
//        params.put("password", currentPassword);
//        params.put("reg_id", JPushInterface.getRegistrationID(YMApplication.getInstance()));
//        params.put("sign", keyCode);
//        ft.post(CommonUrl.DP_COMMON, params, new AjaxCallBack() {
//            @Override
//            public void onSuccess(String t) {
//                super.onSuccess(t);
//                //将原来的代码封装成了下面这个方法中了
//                dealwithData(t, sp1, currentUsername, currentPassword, context, dialog);
//            }
//
//            @Override
//            public void onFailure(Throwable t, int errorNo, String strMsg) {
//                shortToast("连接网络超时");//
//                super.onFailure(t, errorNo, strMsg);
//                dialog.closeProgersssDialog();
//                startLoginPage(context);
//            }
//        });
//    }
//
//    /**
//     * 处理登录后的数据
//     * stone 环信版本升级3.1.4->3.1.5 线程切换
//     * 2017/10/30 下午1:59
//     */
//    private static void dealwithData(String t, SharedPreferences sp1, String currentUsername,
//                                     String currentPassword, final Activity context, final ProgressDialog dialog) {
//        //json解析,最好换成GSon
//        final LoginInfo login = ResolveJson.R_logininfo(t);
//        if (login != null && login.getCode().equals("0")
//                && login.getData() != null
//                && !TextUtils.isEmpty(login.getData().getPid())) {
//            //application中保存用户登录对象
//            YMApplication.setLogin1(login);
//            //共享文件ym_account中保存登录状态
//            AccountSPUtils.loginSuccess(currentUsername);
//            //将用户登录信息保存到save_user共享文件中
//            sp1.edit().putString("login_info", t)
//                    .putString("user_name", currentUsername)
//                    .putString("pass_word", currentPassword)
//                    .putString("photo", login.getData().getPhoto())
//                    .putString("userid", login.getData().getPid())
//                    .apply();
//            // YMApplication.Identity = 0x1;
//            // YMApplication.islogin = true;
//            //不是游客模式
//            setIsGuest(false);
//            MobileAgent.getUserInfo(getLoginInfo().getData().getPid(),
//                    "1", context);
//            //环信登录
////            stone 判断下 EMChatManager.getInstance().
//            if (EMClient.getInstance().isLoggedInBefore()) {
//                YMApplication.getInstance().hxLogout(new EMCallBack() {
//                    @Override
//                    public void onSuccess() {
////                        LogUtils.d("环信登出成功");
////                        startLoginPage(context);
////                        ToastUtils.shortToast("环信登出成功");
//                        YMApplication.applicationHandler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
////                                dialog.closeProgersssDialog();
//                                hxLogin(login.getData().getHuanxin_username(), login.getData().getHuanxin_password(), context, dialog);
//                            }
//                        }, 200);
//                    }
//
//                    @Override
//                    public void onError(int i, String s) {
////                        LogUtils.d("环信登出失败");
////                        startLoginPage(context);
////                        ToastUtils.shortToast("环信登出失败");
//                        YMApplication.applicationHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                YMApplication.getInstance().hxLogout(false, new EMCallBack() {
//                                    @Override
//                                    public void onSuccess() {
////                                ToastUtils.shortToast("okok");
//                                        YMApplication.applicationHandler.postDelayed(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                dialog.closeProgersssDialog();
//                                                hxLogin(login.getData().getHuanxin_username(), login.getData().getHuanxin_password(), context, dialog);
//                                            }
//                                        }, 200);
//                                    }
//
//                                    @Override
//                                    public void onError(int i, String s) {
////                                ToastUtils.shortToast("shibaila"+i+s);
//                                        startLoginPage(context);
//                                    }
//
//                                    @Override
//                                    public void onProgress(int i, String s) {
//
//                                    }
//                                });
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onProgress(int i, String s) {
//
//                    }
//                });
//
//            } else {
//                hxLogin(login.getData().getHuanxin_username(), login.getData().getHuanxin_password(), context, dialog);
//            }
//            return;
//        }
//        //登录返回数据错误,重新登录
//        if (login != null && login.getMsg() != null) {
//            shortToast(login.getMsg());
//        }
//        dialog.closeProgersssDialog();
//        startLoginPage(context);
//
//    }

    private static void dealwithData_New(LoginInfo_New loginInfo_new, SharedPreferences sp1, String currentUsername,
                                         String currentPassword, final Activity context, final ProgressDialog dialog) {
        if (loginInfo_new != null) {
            final LoginInfo login = setLoginInfo(loginInfo_new);
            //共享文件ym_account中保存登录状态
            AccountSPUtils.loginSuccess(currentUsername);
            //将用户登录信息保存到save_user共享文件中
            sp1.edit().putString("login_info", GsonUtils.toJson(login))
                    .putString("user_name", currentUsername)
                    .putString("pass_word", currentPassword)
                    .putString("photo", login.getData().getPhoto())
                    .putString("userid", login.getData().getPid())
                    .apply();
            //不是游客模式
            setIsGuest(false);

            MobileAgent.getUserInfo(getLoginInfo().getData().getPid(),
                    "1", context);

            //new 环信登录 stone
            hxLoginNew(login.getData().getHuanxin_username(), login.getData().getHuanxin_password(), context, dialog);
            XywyIMService.getInstance().connect(BuildConfig.WEBSOCKET_URL_ERLANG, Constants.BSID_RTQA,YMConfig.SOURCE,Long.parseLong(login.getData().getPid()),currentPassword,2);
            //5.3.1即时问题这个小模块是默认都显示的,所以isOnLineConsultUser直接置为1
            YMApplication.getInstance().isOnLineConsultUser = 1;
            YMApplication.getInstance().initWebSocket();
            toMainPage(context, dialog);

            //环信登录 old
//            stone 判断下 EMChatManager.getInstance().
//            if (EMClient.getInstance().isLoggedInBefore()) {
//                YMApplication.getInstance().hxLogout(new EMCallBack() {
//                    @Override
//                    public void onSuccess() {
////                        LogUtils.d("环信登出成功");
////                        startLoginPage(context);
////                        ToastUtils.shortToast("环信登出成功");
//                        YMApplication.applicationHandler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
////                                dialog.closeProgersssDialog();
//                                hxLogin(login.getData().getHuanxin_username(), login.getData().getHuanxin_password(), context, dialog);
//                            }
//                        }, 200);
//                    }
//
//                    @Override
//                    public void onError(int i, String s) {
////                        LogUtils.d("环信登出失败");
////                        startLoginPage(context);
////                        ToastUtils.shortToast("环信登出失败");
//                        YMApplication.applicationHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                YMApplication.getInstance().hxLogout(false, new EMCallBack() {
//                                    @Override
//                                    public void onSuccess() {
////                                ToastUtils.shortToast("okok");
//                                        YMApplication.applicationHandler.postDelayed(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                dialog.closeProgersssDialog();
//                                                hxLogin(login.getData().getHuanxin_username(), login.getData().getHuanxin_password(), context, dialog);
//                                            }
//                                        }, 200);
//                                    }
//
//                                    @Override
//                                    public void onError(int i, String s) {
////                                ToastUtils.shortToast("shibaila"+i+s);
//                                        startLoginPage(context);
//                                    }
//
//                                    @Override
//                                    public void onProgress(int i, String s) {
//
//                                    }
//                                });
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onProgress(int i, String s) {
//
//                    }
//                });
//            } else {
//                hxLogin(login.getData().getHuanxin_username(), login.getData().getHuanxin_password(), context, dialog);
//            }
        }
    }

    //保存登录用户信息
    @NonNull
    private static LoginInfo setLoginInfo(LoginInfo_New loginInfo_new) {
        LoginInfo.UserData userData = new LoginInfo().new UserData();
        userData.setPid(loginInfo_new.user_id + "");
        userData.setHuanxin_username(loginInfo_new.hx_username);
        userData.setHuanxin_password(loginInfo_new.hx_password);
        userData.setPhoto(loginInfo_new.photo);
        userData.setPhone(loginInfo_new.phone + "");
        userData.setHospital(loginInfo_new.hos_name);
        userData.setAudit_status(loginInfo_new.audit_status);
        userData.setToken(loginInfo_new.hx_token);
        userData.setImwd(loginInfo_new.imwd);
        userData.setStat(loginInfo_new.status + "");
        userData.setRealname(loginInfo_new.real_name);
        userData.setUsername(loginInfo_new.real_name);
        userData.setJixiao(loginInfo_new.jixiao);
        userData.setBalance(loginInfo_new.balance);
        userData.setIncome(loginInfo_new.income);
        userData.setTop(loginInfo_new.top);
        userData.setZxzhsh(loginInfo_new.zxzhsh);
        userData.setJsdh(loginInfo_new.jsdh);
        userData.setOpenid(loginInfo_new.openid);
        userData.setIsjob("1");//由于改变后不区分专家和普通医生，这里全部处理成普通医生
        if (null != loginInfo_new.details) {
            userData.setMajorfirst(loginInfo_new.details.major_first);
            String clinic = loginInfo_new.details.clinic;
            userData.setJob(TextUtils.isEmpty(clinic) ? "" : clinic);
            String rank = loginInfo_new.details.rank;
            userData.setHosp_level(TextUtils.isEmpty(rank) ? "" : rank);
            String major_first = loginInfo_new.details.major_first;
            String major_second = loginInfo_new.details.major_second;
            userData.setSubject(TextUtils.isEmpty(major_first) ? "" : major_first + "-" + (TextUtils.isEmpty(major_second) ? "" : major_second));
            String subject_name = loginInfo_new.details.subject_name;
            userData.setSubjectName(subject_name);
            String hos_name = loginInfo_new.details.hos_name;
            userData.setHos_name(hos_name);

        }
        ClinicStatInfo clinicStatInfo = userData.getXiaozhan();
        clinicStatInfo.setFamily(loginInfo_new.familyDoctor + "");//设置开通家庭医生
        clinicStatInfo.setPhone(loginInfo_new.dhys + "");//设置开通电话医生
        clinicStatInfo.setDati(loginInfo_new.club + "");//设置开通问题广场答题
        clinicStatInfo.setYuyue(loginInfo_new.jiahao + "");//设置开通预约转诊
        clinicStatInfo.setClub_assign(loginInfo_new.club_assign);//图文咨询-指定付费  -1未开通  0 待审核 1开通 2 关闭
        clinicStatInfo.setClub_reward(loginInfo_new.club_reward);//图文咨询-悬赏    -1未开通   0 待审核 1开通 2 关闭
        clinicStatInfo.setImwd_assign(loginInfo_new.imwd_assign);//IM问答-指定付费  -1未开通0 待审核 1开通 2 关闭
        clinicStatInfo.setImwd_reward(loginInfo_new.imwd_reward);//IM问答-悬赏  -1未开通0 待审核 1开通 2 关闭

        clinicStatInfo.setServices(loginInfo_new.services);//设置业务的集合
        userData.setXiaozhan(clinicStatInfo);

        final LoginInfo login = new LoginInfo();
        login.setData(userData);
        //application中保存用户登录对象
        YMApplication.setLogin1(login);
        return login;
    }


    /**
     * 环信 用户名登录
     * stone 环信版本升级3.1.4->3.1.5 线程切换
     *
     * @param currentUserName 环信用户名
     * @param password        密码
     */
    public static void hxLoginNew(final String currentUserName, final String password, final Activity activity, final ProgressDialog dialog) {

        //判空登录
        if (!TextUtils.isEmpty(currentUserName) && !TextUtils.isEmpty(password)) {

            //保存环信用户名密码
            YMApplication.getInstance().setUserName(currentUserName);
            YMApplication.getInstance().setPassword(password);

            // TODO: 2018/3/13 测试登录失败  stone
            EMChatManager.getInstance().login(currentUserName, password, new EMCallBack() {
//            EMChatManager.getInstance().login("1", "1", new EMCallBack() {
                @Override
                public void onSuccess() {
                    LogUtils.d("环信登录成功-->" + activity.getComponentName());
                    //stone 在ui线程去加载会话
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            EMChatManager.getInstance().loadAllConversations();
                        }
                    });
                }

                @Override
                public void onError(int i, String s) {
                    LogUtils.d("环信登录失败-->" + activity.getComponentName() + "--->" + s);
                }

                @Override
                public void onProgress(int i, String s) {

                }
            });
        }
    }


//    /**
//     * 环信 用户名登录
//     * stone 环信版本升级3.1.4->3.1.5 线程切换
//     *
//     * @param currentUserName 环信用户名
//     * @param password        密码
//     */
//    public static void hxLogin(final String currentUserName, final String password, final Activity activity, final ProgressDialog dialog) {
//
//        EMChatManager.getInstance().login(currentUserName, password,
//                new EMCallBack() {
//
//                    @Override
//                    public void onSuccess() {
//                        //stone 在ui线程去加载会话 环信版本升级3.1.4->3.1.5
//                        activity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                setIsGuest(false);
//                                com.xywy.util.LogUtils.e("登录成功：");
//                                // 登陆成功，保存环信用户名密码
//                                YMApplication.getInstance().setUserName(currentUserName);
//                                YMApplication.getInstance().setPassword(password);
//                                EMChatManager.getInstance().loadAllConversations();
//
//                                //initLiveShowData(activity, dialog);
//
//                                //连接Websocket stone 去掉
////                                YMApplication.getInstance().initWebSocket();
////                                getDocInfo(activity, dialog);
//
//                                //5.3.1即时问题这个小模块是默认都显示的,所以isOnLineConsultUser直接置为1
//                                YMApplication.getInstance().isOnLineConsultUser = 1;
//                                YMApplication.getInstance().initWebSocket();
//                                toMainPage(activity, dialog);
//                            }
//                        });
//
//                    }
//
//                    @Override
//                    public void onProgress(int progress, String status) {
//                    }
//
//                    @Override
//                    public void onError(final int code, final String message) {
//                        L.e("hx登陆错误日志" + message);
//                        //stone 切到主线程 环信版本升级3.1.4->3.1.5
//                        YMApplication.applicationHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                dialog.closeProgersssDialog();
//                            }
//                        });
//                        startLoginPage(activity);
//                    }
//                });
//
//    }

//    private static void getDocInfo(final Activity activity, final ProgressDialog dialog) {
//        if (!YMUserService.isGuest()) {
//            /**
//             * 获取在线咨询医生信息
//             */
//            ServiceProvider.getDoctorInfo(YMUserService.getCurUserId(), new CommonResponse<DoctorInfoEntity>() {
//                @Override
//                public void onError(Throwable e) {
//                    if (e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof HttpException) {
//                        ToastUtils.shortToast("网络中断，请检查您的网络状态");
//                    } else {
//                        ToastUtils.shortToast("异常提示:" + e.getMessage());
//                    }
//                }
//
//                @Override
//                public void onNext(DoctorInfoEntity doctorInfoEntity) {
//                    if (null != doctorInfoEntity) {
//                        DoctorInfoBean doctorInfoBean = doctorInfoEntity.getData();
//                        YMApplication.getInstance().setDoctorInfo(doctorInfoBean);
//                        if (null != doctorInfoBean) {
//                            if (null != doctorInfoBean.getWork()) {
//                                if (1 == doctorInfoBean.getWork().getImwd()) {
//                                    YMApplication.getInstance().isOnLineConsultUser = 1;
//                                    //stone 新加 连接Websocket
//                                    YMApplication.getInstance().initWebSocket();
//                                } else {
//                                    //未开通 不添加item
//                                    YMApplication.getInstance().isOnLineConsultUser = 0;
//                                    YMApplication.getInstance().disconnectWebSocket();
//                                }
//                            }
//                        }
//                    }
//                    toMainPage(activity, dialog);
//                }
//            });
//        }
//    }

    private static void startLoginPage(final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoginActivity.start(activity);
                if (activity instanceof LoginActivity) {
                    return;
                }
                activity.finish();
            }
        });
    }
}
