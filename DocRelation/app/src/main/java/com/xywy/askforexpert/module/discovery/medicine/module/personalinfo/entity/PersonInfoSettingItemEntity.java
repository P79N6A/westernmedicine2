package com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity;

/**
 * Created by zhangzheng on 2017/4/1.
 */

public class PersonInfoSettingItemEntity {
    public static final int TYPE_NORMAL = 0;
    private int type;
    private String leftText;

    public PersonInfoSettingItemEntity() {

    }

    public PersonInfoSettingItemEntity(String leftText) {
        this.leftText = leftText;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLeftText() {
        return leftText;
    }

    public void setLeftText(String leftText) {
        this.leftText = leftText;
    }
}
