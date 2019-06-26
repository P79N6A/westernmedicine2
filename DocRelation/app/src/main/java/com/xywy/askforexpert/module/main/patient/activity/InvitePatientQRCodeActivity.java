package com.xywy.askforexpert.module.main.patient.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by jason on 2018/11/6.
 */

public class InvitePatientQRCodeActivity extends YMBaseActivity {
    @Bind(R.id.qr_code_im)
    ImageView qr_code_im;
    @Bind(R.id.doc_name_tv)
    TextView doc_name_tv;
    @Bind(R.id.job_title_tv)
    TextView job_title_tv;
    @Bind(R.id.department_tv)
    TextView department_tv;
    private String doctorId = YMUserService.getCurUserId();

    @Override
    protected void initView() {
        doc_name_tv.setText(YMApplication.getLoginInfo().getData().getRealname());
        job_title_tv.setText(YMApplication.getLoginInfo().getData().getJob());
        department_tv.setText(YMApplication.getLoginInfo().getData().getHos_name()+"-"
                +YMApplication.getLoginInfo().getData().getMajorfirst());
    }

    @Override
    protected void initData() {
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
                                ImageLoader.getInstance().displayImage(map.get("data"), qr_code_im);
                            }
                        }
                        super.onSuccess(t);
                    }
                });
        qr_code_im.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                screenCapture();
                return false;
            }
        });
    }

    private void screenCapture(){
        View dView = getWindow().getDecorView();
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(dView.getDrawingCache());
        saveBitmap(bitmap,"ymqr"+System.currentTimeMillis());
    }
    public void saveBitmap(final Bitmap bitmap, final String bitName){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String fileName ;
                File file ;
                Message message = Message.obtain();
                if(Build.BRAND .equals("Xiaomi") ){ // 小米手机
                    fileName = Environment.getExternalStorageDirectory().getPath()+"/DCIM/Camera/"+bitName ;
                }else{  // Meizu 、Oppo
                    fileName = Environment.getExternalStorageDirectory().getPath()+"/DCIM/"+bitName ;
                }
                file = new File(fileName);

                if(file.exists()){
                    file.delete();
                }
                FileOutputStream out;
                try{
                    out = new FileOutputStream(file);
                    // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
                    if(bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out))
                    {
                        out.flush();
                        out.close();
                        MediaStore.Images.Media.insertImage(InvitePatientQRCodeActivity.this.getContentResolver(), file.getAbsolutePath(), bitName, null);
                    }
                    message.what = 1;
                    uiHandler.handleMessage(message);
                }
                catch (Exception e)
                {
                    message.what = 0;
                    uiHandler.handleMessage(message);
                    e.printStackTrace();
                }
                // 发送广播，通知刷新图库的显示
                InvitePatientQRCodeActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));
            }
        });
        thread.start();
    }

    @Override
    protected void handleMsg(Message msg) {
        super.handleMsg(msg);
        switch (msg.what){
            case 0:
                ToastUtils.longToast("保存失败");
                break;
            case 1:
                ToastUtils.longToast("保存成功");
                break;
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.invite_patient_qrcode_layout;
    }
}
