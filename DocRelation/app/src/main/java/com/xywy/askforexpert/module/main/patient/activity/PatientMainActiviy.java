package com.xywy.askforexpert.module.main.patient.activity;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.module.main.diagnose.PatientCenterFragment;
import com.xywy.askforexpert.module.main.diagnose.Patient_Group_ManagerActivity;
import com.xywy.askforexpert.widget.view.SelectBasePopupWindow;
import com.xywy.uilibrary.titlebar.ItemClickListener;

/**
 * 患者管理 stone
 *
 * @author 王鹏
 * @2015-5-30上午10:28:32
 */
public class PatientMainActiviy extends YMBaseActivity {
    //    private SlidingMenu mSlidingMenu;
    private FragmentTransaction mTransaction;
    //    private Menu_PatientList_Right_Fragment rightFragment;
    private PatientCenterFragment centerFragment;
//    private ImageButton btn2;

    private View lay1;
    private View lay2;
    private View popRoot;

    private SelectBasePopupWindow mPopupWindow;


    @Override
    protected void initData() {

    }


    @Override
    protected void initView() {
//        hideCommonBaseTitle();
//        ((TextView) findViewById(R.id.tv_title)).setText("患者管理");
//        ((ImageView) findViewById(R.id.btn2)).setImageResource(R.drawable.service_topque_right_btn);
//        findViewById(R.id.btn2).setVisibility(View.VISIBLE);

        titleBarBuilder.setTitleText("患者管理").addItem("",R.drawable.service_topque_right_btn, new ItemClickListener() {
            @Override
            public void onClick() {
              showPop();
            }
        }).build();


//        mSlidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
//        mSlidingMenu.setCanSliding(false);
//        mSlidingMenu.setRightView(getLayoutInflater().inflate(
//                R.layout.right_frame, null));
//        mSlidingMenu.setCenterView(getLayoutInflater().inflate(
//                R.layout.center_frame, null));
        mTransaction = this.getSupportFragmentManager().beginTransaction();
//        rightFragment = new Menu_PatientList_Right_Fragment();
        centerFragment = new PatientCenterFragment();

        mTransaction.replace(R.id.layout_container, centerFragment);
//        mTransaction.replace(R.id.right_frame, rightFragment);
        mTransaction.commitAllowingStateLoss();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.patient_main;
    }


    private void showPop() {
        if (mPopupWindow == null) {
            mPopupWindow = new SelectBasePopupWindow(true, this);
            popRoot = View.inflate(getBaseContext(), R.layout.pop_layout_patient_manage, null);
            lay1 = popRoot.findViewById(R.id.lay1);
            lay2 = popRoot.findViewById(R.id.lay2);
            lay1.setOnClickListener(mPopOnClickListener);
            lay2.setOnClickListener(mPopOnClickListener);
        }
        if (!mPopupWindow.isShowing()) {
//            mPopupWindow.init(popRoot).showAtLocation(this.getWindow().getDecorView(), Gravity.TOP | Gravity.RIGHT, (AppUtils.dpToPx(48, getResources()) - 24) / 2 + AppUtils.dpToPx(5, getResources()) - 30 - 27, AppUtils.dpToPx(48, getResources()) + YMApplication.getStatusBarHeight() - 30);
            mPopupWindow.init(popRoot).showAtLocation(this.getWindow().getDecorView(), Gravity.TOP | Gravity.RIGHT, AppUtils.dpToPx(15, getResources()), AppUtils.dpToPx(48 + 5, getResources()) + YMApplication.getStatusBarHeight() - 30);
        }
    }

    /**
     * pop监听器 种族
     */
    private View.OnClickListener mPopOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mPopupWindow != null && mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
            switch (view.getId()) {
                case R.id.lay1:
                    if (YMUserService.isGuest()) {
                        DialogUtil.LoginDialog(PatientMainActiviy.this);
                    } else {
                        Intent intent = new Intent(PatientMainActiviy.this,
                                Patient_Group_ManagerActivity.class);
                        intent.putExtra("type", "gruoup");
                        startActivity(intent);
                    }
                    break;
                case R.id.lay2:
                    StatisticalTools.eventCount(PatientMainActiviy.this, "Addpatient");
                    if (YMUserService.isGuest()) {
                        DialogUtil.LoginDialog(PatientMainActiviy.this);
                    } else {
                        Intent intent = new Intent(PatientMainActiviy.this, AddNewPatientActivity.class);
                        startActivity(intent);
                    }
                    break;
                default:
                    break;
            }
        }

    };

}
