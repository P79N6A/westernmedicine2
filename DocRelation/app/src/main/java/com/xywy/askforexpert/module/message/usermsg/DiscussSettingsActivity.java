package com.xywy.askforexpert.module.message.usermsg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.exceptions.HyphenateException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.DoctorAPI;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DensityUtils;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.followList.IsFollowData;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;
import com.xywy.askforexpert.module.docotorcirclenew.activity.MyDynamicNewActivity;
import com.xywy.askforexpert.module.docotorcirclenew.model.PublishType;
import com.xywy.askforexpert.module.message.msgchat.ChatMainActivity;
import com.xywy.askforexpert.module.message.share.ShareCardActivity;
import com.xywy.askforexpert.widget.CircleImageView;
import com.xywy.base.view.ProgressDialog;
import com.xywy.easeWrapper.EMContactManager;
import com.xywy.easeWrapper.db.UserDao;
import com.xywy.easeWrapper.domain.EaseUser;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import java.util.Map;

/**
 * 服务号详情 stone
 */
public class DiscussSettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "DiscussSettingsActivity";
    private Toolbar toolbar;
    private CircleImageView avatarImgIcon;
    private TextView avatarName;
    private TextView avatarClass;
    private TextView discussFuncIntro;
    private ImageButton receiveMsg;
    private ImageButton msgTop;
    private Button btn_enter;
    private LinearLayout msgHistory;
    private CoordinatorLayout contentPage;
    private PopupWindow settingsWindow;

    private String uuid = "";
    private String isDoctor = "";
    private String touserid = "";

    private String realName = "";

    private String sign = "";

    private ProgressDialog pro;

    private int is_msg = -1;          //接受消息开关

    private int receiveType = -1;       //type = 1 取消接收消息 type = 0 打开接收消息

    private boolean isReceive = true;       //默认接受消息

    private boolean isMsgTop = false;         //默认置顶

    private SharedPreferences mPreferences;   //保存本地置顶信息

    private ImageLoader mImageLoader;

    private DisplayImageOptions options;

    private String relation = "";           //是否添加关注，如果大于等于2是关注

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

    private String shareImgUrl = "";

    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);

        setContentView(R.layout.activity_discuss_settings);
        sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        initImageLoader();
        initView();
        initData();
        CommonUtils.setToolbar(this, toolbar);
        setListener();
    }

    private void initImageLoader() {
        mImageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnFail(R.drawable.icon_photo_def)
                .showImageOnLoading(R.drawable.icon_photo_def)
                .showImageForEmptyUri(R.drawable.icon_photo_def)
                .build();
    }

    private void initData() {
        mPreferences = getSharedPreferences("msg_manager", MODE_PRIVATE);
        pro = new ProgressDialog(this, "正在加载中...");
        pro.showProgersssDialog();
        uuid = getIntent().getStringExtra("uuid");
        isDoctor = getIntent().getStringExtra("isDoctor");
        DLog.i(TAG, "个人资料状态" + isDoctor);
        if (YMUserService.isGuest()) {
            touserid = "0";
        } else {
            touserid = YMApplication.getPID();
        }

        if (mPreferences.getBoolean("sid_" + uuid, false)) {
//            isMsgTop = false;
            msgTop.setBackgroundResource(R.drawable.radio_off);
        } else {
//            isMsgTop = true;
            msgTop.setBackgroundResource(R.drawable.radio_on);
        }

        sign = MD5Util.MD5(uuid + Constants.MD5_KEY);
        DoctorAPI.getYanTaoInfos("3", touserid, uuid, uuid, sign,
                new AjaxCallBack() {

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                        pro.closeProgersssDialog();

                        ToastUtils.shortToast("连接网络超时");

                    }

                    @Override
                    public void onStart() {
                        // TODO Auto-generated method stub
                        super.onStart();
                    }

                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        DLog.i(TAG, "病例研讨班设置页数据" + t);
                        pro.closeProgersssDialog();
                        parseJsonData(t);
                    }

                });
    }

    private void parseJsonData(String json) {

        try {
            JSONObject jsonObject = new JSONObject(json);

            String code = jsonObject.getString("code");
            String msg = jsonObject.getString("msg");
            if ("0".equals(code)) {
                JSONObject jsonElement = jsonObject.getJSONObject("data");
                realName = jsonElement.getString("realname");
                fCardDpart = jsonElement.getString("subject");
                relation = jsonElement.getString("relation");

                if (Integer.parseInt(relation) < 2) {
                    btn_enter.setText("添加关注");
                } else {
                    if (btn_enter.getVisibility() == View.GONE) {
                        btn_enter.setVisibility(View.VISIBLE);
                    }
                    btn_enter.setText("进入服务号");
                }
                avatarName.setText(realName);
                avatarClass.setText(fCardDpart);

                discussFuncIntro.setText(jsonElement.getString("synopsis"));
                is_msg = jsonElement.getInt("is_msg");

                shareImgUrl = jsonElement.getString("photo");

                mImageLoader.displayImage(shareImgUrl, avatarImgIcon, options);
                switch (is_msg) {
                    case 1:             //关闭
                        receiveMsg.setBackgroundResource(R.drawable.radio_on);
                        isReceive = false;
                        break;

                    case 0:             //打开
                        receiveMsg.setBackgroundResource(R.drawable.radio_off);
                        isReceive = true;
                        break;
                }
            } else {
                Toast.makeText(DiscussSettingsActivity.this, msg, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setListener() {
        receiveMsg.setOnClickListener(this);
        msgTop.setOnClickListener(this);
        msgHistory.setOnClickListener(this);
        btn_enter.setOnClickListener(this);
    }

    private void initView() {
        contentPage = (CoordinatorLayout) findViewById(R.id.content_page);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        avatarImgIcon = (CircleImageView) findViewById(R.id.avatar_icon);
        avatarName = (TextView) findViewById(R.id.avatar_name);
        avatarClass = (TextView) findViewById(R.id.avatar_class);
        discussFuncIntro = (TextView) findViewById(R.id.discuss_func_intro);
        receiveMsg = (ImageButton) findViewById(R.id.receive_msg_ib);
        msgTop = (ImageButton) findViewById(R.id.msg_top_ib);
        msgHistory = (LinearLayout) findViewById(R.id.msg_history);
        btn_enter = (Button) findViewById(R.id.btn_enter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 查看历史消息
            case R.id.msg_history:
                StatisticalTools.eventCount(this, "xxCaseDiscussAllInfo");
                if (!TextUtils.isEmpty(realName) && !TextUtils.isEmpty(uuid)) {
                    MyDynamicNewActivity.startActivity(this,realName, PublishType.Realname,uuid);
//                    Intent intent = new Intent(DiscussSettingsActivity.this, MyDynamicActivity.class);
//                    intent.putExtra("name", realName);
//                    intent.putExtra("type", "1");
//                    intent.putExtra("uuid", uuid);
//                    intent.putExtra("touserid", touserid);
//                    startActivity(intent);
                }
                break;

            // 分享给好友
            case R.id.share_to_friend:
                StatisticalTools.eventCount(this, "xxCaseDiscussShare");
                closeSettingWindow();
//               SharedUtils.getInstence().initSocialSDK(this, "title", "content", "url", "imgUrl");

//               SharedUtils.getInstence().getmController().openShare(this, false);


                //判断认证 stone
                DialogUtil.showUserCenterCertifyDialog(DiscussSettingsActivity.this, new MyCallBack() {
                    @Override
                    public void onClick(Object data) {
                        if (uuid != null) {
                            Intent intent = new Intent(DiscussSettingsActivity.this,
                                    ShareCardActivity.class);
                            intent.putExtra("id", uuid);
                            intent.putExtra("type", "shareCardDc");
                            intent.putExtra("fCardTitle", fCardTitle);
                            intent.putExtra("fCardHospital", fCardHospital);
                            intent.putExtra("fCardDpart", fCardDpart);
                            intent.putExtra("fCardName", realName);
                            intent.putExtra("imageUrl", shareImgUrl);
                            startActivity(intent);
                        }
                    }
                }, null, null);



                break;

            // 取消或添加关注
            case R.id.not_follow:
                if (Integer.parseInt(relation) < 2) {
                    addFollow();
                } else {
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                EMContactManager.getInstance().deleteContact("sid_" + uuid);
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();
                    deleteRelation(uuid);
                    deleteFriend(uuid);
                    YMApplication.isrefresh = true;
                }
                closeSettingWindow();
                break;

            // 取消设置窗口
            case R.id.cancel:
                closeSettingWindow();
                break;

            case R.id.receive_msg_ib:

                if (isReceive) {
                    isReceive = false;
                    receiveType = 1;
                    receiveMsg.setBackgroundResource(R.drawable.radio_on);
                } else {
                    StatisticalTools.eventCount(this, "xxCaseDiscussRecv");
                    isReceive = true;
                    receiveType = 0;
                    receiveMsg.setBackgroundResource(R.drawable.radio_off);
                }
                String bind = touserid + uuid;
                String msgSign = MD5Util.MD5(bind + Constants.MD5_KEY);
                if (!TextUtils.isEmpty(uuid) && !TextUtils.isEmpty(touserid) && !TextUtils.isEmpty(sign)) {
                    DoctorAPI.getMsgPushState(receiveType + "", uuid, touserid, bind, msgSign, new AjaxCallBack() {
                        @Override
                        public void onSuccess(String s) {
                            super.onSuccess(s);
                            DLog.i(TAG, "是否接收消息" + s);
                        }

                        @Override
                        public void onFailure(Throwable t, int errorNo, String strMsg) {
                            super.onFailure(t, errorNo, strMsg);
                            Toast.makeText(DiscussSettingsActivity.this, "网络不稳定,请稍后修改", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onLoading(long count, long current) {
                            super.onLoading(count, current);
                        }

                        @Override
                        public void onStart() {
                            super.onStart();
                        }
                    });
                }

                break;

            case R.id.msg_top_ib:
                SharedPreferences.Editor edit = getSharedPreferences("msg_manager",
                        MODE_PRIVATE).edit();
                if (isMsgTop) {
                    isMsgTop = false;
                    msgTop.setBackgroundResource(R.drawable.radio_on);
                } else {
                    StatisticalTools.eventCount(this, "xxCaseDiscussUp");
                    isMsgTop = true;
                    msgTop.setBackgroundResource(R.drawable.radio_off);
                }

                DLog.i(TAG, "UUID=" + uuid);
                edit.putBoolean("sid_" + uuid, isMsgTop);
                edit.apply();

                break;

            case R.id.btn_enter:

                //进入公众号

                if (relation.equals("")) {
                    return;
                }
                if (Integer.parseInt(relation) < 2) {
                    addFollow();
                } else {
                    // 进入聊天页面
                    Intent intent = new Intent(DiscussSettingsActivity.this,
                            ChatMainActivity.class);
                    intent.putExtra("userId", "sid_" + uuid);
                    intent.putExtra("username",
                            realName);

                    intent.putExtra("toHeadImge", shareImgUrl);

                    startActivity(intent);
                }


                break;

            default:
                break;
        }
    }

    /**
     * 添加关注
     */
    private void addFollow() {
        String userid = YMApplication.getLoginInfo().getData().getPid();
        String bind = userid + uuid;
        String sign = MD5Util.MD5(bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("a", "dcFriend");
        params.put("m", "friend_add");
        params.put("userid", userid);
        params.put("touserid", uuid);
        params.put("bind", bind);
        params.put("sign", sign);

        FinalHttp request = new FinalHttp();

        DLog.i(TAG, "添加关注" + CommonUrl.doctor_circo_url + "?" + params.toString());

        request.post(CommonUrl.doctor_circo_url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                DLog.i(TAG, "添加关注" + s);
                closeSettingWindow();
                Gson gson = new Gson();
                IsFollowData data = gson.fromJson(s, IsFollowData.class);

                if (data != null) {
                    if (data.getCode().equals("0")) {
                        initData();
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("mustUpdata", "mustUpdata").apply();
                        Toast.makeText(YMApplication.getAppContext(), "关注成功", Toast.LENGTH_SHORT).show();
                    } else {            //已经关注是参数未添加
                        Toast.makeText(YMApplication.getAppContext(), data.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(YMApplication.getAppContext(), "关注失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);

                Toast.makeText(YMApplication.getAppContext(), "关注失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.discuss_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;

            // 研讨班设置窗口
            case R.id.discuss_settings:
                if (!"".equals(relation)) {
                    if (Integer.parseInt(relation) < 2) {
                        showSettings("添加关注");
                    } else {
                        showSettings("取消关注");
                    }
                }

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 弹出设置窗口
     */
    private void showSettings(String showIsFocus) {
        settingsWindow = new PopupWindow();
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(this).inflate(R.layout.discuss_settings_layout, null);
        TextView shareToFriend = (TextView) view.findViewById(R.id.share_to_friend);
        TextView notFollow = (TextView) view.findViewById(R.id.not_follow);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        notFollow.setText(showIsFocus);
        shareToFriend.setOnClickListener(this);
        notFollow.setOnClickListener(this);
        cancel.setOnClickListener(this);
        settingsWindow.setContentView(view);
        settingsWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        settingsWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        settingsWindow.setFocusable(true);
        settingsWindow.setAnimationStyle(R.style.PopupAnimation);
        settingsWindow.setBackgroundDrawable(new ColorDrawable());
        settingsWindow.setOutsideTouchable(true);
        settingsWindow.setTouchable(true);
        DLog.d("MODEL", android.os.Build.MODEL + ", " + Build.BOARD + ", " + Build.MANUFACTURER);
        if ("R7Plus".equals(Build.MODEL)) {
            settingsWindow.showAtLocation(contentPage, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, DensityUtils.dp2px(36));
        } else {
            settingsWindow.showAtLocation(contentPage, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        CommonUtils.backgroundAlpha(this, 0.5f);
        settingsWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                CommonUtils.backgroundAlpha(DiscussSettingsActivity.this, 1f);
            }
        });
    }

    /**
     * 关闭设置窗口
     */
    private void closeSettingWindow() {
        if (settingsWindow != null) {
            if (settingsWindow.isShowing()) {
                settingsWindow.dismiss();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pro != null && pro.isShowing()) {
            pro.dismiss();
        }

    }

    public void deleteRelation(String username) {
        String did = YMApplication.getLoginInfo().getData()
                .getHuanxin_username();
        String bind = did + "sid_" + username;
        Long st = System.currentTimeMillis();

        String sign = MD5Util.MD5(st + bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();

        params.put("timestamp", st + "");
        params.put("bind", bind);
        params.put("a", "chat");
        params.put("m", "delRelation");
        params.put("friend", "sid_" + username);
        params.put("owner", did);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.get(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                    }

                });

    }

    public void deleteFriend(String friendHuanxinId) {
        StatisticalTools.eventCount(this, "xxCaseDiscussFocus");
        String did = YMApplication.getLoginInfo().getData()
                .getPid();
        String bind = did + friendHuanxinId;
        Long st = System.currentTimeMillis();

        String sign = MD5Util.MD5(bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
//        http://pre.yimai.api.xywy.com/app/1.2/index.interface.php?a=dcFriend&m=friend_del&userid=20751339&touserid=87986750&bind=2075133987986750&sign=fc0466b0a065f96ca41a6d8e3b49eccf
        params.put("bind", bind);
        params.put("a", "dcFriend");
        params.put("m", "friend_del");
        params.put("touserid", friendHuanxinId);
        params.put("userid", did);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();

        DLog.i(TAG, "删除好友" + CommonUrl.Patient_Manager_Url + "?" + params.toString());
        fh.get(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        deleteContact("sid_" + uuid);//需异步处理
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("mustUpdata", "mustUpdata").apply();
                        finish();
                    }

                });

    }

    /**
     * 删除联系人
     *
     * @param username
     */
    public void deleteContact(final String username) {

        new Thread(new Runnable() {
            public void run() {
                try {
                    DLog.i(TAG, "删除联系人" + username);
//                    String huanxin_username = YMApplication.getLoginInfo().getData().getHuanxin_username();
//                    DLog.d("HUANXIN", huanxin_username);
//                    DLog.d("HUANXIN", "username = " + username);
//                    EMContactManager.getInstance().deleteContact(username);
                    // 删除db和内存中此用户的数据
                    UserDao dao = new UserDao(YMApplication.getAppContext());
                    dao.deleteContact(username);
                    Map<String, EaseUser> userlist = YMApplication.getContactList();
                    userlist.remove(username);
                    YMApplication.getInstance().setContactList(userlist);
//                    getActivity().runOnUiThread(new Runnable() {
//                        public void run() {
//                            pd.dismiss();
//                            adapter.remove(tobeDeleteUser);
//                            adapter.notifyDataSetChanged();
//
//                        }
//                    });
//                    closeSettingWindow();
                } catch (final Exception e) {
                    e.printStackTrace();
//                    getActivity().runOnUiThread(new Runnable() {
//                        public void run() {
//                            pd.dismiss();
//                            Toast.makeText(getActivity(), st2 + e.getMessage(), 1).show();
//                        }
//                    });

                }

            }
        }).start();

    }
}
