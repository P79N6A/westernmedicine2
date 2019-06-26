package com.xywy.askforexpert.appcommon.old;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 项目名称：D_Platform
 * 类名称：BaseFragment
 * 类描述：fragment 基类 封装了fragment
 * 创建人：shihao
 * 创建时间：2015-8-10 下午4:21:47
 * 修改备注：
 */
@Deprecated
public abstract class BaseFragment extends Fragment {
    public View view;
    public Context context;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = initView(inflater, container, savedInstanceState);
        return view;
    }

    public View getRootView() {
        return view;
    }

    /**
     * 初始化view
     */
    public abstract View initView(LayoutInflater inflater, ViewGroup container,
                                  Bundle savedInstanceState);

    /**
     * 初始化数据
     */
    public abstract void initData(Bundle savedInstanceState);

}
