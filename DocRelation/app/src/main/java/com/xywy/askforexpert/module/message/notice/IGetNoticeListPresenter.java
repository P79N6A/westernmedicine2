package com.xywy.askforexpert.module.message.notice;

import com.xywy.askforexpert.appcommon.base.interfaze.IBasePresenter;

/**
 * Created by xugan on 2018/5/28.
 */

public interface IGetNoticeListPresenter extends IBasePresenter {
    void getNoticeList(String userId);
}
