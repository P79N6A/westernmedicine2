package com.xywy.askforexpert.module.message.friend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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

import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ACache;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.AddressBook;
import com.xywy.askforexpert.module.main.service.person.PersonDetailActivity;
import com.xywy.askforexpert.module.message.adapter.ContactAdapter;
import com.xywy.askforexpert.module.message.msgchat.ChatMainActivity;
import com.xywy.askforexpert.module.message.usermsg.DiscussSettingsActivity;
import com.xywy.askforexpert.widget.Sidebar2;
import com.xywy.easeWrapper.EMContactManager;
import com.xywy.easeWrapper.EaseConstant;
import com.xywy.easeWrapper.db.InviteMessage;
import com.xywy.easeWrapper.db.InviteMessgeDao;
import com.xywy.easeWrapper.domain.EaseUser;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 名片夹 好有列表
 *
 * @author 王鹏
 * @2015-5-14下午3:03:22
 */
public class CardHolderFragment extends Fragment {

    private static final String TAG = "CardHolderFragment";
    public static List<AddressBook> content;
    AddressBook pListInfo;
    private ContactAdapter adapter;
    private List<EaseUser> contactList;
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

    private ACache mCache;
    private Handler hander = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:

                    List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
                    // for (int i = 0; i < msgs.size(); i++)
                    // {
                    // if (msgs.get(i).getStatus().equals(""))
                    // {
                    //
                    // }
                    // }
                    if (msgs != null) {
                        String count = msgs.size() + "";
                        tv_newfriend_num.setText(count);
                    }
                    if (pListInfo == null) {
                        listView.setVisibility(View.GONE);
                        no_data.setVisibility(View.VISIBLE);
                        return;
                    }
                    int size = pListInfo.getData() == null ? -1 : pListInfo.getData().size();
                    if (pListInfo.getCode().equals("0")
                            & size > 0) {
                        listView.setVisibility(View.VISIBLE);
                        no_data.setVisibility(View.GONE);
                        content = pListInfo.getData();
                        soft();
                        if (listView != null & content != null
                                & getActivity() != null) {
                            adapter = new ContactAdapter(YMApplication.getAppContext(), 1, content);

                            listView.setAdapter(adapter);
                            setContentList();
                        }

                    } else {
                        listView.setVisibility(View.GONE);
                        no_data.setVisibility(View.VISIBLE);
                    }
                    break;
                case 500:
                    ToastUtils.shortToast( "网络连接超时");
                    no_data.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.card_holder_number, container, false);
    }

    public void setContentList() {
        Map<String, EaseUser> userlist = new HashMap<String, EaseUser>();
        // for (String username : usernames) {
        // User user = new User();
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        mCache = ACache.get(getActivity());
        // 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
        if (savedInstanceState != null
                && savedInstanceState.getBoolean("isConflict", false)) {
            return;
        }
        inputMethodManager = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        listView = (ListView) getView().findViewById(R.id.list);
        sidebar = (Sidebar2) getView().findViewById(R.id.sidebar);
        sidebar.setListView(listView);
        if (!YMUserService.isGuest()) {
            if (YMApplication.getLoginInfo() != null) {
                uid = YMApplication.getLoginInfo().getData().getPid();
            }

        }
        // 黑名单列表
        // blackList = EMContactManager.getInstance().getBlackListUsernames();
        // contactList = new ArrayList<User>();
        tv_newfriend_num = (TextView) getView().findViewById(
                R.id.tv_newfriend_num);
        re_new_frident = (RelativeLayout) getView().findViewById(
                R.id.re_new_frident);
        edit = (EditText) getActivity().findViewById(R.id.search_bar_view);
        no_data = (LinearLayout) getView().findViewById(R.id.lin_nodata);
        tv_nodata_title = (TextView) getView().findViewById(
                R.id.tv_nodata_title);
        tv_nodata_title.setText("暂无好友");
        img_nodata = (ImageView) getView().findViewById(R.id.img_nodate);
        img_nodata.setBackgroundResource(R.drawable.nofriend);
        if (YMApplication.getAppContext() != null) {
            inviteMessgeDao = new InviteMessgeDao();
            if (YMUserService.isGuest()) {
                // new YMOtherUtils(getActivity()).LoginDialog();
                no_data.setVisibility(View.VISIBLE);
            } else {
                List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();

                if (msgs != null) {
                    int count = msgs.size();
                    for (InviteMessage inviteMessage : msgs) {
                        if (inviteMessage.getFrom().contains("sid")) {
                            count = count - 1;
                        }
                    }

                    tv_newfriend_num.setText(count + "");
                }

                getData();

            }
            listView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    String username = adapter.getItem(position).getHxusername();
                    if (EaseConstant.NEW_FRIENDS_USERNAME.equals(username)) {
                        // // 进入申请与通知页面
                        // User user = YMApplication.getInstance().getContactList()
                        // .get(EaseConstant.NEW_FRIENDS_USERNAME);
                        // user.setUnreadMsgCount(0);
                        // startActivity(new Intent(getActivity(),
                        // NewFriendsMsgActivity.class));
                    }
                    // else if (Constant.GROUP_USERNAME.equals(username))
                    // {
                    // // 进入群聊列表页面
                    // startActivity(new Intent(getActivity(),
                    // GroupsActivity.class));
                    // }
                    else if ("doctor_assistant".equals(adapter.getItem(position)
                            .getHxusername())) {
                        Intent intent = new Intent(getActivity(),
                                ChatMainActivity.class);
//                    intent.putExtra("type","default");
                        intent.putExtra("userId", adapter.getItem(position)
                                .getHxusername());
                        intent.putExtra("username", adapter.getItem(position)
                                .getRealname());
                        intent.putExtra("toHeadImge", adapter.getItem(position)
                                .getPhoto());
                        startActivity(intent);
                    }
//                else if ("sid_87170554".equals(adapter.getItem(position).getHxusername())){
//                    Intent intent = new Intent(getActivity(),
//                            ChatMainActivity.class);
//                    intent.putExtra("userId", adapter.getItem(position)
//                            .getHxusername());
//                    intent.putExtra("username", adapter.getItem(position)
//                            .getRealname());
//                    intent.putExtra("toHeadImge", adapter.getItem(position)
//                            .getPhoto());
//                    startActivity(intent);
//                }
                    else {
                        // demo中直接进入聊天页面，实际一般是进入用户详情页
                        //
                        // Intent intent = new Intent(getActivity(),
                        // ChatMainActivity.class);
                        // intent.putExtra("userId", adapter.getItem(position)
                        // .getHxusername());
                        // intent.putExtra("username", adapter.getItem(position)
                        // .getRealname());
                        // intent.putExtra("toHeadImge", adapter.getItem(position)
                        // .getPhoto());
                        // startActivity(intent);

                        DLog.i(TAG, "环信id 。。。 "
                                + adapter.getItem(position).getHxusername());
                        DLog.i(TAG, "自己id 。。。 "
                                + YMApplication.getLoginInfo().getData().getPid());

                        if (adapter.getItem(position).getHxusername().contains("sid")) {
                            Intent intent = new Intent(getActivity(),
                                    DiscussSettingsActivity.class);
                            intent.putExtra("uuid", adapter.getItem(position).getHxusername().replaceAll("sid_", ""));
                            intent.putExtra("isDoctor", 3 + "");
                            startActivity(intent);
                        } else {
                            PersonDetailActivity.start(getActivity(), adapter.getItem(position).getId(), "3");
                        }

                    }
                }
            });

            listView.setOnItemLongClickListener(new OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    return false;
                }

            });
            re_new_frident.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub

                    startActivity(new Intent(getActivity(),
                            CardNewFriendActivity.class));
                }
            });
            edit.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    if (adapter != null) {

                        adapter.setData(content);
                        adapter.getFilter().filter(s);

                    }

                }

                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                public void afterTextChanged(Editable s) {

                }
            });

        }

    }

    public void refresh() {
        // 获取设置contactlist
        // getContactList();

        // 设置adapter
        if (YMUserService.isGuest()) {
            no_data.setVisibility(View.VISIBLE);
        } else {
            getData();
        }

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        if (YMApplication.isrefresh) {
            refresh();
            YMApplication.isrefresh = false;
        }
        StatisticalTools.fragmentOnResume("CardHolderFragment");
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden) {
            refresh();
        }
    }

    public void getData() {


        String did = YMApplication.getLoginInfo().getData().getPid();
//		AddressBook	pListInfo1 = (AddressBook)mCache.getAsObject("tag");
        String bind = YMApplication.getLoginInfo().getData()
                .getHuanxin_username();
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
        fh.get(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        // TODO Auto-generated method stub
                        DLog.i(TAG, "名片夹返回数据" + t.toString());
                        pListInfo = ResolveJson.R_CardHold(t.toString());
                        hander.sendEmptyMessage(100);
                        mCache.put("card" + uid, pListInfo);
                        super.onSuccess(t);
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        // TODO Auto-generated method stub
                        AddressBook cachePlistInfo = (AddressBook) mCache.getAsObject("card" + uid);
                        if (cachePlistInfo != null) {
                            pListInfo = cachePlistInfo;
                            hander.sendEmptyMessage(100);
                        }

                        super.onFailure(t, errorNo, strMsg);
                    }
                });

    }

    public void soft() {
        Collections.sort(content, new Comparator<AddressBook>() {

            @Override
            public int compare(AddressBook lhs,
                               AddressBook rhs) {
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
        contactList.clear();

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
            // Map<String, User> users =
            // YMApplication.getInstance().getContactList();
            Iterator<Entry<String, EaseUser>> iterator = userlist.entrySet()
                    .iterator();
            while (iterator.hasNext()) {
                Entry<String, EaseUser> entry = iterator.next();
                if (!entry.getKey().equals(EaseConstant.NEW_FRIENDS_USERNAME)
                        && !entry.getKey().equals(EaseConstant.GROUP_USERNAME)
                        && !blackList.contains(entry.getKey())) {
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


    public void onPause() {
        super.onPause();
        StatisticalTools.fragmentOnPause("CardHolderFragment");
    }


}
