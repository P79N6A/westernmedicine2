package com.xywy.askforexpert.model.consultentity;

import java.io.Serializable;

/**
 *IM问题 stone 统一
 */
public class IMQuestionBean implements Serializable{

    private String type_tag;//直接使用这个字段 悬赏3.00元 ... stone 医脉5.4.0新添加

    //历史回复
    //stone 新添加评价
    private CommentBean comment;
    private String id;
    private String last_content;
    private String last_status;
    private String last_time;
    //stone 是否总结
    private int is_summary;//1 总结
    //是否允许总结 1可以 0不可以,也就是超时了
    private int allow_summary;

    //问题库
    private String add_time;

    //处理中
    //stone 医脉特有 表明认领状态
    private int status;//status=1已认领，0未认领
    //stone 新添加的月 天
    private String patient_age_month;
    private String patient_age_day;
    private String content;
    private String created_time;
    private String uid;
    private int is_read;
    private String patient_name;
    private String patient_sex;
    private String patient_age;
    private String qid;
    private String chat_id;
    private String user_photo;
    private String type;
    private String amount;
    private String is_new_reply;
    private String reply_accept;

    public String getReply_accept() {
        return reply_accept;
    }

    public void setReply_accept(String reply_accept) {
        this.reply_accept = reply_accept;
    }

    public String getIs_new_reply() {
        return is_new_reply;
    }

    public void setIs_new_reply(String is_new_reply) {
        this.is_new_reply = is_new_reply;
    }

    public CommentBean getComment() {
        return comment;
    }

    public void setComment(CommentBean comment) {
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLast_content() {
        return last_content;
    }

    public void setLast_content(String last_content) {
        this.last_content = last_content;
    }

    public String getLast_status() {
        return last_status;
    }

    public void setLast_status(String last_status) {
        this.last_status = last_status;
    }

    public String getLast_time() {
        return last_time;
    }

    public void setLast_time(String last_time) {
        this.last_time = last_time;
    }

    public int getIs_summary() {
        return is_summary;
    }

    public void setIs_summary(int is_summary) {
        this.is_summary = is_summary;
    }

    public int getAllow_summary() {
        return allow_summary;
    }

    public void setAllow_summary(int allow_summary) {
        this.allow_summary = allow_summary;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPatient_age_month() {
        return patient_age_month;
    }

    public void setPatient_age_month(String patient_age_month) {
        this.patient_age_month = patient_age_month;
    }

    public String getPatient_age_day() {
        return patient_age_day;
    }

    public void setPatient_age_day(String patient_age_day) {
        this.patient_age_day = patient_age_day;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getPatient_sex() {
        return patient_sex;
    }

    public void setPatient_sex(String patient_sex) {
        this.patient_sex = patient_sex;
    }

    public String getPatient_age() {
        return patient_age;
    }

    public void setPatient_age(String patient_age) {
        this.patient_age = patient_age;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getType_tag() {
        return type_tag;
    }

    public void setType_tag(String type_tag) {
        this.type_tag = type_tag;
    }
}