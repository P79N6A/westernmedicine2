/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xywy.askforexpert.module.message.msgchat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMLocationMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.chat.EMNormalFileMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.PathUtil;
import com.hyphenate.util.VoiceRecorder;
import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ClickUtil;
import com.xywy.askforexpert.appcommon.utils.DensityUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.imageutils.ImageUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.AddNumPerInfo;
import com.xywy.askforexpert.model.CMDMessageInfo;
import com.xywy.askforexpert.model.im.group.GroupModel;
import com.xywy.askforexpert.module.discovery.medicine.common.MyConstant;
import com.xywy.askforexpert.module.discovery.medicine.common.MyRxBus;
import com.xywy.askforexpert.module.discovery.medicine.module.account.beans.UserInfoBean;
import com.xywy.askforexpert.module.discovery.medicine.module.account.data.UserRequest;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.MedicineCartCenter;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.PharmacyActivity;
import com.xywy.askforexpert.module.discovery.medicine.module.patient.PatientManager;
import com.xywy.askforexpert.module.discovery.medicine.module.patient.entity.Patient;
import com.xywy.askforexpert.module.message.adapter.MessageFragmentAdapter;
import com.xywy.askforexpert.module.message.adapter.VoicePlayClickListener;
import com.xywy.askforexpert.module.message.call.CallActivity;
import com.xywy.askforexpert.module.message.imgroup.ContactService;
import com.xywy.askforexpert.module.message.imgroup.constants.GroupBroadCastAction;
import com.xywy.askforexpert.module.message.msgchat.adapter.ExpressionAdapter;
import com.xywy.askforexpert.module.message.msgchat.adapter.ExpressionPagerAdapter;
import com.xywy.askforexpert.widget.ExpandGridView;
import com.xywy.askforexpert.widget.PasteEditText;
import com.xywy.easeWrapper.EMChatManager;
import com.xywy.easeWrapper.EMContactManager;
import com.xywy.easeWrapper.EMGroupManager;
import com.xywy.easeWrapper.controller.EaseUI;
import com.xywy.easeWrapper.utils.SmileUtils;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.xywy.util.SharedPreferencesHelper;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xywy.askforexpert.R.id.btn_more;

/**
 * 聊天页面
 */
public class ChatFragment extends BaseFragment implements OnClickListener {

    //stone 新添加的
    private final String RECIPE_SICK = "recipe_sick";
    private final String PATIENT_ID = "patientId";


    private static final int REQUEST_CODE_EMPTY_HISTORY = 2;
    public static final int REQUEST_CODE_CONTEXT_MENU = 3;
    private static final int REQUEST_CODE_MAP = 4;
    public static final int REQUEST_CODE_TEXT = 5;
    public static final int REQUEST_CODE_VOICE = 6;
    public static final int REQUEST_CODE_PICTURE = 7;
    public static final int REQUEST_CODE_LOCATION = 8;
    public static final int REQUEST_CODE_NET_DISK = 9;
    public static final int REQUEST_CODE_FILE = 10;
    public static final int REQUEST_CODE_COPY_AND_PASTE = 11;
    public static final int REQUEST_CODE_PICK_VIDEO = 12;
    public static final int REQUEST_CODE_DOWNLOAD_VIDEO = 13;
    public static final int REQUEST_CODE_VIDEO = 14;
    public static final int REQUEST_CODE_DOWNLOAD_VOICE = 15;
    public static final int REQUEST_CODE_SELECT_USER_CARD = 16;
    public static final int REQUEST_CODE_SEND_USER_CARD = 17;
    public static final int REQUEST_CODE_CAMERA = 18;
    public static final int REQUEST_CODE_LOCAL = 19;
    public static final int REQUEST_CODE_CLICK_DESTORY_IMG = 20;
    public static final int REQUEST_CODE_GROUP_DETAIL = 21;
    public static final int REQUEST_CODE_SELECT_VIDEO = 23;
    public static final int REQUEST_CODE_SELECT_FILE = 24;
    public static final int REQUEST_CODE_ADD_TO_BLACKLIST = 25;
    public static final int REQUEST_CODE_REPLY_BACK = 26;
    public static final int REQUEST_ADDNUM_BACK = 27;

    public static final int RESULT_CODE_COPY = 1;
    public static final int RESULT_CODE_DELETE = 2;
    public static final int RESULT_CODE_FORWARD = 3;
    public static final int RESULT_CODE_OPEN = 4;
    public static final int RESULT_CODE_DWONLOAD = 5;
    public static final int RESULT_CODE_TO_CLOUD = 6;
    public static final int RESULT_CODE_EXIT_GROUP = 7;

    public static final int CHATTYPE_SINGLE = 1;
    public static final int CHATTYPE_GROUP = 2;

    public static final String COPY_IMAGE = "EASEMOBIMG";
    private static final String TAG = "ChatFragment";
    //stone 底部发送相关的view的容器
    private View bar_bottom;
    private View recordingContainer;
    private ImageView micImage;
    private TextView recordingHint;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private PasteEditText mEditTextContent;
    private View buttonSetModeKeyboard;
    private View buttonSetModeVoice;
    private View buttonSend;
    private View buttonPressToSpeak;
    private ViewPager menu_vpage;
    private LinearLayout emojiIconContainer;
    private LinearLayout btnContainer;
    private ImageView locationImgview;
    private View more;
    private int position;
    private ClipboardManager clipboard;
    private ViewPager expressionViewpager;
    private InputMethodManager manager;
    private List<String> reslist;
    private Drawable[] micImages;
    private int chatType;
    private EMConversation conversation;
    public static ChatFragment activityInstance = null;
    // 给谁发送消息
    private String toChatUsername;
    private VoiceRecorder voiceRecorder;
    private MessageFragmentAdapter adapter;
    private File cameraFile;
    static int resendPos;
    //
    private String toChatUserRealname;
    private String toHeadImge;
    private GroupListener groupListener;
    private AddNumPerInfo addperinfo;
    private ImageView iv_emoticons_normal;
    private ImageView iv_emoticons_checked;
    private RelativeLayout edittext_layout;
    //    private ProgressBar loadmorePB;
    private boolean isloading;
    private final int pagesize = 20;
    private boolean haveMoreData = true;
    private Button btnMore;
    public String playMsgId;
    public ImageView btn_picture;
    public ImageView btn_take_picture, btn_reply, btn_men_time, btn_add_num, btn_complain, btn_video, btn_prescribe1, btn_prescribe2;
    public String type;
    private List<View> menu_views;
    /**
     * 来源 病例研讨班 还是普通页
     */
    private String from = "";


    public LinearLayout lin_complain, lin_men_time, lin_reply, lin_picture, lin_take_picture, lin_add_num, lin_video, lin_prescribe1, lin_prescribe2;

    Handler micImageHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            // 切换msg切换图片
            micImage.setImageDrawable(micImages[msg.what]);
        }
    };
    private EMGroup group;
    private ImageView btnDial;
    private LinearLayout linPhoneDial;
    private boolean isGroup;
    /**
     * 判断是否是签约医生
     */
    private boolean isHealthyUser;
    private String dialId;

    //stone 控制menuView
    //0拍照 1相册 2电视 3快捷回复 4门诊时间 5举报
    private Map<String, Boolean> mMap1;
    private boolean mIsFirstIn = true;//第一次进来

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return YMApplication.sIsYSZS ? inflater.inflate(R.layout.activity_chat_fragmenr_yszs, container, false) : inflater.inflate(R.layout.activity_chat_fragmenr, container, false);
    }

    // @Override
    // protected void onCreate(Bundle savedInstanceState)
    // {
    // super.onCreate(savedInstanceState);et_sendmessage
    // setContentView(R.layout.activity_chat);
    // initView();
    // setUpView();
    // }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle budle = getArguments();

        toChatUsername = budle.getString("userId");
        toChatUserRealname = budle.getString("username");
        toHeadImge = budle.getString("toHeadImge");
        isGroup = budle.getBoolean("isGroup", false);
        type = budle.getString("type");
        chatType = budle.getInt("chatType", CHATTYPE_SINGLE);
        isHealthyUser = budle.getBoolean(ChatMainActivity.IS_HEALTHY_USER_KEY);
        dialId = budle.getString(ChatMainActivity.DIAL_ID);
        initView();
        setUpView();
        adapter.refresh();
    }

    /**
     * 申请开通以后 获取基础信息
     */
    public void getDataInfo() {
        String uid = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(uid + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("userid", uid);
        params.put("sign", sign);
        params.put("command", "yuyuedoc");

        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.MyClinic_State_Url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                DLog.i(TAG, "预约专家返回数据" + t.toString());
                Gson gson = new Gson();
                addperinfo = gson.fromJson(t.toString(), AddNumPerInfo.class);
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    /**
     * initView
     */
    protected void initView() {
        bar_bottom = getActivity().findViewById(R.id.bar_bottom);

        menu_vpage = (ViewPager) getActivity().findViewById(R.id.menu_vpage);
        recordingContainer = getActivity().findViewById(R.id.recording_container);
        micImage = (ImageView) getActivity().findViewById(R.id.mic_image);
        recordingHint = (TextView) getActivity().findViewById(R.id.recording_hint);

        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.chat_swipe_layout);
        listView = (ListView) getActivity().findViewById(R.id.list);
        mEditTextContent = (PasteEditText) getActivity().findViewById(R.id.et_sendmessage);
        buttonSetModeKeyboard = getActivity().findViewById(R.id.btn_set_mode_keyboard);
        buttonSetModeKeyboard.setOnClickListener(new MySetModeKeyboard());
        edittext_layout = (RelativeLayout) getActivity().findViewById(R.id.edittext_layout);
        buttonSetModeVoice = getActivity().findViewById(R.id.btn_set_mode_voice);
        buttonSetModeVoice.setOnClickListener(new MySetModeVoice());
        buttonSend = getActivity().findViewById(R.id.btn_send);
        buttonSend.setOnClickListener(this);
        buttonPressToSpeak = getActivity().findViewById(R.id.btn_press_to_speak);
        expressionViewpager = (ViewPager) getActivity().findViewById(R.id.vPager);
        emojiIconContainer = (LinearLayout) getActivity().findViewById(R.id.ll_face_container);
        btnContainer = (LinearLayout) getActivity().findViewById(R.id.ll_btn_container);
        iv_emoticons_normal = (ImageView) getActivity().findViewById(R.id.iv_emoticons_normal);
        iv_emoticons_checked = (ImageView) getActivity().findViewById(R.id.iv_emoticons_checked);
//        loadmorePB = (ProgressBar) getActivity().findViewById(R.id.pb_load_more);
        btnMore = (Button) getActivity().findViewById(btn_more);
        btnMore.setOnClickListener(new MyMore());
        //stone
        if (!YMApplication.sIsYSZS) {
            iv_emoticons_normal.setVisibility(View.VISIBLE);
            iv_emoticons_checked.setVisibility(View.INVISIBLE);
        }
        more = getActivity().findViewById(R.id.more);
        //		edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);

        //         动画资源文件,用于录制语音时
        micImages = new Drawable[]{getResources().getDrawable(R.drawable.ease_record_animate_01),
                getResources().getDrawable(R.drawable.ease_record_animate_02),
                getResources().getDrawable(R.drawable.ease_record_animate_03),
                getResources().getDrawable(R.drawable.ease_record_animate_04),
                getResources().getDrawable(R.drawable.ease_record_animate_05),
                getResources().getDrawable(R.drawable.ease_record_animate_06),
                getResources().getDrawable(R.drawable.ease_record_animate_07),
                getResources().getDrawable(R.drawable.ease_record_animate_08),
                getResources().getDrawable(R.drawable.ease_record_animate_09),
                getResources().getDrawable(R.drawable.ease_record_animate_10),
                getResources().getDrawable(R.drawable.ease_record_animate_11),
                getResources().getDrawable(R.drawable.ease_record_animate_12),
                getResources().getDrawable(R.drawable.ease_record_animate_13),
                getResources().getDrawable(R.drawable.ease_record_animate_14),};

        // 表情list
        reslist = getExpressionRes(35);
        // 初始化表情viewpager
        List<View> views = new ArrayList<View>();
        View gv1 = getGridChildView(1);
        View gv2 = getGridChildView(2);
        views.add(gv1);
        views.add(gv2);
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        // 添加隐藏菜单选择栏
        View menu_view = inflater.inflate(R.layout.menu1, null);
        //stone 添加开处方
        lin_prescribe1 = (LinearLayout) menu_view.findViewById(R.id.lin_prescribe1);
        lin_prescribe2 = (LinearLayout) menu_view.findViewById(R.id.lin_prescribe2);
        btn_prescribe1 = (ImageView) menu_view.findViewById(R.id.btn_prescribe1);
        btn_prescribe1.setOnClickListener(this);
        btn_prescribe2 = (ImageView) menu_view.findViewById(R.id.btn_prescribe2);
        btn_prescribe2.setOnClickListener(this);
        // View menu_view1 = inflater.inflate(R.layout.menu1, null);
        // 将布局放入集合
        btn_picture = (ImageView) menu_view.findViewById(R.id.btn_picture);
        btn_picture.setOnClickListener(this);
        btn_take_picture = (ImageView) menu_view.findViewById(R.id.btn_take_picture);
        btn_take_picture.setOnClickListener(this);
        // 打电话
        btnDial = (ImageView) menu_view.findViewById(R.id.btn_dial);
        btnDial.setOnClickListener(this);
        btn_complain = (ImageView) menu_view.findViewById(R.id.btn_complain);
        btn_complain.setOnClickListener(this);
        btn_add_num = (ImageView) menu_view.findViewById(R.id.btn_add_num);
        btn_add_num.setOnClickListener(this);
        btn_men_time = (ImageView) menu_view.findViewById(R.id.btn_men_time);
        btn_men_time.setOnClickListener(this);
        btn_reply = (ImageView) menu_view.findViewById(R.id.btn_reply);
        btn_reply.setOnClickListener(this);
        btn_video = (ImageView) menu_view.findViewById(R.id.btn_video);
        btn_video.setOnClickListener(this);
        lin_take_picture = (LinearLayout) menu_view.findViewById(R.id.lin_take_picture);
        lin_video = (LinearLayout) menu_view.findViewById(R.id.lin_video);

        lin_picture = (LinearLayout) menu_view.findViewById(R.id.lin_picture);
        // 打电话
        linPhoneDial = (LinearLayout) menu_view.findViewById(R.id.lin_phone_dial);
        lin_reply = (LinearLayout) menu_view.findViewById(R.id.lin_reply);
        lin_men_time = (LinearLayout) menu_view.findViewById(R.id.lin_men_time);
        lin_complain = (LinearLayout) menu_view.findViewById(R.id.lin_complain);
        lin_add_num = (LinearLayout) menu_view.findViewById(R.id.lin_add_num);

        handleMenus();
        menu_views = new ArrayList<View>();
        menu_views.add(menu_view);
        // menu_views.add(menu_view1);
//        PagerAdapter adapter
        menu_vpage.setAdapter(new MenuPageAdapter());
        expressionViewpager.setAdapter(new ExpressionPagerAdapter(views));
        edittext_layout.requestFocus();
        voiceRecorder = new VoiceRecorder(micImageHandler);
        buttonPressToSpeak.setOnTouchListener(new PressToSpeakListen());
//        mEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener() {
//
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                //				if (hasFocus)
//                //				{
//                //					edittext_layout
//                //							.setBackgroundResource(R.drawable.input_bar_bg_active);
//                //				} else
//                //				{
//                //					edittext_layout
//                //							.setBackgroundResource(R.drawable.input_bar_bg_normal);
//                //				}
//
//            }
//        });

        MyListenner();
    }

    //处理menu stone  视频与举报没用到
    private void handleMenus() {
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, DensityUtils.dp2px(100));
        //stone 用药助手 添加开处方逻辑
        if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
            mMap1 = new HashMap<>();

            if (isHealthyUser) {
                lin_take_picture.setVisibility(View.VISIBLE);
                mMap1.put("0", true);
                lin_picture.setVisibility(View.VISIBLE);
                mMap1.put("1", true);
                linPhoneDial.setVisibility(View.VISIBLE);
                mMap1.put("2", true);
//                btnContainer.setLayoutParams(param);
            } else {
                linPhoneDial.setVisibility(View.GONE);
            }
//        if (isGroup) {
//            buttonSetModeVoice.setVisibility(View.VISIBLE);
//            lin_take_picture.setVisibility(View.VISIBLE);
//            lin_picture.setVisibility(View.VISIBLE);
//            btnContainer.setLayoutParams(param);
//        }
            if ("did".equals(type) || isGroup) {
                buttonSetModeVoice.setVisibility(View.VISIBLE);
                lin_video.setVisibility(View.GONE);


            }

            if ("did".equals(type) || "doc".equals(type) || isGroup) {
                lin_take_picture.setVisibility(View.VISIBLE);
                mMap1.put("0", true);
                lin_picture.setVisibility(View.VISIBLE);
                mMap1.put("1", true);
//                btnContainer.setLayoutParams(param);
            } else if ("uid".equals(type)) {
                lin_take_picture.setVisibility(View.VISIBLE);
                mMap1.put("0", true);
                lin_picture.setVisibility(View.VISIBLE);
                mMap1.put("1", true);
                lin_reply.setVisibility(View.VISIBLE);
                mMap1.put("4", true);
                if (YMApplication.getLoginInfo().getData().getXiaozhan().getYuyue().equals(Constants.FUWU_AUDIT_STATUS_1)) {
                    lin_add_num.setVisibility(View.VISIBLE);
                    lin_men_time.setVisibility(View.VISIBLE);
                    mMap1.put("5", true);
                    if (addperinfo == null) {
                        getDataInfo();
                    }

                } else {
//                    btnContainer.setLayoutParams(param);
                }

            } else if ("qid".equals(type)) {
                //			lin_reply.setVisibility(View.VISIBLE);
                //			lin_complain.setVisibility(View.VISIBLE);
                //			btnContainer.setLayoutParams(param);
                lin_take_picture.setVisibility(View.VISIBLE);
                mMap1.put("0", true);
                lin_picture.setVisibility(View.VISIBLE);
                mMap1.put("1", true);
//                btnContainer.setLayoutParams(param);
            }
            //第一行满四个没有
            if (mMap1.size() == 4) {
                lin_prescribe2.setVisibility(View.VISIBLE);
            } else {
                lin_prescribe1.setVisibility(View.VISIBLE);
                btnContainer.setLayoutParams(param);
            }

            // lin_complain,lin_men_time,lin_reply,lin_picture,lin_take_picture
//            menu_views = new ArrayList<View>();
//            menu_views.add(menu_view);
        } else {
            //医脉逻辑不变 stone
            if (isHealthyUser) {
                lin_take_picture.setVisibility(View.VISIBLE);
                lin_picture.setVisibility(View.VISIBLE);
                linPhoneDial.setVisibility(View.VISIBLE);
                btnContainer.setLayoutParams(param);
            } else {
                linPhoneDial.setVisibility(View.GONE);
            }
//        if (isGroup) {
//            buttonSetModeVoice.setVisibility(View.VISIBLE);
//            lin_take_picture.setVisibility(View.VISIBLE);
//            lin_picture.setVisibility(View.VISIBLE);
//            btnContainer.setLayoutParams(param);
//        }
            if ("did".equals(type) || isGroup) {
                buttonSetModeVoice.setVisibility(View.VISIBLE);
                lin_video.setVisibility(View.GONE);


            }

            if ("did".equals(type) || "doc".equals(type) || isGroup) {
                lin_take_picture.setVisibility(View.VISIBLE);
                lin_picture.setVisibility(View.VISIBLE);
                btnContainer.setLayoutParams(param);
            } else if ("uid".equals(type)) {
                lin_take_picture.setVisibility(View.VISIBLE);
                lin_picture.setVisibility(View.VISIBLE);
                lin_reply.setVisibility(View.VISIBLE);
                if (YMApplication.getLoginInfo().getData().getXiaozhan().getYuyue().equals(Constants.FUWU_AUDIT_STATUS_1)) {
                    lin_add_num.setVisibility(View.VISIBLE);
                    lin_men_time.setVisibility(View.VISIBLE);
                    if (addperinfo == null) {
                        getDataInfo();
                    }

                } else {
                    btnContainer.setLayoutParams(param);
                }

            } else if ("qid".equals(type)) {
                //			lin_reply.setVisibility(View.VISIBLE);
                //			lin_complain.setVisibility(View.VISIBLE);
                //			btnContainer.setLayoutParams(param);
                lin_take_picture.setVisibility(View.VISIBLE);
                lin_picture.setVisibility(View.VISIBLE);
                btnContainer.setLayoutParams(param);
            }
            // lin_complain,lin_men_time,lin_reply,lin_picture,lin_take_picture
//            menu_views = new ArrayList<View>();
//            menu_views.add(menu_view);
        }
    }

    private void MyListenner() {
        mEditTextContent.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //				edittext_layout
                //						.setBackgroundResource(R.drawable.input_bar_bg_active);
                mEditTextContent.setCursorVisible(true);
                more.setVisibility(View.GONE);
                //stone
                if (!YMApplication.sIsYSZS) {
                    iv_emoticons_normal.setVisibility(View.VISIBLE);
                    iv_emoticons_checked.setVisibility(View.INVISIBLE);
                }
                emojiIconContainer.setVisibility(View.GONE);
                btnContainer.setVisibility(View.GONE);
            }
        });
        // 监听文字框
        mEditTextContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    mEditTextContent.setCursorVisible(true);
                    btnMore.setVisibility(View.GONE);
                    buttonSend.setVisibility(View.VISIBLE);
                } else {
                    btnMore.setVisibility(View.VISIBLE);
                    buttonSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public class MenuPageAdapter extends PagerAdapter {
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return menu_views.size();
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(menu_views.get(position));
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(menu_views.get(position));
            return menu_views.get(position);
        }
    }

    private void setUpView() {
        activityInstance = this;
        //stone
        if (!YMApplication.sIsYSZS) {
            iv_emoticons_normal.setOnClickListener(this);
            iv_emoticons_checked.setOnClickListener(this);
        }
        // position = getIntent().getIntExtra("position", -1);
        clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        wakeLock = ((PowerManager) getActivity().getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
        // 判断单聊还是群聊


        conversation = EMChatManager.getInstance().getConversation(toChatUsername);
        // 把此会话的未读数置为0
        conversation.markAllMessagesAsRead();

        // 初始化db时，每个conversation加载数目是getChatOptions().getNumberOfMessagesLoaded
        // 这个数目如果比用户期望进入会话界面时显示的个数不一样，就多加载一些
        final List<EMMessage> msgs = conversation.getAllMessages();

        if (msgs != null && msgs.size() > 0) {
            int msgCount = msgs.size();
            LogUtils.d("conversation.getAllMessages()个数=" + msgCount);
            LogUtils.d("conversation.getAllMsgCount()个数=" + conversation.getAllMsgCount());
            if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
                String msgId = null;
                msgId = msgs.get(0).getMsgId();

                if (chatType == CHATTYPE_SINGLE) {
                    conversation.loadMoreMsgFromDB(msgId, pagesize);
                } else {
                    conversation.loadMoreMsgFromDB(msgId, pagesize);
                }
            }
        }


        adapter = new MessageFragmentAdapter(getActivity(), ChatFragment.this, toChatUsername, toHeadImge, toChatUserRealname, chatType, type);
        // 显示消息
        listView.setAdapter(adapter);
        //		adapter.refresh();
        //stone 去掉
//        listView.setOnScrollListener(new ListScrollListener());
        int count = listView.getCount();
        LogUtils.d("listView.getCount()个数=" + count);
        if (count > 0) {
            listView.setSelection(count - 1);
        }

        listView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                more.setVisibility(View.GONE);
                //stone
                if (!YMApplication.sIsYSZS) {
                    iv_emoticons_normal.setVisibility(View.VISIBLE);
                    iv_emoticons_checked.setVisibility(View.INVISIBLE);
                }
                emojiIconContainer.setVisibility(View.GONE);
                btnContainer.setVisibility(View.GONE);
                return false;
            }
        });

        EMChatManager.getInstance().addMessageListener(messageListener);
        // 注册接收消息广播
        //        receiver = new NewMessageBroadcastReceiver();
        //        IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
        //        // 设置广播的优先级别大于Mainacitivity,这样如果消息来的时候正好在chat页面，直接显示消息，而不是提示消息未读
        //        intentFilter.setPriority(5);
        //        getActivity().registerReceiver(receiver, intentFilter);
        //
        //        // 注册一个ack回执消息的BroadcastReceiver
        //        IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager.getInstance().getAckMessageBroadcastAction());
        //        ackMessageIntentFilter.setPriority(5);
        //        getActivity().registerReceiver(ackMessageReceiver, ackMessageIntentFilter);
        //
        //        // 注册一个消息送达的BroadcastReceiver
        //        IntentFilter deliveryAckMessageIntentFilter = new IntentFilter(EMChatManager.getInstance().getDeliveryAckMessageBroadcastAction());
        //        deliveryAckMessageIntentFilter.setPriority(5);
        //        getActivity().registerReceiver(deliveryAckMessageReceiver, deliveryAckMessageIntentFilter);
        //
        //        IntentFilter cmdMessageIntentFilter = new IntentFilter(EMChatManager.getInstance().getCmdMessageBroadcastAction());
        //        cmdMessageIntentFilter.setPriority(5);
        //        getActivity().registerReceiver(cmdMessageReceiver, cmdMessageIntentFilter);
        //        IntentFilter broadcastIn = new IntentFilter(GroupBroadCastAction.EXIST_GROUP_ACTION);
        //        getActivity().registerReceiver(broadcastReceiver, broadcastIn);

        IntentFilter needUpdateFilter = new IntentFilter("needUpdate");
        needUpdateFilter.setPriority(5);
        getActivity().registerReceiver(needUpdateReceive, needUpdateFilter);

        // 监听当前会话的群聊解散被T事件
        groupListener = new GroupListener();
        EMGroupManager.getInstance().addGroupChangeListener(groupListener);

        // show forward message if the message is not null
        String forward_msg_id = getActivity().getIntent().getStringExtra("forward_msg_id");
        if (forward_msg_id != null) {
            // 显示发送要转发的消息
            forwardMessage(forward_msg_id);
        }

        //TODO stone 是否是系统消息 系统消息只能看不能回 医生助手专属
        if (YMApplication.sIsYSZS) {
            checkIsSystem();
        }
        //stone 新添加swipeRefreshLayout加载更多
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        loadMoreLocalMessage();
                    }
                }, 600);
            }
        });

    }

    private void checkIsSystem() {
        if (toChatUsername.equals(MyConstant.HX_SYSTEM_ID)) {
            bar_bottom.setVisibility(View.GONE);
        }
    }

    /**
     * onActivityResult
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CODE_EXIT_GROUP) {
            getActivity().setResult(getActivity().RESULT_OK);
            getActivity().finish();
            return;
        }
        if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
            switch (resultCode) {
                case RESULT_CODE_COPY: // 复制消息
                    EMMessage copyMsg = adapter.getItem(data.getIntExtra("position", -1));
                    // clipboard.setText(SmileUtils.getSmiledText(ChatActivity.this,
                    // ((EMTextMessageBody) copyMsg.getBody()).getMessage()));
                    clipboard.setText(((EMTextMessageBody) copyMsg.getBody()).getMessage());
                    break;
                case RESULT_CODE_DELETE: // 删除消息
                    EMMessage deleteMsg = adapter.getItem(data.getIntExtra("position", -1));
                    conversation.removeMessage(deleteMsg.getMsgId());
                    adapter.refresh();
                    listView.setSelection(data.getIntExtra("position", adapter.getCount()) - 1);
                    break;

                case RESULT_CODE_FORWARD: // 转发消息
                    // EMMessage forwardMsg = (EMMessage)
                    // adapter.getItem(data.getIntExtra("position", 0));
                    // Intent intent = new Intent(this,
                    // ForwardMessageActivity.class);
                    // intent.putExtra("forward_msg_id", forwardMsg.getMsgId());
                    // startActivity(intent);

                    break;

                default:
                    break;
            }
        }

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 28) {
                Bundle bundle = data.getExtras();
                String str_reply = bundle.getString("isdelete");
                DLog.i(TAG, "删除好.." + str_reply);
            }

            if (requestCode == REQUEST_CODE_REPLY_BACK) {
                Bundle bundle = data.getExtras();
                String str_reply = bundle.getString("type");
                DLog.i(TAG, "快捷回复。。" + str_reply);
                mEditTextContent.setText(str_reply);
            }
            if (requestCode == REQUEST_ADDNUM_BACK) {
                Bundle bundle = data.getExtras();
                String str_reply = bundle.getString("type");
                DLog.i(TAG, "加号。。" + str_reply);
                mEditTextContent.setText(toChatUserRealname + ",您好，已经在" + str_reply);
            }
            // 清空消息
            if (requestCode == REQUEST_CODE_EMPTY_HISTORY) {
                // 清空会话
                EMChatManager.getInstance().clearConversation(toChatUsername);
                adapter.refresh();
            } else if (requestCode == REQUEST_CODE_CAMERA) { // 发送照片
                if (cameraFile != null && cameraFile.exists()) {
                    sendPicture(cameraFile.getAbsolutePath());
                }
            }
//            else if (requestCode == REQUEST_CODE_SELECT_VIDEO) { // 发送本地选择的视频
//
//                int duration = data.getIntExtra("dur", 0);
//                String videoPath = data.getStringExtra("path");
//                File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
//                Bitmap bitmap = null;
//                FileOutputStream fos = null;
//                try {
//                    if (!file.getParentFile().exists()) {
//                        file.getParentFile().mkdirs();
//                    }
//                    bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
//                    if (bitmap == null) {
//                        EMLog.d("chatactivity", "problem load video thumbnail bitmap,use default icon");
//                        //						bitmap = BitmapFactory.decodeResource(getResources(),
//                        //								R.drawable.app_panel_video_icon);
//                    }
//                    fos = new FileOutputStream(file);
//
//                    bitmap.compress(CompressFormat.JPEG, 100, fos);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    if (fos != null) {
//                        try {
//                            fos.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        fos = null;
//                    }
//                    if (bitmap != null) {
//                        bitmap.recycle();
//                        bitmap = null;
//                    }
//
//                }
//                sendVideo(videoPath, file.getAbsolutePath(), duration / 1000);
//
//            }
            else if (requestCode == REQUEST_CODE_LOCAL) { // 发送本地图片
                if (data != null) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        sendPicByUri(selectedImage);
                    }
                }
            }
//            else if (requestCode == REQUEST_CODE_SELECT_FILE) { // 发送选择的文件
//                if (data != null) {
//                    Uri uri = data.getData();
//                    if (uri != null) {
//                        sendFile(uri);
//                    }
//                }
//
//            }
//            else if (requestCode == REQUEST_CODE_MAP) { // 地图
//                double latitude = data.getDoubleExtra("latitude", 0);
//                double longitude = data.getDoubleExtra("longitude", 0);
//                String locationAddress = data.getStringExtra("address");
//                if (locationAddress != null && !locationAddress.equals("")) {
//                    // more(more);
//                    sendLocationMsg(latitude, longitude, "", locationAddress);
//                } else {
//                    // String st =
//                    // getResources().getString(R.string.unable_to_get_loaction);
//                    T.shortToast( "不能获取消息");
//                }
//                // 重发消息
//            }
            else if (requestCode == REQUEST_CODE_TEXT || requestCode == REQUEST_CODE_VOICE || requestCode == REQUEST_CODE_PICTURE || requestCode == REQUEST_CODE_LOCATION || requestCode == REQUEST_CODE_VIDEO || requestCode == REQUEST_CODE_FILE) {
                resendMessage();
            } else if (requestCode == REQUEST_CODE_COPY_AND_PASTE) {
                // 粘贴
                String pasteText = clipboard.getText().toString();
                if (!TextUtils.isEmpty(pasteText) && pasteText.startsWith(COPY_IMAGE)) {

                    // 把图片前缀去掉，还原成正常的path
                    sendPicture(pasteText.replace(COPY_IMAGE, ""));

                }
            } else if (conversation.getAllMsgCount() > 0) {
                adapter.refresh();
                getActivity().setResult(Activity.RESULT_OK);
            } else if (requestCode == REQUEST_CODE_GROUP_DETAIL) {
                adapter.refresh();
            }
        }
    }

    /**
     * 消息图标点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        Intent intent;
        String st1 = getResources().getString(R.string.not_connect_to_server);
        int id = view.getId();
        switch (id) {
            case R.id.btn_send:
                String s = mEditTextContent.getText().toString();
                sendText(s);
                break;
            case R.id.btn_take_picture:
                if (isHealthyUser) {
                    StatisticalTools.eventCount(getActivity(), "ResidentPhotograph");
                }
                selectPicFromCamera();// 点击照相图标
                break;
            case R.id.btn_picture:
                if (isHealthyUser) {
                    StatisticalTools.eventCount(getActivity(), "ResidentPhoto");
                }
                selectPicFromLocal(); // 点击图片图标
                break;
            case R.id.btn_dial:
                // 打电话
                StatisticalTools.eventCount(getActivity(), "ResidentTel");
                CallActivity.start(getActivity(), dialId, toHeadImge);
                break;
            case R.id.btn_add_num:
                if (addperinfo != null) {
                    List<AddNumPerInfo> week = addperinfo.getData().getList().getTime();
                    List<String> list = new ArrayList<String>();
                    for (int i = 0; i < week.size(); i++) {
                        // list
                    }
                    String[] weeks = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
                    String[] halfday = new String[]{"上午", "下午", "晚上"};
                    StringBuilder sb = new StringBuilder();
                    String str_enght = null;
                    List<Integer> position = new ArrayList<Integer>();
                    List<Integer> halfdays = new ArrayList<Integer>();
                    for (int i = 0; i < week.size(); i++) {
                        // int
                        // position=3*(Integer.parseInt(week.get(i).getWeek())-1)+(Integer.parseInt(week.get(i).getHalfday())-1);
                        String we = weeks[Integer.parseInt(week.get(i).getWeek()) - 1];
                        String haf = halfday[Integer.parseInt(week.get(i).getHalfday()) - 1];
                        list.add(we + " " + haf);
                        position.add(Integer.parseInt(week.get(i).getWeek()));
                        halfdays.add(Integer.parseInt(week.get(i).getHalfday()));
                    }
                    intent = new Intent(getActivity(), AddNumActivity.class);
                    intent.putStringArrayListExtra("list", (ArrayList<String>) list);
                    intent.putIntegerArrayListExtra("position", (ArrayList<Integer>) position);
                    intent.putIntegerArrayListExtra("halfdays", (ArrayList<Integer>) halfdays);
                    startActivityForResult(intent, REQUEST_ADDNUM_BACK);

                } else {
                    ToastUtils.shortToast("获取加号时间失败");
                }
                break;
            case R.id.btn_men_time:
                if (addperinfo != null) {

                    List<AddNumPerInfo> week = addperinfo.getData().getList().getTime();
                    // sendText(toChatUserRealname + ",您好，我的门诊时间是："
                    // + T.getString_addnum(week));
                    mEditTextContent.setText(toChatUserRealname + ",您好，我的门诊时间是：" + YMOtherUtils.getString_addnum(week));
                    // T.shortToast( "门诊时间");

                } else {
                    ToastUtils.shortToast("获取门诊时间失败");
                }
                break;
            case R.id.btn_reply:
                //			T.shortToast( "快捷回复");
                intent = new Intent(getActivity(), AskPatientReplyActivity.class);
                // startActivity(intent);
                startActivityForResult(intent, REQUEST_CODE_REPLY_BACK);
                break;
            case R.id.btn_complain:
                ToastUtils.shortToast("举报投书");
                break;
            case R.id.iv_emoticons_normal:// 点击显示表情框
                // TODO: 2017/10/27 隐藏软键盘先 stone
                hideKeyboard();
                mHandler4Keyboard.postDelayed(new Runnable() {
                    public void run() {
                        more.setVisibility(View.VISIBLE);
                        iv_emoticons_normal.setVisibility(View.INVISIBLE);
                        iv_emoticons_checked.setVisibility(View.VISIBLE);
                        btnContainer.setVisibility(View.GONE);
                        emojiIconContainer.setVisibility(View.VISIBLE);
                    }
                }, 50);
                break;
            case R.id.iv_emoticons_checked:// 点击隐藏表情框
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
                btnContainer.setVisibility(View.VISIBLE);
                emojiIconContainer.setVisibility(View.GONE);
                more.setVisibility(View.GONE);
                break;
            case R.id.btn_video:
                // 视频通话
                if (!EMChatManager.getInstance().isConnected()) {
                    Toast.makeText(getActivity(), "doctor，先检查一下您的网络吧!", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(getContext(), VideoCallActivity.class).putExtra("username", toChatUsername).putExtra("isComingCall", false).putExtra("realname", toChatUserRealname).putExtra("headimg", toHeadImge));
                }
                break;
            //开处方 stone
            case R.id.btn_prescribe1:
            case R.id.btn_prescribe2:
                getPatientInfoAndGoPharmacy();
                break;
        }

        // else if (id == R.id.btn_video)
        // {
        // // 点击摄像图标
        // // Intent intent = new Intent(ChatActivity.this,
        // // ImageGridActivity.class);
        // // startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
        // }
        // else if (id == R.id.btn_file)
        // { // 点击文件图标
        // selectFileFromLocal();
        // }
        // else if (id == R.id.btn_voice_call)
        // { // 点击语音电话图标
        // // if (!EMChatManager.getInstance().isConnected())
        // // Toast.makeText(this, st1, 0).show();
        // // else
        // // startActivity(new Intent(ChatActivity.this,
        // // VoiceCallActivity.class).putExtra("username", toChatUsername)
        // // .putExtra("isComingCall", false));
        // } else if (id == R.id.btn_video_call)
        // { // 视频通话
        // // if (!EMChatManager.getInstance().isConnected())
        // // Toast.makeText(this, st1, 0).show();
        // // else
        // // startActivity(new Intent(this,
        // // VideoCallActivity.class).putExtra("username", toChatUsername)
        // // .putExtra("isComingCall", false));
        // }
    }

    //stone 先获取用户信息再跳转 赋值给patientmanager
    private void getPatientInfoAndGoPharmacy() {

        //重复点击
        if (ClickUtil.isFastDoubleClick()) {
            return;
        }

        //请求过了直接跳转 否则请求
        Patient patient = PatientManager.getInstance().getPatient();
        if (patient != null && !TextUtils.isEmpty(patient.getHx_user()) && patient.getHx_user().equals(toChatUsername)) {
            PharmacyActivity.start(getActivity());
        } else {

            String patientId = toChatUsername.replace(MyConstant.HX_ID_PREFIX_DOC, "");
            patientId = patientId.replace(MyConstant.HX_ID_PREFIX_USER, "");
            patientId = patientId.replace("_test", "");
            patientId = patientId.replace("did_", "");
            patientId = patientId.replace("uid_", "");
            BaseRetrofitResponse<BaseData<UserInfoBean>> subscriber = new BaseRetrofitResponse<BaseData<UserInfoBean>>() {
                @Override
                public void onStart() {
                    super.onStart();
//               showProgressDialog("");
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    com.xywy.askforexpert.module.discovery.medicine.util.ToastUtils.shortToast("请重试");
                }

                @Override
                public void onNext(BaseData<UserInfoBean> userInfoBeanBaseData) {
                    super.onNext(userInfoBeanBaseData);
                    Patient p = Patient.createFrom(userInfoBeanBaseData.getData());
                    String oldPatientId = SharedPreferencesHelper.getIns(getActivity()).getString(PATIENT_ID, "");
                    if (!p.getUId().equals(oldPatientId)) {
                        MedicineCartCenter.getInstance().removeAllMedicine();//不进行缓存，如果给之前的用户添加了处方笺，
                        //不进行缓存，清空诊断信息
                        SharedPreferencesHelper.getIns(getActivity()).putString(RECIPE_SICK, "");
                    }
                    //将patientId存入sp中，方便对比多次打开的对话页面是否是同一个患者,以便清空处方笺和诊断信息
                    SharedPreferencesHelper.getIns(getActivity()).putString(PATIENT_ID, p.getUId());

                    p.setHx_user(toChatUsername);
                    PatientManager.getInstance().setPatient(p);

//                getActivity().hideProgressDialog();

                    PharmacyActivity.start(getActivity());
                }
            };
            subscriber.onStart();
            UserRequest.getInstance().getUserInfo(patientId)
                    .subscribe(subscriber);
        }
    }

    /**
     * 照相获取图片
     */
    public void selectPicFromCamera() {
        if (!NetworkUtil.isExitsSdcard()) {
            String st = getResources().getString(R.string.sd_card_does_not_exist);
            ToastUtils.shortToast(st);
            return;
        }
        cameraFile = new File(PathUtil.getInstance().getImagePath(), YMApplication.getInstance().getUserName() + System.currentTimeMillis() + ".jpg");
        cameraFile.getParentFile().mkdirs();
//        File file = new File(imageFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri mImageUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果是7.0android系统
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA,cameraFile.getAbsolutePath());
            mImageUri= getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
        }else{
            mImageUri = Uri.fromFile(cameraFile);
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);

//        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)), REQUEST_CODE_CAMERA);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    /**
     * 选择文件
     */
    private void selectFileFromLocal() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }

    /**
     * 从图库获取图片
     */
    public void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }

    /**
     * 发送文本消息
     *
     * @param content message content
     */
    private void sendText(String content) {

        if (!TextUtils.isEmpty(content.trim()) && content.length() > 0) {
            EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
            // 如果是群聊，设置chattype,默认是单聊
            if (chatType == CHATTYPE_GROUP) {
                message.setChatType(ChatType.GroupChat);
            }
            EMTextMessageBody txtBody = new EMTextMessageBody(content);
            // 设置消息body
            message.addBody(txtBody);
            // 设置要发给谁,用户username或者群聊groupid
            message.setReceipt(toChatUsername);
            // 把messgage加到conversation中
            conversation.insertMessage(message);
            // 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
            adapter.refresh();
            listView.setSelection(listView.getCount() - 1);
            mEditTextContent.setText("");

            getActivity().setResult(getActivity().RESULT_OK);

        } else {
            ToastUtils.shortToast("不能发送空白消息");
        }
    }

    /**
     * 添加好友发送成功
     */
    public void sendAddCardText(String tochatname) {
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
        // 如果是群聊，设置chattype,默认是单聊
        if (chatType == CHATTYPE_GROUP) {
            message.setChatType(ChatType.GroupChat);
        }
        EMTextMessageBody txtBody = new EMTextMessageBody("我们是好友了，可以聊天了");
        // 设置消息body
        message.addBody(txtBody);
        // 设置要发给谁,用户username或者群聊groupid
        message.setReceipt(toChatUsername);
        // 把messgage加到conversation中
        conversation.insertMessage(message);
        // 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
        adapter.refresh();
        listView.setSelection(listView.getCount() - 1);
    }

    /**
     * 发送语音
     *
     * @param filePath
     * @param fileName
     * @param length
     * @param isResend
     */
    private void sendVoice(String filePath, String fileName, String length, boolean isResend) {
        if (!(new File(filePath).exists())) {
            return;
        }
        try {
            final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.VOICE);
            // 如果是群聊，设置chattype,默认是单聊
            if (chatType == CHATTYPE_GROUP) {
                message.setChatType(ChatType.GroupChat);
            }
            message.setReceipt(toChatUsername);
            int len = Integer.parseInt(length);
            EMVoiceMessageBody body = new EMVoiceMessageBody(new File(filePath), len);
            message.addBody(body);

            conversation.insertMessage(message);
            adapter.refresh();
            listView.setSelection(listView.getCount() - 1);
            getActivity().setResult(getActivity().RESULT_OK);
            // send file
            //             sendVoiceSub(filePath, fileName, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送图片
     *
     * @param filePath
     */
    private void sendPicture(final String filePath) {
        String to = toChatUsername;
        // create and add image message in view
        final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.IMAGE);
        // 如果是群聊，设置chattype,默认是单聊
        if (chatType == CHATTYPE_GROUP) {
            message.setChatType(ChatType.GroupChat);
        }

        message.setReceipt(to);
        EMImageMessageBody body = new EMImageMessageBody(new File(filePath));
        // 默认超过100k的图片会压缩后发给对方，可以设置成发送原图
        // body.setSendOriginalImage(true);
        message.addBody(body);
        conversation.insertMessage(message);

        listView.setAdapter(adapter);
        adapter.refresh();
        listView.setSelection(listView.getCount() - 1);
        getActivity().setResult(getActivity().RESULT_OK);
        // more(more);
    }

    /**
     * 发送视频消息
     */
    private void sendVideo(final String filePath, final String thumbPath, final int length) {
        final File videoFile = new File(filePath);
        if (!videoFile.exists()) {
            return;
        }
        try {
            EMMessage message = EMMessage.createSendMessage(EMMessage.Type.VIDEO);
            // 如果是群聊，设置chattype,默认是单聊
            if (chatType == CHATTYPE_GROUP) {
                message.setChatType(ChatType.GroupChat);
            }
            String to = toChatUsername;
            message.setReceipt(to);

            //replace by shijiazi // TODO: 16/8/17
            //            EMVideoMessageBody body = new EMVideoMessageBody(videoFile, thumbPath, length, videoFile.length());
            EMVideoMessageBody body = new EMVideoMessageBody(filePath, thumbPath, length, videoFile.length());

            message.addBody(body);
            conversation.insertMessage(message);
            listView.setAdapter(adapter);
            adapter.refresh();
            listView.setSelection(listView.getCount() - 1);
            getActivity().setResult(getActivity().RESULT_OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据图库图片uri发送图片
     *
     * @param selectedImage
     */
    private void sendPicByUri(Uri selectedImage) {
        // String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(selectedImage, null, null, null, null);
        String st8 = getResources().getString(R.string.cant_find_pictures);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex("_data");
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                // Toast toast = Toast.makeText(this, st8, Toast.LENGTH_SHORT);
                // toast.setGravity(Gravity.CENTER, 0, 0);
                // toast.show();
                ToastUtils.shortToast(st8);
                return;
            }
            sendPicture(picturePath);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                // Toast toast = Toast.makeText(this, st8, Toast.LENGTH_SHORT);
                // toast.setGravity(Gravity.CENTER, 0, 0);
                // toast.show();
                ToastUtils.shortToast(st8);
                return;

            }
            sendPicture(file.getAbsolutePath());
        }

    }

    /**
     * 发送位置信息
     *
     * @param latitude
     * @param longitude
     * @param imagePath
     * @param locationAddress
     */
    private void sendLocationMsg(double latitude, double longitude, String imagePath, String locationAddress) {
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.LOCATION);
        // 如果是群聊，设置chattype,默认是单聊
        if (chatType == CHATTYPE_GROUP) message.setChatType(EMMessage.ChatType.GroupChat);
        EMLocationMessageBody locBody = new EMLocationMessageBody(locationAddress, latitude, longitude);
        message.addBody(locBody);
        message.setReceipt(toChatUsername);
        conversation.insertMessage(message);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setSelection(listView.getCount() - 1);
        getActivity().setResult(getActivity().RESULT_OK);

    }

    /**
     * 发送文件
     *
     * @param uri
     */
    private void sendFile(Uri uri) {
        String filePath = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        File file = new File(filePath);
        if (file == null || !file.exists()) {
            String st7 = getResources().getString(R.string.File_does_not_exist);
            ToastUtils.shortToast(st7);
            return;
        }
        if (file.length() > 10 * 1024 * 1024) {
            String st6 = getResources().getString(R.string.The_file_is_not_greater_than_10_m);
            ToastUtils.shortToast(st6);
            return;
        }

        // 创建一个文件消息
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.FILE);
        // 如果是群聊，设置chattype,默认是单聊
        if (chatType == CHATTYPE_GROUP) {
            message.setChatType(ChatType.GroupChat);
        }

        message.setReceipt(toChatUsername);
        // add message body
        EMNormalFileMessageBody body = new EMNormalFileMessageBody(new File(filePath));
        message.addBody(body);
        conversation.insertMessage(message);
        listView.setAdapter(adapter);
        adapter.refresh();
        listView.setSelection(listView.getCount() - 1);
        getActivity().setResult(getActivity().RESULT_OK);
    }

    /**
     * 重发消息
     */
    private void resendMessage() {
        EMMessage msg = null;
        //// TODO: 16/8/17 replace by shijiazi
        //        msg = conversation.getMessage(resendPos);
        msg = adapter.getItem(resendPos);


        // msg.setBackSend(true);
        msg.setStatus(EMMessage.Status.CREATE);

        adapter.refresh();
        listView.setSelection(resendPos);
    }

    class MySetModeVoice implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            hideKeyboard();
            edittext_layout.setVisibility(View.GONE);
            more.setVisibility(View.GONE);
            buttonSetModeVoice.setVisibility(View.GONE);
            buttonSetModeKeyboard.setVisibility(View.VISIBLE);
            buttonSend.setVisibility(View.GONE);
            btnMore.setVisibility(View.VISIBLE);
            buttonPressToSpeak.setVisibility(View.VISIBLE);
            //stone
            if (!YMApplication.sIsYSZS) {
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
            }
            btnContainer.setVisibility(View.VISIBLE);
            emojiIconContainer.setVisibility(View.GONE);
        }

    }

    /**
     * 显示语音图标按钮
     *
     * @param view
     */
    public void setModeVoice(View view) {
        hideKeyboard();
        edittext_layout.setVisibility(View.GONE);
        more.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        buttonSetModeKeyboard.setVisibility(View.VISIBLE);
        buttonSend.setVisibility(View.GONE);
        btnMore.setVisibility(View.VISIBLE);
        buttonPressToSpeak.setVisibility(View.VISIBLE);
        //stone
        if (!YMApplication.sIsYSZS) {
            iv_emoticons_normal.setVisibility(View.VISIBLE);
            iv_emoticons_checked.setVisibility(View.INVISIBLE);
        }
        btnContainer.setVisibility(View.VISIBLE);
        emojiIconContainer.setVisibility(View.GONE);

    }

    class MySetModeKeyboard implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            edittext_layout.setVisibility(View.VISIBLE);
            more.setVisibility(View.GONE);
            buttonSetModeKeyboard.setVisibility(View.GONE);
            buttonSetModeVoice.setVisibility(View.VISIBLE);
            // mEditTextContent.setVisibility(View.VISIBLE);
            mEditTextContent.requestFocus();
            // buttonSend.setVisibility(View.VISIBLE);
            buttonPressToSpeak.setVisibility(View.GONE);
            if (TextUtils.isEmpty(mEditTextContent.getText())) {
                btnMore.setVisibility(View.VISIBLE);
                buttonSend.setVisibility(View.GONE);
            } else {
                btnMore.setVisibility(View.GONE);
                buttonSend.setVisibility(View.VISIBLE);
            }

        }

    }

    /**
     * 点击清空聊天记录
     *
     * @param view
     */
    public void emptyHistory(View view) {
        String st5 = getResources().getString(R.string.Whether_to_empty_all_chats);
        startActivityForResult(new Intent(getActivity(), AlertDialog.class).putExtra("titleIsCancel", true).putExtra("msg", st5).putExtra("cancel", true), REQUEST_CODE_EMPTY_HISTORY);
    }

    // /**
    // * 点击进入群组详情
    // *
    // * @param view
    // */
    // public void toGroupDetails(View view) {
    // if(group == null){
    // Toast.makeText(getApplicationContext(), R.string.gorup_not_found,
    // 0).show();
    // return;
    // }
    // startActivityForResult((new Intent(this,
    // GroupDetailsActivity.class).putExtra("groupId", toChatUsername)),
    // REQUEST_CODE_GROUP_DETAIL);
    // }

    class MyMore implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            if (isHealthyUser) {
                StatisticalTools.eventCount(getActivity(), "ResidentPlus");
            }
            if (more.getVisibility() == View.GONE) {
                //stone 先隐藏键盘 再..
                hideKeyboard();
                mHandler4Keyboard.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        more.setVisibility(View.VISIBLE);
                        btnContainer.setVisibility(View.VISIBLE);
                        emojiIconContainer.setVisibility(View.GONE);
                    }
                }, 50);
            } else {
                if (emojiIconContainer.getVisibility() == View.VISIBLE) {
                    emojiIconContainer.setVisibility(View.GONE);
                    btnContainer.setVisibility(View.VISIBLE);
                    //stone
                    if (!YMApplication.sIsYSZS) {
                        iv_emoticons_normal.setVisibility(View.VISIBLE);
                        iv_emoticons_checked.setVisibility(View.INVISIBLE);
                    }
                } else {
                    more.setVisibility(View.GONE);
                }

            }

        }

    }

    // /**
    // * 显示或隐藏图标按钮页
    // *
    // * @param view
    // */
    // public void more(View view)
    // {
    // if (more.getVisibility() == View.GONE)
    // {
    // System.out.println("more gone");
    // hideKeyboard();
    // more.setVisibility(View.VISIBLE);
    // btnContainer.setVisibility(View.VISIBLE);
    // emojiIconContainer.setVisibility(View.GONE);
    // } else
    // {
    // if (emojiIconContainer.getVisibility() == View.VISIBLE)
    // {
    // emojiIconContainer.setVisibility(View.GONE);
    // btnContainer.setVisibility(View.VISIBLE);
    // iv_emoticons_normal.setVisibility(View.VISIBLE);
    // iv_emoticons_checked.setVisibility(View.INVISIBLE);
    // } else
    // {
    // more.setVisibility(View.GONE);
    // }
    //
    // }
    //
    // }

    /**
     * 点击文字输入框
     *
     * @param v
     */
    public void editClick(View v) {
        listView.setSelection(listView.getCount() - 1);
        if (more.getVisibility() == View.VISIBLE) {
            more.setVisibility(View.GONE);
            //stone
            if (!YMApplication.sIsYSZS) {
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
            }
        }

    }

    //    /**
    //     * 消息广播接收者
    //     */
    //    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
    //        @Override
    //        public void onReceive(Context context, Intent intent) {
    //            // 记得把广播给终结掉
    //            abortBroadcast();
    //
    //            String username = intent.getStringExtra("from");
    //            String msgid = intent.getStringExtra("msgid");
    //            // 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
    //            EMMessage message = EMChatManager.getInstance().getMessage(msgid);
    //            message.getStringAttribute("toUserRealName", "");
    //            // 如果是群聊消息，获取到group id
    //            if (message.getChatType() == EMMessage.ChatType.GroupChat) {
    //                username = message.getTo();
    //            }
    //            if (!username.equals(toChatUsername)) {
    //                // 消息不是发给当前会话，return
    //                notifyNewMessage(message);
    //                return;
    //            }
    //            // conversation =
    //            // EMChatManager.getInstance().getConversation(toChatUsername);
    //            // 通知adapter有新消息，更新ui
    //            adapter.refresh();
    //            listView.setSelection(listView.getCount() - 1);
    //
    //        }
    //    }

    //    /**
    //     * 消息回执BroadcastReceiver
    //     */
    //    private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {
    //        @Override
    //        public void onReceive(Context context, Intent intent) {
    //            abortBroadcast();
    //
    //            String msgid = intent.getStringExtra("msgid");
    //            String from = intent.getStringExtra("from");
    //            EMConversation conversation = EMChatManager.getInstance().getConversation(from);
    //            if (conversation != null) {
    //                // 把message设为已读
    //                //// replace by shijiazi
    ////                EMMessage msg = conversation.getMessage(msgid);
    //                EMMessage msg = conversation.getMessage(msgid, true);
    //                if (msg != null) {
    //                    msg.setAcked(true);
    //                }
    //            }
    //            adapter.notifyDataSetChanged();
    //
    //        }
    //    };

    //    /**
    //     * 消息送达BroadcastReceiver
    //     */
    //    private BroadcastReceiver deliveryAckMessageReceiver = new BroadcastReceiver() {
    //        @Override
    //        public void onReceive(Context context, Intent intent) {
    //            abortBroadcast();
    //
    //            String msgid = intent.getStringExtra("msgid");
    //            String from = intent.getStringExtra("from");
    //            EMConversation conversation = EMChatManager.getInstance().getConversation(from);
    //            if (conversation != null) {
    //                // 把message设为已读
    //                //// replace by shijiazi
    ////                EMMessage msg = conversation.getMessage(msgid);
    //                EMMessage msg = conversation.getMessage(msgid, true);
    //                if (msg != null) {
    //                    msg.setDelivered(true);
    //                }
    //            }
    //
    //            adapter.notifyDataSetChanged();
    //        }
    //    };
    private PowerManager.WakeLock wakeLock;
    CountDownTimer countDownTimer;
    boolean isSendVoice;

    public void Timer(final View v, final VoiceRecorder voiceRecorder) {
        if (countDownTimer == null) {

            countDownTimer = new CountDownTimer(65 * 1000, 500) {
                @Override
                public void onTick(long millisUntilFinished) {
                    //Do nothing
                    //                   int length = voiceRecorder.stopRecoding();
                    //                    if(length==60)


                }

                @Override
                public void onFinish() {
                    isSendVoice = true;
                    recordingContainer.setVisibility(View.INVISIBLE);
                    sVoice();
                    v.setPressed(false);
                }
            }.start();
        }

    }

    public void sVoice() {
        String st1 = getResources().getString(R.string.Recording_without_permission);
        String st2 = getResources().getString(R.string.The_recording_time_is_too_short);
        String st3 = getResources().getString(R.string.send_failure_please);
        try {
            int length = voiceRecorder.stopRecoding();
            if (length > 0) {
                sendVoice(voiceRecorder.getVoiceFilePath(), voiceRecorder.getVoiceFileName(toChatUsername), Integer.toString(length), false);
            } else if (length == EMError.FILE_INVALID) {
                ToastUtils.shortToast(st1);
            } else {
                ToastUtils.shortToast(st2);

            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.shortToast(st3);
        }
    }

    /**
     * 按住说话listener
     */
    class PressToSpeakListen implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isSendVoice = false;
                    if (!NetworkUtil.isExitsSdcard()) {
                        String st4 = getResources().getString(R.string.Send_voice_need_sdcard_support);
                        ToastUtils.shortToast(st4);
                        return false;
                    }
                    try {

                        v.setPressed(true);
                        wakeLock.acquire();
                        if (VoicePlayClickListener.isPlaying) {
                            VoicePlayClickListener.currentPlayListener.stopPlayVoice();
                        }
                        recordingContainer.setVisibility(View.VISIBLE);
                        recordingHint.setText(getString(R.string.move_up_to_cancel));
                        recordingHint.setBackgroundColor(Color.TRANSPARENT);
                        voiceRecorder.startRecording(null, toChatUsername, getActivity().getApplicationContext());
                        Timer(v, voiceRecorder);
                    } catch (Exception e) {
                        e.printStackTrace();
                        v.setPressed(false);
                        if (wakeLock.isHeld()) {
                            wakeLock.release();
                        }
                        if (voiceRecorder != null) {
                            voiceRecorder.discardRecording();
                        }
                        recordingContainer.setVisibility(View.INVISIBLE);
                        ToastUtils.shortToast(getResources().getString(R.string.recoding_fail));
                        return false;
                    }

                    return true;
                case MotionEvent.ACTION_MOVE: {
                    if (event.getY() < 0) {
                        recordingHint.setText(getString(R.string.release_to_cancel));
                        recordingHint.setBackgroundResource(R.drawable.recording_text_hint_bg);
                    } else {
                        recordingHint.setText(getString(R.string.move_up_to_cancel));
                        recordingHint.setBackgroundColor(Color.TRANSPARENT);
                    }

                    return true;
                }
                case MotionEvent.ACTION_UP:
                    v.setPressed(false);
                    recordingContainer.setVisibility(View.INVISIBLE);
                    if (wakeLock.isHeld()) {
                        wakeLock.release();
                    }
                    if (event.getY() < 0) {
                        // discard the recorded audio.
                        voiceRecorder.discardRecording();
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                            countDownTimer = null;
                        }
                    } else {
                        // stop recording and send voice file
                        if (countDownTimer != null) {
                            if (!isSendVoice) {
                                countDownTimer.onFinish();
                            }
                            countDownTimer.cancel();
                            countDownTimer = null;
                        }


                    }
                    return true;
                default:
                    recordingContainer.setVisibility(View.INVISIBLE);
                    if (voiceRecorder != null) {
                        voiceRecorder.discardRecording();
                    }
                    return false;
            }
        }
    }

    /**
     * 获取表情的gridview的子view
     *
     * @param i
     * @return
     */
    private View getGridChildView(int i) {
        View view = View.inflate(getActivity(), R.layout.expression_gridview, null);
        ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
        List<String> list = new ArrayList<String>();
        if (i == 1) {
            List<String> list1 = reslist.subList(0, 20);
            list.addAll(list1);
        } else if (i == 2) {
            list.addAll(reslist.subList(20, reslist.size()));
        }
        list.add("delete_expression");
        final ExpressionAdapter expressionAdapter = new ExpressionAdapter(getActivity(), 1, list);
        gv.setAdapter(expressionAdapter);
        gv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filename = expressionAdapter.getItem(position);
                try {
                    // 文字输入框可见时，才可输入表情
                    // 按住说话可见，不让输入表情
                    if (buttonSetModeKeyboard.getVisibility() != View.VISIBLE) {

                        if (filename != "delete_expression") { // 不是删除键，显示表情
                            // 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
                            Class clz = Class.forName("com.xywy.easeWrapper.utils.SmileUtils");
                            Field field = clz.getField(filename);
                            mEditTextContent.append(SmileUtils.getSmiledText(getActivity(), (String) field.get(null)));
                        } else { // 删除文字或者表情
                            if (!TextUtils.isEmpty(mEditTextContent.getText())) {

                                int selectionStart = mEditTextContent.getSelectionStart();// 获取光标的位置
                                if (selectionStart > 0) {
                                    String body = mEditTextContent.getText().toString();
                                    String tempStr = body.substring(0, selectionStart);
                                    int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                                    if (i != -1) {
                                        CharSequence cs = tempStr.substring(i, selectionStart);
                                        if (SmileUtils.containsKey(cs.toString())) {
                                            mEditTextContent.getEditableText().delete(i, selectionStart);
                                        } else {
                                            mEditTextContent.getEditableText().delete(selectionStart - 1, selectionStart);
                                        }
                                    } else {
                                        mEditTextContent.getEditableText().delete(selectionStart - 1, selectionStart);
                                    }
                                }
                            }

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        return view;
    }

    public List<String> getExpressionRes(int getSum) {
        List<String> reslist = new ArrayList<String>();
        for (int x = 1; x <= getSum; x++) {
            String filename = "ee_" + x;

            reslist.add(filename);

        }
        return reslist;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        activityInstance = null;
        EMGroupManager.getInstance().removeGroupChangeListener(groupListener);

        EMChatManager.getInstance().removeMessageListener(messageListener);

        //        // 注销广播
        //        try {
        //            getActivity().unregisterReceiver(receiver);
        //            receiver = null;
        //        } catch (Exception e) {
        //        }
        try {
            //            getActivity().unregisterReceiver(ackMessageReceiver);
            //            ackMessageReceiver = null;
            //            getActivity().unregisterReceiver(deliveryAckMessageReceiver);
            //            deliveryAckMessageReceiver = null;
            //            getActivity().unregisterReceiver(cmdMessageReceiver);
            //            cmdMessageReceiver = null;
            //            getActivity().unregisterReceiver(broadcastReceiver);
            //            broadcastReceiver = null;
            getActivity().unregisterReceiver(needUpdateReceive);
            needUpdateReceive = null;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (group != null) {
            ((TextView) getActivity().findViewById(R.id.name)).setText(group.getGroupName());
        }
        //		adapter.refresh();

        if (adapter != null) {
            if (!TextUtils.isEmpty(toChatUsername)) {
                String realname = ContactService.getInstance().getGroupName(toChatUsername);
                String headimg = ContactService.getInstance().getGroupImg(toChatUsername);
                if (!TextUtils.isEmpty(realname)) {
                    adapter.stRealName(realname);
                }
                if (!TextUtils.isEmpty(headimg)) {
                    adapter.stHeadImg(headimg);
                }

            }

            if (mIsFirstIn) {
                mIsFirstIn = false;
                adapter.refresh();
            } else {
                adapter.refreshNoSetSelection();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
        if (VoicePlayClickListener.isPlaying && VoicePlayClickListener.currentPlayListener != null) {
            // 停止语音播放
            VoicePlayClickListener.currentPlayListener.stopPlayVoice();
        }

        try {
            // 停止录音
            if (voiceRecorder.isRecording()) {
                voiceRecorder.discardRecording();
                recordingContainer.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null) {
                manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 加入到黑名单
     *
     * @param username
     */
    private void addUserToBlacklist(final String username) {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage(getString(R.string.Is_moved_into_blacklist));
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMContactManager.getInstance().addUserToBlackList(username, false);
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            ToastUtils.shortToast(getResources().getString(R.string.Move_into_blacklist_success));
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            ToastUtils.shortToast(getResources().getString(R.string.Move_into_blacklist_failure));

                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 返回
     *
     * @param view
     */
    public void back(View view) {
        getActivity().finish();
    }

    // /**
    // * 覆盖手机返回键
    // */
    // @Override
    // public void onBackPressed()
    // {
    // if (more.getVisibility() == View.VISIBLE)
    // {
    // more.setVisibility(View.GONE);
    // iv_emoticons_normal.setVisibility(View.VISIBLE);
    // iv_emoticons_checked.setVisibility(View.INVISIBLE);
    // } else
    // {
    // super.onBackPressed();
    // }
    // }

    /**
     * listview滑动监听listener
     */
    private class ListScrollListener implements OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                case OnScrollListener.SCROLL_STATE_IDLE:
                    if (view.getFirstVisiblePosition() == 0 && !isloading && haveMoreData) {
//                        loadmorePB.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                // sdk初始化加载的聊天记录为20条，到顶时去db里获取更多
                                List<EMMessage> messages;
                                try {
                                    // 获取更多messges，调用此方法的时候从db获取的messages
                                    // sdk会自动存入到此conversation中
                                    if (chatType == CHATTYPE_SINGLE) {
                                        messages = conversation.loadMoreMsgFromDB(adapter.getItem(0).getMsgId(), pagesize);
                                    } else {
                                        messages = conversation.loadMoreMsgFromDB(adapter.getItem(0).getMsgId(), pagesize);
                                    }
                                    LogUtils.d("个数" + messages.size());
                                } catch (Exception e1) {
//                                    loadmorePB.setVisibility(View.GONE);
                                    return;
                                }
                                //stone 去掉
//                        try {
//                            Thread.sleep(300);
//                        } catch (InterruptedException e) {
//                        }
                                if (messages.size() != 0) {
                                    //stone
                                    adapter.refreshNoSetSelection();
                                    // 刷新ui
//                            adapter.notifyDataSetChanged();
                                    //stone 去掉
//                            listView.setSelection(messages.size() - 1);
                                    if (messages.size() != pagesize) {
                                        haveMoreData = false;
                                    }
                                } else {
                                    haveMoreData = false;
                                }
//                                loadmorePB.setVisibility(View.GONE);
                                isloading = false;
                            }
                        }, 600);
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }

    }

    //stone 加载更多消息
    private void loadMoreLocalMessage() {
        if (listView.getFirstVisiblePosition() == 0 && !isloading && haveMoreData) {
            List<EMMessage> messages;
            try {
                messages = conversation.loadMoreMsgFromDB(conversation.getAllMessages().size() == 0 ? "" : conversation.getAllMessages().get(0).getMsgId(),
                        pagesize);
            } catch (Exception e1) {
                swipeRefreshLayout.setRefreshing(false);
                return;
            }
            if (messages.size() > 0) {
                adapter.refreshNoSetSelection();
                if (messages.size() != pagesize) {
                    haveMoreData = false;
                }
            } else {
                haveMoreData = false;
                //stone 没有更多消息提醒及时提醒,不用下次请求的时候提醒
                Toast.makeText(getActivity(), getResources().getString(R.string.no_more_messages),
                        Toast.LENGTH_SHORT).show();
            }

            isloading = false;
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_more_messages),
                    Toast.LENGTH_SHORT).show();
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    // @Override
    // protected void onNewIntent(Intent intent)
    // {
    // // 点击notification bar进入聊天页面，保证只有一个聊天页面
    // String username = intent.getStringExtra("userId");
    // if (toChatUsername.equals(username))
    // super.onNewIntent(intent);
    // else
    // {
    // getActivity().finish();
    // startActivity(intent);
    // }
    //
    // }

    /**
     * 转发消息
     *
     * @param forward_msg_id
     */
    protected void forwardMessage(String forward_msg_id) {
        EMMessage forward_msg = EMChatManager.getInstance().getMessage(forward_msg_id);
        EMMessage.Type type = forward_msg.getType();
        switch (type) {
            case TXT:
                // 获取消息内容，发送消息
                String content = ((EMTextMessageBody) forward_msg.getBody()).getMessage();
                sendText(content);
                break;
            case IMAGE:
                // 发送图片
                String filePath = ((EMImageMessageBody) forward_msg.getBody()).getLocalUrl();
                if (filePath != null) {
                    File file = new File(filePath);
                    if (!file.exists()) {
                        // 不存在大图发送缩略图
                        filePath = ImageUtils.getThumbnailImagePath(filePath);
                    }
                    sendPicture(filePath);
                }
                break;
            default:
                break;
        }
    }


    /**
     * 监测群组解散或者被T事件
     */
    class GroupListener extends EaseGroupRemoveListener {

        @Override
        public void onUserRemoved(final String groupId, String groupName) {
            DLog.d(TAG, "被删除");
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    T.showNoRepeatLong(getActivity(), "您已经被管理员踢掉了");
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                }
            });

        }

        //stone 替换 环信版本升级3.1.4->3.1.5
        @Override
        public void onGroupDestroyed(String s, String s1) {
            // 群组解散正好在此页面，提示群组被解散，并finish此页面
            if (getActivity() != null) {
                getActivity().finish();
            }
        }

        //stone 旧的 环信版本升级3.1.4->3.1.5
//        @Override
//        public void onGroupDestroy(final String groupId, String groupName) {
//            // 群组解散正好在此页面，提示群组被解散，并finish此页面
//            if (getActivity() != null) {
//                getActivity().finish();
//            }
//        }

        @Override
        public void onInvitationReceived(String groupid, String groupName, String invite, String reson) {
            super.onInvitationReceived(groupid, groupName, invite, reson);
            DLog.d(TAG, "被邀请");
        }

    }

    //    /**
    //     * 透传消息BroadcastReceiver
    //     */
    //    private BroadcastReceiver cmdMessageReceiver = new BroadcastReceiver() {
    //
    //        @Override
    //        public void onReceive(Context context, Intent intent) {
    //            abortBroadcast();
    //            DLog.i(TAG, "收到透传消息");
    //            // 获取cmd message对象
    //            try {
    //
    //                if (adapter != null) {
    //
    //
    //                    String msgId = intent.getStringExtra("msgid");
    //                    EMMessage message = intent.getParcelableExtra("message");
    //                    String userid = DPApplication.getLoginInfo().getData().getHuanxin_username();
    ////                    userid  = userid.substring(userid.indexOf("_")+1,userid.length());
    //                    if (message.getFrom().equals(userid)) {
    //                        return;
    //                    }
    //                    // 获取消息body
    //                    EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
    //                    String action = cmdMsgBody.action();// 获取自定义action
    //                    if (!TextUtils.isEmpty(action) && "groupCMD".equals(action)) {
    //
    //                        String command = message.getStringAttribute("command");
    //                        String content = message.getStringAttribute("content");
    //                        String txt = "";
    //                        Gson gson = new Gson();
    //                        CMDMessageInfo cmdMessageInfo = new CMDMessageInfo();
    //                        cmdMessageInfo = gson.fromJson(content, CMDMessageInfo.class);
    //                        ContactService cs = ContactService.getInstance();
    //                        GroupModel groupModel = cs.getGroupInfo(message.getTo());
    //                        switch (command) {
    //                            case "updateName":
    //                                String realname = cmdMessageInfo.getGroupName();
    //                                if (!TextUtils.isEmpty(realname)) {
    //                                    txt = "管理员已将群名称改为" + realname;
    //                                    groupModel.setContactName(cmdMessageInfo.getGroupName());
    //                                    cs.updateGroupinfo(groupModel);
    //
    //                                    adapter.stRealName(realname);
    //                                }
    //
    //                                ((ChatMainActivity) getActivity()).onResume();
    //
    //                                break;
    //                            case "updateImg":
    //                                txt = "管理员已修改群头像";
    //                                String headimg = cmdMessageInfo.getHeadUrl();
    //                                if (!TextUtils.isEmpty(headimg)) {
    //                                    groupModel.setHeadUrl(cmdMessageInfo.getHeadUrl());
    //                                    cs.updateGroupinfo(groupModel);
    //
    //                                    adapter.stHeadImg(headimg);
    //
    //                                }
    //                                break;
    //                            case "inviteUser":
    //                                if (cmdMessageInfo != null && cmdMessageInfo.getInviteNumber() != null && cmdMessageInfo.getInviteNumber().size() > 0) {
    //                                    String invitStr = Strname(cmdMessageInfo.getInviteNumber());
    //                                    txt = "群主" + cmdMessageInfo.getOwnername() + "邀请了 " + invitStr + "加入群聊";
    //
    //                                }
    //                                break;
    //                            case "addUser":
    //                                if (cmdMessageInfo != null && cmdMessageInfo.getInviteNumber() != null && cmdMessageInfo.getInviteNumber().size() > 0) {
    //                                    String invitStr = Strname(cmdMessageInfo.getInviteNumber());
    //                                    txt = invitStr + "已加入群聊";
    //                                }
    //                                break;
    //                            case "deleteUser":
    //                                if (cmdMessageInfo != null && cmdMessageInfo.getDeleteNumber() != null && cmdMessageInfo.getDeleteNumber().size() > 0) {
    //                                    String invitStr = cmdMessageInfo.getDeleteNumber().get(0).getUsername();
    //                                    txt = invitStr + "已被管理员移除本讨论群";
    //                                }
    //                                break;
    //                            case "exitGroupOwen":
    //                                if (cmdMessageInfo != null) {
    //                                    String ownername = cmdMessageInfo.getOwnername();
    //                                    int ownerId = Integer.valueOf(cmdMessageInfo.getOwner());
    //                                    int userids = Integer.valueOf(CommonAppUtils.getCurUserId());
    //                                    txt = (ownerId == userids) ? "您成为本群管理员" : ownername + "已成为本群管理员";
    //                                }
    //                                break;
    //                        }
    //
    //                        ChatSendMessageHelper.saveMessage(message.getTo(), txt, cmdMessageInfo.getGroupName(), cmdMessageInfo.getHeadUrl());
    //                        ContactService.getInstance().getGroupDetail(message.getTo(), new CommonResponse<GroupModel>(getActivity()) {
    //                            @Override
    //                            public void onNext(GroupModel groupModel) {
    //                                String realname = ContactService.getInstance().getGroupName(toChatUsername);
    //                                String headimg = ContactService.getInstance().getGroupImg(toChatUsername);
    //                                if (!TextUtils.isEmpty(realname) && adapter != null) {
    //                                    adapter.stRealName(realname);
    //                                }
    //                                if (!TextUtils.isEmpty(headimg) && adapter != null) {
    //                                    adapter.stHeadImg(headimg);
    //                                }
    //                                ((ChatMainActivity) getActivity()).onResume();
    //                            }
    //                        });
    //
    //                        if (adapter != null)
    //                            adapter.refresh();
    //                    }
    //                }
    //            } catch (HyphenateException e) {
    //                // TODO Auto-generated catch block
    //                e.printStackTrace();
    //            }
    //
    //
    //        }
    //    };

    public BroadcastReceiver needUpdateReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (adapter != null) {
                adapter.refresh();
            }
        }
    };
    /**
     * 群组事件广播监听
     */
    protected BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (GroupBroadCastAction.EXIST_GROUP_ACTION.equals(action)) {
                //TODO：接收到退群广播 需要处理的逻辑
                getActivity().finish();
            }
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

    public String getToChatUsername() {
        return toChatUsername;
    }

    public ListView getListView() {
        return listView;
    }

    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(final List<EMMessage> list) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    for (EMMessage message : list) {
                        onNewMessage(message);
                    }
                }
            });
        }

        @Override
        public void onCmdMessageReceived(final List<EMMessage> list) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    for (EMMessage message : list) {
                        onCMDMessage(message);
                    }
                }
            });
        }

        @Override
        public void onMessageReadAckReceived(final List<EMMessage> list) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    for (EMMessage message : list) {
                        onAckMessage(message);
                    }
                }
            });
        }

        @Override
        public void onMessageDeliveryAckReceived(final List<EMMessage> list) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    for (EMMessage message : list) {
                        onAckMessage(message);
                    }
                }
            });
        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };

    private void onNewMessage(EMMessage message) {
        String username = "";
        message.getStringAttribute("toUserRealName", "");
        // 如果是群聊消息，获取到group id
        if (message.getChatType() == EMMessage.ChatType.GroupChat) {
            username = message.getTo();
        } else {
            username = message.getFrom();
        }
        if (!username.equals(toChatUsername)) {
            // 消息不是发给当前会话，return
            //replace by shijiazi
            //            notifyNewMessage(message);
            EaseUI.getInstance().getNotifier().onNewMsg(message);
            return;
        }

        if (conversation != null) {
            // 把此会话的未读数置为0
            conversation.markAllMessagesAsRead();
        }

        // conversation =
        // EMChatManager.getInstance().getConversation(toChatUsername);
        // 通知adapter有新消息，更新ui
        adapter.refresh();
        listView.setSelection(listView.getCount() - 1);
    }

    private void onAckMessage(EMMessage message) {
        //收到患者发过来的消息，表明有患者咨询了医生，这时，要通知MedicineAssistantActivity
        //页面从新请求最近咨询患者的接口了
        MyRxBus.notifyLatestPatientInfoChanged();
        EMConversation conversation = EMChatManager.getInstance().getConversation(message.getFrom());
        if (conversation != null) {
            if (message != null) {
                message.setAcked(true);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void onCMDMessage(EMMessage message) {
        try {
            if (adapter != null) {
                String userid = YMApplication.getLoginInfo().getData().getHuanxin_username();
                //                    userid  = userid.substring(userid.indexOf("_")+1,userid.length());
                if (message.getFrom().equals(userid)) {
                    return;
                }
                // 获取消息body
                EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                String action = cmdMsgBody.action();// 获取自定义action
                if (!TextUtils.isEmpty(action) && "groupCMD".equals(action)) {

                    String command = message.getStringAttribute("command");
                    String content = message.getStringAttribute("content");
                    String txt = "";
                    Gson gson = new Gson();
                    CMDMessageInfo cmdMessageInfo = new CMDMessageInfo();
                    cmdMessageInfo = gson.fromJson(content, CMDMessageInfo.class);
                    switch (command) {
                        case "updateName":
                            String realname = cmdMessageInfo.getGroupName();
                            if (!TextUtils.isEmpty(realname)) {
                                txt = "管理员已将群名称改为" + realname;
                                //ContactService.getInstance().updateGroupName(message.getTo(), realname);
                                adapter.stRealName(realname);
                            }

                            ((ChatMainActivity) getActivity()).onResume();

                            break;
                        case "updateImg":
                            txt = "管理员已修改群头像";
                            String headimg = cmdMessageInfo.getHeadUrl();
                            if (!TextUtils.isEmpty(headimg)) {
                                //ContactService.getInstance().updateGroupHeadUrl(message.getTo(), headimg);
                                adapter.stHeadImg(headimg);
                            }
                            break;
                        case "inviteUser":
                            if (cmdMessageInfo != null && cmdMessageInfo.getInviteNumber() != null && cmdMessageInfo.getInviteNumber().size() > 0) {
                                String invitStr = Strname(cmdMessageInfo.getInviteNumber());
                                txt = "群主" + cmdMessageInfo.getOwnername() + "邀请了 " + invitStr + "加入群聊";

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
                        default:
                            break;
                    }

                    ChatSendMessageHelper.saveMessage(message.getTo(), txt, cmdMessageInfo.getGroupName(), cmdMessageInfo.getHeadUrl());
                    ContactService.getInstance().getGroupDetail(message.getTo(), new CommonResponse<GroupModel>(getActivity()) {
                        @Override
                        public void onNext(GroupModel groupModel) {
                            String realname = ContactService.getInstance().getGroupName(toChatUsername);
                            String headimg = ContactService.getInstance().getGroupImg(toChatUsername);
                            if (!TextUtils.isEmpty(realname)) {
                                adapter.stRealName(realname);
                            }
                            if (!TextUtils.isEmpty(headimg)) {
                                adapter.stHeadImg(headimg);
                            }
                            ((ChatMainActivity) getActivity()).onResume();
                        }
                    });
                    if (adapter != null) adapter.refresh();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //隐藏键盘使用
    private Handler mHandler4Keyboard = new Handler();

}