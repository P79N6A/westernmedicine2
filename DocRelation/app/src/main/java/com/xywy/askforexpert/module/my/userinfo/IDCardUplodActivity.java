package com.xywy.askforexpert.module.my.userinfo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.util.PathUtil;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.HttpMultipartPost;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.PermissionUtils;
import com.xywy.askforexpert.appcommon.utils.ScreenUtils;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.model.LoginInfo;
import com.xywy.askforexpert.model.UploadImgInfo;
import com.xywy.askforexpert.model.certification.ApproveBean;
import com.xywy.askforexpert.module.discovery.medicine.CertificationAboutRequest;
import com.xywy.askforexpert.module.main.diagnose.adapter.MainGVAdapter;
import com.xywy.askforexpert.module.my.photo.PhotoActivity;
import com.xywy.askforexpert.module.my.photo.PhotoWallActivity;
import com.xywy.askforexpert.widget.SDCardImageLoader;
import com.xywy.askforexpert.widget.view.HorizontalListView;
import com.xywy.askforexpert.widget.view.SelectPicPopupWindow;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.xywy.askforexpert.YMApplication.getLogin1;

/**
 * [认证步骤二]医生身份证上传，证件上传 认证页面 stone 添加职称证上传
 */
public class IDCardUplodActivity extends YMBaseActivity {

    private ApproveBean mApproveBean;
    private String str_job;
    private String str_idcard;
    private String str_jobTitle;


    public static final String JOB_TITLE = "jobTitle";//职称 stone

    private MainGVAdapter adapter;
    private MainGVAdapter adapter2;
    private MainGVAdapter adapter3;
    //执业证
    public static List<String> imagePathList = new ArrayList<String>();
    private List<String> imagelist = new ArrayList<String>();
    //身份证
    public static List<String> imagePahtList_idcard = new ArrayList<String>();
    private List<String> imagelist_idcard = new ArrayList<String>();
    //职称证
    public static List<String> imagePahtList_jobTitle = new ArrayList<String>();
    private List<String> imagelist_jobTitle = new ArrayList<String>();

    private TextView tv_jobtitle_notice;
    private LinearLayout idcard_main;
    private SelectPicPopupWindow menuWindow;
    private LinearLayout next_btn;
    private HorizontalListView idcard;
    private HorizontalListView mainGV;
    private HorizontalListView jobTitle;
    private File origUri;
    private String path_id = "";
//    private final static String FILE_SAVEPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera";
    /**
     * 相机标志
     */
    private static final int FLAG_CHOOSE_CAMERA = 18;
    private HttpMultipartPost post;

    //上传图片返回的图片url
    public static List<String> list_idcard = new ArrayList<String>();
    public static List<String> list_job = new ArrayList<String>();
    public static List<String> list_jobTitle = new ArrayList<String>();

    private SharedPreferences mSharedPreferences;


    public void init_job(String job_path) {
        imagePathList.clear();
        String[] strarry;
        if (!job_path.equals("")) {

            strarry = job_path.split(",");
            for (int i = 0; i < strarry.length; i++) {
                imagePathList.add(strarry[i]);
            }
        }
        adapter.setData(imagePathList);
        adapter.notifyDataSetChanged();
    }

    public void init_idcard(String idcard_path) {
        imagePahtList_idcard.clear();
        String[] strarry;
        strarry = idcard_path.split(",");
        for (int i = 0; i < strarry.length; i++) {
            imagePahtList_idcard.add(strarry[i]);
        }
        adapter2.setData(imagePahtList_idcard);
        adapter2.notifyDataSetChanged();
    }

    public void init_jobTitle(String jobTitle_path) {
        imagePahtList_jobTitle.clear();
        String[] strarry;
        strarry = jobTitle_path.split(",");
        for (int i = 0; i < strarry.length; i++) {
            imagePahtList_jobTitle.add(strarry[i]);
        }
        adapter3.setData(imagePahtList_jobTitle);
        adapter3.notifyDataSetChanged();
    }

    public List<String> init_Save(String save) {
        List<String> list = new ArrayList<String>();
        list.clear();
        String[] strarry;
        strarry = save.split(",");
        for (int i = 0; i < strarry.length; i++) {
            list.add(strarry[i]);
        }
        return list;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.idcardupload;
    }

    @Override
    protected void beforeViewBind() {
        //stone 共享文件名是 用户的唯一标志 pid
        mSharedPreferences = getSharedPreferences(YMApplication.getLoginInfo().getData().getPid(), MODE_PRIVATE);
        ScreenUtils.initScreen(this);

        if (getIntent() != null) {
            mApproveBean = (ApproveBean) getIntent().getSerializableExtra(Constants.KEY_VALUE);
        }
    }

    @Override
    protected void initView() {
        hideCommonBaseTitle();
        tv_jobtitle_notice = (TextView) findViewById(R.id.tv_jobtitle_notice);
        jobTitle = (HorizontalListView) findViewById(R.id.jobTitle);
        mainGV = (HorizontalListView) findViewById(R.id.jobcard);
        idcard_main = (LinearLayout) findViewById(R.id.idcard_main);
        idcard = (HorizontalListView) findViewById(R.id.idcard);
        adapter = new MainGVAdapter(this, 5);
        adapter.setData(imagePathList);
        adapter2 = new MainGVAdapter(this, 2);
        adapter2.setData(imagePahtList_idcard);
        adapter3 = new MainGVAdapter(this, 5);
        adapter3.setData(imagePahtList_jobTitle);
        jobTitle.setAdapter(adapter3);
        idcard.setAdapter(adapter2);
        mainGV.setAdapter(adapter);
        ((TextView) findViewById(R.id.tv_title)).setText("专业认证");

        if (mApproveBean != null) {
            //主任、副主任、主治还需判断职称证
            if (Integer.parseInt(mApproveBean.clinic) <= 3) {
                tv_jobtitle_notice.setText("（必填，可申请更多服务，最多可上传5张图片）");
            } else {
                tv_jobtitle_notice.setText("（选填，可申请更多服务，最多可上传5张图片）");
            }
        }

    }

    @Override
    protected void initData() {
        //首先给清空,否则切换账号有问题 stone
        imagePathList.clear();
        imagePahtList_idcard.clear();
        imagePahtList_jobTitle.clear();
        list_job.clear();
        list_idcard.clear();
        list_jobTitle.clear();


        String job_path = mSharedPreferences.getString("str_job_path", "");
        String idcard_path = mSharedPreferences.getString("str_idcard_path", "");
        String jobTitle_path = mSharedPreferences.getString("str_jobTitle_path", "");

        if (!job_path.equals("")) {
            init_job(job_path);
            list_job = init_Save(mSharedPreferences.getString("str_job_save", ""));
        }
        if (!idcard_path.equals("")) {
            init_idcard(idcard_path);
            list_idcard = init_Save(mSharedPreferences.getString("str_idcard_save", ""));
        }
        if (!jobTitle_path.equals("")) {
            init_jobTitle(jobTitle_path);
            list_jobTitle = init_Save(mSharedPreferences.getString("str_jobTitle_save", ""));
        }

        // loadlist ld=new loadlist();
        // ld.execute(null);
        jobTitle.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                if (arg2 == imagePahtList_jobTitle.size()) {
                    path_id = JOB_TITLE;
                    menuWindow = new SelectPicPopupWindow(
                            IDCardUplodActivity.this, itemsOnClick);

                    // 显示窗口
                    menuWindow.showAtLocation(idcard_main, Gravity.BOTTOM
                            | Gravity.CENTER_HORIZONTAL, 0, 0);
                    //添加遮罩 stone
                    YMOtherUtils.addScreenBg(menuWindow, IDCardUplodActivity.this);

                    SDCardImageLoader.count = imagePahtList_jobTitle.size();
                    SDCardImageLoader.img_max = 5;
                } else {
                    Intent intent = new Intent(IDCardUplodActivity.this,
                            PhotoActivity.class);
                    intent.putExtra("statPosition", arg2);
                    intent.putExtra("keytype", JOB_TITLE);
                    startActivity(intent);
                }
            }
        });

        mainGV.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                if (arg2 == imagePathList.size()) {
                    path_id = "job";
                    menuWindow = new SelectPicPopupWindow(
                            IDCardUplodActivity.this, itemsOnClick);

                    // 显示窗口
                    menuWindow.showAtLocation(idcard_main, Gravity.BOTTOM
                            | Gravity.CENTER_HORIZONTAL, 0, 0);
                    //添加遮罩 stone
                    YMOtherUtils.addScreenBg(menuWindow, IDCardUplodActivity.this);

                    SDCardImageLoader.count = imagePathList.size();
                    SDCardImageLoader.img_max = 5;
                } else {
                    Intent intent = new Intent(IDCardUplodActivity.this,
                            PhotoActivity.class);
                    intent.putExtra("statPosition", arg2);
                    intent.putExtra("keytype", "job");
                    startActivity(intent);
                }
            }
        });
        idcard.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                if (arg2 == imagePahtList_idcard.size()) {
                    path_id = "idcard";
                    menuWindow = new SelectPicPopupWindow(
                            IDCardUplodActivity.this, itemsOnClick);

                    // 显示窗口
                    menuWindow.showAtLocation(idcard_main, Gravity.BOTTOM
                            | Gravity.CENTER_HORIZONTAL, 0, 0);
                    //添加遮罩 stone
                    YMOtherUtils.addScreenBg(menuWindow, IDCardUplodActivity.this);

                    SDCardImageLoader.count = imagePahtList_idcard.size();
                    SDCardImageLoader.img_max = 2;
                } else {
                    Intent intent = new Intent(IDCardUplodActivity.this,
                            PhotoActivity.class);
                    intent.putExtra("statPosition", arg2);
                    intent.putExtra("keytype", "idcard");
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void handleMsg(Message msg) {
        super.handleMsg(msg);
        String code;
        switch (msg.what) {
            case 100:
                UploadImgInfo upinfo = (UploadImgInfo) msg.obj;
                code = upinfo.getCode();
                if (code != null && code.equals("0")) {
                    for (int i = 0; i < upinfo.getData().size(); i++) {
                        list_job.add(upinfo.getData().get(i).getUrl()
                                .toString());
                        imagePathList.add(imagelist.get(i));
                    }

                } else if (code != null && "-1".equals(code)) {
                    for (int i = 0; i < upinfo.getData().size(); i++) {
                        if ("0".equals(upinfo.getData().get(i).getCode())) {
                            list_job.add(upinfo.getData().get(i).getUrl()
                                    .toString());
                            imagePathList.add(imagelist.get(i));

                        } else if ("-1".equals(upinfo.getData().get(i)
                                .getCode())) {

                            int tag = i + 1;
                            ToastUtils.shortToast("第" + tag
                                    + "张上传错误，请重新上传");
                        }
                    }
                }
                adapter.setData(imagePathList);
                adapter.notifyDataSetChanged();
                saveImage();
                break;
            case 200:
                UploadImgInfo upinfo2 = (UploadImgInfo) msg.obj;
                code = upinfo2.getCode();
                if (code != null && code.equals("0")) {
                    for (int i = 0; i < upinfo2.getData().size(); i++) {
                        list_idcard.add(upinfo2.getData().get(i).getUrl().toString());
                        imagePahtList_idcard.add(imagelist_idcard.get(i));
                    }
                    ToastUtils.shortToast("上传成功");
                } else if (code != null && "-1".equals(code)) {
                    for (int i = 0; i < upinfo2.getData().size(); i++) {
                        if ("0".equals(upinfo2.getData().get(i).getCode())) {
                            list_idcard.add(upinfo2.getData().get(i).getUrl()
                                    .toString());
                            imagePahtList_idcard.add(imagelist_idcard.get(i));

                        } else if ("-1".equals(upinfo2.getData().get(i)
                                .getCode())) {

                            int tag = i + 1;
                            ToastUtils.shortToast("第" + tag
                                    + "张上传错误，请重新上传");
                        }
                    }

                }
                adapter2.setData(imagePahtList_idcard);
                adapter2.notifyDataSetChanged();
                saveImage();
                break;
            case 300:
                UploadImgInfo upinfo3 = (UploadImgInfo) msg.obj;
                code = upinfo3.getCode();
                if (code != null && code.equals("0")) {
                    for (int i = 0; i < upinfo3.getData().size(); i++) {
                        list_jobTitle.add(upinfo3.getData().get(i).getUrl().toString());
                        imagePahtList_jobTitle.add(imagelist_jobTitle.get(i));
                    }
                    ToastUtils.shortToast("上传成功");
                } else if (code != null && "-1".equals(code)) {
                    for (int i = 0; i < upinfo3.getData().size(); i++) {
                        if ("0".equals(upinfo3.getData().get(i).getCode())) {
                            list_jobTitle.add(upinfo3.getData().get(i).getUrl()
                                    .toString());
                            imagePahtList_jobTitle.add(imagelist_jobTitle.get(i));

                        } else if ("-1".equals(upinfo3.getData().get(i)
                                .getCode())) {

                            int tag = i + 1;
                            ToastUtils.shortToast("第" + tag
                                    + "张上传错误，请重新上传");
                        }
                    }

                }
                adapter3.setData(imagePahtList_jobTitle);
                adapter3.notifyDataSetChanged();
                saveImage();
                break;
            case 400:
                adapter.setData(imagePathList);
                adapter.notifyDataSetChanged();
                adapter2.setData(imagePahtList_idcard);
                adapter2.notifyDataSetInvalidated();
                adapter3.setData(imagePahtList_jobTitle);
                adapter3.notifyDataSetInvalidated();
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
                    if (!PermissionUtils.checkPermission(IDCardUplodActivity.this, Manifest.permission.CAMERA)) {
                        CommonUtils.permissionRequestDialog(IDCardUplodActivity.this, "请先授予照相机(Camera)权限", 555);
                    } else {
                        origUri = new File(PathUtil.getInstance().getImagePath(),
                                "osc_" + System.currentTimeMillis() + ".jpg");
                        if (origUri.exists()) {
                            if (!origUri.getParentFile().exists()) {
                                origUri.getParentFile().mkdirs();
                            }
                        }
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri mImageUri;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果是7.0android系统
                            ContentValues contentValues = new ContentValues(1);
                            contentValues.put(MediaStore.Images.Media.DATA,origUri.getAbsolutePath());
                            mImageUri= getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
                        }else{
                            mImageUri = Uri.fromFile(origUri);
                        }
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                        startActivityForResult(intent, FLAG_CHOOSE_CAMERA);
                    }
                    break;
                case R.id.item_popupwindows_Photo:
                    intent = new Intent(IDCardUplodActivity.this,
                            PhotoWallActivity.class);
                    startActivity(intent);
                    YMApplication.photoTag = "idcard";
                    YMApplication.isSelectMore = true;
                    break;
                case R.id.item_popupwindows_cancel:
                    menuWindow.dismiss();
                    break;
                default:
                    break;
            }

        }

    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        //相册选择返回数据
        imagelist_jobTitle.clear();
        imagelist_idcard.clear();
        imagelist.clear();
        int code = intent.getIntExtra("code", -1);
        if (code != 100) {
            return;
        }

        ArrayList<String> list1 = new ArrayList<String>();
        list1.clear();
        ArrayList<String> paths = intent.getStringArrayListExtra("paths");
//		CompressImg(paths.get(0));
        // 添加，去重
        boolean hasUpdate = false;
        for (String path : paths) {
            if (path_id.equals("job")) {
                if (!imagePathList.contains(path)) {
                    // 最多5张
                    if (imagePathList.size() == 5) {
                        ToastUtils.shortToast(
                                "最多可添加5张图片");
                        break;
                    }

                    imagelist.add(path);
                    list1.add(path);
                    hasUpdate = true;

                }
            } else if (path_id.equals("idcard")) {
                if (!imagePahtList_idcard.contains(path)) {
                    // 最多2张
                    if (imagePahtList_idcard.size() == 2) {
                        ToastUtils.shortToast(
                                "最多可添加2张图片");
                        break;
                    }

                    imagelist_idcard.add(path);
                    hasUpdate = true;
                    list1.add(path);

                }
            } else if (path_id.equals(JOB_TITLE)) {
                if (!imagePahtList_jobTitle.contains(path)) {
                    // 最多5张
                    if (imagePahtList_jobTitle.size() == 5) {
                        ToastUtils.shortToast(
                                "最多可添加5张图片。");
                        break;
                    }

                    imagelist_jobTitle.add(path);
                    hasUpdate = true;
                    list1.add(path);

                }
            }

        }

        if (hasUpdate) {
            if (path_id.equals("job")) {

                post = new HttpMultipartPost(IDCardUplodActivity.this, list1,
                        CommonUrl.UpdataImgUrl,
                        uiHandler, 100);
                post.execute();
                adapter.notifyDataSetChanged();

            } else if (path_id.equals("idcard")) {
                post = new HttpMultipartPost(IDCardUplodActivity.this, list1,
                        CommonUrl.UpdataImgUrl,
                        uiHandler, 200);
                post.execute();
                adapter2.notifyDataSetChanged();
            } else if (path_id.equals(JOB_TITLE)) {
                post = new HttpMultipartPost(IDCardUplodActivity.this, list1,
                        CommonUrl.UpdataImgUrl,
                        uiHandler, 300);
                post.execute();
                adapter3.notifyDataSetChanged();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //拍照返回数据
        imagelist.clear();
        imagelist_idcard.clear();
        imagelist_jobTitle.clear();
        if (resultCode == RESULT_OK && requestCode == FLAG_CHOOSE_CAMERA) {
            if (path_id.equals("job")) {
                // 最多5张
                if (imagePathList.size() == 5) {
                    ToastUtils.shortToast("最多可添加5张图片");
                    return;
                }
                if (NetworkUtil.isNetWorkConnected()) {
                    List<String> list = new ArrayList<String>();
                    imagelist.add(origUri.getAbsolutePath());
                    list.add(origUri.getAbsolutePath());

                    post = new HttpMultipartPost(IDCardUplodActivity.this,
                            list,
                            CommonUrl.UpdataImgUrl,
                            uiHandler, 100);
                    post.execute();
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtils.shortToast("网络连接失败,图片不能上传,请联网重试");
                }
            } else if (path_id.equals("idcard")) {
                // 最多2张
                if (imagePahtList_idcard.size() == 2) {
                    ToastUtils.shortToast("最多可添加2张图片");
                    return;
                }
                if (NetworkUtil.isNetWorkConnected()) {
                    List<String> list = new ArrayList<String>();
                    imagelist_idcard.add(origUri.getAbsolutePath());
                    list.add(origUri.getAbsolutePath());

                    post = new HttpMultipartPost(IDCardUplodActivity.this,
                            list,
                            CommonUrl.UpdataImgUrl,
                            uiHandler, 200);
                    post.execute();
                    adapter2.notifyDataSetChanged();
                } else {
                    ToastUtils.shortToast("网络连接失败,图片不能上传,请联网重试");
                }
            } else if (path_id.equals(JOB_TITLE)) {
                // 最多5张
                if (imagePahtList_jobTitle.size() == 5) {
                    ToastUtils.shortToast("最多可添加5张图片");
                    return;
                }
                if (NetworkUtil.isNetWorkConnected()) {
                    List<String> list = new ArrayList<String>();
                    imagelist_jobTitle.add(origUri.getAbsolutePath());
                    list.add(origUri.getAbsolutePath());

                    post = new HttpMultipartPost(IDCardUplodActivity.this,
                            list,
                            CommonUrl.UpdataImgUrl,
                            uiHandler, 300);
                    post.execute();
                    adapter3.notifyDataSetChanged();
                } else {
                    ToastUtils.shortToast("网络连接失败,图片不能上传,请联网重试");
                }
            }

        }

    }

    /**
     * 拼接上传图片返回的 图片地址 身份证
     *
     * @return
     */
    public String getListPath(List<String> list) {
        String str = "";
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int j = 0; j < list.size(); j++) {
                sb.append(list.get(j)).append("|");
            }
            // StringUtils.join(stu_list, "");
            if (sb.length() > 0) {
                str = sb.substring(0, sb.lastIndexOf("|"));
            }
        }
        return str;
    }

    /**
     * 拼接上传图片返回的 图片地址 身份证
     *
     * @return
     */
    public String getListPath2(List<String> list) {
        String str = "";
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int j = 0; j < list.size(); j++) {
                sb.append(list.get(j)).append(",");
            }
            // StringUtils.join(stu_list, "");
            if (sb.length() > 0) {
                str = sb.substring(0, sb.lastIndexOf(","));

            }
        }
        return str;
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter3.notifyDataSetChanged();
        adapter2.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
        // if(imagePathList_style!=null&)
        saveImage();

    }


    public void saveImage() {
        // for (int i = 0; i < array.length; i++)
        // {
        //
        // }
        str_job = getListPath(list_job);
        str_idcard = getListPath(list_idcard);
        str_jobTitle = getListPath(list_jobTitle);

        String str_job_path = getListPath2(imagePathList);
        String str_idcard_path = getListPath2(imagePahtList_idcard);
        String str_jobTitle_path = getListPath2(imagePahtList_jobTitle);

        String str_job_save = getListPath2(list_job);
        String str_idcard_save = getListPath2(list_idcard);
        String str_jobTitle_save = getListPath2(list_jobTitle);

        mSharedPreferences.edit().putString("str_job_save", str_job_save).apply();
        mSharedPreferences.edit().putString("str_idcard_save", str_idcard_save).apply();
        mSharedPreferences.edit().putString("str_jobTitle_save", str_jobTitle_save).apply();

        mSharedPreferences.edit().putString("str_job", str_job).apply();
        mSharedPreferences.edit().putString("str_idcard", str_idcard).apply();
        mSharedPreferences.edit().putString("str_jobTitle", str_jobTitle).apply();

        mSharedPreferences.edit().putString("str_job_path", str_job_path).apply();
        mSharedPreferences.edit().putString("str_idcard_path", str_idcard_path).apply();
        mSharedPreferences.edit().putString("str_jobTitle_path", str_jobTitle_path).apply();
    }

    @Override
    protected void onDestroy() {
        saveImage();
        super.onDestroy();
        super.setContentView(R.layout.empty_view);
    }


    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.next_btn:
                showProgressDialog("提交中");
                if (imagePahtList_idcard == null
                        || imagePahtList_idcard.size() == 0) {
                    ToastUtils.longToast("请上传身份证");
                    return;
                }
                if (imagePahtList_idcard.size() != 2) {
                    ToastUtils.longToast("请上传身份证正反两张");
                    return;
                }
                if (imagePathList == null
                        || imagePathList.size() == 0) {
                    ToastUtils.longToast("请上传执业证");
                    return;
                }
                if (Integer.parseInt(mApproveBean.clinic) <= 3) {
                    if (imagePahtList_jobTitle == null
                            || imagePahtList_jobTitle.size() == 0) {
                        ToastUtils.longToast("请上传职称证");
                        return;
                    }
                }

                if (mApproveBean != null) {
                    mApproveBean.shf_image = str_idcard;
                    mApproveBean.zhch_image = str_jobTitle;
                    mApproveBean.zhy_image = str_job;

                    //TODO 提交认证
                    if (NetworkUtil.isNetWorkConnected()) {
                        ToastUtils.shortToast("提交认证");
                        CertificationAboutRequest.getInstance().postCertifyUserInfo(mApproveBean).subscribe(new BaseRetrofitResponse<BaseData>() {
                            @Override
                            public void onNext(BaseData baseData) {
                                super.onNext(baseData);
                                hideProgressDialog();
                                //TODO 认证结果处理
                                if (baseData.getCode() == 10000) {
                                    LoginInfo loginInfo = getLogin1();
                                    if (null != loginInfo.getData()) {
                                        //重置状态 审核中p
                                        loginInfo.getData().setAudit_status(-1);

                                        CheckStateActivity.startActivity(IDCardUplodActivity.this, "checking", "审核中");
                                        YMOtherUtils.backForResult(IDCardUplodActivity.this, null);

                                        //清除缓存
                                        mSharedPreferences.edit().putString("str_job_save", "").apply();
                                        mSharedPreferences.edit().putString("str_idcard_save", "").apply();
                                        mSharedPreferences.edit().putString("str_jobTitle_save", "").apply();

                                        mSharedPreferences.edit().putString("str_job", "").apply();
                                        mSharedPreferences.edit().putString("str_idcard", "").apply();
                                        mSharedPreferences.edit().putString("str_jobTitle", "").apply();

                                        mSharedPreferences.edit().putString("str_job_path", "").apply();
                                        mSharedPreferences.edit().putString("str_idcard_path", "").apply();
                                        mSharedPreferences.edit().putString("str_jobTitle_path", "").apply();
                                    }
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                hideProgressDialog();
                            }
                        });

                    } else {
                        ToastUtils.shortToast("网络连接失败");
                    }
                }


                break;

            case R.id.img_idcard:
                DialogShow(IDCardUplodActivity.this, R.drawable.idcard_big);
                break;
            case R.id.img_job:
                DialogShow(IDCardUplodActivity.this, R.drawable.job_big);
                break;
            default:
                break;
        }
    }

    public void DialogShow(Context context, int resid) {
        @SuppressLint("InflateParams") LinearLayout layout = (LinearLayout) LayoutInflater.from(context)
                .inflate(R.layout.idcard_image, null);
        final Dialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        ImageView head_two = (ImageView) layout
                .findViewById(R.id.img_idcard_show);

        head_two.setBackgroundResource(resid);
    }


}