package com.xywy.askforexpert.module.message.friend;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.base.view.ProgressDialog;
import com.xywy.easeWrapper.EMContactManager;
import com.xywy.uilibrary.titlebar.ItemClickListener;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证消息
 *
 * @author 王鹏
 * @2015-6-2下午7:33:41 modify shr 2016-1-21
 */
public class AddCardHoldVerifyActiviy extends YMBaseActivity {
    private static final String TAG = "AddCardHoldVerifyActiviy";
    private EditText edittext;
    private ImageButton clearSearch;
    private InputMethodManager inputMethodManager;
    private String toAddUsername;
//    private TextView tv_send;
    private Map<String, String> map = new HashMap<String, String>();

    private SharedPreferences sp;

    private ProgressDialog dialog;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.add_card_holder_verif);
//        CommonUtils.initSystemBar(this);
//        sp = getSharedPreferences("login", Context.MODE_PRIVATE);
//        sp.edit().putString("mustUpdata", "").apply();
//        edittext = (EditText) findViewById(R.id.query);
//        clearSearch = (ImageButton) findViewById(R.id.search_clear);
//        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        toAddUsername = getIntent().getStringExtra("toAddUsername");
//        tv_send = (TextView) findViewById(R.id.tv_send);
//        edittext.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int arg1, int arg2,
//                                      int arg3) {
//                // TODO Auto-generated method stub
//                if (s.length() > 0) {
//                    clearSearch.setVisibility(View.VISIBLE);
//                } else {
//                    clearSearch.setVisibility(View.INVISIBLE);
//                }
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence arg0, int arg1,
//                                          int arg2, int arg3) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable arg0) {
//                // TODO Auto-generated method stub
//
//            }
//        });
//
//        clearSearch.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                edittext.getText().clear();
//                hideSoftKeyboard();
//            }
//        });
//        tv_send.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                // TODO Auto-generated method stub
//
//                if (TextUtils.isEmpty(edittext.getText().toString().trim())) {
//                    ToastUtils.shortToast(
//                            "给您的好友说些什么吧");
//                    return;
//                }
////                if (YMApplication.getContactList() != null) {
////                    if (YMApplication.getContactList()
////                            .containsKey(toAddUsername)) {
////                        T.showNoRepeatShort(AddCardHoldVerifyActiviy.this,
////                                "此用户已是您的好友!");
////                        return;
////                    }
////                }
//
//                if (edittext.getText().toString().trim().length() < 20) {
//                    // SharedPreferences sp = getSharedPreferences("login",
//                    // Context.MODE_PRIVATE);
//                    // sp.edit().putString("mustUpdata", "mustUpdata").commit();
//                    String str = toAddUsername.substring(
//                            toAddUsername.indexOf("_") + 1,
//                            toAddUsername.length());
//                    DLog.i(TAG, "添加关注好友。。" + str + "..."
//                            + toAddUsername);
//
//                    sendAddFriend(str);
////					send(edittext.getText().toString());
//                } else {
//                    ToastUtils.shortToast(
//                            "验证信息 不得超过20个字符");
//                }
//            }
//        });
//
//    }

    @Override
    protected int getLayoutResId() {
        return R.layout.add_card_holder_verif;
    }

    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("验证消息");
        titleBarBuilder.addItem("发送", new ItemClickListener() {
            @Override
            public void onClick() {
                if (TextUtils.isEmpty(edittext.getText().toString().trim())) {
                    ToastUtils.shortToast(
                            "给您的好友说些什么吧");
                    return;
                }
//                if (YMApplication.getContactList() != null) {
//                    if (YMApplication.getContactList()
//                            .containsKey(toAddUsername)) {
//                        T.showNoRepeatShort(AddCardHoldVerifyActiviy.this,
//                                "此用户已是您的好友!");
//                        return;
//                    }
//                }

                if (edittext.getText().toString().trim().length() < 20) {
                    // SharedPreferences sp = getSharedPreferences("login",
                    // Context.MODE_PRIVATE);
                    // sp.edit().putString("mustUpdata", "mustUpdata").commit();
                    String str = toAddUsername.substring(
                            toAddUsername.indexOf("_") + 1,
                            toAddUsername.length());
                    DLog.i(TAG, "添加关注好友。。" + str + "..."
                            + toAddUsername);

                    sendAddFriend(str);
//					send(edittext.getText().toString());
                } else {
                    ToastUtils.shortToast(
                            "验证信息 不得超过20个字符");
                }
            }
        }).build();

        sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        sp.edit().putString("mustUpdata", "").apply();
        edittext = (EditText) findViewById(R.id.query);
        clearSearch = (ImageButton) findViewById(R.id.search_clear);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        toAddUsername = getIntent().getStringExtra("toAddUsername");
//        tv_send = (TextView) findViewById(R.id.tv_send);
        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
                if (s.length() > 0) {
                    clearSearch.setVisibility(View.VISIBLE);
                } else {
                    clearSearch.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }
        });

        clearSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                edittext.getText().clear();
                hideSoftKeyboard();
            }
        });
    }

    @Override
    protected void initData() {
    }

    public void sendAddFriend(String touserid) {
        dialog = new ProgressDialog(AddCardHoldVerifyActiviy.this, "正在发送验证");
        dialog.setCanceledOnTouchOutside(false);
        dialog.showProgersssDialog();
        String userid = YMApplication.getLoginInfo().getData().getPid();
        String bind = userid + touserid;
        String sign = MD5Util.MD5(bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("a", "dcFriend");
        params.put("m", "friend_add");
        params.put("userid", userid);
        params.put("touserid", touserid);
        params.put("bind", bind);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        DLog.i(TAG, "添加关注好友。。" + t.toString());
                        map = ResolveJson.R_Action(t.toString());
                        if (map != null) {
                            String userName = YMApplication.getLoginInfo().getData().getRealname();

                            if (userName != null && !TextUtils.isEmpty(userName)) {
                                send(edittext.getText().toString() + "|" + userName);
                            } else {
                                send(edittext.getText().toString());
                            }

                            ToastUtils.shortToast(
                                    map.get("msg"));
                            finish();
                            dialog.dismiss();
                        }
                        super.onSuccess(t);
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        dialog.dismiss();
                        DLog.i(TAG, "添加好友验证失败信息。。" + strMsg);
                        ToastUtils.shortToast(
                                "网络异常");
                        super.onFailure(t, errorNo, strMsg);
                    }
                });
    }

//    public void onClick_back(View view) {
//        switch (view.getId()) {
//            case R.id.btn1:
//                finish();
//                break;
//            // case R.id.tv_send:
//            // send(edittext.getText().toString());
//            // break;
//
//            default:
//                break;
//        }
//    }

    public void send(final String reason) {

        new Thread(new Runnable() {
            public void run() {

                try {


                    EMContactManager.getInstance().addContact(toAddUsername,
                            reason);


                    runOnUiThread(new Runnable() {
                        public void run() {
//							progressDialog.dismiss();

                            ToastUtils.shortToast(
                                    "发送成功");

                        }
                    });
                } catch (final Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            DLog.i(TAG, "错误日志" + e);
//							progressDialog.dismiss();
                            ToastUtils.shortToast(
                                    "发送失败");
                        }
                    });
                }
            }
        }).start();
    }

    void hideSoftKeyboard() {
        if (AddCardHoldVerifyActiviy.this.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (AddCardHoldVerifyActiviy.this.getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(
                        AddCardHoldVerifyActiviy.this.getCurrentFocus()
                                .getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        StatisticalTools.onResume(AddCardHoldVerifyActiviy.this);

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        StatisticalTools.onPause(AddCardHoldVerifyActiviy.this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {


        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        super.onDestroy();
    }
}
