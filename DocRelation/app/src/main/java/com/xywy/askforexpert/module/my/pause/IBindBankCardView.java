package com.xywy.askforexpert.module.my.pause;

import com.xywy.askforexpert.appcommon.base.interfaze.IBaseView;

/**
 * Created by xugan on 2018/5/25.
 */

public interface IBindBankCardView extends IBaseView{

    void showProgressBar();

    void hideProgressBar();

    void showDialogue(String msg);

    void showToast(String str);

}
