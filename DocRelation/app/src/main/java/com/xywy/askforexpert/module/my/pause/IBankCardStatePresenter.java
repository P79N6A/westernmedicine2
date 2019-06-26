package com.xywy.askforexpert.module.my.pause;

import com.xywy.askforexpert.appcommon.base.interfaze.IBasePresenter;

/**
 * Created by xugan on 2018/5/25.
 * 银行卡审核状态业务逻辑处理
 */

public interface IBankCardStatePresenter extends IBasePresenter {
    void getBankCardState(String userId);
}
