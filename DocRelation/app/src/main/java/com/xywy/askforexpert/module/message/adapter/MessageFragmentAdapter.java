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
package com.xywy.askforexpert.module.message.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Parcel;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.ParcelableSpan;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.chat.EMMessage.Direct;
import com.hyphenate.chat.EMMessage.Type;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.DateUtils;
import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.webview.CommonWebViewActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.imageutils.ImageCache;
import com.xywy.askforexpert.appcommon.utils.imageutils.ImageUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.Seminar;
import com.xywy.askforexpert.model.ShareCardInfo;
import com.xywy.askforexpert.model.im.group.GroupModel;
import com.xywy.askforexpert.module.discovery.answer.AnswerMainActivity;
import com.xywy.askforexpert.module.discovery.answer.activity.AnswerDetailActivity;
import com.xywy.askforexpert.module.discovery.answer.activity.AnswerMultiLevelListActivity;
import com.xywy.askforexpert.module.discovery.answer.constants.AnswerShareConstants;
import com.xywy.askforexpert.module.discovery.medicine.common.MyConstant;
import com.xywy.askforexpert.module.discovery.medicine.common.MyRxBus;
import com.xywy.askforexpert.module.doctorcircle.DiscussDetailActivity;
import com.xywy.askforexpert.module.main.media.MediaDetailActivity;
import com.xywy.askforexpert.module.main.patient.activity.PatientPersonInfoActiviy;
import com.xywy.askforexpert.module.main.service.media.InfoDetailActivity;
import com.xywy.askforexpert.module.main.service.person.PersonDetailActivity;
import com.xywy.askforexpert.module.main.subscribe.video.VideoNewsActivity;
import com.xywy.askforexpert.module.message.HappyBirthFragment;
import com.xywy.askforexpert.module.message.health.HealthyUserInfoDetailActivity;
import com.xywy.askforexpert.module.message.imgroup.ContactService;
import com.xywy.askforexpert.module.message.msgchat.AlertDialog;
import com.xywy.askforexpert.module.message.msgchat.ChatFragment;
import com.xywy.askforexpert.module.message.msgchat.ChatMainActivity;
import com.xywy.askforexpert.module.message.msgchat.ChatSendMessageHelper;
import com.xywy.askforexpert.module.message.msgchat.ContextMenus;
import com.xywy.askforexpert.module.message.msgchat.ShowBigImage;
import com.xywy.askforexpert.module.message.msgchat.WebUrlOpenActivity;
import com.xywy.askforexpert.module.message.usermsg.DiscussSettingsActivity;
import com.xywy.easeWrapper.EMChatManager;
import com.xywy.easeWrapper.EaseConstant;
import com.xywy.easeWrapper.EaseImageUtils;
import com.xywy.easeWrapper.HXSDKHelper;
import com.xywy.easeWrapper.utils.SmileUtils;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.hyphenate.chat.EMMessage.Status.INPROGRESS;

/**
 * 消息的Adapter
 * stone
 * 2017/11/1 下午5:51
 */
public class MessageFragmentAdapter extends BaseAdapter {

    public static final String IMAGE_DIR = "chat/image/";
    public static final String VOICE_DIR = "chat/audio/";
    public static final String VIDEO_DIR = "chat/video";
    private final static String TAG = "msg";
    private static final int MESSAGE_TYPE_RECV_TXT = 0;
    private static final int MESSAGE_TYPE_SENT_TXT = 1;
    private static final int MESSAGE_TYPE_SENT_IMAGE = 2;
    private static final int MESSAGE_TYPE_SENT_LOCATION = 3;
    private static final int MESSAGE_TYPE_RECV_LOCATION = 4;
    private static final int MESSAGE_TYPE_RECV_IMAGE = 5;
    private static final int MESSAGE_TYPE_SENT_VOICE = 6;
    private static final int MESSAGE_TYPE_RECV_VOICE = 7;
    private static final int MESSAGE_TYPE_SENT_VIDEO = 8;
    private static final int MESSAGE_TYPE_RECV_VIDEO = 9;
    private static final int MESSAGE_TYPE_SENT_FILE = 10;
    private static final int MESSAGE_TYPE_RECV_FILE = 11;
    private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 12;
    private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 13;
    private static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 14;
    private static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 15;
    private static final int HANDLER_MESSAGE_REFRESH_LIST = 0;

    //stone
    private static final int MESSAGE_TYPE_SENT_PRECISION = 16;
    private static final int MESSAGE_TYPE_RECV_PRECISION = 17;

    EMMessage[] messages = null;
    private String username;
    private LayoutInflater inflater;
    private Fragment activity;
    // reference to conversation object in chatsdk
    private EMConversation conversation;
    private ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
    Handler handler = new Handler() {

        @Override
        public void handleMessage(android.os.Message message) {
            switch (message.what) {
                case HANDLER_MESSAGE_REFRESH_LIST:
                    // UI线程不能直接使用conversation.getAllMessages()
                    // 否则在UI刷新过程中，如果收到新的消息，会导致并发问题
                    messages = conversation.getAllMessages().toArray(new EMMessage[conversation.getAllMessages().size()]);
                    LogUtils.d("adapter conversation.getAllMessages()个数="+conversation.getAllMessages().size());
                    notifyDataSetChanged();
                    //stone 新添加mIsSetBottom判断
                    if (mIsSetBottom && activity instanceof ChatFragment) {
                        ListView listView = ((ChatFragment) activity).getListView();
                        listView.setSelection(listView.getCount() - 1);
                    }
                    mIsSetBottom = true;
                    break;
                default:
            }
        }
    };
    private Context context;
    private String toHeadImge;
    private String toRealname;
    private Map<String, Timer> timers = new Hashtable<String, Timer>();
    private FinalBitmap fb;
    private String isBirthday = "";
    private boolean isPrecision = false;
    /**
     * 身份类型 sid uid
     */
    private String type = "";
    private int subIndex = 0;
    private boolean mIsSetBottom = true;

    public MessageFragmentAdapter(Context context, Fragment activity, String username, String toHeadImge, String toRealname, int chatType, String type) {
        this.username = username;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.activity = activity;
        this.toHeadImge = toHeadImge;
        this.toRealname = toRealname;
        this.type = type;
        this.conversation = EMChatManager.getInstance().getConversation(username);
        fb = FinalBitmap.create(context, false);
    }

    public void stRealName(String toRealname) {
        this.toRealname = toRealname;
    }

    public void stHeadImg(String toHeadImge) {
        this.toHeadImge = toHeadImge;

    }

    /**
     * 获取item数
     */
    public int getCount() {
        return messages == null ? 0 : messages.length;
    }

    /**
     * 刷新页面
     */
    public void refresh() {
        if (handler.hasMessages(HANDLER_MESSAGE_REFRESH_LIST)) {
            return;
        }

        scheduledThreadPool.schedule(new Runnable() {
            @Override
            public void run() {
                LogUtils.i("delay 500 MILLISECONDS");
                //防止服务端还未将数据推送到最近咨询的接口，
                // 延时500毫秒通知MedicineAssistantActivity刷新数据
                //收到患者发过来的消息，表明有患者咨询了医生，这时，要通知MedicineAssistantActivity
                //页面从新请求最近咨询患者的接口了
                MyRxBus.notifyLatestPatientInfoChanged();
            }
        }, 500, TimeUnit.MILLISECONDS);
        android.os.Message msg = handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST);
        handler.sendMessage(msg);
    }

    /**
     * 刷新页面,不设置置底
     */
    public void refreshNoSetSelection() {
        mIsSetBottom = false;
        refresh();
    }

    public EMMessage getItem(int position) {
        if (messages != null && position < messages.length) {
            return messages[position];
        }
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * 获取item类型数
     */
    public int getViewTypeCount() {
        return 18;
    }


    //是否是开处方 stone
    private JSONObject isPrescriptionMsg(EMMessage message) {
        JSONObject prescription = null;
        try {
            //判断是否含有处方字段
            prescription = message.getJSONObjectAttribute("prescription");
            return prescription;
        } catch (HyphenateException e) {
            e.printStackTrace();
            return prescription;
        }
    }

    /**
     * 获取item类型
     */
    public int getItemViewType(int position) {
        EMMessage message = getItem(position);
        if (message == null) {
            return -1;
        }
        if (message.getType() == Type.TXT) {
            if (message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                return message.direct() == Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE_CALL : MESSAGE_TYPE_SENT_VOICE_CALL;
            } else if (message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
                return message.direct() == Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO_CALL : MESSAGE_TYPE_SENT_VIDEO_CALL;
            }
            //stone 处方
            else if (isPrescriptionMsg(message) != null) {
                return message.direct() == Direct.RECEIVE ? MESSAGE_TYPE_RECV_PRECISION : MESSAGE_TYPE_SENT_PRECISION;
            }
            return message.direct() == Direct.RECEIVE ? MESSAGE_TYPE_RECV_TXT : MESSAGE_TYPE_SENT_TXT;
        }
        if (message.getType() == Type.IMAGE) {
            return message.direct() == Direct.RECEIVE ? MESSAGE_TYPE_RECV_IMAGE : MESSAGE_TYPE_SENT_IMAGE;

        }
        if (message.getType() == Type.LOCATION) {
            return message.direct() == Direct.RECEIVE ? MESSAGE_TYPE_RECV_LOCATION : MESSAGE_TYPE_SENT_LOCATION;
        }
        if (message.getType() == Type.VOICE) {
            return message.direct() == Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE : MESSAGE_TYPE_SENT_VOICE;
        }
        if (message.getType() == Type.VIDEO) {
            return message.direct() == Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO : MESSAGE_TYPE_SENT_VIDEO;
        }
        if (message.getType() == Type.FILE) {
            return message.direct() == Direct.RECEIVE ? MESSAGE_TYPE_RECV_FILE : MESSAGE_TYPE_SENT_FILE;
        }

        return -1;// invalid
    }

    private View createViewByMessage(EMMessage message, int position) {
        switch (message.getType()) {

            case IMAGE:
                return message.direct() == Direct.RECEIVE ? inflater.inflate(R.layout.row_received_picture, null) : inflater.inflate(R.layout.row_sent_picture, null);

            case VOICE:
                return message.direct() == Direct.RECEIVE ? inflater.inflate(R.layout.row_received_voice, null) : inflater.inflate(R.layout.row_sent_voice, null);
            // case VIDEO:
            // return message.direct() == EMMessage.Direct.RECEIVE ?
            // inflater.inflate(R.layout.row_received_video, null) :
            // inflater.inflate(
            // R.layout.row_sent_video, null);
            // case FILE:
            // return message.direct() == EMMessage.Direct.RECEIVE ?
            // inflater.inflate(R.layout.row_received_file, null) :
            // inflater.inflate(
            // R.layout.row_sent_file, null);
            default:
                // 语音通话
                //                 if
                //                 (message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_VOICE_CALL,
                //                 false))
                //                 return message.direct() == EMMessage.Direct.RECEIVE ?
                //                 inflater.inflate(R.layout.row_received_voice_call, null) :
                //                 inflater
                //                 .inflate(R.layout.row_sent_voice_call, null);
                //                 // 视频通话
                //                 else
                //                 if
                //                 (message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_VIDEO_CALL,
                //                 false)){
                //                     return message.direct() == EMMessage.Direct.RECEIVE ?
                //                             inflater.inflate(R.layout.row_received_video_call, null) :
                //                             inflater
                //                                     .inflate(R.layout.row_sent_video_call, null);
                //                 }

                String msgtype = message.getStringAttribute("newMsgType", "");
                isBirthday = message.getStringAttribute(HXSDKHelper.FU_CADR, "");
//                isPrecision = isPrescriptionMsg(message);
                DLog.i(TAG, "msgtype.." + msgtype + "====zhufu" + isBirthday);
                // if (!TextUtils.isEmpty(msgtype) & "share".equals(msgtype))
                // {
                return message.direct() == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_share_received_message, null) : inflater.inflate(R.layout.row_share_sent_message, null);
            // } else
            // {
            // return message.direct() == EMMessage.Direct.RECEIVE ? inflater
            // .inflate(R.layout.row_received_message, null)
            // : inflater.inflate(R.layout.row_sent_message, null);
            // }

        }

    }

    @SuppressLint("NewApi")
    public View getView(final int position, View convertView, ViewGroup parent) {
        final EMMessage message = getItem(position);
        ChatType chatType = message.getChatType();
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = createViewByMessage(message, position);

            if (message.getType() == Type.IMAGE) {
                try {
                    holder.iv = ((ImageView) convertView.findViewById(R.id.iv_sendPicture));
                    holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
                    holder.tv = (TextView) convertView.findViewById(R.id.percentage);
                    holder.pb = (ProgressBar) convertView.findViewById(R.id.progressBar);
                    holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
                    holder.tv_usernick = (TextView) convertView.findViewById(R.id.tv_userid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (message.getType() == Type.TXT) {
                try {
                    //处方 stone
                    holder.rl_precision = convertView.findViewById(R.id.rl_precision);
                    holder.m_tv_name = (TextView) convertView.findViewById(R.id.m_tv_name);
                    holder.m_tv_sex = (TextView) convertView.findViewById(R.id.m_tv_sex);
                    holder.m_tv_drug = (TextView) convertView.findViewById(R.id.m_tv_drug);
                    holder.m_tv_usage = (TextView) convertView.findViewById(R.id.m_tv_usage);
                    holder.m_tv_detail = (TextView) convertView.findViewById(R.id.m_tv_detail);

                    holder.group_cmd = (TextView) convertView.findViewById(R.id.group_cmd);
                    holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
                    holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
                    holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
                    // 这里是文字内容
                    holder.tv = (TextView) convertView.findViewById(R.id.tv_chatcontent);
                    holder.tv_usernick = (TextView) convertView.findViewById(R.id.tv_userid);
                    String msgtype = message.getStringAttribute("newMsgType", "");
                    DLog.i(TAG, "msgtype2.." + msgtype);
                    holder.tv_share_title = (TextView) convertView.findViewById(R.id.tv_share_title);
                    holder.img_share_head = (ImageView) convertView.findViewById(R.id.img_share_head);
                    holder.lin_chatcontent = (LinearLayout) convertView.findViewById(R.id.lin_chatcontent);
                    holder.birthday_ll = (LinearLayout) convertView.findViewById(R.id.birthday_ll);
                    holder.rel_share = (RelativeLayout) convertView.findViewById(R.id.rel_share);
                    //添加分享名片夹

                    holder.share_card_rl = (RelativeLayout) convertView.findViewById(R.id.share_card_rl);
                    holder.card_share_img = (ImageView) convertView.findViewById(R.id.card_share_img);
                    holder.card_share_dpart_tv = (TextView) convertView.findViewById(R.id.card_share_dpart_tv);
                    holder.card_share_name_tv = (TextView) convertView.findViewById(R.id.card_share_name_tv);
                    holder.card_share_title_tv = (TextView) convertView.findViewById(R.id.card_share_title_tv);
                    holder.card_share_hospital_tv = (TextView) convertView.findViewById(R.id.card_share_hospital_tv);

                    //添加病例研讨班 LinearLayout seminar_ll ,default_ll;

                    //                    RelativeLayout seminar_img_rl;
                    //
                    //                    ImageView seminar_img_iv;
                    //
                    //                    TextView seminar_title_tv,seminar_img_tv,seminar_content_tv;

                    holder.seminar_ll = (LinearLayout) convertView.findViewById(R.id.seminar_ll);
                    holder.default_rl = (RelativeLayout) convertView.findViewById(R.id.default_rl);
                    holder.seminar_img_rl = (RelativeLayout) convertView.findViewById(R.id.seminar_img_rl);
                    holder.seminar_img_iv = (ImageView) convertView.findViewById(R.id.seminar_img_iv);
                    holder.seminar_title_tv = (TextView) convertView.findViewById(R.id.seminar_title_tv);
                    holder.seminar_img_tv = (TextView) convertView.findViewById(R.id.seminar_img_tv);
                    holder.seminar_content_tv = (TextView) convertView.findViewById(R.id.seminar_content_tv);

                    //stone 处方非处方
                    if (isPrescriptionMsg(message) != null) {
                        holder.tv.setVisibility(GONE);
                        holder.lin_chatcontent.setVisibility(VISIBLE);
                        holder.share_card_rl.setVisibility(GONE);
                        holder.rel_share.setVisibility(GONE);
                        holder.rl_precision.setVisibility(VISIBLE);
//                        if (holder.birthday_ll != null) {
//                            holder.birthday_ll.setVisibility(VISIBLE);
//                        }
                    } else {
                        if (holder.rl_precision != null) {
                            holder.rl_precision.setVisibility(GONE);
                        }

                        if (!TextUtils.isEmpty(msgtype) & "share".equals(msgtype)) {
                            holder.tv.setVisibility(GONE);

                        } else if (!TextUtils.isEmpty(msgtype) & "shareNameCard".equals(msgtype)) {
                            holder.tv.setVisibility(GONE);
                        } else if (!TextUtils.isEmpty(msgtype) & "shareNameDc".equals(msgtype)) {
                            holder.tv.setVisibility(GONE);
                        } else if (!TextUtils.isEmpty(msgtype) && ("seminar".equals(msgtype) || "zixun".equals(msgtype))) {
                            holder.tv.setVisibility(GONE);
                            //底部栏隐藏
                            activity.getView().findViewById(R.id.bar_bottom).setVisibility(GONE);

                        } else if (isBirthday.equals("birthday") && TextUtils.isEmpty(msgtype)) {
                            holder.lin_chatcontent.setVisibility(GONE);
                            holder.birthday_ll.setVisibility(VISIBLE);
                        } else {
                            holder.tv.setVisibility(VISIBLE);

                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (message.getType() == Type.VOICE) {
                try {
                    holder.relative = (LinearLayout) convertView.findViewById(R.id.relative);
                    holder.iv = ((ImageView) convertView.findViewById(R.id.iv_voice));
                    holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
                    holder.tv = (TextView) convertView.findViewById(R.id.tv_length);
                    holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
                    holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
                    holder.tv_usernick = (TextView) convertView.findViewById(R.id.tv_userid);
                    holder.iv_read_status = (ImageView) convertView.findViewById(R.id.iv_unread_voice);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                holder.group_cmd = (TextView) convertView.findViewById(R.id.group_cmd);
                holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
                holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
                holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
                // 这里是文字内容
                holder.tv = (TextView) convertView.findViewById(R.id.tv_chatcontent);
                holder.tv_usernick = (TextView) convertView.findViewById(R.id.tv_userid);
                String msgtype = message.getStringAttribute("newMsgType", "");
                DLog.i(TAG, "msgtype2.." + msgtype);
                holder.tv_share_title = (TextView) convertView.findViewById(R.id.tv_share_title);
                holder.img_share_head = (ImageView) convertView.findViewById(R.id.img_share_head);
                holder.lin_chatcontent = (LinearLayout) convertView.findViewById(R.id.lin_chatcontent);
                holder.birthday_ll = (LinearLayout) convertView.findViewById(R.id.birthday_ll);
                holder.rel_share = (RelativeLayout) convertView.findViewById(R.id.rel_share);
                //添加分享名片夹

                holder.share_card_rl = (RelativeLayout) convertView.findViewById(R.id.share_card_rl);
                holder.card_share_img = (ImageView) convertView.findViewById(R.id.card_share_img);
                holder.card_share_dpart_tv = (TextView) convertView.findViewById(R.id.card_share_dpart_tv);
                holder.card_share_name_tv = (TextView) convertView.findViewById(R.id.card_share_name_tv);
                holder.card_share_title_tv = (TextView) convertView.findViewById(R.id.card_share_title_tv);
                holder.card_share_hospital_tv = (TextView) convertView.findViewById(R.id.card_share_hospital_tv);

                //添加病例研讨班 LinearLayout seminar_ll ,default_ll;

                //                    RelativeLayout seminar_img_rl;
                //
                //                    ImageView seminar_img_iv;
                //
                //                    TextView seminar_title_tv,seminar_img_tv,seminar_content_tv;

                holder.seminar_ll = (LinearLayout) convertView.findViewById(R.id.seminar_ll);
                holder.default_rl = (RelativeLayout) convertView.findViewById(R.id.default_rl);
                holder.seminar_img_rl = (RelativeLayout) convertView.findViewById(R.id.seminar_img_rl);
                holder.seminar_img_iv = (ImageView) convertView.findViewById(R.id.seminar_img_iv);
                holder.seminar_title_tv = (TextView) convertView.findViewById(R.id.seminar_title_tv);
                holder.seminar_img_tv = (TextView) convertView.findViewById(R.id.seminar_img_tv);
                holder.seminar_content_tv = (TextView) convertView.findViewById(R.id.seminar_content_tv);

                if (!TextUtils.isEmpty(msgtype) & "share".equals(msgtype)) {
                    holder.tv.setVisibility(GONE);

                } else if (!TextUtils.isEmpty(msgtype) & "shareNameCard".equals(msgtype)) {
                    holder.tv.setVisibility(GONE);
                } else if (!TextUtils.isEmpty(msgtype) & "shareNameDc".equals(msgtype)) {
                    holder.tv.setVisibility(GONE);
                } else if (!TextUtils.isEmpty(msgtype) && ("seminar".equals(msgtype) || "zixun".equals(msgtype))) {
                    holder.tv.setVisibility(GONE);
                    //底部栏隐藏
                    activity.getView().findViewById(R.id.bar_bottom).setVisibility(GONE);

                } else if (isBirthday.equals("birthday") && TextUtils.isEmpty(msgtype)) {
                    holder.lin_chatcontent.setVisibility(GONE);
                    holder.birthday_ll.setVisibility(VISIBLE);
                } else {
                    holder.tv.setVisibility(VISIBLE);

                }
            }

            //            else if(message.getType() == Type.LOCATION) {
            //                holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
            //                // 这里是文字内容
            //                holder.tv = (TextView) convertView.findViewById(R.id.tv_chatcontent);
            //                // try {
            //                // holder.iv_avatar = (ImageView)
            //                // convertView.findViewById(R.id.iv_userhead);
            //                // holder.tv = (TextView)
            //                // convertView.findViewById(R.id.tv_location);
            //                // holder.pb = (ProgressBar)
            //                // convertView.findViewById(R.id.pb_sending);
            //                // holder.staus_iv = (ImageView)
            //                // convertView.findViewById(R.id.msg_status);
            //                // holder.tv_usernick = (TextView)
            //                // convertView.findViewById(R.id.tv_userid);
            //                // } catch (Exception e) {
            //                // }
            //            } else if(message.getType() == Type.VIDEO) {
            //                holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
            //                // 这里是文字内容
            //                holder.tv = (TextView) convertView.findViewById(R.id.tv_chatcontent);
            //                // try {
            //                // holder.iv = ((ImageView)
            //                // convertView.findViewById(R.id.chatting_content_iv));
            //                // holder.iv_avatar = (ImageView)
            //                // convertView.findViewById(R.id.iv_userhead);
            //                // holder.tv = (TextView)
            //                // convertView.findViewById(R.id.percentage);
            //                // holder.pb = (ProgressBar)
            //                // convertView.findViewById(R.id.progressBar);
            //                // holder.staus_iv = (ImageView)
            //                // convertView.findViewById(R.id.msg_status);
            //                // holder.size = (TextView)
            //                // convertView.findViewById(R.id.chatting_size_iv);
            //                // holder.timeLength = (TextView)
            //                // convertView.findViewById(R.id.chatting_length_iv);
            //                // holder.playBtn = (ImageView)
            //                // convertView.findViewById(R.id.chatting_status_btn);
            //                // holder.container_status_btn = (LinearLayout)
            //                // convertView.findViewById(R.id.container_status_btn);
            //                // holder.tv_usernick = (TextView)
            //                // convertView.findViewById(R.id.tv_userid);
            //                //
            //                // } catch (Exception e) {
            //                // }
            //            } else if(message.getType() == Type.FILE) {
            //                holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
            //                // 这里是文字内容
            //                holder.tv = (TextView) convertView.findViewById(R.id.tv_chatcontent);
            //                // try {
            //                // holder.iv_avatar = (ImageView)
            //                // convertView.findViewById(R.id.iv_userhead);
            //                // holder.tv_file_name = (TextView)
            //                // convertView.findViewById(R.id.tv_file_name);
            //                // holder.tv_file_size = (TextView)
            //                // convertView.findViewById(R.id.tv_file_size);
            //                // holder.pb = (ProgressBar)
            //                // convertView.findViewById(R.id.pb_sending);
            //                // holder.staus_iv = (ImageView)
            //                // convertView.findViewById(R.id.msg_status);
            //                // holder.tv_file_download_state = (TextView)
            //                // convertView.findViewById(R.id.tv_file_state);
            //                // holder.ll_container = (LinearLayout)
            //                // convertView.findViewById(R.id.ll_file_container);
            //                // // 这里是进度值
            //                // holder.tv = (TextView)
            //                // convertView.findViewById(R.id.percentage);
            //                // } catch (Exception e) {
            //                // }
            //                // try {
            //                // holder.tv_usernick = (TextView)
            //                // convertView.findViewById(R.id.tv_userid);
            //                // } catch (Exception e) {
            //                // }
            //
            //            }
//            holder.tv.setVisibility(VISIBLE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 群聊时，显示接收的消息的发送人的名称
        if (chatType == ChatType.GroupChat && message.direct() == Direct.RECEIVE) {
            // demo里使用username代码nick

            try {
                //                String name = message.getStringAttribute("fromRealName");
                GroupModel groupModel = ContactService.getInstance().getGroupInfo(message.getTo());
                String chatid = message.getFrom();
                chatid = chatid.substring(chatid.indexOf("_") + 1, chatid.length());
                if (groupModel != null && chatid.equals(groupModel.getOwner())) {
                    holder.tv_usernick.setText("管理员");
                    holder.tv_usernick.setVisibility(VISIBLE);
                } else {
                    holder.tv_usernick.setText("");
                    holder.tv_usernick.setVisibility(GONE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        // 如果是发送的消息并且不是群聊消息，显示已读textview
        if (message.direct() == EMMessage.Direct.SEND && chatType != EMMessage.ChatType.GroupChat) {
            holder.tv_ack = (TextView) convertView.findViewById(R.id.tv_ack);
            holder.tv_delivered = (TextView) convertView.findViewById(R.id.tv_delivered);
            if (holder.tv_ack != null) {
                // if (message.isAcked()) {
                // if (holder.tv_delivered != null) {
                // holder.tv_delivered.setVisibility(View.INVISIBLE);
                // }
                // holder.tv_ack.setVisibility(View.VISIBLE);
                // }
                // else {
                // holder.tv_ack.setVisibility(View.INVISIBLE);
                //
                // // check and display msg delivered ack status
                // if (holder.tv_delivered != null) {
                // if (message.isDelivered) {
                // holder.tv_delivered.setVisibility(View.VISIBLE);
                // } else {
                // holder.tv_delivered.setVisibility(View.INVISIBLE);
                // }
                // holder.tv_delivered.setVisibility(View.VISIBLE);
                // }
                // }
            }
        } else {
            // 如果是文本或者地图消息并且不是group messgae，显示的时候给对方发送已读回执
            if ((message.getType() == EMMessage.Type.TXT || message.getType() == EMMessage.Type.LOCATION) && !message.isAcked() && chatType != EMMessage.ChatType.GroupChat) {
                // 不是语音通话记录
                if (!message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    try {
                        EMChatManager.getInstance().ackMessageRead(message.getFrom(), message.getMsgId());
                        // 发送已读回执
                        message.setAcked(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // 设置用户头像
        setUserAvatar(message, holder.iv_avatar);

        DLog.i(TAG, "message" + message.getFrom());
        if (holder.birthday_ll != null && holder.birthday_ll.getVisibility() == VISIBLE) {
            holder.birthday_ll.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    HappyBirthFragment fragment = HappyBirthFragment.newInstance();
                    fragment.show(((FragmentActivity) context).getSupportFragmentManager(), "1");
                }
            });
        }
        holder.iv_avatar.setOnClickListener(new MyOnclick(message.getFrom()));
        switch (message.getType()) {
            // 根据消息type显示item
            case IMAGE: // 图片
                handleImageMessage(message, holder, position, convertView);
                break;
            case TXT: // 文本
                // if (message.getBooleanAttribute(
                // EaseConstant.MESSAGE_ATTR_IS_VOICE_CALL, false)
                // || message.getBooleanAttribute(
                // EaseConstant.MESSAGE_ATTR_IS_VIDEO_CALL, false))
                // // 音视频通话
                // handleCallMessage(message, holder, position);
                // else
                handleTextMessage(message, holder, position);
                break;
            // case LOCATION: // 位置
            // handleLocationMessage(message, holder, position, convertView);
            // break;色s
            case VOICE: // 语音
                handleVoiceMessage(message, holder, position, convertView);
                break;
            // case VIDEO: // 视频
            // handleVideoMessage(message, holder, position, convertView);
            // break;
            // case FILE: // 一般文件
            // handleFileMessage(message, holder, position, convertView);
            // break;
            default:
                // not supported
                handleOtherMessage(message, holder, position);
        }

        if (message.direct() == Direct.SEND) {
            View statusView = convertView.findViewById(R.id.msg_status);
            // 重发按钮点击事件
            statusView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    // 显示重发消息的自定义alertdialog
                    Intent intent = new Intent(activity.getActivity(), AlertDialog.class);
                    intent.putExtra("title", activity.getString(R.string.confirm_resend));
                    //                    intent.putExtra("title", activity.getString(R.string.resend));
                    intent.putExtra("cancel", true);
                    intent.putExtra("position", position);
                    if (message.getType() == Type.TXT) {
                        activity.startActivityForResult(intent, ChatFragment.REQUEST_CODE_TEXT);
                    } else if (message.getType() == Type.VOICE) {
                        activity.startActivityForResult(intent, ChatFragment.REQUEST_CODE_VOICE);
                    } else if (message.getType() == Type.IMAGE) {
                        activity.startActivityForResult(intent, ChatFragment.REQUEST_CODE_PICTURE);
                    } else if (message.getType() == Type.LOCATION) {
                        activity.startActivityForResult(intent, ChatFragment.REQUEST_CODE_LOCATION);
                    } else if (message.getType() == Type.FILE) {
                        activity.startActivityForResult(intent, ChatFragment.REQUEST_CODE_FILE);
                    } else if (message.getType() == Type.VIDEO) {
                        activity.startActivityForResult(intent, ChatFragment.REQUEST_CODE_VIDEO);
                    }

                }
            });

        } else {
            // final String st = context.getResources().getString(
            // R.string.Into_the_blacklist);
            // // 长按头像，移入黑名单
            // holder.iv_avatar.setOnLongClickListener(new OnLongClickListener()
            // {
            //
            // @Override
            // public boolean onLongClick(View v)
            // {
            // Intent intent = new Intent(activity.getActivity(),
            // AlertDialog.class);
            // intent.putExtra("msg", st);
            // intent.putExtra("cancel", true);
            // intent.putExtra("position", position);
            // activity.startActivityForResult(intent,
            // ChatFragment.REQUEST_CODE_ADD_TO_BLACKLIST);
            // return true;
            // }
            // });
        }

        TextView timestamp = (TextView) convertView.findViewById(R.id.timestamp);

        if (position == 0) {
            timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
            timestamp.setVisibility(VISIBLE);
        } else {
            // 两条消息时间离得如果稍长，显示时间
            EMMessage prevMessage = getItem(position - 1);
            if (prevMessage != null && DateUtils.isCloseEnough(message.getMsgTime(), prevMessage.getMsgTime())) {
                timestamp.setVisibility(GONE);
            } else {
                timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
                timestamp.setVisibility(VISIBLE);
            }
        }
        return convertView;
    }

    /**
     * 显示用户头像
     *
     * @param message
     * @param imageView
     */
    private void setUserAvatar(EMMessage message, ImageView imageView) {
        if (message.direct() == Direct.SEND) {
            // 显示自己头像
            // UserUtils.setUserAvatar(context,
            // EMChatManager.getInstance().getCurrentUser(), imageView);

            fb.display(imageView, YMApplication.getLoginInfo().getData().getPhoto());
        } else {
            // UserUtils.setUserAvatar(context, message.getFrom(), imageView);

            fb.configLoadfailImage(R.drawable.icon_photo_def);
            fb.configLoadingImage(R.drawable.icon_photo_def);
            if (message.getChatType() == ChatType.GroupChat) {
                try {
                    fb.display(imageView, message.getStringAttribute("fromAvatar"));
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            } else {
                fb.display(imageView, toHeadImge);
            }

            // YMApplication.
        }
    }

    /**
     * 文本消息 stone
     *
     * @param message
     * @param holder
     * @param position
     */
    private void handleTextMessage(final EMMessage message, ViewHolder holder, final int position) {
        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        Spannable span = SmileUtils.getSmiledText(context, txtBody.getMessage());
        String text = txtBody.getMessage();
        span.toString().replaceAll("\\s+", "");
        holder.iv_avatar.setVisibility(VISIBLE);
        holder.lin_chatcontent.setVisibility(VISIBLE);
        holder.group_cmd.setVisibility(GONE);
        // 设置内容
        final String msgtype = message.getStringAttribute("newMsgType", "");

        //stone 处方非处方
        final JSONObject prescription = isPrescriptionMsg(message);
        if (prescription != null) {
            try {
                String drug = prescription.getString("drugInformation");
                holder.m_tv_drug.setText("药品:" + drug);
                String name = prescription.getString("name");
                holder.m_tv_name.setText("姓名:" + name);
                String sex = prescription.getString("sex");
                if (sex.equals(MyConstant.NOT_DEFINED)) {
                    holder.m_tv_sex.setVisibility(GONE);
                } else {
                    holder.m_tv_sex.setVisibility(VISIBLE);
                    holder.m_tv_sex.setText("性别:" + sex);
                }
                String usage = prescription.getString("usage");
                holder.m_tv_usage.setText("用法:" + usage);
                String detail = prescription.getString("detail");
                holder.m_tv_detail.setText(detail);
                //跳转到查看处方详情页面
                holder.rl_precision.setOnClickListener(new MyDetail(prescription.getString("id")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {


            if (!TextUtils.isEmpty(msgtype) && "share".equals(msgtype)) {
                if (holder.default_rl != null && holder.default_rl.getVisibility() == GONE) {
                    holder.default_rl.setVisibility(VISIBLE);
                }
                if (holder.seminar_ll != null && holder.seminar_ll.getVisibility() == VISIBLE) {
                    holder.seminar_ll.setVisibility(GONE);
                }
                if (holder.share_card_rl != null && holder.share_card_rl.getVisibility() == VISIBLE) {
                    holder.share_card_rl.setVisibility(GONE);
                }
                holder.tv.setVisibility(GONE);
                try {
                    String msgString = message.getStringAttribute("msgBody", "");
                    Gson gson = new Gson();
                    final ShareCardInfo sc = gson.fromJson(msgString, ShareCardInfo.class);
                    DLog.i(TAG, "link url share" + gson.toJson(sc));
                    if (sc != null) {
                        // String comment = sc.getCommentTxt();
                        // if (TextUtils.isEmpty(comment))
                        // {
                        // holder.tv.setVisibility(View.GONE);
                        // } else
                        // {
                        // holder.tv.setVisibility(View.VISIBLE);
                        // holder.tv.setText(position+".n."+sc.getCommentTxt());
                        // }
                        holder.rel_share.setVisibility(VISIBLE);
                        holder.tv_share_title.setText(sc.getTitle());
                        if (TextUtils.isEmpty(sc.getImageUrl())) {
                            holder.img_share_head.setBackgroundResource(R.drawable.icon_photo_def);
                        } else {
                            fb.display(holder.img_share_head, sc.getImageUrl());
                            fb.configLoadfailImage(R.drawable.icon_photo_def);
                            fb.configLoadingImage(R.drawable.img_default_bg);

                        }
                        DLog.i(TAG, "link url share" + sc.getTitle() + "sc.getTitle()" + sc.getTitle() + "sc.getShareUrl()" + sc.getShareUrl() + "posts_id" + sc.getPosts_id() + "shareSource = " + sc.getShareSource());
                        holder.lin_chatcontent.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                String shareSoure = sc.getShareSource();
                                if (shareSoure != null && "5".equals(shareSoure)) { // 原生视频资讯
                                    Intent intent = new Intent(context, VideoNewsActivity.class);
                                    intent.putExtra(VideoNewsActivity.NEWS_ID_INTENT_KEY, sc.getPosts_id());
                                    intent.putExtra(VideoNewsActivity.VIDEO_TITLE_INTENT_KEY, sc.getTitle());
                                    intent.putExtra(VideoNewsActivity.VIDEO_UUID_INTENT_KEY, sc.getShareUu());
                                    intent.putExtra(VideoNewsActivity.VIDEO_VUID_INTENT_KEY, sc.getShareVu());
                                    context.startActivity(intent);
                                } else if (shareSoure != null && "6".equals(shareSoure)) {
                                    // 医学试题首页

                                    String shareAnswerid = "", shareAnswerTitle = "", answerType = "", shareAnswerVersion = "";
                                    String share_other = sc.getPosts_id();
                                    if (share_other.contains("|")) {
                                        String[] split = share_other.split("\\|");
                                        if (split.length > 0) {
                                            shareAnswerid = split[0];
                                        }
                                        if (split.length > 1) {
                                            shareAnswerTitle = split[1];
                                        }

                                        if (split.length > 2) {
                                            answerType = split[2];
                                        }

                                        if (split.length > 3) {
                                            shareAnswerVersion = split[3];
                                        }

                                    }
                                    if (!TextUtils.isEmpty(answerType) && answerType.equals(AnswerShareConstants.shareSourceType_Home)) {
                                        AnswerMainActivity.start((Activity) context);
                                    } else if (!TextUtils.isEmpty(answerType) && answerType.equals(AnswerShareConstants.shareSourceType_List)) {
                                        AnswerMultiLevelListActivity.start((Activity) context, shareAnswerid, shareAnswerTitle);
                                    } else if (!TextUtils.isEmpty(answerType) && answerType.equals(AnswerShareConstants.shareSourceType_Detail)) {
                                        AnswerDetailActivity.start((Activity) context, shareAnswerid, shareAnswerTitle, shareAnswerVersion, -1);
                                    } else {
                                        AnswerMainActivity.start((Activity) context);
                                    }
                                } else {
                                    if (sc.getShareUrl().contains(CommonUrl.H5_EXAM)) {
                                        Intent intent = new Intent(context, CommonWebViewActivity.class);
                                        intent.putExtra("isfrom", sc.getTitle());
                                        intent.putExtra("content_url", sc.getShareUrl() + YMApplication.getPID());
                                        intent.putExtra("imageUrl", sc.getImageUrl());
                                        intent.putExtra("description", "");
                                        context.startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(context, InfoDetailActivity.class);
                                        intent.putExtra("title", sc.getTitle());
                                        intent.putExtra("url", sc.getShareUrl());
                                        intent.putExtra("imageurl", sc.getImageUrl());
                                        intent.putExtra("ids", sc.getPosts_id());
                                        context.startActivity(intent);
                                    }
                                }

                                // T.shortToast(
                                // "分享链接"+sc.getShareUrl().toString());

                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (!TextUtils.isEmpty(msgtype) && "shareNameCard".equals(msgtype)) {
                holder.tv.setVisibility(GONE);
                if (holder.seminar_ll != null && holder.seminar_ll.getVisibility() == VISIBLE) {
                    holder.seminar_ll.setVisibility(GONE);
                }
                if (holder.default_rl != null && holder.default_rl.getVisibility() == GONE) {
                    holder.default_rl.setVisibility(VISIBLE);
                }
                if (holder.rel_share != null && holder.rel_share.getVisibility() == VISIBLE) {
                    holder.rel_share.setVisibility(GONE);
                }
                try {
                    String msgString = message.getStringAttribute("msgBody", "");

                    DLog.i(TAG, "收到的拓展消息shareNameCard" + msgString);
                    Gson gson = new Gson();
                    final ShareCardInfo sc = gson.fromJson(msgString, ShareCardInfo.class);

                    DLog.i("收到的拓展类:json==", gson.toJson(sc));
                    if (sc != null) {
                        //名片夹样式分享

                        holder.share_card_rl.setVisibility(VISIBLE);
                        holder.card_share_name_tv.setText(sc.getFriendName());
                        holder.card_share_hospital_tv.setText(sc.getfCardHospital());
                        holder.card_share_title_tv.setText(sc.getfCardTitle());
                        holder.card_share_dpart_tv.setText(sc.getfCardDpart());
                        //                    holder.card_share_hospital_tv
                        if (TextUtils.isEmpty(sc.getImageUrl())) {
                            holder.card_share_img.setBackgroundResource(R.drawable.icon_photo_def);
                        } else {
                            fb.display(holder.card_share_img, sc.getImageUrl());
                            fb.configLoadfailImage(R.drawable.icon_photo_def);
                            fb.configLoadingImage(R.drawable.img_default_bg);
                        }

                        holder.lin_chatcontent.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                if (sc.getfType() != null && sc.getfType().equals("media")) {
                                    Intent intent = new Intent(context, MediaDetailActivity.class);
                                    intent.putExtra("mediaId", sc.getFriendXywyId());
                                    context.startActivity(intent);
                                    return;
                                }
                                String form = sc.getHuanxinId();
                                String type = form.substring(0, 3);
                                DLog.i(TAG, "环信idshareNameCard" + form);

                                if (((ChatMainActivity) context).isHealthyUser()) {
                                    Intent intent = new Intent();
                                    String id = form.substring(form.indexOf("_") + 1, form.length());
                                    intent.setClass(context, HealthyUserInfoDetailActivity.class);
                                    intent.putExtra(HealthyUserInfoDetailActivity.PATIENT_ID_INTENT_KEY, id);
                                    context.startActivity(intent);
                                } else {
                                    if (type.equals("uid")) {
                                        Intent intent = new Intent(context, PatientPersonInfoActiviy.class);
                                        intent.putExtra("hx_userid", form);
                                        String str = form.substring(form.indexOf("_") + 1, form.length());
                                        intent.putExtra("uid", str);
                                        activity.getActivity().startActivityForResult(intent, 28);
                                        // activity.getActivity().finish();
                                    } else if (type.equals("did")) {
                                        Intent intent = new Intent();
                                        String id = form.substring(form.indexOf("_") + 1, form.length());
                                        intent.setClass(context, PersonDetailActivity.class);
                                        intent.putExtra("uuid", id);
                                        intent.putExtra("isDoctor", 3 + "");
                                        activity.getActivity().startActivityForResult(intent, 28);
                                        // activity.getActivity().finish();
                                    } else if (type.equals("sid")) {
                                        Intent intent = new Intent(context, DiscussSettingsActivity.class);
                                        String str = form.substring(form.indexOf("_") + 1, form.length());
                                        intent.putExtra("uuid", str);
                                        intent.putExtra("isDoctor", "2");
                                        activity.startActivityForResult(intent, 28);
                                    }
                                }

                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (!TextUtils.isEmpty(msgtype) && "shareCardDc".equals(msgtype)) {
                holder.tv.setVisibility(GONE);
                if (holder.seminar_ll != null && holder.seminar_ll.getVisibility() == VISIBLE) {
                    holder.seminar_ll.setVisibility(GONE);
                }
                if (holder.default_rl != null && holder.default_rl.getVisibility() == GONE) {
                    holder.default_rl.setVisibility(VISIBLE);
                }
                if (holder.rel_share != null && holder.rel_share.getVisibility() == VISIBLE) {
                    holder.rel_share.setVisibility(GONE);
                }
                try {
                    String msgString = message.getStringAttribute("msgBody", "");

                    DLog.i(TAG, "收到的拓展消息" + msgString);
                    Gson gson = new Gson();
                    final ShareCardInfo sc = gson.fromJson(msgString, ShareCardInfo.class);
                    if (sc != null) {
                        //名片夹样式分享

                        holder.share_card_rl.setVisibility(VISIBLE);
                        holder.card_share_name_tv.setText(sc.getFriendName());
                        holder.card_share_hospital_tv.setText(sc.getfCardHospital());
                        holder.card_share_title_tv.setText(sc.getfCardTitle());
                        holder.card_share_dpart_tv.setText(sc.getfCardDpart());
                        //                    holder.card_share_hospital_tv
                        if (TextUtils.isEmpty(sc.getImageUrl())) {
                            holder.card_share_img.setBackgroundResource(R.drawable.icon_photo_def);
                        } else {
                            fb.display(holder.card_share_img, sc.getImageUrl());
                            fb.configLoadfailImage(R.drawable.icon_photo_def);
                            fb.configLoadingImage(R.drawable.img_default_bg);
                        }

                        holder.lin_chatcontent.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                String form = sc.getHuanxinId();
                                String type = form.substring(0, 3);
                                if (type.equals("uid")) {
                                    Intent intent = new Intent(context, PatientPersonInfoActiviy.class);
                                    intent.putExtra("hx_userid", form);
                                    String str = form.substring(form.indexOf("_") + 1, form.length());
                                    intent.putExtra("uid", str);
                                    activity.getActivity().startActivityForResult(intent, 28);
                                    // activity.getActivity().finish();
                                } else if (type.equals("did")) {
                                    Intent intent = new Intent(context, PersonDetailActivity.class);
                                    String str = form.substring(form.indexOf("_") + 1, form.length());
                                    intent.putExtra("uuid", str);
                                    intent.putExtra("isDoctor", 3 + "");
                                    activity.getActivity().startActivityForResult(intent, 28);
                                    // activity.getActivity().finish();
                                } else if (type.equals("sid")) {
                                    Intent intent = new Intent(context, DiscussSettingsActivity.class);
                                    String str = form.substring(form.indexOf("_") + 1, form.length());
                                    intent.putExtra("uuid", str);
                                    intent.putExtra("isDoctor", "2");
                                    activity.startActivityForResult(intent, 28);
                                }

                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (!TextUtils.isEmpty(msgtype) && ("seminar".equals(msgtype) || "zixun".equals(msgtype))) {
                holder.tv.setVisibility(GONE);
                holder.default_rl.setVisibility(GONE);
                String jsonString = message.getStringAttribute("msgBody", "");
                DLog.d(TAG, "msgbody jsonString = " + jsonString);
                final Seminar seminar = new Seminar();
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    seminar.setDynamicid(jsonObject.optString("dynamicid"));
                    seminar.setTitle(jsonObject.optString("title"));
                    seminar.setType(jsonObject.optInt("type"));
                    seminar.setImageUrl(jsonObject.optString("imageUrl"));
                    seminar.setCountent(jsonObject.optString("countent"));

                    seminar.setLink(jsonObject.optString("link"));
                    seminar.setZixunid(jsonObject.optString("zixunid"));
                    if (jsonObject.optString("uu") != null && !"".equals(jsonObject.optString("uu"))) {
                        seminar.setUu(jsonObject.optString("uu"));
                    }
                    if (jsonObject.optString("vu") != null && !"".equals(jsonObject.optString("vu"))) {
                        seminar.setVu(jsonObject.optString("vu"));
                    }

                    holder.seminar_ll.setVisibility(VISIBLE);
                    holder.seminar_title_tv.setText(seminar.getTitle());

                    if (seminar.getType() == 1) {
                        holder.seminar_content_tv.setVisibility(GONE);
                        holder.seminar_img_rl.setVisibility(VISIBLE);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.seminar_img_iv.getLayoutParams();
                        int screenWidth = AppUtils.getScreenWidth((Activity) context);
                        layoutParams.width = screenWidth;
                        layoutParams.height = screenWidth * 190 / 322;
                        holder.seminar_img_iv.setLayoutParams(layoutParams);
                        fb.configLoadingImage(null);
                        fb.display(holder.seminar_img_iv, seminar.getImageUrl());
                        holder.seminar_img_tv.setText(seminar.getCountent());
                    } else {
                        holder.seminar_img_rl.setVisibility(GONE);
                        holder.seminar_content_tv.setVisibility(VISIBLE);
                        holder.seminar_content_tv.setText(seminar.getCountent());
                    }

                    holder.seminar_ll.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            Intent intent = new Intent();
                            if ("zixun".equals(msgtype)) { // 资讯推送
                                DLog.d(TAG, "uu id = " + seminar.getUu() + ", vu id = " + seminar.getVu());
                                if (seminar.getUu() != null && seminar.getVu() != null) {
                                    intent.setClass(context, VideoNewsActivity.class);
                                    intent.putExtra(VideoNewsActivity.VIDEO_TITLE_INTENT_KEY, seminar.getTitle());
                                    intent.putExtra(VideoNewsActivity.VIDEO_UUID_INTENT_KEY, seminar.getUu());
                                    intent.putExtra(VideoNewsActivity.VIDEO_VUID_INTENT_KEY, seminar.getVu());
                                    intent.putExtra(VideoNewsActivity.NEWS_ID_INTENT_KEY, seminar.getZixunid());
                                } else {
                                    intent.setClass(context, InfoDetailActivity.class);
                                    intent.putExtra("url", seminar.getLink());
                                    intent.putExtra("ids", seminar.getZixunid());
                                    intent.putExtra("title", seminar.getTitle());
                                    intent.putExtra("imageurl", seminar.getImageUrl());
                                }
                                context.startActivity(intent);
                            } else if ("seminar".equals(msgtype)) {
                                StatisticalTools.eventCount(context, "xxCaseDiscussDetail");
                                DLog.d(TAG, "seminar uu id = " + seminar.getUu() + ", vu id = " + seminar.getVu());
                                if (seminar.getUu() != null && seminar.getVu() != null
                                    /*&& !"".equals(seminar.getUu()) && !"".equals(seminar.getVu())*/) {
                                    intent.setClass(context, VideoNewsActivity.class);
                                    intent.putExtra(VideoNewsActivity.VIDEO_TITLE_INTENT_KEY, seminar.getTitle());
                                    intent.putExtra(VideoNewsActivity.VIDEO_UUID_INTENT_KEY, seminar.getUu());
                                    intent.putExtra(VideoNewsActivity.VIDEO_VUID_INTENT_KEY, seminar.getVu());
                                    intent.putExtra(VideoNewsActivity.NEWS_ID_INTENT_KEY, seminar.getDynamicid());
                                } else {
                                    intent.setClass(context, DiscussDetailActivity.class);
                                    intent.putExtra("dynamicid", seminar.getDynamicid());
                                    intent.putExtra("type", 1);
                                }
                                context.startActivity(intent);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //        else if (!TextUtils.isEmpty(msgtype) && ("callVideo".equals(msgtype))) {
            //
            //            holder.rel_share.setVisibility(View.GONE);
            //            holder.share_card_rl.setVisibility(View.GONE);
            //            if (holder.seminar_ll != null && holder.seminar_ll.getVisibility() == View.VISIBLE)
            //                holder.seminar_ll.setVisibility(View.GONE);
            //            if (holder.default_rl != null && holder.default_rl.getVisibility() == View.GONE)
            //                holder.default_rl.setVisibility(View.VISIBLE);
            //            holder.tv.setVisibility(View.VISIBLE);
            //            String callingState = message.getStringAttribute("callingState", "");
            //            if (message.direct() == Direct.RECEIVE) {
            ////                if ("对方已挂断".equals(span.toString())) {
            ////                    holder.tv.setText("已挂断", BufferType.SPANNABLE);
            ////                } else if ("对方未接听".equals(span.toString())) {
            ////                    holder.tv.setText("未接听", BufferType.SPANNABLE);
            ////                } else {
            ////                    holder.tv.setText(span, BufferType.SPANNABLE);
            ////                }
            ////                holder.tv.setText("好友像你发起视频通话，快升级到最新版本与好友尽情沟通吧", BufferType.SPANNABLE);
            //            } else {
            //                holder.tv.setText(span, BufferType.SPANNABLE);
            //            }
            //
            //        }
            else if (!TextUtils.isEmpty(msgtype) && ("groupCMD".equals(msgtype))) {

                holder.lin_chatcontent.setVisibility(GONE);
                holder.iv_avatar.setVisibility(GONE);
                holder.group_cmd.setVisibility(VISIBLE);
                holder.group_cmd.setText(span.toString());

            } else {
                holder.rel_share.setVisibility(GONE);
                holder.share_card_rl.setVisibility(GONE);
                if (holder.seminar_ll != null && holder.seminar_ll.getVisibility() == VISIBLE) {
                    holder.seminar_ll.setVisibility(GONE);
                }
                if (holder.default_rl != null && holder.default_rl.getVisibility() == GONE) {
                    holder.default_rl.setVisibility(VISIBLE);
                }
                holder.tv.setVisibility(VISIBLE);
                //			holder.tv.setText(position + "..." + span, BufferType.SPANNABLE);
                final Intent intent = new Intent(context, WebUrlOpenActivity.class);
                String url = getUrl(span.toString());
                SpannableString newSapn = new SpannableString(text);
                DLog.i("tag", "web url" + url.replaceAll("\\s+", ""));
                intent.putExtra("url", url.replaceAll("\\s+", ""));
                if (!url.equals("")) {
                    newSapn.setSpan(new IntentSpan(intent), subIndex, newSapn.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    //                    newSapn.setSpan(new URLSpan(url.toString()),subIndex,newSapn.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    //                    newSapn.setSpan(new ForegroundColorSpan(Color.WHITE),subIndex,newSapn.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    //                    holder.tv.setMovementMethod(LinkMovementMethod.getInstance());
                }
                if (!TextUtils.isEmpty(url)) {
                    String ss = txtBody.getMessage();
                    //                    String text = span.toString().replace(url,"");
                    holder.tv.setText(ss);
                    holder.tv.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            context.startActivity(intent);
                        }
                    });
                    holder.tv.setText(newSapn, BufferType.SPANNABLE);
                } else {
                    holder.tv.setText(span, BufferType.SPANNABLE);
                }


            }
        }

        // 设置长按事件监听
        holder.tv.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                activity.startActivityForResult((new Intent(activity.getActivity(), ContextMenus.class)).putExtra("position", position).putExtra("type", EMMessage.Type.TXT.ordinal()), ChatFragment.REQUEST_CODE_CONTEXT_MENU);
                return true;
            }
        });

        if (message.direct() == EMMessage.Direct.SEND) {
            switch (message.status()) {
                case SUCCESS: // 发送成功
                    holder.pb.setVisibility(GONE);
                    holder.staus_iv.setVisibility(GONE);
                    break;
                case FAIL: // 发送失败
                    holder.pb.setVisibility(GONE);
                    holder.staus_iv.setVisibility(VISIBLE);
                    break;
                case INPROGRESS: // 发送中
                    holder.pb.setVisibility(VISIBLE);
                    holder.staus_iv.setVisibility(GONE);
                    break;
                default:
                    // 发送消息
                    sendMsgInBackground(message, holder);
            }
        }
    }

    /**
     * 音视频通话记录
     *
     * @param message
     * @param holder
     * @param position
     */

    private void handleCallMessage(EMMessage message, ViewHolder holder, final int position) {
        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        holder.tv.setText(txtBody.getMessage());

    }

    /**
     * 额外消息类型
     *
     * @param message
     * @param holder
     * @param position
     */
    private void handleOtherMessage(EMMessage message, ViewHolder holder, final int position) {
        //        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        holder.lin_chatcontent.setVisibility(VISIBLE);
        if (holder.seminar_ll != null && holder.seminar_ll.getVisibility() == VISIBLE) {
            holder.seminar_ll.setVisibility(GONE);
        }
        if (holder.default_rl != null && holder.default_rl.getVisibility() == GONE) {
            holder.default_rl.setVisibility(VISIBLE);
        }
        holder.rel_share.setVisibility(GONE);
        holder.share_card_rl.setVisibility(GONE);
        holder.tv.setText("消息类型不支持，请更新至最新版本");
        ChatSendMessageHelper.sendCMDMessage(message.getFrom(), "", toRealname, toHeadImge);
        holder.tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WebUrlOpenActivity.class);
                intent.putExtra("url", "http://app.xywy.com/code.php?app=expert ");
                activity.getActivity().startActivity(intent);

            }
        });

    }

    /**
     * 图片消息
     *
     * @param message
     * @param holder
     * @param position
     * @param convertView
     */
    private void handleImageMessage(final EMMessage message, final ViewHolder holder, final int position, View convertView) {
        holder.pb.setTag(position);
        holder.iv.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                activity.startActivityForResult((new Intent(activity.getActivity(), ContextMenus.class)).putExtra("position", position).putExtra("type", Type.IMAGE.ordinal()), ChatFragment.REQUEST_CODE_CONTEXT_MENU);
                return true;
            }
        });

        // 接收方向的消息
        if (message.direct() == Direct.RECEIVE) {
            // "itis receive msg";
            if (message.status() == INPROGRESS) {
                // "!!!! back receive";
                holder.iv.setImageResource(R.drawable.default_image);
                showDownloadImageProgress(message, holder);
            } else {
                // "!!!! not back receive, show image directly");
                holder.pb.setVisibility(GONE);
                holder.tv.setVisibility(GONE);
                holder.iv.setImageResource(R.drawable.default_image);
                EMImageMessageBody imgBody = (EMImageMessageBody) message.getBody();
                LogUtils.d("localUrl=" + imgBody.getLocalUrl());
                LogUtils.d("localThumbUrl=" + imgBody.thumbnailLocalPath());

                LogUtils.d("remoteUrl=" + imgBody.getRemoteUrl());
                LogUtils.d("remoteThumbUrl=" + imgBody.getThumbnailUrl());

                if (imgBody.getLocalUrl() != null) {
                    // String filePath = imgBody.getLocalUrl();
                    String remotePath = imgBody.getRemoteUrl().replaceAll("100_100_", "");
                    String filePath = ImageUtils.getImagePath(remotePath);
                    String thumbRemoteUrl = imgBody.getThumbnailUrl();
                    String thumbnailPath = ImageUtils.getThumbnailImagePath(thumbRemoteUrl);
                    showImageView(thumbnailPath, holder.iv, filePath, remotePath, thumbRemoteUrl, message);
                }
            }
            return;
        }

        // 发送的消息
        // process send message
        // send pic, show the pic directly
        EMImageMessageBody imgBody = (EMImageMessageBody) message.getBody();
        String filePath = imgBody.getLocalUrl().replaceAll("100_100_", "");
        if (filePath != null && new File(filePath).exists()) {
            showImageView(ImageUtils.getThumbnailImagePath(filePath), holder.iv, filePath, null, null, message);
        } else {
            showImageView(ImageUtils.getThumbnailImagePath(filePath), holder.iv, filePath, IMAGE_DIR, null, message);
        }

        switch (message.status()) {
            case SUCCESS:
                holder.pb.setVisibility(GONE);
                holder.tv.setVisibility(GONE);
                holder.staus_iv.setVisibility(GONE);
                break;
            case FAIL:
                holder.pb.setVisibility(GONE);
                holder.tv.setVisibility(GONE);
                holder.staus_iv.setVisibility(VISIBLE);
                break;
            case INPROGRESS:
                holder.staus_iv.setVisibility(GONE);
                holder.pb.setVisibility(VISIBLE);
                holder.tv.setVisibility(VISIBLE);
                if (timers.containsKey(message.getMsgId())) {
                    return;
                }
                // set a timer
                final Timer timer = new Timer();
                timers.put(message.getMsgId(), timer);

                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        if (activity.getActivity() != null) {
                            activity.getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    holder.pb.setVisibility(VISIBLE);
                                    holder.tv.setVisibility(VISIBLE);
                                    holder.tv.setText(message.progress() + "%");
                                    if (message.status() == EMMessage.Status.SUCCESS) {
                                        holder.pb.setVisibility(GONE);
                                        holder.tv.setVisibility(GONE);
                                        // message.setSendingStatus(Message.SENDING_STATUS_SUCCESS);
                                        timer.cancel();
                                    } else if (message.status() == EMMessage.Status.FAIL) {
                                        holder.pb.setVisibility(GONE);
                                        holder.tv.setVisibility(GONE);
                                        // message.setSendingStatus(Message.SENDING_STATUS_FAIL);
                                        // message.setProgress(0);
                                        holder.staus_iv.setVisibility(VISIBLE);
                                        Toast.makeText(activity.getActivity(), activity.getString(R.string.send_fail) + activity.getString(R.string.connect_failuer_toast), Toast.LENGTH_SHORT).show();
                                        timer.cancel();
                                    }

                                }
                            });

                        }

                    }
                }, 0, 500);
                break;
            default:
                sendPictureMessage(message, holder);
        }
    }

    // /**
    // * 视频消息
    // *
    // * @param message
    // * @param holder
    // * @param position
    // * @param convertView
    // */
    // private void handleVideoMessage(final EMMessage message, final ViewHolder
    // holder, final int position, View convertView) {
    //
    // VideoMessageBody videoBody = (VideoMessageBody) message.getBody();
    // // final File image=new File(PathUtil.getInstance().getVideoPath(),
    // // videoBody.getFileName());
    // String localThumb = videoBody.getLocalThumb();
    //
    // holder.iv.setOnLongClickListener(new OnLongClickListener() {
    //
    // @Override
    // public boolean onLongClick(View v) {
    // activity.startActivityForResult(
    // new Intent(activity, ContextMenu.class).putExtra("position",
    // position).putExtra("type",
    // EMMessage.Type.VIDEO.ordinal()), ChatActivity.REQUEST_CODE_CONTEXT_MENU);
    // return true;
    // }
    // });
    //
    // if (localThumb != null) {
    //
    // showVideoThumbView(localThumb, holder.iv, videoBody.getThumbnailUrl(),
    // message);
    // }
    // if (videoBody.getLength() > 0) {
    // String time = DateUtils.toTimeBySecond(videoBody.getLength());
    // holder.timeLength.setText(time);
    // }
    // // holder.playBtn.setImageResource(R.drawable.video_download_btn_nor);
    //
    // if (message.direct() == EMMessage.Direct.RECEIVE) {
    // if (videoBody.getVideoFileLength() > 0) {
    // String size = TextFormater.getDataSize(videoBody.getVideoFileLength());
    // holder.size.setText(size);
    // }
    // } else {
    // if (videoBody.getLocalUrl() != null && new
    // File(videoBody.getLocalUrl()).exists()) {
    // String size = TextFormater.getDataSize(new
    // File(videoBody.getLocalUrl()).length());
    // holder.size.setText(size);
    // }
    // }
    //
    // if (message.direct() == EMMessage.Direct.RECEIVE) {
    //
    // // System.err.println("it is receive msg");
    // if (message.status() == EMMessage.Status.INPROGRESS) {
    // // System.err.println("!!!! back receive");
    // holder.iv.setImageResource(R.drawable.default_image);
    // showDownloadImageProgress(message, holder);
    //
    // } else {
    // // System.err.println("!!!! not back receive, show image directly");
    // holder.iv.setImageResource(R.drawable.default_image);
    // if (localThumb != null) {
    // showVideoThumbView(localThumb, holder.iv, videoBody.getThumbnailUrl(),
    // message);
    // }
    //
    // }
    //
    // return;
    // }
    // holder.pb.setTag(position);
    //
    // // until here ,deal with send video msg
    // switch (message.status()){
    // case SUCCESS:
    // holder.pb.setVisibility(View.GONE);
    // holder.staus_iv.setVisibility(View.GONE);
    // holder.tv.setVisibility(View.GONE);
    // break;
    // case FAIL:
    // holder.pb.setVisibility(View.GONE);
    // holder.tv.setVisibility(View.GONE);
    // holder.staus_iv.setVisibility(View.VISIBLE);
    // break;
    // case INPROGRESS:
    // if (timers.containsKey(message.getMsgId()))
    // return;
    // // set a timer
    // final Timer timer = new Timer();
    // timers.put(message.getMsgId(), timer);
    // timer.schedule(new TimerTask() {
    //
    // @Override
    // public void run() {
    // activity.runOnUiThread(new Runnable() {
    //
    // @Override
    // public void run() {
    // holder.pb.setVisibility(View.VISIBLE);
    // holder.tv.setVisibility(View.VISIBLE);
    // holder.tv.setText(message.progress + "%");
    // if (message.status() == EMMessage.Status.SUCCESS) {
    // holder.pb.setVisibility(View.GONE);
    // holder.tv.setVisibility(View.GONE);
    // // message.setSendingStatus(Message.SENDING_STATUS_SUCCESS);
    // timer.cancel();
    // } else if (message.status() == EMMessage.Status.FAIL) {
    // holder.pb.setVisibility(View.GONE);
    // holder.tv.setVisibility(View.GONE);
    // // message.setSendingStatus(Message.SENDING_STATUS_FAIL);
    // // message.setProgress(0);
    // holder.staus_iv.setVisibility(View.VISIBLE);
    // Toast.makeText(activity,
    // activity.getString(R.string.send_fail) +
    // activity.getString(R.string.connect_failuer_toast), 0)
    // .show();
    // timer.cancel();
    // }
    //
    // }
    // });
    //
    // }
    // }, 0, 500);
    // break;
    // default:
    // // sendMsgInBackground(message, holder);
    // sendPictureMessage(message, holder);
    //
    // }
    //
    // }

    /**
     * 语音消息
     *
     * @param message
     * @param holder
     * @param position
     * @param convertView
     */
    private void handleVoiceMessage(final EMMessage message, final ViewHolder holder, final int position, View convertView) {
        EMVoiceMessageBody voiceBody = (EMVoiceMessageBody) message.getBody();
        int voiceLeng = voiceBody.getLength();
        if (voiceLeng > 60) {
            holder.tv.setText(60 + "\"");
        } else {
            holder.tv.setText(voiceLeng + "\"");
        }

        holder.relative.setOnClickListener(new VoicePlayClickListener(message, holder.iv, holder.iv_read_status, this, activity, username));
        holder.iv.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                activity.startActivityForResult((new Intent(activity.getActivity(), ContextMenu.class)).putExtra("position", position).putExtra("type", Type.VOICE.ordinal()), ChatFragment.REQUEST_CODE_CONTEXT_MENU);
                return true;
            }
        });
        if (((ChatFragment) activity).playMsgId != null && ((ChatFragment) activity).playMsgId.equals(message.getMsgId()) && VoicePlayClickListener.isPlaying) {
            AnimationDrawable voiceAnimation;
            if (message.direct() == Direct.RECEIVE) {
                holder.iv.setImageResource(R.drawable.voice_from_icon);
            } else {
                holder.iv.setImageResource(R.drawable.voice_to_icon);
            }
            voiceAnimation = (AnimationDrawable) holder.iv.getDrawable();
            voiceAnimation.start();
        } else {
            if (message.direct() == Direct.RECEIVE) {
                holder.iv.setImageResource(R.drawable.chatfrom_voice_playing_f3);
            } else {
                //				holder.iv.setImageResource(R.drawable.chatto_voice_playing);
            }
        }

        if (message.direct() == Direct.RECEIVE) {
            if (message.isListened()) {
                // 隐藏语音未听标志
                holder.iv_read_status.setVisibility(View.INVISIBLE);
            } else {
                holder.iv_read_status.setVisibility(VISIBLE);
            }
            System.err.println("it is receive msg");
            if (message.status() == INPROGRESS) {
                holder.pb.setVisibility(VISIBLE);
                System.err.println("!!!! back receive");

                //// TODO: 16/8/17 replace by shijiazi
                message.setMessageStatusCallback(new EMCallBack() {
//                ((EMFileMessageBody) message.getBody()).setDownloadCallback(new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        if (activity.getActivity() != null) {
                            activity.getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    holder.pb.setVisibility(View.INVISIBLE);
                                    notifyDataSetChanged();
                                }
                            });

                        }

                    }

                    @Override
                    public void onProgress(int progress, String status) {
                    }

                    @Override
                    public void onError(int code, String message) {
                        if (activity.getActivity() != null) {
                            activity.getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    holder.pb.setVisibility(View.INVISIBLE);
                                }
                            });

                        }

                    }
                });

            } else {
                holder.pb.setVisibility(View.INVISIBLE);

            }
            return;
        }

        // until here, deal with send voice msg
        switch (message.status()) {
            case SUCCESS:
                holder.pb.setVisibility(GONE);
                holder.staus_iv.setVisibility(GONE);
                break;
            case FAIL:
                holder.pb.setVisibility(GONE);
                holder.staus_iv.setVisibility(VISIBLE);
                break;
            case INPROGRESS:
                holder.pb.setVisibility(VISIBLE);
                holder.staus_iv.setVisibility(GONE);
                break;
            default:
                sendMsgInBackground(message, holder);
        }
    }

    //
    // /**
    // * 文件消息
    // *
    // * @param message
    // * @param holder
    // * @param position
    // * @param convertView
    // */
    // private void handleFileMessage(final EMMessage message, final ViewHolder
    // holder, int position, View convertView) {
    // final EMNormalFileMessageBody fileMessageBody = (EMNormalFileMessageBody)
    // message.getBody();
    // final String filePath = fileMessageBody.getLocalUrl();
    // holder.tv_file_name.setText(fileMessageBody.getFileName());
    // holder.tv_file_size.setText(TextFormater.getDataSize(fileMessageBody.getFileSize()));
    // holder.ll_container.setOnClickListener(new OnClickListener() {
    //
    // @Override
    // public void onClick(View view) {
    // File file = new File(filePath);
    // if (file != null && file.exists()) {
    // // 文件存在，直接打开
    // FileUtils.openFile(file, (Activity) context);
    // } else {
    // // 下载
    // context.startActivity(new Intent(context,
    // ShowNormalFileActivity.class).putExtra("msgbody", fileMessageBody));
    // }
    // if (message.direct() == EMMessage.Direct.RECEIVE && !message.isAcked()) {
    // try {
    // EMChatManager.getInstance().ackMessageRead(message.getFrom(),
    // message.getMsgId());
    // message.setAcked(true);
    // } catch (HyphenateException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }
    // }
    // });
    // String st1 = context.getResources().getString(R.string.Have_downloaded);
    // String st2 = context.getResources().getString(R.string.Did_not_download);
    // if (message.direct() == EMMessage.Direct.RECEIVE) { // 接收的消息
    // System.err.println("it is receive msg");
    // File file = new File(filePath);
    // if (file != null && file.exists()) {
    // holder.tv_file_download_state.setText(st1);
    // } else {
    // holder.tv_file_download_state.setText(st2);
    // }
    // return;
    // }
    //
    // // until here, deal with send voice msg
    // switch (message.status()){
    // case SUCCESS:
    // holder.pb.setVisibility(View.INVISIBLE);
    // holder.tv.setVisibility(View.INVISIBLE);
    // holder.staus_iv.setVisibility(View.INVISIBLE);
    // break;
    // case FAIL:
    // holder.pb.setVisibility(View.INVISIBLE);
    // holder.tv.setVisibility(View.INVISIBLE);
    // holder.staus_iv.setVisibility(View.VISIBLE);
    // break;
    // case INPROGRESS:
    // if (timers.containsKey(message.getMsgId()))
    // return;
    // // set a timer
    // final Timer timer = new Timer();
    // timers.put(message.getMsgId(), timer);
    // timer.schedule(new TimerTask() {
    //
    // @Override
    // public void run() {
    // activity.runOnUiThread(new Runnable() {
    //
    // @Override
    // public void run() {
    // holder.pb.setVisibility(View.VISIBLE);
    // holder.tv.setVisibility(View.VISIBLE);
    // holder.tv.setText(message.progress + "%");
    // if (message.status() == EMMessage.Status.SUCCESS) {
    // holder.pb.setVisibility(View.INVISIBLE);
    // holder.tv.setVisibility(View.INVISIBLE);
    // timer.cancel();
    // } else if (message.status() == EMMessage.Status.FAIL) {
    // holder.pb.setVisibility(View.INVISIBLE);
    // holder.tv.setVisibility(View.INVISIBLE);
    // holder.staus_iv.setVisibility(View.VISIBLE);
    // Toast.makeText(activity,
    // activity.getString(R.string.send_fail) +
    // activity.getString(R.string.connect_failuer_toast), 0)
    // .show();
    // timer.cancel();
    // }
    //
    // }
    // });
    //
    // }
    // }, 0, 500);
    // break;
    // default:
    // // 发送消息
    // sendMsgInBackground(message, holder);
    // }
    //
    // }

    // /**
    // * 处理位置消息
    // *
    // * @param message
    // * @param holder
    // * @param position
    // * @param convertView
    // */
    // private void handleLocationMessage(final EMMessage message, final
    // ViewHolder holder, final int position, View convertView) {
    // TextView locationView = ((TextView)
    // convertView.findViewById(R.id.tv_location));
    // LocationMessageBody locBody = (LocationMessageBody) message.getBody();
    // locationView.setText(locBody.getAddress());
    // LatLng loc = new LatLng(locBody.getLatitude(), locBody.getLongitude());
    // locationView.setOnClickListener(new MapClickListener(loc,
    // locBody.getAddress()));
    // locationView.setOnLongClickListener(new OnLongClickListener() {
    // @Override
    // public boolean onLongClick(View v) {
    // activity.startActivityForResult(
    // (new Intent(activity, ContextMenu.class)).putExtra("position",
    // position).putExtra("type",
    // EMMessage.Type.LOCATION.ordinal()),
    // ChatActivity.REQUEST_CODE_CONTEXT_MENU);
    // return false;
    // }
    // });
    //
    // if (message.direct() == EMMessage.Direct.RECEIVE) {
    // return;
    // }
    // // deal with send message
    // switch (message.status()){
    // case SUCCESS:
    // holder.pb.setVisibility(View.GONE);
    // holder.staus_iv.setVisibility(View.GONE);
    // break;
    // case FAIL:
    // holder.pb.setVisibility(View.GONE);
    // holder.staus_iv.setVisibility(View.VISIBLE);
    // break;
    // case INPROGRESS:
    // holder.pb.setVisibility(View.VISIBLE);
    // break;
    // default:
    // sendMsgInBackground(message, holder);
    // }
    // }

    /**
     * 发送消息
     *
     * @param message
     * @param holder
     */
    public void sendMsgInBackground(final EMMessage message, final ViewHolder holder) {
        holder.staus_iv.setVisibility(GONE);
        holder.pb.setVisibility(VISIBLE);

        final long start = System.currentTimeMillis();
        message.setAttribute("fromRealName", YMApplication.getLoginInfo().getData().getRealname());
        message.setAttribute("fromAvatar", YMApplication.getLoginInfo().getData().getPhoto());

        message.setAttribute("toRealName", toRealname);
        message.setAttribute("toAvatar", toHeadImge);

        //stone 新添加的扩展字段区别是医脉搏还是用药助手
        if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
            message.setAttribute("source", "selldrug");
        }

        // 调用sdk发送异步发送方法
        EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

            @Override
            public void onSuccess() {
                // umeng自定义事件，
                sendEvent2Umeng(message, start);

                updateSendedView(message, holder);
            }

            @Override
            public void onError(int code, String error) {
                sendEvent2Umeng(message, start);

                updateSendedView(message, holder);

                //stone 发送失败重新登录
                YMUserService.ymLogout();

            }

            @Override
            public void onProgress(int progress, String status) {
            }

        });

    }

    /*
     * chat sdk will automatic download thumbnail image for the image message we
     * need to register callback show the download progress
     */
    private void showDownloadImageProgress(final EMMessage message, final ViewHolder holder) {
        System.err.println("!!! show download image progress");
        // final EMImageMessageBody msgbody = (EMImageMessageBody)
        // message.getBody();
        final EMFileMessageBody msgbody = (EMFileMessageBody) message.getBody();
        if (holder.pb != null) holder.pb.setVisibility(VISIBLE);
        if (holder.tv != null) holder.tv.setVisibility(VISIBLE);

        //// TODO: 16/8/17 replace by shijiazi

        message.setMessageStatusCallback(new EMCallBack() {

            @Override
            public void onSuccess() {
                if (activity.getActivity() != null) {
                    activity.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LogUtils.d("图片接收成功");
                            // message.setBackReceive(false);
                            if (message.getType() == EMMessage.Type.IMAGE) {
                                holder.pb.setVisibility(GONE);
                                holder.tv.setVisibility(GONE);
                            }
                            notifyDataSetChanged();
                        }
                    });
                }

            }

            @Override
            public void onError(int code, String message) {

            }

            @Override
            public void onProgress(final int progress, String status) {
                if (message.getType() == EMMessage.Type.IMAGE) {
                    if (activity.getActivity() != null) {
                        activity.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                holder.tv.setText(progress + "%");

                            }
                        });
                    }

                }

            }

        });
    }

    /*
     * send message with new sdk
     */
    private void sendPictureMessage(final EMMessage message, final ViewHolder holder) {
        try {
            String to = message.getTo();
            message.setAttribute("fromRealName", YMApplication.getLoginInfo().getData().getRealname());
            message.setAttribute("fromAvatar", YMApplication.getLoginInfo().getData().getPhoto());

            message.setAttribute("toRealName", toRealname);
            message.setAttribute("toAvatar", toHeadImge);

            //stone 新添加的扩展字段区别是医脉搏还是用药助手
            if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
                message.setAttribute("source", "selldrug");
            }
            // before send, update ui
            holder.staus_iv.setVisibility(GONE);
            holder.pb.setVisibility(VISIBLE);
            holder.tv.setVisibility(VISIBLE);
            holder.tv.setText("0%");

            final long start = System.currentTimeMillis();
            // if (chatType == ChatActivity.CHATTYPE_SINGLE) {
            EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

                @Override
                public void onSuccess() {
                    Log.d(TAG, "send image message successfully");
                    sendEvent2Umeng(message, start);
                    if (activity.getActivity() != null) {
                        activity.getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                // send success
                                holder.pb.setVisibility(GONE);
                                holder.tv.setVisibility(GONE);
                            }
                        });
                    }

                }

                @Override
                public void onError(int code, String error) {
                    sendEvent2Umeng(message, start);
                    if (activity.getActivity() != null) {
                        activity.getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                holder.pb.setVisibility(GONE);
                                holder.tv.setVisibility(GONE);
                                // message.setSendingStatus(Message.SENDING_STATUS_FAIL);
                                holder.staus_iv.setVisibility(VISIBLE);
                                Toast.makeText(activity.getActivity(), activity.getString(R.string.send_fail) + activity.getString(R.string.connect_failuer_toast), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }

                @Override
                public void onProgress(final int progress, String status) {
                    if (activity.getActivity() != null) {
                        activity.getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                holder.tv.setText(progress + "%");
                            }
                        });
                    }

                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新ui上消息发送状态
     *
     * @param message
     * @param holder
     */
    private void updateSendedView(final EMMessage message, final ViewHolder holder) {
        if (activity.getActivity() != null) {

            activity.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // send success
                    if (message.getType() == EMMessage.Type.VIDEO) {
                        holder.tv.setVisibility(GONE);
                    }
                    if (message.status() == EMMessage.Status.SUCCESS) {
                        // if (message.getType() == EMMessage.Type.FILE) {
                        // holder.pb.setVisibility(View.INVISIBLE);
                        // holder.staus_iv.setVisibility(View.INVISIBLE);
                        // } else {
                        // holder.pb.setVisibility(View.GONE);
                        // holder.staus_iv.setVisibility(View.GONE);
                        // }

                    } else if (message.status() == EMMessage.Status.FAIL) {
                        // if (message.getType() == EMMessage.Type.FILE) {
                        // holder.pb.setVisibility(View.INVISIBLE);
                        // } else {
                        // holder.pb.setVisibility(View.GONE);
                        // }
                        // holder.staus_iv.setVisibility(View.VISIBLE);
                        Toast.makeText(activity.getActivity(), activity.getString(R.string.send_fail) + activity.getString(R.string.connect_failuer_toast), Toast.LENGTH_SHORT).show();
                    }

                    notifyDataSetChanged();
                }
            });

        }
    }

    /**
     * load image into image view
     *
     * @param thumbernailPath
     * @param iv
     * @return the image exists or not
     */
    private boolean showImageView(final String thumbernailPath, final ImageView iv, final String localFullSizePath, String remoteDir, String remoteThumbUrl, final EMMessage message) {
        // String imagename =
        // localFullSizePath.substring(localFullSizePath.lastIndexOf("/") + 1,
        // localFullSizePath.length());
        // final String remote = remoteDir != null ? remoteDir+imagename :
        // imagename;
        final String remote = remoteDir;
        LogUtils.d("图片地址local = " + localFullSizePath);
        LogUtils.d("remoteUrl = " + remote);
        LogUtils.d("thumbnail = " + thumbernailPath);
        // first check if the thumbnail image already loaded into cache

        Bitmap bitmap = ImageCache.getInstance().get(thumbernailPath);
        if (bitmap != null) {
            // thumbnail image is already loaded, reuse the drawable
            iv.setImageBitmap(bitmap);
            setOnCLickListener(iv, localFullSizePath, message, remote);
            return true;
        } else if (null != remoteDir) {
            //微信 web界面发送的图片消息 服务端未使用环信默认发送图片方式 自己下载展示
            ImageLoadUtils.INSTANCE.loadImageView(iv, remoteDir);
            setOnCLickListener(iv, localFullSizePath, message, remote);
            return true;
        } else {
//            new LoadImageTask().execute(thumbernailPath, localFullSizePath, remote, message.getChatType(), iv, context, message);
            //stone 新添加
            setOnCLickListener(iv, localFullSizePath, message, remote);
            new AsyncTask<Object, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(Object... args) {
                    File file = new File(thumbernailPath);
                    EMImageMessageBody imgBody = (EMImageMessageBody) message.getBody();
                    if (file.exists()) {
                        return EaseImageUtils.decodeScaleImage(thumbernailPath, 300, 300);
                    } else if (new File(imgBody.thumbnailLocalPath()).exists()) {
                        return EaseImageUtils.decodeScaleImage(imgBody.thumbnailLocalPath(), 300, 300);
                    } else {
                        if (message.direct() == EMMessage.Direct.SEND) {
                            if (localFullSizePath != null && new File(localFullSizePath).exists()) {
                                return EaseImageUtils.decodeScaleImage(localFullSizePath, 300, 300);
                            } else {
                                return null;
                            }
                        } else {
                            if (imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
                                    imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING) {
                                refresh();
                            } else {
                                if (ImageCache.isNetWorkConnected(context)) {
                                    LogUtils.d("接收图片 重新下载");
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            EMClient.getInstance().chatManager().downloadThumbnail(message);
                                            LogUtils.d("接收图片 重新下载");
                                            refresh();
                                        }
                                    }).start();
                                }
                            }

                            return null;
                        }
                    }
                }

                protected void onPostExecute(Bitmap image) {
                    if (image != null) {
                        iv.setImageBitmap(image);
                        ImageCache.getInstance().put(thumbernailPath, image);
                    } else {
                        if (message.status() == EMMessage.Status.FAIL) {
                            if (ImageCache.isNetWorkConnected(context)) {
                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        EMChatManager.getInstance().asyncFetchMessage(message);
                                    }
                                }).start();
                            }
                        }
                    }
                }
            }.execute();
            return true;
        }

    }

    private void setOnCLickListener(ImageView iv, final String localFullSizePath, final EMMessage message, final String remote) {
        iv.setClickable(true);
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.err.println("image view on click");
                Intent intent = new Intent(activity.getActivity(), ShowBigImage.class);
                File file = new File(localFullSizePath);
                if (file.exists()) {
                    Uri uri = Uri.fromFile(file);
                    intent.putExtra("uri", uri);
                    System.err.println("here need to check why download everytime");
                } else {
                    // The local full size pic does not exist yet.
                    // ShowBigImage needs to download it from the server
                    // first
                    // intent.putExtra("", message.get);
                    EMImageMessageBody body = (EMImageMessageBody) message.getBody();
                    intent.putExtra("secret", body.getSecret());
                    intent.putExtra("remotepath", remote);
                }
                if (message != null && message.direct() == Direct.RECEIVE && !message.isAcked() && message.getChatType() != ChatType.GroupChat) {
                    try {
                        EMChatManager.getInstance().ackMessageRead(message.getFrom(), message.getMsgId());
                        message.setAcked(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                activity.startActivity(intent);
            }
        });
    }

    // /**
    // * 展示视频缩略图
    // *
    // * @param localThumb
    // * 本地缩略图路径
    // * @param iv
    // * @param thumbnailUrl
    // * 远程缩略图路径
    // * @param message
    // */
    // private void showVideoThumbView(String localThumb, ImageView iv, String
    // thumbnailUrl, final EMMessage message) {
    // // first check if the thumbnail image already loaded into cache
    // Bitmap bitmap = ImageCache.getInstance().get(localThumb);
    // if (bitmap != null) {
    // // thumbnail image is already loaded, reuse the drawable
    // iv.setImageBitmap(bitmap);
    // iv.setClickable(true);
    // iv.setOnClickListener(new View.OnClickListener() {
    //
    // @Override
    // public void onClick(View v) {
    // VideoMessageBody videoBody = (VideoMessageBody) message.getBody();
    // System.err.println("video view is on click");
    // Intent intent = new Intent(activity, ShowVideoActivity.class);
    // intent.putExtra("localpath", videoBody.getLocalUrl());
    // intent.putExtra("secret", videoBody.getSecret());
    // intent.putExtra("remotepath", videoBody.getRemoteUrl());
    // if (message != null && message.direct() == EMMessage.Direct.RECEIVE &&
    // !message.isAcked
    // && message.getChatType() != ChatType.GroupChat) {
    // message.setAcked(true);
    // try {
    // EMChatManager.getInstance().ackMessageRead(message.getFrom(),
    // message.getMsgId());
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    // activity.startActivity(intent);
    //
    // }
    // });
    //
    // } else {
    // new LoadVideoImageTask().execute(localThumb, thumbnailUrl, iv, activity,
    // message, this);
    // }
    //
    // }

    /**
     * umeng自定义事件统计
     *
     * @param message
     */
    private void sendEvent2Umeng(final EMMessage message, final long start) {
        // activity.runOnUiThread(new Runnable() {
        // public void run() {
        // long costTime = System.currentTimeMillis() - start;
        // Map<String, String> params = new HashMap<String, String>();
        // if(message.status() == EMMessage.Status.SUCCESS)
        // params.put("status", "success");
        // else
        // params.put("status", "failure");
        //
        // switch (message.getType()) {
        // case TXT:
        // case LOCATION:
        // MobclickAgent.onEventValue(activity, "text_msg", params, (int)
        // costTime);
        // MobclickAgent.onEventDuration(activity, "text_msg", (int) costTime);
        // break;
        // case IMAGE:
        // MobclickAgent.onEventValue(activity, "img_msg", params, (int)
        // costTime);
        // MobclickAgent.onEventDuration(activity, "img_msg", (int) costTime);
        // break;
        // case VOICE:
        // MobclickAgent.onEventValue(activity, "voice_msg", params, (int)
        // costTime);
        // MobclickAgent.onEventDuration(activity, "voice_msg", (int) costTime);
        // break;
        // case VIDEO:
        // MobclickAgent.onEventValue(activity, "video_msg", params, (int)
        // costTime);
        // MobclickAgent.onEventDuration(activity, "video_msg", (int) costTime);
        // break;
        // default:
        // break;
        // }
        //
        // }
        // });
    }

    /**
     * get url from s
     *
     * @param s
     * @return a url
     */
    public String getUrl(String s) {

        if (s.contains("http://") || s.contains("https://")) {
            int index = 0;
            char[] chars = s.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                DLog.i("tag", "char==" + chars[i]);
                if (chars[i] == 'h' && chars[i + 1] == 't' && chars[i + 2] == 't' && chars[i + 3] == 'p') {
                    index = i;
                    DLog.i("tag", "截取开始位置" + index);
                    break;
                }
            }
            subIndex = index;
            return s.substring(index, s.length());
        }

        if (!s.contains("http") && s.contains("www")) {
            int index = 0;
            char[] chars = s.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                DLog.i("tag", "char==" + chars[i]);
                if (chars[i] == 'w' && chars[i + 1] == 'w' && chars[i + 2] == 'w') {
                    index = i;
                    DLog.i("tag", "截取开始位置" + index);
                    break;
                }
            }
            subIndex = index;
            return s.substring(index, s.length());
        }

        return "";
    }

    // /*
    // * 点击地图消息listener
    // */
    // class MapClickListener implements View.OnClickListener {
    //
    // LatLng location;
    // String address;
    //
    // public MapClickListener(LatLng loc, String address) {
    // location = loc;
    // this.address = address;
    //
    // }
    //
    // @Override
    // public void onClick(View v) {
    // Intent intent;
    // intent = new Intent(context, BaiduMapActivity.class);
    // intent.putExtra("latitude", location.latitude);
    // intent.putExtra("longitude", location.longitude);
    // intent.putExtra("address", address);
    // activity.startActivity(intent);
    // }
    //
    // }

    public static class ViewHolder {
        //处方 stone
        View rl_precision;
        TextView m_tv_name;
        TextView m_tv_sex;
        TextView m_tv_drug;
        TextView m_tv_usage;
        TextView m_tv_detail;


        TextView group_cmd;
        LinearLayout relative;
        ImageView iv;
        TextView tv;
        ProgressBar pb;
        ImageView staus_iv;
        ImageView iv_avatar;
        TextView tv_usernick;
        // ImageView playBtn;
        TextView timeLength;
        TextView size;
        // LinearLayout container_status_btn;
        LinearLayout ll_container;
        ImageView iv_read_status;
        // 显示已读回执状态
        TextView tv_ack;
        // 显示送达回执状态
        TextView tv_delivered;

        TextView tv_file_name;
        TextView tv_file_size;
        TextView tv_file_download_state;

        // 分享
        TextView tv_share_title;
        ImageView img_share_head;
        LinearLayout lin_chatcontent;
        RelativeLayout rel_share;

        //分享名片夹
        TextView p_card_tv, card_share_name_tv, card_share_title_tv, card_share_dpart_tv, card_share_hospital_tv;
        /*
            名片夹头像
         */ ImageView card_share_img;
        //最外部设置隐藏显示
        RelativeLayout share_card_rl;

        //生日布局
        LinearLayout birthday_ll;

        //病例研讨班
        LinearLayout seminar_ll;

        //默认对话框
        RelativeLayout default_rl;

        RelativeLayout seminar_img_rl;

        ImageView seminar_img_iv;

        TextView seminar_title_tv, seminar_img_tv, seminar_content_tv;
    }

    class MyOnclick implements OnClickListener {
        String form;
        Intent intent;

        public MyOnclick(String form) {
            this.form = form;
        }

        @Override
        public void onClick(View arg0) {
            //stone 医生助手关闭头像点击,医脉正常
            if (YMApplication.sIsYSZS) {
                return;
            }

            DLog.i(TAG, "传回的好友userid" + form);
            String type = form.substring(0, 3);
            if (form.contains(Constants.QXYL_USER_HXID_MARK)) {
                StatisticalTools.eventCount(context, "ResidentHeadPortrait");
                Intent intent = new Intent(context, HealthyUserInfoDetailActivity.class);
                intent.putExtra(HealthyUserInfoDetailActivity.PATIENT_ID_INTENT_KEY, form.replaceAll(Constants.QXYL_USER_HXID_MARK, ""));
                context.startActivity(intent);
            } else {
                if (type.equals("uid")) {
                    intent = new Intent(context, PatientPersonInfoActiviy.class);
                    intent.putExtra("hx_userid", form);
                    String str = form.substring(form.indexOf("_") + 1, form.length());
                    intent.putExtra("uid", str);
                    activity.getActivity().startActivityForResult(intent, 28);
                    // activity.getActivity().finish();
                } else if (type.equals("did")) {
                    Intent intent = new Intent(context, PersonDetailActivity.class);
                    String str = form.substring(form.indexOf("_") + 1, form.length());
                    intent.putExtra("uuid", str);
                    intent.putExtra("isDoctor", 3 + "");            //isDoctor  ？？
                    activity.getActivity().startActivityForResult(intent, 28);
                    // activity.getActivity().finish();
                }
            }
        }

    }

    @SuppressLint("ParcelCreator")
    public class IntentSpan extends ClickableSpan implements ParcelableSpan {
        private Intent mIntent;

        public IntentSpan(Intent toActivity) {
            mIntent = toActivity;
        }

        @Override
        public void onClick(View sourceView) {
            Context context = sourceView.getContext();
            context.startActivity(mIntent);
        }

        @Override
        public int getSpanTypeId() {
            return 100;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flag) {
            // don't write to parcel
        }

        public Intent getIntent() {
            return mIntent;
        }
    }

    //处方详情
    class MyDetail implements OnClickListener {
        String id;

        public MyDetail(String id) {
            this.id = id;
        }

        @Override
        public void onClick(View arg0) {
            //跳转到查看处方详情页面
            YMOtherUtils.skip2PrecsciptionDetail(context, id);
        }

    }

}