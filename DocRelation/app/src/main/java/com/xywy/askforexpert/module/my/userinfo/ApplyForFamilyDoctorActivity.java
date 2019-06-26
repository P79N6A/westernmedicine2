package com.xywy.askforexpert.module.my.userinfo;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hyphenate.util.PathUtil;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.activity.WebViewActivity;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.HttpMultipartPost;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.appcommon.utils.PermissionUtils;
import com.xywy.askforexpert.appcommon.utils.ScreenUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.model.HomeDoctorInfo;
import com.xywy.askforexpert.model.UploadImgInfo;
import com.xywy.askforexpert.module.consult.activity.AddOrEditTextInfoActivity;
import com.xywy.askforexpert.module.main.diagnose.adapter.ApplyForFamilyDoctorAdapter;
import com.xywy.askforexpert.module.my.clinic.FamDoctorMoneyActivity;
import com.xywy.askforexpert.module.my.clinic.FamServerTimeActivity;
import com.xywy.askforexpert.module.my.photo.PhotoActivity;
import com.xywy.askforexpert.module.my.photo.PhotoWallActivity;
import com.xywy.askforexpert.widget.SDCardImageLoader;
import com.xywy.askforexpert.widget.view.SelectPicPopupWindow;
import com.xywy.base.view.ProgressDialog;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

import static com.xywy.askforexpert.R.id.tv_goodat;
import static com.xywy.askforexpert.appcommon.old.Constants.STR_SHOW_SAVE;


/**
 * 家庭医生申请 stone
 */
public class ApplyForFamilyDoctorActivity extends YMBaseActivity {

    @Bind(R.id.scrollView)
    ScrollView scrollView;
    @Bind(R.id.idcard_main)
    LinearLayout idcardMain;
    @Bind(R.id.rl_price)
    RelativeLayout rlPrice;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.rl_time)
    RelativeLayout rlTime;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.rl_hope)
    RelativeLayout rlHope;
    @Bind(R.id.tv_hope)
    TextView tvHope;
    @Bind(R.id.rl_goodat)
    RelativeLayout rlGoodat;
    @Bind(tv_goodat)
    TextView tvGoodat;
    @Bind(R.id.rl_honor)
    RelativeLayout rlHonor;
    @Bind(R.id.tv_honor)
    TextView tvHonor;
    @Bind(R.id.show)
    RecyclerView show;
    @Bind(R.id.style)
    RecyclerView style;
    @Bind(R.id.next_btn)
    TextView nextBtn;
    @Bind(R.id.iv_protocol)
    ImageView iv_protocol;
    @Bind(R.id.tv_protocol)
    TextView tv_protocol;
    @Bind(R.id.tv55)
    TextView tv55;
    @Bind(R.id.tv88)
    TextView tv88;

    //上传成功后返回的图片地址 用|连接
    private String str_show;
    private String str_style;
    //本地图片路径 用,连接
    private String str_show_path;
    private String str_style_path;
    //上传成功后返回的图片地址 用,连接
    private String str_show_save;
    private String str_style_save;

    private ApplyForFamilyDoctorAdapter adapter;
    private ApplyForFamilyDoctorAdapter adapter2;

    //荣誉展示
    //图片文件路径 上传成功后的图片地址
    public static List<String> imagePathList_show = new ArrayList<String>();
    //图片文件路径 上传前选择的图片地址
    private List<String> imagelist_show = new ArrayList<String>();
    //个人风采
    public static List<String> imagePathList_style = new ArrayList<String>();
    private List<String> imagelist_style = new ArrayList<String>();
    //上传图片返回的图片url
    public static List<String> list_show = new ArrayList<String>();
    public static List<String> list_style = new ArrayList<String>();

    private SelectPicPopupWindow menuWindow;
    private File origUri;
    private String path_id = "";
    /**
     * 相机标志
     */
    private static final int FLAG_CHOOSE_CAMERA = 18;
    private HttpMultipartPost post;


    private SharedPreferences mSharedPreferences;


    private HomeDoctorInfo homeoc;
    //    private Map<String, String> map = new HashMap<String, String>();
    private String fam_stat = YMApplication.getLoginInfo().getData().getXiaozhan().getFamily();

    private boolean mProtocolSelected = true;//默认选中

    private List<String> list00 = new ArrayList<String>();//存0-20的索引
    private List<List<String>> choose_list00 = new ArrayList<List<String>>();//21个 一周七天 一天分上午中午下午
    private List<String> day00 = new ArrayList<String>();//周几的集合

    private boolean mIsRequested;


    public void init_show(String path) {
        imagePathList_show.clear();
        String[] strarry;
        if (!path.equals("")) {

            strarry = path.split(",");
            for (int i = 0; i < strarry.length; i++) {
                imagePathList_show.add(strarry[i]);
            }
        }
        adapter.setData(imagePathList_show);
        adapter.notifyDataSetChanged();
    }

    public void init_style(String path) {
        imagePathList_style.clear();
        String[] strarry;
        strarry = path.split(",");
        for (int i = 0; i < strarry.length; i++) {
            imagePathList_style.add(strarry[i]);
        }
        adapter2.setData(imagePathList_style);
        adapter2.notifyDataSetChanged();
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
        return R.layout.apply_for_family_doctor_activity;
    }

    @Override
    protected void beforeViewBind() {
        //stone 共享文件名是 用户的唯一标志 pid
        mSharedPreferences = getSharedPreferences(YMApplication.getLoginInfo().getData().getPid(), MODE_PRIVATE);
        ScreenUtils.initScreen(this);

    }

    @Override
    protected void initView() {
        hideCommonBaseTitle();
        ((TextView) findViewById(R.id.tv_title)).setText("家庭医生服务申请");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        show.setLayoutManager(linearLayoutManager);
        style.setLayoutManager(linearLayoutManager2);

        adapter = new ApplyForFamilyDoctorAdapter(this, imagePathList_show, 10);
        adapter2 = new ApplyForFamilyDoctorAdapter(this, imagePathList_style, 10);

        show.setAdapter(adapter);
        style.setAdapter(adapter2);


        SpannableStringBuilder spannable = new SpannableStringBuilder(getString(R.string.familydoctor_protocol));
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#00c8aa")), 7, getString(R.string.familydoctor_protocol).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new TextClick(), 7, getString(R.string.familydoctor_protocol).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_protocol.setMovementMethod(LinkMovementMethod.getInstance());
        tv_protocol.setText(spannable);

        iv_protocol.setSelected(mProtocolSelected);
    }

    private class TextClick extends ClickableSpan {
        @Override
        public void onClick(View widget) {
            //在此处理点击事件
            WebViewActivity.start(ApplyForFamilyDoctorActivity.this, getString(R.string.familydoctor_protocol_title), Constants.FAMILY_PROTOCOL_URL);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            // ds.setColor(Color.parseColor("#00c8aa")); //文字的颜色
            // ds.setColor(ds.linkColor); //文字的颜色
            // ds.setUnderlineText(true); //是否设置下划线，true表示设置。
        }
    }

    @Override
    protected void initData() {
        //首先给清空,否则切换账号有问题 stone
        imagePathList_show.clear();
        imagePathList_style.clear();
        list_show.clear();
        list_style.clear();


        String show_path = mSharedPreferences.getString(Constants.STR_SHOW_PATH, "");
        String style_path = mSharedPreferences.getString(Constants.STR_STYLE_PATH, "");

        if (!TextUtils.isEmpty(show_path)) {
            init_show(show_path);
            list_show = init_Save(mSharedPreferences.getString(Constants.STR_SHOW_SAVE, ""));
        }
        if (!TextUtils.isEmpty(style_path)) {
            init_style(style_path);
            list_style = init_Save(mSharedPreferences.getString(Constants.STR_STYLE_SAVE, ""));
        }

        adapter.setOnItemClickListener(new ApplyForFamilyDoctorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (position == imagePathList_show.size()) {
                    path_id = Constants.HONOR_SHOW;
                    menuWindow = new SelectPicPopupWindow(
                            ApplyForFamilyDoctorActivity.this, itemsOnClick);

                    // 显示窗口
                    menuWindow.showAtLocation(idcardMain, Gravity.BOTTOM
                            | Gravity.CENTER_HORIZONTAL, 0, 0);
                    //添加遮罩 stone
                    YMOtherUtils.addScreenBg(menuWindow, ApplyForFamilyDoctorActivity.this);

                    SDCardImageLoader.count = imagePathList_show.size();
                    SDCardImageLoader.img_max = 10;
                } else {
                    Intent intent = new Intent(ApplyForFamilyDoctorActivity.this,
                            PhotoActivity.class);
                    intent.putExtra("statPosition", position);
                    intent.putExtra("keytype", Constants.HONOR_SHOW);
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        adapter2.setOnItemClickListener(new ApplyForFamilyDoctorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (position == imagePathList_style.size()) {
                    path_id = Constants.PERSONAL_STYLE;
                    menuWindow = new SelectPicPopupWindow(
                            ApplyForFamilyDoctorActivity.this, itemsOnClick);

                    // 显示窗口
                    menuWindow.showAtLocation(idcardMain, Gravity.BOTTOM
                            | Gravity.CENTER_HORIZONTAL, 0, 0);
                    //添加遮罩 stone
                    YMOtherUtils.addScreenBg(menuWindow, ApplyForFamilyDoctorActivity.this);

                    SDCardImageLoader.count = imagePathList_style.size();
                    SDCardImageLoader.img_max = 10;
                } else {
                    Intent intent = new Intent(ApplyForFamilyDoctorActivity.this,
                            PhotoActivity.class);
                    intent.putExtra("statPosition", position);
                    intent.putExtra("keytype", Constants.PERSONAL_STYLE);
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });


        if (NetworkUtil.isNetWorkConnected()) {
            getData();
        } else {
            ToastUtils.shortToast("网络连接失败");
        }
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
                        //上传成功 添加图片网络地址到集合
                        list_show.add(upinfo.getData().get(i).getUrl().toString());
                        //上传成功,添加本地图片地址到集合
                        imagePathList_show.add(imagelist_show.get(i));
                    }

                } else if (code != null && "-1".equals(code)) {
                    for (int i = 0; i < upinfo.getData().size(); i++) {
                        if ("0".equals(upinfo.getData().get(i).getCode())) {
                            list_show.add(upinfo.getData().get(i).getUrl()
                                    .toString());
                            imagePathList_show.add(imagelist_show.get(i));

                        } else if ("-1".equals(upinfo.getData().get(i)
                                .getCode())) {

                            int tag = i + 1;
                            ToastUtils.shortToast("第" + tag
                                    + "张上传错误，请重新上传");
                        }
                    }
                }
                adapter.setData(imagePathList_show);
                adapter.notifyDataSetChanged();
                saveImage();
                break;
            case 200:
                UploadImgInfo upinfo2 = (UploadImgInfo) msg.obj;
                code = upinfo2.getCode();
                if (code != null && code.equals("0")) {
                    for (int i = 0; i < upinfo2.getData().size(); i++) {
                        list_style.add(upinfo2.getData().get(i).getUrl().toString());
                        imagePathList_style.add(imagelist_style.get(i));
                    }
                    ToastUtils.shortToast("上传成功");
                } else if (code != null && "-1".equals(code)) {
                    for (int i = 0; i < upinfo2.getData().size(); i++) {
                        if ("0".equals(upinfo2.getData().get(i).getCode())) {
                            list_style.add(upinfo2.getData().get(i).getUrl()
                                    .toString());
                            imagePathList_style.add(imagelist_style.get(i));

                        } else if ("-1".equals(upinfo2.getData().get(i)
                                .getCode())) {

                            int tag = i + 1;
                            ToastUtils.shortToast("第" + tag
                                    + "张上传错误，请重新上传");
                        }
                    }

                }
                adapter2.setData(imagePathList_style);
                adapter2.notifyDataSetChanged();
                saveImage();
                break;
            case 400:
                adapter.setData(imagePathList_show);
                adapter.notifyDataSetChanged();
                adapter2.setData(imagePathList_style);
                adapter2.notifyDataSetChanged();
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
                    if (!PermissionUtils.checkPermission(ApplyForFamilyDoctorActivity.this, Manifest.permission.CAMERA)) {
                        CommonUtils.permissionRequestDialog(ApplyForFamilyDoctorActivity.this, "请先授予照相机(Camera)权限", 555);
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
                    intent = new Intent(ApplyForFamilyDoctorActivity.this,
                            PhotoWallActivity.class);
                    startActivity(intent);
                    YMApplication.photoTag = Constants.APPLY_FOR_FAMILY_DOCTOR;
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
        imagelist_show.clear();
        imagelist_style.clear();
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
            if (path_id.equals(Constants.HONOR_SHOW)) {
                if (!imagePathList_show.contains(path)) {
                    // 最多10张
                    if (imagePathList_show.size() == 10) {
                        ToastUtils.shortToast(
                                "最多可添加10张图片");
                        break;
                    }

                    imagelist_show.add(path);
                    list1.add(path);
                    hasUpdate = true;

                }
            } else if (path_id.equals(Constants.PERSONAL_STYLE)) {
                if (!imagePathList_style.contains(path)) {
                    // 最多2张
                    if (imagePathList_style.size() == 10) {
                        ToastUtils.shortToast(
                                "最多可添加10张图片");
                        break;
                    }

                    imagelist_style.add(path);
                    hasUpdate = true;
                    list1.add(path);

                }
            }

        }

        if (hasUpdate) {
            if (path_id.equals(Constants.HONOR_SHOW)) {

                post = new HttpMultipartPost(ApplyForFamilyDoctorActivity.this, list1,
                        CommonUrl.UpdataImgUrl,
                        uiHandler, 100);
                post.execute();
                adapter.notifyDataSetChanged();

            } else if (path_id.equals(Constants.PERSONAL_STYLE)) {
                post = new HttpMultipartPost(ApplyForFamilyDoctorActivity.this, list1,
                        CommonUrl.UpdataImgUrl,
                        uiHandler, 200);
                post.execute();
                adapter2.notifyDataSetChanged();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //拍照返回数据
        if (resultCode == RESULT_OK && requestCode == FLAG_CHOOSE_CAMERA) {
            imagelist_show.clear();
            imagelist_style.clear();
            if (path_id.equals(Constants.HONOR_SHOW)) {
                // 最多10张
                if (imagePathList_show.size() == 10) {
                    ToastUtils.shortToast("最多可添加10张图片");
                    return;
                }
                if (NetworkUtil.isNetWorkConnected()) {
                    List<String> list = new ArrayList<String>();
                    imagelist_show.add(origUri.getAbsolutePath());
                    list.add(origUri.getAbsolutePath());

                    post = new HttpMultipartPost(ApplyForFamilyDoctorActivity.this,
                            list,
                            CommonUrl.UpdataImgUrl,
                            uiHandler, 100);
                    post.execute();
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtils.shortToast("网络连接失败,图片不能上传,请联网重试");
                }
            } else if (path_id.equals(Constants.PERSONAL_STYLE)) {
                // 最多10张
                if (imagePathList_style.size() == 10) {
                    ToastUtils.shortToast("最多可添加10张图片");
                    return;
                }
                if (NetworkUtil.isNetWorkConnected()) {
                    List<String> list = new ArrayList<String>();
                    imagelist_style.add(origUri.getAbsolutePath());
                    list.add(origUri.getAbsolutePath());

                    post = new HttpMultipartPost(ApplyForFamilyDoctorActivity.this,
                            list,
                            CommonUrl.UpdataImgUrl,
                            uiHandler, 200);
                    post.execute();
                    adapter2.notifyDataSetChanged();
                } else {
                    ToastUtils.shortToast("网络连接失败,图片不能上传,请联网重试");
                }
            }

        } else if ((requestCode == Constants.REQUESTCODE_GOOD_AT_FOR_FAMILY_DOCTOR && resultCode == RESULT_OK)) {
            if (data != null) {
                String str = data.getStringExtra(Constants.KEY_VALUE);
                if (!TextUtils.isEmpty(str.trim())) {
                    YMApplication.famdocinfo.setSpecial(str);
                    tvGoodat.setText(str);
                }
            }
        } else if ((requestCode == Constants.REQUESTCODE_PERSONAL_HONOR && resultCode == RESULT_OK)) {
            if (data != null) {
                String str = data.getStringExtra(Constants.KEY_VALUE);
                if (!TextUtils.isEmpty(str.trim())) {
                    tv88.setVisibility(View.GONE);
                    YMApplication.famdocinfo.setHonor(str);
                    tvHonor.setText(str);
                }
            }
        } else if ((requestCode == Constants.REQUESTCODE_DOCTOR_HOPE && resultCode == RESULT_OK)) {
            if (data != null) {
                String str = data.getStringExtra(Constants.KEY_VALUE);
                if (!TextUtils.isEmpty(str.trim())) {
                    YMApplication.famdocinfo.setWords(str);
                    tvHope.setText(str);
                }
            }
        } else if ((requestCode == Constants.REQUESTCODE_SERVICE_TIME && resultCode == RESULT_OK)) {
            handleServieTime();
        } else if ((requestCode == Constants.REQUESTCODE_SERVICE_PRICE && resultCode == RESULT_OK)) {
            tvPrice.setText("包周" + YMApplication.famdocinfo.getWeek() + " 包月" + YMApplication.famdocinfo.getMonth());
        }

    }

    private void handleServieTime() {
        List<String> days = YMApplication.famdocinfo.getDay();

        if (days != null && days.size() >= 4) {
            tv55.setVisibility(View.GONE);
            StringBuilder str = new StringBuilder("");
            int len = days.size();
            for (int i = 0; i < len; i++) {
                switch (Integer.parseInt(days.get(i))) {
                    case 1:
                        str.append("周一 ");
                        break;
                    case 2:
                        str.append("周二 ");
                        break;
                    case 3:
                        str.append("周三 ");
                        break;
                    case 4:
                        str.append("周四 ");
                        break;
                    case 5:
                        str.append("周五 ");
                        break;
                    case 6:
                        str.append("周六 ");
                        break;
                    case 7:
                        str.append("周日 ");
                        break;
                }
            }
            tvTime.setText(str);
        } else {
            tv55.setVisibility(View.VISIBLE);
            tvTime.setText("");
        }
    }

    /**
     * 拼接上传图片返回的 图片地址
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
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int j = 0; j < list.size(); j++) {
                sb.append(list.get(j)).append(",");
            }
            if (sb.length() > 0) {
                str = sb.substring(0, sb.lastIndexOf(","));

            }
        }
        return str;
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        adapter2.notifyDataSetChanged();

        saveImage();
    }


    public void saveImage() {
        if (mIsRequested) {
            return;
        }

        str_show = getListPath(list_show);
        str_style = getListPath(list_style);

        str_show_path = getListPath2(imagePathList_show);
        str_style_path = getListPath2(imagePathList_style);

        str_show_save = getListPath2(list_show);
        str_style_save = getListPath2(list_style);

        mSharedPreferences.edit().putString(Constants.STR_SHOW_SAVE, str_show_save).apply();
        mSharedPreferences.edit().putString(Constants.STR_STYLE_SAVE, str_style_save).apply();

        mSharedPreferences.edit().putString(Constants.STR_SHOW, str_show).apply();
        mSharedPreferences.edit().putString(Constants.STR_STYLE, str_style).apply();

        mSharedPreferences.edit().putString(Constants.STR_SHOW_PATH, str_show_path).apply();
        mSharedPreferences.edit().putString(Constants.STR_STYLE_PATH, str_style_path).apply();
    }

    @Override
    protected void onDestroy() {
        saveImage();
        super.onDestroy();
        super.setContentView(R.layout.empty_view);
    }


    public void onClick_back(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.iv_protocol:
                mProtocolSelected = !mProtocolSelected;
                iv_protocol.setSelected(mProtocolSelected);
                break;
            case R.id.rl_price:
                intent = new Intent(this,
                        FamDoctorMoneyActivity.class);
                int week_min = 10;
                int week_max = 5000;
                int month_min = 30;
                int month_max = 9999;
                if (homeoc != null && homeoc.getData() != null && homeoc.getData().getWeek_price() != null) {
                    week_min = (int) Float.parseFloat(homeoc.getData().getWeek_price().getMin());
                    week_max = (int) Float.parseFloat(homeoc.getData().getWeek_price().getMax());
                    month_min = (int) Float.parseFloat(homeoc.getData().getMonth_price().getMin());
                    month_max = (int) Float.parseFloat(homeoc.getData().getMonth_price().getMax());
                }
                intent.putExtra("week_min", week_min);
                intent.putExtra("week_max", week_max);
                intent.putExtra("month_min", month_min);
                intent.putExtra("month_max", month_max);
                intent.putExtra(Constants.KEY_VALUE, true);
                startActivityForResult(intent, Constants.REQUESTCODE_SERVICE_PRICE);
                break;
            case R.id.rl_time:
                //时间
//                if (fam_stat.equals(Constants.FUWU_AUDIT_STATUS_1)) {
//                    intent = new Intent(this,
//                            FamDoctorTimeShowActivity.class);
//                    intent.putExtra("type", "fam");
//                    startActivity(intent);
//                } else {
                intent = new Intent(this,
                        FamServerTimeActivity.class);
                intent.putExtra(Constants.KEY_VALUE, true);
                startActivityForResult(intent, Constants.REQUESTCODE_SERVICE_TIME);
//                }
                break;
            case R.id.rl_hope:
                //医生寄语
                AddOrEditTextInfoActivity.startDoctorHopeActivityForResult(this, YMApplication.famdocinfo.getWords(), !fam_stat.equals(Constants.FUWU_AUDIT_STATUS_1));
                break;
            case R.id.rl_goodat:
                //擅长疾病
                AddOrEditTextInfoActivity.startGoodAtForFamilyDoctorActivityForResult(this, YMApplication.famdocinfo.getSpecial(), !fam_stat.equals(Constants.FUWU_AUDIT_STATUS_1));
                break;
            case R.id.rl_honor:
                //个人荣誉
                AddOrEditTextInfoActivity.startPersonalHonorActivityForResult(this, YMApplication.famdocinfo.getHonor(), !fam_stat.equals(Constants.FUWU_AUDIT_STATUS_1));
                break;
            case R.id.next_btn:

                String words = YMApplication.famdocinfo.getWords();
                String times = YMApplication.famdocinfo.getTime();
                String month = YMApplication.famdocinfo.getMonth();
                String week = YMApplication.famdocinfo.getWeek();
                String special = YMApplication.famdocinfo.getSpecial();
                String honor = YMApplication.famdocinfo.getHonor();

                if (TextUtils.isEmpty(week)) {
                    ToastUtils.shortToast("请编辑包周价格");
                } else if (TextUtils.isEmpty(month)) {
                    ToastUtils.shortToast("请编辑包月价格");
                } else if (TextUtils.isEmpty(times)) {
                    ToastUtils.shortToast("请编辑服务时间");
                } else if (TextUtils.isEmpty(words)) {
                    ToastUtils.shortToast("请编辑医生寄语");
                } else if (TextUtils.isEmpty(special)) {
                    ToastUtils.shortToast("请编辑擅长疾病");
                } else if (!mProtocolSelected) {
                    ToastUtils.shortToast("请阅读并同意家庭医生规范条例");
                } else {
                    if (NetworkUtil.isNetWorkConnected()) {
                        //stone 更多服务
                        StatisticalTools.eventCount(this, Constants.SUBMITANAPPLICATION);

                        sendData(YMApplication.famdocinfo.getWords(),
                                YMApplication.famdocinfo.getTime(),
                                YMApplication.famdocinfo.getMonth(),
                                YMApplication.famdocinfo.getWeek(),
                                YMApplication.famdocinfo.getSpecial(), honor, str_show, str_style);
                    } else {
                        ToastUtils.shortToast("网络连接失败");
                    }
                }
                break;
            default:
                break;
        }

    }


    /**
     * @param words   医生寄语
     * @param time    接电话时间
     * @param week    包周
     * @param special 擅长
     * @param month
     */
    public void sendData(String words, String time, String month, String week,
                         String special, String honor, String honorpic, String stylepic) {
        String userid = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(userid + Constants.MD5_KEY);

        AjaxParams params = new AjaxParams();
        params.put("uid", userid);
        params.put("func", "form_exec");
        params.put("sign", sign);
        params.put("words", words);
        params.put("time", time);
        params.put("month", month);
        params.put("week", week);
        params.put("special", special);
        params.put("honor", honor);
        params.put("honorpic", honorpic);
        params.put("stylepic", stylepic);

        FinalHttp fh = new FinalHttp();

        fh.post(CommonUrl.MyClinic_Fam_Url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                HomeDoctorInfo info = GsonUtils.toObj(t.toString(), HomeDoctorInfo.class);
                if (info != null && !TextUtils.isEmpty(info.getCode()) && info.getCode().equals("0")) {

                    //清除缓存
                    mSharedPreferences.edit().putString(Constants.STR_SHOW, "").apply();
                    mSharedPreferences.edit().putString(Constants.STR_SHOW_PATH, "").apply();
                    mSharedPreferences.edit().putString(STR_SHOW_SAVE, "").apply();

                    mSharedPreferences.edit().putString(Constants.STR_STYLE, "").apply();
                    mSharedPreferences.edit().putString(Constants.STR_STYLE_PATH, "").apply();
                    mSharedPreferences.edit().putString(Constants.STR_STYLE_SAVE, "").apply();

                    mIsRequested = true;

                    ToastUtils.shortToast(
                            "您的服务申请已经提交，我们将在24小时内审核，请耐心等待！");
                    YMApplication.getLoginInfo().getData().getXiaozhan().setFamily(Constants.FUWU_AUDIT_STATUS_0);
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }


    public void getData() {
        final ProgressDialog dialog = new ProgressDialog(this, "正在加载中...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.showProgersssDialog();
        String userid = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(userid + Constants.MD5_KEY);

        AjaxParams params = new AjaxParams();
        params.put("uid", userid);
        params.put("func", "form_exec");
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();

        fh.post(CommonUrl.MyClinic_Fam_Url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                Gson gson = new Gson();
                homeoc = gson.fromJson(t.toString(), HomeDoctorInfo.class);
//                if (fam_stat.equals(Constants.FUWU_AUDIT_STATUS_1)) {
                fuzhi();
                putData();
//                }
                dialog.dismiss();
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                dialog.dismiss();
                ToastUtils.longToast("网络繁忙,请稍后重试");
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    private void putData() {
        if (!TextUtils.isEmpty(YMApplication.famdocinfo.getHonor())) {
            tv88.setVisibility(View.GONE);
            tvHonor.setText(YMApplication.famdocinfo.getHonor());
        }
        if (!TextUtils.isEmpty(YMApplication.famdocinfo.getSpecial())) {
            tvGoodat.setText(YMApplication.famdocinfo.getSpecial());
        }
        if (!TextUtils.isEmpty(YMApplication.famdocinfo.getWords())) {
            tvHope.setText(YMApplication.famdocinfo.getWords());
        }
        if (!TextUtils.isEmpty(YMApplication.famdocinfo.getWeek()) && !TextUtils.isEmpty(YMApplication.famdocinfo.getMonth())) {
            tvPrice.setText("包周" + YMApplication.famdocinfo.getWeek() + " 包月" + YMApplication.famdocinfo.getMonth());
        }
        handleServieTime();
    }

    public void fuzhi() {
        //stone 判空
        if (homeoc != null
                && homeoc.getData() != null) {
            if (!TextUtils.isEmpty(homeoc.getData().getWords())) {
                YMApplication.famdocinfo.setWords(homeoc.getData().getWords());
                tvHope.setText(YMApplication.famdocinfo.getWords());
            }
            if (!TextUtils.isEmpty(homeoc.getData().getSpecial())) {
                YMApplication.famdocinfo.setSpecial(homeoc.getData().getSpecial());
                tvGoodat.setText(YMApplication.famdocinfo.getSpecial());
            }
            if (!TextUtils.isEmpty(homeoc.getData().getHonor())) {
                YMApplication.famdocinfo.setHonor(homeoc.getData().getHonor());
                tvHonor.setText(YMApplication.famdocinfo.getHonor());
            }
            if (homeoc.getData().getHonorpic() != null && homeoc.getData().getHonorpic().size() > 0) {
                int len = homeoc.getData().getHonorpic().size();
                StringBuilder s = new StringBuilder("");
                for (int i = 0; i < len; i++) {
//                    imagePathList_show.add(homeoc.getData().getHonorpic().get(i).pic);
                    s.append(homeoc.getData().getHonorpic().get(i).pic);
                    if (i != len - 1) {
                        s.append("|");
                    }
                }
//                adapter.setData(imagePathList_show);
//                adapter.notifyDataSetChanged();
                YMApplication.famdocinfo.setHonorPic(s.toString());
//                str_show = s.toString();
            }
            if (homeoc.getData().getStylepic() != null && homeoc.getData().getStylepic().size() > 0) {
                int len = homeoc.getData().getStylepic().size();
                StringBuilder s = new StringBuilder("");
                for (int i = 0; i < len; i++) {
//                    imagePathList_style.add(homeoc.getData().getStylepic().get(i).pic);
                    s.append(homeoc.getData().getStylepic().get(i).pic);
                    if (i != len - 1) {
                        s.append("|");
                    }
                }
//                adapter2.setData(imagePathList_style);
//                adapter2.notifyDataSetChanged();
                YMApplication.famdocinfo.setStylePic(s.toString());
//                str_style = s.toString();
            }

            if (homeoc.getData().getProductinfo() != null && homeoc.getData().getProductinfo().size() >= 2) {
                if (!TextUtils.isEmpty(homeoc.getData().getProductinfo().get(0).getCategory())
                        && homeoc.getData().getProductinfo().get(0).getCategory()
                        .equals("1")) {
                    YMApplication.famdocinfo.setWeek(homeoc.getData()
                            .getProductinfo().get(0).getPrice());
                    YMApplication.famdocinfo.setMonth(homeoc.getData()
                            .getProductinfo().get(1).getPrice());
                } else {
                    YMApplication.famdocinfo.setWeek(homeoc.getData()
                            .getProductinfo().get(1).getPrice());
                    YMApplication.famdocinfo.setMonth(homeoc.getData()
                            .getProductinfo().get(0).getPrice());
                }

                if (!TextUtils.isEmpty(YMApplication.famdocinfo.getWeek()) && !TextUtils.isEmpty(YMApplication.famdocinfo.getMonth())) {
                    tvPrice.setText("包周" + YMApplication.famdocinfo.getWeek() + " 包月" + YMApplication.famdocinfo.getMonth());
                }
            }

            if (homeoc.getData().getOnlinetime() != null) {


                for (int i = 0; i < 21; i++) {
                    list00.add("");
                    choose_list00.add(new ArrayList<String>());
                }

                StringBuilder days = new StringBuilder("");

                List<Map<String, String>> list = new ArrayList<Map<String, String>>();
                Map<String, String> map = new HashMap<String, String>();
                map.put("day_week", "星期一：");
                map.put("day_time", getWeek_map(homeoc.getData().getOnlinetime()
                        .getMonday()));

                if (homeoc.getData().getOnlinetime()
                        .getMonday() != null && homeoc.getData().getOnlinetime()
                        .getMonday().size() > 0) {
                    days.append("周一 ");


                    initList00(1, homeoc.getData().getOnlinetime().getMonday());
                }

                Map<String, String> map1 = new HashMap<String, String>();
                map1.put("day_week", "星期二：");
                map1.put("day_time", getWeek_map(homeoc.getData().getOnlinetime()
                        .getTuesday()));

                if (homeoc.getData().getOnlinetime()
                        .getTuesday() != null && homeoc.getData().getOnlinetime()
                        .getTuesday().size() > 0) {
                    days.append("周二 ");

                    initList00(2, homeoc.getData().getOnlinetime().getTuesday());
                }

                Map<String, String> map2 = new HashMap<String, String>();
                map2.put("day_week", "星期三：");
                map2.put("day_time", getWeek_map(homeoc.getData().getOnlinetime()
                        .getWednesday()));

                if (homeoc.getData().getOnlinetime()
                        .getWednesday() != null && homeoc.getData().getOnlinetime()
                        .getWednesday().size() > 0) {
                    days.append("周三 ");

                    initList00(3, homeoc.getData().getOnlinetime().getWednesday());
                }

                Map<String, String> map3 = new HashMap<String, String>();
                map3.put("day_week", "星期四：");
                map3.put("day_time", getWeek_map(homeoc.getData().getOnlinetime()
                        .getThursday()));

                if (homeoc.getData().getOnlinetime()
                        .getThursday() != null && homeoc.getData().getOnlinetime()
                        .getThursday().size() > 0) {
                    days.append("周四 ");

                    initList00(4, homeoc.getData().getOnlinetime().getThursday());
                }

                Map<String, String> map4 = new HashMap<String, String>();
                map4.put("day_week", "星期五：");
                map4.put("day_time", getWeek_map(homeoc.getData().getOnlinetime()
                        .getFriday()));

                if (homeoc.getData().getOnlinetime()
                        .getFriday() != null && homeoc.getData().getOnlinetime()
                        .getFriday().size() > 0) {
                    days.append("周五 ");

                    initList00(5, homeoc.getData().getOnlinetime().getFriday());
                }

                Map<String, String> map5 = new HashMap<String, String>();
                map5.put("day_week", "星期六：");
                map5.put("day_time", getWeek_map(homeoc.getData().getOnlinetime()
                        .getSaturday()));

                if (homeoc.getData().getOnlinetime()
                        .getSaturday() != null && homeoc.getData().getOnlinetime()
                        .getSaturday().size() > 0) {
                    days.append("周六 ");

                    initList00(6, homeoc.getData().getOnlinetime().getSaturday());
                }

                Map<String, String> map6 = new HashMap<String, String>();
                map6.put("day_week", "星期日：");
                map6.put("day_time", getWeek_map(homeoc.getData().getOnlinetime()
                        .getSunday()));

                if (homeoc.getData().getOnlinetime()
                        .getSunday() != null && homeoc.getData().getOnlinetime()
                        .getSunday().size() > 0) {
                    days.append("周日 ");

                    initList00(7, homeoc.getData().getOnlinetime().getSunday());
                }

                list.add(map);
                list.add(map1);
                list.add(map2);
                list.add(map3);
                list.add(map4);
                list.add(map5);
                list.add(map6);

                YMApplication.famdocinfo.setList(list);
                YMApplication.famdocinfo.setDate_list(list00);
                YMApplication.famdocinfo.setChoolse_list(choose_list00);
                YMApplication.famdocinfo.setDay(day00);
                YMApplication.famdocinfo.setTime(str());

                if (!TextUtils.isEmpty(days)) {
                    tv55.setVisibility(View.GONE);
                    tvTime.setText(days);
                }
            }
        }
    }

    private void initList00(int number, List<HomeDoctorInfo> day) {
        day00.add(String.valueOf(number));

        int length = day.size();
        for (int i = 0; i < length; i++) {
            //早 中 下
            if (Integer.parseInt(day.get(i).getEnd()) <= 12) {
                list00.set(0 + (number - 1) * 3, day.get(i).getStart() + "时 - " + day.get(i).getEnd() + "时");

                choose_list00.get(0 + (number - 1) * 3).add(number + "_" + day.get(i).getStart());
                choose_list00.get(0 + (number - 1) * 3).add(number + "_" + day.get(i).getEnd());
            } else if (Integer.parseInt(day.get(i).getEnd()) <= 18) {
                list00.set(1 + (number - 1) * 3, day.get(i).getStart() + "时 - " + day.get(i).getEnd() + "时");

                choose_list00.get(1 + (number - 1) * 3).add(number + "_" + day.get(i).getStart());
                choose_list00.get(1 + (number - 1) * 3).add(number + "_" + day.get(i).getEnd());
            } else {
                list00.set(2 + (number - 1) * 3, day.get(i).getStart() + "时 - " + day.get(i).getEnd() + "时");

                choose_list00.get(2 + (number - 1) * 3).add(number + "_" + day.get(i).getStart());
                choose_list00.get(2 + (number - 1) * 3).add(number + "_" + day.get(i).getEnd());
            }
        }
    }

    public String str() {
        String str = "";
        List<String> list = list();
        StringBuffer sb = new StringBuffer();

        for (int j = 0; j < list.size(); j++) {
            sb.append(list.get(j) + ",");
        }
        if (sb.length() > 0) {
            str = sb.substring(0, sb.lastIndexOf(","));
        }

        return str;
    }

    public List<String> list() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < choose_list00.size(); i++) {
            if (choose_list00.get(i).size() > 0) {
                for (int j = 0; j < choose_list00.get(i).size(); j++) {
                    list.add(choose_list00.get(i).get(j));
                }
            }
        }
        return list;
    }

    public String getWeek_map(List<HomeDoctorInfo> week_day) {
        if (week_day == null) {
            return "";
        }
        String str = "";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < week_day.size(); i++) {

            sb.append(week_day.get(i).getStart() + "时－"
                    + week_day.get(i).getEnd() + "时" + ",");
        }
        if (sb.length() > 0) {
            str = sb.substring(0, sb.lastIndexOf(","));
        }
        return str;
    }

}