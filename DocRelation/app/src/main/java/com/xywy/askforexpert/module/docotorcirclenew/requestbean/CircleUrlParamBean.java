package com.xywy.askforexpert.module.docotorcirclenew.requestbean;

/**
 * Created by bailiangjin on 2016/11/9.
 */

public class CircleUrlParamBean {

    private String id;
    private String url;


    public CircleUrlParamBean(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
