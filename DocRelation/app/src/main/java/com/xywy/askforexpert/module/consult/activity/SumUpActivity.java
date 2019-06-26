package com.xywy.askforexpert.module.consult.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.SPUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.model.consultentity.CommonRspEntity;
import com.xywy.askforexpert.module.consult.ConsultConstants;
import com.xywy.askforexpert.module.consult.ServiceProvider;
import com.xywy.askforexpert.widget.view.SelectBasePopupWindow;
import com.xywy.uilibrary.titlebar.ItemClickListener;

import butterknife.Bind;
import rx.Subscriber;

/**
 * 问诊总结/举报 stone 空格不算字符数目 目前只是问诊总结,举报功能走的新页面 stone 移植yimai
 * Created by zhangzheng on 2017/5/3.
 */

public class SumUpActivity extends YMBaseActivity implements View.OnTouchListener {
    @Bind(R.id.tv_1)
    TextView tv_1;
    @Bind(R.id.et_sum_up_enter_1)
    EditText etEnter_1;
    @Bind(R.id.tv_sum_up_text_count_1)
    TextView tvCount_1;

    @Bind(R.id.tv_3)
    TextView tv_3;
    @Bind(R.id.et_sum_up_enter_3)
    EditText etEnter_3;
    @Bind(R.id.tv_sum_up_text_count_3)
    TextView tvCount_3;

    public static final int TYPE_TIP_OFF = 0; //当前是举报的界面
    public static final int TYPE_SUM_UP = 1;  //当前是总结的界面
    private static final int TYPE_QUES = 2;  //当前是问题广场的非追问回复界面
    private static final int TYPE_ZHUIWEN = 3;  //当前是问题广场的追问回复界面

    private static final String PARAM_TYPE = "PARAM_TYPE"; //举报或总结
    private static final String PARAM_DID = "PARAM_DID";  //医生ID
    private static final String PARAM_QID = "PARAM_QID";  //问题ID
    private static final String PARAM_STARTTIME = "startTime";  //抢到问题的起始时间

    //字数限制 stone
    private static final int DEFAULT_TIP_OFF_TEXT_LIMIT_MAX = 50;//举报最大字数
    private static final int DEFAULT_SUM_UP_TEXT_LIMIT_MIN_1 = 10;//病情描述最小字数
    private static final int DEFAULT_SUM_UP_TEXT_LIMIT_MIN_3 = 20;//建议最小字数
    private static final int DEFAULT_SUM_UP_TEXT_LIMIT_MAX = 200;//病情描述/建议最大字数
    private static final int DEFAULT_SUM_UP_TEXT_LIMIT_MIN_2 = 0;//建议最小字数

    private static final String REPLY = "reply";
    private static final int RESULTCODE = 2019;
    private static final int RESULTCODE_ZHUIWEN = 2020;
    private static final String ANALYSIS = "analysis";
    private static final String SUGGETIION = "suggestion";
    public static final long LIMITTIME = 600000;

    private static final String TITLE_SUM_UP = "问诊总结";
    private static final String TITLE_TIP_OFF = "举报";
    private static final String TITLE_QUE = "问题广场";


    private int type = TYPE_SUM_UP;
    private int textLimitMax;
    private int textLimitMin;
    private String doctorId;
    private String questionId;
    private long mStartTime;

    //stone 总结成功
    private boolean mIsSumUpOk;
    //stone 算字数字符串
    private String mStr1 = "";
    private String mStr3 = "";
    private int mChineseCount_1;
    private int mChineseCount_3;
    //stone 缓存的问题id
    private String mCacheQuestionId = "";

    private ClipboardManager mClipboardManager;
    private SelectBasePopupWindow mPopupWindow;
    private View popRoot;
    private TextView tv_copy;

    private float mPosX, mPosY;
    //坐标的位置（x、y）
    private final int[] mLocation = new int[2];
    //实例化一个矩形
    private Rect mRect = new Rect();


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_sum_up;
    }

    @Override
    protected void initData() {
    }

    public static void startTipOffActivity(Context context, String doctorId, String questionId) {
        Intent intent = new Intent(context, SumUpActivity.class);
        intent.putExtra(PARAM_TYPE, TYPE_TIP_OFF);
        intent.putExtra(PARAM_DID, doctorId);
        intent.putExtra(PARAM_QID, questionId);
        context.startActivity(intent);

    }

    public static void startSumUpActivityForResult(Activity context, String doctorId, String questionId, int requestCode) {
        Intent intent = new Intent(context, SumUpActivity.class);
        intent.putExtra(PARAM_TYPE, TYPE_SUM_UP);
        intent.putExtra(PARAM_DID, doctorId);
        intent.putExtra(PARAM_QID, questionId);
        context.startActivityForResult(intent, requestCode);
    }

    public static void startSumUpActivityForResult(Activity context, int param_type, String doctorId, String questionId, int requestCode) {
        Intent intent = new Intent(context, SumUpActivity.class);
        intent.putExtra(PARAM_TYPE, param_type);
        intent.putExtra(PARAM_DID, doctorId);
        intent.putExtra(PARAM_QID, questionId);
        context.startActivityForResult(intent, requestCode);
    }

    public static void startSumUpActivityForResult(Activity context, int param_type, String doctorId, String questionId, long startTime, int requestCode) {
        Intent intent = new Intent(context, SumUpActivity.class);
        intent.putExtra(PARAM_TYPE, param_type);
        intent.putExtra(PARAM_DID, doctorId);
        intent.putExtra(PARAM_QID, questionId);
        intent.putExtra(PARAM_STARTTIME, startTime);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void initView() {
        if (getIntent() != null) {
            type = getIntent().getIntExtra(PARAM_TYPE, TYPE_SUM_UP);
            doctorId = getIntent().getStringExtra(PARAM_DID);
            questionId = getIntent().getStringExtra(PARAM_QID);
            mStartTime = getIntent().getLongExtra(PARAM_STARTTIME, 0);
        }


        initViewByType();
        initTitleBar();
        etEnter_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //stone 空格不算字数处理
                if (type != TYPE_QUES && type != TYPE_ZHUIWEN) {
                    if (s != null) {
                        mStr3 = s.toString().trim();
                        if (mStr3.length() > textLimitMax) {
                            ToastUtils.shortToast("字数已超出限制");
                        }
                    } else {
                        mStr3 = "";
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //stone 空格不算字数处理 兼容之前的问诊总结
                mStr3 = s.toString().trim();

                if (type == TYPE_QUES || type == TYPE_ZHUIWEN) {

                    mChineseCount_3 = YMOtherUtils.getChineseCount(mStr3);
                    if (mChineseCount_3 > textLimitMax) {
                        ToastUtils.shortToast("字数已超出限制");
                    }
                    if (0 == mChineseCount_3) {
                        tvCount_3.setText(textLimitMin + "/" + textLimitMax);
                    } else {
                        tvCount_3.setText(mChineseCount_3 + "/" + textLimitMax);
                    }
                } else {
                    tvCount_3.setText(mStr3.length() + "/" + textLimitMax);
                }

            }
        });

        etEnter_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //stone 空格不算字数处理 兼容之前的问诊总结
                if (type != TYPE_QUES && type != TYPE_ZHUIWEN) {
                    if (s != null) {
                        mStr1 = s.toString().trim();
                        if (mStr1.length() > textLimitMax) {
                            ToastUtils.shortToast("字数已超出限制");
                        }
                    } else {
                        mStr1 = "";
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //stone 空格不算字数处理
                mStr1 = s.toString().trim();

                if (type == TYPE_QUES || type == TYPE_ZHUIWEN) {

                    mChineseCount_1 = YMOtherUtils.getChineseCount(mStr1);
                    if (mChineseCount_1 > textLimitMax) {
                        ToastUtils.shortToast("字数已超出限制");
                    }
                    if (0 == mChineseCount_1) {
                        tvCount_1.setText(textLimitMin + "/" + textLimitMax);
                    } else {
                        tvCount_1.setText(mChineseCount_1 + "/" + textLimitMax);
                    }
                    tvCount_1.setText(mChineseCount_1 + "/" + textLimitMax);

                } else {
                    tvCount_1.setText(mStr1.length() + "/" + textLimitMax);
                }
            }
        });

        etEnter_1.setOnTouchListener(this);
        etEnter_3.setOnTouchListener(this);

        //stone 获取缓存数据
        mCacheQuestionId = SPUtils.getUser().getString(Constants.QUESTION_ID);
        if (!TextUtils.isEmpty(mCacheQuestionId) && mCacheQuestionId.equals(questionId)) {
            String dataStr1 = SPUtils.getUser().getString(Constants.SUMUP_DATA1);
            String dataStr3 = SPUtils.getUser().getString(Constants.SUMUP_DATA2);
            if (!TextUtils.isEmpty(dataStr1)) {
                mStr1 = dataStr1.trim();
                etEnter_1.setText(dataStr1);
                etEnter_1.setSelection(dataStr1.length());
                tvCount_1.setText(mStr1.length() + "/" + textLimitMax);
            }
            if (!TextUtils.isEmpty(dataStr3)) {
                mStr3 = dataStr3.trim();
                etEnter_3.setText(dataStr3);
                etEnter_3.setSelection(dataStr3.length());
                tvCount_3.setText(mStr3.length() + "/" + textLimitMax);
            }
        }


        etEnter_1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPop();
                return false;
            }
        });
        etEnter_3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPop();
                return false;
            }
        });


        //添加粘贴 stone
        mClipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//        PromptViewHelper pvHelper = new PromptViewHelper(SumUpActivity.this);
//        pvHelper.setPromptViewManager(new ChatPromptViewManager(SumUpActivity.this, new String[]{"粘贴"}, Location.BOTTOM_RIGHT));
//        pvHelper.addPrompt(etEnter_1);
//        pvHelper.addPrompt(etEnter_3);
//        pvHelper.setOnItemClickListener(new PromptViewHelper.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                String resultString = "";
//                 检查剪贴板是否有内容
//                if (!mClipboardManager.hasPrimaryClip()) {
//                    Toast.makeText(SumUpActivity.this,
//                            "粘贴内容为空", Toast.LENGTH_SHORT).show();
//                } else {
//                    ClipData clipData = mClipboardManager.getPrimaryClip();
//                    int count = clipData.getItemCount();
//
//                    for (int i = 0; i < count; ++i) {
//
//                        ClipData.Item item = clipData.getItemAt(i);
//                        CharSequence str = item
//                                .coerceToText(SumUpActivity.this);
//
//                        resultString += str;
//                    }
//
//                }
//
//                if (etEnter_1.isFocused()) {
//                    Editable editable = etEnter_1.getEditableText();
//                    editable.insert(etEnter_1.getSelectionStart(), resultString);
//                } else {
//                    Editable editable = etEnter_3.getEditableText();
//                    editable.insert(etEnter_3.getSelectionStart(), resultString);
//                }
//
//            }
//        });


    }

    private void initViewByType() {
        switch (type) {
            case TYPE_SUM_UP:
                textLimitMin = DEFAULT_SUM_UP_TEXT_LIMIT_MIN_3;
                textLimitMax = DEFAULT_SUM_UP_TEXT_LIMIT_MAX;
                titleBarBuilder.setTitleText(TITLE_SUM_UP);
                break;
            case TYPE_TIP_OFF:
                etEnter_3.setHint(R.string.consult_tip_off_hint);
                textLimitMin = DEFAULT_SUM_UP_TEXT_LIMIT_MIN_3;
                textLimitMax = DEFAULT_TIP_OFF_TEXT_LIMIT_MAX;
                titleBarBuilder.setTitleText(TITLE_TIP_OFF);
                break;
            case TYPE_QUES:
                etEnter_1.setHint(R.string.question_analysis_hint);
                etEnter_3.setHint(R.string.question_avavice_hint);
                textLimitMin = DEFAULT_SUM_UP_TEXT_LIMIT_MIN_3;
                textLimitMax = DEFAULT_SUM_UP_TEXT_LIMIT_MAX;
                titleBarBuilder.setTitleText(TITLE_QUE);
                break;
            case TYPE_ZHUIWEN:
                tv_1.setText("我的回复");
                etEnter_1.setHint(R.string.ques_tip_zhuiwen_hint);
                etEnter_3.setVisibility(View.GONE);
                tvCount_3.setVisibility(View.GONE);
                tv_3.setVisibility(View.GONE);
                textLimitMin = DEFAULT_SUM_UP_TEXT_LIMIT_MIN_1;
                textLimitMax = DEFAULT_SUM_UP_TEXT_LIMIT_MAX;
                titleBarBuilder.setTitleText(TITLE_QUE);
                break;
            default:
                break;
        }
        titleBarBuilder.setIconVisibility("完成", true);
        //stone 提示默认20-200
        tvCount_3.setText(textLimitMin + "~" + textLimitMax);
        tvCount_1.setText(textLimitMin + "~" + textLimitMax);
    }

    private void initTitleBar() {
        String item_title = "";
        if (type == TYPE_SUM_UP || type == TYPE_TIP_OFF) {
            item_title = "完成";
        } else {
            item_title = "提交";
        }
        titleBarBuilder.addItem(item_title, new ItemClickListener() {
            @Override
            public void onClick() {
                if (type == TYPE_SUM_UP) {
                    if (TextUtils.isEmpty(mStr1)) {
                        ToastUtils.shortToast("请填写问题分析");
                        return;
                    }

                    if (mStr1.length() < DEFAULT_SUM_UP_TEXT_LIMIT_MIN_3 || mStr1.length() > textLimitMax) {
                        ToastUtils.shortToast("问题分析字数要求20-200字");
                        return;
                    }

                    if (TextUtils.isEmpty(mStr3)) {
                        ToastUtils.shortToast("请填写指导建议");
                        return;
                    }

                    if (mStr3.length() < DEFAULT_SUM_UP_TEXT_LIMIT_MIN_3 || mStr3.length() > textLimitMax) {
                        ToastUtils.shortToast("指导建议字数要求20-200字");
                        return;
                    }

                    //stone 统计
                    StatisticalTools.eventCount(YMApplication.getAppContext(), ConsultConstants.EVENT_QUESTION_CONCLUSION_FINISHED);

                    showLoadDataDialog();
                    ServiceProvider.doctorSummary(doctorId, questionId,
                            etEnter_3.getText().toString().trim(),
                            etEnter_1.getText().toString().trim(),
                            null,
                            new Subscriber<CommonRspEntity>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    hideProgressDialog();
                                }

                                @Override
                                public void onNext(CommonRspEntity commonRspEntity) {
                                    hideProgressDialog();
                                    if (commonRspEntity != null && commonRspEntity.getCode() == ConsultConstants.CODE_REQUEST_SUCCESS) {
                                        mIsSumUpOk = true;
                                        ToastUtils.shortToast("总结成功");
                                        Intent data = new Intent();
                                        StringBuffer stringBuffer = new StringBuffer();
                                        stringBuffer.append("问题分析："+etEnter_1.getText().toString());
                                        stringBuffer.append("建议指导："+etEnter_3.getText().toString());
                                        data.putExtra("chatData",stringBuffer.toString());
                                        setResult(ConsultChatActivity.REQUEST_CODE_SUM_UP, data);
                                        onBackPressed();
                                    } else {
                                        ToastUtils.shortToast("总结失败");
                                    }
                                }
                            });
                } else if (type == TYPE_TIP_OFF) {
                    //提交举报
                    ServiceProvider.tipOff(doctorId, questionId, etEnter_3.getText().toString().trim(), new Subscriber<CommonRspEntity>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(CommonRspEntity commonRspEntity) {
                            if (commonRspEntity != null && commonRspEntity.getCode() == ConsultConstants.CODE_REQUEST_SUCCESS) {
                                ToastUtils.shortToast("举报成功");
                                onBackPressed();
                            } else {
                                ToastUtils.shortToast("举报失败");
                            }
                        }
                    });
                } else if (type == TYPE_QUES) {
                    if (0 != mStartTime) {
                        if (System.currentTimeMillis() - mStartTime > LIMITTIME) {
                            ToastUtils.shortToast("回复超时,抢到题后清在10分钟内给出回复哦~");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    YmRxBus.notifyTimeOut();
                                }
                            }, 500);
                            return;
                        }
                    }
                    if (0 == mChineseCount_1) {
                        ToastUtils.shortToast("请填写问题分析描述");
                        return;
                    }

                    if (mChineseCount_1 < DEFAULT_SUM_UP_TEXT_LIMIT_MIN_3 || mChineseCount_1 > textLimitMax) {
                        ToastUtils.shortToast("问题分析字数要求20-200字");
                        return;
                    }

                    if (0 == mChineseCount_3) {
                        ToastUtils.shortToast("请填写指导建议");
                        return;
                    }

                    if (mChineseCount_3 < DEFAULT_SUM_UP_TEXT_LIMIT_MIN_3 || mChineseCount_3 > textLimitMax) {
                        ToastUtils.shortToast("指导建议字数要求20-200字");
                        return;
                    }
                    StatisticalTools.eventCount(SumUpActivity.this, "submitanswer");
                    Intent intent = new Intent();
                    String analysis = etEnter_1.getText().toString().trim();
                    String suggestion = etEnter_3.getText().toString().trim();
                    intent.putExtra(ANALYSIS, analysis);
                    intent.putExtra(SUGGETIION, suggestion);
                    setResult(RESULTCODE, intent);
                    finish();
                } else if (type == TYPE_ZHUIWEN) {
                    //回复帖子追问
//                    if (TextUtils.isEmpty(mStr1)) {
//                        ToastUtils.shortToast("请填写回复信息");
//                        return;
//                    }
//
//                    if (mStr1.length() < DEFAULT_SUM_UP_TEXT_LIMIT_MIN_1 || mStr1.length() > textLimitMax) {
//                        ToastUtils.shortToast("回复信息字数要求10-200字");
//                        return;
//                    }

                    if (0 == mChineseCount_1) {
                        ToastUtils.shortToast("请填写回复信息");
                        return;
                    }

                    if (mChineseCount_1 < DEFAULT_SUM_UP_TEXT_LIMIT_MIN_1 || mChineseCount_1 > textLimitMax) {
                        ToastUtils.shortToast("回复信息字数要求10-200字");
                        return;
                    }
                    StatisticalTools.eventCount(SumUpActivity.this, "submitotheranswer");
                    Intent intent = new Intent();
                    String reply = etEnter_1.getText().toString().trim();
                    intent.putExtra(REPLY, reply);
                    setResult(RESULTCODE_ZHUIWEN, intent);
                    finish();
                }
            }
        }).build();
    }

    @Override
    public boolean onKeyDown(int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {   //按下的如果是BACK，同时没有重复
            if (type == TYPE_QUES) {
                if (0 != mStartTime) {
                    if (System.currentTimeMillis() - mStartTime > LIMITTIME) {
                        ToastUtils.shortToast("回复超时,抢到题后清在10分钟内给出回复哦~");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                YmRxBus.notifyTimeOut();
                                finish();
                            }
                        }, 500);
                    }
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    //stone 缓存
    @Override
    protected void onDestroy() {
        if (type == TYPE_SUM_UP && !mIsSumUpOk) {
            SPUtils.getUser().put(Constants.QUESTION_ID, questionId);
            SPUtils.getUser().put(Constants.SUMUP_DATA1, etEnter_1.getText().toString());
            SPUtils.getUser().put(Constants.SUMUP_DATA2, etEnter_3.getText().toString());
        }
        //成功了 清楚该问题的缓存
        else if (type == TYPE_SUM_UP && mIsSumUpOk) {
            if (!TextUtils.isEmpty(mCacheQuestionId) && mCacheQuestionId.equals(questionId)) {
                SPUtils.getUser().put(Constants.QUESTION_ID, "");
                SPUtils.getUser().put(Constants.SUMUP_DATA1, "");
                SPUtils.getUser().put(Constants.SUMUP_DATA2, "");
            }
        } else if (type == TYPE_QUES || type == TYPE_ZHUIWEN) {
            SPUtils.getUser().put(Constants.QUESTION_ID, questionId);
            SPUtils.getUser().put(Constants.SUMUP_DATA1, etEnter_1.getText().toString());
            SPUtils.getUser().put(Constants.SUMUP_DATA2, etEnter_3.getText().toString());
        }
        super.onDestroy();
    }


    private void showPop() {

        if (etEnter_1.isFocused()) {
            if (!isInViewArea(etEnter_1, mPosX, mPosY)) {
                return;
            }
        } else {
            if (!isInViewArea(etEnter_3, mPosX, mPosY)) {
                return;
            }
        }


        if (mPopupWindow == null) {
            mPopupWindow = new SelectBasePopupWindow(false, this);
//            mPopupWindow.setAnimationStyle(0);
            popRoot = View.inflate(getBaseContext(), R.layout.custom_prompt_view, null);
            tv_copy = (TextView) popRoot.findViewById(R.id.tv_copy);
            tv_copy.setText("粘贴");
            tv_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }

                    String resultString = "";
                    if (!mClipboardManager.hasPrimaryClip()) {
                        Toast.makeText(SumUpActivity.this,
                                "粘贴内容为空", Toast.LENGTH_SHORT).show();
                    } else {
                        ClipData clipData = mClipboardManager.getPrimaryClip();
                        int count = clipData.getItemCount();

                        for (int i = 0; i < count; ++i) {

                            ClipData.Item item = clipData.getItemAt(i);
                            CharSequence str = item
                                    .coerceToText(SumUpActivity.this);

                            resultString += str;
                        }

                    }

                    if (etEnter_1.isFocused()) {
                        Editable editable = etEnter_1.getEditableText();
                        editable.insert(etEnter_1.getSelectionStart(), resultString);
                    } else {
                        Editable editable = etEnter_3.getEditableText();
                        editable.insert(etEnter_3.getSelectionStart(), resultString);
                    }
                }
            });
        }
        if (!mPopupWindow.isShowing()) {
            mPopupWindow.init(popRoot).showAtLocation(this.getWindow().getDecorView(), Gravity.TOP | Gravity.LEFT, (int) mPosX - popRoot.getWidth() / 2, (int) mPosY - popRoot.getHeight() - AppUtils.dpToPx(20, getResources()));
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            /**
             * 点击的开始位置
             */
            case MotionEvent.ACTION_DOWN:
                mPosX = event.getRawX();
                mPosY = event.getRawY();
                break;

            default:
                break;
        }
        /**
         *  注意返回值
         *  true：view继续响应Touch操作；
         *  false：view不再响应Touch操作，故此处若为false，只能显示起始位置，不能显示实时位置和结束位置
         */
        return false;
    }

    private boolean isInViewArea(View view, float x, float y) {
        Rect r = new Rect();
        view.getGlobalVisibleRect(r);
        if (x > r.left && x < r.right && y > r.top && y < r.bottom) {
            return true;
        }
        return false;
    }


}
