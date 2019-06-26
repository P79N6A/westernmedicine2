package com.xywy.askforexpert.module.drug.bean;


import java.io.Serializable;

/**
 * 处方bean stone
 */
public class PrescriptionBean implements Serializable {


    /**
     * id : 25
     * time : 2017-11-01 15:52:06
     * uname : 刘绘
     * uid : 68258081
     * pbn : CF15095227265245
     * usersex : 女
     * age : 0
     * paystate :
     * state : 1
     * expire : 2018-01-30 15:52:06
     * statusText : 已失效
     * <p>
     * <p>
     * dname : 张军红
     * reason : 处方已过期
     * diagnosis : 诊断信息
     * reviewer : 管理员
     * depart : 胸外科
     * appstate : 处方的状态， 1 审核中 2 审核通过 3 已失效 这三个够用不
     */


    public String id;
    public String time;
    public String uname;
    public String uid;
    public String pbn;//处方编号
    public String usersex;
    public String age;
    public String paystate;//0  处方购买情况：0.未购买 1.已购买 2.已退款
    public String state;//处方审核状态：1.待审核 2.审核通过 3.审核不通过
    public String expire;
    public String statusText;//处方当前状态
    public String uniqueId;//医网签签名ID


    private String dname;
    private String reason;
    private String diagnosis;//诊断信息
    private String reviewer;
    private String depart;
    public String appstate;
    public float prePrice; //药费
    public String sign_state; //签章状态

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPbn() {
        return pbn;
    }

    public void setPbn(String pbn) {
        this.pbn = pbn;
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

    public String getPaystate() {
        return paystate;
    }

    public void setPaystate(String paystate) {
        this.paystate = paystate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }
}