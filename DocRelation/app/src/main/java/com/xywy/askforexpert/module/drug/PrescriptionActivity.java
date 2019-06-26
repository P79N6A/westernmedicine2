package com.xywy.askforexpert.module.drug;

import android.content.Intent;
import android.os.Bundle;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;

/**
 * 处方详情/确认处方
 * Created by xugan on 2018/7/9.
 */
public class PrescriptionActivity extends YMBaseActivity {

    private PrescriptionActivityFragment fragment;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_recipe_detail;
    }

    private void initTitle() {
        titleBarBuilder.setTitleText("开处方");
    }

    @Override
    protected void initView() {
        //初始化统一bar，将系统栏的背景设置成透明色
        CommonUtils.initSystemBar(this);
        Intent intent = getIntent();
        String patientName = intent.getStringExtra(Constants.INTENT_KEY_NAME);
        String patientSex = intent.getStringExtra(Constants.INTENT_KEY_SEX);
        String year = intent.getStringExtra(Constants.INTENT_KEY_YEAR);
        String month = intent.getStringExtra(Constants.INTENT_KEY_MONTH);
        String day = intent.getStringExtra(Constants.INTENT_KEY_DAY);
        String department = intent.getStringExtra(Constants.INTENT_KEY_DEPARTMENT);
        String uid = intent.getStringExtra(Constants.INTENT_KEY_UID);
        String time = intent.getStringExtra(Constants.INTENT_KEY_TIME);
        String  questionid = intent.getStringExtra(Constants.INTENT_KEY_QID);
        fragment = new PrescriptionActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.INTENT_KEY_NAME, patientName);
        bundle.putString(Constants.INTENT_KEY_SEX, patientSex);
        bundle.putString(Constants.INTENT_KEY_YEAR, year);
        bundle.putString(Constants.INTENT_KEY_MONTH, month);
        bundle.putString(Constants.INTENT_KEY_DAY, day);
        bundle.putString(Constants.INTENT_KEY_DEPARTMENT, department);
        bundle.putString(Constants.INTENT_KEY_UID, uid);
        bundle.putString(Constants.INTENT_KEY_TIME, time);
        bundle.putString(Constants.INTENT_KEY_QID, questionid);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
        initTitle();
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragment.onActivityResult(requestCode, resultCode, data);
    }
}
