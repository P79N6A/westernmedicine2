package com.xywy.askforexpert.model.discussDetail;

/**
 * Compiler: Android Studio
 * Project: D_Platform
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2015/12/21 18:14
 */
public class DataShare {
    private String share_source;
    private String share_link;
    private String share_title;
    private String share_url;
    private String share_img;

    public String getShare_source() {
        return share_source;
    }

    public void setShare_source(String share_source) {
        this.share_source = share_source;
    }

    public String getShare_link() {
        return share_link;
    }

    public void setShare_link(String share_link) {
        this.share_link = share_link;
    }

    public String getShare_title() {
        return share_title;
    }

    public void setShare_title(String share_title) {
        this.share_title = share_title;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getShare_img() {
        return share_img;
    }

    public void setShare_img(String share_img) {
        this.share_img = share_img;
    }

    @Override
    public String toString() {
        return "DataShare{" +
                "share_source='" + share_source + '\'' +
                ", share_link='" + share_link + '\'' +
                ", share_title='" + share_title + '\'' +
                ", share_url='" + share_url + '\'' +
                ", share_img='" + share_img + '\'' +
                '}';
    }
}
