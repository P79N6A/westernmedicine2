package com.xywy.askforexpert.module.my.userinfo;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;

/**
 * 填写科室 stone
 */
public class FillDepartmentActivity extends YMBaseActivity {

    private EditText et_one, et_two;

    private String mDepartmentOne;
    private String mDepartmentTwo;

    public static void startActivityForResult(Activity context, String one, String two) {
        Intent intent = new Intent(context, FillDepartmentActivity.class);
        intent.putExtra(Constants.KEY_ID, one);
        intent.putExtra(Constants.KEY_POS, two);
        context.startActivityForResult(intent, Constants.REQUESTCODE_DEPARTMENT);
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fill_department_activity;
    }

    @Override
    protected void beforeViewBind() {
    }

    @Override
    public void initView() {
        hideCommonBaseTitle();
        ((TextView) findViewById(R.id.tv_title)).setText("所属科室");

        TextView btn22 = (TextView) findViewById(R.id.btn22);
        btn22.setText(getString(R.string.complete));
        btn22.setVisibility(View.VISIBLE);

        if (getIntent() != null) {
            mDepartmentOne = getIntent().getStringExtra(Constants.KEY_ID);
            mDepartmentTwo = getIntent().getStringExtra(Constants.KEY_POS);
        }

        et_one = (EditText) findViewById(R.id.et_one);
        et_two = (EditText) findViewById(R.id.et_two);

        if (!TextUtils.isEmpty(mDepartmentOne)) {
            et_one.setText(mDepartmentOne);
            et_one.setSelection(mDepartmentOne.length());
        }
        if (!TextUtils.isEmpty(mDepartmentTwo)) {
            et_two.setText(mDepartmentTwo);
            et_two.setSelection(mDepartmentTwo.length());
        }
    }

    @Override
    protected void initData() {
    }


    //点击事件
    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.btn2:
            case R.id.btn22:
                mDepartmentOne = et_one.getText().toString().trim();
                mDepartmentTwo = et_two.getText().toString().trim();
                if (TextUtils.isEmpty(mDepartmentOne)) {
                    ToastUtils.shortToast("请填写一级科室");
                } else if (TextUtils.isEmpty(mDepartmentTwo)) {
                    ToastUtils.shortToast("请填写二级科室");
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.KEY_VALUE, mDepartmentOne.replaceAll(Constants.DIVIDERS, "") + Constants.DIVIDERS + mDepartmentTwo.replaceAll(Constants.DIVIDERS, ""));
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;

            default:
                break;
        }
    }

}
