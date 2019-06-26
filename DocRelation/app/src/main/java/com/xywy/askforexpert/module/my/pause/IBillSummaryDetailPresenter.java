package com.xywy.askforexpert.module.my.pause;

import com.xywy.askforexpert.appcommon.base.interfaze.IBasePresenter;

/**
 * Created by xugan on 2018/6/22.
 */

public interface IBillSummaryDetailPresenter extends IBasePresenter {
    void getBillSummaryDetail(String userid, int month);
}
