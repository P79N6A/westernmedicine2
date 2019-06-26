package com.xywy.askforexpert.module.my.pause;

import com.xywy.askforexpert.appcommon.base.interfaze.IBaseView;

/**
 * Created by xugan on 2018/5/25.
 */

public interface IBankCardStateView extends IBaseView{

    void showProgressBar();

    void hideProgressBar();

    void showToast(String str);

}
