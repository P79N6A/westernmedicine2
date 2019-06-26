package com.xywy.askforexpert.module.liveshow.bean;

/**
 * Created by bailiangjin on 2017/3/6.
 */

public class LiveShowListTabBean {
    private String titleStr;
    private String state;
    private String cate_id;

    public LiveShowListTabBean(String titleStr, String state, String cate_id) {
        this.titleStr = titleStr;
        this.state = state;
        this.cate_id = cate_id;
    }

    public String getCate_id() {
        return cate_id;
    }

    public void setCate_id(String cate_id) {
        this.cate_id = cate_id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTitleStr() {
        return titleStr;
    }

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }
}
