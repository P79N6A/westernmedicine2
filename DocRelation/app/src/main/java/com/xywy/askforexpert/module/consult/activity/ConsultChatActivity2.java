package com.xywy.askforexpert.module.consult.activity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.util.PathUtil;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ClickUtil;
import com.xywy.askforexpert.appcommon.utils.DateUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.certification.MessageBoardBean;
import com.xywy.askforexpert.model.consultentity.BackQuestionRspEntity;
import com.xywy.askforexpert.model.consultentity.ChatBottomItemEntity;
import com.xywy.askforexpert.model.consultentity.CommonRspEntity;
import com.xywy.askforexpert.model.consultentity.ConsultChatEntity;
import com.xywy.askforexpert.model.consultentity.QuestionMsgListRspEntity;
import com.xywy.askforexpert.model.websocket.msg.chatmsg.ChatMsg;
import com.xywy.askforexpert.model.websocket.type.ContentType;
import com.xywy.askforexpert.module.consult.ChatBaseActivity;
import com.xywy.askforexpert.module.consult.ConsultConstants;
import com.xywy.askforexpert.module.consult.ServiceProvider;
import com.xywy.askforexpert.module.consult.adapter.ChatBottomAdapter;
import com.xywy.askforexpert.module.consult.adapter.ConsultChatAdapter;
import com.xywy.askforexpert.module.discovery.medicine.CertificationAboutRequest;
import com.xywy.askforexpert.module.discovery.medicine.IMFastReplyActivity;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;
import com.xywy.askforexpert.module.my.photo.PhotoWallActivity;
import com.xywy.askforexpert.module.my.userinfo.ClipPictureActivity;
import com.xywy.askforexpert.module.websocket.WebSocketRxBus;
import com.xywy.askforexpert.sdk.kdxf.KDXFUtils;
import com.xywy.askforexpert.widget.promptView.ChatPromptViewManager;
import com.xywy.askforexpert.widget.promptView.Location;
import com.xywy.askforexpert.widget.promptView.PromptViewHelper;
import com.xywy.askforexpert.widget.view.SelectPicPopupWindow;
import com.xywy.easeWrapper.utils.ImCacheAsyncTask;
import com.xywy.easeWrapper.utils.ImFileCacheUtil;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.xywy.retrofit.rxbus.Event;
import com.xywy.retrofit.rxbus.EventSubscriber;
import com.xywy.util.KeyBoardUtils;
import com.xywy.util.L;

import net.tsz.afinal.FinalBitmap;

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
import rx.Subscriber;

import static com.xywy.askforexpert.YMApplication.mIsNoticeOpen;
import static com.xywy.askforexpert.appcommon.old.Constants.NIGHT;
import static com.xywy.askforexpert.appcommon.old.Constants.OFFLINE;
import static com.xywy.askforexpert.appcommon.old.Constants.OPENED;
import static com.xywy.askforexpert.model.consultentity.ConsultChatEntity.TYPE_CLOSE_NOTICE;


/**
 * 在线咨询/聊天页 stone  移植yimai (5.3 新功能:1.指定付费的问题直接可以回答,不需要认领,并且不能跳过和转诊)
 * Created by zhangzheng on 2017/4/28.
 */





//public class ConsultChatActivity extends ChatBaseActivity implements View.OnClickListener, ChatBottomAdapter.OnItemClickListener {
//
//    /**
//     * 图片地址
//     */
//    private File origUri;
//    private SelectPicPopupWindow menuWindow;
//
//    private Map<String, String> map = new HashMap<String, String>();
//    private Map<String, String> map2 = new HashMap<String, String>();
//
//    private FinalBitmap fb;
//    private String mHeadIconPath;
//    private String mHeadIconUploadingPath;//上传中图片
//
//
//    private ClipboardManager mClipboardManager;
//
//    private boolean mOfflineOpened, mNightOpend;
//
//    @Bind(R.id.rlv_chat_list)
//    RecyclerView rlvChat;
//
//    @Bind(R.id.main)
//    View main;
//    @Bind(R.id.fl_bottom_adopt)
//    View viewAdopt;
//    @Bind(R.id.iv_chat_bottom_adopt_adopt)
//    ImageView ivAdopt;
//    @Bind(R.id.iv_chat_bottom_adopt_pass)
//    ImageView ivAdoptPass;
//    @Bind(R.id.tv_chat_bottom_adopt_pass)
//    View tvAdoptPass;
//
//    @Bind(R.id.fl_bottom_obtain_question)
//    View viewObtain;
//    @Bind(R.id.iv_chat_bottom_obtain_question)
//    ImageView ivObtain;
//    @Bind(R.id.iv_chat_bottom_obtain_pass)
//    ImageView ivObtainPass;
//
//    //底部聊天
//    @Bind(R.id.fl_bottom_chat)
//    View viewChat;
//    @Bind(R.id.iv_more_btn)
//    View iv_more_btn;
//    @Bind(R.id.et_chat_bottom_chat_content)
//    EditText etChatContent;
//    @Bind(R.id.btn_chat_bottom_chat_send)
//    View btnChatSend;
//    @Bind(R.id.rlv_chat_bottom_chat)
//    RecyclerView rlvChatItems;
//
//    //顶部跳过和转诊 默认展示
//    @Bind(R.id.fl_top)
//    LinearLayout mTop;
//    //stone 我的回复中添加问诊总结
//    @Bind(R.id.fl_sumup_answer)
//    View fl_sumup_answer;
//    @Bind(R.id.tv_sumup_answer)
//    TextView tv_sumup_answer;
//    //stone 问题详情中添加总结超时提示
//    @Bind(R.id.tv_sumup_timeout)
//    View tv_sumup_timeout;
//
//    //底部状态 处理中:认领和跳过 聊天中:已经认领或者已经抢题 问题库:抢题
//    private static final int BOTTOM_TYPE_ADOPT = 0;//认领 处理中
//    private static final int BOTTOM_TYPE_CHAT = 1;//聊天 处理中
//    private static final int BOTTOM_TYPE_OBTAIN = 2;//抢题 这个是自己定义的字段 stone
//
//    //聊天状态 开始 在聊 已经总结过 可以总结
//    private static final int CHAT_STATE_BEGIN = 0;        //聊天开始，医生未输入任何内容
//    private static final int CHAT_STATE_IN_CHAT = 1;      //医生已经发送了消息，不满4条，不能总结
//    private static final int CHAT_STATE_ALREAD_SUM_UP = 2;        //医生已经总结
//    private static final int CHAT_STATE_ABLE_SUM_UP = 3;       //回复达到4条，可以总结
//
//    public static final int REQUEST_CODE_SUM_UP = 0; //总结 request code
//    public static final int REQUEST_CODE_CHANGE_DEPARTMENT = 1;//纠正科室 request code
//    public static final int REQUEST_CODE_FAST_REPLY = 2; //快捷回复 request code
//
//    private int allow_summary_count = 4;//默认是4 医生回复四条能总结
//
//    private static final String STR_ITEM_FAST_REPLY = "快捷回复";
//    private static final String STR_ITEM_SUM_UP = "问诊总结";
//    private static final String STR_ITEM_MODIFY_SUM_UP = "修改总结";
//    private static final String STR_ITEM_VOICE_TO_TEXT = "语音转文字";
//    private static final String STR_ITEM_PICTURE = "图片";
//
//    //stone 修改 2017年12月22日09:58:12 根据返回的内容来判断是否总结过 兼容之前的版本(用来判断是否总结过)
//    private static final String STR_SUM_UP_TITLE = "问题分析";
//    private static final String STR_SUM_UP_TITLE2 = "症状内容";
//    private static final String STR_SUM_UP_TITLE3 = "问题总结";
//
//    //stone
//    private static final String PARAM_QID = "PARAM_QID";//问题id
//    private static final String PARAM_PATIENT_ID = "PARAM_PATIENT_ID";//患者id
//    private static final String PARAM_PATIENT_NAME = "PARAM_PATIENT_NAME";//患者名
//    private static final String PARAM_FROM_HISTORY = "PARAM_FROM_HISTORY"; //是否是历史回复中的问题,问题已关闭
//    private static final String PARAM_TIMEOUT = "PARAM_TIMEOUT"; //是否是超时总结
//    private static final String PARAM_SPECIFY = "PARAM_SPECIFY"; //是否是指定付费
//    private static final String PARAM_IS_FROM_WTK_OR_HISTORY = "PARAM_IS_FROM_WTK_OR_HISTORY"; //是否来自问题库或者历史回复
//
//    private boolean mIsFromWTKOrHistory; //是否来自问题库或者历史回复 true false (因为题目不属于自己,不展示跳过和转诊)
//
//    private boolean mIsObtained;//已经抢题成功啦 stone
//
//    //聊天list的adapter
//    private ConsultChatAdapter chatAdapter;
//    //聊天界面底部功能list的adapter
//    private ChatBottomAdapter bottomAdapter;
//    //标记底部显示认领、抢题、聊天 默认聊天
//    private int bottomType = BOTTOM_TYPE_CHAT;
//    //标记聊天状态 默认聊天开始
//    private int chatState = CHAT_STATE_BEGIN;
//
//    private int errorCode;
//
//
//    private String doctorId = YMApplication.getUUid();//医生id stone
//    private String questionId;//问题id stone
//    private String patientId;//患者id stone
//    private String patientName;//患者名 stone
//    int seq = 0;
//
//    private boolean isClosed;//stone 问题是否已关闭
//
//    private boolean isFromHistory;//stone 来自历史回复 咨询已经结束了
//
//    private boolean isTimeOut;//stone 总结超时
//
//    private boolean isError;
//
//    private Set<String> recvMsgIds = new HashSet<String>();
//    private Set<String> sendSuccessIds = Collections.synchronizedSet(new HashSet<String>());
//
//    //stone 新添加 刚回答完成,等待发送状态改变
//    private boolean mIsWaittingSendingStateChange;
//    private boolean mIsWaittingSending4;//正在发送第四条消息
//    //stone 指定付费问题不能跳过和转诊 并且不需要认领
//    private boolean isSpecified;
//
//
//    private boolean mIsFirstLoad = true;//首次加载列表
//    private int mFirstPos = -1;//第一次展示的位置
//    private boolean mIsNoticeShowed;//留言提示是否添加过了 stone
//
//
//    private long mEndTime;//离线留言结束时间 stone
//    private boolean isConnect;
//
//
//    public static void startActivity(Context context) {
//        context.startActivity(new Intent(context, ConsultChatActivity.class));
//    }
//
//    //stone 咨询中 和 问题库  跳转  添加是否是指定的 isSpecified 指定付费的问题没法跳过和转诊
//    public static void startActivity2(boolean push, boolean is_from_wtk_or_history, Context context, String qid, String patientId, String patientName, boolean isSpecified) {
//        Intent intent = new Intent(context, ConsultChatActivity.class);
//        intent.putExtra(PARAM_IS_FROM_WTK_OR_HISTORY, is_from_wtk_or_history);
//        intent.putExtra(PARAM_QID, qid);
//        intent.putExtra(PARAM_PATIENT_ID, patientId);
//        intent.putExtra(PARAM_PATIENT_NAME, patientName);
//        intent.putExtra(PARAM_FROM_HISTORY, false);
//        intent.putExtra(PARAM_SPECIFY, isSpecified);
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//    }
//
//    //stone 咨询中 和 问题库  跳转  添加是否是指定的 isSpecified 指定付费的问题没法跳过和转诊
//    public static void startActivity(boolean push, boolean is_from_wtk_or_history, Context context, String qid, String patientId, String patientName, boolean isSpecified) {
//        Intent intent = new Intent(context, ConsultChatActivity.class);
//        intent.putExtra(PARAM_IS_FROM_WTK_OR_HISTORY, is_from_wtk_or_history);
//        intent.putExtra(PARAM_QID, qid);
//        intent.putExtra(PARAM_PATIENT_ID, patientId);
//        intent.putExtra(PARAM_PATIENT_NAME, patientName);
//        intent.putExtra(PARAM_FROM_HISTORY, false);
//        intent.putExtra(PARAM_SPECIFY, isSpecified);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//    }
//
//    //stone 咨询中 和 问题库  跳转  添加是否是指定的 isSpecified 指定付费的问题没法跳过和转诊
//    public static void startActivity(boolean is_from_wtk_or_history, Context context, String qid, String patientId, String patientName, boolean isSpecified) {
//        Intent intent = new Intent(context, ConsultChatActivity.class);
//        intent.putExtra(PARAM_IS_FROM_WTK_OR_HISTORY, is_from_wtk_or_history);
//        intent.putExtra(PARAM_QID, qid);
//        intent.putExtra(PARAM_PATIENT_ID, patientId);
//        intent.putExtra(PARAM_PATIENT_NAME, patientName);
//        intent.putExtra(PARAM_FROM_HISTORY, false);
//        intent.putExtra(PARAM_SPECIFY, isSpecified);
//        context.startActivity(intent);
//    }
//
//    //stone 历史回复 跳转 添加超时总结
//    public static void startActivity(boolean is_from_wtk_or_history, Context context, String qid, String patientId, String patientName, boolean is_from_history, boolean isTimeOut) {
//        Intent intent = new Intent(context, ConsultChatActivity.class);
//        intent.putExtra(PARAM_IS_FROM_WTK_OR_HISTORY, is_from_wtk_or_history);
//        intent.putExtra(PARAM_QID, qid);
//        intent.putExtra(PARAM_PATIENT_ID, patientId);
//        intent.putExtra(PARAM_PATIENT_NAME, patientName);
//        intent.putExtra(PARAM_FROM_HISTORY, is_from_history);
//        intent.putExtra(PARAM_TIMEOUT, isTimeOut);
//        context.startActivity(intent);
//    }
//
//
//    @Override
//    public void onConnectError(Exception e) {
//        L.d("Connect Error");
//    }
//
//    @Override
//    public void onChatMsg(ChatMsg chatMsg) {
//        L.d("Msg");
//        handleRecvMsg(chatMsg);
//    }
//
//    @Override
//    public void onChatMsgRead(String msgId, String qid) {
//        L.d("Msg");
//    }
//
//
//    @Override
//    public void onChatMsgReceived(String msgId) {
//        // ToastUtils.shortToast("Msg send success,id:" + msgId);
//        sendSuccessIds.add(msgId);
//        updateMsgSendState(msgId);
//    }
//
//    private void updateTopView() {
//        //stone 来自问题库和历史回复 或者 指定付费内容(指定给我的题目没权利跳过和转诊) 题目不属于我或者历史回复(已经回复过了)所以不显示跳过和转诊
//        //stone 只要回复了就让头部的跳过和问诊消失
//        if (mIsFromWTKOrHistory || isSpecified || (chatAdapter != null && getSendMsgSuccessCount() > 0)) {
//            mTop.setVisibility(View.GONE);
//        } else {
//            mTop.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @Override
//    protected void initView() {
//        fb = FinalBitmap.create(this, false);
//
//        findViewById(R.id.fl_top_left).setOnClickListener(this);
//        findViewById(R.id.fl_top_right).setOnClickListener(this);
//        //stone
//        fl_sumup_answer.setOnClickListener(this);
//        //stone 标题为患者名,推送过来的没有患者名就用 即时问答详情
//        if (getIntent() != null) {
//            patientName = getIntent().getStringExtra(PARAM_PATIENT_NAME);
//            if (TextUtils.isEmpty(patientName)) {
//                patientName = getString(R.string.online_consultation_detail);
//            }
//        }
//        titleBarBuilder.setTitleText(patientName);
//        chatAdapter = new ConsultChatAdapter(mIsFromWTKOrHistory, this, new MyCallBack() {
//
//            @Override
//            public void onClick(Object data) {
//                loadMsgList();
//            }
//        }, new ConsultChatAdapter.SendMsgError() {
//            @Override
//            public void senMsgError(int position) {
//
//            }
//        });
//
//        rlvChat.setAdapter(chatAdapter);
//        rlvChat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        //initBottomLayout();
//        etChatContent.setFilters(new InputFilter[]{new InputFilter() {
//            Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
//                    Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
//
//            @Override
//            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
//                Matcher emojiMatcher = emoji.matcher(source);
//                if (emojiMatcher.find()) {
//                    ToastUtils.shortToast("不支持输入表情");
//                    return "";
//                }
//                return null;
//            }
//        }});
//
//        mClipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//        PromptViewHelper pvHelper = new PromptViewHelper(ConsultChatActivity.this);
//        pvHelper.setPromptViewManager(new ChatPromptViewManager(ConsultChatActivity.this, new String[]{"粘贴"}, Location.TOP_LEFT));
//        pvHelper.addPrompt(etChatContent);
//        pvHelper.setOnItemClickListener(new PromptViewHelper.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                String resultString = "";
//                // 检查剪贴板是否有内容
//                if (!mClipboardManager.hasPrimaryClip()) {
//                    Toast.makeText(ConsultChatActivity.this,
//                            "粘贴内容为空", Toast.LENGTH_SHORT).show();
//                } else {
//                    ClipData clipData = mClipboardManager.getPrimaryClip();
//                    int count = clipData.getItemCount();
//
//                    for (int i = 0; i < count; ++i) {
//
//                        ClipData.Item item = clipData.getItemAt(i);
//                        CharSequence str = item
//                                .coerceToText(ConsultChatActivity.this);
//
//                        resultString += str;
//                    }
//
//                }
//                Editable editable = etChatContent.getEditableText();
//                editable.insert(etChatContent.getSelectionStart(), resultString);
//            }
//        });
//
//
//        //聊天底部的选项卡 问诊总结和快捷回复
//        bottomAdapter = new
//
//                ChatBottomAdapter(this);
//        bottomAdapter.setOnItemClickListener(this);
//        rlvChatItems.setLayoutManager(new
//
//                GridLayoutManager(this, 4));
//        rlvChatItems.setAdapter(bottomAdapter);
//
//        //初始化底部功能
//        initBottomItems();
//
//
//        //stone
//        WebSocketRxBus.registerWebSocketChatMagListener(new EventSubscriber<ChatMsg>()
//
//        {
//            @Override
//            public void onNext(final Event<ChatMsg> chatMsgEvent) {
//
//                if (null == chatMsgEvent) {
//                    return;
//                }
//                final ChatMsg chatMsg = chatMsgEvent.getData();
//                if (null == chatMsg || chatMsg.getBody() == null) {
//                    return;
//                }
//                if (ChatMsg.RECV_MSG_TYPE_NEW_NOTICE == chatMsg.getBody().getType()) {
//
//                    YMApplication.applicationHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            String string = (chatMsg != null && chatMsg.getBody() != null && chatMsg.getBody().getContent() != null
//                                    && !TextUtils.isEmpty(chatMsg.getBody().getContent().getText()))
//                                    ? chatMsg.getBody().getContent().getText() : getString(R.string.messagesetting_notice2);
//
//                            ConsultChatEntity entity = ConsultChatEntity.newInstanceText(
//                                    string, ConsultChatEntity.MSG_TYPE_SEND);
//                            chatAdapter.addData(entity);
//                            chatAdapter.notifyDataSetChanged();
//                            rlvChat.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
//                        }
//                    }, 20);
//
//
////                    runOnUiThread(new Runnable() {
////                        @Override
////                        public void run() {
//////                    stone 添加忙线提示
////                    ConsultChatEntity entity = ConsultChatEntity.newInstanceText(
////                            (chatMsg.getBody().getContent() != null && TextUtils.isEmpty(chatMsg.getBody().getContent().getText())) ? getString(R.string.messagesetting_notice2) : chatMsg.getBody().getContent().getText(), ConsultChatEntity.MSG_TYPE_SEND);
////                    chatAdapter.addData(entity);
////                    chatAdapter.notifyDataSetChanged();
////                    rlvChat.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
////                        }
////                    });
//                }
//            }
//        }, this);
//    }
//
//    @Override
//    protected void initData() {
//        if (doctorId != null && getIntent() != null) {
//            mIsFromWTKOrHistory = getIntent().getBooleanExtra(PARAM_IS_FROM_WTK_OR_HISTORY, false);
//            questionId = getIntent().getStringExtra(PARAM_QID);
//            patientId = getIntent().getStringExtra(PARAM_PATIENT_ID);
//            isFromHistory = getIntent().getBooleanExtra(PARAM_FROM_HISTORY, false);
//            isTimeOut = getIntent().getBooleanExtra(PARAM_TIMEOUT, false);
//            isSpecified = getIntent().getBooleanExtra(PARAM_SPECIFY, false);
//        }
//        //防止推送进入同一个详情页面
//        if (YMApplication.sQuestionId != null) {
//            YMApplication.sQuestionId.add(0, questionId);
//        }
//
//        //stone 新添加的状态
//        CertificationAboutRequest.getInstance().getMessageBoard(YMApplication.getUUid()).subscribe(new BaseRetrofitResponse<BaseData<List<MessageBoardBean>>>() {
//            @Override
//            public void onError(Throwable e) {
//                super.onError(e);
//            }
//
//            @Override
//            public void onNext(BaseData<List<MessageBoardBean>> listBaseData) {
//                super.onNext(listBaseData);
//                hideProgressDialog();
//                if (listBaseData != null
//                        && listBaseData.getData() != null
//                        && listBaseData.getData().size() == 2) {
//
//                    handleSetMessageBoard(listBaseData.getData().get(0));
//                    handleSetMessageBoard(listBaseData.getData().get(1));
//
//                    if (!DateUtils.isCurrentInTimeScope(6, 0, 22, 0)) {
//                        mIsNoticeOpen = mOfflineOpened || mNightOpend;
//                    } else {
//                        mIsNoticeOpen = mOfflineOpened;
//                    }
//
//                }
//            }
//        });
//
//        loadMsgList();
//        readQuestionMsg();
////        updateTopView();
//    }
//
//    private void handleSetMessageBoard(MessageBoardBean bean) {
//        //1 离线 2 夜间
//        if (OFFLINE.equals(bean.getType())) {
//            mOfflineOpened = OPENED.equals(bean.getIsopen());
//            YMApplication.mOfflineMessage = bean.getMessage();
//            //stone
//            mEndTime = Long.parseLong(bean.getLeft_time()) * 1000 + System.currentTimeMillis();
////            mEndTime = Long.parseLong("20000") + System.currentTimeMillis();
//        } else if (NIGHT.equals(bean.getType())) {
//            mNightOpend = OPENED.equals(bean.getIsopen());
//            YMApplication.mNightMessage = bean.getMessage();
//
//            //通知
//            YmRxBus.notifyNightModeChanged(mNightOpend);
//        }
//    }
//
//
//    private void initBottomLayout() {
//        //stone 历史回复与已关闭是一样处理
//        if (isFromHistory || isClosed) {
//            viewAdopt.setVisibility(View.GONE);
//            viewChat.setVisibility(View.GONE);
//            viewObtain.setVisibility(View.GONE);
//            //stone 超时就不能再总结了 回复了几条
//            if (isTimeOut) {
//                tv_sumup_timeout.setVisibility(View.VISIBLE);
//            } else {
//                int state = getChatState(chatAdapter.getDatas());
//                //stone 限制了条数为4条以上的可总结 目前改为只要不超时就能总结
//                if (state == CHAT_STATE_ALREAD_SUM_UP) {
//                    fl_sumup_answer.setVisibility(View.VISIBLE);
//                    tv_sumup_answer.setText(STR_ITEM_MODIFY_SUM_UP);
//                } else if (state == CHAT_STATE_ABLE_SUM_UP) {
//                    fl_sumup_answer.setVisibility(View.VISIBLE);
//                    tv_sumup_answer.setText(STR_ITEM_SUM_UP);
//                } else {
//                    //stone 医生回复少于四条也能总结
//                    fl_sumup_answer.setVisibility(View.VISIBLE);
//                    tv_sumup_answer.setText(STR_ITEM_SUM_UP);
////                    fl_sumup_answer.setVisibility(View.GONE);
//                }
//            }
//            return;
//        }
//        //stone 去掉认领逻辑
//        if (bottomType == BOTTOM_TYPE_ADOPT) {
//            //认领题目
//            viewAdopt.setVisibility(View.VISIBLE);
//            viewChat.setVisibility(View.GONE);
//            viewObtain.setVisibility(View.GONE);
//            ivAdopt.setOnClickListener(this);
//            ivAdoptPass.setOnClickListener(this);
//            //stone 添加指定付费逻辑 不可跳过和转诊
//            if (isSpecified) {
//                ivAdoptPass.setVisibility(View.GONE);
//                tvAdoptPass.setVisibility(View.GONE);
//            }
//
//        } else if (bottomType == BOTTOM_TYPE_OBTAIN) {
//            //抢题
//            viewAdopt.setVisibility(View.GONE);
//            viewChat.setVisibility(View.GONE);
//            viewObtain.setVisibility(View.VISIBLE);
//            ivObtain.setOnClickListener(this);
//            ivObtainPass.setOnClickListener(this);
//        } else if (bottomType == BOTTOM_TYPE_CHAT) {
//            //聊天
//            viewAdopt.setVisibility(View.GONE);
//            viewChat.setVisibility(View.VISIBLE);
//            viewObtain.setVisibility(View.GONE);
//            iv_more_btn.setOnClickListener(this);
//            btnChatSend.setOnClickListener(this);
//
////            bottomAdapter = new ChatBottomAdapter(this);
////            bottomAdapter.setOnItemClickListener(this);
////            rlvChatItems.setLayoutManager(new GridLayoutManager(this, 4));
////            rlvChatItems.setAdapter(bottomAdapter);
////            //初始化底部功能
////            initBottomItems();
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (!isAllowedOperation()) {
//            return;
//        }
//        switch (v.getId()) {
//            case R.id.fl_sumup_answer:
//                //stone 总结
//                StatisticalTools.eventCount(YMApplication.getAppContext(), ConsultConstants.EVENT_QUESTION_HISTORY_CONCLUSION);
//                SumUpActivity.startSumUpActivityForResult(this, doctorId, questionId, REQUEST_CODE_SUM_UP);
//                break;
//            case R.id.fl_top_right:
//                //转诊
//                ConsultQueSwitchDpartAcitivity.startActivity(this, questionId);
//                break;
//            case R.id.fl_top_left:
//                //跳过
//                backQuestion();
//                break;
//            case R.id.iv_chat_bottom_adopt_adopt:
//                //认领题目
//                adoptQueston();
//                break;
//            case R.id.iv_chat_bottom_adopt_pass:
//                //认领跳过
//                backQuestion();
//                break;
//            case R.id.iv_chat_bottom_obtain_question:
//                //抢题
//                obtainQuestion();
//                break;
//            case R.id.iv_chat_bottom_obtain_pass:
//                //抢题跳过
//                backQuestion();
//                break;
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
//            case R.id.btn_chat_bottom_chat_send:
//                //聊天发送
//                KeyBoardUtils.closeKeyboard(etChatContent);
//                sendText(etChatContent.getText().toString());
//                break;
//        }
//    }
//
//    //初始化聊天状态下底部按钮  总结+快捷回复
//    private void initBottomItems() {
//        if (bottomAdapter != null) {
//            List<ChatBottomItemEntity> itemEntityList = new ArrayList<>();
//            ChatBottomItemEntity bottomItemEntity = null;
//
//            //添加语音转文字 stone
//            bottomItemEntity = new ChatBottomItemEntity(STR_ITEM_VOICE_TO_TEXT, true,
//                    R.drawable.chat_bottom_item_voice_to_text, R.drawable.chat_bottom_item_voice_to_text);
//            itemEntityList.add(bottomItemEntity);
//
//            //添加图片 stone
////            bottomItemEntity = new ChatBottomItemEntity(STR_ITEM_PICTURE, true,
////                    R.drawable.chat_bottom_item_picture, R.drawable.chat_bottom_item_picture);
////            itemEntityList.add(bottomItemEntity);
//
//
//            //添加快捷回复 stone
//            bottomItemEntity = new ChatBottomItemEntity(STR_ITEM_FAST_REPLY, true,
//                    R.drawable.chat_bottom_item_fast_reply, R.drawable.chat_bottom_item_fast_reply);
//            itemEntityList.add(bottomItemEntity);
//
//
//            //问诊总结 或者 修改总结
////            if (chatState == CHAT_STATE_ALREAD_SUM_UP || chatState == CHAT_STATE_ABLE_SUM_UP) {
//            bottomItemEntity = new ChatBottomItemEntity(chatState == CHAT_STATE_ABLE_SUM_UP ? STR_ITEM_SUM_UP : STR_ITEM_MODIFY_SUM_UP, true,
//                    R.drawable.chat_bottom_item_sum_up_enable, R.drawable.chat_bottom_item_sum_up_disable);
//            itemEntityList.add(bottomItemEntity);
//            tv_sumup_answer.setText(chatState == CHAT_STATE_ABLE_SUM_UP ? STR_ITEM_SUM_UP : STR_ITEM_MODIFY_SUM_UP);
////            }
//
//
//            bottomAdapter.setData(itemEntityList);
//            bottomAdapter.notifyDataSetChanged();
//        }
//    }
//
//
//    //修改不同聊天状态下，底部按钮的显示 目前只有问诊总结和快捷回复
//    private void refreshChatState() {
//        if (bottomAdapter != null) {
//            List<ChatBottomItemEntity> itemEntities = bottomAdapter.getDatas();
////            if (itemEntities.size() == 2) {
//            ChatBottomItemEntity itemSumUp = itemEntities.get(2);
//            switch (chatState) {
//                case CHAT_STATE_BEGIN:
//                    itemSumUp.setEnable(false);
//                    itemSumUp.setText(STR_ITEM_SUM_UP);
//                    break;
//                case CHAT_STATE_IN_CHAT:
//                    itemSumUp.setEnable(false);
//                    itemSumUp.setText(STR_ITEM_SUM_UP);
//                    break;
//                case CHAT_STATE_ALREAD_SUM_UP:
//                    itemSumUp.setEnable(true);
//                    itemSumUp.setText(STR_ITEM_MODIFY_SUM_UP);
//                    //stone 已完成的问题,底部逻辑跟这个一样
//                    tv_sumup_answer.setText(STR_ITEM_MODIFY_SUM_UP);
//                    break;
//                case CHAT_STATE_ABLE_SUM_UP:
//                    itemSumUp.setEnable(true);
//                    itemSumUp.setText(STR_ITEM_SUM_UP);
//                    break;
//            }
//            bottomAdapter.notifyDataSetChanged();
////            }
////            else if (itemEntities.size() == 1 && (chatState == CHAT_STATE_ALREAD_SUM_UP || chatState == CHAT_STATE_ABLE_SUM_UP)) {
////                //从没有总结到有总结 stone
////                initBottomItems();
////            }
//        }
//    }
//
//    @Override
//    public void onItemClick(ChatBottomItemEntity entity) {
//        if (!isAllowedOperation()) {
//            return;
//        }
//        //stone 底部就只有总结+快捷回复
//        if (entity.getText().equals(STR_ITEM_MODIFY_SUM_UP)) {
//            //点击修改总结
//            StatisticalTools.eventCount(YMApplication.getAppContext(), ConsultConstants.EVENT_QUESTION_SUM_UP);
//            SumUpActivity.startSumUpActivityForResult(this, doctorId, questionId, REQUEST_CODE_SUM_UP);
//        } else if (entity.getText().equals(STR_ITEM_SUM_UP)) {
//            //点击总结
//            StatisticalTools.eventCount(YMApplication.getAppContext(), ConsultConstants.EVENT_QUESTION_SUM_UP);
//            SumUpActivity.startSumUpActivityForResult(this, doctorId, questionId, REQUEST_CODE_SUM_UP);
//        } else if (entity.getText().equals(STR_ITEM_FAST_REPLY)) {
////            //快捷回复 stone
//            startActivityForResult(new Intent(YMApplication.getAppContext(),
//                    IMFastReplyActivity.class), REQUEST_CODE_FAST_REPLY);
//        } else if (entity.getText().equals(STR_ITEM_VOICE_TO_TEXT)) {
////            //语音 stone
//            KDXFUtils.start(this, mRecognizerDialogListener);
//            StatisticalTools.eventCount(ConsultChatActivity.this,"voicetotext");
//        } else if (entity.getText().equals(STR_ITEM_PICTURE)) {
////            //图片 stone
//            // 设置layout在PopupWindow中显示的位置
//            menuWindow = new SelectPicPopupWindow(this,
//                    itemsOnClick);
//            // 显示窗口
//            menuWindow.showAtLocation(main, Gravity.BOTTOM
//                    | Gravity.CENTER_HORIZONTAL, 0, 0);
//
//            //添加遮罩 stone
//            YMOtherUtils.addScreenBg(menuWindow, this);
//
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE_SUM_UP) {
//            if (null!=data) {
//                String chatData = data.getStringExtra("chatData");
//                ConsultChatEntity entity = null;
//                if (!TextUtils.isEmpty(chatData)) {
//                    //直接刷新列表
//                    entity = ConsultChatEntity.newInstanceText(chatData, ConsultChatEntity.MSG_TYPE_SEND);
//                    chatAdapter.addData(entity);
//                    chatAdapter.notifyDataSetChanged();
//                    rlvChat.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
//                    tv_sumup_answer.setText("修改总结");
//                }
//            }
//        } else if (requestCode == REQUEST_CODE_CHANGE_DEPARTMENT && data != null) {
//            boolean isSuccess = data.getBooleanExtra(ConsultQueSwitchDpartAcitivity.PARAM_RESULT, false);
//            if (isSuccess) {
//                onBackPressed();
//            }
//        } else if (requestCode == REQUEST_CODE_FAST_REPLY && data != null) {
//            //快捷回复 stone
//            String s = data.getStringExtra(Constants.KEY_VALUE);
//            sendText(s);
//        } else DLog.i("300", "onActivityResult==" + requestCode);
//        if (requestCode == Constants.REQUESTCODE_CHOOSE_CAMERA && resultCode == RESULT_OK) {
//            Intent intent = new Intent(this, ClipPictureActivity.class);
//            DLog.i("300", "REQUESTCODE_CHOOSE_CAMERA" + origUri.getPath());
//            intent.putExtra("path", origUri.getPath());
//            startActivityForResult(intent, Constants.REQUESTCODE_MODIFY_FINISH);
//        } else if (requestCode == Constants.REQUESTCODE_MODIFY_FINISH && resultCode == RESULT_OK) {
//            DLog.i("300", " REQUESTCODE_MODIFY_FINISH resultCode==" + resultCode);
//            if (data != null) {
//                final String path = data.getStringExtra("path");
//                // Bitmap b = BitmapFactory.decodeFile(path);
//                // iv_head.setImageBitmap(b);
//                if (path != null && !path.equals("")) {
//                    if (NetworkUtil.isNetWorkConnected()) {
//                        mHeadIconUploadingPath = path;
////                        YMUserService.getPerInfo().getData().setPhoto(path);
//                        YMApplication.getLoginInfo().getData().setPhoto(path);
//
//
////                        fb.display(iv_head, mHeadIconUploadingPath);
//
//                        mHeadIconPath = mHeadIconUploadingPath;
//
//                        ToastUtils.shortToast(
//                                "图片上传成功,发送socket");
//
//                    } else {
//                        ToastUtils.shortToast(
//                                "网络连接失败,图片不能上传,请联网重试");
//                    }
//                }
//            }
//        }
//    }
//
//    //获取消息列表 stone
//    private void loadMsgList() {
//        if (questionId == null || questionId.equals("") || doctorId == null || doctorId.equals("")) {
//            return;
//        } else {
//            ServiceProvider.getQuestionMsgList(doctorId, questionId, new Subscriber<QuestionMsgListRspEntity>() {
//                @Override
//                public void onCompleted() {
//
//                }
//
//                @Override
//                public void onError(Throwable e) {
//
//                }
//
//                @Override
//                public void onNext(QuestionMsgListRspEntity questionMsgListRspEntity) {
//                    if (questionMsgListRspEntity == null || questionMsgListRspEntity.getCode() != ConsultConstants.CODE_REQUEST_SUCCESS || questionMsgListRspEntity.getData() == null) {
//                        ToastUtils.longToast(questionMsgListRspEntity.getMsg());
//                        finish();
//                    } else {
//                        // stone 问题是否关闭 关闭了就直接展示成历史回复中一样
//                        isClosed = questionMsgListRspEntity.getData().getClosed() == 1;
//                        //stone 后台控制医生回复几条信息能写医嘱
//                        if (questionMsgListRspEntity.getData().getAllow_summary_count() > 0) {
//                            allow_summary_count = questionMsgListRspEntity.getData().getAllow_summary_count();
//                        }
//
//                        //数据
//                        List<ConsultChatEntity> entityList = new ArrayList<ConsultChatEntity>();
//                        List<QuestionMsgListRspEntity.DataBean.ListBean> list = questionMsgListRspEntity.getData().getList();
//                        if (list != null) {
//                            for (int i = 0; i < list.size(); i++) {
//                                //stone 过滤掉type==4的消息
//                                if (list.get(i).getContent() != null
//                                        && list.get(i).getContent().size() > 0
//                                        && !TextUtils.isEmpty(list.get(i).getContent().get(0).getType())
//                                        && !list.get(i).getContent().get(0).getType().equals("4")) {
//                                    ConsultChatEntity entity = new ConsultChatEntity();
//                                    entity.setDataBean(list.get(i));
//                                    entityList.add(entity);
//                                }
//                            }
//                        }
//
//
////                        //stone 添加关闭留言提示 打开+不是第一次加载+之前回复过已经展示过
////                        if (mIsNoticeOpen && !mIsFirstLoad && mIsNoticeShowed) {
////                            ConsultChatEntity consultChatEntity = new ConsultChatEntity();
////                            consultChatEntity.setType(TYPE_CLOSE_NOTICE);
////                            consultChatEntity.setMsg_type(ConsultChatEntity.MSG_TYPE_SEND);
////                            if (mFirstPos == -1) {
////                                entityList.add(consultChatEntity);
////                                mFirstPos = entityList.size() - 1;
////                            } else {
////                                entityList.add(mFirstPos, consultChatEntity);
////                            }
////                        }
////                        mIsFirstLoad = false;
//
//
//                        //逆序处理
//                        Collections.reverse(entityList);
//                        chatAdapter.setData(entityList);
//                        chatAdapter.notifyDataSetChanged();
//                        rlvChat.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
//
//
//                        //聊天状态 开始聊 在聊(条数少于4) 可总结(条数大于等于4,但未总结过) 已总结
//                        chatState = getChatState(entityList);
//                        refreshChatState();
//
//                        //顶部view展示 跳过和转诊
//                        updateTopView();
//
//                        //stone 底部view状态获取
//                        if (mIsFromWTKOrHistory && !isFromHistory) {
//                            //来源问题库 (抢题) 抢过了直接展示聊天 否则直接要求抢题
//                            if (!mIsObtained) {
//                                bottomType = BOTTOM_TYPE_OBTAIN;
//                            } else {
//                                bottomType = BOTTOM_TYPE_CHAT;
//                                webConnect();
//                            }
//                        } else if (isSpecified) {
//                            //指定付费 直接为聊天状态
//                            bottomType = BOTTOM_TYPE_CHAT;
//                            webConnect();
//                        } else {
//                            //来源咨询中 或者 历史回复 或者 推送 (认领或聊天)
//                            bottomType = questionMsgListRspEntity.getData().getStatus();
//                            if (bottomType == BOTTOM_TYPE_CHAT) {
//                                webConnect();
//                            }
//                        }
//                        //聊天view 抢题view 认领view
//                        initBottomLayout();
//                    }
//                }
//            });
//        }
//    }
//
//    //stone 获取发送的消息数目 不论成功与失败
//    private int getSendMsgCount() {
//        int ret = 0;
//        List<ConsultChatEntity> list = chatAdapter.getDatas();
//        Iterator<ConsultChatEntity> it = list.iterator();
//        while (it.hasNext()) {
//            ConsultChatEntity item = it.next();
//            if (item.getMsg_type() == ConsultChatEntity.MSG_TYPE_SEND) {
//                ret++;
//            }
//        }
//        return ret;
//    }
//
//    //stone 获取发送成功的消息数目
//    private int getSendMsgSuccessCount() {
//        int ret = 0;
//        List<ConsultChatEntity> list = chatAdapter.getDatas();
//        Iterator<ConsultChatEntity> it = list.iterator();
//        while (it.hasNext()) {
//            ConsultChatEntity item = it.next();
//            //stone 添加个判断 发送成功才计数
//            if (item.getMsg_type() == ConsultChatEntity.MSG_TYPE_SEND
//                    && item.getSendState() == ConsultChatEntity.SEND_STATE_SUCCESS) {
//                ret++;
//            }
//        }
//        return ret;
//    }
//
//    //stone 收到消息
//    private void handleRecvMsg(ChatMsg chatMsg) {
//        if (recvMsgIds.contains(chatMsg.getId()) || chatMsg.getBody().getQid() == null ||
//                !chatMsg.getBody().getQid().equals(questionId) || isError) {
//            return;
//        }
//        recvMsgIds.add(chatMsg.getId());
//        // TODO: 2018/5/29 websocket新版本 stone
////        if (recvMsgIds.contains(chatMsg.getMsg_id()) || String.valueOf(chatMsg.getBody().getQid()) == null ||
////                !String.valueOf(chatMsg.getBody().getQid()).equals(questionId) || isError) {
////            return;
////        }
////        recvMsgIds.add(String.valueOf(chatMsg.getMsg_id()));
//
//        ConsultChatEntity entity = null;
//        if (chatMsg.getBody().getType() == ChatMsg.RECV_MSG_TYPE_NORMAL) {
//            //直接刷新列表
////            loadMsgList();
//            if (chatMsg.getBody().getContent().getType() == ContentType.text) {
//                entity = ConsultChatEntity.newInstanceText(chatMsg.getBody().getContent().getText(), ConsultChatEntity.MSG_TYPE_RECV);
//            } else if (chatMsg.getBody().getContent().getType() == ContentType.image) {
//                List<String> imgUrls = new ArrayList<>();
//                imgUrls.add(chatMsg.getBody().getContent().getFile());
//                entity = ConsultChatEntity.newInstanceImgs(imgUrls);
//                entity.setMsg_type(ConsultChatEntity.MSG_TYPE_RECV);
//            }
//            if (entity != null) {
//                chatAdapter.addData(entity);
//                chatAdapter.notifyDataSetChanged();
//                rlvChat.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
//            }
//        } else {
//            switch (chatMsg.getBody().getType()) {
//                case ChatMsg.RECV_MSG_TYPE_ACCEPT_OVER_TIME:
//                case ChatMsg.RECV_MSG_TYPE_DOCTOR_FORBID:
//                case ChatMsg.RECV_MSG_TYPE_UN_ADOPT:
//                case ChatMsg.RECV_MSG_TYPE_CLOSED:
//                case ChatMsg.RECV_MSG_TYPE_QUESTION_OVER_TIME:
//                    //loadMsgList();
//                    errorCode = chatMsg.getBody().getType();
//                    isError = true;
//                    if (chatMsg.getBody().getContent().getText() != null) {
//                        ToastUtils.shortToast(chatMsg.getBody().getContent().getText());
//                    }
//                    break;
//            }
//        }
//
//    }
//
//
//    //发消息
//    private void sendText(String text) {
//        isConnect = false;
//        if (!isAllowedOperation()) {
//            return;
//        }
//
//        //stone 提前
//        if (!isConnected()) {
//            connect(ConsultConstants.WEBSOCKET_ADDRESS, doctorId);
//            //stone 长时间停留在页面需要重新连接 此时不发消息 只连接 给个提示给用户
//            ToastUtils.shortToast("重连中...");
//            isConnect = true;
//            return;
//        }
//
//        if (text == null) {
//            ToastUtils.shortToast("回复内容不能为空");
//            return;
//        }
//        text = text.trim();
//        if (text.equals("")) {
//            ToastUtils.shortToast("回复内容不能为空");
//            return;
//        }
//
//
//        sendChatMsg(doctorId, patientId, (++seq) + "", questionId, text);
//
//        final ConsultChatEntity entity = ConsultChatEntity.newInstanceText(text, ConsultChatEntity.MSG_TYPE_SEND);
//        entity.setSendState(ConsultChatEntity.SEND_STATE_IN_SENDING);
//        entity.setSendId(seq + "");
//        chatAdapter.addData(entity);
//
//        //stone 添加关闭留言提示
//        //离线留言倒计时结束了 需要补弹出系统提示
//        if (!DateUtils.isCurrentInTimeScope(6, 0, 22, 0)) {
//            mIsNoticeOpen = (mOfflineOpened && mEndTime > System.currentTimeMillis()) || mNightOpend;
//        } else {
//            mIsNoticeOpen = mOfflineOpened && mEndTime > System.currentTimeMillis();
//        }
//
//        if (mIsNoticeOpen && !mIsNoticeShowed) {
//            ConsultChatEntity consultChatEntity = new ConsultChatEntity();
//            consultChatEntity.setType(TYPE_CLOSE_NOTICE);
//            consultChatEntity.setMsg_type(ConsultChatEntity.MSG_TYPE_SEND);
//            if (mFirstPos == -1) {
//                chatAdapter.addData(consultChatEntity);
//                mFirstPos = chatAdapter.getDatas().size() - 1;
//            } else {
//                chatAdapter.addDataForPos(mFirstPos, consultChatEntity);
//            }
//            mIsNoticeShowed = true;
//        }
//
//        chatAdapter.notifyDataSetChanged();
//
//        //stone
//        mIsWaittingSendingStateChange = true;
//        //stone 在发送第四条消息
//        if (getSendMsgCount() == allow_summary_count) {
//            mIsWaittingSending4 = true;
//        }
//
//        rlvChat.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (!sendSuccessIds.contains(entity.getSendId())) {
//                    entity.setSendState(ConsultChatEntity.SEND_STATE_FAILED);
//                    chatAdapter.notifyDataSetChanged();
//                } else {
//                    //stone 注释掉
////                    if (getSendMsgCount() == 4) {
////                        ToastUtils.shortToast("问诊完成后，认真填写问诊总结能得到更高绩效哦");
////                    }
////                    if (mTop.getVisibility() != View.GONE) {
////                        mTop.setVisibility(View.GONE);
////                    }
//                }
//            }
//        }, ConsultConstants.CHAT_SEND_FAILED_TIME_LIMITED);
//        rlvChat.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
//        etChatContent.setText("");
//        chatState = getChatState(chatAdapter.getDatas());
//        refreshChatState();
//    }
//
//    //stone 是否允许操作
//    private boolean isAllowedOperation() {
//        switch (errorCode) {
//            case ChatMsg.RECV_MSG_TYPE_ACCEPT_OVER_TIME:
//            case ChatMsg.RECV_MSG_TYPE_DOCTOR_FORBID:
//            case ChatMsg.RECV_MSG_TYPE_UN_ADOPT:
//            case ChatMsg.RECV_MSG_TYPE_CLOSED:
//            case ChatMsg.RECV_MSG_TYPE_QUESTION_OVER_TIME:
//                ToastUtils.shortToast(ChatMsg.RECV_ERROR_MSG_TIP.get(errorCode));
//                rlvChat.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        finish();
//                    }
//                }, 1000);
//                break;
//        }
//        return !isError;
//    }
//
//    private void webConnect() {
//        if (!isConnected()) {
//            connect(ConsultConstants.WEBSOCKET_ADDRESS, doctorId);
//        }
//    }
//
//    //stone 跳过
//    private void backQuestion() {
//        if (ClickUtil.isFastDoubleClick()) {
//            return;
//        }
//        StatisticalTools.eventCount(YMApplication.getAppContext(), ConsultConstants.EVENT_QUESTION_SKIP);
//        ServiceProvider.backQuestion(doctorId, questionId, new Subscriber<BackQuestionRspEntity>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//            }
//
//            @Override
//            public void onNext(BackQuestionRspEntity backQuestionRspEntity) {
//                if (backQuestionRspEntity != null && backQuestionRspEntity.getCode() == ConsultConstants.CODE_REQUEST_SUCCESS) {
//                    ToastUtils.shortToast("问题已跳过");
//                    onBackPressed();
//                } else {
//                    ToastUtils.shortToast("问题跳过失败");
//                }
//
//            }
//        });
//    }
//
//    //stone 抢题
//    private void obtainQuestion() {
//        if (ClickUtil.isFastDoubleClick()) {
//            return;
//        }
//        StatisticalTools.eventCount(YMApplication.getAppContext(), ConsultConstants.EVENT_QUESTION_OBTAIN);
//        ServiceProvider.obtainQuestion(doctorId, questionId, new Subscriber<CommonRspEntity>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//            }
//
//            @Override
//            public void onNext(CommonRspEntity commonRspEntity) {
//                if (commonRspEntity != null && commonRspEntity.getCode() == ConsultConstants.CODE_REQUEST_SUCCESS) {
//
//                    //stone 已经抢题成功了
//                    mIsObtained = true;
//
//                    //ToastUtils.shortToast("抢题成功");
//                    //stone 抢题成功展示顶部的跳过和转诊接口
//                    mTop.setVisibility(View.VISIBLE);
//
//                    chatState = CHAT_STATE_BEGIN;
//                    webConnect();
//                    refreshChatState();
//
//                    bottomType = BOTTOM_TYPE_CHAT;
//                    initBottomLayout();
//                } else {
//                    //stone 更换提示
//                    ToastUtils.shortToast("抢题失败");
////                    if (commonRspEntity != null && !TextUtils.isEmpty(commonRspEntity.getMsg())) {
////                        ToastUtils.shortToast(commonRspEntity.getMsg());
////                    } else {
////                        ToastUtils.shortToast("抢题失败");
////                    }
//                    onBackPressed();
//                }
//            }
//        });
//    }
//
//    //stone 认领
//    private void adoptQueston() {
//        if (ClickUtil.isFastDoubleClick()) {
//            return;
//        }
//        StatisticalTools.eventCount(YMApplication.getAppContext(), ConsultConstants.EVENT_QUESTION_CLAIM);
//        ServiceProvider.adoptQuestion(doctorId, questionId, new Subscriber<CommonRspEntity>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//            }
//
//            @Override
//            public void onNext(CommonRspEntity commonRspEntity) {
//                if (commonRspEntity != null && commonRspEntity.getCode() == ConsultConstants.CODE_REQUEST_SUCCESS) {
//                    //ToastUtils.shortToast("认领成功");
//                    //stone 认领成功展示顶部的跳过和转诊接口 不是指定付费的才能跳过和转诊
//                    if (!isSpecified) {
//                        mTop.setVisibility(View.VISIBLE);
//                    }
//
//                    chatState = CHAT_STATE_BEGIN;
//                    refreshChatState();
//                    webConnect();
//
//                    bottomType = BOTTOM_TYPE_CHAT;
//                    initBottomLayout();
//                } else {
//                    //stone 更换提示
//                    ToastUtils.shortToast("认领失败");
////                    if (commonRspEntity != null && !TextUtils.isEmpty(commonRspEntity.getMsg())) {
////                        ToastUtils.shortToast(commonRspEntity.getMsg());
////                    } else {
////                        ToastUtils.shortToast("认领失败");
////                    }
//                    onBackPressed();
//                }
//            }
//        });
//    }
//
//    //stone 获取聊天状态 用来展示不同的底部view
//    private int getChatState(List<ConsultChatEntity> list) {
//        int sendMsgCount = 0;
//        boolean isSumUp = false;
//        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).getMsg_type() == ConsultChatEntity.MSG_TYPE_SEND) {
//                //stone 发送成功才计数
//                if (list.get(i).getSendState() == ConsultChatEntity.SEND_STATE_SUCCESS) {
//                    sendMsgCount++;
//                }
//                //问诊总结过与否 stone 兼容老版本
//                if (!isSumUp && !TextUtils.isEmpty(list.get(i).getText()) &&
//                        (list.get(i).getText().trim().startsWith(STR_SUM_UP_TITLE)
//                                || list.get(i).getText().trim().startsWith(STR_SUM_UP_TITLE2)
//                                || list.get(i).getText().trim().startsWith(STR_SUM_UP_TITLE3))) {
//                    isSumUp = true;
//                }
//            }
//        }
//
//        //stone 新添加
//        if (mIsWaittingSending4) {
//            ToastUtils.shortToast("问诊完成后，认真填写问诊总结能得到更高绩效哦");
//            mIsWaittingSending4 = false;
//        }
//        //stone 只要回复过问题了 就不能跳过和转诊了
//        if (mIsWaittingSendingStateChange && mTop.getVisibility() != View.GONE) {
//            mTop.setVisibility(View.GONE);
//        }
//        mIsWaittingSendingStateChange = false;
//
//        //stone 调换位置 使得历史回复中的回复条数少于四条时 去总结了 回来刷新按钮上文字
//        if (sendMsgCount == 0) {
//            return CHAT_STATE_BEGIN;
//        } else if (isSumUp) {
//            return CHAT_STATE_ALREAD_SUM_UP;
//        } else if (sendMsgCount < allow_summary_count) {
//            return CHAT_STATE_IN_CHAT;
//        } else {
//            return CHAT_STATE_ABLE_SUM_UP;
//        }
//    }
//
//    //刷新发送状态和底部状态 stone
//    private void updateMsgSendState(String id) {
//        for (int i = chatAdapter.getItemCount() - 1; i >= 0; i--) {
//            if (chatAdapter.getDatas().get(i).getMsg_type() == ConsultChatEntity.MSG_TYPE_SEND
//                    && !TextUtils.isEmpty(chatAdapter.getDatas().get(i).getSendId())
//                    && chatAdapter.getDatas().get(i).getSendId().equals(id)) {
//                chatAdapter.getDatas().get(i).setSendState(ConsultChatEntity.SEND_STATE_SUCCESS);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        chatAdapter.notifyDataSetChanged();
//                    }
//                });
//                break;
//            }
//        }
//        //stone 新添加
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                chatState = getChatState(chatAdapter.getDatas());
//                refreshChatState();
//            }
//        });
//        if (isConnect){
//            isConnect = false;
//            loadMsgList();
//        }
//
//    }
//
//    //阅读消息 stone
//    private void readQuestionMsg() {
//        ServiceProvider.readQuestion(doctorId, questionId, new Subscriber<CommonRspEntity>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(CommonRspEntity commonRspEntity) {
//                if (commonRspEntity != null && commonRspEntity.getCode() == ConsultConstants.CODE_REQUEST_SUCCESS) {
//                    // ToastUtils.shortToast("消息阅读成功");
//                } else {
//                    //ToastUtils.shortToast("消息阅读失败");
//                }
//            }
//        });
//    }
//
//
//    /**
//     * @param sequence 服务端消息起始编号
//     */
//    @Override
//    public void onStartChat(int sequence) {
//        super.onStartChat(sequence);
//        // TODO: 2017/5/5 会话已建立
//    }
//
//    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_consult_chat;
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        KDXFUtils.cancel();
//
//        if (YMApplication.sQuestionId != null && YMApplication.sQuestionId.size() > 0) {
//            YMApplication.sQuestionId.remove(0);
//        }
//
//        super.onDestroy();
//
//    }
//
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        loadMsgList();
//
//        setIntent(intent);
//        DLog.i("300", "裁剪" + getIntent().getIntExtra("code", 0));
//        int code = intent.getIntExtra("code", -1);
//        if (code != 100) {
//            return;
//        }
//
//        ArrayList<String> list1 = new ArrayList<String>();
//        list1.clear();
//        ArrayList<String> paths = intent.getStringArrayListExtra("paths");
//        if (paths != null && paths.size() > 0) {
//            Intent intent2 = new Intent(this, ClipPictureActivity.class);
//            DLog.d("approve", paths.get(0));
//            intent2.putExtra("path", paths.get(0));
//            startActivityForResult(intent2, Constants.REQUESTCODE_MODIFY_FINISH);
//        } else {
//            ToastUtils.shortToast("图片选取失败");
//        }
//    }
//
//
//    /**
//     * 听写监听器
//     */
//    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
//        public void onResult(RecognizerResult results, boolean isLast) {
//            L.d("语音", results.getResultString());
//            String text = KDXFUtils.printResult(results);
//            if (isLast) {
//                etChatContent.append(text);
//                if (YMApplication.getFirstUseKdxf()) {
//                    YMApplication.setFirstUseKdxf(false);
//                    ToastUtils.longToast("记得点击发送按键回复患者哟~");
//                }
//            }
//        }
//
//        /**
//         * 识别回调错误.
//         */
//        public void onError(SpeechError error) {
//            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
////            ToastUtils.shortToast(error.getPlainDescription(true));
//        }
//
//    };
//
//
//    //图片
//
//    // 为弹出窗口实现监听类
//    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
//        Intent intent;
//
//        public void onClick(View v) {
//            menuWindow.dismiss();
//            switch (v.getId()) {
//                case R.id.item_popupwindows_camera:
//                    // 照片命名
//                    // String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
//                    // .format(new Date());
//                    // String origFileName = "osc_3" + timeStamp + ".jpg";
//                    // origUri = Uri.fromFile(new File(FILE_SAVEPATH,
//                    // origFileName));
//                    origUri = new File(PathUtil.getInstance().getImagePath(),
//                            "osc_" + System.currentTimeMillis() + ".jpg");
//                    origUri.getParentFile().mkdirs();
//                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(origUri));
//                    startActivityForResult(intent, Constants.REQUESTCODE_CHOOSE_CAMERA);
//                    // startActivity(intent);
//                    break;
//                case R.id.item_popupwindows_Photo:
//                    intent = new Intent(ConsultChatActivity.this, PhotoWallActivity.class);
//                    startActivity(intent);
//                    YMApplication.photoTag = Constants.IM_CHAT;
//                    YMApplication.isSelectMore = false;
//                    break;
//                case R.id.item_popupwindows_cancel:
//                    menuWindow.dismiss();
//                    break;
//                default:
//                    break;
//            }
//        }
//    };


//}