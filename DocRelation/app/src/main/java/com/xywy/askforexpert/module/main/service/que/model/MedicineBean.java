package com.xywy.askforexpert.module.main.service.que.model;

/**
 * Created by bailiangjin on 2017/5/3.
 */

public class MedicineBean {

    private String id;
    private String name;
    private String imageUrl;
    private String h5Url;
    private String pcUrl;

    public MedicineBean(String id, String name, String imageUrl, String h5Url, String pcUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.h5Url = h5Url;
        this.pcUrl = pcUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getH5Url() {
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }

    public String getPcUrl() {
        return pcUrl;
    }

    public void setPcUrl(String pcUrl) {
        this.pcUrl = pcUrl;
    }
}
