package com.xywy.askforexpert.module.main.service.recruit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;

/**
 * 招聘中心 右侧按钮
 *
 * @author 王鹏
 * @2015-6-16下午2:40:36
 */

public class Menu_RecruitCenter_Right_Fragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_right_recruit, container,
                false);
        return v;
    }

    public void onResume() {
        super.onResume();
        StatisticalTools.fragmentOnResume("Menu_RecruitCenter_Right_Fragment");
    }

    public void onPause() {
        super.onPause();
        StatisticalTools.fragmentOnPause("Menu_RecruitCenter_Right_Fragment");
    }

}
