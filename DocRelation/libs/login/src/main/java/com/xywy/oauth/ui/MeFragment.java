package com.xywy.oauth.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.oauth.R;
import com.xywy.oauth.model.LoginModel;
import com.xywy.oauth.model.UserInfoCenter;
import com.xywy.oauth.utils.SharedPreferencesHelper;
import com.xywy.oauth.utils.XYImageLoader;

/**
 * Created by bobby on 16/3/11.
 */
public class MeFragment extends Fragment implements View.OnClickListener {
    ImageView ivMeHead;
    TextView tvMeName;
    ImageView btnMessage;
    TextView meAbout;
    TextView titleName;
    TextView tvTitle;
    RelativeLayout meLogin;
    FrameLayout meMessage;
    private View mapRootView;
    private String MESSAGE_NOTIFY = "messageNotify";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mapRootView = inflater.inflate(R.layout.fragment_me_layout, container, false);
        initView();
        initListener();
        return mapRootView;
    }

    private void initView() {
        meLogin = (RelativeLayout) mapRootView.findViewById(R.id.me_login);
        meMessage = (FrameLayout) mapRootView.findViewById(R.id.me_message);
        ivMeHead = (ImageView) mapRootView.findViewById(R.id.iv_me_head);
        tvMeName = (TextView) mapRootView.findViewById(R.id.tv_me_name);
        btnMessage = (ImageView) mapRootView.findViewById(R.id.btn_message);
        meAbout = (TextView) mapRootView.findViewById(R.id.me_about);
        titleName = (TextView) mapRootView.findViewById(R.id.title_name);
        tvMeName = (TextView) mapRootView.findViewById(R.id.tv_me_name);
        tvTitle = (TextView) mapRootView.findViewById(R.id.tv_title);

        titleName.setText("我的主页");
    }

    private void initListener() {
        meAbout.setOnClickListener(this);
        meLogin.setOnClickListener(this);
        meMessage.setOnClickListener(this);
        btnMessage.setOnClickListener(this);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initPush();
    }

    private void initPush() {
        SharedPreferencesHelper sp = SharedPreferencesHelper.getIns();
        boolean notifyOpen = sp.getBoolean(MESSAGE_NOTIFY);
        if (notifyOpen) {
            btnMessage.setImageResource(R.drawable.me_switch_open);
        } else {
            btnMessage.setImageResource(R.drawable.me_switch_close);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.me_login) {
            login();
        } else if (i == R.id.me_message) {
            startActivity(new Intent(getActivity(), MessageActivity.class));

        } else if (i == R.id.btn_message) {
            newMessageNotify();

        } else if (i == R.id.me_about) {
            startActivity(new Intent(getActivity(), AboutActivity.class));

        }
    }

    private void newMessageNotify() {
        SharedPreferencesHelper sp = SharedPreferencesHelper.getIns();
        boolean notifyOpen = sp.getBoolean(MESSAGE_NOTIFY);
        if (notifyOpen) {
            sp.putBoolean(MESSAGE_NOTIFY, false);
            btnMessage.setImageResource(R.drawable.me_switch_close);
//            JPushInterface.stopPush(getContext());
//            Toast.makeText(getContext(),"关闭了",Toast.LENGTH_SHORT).show();
        } else {
            sp.putBoolean(MESSAGE_NOTIFY, true);
            btnMessage.setImageResource(R.drawable.me_switch_open);
//            JPushInterface.resumePush(getContext());
//            Toast.makeText(getContext(),"打开了",Toast.LENGTH_SHORT).show();
        }
    }

    private void login() {
        if (UserInfoCenter.getInstance().isLogin()) {
            //跳转到个人信息页面
            startActivity(new Intent(getActivity(), UserInfoActivity.class));
        } else {
            //跳转到登录页面
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UserInfoCenter.getInstance().isLogin()) {
            LoginModel model = UserInfoCenter.getInstance().getLoginModel();
            if (model.getPhoto() == null || model.getPhoto().trim().equals("")) {
                ivMeHead.setImageResource(R.drawable.icon_default_head);
            } else {
                XYImageLoader.getInstance().displayUserHeadImage(model.getPhoto(), ivMeHead);
            }
            tvMeName.setText(model.getNickname());
        } else {
            // TODO: 2016/3/18 默认头像
            ivMeHead.setImageResource(R.drawable.icon_default_head);
            tvMeName.setText("登录");
        }
    }

    private void reset() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        reset();
    }


}
