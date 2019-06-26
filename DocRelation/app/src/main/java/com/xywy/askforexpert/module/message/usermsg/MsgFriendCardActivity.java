package com.xywy.askforexpert.module.message.usermsg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ACache;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.AddressBook;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;
import com.xywy.askforexpert.module.main.media.MediaCenterActivity;
import com.xywy.askforexpert.module.main.service.person.PersonDetailActivity;
import com.xywy.askforexpert.module.message.adapter.ContactAdapter;
import com.xywy.askforexpert.module.message.friend.CardNewFriendActivity;
import com.xywy.askforexpert.module.message.imgroup.GroupListActivity;
import com.xywy.askforexpert.module.message.imgroup.constants.GroupListShowType;
import com.xywy.askforexpert.module.message.msgchat.ChatMainActivity;
import com.xywy.askforexpert.widget.Sidebar2;
import com.xywy.easeWrapper.db.InviteMessage;
import com.xywy.easeWrapper.db.InviteMessgeDao;
import com.xywy.easeWrapper.domain.EaseUser;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 名片夹 stone
 */
public class MsgFriendCardActivity extends YMBaseActivity {

    private static final String TAG = "MsgFriendCardActivity";
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
    /**
     * 类型
     */
    private int type = -1;

    private Handler hander = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
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
                    if (pListInfo.getCode().equals("0") & size > 0) {
                        listView.setVisibility(View.VISIBLE);
                        no_data.setVisibility(View.GONE);
                        content = pListInfo.getData();
                        soft();
                        if (listView != null & content != null) {
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
                    ToastUtils.shortToast("网络连接超时");
                    no_data.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }

    };

    public void setContentList() {
        Map<String, EaseUser> userlist = new HashMap<String, EaseUser>();
        for (int i = 0; i < pListInfo.getData().size(); i++) {

            String namehx = pListInfo.getData().get(i).getHxusername();
            EaseUser user = new EaseUser(namehx);
            user.setHeader(pListInfo.getData().get(i).getHeader());
            String userRealname = pListInfo.getData().get(i).getRealname();
            if (TextUtils.isEmpty(userRealname)) {
                userRealname = "用户" + namehx;
            }
            user.setUserRealName(userRealname);
            // setUserHearder(username, user);
            userlist.put(namehx, user);
        }
        YMApplication.getInstance().setContactList(userlist);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mCache = ACache.get(this);
        // 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
            return;
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.card_holder_number;
    }

    @Override
    protected void beforeViewBind() {

    }

    @Override
    protected void initView() {
        hideCommonBaseTitle();

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
        edit = (EditText) findViewById(R.id.search_bar_view);
        no_data = (LinearLayout) findViewById(R.id.lin_nodata);
        tv_nodata_title = (TextView) findViewById(R.id.tv_nodata_title);
        tv_nodata_title.setText("暂无好友");
        img_nodata = (ImageView) findViewById(R.id.img_nodate);
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
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    String username = adapter.getItem(position).getHxusername();

                    type = adapter.getItem(position).getType();

                    //type 0 我的助理，1 医生，2 病历研讨班，3 媒体号 类型
                    Intent intent = new Intent();
                    switch (type) {
                        case 0:
                            intent.setClass(MsgFriendCardActivity.this, ChatMainActivity.class);
                            intent.putExtra("userId", adapter.getItem(position).getHxusername());
                            intent.putExtra("username", adapter.getItem(position).getRealname());
                            intent.putExtra("toHeadImge", adapter.getItem(position).getPhoto());
                            startActivity(intent);
                            break;
                        case 1:
                            //判断认证 stone
                            DialogUtil.showUserCenterCertifyDialog(MsgFriendCardActivity.this, new MyCallBack() {
                                @Override
                                public void onClick(Object data) {
                                    PersonDetailActivity.start(MsgFriendCardActivity.this, adapter.getItem(position).getId(), 3 + "");
                                }
                            }, null, null);
                            break;
                        case 2:
                            intent.setClass(MsgFriendCardActivity.this, MediaCenterActivity.class);

                            intent.putExtra("mediaList", (Serializable) adapter.getItem(position).getData());
                            intent.putExtra("type", 2);
                            startActivity(intent);
                            break;
                        case 3:
                            intent.setClass(MsgFriendCardActivity.this, MediaCenterActivity.class);
//                            intent.putExtra("mediaList", (Serializable) adapter.getItem(position).getData());
                            intent.putExtra("type", 3);
                            startActivity(intent);
                            break;
                        case 5:
                            //                            GroupInfoActivity.start(MsgFriendCardActivity.this,"207106433559298476","测试群组");
                            GroupListActivity.start(MsgFriendCardActivity.this, GroupListShowType.SHOW);
                            StatisticalTools.eventCount(MsgFriendCardActivity.this, "Group");
                            break;
                        default:
                            break;
                    }

                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    return false;
                }

            });
            re_new_frident.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    startActivity(new Intent(MsgFriendCardActivity.this, CardNewFriendActivity.class));
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
    }


    @Override
    protected void initData() {
        //测试更改

    }

    public void getData() {


        String did = YMApplication.getLoginInfo().getData().getPid();
        //		AddressBook	pListInfo1 = (AddressBook)mCache.getAsObject("tag");
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
        DLog.d(TAG, "名片夹url = " + CommonUrl.Patient_Manager_Url + "?" + params.toString());
        fh.get(CommonUrl.Patient_Manager_Url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                LogUtils.i("名片夹返回数据" + t.toString());
                pListInfo = ResolveJson.R_CardHold(t.toString());
                hander.sendEmptyMessage(100);
                mCache.put("card" + uid, pListInfo);
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
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
            public int compare(AddressBook lhs, AddressBook rhs) {
                return lhs.getHeader().compareTo(rhs.getHeader());
            }
        });

    }


    public void cardListener(View v) {
        if (v.getId() == R.id.card_back) {
            finish();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (YMApplication.isrefresh) {
            refresh();
            YMApplication.isrefresh = false;
        }
    }


    public void refresh() {
        // 设置adapter
        if (YMUserService.isGuest()) {
            no_data.setVisibility(View.VISIBLE);
        } else {
            getData();
        }

    }
}
