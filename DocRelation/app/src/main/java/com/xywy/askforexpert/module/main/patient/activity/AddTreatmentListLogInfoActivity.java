package com.xywy.askforexpert.module.main.patient.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.hyphenate.util.PathUtil;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.HttpMultipartPost;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ScreenUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.model.UploadImgInfo;
import com.xywy.askforexpert.module.main.patient.adapter.AddMainGVAdapter;
import com.xywy.askforexpert.module.my.photo.PhotoActivity;
import com.xywy.askforexpert.module.my.photo.PhotoWallActivity;
import com.xywy.askforexpert.widget.SDCardImageLoader;
import com.xywy.askforexpert.widget.view.MyGridView;
import com.xywy.askforexpert.widget.view.SelectPicPopupWindow;
import com.xywy.base.view.ProgressDialog;
import com.xywy.uilibrary.titlebar.ItemClickListener;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 添加 新的 诊疗纪录
 *
 * @author 王鹏
 * @2015-5-25下午2:19:21
 */
public class AddTreatmentListLogInfoActivity extends YMBaseActivity {

    private final static String FILE_SAVEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera";
    /**
     * 相机标志
     */
    private static final int FLAG_CHOOSE_CAMERA = 0x17;
    public static List<String> imagePathList = new ArrayList<String>();
    public static List<String> stu_list = new ArrayList<String>();
    public boolean isTtiaozhuan;
    private EditText edit_title;
    private File origUri;
    private String path_id;
    private LinearLayout main;
    private SelectPicPopupWindow menuWindow;
    private List<String> imagelist = new ArrayList<String>();
    private AddMainGVAdapter adapter;
    private MyGridView gridView;
    private Map<String, String> map = new HashMap<String, String>();
    private String treatmentid;
    private HttpMultipartPost post;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 100:
                    if (map.get("code").equals("0")) {
                        ToastUtils.shortToast(
                                "修改成功");
                        finish();
                    } else {
                        ToastUtils.shortToast(
                                map.get("msg"));
                    }
                    break;
                case 200:
                    UploadImgInfo upinfo = (UploadImgInfo) msg.obj;
                    String code = upinfo.getCode();
                    if (code != null && code.equals("1")) {
                        ToastUtils.shortToast(
                                "上传成功");
                        for (int i = 0; i < upinfo.getData().size(); i++) {
                            stu_list.add(upinfo.getData().get(i).getUrl());
                            imagePathList.add(imagelist.get(i));
                        }

                    }
                    adapter.setData(imagePathList);
                    adapter.maxsize = imagePathList.size() + 1;
                    adapter.notifyDataSetChanged();
                    break;

                default:
                    break;
            }
        }
    };
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
                    // String origFileName = "osc_" + timeStamp + ".jpg";
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
                    intent = new Intent(AddTreatmentListLogInfoActivity.this,
                            PhotoWallActivity.class);
                    startActivity(intent);
                    YMApplication.photoTag = "add_treatment";
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

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.add_treatment_list);
//
//        ScreenUtils.initScreen(this);
//        treatmentid = getIntent().getStringExtra("treatmentid");
//        edit_title = (EditText) findViewById(R.id.edit_title);
//        gridView = (MyGridView) findViewById(R.id.gv_shwo);
//        int num;
//        if (TextUtils.isEmpty(imagePathList.size() + "")) {
//            num = 1;
//        } else {
//            num = imagePathList.size() + 1;
//        }
//
//        adapter = new AddMainGVAdapter(gridView, AddTreatmentListLogInfoActivity.this, num);
//        main = (LinearLayout) findViewById(R.id.main);
//        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
//        adapter.setData(imagePathList);
////		adapter.isAddAllShow=true;
//        gridView.setAdapter(adapter);
//        gridView.setCacheColorHint(0);
//        ((TextView) findViewById(R.id.tv_title)).setText("添加病程记录");
//        gridView.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//                                    long arg3) {
//
//                if (arg2 == imagePathList.size()) {
//                    path_id = "job";
//                    menuWindow = new SelectPicPopupWindow(
//                            AddTreatmentListLogInfoActivity.this, itemsOnClick);
//                    // 显示窗口
//                    menuWindow.showAtLocation(main, Gravity.BOTTOM
//                            | Gravity.CENTER_HORIZONTAL, 0, 0);
//                    SDCardImageLoader.count = imagePathList.size();
//                    SDCardImageLoader.img_max = 9;
//                    SDCardImageLoader.isMax_check = false;
//                } else {
//                    Intent intent = new Intent(
//                            AddTreatmentListLogInfoActivity.this,
//                            PhotoActivity.class);
//                    intent.putExtra("statPosition", arg2);
//                    intent.putExtra("keytype", "add_treatment");
//                    startActivity(intent);
//                    isTtiaozhuan = true;
//                }
//            }
//        });
//    }

    @Override
    protected int getLayoutResId() {
        return R.layout.add_treatment_list;
    }
    @Override
    protected void initView() {
        ScreenUtils.initScreen(this);
        treatmentid = getIntent().getStringExtra("treatmentid");
        edit_title = (EditText) findViewById(R.id.edit_title);
        gridView = (MyGridView) findViewById(R.id.gv_shwo);
        int num;
        if (TextUtils.isEmpty(imagePathList.size() + "")) {
            num = 1;
        } else {
            num = imagePathList.size() + 1;
        }
        adapter = new AddMainGVAdapter(gridView, AddTreatmentListLogInfoActivity.this, num);
        main = (LinearLayout) findViewById(R.id.main);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter.setData(imagePathList);
//		adapter.isAddAllShow=true;
        gridView.setAdapter(adapter);
        gridView.setCacheColorHint(0);
//        ((TextView) findViewById(R.id.tv_title)).setText("添加病程记录");
        titleBarBuilder.setTitleText("添加病程记录");
        titleBarBuilder.addItem("", R.drawable.sure, new ItemClickListener() {
            @Override
            public void onClick() {
                String str_edit = edit_title.getText().toString().trim();
                String imgs = getListPath(stu_list);
                if (!TextUtils.isEmpty(str_edit) & str_edit != null & !str_edit.equals("") || !"".equals(imgs)) {
                    getData(treatmentid, str_edit, imgs);
                } else {
                    ToastUtils.shortToast(
                            "请为患者填写病程记录");
                }
            }
        }).build();
        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                if (arg2 == imagePathList.size()) {
                    path_id = "job";
                    menuWindow = new SelectPicPopupWindow(
                            AddTreatmentListLogInfoActivity.this, itemsOnClick);
                    // 显示窗口
                    menuWindow.showAtLocation(main, Gravity.BOTTOM
                            | Gravity.CENTER_HORIZONTAL, 0, 0);
                    SDCardImageLoader.count = imagePathList.size();
                    SDCardImageLoader.img_max = 9;
                    SDCardImageLoader.isMax_check = false;
                } else {
                    Intent intent = new Intent(
                            AddTreatmentListLogInfoActivity.this,
                            PhotoActivity.class);
                    intent.putExtra("statPosition", arg2);
                    intent.putExtra("keytype", "add_treatment");
                    startActivity(intent);
                    isTtiaozhuan = true;
                }
            }
        });
    }

    @Override
    protected void initData() {
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
            if (!imagePathList.contains(path)) {

                imagelist.add(path);
                list1.add(path);
                hasUpdate = true;

            }
        }

        if (hasUpdate) {

            adapter.notifyDataSetChanged();
            post = new HttpMultipartPost(AddTreatmentListLogInfoActivity.this,
                    list1,
                    "http://api.imgupload.xywy.com/upload.php?from=yixian",
                    handler, 200);
            post.isYixian = true;
            post.execute();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        imagelist.clear();
        if (requestCode == FLAG_CHOOSE_CAMERA && resultCode == RESULT_OK) {
            List<String> list1 = new ArrayList<String>();
            imagelist.add(origUri.getPath());
            list1.add(origUri.getPath());
            adapter.notifyDataSetChanged();
            post = new HttpMultipartPost(AddTreatmentListLogInfoActivity.this,
                    list1,
                    "http://api.imgupload.xywy.com/upload.php?from=yixian",
                    handler, 200);
            post.isYixian = true;
            post.execute();

        }
    }

//    public void onClick_back(View view) {
//        switch (view.getId()) {
//            case R.id.btn1:
//                finish();
//                break;
//            case R.id.btn2:
//                String str_edit = edit_title.getText().toString().trim();
//                String imgs = getListPath(stu_list);
//                if (!TextUtils.isEmpty(str_edit) & str_edit != null & !str_edit.equals("") || !"".equals(imgs)) {
//                    getData(treatmentid, str_edit, imgs);
//                } else {
//                    ToastUtils.shortToast(
//                            "请为患者填写病程记录");
//                }
//                break;
//            default:
//                break;
//        }
//    }

    public void getData(String treatmentid, String content, String imgs) {
        // String group_name1=URLEncoder.encode(group_name, "utf-8");
        final ProgressDialog dialog = new ProgressDialog(this, "正在加载中...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.showProgersssDialog();
        String did = YMApplication.getLoginInfo().getData().getPid();
        String bind = did + treatmentid;
        Long st = System.currentTimeMillis();

        String sign = MD5Util.MD5(st + bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("a", "chat");
        params.put("m", "treatDetailEdit");

        params.put("timestamp", st + "");
        params.put("bind", bind);
        params.put("content", content);
        params.put("did", did);
        params.put("treatmentid", treatmentid);
        params.put("imgs", imgs);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.Patient_Manager_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                        map = ResolveJson.R_Action(t.toString());
                        handler.sendEmptyMessage(100);
                        super.onSuccess(t);
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                        super.onFailure(t, errorNo, strMsg);
                    }
                });


    }

    /**
     * 拼接上传图片返回的 图片地址
     *
     * @return
     */
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

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (adapter != null) {

            adapter.setData(imagePathList);
//			adapter.notifyDataSetChanged();
            gridView.setAdapter(adapter);

        }
        StatisticalTools.onResume(AddTreatmentListLogInfoActivity.this);

    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        if (gridView != null) {
            gridView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        if (isTtiaozhuan) {
            gridView.setVisibility(View.GONE);
            isTtiaozhuan = false;
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub

        super.onStop();
//		if(isTtiaozhuan)
//		{
//			YMApplication.Trace("是否隐藏了");
//			gridView.setVisibility(View.GONE);
//			isTtiaozhuan=false;
//		}


    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if (imagePathList != null) {
            if (imagePathList.size() > 0) {
                stu_list.clear();
                imagePathList.clear();
            }
        }
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub

        StatisticalTools.onPause(AddTreatmentListLogInfoActivity.this);
        super.onPause();
    }
}
