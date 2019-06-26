package com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity;

/**
 * Created by wkteam on 2017/4/12.
 */

public class CertUploadEntity {
    private String text;
    private String imgUrl;

    public CertUploadEntity() {

    }

    public CertUploadEntity(String imgUrl, String text) {
        this.text = text;
        this.imgUrl = imgUrl;
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
