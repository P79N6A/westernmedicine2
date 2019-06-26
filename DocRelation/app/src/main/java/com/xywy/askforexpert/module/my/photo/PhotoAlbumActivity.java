package com.xywy.askforexpert.module.my.photo;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.model.PhotoAlbumLVItem;
import com.xywy.askforexpert.module.consult.activity.ConsultChatActivity;
import com.xywy.askforexpert.module.doctorcircle.DoctorCircleSendMessageActivty;
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 分相册查看SD卡所有图片。
 * Created by hanj on 14-10-14.
 */
public class PhotoAlbumActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);

        setContentView(R.layout.photo_album);

        TextView titleTV = (TextView) findViewById(R.id.tv_title);

        View cancelBtn = findViewById(R.id.btn2);
        cancelBtn.setVisibility(View.GONE);
        View backBtn = findViewById(R.id.btn1);
        if (!NetworkUtil.isExitsSdcard()) {
            ToastUtils.shortToast("SD卡不可用。");
            backAction();
            return;
        }

        Intent t = getIntent();
        if (!t.hasExtra("latest_count")) {
            backAction();
            return;
        }


        ListView listView = (ListView) findViewById(R.id.select_img_listView);

//        //第一种方式：使用file
//        File rootFile = new File(Utility.getSDcardRoot());
//        //屏蔽/mnt/sdcard/DCIM/.thumbnails目录
//        String ignorePath = rootFile + File.separator + "DCIM" + File.separator + ".thumbnails";
//        getImagePathsByFile(rootFile, ignorePath);

        //第二种方式：使用ContentProvider。（效率更高）
        final ArrayList<PhotoAlbumLVItem> list = new ArrayList<PhotoAlbumLVItem>();
        //“最近照片”
        list.add(new PhotoAlbumLVItem("最近的照片",
                t.getIntExtra("latest_count", -1), t.getStringExtra("latest_first_img")));
        //相册
        list.addAll(getImagePathsByContentProvider());

        PhotoAlbumLVAdapter adapter = new PhotoAlbumLVAdapter(this, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PhotoAlbumActivity.this, PhotoWallActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                //第一行为“最近照片”
                if (position == 0) {
                    intent.putExtra("code", 200);
                } else {
                    intent.putExtra("code", 100);
                    intent.putExtra("folderPath", list.get(position).getPathName());
                }
                startActivity(intent);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消，回到主页面
                backAction();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backAction();
            }
        });
    }

    /**
     * 点击返回时，回到相册页面
     */
    private void backAction() {
        Intent intent = null;
        if (YMApplication.photoTag.equals(Constants.IM_CHAT)) {
            intent = new Intent(PhotoAlbumActivity.this, ConsultChatActivity.class);
        } else if (YMApplication.photoTag.equals(Constants.APPLY_FOR_FAMILY_DOCTOR)) {
            intent = new Intent(PhotoAlbumActivity.this, ApplyForFamilyDoctorActivity.class);
        } else if (YMApplication.photoTag.equals("persyle")) {
            intent = new Intent(PhotoAlbumActivity.this, PersonalStyleActivity.class);
        } else if (YMApplication.photoTag.equals("idcard")) {
            intent = new Intent(PhotoAlbumActivity.this, IDCardUplodActivity.class);
        } else if (YMApplication.photoTag.equals("perinfo")) {
            intent = new Intent(PhotoAlbumActivity.this, PersonInfoActivity.class);
        } else if (YMApplication.photoTag.equals("stu_idcard")) {
            intent = new Intent(PhotoAlbumActivity.this,
                    IDCardUpStuActivity.class);
        } else if (YMApplication.photoTag.equals("approve")) {
            intent = new Intent(PhotoAlbumActivity.this,
                    ApproveInfoActivity.class);
        } else if (YMApplication.photoTag.equals("doctor")) {
            intent = new Intent(PhotoAlbumActivity.this,
                    DoctorCircleSendMessageActivty.class);
        } else if (YMApplication.photoTag.equals("diagon_log")) {
            intent = new Intent(PhotoAlbumActivity.this,
                    DiagnoselogAddEditActiviy.class);
        } else if (YMApplication.photoTag.equals("add_treatment")) {
            intent = new Intent(PhotoAlbumActivity.this,
                    AddTreatmentListLogInfoActivity.class);
        } else if (YMApplication.photoTag.equals("perhonor")) {
            intent = new Intent(PhotoAlbumActivity.this,
                    PersonalHonorActivity.class);
        } else if (YMApplication.photoTag.equals("createEditTopic")) {
            intent = new Intent(PhotoAlbumActivity.this, CreateEditTopicActivity.class);
        } else if (YMApplication.photoTag.equals(Constants.ONLINE_CHAT)) {
            intent = new Intent(PhotoAlbumActivity.this, OnlineChatDetailActivity.class);
        } else if (YMApplication.photoTag.equals(Constants.PATIENT_CHAT)) {
            intent = new Intent(PhotoAlbumActivity.this, PatientChatDetailActivity.class);
        }

//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //重写返回键
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
     * 获取目录中图片的个数。
     */
    private int getImageCount(File folder) {
        int count = 0;
        if (folder != null) {
            File[] files = folder.listFiles();
            for (File file : files) {
                if (isImage(file.getName())) {
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * 获取目录中最新的一张图片的绝对路径。
     */
    private String getFirstImagePath(File folder) {
        if (folder != null) {
            File[] files = folder.listFiles();
            for (int i = files.length - 1; i >= 0; i--) {
                File file = files[i];
                if (isImage(file.getName())) {
                    return file.getAbsolutePath();
                }
            }
        }

        return null;
    }

    public boolean isImage(String name) {
        if (name.contains(".jpg") || name.contains(".png")) {
            return true;
        }
        return true;
    }

    /**
     * 使用ContentProvider读取SD卡所有图片。
     */
    private List<PhotoAlbumLVItem> getImagePathsByContentProvider() {
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String key_MIME_TYPE = MediaStore.Images.Media.MIME_TYPE;
        String key_DATA = MediaStore.Images.Media.DATA;

        ContentResolver mContentResolver = getContentResolver();

        // 只查询jpg和png的图片
        Cursor cursor = mContentResolver.query(mImageUri, new String[]{key_DATA},
                key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=?",
                new String[]{"image/jpg", "image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED);

        ArrayList<PhotoAlbumLVItem> list = null;
        if (cursor != null) {
            if (cursor.moveToLast()) {
                //路径缓存，防止多次扫描同一目录
                HashSet<String> cachePath = new HashSet<>();
                list = new ArrayList<>();

                while (true) {
                    // 获取图片的路径
                    String imagePath = cursor.getString(0);

                    File parentFile = new File(imagePath).getParentFile();
                    String parentPath = parentFile.getAbsolutePath();

                    //不扫描重复路径
                    if (!cachePath.contains(parentPath)) {
                        list.add(new PhotoAlbumLVItem(parentPath, getImageCount(parentFile),
                                getFirstImagePath(parentFile)));
                        cachePath.add(parentPath);
                    }

                    if (!cursor.moveToPrevious()) {
                        break;
                    }
                }
            }

            cursor.close();
        }

        return list;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        //动画
//        overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
    }

    //    /**
//     * 使用File读取SD卡所有图片。
//     */
//    private void getImagePathsByFile(File file, String ignorePath) {
//        if (file.isFile()) {
//            File parentFile = file.getParentFile();
//            String parentFilePath = parentFile.getAbsolutePath();
//
//            if (cachePath.contains(parentFilePath)) {
//                return;
//            }
//
//            if (isImage(file.getName())) {
//                list.add(new SelectImgGVItem(parentFilePath, getImageCount(parentFile),
//                        getFirstImagePath(parentFile)));
//                cachePath.add(parentFilePath);
//            }
//        } else {
//            String absolutePath = file.getAbsolutePath();
//            //屏蔽文件夹
//            if (absolutePath.equals(ignorePath)) {
//                return;
//            }
//
//            //不读取缩略图
//            if (absolutePath.contains("thumb")) {
//                return;
//            }
//
//            //不读取层级超过5的
//            if (Utility.countMatches(absolutePath, File.separator) > 5) {
//                return;
//            }
//
//            //不读取路径包含.的和隐藏文件
//            if (file.getName().contains(".")) {
//                return;
//            }
//
//            File[] childFiles = file.listFiles();
//            if (childFiles != null) {
//                for (File childFile : childFiles) {
//                    getImagePathsByFile(childFile, ignorePath);
//                }
//            }
//        }
//    }
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

        super.onDestroy();
    }
}
