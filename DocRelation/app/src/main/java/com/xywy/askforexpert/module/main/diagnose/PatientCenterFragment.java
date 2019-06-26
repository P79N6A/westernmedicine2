package com.xywy.askforexpert.module.main.diagnose;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.model.PatientListInfo;
import com.xywy.askforexpert.module.main.patient.activity.PatientPersonInfoActiviy;
import com.xywy.askforexpert.module.main.patient.adapter.PatientContactAdapter;
import com.xywy.askforexpert.module.main.patient.service.PatientFriend;
import com.xywy.askforexpert.module.message.friend.NewPatientActiviy;
import com.xywy.askforexpert.widget.Sidebar;
import com.xywy.easeWrapper.EMContactManager;
import com.xywy.easeWrapper.EaseConstant;
import com.xywy.easeWrapper.domain.EaseUser;

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

public class PatientCenterFragment extends Fragment {

    private PatientContactAdapter adapter;
    private List<EaseUser> contactList;
    private ListView listView;
    private boolean hidden;
    private Sidebar sidebar;
    private InputMethodManager inputMethodManager;
    private List<String> blackList;
    private RelativeLayout re_new_frident;
    private PatientListInfo pListInfo;
    private TextView tv_mypatient;
    private TextView tv_newpatient;
    private boolean isFistin;
    private EditText edit;
    private List<PatientListInfo> content;
    private SharedPreferences sp;

    private LinearLayout no_data;
    private TextView tv_nodata_title;
    private ImageView img_nodata;
    private Map<String, String> map = new HashMap<String, String>();
    private Handler hander = new Handler() {
        public void handleMessage(android.os.Message msg) {
            pListInfo = (PatientListInfo) msg.obj;
            switch (msg.what) {
                case 100:
                    if (pListInfo.getCode().equals("0")) {
                        if (pListInfo.getData().getList().size() > 0) {
                            no_data.setVisibility(View.GONE);
                            content = pListInfo.getData().getList();
                            soft();
                            if (content != null) {
                                adapter = new PatientContactAdapter(getActivity(),
                                        R.layout.row_patient, content);
                                listView.setAdapter(adapter);
                            }

                        } else {
                            listView.setVisibility(View.GONE);
                            no_data.setVisibility(View.VISIBLE);
                        }

                        tv_mypatient.setText(pListInfo.getData().getListcount());
                        tv_newpatient.setText(pListInfo.getData().getNewpatient());
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
        return inflater.inflate(R.layout.patient_number_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        sp = getActivity().getSharedPreferences("save_user", Context.MODE_PRIVATE);
        isFistin = sp.getBoolean(YMApplication.getLoginInfo().getData().getPid(), true);
        // 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
        if (savedInstanceState != null
                && savedInstanceState.getBoolean("isConflict", false)) {
            return;
        }
        inputMethodManager = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        no_data = (LinearLayout) getView().findViewById(R.id.lin_nodata);
        tv_nodata_title = (TextView) getView().findViewById(
                R.id.tv_nodata_title);
        tv_nodata_title.setText("暂无患者");
        img_nodata = (ImageView) getView().findViewById(R.id.img_nodate);
        img_nodata.setBackgroundResource(R.drawable.log_nodata);
        if (NetworkUtil.isNetWorkConnected()) {
            PatientFriend.getData(getActivity(), "0", hander, 100);

        } else {
            no_data.setVisibility(View.VISIBLE);
            tv_nodata_title.setText("网络连接失败");
        }
        listView = (ListView) getView().findViewById(R.id.list);
        sidebar = (Sidebar) getView().findViewById(R.id.sidebar);
        tv_mypatient = (TextView) getView().findViewById(R.id.tv_mypatient);
        tv_newpatient = (TextView) getView().findViewById(R.id.tv_newpatient);
        tv_newpatient.setTextColor(getResources().getColor(R.color.red));
        sidebar.setListView(listView);
        re_new_frident = (RelativeLayout) getView().findViewById(
                R.id.re_new_frident);
        edit = (EditText) getActivity().findViewById(R.id.search_bar_view);
        // 黑名单列表
        blackList = EMContactManager.getInstance().getBlackListUsernames();
        contactList = new ArrayList<EaseUser>();
        // 获取设置contactlist
        // getContactList();

        if (isFistin) {
            setGroupList();
        }
        // 设置adapter

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String username = adapter.getItem(position).getRealname();
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
                else {
                    // demo中直接进入聊天页面，实际一般是进入用户详情页
                    // startActivity(new Intent(getActivity(),
                    // ChatActivity.class)
                    // .putExtra("userId", adapter.getItem(position)
                    // .getHxusername()));

                    Intent intent = new Intent(getActivity(),
                            PatientPersonInfoActiviy.class);
                    intent.putExtra("hx_userid", adapter.getItem(position)
                            .getHxusername());
                    intent.putExtra("uid", adapter.getItem(position).getId());
                    startActivity(intent);
                }
            }
        });
        re_new_frident.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent1 = new Intent(getActivity(),
                        NewPatientActiviy.class);
                startActivity(intent1);

            }
        });

        edit.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (adapter != null) {
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

    public void soft() {
        Collections.sort(content, new Comparator<PatientListInfo>() {

            @Override
            public int compare(PatientListInfo lhs, PatientListInfo rhs) {
                return lhs.getHeader().compareTo(rhs.getHeader());
            }
        });

    }

    void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(getActivity()
                                .getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (YMApplication.isrefresh) {
            if (NetworkUtil.isNetWorkConnected()) {
                PatientFriend.getData(getActivity(), "0", hander, 100);
            }
            YMApplication.isrefresh = false;
        }
        StatisticalTools.fragmentOnResume("PatientCenterFragment");
    }

    /**
     * 获取联系人列表，并过滤掉黑名单和排序g
     */
    private void getContactList() {
        contactList.clear();
        // 获取本地好友列表
        Map<String, EaseUser> users = YMApplication.getContactList();
        Iterator<Entry<String, EaseUser>> iterator = users.entrySet().iterator();
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

                        map = ResolveJson.R_Action(t.toString());
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


    public void onPause() {
        super.onPause();
        StatisticalTools.fragmentOnPause("PatientCenterFragment");
    }

}
