package com.xywy.askforexpert.module.message;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.media.MediaList;
import com.xywy.askforexpert.model.notice.Notice;
import com.xywy.askforexpert.module.discovery.medicine.common.MyConstant;
import com.xywy.askforexpert.module.drug.OnlineRoomMessageListActivity;
import com.xywy.askforexpert.module.main.subscribe.SubscribeMediactivity;
import com.xywy.askforexpert.module.message.adapter.ChatAllHistoryAdapter;
import com.xywy.askforexpert.module.message.friend.AddCardHoldVerifyActiviy;
import com.xywy.askforexpert.module.message.friend.CardNewFriendActivity;
import com.xywy.askforexpert.module.message.msgchat.ChatMainActivity;
import com.xywy.askforexpert.module.message.msgchat.MsgZhuShou;
import com.xywy.askforexpert.module.message.msgchat.SerchbarActivity;
import com.xywy.askforexpert.module.message.notice.GetNoticeListImpl;
import com.xywy.askforexpert.module.message.notice.IGetNoticeListPresenter;
import com.xywy.askforexpert.module.message.notice.IGetNoticeListView;
import com.xywy.askforexpert.module.message.notice.NoticeListActivity;
import com.xywy.askforexpert.module.my.setting.utils.VersionUtils;
import com.xywy.askforexpert.widget.ActionItem;
import com.xywy.askforexpert.widget.TitlePopup;
import com.xywy.base.view.ProgressDialog;
import com.xywy.easeWrapper.EMChatManager;
import com.xywy.easeWrapper.EaseConstant;
import com.xywy.easeWrapper.db.InviteMessage;
import com.xywy.easeWrapper.db.InviteMessgeDao;
import com.xywy.retrofit.net.BaseData;
import com.xywy.util.LogUtils;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


/**
 * 消息列表/会话列表
 * modify 添加媒体号
 * 2016-3-15
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MessageInfoFragment extends Fragment implements OnClickListener, TitlePopup.OnItemOnClickListener,IGetNoticeListView {
    private static final String TAG = "MessageInfoFragment";

    private InputMethodManager inputMethodManager;
    private ListView listView;
    private ChatAllHistoryAdapter adapter;
    /**
     * 搜索
     */
    private LinearLayout serch_layout;
    public RelativeLayout errorItem;
    public TextView errorText;
    private boolean hidden;

    private LinearLayout no_data;
    private TextView tv_nodata_title;
    private ImageView img_nodata;
    private String uid;

    private ImageView msg_card_iv;
    private ImageButton message_right;
//    private TitlePopup menuPopup;
    Map<String, String> map;
//    private static final String ADD_FRIEND = "添加好友";
//    private static final String GROUP_CHAT = "发起群聊";
//    private static final String INVITE_FRIEND = "邀请好友";
//    private static final String MY_CARD = "我的名片";
//    private static final String ERWEIMA = "扫一扫";
    private SharedPreferences sp;
    public static List<EMConversation> conversationList = new ArrayList<EMConversation>();

    private ProgressDialog progressDialog;

    private IGetNoticeListPresenter iGetNoticeListPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getActivity().getSharedPreferences("save_user", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation_history, container, false);
        //这下面两行代码，之前医脉未注销
//        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        CommonUtils.setTransparentBar(toolbar, getActivity(), DensityUtils.dp2px(48));
        msg_card_iv = (ImageView) view.findViewById(R.id.msg_card_iv);
        message_right = (ImageButton) view.findViewById(R.id.message_right);
        RelativeLayout list_item_layout = (RelativeLayout) view.findViewById(R.id.list_item_layout);
        list_item_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(),OnlineRoomMessageListActivity.class));
            }
        });
        if (!YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
//            menuPopup = new TitlePopup(getActivity(), LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//            menuPopup.setItemOnClickListener(this);
//
//            menuPopup.cleanAction();
//            menuPopup.addAction(new ActionItem(getActivity(), ADD_FRIEND, R.drawable.add_friend_icon));
//            menuPopup.addAction(new ActionItem(getActivity(), GROUP_CHAT, R.drawable.group_chat_icon));
//            menuPopup.addAction(new ActionItem(getActivity(), INVITE_FRIEND, R.drawable.invite_friend));
//            menuPopup.addAction(new ActionItem(getActivity(), MY_CARD, R.drawable.my_card_icon));
//            menuPopup.addAction(new ActionItem(getActivity(), ERWEIMA, R.drawable.erweima_icon));
//            msg_card_iv.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startActivity(new Intent(YMApplication.getAppContext(), MsgFriendCardActivity.class));
//                }
//            });
//            message_right.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    StatisticalTools.eventCount(getActivity(), "NewTopPlus");
//                    menuPopup.show(v);
//                }
//            });
//
//            final String userid = YMUserService.isGuest() ? "0" : YMApplication.getPID();
//            DLog.d(TAG, "is msg tutorial showed? " + sp.getBoolean("msg_tutorial_show_" + userid, false));
//            if (!sp.getBoolean("msg_tutorial_show_" + userid, false)) {
//                message_right.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        menuPopup.show(message_right);
//                        MsgTutorialFragment tutorialDialog = MsgTutorialFragment.newInstance(R.layout.msg_tutorial_layout);
//                        tutorialDialog.show(getActivity().getSupportFragmentManager(), TAG);
//                        sp.edit().putBoolean("msg_tutorial_show_" + userid, true).apply();
//                    }
//                });
//            }
        } else {
            msg_card_iv.setVisibility(View.GONE);
            message_right.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
            return;
        }
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        errorItem = (RelativeLayout) getView().findViewById(R.id.rl_error_item);
        errorText = (TextView) errorItem.findViewById(R.id.tv_connect_errormsg);

        no_data = (LinearLayout) getView().findViewById(R.id.lin_nodata);
        tv_nodata_title = (TextView) getView().findViewById(R.id.tv_nodata_title);
        tv_nodata_title.setText("暂无消息");
        img_nodata = (ImageView) getView().findViewById(R.id.img_nodate);
        img_nodata.setBackgroundResource(R.drawable.no_message);
        if (!YMUserService.isGuest()) {
            if (YMApplication.getLoginInfo() != null) {
                uid = YMApplication.getLoginInfo().getData().getPid();
            }

        }


        listView = (ListView) getView().findViewById(R.id.list);
        adapter = new ChatAllHistoryAdapter(getActivity(), 1, conversationList);
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (adapter.getCount() == 0) {
                    if (isResumed()) {
                        no_data.setVisibility(View.VISIBLE);
                    }

                }
            }
        });
        // 设置adapter
        listView.setAdapter(adapter);

        final String st2 = getResources().getString(R.string.Cant_chat_with_yourself);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    EMConversation conversation = adapter.getItem(position);
                    String username = conversation.getUserName();

                    DLog.d(TAG, "msg info username = " + username);
                    if (username.equals("消息小助手")) {
                        Intent intent = new Intent(getActivity(), MsgZhuShou.class);
                        startActivity(intent);
                    } else if (username.equals("新朋友")) {
                        Intent intent = new Intent(getActivity(), CardNewFriendActivity.class);
                        startActivity(intent);
                    } else if (username.contains("qid")) {
                        // iniUpdata();
//                        iniUpdata_new();
                        VersionUtils.getInstance(getActivity()).checkVersionUpdate();
                    } else if (username.contains("订阅媒体号")) {
                        Intent intent = new Intent(getActivity(), SubscribeMediactivity.class);
                        startActivity(intent);
                    }else if(username.contains("网站公告")){
                        StatisticalTools.eventCount(getActivity(),"Websiteannouncement");
                        Intent intent = new Intent(getActivity(), NoticeListActivity.class);
                        startActivity(intent);
                    }
//                    else if (conversation.isGroup()) {
//                        String groupAvatar = conversation.getLastMessage().getStringAttribute("toAvatar");
//                        String groupname = conversation.getLastMessage().getStringAttribute("toRealName");
//                        ChatMainActivity.start(getActivity(), username, groupname, groupAvatar, true);
//
//                    }
                    // TODO: 2017/11/1 stone
                    else if (username.equals(MyConstant.HX_SYSTEM_ID)) {
                        //stone 进入系统消息 新添加的判断
                        String toChatUserRealname = "";
                        String toHeadImge = "";
                        toChatUserRealname = conversation.getLastMessage().getStringAttribute("fromRealName");
                        toHeadImge = conversation.getLastMessage().getStringAttribute("fromAvatar");
                        Intent intent = new Intent(getActivity(), ChatMainActivity.class);
                        intent.putExtra("userId", username);
                        //stone
                        intent.putExtra("username", toChatUserRealname.replaceAll(":", "").trim());
                        intent.putExtra("toHeadImge", toHeadImge);
                        startActivity(intent);
                    } else {
                        // TODO: 2017/11/1 stone 假数据
                        String toChatUserRealname = "";
                        String toHeadImge = "";
                        String newMsgType = "";
                        String msgBody = "";
                        DLog.d(TAG, "direct = " + conversation.getLastMessage().direct());
                        // TODO: 2017/11/1 stone
                        if (conversation.getLastMessage().direct() == EMMessage.Direct.SEND) {
                            toChatUserRealname = conversation.getLastMessage().getStringAttribute("toRealName");
                            toHeadImge = conversation.getLastMessage().getStringAttribute("toAvatar");
                            DLog.d(TAG, "to chat name send = " + toChatUserRealname);
                        } else {
                            toChatUserRealname = conversation.getLastMessage().getStringAttribute("fromRealName");
                            toHeadImge = conversation.getLastMessage().getStringAttribute("fromAvatar");
                            //                            newMsgType = conversation.getLastMessage().getStringAttribute("newMsgType");
                            //                            msgBody = conversation.getLastMessage().getStringAttribute("msgBody");
                            //                            conversation.getLastMessage().getStringAttribute("")
                            DLog.d(TAG, "to chat name = " + toChatUserRealname);
                        }

                        if (username.equals(YMApplication.getInstance().getUserName())) {
                            Toast.makeText(getActivity(), st2, Toast.LENGTH_SHORT).show();
                        } else {
                            // 进入聊天页面
                            Intent intent = new Intent(getActivity(), ChatMainActivity.class);
                            if (conversation.isGroup()) {
                                // it is group chat
                                // intent.putExtra("userId", username);
                                // intent.putExtra("username",
                                // toChatUserRealname);
                                // intent.putExtra("toHeadImge", toHeadImge);
                                // iniUpdata();
                                String groupAvatar = conversation.getLastMessage().getStringAttribute("toAvatar");
                                String groupname = conversation.getLastMessage().getStringAttribute("toRealName");
                                DLog.d(TAG, "group name = " + groupname);
                                ChatMainActivity.start(getActivity(), username, groupname, groupAvatar, true);
                            } else {
                                // it is single chat
                                DLog.i(TAG, "打印。。" + toChatUserRealname);
                                if (username.contains("sid")) {
                                    StatisticalTools.eventCount(getActivity(), "xxCaseDiscuss");
                                }

                                intent.putExtra("userId", username);
                                DLog.d(TAG, "username = " + username);
                                if (username.contains(Constants.QXYL_USER_HXID_MARK)) {
                                    intent.putExtra(ChatMainActivity.IS_HEALTHY_USER_KEY, true);
                                }
                                if (TextUtils.isEmpty(toChatUserRealname)) {
                                    username = username.replaceAll("did_", "");
                                    username = username.replaceAll("uid_", "");
                                    username = username.replaceAll(Constants.QXYL_USER_HXID_MARK, "");
                                    intent.putExtra("username", "用户" + username);
                                } else {
                                    username = username.replaceAll("did_", "");
                                    username = username.replaceAll("uid_", "");
                                    username = username.replaceAll(Constants.QXYL_USER_HXID_MARK, "");
                                    intent.putExtra("username", toChatUserRealname);
                                }

                                intent.putExtra("toHeadImge", toHeadImge);
                                startActivity(intent);
                            }

                        }
                    }
                } catch (HyphenateException e) {
                    DLog.i(TAG, "点击错误日志+" + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("您确定要删除此条聊天记录吗？");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMsg(position);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                return true;
            }
        });
        // 注册上下文菜单
        //		registerForContextMenu(listView);

        listView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 隐藏软键盘
                hideSoftKeyboard();
                return false;
            }

        });
        serch_layout = (LinearLayout) getView().findViewById(R.id.serch_layout);
        serch_layout.setOnClickListener(this);


    }

    private void deleteMsg(int position) {
        //		boolean handled = false;
        //		boolean deleteMessage = true;
        EMConversation tobeDeleteCons = adapter.getItem(position);
        // 删除此会话
        EMChatManager.getInstance().deleteConversation(tobeDeleteCons.getUserName(), tobeDeleteCons.isGroup(), true);
        // inviteMessgeDao.deleteMessage(tobeDeleteCons.getUserName(),uid);
        adapter.remove(tobeDeleteCons);
        adapter.notifyDataSetChanged();

        // 更新消息未读数
//        ((MainActivity) getActivity()).updateUnreadLabel();
        YmRxBus.notifyUpdateUnreadLabel();
    }


    void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


    /**
     * 刷新页面
     */
    public void refresh() {

        //stone 添加环信登录 未登录过 账号不为空
        if (!EMClient.getInstance().isLoggedInBefore()) {
            if (!TextUtils.isEmpty(YMApplication.getInstance().getUserName()) && !TextUtils.isEmpty(YMApplication.getInstance().getPassword())) {
                EMChatManager.getInstance().login(YMApplication.getInstance().getUserName(), YMApplication.getInstance().getPassword(), new EMCallBack() {
                    //            EMChatManager.getInstance().login("1", "1", new EMCallBack() {
                    @Override
                    public void onSuccess() {
//                        ToastUtils.shortToast("环信登录成功-->MessageInfoFragment");
                        LogUtils.d("环信登录成功-->MessageInfoFragment");
                        //stone 在ui线程去加载会话
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DLog.i("shrmsg", "消息MessageInforefresh");
                                getNewMediaNum();
                            }
                        });
                    }

                    @Override
                    public void onError(int i, final String s) {
                        LogUtils.d("环信登录失败-->MessageInfoFragment" + s);
//                        ToastUtils.shortToast("环信登录失败-->MessageInfoFragment" + s);
                    }


                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        } else {
            DLog.i("shrmsg", "消息MessageInforefresh");
            getNewMediaNum();
        }


    }

    private void notifyDataSetChanged() {
        conversationList.clear();
        List<EMConversation> conlist = loadConversationsWithRecentChat();
        conversationList.addAll(conlist);
        if (conlist.size() == 0) {
            if (no_data != null)
                no_data.setVisibility(View.VISIBLE);
        } else {
            if (no_data != null)
                no_data.setVisibility(View.GONE);
        }
        if (adapter != null) {
            DLog.i(TAG, "当前list大小" + conversationList.size());
            adapter.notifyDataSetChanged();
        }
    }

    private void getNoticeList() {
        iGetNoticeListPresenter = new GetNoticeListImpl(this);
        iGetNoticeListPresenter.getNoticeList(YMUserService.getCurUserId());
    }

    /**
     * 获取所有会话
     *
     * @return
     */
    private List<EMConversation> loadConversationsWithRecentChat() {
        // 获取所有会话，包括陌生人

        List<EMConversation> list = new ArrayList<EMConversation>();

        InviteMessgeDao inviteMessgeDao = new InviteMessgeDao();
        List<InviteMessage> inviteMessageList = inviteMessgeDao.getMessagesList();

        if (inviteMessageList.size() > 0) {
            for (int i = 0; i < inviteMessageList.size(); i++) {
                DLog.i(TAG, "=======邀请的好友" + inviteMessageList.get(i).getFrom());
            }

            //// TODO: 16/8/17 shijiazi replace method
//            EMConversation em = new EMConversation("新朋友");
            EMConversation em = EMChatManager.getInstance().getConversation("新朋友", EMConversation.EMConversationType.Chat);
            EMMessage msg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
            // msg.setAttribute("", arg1)
            msg.setAttribute("toRealName", "新朋友");
            msg.setAttribute("fromRealName", "新朋友");
            // msg.setAttribute("toAvatar", toHeadImge);
            // 设置消息body
            //						String msgtime = YMApplication.msgLists.get(0)
            //								.getCreateTime();
            String msgtime = inviteMessageList.get(inviteMessageList.size() - 1).getTime() + "";
            if (!TextUtils.isEmpty(msgtime)) {
                msg.setMsgTime(Long.parseLong(msgtime) * 1000);
            }
            DLog.i("msg", "添加新朋友信息==" + YMApplication.addMsgDetail);
            String reason = inviteMessageList.get(inviteMessageList.size() - 1).getReason();
            String username = inviteMessageList.get(inviteMessageList.size() - 1).getFrom();
            //added by shijiazi
            msg.setAttribute("toRealName", username);

            //stone 新添加的扩展字段区别是医脉搏还是用药助手
            if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
                msg.setAttribute("source", "selldrug");
            }

            if (reason.contains("|")) {
                YMApplication.addMsgDetail = reason.substring(reason.lastIndexOf("|") + 1, reason.length()) + "请求添加您为好友，快去通过吧！";
            } else {
                if (username.contains("did")) {
                    username = username.replaceAll("did_", "");
                }
                YMApplication.addMsgDetail = "用户" + username + "请求添加您为好友，快去通过吧！";
            }
            EMTextMessageBody txtBody = new EMTextMessageBody(YMApplication.addMsgDetail);
            msg.addBody(txtBody);
            em.insertMessage(msg);

            //deleted by shijiazi
//            sortList.add(new Pair<>(em.getLastMessage().getMsgTime(), em));

            //            list.add(em);

        }

        try {

            SharedPreferences sharedPreferences = YMApplication.getAppContext().getSharedPreferences("msg_manager", Context.MODE_PRIVATE);


            List<EMConversation> emConversationList = new ArrayList<EMConversation>();

            for (int i = 0; i < list.size(); i++) {
                //                DLog.i(TAG, "当前回话列表===position" + i + "=====" + list.get(i).getUserName() + "====" +
                //                        list.get(i).getLastMessage());
                if (sharedPreferences.getBoolean(list.get(i).getUserName(), false)) {
                    //                   DLog.i(TAG,"当前指定的id==="+list.get(i).getUserName());
                    emConversationList.add(list.get(i));
                    list.remove(i);
                }
            }

            //stone 医生助手不需要这个 消息小助手和订阅媒体号
            if (!YMApplication.sIsYSZS) {
                if (!YMUserService.isGuest()) {
                    if (YMApplication.DoctorType() == 1 & !TextUtils.isEmpty(YMApplication.msgnum)) {

                        if (Integer.valueOf(YMApplication.msgnum) > 0) {

                            //// TODO: 16/8/17 shijiazi replace method
                            EMConversation em = EMChatManager.getInstance().getConversation("消息小助手", EMConversation.EMConversationType.Chat);

//                        EMConversation em = new EMConversation("消息小助手");

                            EMMessage msg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
                            // msg.setAttribute("", arg1)
                            msg.setAttribute("toRealName", "消息小助手");
                            msg.setAttribute("fromRealName", "消息小助手");
                            // msg.setAttribute("toAvatar", toHeadImge);
                            // 设置消息body
                            //						String msgtime = YMApplication.msgLists.get(0)
                            //								.getCreateTime();
                            String msgtime = YMApplication.msgcreatetime;
                            if (!TextUtils.isEmpty(msgtime)) {
                                msg.setMsgTime(Long.parseLong(msgtime) * 1000);
                            }

                            //stone 新添加的扩展字段区别是医脉搏还是用药助手
                            if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
                                msg.setAttribute("source", "selldrug");
                            }

                            EMTextMessageBody txtBody = new EMTextMessageBody(YMApplication.msgdetail);
                            msg.addBody(txtBody);
                            em.insertMessage(msg);
                            //deleted by shijiazi
//                        sortList.add(new Pair<>(em.getLastMessage().getMsgTime(), em));


                            //                        list.add(0, em);

                        }

                    }

                    MediaList mediaList = YMApplication.getInstance().getMediaList();
                    DLog.i(TAG, "当前media" + mediaList.getCode() + "==" + mediaList.getIsnew() + "==" + mediaList.getMessage() + "data==" +
                            (mediaList.getData() == null));
                    if (mediaList.getCode() == 10000 && mediaList.getData() != null && mediaList.getData().size() > 0) {

                        //// TODO: 16/8/17 shijiazi replace method
                        EMConversation em = EMChatManager.getInstance().getConversation("订阅媒体号", EMConversation.EMConversationType.Chat);

//                    EMConversation em = new EMConversation("订阅媒体号");


                        EMMessage msg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
                        msg.setMsgTime(Long.parseLong(mediaList.getMessage().getTime() + "") * 1000);
                        EMTextMessageBody txtBody = new EMTextMessageBody(mediaList.getMessage().getTitle());
                        msg.addBody(txtBody);
                        em.insertMessage(msg);


//                    sortList.add(new Pair<Long, EMConversation>(em.getLastMessage().getMsgTime(), em));


                        //                    if (sharedPreferences.getBoolean("mediaSetting", false)) {
                        //                        list.add(0, em);
                        //                    } else {
                        //                        list.add(em);
                        //                    }
                    }

                    //公告列表
                    List<Notice> noticeList = YMApplication.getInstance().getNoticeList();
                    if (noticeList != null && noticeList.size() > 0) {
                        EMChatManager.getInstance().deleteConversation("网站公告", true,true);
                        EMConversation em = EMChatManager.getInstance().getConversation("网站公告", EMConversation.EMConversationType.Chat);
                        EMMessage msg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
                        Notice notice = noticeList.get(0);
                        if(null != notice){
                            msg.setMsgTime(Long.parseLong(notice.intime) * 1000);
                            EMTextMessageBody txtBody = new EMTextMessageBody(notice.title);
                            msg.addBody(txtBody);
                            msg.setAttribute(Constants.HS_READ, notice.hs_read);
                        }

                        em.insertMessage(msg);
                    }
                }
            }

            Map<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
            DLog.d(TAG, "coversations = " + conversations);

            // 过滤掉messages size为0的conversation
            /**
             * 如果在排序过程中有新消息收到，lastMsgTime会发生变化 影响排序过程，Collection.sort会产生异常
             * 保证Conversation在Sort过程中最后一条消息的时间不变 避免并发问题
             */
            List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
            synchronized (conversations) {
                for (EMConversation conversation : conversations.values()) {
                    DLog.i(TAG, "用户conversation=" + conversation.getUserName());
                    if (conversation.getType() != EMConversation.EMConversationType.ChatRoom && conversation.getAllMessages().size() != 0) {
                        sortList.add(new Pair<>(conversation.getLastMessage().getMsgTime(), conversation));
                    }
                }
            }
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
            for (Pair<Long, EMConversation> sortItem : sortList) {
                list.add(sortItem.second);
            }
            if (YMApplication.DoctorType() == 1 && list.size() > 1) {
                list.addAll(1, emConversationList);
            } else {
                list.addAll(0, emConversationList);
            }
            DLog.i(TAG, "历史记录大小=" + list.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 根据最后一条消息的时间排序
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first == con2.first) {
                    return 0;
                } else if (con2.first > con1.first) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden) {
            refresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        StatisticalTools.fragmentOnResume("MessageInfoFragment");
        if (!hidden && !((MessageInfoActivity) getActivity()).isConflict) {
            refresh();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (((MessageInfoActivity) getActivity()).isConflict) {
            outState.putBoolean("isConflict", true);
        } else if (((MessageInfoActivity) getActivity()).isCurrentAccountRemoved) {
            outState.putBoolean(EaseConstant.ACCOUNT_REMOVED, true);

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(null != progressDialog && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.serch_layout:
                StatisticalTools.eventCount(getActivity(), "xxsearch");

                Intent intent = new Intent(getActivity(), SerchbarActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fade_right_in, R.anim.fade_left_out);
                break;

            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            sendData(scanResult);
            // T.shortToast(scanResult );
        }
    }

    public void sendData(final String str) {
        String did = YMApplication.getLoginInfo().getData().getPid();
        String bind = did + str;
        Long st = System.currentTimeMillis();

        String sign = MD5Util.MD5(st + bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();

        params.put("timestamp", st + "");
        params.put("bind", bind);
        params.put("a", "chat");
        params.put("m", "qrcodeScan");
        params.put("url", str);
        params.put("did", did);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.get(CommonUrl.Patient_Manager_Url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                // TODO Auto-generated method stub
                DLog.i(TAG, "扫面返回的结果" + t.toString());
                Gson gson = new Gson();
                map = ResolveJson.R_Action_twos(t.toString());
                if (map.get("code").equals("0")) {
                    if (map.get("isxywy").equals("1")) {
                        Intent intenAdd = new Intent(getActivity(), AddCardHoldVerifyActiviy.class);
                        intenAdd.putExtra("toAddUsername", "did_" + map.get("did"));
                        startActivity(intenAdd);
                    } else {
                        if (str.contains("http")) {
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            Uri content_url = Uri.parse(str);
                            intent.setData(content_url);
                            startActivity(intent);
                        } else {
                            Dialog(getActivity(), str);
                        }

                    }
                } else {
                    ToastUtils.shortToast(map.get("msg"));
                    if (str.contains("http")) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(str);
                        intent.setData(content_url);
                        startActivity(intent);
                    } else {
                        Dialog(getActivity(), str);
                    }
                }
                super.onSuccess(t);
            }
        });

    }

    public void Dialog(Context context, String str) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.myclic_dialog, null);
        final Dialog dialog = new android.app.AlertDialog.Builder(context).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        TextView content = (TextView) layout.findViewById(R.id.tv_content);
        content.setText(str);
        RelativeLayout re_ok = (RelativeLayout) layout.findViewById(R.id.rl_ok);
        re_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();

            }
        });

    }

    /**
     * 获取媒体号，save
     * 获取透传信息 下个版本 极光暂定？？？？
     */
    public void getNewMediaNum() {

        String sign = MD5Util.MD5(uid + Constants.MD5_KEY);
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
//                    DLog.i("媒体号返回数据msg", s);
                    Gson gson = new Gson();
                    MediaList mediaData = gson.fromJson(s, MediaList.class);
                    if (mediaData.getData() != null && mediaData.getData().size() > 0) {
                        String mediaId = mediaData.getData().get(0).getId();
                        if (sp != null) {
                            sp.edit().putString("mediaId", mediaId == null ? "" : mediaId).apply();
                        }
                    }

                    YMApplication.getInstance().setMediaList(mediaData);
                    getNoticeList();
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

    public void onPause() {
        super.onPause();
        StatisticalTools.fragmentOnPause("MessageInfoFragment");
    }

    @Override
    public void onItemClick(ActionItem item, int position) {
//        String mTitle = (String) item.mTitle;
//        Intent intent;
//        switch (mTitle) {
//            // 添加好友
//            case ADD_FRIEND:
//                StatisticalTools.eventCount(getActivity(), "NewAddFriends");
//                if (YMUserService.isGuest()) {
//                    DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
//                } else {
//                    intent = new Intent(getActivity(), AddNewCardHolderActivity.class);
//                    startActivity(intent);
//                }
//                break;
//            // 发起群聊
//            case GROUP_CHAT:
//                //判断认证 stone
//                DialogUtil.showUserCenterCertifyDialog(getActivity(), new MyCallBack() {
//                    @Override
//                    public void onClick(Object data) {
//                        ImUserListActivity.start(getActivity(), null, UserPageShowType.CREATE_GROUP);
//                    }
//                }, null, null);
//
//                break;
//            // 邀请好友
//            case INVITE_FRIEND:
//                StatisticalTools.eventCount(getActivity(), "NewInviteFriends");
//                if (YMUserService.isGuest()) {
//                    DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
//                } else {
//                    intent = new Intent(getActivity(), InviteNewFriendMainActivity.class);
//                    startActivity(intent);
//                }
//                break;
//
//            // 我的名片
//            case MY_CARD:
//                StatisticalTools.eventCount(getActivity(), "NewMycard");
//                //判断认证 stone
//                DialogUtil.showUserCenterCertifyDialog(getActivity(), new MyCallBack() {
//                    @Override
//                    public void onClick(Object data) {
//
//
////                if (YMUserService.isGuest()) {
////                    DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
////                } else {
//                        if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
//                            if (YMApplication.getInstance().getHasSyncInfo()) {
//                                //如果已经同步过医生信息
//                                if (YMApplication.getInstance().getSyncInfoResult()) {
//                                    //医生信息同步成功
//                                    if (YMApplication.getInstance().getHasSellDrug()) {
//                                        //已经检查过医生是否具有售药的权限
//                                        if (YMApplication.getInstance().getSellDrugResult()) {
//                                            //当前医生具有售药的权限
//                                            LogUtils.i("跳转到二维码展示页面");
//                                            Intent intent = new Intent(getActivity(), MyIdCardActivity.class);
//                                            startActivity(intent);
//                                        } else {
//                                            ToastUtils.shortToast(YMApplication.getInstance().getSellDrugMsg());
//                                        }
//                                    } else {
//                                        //还未检查医生是否具有售药的权限
//                                        sellDrug();
//                                    }
//                                } else {
//                                    //医生信息同步失败
//                                    com.xywy.askforexpert.module.discovery.medicine.util.ToastUtils.shortToast(YMApplication.getInstance().getSyncInfoMsg());
//                                }
//                            } else {
//                                //没有同步过医生信息,则同步医生信息
//                                syncInfo();
//                            }
//                        } else {
//                            Intent intent = new Intent(getActivity(), MyIdCardActivity.class);
//                            startActivity(intent);
//                        }
//
////                }
//
//                    }
//                }, null, null);
//                break;
//
//            // 扫一扫
//            case ERWEIMA:
//                StatisticalTools.eventCount(getActivity(), "RichScan");
//                if (YMUserService.isGuest()) {
//                    DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
//                } else {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                            CommonUtils.permissionRequestDialog(getActivity(), "无法启动相机，请授予照相机(Camera)权限", 222);
//                        } else {
//                            Intent openCameraIntent = new Intent(getActivity(), CaptureActivity.class);
//                            startActivityForResult(openCameraIntent, 0);
//                        }
//                    } else {
//                        Intent openCameraIntent = new Intent(getActivity(), CaptureActivity.class);
//                        startActivityForResult(openCameraIntent, 0);
//                    }
//                }
//                break;
//
//            default:
//                break;
//        }
    }

    @Override
    public void onSuccessResultView(Object o, String flag) {
        BaseData<List<Notice>> baseData = (BaseData<List<Notice>>)o;
        List<Notice> noticeList = baseData.getData();
        YMApplication.getInstance().setNoticeList(noticeList);
        notifyDataSetChanged();
    }

    @Override
    public void onErrorResultView(Object o, String flag, Throwable e) {

    }

    @Override
    public void showProgressBar() {
        showProgressDialog("");
    }

    @Override
    public void hideProgressBar() {
        hideProgressDialog();
    }

    @Override
    public void showToast(String str) {

    }

//    /**
//     * 同步医生信息
//     */
//    public void syncInfo() {
//        long doctroId = Long.parseLong(YMUserService.getCurUserId());
//        SyncInfoRequest.getInstance().syncInfo(doctroId).subscribe(new BaseRetrofitResponse<BaseData>() {
//            @Override
//            public void onStart() {
//                super.onStart();
//                showProgressDialog("");
//            }
//
//            @Override
//            public void onCompleted() {
//                super.onCompleted();
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                hideProgressDialog();
//                if (e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof HttpException) {
//                    //未同步医生信息接口
//                    YMApplication.getInstance().setHasSyncInfo(false);
//                    Toast.makeText(RetrofitClient.getContext(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
//                } else {
//                    //已经同步过医生信息接口
//                    YMApplication.getInstance().setHasSyncInfo(true);
//                    //同步过医生信息接口的返回结果是：同步医生信息失败
//                    YMApplication.getInstance().setSyncInfoResult(false, e.getMessage());
//                    T.showShort(RetrofitClient.getContext(), e.getMessage());
//                }
//            }
//
//            @Override
//            public void onNext(BaseData entry) {
//                super.onNext(entry);
//                if (entry != null) {
//                    dealwithEntry(entry);
//                }
//            }
//        });
//    }
//
//    private void dealwithEntry(BaseData entry) {
//        if (null != entry && entry.getCode() == 10000) {
//            //已经同步过医生信息接口
//            YMApplication.getInstance().setHasSyncInfo(true);
//            //同步过医生信息接口的返回结果是：同步医生信息成功
//            YMApplication.getInstance().setSyncInfoResult(true, "");
//            sellDrug();
//        }
//    }
//
//    private void sellDrug() {
//        SellDrugRequest.getInstance().getSellDrug(Integer.parseInt(YMUserService.getCurUserId())).subscribe(new BaseRetrofitResponse<BaseData>() {
//            @Override
//            public void onStart() {
//                super.onStart();
//            }
//
//            @Override
//            public void onCompleted() {
//                super.onCompleted();
//                hideProgressDialog();
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                hideProgressDialog();
//                if (e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof HttpException) {
//                    //还未调用检测是否具有售药权限的接口
//                    YMApplication.getInstance().setHasSellDrug(false);
//                    Toast.makeText(RetrofitClient.getContext(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
//                } else {
//                    //已经调用了检测是否具有售药权限的接口
//                    YMApplication.getInstance().setHasSellDrug(true);
//                    //检测是否具有售药权限的接口返回的结果是：没有售药权限
//                    YMApplication.getInstance().setSellDrugResult(false, e.getMessage());
//                    T.showShort(RetrofitClient.getContext(), e.getMessage());
//                }
//            }
//
//            @Override
//            public void onNext(BaseData entry) {
//                super.onNext(entry);
//                if (entry != null) {
//                    //已经调用了检测是否具有售药权限的接口
//                    YMApplication.getInstance().setHasSellDrug(true);
//                    //检测是否具有售药权限的接口返回的结果是：具有售药权限
//                    YMApplication.getInstance().setSellDrugResult(true, "");
//                    Intent intent = new Intent(getActivity(), MyIdCardActivity.class);
//                    startActivity(intent);
//
//                }
//            }
//        });
//    }
//
    /**
     * 显示进度dialog
     *
     * @param content 提示文字内容
     */
    public void showProgressDialog(String content) {
        if (null == progressDialog) {
            progressDialog = new ProgressDialog(getActivity(), content);
            progressDialog.setCanceledOnTouchOutside(false);

        } else {
            progressDialog.setTitle(content);
        }
        progressDialog.showProgersssDialog();
    }

    /**
     * 隐藏进度dialog
     */
    public void hideProgressDialog() {
        if (null == progressDialog) {
            return;
        }
        if (getActivity() != null && !getActivity().isFinishing() && progressDialog.isShowing()) {
            progressDialog.closeProgersssDialog();
        }
    }
}
