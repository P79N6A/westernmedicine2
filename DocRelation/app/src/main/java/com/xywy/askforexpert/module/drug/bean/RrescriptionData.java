package com.xywy.askforexpert.module.drug.bean;

import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.MedicineCartEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jason on 2018/7/11.
 */

public class RrescriptionData {
    static private RrescriptionData instance;
    public static boolean BACK_FLAG = false;
    private Map<String, RrescriptionBean> mAddedCartMedicine = new HashMap<>();
    private RrescriptionData() {
    }
    static public RrescriptionData getInstance() {
        if(instance ==null) {
            instance = new RrescriptionData();
        }
        return instance;
    }

    public void addMedicine(RrescriptionBean entity) {
        if(entity!=null) {
            mAddedCartMedicine.put(entity.getId(), entity);
        }
    }

    public void removeMedicine(String id) {
        mAddedCartMedicine.remove(id);
    }

    public void removeAllMedicine() {
        mAddedCartMedicine.clear();
    }

    public boolean isAdded(String productId) {
        return mAddedCartMedicine.containsKey(productId);
    }

    public List<RrescriptionBean> getMedicineList() {
        return new ArrayList<>(mAddedCartMedicine.values());
    }

    public RrescriptionBean getMedicine(String id) {
        return mAddedCartMedicine.get(id);
    }
}
