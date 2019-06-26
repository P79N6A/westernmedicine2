package com.xywy.askforexpert.module.doctorcircle;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.util.PathUtil;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.interfaces.TextWatcherImpl;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.DensityUtils;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.PermissionUtils;
import com.xywy.askforexpert.appcommon.utils.ScreenUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.VersionCheckUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.appcommon.utils.others.WeakHandler;
import com.xywy.askforexpert.model.topics.TopicEntity;
import com.xywy.askforexpert.module.docotorcirclenew.model.PublishType;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.SendDCMsgSuccessBean;
import com.xywy.askforexpert.module.docotorcirclenew.utils.RichTextUtils;
import com.xywy.askforexpert.module.doctorcircle.topic.TopicSearchActivity;
import com.xywy.askforexpert.module.message.adapter.SendMesagesGridviewAdapter;
import com.xywy.askforexpert.module.message.share.ShareUtil;
import com.xywy.askforexpert.module.my.photo.PhotoActivity;
import com.xywy.askforexpert.module.my.photo.PhotoWallActivity;
import com.xywy.askforexpert.module.my.userinfo.PersonInfoActivity;
import com.xywy.askforexpert.widget.SDCardImageLoader;
import com.xywy.askforexpert.widget.view.SelectPicPopupWindow;
import com.xywy.base.view.ProgressDialog;
import com.xywy.easeWrapper.utils.SmileUtils;
import com.xywy.uilibrary.titlebar.ItemClickListener;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 导入话题匹配正则表达式

/**
 * 4.2.2 医圈发送动态
 *
 * @author LG
 * @modified Jack Fang
 */

public class DoctorCircleSendMessageActivty extends YMBaseActivity implements OnClickListener {
    private static final String LOG_TAG = DoctorCircleSendMessageActivty.class.getSimpleName();

    // 话题相关参数

    public static final int TOPIC_SEARCH_REQUEST_CODE = 777;
    private static final int MAX_TOPIC_COUNT = 5;
    private int selectedOne = -1;
    private List<TopicEntity> mTopics = new ArrayList<>();
    private List<ForegroundColorSpan> mColorSpans = new ArrayList<>();
    private int endIndex = -1;
    private boolean isShouldCompute = true;
    private boolean isShouldSub = true;
    private final int[] mLocation = new int[2];
    //实例化一个矩形
    private Rect mRect = new Rect();
    // 到此为止

    /**
     * 实名动态最大输入字数限制
     */
    private static final int REAL_POST_MAX_INPUT_LENGTH = 1800;

    /**
     * 匿名动态最大输入字数限制
     */
    private static final int ANONYMOUS_POST_MAX_INPUT_LENGTH = 140;

    /**
     * 分享评论最大输入字数限制
     */
    private static final int COMMENT_POST_MAX_INPUT_LENGTH = 150;

    private final String contentKey = "messageContent";
    private final String urlKey = "messageUrl";
    private Activity context;
    final String TAG = "DoctorCircleSendMessageActivty";
    public static List<String> imagePahtList_idcard = null;
    /**
     * 图片地址
     */
    // private Uri origUri;
    private File origUri;
    private final static String FILE_SAVEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera";
    /**
     * 本地图片选取标志
     */
    private static final int FLAG_CHOOSE_IMG = 0x11;
    /**
     * 截取结束标志
     */
    private static final int FLAG_MODIFY_FINISH = 7;
    /**
     * 相机标志
     */
    private static final int FLAG_CHOOSE_CAMERA = 0x17;
    //private TextView tv_type, tv_doctor_send, tv_contentNum;
    private TextView tv_contentNum;
    private EditText ed_content;
    private String type;
    private SelectPicPopupWindow selectPicPopupWindow;
    private GridView gv_iamges;
    private MyOnclic myOnclic;
    private List<String> list1 = new ArrayList<String>();
    private SendMesagesGridviewAdapter mesagesGridviewAdapter;
    private ProgressDialog pro;
    private CheckBox cb;
    private AlertDialog createDiLog;
    private SharedPreferences sp;
    long leng;
    private String content;
    private String uuid = "0";
    private String shareId = "";
    private AjaxParams params = new AjaxParams();
    private FinalHttp fh = new FinalHttp();

    private String err_str;
    private String ok_str;

    private static final String TOPIC_SP_KEY = "topicSaved";
    private static String DRAFT_SP_KEY;


    private String shareToDoctorCircle;
    private String imgURL;
    private String title;
    private String tagURL;
    private String source;
    private String share_source;
    private int postLength;
    private TextView errorText;
    private ImageButton camera;
    private TextView anonymousName;
    private LinearLayout contentPage;
    private String anonymous_name = "华佗";
    private SharedPreferences topicSP;
    private ImageButton btn_add_topic;
    private SharedPreferences topicRemind;
    private String themeuserid;

    private String shareAnswerid, shareAnswerTitle, shareAnswerType, shareAnswerVersion;

    private String newTopicName;
    private String newTopicId;


    public static void startActivity(Activity activity, String type) {
        Intent intent = new Intent(activity, DoctorCircleSendMessageActivty.class);
        intent.putExtra("type", type);
        activity.startActivity(intent);
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_doctor_circol_sendmessage;
    }

    @Override
    protected void beforeViewBind() {
        context = this;
        type = getIntent().getStringExtra("type");
        imgURL = getIntent().getStringExtra("imgURL");// 图片
        title = getIntent().getStringExtra("title");// 标题
        tagURL = getIntent().getStringExtra("tagURL");// h5
        shareId = getIntent().getStringExtra("id");//分享的Id
        shareToDoctorCircle = getIntent().getStringExtra("shareToDoctorCircle");// 来自分享
        share_source = getIntent().getStringExtra("share_source");// 一天2咨询1

        if (!TextUtils.isEmpty(share_source) && "6".equals(share_source)) {
            shareAnswerid = getIntent().getStringExtra("shareAnswerId");
            shareAnswerTitle = getIntent().getStringExtra("shareAnswerTitle");
            shareAnswerType = getIntent().getStringExtra("shareAnswerType");
            shareAnswerVersion = getIntent().getStringExtra("answerversion");
            StringBuffer sb = new StringBuffer();
            sb.append(shareAnswerid + "|");
            sb.append(shareAnswerTitle + "|");
            sb.append(shareAnswerType + "|");
            sb.append(shareAnswerVersion);
            shareId = sb.toString();
        }

        themeuserid = getIntent().getStringExtra("themeuserid");

        newTopicName = getIntent().getStringExtra("topicName");
        newTopicId = getIntent().getStringExtra("topicId");

        DRAFT_SP_KEY = TOPIC_SP_KEY + type;

        if (YMUserService.isGuest()) {
            uuid = "0";
        } else {
            uuid = YMApplication.getLoginInfo().getData().getPid();
        }
        sp = getSharedPreferences("message", Context.MODE_PRIVATE);
        pro = new ProgressDialog(this, "正在加载中...");
        imagePahtList_idcard = new ArrayList<>();
        ScreenUtils.initScreen(this);


        topicSP = getSharedPreferences(TOPIC_SP_KEY, MODE_PRIVATE);

        if ("shareToDoctorCircle".equals(shareToDoctorCircle)) {
            err_str = "分享失败";
            ok_str = "已分享";
        } else {
            err_str = "发表失败";
            ok_str = "发表成功";
        }

    }


    @Override
    protected void initView() {

        initMyView();
        initMyData();
        setlisener();


        switch (type) {
            case PublishType.Realname:
                anonymousName.setVisibility(View.GONE);
                btn_add_topic.setVisibility(View.VISIBLE);
                break;

            case PublishType.Anonymous:
                anonymousName.setVisibility(View.VISIBLE);
                btn_add_topic.setVisibility(View.GONE);
                break;
        }

        // 实名动态，并且没有显示过话题使用导航时显示话题导航
        if (type.equals(PublishType.Realname)) {
            topicRemind = getSharedPreferences("topicRemind", MODE_PRIVATE);
            if (topicRemind.getBoolean("topicRemind" + AppUtils.getAppVersionCode() + uuid, true)) {
                btn_add_topic.post(new Runnable() {
                    @Override
                    public void run() {
                        DLog.d(LOG_TAG, "# coordination = " + btn_add_topic.getX() + ", " + btn_add_topic.getY());
                        showPopup();
                    }
                });
            }
        }

    }

    @Override
    protected void initData() {

        titleBarBuilder.addItem("发表", new ItemClickListener() {
            @Override
            public void onClick() {
                View currentFocus = getWindow().getCurrentFocus();
                if (currentFocus != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                }
                sendMessage();
            }
        }).setBackIconClickEvent(new ItemClickListener() {
            @Override
            public void onClick() {
                goBack();
            }
        }).build();
    }


    private void initMyView() {
        gv_iamges = (GridView) findViewById(R.id.gv_images);
        ed_content = (EditText) findViewById(R.id.et_content);
        tv_contentNum = (TextView) findViewById(R.id.tv_contentNum);
        ImageView iv_share_poto = (ImageView) findViewById(R.id.iv_share_poto);
        LinearLayout ll_share = (LinearLayout) findViewById(R.id.ll_share);
        TextView tv_share_title = (TextView) findViewById(R.id.tv_share_title);
        if ("shareToDoctorCircle".equals(shareToDoctorCircle)) {
            gv_iamges.setVisibility(View.GONE);
            FinalBitmap create = FinalBitmap.create(context, false);
            create.configLoadfailImage(R.drawable.img_default_bg);
            create.configLoadingImage(R.drawable.img_default_bg);
            if (TextUtils.isEmpty(imgURL)) {
                imgURL = ShareUtil.DEFAULT_SHARE_IMG_ULR;
            }
            create.display(iv_share_poto, imgURL);
            tv_share_title.setText(title);
            type = PublishType.Realname;
            ll_share.setVisibility(View.VISIBLE);
            source = "3";
        } else {
            gv_iamges.setVisibility(View.VISIBLE);
            ll_share.setVisibility(View.GONE);
            iv_share_poto.setVisibility(View.GONE);
            tv_share_title.setVisibility(View.GONE);

        }


        errorText = (TextView) findViewById(R.id.error_text);
        camera = (ImageButton) findViewById(R.id.camera);
        btn_add_topic = (ImageButton) findViewById(R.id.add_topic);
        anonymousName = (TextView) findViewById(R.id.anonymous_name);
        contentPage = (LinearLayout) findViewById(R.id.post_content_page);
    }


    protected void initMyData() {
        requestAnonymousName();


        if (!"shareToDoctorCircle".equals(shareToDoctorCircle)) {
            // 读取话题草稿
            String topicSPString = getTopicDraft();
            DLog.d(LOG_TAG, "saved topics string = " + topicSPString);
            if (!topicSPString.equals("")) {
                DLog.d(LOG_TAG, "topicSaved = " + topicSPString);
                try {
                    List<TopicEntity> topicEntities = TopicEntity.parseJson(new JSONArray(topicSPString));
                    mTopics.addAll(topicEntities);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        String message = sp.getString(contentKey + type, "");
        String urlList = sp.getString(urlKey + type, "");

        if ("shareToDoctorCircle".equals(shareToDoctorCircle)) {
            tv_contentNum.setText(ed_content.getText().toString().trim().length() + "/" + COMMENT_POST_MAX_INPUT_LENGTH);
        } else {
            try {
                JSONArray ja = new JSONArray(urlList);
                if (ja.length() > 0) {
                    for (int i = 0; i < ja.length(); i++) {
                        imagePahtList_idcard.add(ja.getString(i));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mesagesGridviewAdapter = new SendMesagesGridviewAdapter(imagePahtList_idcard, context);
            gv_iamges.setAdapter(mesagesGridviewAdapter);

            if (!TextUtils.isEmpty(message)) {
                Spannable spannable = SmileUtils.getSmiledText(this, message);
                ed_content.setText(spannable, TextView.BufferType.SPANNABLE);
                ed_content.setSelection(message.length());// 光标
                if (type.equals(PublishType.Realname)) {
                    highlight();
                }
                tv_contentNum.setText(ed_content.getText().toString().trim().length() + "/" + ANONYMOUS_POST_MAX_INPUT_LENGTH);
            }

            if (type.equals(PublishType.Realname)) {
                if (!TextUtils.isEmpty(newTopicId) && !TextUtils.isEmpty(newTopicName)) {
                    isShouldSub = false;
                    addTopic(newTopicName, newTopicId);
                }
            }
        }

        String birthWishes = getIntent().getStringExtra("birthWishes");
        if (birthWishes != null && !birthWishes.isEmpty()) {
            ed_content.append(birthWishes);
            ed_content.setSelection(birthWishes.length());
        }


        if (PublishType.Realname.equals(type)) {// 实名动态
            if ("shareToDoctorCircle".equals(shareToDoctorCircle)) {
                titleBarBuilder.setTitleText("医圈分享");
                tv_contentNum.setVisibility(View.VISIBLE);
                ed_content.setHint("跟小伙伴说点什么吧~");
            } else {
                tv_contentNum.setVisibility(View.INVISIBLE);
                titleBarBuilder.setTitleText("实名动态");
                ed_content.setHint(getString(R.string.real_name_post_hint));
            }
        } else {
            titleBarBuilder.setTitleText("匿名动态");
            tv_contentNum.setVisibility(View.VISIBLE);
            ed_content.setHint(getString(R.string.anonymous_post_hint));
        }

        if (imagePahtList_idcard.size() == 0) {
            gv_iamges.setVisibility(View.GONE);
        } else {
            gv_iamges.setVisibility(View.VISIBLE);
        }

        postLength = ed_content.getText().toString().trim().length();
        setLengthErrorText();

        String img_path = getIntent().getStringExtra("img_path");
        if (img_path != null) {
            list1.clear();
            if (imagePahtList_idcard.size() <= 8) {
                imagePahtList_idcard.add(img_path);
            }
            if (mesagesGridviewAdapter == null) {
                mesagesGridviewAdapter = new SendMesagesGridviewAdapter(imagePahtList_idcard, context);
                gv_iamges.setAdapter(mesagesGridviewAdapter);
            }
            mesagesGridviewAdapter.notifyDataSetChanged();
        }
    }

    @NonNull
    private String getTopicDraft() {
        return topicSP.getString(DRAFT_SP_KEY, "");
    }

    private boolean isHaveDraft() {
        return !TextUtils.isEmpty(getTopicDraft());
    }

    /**
     * 获取匿名名字
     */
    private void requestAnonymousName() {
        String bind = YMApplication.getPID();
        String sign = CommonUtils.computeSign(bind);
        String url = CommonUrl.FOLLOW_LIST + "a=doctor&m=doctor_row&userid=" + YMApplication.getPID()
                + "&bind=" + YMApplication.getPID() + "&sign=" + sign + "&type=2";
        FinalHttp request = new FinalHttp();
        request.get(url, new AjaxCallBack() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject data = jsonObject.getJSONObject("data");
                    if (data.getString("nickname") == null || data.getString("nickname").equals("")) {
                        anonymous_name = "华佗";
                        anonymousName.setText("匿名身份：华佗");
                    } else {
                        anonymous_name = data.getString("nickname");
                        anonymousName.setText("匿名身份：" + anonymous_name);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    anonymous_name = "华佗";
                    anonymousName.setText("匿名身份：华佗");
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                anonymous_name = "华佗";
                anonymousName.setText("匿名身份：华佗");
            }
        });
    }

    private void setLengthErrorText() {
        if (PublishType.Realname.equals(type)) {// 实名动态"
            if ("shareToDoctorCircle".equals(shareToDoctorCircle)) {
                tv_contentNum.setText(postLength + "/" + COMMENT_POST_MAX_INPUT_LENGTH);
                if (postLength > COMMENT_POST_MAX_INPUT_LENGTH) {
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText("分享评论最多只能输入" + COMMENT_POST_MAX_INPUT_LENGTH + "字");
                    tv_contentNum.setTextColor(DoctorCircleSendMessageActivty.this.getResources().getColor(android.R.color.holo_red_light));
                } else {
                    tv_contentNum.setTextColor(DoctorCircleSendMessageActivty.this.getResources().getColor(R.color.gray_text));
                }
            } else {
                if (postLength > REAL_POST_MAX_INPUT_LENGTH) {
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText("最多只能发表" + REAL_POST_MAX_INPUT_LENGTH + "字");
                } else {
                    errorText.setVisibility(View.GONE);
                }
            }
        } else {
            tv_contentNum.setText(postLength + "/" + ANONYMOUS_POST_MAX_INPUT_LENGTH);
            if (postLength > ANONYMOUS_POST_MAX_INPUT_LENGTH) {
                errorText.setVisibility(View.VISIBLE);
                errorText.setText("最多只能输入" + ANONYMOUS_POST_MAX_INPUT_LENGTH + "字");
                tv_contentNum.setTextColor(DoctorCircleSendMessageActivty.this.getResources().getColor(android.R.color.holo_red_light));
            } else {
                errorText.setVisibility(View.GONE);
                tv_contentNum.setTextColor(DoctorCircleSendMessageActivty.this.getResources().getColor(R.color.gray_text));
            }
        }
    }

    private void setlisener() {
        myOnclic = new MyOnclic();
        ed_content.setOnClickListener(this);
        ed_content.addTextChangedListener(new TextWatcherImpl() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);

                DLog.d(LOG_TAG, "onTextChanged s = " + s + ", " + start + ", " + before + ", " + count);
                if (isShouldCompute) {
                    endIndex = start + count;
                }
                Log.d(LOG_TAG, "onTextChanged endIndex = " + endIndex);

                postLength = s.length();
                setLengthErrorText();

                if (type.equals(PublishType.Realname)) { // 实名动态
                    // 话题相关
                    updateTopicList();
                    if (start < s.length()) {
                        char c = s.charAt(start);
                        boolean b = String.valueOf(c).equals("#") && (count == 1);
                        if (b) {
                            DLog.d(LOG_TAG, "onTextChanged input #");
                            isShouldSub = true;
                            // 当用户输入了 # 转跳到话题搜索页面
                            Intent intent = new Intent(DoctorCircleSendMessageActivty.this,
                                    TopicSearchActivity.class);
                            intent.putExtra("from", 1);
                            startActivityForResult(intent, TOPIC_SEARCH_REQUEST_CODE);
                        }
                    }
                }
            }
        });
        gv_iamges.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                if (arg2 == imagePahtList_idcard.size()) {
                    addPhoto();
                } else {
                    Intent intent = new Intent(context, PhotoActivity.class);
                    intent.putExtra("keytype", "sendMessages");
                    intent.putExtra("statPosition", arg2);
                    context.startActivityForResult(intent, 100);
                }

            }

        });

        camera.setOnClickListener(this);
        btn_add_topic.setOnClickListener(this);
        anonymousName.setOnClickListener(this);

        // 监听删除键
        ed_content.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    // 删除键
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        int selectionStart = ed_content.getSelectionStart();
                        int selectionEnd = ed_content.getSelectionEnd();
                        int lastPos = 0;
                        //如果光标起始和结束在同一位置,说明是选中效果,直接返回 false 交给系统执行删除动作
                        if (selectionStart != selectionEnd) {
                            return false;
                        }

                        for (int i = 0; i < mTopics.size(); i++) {
                            lastPos = ed_content.getText().toString().indexOf(mTopics.get(i).getTopicName(), lastPos);
                            if (lastPos != -1) {
                                mTopics.get(i).setStartIndex(lastPos);
                                mTopics.get(i).setEndIndex(lastPos + mTopics.get(i).getTopicName().length());
                                // 选中话题
                                if (selectionStart > lastPos && selectionStart <= (lastPos + mTopics.get(i).getTopicName().length())) {
                                    Log.d(LOG_TAG, "select_topic");
                                    Log.d(LOG_TAG, "select_topic " + lastPos);
                                    Log.d(LOG_TAG, "select_topic " + (lastPos + mTopics.get(i).getTopicName().length()));
                                    ed_content.setText(ed_content.getText());
                                    Spannable span = ed_content.getText();
                                    Selection.setSelection(span, lastPos, lastPos + mTopics.get(i).getTopicName().length());
                                    // 记录选中的位置
                                    selectedOne = i;
                                    return true;
                                } else {
                                    lastPos += mTopics.get(i).getTopicName().length();
                                }
                            }
                        }
                    }
                }

                return false;
            }
        });
    }

    private void addPhoto() {
        SDCardImageLoader.count = imagePahtList_idcard.size();
        SDCardImageLoader.img_max = 9;
        if (imagePahtList_idcard.size() > 9) {
            SendMesagesGridviewAdapter.isAddShow = false;
            return;
        }
        selectPicPopupWindow = new SelectPicPopupWindow(context, myOnclic);
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
                CommonUtils.backgroundAlpha(DoctorCircleSendMessageActivty.this, 1f);
            }
        });

        hideKeyboard();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rl_channel:
                createDiLog.dismiss();
                break;

            case R.id.rl_enter:
                if ("shareToDoctorCircle".equals(shareToDoctorCircle)) {
                    this.finish();
                    return;
                }

                if (cb.isChecked()) {
                    saveDraft();
                } else {
                    sp.edit().remove(contentKey + type).apply();
                    sp.edit().remove(urlKey + type).apply();
                    DLog.d(LOG_TAG, "topicSP clear type = " + type);
                    if (type.equals(PublishType.Realname)) {
                        topicSP.edit().clear().apply();
                    }
                }
                createDiLog.dismiss();
                context.finish();
                break;

            case R.id.camera:
                StatisticalTools.eventCount(this, "yqWritePhoto");
                addPhoto();
                break;

            case R.id.add_topic:
                StatisticalTools.eventCount(this, "yqWriteTopic");
                if (mTopics.size() < MAX_TOPIC_COUNT) {
                    isShouldSub = false;
                    Intent addTopicIntent = new Intent(DoctorCircleSendMessageActivty.this,
                            TopicSearchActivity.class);
                    addTopicIntent.putExtra("from", 1);
                    startActivityForResult(addTopicIntent, TOPIC_SEARCH_REQUEST_CODE);
                } else {
                    Toast.makeText(this, "最多添加" + MAX_TOPIC_COUNT + "个话题", Toast.LENGTH_SHORT).show();
                }
                break;

            // 匿名介绍
            case R.id.anonymous_name:
                StatisticalTools.eventCount(this, "yqHideShareStatus");
                Intent intent = new Intent(this, AnonymousNameIntroActivity.class);
                intent.putExtra("anonymousName", anonymous_name);
                startActivity(intent);
                break;

            case R.id.et_content:

                int selectionStart = ((EditText) v).getSelectionStart();
                DLog.d(LOG_TAG, "onClick_selectionStart = " + selectionStart);
                selectedOne = -1;
                updateTopicList();
                if (!"shareToDoctorCircle".equals(shareToDoctorCircle)) {
                    DLog.d(LOG_TAG, "onClick mTopics size = " + mTopics.size());
                    for (int i = 0; i < mTopics.size(); i++) {
                        TopicEntity topicEntity = mTopics.get(i);
                        if (selectionStart > topicEntity.getStartIndex() && selectionStart < topicEntity.getEndIndex()) {
                            ed_content.setSelection(topicEntity.getEndIndex());
                        }
                    }
                }
                break;

            default:
                break;
        }

    }

    /**
     * 保存草稿
     */
    private void saveDraft() {
        Editor edit = sp.edit();
        edit.putString(contentKey + type, ed_content.getText().toString().trim());
        JSONArray ja = new JSONArray(imagePahtList_idcard);
        edit.putString(urlKey + type, ja.toString());
        edit.apply();
        if (type.equals(PublishType.Realname)) {
            // 保存话题草稿
            DLog.d(LOG_TAG, "saved mTopics size = " + mTopics.size() + ", " + mTopics.isEmpty());
            topicSP.edit().clear().apply();
            if (!mTopics.isEmpty()) {
                JSONArray array = new JSONArray();
                for (int i = 0; i < mTopics.size(); i++) {
                    TopicEntity topicEntity = mTopics.get(i);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("topicName", topicEntity.getTopicName());
                        jsonObject.put("topicId", topicEntity.getTopicId());
                        jsonObject.put("startIndex", topicEntity.getStartIndex());
                        jsonObject.put("endIndex", topicEntity.getEndIndex());
                        array.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                String s = array.toString();

                topicSP.edit().putString(DRAFT_SP_KEY, s).apply();
            }
        }
    }

    @Override
    protected void onResume() {
        StatisticalTools.onResume(this);

        if (imagePahtList_idcard.size() == 0) {
            gv_iamges.setVisibility(View.GONE);
        } else {
            gv_iamges.setVisibility(View.VISIBLE);
        }
        if (mesagesGridviewAdapter != null) {
            mesagesGridviewAdapter.notifyDataSetChanged();
        } else {
            mesagesGridviewAdapter = new SendMesagesGridviewAdapter(imagePahtList_idcard, context);
            gv_iamges.setAdapter(mesagesGridviewAdapter);
        }

        super.onResume();
    }

    class MyOnclic implements OnClickListener {

        @Override
        public void onClick(View v) {
            selectPicPopupWindow.dismiss();
            switch (v.getId()) {
                case R.id.item_popupwindows_camera:// 打开相机
                    if (!PermissionUtils.checkPermission(DoctorCircleSendMessageActivty.this, Manifest.permission.CAMERA)) {
                        CommonUtils.permissionRequestDialog(context, "请先授予照相机(Camera)权限", 555);
                    }else{
                        origUri = new File(PathUtil.getInstance().getImagePath(), "osc_" + System.currentTimeMillis() + ".jpg");
                        origUri.getParentFile().mkdirs();
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
//                        origUri = CommonUtils.startCamera(DoctorCircleSendMessageActivty.this, FLAG_CHOOSE_CAMERA);
                    }
                    break;

                case R.id.item_popupwindows_Photo:// 打开相册 自定义
                    if (VersionCheckUtils.isMarshmallowOrHigher()) {
                        if (!PermissionUtils.checkPermission(DoctorCircleSendMessageActivty.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            CommonUtils.permissionRequestDialog(context, "无法打开相册，请授予内存(Storage)权限", 123);
                        } else {
                            Intent intent = new Intent(context, PhotoWallActivity.class);
                            intent.putExtra("yiquan", "yiquan");
                            startActivityForResult(intent, FLAG_CHOOSE_IMG);
                            YMApplication.photoTag = "doctor";
                            YMApplication.isSelectMore = true;
                        }
                    } else {
                        Intent intent = new Intent(context, PhotoWallActivity.class);
                        intent.putExtra("yiquan", "yiquan");
                        startActivityForResult(intent, FLAG_CHOOSE_IMG);
                        YMApplication.photoTag = "doctor";
                        YMApplication.isSelectMore = true;
                    }
                    break;
            }

        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        int code = intent.getIntExtra("code", -1);
        if (code != 100) {
            return;
        }

        list1.clear();
        ArrayList<String> paths = intent.getStringArrayListExtra("paths");
        // 添加，去重
        for (int i = 0; i < paths.size(); i++) {
            if (imagePahtList_idcard.size() <= 8) {
                imagePahtList_idcard.add(paths.get(i));
            }
        }
        if (mesagesGridviewAdapter == null) {

            mesagesGridviewAdapter = new SendMesagesGridviewAdapter(imagePahtList_idcard, context);
            gv_iamges.setAdapter(mesagesGridviewAdapter);
        }
        mesagesGridviewAdapter.notifyDataSetChanged();
    }

    /**
     * 发表
     */
    private void sendMessage() {
        if (!NetworkUtil.isNetWorkConnected()) {
            ToastUtils.shortToast("网络异常，请检查网络连接");
            return;
        }
        if (YMUserService.isGuest()) {// 是游客
            DialogUtil.LoginDialog(new YMOtherUtils(context).context);
            return;
        }

        if (PublishType.Realname.equals(type)) {
            if ("shareToDoctorCircle".equals(shareToDoctorCircle)) {
                if (postLength > 150) {
                    ToastUtils.shortToast("超过字数限制");
                    return;
                }
            } else {
                if (postLength > REAL_POST_MAX_INPUT_LENGTH) {
                    ToastUtils.shortToast("超过字数限制");
                    return;
                }
            }
        } else {
            if (postLength > 140) {
                ToastUtils.shortToast("超过字数限制");
                return;
            }
        }

        if (PublishType.Realname.equals(type)) {
            StatisticalTools.eventCount(context, "Publisheddynamic");
        } else {
            StatisticalTools.eventCount(this, "Ananonymousdynamic");
        }

        fh.configTimeout(30000);

        if (type.equals(PublishType.Realname)) {
            content = genStrToPost();
        } else {
            content = ed_content.getText().toString().trim();
        }

        if (!"shareToDoctorCircle".equals(shareToDoctorCircle)) {
            if (content.length() == 0) {
                ToastUtils.shortToast("内容不能为空");
                return;
            }
        }
        handler.sendEmptyMessage(2);
    }

    public void showDailog() {
        String msgInfo;
        if (YMApplication.isDoctor()) {
            msgInfo = getString(R.string.doctor_not_all_infors);// 需要完善姓名、职称、医院、科室等信息，才能发表实名动态
        } else {
            msgInfo = getString(R.string.doctor_student_not_all_infors);// 需要完善姓名、学校、专业等信息，才能发表实名动态
        }
        android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(context);

        dialog.setMessage(msgInfo);
        dialog.setNegativeButton(getString(R.string.cancel),
                new DialogInterface.OnClickListener() {// "取消"

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                    }
                });

        dialog.setPositiveButton("去完善", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent inten = new Intent(context, PersonInfoActivity.class);
                inten.putExtra("doctorInfo", "doctorInfo");
                context.startActivity(inten);
                arg0.dismiss();
            }
        });
        dialog.create().show();

        hideKeyboard();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case FLAG_CHOOSE_IMG:
                    list1.clear();
                    ArrayList<String> paths = data.getStringArrayListExtra("paths");
                    // 添加，去重
                    for (int i = 0; i < paths.size(); i++) {
                        if (imagePahtList_idcard.size() <= 8) {
                            imagePahtList_idcard.add(paths.get(i));
                        }
                    }
                    if (mesagesGridviewAdapter == null) {
                        mesagesGridviewAdapter = new SendMesagesGridviewAdapter(imagePahtList_idcard, context);
                        gv_iamges.setAdapter(mesagesGridviewAdapter);
                    }
                    mesagesGridviewAdapter.notifyDataSetChanged();
                    break;

                case FLAG_CHOOSE_CAMERA:
                    try {
                        imagePahtList_idcard.add(origUri.getPath());
                        if (mesagesGridviewAdapter == null) {
                            mesagesGridviewAdapter = new SendMesagesGridviewAdapter(imagePahtList_idcard, context);
                            gv_iamges.setAdapter(mesagesGridviewAdapter);
                        }
                        mesagesGridviewAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                // 选完话题回来
                // 添加话题
                case TOPIC_SEARCH_REQUEST_CODE:
                    String topicName = data.getStringExtra("topicName");
                    String topicId = data.getStringExtra("topicId");
                    DLog.d(LOG_TAG, "topicName = " + topicName + ", topicId = " + topicId);
                    addTopic(topicName, topicId);
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goBack();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void goBack() {
        View v = this.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.sendmessagmain_bg_dialog, null);
        builder.setView(view);
        createDiLog = builder.create();
        Window window = createDiLog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        window.setGravity(Gravity.CENTER);

        if ("shareToDoctorCircle".equals(shareToDoctorCircle)) {
            createDiLog.show();
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_title.setText("医圈");
            TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_content.setTextSize(14);
            tv_content.setText("取消此次分享？");
            cb = (CheckBox) view.findViewById(R.id.cb);
            cb.setVisibility(View.GONE);
            cb.setSelected(false);
            RelativeLayout rl_channel = (RelativeLayout) view.findViewById(R.id.rl_channel);
            RelativeLayout rl_enter = (RelativeLayout) view.findViewById(R.id.rl_enter);
            ((TextView) view.findViewById(R.id.tv_ok)).setText("确定");
            rl_channel.setOnClickListener(this);
            rl_enter.setOnClickListener(this);
        } else {
            if (TextUtils.isEmpty(ed_content.getText().toString().trim()) && imagePahtList_idcard.size() == 0) {
                DLog.d(LOG_TAG, "edit_content_go_back is empty");
                sp.edit().remove(contentKey + type).apply();
                sp.edit().remove(urlKey + type).apply();
                if (type.equals(PublishType.Realname)) {
                    topicSP.edit().clear().apply();
                }
                context.finish();
                return;
            }

            DLog.d(LOG_TAG, "edit_content_go_back = " + ed_content.getText().toString().trim());
            createDiLog.show();
            cb = (CheckBox) view.findViewById(R.id.cb);
            RelativeLayout rl_channel = (RelativeLayout) view.findViewById(R.id.rl_channel);
            RelativeLayout rl_enter = (RelativeLayout) view.findViewById(R.id.rl_enter);
            rl_channel.setOnClickListener(this);
            rl_enter.setOnClickListener(this);
        }

    }

    // 强制隐藏键盘

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        InputMethodManager manager = (InputMethodManager) this.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (this.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (this.getCurrentFocus() != null) {
                manager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DLog.d("DOC_SEND", "onDestroy");

        mesagesGridviewAdapter = null;
        if (pro != null && pro.isShowing()) {
            pro.closeProgersssDialog();
        }

    }


    /**
     * 重新获取输入框中各个话题的起始位置并将话题集合重新排序
     *
     * @author Jack Fang
     */
    private synchronized void updateTopicList() {
        try {
            Collections.sort(mTopics);
            // 删除选中话题
            if (selectedOne != -1 && !mTopics.isEmpty()) {
                mTopics.remove(selectedOne);
                selectedOne = -1;
            }
            DLog.d(LOG_TAG, "update mTopics size = " + mTopics.size());

            // 记录上一个话题的终止位置，避免相同话题只能计算到第一个
            int preEnd = 0;
            for (int i = 0; i < mTopics.size(); i++) { //循环遍历整个输入框的所有字符
                String s = ed_content.getText().toString();
                String topicName = mTopics.get(i).getTopicName();
                int lastPos = s.substring(preEnd, s.length()).indexOf(topicName, 0) + preEnd;
                preEnd = lastPos + topicName.length();
                mTopics.get(i).setStartIndex(lastPos);
                mTopics.get(i).setEndIndex(preEnd);
                DLog.d(LOG_TAG, "lastPos = " + lastPos);
                DLog.d(LOG_TAG, "lastPos + = " + (lastPos + topicName.length()));
                DLog.d(LOG_TAG, "lastPos preEnd = " + preEnd);
            }
            Collections.sort(mTopics);
        } catch (Exception e) {
            DLog.d(TAG, e.getMessage());
        }
    }

    /**
     * 将输入框中的话题高亮
     *
     * @author Jack Fang
     */
    private void highlight() {
        try {
            updateTopicList();
            Editable editable = ed_content.getText();
            for (int i = 0; i < mColorSpans.size(); i++) {
                editable.removeSpan(mColorSpans.get(i));
            }
            mColorSpans.clear();
            SpannableStringBuilder spannable = new SpannableStringBuilder(editable.toString());//用于可变字符串
            DLog.d(LOG_TAG, "highlight mTopics.size() = " + mTopics.size());
            for (int i = 0; i < mTopics.size(); i++) {
                TopicEntity topicEntity = mTopics.get(i);
                ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#0EC3CE"));
                DLog.d(LOG_TAG, "highlight startIndex = " + topicEntity.getStartIndex() + ", endIndex = " + topicEntity.getEndIndex());
                try {
                    spannable.setSpan(span, topicEntity.getStartIndex(), topicEntity.getEndIndex(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mColorSpans.add(span);
            }

            isShouldCompute = false;
            ed_content.setText(spannable);
            DLog.d(LOG_TAG, "highlight endIndex before = " + endIndex);
            if (endIndex == -1) {
                endIndex = ed_content.getText().length();
            }
            DLog.d(LOG_TAG, "highlight endIndex after = " + endIndex);
            ed_content.setSelection(endIndex);
            endIndex = -1;
            isShouldCompute = true;
        } catch (Exception e) {
            e.printStackTrace();
            DLog.e(TAG, e.getMessage());
        }
    }

    /**
     * 在动态输入框中添加话题
     *
     * @param topicName 话题名称
     * @param topicId   话题id
     * @author Jack Fang
     */
    private void addTopic(String topicName, String topicId) {

        int id=Integer.parseInt(topicId);
        if (isHasThisTopic(id)) {
            LogUtils.d("已经包含该话题");
            return;
        }
        DLog.d(LOG_TAG, "topicName and topicId = " + topicName + ", " + topicId);
        if (mTopics != null && mTopics.size() < MAX_TOPIC_COUNT) {
            DLog.d(LOG_TAG, "selectionEnd = " + ed_content.getSelectionEnd());
            int startIndex = (ed_content.getSelectionEnd() > 0 && isShouldSub) ?
                    ed_content.getSelectionEnd() - 1 : ed_content.getSelectionEnd();
            DLog.d(LOG_TAG, "startIndex = " + startIndex);
            // 添加话题前删除前面输入的 #
            String editTextStr = ed_content.getText().toString();
            String substring1;
            String substring2 = "";
            if (isShouldSub) {
                substring1 = editTextStr.substring(0, startIndex);
                if (startIndex + 1 < editTextStr.length()) {
                    substring2 = editTextStr.substring(startIndex + 1, editTextStr.length());
                }
            } else {
                substring1 = editTextStr;
            }
            editTextStr = substring1 + substring2;
            ed_content.setText(editTextStr);
            isShouldSub = true;
            DLog.d(LOG_TAG, "startIndex after = " + startIndex);
            TopicEntity topicEntity = new TopicEntity("#" + topicName + "#", Integer.parseInt(topicId));
            DLog.d(LOG_TAG, "added_startIndex = " + startIndex);
            if (startIndex < 0) {
                startIndex = ed_content.getText().length();
            }
            ed_content.getText().insert(startIndex, topicEntity.getTopicName());
            endIndex = startIndex + topicEntity.getTopicName().length();
            DLog.d(LOG_TAG, "endIndex = " + endIndex);
            topicEntity.setStartIndex(startIndex);
            topicEntity.setEndIndex(endIndex);
            mTopics.add(topicEntity);
        } else {
            Toast.makeText(this, "最多添加" + MAX_TOPIC_COUNT + "个话题", Toast.LENGTH_SHORT).show();
        }
        Collections.sort(mTopics);
        highlight();
    }

    private boolean isHasThisTopic(int topicId) {
        boolean isHasThisTopic = false;

        for (TopicEntity entity : mTopics) {
            if (entity.getTopicId() == topicId) {
                isHasThisTopic = true;
                break;
            }
        }
        return isHasThisTopic;
    }

    /**
     * 生成像服务器发送的动态数据，主要用于在动态中的话题后添加$$
     *
     * @return 可以发送给服务器的动态数据
     * @author Jack Fang
     */
    private String genStrToPost() {
        String s = ed_content.getText().toString().trim();
        try {
            updateTopicList();

            DLog.d(LOG_TAG, "s before = " + s);
            int maxIndex = s.length();

            // 将动态内容按话题切割，每个话题中间和两端的部分单独分到一个subString中，用于匹配用户自己添加的话题
            List<String> subStrs = new ArrayList<>();
            if (mTopics != null && !mTopics.isEmpty()) {
                subStrs.add(s.substring(0, mTopics.get(0).getStartIndex()));
                for (int i = 0; i < mTopics.size() - 1; i++) {
                    subStrs.add(s.substring(mTopics.get(i).getEndIndex(), mTopics.get(i + 1).getStartIndex()));
                }
                subStrs.add(s.substring(mTopics.get(mTopics.size() - 1).getEndIndex(), maxIndex));
            } else {
                subStrs.add(s);
            }

            // 用正则匹配每一个话题
            List<String> groupFinded = new ArrayList<>();
            for (int i = 0; i < subStrs.size(); i++) {
                Pattern p = Pattern.compile(RichTextUtils.TOPIC_TEXT_REGEX);
                Matcher m = p.matcher(subStrs.get(i));
                while (m.find()) {
                    DLog.d(LOG_TAG, "group_find = " + m.group());
                    groupFinded.add(m.group());
                }
            }
            // 在匹配到的话题后添加 $$
            if (!groupFinded.isEmpty()) {
                for (int i = 0; i < groupFinded.size(); i++) {
                    String target = groupFinded.get(i);
                    s = s.replace(target,
                            target.substring(0, target.length() - 1)
                                    + "$$ "
                                    + target.substring(target.length() - 1, target.length()));
                }
            }

            DLog.d(LOG_TAG, "s medium = " + s);
            if (mTopics != null && !mTopics.isEmpty()) {
                // 匹配每一个话题，并添加话题id
                for (int i = 0; i < mTopics.size(); i++) {
                    s = s.replace(mTopics.get(i).getTopicName(), mTopics.get(i).toString());
                }
            }

            DLog.d(LOG_TAG, "s after = " + s);
        } catch (Exception e) {
            DLog.d(TAG, e.getMessage());
        }
        return s;
    }

    /**
     * 话题使用导航
     */
    private void showPopup() {
        DLog.d(LOG_TAG, "show topic remind popup");
        final PopupWindow p = new PopupWindow();
        p.setFocusable(true);
        p.setTouchable(true);
        p.setOutsideTouchable(true);
        p.setBackgroundDrawable(new BitmapDrawable());
        p.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        p.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(this).inflate(R.layout.topic_remind_popup, null);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (p.isShowing()) {
                    p.dismiss();
                }
            }
        });
        p.setContentView(view);
        btn_add_topic.getLocationOnScreen(mLocation);
        DLog.d(LOG_TAG, "screen location = " + mLocation[0] + ", " + mLocation[1]);
        DLog.d(LOG_TAG, "view top = " + btn_add_topic.getTop() + ", " + btn_add_topic.getBottom());
        //设置矩形的大小
        mRect.set(mLocation[0], mLocation[1], mLocation[0] + btn_add_topic.getWidth(), mLocation[1] + btn_add_topic.getHeight());
        int pGravity = Gravity.START | Gravity.TOP;
        DLog.d(LOG_TAG, "mRect.top = " + mRect.top + ", mRect.bottom = " + mRect.bottom);
        CommonUtils.backgroundAlpha(this, 0.5f);
        p.showAtLocation(btn_add_topic, pGravity, DensityUtils.dp2px(16),
                mRect.top / 2 - btn_add_topic.getHeight() - DensityUtils.dp2px(12));
        p.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                CommonUtils.backgroundAlpha(DoctorCircleSendMessageActivty.this, 1f);
                topicRemind.edit().putBoolean("topicRemind" +
                                AppUtils.getAppVersionCode() + uuid,
                        false).apply();
            }
        });
    }


    private WeakHandler handler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(final Message msg) {
            switch (msg.what) {
                case 1:// 发送数据
                    if (leng > 8 * 1024 * 1024) {
                        pro.closeProgersssDialog();
                        ToastUtils.longToast("上传图片不能超过8M");
                        break;
                    }
                    DLog.d(LOG_TAG, "post send url = " + CommonUrl.doctor_circo_url + "?" + params.toString());
                    fh.upLoadFile(CommonUrl.doctor_circo_url, params,
                            new AjaxCallBack() {

                                @Override
                                public void onFailure(Throwable t, int errorNo, String strMsg) {
                                    if (pro != null && pro.isShowing()) {
                                        pro.closeProgersssDialog();
                                    }
                                    super.onFailure(t, errorNo, strMsg);

                                    ToastUtils.shortToast(err_str);
                                }

                                @Override
                                public void onSuccess(String t) {
                                    super.onSuccess(t);
                                    if (pro != null && pro.isShowing()) {
                                        pro.closeProgersssDialog();
                                    }

                                    try {
                                        JSONObject obj = new JSONObject(t.toString());
                                        String code = obj.getString("code");
                                        String message = obj.optString("msg");
                                        if (TextUtils.isEmpty(code)) {
                                            ToastUtils.shortToast(err_str);
                                            return;
                                        }
                                        if ("0".equals(code)) {
                                            sp.edit().remove(contentKey + type).apply();
                                            sp.edit().remove(urlKey + type).apply();
                                            if (type.equals(PublishType.Realname)) {
                                                topicSP.edit().clear().apply();
                                            }
                                            imagePahtList_idcard.clear();
                                            if (!"shareToDoctorCircle".equals(shareToDoctorCircle)) {
                                                if (PublishType.Anonymous.equals(type)) {
                                                    sp.edit().putString("nonamesned", "nonamesned").apply();
                                                }

                                            }
                                            ToastUtils.shortToast(ok_str);
                                            //通知医圈页面刷新数据
                                            YmRxBus.notifySendDCMsgSuccess(new SendDCMsgSuccessBean(type));
                                            DoctorCircleSendMessageActivty.this.finish();
                                        } else if ("-3".equals(code)) {
                                            CommonUtils.showWarnDialog(DoctorCircleSendMessageActivty.this, message);
                                        } else if ("-2".equals(code)) {
                                            showDailog();
                                        } else {
                                            ToastUtils.shortToast(obj.getString("msg"));
                                            context.finish();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    break;

                case 2:// 压缩图片
                    if (pro != null && !pro.isShowing()) {
                        pro.showProgersssDialog();
                    }
                    String sign = MD5Util.MD5(uuid + type + Constants.MD5_KEY);
                    if ("shareToDoctorCircle".equals(shareToDoctorCircle)) {// 分享过来的
                        params.put("source", source);
                        params.put("share_source", share_source);
                        params.put("share_link", tagURL);// h5
                        params.put("share_img", imgURL);// 分享图片
                        params.put("share_title", title);// 分享文字
                        params.put("share_other", shareId);//分享id
                    }
                    params.put("m", "dynamic_add");
                    params.put("a", "dynamic");
                    params.put("userid", uuid);
                    params.put("bind", uuid + type);
                    params.put("type", type + "");
                    params.put("sign", sign);
                    params.put("content", content);
                    params.put("themeuserid", (themeuserid == null || themeuserid.equals("")) ? "0" : themeuserid);
                    new Thread() {
                        @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
                        public void run() {
                            if (imagePahtList_idcard.size() > 0) {
                                leng = 0;
                                for (int i = 0; i < imagePahtList_idcard.size(); i++) {
                                    try {
                                        File file = new File(imagePahtList_idcard.get(i));
                                        params.put("file" + i, file);
//                                        if (file.length() / 1024 < 500) {// 500k
//                                            params.put("file" + i, file);
//                                        } else {
//                                            Bitmap image = ImageUtils.getImage(imagePathList_style.get(i));
//                                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                                            baos.reset();// 重置baos即清空baos
//                                            image.compress(Bitmap.CompressFormat.JPEG, 50, baos); // 这里压缩50%，把压缩后的数据存放到baos中
//                                            leng = leng + baos.toByteArray().length;
//                                            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
//                                            params.put("file" + i, isBm);
//                                            image.recycle();
//                                            baos.reset();
//                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            handler.sendEmptyMessage(1);
                        }
                    }.start();
                    break;

                default:
                    break;
            }
            return true;
        }
    });

}
