package com.xywy.askforexpert.module.message.msgchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage.Direct;
import com.hyphenate.exceptions.HyphenateException;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.UpDataInfo;
import com.xywy.askforexpert.module.main.subscribe.SubscribeMediactivity;
import com.xywy.askforexpert.module.message.MessageInfoFragment;
import com.xywy.askforexpert.module.message.adapter.ChatAllHistoryAdapter;
import com.xywy.askforexpert.module.message.friend.CardNewFriendActivity;
import com.xywy.askforexpert.module.my.setting.utils.VersionUtils;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

/**
 * @author 王鹏
 * @2015-4-21下午9:23:38
 */
public class SerchbarActivity extends Activity {
    private static final String TAG = "SerchbarActivity";
    public static UpDataInfo mDataInfo;// 版本更新infos
    private LinearLayout view;
    private EditText query;
    private ImageButton clearSearch;
    private ListView serch_list;
    private ChatAllHistoryAdapter adapter;
    private InputMethodManager inputMethodManager;
    private LinearLayout serch_no;
    private TextView tv_cancle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化统一bar
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.search_bar);
        query = (EditText) findViewById(R.id.query);
        clearSearch = (ImageButton) findViewById(R.id.search_clear);
        serch_list = (ListView) findViewById(R.id.serch_list);
        serch_no = (LinearLayout) findViewById(R.id.serch_no);
        adapter = new ChatAllHistoryAdapter(SerchbarActivity.this, 1,
                MessageInfoFragment.conversationList);
        TextView tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        serch_list.setAdapter(adapter);
        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                adapter.getFilter().filter(s);
                if (s.length() > 0) {
                    clearSearch.setVisibility(View.VISIBLE);
                    serch_no.setVisibility(View.GONE);
                    serch_list.setVisibility(View.VISIBLE);
                } else {
                    clearSearch.setVisibility(View.INVISIBLE);
                    serch_no.setVisibility(View.VISIBLE);
                    serch_list.setVisibility(View.GONE);
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {

            }
        });
        serch_list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                try {
                    EMConversation conversation = adapter.getItem(position);
                    String username = conversation.getUserName();
                    DLog.d(TAG, "msg info username = " + username);
                    if (username.equals("消息小助手")) {
                        Intent intent = new Intent(SerchbarActivity.this, MsgZhuShou.class);
                        startActivity(intent);
                    } else if (username.equals("新朋友")) {
                        Intent intent = new Intent(SerchbarActivity.this, CardNewFriendActivity.class);
                        startActivity(intent);
                    } else if (username.contains("qid")) {
                        // iniUpdata();
//                        iniUpdata_new();
                        VersionUtils.getInstance(SerchbarActivity.this).checkVersionUpdate();
                    } else if (username.contains("订阅媒体号")) {
                        Intent intent = new Intent(SerchbarActivity.this, SubscribeMediactivity.class);
                        startActivity(intent);
                    }
//                    else if (conversation.isGroup()) {
//                        String groupAvatar = conversation.getLastMessage().getStringAttribute("toAvatar");
//                        String groupname = conversation.getLastMessage().getStringAttribute("toRealName");
//                        ChatMainActivity.start(SerchbarActivity.this, username, groupname, groupAvatar, true);
//
//                    }
                    else {
                        String toChatUserRealname;
                        String toHeadImge;
                        String newMsgType = "";
                        String msgBody = "";
                        DLog.d(TAG, "direct = " + conversation.getLastMessage().direct());
                        if (conversation.getLastMessage().direct() == Direct.SEND) {
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
                            Toast.makeText(SerchbarActivity.this, "不能和自己聊天", Toast.LENGTH_SHORT).show();
                        } else {
                            // 进入聊天页面
                            goChatPage(conversation, username, toChatUserRealname, toHeadImge);
                        }
                    }
                } catch (HyphenateException e) {
                    DLog.i(TAG, "点击错误日志+" + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        clearSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
                hideSoftKeyboard();
            }
        });
        tv_cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();

            }
        });
    }

    /**
     * 进入聊天页面
     * @param conversation
     * @param username
     * @param toChatUserRealname
     * @param toHeadImge
     * @throws HyphenateException
     */
    private void goChatPage(EMConversation conversation, String username, String toChatUserRealname, String toHeadImge) throws HyphenateException {
        Intent intent = new Intent(SerchbarActivity.this, ChatMainActivity.class);
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
            ChatMainActivity.start(SerchbarActivity.this, username, groupname, groupAvatar, true);
        } else {
            // it is single chat
            DLog.i(TAG, "打印。。" + toChatUserRealname);
            if (username.contains("sid")) {
                StatisticalTools.eventCount(SerchbarActivity.this, "xxCaseDiscuss");
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

    /**
     * 版本更新
     */
    private void iniUpdata_new() {

        FinalHttp fh = new FinalHttp();
        fh.get(CommonUrl.UpdataUrl, new AjaxCallBack() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                Log.e("UpData", "失败" + strMsg);
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                Log.e("UpData", t.toString());
                Gson gg = new Gson();

                mDataInfo = gg.fromJson(t.toString(), UpDataInfo.class);
                if (TextUtils.isEmpty(mDataInfo.apkurl)
                        || TextUtils.isEmpty(mDataInfo.description)) {
                    return;
                }

                if (mDataInfo != null) {
//                    VersionUtils incetecn = VersionUtils.getInstance();
//                    incetecn.initData(SerchbarActivity.this, mDataInfo);
//                    incetecn.checkUpdate();
                }
            }
        });
    }

    void hideSoftKeyboard() {
        if (SerchbarActivity.this.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (SerchbarActivity.this.getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(
                        SerchbarActivity.this.getCurrentFocus()
                                .getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
