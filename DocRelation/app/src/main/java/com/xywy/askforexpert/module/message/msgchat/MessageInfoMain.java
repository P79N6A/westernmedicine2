package com.xywy.askforexpert.module.message.msgchat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.module.message.MessageInfoFragment;
import com.xywy.askforexpert.module.message.friend.Menu_CardHoder_Right_Fragment;
import com.xywy.askforexpert.widget.view.SlidingMenu;

public class MessageInfoMain extends Fragment {

    private SlidingMenu mSlidingMenu;
    private FragmentTransaction mTransaction;
    private Menu_CardHoder_Right_Fragment rightFragment;
    private MessageInfoFragment centerFragment;
    private ImageButton btn2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.card_holder_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    public void refresh() {
        centerFragment.refresh();
    }

    public void isVisibility(boolean ishide, String str) {
        if (ishide) {
            centerFragment.errorItem.setVisibility(View.GONE);
        } else {
            centerFragment.errorItem.setVisibility(View.VISIBLE);

            centerFragment.errorText.setText(str);
        }
    }

    public void initView() {

        mSlidingMenu = (SlidingMenu) getView().findViewById(R.id.slidingMenu);
        mSlidingMenu.setCanSliding(false);
        mSlidingMenu.setRightView(getActivity().getLayoutInflater().inflate(
                R.layout.right_frame, null));
        mSlidingMenu.setCenterView(getActivity().getLayoutInflater().inflate(
                R.layout.center_frame, null));
        mTransaction = getActivity().getSupportFragmentManager()
                .beginTransaction();
        rightFragment = new Menu_CardHoder_Right_Fragment();
        centerFragment = new MessageInfoFragment();
        btn2 = (ImageButton) getView().findViewById(R.id.btn2);
        mTransaction.replace(R.id.center_frame, centerFragment);
        mTransaction.replace(R.id.right_frame, rightFragment);
        mTransaction.commit();
        btn2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                mSlidingMenu.showRightView();

            }
        });
    }

    public void onResume() {
        super.onResume();
        StatisticalTools.fragmentOnResume("MessageInfoMain");
    }

    public void onPause() {
        super.onPause();
        StatisticalTools.fragmentOnPause("MessageInfoMain");
    }

}
