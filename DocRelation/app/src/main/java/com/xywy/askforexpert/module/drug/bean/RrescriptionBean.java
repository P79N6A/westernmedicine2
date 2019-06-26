package com.xywy.askforexpert.module.drug.bean;

/**
 * Created by jason on 2018/7/11.
 */

public class RrescriptionBean {
    //商品ID
    private String id;
    //开药数量
    private String num;
    //开药量单位名称
    private String doseUnit;
    //服用频率（每天几次 string
    private String take_rate;
    //服用时间（饭前、饭后... string)
    private String take_time;
    //服用名称
    private String take_time_name;
    //单次服用量
    private String take_num;
    //单次服用量单位(int)
    private String take_unit;
    //单次服用量单位名称
    private String doseName;
    //服用方法(int)
    private String take_method;
    //服用方法名称
    private String methodName;
    //备注
    private String remark;
    //用药天数 (int)
    private String take_day;
    //开药量单位(int)
    private String drug_unit;
    //药品名称
    private String drugName;
    //药品包装数量
    private String specification;
    //药品价格
    private String wksmj;

    private String stock;

    public String getTake_time_name() {
        return take_time_name;
    }

    public void setTake_time_name(String take_time_name) {
        this.take_time_name = take_time_name;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getDoseUnit() {
        return doseUnit;
    }

    public void setDoseUnit(String doseUnit) {
        this.doseUnit = doseUnit;
    }

    public String getDoseName() {
        return doseName;
    }

    public void setDoseName(String doseName) {
        this.doseName = doseName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getWksmj() {
        return wksmj;
    }

    public void setWksmj(String wksmj) {
        this.wksmj = wksmj;
    }

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


}
