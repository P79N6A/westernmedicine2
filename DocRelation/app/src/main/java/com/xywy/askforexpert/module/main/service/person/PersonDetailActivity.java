package com.xywy.askforexpert.module.main.service.person;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hyphenate.exceptions.HyphenateException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.DoctorAPI;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DensityUtils;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.doctor.FocusedUser;
import com.xywy.askforexpert.model.doctor.doctorInfosBean;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;
import com.xywy.askforexpert.module.docotorcirclenew.activity.MyDynamicNewActivity;
import com.xywy.askforexpert.module.docotorcirclenew.model.PublishType;
import com.xywy.askforexpert.module.doctorcircle.FollowListActivity;
import com.xywy.askforexpert.module.message.friend.AddCardHoldVerifyActiviy;
import com.xywy.askforexpert.module.message.msgchat.ChatMainActivity;
import com.xywy.askforexpert.module.message.share.ShareCardActivity;
import com.xywy.askforexpert.widget.CircleImageView;
import com.xywy.askforexpert.widget.view.DeletePopupWindow;
import com.xywy.base.view.ProgressDialog;
import com.xywy.easeWrapper.EMContactManager;
import com.xywy.easeWrapper.domain.EaseUser;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.List;
import java.util.Map;

/**
 * 个人中心 stone
 *
 * @author LG
 */
public class PersonDetailActivity extends Activity implements OnClickListener {
    private static final String TAG = "PersonDetailActivity";
    private ImageView back;
    private TextView username, sex, ages, info, num, person_info, tv_job;
    private String visitedUserId;
    private RelativeLayout dysicle, fo_rl;
    private ProgressDialog pro;
    private LinearLayout lin_send, lin_add;
    private Button btn_send_message, btn_add_newfriend;
    private String isDoctor;
    doctorInfosBean json;
    private LinearLayout main;
    // ImageView btn2;
    ImageView btn2;
    private String touserid;
    DeletePopupWindow menuWindow;
    android.support.v7.app.AlertDialog.Builder dialog1;

    private AlertDialog.Builder shareDialog;

    private String shareImgUrl, shareId, shareRelName;

    private CircleImageView user_pic;

    private TextView fo_enter_tv;
    private ImageView fo_enter_iv;

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

    /**
     * 图片加载器
     */
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;

    private PdGradViewAdapter pdGradViewAdapter;

    private List<FocusedUser> watchedList = null;

    private ImageView find_photo_iv1, find_photo_iv2, find_photo_iv3, find_photo_iv4;

    private SharedPreferences sp;


    public static void start(Context context, String userId, String isDoctor) {
        Intent intent = new Intent(context, PersonDetailActivity.class);
        intent.putExtra("uuid", userId);
        intent.putExtra("isDoctor", isDoctor);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_info);
        CommonUtils.initSystemBar(this);
        sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        sp.edit().putString("mustUpdata", "").apply();
        visitedUserId = getIntent().getStringExtra("uuid");
        isDoctor = getIntent().getStringExtra("isDoctor");
//        DLog.i(TAG, "个人资料状态" + isDoctor);
        if (YMUserService.isGuest()) {
            touserid = "0";
        } else {
            touserid = YMApplication.getLoginInfo().getData().getPid();
        }


        initImageLoader();
        initView();
        initData();
        setLisneer();
    }

    private void initImageLoader() {
        mImageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnFail(R.drawable.icon_photo_def)
                .showImageOnLoading(R.drawable.icon_photo_def)
                .build();
    }

    private void initView() {

        iv_enter = (ImageView) findViewById(R.id.iv_enter);
        main = (LinearLayout) findViewById(R.id.main);
        ll_num = (LinearLayout) findViewById(R.id.ll_num);
        back = (ImageView) findViewById(R.id.iv_back);
        user_pic = (CircleImageView) findViewById(R.id.user_pic);
        username = (TextView) findViewById(R.id.username);
        sex = (TextView) findViewById(R.id.sex);
        info = (TextView) findViewById(R.id.info);
        dysicle = (RelativeLayout) findViewById(R.id.dysicle);
        fo_rl = (RelativeLayout) findViewById(R.id.fo_rl);
        num = (TextView) findViewById(R.id.num);
        person_info = (TextView) findViewById(R.id.person_info);
        btn_send_message = (Button) findViewById(R.id.btn_send_message);
        lin_send = (LinearLayout) findViewById(R.id.lin_send);
        lin_add = (LinearLayout) findViewById(R.id.lin_add);
        btn_add_newfriend = (Button) findViewById(R.id.btn_add_newfriend);
        btn2 = (ImageView) findViewById(R.id.btn2);
        tv_job = (TextView) findViewById(R.id.tv_job);

        find_photo_iv1 = (ImageView) findViewById(R.id.find_photo_iv1);
        find_photo_iv2 = (ImageView) findViewById(R.id.find_photo_iv2);
        find_photo_iv3 = (ImageView) findViewById(R.id.find_photo_iv3);
        find_photo_iv4 = (ImageView) findViewById(R.id.find_photo_iv4);

        fo_enter_tv = (TextView) findViewById(R.id.fo_enter_tv);
        fo_enter_iv = (ImageView) findViewById(R.id.fo_enter_iv);

        // if ("0".equals(isDoctor) || "1".equals(isDoctor)
        // || "2".equals(isDoctor))
        // {
        // lin_add.setVisibility(View.VISIBLE);
        // lin_send.setVisibility(View.GONE);
        // btn2.setVisibility(View.GONE);
        // btn_add_newfriend.setOnClickListener(this);
        // } else if ("3".equals(isDoctor))
        // {
        // lin_send.setVisibility(View.VISIBLE);
        // lin_add.setVisibility(View.GONE);
        // btn_send_message.setOnClickListener(this);
        // }
        //
        if (visitedUserId != null && visitedUserId.equals("0")) {
            lin_add.setVisibility(View.GONE);
            lin_send.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
        } else if (!YMUserService.isGuest()) {
            if (visitedUserId.equals(YMApplication.getLoginInfo().getData().getPid())) {
                lin_add.setVisibility(View.GONE);
                lin_send.setVisibility(View.GONE);
                btn2.setVisibility(View.GONE);
            }
        }

    }

    private void initData() {
        pro = new ProgressDialog(this, "正在加载中...");
        pro.showProgersssDialog();
        pdGradViewAdapter = new PdGradViewAdapter(this);
        String sign = MD5Util.MD5(visitedUserId + Constants.MD5_KEY);
        DoctorAPI.getDoctorInfos(touserid, visitedUserId, visitedUserId, sign,
                new AjaxCallBack() {

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                        if (pro != null) {
                            pro.closeProgersssDialog();
                        }

                        ToastUtils.shortToast("连接网络超时");
                    }

                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        DLog.i(TAG, "个人资料返回数据。。。" + t.toString());
                        if (pro != null) {
                            pro.closeProgersssDialog();
                        }
                        getServerData(t.toString());
                    }

                });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        visitedUserId = intent.getStringExtra("uuid");
        initData();

    }

    private void getServerData(String t) {
        boolean isJsonData = YMOtherUtils.isJsonData(t.toString());
        if (!isJsonData) {
            return;
        }
        Gson mGson = new Gson();
        try {

            json = mGson.fromJson(t, doctorInfosBean.class);
        } catch (Exception e) {
            // TODO: handle exception
            return;
        }
        if (json == null) {
            return;
        }
        if (json.data != null) {
            watchedList = json.data.watchedlist;
            //动态设置fo_gv大小

            if (watchedList != null && watchedList.size() > 0) {
                fo_enter_iv.setVisibility(View.VISIBLE);
                fo_enter_tv.setVisibility(View.GONE);

                showFollowImg(watchedList);

            } else {
                fo_enter_iv.setVisibility(View.INVISIBLE);
                fo_enter_tv.setVisibility(View.VISIBLE);
                fo_rl.setEnabled(false);
            }

            mImageLoader.displayImage(json.data.photo, user_pic, options);
            if (TextUtils.isEmpty(json.data.nickname)) {
                username.setText("用户" + json.data.userid);
            } else {
                username.setText(json.data.nickname);
            }
            shareId = json.data.userid;
            shareImgUrl = json.data.photo;
            shareRelName = json.data.nickname;
            fCardName = json.data.nickname;
            // if ("0".equals(json.data.sex))
            // {
            // sex.setText("女");
            // } else
            // {
            // sex.setText("男");
            // }

            if ("0".equals(json.data.relation)
                    || "1".equals(json.data.relation)
                    || "2".equals(json.data.relation)) {
                lin_add.setVisibility(View.VISIBLE);
                lin_send.setVisibility(View.GONE);
                btn2.setVisibility(View.GONE);
                btn_add_newfriend.setOnClickListener(this);
            } else if ("3".equals(json.data.relation)) {
                lin_send.setVisibility(View.VISIBLE);
                lin_add.setVisibility(View.GONE);
                btn_send_message.setOnClickListener(this);
                btn2.setVisibility(View.VISIBLE);
            } else if ("4".equals(json.data.relation)) {
                lin_add.setVisibility(View.GONE);
                lin_send.setVisibility(View.GONE);
                btn2.setVisibility(View.GONE);
            }

            sex.setText(json.data.subject);
            fCardDpart = json.data.subject;
            info.setText(json.data.hospital);
            fCardHospital = json.data.hospital;
            tv_job.setText(json.data.job);
            fCardTitle = json.data.job;
            if (!"0".equals(json.data.dynamiccount)) {

                ll_num.setVisibility(View.VISIBLE);
                num.setText(json.data.dynamiccount + "条");
            } else {
                ll_num.setVisibility(View.VISIBLE);
                num.setText("暂无动态");
                iv_enter.setVisibility(View.INVISIBLE);
                dysicle.setEnabled(false);
                dysicle.setBackgroundResource(R.color.white);
                // ll_num.setVisibility(View.INVISIBLE);
            }
            if (TextUtils.isEmpty(json.data.synopsis)) {
                person_info.setText("这个人很懒，连个人简介都懒得写");
                person_info.setTextColor(getResources().getColor(
                        R.color.tab_color_nomal));
            } else {
                person_info.setText(json.data.synopsis);
            }

//            if ("3".equals(isDoctor)) {
//                lin_send.setVisibility(View.VISIBLE);
//                lin_add.setVisibility(View.GONE);
//                btn2.setVisibility(View.VISIBLE);
//
//                btn_send_message.setOnClickListener(this);
//            }
            if (!YMUserService.isGuest()) {
                if (visitedUserId.equals(YMApplication.getLoginInfo().getData().getPid())) {
                    lin_add.setVisibility(View.GONE);
                    lin_send.setVisibility(View.GONE);
                    btn2.setVisibility(View.GONE);

                }
            } else {
                lin_add.setVisibility(View.GONE);
                lin_send.setVisibility(View.GONE);
                btn2.setVisibility(View.GONE);
            }

        }
    }

    private void showFollowImg(List<FocusedUser> watchedList) {
        int size = watchedList.size();
        if (size >= 5) {
            size = 4;
        }
        DLog.i(TAG, "关注列表大小" + size);
        switch (size) {
            case 1:
                DLog.i(TAG, "关注列表头像" + watchedList.get(0).photo);
                mImageLoader.displayImage(watchedList.get(0).photo, find_photo_iv1, options);
                break;
            case 2:
                mImageLoader.displayImage(watchedList.get(0).photo, find_photo_iv1, options);
                mImageLoader.displayImage(watchedList.get(1).photo, find_photo_iv2, options);

                break;
            case 3:
                mImageLoader.displayImage(watchedList.get(0).photo, find_photo_iv1, options);
                mImageLoader.displayImage(watchedList.get(1).photo, find_photo_iv2, options);
                mImageLoader.displayImage(watchedList.get(2).photo, find_photo_iv3, options);
                break;
            case 4:
                mImageLoader.displayImage(watchedList.get(0).photo, find_photo_iv1, options);
                mImageLoader.displayImage(watchedList.get(1).photo, find_photo_iv2, options);
                mImageLoader.displayImage(watchedList.get(2).photo, find_photo_iv3, options);
                mImageLoader.displayImage(watchedList.get(3).photo, find_photo_iv4, options);
                break;
        }
    }

    private void setLisneer() {
        back.setOnClickListener(this);
        dysicle.setOnClickListener(this);
        fo_rl.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        Intent intent;
        switch (arg0.getId()) {
            case R.id.iv_back:
                this.finish();
                break;

            case R.id.dysicle:
                StatisticalTools.eventCount(PersonDetailActivity.this, "yqHomepageDynamic");
//               if (json != null && !"0".equals(json.data.dynamiccount)) {
//                   MyDynamicNewActivity.startActivity(PersonDetailActivity.this,json.data.nickname, PublishType.Realname, visitedUserId);
////                    intent = new Intent(getApplicationContext(),
////                            MyDynamicActivity.class);
////                    intent.putExtra("uuid", uuid);
////                    intent.putExtra("name", json.data.nickname);
////                    intent.putExtra("type", "1");
////                    startActivity(intent);
//                } else {
//                    ll_num.setVisibility(View.INVISIBLE);
//                }
                DialogUtil.showUserCenterCertifyDialog(PersonDetailActivity.this, new MyCallBack() {
                    @Override
                    public void onClick(Object object) {
                        if (json != null && !"0".equals(json.data.dynamiccount)) {
                            MyDynamicNewActivity.startActivity(PersonDetailActivity.this,json.data.nickname, PublishType.Realname, visitedUserId);
                        } else {
                            ll_num.setVisibility(View.INVISIBLE);
                        }
                    }
                }, null, null);
                break;
            case R.id.btn_send_message:

                StatisticalTools.eventCount(PersonDetailActivity.this, "yqHomepageSendmesage");

//                if (json != null && json.data != null) {
//                    intent = new Intent(PersonDetailActivity.this,
//                            ChatMainActivity.class);
////                intent.putExtra("type","default");
//                    intent.putExtra("userId", "did_" + json.data.userid);
//                    intent.putExtra("username", json.data.nickname);
//                    intent.putExtra("toHeadImge", json.data.photo);
//
//                    DLog.i(TAG, "发送消息的环信id==" + "did_" + json.data.userid);
//                    startActivity(intent);
//                }
                DialogUtil.showUserCenterCertifyDialog(PersonDetailActivity.this, new MyCallBack() {
                    @Override
                    public void onClick(Object object) {
                        if (json != null && json.data != null) {
                            Intent intent = new Intent(PersonDetailActivity.this,
                                    ChatMainActivity.class);
//                intent.putExtra("type","default");
                            intent.putExtra("userId", "did_" + json.data.userid);
                            intent.putExtra("username", json.data.nickname);
                            intent.putExtra("toHeadImge", json.data.photo);

                            DLog.i(TAG, "发送消息的环信id==" + "did_" + json.data.userid);
                            startActivity(intent);
                        }
                    }
                }, null, null);


                break;
            case R.id.btn_add_newfriend:

                StatisticalTools.eventCount(PersonDetailActivity.this, "yqHomepageAddf");

//                if (json != null && json.data != null) {
//                    intent = new Intent(PersonDetailActivity.this,
//                            AddCardHoldVerifyActiviy.class);
//                    intent.putExtra("toAddUsername", "did_" + json.data.userid);
//                    startActivity(intent);
//                }
                DialogUtil.showUserCenterCertifyDialog(PersonDetailActivity.this, new MyCallBack() {
                    @Override
                    public void onClick(Object object) {
                        if (json != null && json.data != null) {
                            Intent i = new Intent(PersonDetailActivity.this,
                                    AddCardHoldVerifyActiviy.class);
                            i.putExtra("toAddUsername", "did_" + json.data.userid);
                            startActivity(i);
                        }
                    }
                }, null, null);

                break;
            case R.id.fo_rl:

                //点击关注去关注列表

//                if (json != null && json.data != null) {
//                    if (json.data.watchedlist != null && json.data.watchedlist.size() > 0) {
//                        intent = new Intent(PersonDetailActivity.this,
//                                FollowListActivity.class);
////                intent.putExtra("toAddUsername", "did_" + json.data.userid);
//                        intent.putExtra("userid", json.data.userid);
//                        intent.putExtra("type", "1");
//                        startActivity(intent);
//                    }
//                }
                DialogUtil.showUserCenterCertifyDialog(PersonDetailActivity.this, new MyCallBack() {
                    @Override
                    public void onClick(Object object) {
                        if (json != null && json.data != null) {
                            if (json.data.watchedlist != null && json.data.watchedlist.size() > 0) {
                                Intent intent = new Intent(PersonDetailActivity.this,
                                        FollowListActivity.class);
//                intent.putExtra("toAddUsername", "did_" + json.data.userid);
                                intent.putExtra("userid", json.data.userid);
                                intent.putExtra("type", "1");
                                startActivity(intent);
                            }
                        }
                    }
                }, null, null);

                break;
        }
    }

    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn2:
                //获取当前身份状态
                int type = -1;
                if (!YMUserService.isGuest()) {
                    if (YMApplication.isDoctor()) {
                        type = 1;
                    } else {
                        type = 2;
                    }
                } else {
                    type = 0;
                }
                menuWindow = new DeletePopupWindow(PersonDetailActivity.this,
                        itemsOnClick, "删除好友", type);
                // // 显示窗口
                if ("R7Plus".equals(Build.MODEL)) {
                    menuWindow.showAtLocation(main, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, DensityUtils.dp2px(36));
                } else {
                    menuWindow.showAtLocation(main, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }

                backgroundAlpha(0.5f);

                // 添加pop窗口关闭事件
                menuWindow.setOnDismissListener(new poponDismissListener());
                break;

            default:
                break;
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     *
     * @author cg
     */
    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            // Log.v("List_noteTypeActivity:", "我是关闭事件");
            backgroundAlpha(1f);
        }

    }

    // 性别 窗口实现监听类
    private OnClickListener itemsOnClick = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            switch (arg0.getId()) {
                case R.id.item_popupwindows_Photo:
                    menuWindow.dismiss();
                    // deletePatient();

                    //判断认证 stone
                    DialogUtil.showUserCenterCertifyDialog(PersonDetailActivity.this, new MyCallBack() {
                        @Override
                        public void onClick(Object data) {


                    dialog1 = new android.support.v7.app.AlertDialog.Builder(
                            PersonDetailActivity.this);
                    dialog1.setTitle("是否确定删除好友");
                    dialog1.setMessage("删除该好友后将不会再接收到TA消息和不能给TA发送消息！");

                    dialog1.setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    // TODO Auto-generated method stub
                                    arg0.dismiss();
                                }
                            });

                    dialog1.setPositiveButton("确认",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    // TODO Auto-generated method stub
                                    arg0.dismiss();

                                    if (NetworkUtil
                                            .isNetWorkConnected()) {

                                        new Thread(new Runnable() {
                                            public void run() {

                                                try {
                                                    EMContactManager
                                                            .getInstance()
                                                            .deleteContact(
                                                                    "did_"
                                                                            + json.data.userid);
                                                } catch (HyphenateException e) {
                                                    // TODO Auto-generated catch
                                                    // block


                                                    e.printStackTrace();
                                                }

                                            }
                                        }).start();

                                        deleteFriend();
                                        deletePatient();
                                        YMApplication.isrefresh = true;
                                    } else {
                                        ToastUtils.shortToast(
                                                "网络连接失败");
                                    }

                                }
                            });
                    dialog1.create().show();
                        }
                    }, null, null);
                    break;

                case R.id.share_to_friend_btn:
                    menuWindow.dismiss();
                    //判断认证 stone
                    DialogUtil.showUserCenterCertifyDialog(PersonDetailActivity.this, new MyCallBack() {
                        @Override
                        public void onClick(Object data) {
                            //分享到好友
                            Intent intent = new Intent(PersonDetailActivity.this,
                                    ShareCardActivity.class);
                            intent.putExtra("id", shareId);
                            intent.putExtra("type", "shareNameCard");
                            intent.putExtra("fCardTitle", fCardTitle);
                            intent.putExtra("fCardHospital", fCardHospital);
                            intent.putExtra("fCardDpart", fCardDpart);
                            intent.putExtra("fCardName", fCardName);
                            intent.putExtra("imageUrl", shareImgUrl);
                            startActivity(intent);
                        }
                    }, null, null);

                    break;
            }

        }

    };

    private LinearLayout ll_num;
    private ImageView iv_enter;

    public void deleteFriend() {
        String did = YMApplication.getLoginInfo().getData()
                .getHuanxin_username();
        String bind = did + "did_" + json.data.userid;
        Long st = System.currentTimeMillis();

        String sign = MD5Util.MD5(st + bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();

        params.put("timestamp", st + "");
        params.put("bind", bind);
        params.put("a", "chat");
        params.put("m", "delRelation");
        params.put("friend", "did_" + json.data.userid);
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

    public void deletePatient() {

        String did = YMApplication.getLoginInfo().getData().getPid();
        String bind = did + visitedUserId;
        Long st = System.currentTimeMillis();

        String sign = MD5Util.MD5(bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();

        // params.put("timestamp", st + "");
        params.put("bind", bind);
        params.put("a", "dcFriend");
        params.put("m", "friend_del");
        params.put("userid", did);
        params.put("touserid", visitedUserId);
        params.put("did", did);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        // TODO Auto-generated method stub
                        DLog.i(TAG, "返回数据。。" + t.toString());
                        Map<String, String> map = ResolveJson.R_Action(t.toString());
                        if (map.get("code").equals("0")) {
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putString("isdelete", "true");
                            intent.putExtras(bundle);
                            PersonDetailActivity.this.setResult(RESULT_OK,
                                    intent);

                            Map<String, EaseUser> userlist = YMApplication.getContactList();
                            userlist.remove("did_" + json.data.userid);
                            YMApplication.getInstance().setContactList(userlist);
                            finish();
                            YMApplication.isrefresh = true;
                            ToastUtils.shortToast("删除成功");
                        }
                        // handler.sendEmptyMessage(100);
                        // dialog.channelDialog();
                        super.onSuccess(t);
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        if (pro != null) {
            pro.closeProgersssDialog();
        }
        super.onDestroy();
    }

    private class PdGradViewAdapter extends BaseAdapter {
        private Context mContext;

        public PdGradViewAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return watchedList == null ? 0 : watchedList.size();
        }

        @Override
        public Object getItem(int position) {
            return watchedList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //定义一个ImageView,显示在GridView里
//            ImageView imageView;
            ViewHolder mHolder;
            if (convertView == null) {
                mHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.person_detail_item, null);
                mHolder.imgView = (ImageView) convertView.findViewById(R.id.pd_iv);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
//            watchedList.get(position).photo;
            mImageLoader.displayImage(watchedList.get(position).photo, mHolder.imgView, options);
//            mHolder.imgView.setImageResource();
            return convertView;
        }
    }

    class ViewHolder {
        ImageView imgView;
    }


    public void setGridViewViewHeightBasedOnChildren(GridView gridView) {
        // 获取listview的adapter
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        // 固定列宽，有多少列
        int col = 4;// listView.getNumColumns();
        int totalHeight = 0;
        // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
        // listAdapter.getCount()小于等于8时计算两次高度相加
        for (int i = 0; i < listAdapter.getCount(); i += col) {
            // 获取listview的每一个item
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight();
        }

        // 获取listview的布局参数
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        // 设置高度
        params.height = totalHeight;
        // 设置margin
        ((RelativeLayout.LayoutParams) params).setMargins(10, 10, 10, 10);
        // 设置参数
        gridView.setLayoutParams(params);
    }
}