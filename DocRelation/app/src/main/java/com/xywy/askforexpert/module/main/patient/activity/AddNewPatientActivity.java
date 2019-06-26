package com.xywy.askforexpert.module.main.patient.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.UMShareAPI;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.PatientAddInfo;
import com.xywy.askforexpert.module.message.share.ShareUtil;
import com.xywy.uilibrary.titlebar.ItemClickListener;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

//import com.umeng.socialize.controller.UMServiceFactory;
//import com.umeng.socialize.controller.UMSocialService;
//import com.umeng.socialize.sso.QZoneSsoHandler;
//import com.umeng.socialize.sso.UMQQSsoHandler;
//import com.umeng.socialize.sso.UMSsoHandler;
//import com.umeng.socialize.weixin.controller.UMWXHandler;
//import com.umeng.socialize.weixin.media.CircleShareContent;
//import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * 添加患者
 *
 * @author 王鹏
 * @2015-5-21下午7:41:34
 */
public class AddNewPatientActivity extends YMBaseActivity {

    private TextView tv_realname;
    private TextView tv_content;
    private ImageView img_wxing;
    private PatientAddInfo patientinfo;
    //	private DialogWindow dialog;
//    private HttpHandler mHttpHandler;
    private TextView btn22;
    private ImageView userIcon;
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;


    public void getData() {
        String did = YMApplication.getLoginInfo().getData().getPid();
        Long st = System.currentTimeMillis();

        String sign = MD5Util.MD5(st + did + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();

        params.put("timestamp", st + "");
        params.put("bind", did);
        params.put("a", "chat");
        params.put("m", "patientAddInfo");
        params.put("did", did);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.get(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        DLog.d("ADD_PATIENT", t.toString());
                        Gson gson = new Gson();
                        patientinfo = gson.fromJson(t.toString(), PatientAddInfo.class);
                        if (patientinfo.getCode().equals("0")) {
                            if (patientinfo.getData() != null) {
                                tv_realname.setText(patientinfo.getData().getRealname());
                                String str = patientinfo.getData().getJob() + " "
                                        + patientinfo.getData().getDepart() + " "
                                        + patientinfo.getData().getHospital();
                                tv_content.setText(str);
                                if (YMApplication.getLoginInfo().getData().getPhoto() != null
                                        && !YMApplication.getLoginInfo().getData().getPhoto().equals("")) {
                                    mImageLoader.displayImage(YMApplication.getLoginInfo().getData().getPhoto(),
                                            userIcon, options);
                                }
                                mImageLoader.displayImage(patientinfo.getData().getWximg(), img_wxing, options);
//                                btn22.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        StatisticalTools.eventCount(AddNewPatientActivity.this, "ewmshare");
//                                        // 初始化分享平台
//                                        String url = "http://xianxia.club.xywy.com/index.php?r=doctor/share&did="
//                                                + YMApplication.getLoginInfo().getData().getPid();
////                                        initSocialSDK(patientinfo.getData().getRealname() + "医生的寻医诊所开通啦！",
////                                                "我在寻医问药网开通了在线诊所，我们可以随时“面对面”聊天。",
////                                                url, YMApplication.getLoginInfo().getData().getPhoto());
////                                        mController.openShare(AddNewPatientActivity.this, false);
//
//                                        new ShareUtil.Builder()
//                                                .setTitle(patientinfo.getData().getRealname() + "医生的寻医诊所开通啦！")
//                                                .setText("我在寻医问药网开通了在线诊所，我们可以随时“面对面”聊天。")
//                                                .setTargetUrl(url)
//                                                .setImageUrl(ShareUtil.DEFAULT_SHARE_IMG_ULR)
//                                                .build(AddNewPatientActivity.this).outerShare();
//                                    }
//                                });
                            } else {
                                Toast.makeText(AddNewPatientActivity.this, "您暂时无法使用此功能", Toast.LENGTH_SHORT).show();
                                AddNewPatientActivity.this.finish();
                            }
                        }
                        super.onSuccess(t);
                    }
                });

    }

    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;

            case R.id.next_btn:
                StatisticalTools.eventCount(AddNewPatientActivity.this, "telenumberadd");
                Intent intent = new Intent(this, AddPatientEditActvity.class);
                startActivity(intent);
                break;

        }
    }
//
//	private OnClickListener itemsOnClick = new OnClickListener()
//	{
//
//		@Override
//		public void onClick(View v)
//		{
//			switch (v.getId())
//			{
//			case R.id.rl_channel:
//				dialog.channelDialog();
//				break;
//
//			case R.id.rl_enter:
//				if (NetworkUtil.isExitsSdcard())
//				{
//					String fileth = Environment.getExternalStorageDirectory()
//							.getAbsolutePath() + "/weixin.jpg";
//
//					String url = patientinfo.getData().getWximg().toString();
//					FinalHttp fh = new FinalHttp();
//					// 调用download方法开始下载
//					fh.download(url,
//					// 这里是下载的路径
//					// true:断点续传 false:不断点续传（全新下载）
//							fileth, false,// 这是保存到本地的路径
//							new AjaxCallBack()
//							{
//
//								@Override
//								public void onSuccess(Object t)
//								{
//									YMApplication.Trace("保存成功");
//									T.showNoRepeatShort(
//											AddNewPatientActivity.this, "保存成功");
//									dialog.channelDialog();
//									super.onSuccess(t);
//								}
//
//								@Override
//								public void onFailure(Throwable t, int errorNo,
//										String strMsg)
//								{
//									// TODO Auto-generated method stub
//									YMApplication.Trace("错误日子" + strMsg);
//									dialog.channelDialog();
//									super.onFailure(t, errorNo, strMsg);
//								}
//
//							});
//
//				} else
//				{
//					T.showNoRepeatShort(AddNewPatientActivity.this, "SD卡不存在");
//				}
//				break;
//			}
//		}
//
//	};

    // 初始化分享平台
//    private void initSocialSDK(String shareTitle, String shareContent,
//                               String webUrl, String imageUrl) {
//        // 首先在您的Activity中添加如下成员变量
//        mController = UMServiceFactory.getUMSocialService("com.umeng.share");
//        mController.getConfig().removePlatform(SHARE_MEDIA.TENCENT,
//                SHARE_MEDIA.DOUBAN, SHARE_MEDIA.RENREN);
//        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID ,
//        // http://dev.umeng.com/social/android/operation 分享平台不显示友盟
//        // 注册时候签名33a19e7cf5d223a88c83688b96dacb0b
//        String appID = "wx7adc9ead4e1de7ef";
//        String appSecret = "c2958650c92241d801f105a2d1eaa4d9";
//        // 添加微信平台
//        UMWXHandler wxHandler = new UMWXHandler(AddNewPatientActivity.this,
//                appID, appSecret);
//        wxHandler.addToSocialSDK();
//        WeiXinShareContent weixinContent = new WeiXinShareContent();
//        // 设置分享文字
//        weixinContent.setShareContent(shareContent);
//        // 设置title
//        weixinContent.setTitle(shareTitle);
//        // 设置分享内容跳转URL
//        weixinContent.setTargetUrl(webUrl);
//        // 设置分享图片
//        if ("".equals(imageUrl) || null == imageUrl) {
//            weixinContent.setShareImage(new UMImage(AddNewPatientActivity.this,
//                    R.drawable.dp_icon));
//        } else {
//            weixinContent.setShareImage(new UMImage(AddNewPatientActivity.this,
//                    imageUrl));
//        }
//        mController.setShareMedia(weixinContent);
//
//        // 支持微信朋友圈
//        UMWXHandler wxCircleHandler = new UMWXHandler(
//                AddNewPatientActivity.this, appID, appSecret);
//        wxCircleHandler.setToCircle(true);
//        wxCircleHandler.addToSocialSDK();
//
//        // 设置微信朋友圈分享内容
//        CircleShareContent circleMedia = new CircleShareContent();
//        circleMedia.setShareContent(shareContent);
//        // 设置朋友圈title
//        circleMedia.setTitle(shareTitle);
//        if ("".equals(imageUrl) || null == imageUrl) {
//            circleMedia.setShareImage(new UMImage(AddNewPatientActivity.this,
//                    R.drawable.dp_icon));
//        } else {
//            circleMedia.setShareImage(new UMImage(AddNewPatientActivity.this,
//                    imageUrl));
//        }
//
//        circleMedia.setTargetUrl(webUrl);
//        mController.setShareMedia(circleMedia);
//        // QQ平台
//        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(
//                AddNewPatientActivity.this, "1101752729", "LgrXeXE27j0PFDlU");
//        qqSsoHandler.addToSocialSDK();
//
//        QQShareContent qqShareContent = new QQShareContent();
//        // 设置分享文字
//        qqShareContent.setShareContent(shareContent);
//        // 设置分享title
//        qqShareContent.setTitle(shareTitle);
//        // 设置分享图片
//        if ("".equals(imageUrl) || null == imageUrl) {
//            qqShareContent.setShareImage(new UMImage(
//                    AddNewPatientActivity.this, R.drawable.dp_icon));
//        } else {
//            qqShareContent.setShareImage(new UMImage(
//                    AddNewPatientActivity.this, imageUrl));
//        }
//
//        // 设置点击分享内容的跳转链接
//        qqShareContent.setTargetUrl(webUrl);
//        mController.setShareMedia(qqShareContent);
//
//        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
//                AddNewPatientActivity.this, "1101752729",
//                "LgrXeXE27j0PFDlU");
//        qZoneSsoHandler.addToSocialSDK();
//
//        // qq空间
//        QZoneShareContent qzone = new QZoneShareContent();
//        // 设置分享文字
//        qzone.setShareContent(shareContent);
//        // 设置点击消息的跳转URL
//        qzone.setTargetUrl(webUrl);
//        // 设置分享内容的标题
//        qzone.setTitle(shareTitle);
//        // 设置分享图片
//        if ("".equals(imageUrl) || null == imageUrl) {
//            qzone.setShareImage(new UMImage(AddNewPatientActivity.this,
//                    R.drawable.dp_icon));
//        } else {
//            qzone.setShareImage(new UMImage(AddNewPatientActivity.this,
//                    imageUrl));
//        }
//
//        mController.setShareMedia(qzone);
//
//        // // 新浪微博平台
////		mController.getConfig().setSsoHandler(new SinaSsoHandler());
//        SinaShareContent sinaContent = new SinaShareContent();
//        sinaContent.setTitle(shareTitle);
//        sinaContent.setShareContent(shareContent + webUrl);
//        sinaContent.setTargetUrl(webUrl);
//        if ("".equals(imageUrl) || null == imageUrl) {
//            sinaContent.setShareImage(new UMImage(AddNewPatientActivity.this,
//                    R.drawable.dp_icon));
//        } else {
//            sinaContent.setShareImage(new UMImage(AddNewPatientActivity.this,
//                    imageUrl));
//        }
//
//        mController.setShareMedia(sinaContent);
//        mController.getConfig().setPlatformOrder(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ,
//                SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA);
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        /** 使用SSO授权必须添加如下代码 */
//        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
//                requestCode);
//        if (ssoHandler != null) {
//            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
//        }
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void initView() {
//        hideCommonBaseTitle();
//        ((TextView) findViewById(R.id.tv_title)).setText("添加患者");
//        btn22 = (TextView) findViewById(R.id.btn22);
//        btn22.setVisibility(View.VISIBLE);
//        ((TextView) findViewById(R.id.btn22)).setText("分享");

        titleBarBuilder.setTitleText("患者管理").addItem("分享", new ItemClickListener() {
            @Override
            public void onClick() {

                StatisticalTools.eventCount(AddNewPatientActivity.this, "ewmshare");
                // 初始化分享平台
                String url = "http://xianxia.club.xywy.com/index.php?r=doctor/share&did="
                        + YMApplication.getLoginInfo().getData().getPid();
//                                        initSocialSDK(patientinfo.getData().getRealname() + "医生的寻医诊所开通啦！",
//                                                "我在寻医问药网开通了在线诊所，我们可以随时“面对面”聊天。",
//                                                url, YMApplication.getLoginInfo().getData().getPhoto());
//                                        mController.openShare(AddNewPatientActivity.this, false);

                new ShareUtil.Builder()
                        .setTitle(patientinfo.getData().getRealname() + "医生的寻医诊所开通啦！")
                        .setText("我在寻医问药网开通了在线诊所，我们可以随时“面对面”聊天。")
                        .setTargetUrl(url)
                        .setImageUrl(ShareUtil.DEFAULT_SHARE_IMG_ULR)
                        .build(AddNewPatientActivity.this).outerShare();

//                StatisticalTools.eventCount(AddNewPatientActivity.this, "telenumberadd");
//                Intent intent = new Intent(AddNewPatientActivity.this, AddPatientEditActvity.class);
//                startActivity(intent);
            }
        }).build();


        mImageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .showImageOnFail(R.drawable.icon_photo_def)
                .showImageOnLoading(R.drawable.icon_photo_def)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        tv_realname = (TextView) findViewById(R.id.tv_realname);
        tv_content = (TextView) findViewById(R.id.tv_content);
        userIcon = (ImageView) findViewById(R.id.user_icon);
        img_wxing = (ImageView) findViewById(R.id.img_wxing);
//        btn22 = (ImageButton) findViewById(R.id.btn2);
        if (NetworkUtil.isNetWorkConnected()) {
            getData();
        } else {
            ToastUtils.shortToast("网络连接失败");
        }

        img_wxing.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
//				dialog = new DialogWindow(AddNewPatientActivity.this, "是否保存",
//						"可以将图片保存到文件根目录下", "取消", "保存");
//				dialog.createDialogView(itemsOnClick);

                final android.support.v7.app.AlertDialog.Builder dialogs = new android.support.v7.app.AlertDialog.Builder(
                        AddNewPatientActivity.this);
                dialogs.setTitle("是否保存");

                dialogs.setMessage("可以将图片保存到文件根目录下");

                dialogs.setPositiveButton("确定", new OnClickListener() {

                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        if (NetworkUtil.isExitsSdcard()) {
                            String fileth = Environment.getExternalStorageDirectory()
                                    .getAbsolutePath() + "/weixin.jpg";

                            String url = patientinfo.getData().getWximg();
                            FinalHttp fh = new FinalHttp();
                            // 调用download方法开始下载
                            fh.download(url,
                                    // 这里是下载的路径
                                    // true:断点续传 false:不断点续传（全新下载）
                                    fileth, false,// 这是保存到本地的路径
                                    new AjaxCallBack() {
                                        public void onSuccess(String t) {
                                            ToastUtils.shortToast(
                                                    "保存成功");
                                            dialog.dismiss();
                                            super.onSuccess(t);
                                        }

                                        @Override
                                        public void onFailure(Throwable t, int errorNo,
                                                              String strMsg) {
                                            dialog.dismiss();
                                            super.onFailure(t, errorNo, strMsg);
                                        }

                                    });

                        } else {
                            ToastUtils.shortToast("SD卡不存在");
                        }


                    }
                });
                dialogs.setNegativeButton("取消", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                dialogs.create().show();


                return false;
            }
        });
    }

    @Override
    protected void initData() {

    }


    @Override
    protected int getLayoutResId() {
        return R.layout.add_new_patient;
    }
}
