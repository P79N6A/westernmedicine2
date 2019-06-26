package com.xywy.askforexpert.module.drug.bean;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 2018/7/13.
 */

public class PrescriptionInfo {

    private String did;
    private String dname;
    private String uid;
    private String uname;
    private String usersex;
    private String age;
    private String diagnosis;
    private List<RrescriptionBean> drugs;
    private String usource;
    private String questionid;

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUsersex() {
        return usersex;
    }

    public void setUsersex(String usersex) {
        this.usersex = usersex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public List<RrescriptionBean> getDrugs() {
        return drugs;
    }

    public void setDrugs(List<RrescriptionBean> drugs) {
        this.drugs = drugs;
    }

    public String getUsource() {
        return usource;
    }

    public void setUsource(String usource) {
        this.usource = usource;
    }

    public String getQuestionid() {
        return questionid;
    }

    public void setQuestionid(String questionid) {
        this.questionid = questionid;
    }
}
