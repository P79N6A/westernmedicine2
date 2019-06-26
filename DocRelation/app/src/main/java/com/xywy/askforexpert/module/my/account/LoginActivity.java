package com.xywy.askforexpert.module.my.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xywy.askforexpert.MainActivity;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.DoctorAPI;
import com.xywy.askforexpert.appcommon.net.utils.JudgeNetIsConnectedReceiver;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.imageutils.DrawableImageLoader;
import com.xywy.askforexpert.model.LoginInfo;
import com.xywy.askforexpert.module.main.service.que.QuePerActivity;
import com.xywy.askforexpert.widget.CircleImageView;
import com.xywy.base.view.ProgressDialog;
import com.xywy.uilibrary.dialog.pndialog.XywyPNDialog;
import com.xywy.uilibrary.dialog.pndialog.listener.PositiveDialogListener;

import butterknife.Bind;
import butterknife.OnClick;

import static com.xywy.askforexpert.module.consult.ConsultConstants.LOG_OUT_FALG;

/**
 * 登陆
 *
 * @author 王鹏
 * @2015-4-15上午10:12:26
 */
public class LoginActivity extends YMBaseActivity {

    private static final String TAG = "LoginActivity";
    private EditText usernameEditText, passwordEditText;

    @Bind(R.id.tv_check)
    TextView tv_check;

    @Bind(R.id.tv_service_clause)
    TextView tv_service_clause;

    private boolean isServiceClauseChecked;

//    /**
//     * 用户名
//     */
//    private String currentUsername;
//    /**
//     * 密码
//     */
//    private String currentPassword;
    /**
     * 是否自动登陆
     */
    private boolean autoLogin = false;
    private LoginInfo logininfo = new LoginInfo();
    private SharedPreferences sp;
    private InputMethodManager manager;
    private CircleImageView ivHeaderView;
    private DrawableImageLoader imageLoader;
    private JudgeNetIsConnectedReceiver connectedReceiver;
    private RelativeLayout bgView;
    private ProgressDialog dialog;
    private String doctorKey; // 医圈过来登陆
    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    protected void beforeViewBind() {
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        imageLoader = DrawableImageLoader.getInstance();

        sp = getSharedPreferences("save_user", MODE_PRIVATE);
        // doctorCicleKey
        doctorKey = getIntent().getStringExtra(DoctorAPI.doctorCicleKey);
    }

  static final String USE_CACHE_PASSWORD="!@&%#(&#)$*^!*#";
    @Override
    protected void initView() {
        hideCommonBaseTitle();
        usernameEditText = (EditText) findViewById(R.id.et_login_uname);
        passwordEditText = (EditText) findViewById(R.id.et_login_pwd);

        ivHeaderView = (CircleImageView) findViewById(R.id.iv_head_icon);

        bgView = (RelativeLayout) findViewById(R.id.login_layout_bg);

        String username = sp.getString("user_name", "").trim();
        usernameEditText.setText(username);
        String pass_word = sp.getString("pass_word", "").trim();
        if (!TextUtils.isEmpty(pass_word)){
            passwordEditText.setText(pass_word);
        }
        if (LOG_OUT_FALG){
            passwordEditText.setText("");
            LOG_OUT_FALG = false;
        }
        usernameEditText.setSelection(username.length());
        ImageLoadUtils.INSTANCE.loadImageView(ivHeaderView,sp.getString("photo", ""),R.drawable.icon_photo_def);
//        if (sp.getString("photo", "").equals("")) {
//            ivHeaderView.setBackgroundResource(R.drawable.icon_photo_def);
//        } else {
//            ivHeaderView.setTag(sp.getString("userid", "1001") + sp.getString("photo", ""));
//            Drawable cachedImage = imageLoader.loadDrawable(sp.getString("photo", ""), sp.getString("userid", "1001") + sp.getString("photo", ""), new ImageLoadCallback(sp.getString("photo", ""), ivHeaderView));
//            if (cachedImage != null) {
//                ivHeaderView.setBackgroundDrawable(cachedImage);
//            } else {
//                ivHeaderView.setBackgroundResource(R.drawable.icon_photo_def);
//            }
//        }
        bgView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideKeyboard();
            }
        });
    }

    @Override
    protected void initData() {
        //2017-01-16 廖阁说登录时 默认都改为选中状态
        isServiceClauseChecked = true;
        updateCheckIconState();
    }

    private void updateCheckIconState() {
        tv_check.setSelected(isServiceClauseChecked);
    }

    @OnClick({R.id.btn_login, R.id.tv_check, R.id.tv_service_clause, R.id.btn_register, R.id.tv_forgot_password})
    public void onClick(View v) {
        //点击事件 事件分发处理
        switch (v.getId()) {
            case R.id.tv_check:
                isServiceClauseChecked = !isServiceClauseChecked;
                updateCheckIconState();
                break;
            case R.id.tv_service_clause:
                Intent intent = new Intent(LoginActivity.this, QuePerActivity.class);
                intent.putExtra("isfrom", "服务协议");
                startActivity(intent);
                break;
            case R.id.btn_login:
                if (!isServiceClauseChecked) {
                    new XywyPNDialog.Builder()
                            .setNoNegativeBtn(true)
                            .setNoTitle(true)
                            .setCancelable(false)
                            .setContent("请勾选同意《寻医问药网平台服务协议》，才能登陆医脉。")
                            .setPositiveStr("我知道了")
                            .create(LoginActivity.this, new PositiveDialogListener() {
                                @Override
                                public void onPositive() {

                                }
                            }).show();
                    return;
                }
                StatisticalTools.eventCount(this, "login");
                hideKeyboard();
                if (JudgeNetIsConnectedReceiver.judgeNetIsConnected(this)) {
                    String username = usernameEditText.getText().toString().trim();
                    String pwd = passwordEditText.getText().toString().trim();
                    if (pwd.length() <= 15) {
                        if ("".equals(username)) {
                            Toast.makeText(this, "请输入手机号/用户名", Toast.LENGTH_SHORT).show();
                        } else {
                            if ("".equals(pwd)) {
                                Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                            } else {
                                if (pwd.equals(sp.getString("pass_word", ""))) {
//                                YMUserService.ymLogin(LoginActivity.this,username, sp.getString("pass_word", "").trim());
                                    YMUserService.ymLogin_New(LoginActivity.this, username, sp.getString("pass_word", "").trim());
                                } else {
//                                YMUserService.ymLogin(LoginActivity.this,username, RSATools.strRSA(pwd));
                                    YMUserService.ymLogin_New(LoginActivity.this, username, pwd);
                                }

                            }
                        }
                    }else{
                        ToastUtils.shortToast("密码需为6-15位，请重新输入");
                    }
                } else {
                    Toast.makeText(this, "无网络，请检查网络连接", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.btn_register:
                StatisticalTools.eventCount(this, "register");
                Intent intent1 = new Intent();
                intent1.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent1);
                break;
//            case R.id.btn_guest:
//                StatisticalTools.eventCount(this, "visitor");
//                if (NetworkUtil.isNetWorkConnected()) {
//                    YMUserService.setIsGuest(true);
//                    if (YMApplication.getLogin1() != null) {
//                        LoginInfo.UserData login = logininfo.getData();
//                        login.setPid("0");
//                        YMApplication.getLogin1().setData(login);
//                    }
//                    MobileAgent.getUserInfo("", "3", LoginActivity.this);
//                    toMainPage(LoginActivity.this);
//                } else {
//                    Toast.makeText(this, "无网络，请检查网络连接", Toast.LENGTH_SHORT).show();
//                }
//                break;
            case R.id.tv_forgot_password:
                StatisticalTools.eventCount(this, "ForgetPwd");
                startActivityForResult(new Intent(LoginActivity.this, RetrievePasswordActivity.class), 1010);
                break;
            default:
                break;
        }

    }

    private void toMainPage(final Activity activity) {
        startActivity(new Intent(activity, MainActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(connectedReceiver, filter);
        if (autoLogin) {
            return;
        }
    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN && getCurrentFocus() != null) {
            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    @Override
    protected void onPause() {
        if (connectedReceiver != null) {
            this.unregisterReceiver(connectedReceiver);
        }
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1010 && resultCode == 1010) {
            usernameEditText.setText(data.getStringExtra("username"));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        super.onDestroy();
    }


    static class ImageLoadCallback implements DrawableImageLoader.ImageCallback {
        String tag;
        ImageView view;

        public ImageLoadCallback(String t, ImageView img) {
            tag = t;
            this.view = img;

        }

        @Override
        public void imageLoaded(Drawable imageDrawable, String imageUrl) {
            if (imageDrawable != null && view != null) {
                view.setBackgroundDrawable(imageDrawable);
            }
        }
    }
}
