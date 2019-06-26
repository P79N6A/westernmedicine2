package com.xywy.askforexpert.model.media;

/**
 * 媒体号
 */
public class MediaNumberBean {
    private  String mid;
    private  String name;
    private String introduce;
    private String img;
    private String is_subscription;
    private String score;
    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getIs_subscription() {
        return is_subscription;
    }

    public void setIs_subscription(String is_subscription) {
        this.is_subscription = is_subscription;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }


}
