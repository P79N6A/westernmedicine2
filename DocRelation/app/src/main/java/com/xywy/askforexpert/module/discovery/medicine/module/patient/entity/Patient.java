package com.xywy.askforexpert.module.discovery.medicine.module.patient.entity;

import android.text.TextUtils;

import com.xywy.askforexpert.module.discovery.medicine.module.account.beans.UserInfoBean;
import com.xywy.askforexpert.module.discovery.medicine.common.MyConstant;

import java.io.Serializable;

/**
 * Created by xgxg on 2017/3/27.
 */

public class Patient extends BaseIndexPinyinBean implements Serializable{
    private static final long serialVersionUID = -7060210544600464481L;
    private String updated_at;
    private String uid;
    private String realname;
    private String sex;
    private String age;
    private String photo;
    private String hx_user;
    private String pinyin;
    private String firstLetter;
    private Group group;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    private String province;
    private String city;

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public class Group implements Serializable{
        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public String getGname() {
            return gname;
        }

        public void setGname(String gname) {
            this.gname = gname;
        }

        private String gid;
        private String gname;
    }

    public String getHx_user() {
        return hx_user;
    }

    public void setHx_user(String hx_user) {
        this.hx_user = hx_user;
    }

    public String getPatientHxid() {
        return getHx_user();
    }



    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public void setUId(String uid){
        this.uid = uid;
    }

    public String getUId(){
        return this.uid;
    }

    public void setRealName(String name){
        this.realname = name;
    }

    public String getRealName(){
        return this.realname;
    }

    public void setSex(String sex){
        this.sex = sex;
    }

    public  String getSex(){
        return TextUtils.isEmpty(sex)? MyConstant.NOT_DEFINED:sex;
    }

    public void setAge(String age){
        this.age = age;
    }

    public String getAge(){
        return this.age;
    }

    public void setPhoto(String photo){
        this.photo = photo;
    }

    public String getPhoto(){
        return this.photo;
    }


    public static  Patient createFrom(UserInfoBean userInfoBean){
        Patient p=new Patient();
        p.setAge(userInfoBean.getAge());
        p.setPhoto(userInfoBean.getPhoto());
        p.setRealName(userInfoBean.getRealname());
        p.setSex(userInfoBean.getSex());
        p.setUId(userInfoBean.getUserid());
        return p;
    }

    @Override
    public String getTarget() {
        return firstLetter;
    }



}
