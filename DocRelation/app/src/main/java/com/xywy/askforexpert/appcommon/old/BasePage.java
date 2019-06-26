package com.xywy.askforexpert.appcommon.old;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * 项目名称：D_Platform
 * 类名称：BasePage
 * 类描述：封装了page 提供给子pager实现
 * 创建人：shihao
 * 创建时间：2015-8-10 下午4:22:20
 * 修改备注：
 */
@Deprecated
public abstract class BasePage {

    public Context context;
    private View view;

    public BasePage(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        view = initView(inflater);
    }

    public View getRootView() {
        return view;
    }

    public abstract View initView(LayoutInflater inflater);

    public abstract void initData();
}
