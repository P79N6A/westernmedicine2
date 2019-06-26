package com.xywy.askforexpert.module.discovery.medicine.adapter.entity;

/**
 * Created by xgxg on 2017/10/23.
 * 药品助手首页的实体类
 */

public class MedicineItemBean {
    //咨询患者
    public static final int TYPE_LIST_PATIENT = 1;
    //用药记录
    public static final int TYPE_MEDICINE_RECORD = 2;
    private int type;
    private LastestPatientListItem lastestPatientListItem;
    private LastestMedicineRecordListItem lastestMedicineRecordListItem;
    public MedicineItemBean(int type) {
        this.type = type;
    }

    public MedicineItemBean(int type,LastestPatientListItem lastestPatientListItem) {
        this(type);
        this.lastestPatientListItem = lastestPatientListItem;
    }

    public MedicineItemBean(int type,LastestMedicineRecordListItem lastestMedicineRecordListItem) {
        this(type);
        this.lastestMedicineRecordListItem = lastestMedicineRecordListItem;
    }
    public int getType(){
        return type;
    }

    public LastestPatientListItem getLastestPatientItem() {
        return lastestPatientListItem;
    }

    public LastestMedicineRecordListItem getLastestMedicineRecordItem() {
        return lastestMedicineRecordListItem;
    }
}
