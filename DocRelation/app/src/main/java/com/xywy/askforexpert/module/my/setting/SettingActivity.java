package com.xywy.askforexpert.module.my.setting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.fb.FeedbackAgent;
import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.model.UpDataInfo;
import com.xywy.askforexpert.module.discovery.medicine.common.dialog.MsDialogBuilder;
import com.xywy.askforexpert.module.discovery.medicine.module.appupdate.UpdateAppRequest;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity.AppVersion;
import com.xywy.askforexpert.module.discovery.medicine.util.ToastUtils;
import com.xywy.askforexpert.module.my.setting.utils.VersionUtils;
import com.xywy.askforexpert.module.my.userinfo.IDAndSafeActivity;
import com.xywy.base.view.ProgressDialog;
import com.xywy.datarequestlibrary.XywyDataRequestApi;
import com.xywy.datarequestlibrary.downloadandupload.listener.OkDownloadListener;
import com.xywy.retrofit.RetrofitClient;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.xywy.uilibrary.dialog.pndialog.listener.PositiveDialogListener;
import com.xywy.util.FilePathUtil;
import com.xywy.util.LogUtils;
import com.xywy.util.T;

import java.io.File;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import cn.org.bjca.sdk.core.kit.BJCASDK;
import retrofit2.adapter.rxjava.HttpException;

import static com.xywy.askforexpert.appcommon.utils.update.SafetyUtils.getFileMD5;
import static com.xywy.askforexpert.module.consult.ConsultConstants.LOG_OUT_FALG;

/**
 * 我的设置界面
 *
 * @author shihao
 * @2015-4-23下午2:57:12
 */
public class SettingActivity extends YMBaseActivity{
    private UpDataInfo mDataInfo;// 版本更新infos
    private ProgressDialog pro;

    private ProgressDialog logoutProgressDialog;
    private SharedPreferences sp;

    private TextView tvVersion;

    //用药助手
    private String mDownLoadUrl,mAppSecretKey;
    private String mAppVersionName;
    private static final String IS_UPGRADE = "1";//是否升级 1.是 0.否
    private boolean mHasCheckedAppversion;
    private AppVersion mAppVersion;
    private ProgressDialog progressDialog;
    private static final String APP_VERSION_MSG = "系统被检测到当前有最新的版本，是否更新?";
    private String mErrorMessage;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        CommonUtils.initSystemBar(this);
//
//        setContentView(R.layout.activity_setting);
//        pro = new ProgressDialog(this,
//                "正在加载，请稍后...");
//        sp = getSharedPreferences("save_user", MODE_PRIVATE);
//        tvVersion = (TextView) findViewById(R.id.tv_app_version);
//        tvVersion.setText("当前版本" + AppUtils.getAppVersionName(this));
//        mAppVersionName = AppUtils.getVersionName(this);
//    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("设置");
        pro = new ProgressDialog(this,
                "正在加载，请稍后...");
        sp = getSharedPreferences("save_user", MODE_PRIVATE);
        tvVersion = (TextView) findViewById(R.id.tv_app_version);
        tvVersion.setText("当前版本" + AppUtils.getAppVersionName(this));
        mAppVersionName = AppUtils.getVersionName(this);
    }

    @Override
    protected void initData() {
    }

    @SuppressLint("SdCardPath")
    public void onClickListener(View v) {
        switch (v.getId()) {
//            case R.id.btn_setting_back:
//                finish();
//                break;

            case R.id.rl_account_and_security:
                Intent intent = new Intent(SettingActivity.this,
                        IDAndSafeActivity.class);
                startActivity(intent);
                break;

            case R.id.rl_message_set:
                Intent msgIntent = new Intent(SettingActivity.this,
                        MsgManagerActivity.class);
                startActivity(msgIntent);
                break;

            case R.id.rl_check_update:// 检查版本更新
                if(YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)){
                    //版本检测与升级
                    if(mHasCheckedAppversion){//已经做过了版本接口的请求
                        //已经做过了版本检测了，根据接口返回的结果，来决定是否升级
                        if(null != mAppVersion){
                            if (VersionUtils.getInstance(this).isDownload||XywyDataRequestApi.getInstance().isDownLoad){
                                ToastUtils.longToast("用户正在下载");
                            }else{
                                dealwithEntry(mAppVersion);
                            }

                        }else {
                            T.showShort(RetrofitClient.getContext(),mErrorMessage);
                        }

                    }else {//如果未进行版本接口的请求，则需要进行版本接口的请求
                        if (VersionUtils.getInstance(this).isDownload||XywyDataRequestApi.getInstance().isDownLoad){
                            ToastUtils.longToast("用户正在下载");
                        }else{
                            checkAppVersion();
                        }
                    }
                }else {
                    if (VersionUtils.getInstance(this).isDownload||XywyDataRequestApi.getInstance().isDownLoad){
                        ToastUtils.longToast("用户正在下载");
                    }else{
                        VersionUtils.getInstance(this).checkVersionUpdate();
                    }

                }
                break;

            case R.id.rl_clear_cache:

                clearCache();
                break;

            case R.id.rl_help_and_feedback:
                StatisticalTools.eventCount(this, "feedback");
                FeedbackAgent agent = new FeedbackAgent(SettingActivity.this);
                agent.startFeedbackActivity();
                break;

            case R.id.btn_log_out:
                StatisticalTools.eventCount(this, "exit");
                SharedPreferences sp = getSharedPreferences("saveChannel", Context.MODE_PRIVATE);
                sp.edit().putBoolean("islogout", true).apply();
                BJCASDK.getInstance().clearCert(this);
                BJCASDK.getInstance().clearPin(this);
                try {
                    LOG_OUT_FALG = true;
                    YMUserService.ymLogout();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.rl_about:
                Intent aboutIntent = new Intent(SettingActivity.this,
                        AboutActivity.class);
                startActivity(aboutIntent);
                break;
        }

    }

    private void checkAppVersion() {
        UpdateAppRequest.getInstance().checkVersion(mAppVersionName).subscribe(new BaseRetrofitResponse<BaseData<AppVersion>>() {
            @Override
            public void onStart() {
                super.onStart();
                showProgressDialog("");

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
                    Toast.makeText(RetrofitClient.getContext(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                    //已经进行过版本检测
                    mHasCheckedAppversion = false;
                } else {
                    //已经进行过版本检测
                    mHasCheckedAppversion = true;
                    mErrorMessage = "异常提示:" + e.getMessage();
                    Toast.makeText(RetrofitClient.getContext(), "异常提示:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNext(BaseData<AppVersion> entry) {
                super.onNext(entry);
                mHasCheckedAppversion = true;
                if (entry != null && entry.getData() != null) {
                    dealwithEntry(entry.getData());
                }
            }
        });

    }

    private void dealwithEntry(AppVersion entry) {
        LogUtils.i("url=" + entry.getUrl());
        LogUtils.i("md5=" + entry.getApp_secret_key());
        String is_upgrade = entry.getIs_upgrade();
        mAppVersion = entry;
        if (!TextUtils.isEmpty(is_upgrade) && is_upgrade.equals(IS_UPGRADE)) {
            mDownLoadUrl = entry.getUrl();
            mAppSecretKey = entry.getApp_secret_key();
            new MsDialogBuilder()
                    .setContent(APP_VERSION_MSG)
                    .create(this, new PositiveDialogListener() {
                        @Override
                        public void onPositive() {
                            shortToast("安装新版本");
                            if (!TextUtils.isEmpty(mDownLoadUrl)) {
                                downLoadNewInstallPackage(mDownLoadUrl);
                            }
                        }
                    }).show();
        } else {
            shortToast("目前版本已是最新版本");
        }

    }

    private void downLoadNewInstallPackage(String downloadFileUrl) {

        XywyDataRequestApi.getInstance().downloadFile(downloadFileUrl, FilePathUtil.getSdcardPath(), new OkDownloadListener() {
            @Override
            public void onProgress(long totalBytes, long currentBytes) {
                if (0 != totalBytes) {
                    shortToast("下载进度：" + currentBytes * 100 / totalBytes + "%");
//                    LogUtils.i("下载进度："+currentBytes*100/totalBytes+"%");
                }
            }

            @Override
            public void onSuccess(final String absoluteFilePath) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        shortToast("下载成功：" + absoluteFilePath);
                        String md5 = getFileMD5(new File(absoluteFilePath));
                        LogUtils.i("安装包的md5===="+md5);
                        if(null !=mAppSecretKey && mAppSecretKey.equals(md5)){
                            installApk(absoluteFilePath);
                        }
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                shortToast("下载异常：" + e.getMessage());
            }
        });
    }

    /**
     * 安装apk
     *
     * @param path
     */
    private void installApk(String path) {
        File apkfile = new File(path);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri mImageUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果是7.0android系统
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            mImageUri = VersionUtils.getFileExternalContentUri(this,apkfile);
        }else{
            mImageUri = Uri.parse("file://" + apkfile.toString());
        }
        i.setDataAndType(mImageUri,
                "application/vnd.android.package-archive");
//        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
//                "application/vnd.android.package-archive");
        startActivity(i);
    }

    /**
     * 显示进度dialog
     *
     * @param content 提示文字内容
     */
    public void showProgressDialog(String content) {
        if (null == progressDialog) {
            progressDialog = new ProgressDialog(SettingActivity.this, content);
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

    /**
     * 清理缓存
     */
    private void clearCache() {
        // 位置 内部的cache路径和外部的xywy

        StringBuffer filePath = new StringBuffer();
        if (AppUtils.isExistSDCard()) {
            filePath.append(Environment.getExternalStorageDirectory()
                    .getAbsolutePath());

            filePath.append(File.separator).append(CommonUrl.DIRE);
            AppUtils.delFolder(YMApplication.getAppContext(),
                    filePath.toString());

        }

        boolean flag = AppUtils.delFolder(YMApplication.getAppContext(),
                YMApplication.getAppContext().getCacheDir()
                        .getAbsolutePath().toString());

        if (flag) {
            Toast.makeText(this, "缓存清理成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "缓存清理失败，请稍候再试", Toast.LENGTH_SHORT).show();
        }

    }

    public void onResume() {
        super.onResume();
        StatisticalTools.onResume(this);
    }

    public void onPause() {

        StatisticalTools.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (pro != null && pro.isShowing()) {
            pro.closeProgersssDialog();
        }

        if (logoutProgressDialog != null && logoutProgressDialog.isShowing()) {
            logoutProgressDialog.closeProgersssDialog();
        }
        super.onDestroy();
    }
}
