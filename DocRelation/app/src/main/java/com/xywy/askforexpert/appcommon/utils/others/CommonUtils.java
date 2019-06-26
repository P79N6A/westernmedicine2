package com.xywy.askforexpert.appcommon.utils.others;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.hyphenate.util.PathUtil;
import com.letv.controller.PlayProxy;
import com.letv.universal.iplay.EventPlayProxy;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.common.SocializeConstants;
import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.activity.WebViewActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.PermissionUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.SystemBarTintManager;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.statusbar.IStatusBar;
import com.xywy.askforexpert.appcommon.utils.statusbar.MeizuStatusBar;
import com.xywy.askforexpert.appcommon.utils.statusbar.MiuiStatusbar;
import com.xywy.askforexpert.appcommon.utils.statusbar.OSM;
import com.xywy.askforexpert.appcommon.utils.statusbar.OSUtils;
import com.xywy.askforexpert.model.ClinicStatInfo;
import com.xywy.askforexpert.model.LoginInfo;
import com.xywy.askforexpert.model.QueData;
import com.xywy.askforexpert.model.home.JSDHCookieBean;
import com.xywy.askforexpert.module.consult.ConsultConstants;
import com.xywy.askforexpert.module.consult.ServiceProvider;
import com.xywy.askforexpert.module.consult.activity.ConsultOnlineActivity;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;
import com.xywy.askforexpert.module.docotorcirclenew.activity.NewTopicDetailActivity;
import com.xywy.askforexpert.module.doctorcircle.DoctorCircleSendMessageActivty;
import com.xywy.askforexpert.module.drug.OnlineRoomActivity;
import com.xywy.askforexpert.module.drug.OnlineRoomCloseActivity;
import com.xywy.askforexpert.module.main.media.MediaCenterActivity;
import com.xywy.askforexpert.module.main.patient.activity.PatientMainActiviy;
import com.xywy.askforexpert.module.main.patient.activity.PatientManagerActivity;
import com.xywy.askforexpert.module.main.service.ApplyBusinessResultActivity;
import com.xywy.askforexpert.module.main.service.codex.CheckBookActivity;
import com.xywy.askforexpert.module.main.service.codex.CodexFragActivity;
import com.xywy.askforexpert.module.main.service.document.PhysicLiteratureActivity;
import com.xywy.askforexpert.module.main.service.linchuang.GuideActivity;
import com.xywy.askforexpert.module.main.service.que.QueMyReplyActivity;
import com.xywy.askforexpert.module.main.service.recruit.RecruitCenterMainActivity;
import com.xywy.askforexpert.module.main.service.service.ServiceHomeDoctorActivity;
import com.xywy.askforexpert.module.main.service.service.ServiceMakeApp;
import com.xywy.askforexpert.module.main.service.service.ServiceMoreActivity;
import com.xywy.askforexpert.module.main.service.service.ServiceQueActivity;
import com.xywy.askforexpert.module.main.service.service.ServiceTelDoctor;
import com.xywy.askforexpert.module.main.subscribe.ChannelUtil;
import com.xywy.askforexpert.module.my.userinfo.ApplyForFamilyDoctorActivity;
import com.xywy.askforexpert.module.my.userinfo.ApproveInfoActivity;
import com.xywy.askforexpert.module.my.userinfo.CheckStateActivity;
import com.xywy.askforexpert.widget.module.topics.MyClickSpan;
import com.xywy.sdk.stats.MobileAgent;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Subscriber;

import static java.lang.Integer.parseInt;


/**
 * Compiler: Android Studio
 * Project: D_Platform
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2015/12/17 13:56
 * <p>
 * 待重构 拆分的工具类 不建议直接使用 如需使用建议将其中方法 拆分出去 形成专有的独立工具类再使用
 */
@Deprecated
public final class CommonUtils {
    public static boolean isPush;
    public static int type;

    private CommonUtils() {
        throw new UnsupportedOperationException("CommonUtils Cannot be Initialized");
    }

    public static CommonUtils newInstance() {
        return new CommonUtils();
    }

    //stone 设置友盟渠道号
    public static void initMobileAgent(Context context) {
        MobileAgent.initSdk(BuildConfig.xywyAppid, context);
        if (!BuildConfig.DEBUG) {
//            String appid = Constants.APP_ID;// 友盟的appkey
            String appid = (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) ? Constants.APP_ID_YSZS : Constants.APP_ID;// 友盟的appkey
            LogUtils.i("appid=" + appid);
            //设置key
//        AnalyticsConfig.setAppkey(appid);

            SocializeConstants.APPKEY = appid;
            //设置渠道
//        AnalyticsConfig.setChannel(ChannelUtil.getChannel(this, "xywypc"));

            MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(context, appid, ChannelUtil.getChannel(context, "xywypc"));
            MobclickAgent.startWithConfigure(config);
            MobileAgent.onError(context);
        }
    }

    public static void setToolbar(Context context, Toolbar toolbar) {
        if (context != null && toolbar != null && context instanceof AppCompatActivity) {
            ((AppCompatActivity) context).setSupportActionBar(toolbar);

            ActionBar actionBar = ((AppCompatActivity) context).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setHomeAsUpIndicator(R.drawable.back_selector);
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    public static void setWebView(WebView webView) {
        if (webView != null) {
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
            webSettings.setSupportZoom(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setLoadsImagesAutomatically(true);
            webSettings.setAppCacheEnabled(true);
//            webSettings.setAppCachePath("");
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webSettings.setDisplayZoomControls(false);
            webSettings.setLoadWithOverviewMode(true);
            webView.setHorizontalScrollBarEnabled(false);
            webView.setVerticalScrollBarEnabled(false);
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public static void backgroundAlpha(Context context, float bgAlpha) {
        if (context != null) {
            Window window = null;
            if (context instanceof AppCompatActivity) {
                window = ((AppCompatActivity) context).getWindow();
            } else if (context instanceof Activity) {
                window = ((Activity) context).getWindow();
            }

            if (window != null) {
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.alpha = bgAlpha; // 0.0-1.0
                window.setAttributes(lp);
            }
        }
    }

    public static void setRefresh(SwipeRefreshLayout refreshLayout) {
        if (refreshLayout != null) {
            refreshLayout.setColorSchemeResources(R.color.color_scheme_2_1, R.color.color_scheme_2_2,
                    R.color.color_scheme_2_3, R.color.color_scheme_2_4);
        }
    }

    /**
     * 展示去认证对话框
     */
    public static void showApproveDialog(final Context context, String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("您还没有进行专业认证");
        builder.setMessage(msg);
        builder.setPositiveButton("去认证", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StatisticalTools.eventCount(context, "gocertification");
                if (YMApplication.DoctorApproveType() == -1) {
                    CheckStateActivity.startActivity(context, "checking", "审核中");
                } else {
                    Intent intent = new Intent(context, ApproveInfoActivity.class);
                    context.startActivity(intent);
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StatisticalTools.eventCount(context, "cancelrz");
            }
        });
        builder.create().show();
    }

    public static void showApproveDialog1(final Context context, String msg) {
        final SharedPreferences sp_save_user = context.getSharedPreferences("save_user", Context.MODE_PRIVATE);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("您还没有进行专业认证");
        builder.setMessage(msg);
        builder.setPositiveButton("去认证", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (sp_save_user.getBoolean(YMApplication.getPID() + "expertapp", false)) {
                    CheckStateActivity.startActivity(context, "checking", "审核中");
                } else {
                    context.startActivity(new Intent(context, ApproveInfoActivity.class));
//                    context.startActivity(new Intent(context, ExpertApproveActivity.class));
                }
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    private static void showApproveDialog2(final Context context, String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("去开通", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StatisticalTools.eventCount(context, "open");
                CheckStateActivity.startActivity(context, "checking", "审核中");
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StatisticalTools.eventCount(context, "cancelkt");
            }
        });
        builder.create().show();
    }

    public static void showApproveDialog(final Context context, String title, String msg, final Class clazz) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("去开通", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StatisticalTools.eventCount(context, "open");
                context.startActivity(new Intent(context, clazz));
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StatisticalTools.eventCount(context, "cancelkt");
            }
        });
        builder.create().show();
    }

    /**
     * 获取定制服务的名字、图标、背景色
     *
     * @param id 定制服务的id
     * @return 定制服务的信息
     */
    public static Map<String, Object> getServiceInfo(int id) {
        // 服务名称
        String name = "name";
        // 服务块背景色
        String bg = "bgColor";
        // 服务icon
        String icon = "icon";
        // 服务信息集合
        Map<String, Object> map = new HashMap<>();
        switch (id) {
            // 定制服务
            case 999:
                map.put(name, "定制服务");
                map.put(bg, "#C6C8C8");
                map.put(icon, R.drawable.add_services);
                break;

            // 问题广场
            case 1:
                map.put(name, "问题广场");
                map.put(bg, "#6AB7EB");
                map.put(icon, R.drawable.questions);
                break;

            // 医学文献
            case 2:
                map.put(name, "医学文献");
                map.put(bg, "#9AD279");
                map.put(icon, R.drawable.docs);
                break;

            // 患者管理
            case 3:
                map.put(name, "患者管理");
                map.put(bg, "#FFAA80");
                map.put(icon, R.drawable.patient_control);
                break;

            // 检查手册
            case 4:
                map.put(name, "检查手册");
                map.put(bg, "#98E2B3");
                map.put(icon, R.drawable.check_doc);
                break;

            // 家庭医生
            case 5:
                map.put(name, "家庭医生");
                map.put(bg, "#85C1E9");
                map.put(icon, R.drawable.home_doc);
                break;

            // 用药助手
            case 6:
                map.put(name, "用药助手");
                map.put(bg, "#FBA5A4");
                map.put(icon, R.drawable.yaodian);
                break;

            // 电话医生
            case 7:
                map.put(name, "电话医生");
                map.put(bg, "#F2CE87");
                map.put(icon, R.drawable.phone_doc);
                break;

            // 临床指南
            case 8:
                map.put(name, "临床指南");
                map.put(bg, "#F2D03B");
                map.put(icon, R.drawable.guide);
                break;

            // 找工作（小）
            case 9:
                map.put(name, "找工作");
                map.put(bg, "#88DDE5");
                map.put(icon, R.drawable.recruit_small);
                break;

            // 找工作（大）
            case 997:
                map.put(name, "找工作");
                map.put(bg, "#88DDE5");
                map.put(icon, R.drawable.recruit_big);
                break;

            // 预约转诊
            case 10:
                map.put(name, "预约转诊");
                map.put(bg, "#B4ABEE");
                map.put(icon, R.drawable.pre_order);
                break;

            // 签约居民(小)
            case 11:
                map.put(name, "签约居民");
                map.put(bg, "#35C7B3");
                map.put(icon, R.drawable.healthy_service_small);
                break;

            // 签约居民（大）
            case 998:
                map.put(name, "签约居民");
                map.put(bg, "#35C7B3");
                map.put(icon, R.drawable.healthy_service);
                break;

            // 默认，预防空指针
            default:
                map.put(name, "定制服务");
                map.put(bg, "#C6C8C8");
                map.put(icon, R.drawable.add_services);
                break;
        }

        return map;
    }

    /**
     * 获取一组服务信息
     *
     * @param ids 服务id
     * @return 服务信息集合
     */
    public static List<Map<String, Object>> getServiceInfos(List<Integer> ids) {
        List<Map<String, Object>> serviceInfos = new ArrayList<>();
        if (ids != null && !ids.isEmpty()) {
            for (int id : ids) {
                Map<String, Object> serviceInfo = getServiceInfo(id);
                serviceInfos.add(serviceInfo);
            }
        }

        return serviceInfos;
    }

    /**
     * 根据不同id转跳到不同的服务页面
     *
     * @param context   上下文
     * @param serviceId 服务id
     */
    public static void gotoService(final Context context, Integer serviceId) {
        Intent intent = new Intent();
        switch (serviceId) {
            // 在线咨询 stone
            case 888:
                goIMChat(context);
                break;
            // 定制服务
            case 999:
                StatisticalTools.eventCount(context, "CustomService");
                intent.setClass(context, ServiceMoreActivity.class);
                context.startActivity(intent);
                break;

            //[服务] 问题广场 stone 游客-认证通过与否
            case 1:
                gotoQuestions(context, isPush, type);
                break;

            // 医学文献
            case 2:
                StatisticalTools.eventCount(context, "medicalliterature");
                context.startActivity(new Intent(context, PhysicLiteratureActivity.class));
                break;

            // 患者管理
            case 3:
                DialogUtil.showUserCenterCertifyDialog(context, new MyCallBack() {
                    @Override
                    public void onClick(Object data) {
                        context.startActivity(new Intent(context, PatientMainActiviy.class));
                    }
                }, null, null);
                break;

            // 检查手册
            case 4:
                StatisticalTools.eventCount(context, "manualcheck");
                context.startActivity(new Intent(context, CheckBookActivity.class));
                break;

            // 家庭医生 stone 业务状态 -1 未开通 0 待审核 1开通 2 关闭 3暂时关闭
            case 5:
                goToFamilyDoctor(context);
                break;

            // 用药助手
            case 6:
                if (!YMUserService.isGuest()) {
                    StatisticalTools.eventCount(context, "pharmacopeia");
                    intent.setClass(context, CodexFragActivity.class);
                    context.startActivity(intent);
                } else {
                    DialogUtil.LoginDialog(new YMOtherUtils(context).context);
                }
                break;

            // [服务]电话医生
            case 7:
                goToPhoneDoctor(context);
                break;

            // 临床指南
            case 8:
                StatisticalTools.eventCount(context, "clinicalguideline");
                context.startActivity(new Intent(context, GuideActivity.class));
                break;

            // 招聘
            case 9:
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(context).context);
                } else {
                    StatisticalTools.eventCount(context, "zhaopin");
                    context.startActivity(new Intent(YMApplication.getAppContext(),
                            RecruitCenterMainActivity.class).putExtra("type", "false"));
                }
                break;

            //[服务] 预约转诊
            case 10:
                goToYuYue(context);
                break;

            // 签约居民
            case 11:
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(context).context);
                } else {
                    intent.setClass(context, MediaCenterActivity.class);
                    intent.putExtra("type", 4);
                    context.startActivity(intent);
                }
                break;
            // 在线诊室
            case 12:
                if (!TextUtils.isEmpty(YMApplication.getLoginInfo().getData().getZxzhsh())&&
                        "1".equals(YMApplication.getLoginInfo().getData().getZxzhsh())) {
                    context.startActivity(new Intent(context, OnlineRoomActivity.class));
                }else{
                    context.startActivity(new Intent(context,OnlineRoomCloseActivity.class));
                }
                break;
            case 13:
//                WebViewActivity.start((Activity) context,"极速电话", ConsultConstants.JSDH_URL);
                ServiceProvider.getJsdhCookie(YMUserService.getCurUserId(), new Subscriber<JSDHCookieBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.longToast("暂时无法进入");
                    }

                    @Override
                    public void onNext(JSDHCookieBean jsdhCookieBean) {
                        WebViewActivity.start((Activity) context,"极速电话", ConsultConstants.METHOD_GET_JSDH_URL,jsdhCookieBean.getData());
                    }
                });
                break;
            case 14:
                context.startActivity(new Intent(context,PatientManagerActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * 家庭医生 stone
     *
     * @param context
     */
    private static void goToFamilyDoctor(final Context context) {
        DialogUtil.showUserCenterCertifyDialog(context, new MyCallBack() {
            @Override
            public void onClick(Object data) {
                //开通状态 -1未开通 0待审核 1已开通 2关闭 3暂时关闭
                int enterHomeDoc = parseInt(YMApplication.getLoginInfo().getData().getXiaozhan().getFamily());
                if (enterHomeDoc == Integer.parseInt(Constants.FUWU_AUDIT_STATUS_1)) {
                    Intent intent = new Intent();
                    StatisticalTools.eventCount(context, "familydoctor");
                    intent.setClass(YMApplication.getAppContext(), ServiceHomeDoctorActivity.class);
                    context.startActivity(intent);
                } else if (enterHomeDoc == Integer.parseInt(Constants.FUWU_AUDIT_STATUS_0)) {
//                    CheckStateActivity.startActivity(context, "checking", "审核中");
                    Intent intent = new Intent();
                    intent.setClass(YMApplication.getAppContext(), ApplyBusinessResultActivity.class);
                    intent.putExtra(Constants.INTENT_KEY_TITLE, Constants.FAMILYDOCTOR_CN);
                    context.startActivity(intent);
                } else if (enterHomeDoc == Integer.parseInt(Constants.FUWU_AUDIT_STATUS_NO)) {
                    //stone 直接进入新的开通页面
                    Intent intent = new Intent();
                    intent.setClass(YMApplication.getAppContext(), ApplyForFamilyDoctorActivity.class);
                    context.startActivity(intent);
                    //老的提示 进入开通页面
//                    showApproveDialog(context, "您还没有开通家庭医生", "离开通家庭医生只有一步之遥了，快去申请吧！",
//                            FamDoctorOpenActivity.class);
                } else if (enterHomeDoc == Integer.parseInt(Constants.FUWU_AUDIT_STATUS_2) || enterHomeDoc == Integer.parseInt(Constants.FUWU_AUDIT_STATUS_3)) {
//                    DialogUtil.NewDialog(context, "家庭医生服务已关闭");
                    Intent intent = new Intent();
                    intent.setClass(YMApplication.getAppContext(), ApplyBusinessResultActivity.class);
                    intent.putExtra(Constants.INTENT_KEY_TITLE, Constants.FAMILYDOCTOR_CN);
                    context.startActivity(intent);
                }
            }
        }, null, "家庭医生");
    }

    /**
     * 电话医生 stone
     *
     * @param context
     */
    private static void goToPhoneDoctor(final Context context) {
        DialogUtil.showUserCenterCertifyDialog(context, new MyCallBack() {
            @Override
            public void onClick(Object data) {
                //开通状态 -1未开通 0待审核 1已开通 2关闭 3暂时关闭
                String enterPhone = YMApplication.getLoginInfo().getData().getXiaozhan().getPhone();
                if (Constants.FUWU_AUDIT_STATUS_1.equals(enterPhone)) {
                    StatisticalTools.eventCount(context, "Calldoctor");
                    Intent intent = new Intent();
                    intent.setClass(YMApplication.getAppContext(), ServiceTelDoctor.class);
                    context.startActivity(intent);
                } else {
                    //联系客服开通电话医生
                    DialogUtil.showCustomDialog(context, R.layout.custom_dialogue, "", "请联系寻医问药客服帮您开通此服务：4008591200", "",
                            "呼叫", "取消", new MyCallBack<Object>() {
                                @Override
                                public void onClick(Object data) {
                                    Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4008591200"));//跳转到拨号界面，同时传递电话号码
                                    context.startActivity(dialIntent);
                                }
                            });
                }
                //目前电话医生的后台还未开发完，暂时先注销
//                else if (Constants.FUWU_AUDIT_STATUS_0.equals(enterPhone)) {
//                    CheckStateActivity.startActivity(context, "checking", "审核中");
//                } else if (Constants.FUWU_AUDIT_STATUS_NO.equals(enterPhone)) {
//                    showApproveDialog(context, "您还没有开通电话医生", "离开通电话医生只有一步之遥了，快去申请吧!",
//                            PhoneDoctorOpenActiviy.class);
//                } else if (Constants.FUWU_AUDIT_STATUS_2.equals(enterPhone) || Constants.FUWU_AUDIT_STATUS_3.equals(enterPhone)) {
//                    DialogUtil.NewDialog(context, "电话医生服务已关闭");
//                }
            }
        }, null, "电话医生");
    }

    /**
     * 预约转诊 stone
     *
     * @param context
     */
    private static void goToYuYue(final Context context) {
        DialogUtil.showUserCenterCertifyDialog(context, new MyCallBack() {
            @Override
            public void onClick(Object data) {
                //开通状态 -1未开通 0待审核 1已开通 2关闭 3暂时关闭
                int yuyue = parseInt(YMApplication.getLoginInfo().getData().getXiaozhan().getYuyue());
                if (yuyue == 1) {
                    StatisticalTools.eventCount(context, "registration");

                    AjaxParams params = new AjaxParams();
                    params.put("userid", YMApplication.getLoginInfo().getData().getPid());
                    params.put("sign", computeSign(YMApplication.getPID()));
                    FinalHttp fh = new FinalHttp();
                    fh.post(CommonUrl.MAKE_ADD_NUM_TITLE, params, new AjaxCallBack() {
                        @Override
                        public void onFailure(Throwable t, int errorNo, String strMsg) {
                            super.onFailure(t, errorNo, strMsg);
                            Toast.makeText(YMApplication.getAppContext(), "网络繁忙，请稍后重试", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(String t) {
                            super.onSuccess(t);
                            ArrayList<QueData> mLables = new ArrayList<>();
                            try {
                                JSONObject jsonObject = new JSONObject(t.toString());
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");
                                JSONArray array = jsonObject.getJSONArray("data");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonElement = array.getJSONObject(i);
                                    QueData data = new QueData();
                                    data.setName(jsonElement.getString("name"));
                                    data.setStatus(jsonElement.getString("state"));
                                    mLables.add(data);
                                }
                                if (code == 0) {
                                    Intent intent = new Intent();
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("mLables", mLables);
                                    intent.putExtras(bundle);
                                    intent.setClass(context, ServiceMakeApp.class);
                                    context.startActivity(intent);
                                } else {
                                    ToastUtils.shortToast(msg);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                } else {
                    //联系客服开通预约转诊
                    DialogUtil.showCustomDialog(context, R.layout.custom_dialogue, "", "请联系寻医问药客服帮您开通此服务：4008591200", "",
                            "呼叫", "取消", new MyCallBack<Object>() {
                                @Override
                                public void onClick(Object data) {
                                    Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4008591200"));//跳转到拨号界面，同时传递电话号码
                                    context.startActivity(dialIntent);
                                }
                            });
                }
                //暂时先注销，目前预约转诊后台还未完成
//                else if (yuyue == 0) {
//                    CheckStateActivity.startActivity(context, "checking", "审核中");
//                } else if (yuyue == -1) {
//                    showApproveDialog(context, "您还没有开通预约转诊", "离开通预约转诊只有一步之遥了，快去申请吧",
//                            AppointActivity.class);
//                } else if (yuyue == 2 || yuyue == 3) {
//                    DialogUtil.NewDialog(context, "预约转诊服务已关闭");
//                }
            }
        }, null, "预约转诊");
    }


    //去往在线咨询 stone
    public static void goIMChat(final Context context) {
        DialogUtil.showUserCenterCertifyDialog(context, new MyCallBack() {
            @Override
            public void onClick(Object data) {
                StatisticalTools.eventCount(context, "Onlineconsulting");
                LoginInfo login1 = YMApplication.getLogin1();
                if (null != login1) {
                    LoginInfo.UserData userData = login1.getData();
                    if (null != userData) {
                        if (userData.getImwd() == 1) {
                            ConsultOnlineActivity.startActivity(context);
                        } else {
                            DialogUtil.NewDialog(context, "请联系客服帮你开通，客服电话4008591200");
                        }
                    } else {
                        DialogUtil.NewDialog(context, "请联系客服帮你开通，客服电话4008591200");
                    }
                } else {
                    DialogUtil.NewDialog(context, "请联系客服帮你开通，客服电话4008591200");
                }
            }
        }, null, "即时问答");
    }

    //去往问题广场 stone
    public static void gotoQuestions(final Context context, final boolean isPush, final int type) {
        DialogUtil.showUserCenterCertifyDialog(context, new MyCallBack() {
            @Override
            public void onClick(Object data) {
                LoginInfo login1 = YMApplication.getLogin1();
                if (null != login1) {
                    LoginInfo.UserData userData = login1.getData();
                    if (null != userData) {
                        ClinicStatInfo clinicStatInfo = userData.getXiaozhan();
                        Intent intent = new Intent(context, ApplyBusinessResultActivity.class);
                        if (null != clinicStatInfo) {
                            if (Constants.FUWU_AUDIT_STATUS_1.equals(clinicStatInfo.getDati())) { //已开通
                                getContentData(context, isPush, type);
                            } else if (Constants.FUWU_AUDIT_STATUS_0.equals(clinicStatInfo.getDati())
                                    || Constants.FUWU_AUDIT_STATUS_NO.equals(clinicStatInfo.getDati())) { //审核中或者未通过
                                intent.putExtra(Constants.INTENT_KEY_TITLE, Constants.CLUB_CN);
                                context.startActivity(intent);
                            } else {
                                DialogUtil.NewDialog(context, "请联系客服帮你开通，客服电话4008591200");
                            }
                        } else {
                            DialogUtil.NewDialog(context, "请联系客服帮你开通，客服电话4008591200");
                        }
                    } else {
                        DialogUtil.NewDialog(context, "请联系客服帮你开通，客服电话4008591200");
                    }
                } else {
                    DialogUtil.NewDialog(context, "请联系客服帮你开通，客服电话4008591200");
                }

            }
        }, null, "问题广场");
    }

    //获取问题广场数据 stone
    public static List<QueData> getContentData(final Context context, final boolean isPush, final int type) {
        final List<QueData> mLables = new ArrayList<>();
        AjaxParams params = new AjaxParams();
        params.put("userid", YMApplication.getLoginInfo().getData().getPid());
        params.put("sign", MD5Util.MD5(YMApplication.getLoginInfo().getData().getPid()
                + Constants.MD5_KEY));
        FinalHttp request = new FinalHttp();
        request.post(CommonUrl.QUE_TITLE, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);

                Map<String, Integer> indexMap = new HashMap<String, Integer>();

                String firstStr = "";
                int first = 0;
                int isJump = 0;
                int backNum = 0;
                int hisnum = 0;
                try {
                    JSONObject jsonObject = new JSONObject(s.toString());
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("msg");
                    if (code == 0) {
                        JSONObject jsonElement = jsonObject.getJSONObject("data");
                        firstStr = jsonElement.optString("first");
                        isJump = jsonElement.getInt("isJump");
                        backNum = jsonElement.getInt("backNum");
                        hisnum = jsonElement.getInt("hisnum");
                        JSONArray array = jsonElement.getJSONArray("menu");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonChild = array.getJSONObject(i);
                            QueData queData = new QueData();

                            queData.setName(jsonChild.getString("name"));
                            String url = jsonChild.getString("url");
                            indexMap.put(url, i);
                            queData.setUrl(url);
                            queData.setNum(jsonChild.getInt("num"));
                            mLables.add(queData);
                        }

                    } else {
                        ToastUtils.shortToast(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (mLables.size() > 0) {
                    YMApplication.getInstance().setmLables(mLables);
                    Bundle bundle = new Bundle();
                    if (isPush) {
                        switch (type) {
                            case 1:
                                bundle.putInt("first", mLables.size() - 2);
                                break;

                            case 2:
                                bundle.putInt("first", mLables.size() - 1);
                                break;

                            default:
                                break;
                        }
                    } else {
                        if (!indexMap.containsKey(firstStr)) {
                            LogUtils.e("服务端first参数错误:不在给定tab内");
                        }

                        int index = indexMap.containsKey(firstStr) ? indexMap.get(firstStr) : 0;
                        bundle.putInt("first", index);
                    }
                    bundle.putInt("isJump", isJump);
                    bundle.putInt("backNum", backNum);
                    bundle.putInt("hisnum", hisnum);
                    bundle.putInt("from", 0);
                    Intent queIntent = new Intent(context, ServiceQueActivity.class);
                    queIntent.putExtra("data", bundle);
                    context.startActivity(queIntent);
                } else {
                    Toast.makeText(context, "数据加载出错，请稍后再试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                Toast.makeText(context, "网络繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
            }
        });

        return mLables;
    }

    public static void getBackNum(final Context context) {
        AjaxParams params = new AjaxParams();
        params.put("userid", YMApplication.getLoginInfo().getData().getPid());
        params.put("sign", MD5Util.MD5(YMApplication.getLoginInfo().getData().getPid()
                + Constants.MD5_KEY));
        FinalHttp fh = new FinalHttp();

        fh.post(CommonUrl.DP_COMMON + "command=backNum", params, new AjaxCallBack() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jsonObject = new JSONObject(t.toString());
                    int code = jsonObject.getInt("code");
                    JSONObject jsonElement = jsonObject.getJSONObject("data");
                    if (code == 0) {
                        int backNum = jsonElement.getInt("num");
                        Intent intent = new Intent(context, QueMyReplyActivity.class);
                        intent.putExtra("backNum", backNum);
                        context.startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public static void initSystemBar(Activity activity) {

        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0以上的手机
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            ViewGroup content = (ViewGroup) activity.findViewById(android.R.id.content);
            ViewGroup childView = (ViewGroup) content.getChildAt(0);
            if (childView != null) {
                childView.setFitsSystemWindows(true);
            }
            window.setStatusBarColor(Color.WHITE);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4以上5.0一下的手机
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            ViewGroup content = (ViewGroup) activity.findViewById(android.R.id.content);
            ViewGroup childView = (ViewGroup) content.getChildAt(0);
            if (childView != null) {
                childView.setFitsSystemWindows(true);
            }

            View view = new View(activity);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusHeight(activity)));
            view.setBackgroundColor(Color.WHITE);
            content.addView(view);
        }


        applay(activity, true);


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            //android6.0以后可以对状态栏文字颜色和图标进行修改
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }


//        initSystemBar(activity, R.drawable.toolbar_bg_no_alpha_new);
    }

    public static void applay(Activity activity, boolean isDarkFont) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            IStatusBar statusBar = null;
            if (OSUtils.isMeizuFlymeOS()) {
                statusBar = new MeizuStatusBar();
            } else if (OSUtils.isMIUI()) {
                statusBar = new MiuiStatusbar();
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                statusBar = new OSM();
            }

            if (statusBar != null) {
                statusBar.setStatusBarLightMode(activity, isDarkFont);
            }

        }

    }

    public static void initSystemBar(Activity activity, int bgResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(activity, true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(bgResId);
    }


//    public static void initSystemBar(Activity activity, int color) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatus(activity, true);
//        }
//
//        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
//        tintManager.setStatusBarTintEnabled(true);
//        tintManager.setStatusBarTintColor(color);
//    }

    public static void initSystemBar(Activity activity, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(activity, true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintDrawable(drawable);
    }

    @TargetApi(19)
    private static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }

        win.setAttributes(winParams);
    }


    /**
     * 展示未获得权限对话框
     *
     * @param context
     * @param msg
     * @param requestCode
     */
    public static void permissionRequestDialog(final Context context, String msg, final int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("去授权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 转跳到app详细信息页面，进行授权操作
                if (context instanceof AppCompatActivity) {
                    ((AppCompatActivity) context).startActivityForResult(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + context.getPackageName())), requestCode);
                } else if (context instanceof Activity) {
                    ((Activity) context).startActivityForResult(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + context.getPackageName())), requestCode);
                } else {
                    throw new UnsupportedOperationException();
                }

                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 启动相机
     *
     * @param context
     * @param requestCode
     * @return
     */
    public static File startCamera(Context context, int requestCode) {
        View view = null;
        if (context instanceof AppCompatActivity) {
            view = ((AppCompatActivity) context).getCurrentFocus();
        } else if (context instanceof Activity) {
            view = ((Activity) context).getCurrentFocus();
        }
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        File origUri = null;
        if (PermissionUtils.checkPermission(context, Manifest.permission.CAMERA)) {
            CommonUtils.permissionRequestDialog(context, "无法打开相机，请授予照相机(Camera)权限", 123);
        } else {
            // 照片命名
            origUri = new File(PathUtil.getInstance().getImagePath(), "osc_" + System.currentTimeMillis() + ".jpg");
            origUri.getParentFile().mkdirs();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(origUri));
            if (context instanceof AppCompatActivity) {
                ((AppCompatActivity) context).startActivityForResult(intent, requestCode);
            } else if (context instanceof Activity) {
                ((Activity) context).startActivityForResult(intent, requestCode);
            }
        }
        return origUri;
    }

    /**
     * 解析服务器返回的带有话题的动态内容
     *
     * @param context 上下文
     * @param content 需要解析的内容
     * @return 解析后生成的SpannableString
     */
    public static SpannableString parsePostContent(final Context context, String content) {
        if (content == null) {
            return null;
        }

        if ("".equals(content)) {
            return new SpannableString("");
        }

        // 用于保存话题id和话题名称
        List<Map<String, String>> topicLists = new ArrayList<>();
        DLog.d("parsePostContent", "content before = " + content);
        // 设置正则匹配所有话题
        Pattern pattern = Pattern.compile(Constants.TOPIC_REGEX);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            // 设置正则匹配所有话题的id，并取出保存
            Pattern p = Pattern.compile(Constants.TOPIC);
            Matcher m = p.matcher(matcher.group());
            while (m.find()) {
                String group = m.group();
                Map<String, String> topicMaps = new HashMap<>();
                topicMaps.put(group.trim().replaceAll("\\$", ""), matcher.group().replaceAll(Constants.TOPIC, ""));
                content = content.replace(matcher.group(), matcher.group().replaceAll(Constants.TOPIC, ""));
                topicLists.add(topicMaps);
            }
        }
        LogUtils.d("parsePostContent" + "content after = " + content);

        // 遍历字符串并找出话题的位置，设置ClickableSpan
        SpannableString spannableString = new SpannableString(content);
        LogUtils.d("parsePostContent" + "spannableString before = " + spannableString);
        try {
            int preEnd = 0;
            if (!topicLists.isEmpty()) {
                LogUtils.d("parsePostContent" + "topicLists size = " + topicLists.size());
                for (int i = 0; i < topicLists.size(); i++) {
                    Map<String, String> map = topicLists.get(i);
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        final String topicId = entry.getKey();
                        final String topicName = entry.getValue();
                        LogUtils.d("parsePostContent" + "topicName = " + topicName + ", topicID = " + topicId);
                        int start = spannableString.toString().substring(preEnd, spannableString.length()).indexOf(topicName, 0) + preEnd;
                        int end = start + topicName.length();
                        DLog.d("parsePostContent", "start = " + start + ", end = " + end);
                        preEnd = end;
                        // 话题点击事件
                        MyClickSpan myClickSpan = new MyClickSpan() {
                            @Override
                            public void onClick(View widget) {
                                DLog.d("parsePostContent", "点击了" + topicName + ", " + topicId);
                                Intent intent = new Intent(context, NewTopicDetailActivity.class);
                                intent.putExtra(NewTopicDetailActivity.TOPICID_INTENT_PARAM_KEY, parseInt(topicId));
                                context.startActivity(intent);
                            }
                        };
                        if (start != -1 && end != -1) {
                            spannableString.setSpan(myClickSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                }
            }
            DLog.d("parsePostContent", "spannableString after = " + spannableString);
        } catch (Exception e) {
            DLog.d("topic parse", e.getMessage());
        }

        return spannableString;
    }

    public static void showWarnDialog(final Context context, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        if (context instanceof DoctorCircleSendMessageActivty) {
                            ((DoctorCircleSendMessageActivty) context).finish();
                        }
                    }
                })
                .show();
    }

    /**
     * 获取状态栏高度
     *
     * @param context 上下文
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }

        return statusBarHeight;
    }

    /**
     * 获取ActionBar高度
     */
    public static int getActionBarHeight(Context context) {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }

        return actionBarHeight;
    }

    /**
     * 显示对话框
     * 通用
     *
     * @param context     上下文
     * @param title       对话框标题
     * @param message     对话框内容
     * @param positive    确定按钮
     * @param negative    取消按钮
     * @param target      目标转跳class
     * @param contentView 自定义内容view
     */
    public static void showCommonDialog(final Context context, String title, String message, String positive,
                                        String negative, final Class target, View contentView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }
        if (TextUtils.isEmpty(positive)) {
            positive = "OK";
        }
        builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (target != null) {
                    context.startActivity(new Intent(context, target));
                }
            }
        });
        if (TextUtils.isEmpty(negative)) {
            negative = "CANCEL";
        }
        builder.setNegativeButton(negative, null);
        if (contentView != null) {
            builder.setView(contentView);
        }
        builder.show();
    }


    /**
     * 计算sign
     */
    public static String computeSign(String s) {
        return MD5Util.MD5(s + BuildConfig.md5_key);
    }

    /**
     * Show or hide a view
     *
     * @param view view to show or hide
     * @param b    true if show, false if hide
     */
    public static void showOrHideView(View view, boolean b) {
        int v = b ? View.VISIBLE : View.GONE;
        if (view != null) {
            view.setVisibility(v);
        }
    }

    /**
     * Show or hide views
     */
    public static void showOrHideViews(View[] views, boolean[] b) {
        if (views != null && b != null) {
            if (views.length != b.length) {
                return;
            }

            if (views.length > 0) {
                for (int i = 0; i < views.length; i++) {
                    views[i].setVisibility(b[i] ? View.VISIBLE : View.GONE);
                }
            }

        }
    }

    /**
     * 乐视云点播参数设置
     * 没有的数值传空字符串""
     *
     * @param uuid      必须
     * @param vuid      必须
     * @param checkCode 非必须，收费视频需要
     * @param userKey   非必须，收费视频需要
     * @param playName  非必须，视频名称
     */
    public static Bundle setVodParams(String uuid, String vuid, String checkCode, String userKey, String playName) {
        Bundle mBundle = new Bundle();
        mBundle.putInt(PlayProxy.PLAY_MODE, EventPlayProxy.PLAYER_VOD);
        mBundle.putString(PlayProxy.PLAY_UUID, uuid);
        mBundle.putString(PlayProxy.PLAY_VUID, vuid);
        mBundle.putString(PlayProxy.PLAY_CHECK_CODE, checkCode);
        mBundle.putString(PlayProxy.PLAY_PLAYNAME, playName);
        mBundle.putString(PlayProxy.PLAY_USERKEY, userKey);
        // mBundle.putString(PlayProxy.PLAY_SERVICE_NUMBER, "业务线");
        // mBundle.putString(PlayProxy.PLAY_CUSTOMERID, "用户id");
        return mBundle;
    }

    public static void showOrHideSoftKeyboard(Context context, boolean b, View focus) {
        if (context != null && focus != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (b) {
                imm.showSoftInputFromInputMethod(focus.getWindowToken(), InputMethodManager.SHOW_FORCED);
            } else {
                imm.hideSoftInputFromWindow(focus.getWindowToken(), 0);
            }
        }
    }

    public static void showLoadingIndicator(final SwipeRefreshLayout refreshLayout, final boolean active) {
        if (refreshLayout != null) {
            refreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setRefreshing(active);
                }
            });
        }
    }

    public static void setTransparentBar(ViewGroup toolbar, Context ctx, int topPadding) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams layoutParams = toolbar.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = topPadding + getStatusBarHeight(ctx);
            toolbar.setLayoutParams(layoutParams);
            toolbar.setPadding(0, getStatusBarHeight(ctx), 0, 0);
        }
    }


    /**
     * 获取状态栏高度
     *
     * @return
     */
    private static int getStatusHeight(Activity activity) {
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return activity.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
