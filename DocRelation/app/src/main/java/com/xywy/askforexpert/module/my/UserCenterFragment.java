package com.xywy.askforexpert.module.my;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.MainActivity;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.activity.WebViewActivity;
import com.xywy.askforexpert.appcommon.base.YMBaseFragment;
import com.xywy.askforexpert.appcommon.base.webview.CommonWebViewActivity;
import com.xywy.askforexpert.appcommon.medicine.SyncInfoRequest;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DensityUtils;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.appcommon.view.CustomToast;
import com.xywy.askforexpert.model.LoginInfo;
import com.xywy.askforexpert.model.LoginInfo_New;
import com.xywy.askforexpert.model.Qdinfo;
import com.xywy.askforexpert.model.certification.MessageBoardBean;
import com.xywy.askforexpert.model.my.Income;
import com.xywy.askforexpert.module.discovery.medicine.CertificationAboutRequest;
import com.xywy.askforexpert.module.discovery.medicine.SellDrugRequest;
import com.xywy.askforexpert.module.discovery.medicine.common.Callback;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;
import com.xywy.askforexpert.module.discovery.medicine.module.patient.PatientListActivity;
import com.xywy.askforexpert.module.doctorcircle.InterestPersonActivity;
import com.xywy.askforexpert.module.liveshow.MyLiveShowActivity;
import com.xywy.askforexpert.module.liveshow.constants.H5PageUrl;
import com.xywy.askforexpert.module.my.collection.MyConlectActivity;
import com.xywy.askforexpert.module.my.download.MyDownActivity;
import com.xywy.askforexpert.module.my.gzh.WeChat_Official_Accounts_Activity;
import com.xywy.askforexpert.module.my.integral.MyScoresActivity;
import com.xywy.askforexpert.module.my.invite.InviteMoneyActivity;
import com.xywy.askforexpert.module.my.pause.MyPurseActivity;
import com.xywy.askforexpert.module.my.request.UserCenterFragmentRequest;
import com.xywy.askforexpert.module.my.setting.SettingActivity;
import com.xywy.askforexpert.module.my.smallstation.MySite;
import com.xywy.askforexpert.module.my.userinfo.ApproveInfoActivity;
import com.xywy.askforexpert.module.my.userinfo.CheckStateActivity;
import com.xywy.askforexpert.module.my.userinfo.PersonInfoActivity;
import com.xywy.askforexpert.widget.CircleImageView;
import com.xywy.easeWrapper.EaseConstant;
import com.xywy.retrofit.RetrofitClient;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.xywy.retrofit.rxbus.Event;
import com.xywy.retrofit.rxbus.EventSubscriber;
import com.xywy.util.LogUtils;
import com.xywy.util.T;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.org.bjca.sdk.core.kit.BJCASDK;
import retrofit2.adapter.rxjava.HttpException;

import static com.xywy.askforexpert.appcommon.old.Constants.CLOSEED;
import static com.xywy.askforexpert.appcommon.old.Constants.NIGHT;
import static com.xywy.askforexpert.appcommon.old.Constants.OPENED;

/**
 * 我的 stone 新版本
 */
public class UserCenterFragment extends YMBaseFragment implements View.OnClickListener, View.OnTouchListener {
    private Callback mRefreshUserInfoCallback;
    private LoginInfo_New mInfoNew;
    private final LoginInfo.UserData userData = YMApplication.getLoginInfo().getData();

    private static final String LOG_TAG = "UserCenterFragment";

    @Bind(R.id.user_img)
    CircleImageView userImg;
    @Bind(R.id.user_name)
    TextView userName;
    @Bind(R.id.user_title)
    TextView userTitle;
    @Bind(R.id.user_center_header)
    LinearLayout userCenterHeader;

    @Bind(R.id.my_integration)
    TextView myIntegration;
    @Bind(R.id.my_purse)
    TextView myPurse;
    @Bind(R.id.my_clinic)
    RelativeLayout myClinic;
    @Bind(R.id.divider_1)
    View divider_1;
    @Bind(R.id.user_center_list_divider_2)
    View userCenterListDivider2;
    @Bind(R.id.invite_for_money)
    RelativeLayout inviteForMoney;
    @Bind(R.id.tv_renzhen)
    TextView tvRenzhen;
    @Bind(R.id.user_center_scroll_list)
    ScrollView userCenterScrollList;
    @Bind(R.id.tv_visitor_num)
    TextView tvVisitorNum;
    @Bind(R.id.patient_manage)
    RelativeLayout patientManage;
    @Bind(R.id.tv_patient)
    TextView tvPatient;
    @Bind(R.id.rl_score_shop)
    RelativeLayout rlScoreShop;
    @Bind(R.id.iv_qd)
    ImageView iv_qd;

    @Bind(R.id.tv_income_month)
    TextView tv_income_month;    //本月收入

    @Bind(R.id.tv_total_income)
    TextView tv_total_income;    //总收入

    @Bind(R.id.tv_ranking)
    TextView tv_ranking;    //收入排名

    @Bind(R.id.tv_imcome_yesterday)
    TextView tv_imcome_yesterday;  //昨日收入

    @Bind(R.id.tv_video_status)
    TextView tv_video_status;

    @Bind(R.id.rl_checkin)
    RelativeLayout rlCheckin;

    @Bind(R.id.fl_purse)
    FrameLayout fl_purse;

    @Bind(R.id.ll_integration)
    LinearLayout ll_integration;

    @Bind(R.id.ll_room)
    LinearLayout ll_room;

    @Bind(R.id.ll_doctot_person)
    TextView ll_doctot_person;

    @Bind(R.id.rl_my_visitor)
    RelativeLayout rl_my_visitor;

    @Bind(R.id.rl_gzh)
    RelativeLayout rl_gzh;//微信公众号

    @Bind(R.id.iv_mode)
    ImageView iv_mode;//夜间模式

    //医网签证书和签章是否都设置
    @Bind(R.id.tv_electronic_sign)
    TextView tv_electronic_sign;

    @Bind(R.id.electronic_sign_rl)
    RelativeLayout electronic_sign_rl;

    /**
     * 接口返回的签到信息
     */
    public static Qdinfo mQdinfo;

    private SharedPreferences sp;

    private boolean mSkip;
    private boolean mNightOpend;//夜间模式 stone
    private boolean mNightModeRequested;


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_user_center;
    }

    @Override
    protected void initView() {
        if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
            tvPatient.setText("我的患者");
            ll_doctot_person.setVisibility(View.GONE);
            rl_my_visitor.setVisibility(View.GONE);
        } else {
            tvPatient.setText("患者管理");
        }
        fl_purse.setOnTouchListener(this);
        ll_integration.setOnTouchListener(this);
        ll_room.setOnTouchListener(this);
        mRefreshUserInfoCallback = new Callback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onProgress(int progress) {

            }

            @Override
            public void onSuccess(Object data) {
                mInfoNew = (LoginInfo_New) data;
                if (mInfoNew != null) {
                    handleInfo();
                }
            }

            @Override
            public void onFail(Throwable e) {

            }
        };

        //stone
        YmRxBus.registerNightModeChangedListener(new EventSubscriber<Boolean>() {
            @Override
            public void onNext(Event<Boolean> isOpen) {
                mNightOpend = isOpen.getData();
                iv_mode.setImageResource(mNightOpend ? R.drawable.mode_open : R.drawable.mode_close);
            }
        }, getActivity());
        if (TextUtils.isEmpty(YMApplication.getLoginInfo().getData().getOpenid())){
            electronic_sign_rl.setVisibility(View.GONE);
        }else{
            if(!BJCASDK.getInstance().existsCert(getActivity()) &&
                    !BJCASDK.getInstance().existsStamp(getActivity())){
                tv_electronic_sign.setText("未设置");
            }
        }
//

    }

    @Override
    protected void beforeViewBind() {
        super.beforeViewBind();
        sp = getActivity().getSharedPreferences("save_user", Context.MODE_PRIVATE);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
        StatisticalTools.fragmentOnResume("MainTab04");
        if(BJCASDK.getInstance().existsCert(getActivity()) &&
                BJCASDK.getInstance().existsStamp(getActivity())){
            tv_electronic_sign.setText("已设置");
        }
        onRefresh();
    }

    @Override
    public void onPause() {
        super.onPause();
        StatisticalTools.fragmentOnPause("MainTab04");
    }

    @Override
    public String getStatisticalPageName() {
        return "UserCenterFragment";
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (((MainActivity) getActivity()).isConflict) {
            outState.putBoolean("isConflict", true);
        } else if (((MainActivity) getActivity()).getCurrentAccountRemoved()) {
            outState.putBoolean(EaseConstant.ACCOUNT_REMOVED, true);
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //底部切换Fragment的时候会实现可见即刷新数据,第一次实例化的时候不会刷新
        if (!hidden) {
            onRefresh();
        }
    }

    private void setChildViewVisibility() {
        // 设置签到红点可见/隐藏
//        checkRed.setVisibility(sp.getBoolean("main_isfist", false) ? View.GONE : View.VISIBLE);

        switch (YMApplication.DoctorApproveType()) {
            case -2:
                tvRenzhen.setText("驳回");
                break;
            case -1:
                tvRenzhen.setText("认证中");
                break;
            case 0:
                tvRenzhen.setText("未认证");
                break;
            case 1:
                tvRenzhen.setText("已认证");
                break;
            case 2:
                tvRenzhen.setText("不通过");
                break;
            case 3:
                tvRenzhen.setText("待跟踪");
                break;
            case 4:
                tvRenzhen.setText("暂不开通");
                break;

            default:
                break;
        }


    }

    @Override
    protected void initListener() {
        userCenterScrollList.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = userCenterScrollList.getScrollY();
                DLog.d(LOG_TAG, "scroll y = " + scrollY);
                if (scrollY > 0) {
                    ViewCompat.setElevation(userCenterHeader, DensityUtils.dp2px(4));
                } else {
                    ViewCompat.setElevation(userCenterHeader, 0);
                }
            }
        });
    }

    private void setQdView() {
        if (mQdinfo == null) {
            return;
        }
        if ("1".equals(mQdinfo.getData().getQd_flag())) {
            iv_qd.setImageResource(R.drawable.qd_ok);
        } else {
            iv_qd.setImageResource(R.drawable.qd);
        }
    }

    @OnClick({
            R.id.rl_my_visitor, R.id.rl_score_shop, R.id.patient_manage, R.id.rl_checkin, R.id.user_img,
            R.id.rl_gzh, R.id.iv_mode, R.id.user_name, R.id.user_title,R.id.fl_purse,R.id.ll_integration,R.id.ll_room,
            R.id.my_clinic, R.id.idcard_renz, R.id.ll_conlect, R.id.my_downloads, R.id.invite_for_money, R.id.my_setting
            , R.id.rl_live_video_show, R.id.rl_live_show_gains, R.id.rl_live_show_coins,R.id.electronic_sign_rl
    })
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.rl_live_video_show:
                //点击了我的直播
                if (YMUserService.checkLiveShowState(getActivity())) {
                    MyLiveShowActivity.start(getActivity());
                }
                break;

            case R.id.rl_live_show_gains:
                //点击了直播收益
                if (YMUserService.checkLiveShowState(getActivity())) {
                    //shortToast("跳转直播收益页");
                    WebViewActivity.start(getActivity(), "直播收益", H5PageUrl.LIVE_SHOW_PROFITS_URL);
                }
                break;

            case R.id.rl_live_show_coins:
                if (YMUserService.isLoginUser(getActivity())) {
                    //点击了我的健康币
                    //shortToast("跳转我的健康币页面");
                    //  LiveManager.getInstance().startCharge(getActivity(), RechargeType.mypurse);
                }

                break;
            case R.id.rl_my_visitor:
                //TODO 测试stone 在线诊室
//                OnlineChatDetailActivity.startActivity(getActivity(),"121","123121","测试",false);
//                startActivity(new Intent(getActivity(), OnlineChatDetailActivity.class));
//                startActivity(new Intent(getActivity(), ApplyForFamilyDoctorActivity.class));
//                startActivity(new Intent(getActivity(), ApproveInfoActivity.class));
//                startActivity(new Intent(getActivity(), DrugSettingActivity.class));

//                我的访客
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
                    return;
                }
                int unVisitCount = 0;
                if (mQdinfo != null) {
                    unVisitCount = mQdinfo.getAccess();
                    mQdinfo.setAccess(0);
                }
                tvVisitorNum.setText("");
                InterestPersonActivity.startActivity(getActivity(), "2", unVisitCount);
                break;
            case R.id.rl_score_shop:
                StatisticalTools.eventCount(getActivity(), "IntegralMall");
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
                    return;
                }
                intent = new Intent(getActivity(), CommonWebViewActivity.class);
                intent.putExtra(CommonWebViewActivity.IS_FROM_INTENT_KEY, "积分商城");
                intent.putExtra(CommonWebViewActivity.CONTENT_URL_INTENT_KEY, CommonUrl.DISCOVER_MALL + YMApplication.getPID());
                intent.putExtra(CommonWebViewActivity.SHARE_VISIBLE_INTENT_KEY, false);
                startActivity(intent);
                break;
            case R.id.patient_manage:
//                CommonUtils.gotoService(getActivity(), 3);
                if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
                    if (YMApplication.getInstance().getHasSyncInfo()) {
                        //如果已经同步过医生信息
                        if (YMApplication.getInstance().getSyncInfoResult()) {
                            //医生信息同步成功
                            if (YMApplication.getInstance().getHasSellDrug()) {
                                //已经检查过医生是否具有售药的权限
                                if (YMApplication.getInstance().getSellDrugResult()) {
                                    //当前医生具有售药的权限
                                    LogUtils.i("跳转到患者列表");
                                    startActivity(new Intent(getActivity(), PatientListActivity.class));
                                } else {
                                    ToastUtils.shortToast(YMApplication.getInstance().getSellDrugMsg());
                                }
                            } else {
                                //还未检查医生是否具有售药的权限
                                sellDrug();
                            }
                        } else {
                            //医生信息同步失败
                            com.xywy.askforexpert.module.discovery.medicine.util.ToastUtils.shortToast(YMApplication.getInstance().getSyncInfoMsg());
                        }
                    } else {
                        //没有同步过医生信息,则同步医生信息
                        syncInfo();
                    }

                } else {
                    CommonUtils.gotoService(getActivity(), 3);
                }

                break;
            case R.id.rl_checkin:
                // 签到
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
                } else {
                    sp.edit().putBoolean("main_isfist", true).apply();
//                    checkRed.setVisibility(View.GONE);
                    if (mQdinfo == null) {
                        return;
                    }
                    if ("1".equals(mQdinfo.getData().getQd_flag())) {
                        CustomToast.showAlreadySigned(getActivity(), "您已签到", R.drawable.already_signed);
                    } else {
                        signName();
                    }

                }
                break;

            case R.id.user_img:
            case R.id.user_name:
            case R.id.user_title:
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
                } else {
                    startActivity(new Intent(getActivity(), PersonInfoActivity.class));
                }
                break;
//            case R.id.user_info_layout:
//                // 转跳个人信息
//                StatisticalTools.eventCount(getActivity(), "personalziliao");
//
//                if (YMUserService.isGuest()) {
//                    DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
//                } else {
//                    startActivity(new Intent(getActivity(), PersonInfoActivity.class));
//                }
//                break;

//            case R.id.my_clinic:
//                // 我的诊所
//                StatisticalTools.eventCount(getActivity(), "MyClinic");
//
//                DialogUtil.showUserCenterCertifyDialog(getActivity(), new MyCallBack() {
//                    @Override
//                    public void onClick(Object data) {
//                        startActivity(new Intent(getActivity(), MySite.class));
//                    }
//                }, null, null);
//
//                break;

            case R.id.fl_purse:
                //我的钱包
                StatisticalTools.eventCount(getActivity(), "mywallet");
                DialogUtil.showUserCenterCertifyDialog(getActivity(), new MyCallBack() {
                    @Override
                    public void onClick(Object data) {
                        Intent intent = new Intent(getActivity(), MyPurseActivity.class);
                        if (mInfoNew != null) {
                            intent.putExtra(Constants.INTENT_KEY_INCOME, mInfoNew.income + "");
                            intent.putExtra(Constants.INTENT_KEY_BALANCE, mInfoNew.balance + "");
                            intent.putExtra(Constants.INTENT_KEY_JIXIAO, mInfoNew.jixiao + "");
                        }
                        startActivity(intent);
                    }
                }, null, null);
                break;

            case R.id.ll_integration:
                // 我的积分
                StatisticalTools.eventCount(getActivity(), "scorerule");

                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
                } else {
                    startActivity(new Intent(getActivity(), MyScoresActivity.class));
                }
                break;
            case R.id.ll_room:
                // 我的诊室
                StatisticalTools.eventCount(getActivity(), "myDoctorCircle");
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
                } else {
                    startActivity(new Intent(getActivity(), MySite.class));
                }
                break;
            case R.id.idcard_renz:
                // 去认证 根据认证状态去
                gotoApprove();
                break;
            case R.id.electronic_sign_rl:
                //查看医网签证书
                BJCASDK.getInstance().startDoctor(getActivity(), Constants.YWQ_CLIENTID);
                break;

            case R.id.ll_conlect:
                // 我的收藏
                StatisticalTools.eventCount(getActivity(), "mycollect");
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
//                    startLogin();
                } else {
                    getActivity().startActivity(new Intent(getActivity(), MyConlectActivity.class));
                }
                break;

            case R.id.my_downloads:
                //我的下载
                StatisticalTools.eventCount(getActivity(), "mydownload");
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
                } else {
                    startActivity(new Intent(getActivity(), MyDownActivity.class));
                }
                break;

            case R.id.invite_for_money:
                //TODO 测试stone 家庭医生设置
//                startActivity(new Intent(getActivity(), FamDoctorSetingActivity.class));
//                startActivity(new Intent(getActivity(), MyPharmacyActivity.class));class

//                 邀请发钱啦
                StatisticalTools.eventCount(getActivity(), "InviteMoney");
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
                } else {
                    Intent intent1 = new Intent(getActivity(), InviteMoneyActivity.class);
                    getActivity().startActivity(intent1);
                }
                break;

            case R.id.ll_doctot_person:

                break;

            case R.id.rl_gzh:
                //微信公众号 stone
                StatisticalTools.eventCount(getActivity(), Constants.FOCUSONWECHATPUBLIC);
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
                } else {
                    getActivity().startActivity(new Intent(getActivity(), WeChat_Official_Accounts_Activity.class));
                }
                break;
            case R.id.iv_mode:
                // 夜间模式 stone
                StatisticalTools.eventCount(getActivity(), Constants.NIGHTMODO);
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
                } else {

                    CertificationAboutRequest.getInstance().getSetMessageBoard(YMApplication.getUUid(), NIGHT, !mNightOpend ? OPENED : CLOSEED, YMApplication.mNightMessage).subscribe(new BaseRetrofitResponse<BaseData<MessageBoardBean>>() {
                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            hideProgressDialog();
                            ToastUtils.shortToast("修改夜间留言失败");
                        }

                        @Override
                        public void onNext(BaseData<MessageBoardBean> listBaseData) {
                            super.onNext(listBaseData);
                            hideProgressDialog();
                            if (listBaseData != null
                                    && listBaseData.getData() != null) {

                                handleSetMessageBoard(listBaseData.getData());

                            }
                        }
                    });

//                    boolean nightMode = sp.getBoolean(Constants.SP_KEY_NIGHT_MODE, false);
//                    iv_mode.setImageResource(mNightOpend ? R.drawable.mode_open : R.drawable.mode_close);
//                    sp.edit().putBoolean(Constants.SP_KEY_NIGHT_MODE, !nightMode).commit();
                }
                break;

            case R.id.my_setting:
                // 设置
                StatisticalTools.eventCount(getActivity(), "set");
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
                } else {
                    startActivity(new Intent(getActivity(), SettingActivity.class));
                }
                break;
        }
    }

    /**
     * 同步医生信息
     */
    public void syncInfo() {
        long doctroId = Long.parseLong(YMUserService.getCurUserId());
        SyncInfoRequest.getInstance().syncInfo(doctroId).subscribe(new BaseRetrofitResponse<BaseData>() {
            @Override
            public void onStart() {
                super.onStart();
                showProgressDialog("");
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                hideProgressDialog();
                if (e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof HttpException) {
                    //未同步医生信息接口
                    YMApplication.getInstance().setHasSyncInfo(false);
                    Toast.makeText(RetrofitClient.getContext(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else {
                    //已经同步过医生信息接口
                    YMApplication.getInstance().setHasSyncInfo(true);
                    //同步过医生信息接口的返回结果是：同步医生信息失败
                    YMApplication.getInstance().setSyncInfoResult(false, e.getMessage());
                    T.showShort(RetrofitClient.getContext(), e.getMessage());
                }
            }

            @Override
            public void onNext(BaseData entry) {
                super.onNext(entry);
                if (entry != null) {
                    dealwithEntry(entry);
                }
            }
        });
    }

    private void dealwithEntry(BaseData entry) {
        if (null != entry && entry.getCode() == 10000) {
            //已经同步过医生信息接口
            YMApplication.getInstance().setHasSyncInfo(true);
            //同步过医生信息接口的返回结果是：同步医生信息成功
            YMApplication.getInstance().setSyncInfoResult(true, "");
            sellDrug();
        }
    }

    private void sellDrug() {
        SellDrugRequest.getInstance().getSellDrug(Integer.parseInt(YMUserService.getCurUserId())).subscribe(new BaseRetrofitResponse<BaseData>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                hideProgressDialog();

            }

            @Override
            public void onError(Throwable e) {
                hideProgressDialog();
                if (e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof HttpException) {
                    //还未调用检测是否具有售药权限的接口
                    YMApplication.getInstance().setHasSellDrug(false);
                    Toast.makeText(RetrofitClient.getContext(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else {
                    //已经调用了检测是否具有售药权限的接口
                    YMApplication.getInstance().setHasSellDrug(true);
                    //检测是否具有售药权限的接口返回的结果是：没有售药权限
                    YMApplication.getInstance().setSellDrugResult(false, e.getMessage());
                    T.showShort(RetrofitClient.getContext(), e.getMessage());
                }
            }

            @Override
            public void onNext(BaseData entry) {
                super.onNext(entry);
                if (entry != null) {
                    //已经调用了检测是否具有售药权限的接口
                    YMApplication.getInstance().setHasSellDrug(true);
                    //检测是否具有售药权限的接口返回的结果是：具有售药权限
                    YMApplication.getInstance().setSellDrugResult(true, "");
                    LogUtils.i("跳转到患者列表");
                    startActivity(new Intent(getActivity(), PatientListActivity.class));
                }
            }
        });
    }


    //签到
    public void signName() {
        String userid;

        AjaxParams params = new AjaxParams();
        if (YMUserService.isGuest()) {
            userid = "0";
        } else {
            userid = YMApplication.getLoginInfo().getData().getPid();
        }
        String sign = MD5Util.MD5(userid + Constants.MD5_KEY);

        params.put("uid", userid);
        params.put("command", "qdSave");
        params.put("sign", sign);
        params.put("score", mQdinfo.getData().getScore());
        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.QDINFO_URL, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {

                mQdinfo.getData().setPoint(
                        Integer.parseInt(mQdinfo.getData()
                                .getPoint()) + Integer.parseInt(mQdinfo.getData().getScore()) + "");
                mQdinfo.getData().setQd_flag("1");
                setQdView();
                if (BuildConfig.isTestServer) {
                    CustomToast.showSignName(getActivity(), "签到成功!\n+10积分", R.drawable.signname_success);
                } else {
                    CustomToast.showSignName(getActivity(), "签到成功!\n+20积分", R.drawable.signname_success);
                }

                YmRxBus.notifySignName();
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });

    }

    //去认证 stone
    private void gotoApprove() {
        if (YMUserService.isGuest()) {
            DialogUtil.LoginDialog(getActivity());
            return;
        }

        // TODO: 2018/3/2   测试stone
//        int ran = new Random().nextInt(7) - 2;

        StatisticalTools.eventCount(getActivity(), "certification");
        switch (YMApplication.DoctorApproveType()) {
//        switch (ran) {
            case -2:
                CheckStateActivity.startActivity(getActivity(), "check_reject", "被驳回");
                break;
            case -1:
                CheckStateActivity.startActivity(getActivity(), "checking", "审核中");
                break;
            case 0:
                if (YMApplication.getUserInfo() != null) {
                    Intent intent = new Intent(getActivity(), ApproveInfoActivity.class);
                    startActivity(intent);
                } else {
                    mSkip = true;
                    // 获取用户信息 stone
                    YMUserService.refreshUserInfo(getActivity(), mRefreshUserInfoCallback);
                }
                break;
            case 1:
                ToastUtils.shortToast("已认证");
                break;
            case 2:
                CheckStateActivity.startActivity(getActivity(), "check_err", "不通过");
                break;
            case 3:
                ToastUtils.shortToast("待跟踪");
                break;
            case 4:
                ToastUtils.shortToast("暂不开通");
                break;

            default:
                break;
        }
    }


    /**
     * 获取签到信息 以及访客的数量  stone
     */
    public void getQD_Data() {
        String userid;

        AjaxParams params = new AjaxParams();
        if (YMUserService.isGuest()) {
            userid = "0";
        } else {
            userid = userData.getPid();
        }
        String sign = CommonUtils.computeSign(userid);

        params.put("uid", userid);
        params.put("command", "qdInfo");
        params.put("sign", sign);
        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.QDINFO_URL, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                Gson gson = new Gson();

                DLog.i("test", "签到数据" + t);
                mQdinfo = gson.fromJson(t, Qdinfo.class);
                if (mQdinfo != null && mQdinfo.getAccess() != 0) {
                    tvVisitorNum.setText(mQdinfo.getAccess() + "");
                }
                setQdView();
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    public void onRefresh() {
        if (YMUserService.isGuest()) {
            userName.setText("游客");
        }
        if (!mNightModeRequested) {
            //stone 获取夜间模式开启与否
            CertificationAboutRequest.getInstance().getMessageBoard(YMApplication.getUUid()).subscribe(new BaseRetrofitResponse<BaseData<List<MessageBoardBean>>>() {
                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                }

                @Override
                public void onNext(BaseData<List<MessageBoardBean>> listBaseData) {
                    super.onNext(listBaseData);
                    hideProgressDialog();
                    if (listBaseData != null
                            && listBaseData.getData() != null
                            && listBaseData.getData().size() == 2) {

                        handleSetMessageBoard(listBaseData.getData().get(0));
                        handleSetMessageBoard(listBaseData.getData().get(1));
                    }
                }
            });
        }

        if (YMApplication.getLoginInfo() != null && !YMUserService.isGuest()) {
            setChildViewVisibility();
            getQD_Data();
            // 获取用户信息 stone
            YMUserService.refreshUserInfo(getActivity(), mRefreshUserInfoCallback);
            //获取医生的收入，绩效等(新的计算方式计算出的收入和绩效)
            getNewIncome();
        }
    }

    private void getNewIncome() {
        UserCenterFragmentRequest.getInstance().getNewIncome(YMUserService.getCurUserId()).subscribe(new BaseRetrofitResponse<BaseData<Income>>(){
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onNext(BaseData<Income> incomeBaseData) {
                Income data = incomeBaseData.getData();
                if(null != data){
                    tv_income_month.setText(data.month + "");
                    tv_total_income.setText("累计收入" + data.jixiao);
                    tv_imcome_yesterday.setText("昨日收入" + data.yeday);
                }

            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    private void handleSetMessageBoard(MessageBoardBean bean) {
        //1 离线 2 夜间
        if (NIGHT.equals(bean.getType())) {
            mNightOpend = OPENED.equals(bean.getIsopen());
            YMApplication.mNightMessage = bean.getMessage();

            iv_mode.setImageResource(mNightOpend ? R.drawable.mode_open : R.drawable.mode_close);
            mNightModeRequested = true;
        }
    }


    /**
     * 处理个人信息
     */
    private void handleInfo() {

        if (mSkip) {
            mSkip = false;
            Intent intent = new Intent(getActivity(), ApproveInfoActivity.class);
            startActivity(intent);
        }

        ImageLoadUtils.INSTANCE.loadImageView(userImg, mInfoNew.photo);
        if (TextUtils.isEmpty(mInfoNew.real_name)) {
            userName.setText("我是谁?");
        } else {
            tv_video_status.setText(YMUserService.isLiveShowUser() ? "已开通" : "未开通");

            userName.setText(mInfoNew.real_name);
            if (mInfoNew.details != null) {
                if (TextUtils.isEmpty(mInfoNew.details.clinic)) {
                    userTitle.setVisibility(View.GONE);
                } else {
                    userTitle.setVisibility(View.VISIBLE);
                    userTitle.setText(mInfoNew.details.clinic);
                }
                //重新设置认证状态
                setChildViewVisibility();
            }
        }
//        tv_income_month.setText(mInfoNew.income + "");
//        tv_total_income.setText("累计收入" + mInfoNew.balance);
//        tv_imcome_yesterday.setText("昨日收入" + mInfoNew.jixiao);
        if (TextUtils.isEmpty(mInfoNew.top)) {
            tv_ranking.setVisibility(View.INVISIBLE);
        } else {
            tv_ranking.setVisibility(View.VISIBLE);
            tv_ranking.setText("您的收入已经打败了网站" + mInfoNew.top + "的医生");
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (view.getId() == R.id.fl_purse) {
                    fl_purse.setBackgroundColor(getResources().getColor(R.color.color_fafafa));
                    ll_integration.setBackgroundColor(getResources().getColor(R.color.white));
                    ll_room.setBackgroundColor(getResources().getColor(R.color.white));
                } else if (view.getId() == R.id.ll_integration) {
                    fl_purse.setBackgroundColor(getResources().getColor(R.color.white));
                    ll_integration.setBackgroundColor(getResources().getColor(R.color.color_fafafa));
                    ll_room.setBackgroundColor(getResources().getColor(R.color.white));
                } else {
                    fl_purse.setBackgroundColor(getResources().getColor(R.color.white));
                    ll_integration.setBackgroundColor(getResources().getColor(R.color.white));
                    ll_room.setBackgroundColor(getResources().getColor(R.color.color_fafafa));
                }
                break;
            case MotionEvent.ACTION_UP:
                fl_purse.setBackgroundColor(getResources().getColor(R.color.white));
                ll_integration.setBackgroundColor(getResources().getColor(R.color.white));
                ll_room.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            default:
                fl_purse.setBackgroundColor(getResources().getColor(R.color.white));
                ll_integration.setBackgroundColor(getResources().getColor(R.color.white));
                ll_room.setBackgroundColor(getResources().getColor(R.color.white));
                break;
        }
        return false;
    }
}
