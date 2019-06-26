package com.xywy.askforexpert.module.discovery.medicine.module.patient;


import com.xywy.askforexpert.module.discovery.medicine.module.patient.entity.Patient;

/**
 * 患者管理类 记录下当前跟哪个患者聊天
 * stone
 * 2017/11/1 下午4:55
 */
public class PatientManager {
    static private PatientManager instance= new PatientManager();
    private Patient mPatient;

    private PatientManager() {}

    static public PatientManager getInstance() {
        return instance;
    }

    public void setPatient(Patient entity) {
        this.mPatient = entity;
    }


    public Patient getPatient() {
        return this.mPatient;
    }
}
