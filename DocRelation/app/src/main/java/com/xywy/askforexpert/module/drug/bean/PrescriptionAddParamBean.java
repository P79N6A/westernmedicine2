package com.xywy.askforexpert.module.drug.bean;


import java.io.Serializable;

/**
 * 处方添加的参数bean stone
 */
public class PrescriptionAddParamBean implements Serializable {


//    jsonarray ，字段说明：
//    id：商品ID
//    num：开药数量(int),
//    take_rate：服用频率（每天几次 string）
//    take_time：服用时间（饭前、饭后... string)
//    take_num：服用量（int）
//    take_unit：服用量单位(int),
//    take_method：服用方法(int),
//    remark：备注(string)
//    take_day ：用药天数 (int)
//    drug_unit：开药单位(int)

    public String did;//医生id
    public String dname;//医生名字
    public String uname;//患者名字
    public String uid;//患者id
    public String usersex;//患者性别 1.男 2.女
    public String age;
    public String diagnosis;//诊断信息
    public String pov;//有效天数
    public String usource;//业务处方来源 ： 1.寻医问药app
    public String drugs;//药品信息
    public String questionid;//问题id


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

    public String getPov() {
        return pov;
    }

    public void setPov(String pov) {
        this.pov = pov;
    }

    public String getUsource() {
        return usource;
    }

    public void setUsource(String usource) {
        this.usource = usource;
    }

    public String getDrugs() {
        return drugs;
    }

    public void setDrugs(String drugs) {
        this.drugs = drugs;
    }

    public String getQuestionid() {
        return questionid;
    }

    public void setQuestionid(String questionid) {
        this.questionid = questionid;
    }
}