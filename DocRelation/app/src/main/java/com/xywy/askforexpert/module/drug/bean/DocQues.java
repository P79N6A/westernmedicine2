package com.xywy.askforexpert.module.drug.bean;

import java.io.Serializable;

/**
 * Created by xugan on 2018/7/4.
 */

public class DocQues implements Serializable {
    public String qid;  //问题id
    public String uid;  //用户id
    /**
     * 问题类别 6悬赏付费 7指定付费
     */
    public String type;
    public String patient_photo;
    public String patient_name;
    /**
     * 患者性别 1男 2女
     */
    public String patient_sex;
    public String patient_age;
    public String patient_age_month;
    public String patient_age_day;
    public String title;
    public String content;
    public String amount;
    public String date_time;
    public String created_time;
    public String last_time;
    public String is_read;
    /**
     * 状态 4退款待审核，5退款审核通过，6退款审核未通过，7退款完成 8已完成 11申请退款成功，12申请退款失败
     */
    public String status;
    public String id;
    public String time;
    public String uname;
    public String usersex;
    public String pbn;
    public String age;
    public String paystate;
    public String state;
    public String expire;
    public String statusText;
    public String diagnosis;
}
