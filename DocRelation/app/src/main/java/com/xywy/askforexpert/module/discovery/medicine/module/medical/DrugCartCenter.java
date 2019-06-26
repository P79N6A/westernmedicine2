package com.xywy.askforexpert.module.discovery.medicine.module.medical;


import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.DrugCartEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 加入处方签/药品购物车
 */
public class DrugCartCenter {
    static private DrugCartCenter instance;
    private Map<String, DrugCartEntity> mAddedCartMedicine = new HashMap<>();

    private DrugCartCenter() {
    }

    static public DrugCartCenter getInstance() {
        if (instance == null) {
            instance = new DrugCartCenter();
        }
        return instance;
    }

    public void addMedicine(DrugCartEntity entity) {
        if (entity != null && entity.getEntity() != null) {
            mAddedCartMedicine.put(entity.getEntity().getProductId(), entity);
        }
    }

    public void removeMedicine(int productId) {
        mAddedCartMedicine.remove(productId);
    }

    public void removeAllMedicine() {
        mAddedCartMedicine.clear();
    }

    public boolean isAdded(String productId) {
        return mAddedCartMedicine.containsKey(productId);
    }

    public List<DrugCartEntity> getMedicineList() {
        return new ArrayList<>(mAddedCartMedicine.values());
    }
}
