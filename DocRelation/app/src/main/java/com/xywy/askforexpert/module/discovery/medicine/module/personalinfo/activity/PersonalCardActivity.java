package com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.socialize.UMShareAPI;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.model.consultentity.DoctorInfoBean;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.request.PersonCardRequest;
import com.xywy.askforexpert.module.message.share.ShareUtil;
import com.xywy.base.XywyBaseActivity;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.xywy.util.ImageLoaderUtils;
import com.xywy.util.LogUtils;

/**
 * 二维码页面
 * Created by xgxg on 2017/3/28.
 */

public class PersonalCardActivity extends XywyBaseActivity {
    public static String DOCTOR_ID = "doctor_id";
    private ImageButton mIbShare;
    private ImageView mFace,mCard;
    private TextView mName,mLevel,mAddress;
    private String mDoctorId;
    private static final String SHARE_TITLE = "医生的寻医诊所开通了！";
    private static final String SHARE_CONTENT = "我在寻医问药网开通了在线诊所，我们可以随时’’面对面’’聊天。";
    private static final String SHARE_TARGET_URL = "http://xianxia.club.xywy.com/index.php?r=doctor/share&appName=wkyszsy&did=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_card);
        hideCommonBaseTitle();
        initView();
        initData();
    }

    private void initView(){
        //初始化统一bar
        CommonUtils.initSystemBar(this);
        initTitle();
        mFace = (ImageView) findViewById(R.id.iv_face);

        mCard = (ImageView) findViewById(R.id.iv_card);

        mName = (TextView) findViewById(R.id.tv_name);

        mLevel = (TextView) findViewById(R.id.tv_level);

        mAddress = (TextView) findViewById(R.id.tv_address);
    }

    private void initTitle() {
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mIbShare = (ImageButton) findViewById(R.id.ib_share);
        mIbShare.setVisibility(View.GONE);
    }

    private void initData(){
////        mDoctorId = getIntent().getIntExtra(DOCTOR_ID, -1);
////        mDoctorId = 68257701;//测试数据
//        UserBean user = UserManager.getInstance().getCurrentLoginUser();
//        dealwithEntry(user.getPersonServerBean());
//        if(null != user){
//            mDoctorId = user.getLoginServerBean().getUser_id();
//            PersonCardRequest.getInstance().getDoctorCard(mDoctorId).subscribe(new BaseRetrofitResponse<BaseData<PersonCard>>(){
//                @Override
//                public void onStart() {
//                    super.onStart();
//                    showProgressDialog("loading");
//                }
//
//                @Override
//                public void onCompleted() {
//                    hideProgressDialog();
//                    super.onCompleted();
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                    super.onError(e);
//                    hideProgressDialog();
//
//                }
//
//                @Override
//                public void onNext(BaseData<PersonCard> entry) {
//                    super.onNext(entry);
//                    if(entry != null ){
//                        if(entry.getCode() == 10000){
//                            if(entry.getData()!=null){
//                                dealwithEntry(entry.getData());
//                            }else {
//                                LogUtils.i("entry.getData()== null");
//                            }
//                        }else {
//                            LogUtils.i("数据返回有问题 code="+entry.getCode());
//                        }
//                    }else {
//                        LogUtils.i("数据返回有问题 entry="+entry);
//                    }
//                }
//            });
//        }

        //采用医脉的用户id
        String doctorId = YMUserService.getCurUserId();
        dealwithEntry(YMApplication.getInstance().getDoctorInfo());
        PersonCardRequest.getInstance().getDoctorEWM(doctorId).subscribe(new BaseRetrofitResponse<BaseData>() {
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
                            share();//设置分享
                            ImageLoaderUtils.getInstance().displayImage(entry.getData().toString(),mCard);
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

    private void share() {
        mIbShare.setVisibility(View.VISIBLE);
        mIbShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.xywy.askforexpert.appcommon.utils.LogUtils.i("分享---"+YMUserService.getCurUserName());

                new ShareUtil.Builder().setTitle(YMUserService.getCurUserName()+SHARE_TITLE)
                        .setText(SHARE_CONTENT)
                        .setTargetUrl(SHARE_TARGET_URL+ YMApplication.getLoginInfo().getData().getPid())
                        .setImageUrl(YMUserService.getCurUserHeadUrl())
                        .build(PersonalCardActivity.this).innerShare();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** 使用SSO授权必须添加如下代码 */
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void dealwithEntry(DoctorInfoBean data) {
        ImageLoaderUtils.getInstance().displayImage(data.getPhoto(),mFace);
        mName.setText(data.getReal_name());

        DoctorInfoBean.DetailsBean detailsBean = data.getDetails();
        if(null != detailsBean){
            mLevel.setText(detailsBean.getClinic());
            StringBuffer sb = new StringBuffer();
            if(!TextUtils.isEmpty(detailsBean.getHos_name())){
                sb.append(detailsBean.getHos_name());
            }else {
                sb.append("");
            }

            if(!TextUtils.isEmpty(detailsBean.getMajor_first())){
                sb.append("-"+detailsBean.getMajor_first());
            }else {
                sb.append("");
            }

            if (!TextUtils.isEmpty(detailsBean.getMajor_second())) {
                sb.append("-" + detailsBean.getMajor_second());
            } else {
                sb.append("");
            }
            mAddress.setText(sb.toString());
        }else {
            mLevel.setText("");
            mAddress.setText("");
        }







//        mFace.setImageResource(R.drawable.test_ic_img_user_default);
//        mName.setText("梁泽燕");
//        mLevel.setText("主治医师");
//        mAddress.setText("廊坊市人名医院-磁共振CT室");
//        Bitmap qrBitmap = generateBitmap("http://www.csdn.net", DensityUtil.dip2px(180),DensityUtil.dip2px(180));
//        mCard.setImageBitmap(qrBitmap);
    }

//    private void dealwithEntry(PersonCard data) {
//        ImageLoaderUtils.getInstance().displayImage(data.getPhoto(),mFace);
//        mName.setText(data.getReal_name());
//
//        UserBean user = UserManager.getInstance().getCurrentLoginUser();
//        if(null != user){
//            mLevel.setText(user.getJobTitle().getName());
//        }else {
//            mLevel.setText("");
//        }
//
//
//
//        StringBuffer sb = new StringBuffer();
//        if(!TextUtils.isEmpty(data.getHos_name())){
//            sb.append(data.getHos_name());
//        }else {
//            sb.append("");
//        }
//
//        if(!TextUtils.isEmpty(data.getMajor_first())){
//            sb.append("-"+data.getMajor_first());
//        }else {
//            sb.append("");
//        }
//
//        if(!TextUtils.isEmpty(data.getMajor_second())){
//            sb.append("-"+data.getMajor_second());
//        }else {
//            sb.append("");
//        }
//        mAddress.setText(sb.toString());
//
//        ImageLoaderUtils.getInstance().displayImage(data.getQrcode(),mCard);
//
////        mFace.setImageResource(R.drawable.test_ic_img_user_default);
////        mName.setText("梁泽燕");
////        mLevel.setText("主治医师");
////        mAddress.setText("廊坊市人名医院-磁共振CT室");
////        Bitmap qrBitmap = generateBitmap("http://www.csdn.net", DensityUtil.dip2px(180),DensityUtil.dip2px(180));
////        mCard.setImageBitmap(qrBitmap);
//    }


//    private Bitmap generateBitmap(String content,int width, int height) {
//        QRCodeWriter qrCodeWriter = new QRCodeWriter();
//        Map<EncodeHintType, String> hints = new HashMap<>();
//        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//        try {
//            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
//            int[] pixels = new int[width * height];
//            for (int i = 0; i < height; i++) {
//                for (int j = 0; j < width; j++) {
//                    if (encode.get(j, i)) {
//                        pixels[i * width + j] = 0x00000000;
//                    } else {
//                        pixels[i * width + j] = 0xffffffff;
//                    }
//                }
//            }
//            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


}
