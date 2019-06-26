package com.xywy.askforexpert;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EasyUtils;
import com.hyphenate.util.NetUtils;
import com.umeng.analytics.MobclickAgent;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ACache;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.DebugUtils;
import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.ScreenUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.AddressBook;
import com.xywy.askforexpert.model.CMDMessageInfo;
import com.xywy.askforexpert.model.Message;
import com.xywy.askforexpert.model.doctor.Messages;
import com.xywy.askforexpert.model.im.group.GroupModel;
import com.xywy.askforexpert.model.media.MediaList;
import com.xywy.askforexpert.model.newdoctorcircle.CircleMsgs;
import com.xywy.askforexpert.model.notice.Notice;
import com.xywy.askforexpert.model.websocket.msg.chatmsg.ChatMsg;
import com.xywy.askforexpert.module.consult.activity.ConsultChatActivity;
import com.xywy.askforexpert.module.consult.activity.ConsultOnlineActivity;
import com.xywy.askforexpert.module.consult.activity.SumUpActivity;
import com.xywy.askforexpert.module.discovery.DiscoverFragment;
import com.xywy.askforexpert.module.discovery.DoctorOneDayActivity;
import com.xywy.askforexpert.module.discovery.medicine.common.dialog.MsDialogBuilder;
import com.xywy.askforexpert.module.discovery.medicine.module.appupdate.UpdateAppRequest;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity.AppVersion;
import com.xywy.askforexpert.module.discovery.medicine.module.web.WebActivity;
import com.xywy.askforexpert.module.docotorcirclenew.fragment.tab.DocCircleMainFragment;
import com.xywy.askforexpert.module.drug.OnlineChatDetailActivity;
import com.xywy.askforexpert.module.drug.OnlineRoomActivity;
import com.xywy.askforexpert.module.information.InfoFragment;
import com.xywy.askforexpert.module.main.HomeFragment;
import com.xywy.askforexpert.module.main.patient.activity.PatientManagerActivity;
import com.xywy.askforexpert.module.main.service.que.QueDetailActivity;
import com.xywy.askforexpert.module.main.service.que.QueListPage;
import com.xywy.askforexpert.module.message.HappyBirthFragment;
import com.xywy.askforexpert.module.message.MessageInfoFragment;
import com.xywy.askforexpert.module.message.imgroup.ContactService;
import com.xywy.askforexpert.module.message.msgchat.ChatFragment;
import com.xywy.askforexpert.module.message.msgchat.ChatMainActivity;
import com.xywy.askforexpert.module.message.msgchat.ChatSendMessageHelper;
import com.xywy.askforexpert.module.message.notice.request.GetNoticeListRequest;
import com.xywy.askforexpert.module.message.usermsg.UserMsgFragment;
import com.xywy.askforexpert.module.my.UserCenterFragment;
import com.xywy.askforexpert.module.my.setting.utils.VersionUtils;
import com.xywy.askforexpert.module.websocket.WebSocketRxBus;
import com.xywy.askforexpert.sdk.jpush.MyReceiver;
import com.xywy.askforexpert.widget.view.arcmenu.ArcLayout;
import com.xywy.askforexpert.widget.view.arcmenu.ArcMenu;
import com.xywy.component.uimodules.photoPicker.PhotoPreviewActivity;
import com.xywy.datarequestlibrary.XywyDataRequestApi;
import com.xywy.datarequestlibrary.downloadandupload.listener.OkDownloadListener;
import com.xywy.easeWrapper.EMChatManager;
import com.xywy.easeWrapper.EMContactManager;
import com.xywy.easeWrapper.EaseConstant;
import com.xywy.easeWrapper.controller.EMNotifier;
import com.xywy.easeWrapper.controller.EaseUI;
import com.xywy.easeWrapper.db.InviteMessage;
import com.xywy.easeWrapper.db.InviteMessgeDao;
import com.xywy.easeWrapper.domain.EaseUser;
import com.xywy.im.sdk.IMServiceLogOutObserver;
import com.xywy.im.sdk.XywyIMService;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.xywy.retrofit.rxbus.Event;
import com.xywy.retrofit.rxbus.EventSubscriber;
import com.xywy.uilibrary.dialog.pndialog.XywyPNDialog;
import com.xywy.uilibrary.dialog.pndialog.listener.PositiveDialogListener;
import com.xywy.util.FilePathUtil;
import com.xywy.util.L;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import static com.xywy.askforexpert.YMApplication.APPLICATIONID;
import static com.xywy.askforexpert.appcommon.utils.update.SafetyUtils.getFileMD5;
import static com.xywy.askforexpert.module.consult.ConsultConstants.LOG_OUT_FALG;

/**
 * 主页
 * stone
 * 2017/12/6 下午2:11
 */
public class MainActivity extends YMBaseActivity implements OnClickListener, UserMsgFragment.OnFragmentInteractionListener ,
        IMServiceLogOutObserver {

    private int[] location = new int[2];

    private static final String TAG = "MainActivity";

    /**
     * 账号在别处登录
     */
    public boolean isConflict = false;
    String uid = "";
    private SharedPreferences sp, remindSP;

    private HomeFragment newHomeFragment;
    private MessageInfoFragment messageInfoFragment;
    private InfoFragment infoFragment;
    private DocCircleMainFragment docCircleMainFragment;
    private DiscoverFragment discoverFragment;
    private UserCenterFragment userCenterFragment;
    private Fragment[] fragments;
    private int currentTabIndex;
    private View[] btn_tab;
    private int index;
    /**
     * 账号被移除
     */
    private boolean isCurrentAccountRemoved = false;
    /**
     * 未读消息textview
     */
    private TextView unreadLabel;
    /**
     * 医圈未读消息数
     */
    private TextView unread_circle_num;
    private ImageView circle_remind_iv;
    private ArcMenu arc_menu;
    private InviteMessgeDao inviteMessgeDao;
    private String type = "";
    private boolean isPush;
    private int typeId = 0;
    //    private PushMsgReceiver pushMsgReceiver;
    private GoToCircleReceiver goToCircleReceiver;
    private ImageView tv_red_main4;
    private AddressBook pListInfo;
    private ACache mCache;
    private boolean isBirthday = false;
    //    private FragmentManager manager;
    private FragmentTransaction transaction;
    private Fragment currentFragment;
    private boolean isAccountRemovedDialogShow;
    private long mExitTime;


    private MyContactListener contactListener;
    private MyConnectionListener connectionListener;
    private ArcLayout arc_layout;
    private TextView tv_circle, tv_circle_pubish;
    protected NotificationManager notificationManager;
    private XywyPNDialog dialog;
    private String url;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    private boolean isFistin;

    //用药助手
    private String mAppSecretKey;

    public void init() {
        if (!YMUserService.isGuest()) {
            try {
                ContactService.getInstance().init(YMUserService.getCurUserId());
                ChatSendMessageHelper.getMsgFriendCard(MainActivity.this);

                if (YMApplication.getLoginInfo() != null) {
                    uid = YMApplication.getLoginInfo().getData().getPid().trim();
                    // String[] sArray = jpust_tag.split(",");
                    Set<String> tagSet = new LinkedHashSet<String>();
                    tagSet.add(uid);
                    //stone 极光推送 添加设置别名也为uid(跟ios保持一致)
                    JPushInterface.setAliasAndTags(getApplicationContext(), uid, tagSet, mTagsCallback);

                    if (YMApplication.DoctorType() == 1) {
                        msgXiaoZhu();
                    }
                } else {
                    uid = "";
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        checkAccountState();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    private void checkAccountState() {
        if (getIntent().getBooleanExtra("conflict", false)) {
            showConflictDialogNew();
        } else if (getIntent().getBooleanExtra(EaseConstant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }
    }

    /**
     * 注册广播
     */
    private void setBroadcastReceiver() {
        // 注册一个接收消息的BroadcastReceiver

        inviteMessgeDao = new InviteMessgeDao();


        EMChatManager.getInstance().addMessageListener(messageListener);

        contactListener = new MyContactListener();
        connectionListener = new MyConnectionListener();
        EMContactManager.getInstance().setContactListener(contactListener);
        // 注册一个监听连接状态的listener
        EMChatManager.getInstance().addConnectionListener(connectionListener);

        //// TODO: 16/8/17 shijiazi
        //EMChat.getInstance().setAppInited();

        IntentFilter needUpdateFilter = new IntentFilter("needUpdate");
        needUpdateFilter.addAction(EaseConstant.ACTION_GROUP_CHANAGED);
        needUpdateFilter.setPriority(3);
        registerReceiver(needUpdateReceive, needUpdateFilter);

        if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
            //用药助手暂时不用
            // 在此处进行加入广播监听状态是否发生改变消息推送
            //stone 去掉这些 现在的推送逻辑改变了
//            IntentFilter filter = new IntentFilter(BuildConfig.APPLICATION_ID);
//            pushMsgReceiver = new PushMsgReceiver();
//            registerReceiver(pushMsgReceiver, filter);
        } else {
            // 在此处进行加入广播监听状态是否发生改变消息推送
            //stone 去掉这些 现在的推送逻辑改变了
//            IntentFilter filter = new IntentFilter("com.main.pushchange");
//            pushMsgReceiver = new PushMsgReceiver();
//            registerReceiver(pushMsgReceiver, filter);
            IntentFilter flCircle = new IntentFilter(QueListPage.STATICACTION);
            goToCircleReceiver = new GoToCircleReceiver();
            registerReceiver(goToCircleReceiver, flCircle);
        }


        YmRxBus.registerMessageCountChanged(new EventSubscriber<Messages>() {
            @Override
            public void onNext(Event<Messages> messagesEvent) {
                updateMessage(messagesEvent.getData());
            }
        }, this);
        YmRxBus.registerSignName(new EventSubscriber() {
            @Override
            public void onNext(Object o) {
                setMyInfoFragmentRedPoint();
            }
        }, this);

        YmRxBus.registerUpdateUnreadLabel(new EventSubscriber() {
            @Override
            public void onNext(Object o) {
                updateUnreadLabel();
            }
        }, this);
    }

    /**
     * 初始化
     */
    private void initview() {
        tv_red_main4 = (ImageView) findViewById(R.id.tv_red_main4);
        //        tv_red = (ImageView) findViewById(R.id.tv_red);
        setMyInfoFragmentRedPoint();
        unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
        unread_circle_num = (TextView) findViewById(R.id.unread_circle_num);

        circle_remind_iv = (ImageView) findViewById(R.id.circle_remind_iv);

        //stone 会出现已经添加的bug 重启时获取到而不new
        clearBug();
        if (infoFragment == null) {
            infoFragment = new InfoFragment();
        }
        if (discoverFragment == null) {
            discoverFragment = new DiscoverFragment();
        }
        if (userCenterFragment == null) {
            userCenterFragment = new UserCenterFragment();
        }
        if (docCircleMainFragment == null) {
            docCircleMainFragment = new DocCircleMainFragment();
        }

        if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {

            if (messageInfoFragment == null) {
                messageInfoFragment = new MessageInfoFragment();
            }
            //stone
            if (newHomeFragment == null) {
                newHomeFragment = new HomeFragment();
            }

            fragments = new Fragment[]{discoverFragment, messageInfoFragment, userCenterFragment, docCircleMainFragment, newHomeFragment};
            btn_tab = new View[5];
            btn_tab[0] = findViewById(R.id.btn_tab1);
            btn_tab[1] = findViewById(R.id.btn_tab2);
            btn_tab[2] = findViewById(R.id.btn_tab5);
            btn_tab[3] = findViewById(R.id.btn_tab4);
            btn_tab[4] = findViewById(R.id.btn_tab3);

            btn_tab[0].setOnClickListener(this);
            btn_tab[1].setOnClickListener(this);
            btn_tab[2].setOnClickListener(this);
        } else {
            //stone
            if (newHomeFragment == null) {
                newHomeFragment = new HomeFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("jpush", isPush);
                bundle.putInt("type", typeId);
                newHomeFragment.setArguments(bundle);
            }
            fragments = new Fragment[]{newHomeFragment, docCircleMainFragment, infoFragment, discoverFragment, userCenterFragment};
            btn_tab = new View[5];
            btn_tab[0] = findViewById(R.id.btn_tab1);
            btn_tab[1] = findViewById(R.id.btn_tab2);
            btn_tab[2] = findViewById(R.id.btn_tab3);
            btn_tab[3] = findViewById(R.id.btn_tab4);
            btn_tab[4] = findViewById(R.id.btn_tab5);

            btn_tab[0].setOnClickListener(this);
            btn_tab[1].setOnClickListener(this);
            btn_tab[2].setOnClickListener(this);
            btn_tab[3].setOnClickListener(this);
            btn_tab[4].setOnClickListener(this);
        }

        RelativeLayout rl_tab4 = (RelativeLayout) findViewById(R.id.rl_tab4);

        tv_circle = (TextView) findViewById(R.id.tv_circle);
        tv_circle_pubish = (TextView) findViewById(R.id.tv_circle_pubish);

        arc_layout = (ArcLayout) findViewById(R.id.arc_layout);
        arc_layout.setmRadius(ScreenUtils.getScreenWidth(this) / 2);
        arc_menu = new ArcMenu(arc_layout, tv_circle_pubish);
        initArcMenu(arc_menu);
        arc_menu.setCenterView(findViewById(R.id.main_bottom));
        arc_layout.setOnExpandChangeListener(new ArcLayout.OnExpandChangeListener() {
            @Override
            public void onExpandChanged(boolean expanded) {
                if (expanded) {
                    tv_circle_pubish.setText("取消");
                } else {
                    tv_circle_pubish.setText("发布");
                }
            }
        });
        // 默认显示首页
//        manager = getSupportFragmentManager();
        ShowFragment(0);
//        btn_tab[0].setSelected(true);
//        manager = getSupportFragmentManager();
//        transaction = manager.beginTransaction();
//        transaction.add(R.id.fragment_container, fragments[0], "0").commit();
//        currentFragment = fragments[0];
        registerOnlineConsultAskMsgListener();
        if (APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
            btn_tab[4].setVisibility(View.GONE);
            rl_tab4.setVisibility(View.GONE);
        }
    }

    void setMyInfoFragmentRedPoint() {
        if (sp.getBoolean("main_isfist", false)) {
            tv_red_main4.setVisibility(View.GONE);
        }
    }

    private void initArcMenu(ArcMenu menu) {
        int[] itemDrawables = {R.drawable.add_news,
                R.drawable.add_media_pressed};
        String[] texts = {"实名动态", "匿名八卦"};
        final int itemCount = itemDrawables.length;
        TextView[] items = new TextView[itemCount];
        for (int i = 0; i < itemCount; i++) {
            TextView item = new TextView(this);
            Drawable drawable = getResources().getDrawable(itemDrawables[i]);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            item.setCompoundDrawables(null, drawable, null, null);
            item.setPadding(10, 20, 10, 20);
            item.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
            item.setBackgroundColor(getResources().getColor(R.color.black));
            item.setText(texts[i]);
            items[i] = item;
            final int index = i;
            menu.addItem(item, new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, DebugUtils.getViewStringId(v.getContext(), v) + index, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * 注册 在线咨询 提问消息全局监听
     */
    private void registerOnlineConsultAskMsgListener() {
        WebSocketRxBus.registerWebSocketChatMagListener(new EventSubscriber<ChatMsg>() {
            @Override
            public void onNext(final Event<ChatMsg> chatMsgEvent) {

                if (null == chatMsgEvent) {
                    return;
                }
                final ChatMsg chatMsg = chatMsgEvent.getData();
                if (null == chatMsg) {
                    return;
                }
                if (ChatMsg.RECV_MSG_TYPE_ASK == chatMsg.getBody().getType()) {
                    //stone 有新问题推送过来 刷新小红点
                    if (newHomeFragment != null && currentFragment == newHomeFragment && newHomeFragment.isAdded()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                newHomeFragment.refresh();
                            }
                        });

                    }
                    final Activity topActivity = YMApplication.getTopActivity();
                    //stone 总结页面和聊天页面和图片预览页不弹提示
                    if (!YMUserService.isGuest() && null != topActivity && !(topActivity instanceof ConsultChatActivity) &&
                            !(topActivity instanceof ConsultOnlineActivity) && !(topActivity instanceof SumUpActivity)
                            && !(topActivity instanceof PhotoPreviewActivity)
                            && !(topActivity instanceof OnlineChatDetailActivity)
                            && !(topActivity instanceof PatientManagerActivity)&&
                            !(topActivity instanceof OnlineRoomActivity) && !YMApplication.isTopActiviyPauseed()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                YMApplication.getInstance().showPop4NewMessage(chatMsg.getBody().getBsid(),chatMsg.getBody().getType());

                                //stone 老版本的对话框弹框提示  old
//                                final XywyPNDialog xywyPNDialog =
//                                        new XywyPNDialog.Builder()
//                                                .setContent("当前有分配给您的在线问答，请及时查看。")
//                                                .setCancelable(false)
//                                                .setPositiveStr("去看看")
//                                                .setNegativeStr("取消")
//                                                .create(topActivity, new PNDialogListener() {
//                                                    @Override
//                                                    public void onPositive() {
//                                                        isConsultChatMsgShow = false;
//                                                        //跳转到在线咨询消息列表页
//                                                        ConsultOnlineActivity.startActivity(topActivity);
//                                                    }
//
//                                                    @Override
//                                                    public void onNegative() {
//                                                        isConsultChatMsgShow = false;
//                                                    }
//                                                });
//                                xywyPNDialog.show();
//                                new CountDownTimer(60 * 1000, 60 * 1000) {
//
//                                    public void onTick(long millisUntilFinished) {
//                                    }
//
//                                    public void onFinish() {
//                                        //stone 修复bug (not attached to window manager)
//                                        if (null != xywyPNDialog && topActivity != null && !topActivity.isFinishing()) {
//                                            isConsultChatMsgShow = false;
//                                            xywyPNDialog.dismiss();
//                                        }
//                                    }
//                                }.start();

                            }
                        });

                    }
                }
            }
        }, this);
    }


    @Override
    public void onClick(View v) {
        if (APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
            switch (v.getId()) {
                case R.id.btn_tab1:
                    index = 0;
                    StatisticalTools.eventCount(MainActivity.this, "service");
                    ShowFragment(index);
//                    if (currentFragment == discoverFragment && discoverFragment != null) {
//                        //stone onresume去执行 刷新消息
//                        discoverFragment.refreshData();
//                    }
                    break;
                case R.id.btn_tab2:
                    index = 1;
                    if (!YMUserService.isGuest()) {
                        if (YMApplication.DoctorType() == 1) {
                            msgXiaoZhu();
                        }
                        if (currentFragment == fragments[1] && messageInfoFragment != null) {
                            messageInfoFragment.refresh();
                        }
                    }
                    StatisticalTools.eventCount(MainActivity.this, "message");
                    ShowFragment(index);
                    break;
                case R.id.btn_tab5:
                    index = 2;
                    StatisticalTools.eventCount(MainActivity.this, "mine");
                    ShowFragment(index);
                    break;
                default:
                    break;
            }
        } else {
            switch (v.getId()) {
                case R.id.btn_tab1:
                    index = 0;
                    StatisticalTools.eventCount(MainActivity.this, "service");
                    ShowFragment(index);
                    if (!YMUserService.isGuest()) {
                        if (YMApplication.DoctorType() == 1) {
                            msgXiaoZhu();
                        }
                        if (currentFragment == newHomeFragment && newHomeFragment != null) {
                            newHomeFragment.refresh();
                        }
                    }

                    break;
                case R.id.btn_tab2:
                    index = 1;
                    //                docCircleFragment.getDoctorNotNameFragment().setOnCircleRefreshListener(this);
                    //                docCircleFragment.getDoctorRealNameFragment().setOnCircleRefreshListener(this);
                    StatisticalTools.eventCount(MainActivity.this, "DoctorCircle");
                    if (currentFragment != fragments[1]) {
                        ShowFragment(index);
                    }
                    break;
                case R.id.btn_tab3:
                    index = 2;
                    if (!YMUserService.isGuest()) {
//                        if (YMApplication.DoctorType() == 1) {
//                            msgXiaoZhu();
//                        }
                        if (currentFragment != fragments[2] && infoFragment != null) {
                            infoFragment.refresh();
                        }
                    }
                    StatisticalTools.eventCount(MainActivity.this, "message");
                    ShowFragment(index);
                    break;

                case R.id.btn_tab4:
                    // sp.edit().putBoolean("main_isfist", true).commit();
                    // tv_red_main4.setVisibility(View.GONE);
                    index = 3;
                    StatisticalTools.eventCount(MainActivity.this, "discovery");
                    ShowFragment(index);
//                discoverFragment.refreshData();
                    break;
                case R.id.btn_tab5:
                    index = 4;
                    StatisticalTools.eventCount(MainActivity.this, "mine");
                    ShowFragment(index);
                    break;
                default:
                    break;
            }
        }

    }

    private void getNoticeCount() {
        GetNoticeListRequest.getInstance().getNewNotice(YMUserService.getCurUserId()).
                subscribe(new BaseRetrofitResponse<BaseData<Notice>>(){
                    @Override
                    public void onNext(BaseData<Notice> baseData) {
                        if (baseData.getData().new_read==1){
                            url = baseData.getData().new_url;
                            //                                    .setPositiveStrColor(R.color.c_25c3cd)
                            if (dialog!=null){
                                dialog.show();
                            } else {
                                dialog = new XywyPNDialog.Builder()
                                        .setCancelable(false)
                                        .setNoNegativeBtn(true)
                                        .setNoTitle(true)
//                                    .setPositiveStrColor(R.color.c_25c3cd)
                                        .setPositiveStr("请查看")
                                        .setContent("您有一条重要公告未读")
                                        .create(YMApplication.getInstance().getTopActivity(), new PositiveDialogListener() {
                                            @Override
                                            public void onPositive() {
                                                WebActivity.startActivity(MainActivity.this, url,"公告");
                                            }
                                        });
                            }

                        }

                        YMApplication.getInstance().setHasNewNotice((Constants.ZERO==baseData.getData().hs_read)?false:true);
                    }
                });
    }

    //stone tag用类名
    private void ShowFragment(int index) {
        //stone 自动重连 2018年01月11日13:19:59
        YMOtherUtils.autoConnectWebSocketIfNeed();

        if (index >= 0 && index < fragments.length) {
            transaction = getSupportFragmentManager().beginTransaction();
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragments[index].getClass().getName());
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            if (fragment == null) {
                transaction.add(R.id.fragment_container, fragments[index], fragments[index].getClass().getName()).show(fragments[index]).commitAllowingStateLoss();
            } else {
                transaction.show(fragment).commitAllowingStateLoss();
            }
            currentFragment = fragments[index];
            btn_tab[currentTabIndex].setSelected(false);
            btn_tab[index].setSelected(true);
            btn_tab[currentTabIndex].setEnabled(true);
            btn_tab[index].setEnabled(false);
            currentTabIndex = index;
        }
    }

    /**
     * 显示帐号在别处登录dialog
     */
    private void showConflictDialogNew() {
        //stone 测试时候防止被踢 上线要改
        YMUserService.setIsGuest(true);
        YMApplication.getInstance().disconnectWebSocket();
        new XywyPNDialog.Builder()
                .setCancelable(false)
                .setNoNegativeBtn(true)
                .setContent("同一账号已在其他设备登录")
                .create(YMApplication.getInstance().getTopActivity(), new PositiveDialogListener() {
                    @Override
                    public void onPositive() {
                        LOG_OUT_FALG = true;
                        YMUserService.ymLogout();
                    }
                });

    }

    /**
     * 帐号被移除的dialog
     */
    private void showAccountRemovedDialog() {
        YMUserService.setIsGuest(true);
        YMApplication.getInstance().disconnectWebSocket();
        new XywyPNDialog.Builder()
                .setCancelable(false)
                .setNoNegativeBtn(true)
                .setContent("当前登录用户环信账号已经被移除，请重新登录")
                .create(YMApplication.getInstance().getTopActivity(), new PositiveDialogListener() {
                    @Override
                    public void onPositive() {
                        YMUserService.ymLogout();
                    }
                });

    }


    @Override
    public void onFragmentInteraction(Uri uri) {
        DLog.i(TAG, "uri====" + uri);
    }

    @Override
    public void onLogOut() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isConflict = true;
                showConflictDialogNew();
            }
        });
    }


    /***
     * 好友变化listener
     */

    private class MyContactListener implements EMContactListener {

        @Override
        public void onContactDeleted(final String username) {

            runOnUiThread(new Runnable() {
                public void run() {
                    // 如果正在与此用户的聊天页面
                    String st10 = getResources().getString(R.string.have_you_removed);
                    if (ChatMainActivity.activityInstance != null && username.equals(ChatMainActivity.activityInstance.getToChatUsername())) {
                        Toast.makeText(MainActivity.this, ChatMainActivity.activityInstance.getToChatUsername() + st10, Toast.LENGTH_LONG).show();
                        ChatMainActivity.activityInstance.finish();
                    }
                    updateUnreadLabel();
                    // 刷新ui
                    // if (currentTabIndex == 1)
                    // contactListFragment.refresh();
                    // else
                    if (messageInfoFragment != null) {
                        DLog.i("shrmsg", "好友变化listener消息刷新在main创建时候");
                        messageInfoFragment.refresh();
                    }


                }
            });
        }

        @Override
        public void onContactAdded(String s) {

        }

        @Override
        public void onContactInvited(final String usernameOrigin, final String reason) {
            // TODO Auto-generated method stub
            // 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不需要重复提醒
            // tv_red.setMessage2(true);
            runOnUiThread(new Runnable() {
                public void run() {
                    String username = usernameOrigin;
                    List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();

                    DLog.i(TAG, "名字" + username + "原因==" + reason);

                    if (username.contains("sid")) {
                        inviteMessgeDao.deleteMessage(username);
                    }

                    for (InviteMessage inviteMessage : msgs) {
                        DLog.i(TAG, "邀请的人" + inviteMessage.getFrom());

                        if (!inviteMessage.getFrom().equals("doctor_assistant")) {

                            if (inviteMessage.getGroupId() == null && inviteMessage.getFrom().equals(username)) {
                                inviteMessgeDao.deleteMessage(username);
                            }

                        }
                    }

                    // 自己封装的javabean
                    InviteMessage msg = new InviteMessage();
                    msg.setFrom(username);
                    msg.setTime(System.currentTimeMillis() / 1000L);
                    msg.setReason(reason);
                    // Log.d(TAG, username + "请求加你为好友,reason: " + reason);
                    // 设置相应status
                    msg.setStatus(InviteMessage.InviteMesageStatus.BEINVITEED);


                    // msg.setUid(uid);

                    //显示在消息页面上

                    YMApplication.addMsgCreateTime = (System.currentTimeMillis() / 1000L) + "";
                    if (reason.contains("|")) {
                        YMApplication.addMsgDetail = reason.substring(reason.lastIndexOf("|") + 1, reason.length()) + "请求添加您为好友，快去通过吧！";
                    } else {
                        if (username.contains("did")) {
                            username = username.replaceAll("did_", "");
                        }
                        YMApplication.addMsgDetail = "用户" + username + "请求添加您为好友，快去通过吧！";
                    }

                    DLog.i("msg", "main 邀请" + YMApplication.addMsgDetail);
                    notifyNewIviteMessage(msg);
                    updateCardUnmber();
                }
            });

        }

        @Override
        public void onContactAgreed(String username) {
            // TODO Auto-generated method stub

            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
            // for (InviteMessage inviteMessage : msgs) {
            // if (inviteMessage.getFrom().equals(username)) {
            // return;
            // }
            // }
            // // 自己封装的javabean
            // InviteMessage msg = new InviteMessage();
            // msg.setFrom(username);
            // msg.setTime(System.currentTimeMillis());
            // System.out.println(username + "同意了你的好友请求");
            // msg.setStatus(InviteMesageStatus.BEAGREED);
            // notifyNewIviteMessage(msg);
            DLog.i(TAG, "同意");
            // if (msgs.size() <= 0) {
            // tv_red.setMessage(false);
            // }
            updateCardUnmber();
            //            if (messageInfoFragment!=null){
            //                messageInfoFragment.refresh();
            //            }

        }

        @Override
        public void onContactRefused(String arg0) {
            // TODO Auto-generated method stub
            DLog.i(TAG, "拒绝");
            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
            // if (msgs.size() <= 0) {
            // tv_red.setMessage(false);
            // }
            updateCardUnmber();
        }

    }

    /**
     * 连接监听listener
     */
    private class MyConnectionListener implements EMConnectionListener {

        @Override
        public void onConnected() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
//                    if (messageInfoFragment.errorItem != null) {
//                        messageInfoFragment.errorItem.setVisibility(View.GONE);
//                    }
                }

            });
        }

        @Override
        public void onDisconnected(final int error) {
            final String st1 = getResources().getString(R.string.Less_than_chat_server_connection);
            final String st2 = getResources().getString(R.string.the_current_network);
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (error == EMError.USER_REMOVED) {
                        isCurrentAccountRemoved = true;
                        // 显示帐号已经被移除
                        showAccountRemovedDialog();
                    } else if (error == EMError.USER_ALREADY_LOGIN) {
                        LogUtils.e("已登录");
                    } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        LogUtils.e("被踢出");
                        //TODO shijiazi 确定错误码
                        isConflict = true;
                        // 显示帐号在其他设备登陆dialog
//                        showConflictDialogNew();
                    } else {
                        //stone 增加判空
                        if (!YMUserService.isGuest() && messageInfoFragment != null) {
                            if (messageInfoFragment.errorItem != null) {
                                messageInfoFragment.errorItem.setVisibility(View.VISIBLE);
                                if (NetUtils.hasNetwork(MainActivity.this)) {
                                    messageInfoFragment.errorText.setText(st1);
                                }
                                // chatHistoryFragment.isVisibility(false, st1);
                                else {
                                    messageInfoFragment.errorText.setText(st2);
                                }
                                // chatHistoryFragment.isVisibility(false, st2);
                            }

                        }

                    }
                }

            });
        }
    }

    /**
     * 消息回执BroadcastReceiver
     */
    private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();

            String msgid = intent.getStringExtra("msgid");
            String from = intent.getStringExtra("from");

            EMConversation conversation = EMChatManager.getInstance().getConversation(from);
            if (conversation != null) {
                // 把message设为已读
                EMMessage msg = conversation.getMessage(msgid, true);

                if (msg != null) {

                    // 2014-11-5 修复在某些机器上，在聊天页面对方发送已读回执时不立即显示已读的bug
                    if (ChatMainActivity.activityInstance != null) {
                        if (msg.getChatType() == EMMessage.ChatType.Chat) {
                            if (from.equals(ChatMainActivity.activityInstance.getToChatUsername())) {
                                return;
                            }
                        }
                    }

                    msg.setAcked(true);
                }
            }

        }
    };

    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(final List<EMMessage> messages) {
            runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        for (int i = 0; i < messages.size(); i++) {
                            EMMessage message = messages.get(i);
                            if (message.getChatType() == EMMessage.ChatType.ChatRoom) {
                                break;
                            }
                            // 主页面收到消息后，主要为了提示未读，实际消息内容需要到chat页面查看
                            DLog.i(TAG, "消息来了");

                            // fix: logout crash， 如果正在接收大量消息
                            // 因为此时已经logout，消息队列已经被清空， broadcast延时收到，所以会出现message为空的情况
                            if (message == null) {
                                return;
                            }

                            // 2014-10-22 修复在某些机器上，在聊天页面对方发消息过来时不立即显示内容的bug
                            if (ChatFragment.activityInstance != null) {
                                if (message.getChatType() == EMMessage.ChatType.GroupChat) {
                                    if (message.getTo().equals(ChatMainActivity.activityInstance.getToChatUsername())) {
                                        return;
                                    }
                                } else {
                                    if (message.getFrom().equals(ChatMainActivity.activityInstance.getToChatUsername())) {
                                        return;
                                    }
                                }
                            }
                            // context.unregisterReceiver(this);
                            notifyNewMessage(message);

                            // 刷新bottom bar消息未读数
                            DLog.i(TAG, "消息刷");
                            updateUnreadLabel();
                            //            if (currentTabIndex == 1) {
                            // 当前页面如果为聊天历史页面，刷新此页面
                            //                if (chatHistoryFragment != null) {
                            //                    chatHistoryFragment.refresh();
                            //                }
                            if (messageInfoFragment != null) {
                                messageInfoFragment.refresh();
                            }
                        }
                        //            }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //red packet code : 处理红包回执透传消息
            for (EMMessage message : messages) {
                DLog.i(TAG, "收到透传消息111");
                if (message.getChatType() == EMMessage.ChatType.ChatRoom) {
                    break;
                }
                // 获取cmd message对象
                try {

                    String userid = YMApplication.getLoginInfo().getData().getHuanxin_username();
                    //                userid  = userid.substring(userid.indexOf("_")+1,userid.length());
                    if (message.getFrom().equals(userid)) {
                        return;
                    }
                    // 获取消息body
                    EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                    String action = cmdMsgBody.action();// 获取自定义action
                    if (!TextUtils.isEmpty(action) && "groupCMD".equals(action)) {
                        //                    String toRealname = message.getStringAttribute("toRealName");
                        //                    String toheadImg = message.getStringAttribute("toAvatar");
                        String command = message.getStringAttribute("command");
                        String content = message.getStringAttribute("content");
                        String txt = "";
                        Gson gson = new Gson();
                        CMDMessageInfo cmdMessageInfo;
                        cmdMessageInfo = gson.fromJson(content.trim(), CMDMessageInfo.class);
                        DLog.d(TAG, "MAIN group id = " + message.getTo());
                        DLog.i(TAG, "收到透传消息:cmd:" + command);
                        switch (command) {
                            case "updateName":
                                String groupName = cmdMessageInfo.getGroupName();
                                if (!TextUtils.isEmpty(groupName)) {
                                    txt = "管理员已将群名称改为" + groupName;
//                                    ContactService.getInstance().updateGroupName(message.getTo(),groupName);
                                }
                                break;
                            case "updateImg":
                                txt = "管理员已修改群头像";
                                String heading = cmdMessageInfo.getHeadUrl();
                                if (!TextUtils.isEmpty(heading)) {
                                    //                                groupModel.setHeadUrl(cmdMessageInfo.getHeadUrl());
                                    //                                cs.updateGroupinfo(groupModel);
                                }

                                break;
                            case "inviteUser":
                                if (cmdMessageInfo != null && cmdMessageInfo.getInviteNumber() != null && cmdMessageInfo.getInviteNumber().size() > 0) {
                                    String invitStr = Strname(cmdMessageInfo.getInviteNumber());
                                    txt = "群主" + cmdMessageInfo.getOwnername() + "邀请了 " + invitStr + "加入群聊";
//                                    ContactService.getInstance().initOrUpdateGroupInfo(message.getTo(),cmdMessageInfo.getGroupName(),cmdMessageInfo.getHeadUrl(),cmdMessageInfo.getOwner(),cmdMessageInfo.getOwnername());
                                }
                                break;
                            case "addUser":
                                if (cmdMessageInfo != null && cmdMessageInfo.getInviteNumber() != null && cmdMessageInfo.getInviteNumber().size() > 0) {
                                    String invitStr = Strname(cmdMessageInfo.getInviteNumber());
                                    txt = invitStr + "已加入群聊";
                                }
                                break;
                            case "deleteUser":
                                if (cmdMessageInfo != null && cmdMessageInfo.getDeleteNumber() != null && cmdMessageInfo.getDeleteNumber().size() > 0) {
                                    String invitStr = cmdMessageInfo.getDeleteNumber().get(0).getUsername();
                                    txt = invitStr + "已被管理员移除本讨论群";
                                }
                                break;
                            case "exitGroupOwen":
                                if (cmdMessageInfo != null) {
                                    String ownername = cmdMessageInfo.getOwnername();
                                    int ownerId = Integer.valueOf(cmdMessageInfo.getOwner());
                                    int userids = Integer.valueOf(YMUserService.getCurUserId());
                                    txt = (ownerId == userids) ? "您成为本群管理员" : ownername + "已成为本群管理员";
                                }
                                break;
                        }

                        ChatSendMessageHelper.saveMessage(message.getTo(), txt, cmdMessageInfo.getGroupName(), cmdMessageInfo.getHeadUrl());
                        ContactService.getInstance().getGroupDetail(message.getTo(), new CommonResponse<GroupModel>(MainActivity.this) {
                            @Override
                            public void onNext(GroupModel groupModel) {

                            }
                        });
                        if (YMApplication.deleteGroupId.equals(message.getTo())) {
                            EMChatManager.getInstance().deleteConversation(YMApplication.deleteGroupId, true, true);
                            YMApplication.deleteGroupId = "";
                        }
                        if (messageInfoFragment != null) {
                            messageInfoFragment.refresh();
                        }

                    } else {
                        String str = message.getStringAttribute("command");
                        DLog.i(TAG, "透传扩展字段" + str);

                        if (!TextUtils.isEmpty(str) && "login".equals(str)) {
                            YMUserService.autoLogin(MainActivity.this);
                        }
                    }


                    DLog.i(TAG, String.format("透传消息：action:%s,message:%s", action, message.toString()));
                } catch (HyphenateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };


    /**
     * 检查当前用户是否被删除
     */
    public boolean getCurrentAccountRemoved() {
        return isCurrentAccountRemoved;
    }

    /**
     * 刷新未读消息数
     */
    public void updateUnreadLabel() {
        int count;
        int newFriendNum = 0;
        if (!"".equals(YMApplication.addFriendNum)) {
            newFriendNum = Integer.parseInt(YMApplication.addFriendNum);
        }

        count = getUnreadMsgCountTotal() + newFriendNum;
        // }
        if (count > 0) {
            if (count > 99) {
                unreadLabel.setText(String.valueOf(99 + "+"));
            } else {
                unreadLabel.setText(String.valueOf(count));
            }

            unreadLabel.setVisibility(View.VISIBLE);
        } else {
            unreadLabel.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * 保存提示新消息
     *
     * @param msg
     */
    private void notifyNewIviteMessage(InviteMessage msg) {
        if (msg.getFrom().contains("sid")) {
            return;
        }
        inviteMessgeDao.saveMessage(msg);
        // saveInviteMsg(msg);
        // 提示有新消息
        EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();


        // 刷新bottom bar消息未读数
        // updateUnreadAddressLable();
        // 刷新好友页面ui
        if (messageInfoFragment != null) {
            messageInfoFragment.refresh();
        }
    }

    /**
     * 获取未读消息数
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal;
        unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
        return unreadMsgCountTotal;
    }

    @Override
    protected void onResume() {
        super.onResume();

        EMChatManager.getInstance().activityResumed();
        Log.d("main", "onResume()isPush==" + isPush + "====Curent" + currentTabIndex + "===INDEX" + index);
        if (!YMUserService.isGuest()) {
            updateUnreadLabel();
            if (YMApplication.getLoginInfo() != null) {
                uid = YMApplication.getLoginInfo().getData().getPid();
            }
            getNewMediaNum();
            getDoctorNewsData();
            getCardListData();
        }
        DLog.i(TAG, "onResume未读条数" + YMApplication.msgnum);
        DLog.i(TAG, "onResume未读条数踢的状态" + isConflict);
        if (!isConflict || !isCurrentAccountRemoved) {
            YMApplication.hxSDKHelper.onInit(YMApplication.getAppContext());
            updateCardUnmber();
            // updateUnreadAddressLable();
            EMChatManager.getInstance().activityResumed();
            YMApplication.hxSDKHelper.getCancleNotification();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isConflict) {
            YMUserService.ymLogout();
            XywyIMService.getInstance().removeLogOutObserver(this);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        hideCommonBaseTitle();
        XywyIMService.getInstance().addLogOutObserver(this);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (savedInstanceState != null && (savedInstanceState.getBoolean(EaseConstant.ACCOUNT_REMOVED, false) || savedInstanceState.getBoolean("isConflict", false))) {
            // 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            YMUserService.ymLogout();
            return;
        }
    }

    public void setGroupList() {
        String did = YMApplication.getLoginInfo().getData().getPid();
        String bind = did;
        Long st = System.currentTimeMillis();
        String sign = MD5Util.MD5(st + bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("a", "chat");
        params.put("m", "groupInit");
        params.put("timestamp", st + "");
        params.put("bind", did);
        params.put("did", did);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.get(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        Map<String, String> map = ResolveJson.R_Action(t.toString());
                        if (map.get("code").equals("0")) {
                            sp.edit()
                                    .putBoolean(
                                            YMApplication.getLoginInfo().getData()
                                                    .getPid(), false).commit();
                        }
                        super.onSuccess(t);
                    }

                });
    }

    /**
     * 获取当前应用名称
     *
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        String appName = null;
        PackageManager packageManager = context.getPackageManager();
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            //CharSequence这两者效果是一样的.
            appName = packageManager.getApplicationLabel(applicationInfo).toString();
            appName = (String) packageManager.getApplicationLabel(applicationInfo);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("GsonUtils", "Exception=" + e.toString());
            return null;
        }
        return appName;
    }

    @Override
    protected void initView() {

        //stone 环信登录
        if (!YMUserService.isGuest()) {
            if (!EMClient.getInstance().isLoggedInBefore()) {
                if (TextUtils.isEmpty(YMApplication.getInstance().getUserName()) || TextUtils.isEmpty(YMApplication.getInstance().getPassword())) {
                    return;
                }
                EMChatManager.getInstance().login(YMApplication.getInstance().getUserName(), YMApplication.getInstance().getPassword(), new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        LogUtils.d("环信登录成功-->MainActivity");
                        //stone 在ui线程去加载会话
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                EMChatManager.getInstance().loadAllConversations();
                            }
                        });
                    }

                    @Override
                    public void onError(int i, String s) {
                        LogUtils.d("环信登录失败-->MainActivity" + s);
                    }


                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        }


        // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
        JPushInterface.init(getApplicationContext());

        LogUtils.i("packageName=" + getAppName(this));
        LogUtils.i("packageName=" + this.getPackageName());

        sp = getSharedPreferences("save_user", MODE_PRIVATE);
        isFistin = sp.getBoolean(YMApplication.getLoginInfo().getData().getPid(), true);
        if (isFistin) {
            setGroupList();
        }
        remindSP = getSharedPreferences("login", Context.MODE_PRIVATE);
        type = getIntent().getStringExtra("entertype");
        if (type != null && type.equals("1")) {

            Intent intent = new Intent(MainActivity.this, DoctorOneDayActivity.class);
            startActivity(intent);
        }
        isPush = getIntent().getBooleanExtra("jpush", false);
        typeId = getIntent().getIntExtra("type", 0);
        isBirthday = getIntent().getBooleanExtra("birthflag", false);
        // 判断是否二次打包如果是退出程序
        // 开发测试的时候注销掉
        //		 Sign sign = new Sign(MainActivity.this);
        //		 sign.getSingInfo();
        // 禁止默认的页面统计
        MobclickAgent.openActivityDurationTrack(false);
        mCache = ACache.get(this);
        initview();
        init();


        setBroadcastReceiver();

        int index = getIntent().getIntExtra("tag", -1);
        if (APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
            if (index != -1 && index == 1) {
                ShowFragment(2);
            }
        } else {
            if (index != -1 && index == 1) {
                ShowFragment(4);
            }
        }


        if (isBirthday) {
            HappyBirthFragment fragment = HappyBirthFragment.newInstance();
            fragment.show(getSupportFragmentManager(), "1");
        }

        if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
            //调用用药助手的版本更新
            checkAppVersion();
        } else {
            //版本更新
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
//                    iniUpdata();
                    // 版本更新
                    VersionUtils versionUtils = VersionUtils.getInstance(MainActivity.this);
                    versionUtils.setShowToast(false);
                    versionUtils.checkVersionUpdate();
                }
            }, 500);
        }

        DLog.i("main", "isPush==" + isPush + "====Curent" + currentTabIndex + "===INDEX" + index + "当前时间戳" + System.currentTimeMillis() / 1000L);


        //app没在运行状态时候,推送过来的
        handlePush();

    }

    private void checkAppVersion() {
        String appVersionName = AppUtils.getVersionName(this);
        UpdateAppRequest.getInstance().checkVersion(appVersionName).subscribe(new BaseRetrofitResponse<BaseData<AppVersion>>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.e("请求更新失败：" + e.toString());
            }

            @Override
            public void onNext(BaseData<AppVersion> entry) {
                super.onNext(entry);
                if (entry != null && entry.getData() != null) {
                    dealwithEntry(entry.getData());
                }
            }
        });

    }

    private void dealwithEntry(final AppVersion entry) {
        String IS_UPGRADE = "1";//是否升级 1.是 0.否
        String APP_VERSION_MSG = "系统被检测到当前有最新的版本，是否更新?";
        com.xywy.util.LogUtils.i("url=" + entry.getUrl());
        com.xywy.util.LogUtils.i("md5=" + entry.getApp_secret_key());
        String is_upgrade = entry.getIs_upgrade();
        mAppSecretKey = entry.getApp_secret_key();
        if (!TextUtils.isEmpty(is_upgrade) && is_upgrade.equals(IS_UPGRADE)) {
            new MsDialogBuilder()
                    .setContent(APP_VERSION_MSG)
                    .create(this, new PositiveDialogListener() {
                        @Override
                        public void onPositive() {
                            shortToast("安装新版本");
                            if (!TextUtils.isEmpty(entry.getUrl())) {
                                downLoadNewInstallPackage(entry.getUrl());
                            }
                        }
                    }).show();
        }

    }

    private void downLoadNewInstallPackage(String downloadFileUrl) {
        XywyDataRequestApi.getInstance().downloadFile(downloadFileUrl, FilePathUtil.getSdcardPath(), new OkDownloadListener() {
            @Override
            public void onProgress(long totalBytes, long currentBytes) {
                if (0 != totalBytes) {
                    shortToast("下载进度：" + currentBytes * 100 / totalBytes + "%");
//                    LogUtils.i("下载进度："+currentBytes*100/totalBytes+"%");
                }
            }

            @Override
            public void onSuccess(final String absoluteFilePath) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        shortToast("下载成功：" + absoluteFilePath);
                        String md5 = getFileMD5(new File(absoluteFilePath));
                        com.xywy.util.LogUtils.i("安装包的md5====" + md5);
                        if (null != mAppSecretKey && mAppSecretKey.equals(md5)) {
                            installApk(absoluteFilePath);
                        }
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                shortToast("下载异常：" + e.getMessage());
            }
        });
    }

    /**
     * 安装apk
     *
     * @param path
     */
    private void installApk(String path) {
        File apkfile = new File(path);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        startActivity(i);
    }

    @Override
    protected void initData() {
//        if (!YMUserService.isGuest()) {
//            /**
//             * 获取在线咨询医生信息
//             */
//            ServiceProvider.getDoctorInfo(YMUserService.getCurUserId(), new CommonResponse<DoctorInfoEntity>() {
//                @Override
//                public void onNext(DoctorInfoEntity doctorInfoEntity) {
//                    if (null != doctorInfoEntity) {
////                        DoctorInfoBean doctorInfoBean = doctorInfoEntity.getData();
////                        YMApplication.getInstance().setDoctorInfo(doctorInfoBean);
////                        {
////                            if (null != doctorInfoBean.getWork()) {
////                                if (1 == doctorInfoBean.getWork().getImwd()) {
////                                    YMApplication.getInstance().isOnLineConsultUser = 1;
////                                    YMApplication.getInstance().initWebSocket();
////
////                                } else {
////                                    //未开通 不添加item
////                                    YMApplication.getInstance().isOnLineConsultUser = 0;
////                                    YMApplication.getInstance().disconnectWebSocket();
////                                }
////                            }
////                        }
//
//                        DoctorInfoBean doctorInfoBean = doctorInfoEntity.getData();
//                        YMApplication.getInstance().setDoctorInfo(doctorInfoBean);
//                        if(null != doctorInfoBean){
//                            if (null != doctorInfoBean.getWork()) {
//                                if (1 == doctorInfoBean.getWork().getImwd()) {
//                                    YMApplication.getInstance().isOnLineConsultUser = 1;
//                                    YMApplication.getInstance().initWebSocket();
//
//                                } else {
//                                    //未开通 不添加item
//                                    YMApplication.getInstance().isOnLineConsultUser = 0;
//                                    YMApplication.getInstance().disconnectWebSocket();
//                                }
//                            }
//                        }
//                    }
//                }
//            });
//        }

    }

    /**
     * 监听医圈跳转
     */
    public class GoToCircleReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra("msg");
            DLog.i(TAG, "===msg" + msg + "==currentTabIndex" + currentTabIndex);
            if (currentTabIndex != 2) {
                ShowFragment(2);
            }
        }
    }

//stone 去掉这些 现在的推送逻辑改变了
//    public class PushMsgReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
//                LogUtils.i("用药助手收到广播");
//            } else {
//                // 接收到广播，改变状态操作
//                // Toast.makeText(context, "接收到状态改变广播", 0).show();
//                boolean isPush = intent.getBooleanExtra("jpush", false);
//                int typeId = intent.getIntExtra("type", 0);
//                DLog.i("main", "isPush======" + isPush + "type======" + typeId);
//                if (isPush) {
//                    // 进入问题广场
//
//                    if (newHomeFragment != null) {
//                        DLog.i("main", "fragment2!=nullisPush======" + isPush + "type======" + typeId);
//                        if (typeId == 3) {
//                            CommonUtils.getBackNum(MainActivity.this);
//                            //                        fragment2.getBackNum();
//                        } else {
//                            CommonUtils.gotoQuestions(MainActivity.this, isPush, typeId);
//                            //                        fragment2.goToQue(isPush, typeId);
//                        }
//                    }
//
//                }
//            }
//
//        }
//
//    }

    public void updateCardUnmber() {
        if (!YMUserService.isGuest()) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    DLog.i(TAG, "点击。。。");
                    List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
                    DLog.i(TAG, "点击。。。" + msgs.size());
                    YMApplication.addFriendNum = msgs.size() + "";

                }
            });
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //new stone 环信登出
        YMUserService.huanxinLogout();

        // 注销广播接收者
        try {
            //stone 去掉这些 现在的推送逻辑改变了
//            if (pushMsgReceiver != null) {
//                unregisterReceiver(pushMsgReceiver);
//            }
            if (goToCircleReceiver != null) {
                unregisterReceiver(goToCircleReceiver);
            }
            EMChatManager.getInstance().removeMessageListener(messageListener);
            EMContactManager.getInstance().removeContactListener(contactListener);
            // 注册一个监听连接状态的listener
            EMChatManager.getInstance().removeConnectionListener(connectionListener);
//            unregisterReceiver(msgReceiver);
//            unregisterReceiver(cmdMessageReceiver);
            // unregisterReceiver(this);
            // unregisterReceiver(ackMessageReceiver);
            if (needUpdateReceive != null) {
                unregisterReceiver(needUpdateReceive);
                needUpdateReceive = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 双击退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
                YMApplication.getInstance().appExit();
            }
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            SharedPreferences saveChannel = getSharedPreferences("saveChannel", Context.MODE_PRIVATE);
            if (!YMUserService.isGuest()) {
                saveChannel.edit().putBoolean("changed", true).apply();
            } else {
                saveChannel.edit().putBoolean("changedGuest", true).apply();
            }
            return true;
        }
        return keyCode == KeyEvent.KEYCODE_MENU || super.onKeyDown(keyCode, event);
    }

    /**
     * 请求网络获取数据
     */
    private void msgXiaoZhu() {
        AjaxParams params = new AjaxParams();
        String userid = YMApplication.getLoginInfo().getData().getPid();
        params.put("userid", userid);
        params.put("sign", CommonUtils.computeSign(userid));
        FinalHttp fh = new FinalHttp();

        fh.post(CommonUrl.MSG_ZHU_SHOU, params, new AjaxCallBack() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                super.onFailure(t, errorNo, strMsg);
            }

            @Override
            public void onLoading(long count, long current) {
                // TODO Auto-generated method stub
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                DLog.i(TAG, "消息小助手" + t.toString());
                YMApplication.msgLists = new ArrayList<Message>();
                try {
                    JSONObject jsonObject = new JSONObject(t.toString());
                    JSONObject jsonChild = jsonObject.getJSONObject("data");
                    String count = jsonChild.getString("count"); //count  有多少种消息
                    String msgTotals = jsonChild.getString("msgTotal");//msgTotal  消息总数
                    String msg_detail = jsonChild.getString("detail");//detail  最新一条
                    String msg_createtime = jsonChild.getString("createtime");//createtime  最新一条时间
                    YMApplication.msgnum = count;
                    YMApplication.msgdetail = msg_detail;
                    YMApplication.msgcreatetime = msg_createtime;
                    YMApplication.msgtotal = msgTotals;
//                    getNoticeCount();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    DLog.i(TAG, "Jpush" + logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    DLog.i(TAG, "Jpush" + logs);
                    if (NetworkUtil.isNetWorkConnected()) {
                        // mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS,
                        // tags), 1000 * 60);
                    } else {
                        DLog.i(TAG, "Jpush" + "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    DLog.i(TAG, "Jpush" + logs);
            }

            // ExampleUtil.showToast(logs, getApplicationContext());
        }

    };


    public String Strname(List<CMDMessageInfo.NumberEntity> list) {
        String str_userid = YMUserService.getCurUserId();
        String str = "";
        boolean isUser = false; //邀请的人中是否又自己
        if (TextUtils.isEmpty(str_userid)) {
            return str;
        }
        int userid = Integer.valueOf(str_userid);
        int index = 0;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUserid() == userid) {
                list.remove(i);
                isUser = true;
                break;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i).getUsername() + "、");
        }

        if (sb.length() > 0) {
            str = sb.substring(0, sb.length() - 1);
        }

        return isUser ? ("您、" + str) : str;

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isConflict", isConflict);
        outState.putBoolean(EaseConstant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkAccountState();
    }


    /**
     * 获取医圈消息数
     */
    public void getDoctorNewsData() {
        FinalHttp fh = new FinalHttp();

        String oldid = remindSP.getString("oldid" + uid, "0");
        String noldid = remindSP.getString("noldid" + uid, "0");
        String sign = CommonUtils.computeSign(uid + oldid + noldid);

        AjaxParams params = new AjaxParams();
        params.put("a", "dynamic");
        params.put("m", "dynamic_new");
        params.put("userid", uid);
        params.put("oldid", oldid);
        params.put("noldid", noldid);
        params.put("bind", uid + oldid + noldid);
        params.put("sign", sign);
        DLog.i(TAG, "获取医圈消息数url=" + CommonUrl.doctor_circo_url + "?" + params.toString());
        fh.post(CommonUrl.doctor_circo_url, params, new AjaxCallBack() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                //				DLog.i(TAG, "医圈消息数json" + t);
                //                sunread":0,"snew":0,"nunread":0,"nnew":0,

                if (TextUtils.isEmpty(t)) {
                    LogUtils.e("服务端返回数据为空");
                    return;
                }
                CircleMsgs circleMsgs = GsonUtils.toObj(t, CircleMsgs.class);
                if (null != circleMsgs) {
                    YmRxBus.notifyMessageCountChanged(circleMsgs.message);
                }
            }
        });

    }

    private void updateMessage(Messages messages) {
        int sunread = Integer.parseInt(messages.sunread);
        int nunread = Integer.parseInt(messages.nunread);

        int snew = Integer.parseInt(messages.snew);
        int nnew = Integer.parseInt(messages.nnew);

        int showNum = sunread + nunread;

        int remindNum = snew + nnew;

        DLog.i(TAG, "医圈通知条数showNum=" + showNum + "remindNum=" + remindNum);

        if (showNum > 0) {
            circle_remind_iv.setVisibility(View.GONE);
            unread_circle_num.setVisibility(View.VISIBLE);
            if (showNum > 99) {
                unread_circle_num.setText("99+");
            } else {
                unread_circle_num.setText(showNum + "");
            }

        } else {
            unread_circle_num.setVisibility(View.GONE);
            if (remindNum > 0) {
                circle_remind_iv.setVisibility(View.VISIBLE);
            } else {
                circle_remind_iv.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 获取媒体号，save
     */
    public void getNewMediaNum() {

        String sign = CommonUtils.computeSign(uid);
        AjaxParams params = new AjaxParams();

        params.put("bind", uid);
        params.put("userid", uid);
        params.put("a", "media");
        params.put("m", "mediaArticleList");
        params.put("sign", sign);
        params.put("page", 1 + "");

        FinalHttp fh = new FinalHttp();
        DLog.i(TAG, "URL" + CommonUrl.doctor_circo_url + "?" + params.toString());
        fh.post(CommonUrl.doctor_circo_url, params, new AjaxCallBack() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    DLog.i("媒体号返回数据", s);
                    Gson gson = new Gson();
                    MediaList mediaData = gson.fromJson(s, MediaList.class);
                    if (mediaData.getData() != null && mediaData.getData().size() > 0) {
                        String mediaId = mediaData.getData().get(0).getId();
                        if (sp != null) {
                            sp.edit().putString("mediaId", mediaId == null ? "" : mediaId).apply();
                        }
                    }
                    YMApplication.getInstance().setMediaList(mediaData);
                    getNoticeCount();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);

            }
        });
    }

    /**
     * 名片夹
     */
    public void getCardListData() {

        String bind = YMApplication.getLoginInfo().getData().getHuanxin_username();
        Long st = System.currentTimeMillis();

        String sign = CommonUtils.computeSign(st + bind);
        AjaxParams params = new AjaxParams();

        params.put("timestamp", st + "");
        params.put("bind", bind);
        params.put("a", "chat");
        params.put("m", "getRelation");
        params.put("username", bind);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.get(CommonUrl.Patient_Manager_Url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                try {
                    pListInfo = new AddressBook();
                    pListInfo = ResolveJson.R_CardHold(t.toString());
                    mCache.put("card" + uid, pListInfo);
                    super.onSuccess(t);
                    Map<String, EaseUser> userlist = new HashMap<String, EaseUser>();
                    for (int i = 0; i < pListInfo.getData().size(); i++) {
                        EaseUser user = new EaseUser(pListInfo.getData().get(i).getHxusername());
                        // setEaseUserHearder(username, user);
                        userlist.put(pListInfo.getData().get(i).getHxusername(), user);
                    }
                    YMApplication.getInstance().setContactList(userlist);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                try {
                    AddressBook cachePlistInfo = (AddressBook) mCache.getAsObject("card" + uid);
                    if (cachePlistInfo != null) {
                        pListInfo = new AddressBook();
                        pListInfo = cachePlistInfo;
                        Map<String, EaseUser> userlist = new HashMap<String, EaseUser>();
                        for (int i = 0; i < pListInfo.getData().size(); i++) {
                            EaseUser user = new EaseUser(pListInfo.getData().get(i).getHxusername());
                            // setUserHearder(username, user);
                            userlist.put(pListInfo.getData().get(i).getHxusername(), user);
                        }
                        YMApplication.getInstance().setContactList(userlist);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                super.onFailure(t, errorNo, strMsg);
            }
        });

    }

    public BroadcastReceiver needUpdateReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (messageInfoFragment != null) {
                messageInfoFragment.refresh();
            }

        }
    };

    /**
     * 当应用在前台时，如果当前消息不是属于当前会话，在状态栏提示一下 如果不需要，注释掉即可
     *
     * @param message
     */
    protected void notifyNewMessage(EMMessage message) {
        // 如果是设置了不提醒只显示数目的群组(这个是app里保存这个数据的，demo里不做判断)
        // 以及设置了setShowNotificationInbackgroup:false(设为false后，后台时sdk也发送广播)
        if (!EasyUtils.isAppRunningForeground(this)) {
            return;
        }

        EaseUI.getInstance().getNotifier().onNewMsg(message);

    }


    //stone hide show 去重影 内存重启时导致bug 后google修复啦
    private void clearBug() {
        messageInfoFragment = (MessageInfoFragment) getSupportFragmentManager().findFragmentByTag(
                MessageInfoFragment.class.getName());
        docCircleMainFragment = (DocCircleMainFragment) getSupportFragmentManager().findFragmentByTag(
                DocCircleMainFragment.class.getName());
        discoverFragment = (DiscoverFragment) getSupportFragmentManager().findFragmentByTag(
                DiscoverFragment.class.getName());
        newHomeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(
                HomeFragment.class.getName());
        userCenterFragment = (UserCenterFragment) getSupportFragmentManager().findFragmentByTag(
                UserCenterFragment.class.getName());
    }


    /**
     * 处理推送 stone
     */
    private void handlePush() {
        if (YMApplication.sPushExtraBean != null) {
            YMApplication.applicationHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!MainActivity.this.isFinishing()) {

                        if (YMApplication.sPushExtraBean.getType() == 1) {
                            //即时问答详情
                            ConsultChatActivity.startActivity(true, false, MainActivity.this,
                                    String.valueOf(YMApplication.sPushExtraBean.getQid()), String.valueOf(YMApplication.sPushExtraBean.getUid()),
                                    null, (!TextUtils.isEmpty(YMApplication.sPushExtraBean.getQtype()) && YMApplication.sPushExtraBean.getQtype().equals("3")) ? true : false
                            );
                        } else if (YMApplication.sPushExtraBean.getType() == 2) {
                            //问题广场
                            //直接获取问题详情数据,跳转到问题详情页面
                            forwordToQueDetailActivity(MainActivity.this, MyReceiver.JPUSH, YMApplication.sPushExtraBean.getQid() + "", YMApplication.sPushExtraBean.getWaitType(), YMApplication.sPushExtraBean.getRid(), YMApplication.sPushExtraBean.getQtype());
                        }
                        //处理完,置null
                        YMApplication.sPushExtraBean = null;
                    }
                }
            }, 500);
        }
    }

    private void forwordToQueDetailActivity(Context context, String isFrom, String qid, String type, String rid, String q_type) {
        Intent intent = new Intent(context, QueDetailActivity.class);
        intent.putExtra("tag", "other");
        intent.putExtra(MyReceiver.INTENT_KEY_ISFROM, isFrom);
        intent.putExtra("type", type);
        intent.putExtra(MyReceiver.INTENT_KEY_Q_TYPE, q_type);
        intent.putExtra(MyReceiver.INTENT_KEY_RID, rid);
        intent.putExtra("id", qid);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        context.startActivity(intent);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        findViewById(R.id.mainLayout).getLocationOnScreen(location);
        YMApplication.setStatusBarHeight(location[1] > 0 ? location[1] : 0);
        L.d("状态栏=" + location[1]);
    }
}
