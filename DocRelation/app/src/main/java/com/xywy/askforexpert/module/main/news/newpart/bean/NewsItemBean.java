package com.xywy.askforexpert.module.main.news.newpart.bean;

import com.xywy.askforexpert.model.main.CommonNewsItemBean;

/**
 * Created by bailiangjin on 2017/1/3.
 */

public class NewsItemBean {

    private CommonNewsItemBean commonNewsItemBean;

    public NewsItemBean(CommonNewsItemBean commonNewsItemBean) {
        this.commonNewsItemBean = commonNewsItemBean;
    }


    public CommonNewsItemBean getCommonNewsItemBean() {
        return commonNewsItemBean;
    }
}
