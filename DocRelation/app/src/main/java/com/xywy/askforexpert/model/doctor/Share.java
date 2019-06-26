package com.xywy.askforexpert.model.doctor;

import java.io.Serializable;

public class Share implements Serializable {
    //	 "share_source": "1",
//     "share_link": "35192",
//     "share_img": "http://static.i1.xywy.com/club/20150327/mpzx/5514ea828556f.gif",
//     "share_title": "黑龙江三甲医院医师可多点执业",
//     "share_url": "http://club.xywy.com/zixun/d35192.html?xywyfrom=h5"
    public String share_source;
    public String share_link;
    public String share_title;
    public String share_url;
    public String share_img;
    public String share_other;

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

    public String getShare_other() {
        return share_other;
    }

    public void setShare_other(String share_other) {
        this.share_other = share_other;
    }
}
