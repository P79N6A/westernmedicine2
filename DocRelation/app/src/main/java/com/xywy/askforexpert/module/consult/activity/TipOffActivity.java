package com.xywy.askforexpert.module.consult.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.model.consultentity.CommonRspEntity;
import com.xywy.askforexpert.module.consult.ConsultConstants;
import com.xywy.askforexpert.module.consult.ServiceProvider;
import com.xywy.uilibrary.titlebar.ItemClickListener;

import butterknife.Bind;
import rx.Subscriber;

/**
 * Created by zhangzheng on 2017/5/3. stone 移植yimai
 */

public class TipOffActivity extends YMBaseActivity {
    @Bind(R.id.et_sum_up_enter)
    EditText etEnter;
    @Bind(R.id.tv_sum_up_text_count)
    TextView tvCount;

    public static final int TYPE_TIP_OFF = 0; //当前是举报的界面
    public static final int TYPE_SUM_UP = 1;  //当前是总结的界面

    public static final String PARAM_RESULT_DATA = "PARAM_RESULT_DATA";

    private static final String PARAM_TYPE = "PARAM_TYPE"; //举报或总结
    private static final String PARAM_DID = "PARAM_DID";  //医生ID
    private static final String PARAM_QID = "PARAM_QID";  //问题ID

    //字数限制
    private static final int DEFAULT_TIP_OFF_TEXT_LIMIT_MIN = 10;
    private static final int DEFAULT_TIP_OFF_TEXT_LIMIT_MAX = 50;
    private static final int DEFAULT_SUM_UP_TEXT_LIMIT_MIN = 25;
    private static final int DEFAULT_SUM_UP_TEXT_LIMIT_MAX = 200;

    private static final String TITLE_SUM_UP = "总结";
    private static final String TITLE_TIP_OFF = "举报";


    private int type = TYPE_SUM_UP;
    private int textLimitMin;
    private int textLimitMax;
    private String doctorId;
    private String questionId;
    private String sumUpContent;

    public static void startTipOffActivity(Context context, String doctorId, String questionId) {
        Intent intent = new Intent(context, TipOffActivity.class);
        intent.putExtra(PARAM_TYPE, TYPE_TIP_OFF);
        intent.putExtra(PARAM_DID, doctorId);
        intent.putExtra(PARAM_QID, questionId);
        context.startActivity(intent);

    }

    @Override
    protected void initView() {
        if (getIntent() != null) {
            type = getIntent().getIntExtra(PARAM_TYPE, TYPE_SUM_UP);
            doctorId = getIntent().getStringExtra(PARAM_DID);
            questionId = getIntent().getStringExtra(PARAM_QID);
        }
        initViewByType();
        initTitleBar();
        etEnter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > textLimitMax) {
                    ToastUtils.shortToast("字数已超出限制");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                tvCount.setText(((s == null || s.equals("")) ? 0 : s.length()) + "/" + textLimitMax);
            }
        });
    }

    @Override
    protected void initData() {
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_tip_off;
    }

    private void initViewByType() {
        switch (type) {
            case TYPE_SUM_UP:
                etEnter.setHint(R.string.consult_sum_up_hint);
                textLimitMax = DEFAULT_SUM_UP_TEXT_LIMIT_MAX;
                textLimitMin = DEFAULT_SUM_UP_TEXT_LIMIT_MIN;
                titleBarBuilder.setTitleText(TITLE_SUM_UP);
                break;
            case TYPE_TIP_OFF:
                etEnter.setHint(R.string.consult_tip_off_hint);
                textLimitMax = DEFAULT_TIP_OFF_TEXT_LIMIT_MAX;
                textLimitMin = DEFAULT_TIP_OFF_TEXT_LIMIT_MIN;
                titleBarBuilder.setTitleText(TITLE_TIP_OFF);
                break;
            default:
                break;
        }
        titleBarBuilder.setIconVisibility("完成", true);
        tvCount.setText("0/" + textLimitMax);
    }

    private void initTitleBar() {
        titleBarBuilder.addItem("完成", new ItemClickListener() {
            @Override
            public void onClick() {
                if (type == TYPE_SUM_UP) {
                    //提交总结
                    if (etEnter.getText().length() < textLimitMin||etEnter.getText().length()>textLimitMax) {
                        ToastUtils.shortToast("总结字数限制在25-200字");
                        return;
                    }

                    Intent data = new Intent();
                    data.putExtra(PARAM_RESULT_DATA, etEnter.getText().toString());
                    setResult(ConsultChatActivity.REQUEST_CODE_SUM_UP, data);
                    onBackPressed();
                } else if (type == TYPE_TIP_OFF) {
                    //提交举报
                    ServiceProvider.tipOff(doctorId, questionId, etEnter.getText().toString(), new Subscriber<CommonRspEntity>() {
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
                }
            }
        }).build();
    }

}
