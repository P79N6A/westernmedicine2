package com.xywy.askforexpert.module.discovery.medicine.adapter.entity;

import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.PharmacyRecordEntity;

import java.util.List;

/**
 * Created by xgxg on 2017/10/23.
 */

public class LastestMedicineRecordListItem {
    private List<PharmacyRecordEntity> innerItemList;

    public LastestMedicineRecordListItem(List<PharmacyRecordEntity> innerItemList) {
        this.innerItemList = innerItemList;
    }

    public List<PharmacyRecordEntity> getInnerItemList() {
        return innerItemList;
    }

    public void setInnerItemList(List<PharmacyRecordEntity> innerItemList) {
        this.innerItemList = innerItemList;
    }
}
