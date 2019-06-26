package com.xywy.askforexpert.module.discovery.medicine.module.medical;


import com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.MedicineCartEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bobby on 17/3/29.
 * 药品购物车中心
 */

public class MedicineCartCenter {
    static private MedicineCartCenter instance;
    private Map<Integer, MedicineCartEntity> mAddedCartMedicine = new HashMap<>();
    private MedicineCartCenter() {
    }
    static public MedicineCartCenter getInstance() {
        if(instance ==null) {
            instance = new MedicineCartCenter();
        }
        return instance;
    }

    public void addMedicine(MedicineCartEntity entity) {
        if(entity!=null && entity.getEntity()!=null) {
            mAddedCartMedicine.put(entity.getEntity().getProductId(), entity);
        }
    }

    public void removeMedicine(int productId) {
        mAddedCartMedicine.remove(productId);
    }

    public void removeAllMedicine() {
        mAddedCartMedicine.clear();
    }

    public boolean isAdded(int productId) {
        return mAddedCartMedicine.containsKey(productId);
    }

    public List<MedicineCartEntity> getMedicineList() {
        return new ArrayList<>(mAddedCartMedicine.values());
    }
}
