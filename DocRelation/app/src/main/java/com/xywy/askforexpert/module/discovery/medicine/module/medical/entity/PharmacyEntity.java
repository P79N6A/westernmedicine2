package com.xywy.askforexpert.module.discovery.medicine.module.medical.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bobby on 17/3/21.
 */

public class PharmacyEntity {
    private int id;
    private int pid;
    private String name;
    private boolean isSelected;
    List<MedicalCategoryEntity> son = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public List<MedicalCategoryEntity> getSon() {
        return son;
    }

    public void setSon(List<MedicalCategoryEntity> son) {
        this.son = son;
    }
}
