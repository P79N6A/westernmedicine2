package com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity;

/**
 * Created by zhangzheng on 2017/4/1.
 */

public class PersonInfoItemEntity {
    public static final int TYPE_NORMAL = 0;
    private int type = 0;

    private int leftImgId;
    private String leftText;
    private String rightText;

    public PersonInfoItemEntity() {

    }

    public PersonInfoItemEntity(int leftImgId, String leftText, String rightText) {
        this.leftImgId = leftImgId;
        this.leftText = leftText;
        this.rightText = rightText;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLeftImgId() {
        return leftImgId;
    }

    public void setLeftImgId(int leftImgId) {
        this.leftImgId = leftImgId;
    }

    public String getLeftText() {
        return leftText;
    }

    public void setLeftText(String leftText) {
        this.leftText = leftText;
    }

    public String getRightText() {
        return rightText;
    }

    public void setRightText(String rightText) {
        this.rightText = rightText;
    }
}
