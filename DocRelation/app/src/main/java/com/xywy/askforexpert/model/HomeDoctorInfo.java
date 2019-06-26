package com.xywy.askforexpert.model;

import java.io.Serializable;
import java.util.List;

/**
 * 家庭医生
 *
 * @author 王鹏
 * @2015-5-26下午4:26:38
 */
public class HomeDoctorInfo implements Serializable{

    private String msg;
    private String code;
    private HomeDoctorInfo data;

    private HomeDoctorInfo onlinetime;

    private List<HomeDoctorInfo> monday;
    private List<HomeDoctorInfo> tuesday;
    private List<HomeDoctorInfo> wednesday;
    private List<HomeDoctorInfo> thursday;
    private List<HomeDoctorInfo> friday;
    private List<HomeDoctorInfo> saturday;
    private List<HomeDoctorInfo> sunday;
    private String start;
    private String end;
    private String id;
    private String daytype;
    private String timestr;
    private List<HomeDoctorInfo> productinfo;
    private String price;
    private String category;

    private HomeDoctorInfo week_price;
    private String min;
    private String max;
    private HomeDoctorInfo month_price;

    private String uid;
    private boolean is_discount;
    private String fd_doctor_primary_key;
    private boolean is_set;
    private String words;
    private String special;

    //stone 5.4 新添加
    private String honor;//个人荣誉
    private List<PicBean> honorpic;//荣誉展示图片
    private List<PicBean> stylepic;//个人风采展示图片

    public boolean getIs_set() {
        return is_set;
    }

    public void setIs_set(boolean is_set) {
        this.is_set = is_set;
    }

    public HomeDoctorInfo getOnlinetime() {
        return onlinetime;
    }

    public void setOnlinetime(HomeDoctorInfo onlinetime) {
        this.onlinetime = onlinetime;
    }

    public List<HomeDoctorInfo> getMonday() {
        return monday;
    }

    public void setMonday(List<HomeDoctorInfo> monday) {
        this.monday = monday;
    }

    public List<HomeDoctorInfo> getTuesday() {
        return tuesday;
    }

    public void setTuesday(List<HomeDoctorInfo> tuesday) {
        this.tuesday = tuesday;
    }

    public List<HomeDoctorInfo> getWednesday() {
        return wednesday;
    }

    public void setWednesday(List<HomeDoctorInfo> wednesday) {
        this.wednesday = wednesday;
    }

    public List<HomeDoctorInfo> getThursday() {
        return thursday;
    }

    public void setThursday(List<HomeDoctorInfo> thursday) {
        this.thursday = thursday;
    }

    public List<HomeDoctorInfo> getFriday() {
        return friday;
    }

    public void setFriday(List<HomeDoctorInfo> friday) {
        this.friday = friday;
    }

    public List<HomeDoctorInfo> getSaturday() {
        return saturday;
    }

    public void setSaturday(List<HomeDoctorInfo> saturday) {
        this.saturday = saturday;
    }

    public List<HomeDoctorInfo> getSunday() {
        return sunday;
    }

    public void setSunday(List<HomeDoctorInfo> sunday) {
        this.sunday = sunday;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public HomeDoctorInfo getData() {
        return data;
    }

    public void setData(HomeDoctorInfo data) {
        this.data = data;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDaytype() {
        return daytype;
    }

    public void setDaytype(String daytype) {
        this.daytype = daytype;
    }

    public String getTimestr() {
        return timestr;
    }

    public void setTimestr(String timestr) {
        this.timestr = timestr;
    }

    public List<HomeDoctorInfo> getProductinfo() {
        return productinfo;
    }

    public void setProductinfo(List<HomeDoctorInfo> productinfo) {
        this.productinfo = productinfo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public HomeDoctorInfo getWeek_price() {
        return week_price;
    }

    public void setWeek_price(HomeDoctorInfo week_price) {
        this.week_price = week_price;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean getIs_discount() {
        return is_discount;
    }

    public void setIs_discount(boolean is_discount) {
        this.is_discount = is_discount;
    }

    public String getFd_doctor_primary_key() {
        return fd_doctor_primary_key;
    }

    public void setFd_doctor_primary_key(String fd_doctor_primary_key) {
        this.fd_doctor_primary_key = fd_doctor_primary_key;
    }

    public HomeDoctorInfo getMonth_price() {
        return month_price;
    }

    public void setMonth_price(HomeDoctorInfo month_price) {
        this.month_price = month_price;
    }

    public String getHonor() {
        return honor;
    }

    public void setHonor(String honor) {
        this.honor = honor;
    }

    public List<PicBean> getHonorpic() {
        return honorpic;
    }

    public void setHonorpic(List<PicBean> honorpic) {
        this.honorpic = honorpic;
    }

    public List<PicBean> getStylepic() {
        return stylepic;
    }

    public void setStylepic(List<PicBean> stylepic) {
        this.stylepic = stylepic;
    }
}
