package com.xywy.askforexpert.model;


import com.xywy.askforexpert.module.main.service.que.model.MedicineBean;

/**
 * 帖子实体类
 *
 * @author shihao 2015-5-17
 */

public class QuestionSquareMsgItem {

    private String title;
    private int type;
    private String createTime;
    private String photo;
    private String sex;
    private String age;
    private String name;
    private String week;
    private String halfday;
    private String todate;
    private String birthday;
    private String picture;
    private String isAudit;
    private String isConfirm;
    private String ques_from;

    private MedicineBean medicineBean;


    /**
     * 奖赏金额
     */
    private String awardMoney;

    /**
     * 回复id
     */
    private int id;
    /**
     * 帖子id
     */
    private int qId;
    /**
     * 医生id
     */
    private int uId;

    private String rId;


    /**
     * 详情
     */
    private String detail;
    /**
     * 当前症状
     */
    private String state;
    /**
     * 想获得的帮助
     */
    private String help;
    /**
     * 科室
     */
    private String subjectName;
    /**
     * 二级科室
     */
    private String subject_pidName;

    private String url;

    public QuestionSquareMsgItem() {
    }

    public QuestionSquareMsgItem(MedicineBean medicineBean) {
        this.type=4;
        this.medicineBean = medicineBean;
    }

    public MedicineBean getMedicineBean() {
        return medicineBean;
    }

    public void setMedicineBean(MedicineBean medicineBean) {
        this.medicineBean = medicineBean;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHalfday() {
        return halfday;
    }

    public void setHalfday(String halfday) {
        this.halfday = halfday;
    }

    public String getTodate() {
        return todate;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getrId() {
        return rId;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getqId() {
        return qId;
    }

    public void setqId(int qId) {
        this.qId = qId;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubject_pidName() {
        return subject_pidName;
    }

    public void setSubject_pidName(String subject_pidName) {
        this.subject_pidName = subject_pidName;
    }

    public String getIsAudit() {
        return isAudit;
    }

    public void setIsAudit(String isAudit) {
        this.isAudit = isAudit;
    }

    public String getIsConfirm() {
        return isConfirm;
    }

    public void setIsConfirm(String isConfirm) {
        this.isConfirm = isConfirm;
    }

    public String getAwardMoney() {
        return awardMoney;
    }

    public void setAwardMoney(String awardMoney) {
        this.awardMoney = awardMoney;
    }

    public String getQues_from() {
        return ques_from;
    }

    public void setQues_from(String ques_from) {
        this.ques_from = ques_from;
    }
}
