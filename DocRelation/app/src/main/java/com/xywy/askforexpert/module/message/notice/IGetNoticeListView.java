package com.xywy.askforexpert.module.message.notice;

import com.xywy.askforexpert.appcommon.base.interfaze.IBaseView;

/**
 * Created by xugan on 2018/5/28.
 */

public interface IGetNoticeListView extends IBaseView {

    void showProgressBar();

    void hideProgressBar();

    void showToast(String str);
}
