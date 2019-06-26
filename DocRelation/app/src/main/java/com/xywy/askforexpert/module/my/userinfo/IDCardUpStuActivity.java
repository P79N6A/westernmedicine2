package com.xywy.askforexpert.module.my.userinfo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hyphenate.util.PathUtil;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.HttpMultipartPost;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ScreenUtils;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.UploadImgInfo;
import com.xywy.askforexpert.module.main.diagnose.adapter.MainGVAdapter;
import com.xywy.askforexpert.module.my.photo.PhotoActivity;
import com.xywy.askforexpert.module.my.photo.PhotoWallActivity;
import com.xywy.askforexpert.widget.SDCardImageLoader;
import com.xywy.askforexpert.widget.view.HorizontalListView;
import com.xywy.askforexpert.widget.view.SelectPicPopupWindow;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学生 认证页面
 *
 * @author 王鹏
 * @2015-4-27下午4:30:51
 */
@Deprecated
public class IDCardUpStuActivity extends YMBaseActivity {
    private static final String TAG = "IDCardUpStuActivity";
    private SelectPicPopupWindow menuWindow;
    private HorizontalListView mainGV;
    public static List<String> imagePathList = new ArrayList<String>();

    private List<String> imagelist = new ArrayList<String>();
    private LinearLayout idcard_main;
    private File origUri;
    private String path_id;
    private final static String FILE_SAVEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera";
    /**
     * 相机标志
     */
    private static final int FLAG_CHOOSE_CAMERA = 0x17;
    /**
     * 截取完成
     */
    private static final int FLAG_MODIFY_FINISH = 7;

    private MainGVAdapter adapter;
    HttpMultipartPost post;
    private ImageView headimgs;
    private SharedPreferences sp;
    private FinalBitmap fb;
    private String photoUrl;
    public static List<String> stu_list = new ArrayList<String>();
    private Map<String, String> map = new HashMap<String, String>();


    @Override
    protected int getLayoutResId() {
        return R.layout.idcarduploadstudent;
    }

    @Override
    protected void beforeViewBind() {
        sp = getSharedPreferences(YMApplication.getLoginInfo().getData().getPid(),
                MODE_PRIVATE);

        ScreenUtils.initScreen(IDCardUpStuActivity.this);
        fb = FinalBitmap.create(IDCardUpStuActivity.this, true);

    }

    @Override
    protected void initView() {
        hideCommonBaseTitle();
        mainGV = (HorizontalListView) findViewById(R.id.idcard_stu);
        idcard_main = (LinearLayout) findViewById(R.id.main_stu);
        headimgs = (ImageView) findViewById(R.id.headimgs);

        adapter = new MainGVAdapter(IDCardUpStuActivity.this, 2);
        adapter.setData(imagePathList);
        mainGV.setAdapter(adapter);
        mainGV.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == imagePathList.size()) {
                    path_id = "idcard";
                    menuWindow = new SelectPicPopupWindow(
                            IDCardUpStuActivity.this, itemsOnClick);
                    // 显示窗口
                    menuWindow.showAtLocation(idcard_main, Gravity.BOTTOM
                            | Gravity.CENTER_HORIZONTAL, 0, 0);
                    SDCardImageLoader.count = imagePathList.size();
                    SDCardImageLoader.img_max = 2;
                } else {
                    Intent intent = new Intent(IDCardUpStuActivity.this,
                            PhotoActivity.class);
                    intent.putExtra("statPosition", arg2);
                    intent.putExtra("keytype", "stu_job");
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void initData() {
        photoUrl = sp.getString("photo", "");
        if (photoUrl.equals("")) {
            photoUrl = YMApplication.getLoginInfo().getData().getPhoto();
        }
        fb.display(headimgs, photoUrl);
        String path = sp.getString("str_stu_idcard_path", "");
        String save_path = sp.getString("str_stu_idcard_save", "");
        stu_list.clear();
        imagePathList.clear();
        String[] strarry;
        String[] strarry1;
        if (!path.equals("")) {
            strarry = path.split(",");
            for (int i = 0; i < strarry.length; i++) {
                imagePathList.add(strarry[i]);
            }
            strarry1 = save_path.split(",");
            for (int i = 0; i < strarry1.length; i++) {
                stu_list.add(strarry1[i]);
            }

        }
        adapter.setData(imagePathList);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void handleMsg(Message msg) {
        super.handleMsg(msg);

        switch (msg.what) {
            case 100:
                UploadImgInfo upinfo = (UploadImgInfo) msg.obj;
                String code = upinfo.getCode();
                if (code != null && code.equals("0")) {
                    ToastUtils.shortToast("上传成功");
                    for (int i = 0; i < upinfo.getData().size(); i++) {
                        stu_list.add(upinfo.getData().get(i).getUrl());
                        imagePathList.add(imagelist.get(i));
                    }

                } else if (code != null && code.equals("-1")) {
                    for (int i = 0; i < upinfo.getData().size(); i++) {
                        if ("0".equals(upinfo.getData().get(i).getCode())) {
                            stu_list.add(upinfo.getData().get(i).getUrl());
                            imagePathList.add(imagelist.get(i));
                        } else {
                            int tag = i + 1;
                            ToastUtils.shortToast( "第" + tag + "张上传错误，请重新上传");
                        }

                    }
                }
                adapter.setData(imagePathList);
                adapter.notifyDataSetChanged();
                break;
            case 200:

                break;
            case 300:
                if (map != null && map.get("code").equals("0")) {
                    YMApplication.getLoginInfo().getData().setIsdoctor("13");
                    YMApplication.getLoginInfo().getData().setApproveid("0");
                }
                ToastUtils.shortToast(map.get("msg"));
                finish();
                break;
            default:
                break;
        }
    }

    // 为弹出窗口实现监听类
    private OnClickListener itemsOnClick = new OnClickListener() {
        Intent intent;

        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.item_popupwindows_camera:
                    // 照片命名
                    // String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                    // .format(new Date());
                    // String origFileName = "osc_3" + timeStamp + ".jpg";
                    // origUri = Uri.fromFile(new File(FILE_SAVEPATH,
                    // origFileName));
                    origUri = new File(PathUtil.getInstance().getImagePath(),
                            "osc_" + System.currentTimeMillis() + ".jpg");
                    origUri.getParentFile().mkdirs();
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(origUri));
                    startActivityForResult(intent, FLAG_CHOOSE_CAMERA);
                    // startActivity(intent);
                    break;
                case R.id.item_popupwindows_Photo:
                    intent = new Intent(IDCardUpStuActivity.this,
                            PhotoWallActivity.class);
                    startActivity(intent);
                    YMApplication.photoTag = "stu_idcard";
                    YMApplication.isSelectMore = !path_id.equals("headimg");

                    break;
                case R.id.item_popupwindows_cancel:
                    menuWindow.dismiss();
                    break;
                default:
                    break;
            }

        }

    };

    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.re_heads:
                path_id = "headimg";
                menuWindow = new SelectPicPopupWindow(IDCardUpStuActivity.this,
                        itemsOnClick);
                // 显示窗口
                menuWindow.showAtLocation(idcard_main, Gravity.BOTTOM
                        | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.btn2:
                Intent intent = new Intent(IDCardUpStuActivity.this,
                        ApproveInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.lin_submit:
                String str_idcard = getListPath();
                if (TextUtils.isEmpty(photoUrl)) {
                    ToastUtils.shortToast("请上传头像");
                } else if (TextUtils.isEmpty(str_idcard)) {
                    ToastUtils.shortToast("请选择证件照");
                } else {
                    sendData(photoUrl, str_idcard);
                }
                break;
            case R.id.img_idcard:
                DialogShow(IDCardUpStuActivity.this, R.drawable.idcard_stu_big);
                break;
            default:
                break;
        }
    }

    public void DialogShow(Context context, int resid) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(context)
                .inflate(R.layout.idcard_image, null);
        final Dialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        ImageView head_two = (ImageView) layout
                .findViewById(R.id.img_idcard_show);

        head_two.setBackgroundResource(resid);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        imagelist.clear();
        int code = intent.getIntExtra("code", -1);
        if (code != 100) {
            return;
        }

        ArrayList<String> list1 = new ArrayList<String>();
        list1.clear();
        ArrayList<String> paths = intent.getStringArrayListExtra("paths");
        // 添加，去重
        boolean hasUpdate = false;
        for (String path : paths) {
            if (path_id.equals("idcard")) {
                if (!imagePathList.contains(path)) {
                    // 最多5张
                    if (imagePathList.size() == 3) {
                        ToastUtils.shortToast(
                                "最多可添加5张图片。");
                        break;
                    }

                    imagelist.add(path);
                    list1.add(path);
                    hasUpdate = true;

                }
            } else if (path_id.equals("headimg")) {
                if (paths != null & paths.size() > 0) {
                    Intent intent2 = new Intent(IDCardUpStuActivity.this,
                            ClipPictureActivity.class);
                    intent2.putExtra("path", paths.get(0));
                    startActivityForResult(intent2, FLAG_MODIFY_FINISH);
                } else {
                    ToastUtils.shortToast("图片选取失败");
                }
            }

        }

        if (hasUpdate) {
            if (path_id.equals("idcard")) {
                post = new HttpMultipartPost(IDCardUpStuActivity.this, list1,
                        CommonUrl.UpdataImgUrl,
                        uiHandler, 100);
                post.execute();
                adapter.notifyDataSetChanged();

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        imagelist.clear();
        if (requestCode == FLAG_CHOOSE_CAMERA && resultCode == RESULT_OK) {
            if (path_id.equals("idcard")) {
                // 最多4张
                if (imagePathList.size() == 3) {
                    ToastUtils.shortToast("最多可添加3张图片。");
                    return;
                }
                if (NetworkUtil.isNetWorkConnected()) {
                    List<String> list = new ArrayList<String>();
                    list.add(origUri.getPath());
                    imagelist.add(origUri.getPath());
                    adapter.notifyDataSetChanged();
                    post = new HttpMultipartPost(IDCardUpStuActivity.this,
                            list,
                            CommonUrl.UpdataImgUrl,
                            uiHandler, 100);
                    post.execute();
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtils.shortToast( "网络连接失败,图片不能上传,请联网重试");

                }
            } else if (path_id.equals("headimg")) {
                Intent intent = new Intent(IDCardUpStuActivity.this,
                        ClipPictureActivity.class);
                intent.putExtra("path", origUri.getPath());
                startActivityForResult(intent, FLAG_MODIFY_FINISH);

            }

        } else if (requestCode == FLAG_MODIFY_FINISH && resultCode == RESULT_OK) {
            if (data != null) {
                final String path = data.getStringExtra("path");
                // Bitmap b = BitmapFactory.decodeFile(path);
                // headimgs.setImageBitmap(b);
                if (path != null && !path.equals("")) {
                    sp.edit().putString("photo", path).commit();
                    fb.display(headimgs, path);
                    photoUrl = path;
                    // YMApplication.perInfo.getData().setPhoto(path);
                    // UpdatePersonInfo.Update("photo", path, 400, handler);

                }
            }
        }

    }

    /**
     * 拼接上传图片返回的 图片地址
     *
     * @return
     */
    public String getListPath() {
        String str = "";
        StringBuffer sb = new StringBuffer();
        if (stu_list != null && stu_list.size() > 0) {
            for (int j = 0; j < stu_list.size(); j++) {
                sb.append(stu_list.get(j) + "|");
            }
            // StringUtils.join(stu_list, "");
            if (sb.length() > 0) {
                str = sb.substring(0, sb.lastIndexOf("|"));
            }

        }
        return str;
    }

    /**
     * 拼接上传图片返回的 图片地址
     *
     * @return
     */
    public String getListPath2(List<String> list) {
        String str = "";
        StringBuffer sb = new StringBuffer();
        if (list != null && list.size() > 0) {
            for (int j = 0; j < list.size(); j++) {
                sb.append(list.get(j) + ",");
            }
            // StringUtils.join(stu_list, "");
            if (sb.length() > 0) {
                str = sb.substring(0, sb.lastIndexOf(","));
            }

        }
        return str;
    }

    public void sendData(String photo, String idcardurl) {
        final String uid = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(uid + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("userid", YMApplication.getLoginInfo().getData().getPid());

        params.put("photo", photo);
        params.put("school_card", idcardurl);
        params.put("command", "yixue_rz");
        params.put("sign", sign);
        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.DP_COMMON, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                DLog.i(TAG, "一学生提交数据。" + t.toString());
                map = ResolveJson.R_Action(t.toString());
                uiHandler.sendEmptyMessage(300);
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
//				System.out.println("错误信息" + strMsg);
                ToastUtils.shortToast(strMsg);
                super.onFailure(t, errorNo, strMsg);
            }

            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }
        });
    }

    public void saveImg() {
        String str_idcard = getListPath();
        String str_idcard_path = getListPath2(imagePathList);
        String str_idcard_save = getListPath2(stu_list);
        sp.edit().putString("str_stu_idcard_save", str_idcard_save).commit();
        sp.edit().putString("str_idcard", str_idcard).commit();
        sp.edit().putString("str_stu_idcard_path", str_idcard_path).commit();
    }

    @Override
    protected void onDestroy() {
        saveImg();
        super.onDestroy();
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        saveImg();
    }


}
