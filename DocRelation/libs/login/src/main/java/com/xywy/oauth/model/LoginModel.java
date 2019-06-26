package com.xywy.oauth.model;

import java.io.Serializable;

/**
 * Created by aidonglei on 2015/12/9.
 */
public class LoginModel implements Serializable {

    private String userid;
    private String username;
    private String insert_data;
    private String userphone;
    private String useremail;
    private String usersource;
    private String type;
    private String devid;
    private String lasttime;
    private String nickname;
    private String realname;
    private String birthday;
    private String sex;
    private String age;
    private String idcard;
    private String weight;
    private String height;
    private String blood_type;
    private String blood_pressure;
    private String photo;
    private String points;
    private String first_login_time;
    /**
     * 社区需要的cookie
     */
    private String qz_login_time;


    public String getQz_login_time() {
        return qz_login_time;
    }


    public void setQz_login_time(String qz_login_time) {
        this.qz_login_time = qz_login_time;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getInsert_data() {
        return insert_data;
    }

    public void setInsert_data(String insert_data) {
        this.insert_data = insert_data;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getUsersource() {
        return usersource;
    }

    public void setUsersource(String usersource) {
        this.usersource = usersource;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDevid() {
        return devid;
    }

    public void setDevid(String devid) {
        this.devid = devid;
    }

    public String getLasttime() {
        return lasttime;
    }

    public void setLasttime(String lasttime) {
        this.lasttime = lasttime;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
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

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getBlood_type() {
        return blood_type;
    }

    public void setBlood_type(String blood_type) {
        this.blood_type = blood_type;
    }

    public String getBlood_pressure() {
        return blood_pressure;
    }

    public void setBlood_pressure(String blood_pressure) {
        this.blood_pressure = blood_pressure;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getFirst_login_time() {
        return first_login_time;
    }

    public void setFirst_login_time(String first_login_time) {
        this.first_login_time = first_login_time;
    }
}
