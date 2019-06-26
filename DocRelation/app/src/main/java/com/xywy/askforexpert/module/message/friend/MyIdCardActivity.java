package com.xywy.askforexpert.module.message.friend;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.request.PersonCardRequest;
import com.xywy.base.view.ProgressDialog;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.xywy.uilibrary.titlebar.ItemClickListener;
import com.xywy.util.LogUtils;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.Map;

public class MyIdCardActivity extends YMBaseActivity {

    private ImageView myQR;
    private ImageView myIcon;
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;

    private ProgressDialog progressDialog;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        CommonUtils.initSystemBar(this);
//
//        setContentView(R.layout.activity_my_id_card);
//
//        initView();
//        if(YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)){
//            LogUtils.i("获取用药助手的医生二维码");
//            getErWeiCode();
//        }else {
//            requestData();
//        }
//
//    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_my_id_card;
    }


    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("我的二维码");
        titleBarBuilder.addItem("完成", new ItemClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        }).build();
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        CommonUtils.setToolbar(this, toolbar);

        myQR = (ImageView) findViewById(R.id.qr_code);
        myIcon = (ImageView) findViewById(R.id.user_icon);

        mImageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .showImageOnFail(R.drawable.icon_photo_def)
                .showImageOnLoading(R.drawable.icon_photo_def)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    protected void initData() {
        if(YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)){
            LogUtils.i("获取用药助手的医生二维码");
            getErWeiCode();
        }else {
            requestData();
        }
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
                            mImageLoader.displayImage(entry.getData().toString(),myQR, options);
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

    private void requestData() {
        if (YMApplication.getLoginInfo().getData().getPhoto() != null
                && !YMApplication.getLoginInfo().getData().getPhoto().equals("")) {
            mImageLoader.displayImage(YMApplication.getLoginInfo().getData().getPhoto(), myIcon, options);
        }

        String did = YMApplication.getLoginInfo().getData().getPid();
        Long st = System.currentTimeMillis();

        String sign = MD5Util.MD5(st + did + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();

        params.put("timestamp", st + "");
        params.put("bind", did);
        params.put("a", "chat");
        params.put("m", "getqrcode");
        params.put("did", did);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.get(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        Map<String, String> map = ResolveJson.R_Action_two(t.toString());
                        if (map != null && map.get("code") != null) {
                            if (map.get("code").equals("0")) {
                                mImageLoader.displayImage(map.get("data"), myQR, options);
                            }
                        }
                        super.onSuccess(t);
                    }
                });
    }

//    private void initView() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        CommonUtils.setToolbar(this, toolbar);
//
//        myQR = (ImageView) findViewById(R.id.qr_code);
//        myIcon = (ImageView) findViewById(R.id.user_icon);
//
//        mImageLoader = ImageLoader.getInstance();
//        options = new DisplayImageOptions.Builder()
//                .cacheOnDisk(true)
//                .cacheInMemory(true)
//                .showImageOnFail(R.drawable.icon_photo_def)
//                .showImageOnLoading(R.drawable.icon_photo_def)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .build();
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.my_id_card_menu, menu);
//
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//            case R.id.my_id_card_complete:
//                this.finish();
//                break;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    /**
     * 显示进度dialog
     *
     * @param content 提示文字内容
     */
    public void showProgressDialog(String content) {
        if (null == progressDialog) {
            progressDialog = new ProgressDialog(MyIdCardActivity.this, content);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
