package com.xywy.askforexpert.model.doctor;

/**
 * 感兴趣的人 PersonItem
 *
 * @author apple
 */
public class InterestePersonItemBean {
    /**   {
     *      "nickname": "王桂林",
     "realname": "王桂林",
     "photo": "http://home.xywy.com/upload/defaule/fv_new.jpg",
     "hospital": "中国人民解放军三零七医院",
     "site": "",
     "isdoctor": "0",
     "job": "",
     "isjob": "0",
     "phone": "",
     "mobilephone": "",
     "sex": "1",
     "speciality": "",
     "synopsis": "放射病著名专家，从事临床血液病及急性放射病临床研究30余年，积累了丰富的临床经验。对急、慢性白血病、再生障碍性贫血、血小板减少性紫癜等疾病诊断与治疗有丰富的经验。多次参加国内辐射事故中急性放射病的救治工作。获军队科技进步二等奖2项，三等奖5项。",
     "subject": "",
     "isdoc": "专业会员",
     "honor": "0",
     "business": null,
     "address_bus": "",
     "subject_bus": "",
     "user_type": 1,
     "department": "",
     "school": null,
     "profession": null,
     "training_hospital": null,
     "userid": "4840",
     "doctortype": 1,
     "is_doctor": 0
     },
     */

    /**
     * 用户id
     */
    public String id;
    /**
     * 用户昵称
     */
    public String nickname;
    /**
     * 用户真实名字
     */
    public String realname;
    /**
     * 头像
     */
    public String photo;
    /**
     * 所属
     */
    public String subject;
    /**
     * 工作
     */
    public String job;
    /**
     * 所在医院
     */
    public String hospital;
    /**
     * 用户类型
     */
    public String usertype;
    /**
     * 用户动态
     */
    public String synopsis;

    private String site;
    private String isdoctor;
    private String isjob;
    private String phone;
    private String mobilephone;
    private String sex;
    private String speciality;
    private String isdoc;
    private String honor;
    private String business;
    private String address_bus;
    private String relation;
    private String qq;
    private String department;
    private String user_type;
    private String school;
    private String profession;
    private String training_hospital;
    private String userid;
    private String doctortype;
    private String is_doctor;

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getIsdoctor() {
        return isdoctor;
    }

    public void setIsdoctor(String isdoctor) {
        this.isdoctor = isdoctor;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getIsjob() {
        return isjob;
    }

    public void setIsjob(String isjob) {
        this.isjob = isjob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getIsdoc() {
        return isdoc;
    }

    public void setIsdoc(String isdoc) {
        this.isdoc = isdoc;
    }

    public String getHonor() {
        return honor;
    }

    public void setHonor(String honor) {
        this.honor = honor;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getAddress_bus() {
        return address_bus;
    }

    public void setAddress_bus(String address_bus) {
        this.address_bus = address_bus;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getTraining_hospital() {
        return training_hospital;
    }

    public void setTraining_hospital(String training_hospital) {
        this.training_hospital = training_hospital;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDoctortype() {
        return doctortype;
    }

    public void setDoctortype(String doctortype) {
        this.doctortype = doctortype;
    }

    public String getIs_doctor() {
        return is_doctor;
    }

    public void setIs_doctor(String is_doctor) {
        this.is_doctor = is_doctor;
    }


}
