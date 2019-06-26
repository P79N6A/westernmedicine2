package com.xywy.askforexpert.module.doctorcircle.topic;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DensityUtils;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.appcommon.utils.others.WeakHandler;
import com.xywy.askforexpert.model.topics.TopicType;
import com.xywy.askforexpert.module.my.photo.PhotoWallActivity;
import com.xywy.askforexpert.module.my.userinfo.ClipPictureActivity;
import com.xywy.askforexpert.module.my.userinfo.PersonInfoActivity;
import com.xywy.askforexpert.widget.SDCardImageLoader;
import com.xywy.askforexpert.widget.view.SelectPicPopupWindow;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xywy.askforexpert.appcommon.old.Constants.CREATE_TOPIC;
import static com.xywy.askforexpert.appcommon.old.Constants.EDIT_TOPIC;

/**
 * 创建/编辑话题
 *
 * @author Jack Fang
 */
public class CreateEditTopicActivity extends AppCompatActivity {

    public static final String MODE_KEY = "topicType";
    public static final String TOPIC_ID_KEY = "topicId";
    public static final String TOPIC_NAME_KEY = "topicName";
    public static final String TOPIC_COVER_KEY = "topicCoverPath";
    public static final String TOPIC_TYPE_KEY = "topicTypes";
    public static final String TOPIC_INTRO_KEY = "topicIntro";
    private static final String TAG = CreateEditTopicActivity.class.getSimpleName();
    private static final String CREATE_EDIT_TOPIC_PARAM_A = "theme";
    private static final String CREATE_EDIT_TOPIC_PARAM_M = "theme_add";
    /**
     * 最多可选择的话题分类的个数
     */
    private static final int MAX_CHOICE_COUNT = 2;
    private static final int MIN_TOPIC_NAME_EMS = 2;
    private static final int MAX_TOPIC_NAME_EMS = 10;
    private static final int MIN_TOPIC_INTRO_EMS = 10;
    private static final int MAX_TOPIC_INTRO_EMS = 200;
    private static final int HANDLER_UPDATE_TOPIC_TYPE = 0;
    private static final int HANDLER_UPDATE_TOPIC_COVER = 1;
    /**
     * 截取结束标志
     */
    private static final int FLAG_MODIFY_FINISH = 7;
    /**
     * 本地图片选取标志
     */
    private static final int FLAG_CHOOSE_IMG = 0x11;
    /**
     * 相机标志
     */
    private static final int FLAG_CHOOSE_CAMERA = 0x17;
    /**
     * 显示话题分类选择对话框时相关参数
     */
    private static final String DIALOG_TITLE = "选择你的话题分类";
    private static final String TOPIC_TYPE_PARAM_M = "theme_category";
    private static final String TOPIC_TYPE_PARAM_A = "theme";
    private static boolean showDialog;
    @Bind(R.id.create_edit_topic_content_page)
    CoordinatorLayout contentPage;
    @Bind(R.id.create_edit_tool_title)
    TextView createEditToolTitle;
    @Bind(R.id.create_edit_toolbar)
    Toolbar createEditToolbar;
    @Bind(R.id.topic_name_input)
    EditText topicNameInput;
    @Bind(R.id.topic_edit_cover)
    ImageView topicEditCover;
    @Bind(R.id.topic_edit_type)
    TextView topicEditType;
    @Bind(R.id.topic_edit_intro)
    EditText topicEditIntro;
    /**
     * 区分创建/编辑话题，默认为创建话题
     */
    private int type = CREATE_TOPIC;
    private String topicId = "";
    private String topicName;
    private String topicCoverPath;
    private List<TopicType.TopicTypeBean> topicTypes;
    private String topicIntro;
    /**
     * 所有话题分类
     */
    private List<TopicType.TopicTypeBean> topicTypeBeanList = new ArrayList<>();
    /**
     * 选中的话题分类
     */
    private List<TopicType.TopicTypeBean> selectedTopicTypes = new ArrayList<>();
    /**
     * 所有话题分类名称
     */
    private CharSequence[] topicTypeNames = null;
    private String userId;
    private SelectPicPopupWindow selectPicPopupWindow;
    private File origUri;
    private FinalBitmap bitmap;
    private List<String> selectedTopicCoverPath = new ArrayList<>();
    private WeakHandler mHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_UPDATE_TOPIC_TYPE:
                    if (selectedTopicTypes != null) {
                        String s = "";
                        for (int i = 0; i < selectedTopicTypes.size(); i++) {
                            s += selectedTopicTypes.get(i).getName() + "  ";
                        }
                        topicEditType.setText(s.trim().equals("") ? "选择分类" : s.trim());
                        topicEditType.setTextColor(s.trim().equals("") ? Color.parseColor("#999999") : Color.parseColor("#333333"));
                    }
                    break;

                case HANDLER_UPDATE_TOPIC_COVER:
                    if (!selectedTopicCoverPath.isEmpty()) {
                        DLog.d(TAG, "cover path = " + selectedTopicCoverPath.get(0));
                        bitmap.display(topicEditCover, selectedTopicCoverPath.get(0));
                    }
                    break;
            }

            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_topic);
        ButterKnife.bind(this);
        CommonUtils.initSystemBar(this);

        CommonUtils.setToolbar(this, createEditToolbar);

        if (YMUserService.isGuest()) {
            userId = "0";
        } else {
            userId = YMApplication.getPID();
        }

        bitmap = FinalBitmap.create(this, false);

        getParams();
        setToolbarTitle();
        initData();
        requestTopicType();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    /**
     * 获取编辑话题时传递的数据
     */
    private void getParams() {
        type = getIntent().getIntExtra(MODE_KEY, CREATE_TOPIC);
        topicId = getIntent().getStringExtra(TOPIC_ID_KEY);
        topicName = getIntent().getStringExtra(TOPIC_NAME_KEY);
        topicCoverPath = getIntent().getStringExtra(TOPIC_COVER_KEY);
        topicTypes = getIntent().getParcelableArrayListExtra(TOPIC_TYPE_KEY);
        topicIntro = getIntent().getStringExtra(TOPIC_INTRO_KEY);
    }

    private void setToolbarTitle() {
        switch (type) {
            case CREATE_TOPIC:
                createEditToolTitle.setText(getResources().getString(R.string.create_topic));
                topicNameInput.setClickable(true);
                topicNameInput.setFocusable(true);
                break;

            case EDIT_TOPIC:
                createEditToolTitle.setText(getResources().getString(R.string.edit_topic));
                topicNameInput.setClickable(false);
                topicNameInput.setFocusable(false);
                break;

            default:
                createEditToolTitle.setText(getResources().getString(R.string.create_topic));
                topicNameInput.setClickable(true);
                topicNameInput.setFocusable(true);
                break;
        }
    }

    /**
     * 初始化页面数据
     */
    private void initData() {
        if (type == EDIT_TOPIC) {
            if (topicName != null) {
                topicNameInput.setText(topicName);
            }

            if (topicCoverPath != null && !TextUtils.isEmpty(topicCoverPath)) {
                if (bitmap == null) {
                    bitmap = FinalBitmap.create(this, false);
                }
                DLog.d(TAG, "topicCoverPath = " + topicCoverPath);
                bitmap.display(topicEditCover, topicCoverPath);
                selectedTopicCoverPath.add(topicCoverPath);
            }

            if (topicTypes != null && !topicTypes.isEmpty()) {
                selectedTopicTypes.addAll(topicTypes);
                String s = "";
                for (int i = 0; i < topicTypes.size(); i++) {
                    s += topicTypes.get(i).getName() + "  ";
                }
                topicEditType.setText(s.trim());
                topicEditType.setTextColor(Color.parseColor("#333333"));
            }

            if (topicIntro != null) {
                topicEditIntro.setText(topicIntro);
                topicEditIntro.setSelection(topicEditIntro.getText().length());
            }
        } else if (type == CREATE_TOPIC) {
            if (topicName != null) {
                topicNameInput.setText(topicName);
                topicNameInput.setSelection(topicName.length());
            }
        }
    }

    /**
     * 请求话题分类
     */
    private void requestTopicType() {
        String url = CommonUrl.FOLLOW_LIST + "m=" + TOPIC_TYPE_PARAM_M + "&a=" + TOPIC_TYPE_PARAM_A;
        DLog.d(TAG, "topic type request url = " + url);
        new FinalHttp().get(url, new AjaxCallBack() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);

                DLog.d(TAG, "topic type request success");
                DLog.d(TAG, "topic type s = " + s);

                TopicType topicType = new Gson().fromJson(s, TopicType.class);
                if (topicType != null && topicType.getList() != null) {
                    topicTypeBeanList.addAll(topicType.getList());
                }
                if (!topicTypeBeanList.isEmpty()) {
                    if (topicTypeNames != null && topicTypeNames.length > 0) {
                        topicTypeNames = null;
                    }
                    topicTypeNames = new CharSequence[topicTypeBeanList.size()];
                    for (int i = 0; i < topicTypeBeanList.size(); i++) {
                        topicTypeNames[i] = topicTypeBeanList.get(i).getName();
                    }

                    if (showDialog) {
                        showDialog = false;
                        showTopicTypeChoiceDialog();
                    }
                }
            }
        });
    }

    /**
     * 显示话题分类选择框
     */
    private void showTopicTypeChoiceDialog() {
        boolean[] checkItems = null;
        if (topicTypeNames != null && topicTypeNames.length > 0) {
            checkItems = new boolean[topicTypeNames.length];
            for (int i = 0; i < topicTypeNames.length; i++) {
                checkItems[i] = false;
            }
        }
        if (selectedTopicTypes != null) {
            for (int i = 0; i < selectedTopicTypes.size(); i++) {
                int index = topicTypeBeanList.indexOf(selectedTopicTypes.get(i));
                DLog.d(TAG, "index = " + index);
                if (checkItems != null && checkItems.length >= index && index >= 0) {
                    checkItems[index] = true;
                }
            }
        }
        final CusMultiChoiceDialog fragment = CusMultiChoiceDialog.newInstance(DIALOG_TITLE, topicTypeNames,
                checkItems, true, MAX_CHOICE_COUNT);
        fragment.setOnUpdateSelectedItems(new UpdateSelectedItems() {
            @Override
            public void updateSelectedItems() {
                List<Integer> selectedItems = fragment.getSelectedItems();
                if (!selectedTopicTypes.isEmpty()) {
                    selectedTopicTypes.clear();
                }
                if (!selectedItems.isEmpty()) {
                    for (int i = 0; i < selectedItems.size(); i++) {
                        if (selectedTopicTypes.size() < MAX_CHOICE_COUNT) {
                            selectedTopicTypes.add(topicTypeBeanList.get(selectedItems.get(i)));
                        } else {
                            return;
                        }
                    }
                }
                DLog.d(TAG, "selectedTopicTypes size = " + selectedTopicTypes.size());
                // 更新页面显示的话题分类
                mHandler.sendEmptyMessage(HANDLER_UPDATE_TOPIC_TYPE);
            }
        });
        fragment.show(getSupportFragmentManager(), TAG);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_edit_topic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        if (id == R.id.action_create_edit_done) {
            if (YMUserService.isGuest()) {
                DialogUtil.LoginDialog(new YMOtherUtils(this).context);
            } else {
                createEditDone();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 创建/编辑话题完成
     */
    private void createEditDone() {
        final String topicName = topicNameInput.getText().toString().trim();
        if (topicName.contains("#")) {
            Toast.makeText(this, "话题标题不能含有#", Toast.LENGTH_SHORT).show();
            return;
        }
        if (topicName.length() < MIN_TOPIC_NAME_EMS || topicName.length() > MAX_TOPIC_NAME_EMS) {
            Toast.makeText(this, "输入话题标题" + MIN_TOPIC_NAME_EMS + "~" + MAX_TOPIC_NAME_EMS + "字",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String topicTypeIds = "";
        if (selectedTopicTypes != null && !selectedTopicTypes.isEmpty()) {
            for (int i = 0; i < selectedTopicTypes.size(); i++) {
                topicTypeIds += selectedTopicTypes.get(i).getId() + ",";
            }
            topicTypeIds = topicTypeIds.substring(0, topicTypeIds.length() - 1);
        }
        if (topicTypeIds.equals("")) {
            Toast.makeText(this, "至少选择一个话题分类", Toast.LENGTH_SHORT).show();
            return;
        }

        String topicIntro = topicEditIntro.getText().toString().trim();
        if (topicIntro.equals("")) {
            Toast.makeText(this, "请填写话题简介", Toast.LENGTH_SHORT).show();
            return;
        }
        if (topicIntro.length() < MIN_TOPIC_INTRO_EMS) {
            Toast.makeText(this, "话题简介不能少于" + MIN_TOPIC_INTRO_EMS + "字", Toast.LENGTH_SHORT).show();
            return;
        } else if (topicIntro.length() > MAX_TOPIC_INTRO_EMS) {
            Toast.makeText(this, "话题简介太长了，不能大于" + MAX_TOPIC_INTRO_EMS + "字", Toast.LENGTH_SHORT).show();
            return;
        }

        String topicCover = "";
        if (selectedTopicCoverPath != null && !selectedTopicCoverPath.isEmpty()) {
            topicCover = selectedTopicCoverPath.get(0);
        }

        DLog.d(TAG, "create edit done:\ntopicIntro = " + topicIntro + "\ntopicImg = " + topicCover);
        String sign = MD5Util.MD5(userId + Constants.MD5_KEY);
        String bind = userId;
        FinalHttp request = new FinalHttp();
        AjaxParams params = new AjaxParams();
        params.put("img", topicCover);
        params.put("description", topicIntro);
        params.put("catid", topicTypeIds);
        params.put("theme", topicName);
        params.put("themeid", topicId);
        params.put("a", CREATE_EDIT_TOPIC_PARAM_A);
        params.put("userid", userId);
        params.put("m", CREATE_EDIT_TOPIC_PARAM_M);
        params.put("sign", sign);
        params.put("bind", bind);
        request.post(CommonUrl.FOLLOW_LIST, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);

                DLog.d(TAG, "create edit topic s = " + s);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jsonObject == null) {
                    Toast.makeText(CreateEditTopicActivity.this, getResources().getString(R.string.server_error),
                            Toast.LENGTH_SHORT).show();
                } else {
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");
                    int id = jsonObject.optInt("id");
                    if (code == 0) {
                        DLog.d(TAG, "type = " + type);
                        showToast(true);
                        Intent intent = new Intent();
                        intent.putExtra("topicName", topicName);
                        intent.putExtra("topicId", String.valueOf(id));
                        setResult(RESULT_OK, intent);
                        CreateEditTopicActivity.this.finish();
                    } else if (code == 30205) {
                        String message;
                        if (YMApplication.isDoctor()) {
                            message = getString(R.string.doctor_not_all_infors);
                        } else {
                            message = getString(R.string.doctor_student_not_all_infors);
                        }
                        CommonUtils.showCommonDialog(CreateEditTopicActivity.this, null, message,
                                "去完善", "取消", PersonInfoActivity.class, null);
                    } else if (code == 20000) {
                        Toast.makeText(CreateEditTopicActivity.this, "此话题已创建，请更换话题",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        showToast(false);
                    }
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);

                showToast(false);
            }
        });
    }

    @OnClick({R.id.topic_edit_cover, R.id.topic_edit_type})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.topic_edit_cover:
                View currentFocus = this.getCurrentFocus();
                DLog.d(TAG, "currentFocus = " + (currentFocus == null));
                if (currentFocus != null) {
                    InputMethodManager imm = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                }
                SDCardImageLoader.count = 0;
                SDCardImageLoader.img_max = 1;
                // 选择封面
                showCameraPopup();
                break;

            case R.id.topic_edit_type:
                // 选择话题分类对话框
                if (topicTypeNames == null || topicTypeNames.length == 0) {
                    if (NetworkUtil.isNetWorkConnected()) {
                        if (!showDialog) {
                            Toast.makeText(this, "正在为您获取话题分类，请稍等……", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "正在为您获取话题分类，请稍等……", Toast.LENGTH_SHORT).show();
                            showDialog = true;
                            requestTopicType();
                        }
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.no_network),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showTopicTypeChoiceDialog();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case FLAG_CHOOSE_IMG:
                    if (data != null) {
                        Uri uri = data.getData();
                        if (!TextUtils.isEmpty(uri.getAuthority())) {
                            Cursor cursor = getContentResolver().query(uri,
                                    new String[]{MediaStore.Images.Media.DATA},
                                    null, null, null);
                            if (null == cursor) {
                                Toast.makeText(getApplicationContext(), "图片没找到", Toast.LENGTH_SHORT)
                                        .show();
                                return;
                            }
                            cursor.moveToFirst();
                            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                            cursor.close();

                            Intent intent = new Intent(this, ClipPictureActivity.class);
                            intent.putExtra("path", path);
                            startActivityForResult(intent, FLAG_MODIFY_FINISH);
                        } else {
                            Intent intent = new Intent(this, ClipPictureActivity.class);
                            intent.putExtra("path", uri.getPath());
                            startActivityForResult(intent, FLAG_MODIFY_FINISH);
                        }
                    }
                    break;

                case FLAG_CHOOSE_CAMERA:
                    Intent intent = new Intent(this, ClipPictureActivity.class);
                    intent.putExtra("path", origUri.getPath());
                    startActivityForResult(intent, FLAG_MODIFY_FINISH);
                    break;

                case FLAG_MODIFY_FINISH:
                    if (data != null) {
                        final String path = data.getStringExtra("path");

                        if (!selectedTopicCoverPath.isEmpty()) {
                            selectedTopicCoverPath.clear();
                        }

                        selectedTopicCoverPath.add(path);

                        mHandler.sendEmptyMessage(HANDLER_UPDATE_TOPIC_COVER);
                    }
                    break;
            }
        }
    }

    private void showCameraPopup() {
        // 打开相机
        // 打开相册 自定义
        selectPicPopupWindow = new SelectPicPopupWindow(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicPopupWindow.dismiss();
                switch (v.getId()) {
                    case R.id.item_popupwindows_camera:// 打开相机
                        origUri = CommonUtils.startCamera(CreateEditTopicActivity.this, FLAG_CHOOSE_CAMERA);
                        break;

                    case R.id.item_popupwindows_Photo:// 打开相册 自定义
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (ContextCompat.checkSelfPermission(CreateEditTopicActivity.this,
                                    Manifest.permission.READ_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {
                                CommonUtils.permissionRequestDialog(CreateEditTopicActivity.this,
                                        "无法打开相册，请授予内存(Storage)权限", 123);
                            } else {
                                Intent intent = new Intent(CreateEditTopicActivity.this, PhotoWallActivity.class);
                                startActivity(intent);
                                YMApplication.photoTag = "createEditTopic";
                                YMApplication.isSelectMore = false;
                            }
                        } else {
                            Intent intent = new Intent(CreateEditTopicActivity.this, PhotoWallActivity.class);
                            startActivity(intent);
                            YMApplication.photoTag = "createEditTopic";
                            YMApplication.isSelectMore = false;
                        }
                        break;
                }
            }
        });
        CommonUtils.backgroundAlpha(this, 0.5f);
        // 显示窗口
        if ("R7Plus".equals(Build.MODEL)) {
            selectPicPopupWindow.showAtLocation(contentPage, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                    0, DensityUtils.dp2px(36));
        } else {
            selectPicPopupWindow.showAtLocation(contentPage, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        selectPicPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                CommonUtils.backgroundAlpha(CreateEditTopicActivity.this, 1f);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        int code = intent.getIntExtra("code", -1);
        if (code != 100) {
            return;
        }
        ArrayList<String> paths = intent.getStringArrayListExtra("paths");
        if (paths != null && paths.size() > 0) {
            Intent intent2 = new Intent(CreateEditTopicActivity.this, ClipPictureActivity.class);
            intent2.putExtra("path", paths.get(0));
            startActivityForResult(intent2, FLAG_MODIFY_FINISH);
        } else {
            ToastUtils.shortToast("图片选取失败");
        }
    }

    private void showToast(boolean b) {
        String s = b ? "成功" : "失败";
        switch (type) {
            case CREATE_TOPIC:
                Toast.makeText(CreateEditTopicActivity.this, "话题创建" + s, Toast.LENGTH_SHORT).show();
                break;

            case EDIT_TOPIC:
                Toast.makeText(CreateEditTopicActivity.this, "话题修改" + s, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
