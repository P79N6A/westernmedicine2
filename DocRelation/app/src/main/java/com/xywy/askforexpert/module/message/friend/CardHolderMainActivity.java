package com.xywy.askforexpert.module.message.friend;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.widget.view.SlidingMenu;

/**
 * 名片夹
 *
 * @author 王鹏
 * @2015-5-14下午2:01:24
 */
public class CardHolderMainActivity extends FragmentActivity {

    private SlidingMenu mSlidingMenu;
    private FragmentTransaction mTransaction;
    private Menu_CardHoder_Right_Fragment rightFragment;
    private CardHolderFragment centerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_holder_main);
        initView();
    }

    public void initView() {

        mSlidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
        mSlidingMenu.setCanSliding(false);
        mSlidingMenu.setRightView(getLayoutInflater().inflate(
                R.layout.right_frame, null));
        mSlidingMenu.setCenterView(getLayoutInflater().inflate(
                R.layout.center_frame, null));
        mTransaction = this.getSupportFragmentManager().beginTransaction();
        rightFragment = new Menu_CardHoder_Right_Fragment();
        centerFragment = new CardHolderFragment();

        mTransaction.replace(R.id.center_frame, centerFragment);
        mTransaction.replace(R.id.right_frame, rightFragment);
        mTransaction.commit();

    }

    public void onClickBack(View view) {
        switch (view.getId()) {
            case R.id.btn2:
                mSlidingMenu.showRightView();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        StatisticalTools.onResume(CardHolderMainActivity.this);

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        StatisticalTools.onPause(CardHolderMainActivity.this);
    }

}
