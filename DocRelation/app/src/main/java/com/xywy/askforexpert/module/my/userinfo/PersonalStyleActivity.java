package com.xywy.askforexpert.module.my.userinfo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.util.PathUtil;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.HttpMultipartPost;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.utils.ScreenUtils;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.model.UploadImgInfo;
import com.xywy.askforexpert.module.main.diagnose.adapter.MainGVAdapter;
import com.xywy.askforexpert.module.my.photo.PhotoActivity;
import com.xywy.askforexpert.module.my.photo.PhotoWallActivity;
import com.xywy.askforexpert.widget.SDCardImageLoader;
import com.xywy.askforexpert.widget.view.SelectPicPopupWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 个人风才 （选填）
 *
 * @author 王鹏
 * @2015-5-6下午7:40:33
 */
@Deprecated
public class PersonalStyleActivity extends YMBaseActivity {
    private GridView gv_perstyle;
    public static List<String> pathlist = new ArrayList<String>();
    private List<String> imagelist = new ArrayList<String>();
    private SelectPicPopupWindow menuWindow;
    private MainGVAdapter adapter;
    private LinearLayout main;
    private File origUri;
    public static List<String> list_save = new ArrayList<String>();
    private final static String FILE_SAVEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera";
    /**
     * 相机标志
     */
    private static final int FLAG_CHOOSE_CAMERA = 0x17;
    SharedPreferences sp;
    HttpMultipartPost post;


    public void init_job(String job_path) {
        pathlist.clear();
        String[] strarry;
        if (!job_path.equals("")) {

            strarry = job_path.split(",");
            for (int i = 0; i < strarry.length; i++) {
                pathlist.add(strarry[i]);
            }
        }
        adapter.setData(pathlist);
        adapter.notifyDataSetChanged();
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
        return R.layout.personal_style;
    }

    @Override
    protected void beforeViewBind() {

    }

    @Override
    protected void initView() {
        hideCommonBaseTitle();
        ScreenUtils.initScreen(this);
        gv_perstyle = (GridView) findViewById(R.id.gv_personal_style);
        sp = getSharedPreferences(YMApplication.getLoginInfo().getData().getPid() + "approve", MODE_PRIVATE);
        main = (LinearLayout) findViewById(R.id.main);
        adapter = new MainGVAdapter(PersonalStyleActivity.this, 6);
        adapter.setData(pathlist);
        gv_perstyle.setAdapter(adapter);
        String str_path = sp.getString("str_path", "");
        String str_svae_path = sp.getString("str_save_path", "");
        if (!"".equals(str_path)) {
            init_job(str_path);
            list_save = init_Save(str_svae_path);
        }

        ((TextView) findViewById(R.id.tv_title)).setText("个人风采");
        gv_perstyle.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                if (arg2 == pathlist.size()) {
                    menuWindow = new SelectPicPopupWindow(
                            PersonalStyleActivity.this, itemsOnClick);
                    // 显示窗口
                    menuWindow.showAtLocation(main, Gravity.BOTTOM
                            | Gravity.CENTER_HORIZONTAL, 0, 0);
                    SDCardImageLoader.count = pathlist.size();
                    SDCardImageLoader.img_max = 6;
                } else {
                    Intent intent = new Intent(PersonalStyleActivity.this,
                            PhotoActivity.class);
                    intent.putExtra("statPosition", arg2);
                    intent.putExtra("keytype", "perstyle");
                    startActivity(intent);
                }

            }
        });

    }

    @Override
    protected void initData() {

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
                        list_save.add(upinfo.getData().get(i).getUrl()
                                .toString());
                        pathlist.add(imagelist.get(i));
                    }
                    ToastUtils.shortToast("上传成功");
                    String str_save = getListPath(list_save);
                    YMUserService.getPerInfo().setFengcai(str_save);
                    sp.edit().putString("str_save", str_save).commit();
                    saveImage();
                } else if (code != null && code.equals("-1")) {
                    for (int i = 0; i < upinfo.getData().size(); i++) {
                        if ("0".equals(upinfo.getData().get(i).getCode())) {
                            list_save.add(upinfo.getData().get(i).getUrl()
                                    .toString());
                            pathlist.add(imagelist.get(i));
                        } else {
                            int tag = i + 1;
                            ToastUtils.shortToast( "第" + tag + "张上传错误，请重新上传");
                        }

                    }
                    String str_save = getListPath(list_save);
                    YMUserService.getPerInfo().setFengcai(str_save);
                    sp.edit().putString("str_save", str_save).commit();
                    saveImage();
                }
                adapter.setData(pathlist);
                adapter.notifyDataSetChanged();
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
                    origUri = new File(PathUtil.getInstance().getImagePath(),
                            "osc_" + System.currentTimeMillis() + ".jpg");
                    origUri.getParentFile().mkdirs();
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(origUri));
                    startActivityForResult(intent, FLAG_CHOOSE_CAMERA);
                    // startActivity(intent);
                    break;
                case R.id.item_popupwindows_Photo:
                    intent = new Intent(PersonalStyleActivity.this,
                            PhotoWallActivity.class);
                    startActivity(intent);
                    YMApplication.photoTag = "persyle";
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

    protected void onNewIntent(Intent intent) {

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

            if (!pathlist.contains(path)) {
                imagelist.add(path);
                list1.add(path);
                hasUpdate = true;

            }
        }
        if (hasUpdate) {
            if (NetworkUtil.isNetWorkConnected()) {
                adapter.notifyDataSetChanged();
                post = new HttpMultipartPost(PersonalStyleActivity.this, list1,
                        CommonUrl.UpdataImgUrl,
                        uiHandler, 100);
                post.execute();
            } else {
                ToastUtils.shortToast(
                        "网络连接失败，图片不能上传,请联网重试");
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        imagelist.clear();
        if (requestCode == FLAG_CHOOSE_CAMERA && resultCode == RESULT_OK) {
            if (NetworkUtil.isNetWorkConnected()) {
                imagelist.add(origUri.getPath());
                adapter.notifyDataSetChanged();
                List<String> list = new ArrayList<String>();
                list.add(origUri.getPath());
                post = new HttpMultipartPost(PersonalStyleActivity.this, list,
                        CommonUrl.UpdataImgUrl,
                        uiHandler, 100);
                post.execute();

            } else {
                ToastUtils.shortToast( "网络连接失败,图片不能上传,请联网重试");
            }
        }
    }

    public String getListPath(List<String> list) {
        String str = "";
        StringBuffer sb = new StringBuffer();
        if (list != null && list.size() > 0) {
            for (int j = 0; j < list.size(); j++) {
                sb.append(list.get(j) + "|");
            }
            // StringUtils.join(stu_list, "");
            if (sb.length() > 0) {
                str = sb.substring(0, sb.lastIndexOf("|"));

            }
        }
        return str;
    }

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

    public void saveImage() {
        String str_save = getListPath(list_save);
        String str_save_path = getListPath2(list_save);
        String str_path = getListPath2(pathlist);

        sp.edit().putString("str_save", str_save).commit();
        sp.edit().putString("str_save_path", str_save_path).commit();
        sp.edit().putString("str_path", str_path).commit();
    }

    @Override
    protected void onDestroy() {
        saveImage();
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
        saveImage();
        adapter.notifyDataSetChanged();
    }



    /**
     * 返回监听
     *
     * @param view
     */
    public void onClick_back(View view) {
        finish();

    }
}
