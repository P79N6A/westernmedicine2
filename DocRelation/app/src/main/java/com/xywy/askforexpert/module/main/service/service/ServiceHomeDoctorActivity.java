package com.xywy.askforexpert.module.main.service.service;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.module.my.clinic.FamDoctorSetingActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 功能：服务-家庭医生 stone
 * </p>
 *
 * @author liuxuejiao
 * @2015-5-11 下午 16:27:30
 */
public class ServiceHomeDoctorActivity extends FragmentActivity implements
        View.OnClickListener {
    private ViewPager mViewPager;
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
     * mSignOderFragment: 签约订单fragment
     */
    private HomeDoctorSignOderFragment mSignOderFragment;
    /**
     * mCommentFragment: 用户评价fragment
     */
    private HomeDoctorCommentFragment mCommentFragment;
    /**
     * fragments: fragment集合
     */
    private List<Fragment> fragments;
    /**
     * btn_tab: tab按钮集合
     */
    private Button btn_tab[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_service_homedoctor);
        CommonUtils.initSystemBar(this);
        initview();
        initUtils();
        initListener();
        initData();

    }

    /**
     * <p>
     * 功能：注册监听器
     * </p>
     *
     * @author 刘雪娇
     * @version
     * @2015-5-12 上午10:37:51
     */
    private void initListener() {
        // TODO Auto-generated method stub
        mBtnBack.setOnClickListener(this);
        mBtnSetting.setOnClickListener(this);
        btn_tab[0].setOnClickListener(this);
        btn_tab[1].setOnClickListener(this);
    }

    /**
     * <p>
     * 功能：初始化工具
     * </p>
     *
     * @author 刘雪娇
     * @version
     * @2015-5-12 上午11:18:51
     */
    private void initUtils() {
        // TODO Auto-generated method stub
    }

    /**
     * <p>
     * 功能：初始化view
     * </p>
     *
     * @author 刘雪娇
     * @version
     * @2015-5-11 上午10:37:51
     */
    private void initview() {
        mViewPager = (ViewPager) findViewById(R.id.home_doctor_fragment_container);
        mBtnBack =  findViewById(R.id.btn1);
        mBtnSetting = (ImageView) findViewById(R.id.btn2);
        mTxtTitle = (TextView) findViewById(R.id.tv_title);
        mSignOderFragment = new HomeDoctorSignOderFragment();
        mCommentFragment = new HomeDoctorCommentFragment();
        fragments = new ArrayList<Fragment>();
        fragments.add(mSignOderFragment);
        fragments.add(mCommentFragment);

        btn_tab = new Button[2];
        btn_tab[0] = (Button) findViewById(R.id.radio_home_doctor_sign_orders);
        btn_tab[1] = (Button) findViewById(R.id.radio_home_doctor_user_comment);

    }

    /**
     * <p>
     * 功能：初始化数据
     * </p>
     *
     * @author 刘雪娇
     * @version
     * @2015-5-12 上午11:37:51
     */
    private void initData() {
        mTxtTitle.setText(R.string.service_home_doctor_txt);
        mBtnSetting.setVisibility(View.VISIBLE);
        mBtnSetting
                .setImageResource(R.drawable.service_homedoctor_setting);
        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(),
                fragments));
        onClick(btn_tab[0]);
        btn_tab[0].setSelected(true);
        btn_tab[1].setSelected(false);
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.radio_home_doctor_sign_orders:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.radio_home_doctor_user_comment:
                mViewPager.setCurrentItem(1);
                break;

            case R.id.btn1:
                this.finish();
                break;
            case R.id.btn2:
                // 设置-我的诊所页面
                Intent intent = new Intent(ServiceHomeDoctorActivity.this,
                        FamDoctorSetingActivity.class);
                startActivity(intent);
                break;
        }

    }

    public class FragmentAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentArray;

        public FragmentAdapter(FragmentManager fm,
                               List<Fragment> fragmentArray) {
            this(fm);
            this.fragmentArray = fragmentArray;
        }

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return this.fragmentArray.get(arg0);
        }

        @Override
        public int getCount() {
            return this.fragmentArray.size();
        }
    }

    /**
     * 页卡切换监听
     */
    public class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {

            if (arg0 == 0) {
                btn_tab[0].setSelected(true);
                btn_tab[1].setSelected(false);
                mViewPager.scrollTo(0, 0);
            } else {
                btn_tab[0].setSelected(false);
                btn_tab[1].setSelected(true);
                mViewPager.scrollTo(mViewPager.getCurrentItem(), 0);
            }

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
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
