package com.xywy.askforexpert.module.drug.bean;

import com.xywy.askforexpert.model.consultentity.OnlineQuestionMsgListRspEntity;

import java.io.Serializable;

/**
 * Created by jason on 2018/7/11.
 */

public class PrescriptionData implements Serializable{
    private String patientName;
    private String patientSex;
    private String patient_age_year;
    private String department;
    private String time;
    private String uid;
    private String source;
    private String prescription_id;
    private String patient_age_month;
    private String patient_age_day;
    private String doctor_depart;

    public String getDoctor_depart() {
        return doctor_depart;
    }

    public void setDoctor_depart(String doctor_depart) {
        this.doctor_depart = doctor_depart;
    }

    public String getPatient_age_year() {
        return patient_age_year;
    }

    public void setPatient_age_year(String patient_age_year) {
        this.patient_age_year = patient_age_year;
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

    public String getPrescription_id() {
        return prescription_id;
    }

    public void setPrescription_id(String prescription_id) {
        this.prescription_id = prescription_id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientSex() {
        return patientSex;
    }

    public void setPatientSex(String patientSex) {
        this.patientSex = patientSex;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void initData(OnlineQuestionMsgListRspEntity.DataBean data) {
        patientName = data.getPatient_name();
        patientSex = data.getPatient_sex();
        patient_age_year = data.getPatient_age_year();
        patient_age_month = data.getPatient_age_month();
        patient_age_day = data.getPatient_age_day();
        department = data.getDepart();
        time = data.getCreated_time();
        uid = data.getUid();
        source = data.getSource();
        prescription_id =data.getPrescription_id();
        doctor_depart = data.getDoctor_depart();
    }
}
