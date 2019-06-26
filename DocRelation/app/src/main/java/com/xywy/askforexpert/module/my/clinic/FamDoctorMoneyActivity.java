package com.xywy.askforexpert.module.my.clinic;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.uilibrary.titlebar.ItemClickListener;

import butterknife.Bind;

/**
 * 家庭医生 价格设置 stone
 *
 * @author 王鹏
 * @2015-5-26下午5:09:40 bailiangjin 重写
 * <p>
 * 2017-04-26
 */
public class FamDoctorMoneyActivity extends YMBaseActivity {

    @Bind(R.id.et_week)
    EditText et_week;

    @Bind(R.id.et_month)
    EditText et_month;


    private int week_min, week_max, month_min, month_max;

    private boolean fromApplyPage;//来自申请页面 stone

    @Override
    protected int getLayoutResId() {
        return R.layout.fam_doctor_money;
    }

    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("价格设置");
    }

    @Override
    protected void initData() {
        fromApplyPage = getIntent().getBooleanExtra(Constants.KEY_VALUE, false);

        week_min = getIntent().getIntExtra("week_min", 0);
        week_max = getIntent().getIntExtra("week_max", 0);
        month_min = getIntent().getIntExtra("month_min", 0);
        month_max = getIntent().getIntExtra("month_max", 0);

//        et_week.setText("" + week_max);
//        et_month.setText("" + month_max);
        if (YMApplication.famdocinfo.getWeek() == null) {
            et_week.setText("");
        } else {
            et_week.setText(YMApplication.famdocinfo.getWeek());
        }
        if (YMApplication.famdocinfo.getMonth() == null) {
            et_month.setText("");
        } else {
            et_month.setText("" + YMApplication.famdocinfo.getMonth());
        }

        et_week.setSelection(et_week.getText().length());
        et_month.setSelection(et_month.getText().length());

        et_week.setHint(String.format("请输入包周价格，建议区间%d-%d", week_min, week_max));
        et_month.setHint(String.format("请输入包月价格，建议区间%d-%d", month_min, month_max));

        if (YMApplication.getLoginInfo().getData().getXiaozhan().getFamily().equals(Constants.FUWU_AUDIT_STATUS_1)) {
            et_week.setEnabled(false);
            et_month.setEnabled(false);
        } else {
            et_week.setEnabled(true);
            et_month.setEnabled(true);
            titleBarBuilder.addItem("确定", new ItemClickListener() {
                @Override
                public void onClick() {

                    String weekPrice = et_week.getText().toString().trim();
                    String mothPrice = et_month.getText().toString().trim();
                    if (TextUtils.isEmpty(weekPrice)) {
                        DialogUtil.showDialog(FamDoctorMoneyActivity.this, null, getString(R.string.familydoctor_notice1), null, null, null);
                        return;
                    }

                    float weekPriceInt = Float.parseFloat(weekPrice);
                    if (weekPriceInt < week_min || weekPriceInt > week_max) {
                        DialogUtil.showDialog(FamDoctorMoneyActivity.this, null, String.format("包周价格范围为%d-%d", week_min, week_max), null, null, null);
                        return;
                    }


                    if (TextUtils.isEmpty(mothPrice)) {
                        DialogUtil.showDialog(FamDoctorMoneyActivity.this, null, getString(R.string.familydoctor_notice2), null, null, null);
                        return;
                    }

                    float monthPriceInt = Float.parseFloat(mothPrice);
                    if (monthPriceInt < month_min || monthPriceInt > month_max) {
                        DialogUtil.showDialog(FamDoctorMoneyActivity.this, null, String.format("包月价格范围为%d-%d", month_min, month_max), null, null, null);
                        return;
                    }

                    YMApplication.famdocinfo.setWeek(String.valueOf(weekPriceInt));
                    YMApplication.famdocinfo.setMonth(String.valueOf(monthPriceInt));
                    if (fromApplyPage) {
                        setResult(RESULT_OK, new Intent());
                        finish();
                    } else {
                        finish();
                    }
                }
            }).build();
        }

        et_week.addTextChangedListener(mTextWatcher);
        et_month.addTextChangedListener(mTextWatcher);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String text = s.toString();
            int len = s.toString().length();
            if (len == 1 && text.startsWith("0")) {
                s.clear();
            }
        }
    };

}
