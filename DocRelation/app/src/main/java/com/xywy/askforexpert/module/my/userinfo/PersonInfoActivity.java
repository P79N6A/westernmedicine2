package com.xywy.askforexpert.module.my.userinfo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.util.PathUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.MainActivity;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.medicine.SyncInfoRequest;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DensityUtils;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.ScreenUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.LoginInfo_New;
import com.xywy.askforexpert.module.consult.activity.AddOrEditTextInfoActivity;
import com.xywy.askforexpert.module.discovery.medicine.SellDrugRequest;
import com.xywy.askforexpert.module.discovery.medicine.common.Callback;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.request.PersonCardRequest;
import com.xywy.askforexpert.widget.view.RoundAngleImageView;
import com.xywy.retrofit.RetrofitClient;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.xywy.util.LogUtils;
import com.xywy.util.T;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.io.File;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;

import retrofit2.adapter.rxjava.HttpException;

/**
 * 个人信息 stone
 */
public class PersonInfoActivity extends YMBaseActivity {

    private Callback mRefreshUserInfoCallback;

    private static final String TAG = "PersonInfoActivity";
//    private String str, type;

    //    private LinearLayout main;
    private RoundAngleImageView iv_head;
    private TextView tv_name, tv_sex, tv_area, tv_hospital, tv_department, tv_jobtitle, tv_goodat, tv_desc;

    /**
     * 图片地址
     */
    private File origUri;
//    private SelectPicPopupWindow menuWindow;
//    private final static String FILE_SAVEPATH = Environment
//            .getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera";
    /**
     * 本地图片选取标志
     */
    private static final int FLAG_CHOOSE_IMG = 0x11;
    /**
     * 截取结束标志
     */
    private static final int FLAG_MODIFY_FINISH = 7;
    /**
     * 相机标志
     */
    private static final int FLAG_CHOOSE_CAMERA = 0x17;

//    public static String jobtypid;

//    private NumberPickerPopupwinow numPickPop;
//    private SexNumberPickerPopup sexNumPicPop;

//    private static final int perinfo = 0x123;

    //    private Map<String, String> map;
    private FinalBitmap fb;

    private String intent_type;

    private LoginInfo_New mInfoNew = YMApplication.getUserInfo();


    @Override
    protected int getLayoutResId() {
        return R.layout.periformation;
    }

    @Override
    protected void beforeViewBind() {
        //stone 共享文件login中保存mustUpdata(必须更新),isFistEnter(第一次进入),versonCode(版本号),"oldid" + uid,"noldid" + uid...
        SharedPreferences sp = getSharedPreferences("login",
                Context.MODE_PRIVATE);
        sp.edit().putString("mustUpdata", "").apply();

        intent_type = getIntent().getStringExtra("type");

    }

    @Override
    public void initView() {
        hideCommonBaseTitle();
        ((TextView) findViewById(R.id.tv_title)).setText("个人信息");

        ScreenUtils.initScreen(this);

        fb = FinalBitmap.create(PersonInfoActivity.this, true);

//        main = (LinearLayout) findViewById(R.id.main);
        iv_head = (RoundAngleImageView) findViewById(R.id.iv_head);// 头像
        tv_name = (TextView) findViewById(R.id.tv_name);// 姓名
        tv_sex = (TextView) findViewById(R.id.tv_sex);// 性别
        tv_area = (TextView) findViewById(R.id.tv_area);//地区
        tv_hospital = (TextView) findViewById(R.id.tv_hospital);//医院
        tv_department = (TextView) findViewById(R.id.tv_department);//科室
        tv_jobtitle = (TextView) findViewById(R.id.tv_jobtitle);//职称
        tv_goodat = (TextView) findViewById(R.id.tv_goodat);//擅长疾病
        tv_desc = (TextView) findViewById(R.id.tv_desc);// 个人简介


        mRefreshUserInfoCallback = new Callback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onProgress(int progress) {

            }

            @Override
            public void onSuccess(Object data) {
                mInfoNew = (LoginInfo_New) data;
                if (mInfoNew != null && mInfoNew.details != null) {
                    putData();
                }
            }

            @Override
            public void onFail(Throwable e) {

            }
        };
    }

    @Override
    protected void initData() {

        //获取现有数据
        if (mInfoNew != null && mInfoNew.details != null) {
            putData();
        } else if (NetworkUtil.isNetWorkConnected()) {
            // 获取用户信息 stone
            YMUserService.refreshUserInfo(this, mRefreshUserInfoCallback);
        } else {
            ToastUtils.shortToast("网络连接失败");
        }

//        //请求网络最新数据
//        if (NetworkUtil.isNetWorkConnected()) {
//            getData();
//        } else {
//            ToastUtils.shortToast("网络连接失败");
//        }

    }

    /**
     * 获取并填充用户信息到view stone
     */
    private void putData() {

        tv_name.setText(mInfoNew.real_name);
        fb.display(iv_head, mInfoNew.photo);
        fb.configLoadfailImage(R.drawable.icon_photo_def);
        fb.configLoadingImage(R.drawable.icon_photo_def);

        if (TextUtils.isEmpty(mInfoNew.photo)) {
            iv_head.setBackgroundResource(R.drawable.icon_photo_def);
        }

        tv_sex.setText(mInfoNew.details.gender);
        tv_jobtitle.setText(mInfoNew.details.clinic);
        tv_hospital.setText(mInfoNew.details.hos_name);
        if (!TextUtils.isEmpty(mInfoNew.details.province)) {
            tv_area.setText(mInfoNew.details.province + Constants.DIVIDERS + mInfoNew.details.city);
        }
        //认证前后 科室的字段不同
        if (!TextUtils.isEmpty(mInfoNew.details.subject_first)) {
            tv_department.setText(mInfoNew.details.subject_first + Constants.DIVIDERS + mInfoNew.details.subject_second);
        }

        tv_goodat.setText(mInfoNew.be_good_at);
        tv_desc.setText(mInfoNew.introduce);

        //照片命名
        origUri = new File(PathUtil.getInstance().getImagePath(), "osc_"
                + System.currentTimeMillis() + ".jpg");
        if (origUri.getParentFile() != null) {
            origUri.getParentFile().mkdirs();
        }
    }


//    @Override
//    protected void handleMsg(Message msg) {
//        super.handleMsg(msg);
//        map = (HashMap<String, String>) msg.obj;
//        switch (msg.what) {
//            case 200:
//                //stone 请求完毕,刷新页面
//                putData();
//                break;
//            case 100:
//                if (map.get("code").equals("0")) {
//                    YMUserService.getPerInfo().getData().setBirth_day(
//                            numPickPop.getDate());
////                    tv_birthday.setText(numPickPop.getData2());
//                    ToastUtils.shortToast(map.get("msg"));
//                }
//                break;
//            case 300:
//                if (map.get("code").equals("0")) {
//                    YMUserService.getPerInfo().getData().setSex(
//                            sexNumPicPop.getID() + "");
//                    tv_sex.setText(sexNumPicPop.getData());
//                    ToastUtils.shortToast(map.get("msg"));
//                }
//                break;
//            case 400:
//                if (map.get("code").equals("0")) {
//                    fb.display(iv_head, YMUserService.getPerInfo().getData()
//                                    .getPhoto(), DensityUtils.dp2px(56),
//                            DensityUtils.dp2px(56));
//                }
//                break;
//
//            default:
//                break;
//        }
//    }

    //点击事件
    public void onClick_back(View view) {
//        Intent intent;
        switch (view.getId()) {
            case R.id.btn1:
                if (TextUtils.isEmpty(intent_type)) {
                    finish();
                } else if ("login".equals(intent_type)) {
                    startActivity(new Intent(PersonInfoActivity.this,
                            MainActivity.class).putExtra("tag", 1));
                    finish();
                }
                break;
//            case R.id.rl_head:
            // 设置layout在PopupWindow中显示的位置
//                if (YMApplication.isPerCheckInfo() == 0) {
//                    menuWindow = new SelectPicPopupWindow(PersonInfoActivity.this, itemsOnClick);
//                    backgroundAlpha(0.5f);
//                    // 显示窗口
//                    menuWindow.showAtLocation(main, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//                    menuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                        @Override
//                        public void onDismiss() {
//                            backgroundAlpha(1f);
//                        }
//                    });
//                } else if (YMApplication.isPerCheckInfo() == 1) {
//                    Dialog(PersonInfoActivity.this, "认证中，此项不可修改！");
//                } else if (YMApplication.isPerCheckInfo() == 2) {
//                    Dialog(PersonInfoActivity.this, "您已认证，此项不可修改！");
//                }
//                break;

            case R.id.rl_code:
                //判断认证 stone
                DialogUtil.showUserCenterCertifyDialog(PersonInfoActivity.this, new MyCallBack() {
                    @Override
                    public void onClick(Object data) {
                        //有数据
                        if (mInfoNew != null && mInfoNew.details != null) {
                            handleCodeClick();
                        } else if (NetworkUtil.isNetWorkConnected()) {
                            // 获取用户信息 stone
                            YMUserService.refreshUserInfo(PersonInfoActivity.this, mRefreshUserInfoCallback);
                        } else {
                            ToastUtils.shortToast("网络连接失败");
                        }
                    }
                }, null, null);


                break;
            case R.id.rl_goodat:
                // 擅长疾病
                if (!TextUtils.isEmpty(tv_goodat.getText().toString().trim())) {
                    AddOrEditTextInfoActivity.startGoodAtActivityForResult(this, tv_goodat.getText().toString().trim(), false);
                }
                break;
            case R.id.rl_desc:
                // 个人简介
                if (!TextUtils.isEmpty(tv_desc.getText().toString().trim())) {
                    AddOrEditTextInfoActivity.startPersonalDescActivityForResult(this, tv_desc.getText().toString().trim(), false);
                }
                break;

            default:
                break;
        }
    }

    //处理二维码点击 stone
    private void handleCodeClick() {
        if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
            //用药助手逻辑
            if (YMApplication.getInstance().getHasSyncInfo()) {
                //如果已经同步过医生信息
                if (YMApplication.getInstance().getSyncInfoResult()) {
                    //医生信息同步成功
                    if (YMApplication.getInstance().getHasSellDrug()) {
                        //已经检查过医生是否具有售药的权限
                        if (YMApplication.getInstance().getSellDrugResult()) {
                            //当前医生具有售药的权限
                            LogUtils.i("获取二维码");
                            getErWeiCode();
                        } else {
                            ToastUtils.shortToast(YMApplication.getInstance().getSellDrugMsg());
                        }
                    } else {
                        //还未检查医生是否具有售药的权限
                        sellDrug();
                    }
                } else {
                    //医生信息同步失败
                    com.xywy.askforexpert.module.discovery.medicine.util.ToastUtils.shortToast(YMApplication.getInstance().getSyncInfoMsg());
                }
            } else {
                //没有同步过医生信息,则同步医生信息
                syncInfo();
            }
        } else {
            //医脉逻辑
            if (NetworkUtil.isNetWorkConnected()) {
                Dialogtwo(PersonInfoActivity.this, YMApplication.getUserInfo().real_name, YMApplication.getUserInfo().photo, YMApplication.getUserInfo().hos_name);
            } else {
                ToastUtils.shortToast("网络连接失败");
            }
        }
    }

    /**
     * 同步医生信息
     */
    public void syncInfo() {
        long doctroId = Long.parseLong(YMUserService.getCurUserId());
        SyncInfoRequest.getInstance().syncInfo(doctroId).subscribe(new BaseRetrofitResponse<BaseData>() {
            @Override
            public void onStart() {
                super.onStart();
                showProgressDialog("");
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                hideProgressDialog();
                if (e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof HttpException) {
                    //未同步医生信息接口
                    YMApplication.getInstance().setHasSyncInfo(false);
                    Toast.makeText(RetrofitClient.getContext(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else {
                    //已经同步过医生信息接口
                    YMApplication.getInstance().setHasSyncInfo(true);
                    //同步过医生信息接口的返回结果是：同步医生信息失败
                    YMApplication.getInstance().setSyncInfoResult(false, e.getMessage());
                    T.showShort(RetrofitClient.getContext(), e.getMessage());
                }
            }

            @Override
            public void onNext(BaseData entry) {
                super.onNext(entry);
                if (entry != null) {
                    dealwithEntry(entry);
                }
            }
        });
    }

    private void dealwithEntry(BaseData entry) {
        if (null != entry && entry.getCode() == 10000) {
            //已经同步过医生信息接口
            YMApplication.getInstance().setHasSyncInfo(true);
            //同步过医生信息接口的返回结果是：同步医生信息成功
            YMApplication.getInstance().setSyncInfoResult(true, "");
            sellDrug();
        }
    }

    private void sellDrug() {
        SellDrugRequest.getInstance().getSellDrug(Integer.parseInt(YMUserService.getCurUserId())).subscribe(new BaseRetrofitResponse<BaseData>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                hideProgressDialog();

            }

            @Override
            public void onError(Throwable e) {
                hideProgressDialog();
                if (e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof HttpException) {
                    //还未调用检测是否具有售药权限的接口
                    YMApplication.getInstance().setHasSellDrug(false);
                    Toast.makeText(RetrofitClient.getContext(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else {
                    //已经调用了检测是否具有售药权限的接口
                    YMApplication.getInstance().setHasSellDrug(true);
                    //检测是否具有售药权限的接口返回的结果是：没有售药权限
                    YMApplication.getInstance().setSellDrugResult(false, e.getMessage());
                    T.showShort(RetrofitClient.getContext(), e.getMessage());
                }
            }

            @Override
            public void onNext(BaseData entry) {
                super.onNext(entry);
                if (entry != null) {
                    //已经调用了检测是否具有售药权限的接口
                    YMApplication.getInstance().setHasSellDrug(true);
                    //检测是否具有售药权限的接口返回的结果是：具有售药权限
                    YMApplication.getInstance().setSellDrugResult(true, "");
                    LogUtils.i("获取二维码");
                    getErWeiCode();
                }
            }
        });
    }


    private void getErWeiCode() {
        PersonCardRequest.getInstance().getDoctorEWM(YMUserService.getCurUserId()).subscribe(new BaseRetrofitResponse<BaseData>() {
            @Override
            public void onStart() {
                super.onStart();
                showProgressDialog("loading");
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

            }

            @Override
            public void onNext(BaseData entry) {
                super.onNext(entry);
                if (entry != null) {
                    if (entry.getCode() == 10000) {
                        if (entry.getData() != null) {
                            if (NetworkUtil.isNetWorkConnected()) {
                                Dialogtwo(PersonInfoActivity.this, YMApplication.getUserInfo().real_name, YMApplication.getUserInfo().photo, YMApplication.getUserInfo().hos_name);
                            } else {
                                ToastUtils.shortToast("网络连接失败");
                            }
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

//    /**
//     * 设置添加屏幕的背景透明度
//     *
//     * @param bgAlpha
//     */
//    public void backgroundAlpha(float bgAlpha) {
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.alpha = bgAlpha; // 0.0-1.0
//        getWindow().setAttributes(lp);
//    }

    // 性别 窗口实现监听类
//    private OnClickListener saveOnclik_sex = new OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.btn_save:
//                    UpdatePersonInfo.update("sex", sexNumPicPop.getID() + "", 300,
//                            uiHandler);
//                    sexNumPicPop.dismiss();
//                    break;
//
//                default:
//                    break;
//            }
//
//        }
//
//    };
    // 生日 弹窗实现监听类
//    private OnClickListener saveOnclik = new OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.btn_save:
//                    UpdatePersonInfo.update("birth_day", numPickPop.getData2(),
//                            100, uiHandler);
//                    numPickPop.dismiss();
//                    break;
//
//                default:
//                    break;
//            }
//
//        }
//
//    };
    // 头像修改 窗口实现监听类
//    private OnClickListener itemsOnClick = new OnClickListener() {
//        Intent intent;
//
//        public void onClick(View v) {
//            menuWindow.dismiss();
//            switch (v.getId()) {
//                case R.id.item_popupwindows_camera:
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if (ContextCompat.checkSelfPermission(PersonInfoActivity.this,
//                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                            CommonUtils.permissionRequestDialog(PersonInfoActivity.this, "无法打开相机，请授予照相机(Camera)权限", 123);
//                        } else {
//                            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(origUri));
//                            startActivityForResult(intent, FLAG_CHOOSE_CAMERA);
//                        }
//                    } else {
//                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(origUri));
//                        startActivityForResult(intent, FLAG_CHOOSE_CAMERA);
//                    }
//                    // startActivity(intent);
//                    break;
//                case R.id.item_popupwindows_Photo:
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if (ContextCompat.checkSelfPermission(PersonInfoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                                != PackageManager.PERMISSION_GRANTED) {
//                            CommonUtils.permissionRequestDialog(PersonInfoActivity.this, "无法打开相册，请授予内存(Storage)权限", 123);
//                        } else {
//                            intent = new Intent(PersonInfoActivity.this, PhotoWallActivity.class);
//                            startActivity(intent);
//                            YMApplication.photoTag = "perinfo";
//                            YMApplication.isSelectMore = false;
//                        }
//                    } else {
//                        intent = new Intent(PersonInfoActivity.this, PhotoWallActivity.class);
//                        startActivity(intent);
//                        YMApplication.photoTag = "perinfo";
//                        YMApplication.isSelectMore = false;
//                    }
//                    break;
//                case R.id.item_popupwindows_cancel:
//                    menuWindow.dismiss();
//
//                    break;
//                default:
//                    break;
//            }
//
//        }
//
//    };

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == FLAG_CHOOSE_IMG && resultCode == RESULT_OK) {
//            if (data != null) {
//                Uri uri = data.getData();
//                if (!TextUtils.isEmpty(uri.getAuthority())) {
//                    Cursor cursor = getContentResolver().query(uri,
//                            new String[]{MediaStore.Images.Media.DATA},
//                            null, null, null);
//                    if (null == cursor) {
//                        Toast.makeText(getApplicationContext(), "图片没找到", Toast.LENGTH_SHORT)
//                                .show();
//                        return;
//                    }
//                    cursor.moveToFirst();
//                    String path = cursor.getString(cursor
//                            .getColumnIndex(MediaStore.Images.Media.DATA));
//                    cursor.close();
//
//                    Intent intent = new Intent(this, ClipPictureActivity.class);
//                    intent.putExtra("path", path);
//                    startActivityForResult(intent, FLAG_MODIFY_FINISH);
//                } else {
//                    Intent intent = new Intent(this, ClipPictureActivity.class);
//                    intent.putExtra("path", uri.getPath());
//                    startActivityForResult(intent, FLAG_MODIFY_FINISH);
//                }
//            }
//        } else if (requestCode == FLAG_CHOOSE_CAMERA && resultCode == RESULT_OK) {
//            Intent intent = new Intent(this, ClipPictureActivity.class);
//            intent.putExtra("path", origUri.getPath());
//            startActivityForResult(intent, FLAG_MODIFY_FINISH);
//        } else if (requestCode == FLAG_MODIFY_FINISH && resultCode == RESULT_OK) {
//            if (data != null) {
//                final String path = data.getStringExtra("path");
//                // Bitmap b = BitmapFactory.decodeFile(path);
//                // headimgs.setImageBitmap(b);
//                if (path != null && !path.equals("")) {
//                    YMUserService.getPerInfo().getData().setPhoto(path);
//                    YMApplication.getLoginInfo().getData().setPhoto(path);
//                    UpdatePersonInfo.update("photo", path, 400, uiHandler);
//
//                }
//            }
//        } else if (requestCode == perinfo && resultCode == RESULT_OK) {
//            ToastUtils.shortToast("显示");
//            putData();
//        }
//    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//
//        setIntent(intent);
//        DLog.i("300", "onNewIntent" + intent.getIntExtra("code", -1));
//        int code = intent.getIntExtra("code", -1);
//        if (code != 100) {
//            return;
//        }
//        ArrayList<String> paths = intent.getStringArrayListExtra("paths");
//        if (paths != null && paths.size() > 0) {
//            Intent intent2 = new Intent(PersonInfoActivity.this,
//                    ClipPictureActivity.class);
//            intent2.putExtra("path", paths.get(0));
//            startActivityForResult(intent2, FLAG_MODIFY_FINISH);
//        } else {
//            ToastUtils.shortToast("图片选取失败");
//        }
//
//    }


//    /**
//     * 获取个人信息 stone
//     */
//    public void getData() {
//        final ProgressDialog dialog = new ProgressDialog(this, "正在加载中...");
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.showProgersssDialog();
//        String status = "get";
//        String userid = YMApplication.getLoginInfo().getData().getPid();
//        String sign = MD5Util.MD5(userid + status + Constants.MD5_KEY);
//        AjaxParams params = new AjaxParams();
//        params.put("command", "info");
//        params.put("status", status);
//        params.put("userid", userid);
//        params.put("sign", sign);
//
//        FinalHttp fh = new FinalHttp();
//        fh.post(CommonUrl.DP_COMMON, params, new AjaxCallBack() {
//            @Override
//            public void onLoading(long count, long current) {
//                // TODO Auto-generated method stub
//                super.onLoading(count, current);
//            }
//
//            @Override
//            public void onSuccess(String t) {
//                DLog.d("info_follow", t.toString());
//                dialog.dismiss();
//                //保存用户信息 stone
//                YMUserService.setPerInfo(ResolveJson.R_personinfo(t.toString()));
//                if (YMUserService.getPerInfo().getCode().equals("0")) {
//                    uiHandler.sendEmptyMessage(200);
//                }
//
//                super.onSuccess(t);
//            }
//
//            @Override
//            public void onFailure(Throwable t, int errorNo, String strMsg) {
//                DLog.i(TAG, "错误，" + strMsg);
//                dialog.dismiss();
//                super.onFailure(t, errorNo, strMsg);
//            }
//
//        });
//    }

    @Override
    protected void onResume() {
        super.onResume();
        StatisticalTools.onResume(this);
    }

    public void onPause() {
        StatisticalTools.onPause(this);
        super.onPause();

    }

    public void Dialog(Context context, String str) {
        DialogUtil.NewDialog(new YMOtherUtils(context).context, str);

    }

    /**
     * 弹出框 包含医生二维码 二维码是实时获取的 stone
     *
     * @param context
     * @param str      姓名
     * @param img      头像
     * @param str_area 地区
     */
    public void Dialogtwo(Context context, String str, String img,
                          String str_area) {

        final ImageLoader mImageLoader = ImageLoader.getInstance();
        final DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .showImageOnFail(R.drawable.icon_photo_def)
                .showImageOnLoading(R.drawable.icon_photo_def)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        LinearLayout layout = (LinearLayout) LayoutInflater.from(context)
                .inflate(R.layout.img_two, null);
        final Dialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        TextView name = (TextView) layout.findViewById(R.id.tv_name);
        RoundAngleImageView head_two = (RoundAngleImageView) layout
                .findViewById(R.id.headimg_two);
        ImageView userIcon = (ImageView) layout.findViewById(R.id.user_icon);
        final ImageView img_wxing = (ImageView) layout
                .findViewById(R.id.img_wxing);
        TextView area = (TextView) layout.findViewById(R.id.tv_area);

        name.setText(str);
        area.setText(str_area);
        fb.display(head_two, img, DensityUtils.dp2px(56), DensityUtils.dp2px(56));
        if (img != null && !"".equals(img)) {
            mImageLoader.displayImage(img, userIcon, options);
        }
        img_wxing.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        //TODO 获取医生二维码 stone 此处需要做修改适配用药助手
        if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
            mImageLoader.displayImage(img,
                    img_wxing, options);
        } else {
            String did = YMApplication.getLoginInfo().getData().getPid();
            String bind = did;
            Long st = System.currentTimeMillis();

            String sign = MD5Util.MD5(st + bind + Constants.MD5_KEY);
            AjaxParams params = new AjaxParams();

            params.put("timestamp", st + "");
            params.put("bind", bind);
            params.put("a", "chat");
            params.put("m", "getqrcode");
            params.put("did", did);
            params.put("sign", sign);

            FinalHttp fh = new FinalHttp();
            fh.get(CommonUrl.Patient_Manager_Url, params,
                    new AjaxCallBack() {
                        @Override
                        public void onSuccess(String t) {
                            Gson gson = new Gson();
                            DLog.i(TAG, "返回数据。。" + t.toString());
                            Map<String, String> map = ResolveJson
                                    .R_Action_two(t.toString());
                            if (map != null) {
                                if (map.get("code").equals("0")) {
//                                fb.display(img_wxing, map.get("data"));
                                    if (map.get("data") != null && !map.get("data").equals("")) {
                                        mImageLoader.displayImage(map.get("data"),
                                                img_wxing, options);
                                    }
                                }
                            }
                            super.onSuccess(t);
                        }
                    });
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (TextUtils.isEmpty(intent_type)) {
                finish();
            } else if ("login".equals(intent_type)) {
                startActivity(new Intent(PersonInfoActivity.this,
                        MainActivity.class).putExtra("tag", 1));
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
