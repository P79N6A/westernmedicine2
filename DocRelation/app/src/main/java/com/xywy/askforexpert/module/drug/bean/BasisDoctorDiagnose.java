package com.xywy.askforexpert.module.drug.bean;

import java.io.Serializable;

/**
 * Created by jason on 2019/5/8.
 */

public class BasisDoctorDiagnose implements Serializable{
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
