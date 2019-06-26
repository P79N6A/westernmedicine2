package com.xywy.askforexpert.module.message.msgchat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.old.BaseActivity;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ClickUtil;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.module.discovery.medicine.common.MyConstant;
import com.xywy.askforexpert.module.discovery.medicine.module.medical.MedicineCartCenter;
import com.xywy.askforexpert.module.discovery.medicine.module.patient.PatientManager;
import com.xywy.askforexpert.module.discovery.medicine.module.patient.entity.Patient;
import com.xywy.askforexpert.module.discovery.medicine.module.patient.request.PatientListRequest;
import com.xywy.askforexpert.module.message.health.HealthyUserInfoDetailActivity;
import com.xywy.askforexpert.module.message.imgroup.ContactService;
import com.xywy.askforexpert.module.message.imgroup.activity.GroupInfoActivity;
import com.xywy.askforexpert.module.message.usermsg.DiscussSettingsActivity;
import com.xywy.askforexpert.widget.view.SlidingMenu;
import com.xywy.base.view.ProgressDialog;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.xywy.util.SharedPreferencesHelper;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;

/**
 * 聊天页
 *
 * @author 王鹏
 * @2015-6-3上午9:18:10
 */
public class ChatMainActivity extends BaseActivity {
    private static final String TAG = "ChatMainActivity";

    private LinearLayout mNetworkError;

    //stone 新添加的
    private final String RECIPE_SICK = "recipe_sick";
    private final String PATIENT_ID = "patientId";

    public static final String IS_HEALTHY_USER_KEY = "isHealthyUser";
    public static final String DIAL_ID = "dialUserId";

    private SlidingMenu mSlidingMenu;
    private FragmentTransaction mTransaction;
    private Menu_Chat_Right_Fragment rightFragment;
    private ChatFragment centerFragment;
    private ImageButton btn2;
    public static ChatMainActivity activityInstance = null;

    private String toChatUsername;
    private String toChatUserRealname;
    private String toHeadImge;
    private String type = "is";
    private String uid;
    private String lastgid;

    public static boolean isPatientFinish = false;
    SharedPreferences sp;

    /**
     * 判断是否为签约医生，默认不是
     */
    private boolean isHealthyUser;
    private String dialId;
    private boolean isGroupChat;

    public TextView textView;

    public boolean isHealthyUser() {
        return isHealthyUser;
    }

    private ProgressDialog progressDialog;

    //	/**
//	 * 页面展示类型 type=default type = seminar
//	 */
//	private String from = "";
//	/**
//	 * 消息体
//	 */
//	private String msgBody = "";

    public static void start(Context context, String groupId, String groupName, String headUrl, boolean isGroup) {
        Intent intent = new Intent(context, ChatMainActivity.class);
        intent.putExtra("isGroup", isGroup);
        intent.putExtra("userId", groupId);
        intent.putExtra("username", groupName);
        intent.putExtra("toHeadImge", headUrl);

        context.startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String newName = intent.getStringExtra("userId");
        if (toChatUsername.equals(newName)) {
            super.onNewIntent(intent);
        } else {
            finish();
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityInstance = this;
        setContentView(R.layout.chat_main);

        CommonUtils.initSystemBar(this);
        init();
        //stone 系统消息不请求
        if (YMApplication.sIsYSZS && !toChatUsername.equals(MyConstant.HX_SYSTEM_ID)) {
            getPatientInfo();
        }

    }

    public void init() {
        sp = getSharedPreferences("save_gid", MODE_PRIVATE);
        isGroupChat = getIntent().getBooleanExtra("isGroup", false);
        toChatUsername = getIntent().getStringExtra("userId");
        toChatUserRealname = getIntent().getStringExtra("username");
        toHeadImge = getIntent().getStringExtra("toHeadImge");
        if (!isGroupChat) {
            // 判断是否为签约医生，默认不是
            isHealthyUser = getIntent().getBooleanExtra(IS_HEALTHY_USER_KEY, false);
            dialId = getIntent().getStringExtra(DIAL_ID);

            if (!TextUtils.isEmpty(toChatUsername)) {
                if (toChatUsername.contains(Constants.QXYL_USER_HXID_MARK)) {
                    isHealthyUser = true;
                    type = Constants.QXYL_USER_HXID_MARK.replace("_", "");
                } else {
                    type = toChatUsername.substring(0, 3);
                }
            }
            DLog.i(TAG, "查看进入的是什么身份。。" + type);
            if (type != null && type.equals("uid")) {
                uid = getIntent().getStringExtra("uid");
                lastgid = sp.getString(toChatUsername, "0");
            }
        }


        initView();
    }

    @Override
    protected void onResume() {
        lastgid = sp.getString(toChatUsername, "0");
        if (textView != null) {
            if (!TextUtils.isEmpty(toChatUsername)) {
                String st = ContactService.getInstance().getGroupName(toChatUsername);
                DLog.d(TAG, "group name st = " + st);
                if (!TextUtils.isEmpty(st)) {
                    textView.setText(st);
                    toChatUserRealname = st;
                }
            }


        }
        super.onResume();
    }

    //stone 给btn2 gone改成invisible 布局更改
    public void initView() {
        if (isPatientFinish) {
            finish();
            isPatientFinish = false;
            return;
        }
        textView = (TextView) findViewById(R.id.tv_title);
        textView.setSelected(true);
        textView.setText(Html.fromHtml(toChatUserRealname));
        btn2 = (ImageButton) findViewById(R.id.btn2);
        if (isHealthyUser()) {
            btn2.setBackgroundResource(R.drawable.user_header_selector);
            btn2.setVisibility(View.VISIBLE);
        } else {
            if ("did".equals(type)) {
                btn2.setVisibility(View.GONE);
            } else if ("uid".equals(type)) {
                btn2.setBackgroundResource(R.drawable.service_topque_right_btn);
                btn2.setVisibility(View.VISIBLE);
            } else if ("qid".equals(type)) {
                btn2.setVisibility(View.INVISIBLE);
            } else if ("doc".equals(type)) {
                btn2.setVisibility(View.INVISIBLE);
            } else if ("sid".equals(type)) {
                btn2.setBackgroundResource(R.drawable.user_header_selector);
                btn2.setVisibility(View.VISIBLE);
            } else if (isGroupChat) {
                btn2.setBackgroundResource(R.drawable.group_icon_right);
                btn2.setVisibility(View.VISIBLE);
            } else {
                btn2.setVisibility(View.INVISIBLE);
            }
        }
        mSlidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
        mSlidingMenu.setCanSliding(false);
        mSlidingMenu.setRightView(getLayoutInflater().inflate(
                R.layout.right_frame, null));
        mSlidingMenu.setCenterView(getLayoutInflater().inflate(
                R.layout.center_frame, null));
        mTransaction = this.getSupportFragmentManager().beginTransaction();
        rightFragment = new Menu_Chat_Right_Fragment();
        centerFragment = new ChatFragment();
        Bundle bundle2 = new Bundle();
//		bundle2.putString("from", from);
//		bundle2.putString("msgBody",msgBody);
        bundle2.putString("userId", toChatUsername);
        bundle2.putString("username", toChatUserRealname);
        bundle2.putString("toHeadImge", toHeadImge);
        bundle2.putString("type", type);
        bundle2.putBoolean(IS_HEALTHY_USER_KEY, isHealthyUser);
        bundle2.putString(DIAL_ID, dialId);
        bundle2.putBoolean("isGroup", isGroupChat);
        if ("uid".equals(type)) {
            bundle2.putString("uid", uid);
            bundle2.putString("lastgid", lastgid);
        }
        if (isGroupChat) {
            bundle2.putInt("chatType", 2);
        }

        centerFragment.setArguments(bundle2);
        rightFragment.setArguments(bundle2);
        mTransaction.replace(R.id.center_frame, centerFragment);
        mTransaction.replace(R.id.right_frame, rightFragment);
        mTransaction.commit();

        //stone
        mNetworkError = (LinearLayout) findViewById(R.id.network_error);
        mNetworkError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ClickUtil.isFastDoubleClick()) {
                    getPatientInfo();
                }
            }
        });

    }

    public void onClickBack(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.btn2:
                if (isHealthyUser) {
                    StatisticalTools.eventCount(this, "ResidentHome");
                    Intent healthyIntent = new Intent(this, HealthyUserInfoDetailActivity.class);
                    healthyIntent.putExtra(HealthyUserInfoDetailActivity.PATIENT_ID_INTENT_KEY,
                            toChatUsername.replaceAll(Constants.QXYL_USER_HXID_MARK, ""));
                    startActivity(healthyIntent);
                    return;
                }
                if ("sid".equals(type)) {
                    Intent senIntent = new Intent(ChatMainActivity.this, DiscussSettingsActivity.class);
                    senIntent.putExtra("uuid", toChatUsername.replaceAll("sid_", ""));
                    senIntent.putExtra("isDoctor", "2");
                    startActivity(senIntent);
                } else if (isGroupChat) {
                    GroupInfoActivity.start(ChatMainActivity.this, toChatUsername);
                } else {
                    mSlidingMenu.showRightView();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 28) {
                Bundle bundle = data.getExtras();
                String str_reply = bundle.getString("isdelete");
                DLog.i(TAG, "删除好2.." + str_reply);
                if ("true".equals(str_reply)) {
                    finish();
                }
            }
        }
    }

    public String getToChatUsername() {
        return toChatUsername;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    //stone 先获取用户信息 赋值给patientmanager
    private void getPatientInfo() {

        String patientId = toChatUsername.replace(MyConstant.HX_ID_PREFIX_DOC, "");
        patientId = patientId.replace(MyConstant.HX_ID_PREFIX_USER, "");
        patientId = patientId.replace("_test", "");
        patientId = patientId.replace("did_", "");
        patientId = patientId.replace("uid_", "");
        int doctorId = Integer.parseInt(YMUserService.getCurUserId());

        PatientListRequest.getInstance().getPatientInfo(doctorId,patientId).subscribe(new BaseRetrofitResponse<BaseData<List<Patient>>>() {
            @Override
            public void onStart() {
                super.onStart();
                mNetworkError.setVisibility(View.GONE);
                showProgressDialog("");
            }

            @Override
            public void onCompleted() {
                hideProgressDialog();
                super.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                hideProgressDialog();
                if (e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof HttpException) {
                    mNetworkError.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNext(BaseData<List<Patient>> entry) {
                super.onNext(entry);
                mNetworkError.setVisibility(View.GONE);
                if (entry != null) {
                    if (entry.getCode() == 10000) {
                        if (entry.getData() != null) {
                            dealwithEntry(entry.getData());
                        } else {
                            LogUtils.i("entry.getData()== null");
                        }
                    } else {
                        LogUtils.i("数据返回有问题 code=" + entry.getCode());
                    }
                } else {
                    LogUtils.i("数据返回有问题 entry=" + entry);
                }
            }
        });
    }

    private void dealwithEntry(List<Patient> data) {
        Patient p = data.get(0);
        String oldPatientId = SharedPreferencesHelper.getIns(ChatMainActivity.this).getString(PATIENT_ID, "");
        if (!p.getUId().equals(oldPatientId)) {
            MedicineCartCenter.getInstance().removeAllMedicine();//不进行缓存，如果给之前的用户添加了处方笺，
            //不进行缓存，清空诊断信息
            SharedPreferencesHelper.getIns(ChatMainActivity.this).putString(RECIPE_SICK, "");
        }
        //将patientId存入sp中，方便对比多次打开的对话页面是否是同一个患者,以便清空处方笺和诊断信息
        SharedPreferencesHelper.getIns(ChatMainActivity.this).putString(PATIENT_ID, p.getUId());

        p.setHx_user(toChatUsername);
        PatientManager.getInstance().setPatient(p);
    }


    /**
     * 显示进度dialog
     *
     * @param content 提示文字内容
     */
    public void showProgressDialog(String content) {
        if (null == progressDialog) {
            progressDialog = new ProgressDialog(ChatMainActivity.this, content);
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
        if (progressDialog.isShowing()) {
            progressDialog.closeProgersssDialog();
        }
    }
}
