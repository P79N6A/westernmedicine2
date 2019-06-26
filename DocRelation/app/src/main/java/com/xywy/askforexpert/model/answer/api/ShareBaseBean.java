package com.xywy.askforexpert.model.answer.api;

import com.google.gson.annotations.SerializedName;
import com.xywy.askforexpert.model.api.BaseResultBean;

/**
 * Created by bailiangjin on 16/7/13.
 */
public class ShareBaseBean extends BaseResultBean {

    @SerializedName("share_img")
    protected String share_img;
    @SerializedName("share_title")
    protected String share_text;
    @SerializedName("share_url")
    protected String share_url;

    public String getShare_img() {
        return share_img;
    }

    public void setShare_img(String share_img) {
        this.share_img = share_img;
    }

    public String getShare_text() {
        return share_text;
    }

    public void setShare_text(String share_text) {
        this.share_text = share_text;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }
}
