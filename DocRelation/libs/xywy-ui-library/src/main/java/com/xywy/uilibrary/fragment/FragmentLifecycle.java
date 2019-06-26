package com.xywy.uilibrary.fragment;

import android.os.Bundle;
import android.view.View;

/**
 * Created by bailiangjin on 2017/1/18.
 */

public interface FragmentLifecycle {

    /**
     * 设置layout ResId
     *
     * @return ResId
     */
     int getLayoutResId();


     void beforeViewBind();

    /**
     * BaseFragment 添加view绑定使用 具体Fragment不需要Override该方法
     *
     * @param view
     */
     void bindView(View view);

     void initView();

     void initData(Bundle savedInstanceState);

     void unBindView();
}
