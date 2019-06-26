package com.xywy.askforexpert.model.main.promotion;

import com.xywy.askforexpert.model.api.BaseResultBean;

/**
 * 晋级页信息 Bean
 * Created by bailiangjin on 2016/10/21.
 */
public class PromotionPageBean extends BaseResultBean {


    private PromotionBean data;


    public PromotionBean getData() {
        return data;
    }

    public void setData(PromotionBean data) {
        this.data = data;
    }


}
