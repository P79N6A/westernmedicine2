package com.xywy.askforexpert.module.discovery.medicine.adapter.entity;

import com.xywy.askforexpert.module.discovery.medicine.module.patient.entity.Patient;

import java.util.List;

/**
 * Created by xgxg on 2017/10/23.
 */

public class LastestPatientListItem {
    private List<Patient> innerItemList;

    public LastestPatientListItem(List<Patient> innerItemList) {
        this.innerItemList = innerItemList;
    }

    public List<Patient> getInnerItemList() {
        return innerItemList;
    }

    public void setInnerItemList(List<Patient> innerItemList) {
        this.innerItemList = innerItemList;
    }
}
