package com.xywy.askforexpert.module.discovery.medicine.common;

import android.os.Bundle;
import android.widget.Toast;

import com.xywy.base.XywyBaseActivity;

import butterknife.ButterKnife;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/3/24 10:18
 */

public abstract class MyBaseActivity extends XywyBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( getLayoutResId());
        ButterKnife.bind(this);
//        toolbar.setNavigationIcon(R.drawable.base_back_btn_selector_new);
        initView();
        initData();
    }
    protected abstract int getLayoutResId();


    protected void initView() {
    }

    protected void initData() {}


    /**
     * shortToast toast by string
     *
     * @param string
     */
    protected void shortToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    /**
     * shortToast toast by res id
     *
     * @param resId
     */
    protected void shortToast(int resId) {
        Toast.makeText(this, this.getResources().getString(resId), Toast.LENGTH_SHORT).show();
    }

    /**
     * long toast
     *
     * @param string
     */
    protected void longToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_LONG).show();
    }

    /**
     * long toast
     *
     * @param resId
     */
    protected void longToast(int resId) {
        Toast.makeText(this, this.getResources().getString(resId), Toast.LENGTH_LONG).show();
    }

}
