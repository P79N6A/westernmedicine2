package com.xywy.askforexpert.module.consult.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.uilibrary.titlebar.ItemClickListener;

import butterknife.Bind;

/**
 * 添加或者编辑文本信息 stone
 */
public class AddOrEditTextInfoActivity extends YMBaseActivity {
    @Bind(R.id.et1)
    EditText et1;
    @Bind(R.id.tv_count1)
    TextView tv_count1;

    private static final int LIMIT_MIN_10 = 10;//最小字数
    private static final int LIMIT_MIN_20 = 20;//最小字数

    private static final int LIMIT_MAX_1000 = 1000;//最大字数
    private static final int LIMIT_MAX_50 = 50;//最大字数
    private static final int LIMIT_MAX_200 = 200;//最大字数
    private static final int LIMIT_MAX_2000 = 2000;//最大字数

    private static final String PARAM_TYPE = "PARAM_TYPE"; //类型
    private static final String PARAM_TEXT = "PARAM_TEXT"; //内容
    private static final String PARAM_CHANGE = "PARAM_CHANGE"; //是否能改变

    private static final int TYPE_GOOD_AT_APPLY_FAMILY_DOCTOR = 4; //当前擅长疾病页(申请家庭医生) 20-200个汉字
    private static final int TYPE_GOOD_AT = 0; //当前擅长疾病页
    private static final int TYPE_PERSONAL_DESC = 1;  //当前是个人简介页
    private static final int TYPE_DOCTOR_HOPE = 2;  //当前是医生寄语
    private static final int TYPE_PERSONAL_HONOR = 3;  //当前是个人荣誉

    private static final String TITLE_GOOD_AT = "擅长疾病";
    private static final String TITLE_PERSONAL_DESC = "个人简介";
    private static final String TITLE_DOCTOR_HOPE = "医生寄语"; //(申请家庭医生) 20-50个汉字
    private static final String TITLE_PERSONAL_HONOR = "个人荣誉";//(申请家庭医生) 10-200个汉字

    private boolean mCanChange;
    private int mType;
    private int mTextLimitMax;
    private int mTextLimitMin;
    private String mStr1 = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_add_or_edit_textinfo;
    }

    @Override
    protected void initData() {
    }

    public static void startGoodAtForFamilyDoctorActivityForResult(Activity context, String content, boolean isCanChange) {
        Intent intent = new Intent(context, AddOrEditTextInfoActivity.class);
        intent.putExtra(PARAM_TYPE, TYPE_GOOD_AT_APPLY_FAMILY_DOCTOR);
        intent.putExtra(PARAM_TEXT, content);
        intent.putExtra(PARAM_CHANGE, isCanChange);
        context.startActivityForResult(intent, Constants.REQUESTCODE_GOOD_AT_FOR_FAMILY_DOCTOR);
    }

    public static void startGoodAtActivityForResult(Activity context, String content, boolean isCanChange) {
        Intent intent = new Intent(context, AddOrEditTextInfoActivity.class);
        intent.putExtra(PARAM_TYPE, TYPE_GOOD_AT);
        intent.putExtra(PARAM_TEXT, content);
        intent.putExtra(PARAM_CHANGE, isCanChange);
        context.startActivityForResult(intent, Constants.REQUESTCODE_GOOD_AT);
    }

    public static void startPersonalDescActivityForResult(Activity context, String content, boolean isCanChange) {
        Intent intent = new Intent(context, AddOrEditTextInfoActivity.class);
        intent.putExtra(PARAM_TYPE, TYPE_PERSONAL_DESC);
        intent.putExtra(PARAM_TEXT, content);
        intent.putExtra(PARAM_CHANGE, isCanChange);
        context.startActivityForResult(intent, Constants.REQUESTCODE_PERSONAL_DESC);
    }

    public static void startPersonalHonorActivityForResult(Activity context, String content, boolean isCanChange) {
        Intent intent = new Intent(context, AddOrEditTextInfoActivity.class);
        intent.putExtra(PARAM_TYPE, TYPE_PERSONAL_HONOR);
        intent.putExtra(PARAM_TEXT, content);
        intent.putExtra(PARAM_CHANGE, isCanChange);
        context.startActivityForResult(intent, Constants.REQUESTCODE_PERSONAL_HONOR);
    }

    public static void startDoctorHopeActivityForResult(Activity context, String content, boolean isCanChange) {
        Intent intent = new Intent(context, AddOrEditTextInfoActivity.class);
        intent.putExtra(PARAM_TYPE, TYPE_DOCTOR_HOPE);
        intent.putExtra(PARAM_TEXT, content);
        intent.putExtra(PARAM_CHANGE, isCanChange);
        context.startActivityForResult(intent, Constants.REQUESTCODE_DOCTOR_HOPE);
    }

    @Override
    protected void initView() {
        if (getIntent() != null) {
            mType = getIntent().getIntExtra(PARAM_TYPE, TYPE_GOOD_AT);
            mStr1 = getIntent().getStringExtra(PARAM_TEXT);
            //个人中心进来只能查看,不能编辑
            mCanChange = getIntent().getBooleanExtra(PARAM_CHANGE, false);
        }
        initViewByType();
        initTitleBar();

        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mStr1 = s.toString().trim();

                //汉字限制 stone
                if (mType == TYPE_GOOD_AT_APPLY_FAMILY_DOCTOR
                        || mType == TYPE_DOCTOR_HOPE
                        || mType == TYPE_PERSONAL_HONOR) {

                    if (YMOtherUtils.getChineseCount(mStr1) > mTextLimitMax) {
                        ToastUtils.shortToast("字数已超出限制");
                    }

                    tv_count1.setText(YMOtherUtils.getChineseCount(mStr1) + "/" + mTextLimitMax);

                } else {

                    if (mStr1.length() > mTextLimitMax) {
                        ToastUtils.shortToast("字数已超出限制");
                    }
                    tv_count1.setText(mStr1.length() + "/" + mTextLimitMax);
                }


            }
        });

        //设置不可编辑
        if (!mCanChange) {
            et1.setFocusable(false);
            et1.setFocusableInTouchMode(false);
            tv_count1.setVisibility(View.GONE);
        }
        if (mType==TYPE_PERSONAL_DESC) {
            TextView tv_count1 = (TextView) findViewById(R.id.tv_count1);
            tv_count1.setText("0/2000");
        }
    }

    private void initViewByType() {
        switch (mType) {
            case TYPE_GOOD_AT_APPLY_FAMILY_DOCTOR:
                et1.setHint(R.string.hint_goodat_family_doctor);
                mTextLimitMin = LIMIT_MIN_20;
                mTextLimitMax = LIMIT_MAX_200;
                titleBarBuilder.setTitleText(TITLE_GOOD_AT);
//                tv_count1.setVisibility(View.GONE);
                break;
            case TYPE_GOOD_AT:
                et1.setHint(R.string.hint_goodat);
                mTextLimitMin = LIMIT_MIN_10;
                mTextLimitMax = LIMIT_MAX_1000;
                titleBarBuilder.setTitleText(TITLE_GOOD_AT);
                break;
            case TYPE_PERSONAL_DESC:
                et1.setHint(R.string.hint_personal_desc);
                mTextLimitMin = LIMIT_MIN_10;
                mTextLimitMax = LIMIT_MAX_2000;
                titleBarBuilder.setTitleText(TITLE_PERSONAL_DESC);
                break;
            case TYPE_PERSONAL_HONOR:
                et1.setHint(R.string.hint_personal_honor);
                mTextLimitMin = LIMIT_MIN_10;
                mTextLimitMax = LIMIT_MAX_200;
                titleBarBuilder.setTitleText(TITLE_PERSONAL_HONOR);
//                tv_count1.setVisibility(View.GONE);
                break;
            case TYPE_DOCTOR_HOPE:
                et1.setHint(R.string.hint_doctor_hope);
                mTextLimitMin = LIMIT_MIN_20;
                mTextLimitMax = LIMIT_MAX_50;
                titleBarBuilder.setTitleText(TITLE_DOCTOR_HOPE);
//                tv_count1.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        if (!TextUtils.isEmpty(mStr1)) {
            et1.setText(mStr1);
            et1.setSelection(mStr1.length());
            //汉字限制 stone
            if (mType == TYPE_GOOD_AT_APPLY_FAMILY_DOCTOR
                    || mType == TYPE_DOCTOR_HOPE
                    || mType == TYPE_PERSONAL_HONOR) {
                tv_count1.setText(YMOtherUtils.getChineseCount(mStr1) + "/" + mTextLimitMax);
            } else {
                tv_count1.setText(mStr1.length() + "/" + mTextLimitMax);
            }
        } else {
            tv_count1.setText(mTextLimitMin + "~" + mTextLimitMax);
        }
        titleBarBuilder.setIconVisibility("完成", true);
    }

    private void initTitleBar() {
        String item_title = "";
        if (mType == TYPE_GOOD_AT
                || mType == TYPE_PERSONAL_DESC
                || mType == TYPE_PERSONAL_HONOR
                || mType == TYPE_DOCTOR_HOPE
                || mType == TYPE_GOOD_AT_APPLY_FAMILY_DOCTOR) {
            item_title = "保存";
        }

        //显示提交
        if (mCanChange) {
            titleBarBuilder.addItem(item_title, new ItemClickListener() {
                @Override
                public void onClick() {
                    if (mType == TYPE_GOOD_AT_APPLY_FAMILY_DOCTOR) {
                        if (TextUtils.isEmpty(mStr1)) {
                            ToastUtils.shortToast("请填写擅长疾病");
                            return;
                        }

                        if (YMOtherUtils.getChineseCount(mStr1) < mTextLimitMin || YMOtherUtils.getChineseCount(mStr1) > mTextLimitMax) {
                            ToastUtils.shortToast("擅长疾病字数要求20-200字");
                            return;
                        }

                        Intent intent = new Intent();
                        intent.putExtra(Constants.KEY_VALUE, mStr1);
                        AddOrEditTextInfoActivity.this.setResult(RESULT_OK, intent);
                        AddOrEditTextInfoActivity.this.finish();
//                    showLoadDataDialog(); //加载框

                    } else if (mType == TYPE_GOOD_AT) {
                        if (TextUtils.isEmpty(mStr1)) {
                            ToastUtils.shortToast("请填写擅长疾病");
                            return;
                        }

                        if (mStr1.length() < mTextLimitMin || mStr1.length() > mTextLimitMax) {
                            ToastUtils.shortToast("擅长疾病字数要求10-1000字");
                            return;
                        }

                        Intent intent = new Intent();
                        intent.putExtra(Constants.KEY_VALUE, mStr1);
                        AddOrEditTextInfoActivity.this.setResult(RESULT_OK, intent);
                        AddOrEditTextInfoActivity.this.finish();
//                    showLoadDataDialog(); //加载框

                    } else if (mType == TYPE_PERSONAL_DESC) {
                        if (TextUtils.isEmpty(mStr1)) {
                            ToastUtils.shortToast("请填写个人简介");
                            return;
                        }

                        if (mStr1.length() < mTextLimitMin || mStr1.length() > mTextLimitMax) {
                            ToastUtils.shortToast("个人简介字数要求10-2000字");
                            return;
                        }

                        Intent intent = new Intent();
                        intent.putExtra(Constants.KEY_VALUE, mStr1);
                        AddOrEditTextInfoActivity.this.setResult(RESULT_OK, intent);
                        AddOrEditTextInfoActivity.this.finish();
//                    showLoadDataDialog(); //加载框
                    } else if (mType == TYPE_PERSONAL_HONOR) {
                        if (TextUtils.isEmpty(mStr1)) {
                            ToastUtils.shortToast("请填写个人荣誉");
                            return;
                        }

                        if (YMOtherUtils.getChineseCount(mStr1) < mTextLimitMin || YMOtherUtils.getChineseCount(mStr1) > mTextLimitMax) {
                            ToastUtils.shortToast("个人荣誉字数要求20-50字");
                            return;
                        }

                        Intent intent = new Intent();
                        intent.putExtra(Constants.KEY_VALUE, mStr1);
                        AddOrEditTextInfoActivity.this.setResult(RESULT_OK, intent);
                        AddOrEditTextInfoActivity.this.finish();
                    } else if (mType == TYPE_DOCTOR_HOPE) {
                        if (TextUtils.isEmpty(mStr1)) {
                            ToastUtils.shortToast("请填写医生寄语");
                            return;
                        }

                        if (YMOtherUtils.getChineseCount(mStr1) < mTextLimitMin || YMOtherUtils.getChineseCount(mStr1) > mTextLimitMax) {
                            ToastUtils.shortToast("医生寄语字数要求20-50字");
                            return;
                        }

                        Intent intent = new Intent();
                        intent.putExtra(Constants.KEY_VALUE, mStr1);
                        AddOrEditTextInfoActivity.this.setResult(RESULT_OK, intent);
                        AddOrEditTextInfoActivity.this.finish();
                    }
                }
            }).build();
        }

    }

}
