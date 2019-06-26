package com.xywy.askforexpert.module.main.patient.activity;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.util.PathUtil;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ClickUtil;
import com.xywy.askforexpert.appcommon.utils.DateUtils;
import com.xywy.askforexpert.appcommon.utils.PermissionUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.certification.MessageBoardBean;
import com.xywy.askforexpert.model.consultentity.BackQuestionRspEntity;
import com.xywy.askforexpert.model.consultentity.ChatBottomItemEntity;
import com.xywy.askforexpert.model.consultentity.CommonRspEntity;
import com.xywy.askforexpert.model.consultentity.ConsultChatEntity;
import com.xywy.askforexpert.model.consultentity.OnlineConsultChatEntity;
import com.xywy.askforexpert.model.consultentity.OnlineQuestionMsgListRspEntity;
import com.xywy.askforexpert.model.websocket.msg.chatmsg.ChatMsg;
import com.xywy.askforexpert.model.websocket.rxevent.MsgReadEventBody;
import com.xywy.askforexpert.model.websocket.type.ContentType;
import com.xywy.askforexpert.module.consult.ConsultConstants;
import com.xywy.askforexpert.module.consult.ServiceProvider;
import com.xywy.askforexpert.module.consult.activity.ConsultQueSwitchDpartAcitivity;
import com.xywy.askforexpert.module.consult.adapter.ChatBottomAdapter;
import com.xywy.askforexpert.module.consult.adapter.delegate.OnlineConsultChatAdapter;
import com.xywy.askforexpert.module.discovery.medicine.IMFastReplyActivity;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;
import com.xywy.askforexpert.module.drug.PrescriptionActivity;
import com.xywy.askforexpert.module.drug.PrescriptionDetailActivity;
import com.xywy.askforexpert.module.drug.bean.PrescriptionData;
import com.xywy.askforexpert.module.my.photo.PhotoWallActivity;
import com.xywy.askforexpert.module.my.userinfo.ClipPictureActivity;
import com.xywy.askforexpert.module.websocket.WebSocketApi;
import com.xywy.askforexpert.module.websocket.WebSocketImInterface;
import com.xywy.askforexpert.module.websocket.WebSocketRxBus;
import com.xywy.askforexpert.sdk.kdxf.KDXFUtils;
import com.xywy.askforexpert.widget.promptView.ChatPromptViewManager;
import com.xywy.askforexpert.widget.promptView.Location;
import com.xywy.askforexpert.widget.promptView.PromptViewHelper;
import com.xywy.askforexpert.widget.view.SelectPicPopupWindow;
import com.xywy.retrofit.rxbus.Event;
import com.xywy.retrofit.rxbus.EventSubscriber;
import com.xywy.util.KeyBoardUtils;
import com.xywy.util.L;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscriber;

import static com.xywy.askforexpert.YMApplication.mIsNoticeOpen;
import static com.xywy.askforexpert.appcommon.old.Constants.NIGHT;
import static com.xywy.askforexpert.appcommon.old.Constants.OFFLINE;
import static com.xywy.askforexpert.appcommon.old.Constants.OPENED;
import static com.xywy.askforexpert.model.consultentity.ConsultChatEntity.TYPE_CLOSE_NOTICE;


/**
 * 在线诊室聊天详情页面 stone
 */
public class PatientChatDetailActivity extends YMBaseActivity implements View.OnClickListener, ChatBottomAdapter.OnItemClickListener, WebSocketImInterface {

    /**
     * 图片地址
     */
    private File origUri;
    private SelectPicPopupWindow menuWindow;

    private Map<String, String> map = new HashMap<String, String>();
    private Map<String, String> map2 = new HashMap<String, String>();

    private FinalBitmap fb;
    private String mHeadIconPath;
    private String mHeadIconUploadingPath;//上传中图片


    private ClipboardManager mClipboardManager;

    private boolean mOfflineOpened, mNightOpend;

    @Bind(R.id.rlv_chat_list)
    RecyclerView rlvChat;

    @Bind(R.id.main)
    View main;

    //底部聊天
    @Bind(R.id.fl_bottom_chat)
    View viewChat;
    @Bind(R.id.iv_more_btn)
    View iv_more_btn;
    @Bind(R.id.et_chat_bottom_chat_content)
    EditText etChatContent;
    @Bind(R.id.btn_chat_bottom_chat_send)
    View btnChatSend;
    @Bind(R.id.rlv_chat_bottom_chat)
    RecyclerView rlvChatItems;
    @Bind(R.id.voice_to_text_im)
    ImageView voice_to_text_im;
    //接诊按钮
    @Bind(R.id.visiting_rl)
    RelativeLayout visiting_rl;
    private PrescriptionData prescriptionData = new PrescriptionData();
    private int visitType;
    private boolean uploadImg = false;
    private String hospital = "0";
    public static String userPhoto;

    @OnClick(R.id.visiting_rl) void visiting(){
        //接诊接口调用
        //我的接待
        if (visitType==3) {
            ServiceProvider.adoptQuestion(doctorId, questionId, new Subscriber<CommonRspEntity>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onNext(CommonRspEntity commonRspEntity) {
                    if (commonRspEntity != null && commonRspEntity.getCode() == ConsultConstants.CODE_REQUEST_SUCCESS) {
                        setBottomView();
                    } else {
                        ToastUtils.shortToast("接诊失败");
                    }
                }
            });
            //未指定接待
        }else if(visitType==4){
            ServiceProvider.obtainQuestion(doctorId, questionId, new Subscriber<CommonRspEntity>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onNext(CommonRspEntity commonRspEntity) {
                    if (commonRspEntity != null && commonRspEntity.getCode() == ConsultConstants.CODE_REQUEST_SUCCESS) {
                        setBottomView();
                    } else {
                        ToastUtils.shortToast("接诊失败");
                    }
                }
            });
        }

    }
    //不接诊
    @Bind(R.id.no_visiting_tv)
    TextView no_visiting_tv;
    @OnClick(R.id.no_visiting_tv) void onVisiting(){
        finish();
    }
    //接诊&不接诊布局
    @Bind(R.id.fl_sumup_answer)
    LinearLayout fl_sumup_answer;
    //处方
    @Bind(R.id.prescription_rl)
    RelativeLayout prescription_rl;
//    @Bind(R.id.refresh_view)
//    SwipeRefreshLayout refresh_view;




    public static final int REQUEST_CODE_SUM_UP = 0; //总结 request code
    public static final int REQUEST_CODE_CHANGE_DEPARTMENT = 1;//纠正科室 request code
    public static final int REQUEST_CODE_FAST_REPLY = 2; //快捷回复 request code
    public static final int ADD_PRESCRIPTION_CODE = 3; //总结 request code


    private static final String STR_ITEM_PRESCRIPTION = "开处方";
    private static final String STR_ITEM_FAST_REPLY = "快捷回复";
    private static final String STR_ITEM_SUM_UP = "问诊总结";
    private static final String STR_ITEM_MODIFY_SUM_UP = "修改总结";
    private static final String STR_ITEM_VOICE_TO_TEXT = "语音转文字";
    private static final String STR_ITEM_PICTURE = "图片";

    //stone 修改 2017年12月22日09:58:12 根据返回的内容来判断是否总结过 兼容之前的版本(用来判断是否总结过)
    private static final String STR_SUM_UP_TITLE = "问题分析";
    private static final String STR_SUM_UP_TITLE2 = "症状内容";
    private static final String STR_SUM_UP_TITLE3 = "问题总结";

    //stone
    private static final String PARAM_QID = "PARAM_QID";//问题id
    private static final String PARAM_PATIENT_ID = "PARAM_PATIENT_ID";//患者id
    private static final String PARAM_PATIENT_NAME = "PARAM_PATIENT_NAME";//患者名
    private static final String VISIT_END = "VISIT_END"; //是否就诊结束
    private static final String VISIT_TYPE = "VISIT_TYPE"; //是否就诊结束


    //聊天list的adapter
    private OnlineConsultChatAdapter chatAdapter;
    //聊天界面底部功能list的adapter
    private ChatBottomAdapter bottomAdapter;
    //标记聊天状态 默认聊天开始
//    private int chatState = CHAT_STATE_BEGIN;

    private int errorCode;


    private String doctorId = YMUserService.getCurUserId();//医生id stone
    private String questionId;//问题id stone
    private String patientId;//患者id stone
    private String patientName;//患者名 stone
    int seq = 0;

    private boolean isClosed;//stone 问题是否已关闭
    private boolean isStatus;//是否接诊状态 //接诊状态 1已接诊 0未接诊  stone

    private boolean isError;

    private Set<String> recvMsgIds = new HashSet<String>();
    private Set<String> sendSuccessIds = Collections.synchronizedSet(new HashSet<String>());



    private int mFirstPos = -1;//第一次展示的位置
    private boolean mIsNoticeShowed;//留言提示是否添加过了 stone


    private long mEndTime;//离线留言结束时间 stone





    /**
     *
     * @param context
     * @param qid 问题ID
     * @param patientId 患者ID
     * @param patientName 患者姓名
     * @param visit_end 是否就诊结束<默认false>
     * @param visitType 接诊接口调用用判断标识
     */
    //stone 历史回复 跳转 添加超时总结
    public static void startActivity(Context context, String qid, String patientId, String patientName, boolean visit_end,int visitType) {
        Intent intent = new Intent(context, PatientChatDetailActivity.class);
        intent.putExtra(PARAM_QID, qid);
        intent.putExtra(PARAM_PATIENT_ID, patientId);
        intent.putExtra(PARAM_PATIENT_NAME, patientName);
        intent.putExtra(VISIT_END, visit_end);
        intent.putExtra(VISIT_TYPE, visitType);
        context.startActivity(intent);
    }


    @Override
    public void onConnectError(Exception e) {
        L.d("Connect Error");
    }

    @Override
    public void onChatMsg(ChatMsg chatMsg) {
        L.d("Msg");
        handleRecvMsg(chatMsg);
    }

    @Override
    public void onChatMsgRead(String msgId, String qid) {
        L.d("Msg");
    }


    @Override
    public void onChatMsgReceived(String msgId) {
        // ToastUtils.shortToast("Msg send success,id:" + msgId);
        if (uploadImg){
            ArrayList<String> imgsPath = new ArrayList<>();
            imgsPath.add(mHeadIconPath);
            final List<OnlineConsultChatEntity> entityList = new ArrayList<OnlineConsultChatEntity>();
            OnlineConsultChatEntity entity = new OnlineConsultChatEntity();
            entity.setType(ConsultChatEntity.TYPE_ONLY_IMG);
            entity.setImgUrls(imgsPath);
            entity.setMsg_type(ConsultChatEntity.MSG_TYPE_SEND);
            entityList.add(entity);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    chatAdapter.addData(entityList);
                    chatAdapter.notifyDataSetChanged();
                    rlvChat.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
                }
            });

//            rlvChat.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
            uploadImg = false;
        }else {
            sendSuccessIds.add(msgId);
            updateMsgSendState(msgId);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_online_chat;
    }

    @Override
    protected void initView() {
        voice_to_text_im.setVisibility(View.GONE);
        rlvChatItems.setVisibility(View.GONE);
        fb = FinalBitmap.create(this, false);
        btnChatSend.setOnClickListener(this);
        //stone
        //stone 标题为患者名,推送过来的没有患者名就用 即时问答详情
        if (getIntent() != null) {
            patientName = getIntent().getStringExtra(PARAM_PATIENT_NAME);
            if (TextUtils.isEmpty(patientName)) {
                patientName = getString(R.string.online_consultation_detail);
            }
        }
        titleBarBuilder.setTitleText(patientName);
        chatAdapter = new OnlineConsultChatAdapter(this,true, new MyCallBack() {

            @Override
            public void onClick(Object data) {
                loadMsgList();
            }
        });

        rlvChat.setAdapter(chatAdapter);
        rlvChat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //initBottomLayout();
        etChatContent.setFilters(new InputFilter[]{new InputFilter() {
            Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                    Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Matcher emojiMatcher = emoji.matcher(source);
                if (emojiMatcher.find()) {
                    ToastUtils.shortToast("不支持输入表情");
                    return "";
                }
                return null;
            }
        }});

        mClipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        PromptViewHelper pvHelper = new PromptViewHelper(PatientChatDetailActivity.this);
        pvHelper.setPromptViewManager(new ChatPromptViewManager(PatientChatDetailActivity.this, new String[]{"粘贴"}, Location.TOP_LEFT));
        pvHelper.addPrompt(etChatContent);
        pvHelper.setOnItemClickListener(new PromptViewHelper.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String resultString = "";
                // 检查剪贴板是否有内容
                if (!mClipboardManager.hasPrimaryClip()) {
                    Toast.makeText(PatientChatDetailActivity.this,
                            "粘贴内容为空", Toast.LENGTH_SHORT).show();
                } else {
                    ClipData clipData = mClipboardManager.getPrimaryClip();
                    int count = clipData.getItemCount();

                    for (int i = 0; i < count; ++i) {

                        ClipData.Item item = clipData.getItemAt(i);
                        CharSequence str = item
                                .coerceToText(PatientChatDetailActivity.this);

                        resultString += str;
                    }

                }
                Editable editable = etChatContent.getEditableText();
                editable.insert(etChatContent.getSelectionStart(), resultString);
            }
        });


        //聊天底部的选项卡 问诊总结和快捷回复
        bottomAdapter = new

                ChatBottomAdapter(this);
        bottomAdapter.setOnItemClickListener(this);
        rlvChatItems.setLayoutManager(new

                GridLayoutManager(this, 4));
        rlvChatItems.setAdapter(bottomAdapter);

        //初始化底部功能
        initBottomItems();


        //stone
        WebSocketRxBus.registerWebSocketChatMagListener(new EventSubscriber<ChatMsg>()

        {
            @Override
            public void onNext(final Event<ChatMsg> chatMsgEvent) {

                if (null == chatMsgEvent) {
                    return;
                }
                final ChatMsg chatMsg = chatMsgEvent.getData();
                if (null == chatMsg || chatMsg.getBody() == null) {
                    return;
                }
                if (ChatMsg.RECV_MSG_TYPE_NEW_NOTICE == chatMsg.getBody().getType()) {

                    YMApplication.applicationHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String string = (chatMsg != null && chatMsg.getBody() != null && chatMsg.getBody().getContent() != null
                                    && !TextUtils.isEmpty(chatMsg.getBody().getContent().getText()))
                                    ? chatMsg.getBody().getContent().getText() : getString(R.string.messagesetting_notice2);

                            OnlineConsultChatEntity entity = OnlineConsultChatEntity.newInstanceText(
                                    string, ConsultChatEntity.MSG_TYPE_SEND);
                            chatAdapter.addData(entity);
                            chatAdapter.notifyDataSetChanged();
                            rlvChat.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
                        }
                    }, 20);


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                    stone 添加忙线提示
                    OnlineConsultChatEntity entity = OnlineConsultChatEntity.newInstanceText(
                            (chatMsg.getBody().getContent() != null && TextUtils.isEmpty(chatMsg.getBody().getContent().getText())) ? getString(R.string.messagesetting_notice2) : chatMsg.getBody().getContent().getText(), ConsultChatEntity.MSG_TYPE_SEND);
                    chatAdapter.addData(entity);
                    chatAdapter.notifyDataSetChanged();
                    rlvChat.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
                        }
                    });
                }
            }
        }, this);
        prescription_rl.setOnClickListener(PatientChatDetailActivity.this);
//        refresh_view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                ToastUtils.longToast("刷新");
//            }
//        });
    }

    @Override
    protected void initData() {
        if (doctorId != null && getIntent() != null) {
            questionId = getIntent().getStringExtra(PARAM_QID);
            patientId = getIntent().getStringExtra(PARAM_PATIENT_ID);
            isClosed = getIntent().getBooleanExtra(VISIT_END, false);
            visitType = getIntent().getIntExtra(VISIT_TYPE,0);
        }
        //防止推送进入同一个详情页面
        if (YMApplication.sQuestionId != null) {
            YMApplication.sQuestionId.add(0, questionId);
        }


        loadMsgList();
        readQuestionMsg();
    }

    private void handleSetMessageBoard(MessageBoardBean bean) {
        //1 离线 2 夜间
        if (OFFLINE.equals(bean.getType())) {
            mOfflineOpened = OPENED.equals(bean.getIsopen());
            YMApplication.mOfflineMessage = bean.getMessage();
            //stone
            mEndTime = Long.parseLong(bean.getLeft_time()) * 1000 + System.currentTimeMillis();
//            mEndTime = Long.parseLong("20000") + System.currentTimeMillis();
        } else if (NIGHT.equals(bean.getType())) {
            mNightOpend = OPENED.equals(bean.getIsopen());
            YMApplication.mNightMessage = bean.getMessage();

            //通知
            YmRxBus.notifyNightModeChanged(mNightOpend);
        }
    }


    private void initBottomLayout() {
        //TODO 接诊是否结束标识
        if (isClosed) {
            //如果关闭问题1
            fl_sumup_answer.setVisibility(View.GONE);
            rlvChatItems.setVisibility(View.GONE);
            etChatContent.setEnabled(false);
        }else {
            if (isStatus){
                //未关闭问题已接诊
                rlvChatItems.setVisibility(View.VISIBLE);
                etChatContent.setEnabled(true);
                fl_sumup_answer.setVisibility(View.GONE);
                webConnect();
            }else{
                //未关闭问题未接诊
                viewChat.setVisibility(View.GONE);
                fl_sumup_answer.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void onClick(View v) {
        if (!isAllowedOperation()) {
            return;
        }
        switch (v.getId()) {
//            case R.id.iv_more_btn:
//                //更多
//                rlvChatItems.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (rlvChatItems.getVisibility() == View.GONE) {
//                            rlvChatItems.setVisibility(View.VISIBLE);
//                        } else {
//                            rlvChatItems.setVisibility(View.GONE);
//                        }
//                    }
//                }, 200);
//
//                break;
            case R.id.btn_chat_bottom_chat_send:
                //聊天发送
                KeyBoardUtils.closeKeyboard(etChatContent);
                if (!isClosed) {
                    sendText(etChatContent.getText().toString());
                }
                break;
            case R.id.prescription_rl:
                startActivity(new Intent(this,PrescriptionDetailActivity.class).
                        putExtra(Constants.KEY_ID,prescriptionData.getPrescription_id()));
                break;
        }
    }

    //初始化聊天状态下底部按钮  总结+快捷回复
    private void initBottomItems() {
        if (bottomAdapter != null) {
            List<ChatBottomItemEntity> itemEntityList = new ArrayList<>();
            ChatBottomItemEntity bottomItemEntity = null;

            //添加语音转文字 stone
            bottomItemEntity = new ChatBottomItemEntity(STR_ITEM_VOICE_TO_TEXT, true,
                    R.drawable.chat_bottom_item_voice_to_text, R.drawable.chat_bottom_item_voice_to_text);
            itemEntityList.add(bottomItemEntity);

            //添加图片 stone
            bottomItemEntity = new ChatBottomItemEntity(STR_ITEM_PICTURE, true,
                    R.drawable.chat_bottom_item_picture, R.drawable.chat_bottom_item_picture);
            itemEntityList.add(bottomItemEntity);


            //添加快捷回复 stone
            bottomItemEntity = new ChatBottomItemEntity(STR_ITEM_FAST_REPLY, true,
                    R.drawable.chat_bottom_item_fast_reply, R.drawable.chat_bottom_item_fast_reply);
            itemEntityList.add(bottomItemEntity);

            //添加开处方 stone 是否是首次患者
//            boolean isEnable = false;
//            if (!TextUtils.isEmpty(hospital)&&"1".equals(hospital)) {
//                isEnable = true;
//            }
            bottomItemEntity = new ChatBottomItemEntity(STR_ITEM_PRESCRIPTION, true,
                    R.drawable.chat_bottom_item_prescription, R.drawable.chat_bottom_item_unprescription);
            itemEntityList.add(bottomItemEntity);
            //问诊总结 或者 修改总结
//            if (chatState == CHAT_STATE_ALREAD_SUM_UP || chatState == CHAT_STATE_ABLE_SUM_UP) {
//            bottomItemEntity = new ChatBottomItemEntity(chatState == CHAT_STATE_ABLE_SUM_UP ? STR_ITEM_SUM_UP : STR_ITEM_MODIFY_SUM_UP, true,
//                    R.drawable.chat_bottom_item_sum_up_enable, R.drawable.chat_bottom_item_sum_up_disable);
//            itemEntityList.add(bottomItemEntity);
//            tv_sumup_answer.setText(chatState == CHAT_STATE_ABLE_SUM_UP ? STR_ITEM_SUM_UP : STR_ITEM_MODIFY_SUM_UP);
//            }


            bottomAdapter.setData(itemEntityList);
            bottomAdapter.notifyDataSetChanged();
        }
    }



    //TODO 底部按钮标签
    @Override
    public void onItemClick(ChatBottomItemEntity entity) {
        if (!isAllowedOperation()) {
            return;
        }
        //stone 底部就只有总结+快捷回复
        if (entity.getText().equals(STR_ITEM_PRESCRIPTION)) {
            //跳转到处方页面
            startActivityForResult(new Intent(PatientChatDetailActivity.this,PrescriptionActivity.class).
                    putExtra(Constants.INTENT_KEY_NAME,prescriptionData.getPatientName()).
                    putExtra(Constants.INTENT_KEY_SEX,prescriptionData.getPatientSex()).
                    putExtra(Constants.INTENT_KEY_YEAR,prescriptionData.getPatient_age_year()).
                    putExtra(Constants.INTENT_KEY_MONTH,prescriptionData.getPatient_age_month()).
                    putExtra(Constants.INTENT_KEY_DAY,prescriptionData.getPatient_age_day()).
                    putExtra(Constants.INTENT_KEY_DEPARTMENT,prescriptionData.getDepartment()).
                    putExtra(Constants.INTENT_KEY_UID,prescriptionData.getUid()).
                    putExtra(Constants.INTENT_KEY_TIME,prescriptionData.getTime()).
                    putExtra(Constants.INTENT_KEY_QID,questionId),ADD_PRESCRIPTION_CODE);

        } else if (entity.getText().equals(STR_ITEM_FAST_REPLY)) {
//            //快捷回复 stone
            startActivityForResult(new Intent(YMApplication.getAppContext(),
                    IMFastReplyActivity.class), REQUEST_CODE_FAST_REPLY);
        } else if (entity.getText().equals(STR_ITEM_VOICE_TO_TEXT)) {
//            //语音 stone
            KDXFUtils.start(this, mRecognizerDialogListener);
            StatisticalTools.eventCount(PatientChatDetailActivity.this, "voicetotext");
        } else if (entity.getText().equals(STR_ITEM_PICTURE)) {
//            //图片 stone
            // 设置layout在PopupWindow中显示的位置
            if (!PermissionUtils.checkPermission(this, Manifest.permission.CAMERA)) {
                CommonUtils.permissionRequestDialog(this, "请先授予照相机(Camera)权限", 555);
            }else{
                menuWindow = new SelectPicPopupWindow(this,
                        itemsOnClick);
                // 显示窗口
                menuWindow.showAtLocation(main, Gravity.BOTTOM
                        | Gravity.CENTER_HORIZONTAL, 0, 0);

                //添加遮罩 stone
                YMOtherUtils.addScreenBg(menuWindow, this);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SUM_UP) {
//            loadMsgList();
        } else if (requestCode == REQUEST_CODE_CHANGE_DEPARTMENT && data != null) {
            boolean isSuccess = data.getBooleanExtra(ConsultQueSwitchDpartAcitivity.PARAM_RESULT, false);
            if (isSuccess) {
                onBackPressed();
            }
        } else if (requestCode == REQUEST_CODE_FAST_REPLY && data != null) {
            //快捷回复 stone
            String s = data.getStringExtra(Constants.KEY_VALUE);
            etChatContent.setText(s);
//            sendText(s);
        } else DLog.i("300", "onActivityResult==" + requestCode);
        if (requestCode == Constants.REQUESTCODE_CHOOSE_CAMERA && resultCode == RESULT_OK) {
            Intent intent = new Intent(this, ClipPictureActivity.class);
            DLog.i("300", "REQUESTCODE_CHOOSE_CAMERA" + origUri.getPath());
            intent.putExtra("path", origUri.getPath());
            startActivityForResult(intent, Constants.REQUESTCODE_MODIFY_FINISH);
        } else if (requestCode == Constants.REQUESTCODE_MODIFY_FINISH && resultCode == RESULT_OK) {
            DLog.i("300", " REQUESTCODE_MODIFY_FINISH resultCode==" + resultCode);
            if (data != null) {
                final String path = data.getStringExtra("path");
                // Bitmap b = BitmapFactory.decodeFile(path);
                // iv_head.setImageBitmap(b);
                if (path != null && !path.equals("")) {
                    if (NetworkUtil.isNetWorkConnected()) {
                        mHeadIconUploadingPath = path;
//                       YMUserService.getPerInfo().getData().setPhoto(path);
//                        YMApplication.getLoginInfo().getData().setPhoto(path);


//                        b.display(iv_head, mHeadIconUploadingPath);
                        mHeadIconPath = mHeadIconUploadingPath;
//                        ToastUtils.shortToast(
//                                "图片上传成功,发送socket");

                        if (!TextUtils.isEmpty(mHeadIconPath)) {
                            uploadImg = true;

                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("type","image");
                                jsonObject.put("file",mHeadIconPath);
                                sendText(jsonObject.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
//                        HttpMultipartPost post = new HttpMultipartPost(OnlineChatDetailActivity.this,imgsPath ,
//                                ConsultConstants.METHOD_UOLOADIMAGE_PATH,
//                                handler, 100);
//                        post.isYixian = true;
//                        post.execute();
                    } else {
                        ToastUtils.shortToast(
                                "网络连接失败,图片不能上传,请联网重试");
                    }
                }
            }else{
                ToastUtils.shortToast(
                        "上传失败");
            }
        } else if(requestCode == ADD_PRESCRIPTION_CODE && data != null){
            if (!TextUtils.isEmpty(data.getStringExtra("id"))){
                prescription_rl.setVisibility(View.VISIBLE);
                prescriptionData.setPrescription_id(data.getStringExtra("id"));
            }
        }
    }

    //获取消息列表 stone
    private void loadMsgList() {
        if (questionId == null || questionId.equals("") || doctorId == null || doctorId.equals("")) {
            return;
        } else {
          ServiceProvider.getPatientQuestionMsgList(doctorId, questionId, new Subscriber<OnlineQuestionMsgListRspEntity>()
//            ServiceProvider.getOnlineQuestionMsgList("13996218", "9315", new Subscriber<OnlineQuestionMsgListRspEntity>()
            {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(OnlineQuestionMsgListRspEntity questionMsgListRspEntity) {
                    if (questionMsgListRspEntity == null || questionMsgListRspEntity.getCode() != ConsultConstants.CODE_REQUEST_SUCCESS || questionMsgListRspEntity.getData() == null) {
                        return;
                    } else {
                        // stone 问题是否关闭 关闭了就直接展示成历史回复中一样
//                        isClosed = questionMsgListRspEntity.getData().getClosed() == 1;
                        isClosed = false;
//                        if (questionMsgListRspEntity.getData().getStatus()==1){
//                            isStatus = true;
//                        }else{
//                            isStatus = false;
//                        }
                        isStatus = true;
                        //数据
                        hospital = questionMsgListRspEntity.getData().getHospital();
                        List<OnlineConsultChatEntity> entityList = new ArrayList<OnlineConsultChatEntity>();
                        List<OnlineQuestionMsgListRspEntity.DataBean.ListBean> list = questionMsgListRspEntity.getData().getChat();
                        prescriptionData.initData(questionMsgListRspEntity.getData());
                        if (!questionMsgListRspEntity.getData().getPrescription_id().equals("0")){
                            prescription_rl.setVisibility(View.VISIBLE);
                        }
                        if (list != null &&list.size()!=0) {
                            for (int i = 0; i < list.size(); i++) {
                                //stone 过滤掉type==4的消息
                                if (list.get(i).getContent() != null
                                        && list.get(i).getContent().size() > 0
                                        && !TextUtils.isEmpty(list.get(i).getContent().get(0).getType())
                                        && !list.get(i).getContent().get(0).getType().equals("4")) {
                                    OnlineConsultChatEntity entity = new OnlineConsultChatEntity();
                                    if (i==list.size()-1){
                                        entity.setPatientTitleData(questionMsgListRspEntity.getData());
                                        OnlineConsultChatEntity lastEntity = new OnlineConsultChatEntity();
                                        lastEntity.setDataBean(list.get(i));
                                        entityList.add(lastEntity);
                                    }else{
                                        entity.setDataBean(list.get(i));
                                        if (TextUtils.isEmpty(userPhoto)&&
                                                entity.getMsg_type()==ConsultChatEntity.MSG_TYPE_RECV){
                                            userPhoto = entity.getDataBean().getUser_photo();
                                        }
                                    }
                                    entityList.add(entity);
                                }
                            }

                        }else {
                            OnlineConsultChatEntity entity = new OnlineConsultChatEntity();
                            entity.setPatientTitleData(questionMsgListRspEntity.getData());
                            entityList.add(entity);
                        }

                        if (isClosed){
                            OnlineConsultChatEntity closedEntity = new OnlineConsultChatEntity();
                            closedEntity.setClosedData(questionMsgListRspEntity.getData());
                            entityList.add(0,closedEntity);
                        }
//                        //stone 添加关闭留言提示 打开+不是第一次加载+之前回复过已经展示过
//                        if (mIsNoticeOpen && !mIsFirstLoad && mIsNoticeShowed) {
//                            ConsultChatEntity consultChatEntity = new ConsultChatEntity();
//                            consultChatEntity.setType(TYPE_CLOSE_NOTICE);
//                            consultChatEntity.setMsg_type(ConsultChatEntity.MSG_TYPE_SEND);
//                            if (mFirstPos == -1) {
//                                entityList.add(consultChatEntity);
//                                mFirstPos = entityList.size() - 1;
//                            } else {
//                                entityList.add(mFirstPos, consultChatEntity);
//                            }
//                        }
//                        mIsFirstLoad = false;


                        //逆序处理
                        Collections.reverse(entityList);
                        chatAdapter.setData(entityList);
                        chatAdapter.notifyDataSetChanged();
                        if (chatAdapter.getDatas().size()!=0) {
                            rlvChat.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
                        }
                        initBottomLayout();
                        initBottomItems();
                    }
                }
            });
        }
    }

    //stone 获取发送的消息数目 不论成功与失败
    private int getSendMsgCount() {
        int ret = 0;
        List<OnlineConsultChatEntity> list = chatAdapter.getDatas();
        Iterator<OnlineConsultChatEntity> it = list.iterator();
        while (it.hasNext()) {
            OnlineConsultChatEntity item = it.next();
            if (item.getMsg_type() == ConsultChatEntity.MSG_TYPE_SEND) {
                ret++;
            }
        }
        return ret;
    }

    //stone 获取发送成功的消息数目
    private int getSendMsgSuccessCount() {
        int ret = 0;
        List<OnlineConsultChatEntity> list = chatAdapter.getDatas();
        Iterator<OnlineConsultChatEntity> it = list.iterator();
        while (it.hasNext()) {
            OnlineConsultChatEntity item = it.next();
            //stone 添加个判断 发送成功才计数
            if (item.getMsg_type() == ConsultChatEntity.MSG_TYPE_SEND
                    && item.getSendState() == ConsultChatEntity.SEND_STATE_SUCCESS) {
                ret++;
            }
        }
        return ret;
    }

    //stone 收到消息
    private void handleRecvMsg(ChatMsg chatMsg) {
        if (recvMsgIds.contains(chatMsg.getId()) || chatMsg.getBody().getQid() == null ||
                !chatMsg.getBody().getQid().equals(questionId) || isError) {
            return;
        }
        recvMsgIds.add(chatMsg.getId());
        // TODO: 2018/5/29 websocket新版本 stone
//        if (recvMsgIds.contains(chatMsg.getMsg_id()) || String.valueOf(chatMsg.getBody().getQid()) == null ||
//                !String.valueOf(chatMsg.getBody().getQid()).equals(questionId) || isError) {
//            return;
//        }
//        recvMsgIds.add(String.valueOf(chatMsg.getMsg_id()));

        OnlineConsultChatEntity entity = null;
        if (chatMsg.getBody().getType() == ChatMsg.RECV_MSG_TYPE_NORMAL) {
            //直接刷新列表
//            loadMsgList();
            chatMsg.getBody().setUser_photo(TextUtils.isEmpty(userPhoto)?"":userPhoto);
            if (chatMsg.getBody().getContent().getType() == ContentType.text) {
                entity = OnlineConsultChatEntity.newInstanceText(chatMsg.getBody().getContent().getText(), ConsultChatEntity.MSG_TYPE_RECV);
            } else if (chatMsg.getBody().getContent().getType() == ContentType.image) {
                List<String> imgUrls = new ArrayList<>();
                imgUrls.add(chatMsg.getBody().getContent().getFile());
                entity = OnlineConsultChatEntity.newInstanceImgs(imgUrls);
                entity.setMsg_type(ConsultChatEntity.MSG_TYPE_RECV);
            }
            if (entity != null) {
                chatAdapter.addData(entity);
                chatAdapter.notifyDataSetChanged();
                rlvChat.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
            }
        } else {
            switch (chatMsg.getBody().getType()) {
                case ChatMsg.RECV_MSG_TYPE_ACCEPT_OVER_TIME:
                case ChatMsg.RECV_MSG_TYPE_DOCTOR_FORBID:
                case ChatMsg.RECV_MSG_TYPE_UN_ADOPT:
                case ChatMsg.RECV_MSG_TYPE_CLOSED:
                case ChatMsg.RECV_MSG_TYPE_QUESTION_OVER_TIME:
                    //loadMsgList();
                    errorCode = chatMsg.getBody().getType();
                    isError = true;
                    if (chatMsg.getBody().getContent().getText() != null) {
                        ToastUtils.shortToast(chatMsg.getBody().getContent().getText());
                    }
                    break;
            }
        }

    }


    //发消息
    private void sendText(String text) {
        if (!isAllowedOperation()) {
            return;
        }

        //stone 提前
        if (!isConnected()) {
            connect(ConsultConstants.WEBSOCKET_ADDRESS, doctorId);
            //stone 长时间停留在页面需要重新连接 此时不发消息 只连接 给个提示给用户
            ToastUtils.shortToast("重连中...");
            return;
        }

        if (text == null) {
            ToastUtils.shortToast("回复内容不能为空");
            return;
        }
        text = text.trim();
        if (text.equals("")) {
            ToastUtils.shortToast("回复内容不能为空");
            return;
        }


        sendChatMsg(doctorId, patientId, (++seq) + "", questionId, text);
        if (!uploadImg){
            final OnlineConsultChatEntity entity = OnlineConsultChatEntity.newInstanceText(text, ConsultChatEntity.MSG_TYPE_SEND);
            entity.setSendState(ConsultChatEntity.SEND_STATE_IN_SENDING);
            entity.setSendId(seq + "");
            chatAdapter.addData(entity);

            //stone 添加关闭留言提示
            //离线留言倒计时结束了 需要补弹出系统提示
            if (!DateUtils.isCurrentInTimeScope(6, 0, 22, 0)) {
                mIsNoticeOpen = (mOfflineOpened && mEndTime > System.currentTimeMillis()) || mNightOpend;
            } else {
                mIsNoticeOpen = mOfflineOpened && mEndTime > System.currentTimeMillis();
            }

            if (mIsNoticeOpen && !mIsNoticeShowed) {
                OnlineConsultChatEntity consultChatEntity = new OnlineConsultChatEntity();
                consultChatEntity.setType(TYPE_CLOSE_NOTICE);
                consultChatEntity.setMsg_type(ConsultChatEntity.MSG_TYPE_SEND);
                if (mFirstPos == -1) {
                    chatAdapter.addData(consultChatEntity);
                    mFirstPos = chatAdapter.getDatas().size() - 1;
                } else {
                    chatAdapter.addDataForPos(mFirstPos, consultChatEntity);
                }
                mIsNoticeShowed = true;
            }

            chatAdapter.notifyDataSetChanged();


            rlvChat.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!sendSuccessIds.contains(entity.getSendId())) {
                        entity.setSendState(ConsultChatEntity.SEND_STATE_FAILED);
                        chatAdapter.notifyDataSetChanged();
                    } else {
                        //stone 注释掉
//                    if (getSendMsgCount() == 4) {
//                        ToastUtils.shortToast("问诊完成后，认真填写问诊总结能得到更高绩效哦");
//                    }
//                    if (mTop.getVisibility() != View.GONE) {
//                        mTop.setVisibility(View.GONE);
//                    }
                    }
                }
            }, ConsultConstants.CHAT_SEND_FAILED_TIME_LIMITED);
            rlvChat.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
            etChatContent.setText("");
//        chatState = getChatState(chatAdapter.getDatas());
//        refreshChatState();
        }
    }

    //stone 是否允许操作
    private boolean isAllowedOperation() {
        switch (errorCode) {
            case ChatMsg.RECV_MSG_TYPE_ACCEPT_OVER_TIME:
            case ChatMsg.RECV_MSG_TYPE_DOCTOR_FORBID:
            case ChatMsg.RECV_MSG_TYPE_UN_ADOPT:
            case ChatMsg.RECV_MSG_TYPE_CLOSED:
            case ChatMsg.RECV_MSG_TYPE_QUESTION_OVER_TIME:
                ToastUtils.shortToast(ChatMsg.RECV_ERROR_MSG_TIP.get(errorCode));
                rlvChat.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
                break;
        }
        return !isError;
    }

    //TODO 链接socket
    private void webConnect() {
        if (!isConnected()) {
            connect(ConsultConstants.WEBSOCKET_ADDRESS, doctorId);
        }
    }

    //stone 跳过
    private void backQuestion() {
        if (ClickUtil.isFastDoubleClick()) {
            return;
        }
        StatisticalTools.eventCount(YMApplication.getAppContext(), ConsultConstants.EVENT_QUESTION_SKIP);
        ServiceProvider.backQuestion(doctorId, questionId, new Subscriber<BackQuestionRspEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(BackQuestionRspEntity backQuestionRspEntity) {
                if (backQuestionRspEntity != null && backQuestionRspEntity.getCode() == ConsultConstants.CODE_REQUEST_SUCCESS) {
                    ToastUtils.shortToast("问题已跳过");
                    onBackPressed();
                } else {
                    ToastUtils.shortToast("问题跳过失败");
                }

            }
        });
    }





    //TODO 消息发送成功
    //刷新发送状态和底部状态 stone
    private void updateMsgSendState(String id) {
        for (int i = chatAdapter.getItemCount() - 1; i >= 0; i--) {
            if (chatAdapter.getDatas().get(i).getMsg_type() == ConsultChatEntity.MSG_TYPE_SEND
                    && !TextUtils.isEmpty(chatAdapter.getDatas().get(i).getSendId())
                    && chatAdapter.getDatas().get(i).getSendId().equals(id)) {
                chatAdapter.getDatas().get(i).setSendState(ConsultChatEntity.SEND_STATE_SUCCESS);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chatAdapter.notifyDataSetChanged();
                    }
                });
                break;
            }
        }

    }

    //阅读消息 stone
    private void readQuestionMsg() {
        ServiceProvider.readQuestion(doctorId, questionId, new Subscriber<CommonRspEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(CommonRspEntity commonRspEntity) {
                if (commonRspEntity != null && commonRspEntity.getCode() == ConsultConstants.CODE_REQUEST_SUCCESS) {
                    // ToastUtils.shortToast("消息阅读成功");
                } else {
                    //ToastUtils.shortToast("消息阅读失败");
                }
            }
        });
    }


    /**
     * @param sequence 服务端消息起始编号
     */
    @Override
    public void onStartChat(int sequence) {
        // TODO: 2017/5/5 会话已建立
    }

    @Override
    protected void onDestroy() {
        KDXFUtils.cancel();

        if (YMApplication.sQuestionId != null && YMApplication.sQuestionId.size() > 0) {
            YMApplication.sQuestionId.remove(0);
        }

        super.onDestroy();

    }


    //TODO 选择本地相册图片上传
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        DLog.i("300", "裁剪" + getIntent().getIntExtra("code", 0));
        int code = intent.getIntExtra("code", -1);
        if (code != 100) {
            return;
        }

        ArrayList<String> list1 = new ArrayList<String>();
        list1.clear();
        ArrayList<String> paths = intent.getStringArrayListExtra("paths");
        if (paths != null && paths.size() > 0) {
            Intent intent2 = new Intent(this, ClipPictureActivity.class);
            DLog.d("approve", paths.get(0));
            intent2.putExtra("path", paths.get(0));
            startActivityForResult(intent2, Constants.REQUESTCODE_MODIFY_FINISH);
        } else {
            ToastUtils.shortToast("图片选取失败");
            return;
        }
    }


    /**
     * 听写监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            L.d("语音", results.getResultString());
            String text = KDXFUtils.printResult(results);
            if (isLast) {
                etChatContent.append(text);
                if (YMApplication.getFirstUseKdxf()) {
                    YMApplication.setFirstUseKdxf(false);
                    ToastUtils.longToast("记得点击发送按键回复患者哟~");
                }
            }
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
//            ToastUtils.shortToast(error.getPlainDescription(true));
        }

    };


    //图片

    // 为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        Intent intent;

        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.item_popupwindows_camera:
                    // 照片命名
                    // String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                    // .format(new Date());
                    // String origFileName = "osc_3" + timeStamp + ".jpg";
                    // origUri = Uri.fromFile(new File(FILE_SAVEPATH,
                    // origFileName));
                    origUri = new File(PathUtil.getInstance().getImagePath(),
                            "osc_" + System.currentTimeMillis() + ".jpg");
                    origUri.getParentFile().mkdirs();
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(origUri));
                    startActivityForResult(intent, Constants.REQUESTCODE_CHOOSE_CAMERA);
                    // startActivity(intent);
                    break;
                case R.id.item_popupwindows_Photo:
                    intent = new Intent(PatientChatDetailActivity.this, PhotoWallActivity.class);
                    startActivity(intent);
                    YMApplication.photoTag = Constants.PATIENT_CHAT;
                    YMApplication.isSelectMore = false;
                    break;
                case R.id.item_popupwindows_cancel:
                    menuWindow.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void beforeViewBind() {
        super.beforeViewBind();

        WebSocketRxBus.registerChatStartedListener(new EventSubscriber<Integer>() {
            @Override
            public void onNext(Event<Integer> chatMsgEvent) {
                onStartChat(chatMsgEvent.getData());
            }
        }, this);
        WebSocketRxBus.registerWebSocketChatMagListener(new EventSubscriber<ChatMsg>() {
            @Override
            public void onNext(final Event<ChatMsg> chatMsgEvent) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onChatMsg(chatMsgEvent.getData());
                    }
                });

            }
        }, this);

        WebSocketRxBus.registerWebSocketChatMagReceivedListener(new EventSubscriber<String>() {
            @Override
            public void onNext(Event<String> chatMsgReceivedEvent) {
                onChatMsgReceived(chatMsgReceivedEvent.getData());
            }
        }, this);

        WebSocketRxBus.registerChatMagReadListener(new EventSubscriber<MsgReadEventBody>() {
            @Override
            public void onNext(Event<MsgReadEventBody> chatMsgReadEvent) {
                onChatMsgRead(String.valueOf(chatMsgReadEvent.getData().getId()), chatMsgReadEvent.getData().getQid());
                // TODO: 2018/5/29 websocket新版本 stone
//                onChatMsgRead(String.valueOf(chatMsgReadEvent.getData().getMsg_id()), String.valueOf(chatMsgReadEvent.getData().getQid()));
            }
        }, this);
    }


    @Override
    public void connect(String webAddress, String userId) {
        if (!isConnected()) {
            YMApplication.getInstance().initWebSocket();
//            YMApplication.getInstance().initWebSocketForOnline();
        }
    }

    public boolean isConnected() {
//        return WSApi.INSTANCE.isConnected();
        return WebSocketApi.INSTANCE.isConnected();
    }

    @Override
    public void sendChatMsg(ChatMsg chatMsg) {
//        WSApi.INSTANCE.sendMsg(chatMsg);
        WebSocketApi.INSTANCE.sendMsg(chatMsg);
    }

    @Override
    public void sendAckMsg(String msgId) {
//        WSApi.INSTANCE.sendMsgAck(msgId);
        WebSocketApi.INSTANCE.sendMsgAck(msgId);

    }

    @Override
    public void sendReadMsg(String msgId, String qid) {
//        WSApi.INSTANCE.sendMsgReadAck(msgId, qid);
        WebSocketApi.INSTANCE.sendMsgReadAck(msgId, qid);
    }


    /**
     * 发送聊天消息
     *
     * @param from    发送人id
     * @param to      接收人id
     * @param msgId   消息id
     * @param qid     问题id
     * @param content 消息内容
     */
    @Override
    public void sendChatMsg(String from, String to, String msgId, String qid, String content) {
//        WSApi.INSTANCE.sendChatMsg(from, to, msgId, qid, content);
        WebSocketApi.INSTANCE.sendChatMsg(from, to, msgId, qid, content);
    }

    /**
     * 发送字符串消息
     *
     * @param msg
     */
    @Override
    public void sendMsg(String msg) {
//        WSApi.INSTANCE.sendMsg(msg);
        WebSocketApi.INSTANCE.sendMsg(msg);
    }

    private void setBottomView(){
        viewChat.setVisibility(View.VISIBLE);
        fl_sumup_answer.setVisibility(View.GONE);
        rlvChatItems.setVisibility(View.VISIBLE);
        webConnect();
    }

}