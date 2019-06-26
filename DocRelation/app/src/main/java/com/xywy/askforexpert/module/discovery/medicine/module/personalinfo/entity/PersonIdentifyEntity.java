package com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity;


import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.adapter.PersonIdentifyAdapter;

/**
 * Created by zhangzheng on 2017/4/11.
 */

public class PersonIdentifyEntity {

    private int type = PersonIdentifyAdapter.TYPE_NORMAL;
    private String text;
    private String imgUrl;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
