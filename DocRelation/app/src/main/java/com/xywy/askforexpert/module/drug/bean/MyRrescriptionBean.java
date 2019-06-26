package com.xywy.askforexpert.module.drug.bean;

/**
 * Created by jason on 2018/7/24.
 */

public class MyRrescriptionBean {
    private String id;
    private String num;
    private String take_rate;
    private String take_time;
    private String take_num;
    private String take_unit;
    private String take_method;
    private String remark;
    private String take_day;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTake_rate() {
        return take_rate;
    }

    public void setTake_rate(String take_rate) {
        this.take_rate = take_rate;
    }

    public String getTake_time() {
        return take_time;
    }

    public void setTake_time(String take_time) {
        this.take_time = take_time;
    }

    public String getTake_num() {
        return take_num;
    }

    public void setTake_num(String take_num) {
        this.take_num = take_num;
    }

    public String getTake_unit() {
        return take_unit;
    }

    public void setTake_unit(String take_unit) {
        this.take_unit = take_unit;
    }

    public String getTake_method() {
        return take_method;
    }

    public void setTake_method(String take_method) {
        this.take_method = take_method;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTake_day() {
        return take_day;
    }

    public void setTake_day(String take_day) {
        this.take_day = take_day;
    }

    public String getDrug_unit() {
        return drug_unit;
    }

    public void setDrug_unit(String drug_unit) {
        this.drug_unit = drug_unit;
    }

    private String drug_unit;
}
