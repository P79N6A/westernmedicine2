package com.xywy.askforexpert;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.base.view.ProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * 启动页
 *
 * @author LG
 */
public class StartActivity extends Activity {

    public final static String isFistEnter = "isFistEnter";
    protected static final String TAG = "StartActivity";
    private static final int MY_PERMISSIONS_REQUEST = 889;
    private final int SLEEPTIE = 1000;
    private Activity context;
    private SharedPreferences sp1;
    /**
     * 是否自动登陆
     */
    private boolean autoLogin = false;
    /**
     * 跳转类型
     */
    private String type;

    private int typeId = 0;

    private ProgressDialog dialog;

    private boolean isPush;


    private SharedPreferences sp;
    private String fist;
    private SharedPreferences sharedPreferences;
    private SharedPreferences isShowDialog;
    private SharedPreferences isFirstRun;
    private int versionCode = 14;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("storeDeviceID", MODE_PRIVATE);

        isShowDialog = getSharedPreferences("isShowDialog", MODE_PRIVATE);

        isFirstRun = getSharedPreferences("isFirstRun", MODE_PRIVATE);

        // 去除状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            type = getIntent().getStringExtra("entertype");
        } catch (Exception e) {
            e.printStackTrace();
        }
        isPush = getIntent().getBooleanExtra("jpush", false);
        typeId = getIntent().getIntExtra("type", 0);
        this.context = this;

        setContentView(R.layout.activity_start);
        if(YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)){
            findViewById(R.id.rl_root).setBackgroundResource(R.drawable.start_page_yszs);
        }

        sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        fist = sp.getString(isFistEnter, "");
        sp1 = getSharedPreferences("save_user", MODE_PRIVATE);

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // Android 6.0 以上设备
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 适配Android 6.0 应用权限申请
            initPermissions();
        } else {
            getDeviceID();
            init();
        }


    }

    /**
     * 初始化需要的权限
     */
    private void initPermissions() {
        List<String> permissionNeeded = new ArrayList<>();

        final List<String> permissionList = new ArrayList<>();
        if (!addPermission(permissionList, Manifest.permission.CAMERA)) {
            permissionNeeded.add("相机");
        }
        if (!addPermission(permissionList, Manifest.permission.READ_CONTACTS)) {
            permissionNeeded.add("联系人");
        }
        if (!addPermission(permissionList, Manifest.permission.ACCESS_FINE_LOCATION)
                || !addPermission(permissionList, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            permissionNeeded.add("位置");
        }
        if (!addPermission(permissionList, Manifest.permission.RECORD_AUDIO)) {
            permissionNeeded.add("麦克风");
        }
        if (!addPermission(permissionList, Manifest.permission.READ_PHONE_STATE)) {
            permissionNeeded.add("电话");
        }
        if (!addPermission(permissionList, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            permissionNeeded.add("存储卡");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (!addPermission(permissionList, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                if (!permissionNeeded.contains("存储卡")) {
                    permissionNeeded.add("存储卡");
                }
            }
        }

        if (permissionList.size() > 0) {
            if (permissionNeeded.size() > 0) {
                // Need Rationale
                if (isShowDialog.getBoolean("shouldShow" + versionCode, true)) {
                    String message = "授予更多权限，可以帮助您更好的使用和体验"
                            + getResources().getString(R.string.app_name);
                    showMessageOKCancel(message, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            StartActivity.this.startActivityForResult(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.parse("package:" + StartActivity.this.getPackageName())), 111);
                            dialog.dismiss();
                        }
                    });
                } else {
                    init();
                }
                return;
            }
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]),
                    MY_PERMISSIONS_REQUEST);
            return;
        }
        getDeviceID();
        init();
    }

    /**
     * 添加权限
     */
    private boolean addPermission(List<String> permissionList, String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(permission);

            DLog.d(TAG, "isFirst = " + isFirstRun.getBoolean("isFirst", true));
            if (isFirstRun.getBoolean("isFirst", true)) {
                return !ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
            } else {
                return ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
            }
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("去授权", okListener)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isShowDialog.edit().putBoolean("shouldShow" + versionCode, false).apply();
                        init();
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        init();
    }

    /**
     * Handle the permissions request response
     * Android 6.0 以上设备权限处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST:
                // If request is cancelled, the result arrays are empty.
                Map<String, Integer> perms = new HashMap<>();
                // initial
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                }
                for (int i = 0; i < permissions.length; i++) {
                    perms.put(permissions[i], grantResults[i]);
                }
                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // 申请权限被允许
                    DLog.d("FinalHttp", "all permissions granted");
                    getDeviceID();
                    init();
                } else {
                    // Some Permission is Denied
//                    Toast.makeText(this, "Some Permission is Denied", Toast.LENGTH_SHORT).show();
                    if (perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        getDeviceID();
                    }
                    init();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    /**
     * 获取设备号
     */
    private void getDeviceID() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        sharedPreferences.edit().putString("deviceID", deviceId).apply();
    }

    private void init() {
//        new Thread() {
//            public void run() {
//                try {
//                    sleep(SLEEPTIE);
//                    if (isFistEnter.equals(fist)) {
//                        String versonCode = sp.getString("versonCode", "0");
//                        String currentVersonCode = "";
//                        try {
//                            currentVersonCode = AppUtils.getAppVersionCodeStr();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        if (!versonCode.equals(currentVersonCode)) {
//                            gotoGuide();
//                            return;
//                        }
//                       YMUserService.autoLogin(StartActivity.this);
//
//                    } else {
//                        gotoGuide();
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//        }.start();

        if (isFistEnter.equals(fist)) {
            String versonCode = sp.getString("versonCode", "0");
            String currentVersonCode = "";
            try {
                currentVersonCode = AppUtils.getAppVersionCodeStr();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!versonCode.equals(currentVersonCode)) {
                gotoGuide();
                return;
            }
            YMUserService.autoLogin(StartActivity.this);

        } else {
            gotoGuide();
        }
    }

    private void gotoGuide() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intentGuide = new Intent(context, GuideActivity.class);
                startActivity(intentGuide);
                context.finish();
            }
        });

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK;
    }

    @Override
    protected void onPause() {
        isFirstRun.edit().putBoolean("isFirst", false).apply();
        StatisticalTools.onPause(this);
        JPushInterface.onPause(this);
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        StatisticalTools.onResume(this);
        JPushInterface.onResume(this);
    }

    @Override
    protected void onDestroy() {
        if (dialog != null && dialog.isShowing()) {
            dialog.closeProgersssDialog();
        }

        super.onDestroy();
    }
}
