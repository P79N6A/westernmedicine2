package com.xywy.askforexpert.module.my.pause;

import com.xywy.askforexpert.appcommon.base.interfaze.IBasePresenter;

/**
 * Created by xugan on 2018/5/25.
 */

public interface IBindBankCardPresenter extends IBasePresenter {
    void bindBankCard(String card_id,String card_name,String card_dress,String card_num,String userId);
}
