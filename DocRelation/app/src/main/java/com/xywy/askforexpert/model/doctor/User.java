package com.xywy.askforexpert.model.doctor;

import java.io.Serializable;

/**
 * 项目名称：D_Platform
 * 类名称：User
 * 类描述：用户类
 * 创建人：shihao
 * 创建时间：2015-5-12 下午5:02:43
 * 修改备注：2015-6-30
 */
public class User implements Serializable {
    public String userid;
    public String nickname;
    public String photo;
    public String job;
    public String subject;
    public String hospital;
    public String appdate;
    public String approveid;
    public String birth_day;
    public String city;
    public String cored;
    public int relation;
    public String department;
    public String is_doctor;
    /**
     * 答题天数
     */
    private String loseDate;
    /**
     * 申请状态
     */
    private String sqState;
    /**
     * 当前身份
     */
    private String medalName;
    /**
     * 下级身份
     */
    private String next;
    /**
     * 晋级条件
     */
    private String proTj;
    /**
     * 晋级条件下的注
     */
    private String note;
    /**
     * 申请晋级按钮可点
     */
    private String click;
    /**
     * 申请晋级方法
     */
    private String type;
    /**
     * 用户类型
     */
    private String usertype;
    /**
     * 活跃总数
     */
    private String hy;
    /**
     * 当前活跃数
     */
    private String work;

    @Override
    public String toString() {
        return "User [userid=" + userid + ", nickname=" + nickname + ", photo="
                + photo + ", subject=" + subject + ", hospital=" + hospital
                + "]";
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLoseDate() {
        return loseDate;
    }

    public void setLoseDate(String loseDate) {
        this.loseDate = loseDate;
    }

    public String getSqState() {
        return sqState;
    }

    public void setSqState(String sqState) {
        this.sqState = sqState;
    }

    public String getMedalName() {
        return medalName;
    }

    public void setMedalName(String medalName) {
        this.medalName = medalName;
    }

    public String getProTj() {
        return proTj;
    }

    public void setProTj(String proTj) {
        this.proTj = proTj;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHy() {
        return hy;
    }

    public void setHy(String hy) {
        this.hy = hy;
    }

    public String getUserType() {
        return usertype;
    }

    public void setUserType(String userType) {
        this.usertype = userType;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getAppdate() {
        return appdate;
    }

    public void setAppdate(String appdate) {
        this.appdate = appdate;
    }

    public String getApproveid() {
        return approveid;
    }

    public void setApproveid(String approveid) {
        this.approveid = approveid;
    }

    public String getBirth_day() {
        return birth_day;
    }

    public void setBirth_day(String birth_day) {
        this.birth_day = birth_day;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCored() {
        return cored;
    }

    public void setCored(String cored) {
        this.cored = cored;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getIs_doctor() {
        return is_doctor;
    }

    public void setIs_doctor(String is_doctor) {
        this.is_doctor = is_doctor;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }
}
