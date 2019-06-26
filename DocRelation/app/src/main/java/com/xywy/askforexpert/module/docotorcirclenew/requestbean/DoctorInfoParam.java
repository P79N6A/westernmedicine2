package com.xywy.askforexpert.module.docotorcirclenew.requestbean;

/**
 * Created by bailiangjin on 2016/11/9.
 */

public class DoctorInfoParam {

    private String type;
    private String userId;
    private String relation;

    public DoctorInfoParam(String type, String userId, String relation) {
        this.type = type;
        this.userId = userId;
        this.relation = relation;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}
