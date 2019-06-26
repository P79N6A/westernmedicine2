package com.xywy.askforexpert.module.main.service.service;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.model.QueData;
import com.xywy.askforexpert.module.my.clinic.AddnumberSettingActivity;

import java.util.ArrayList;

/**
 * <p>
 * 功能：服务-预约加号 预约转诊
 * </p>
 *
 * @author liuxuejiao
 * @2015-5-13 上午 11:47:30
 */
public class ServiceMakeApp extends FragmentActivity implements OnClickListener {

    /**
     * mBtnBack: 返回按钮
     */
    private View mBtnBack;
    /**
     * mBtnSetting: 设置按钮
     */
    private ImageView mBtnSetting;
    /**
     * mTxtTitle: 标题文字
     */
    private TextView mTxtTitle;
    /**
     * mContainer: 内容容器
     */
    private FrameLayout mContainer;
    private MakeAppViewPagerFragment makeAppViewPagerFragment;
    private FragmentTransaction mTransaction;
    private int first;
    private int isJump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        MobclickAgent.openActivityDurationTrack(false);
        setContentView(R.layout.activity_make_app);
        initView();
        initUtils();
        initListener();
        initData();
    }

    private void initData() {
        // TODO Auto-generated method stub
        mTxtTitle.setText(R.string.service_make_appointment_txt);
        mBtnSetting.setVisibility(View.VISIBLE);
        mBtnSetting
                .setImageResource(R.drawable.service_homedoctor_setting);

        makeAppViewPagerFragment = new MakeAppViewPagerFragment();
        ArrayList<QueData> mLables = (ArrayList<QueData>) getIntent()
                .getSerializableExtra("mLables");

        Bundle bundle = new Bundle();
        bundle.putInt("first", first);
        bundle.putInt("isJump", isJump);
        bundle.putSerializable("mLables", mLables);
        makeAppViewPagerFragment.setArguments(bundle);

        mTransaction.replace(R.id.make_app_container, makeAppViewPagerFragment);
        mTransaction.commit();
    }

    private void initListener() {
        // TODO Auto-generated method stub
        mBtnBack.setOnClickListener(this);
        mBtnSetting.setOnClickListener(this);
    }

    private void initUtils() {
        // TODO Auto-generated method stub
        mTransaction = this.getSupportFragmentManager().beginTransaction();

    }

    private void initView() {
        // TODO Auto-generated method stub
        mBtnBack =  findViewById(R.id.btn1);
        mBtnSetting = (ImageView) findViewById(R.id.btn2);
        mTxtTitle = (TextView) findViewById(R.id.tv_title);
        mContainer = (FrameLayout) findViewById(R.id.make_app_container);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn1:
                this.finish();
                break;
            case R.id.btn2:
                Intent intent = new Intent(ServiceMakeApp.this,
                        AddnumberSettingActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    public void onResume() {
        super.onResume();
        StatisticalTools.onResume(this);
    }

    public void onPause() {
        super.onPause();
        StatisticalTools.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
