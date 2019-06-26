package com.xywy.askforexpert.module.message.share;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;
import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.AddressBook;
import com.xywy.askforexpert.model.ShareCardInfo;
import com.xywy.askforexpert.module.message.adapter.ContactAdapter;
import com.xywy.askforexpert.module.message.friend.CardNewFriendActivity;
import com.xywy.askforexpert.module.message.imgroup.GroupListActivity;
import com.xywy.askforexpert.widget.PasteEditText;
import com.xywy.askforexpert.widget.Sidebar2;
import com.xywy.easeWrapper.EMChatManager;
import com.xywy.easeWrapper.EMContactManager;
import com.xywy.easeWrapper.EaseConstant;
import com.xywy.easeWrapper.db.InviteMessage;
import com.xywy.easeWrapper.db.InviteMessgeDao;
import com.xywy.easeWrapper.domain.EaseUser;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 名片夹 分享
 *
 * @2015-8-4下午2:18:46 MODIFY 2016-3-30
 */
public class ShareCardActivity extends Activity {

    private static final String TAG = "ShareCardActivity";
    public static List<AddressBook> content;
    AddressBook pListInfo;
    AlertDialog dialog;
    String url;
    String title;
    String imageUrl;
    String type, id;
    FinalBitmap fb;
    String chatName;
    String shareAnswerid;
    String shareAnswerTitle;
    String shareAnswerType;
    String answerversion;
    private ContactAdapter adapter;
    private List<EaseUser> contactList = new ArrayList<>();
    private ListView listView;
    private boolean hidden;
    private Sidebar2 sidebar;
    private InputMethodManager inputMethodManager;
    private List<String> blackList;
    private TextView tv_newfriend_num;
    private RelativeLayout re_new_frident;
    private InviteMessgeDao inviteMessgeDao;
    private EditText edit;
    private LinearLayout no_data;
    private TextView tv_nodata_title;
    private ImageView img_nodata;
    private String uid;
    private String realname;
    private String hx_usernam;
    private String head_img;
    private String currentFriendId = "";
    private String currentFriendName = "";
    private android.support.v7.app.AlertDialog.Builder builder;
    /**
     * 职称
     */
    private String fCardTitle = "";
    /**
     * 所在医院
     */
    private String fCardHospital = "";
    /**
     * 所属科室
     */
    private String fCardDpart = "";
    private String fCardName = "";
    private String shareSource;
    private String uu;
    private String vu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        setContentView(R.layout.share_card_holder_number);



        fb = FinalBitmap.create(ShareCardActivity.this, false);

        builder = new android.support.v7.app.AlertDialog.Builder(this);
        // 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
            return;
        }
        inputMethodManager = (InputMethodManager) ShareCardActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);

        ((TextView) findViewById(R.id.tv_title)).setText("选择联系人");

        id = getIntent().getStringExtra("id");
        url = getIntent().getStringExtra("url");
        type = getIntent().getStringExtra("type");
        title = getIntent().getStringExtra("title");
        imageUrl = getIntent().getStringExtra("imageUrl");
        shareSource = getIntent().getStringExtra("shareSource");
        if (shareSource != null && "5".equals(shareSource)) {
            uu = getIntent().getStringExtra("uu");
            vu = getIntent().getStringExtra("vu");
        }
        if (!TextUtils.isEmpty(shareSource) && "6".equals(shareSource)) {
            shareAnswerid = getIntent().getStringExtra("shareAnswerId");
            shareAnswerTitle = getIntent().getStringExtra("shareAnswerTitle");
            shareAnswerType = getIntent().getStringExtra("shareAnswerType");
            answerversion = getIntent().getStringExtra("answerversion");
            StringBuffer sb = new StringBuffer();
            sb.append(shareAnswerid + "|");
            sb.append(shareAnswerTitle + "|");
            sb.append(shareAnswerType + "|");
            sb.append(answerversion);
            id = sb.toString();
        }


        if (type.equals("shareNameCard")) {
            fCardTitle = getIntent().getStringExtra("fCardTitle");
            fCardHospital = getIntent().getStringExtra("fCardHospital");
            fCardDpart = getIntent().getStringExtra("fCardDpart");
            fCardName = getIntent().getStringExtra("fCardName");
            currentFriendId = id;
        } else if (type.equals("shareCardDc")) {
            fCardTitle = getIntent().getStringExtra("fCardTitle");
            fCardHospital = getIntent().getStringExtra("fCardHospital");
            fCardDpart = getIntent().getStringExtra("fCardDpart");
            fCardName = getIntent().getStringExtra("fCardName");
            currentFriendId = id;
        } else if (type.equals("shareNameCardMedia")) {
            fCardTitle = getIntent().getStringExtra("fCardTitle");
            fCardHospital = getIntent().getStringExtra("fCardHospital");
            fCardDpart = getIntent().getStringExtra("fCardDpart");
            fCardName = getIntent().getStringExtra("fCardName");
            currentFriendId = id;
        } else {
            type = "share";
        }

        listView = (ListView) findViewById(R.id.list);
        sidebar = (Sidebar2) findViewById(R.id.sidebar);
        sidebar.setListView(listView);
        if (!YMUserService.isGuest()) {
            if (YMApplication.getLoginInfo() != null) {
                uid = YMApplication.getLoginInfo().getData().getPid();
            }

        }
        // 黑名单列表
        // blackList = EMContactManager.getInstance().getBlackListUsernames();
        // contactList = new ArrayList<User>();
        tv_newfriend_num = (TextView) findViewById(R.id.tv_newfriend_num);
        re_new_frident = (RelativeLayout) findViewById(R.id.re_new_frident);
        edit = (EditText) ShareCardActivity.this.findViewById(R.id.search_bar_view);
        no_data = (LinearLayout) findViewById(R.id.lin_nodata);
        tv_nodata_title = (TextView) findViewById(R.id.tv_nodata_title);
        tv_nodata_title.setText("暂无好友");
        img_nodata = (ImageView) findViewById(R.id.img_nodate);
        img_nodata.setBackgroundResource(R.drawable.nofriend);
        inviteMessgeDao = new InviteMessgeDao();
        if (YMUserService.isGuest()) {
            no_data.setVisibility(View.VISIBLE);
        } else {
            getData();
        }

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String username = adapter.getItem(position).getHxusername();
                int chattype = adapter.getItem(position).getType();
                if (chattype == 5) {
                    GroupListActivity.startForResult(ShareCardActivity.this, 100);
                } else {

                    if (!EaseConstant.NEW_FRIENDS_USERNAME.equals(username)) {

                        head_img = adapter.getItem(position).getPhoto();

                        realname = adapter.getItem(position).getRealname();
                        chatName = adapter.getItem(position).getHxusername();

                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        DLog.i(TAG, "link url share:" + type);
                        if (type.equals("shareNameCard")) { //// TODO: 16/8/25 分享卡片
                            title = fCardName + "   " + fCardTitle;
                            showDialog(builder, fCardName, false);
                        } else if (type.equals("shareCardDc")) {   //shareCardDc
                            title = fCardName + "   " + fCardTitle;
                            showDialog(builder, fCardName, false);
                        } else {
                            Dialog(ShareCardActivity.this, head_img, title, imageUrl, false);
                        }

                    }

                }
            }
        });

        listView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                return false;
            }

        });
        re_new_frident.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startActivity(new Intent(ShareCardActivity.this, CardNewFriendActivity.class));
            }
        });
        edit.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) {

                    adapter.setData(content);
                    adapter.getFilter().filter(s);

                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void showDialog(android.support.v7.app.AlertDialog.Builder builder, String fCardName, final boolean isGroup) {
        builder.setMessage("发送" + fCardName + "的名片到当前聊天");
        builder.setMessage("发送" + fCardName + "的名片到当前聊天");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
                ToastUtils.shortToast( "取消分享");
            }
        });
        builder.setPositiveButton("发送", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendAddCardText(chatName, head_img, type, isGroup);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void refresh() {
        // 获取设置contactlist
        getContactList();
        // // 设置adapter
        if (YMUserService.isGuest()) {
            no_data.setVisibility(View.VISIBLE);
        } else {
            getData();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (YMApplication.isrefresh) {
            refresh();
            YMApplication.isrefresh = false;
        }


    }

    public void getData() {
        String bind = YMApplication.getLoginInfo().getData().getHuanxin_username();
        Long st = System.currentTimeMillis();

        String sign = MD5Util.MD5(st + bind + Constants.MD5_KEY);
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
                super.onSuccess(t);
                DLog.i(TAG, "JSON==" + t.toString());
                pListInfo = ResolveJson.R_CardHold(t.toString());
                //                        if (type.equals("shareNameCardMedia")||type.equals("shareNameCard")
                //                                ||type.equals("shareCardDc")) {
                for (int i = 0; i < pListInfo.getData().size(); i++) {
                    if (pListInfo.getData().get(i).getRealname().equals("我的助理")) {
                        pListInfo.getData().remove(i);
                    } else if (pListInfo.getData().get(i).getRealname().equals("媒体号")) {
                        pListInfo.getData().remove(i);
                    } else if (pListInfo.getData().get(i).getRealname().equals("服务号")) {
                        pListInfo.getData().remove(i);
                    }
                }
                //                        }
                List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();

                if (msgs != null) {
                    String count = msgs.size() + "";
                    tv_newfriend_num.setText(count);
                }

                if (pListInfo.getCode().equals("0") & pListInfo.getData().size() > 0) {
                    listView.setVisibility(View.VISIBLE);
                    no_data.setVisibility(View.GONE);
                    content = pListInfo.getData();
                    soft();
                    if (listView != null & content != null & ShareCardActivity.this != null) {
                        adapter = new ContactAdapter(YMApplication.getAppContext(), 1, content);

                        listView.setAdapter(adapter);
                        setContentList();
                    }
                } else {
                    listView.setVisibility(View.GONE);
                    no_data.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });

    }

    public void soft() {
        Collections.sort(content, new Comparator<AddressBook>() {

            @Override
            public int compare(AddressBook lhs, AddressBook rhs) {
                return lhs.getHeader().compareTo(rhs.getHeader());
            }
        });

    }

    /**
     * 获取联系人列表，并过滤掉黑名单和排序
     *
     * @throws HyphenateException
     */
    private void getContactList() {
        if (contactList != null) {
            contactList.clear();
        }

        List<String> usernames;
        try {
            usernames = EMContactManager.getInstance().getAllContactsFromServer();
            EMLog.d("roster", "contacts size: " + usernames.size());
            Map<String, EaseUser> userlist = new HashMap<String, EaseUser>();
            for (String username : usernames) {
                EaseUser user = new EaseUser(username);
                // setUserHearder(username, user);
                userlist.put(username, user);
            }

            // 获取本地好友列表
            // Map<String,EaseUser> users =
            // YMApplication.getInstance().getContactList();
            Iterator<Entry<String, EaseUser>> iterator = userlist.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, EaseUser> entry = iterator.next();
                if (!entry.getKey().equals(EaseConstant.NEW_FRIENDS_USERNAME) && !entry.getKey().equals(EaseConstant.GROUP_USERNAME) && !blackList.contains(entry.getKey())) {
                    contactList.add(entry.getValue());
                }
            }
            // 排序
            Collections.sort(contactList, new Comparator<EaseUser>() {

                @Override
                public int compare(EaseUser lhs, EaseUser rhs) {
                    return lhs.getUsername().compareTo(rhs.getUsername());
                }
            });
        } catch (HyphenateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void setContentList() {
        Map<String, EaseUser> userlist = new HashMap<String, EaseUser>();
        // for (String username : usernames) {
        //EaseUser user = newEaseUser();
        // user.setUsername(username);
        // // setUserHearder(username, user);
        // userlist.put(username, user);
        // }
        for (int i = 0; i < pListInfo.getData().size(); i++) {
            EaseUser user = new EaseUser(pListInfo.getData().get(i).getHxusername());
            // setUserHearder(username, user);
            userlist.put(pListInfo.getData().get(i).getHxusername(), user);
        }
        YMApplication.getInstance().setContactList(userlist);
    }

    public void Dialog(final Context context, final String head_img, String str, String imgurl, final boolean isGroup) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.share_card_dialog, null);
        if (dialog == null) {
            dialog = new AlertDialog.Builder(context).create();

        }
        dialog.setCanceledOnTouchOutside(false);
        ImageView imView = (ImageView) layout.findViewById(R.id.img_head);
        if (!TextUtils.isEmpty("imgurl")) {
            fb.configLoadfailImage(R.drawable.img_default_bg);
            fb.configLoadingImage(R.drawable.img_default_bg);
            fb.display(imView, imgurl);
        }

        TextView title = (TextView) layout.findViewById(R.id.tv_title);
        title.setText(str);
        final PasteEditText edit = (PasteEditText) layout.findViewById(R.id.edit_comment);

        RelativeLayout re_cancle = (RelativeLayout) layout.findViewById(R.id.rl_channel);
        RelativeLayout re_ok = (RelativeLayout) layout.findViewById(R.id.rl_enter);
        edit.findFocus();
        dialog.setView(layout);
        dialog.show();

        re_cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                finish();
                ToastUtils.shortToast( "取消分享");

            }
        });
        re_ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String share_text = edit.getText().toString().trim();
                if (share_text.length() < 150) {
                    sendAddCardText(chatName, head_img, type, isGroup);
                    if (!TextUtils.isEmpty(edit.getText().toString().trim())) {
                        sendAddCardText_Comm(chatName, head_img, edit.getText().toString().trim(), isGroup);
                    }
                    dialog.dismiss();

                } else {
                    ToastUtils.shortToast( "评论不得超过150个字");
                }
            }
        });

    }

    /**
     * 发表评论
     *
     * @param tochatname
     * @param head_img
     * @param str_comm
     * @param isGroup
     */
    public void sendAddCardText_Comm(String tochatname, String head_img, String str_comm, boolean isGroup) {
        EMConversation conversation = EMChatManager.getInstance().getConversation(tochatname);
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
        // 如果是群聊，设置chattype,默认是单聊
        // if (chatType == CHATTYPE_GROUP)
        if (isGroup) {
            message.setChatType(EMMessage.ChatType.GroupChat);
        }
        EMTextMessageBody txtBody = new EMTextMessageBody(str_comm);
        // 设置消息body
        message.addBody(txtBody);
        // 设置要发给谁,用户username或者群聊groupid
        message.setReceipt(tochatname);
        // 把messgage加到conversation中
        conversation.insertMessage(message);
        // 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法

        final long start = System.currentTimeMillis();
        message.setAttribute("fromRealName", YMApplication.getLoginInfo().getData().getRealname());
        message.setAttribute("fromAvatar", YMApplication.getLoginInfo().getData().getPhoto());

        if (TextUtils.isEmpty(realname)) {
            tochatname = tochatname.replaceAll("did_", "");
            tochatname = tochatname.replaceAll("uid_", "");
            message.setAttribute("toRealName", "用户" + tochatname);
        } else {
            message.setAttribute("toRealName", realname);
        }

        message.setAttribute("toAvatar", head_img);

        //stone 新添加的扩展字段区别是医脉搏还是用药助手
        if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
            message.setAttribute("source", "selldrug");
        }
        // 调用sdk发送异步发送方法
        EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

            @Override
            public void onSuccess() {
                // umeng自定义事件，
                // sendEvent2Umeng(message, start);
                //
                // updateSendedView(message, holder);
            }

            @Override
            public void onError(int code, String error) {
                // sendEvent2Umeng(message, start);
                //
                // updateSendedView(message, holder);
            }

            @Override
            public void onProgress(int progress, String status) {
            }

        });

    }

    public void sendAddCardText(String tochatname, String img, String type, boolean isGroup) {

        DLog.i(TAG, "sendCard" + tochatname + "=====img=====" + img + "=====" + type);
        EMConversation conversation = EMChatManager.getInstance().getConversation(tochatname);
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
        // 如果是群聊，设置chattype,默认是单聊
        // if (chatType == CHATTYPE_GROUP)
        if (isGroup) {
            message.setChatType(EMMessage.ChatType.GroupChat);
        }
        EMTextMessageBody txtBody = new EMTextMessageBody("分享了一个链接");
        // 设置消息body
        message.addBody(txtBody);
        // 设置要发给谁,用户username或者群聊groupid
        message.setReceipt(tochatname);
        // 把messgage加到conversation中
        conversation.insertMessage(message);
        // 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法

        final long start = System.currentTimeMillis();
        message.setAttribute("fromRealName", YMApplication.getLoginInfo().getData().getRealname());
        message.setAttribute("fromAvatar", YMApplication.getLoginInfo().getData().getPhoto());

        if (TextUtils.isEmpty(realname)) {
            realname = hx_usernam.replaceAll("did_", "");
            realname = hx_usernam.replaceAll("uid_", "");
            message.setAttribute("toRealName", "用户" + realname);
        } else {
            message.setAttribute("toRealName", realname);
        }

        ShareCardInfo sc = new ShareCardInfo();
        //@"fType"
        if (type.equals("shareNameCard")) {
            sc.setFriendXywyId(currentFriendId);
            sc.setHuanxinId("did_" + currentFriendId);
            sc.setFriendName(fCardName);
            sc.setfCardDpart(fCardDpart);
            sc.setfCardHospital(fCardHospital);
            sc.setfCardTitle(fCardTitle);
        } else if (type.equals("shareCardDc")) {
            sc.setFriendXywyId(currentFriendId);
            sc.setHuanxinId("sid_" + currentFriendId);
            sc.setFriendName(fCardName);
            sc.setfCardDpart(fCardDpart);
            sc.setfCardHospital(fCardHospital);
            sc.setfCardTitle(fCardTitle);
        } else if (type.equals("shareNameCardMedia")) {
            sc.setFriendXywyId(currentFriendId);
            sc.setHuanxinId("sid_" + currentFriendId);
            sc.setFriendName(fCardName);
            sc.setfCardDpart(fCardDpart);
            sc.setfCardHospital(fCardHospital);
            sc.setfCardTitle(fCardTitle);
            sc.setfType("media");
        } else {
            sc.setCommentTxt("");
            sc.setShareUrl(url);
            sc.setTitle(title);
            sc.setPosts_id(id);
            if (shareSource != null) {
                sc.setShareSource(shareSource);
            }
        }

        if (shareSource != null && "5".equals(shareSource)) {
            sc.setShareUu(uu);
            sc.setShareVu(vu);
        }



        sc.setImageUrl(imageUrl);

        Gson gson = new Gson();
        String str_msg = gson.toJson(sc);
        DLog.i(TAG, "封装的josn数据" + str_msg);
        message.setAttribute("toAvatar", img);
        if (type.equals("shareCardDc") || type.equals("shareNameCardMedia")) {
            type = "shareNameCard";
        }
        message.setAttribute("newMsgType", type);
        message.setAttribute("msgBody", str_msg);

        //stone 新添加的扩展字段区别是医脉搏还是用药助手
        if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
            message.setAttribute("source", "selldrug");
        }

        // 调用sdk发送异步发送方法
        EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

            @Override
            public void onSuccess() {
                // umeng自定义事件，
                // sendEvent2Umeng(message, start);
                //
                // updateSendedView(message, holder);
                runOnUiThread(new Runnable() {
                    public void run() {
                        ToastUtils.shortToast("已分享");
                        finish();
                    }
                });
            }

            @Override
            public void onError(final int code, final String error) {
                // sendEvent2Umeng(message, start);
                //
                // updateSendedView(message, holder);
                runOnUiThread(new Runnable() {
                    public void run() {
                        ToastUtils.shortToast("分享失败");
                    }
                });

            }

            @Override
            public void onProgress(int progress, String status) {
            }

        });

    }

    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {

            chatName = data.getStringExtra(GroupListActivity.GROUP_ID_INTENT_KEY);
            realname = data.getStringExtra(GroupListActivity.GROUP_NAME_INTENT_KEY);
            head_img = data.getStringExtra(GroupListActivity.GROUP_HEAD_URL_INTENT_KEY);

            if (dialog != null) {
                dialog.dismiss();
            }
            DLog.i(TAG, "link url share:" + type);
            if (type.equals("shareNameCard")) {
                title = fCardName + "   " + fCardTitle;
                showDialog(builder, fCardName, true);
            } else if (type.equals("shareCardDc")) {   //shareCardDc
                title = fCardName + "   " + fCardTitle;
                showDialog(builder, fCardName, true);
            } else {
                Dialog(ShareCardActivity.this, head_img, title, imageUrl, true);
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }


        super.onDestroy();
    }
}
