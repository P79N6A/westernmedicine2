package com.xywy.askforexpert.model;

/**
 * 类描述:
 * 创建人: shihao on 15/12/18 16:49.
 *
 * @modified Jack Fang
 */
public class Seminar {

    private String dynamicid;
    private String title;
    private int type;
    private String imageUrl;
    private String countent;

    private String link;
    private String zixunid;
    private String uu;
    private String vu;

    public String getCountent() {
        return countent;
    }

    public void setCountent(String countent) {
        this.countent = countent;
    }

    public String getDynamicid() {
        return dynamicid;
    }

    public void setDynamicid(String dynamicid) {
        this.dynamicid = dynamicid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getZixunid() {
        return zixunid;
    }

    public void setZixunid(String zixunid) {
        this.zixunid = zixunid;
    }

    public String getUu() {
        return uu;
    }

    public void setUu(String uu) {
        this.uu = uu;
    }

    public String getVu() {
        return vu;
    }

    public void setVu(String vu) {
        this.vu = vu;
    }
}
