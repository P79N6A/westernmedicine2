package com.xywy.askforexpert.module.docotorcirclenew.requestbean;

/**
 * Created by bailiangjin on 2016/11/9.
 */

public class TopicParamBean {

    private String id;
    private String name;


    public TopicParamBean(String id, String name) {
        this.id = id;
        this.name = name;
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
}
