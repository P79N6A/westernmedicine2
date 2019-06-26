package com.xywy.askforexpert.module.my.photo;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.module.consult.activity.ConsultChatActivity;
import com.xywy.askforexpert.module.doctorcircle.topic.CreateEditTopicActivity;
import com.xywy.askforexpert.module.drug.OnlineChatDetailActivity;
import com.xywy.askforexpert.module.main.diagnose.DiagnoselogAddEditActiviy;
import com.xywy.askforexpert.module.main.patient.activity.AddTreatmentListLogInfoActivity;
import com.xywy.askforexpert.module.main.patient.activity.PatientChatDetailActivity;
import com.xywy.askforexpert.module.my.userinfo.ApplyForFamilyDoctorActivity;
import com.xywy.askforexpert.module.my.userinfo.ApproveInfoActivity;
import com.xywy.askforexpert.module.my.userinfo.IDCardUpStuActivity;
import com.xywy.askforexpert.module.my.userinfo.IDCardUplodActivity;
import com.xywy.askforexpert.module.my.userinfo.PersonInfoActivity;
import com.xywy.askforexpert.module.my.userinfo.PersonalHonorActivity;
import com.xywy.askforexpert.module.my.userinfo.PersonalStyleActivity;
import com.xywy.askforexpert.widget.SDCardImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 选择照片页面
 */
public class PhotoWallActivity extends Activity {
    private static final String TAG = "PhotoWallActivity";
    private TextView titleTV;


    private List<String> list;
    private GridView mPhotoWall;
    private PhotoWallAdapter adapter;
    Button confirmBtn;
    Long maxFilesize = 8 * 1024 * 1024L;
    private long check_file = 0l;
    private File file;
    /**
     * 当前文件夹路径
     */
    private String currentFolder = null;
    /**
     * 当前展示的是否为最近照片
     */
    private boolean isLatest = true;
    /**
     * 是否选择了最大值
     */
    private int cocunt;
    private boolean isMax = false;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 400:
                    if (check_file > maxFilesize) {
                        isMax = true;
                        ToastUtils.shortToast("上传图片大小不可超过8M!");
                    } else {
                        isMax = false;
                    }
                    break;
                case 100:
                    if (check_file > maxFilesize) {
                        isMax = true;
                        ToastUtils.shortToast("上传图片大小不可超过8M!");
                    } else {
                        if (SDCardImageLoader.count > SDCardImageLoader.img_max - 1) {
                            ToastUtils.shortToast("最多选择"
                                    + SDCardImageLoader.img_max + "张图片");
                            isMax = true;
                        } else {
                            isMax = false;
                        }
                    }

                    break;
                case 300:
                    List<String> paths = getSelectImagePaths();
                    Intent intent = null;
                    DLog.i("300", "进入" + YMApplication.photoTag);
                    if (YMApplication.photoTag.equals("groupinfo")) {
                        Intent intent1 = new Intent();
                        intent1.putExtra("data", paths.get(0));
                        setResult(Activity.RESULT_OK, intent1);
                        finish();
                        return;
                    }
                    if (YMApplication.photoTag.equals("perinfo")) {
                        intent = new Intent(PhotoWallActivity.this, PersonInfoActivity.class);
                    } else if (YMApplication.photoTag.equals("stu_idcard")) {
                        intent = new Intent(PhotoWallActivity.this, IDCardUpStuActivity.class);
                    } else if (YMApplication.photoTag.equals("approve")) {
                        intent = new Intent(PhotoWallActivity.this, ApproveInfoActivity.class);
                    } else if (YMApplication.photoTag.equals("createEditTopic")) {
                        intent = new Intent(PhotoWallActivity.this, CreateEditTopicActivity.class);
                    } else if (YMApplication.photoTag.equals(Constants.IM_CHAT)) {
                        intent = new Intent(PhotoWallActivity.this, ConsultChatActivity.class);
                    }else if (YMApplication.photoTag.equals(Constants.ONLINE_CHAT)) {
                        intent = new Intent(PhotoWallActivity.this, OnlineChatDetailActivity.class);
                    }else if (YMApplication.photoTag.equals(Constants.PATIENT_CHAT)) {
                        intent = new Intent(PhotoWallActivity.this, PatientChatDetailActivity.class);
                    }
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    DLog.i("300", "进入");
                    intent.putExtra("code", paths != null ? 100 : 101);
                    intent.putStringArrayListExtra("paths", (ArrayList<String>) paths);
                    startActivity(intent);

                default:
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);

        setContentView(R.layout.photo_wall);
        cocunt = SDCardImageLoader.count;
        titleTV = (TextView) findViewById(R.id.tv_title);
        yiquan = "";
        View backBtn = findViewById(R.id.btn1);
        confirmBtn = (Button) findViewById(R.id.btn2);

        mPhotoWall = (GridView) findViewById(R.id.photo_wall_grid);
        list = getLatestImagePaths(100);
        adapter = new PhotoWallAdapter(this, list);
        adapter.init();
        mPhotoWall.setAdapter(adapter);
        if (YMApplication.isSelectMore) {
            confirmBtn.setVisibility(View.VISIBLE);
        } else {
            confirmBtn.setVisibility(View.GONE);
        }
        // 选择照片完成
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 选择图片完成,回到起始页面
                List<String> paths = getSelectImagePaths();
                Intent intent = null;
                if (YMApplication.photoTag.equals(Constants.IM_CHAT)) {
                    intent = new Intent(PhotoWallActivity.this, ConsultChatActivity.class);
                } else if (YMApplication.photoTag.equals(Constants.APPLY_FOR_FAMILY_DOCTOR)) {
                    intent = new Intent(PhotoWallActivity.this, ApplyForFamilyDoctorActivity.class);
                } else if (YMApplication.photoTag.equals("idcard")) {
                    intent = new Intent(PhotoWallActivity.this, IDCardUplodActivity.class);
                } else if (YMApplication.photoTag.equals("persyle")) {
                    intent = new Intent(PhotoWallActivity.this, PersonalStyleActivity.class);
                } else if (YMApplication.photoTag.equals("stu_idcard")) {
                    intent = new Intent(PhotoWallActivity.this, IDCardUpStuActivity.class);
                } else if (YMApplication.photoTag.equals("doctor")) {
                    intent = new Intent();
                    intent.putStringArrayListExtra("paths", (ArrayList<String>) paths);
                    setResult(RESULT_OK, intent);
                    PhotoWallActivity.this.finish();
                    return;
                } else if (YMApplication.photoTag.equals("diagon_log")) {
                    intent = new Intent(PhotoWallActivity.this, DiagnoselogAddEditActiviy.class);
                } else if (YMApplication.photoTag.equals("add_treatment")) {
                    intent = new Intent(PhotoWallActivity.this, AddTreatmentListLogInfoActivity.class);
                } else if (YMApplication.photoTag.equals("perhonor")) {
                    intent = new Intent(PhotoWallActivity.this, PersonalHonorActivity.class);
                }/* else if (YMApplication.photoTag.equals("createEditTopic")) {
                    intent = new Intent();
                    intent.putStringArrayListExtra("paths", paths);
                    setResult(RESULT_OK, intent);
                    PhotoWallActivity.this.finish();
                    return;
                }*/
                if (NetworkUtil.isNetWorkConnected()) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("code", paths != null ? 100 : 101);
                } else {
                    intent.putExtra("code", 101);
                    ToastUtils.shortToast("网络连接失败,图片不能上传,请联网重试");
                }
                intent.putStringArrayListExtra("paths", (ArrayList<String>) paths);
                startActivity(intent);
            }

        });
        mPhotoWall.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                boolean ischek = PhotoWallAdapter.selectionMap.get(arg2);
                adapter.notifyDataSetChanged();
                if (YMApplication.isSelectMore) {
                    if (!ischek) {
                        ++SDCardImageLoader.count;
                        file = new File(list.get(arg2));
                        if (TextUtils.isEmpty(yiquan)) {
                            check_file = check_file + file.length();
                        }
                        DLog.i(TAG, "点击。。" + check_file);
                    } else {
                        --SDCardImageLoader.count;
                        file = new File(list.get(arg2));
                        check_file = check_file - file.length();
                        DLog.i(TAG, "取消点击。。" + check_file);
                    }
                    DLog.i(TAG, "图片内存" + check_file);
                    if (SDCardImageLoader.isMax_check) {
                        if (SDCardImageLoader.count > SDCardImageLoader.img_max) {
                            ToastUtils.shortToast("最多选择" + SDCardImageLoader.img_max + "张图片");
                            --SDCardImageLoader.count;
                            isMax = true;
                        } else {
                            isMax = false;
                        }
                        // handler.sendEmptyMessage(100);
                    } else if (!SDCardImageLoader.isMax_check) {
                    }

                    if (isMax && !ischek) {
                        // handler.sendEmptyMessage(100);
                        return;
                    } else {
                        PhotoWallAdapter.selectionMap.put(arg2, !ischek);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    PhotoWallAdapter.selectionMap.put(arg2, !ischek);
                    handler.sendEmptyMessage(300);
                }

            }
        });
        // 点击返回，回到选择相册页面
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backAction();
            }
        });
    }


    /**
     * 第一次跳转至相册页面时，传递最新照片信息
     */
    private boolean firstIn = true;

    private String yiquan;

    /**
     * 点击返回时，跳转至相册页面
     */
    private void backAction() {
        SDCardImageLoader.count = cocunt;
        Intent intent = new Intent(this, PhotoAlbumActivity.class);

        // 传递“最近照片”分类信息
        if (firstIn) {
            if (list != null && list.size() > 0) {
                intent.putExtra("latest_count", list.size());
                intent.putExtra("latest_first_img", list.get(0));
            }
            firstIn = false;
        }

        startActivity(intent);
        // // 动画
        // overridePendingTransition(R.anim.in_from_left,
        // R.anim.out_from_right);
    }

    // 重写返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backAction();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 根据图片所属文件夹路径，刷新页面
     */
    private void updateView(int code, String folderPath) {
        list.clear();
        adapter.clearSelectionMap();
        adapter.notifyDataSetChanged();

        if (code == 100) { // 某个相册
            int lastSeparator = folderPath.lastIndexOf(File.separator);
            String folderName = folderPath.substring(lastSeparator + 1);
            titleTV.setText(folderName);
            list.addAll(getAllImagePathsByFolder(folderPath));
        } else if (code == 200) { // 最近照片
            titleTV.setText("最近照片");
            list.addAll(getLatestImagePaths(100));
        }

        adapter.notifyDataSetChanged();
        if (list.size() > 0) {
            // 滚动至顶部
            mPhotoWall.smoothScrollToPosition(0);
        }
    }

    /**
     * 获取指定路径下的所有图片文件。
     */
    private List<String> getAllImagePathsByFolder(String folderPath) {
        File folder = new File(folderPath);
        String[] allFileNames = folder.list();
        if (allFileNames == null || allFileNames.length == 0) {
            return null;
        }

        ArrayList<String> imageFilePaths = new ArrayList<String>();
        for (int i = allFileNames.length - 1; i >= 0; i--) {
            if (isImage(allFileNames[i])) {
                imageFilePaths.add(folderPath + File.separator + allFileNames[i]);
            }
        }

        return imageFilePaths;
    }

    public boolean isImage(String name) {
        if (name.contains(".jpg") || name.contains(".png")) {
            return true;
        }
        return true;
    }

    /**
     * 使用ContentProvider读取SD卡最近图片。
     */
    private List<String> getLatestImagePaths(int maxCount) {

        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String key_MIME_TYPE = MediaStore.Images.Media.MIME_TYPE;
        String key_DATA = MediaStore.Images.Media.DATA;

        ContentResolver mContentResolver = getContentResolver();

        // 只查询jpg和png的图片,按最新修改排序
        Cursor cursor = mContentResolver.query(mImageUri,
                new String[]{key_DATA}, key_MIME_TYPE + "=? or "
                        + key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=?",
                new String[]{"image/jpg", "image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED);

        ArrayList<String> latestImagePaths = new ArrayList<>();
        if (cursor != null) {
            // 从最新的图片开始读取.
            // 当cursor中没有数据时，cursor.moveToLast()将返回false
            if (cursor.moveToLast()) {
//                latestImagePaths = new ArrayList<>();
                while (true) {
                    // 获取图片的路径
                    String path = cursor.getString(0);
                    latestImagePaths.add(path);
                    if (latestImagePaths.size() >= maxCount || !cursor.moveToPrevious()) {
                        break;
                    }
                }
            }
            cursor.close();
        }

        return latestImagePaths;
    }

    // 获取已选择的图片路径
    private List<String> getSelectImagePaths() {
        Long filesize = 0l;
        File df;
        SparseBooleanArray map = adapter.getSelectionMap();
        if (map == null) {
            return null;
        }
        if (map.size() == 0) {
            return null;
        }

        ArrayList<String> selectedImageList = new ArrayList<String>();

        for (int i = 0; i < list.size(); i++) {
            if (map.get(i)) {
                selectedImageList.add(list.get(i));
                df = new File(list.get(i));
                if (TextUtils.isEmpty(yiquan)) {
                    filesize = filesize + df.length();
                }
                DLog.i(TAG, "上传的图片大小.." + filesize);
            }
        }

        return selectedImageList;
    }

    // 从相册页面跳转至此页
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // // 动画
        // overridePendingTransition(R.anim.in_from_right,
        // R.anim.out_from_left);

        int code = intent.getIntExtra("code", -1);
        if (code == 100) {
            // 某个相册
            String folderPath = intent.getStringExtra("folderPath");
            if (isLatest
                    || (folderPath != null && !folderPath.equals(currentFolder))) {
                currentFolder = folderPath;
                updateView(100, currentFolder);
                isLatest = false;
            }
        } else if (code == 200) {
            // “最近照片”
            if (!isLatest) {
                updateView(200, null);
                isLatest = true;
            }
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
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
