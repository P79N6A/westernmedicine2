package com.xywy.askforexpert.module.discovery.medicine.module.medical.entity;

/**
 * Created by bobby on 17/3/31.
 */

public class MedicineCartEntity {
    private MedicineEntity entity;
    private int countCart;
    private int dayTimes;
    private int timeCounts;
    private String useIntro;
    private String memo;

    public MedicineCartEntity(MedicineEntity entity){
        this.entity = entity;
    }

    public MedicineEntity getEntity() {
        return entity;
    }

    public void setEntity(MedicineEntity entity) {
        this.entity = entity;
    }

    public int getCountCart() {
        return countCart;
    }

    public void setCountCart(int countCart) {
        this.countCart = countCart;
    }

    public int getDayTimes() {
        return dayTimes;
    }

    public void setDayTimes(int dayTimes) {
        this.dayTimes = dayTimes;
    }

    public int getTimeCounts() {
        return timeCounts;
    }

    public void setTimeCounts(int timeCounts) {
        this.timeCounts = timeCounts;
    }

    public String getUseIntro() {
        return useIntro;
    }

    public void setUseIntro(String useIntro) {
        this.useIntro = useIntro;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
