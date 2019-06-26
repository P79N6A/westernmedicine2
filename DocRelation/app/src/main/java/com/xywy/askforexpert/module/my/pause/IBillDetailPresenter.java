package com.xywy.askforexpert.module.my.pause;

import com.xywy.askforexpert.appcommon.base.interfaze.IBasePresenter;

/**
 * Created by xugan on 2018/6/21.
 */

public interface IBillDetailPresenter extends IBasePresenter {
    void getBillDetail(String userid,int month,int mtype,int page,int psize);
}
