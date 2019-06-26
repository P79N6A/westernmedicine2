package com.xywy.askforexpert.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jason on 2018/11/2.
 */

public class PatientData implements Serializable{
    private int total;
    private ArrayList<PatienTtitle> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<PatienTtitle> getData() {
        return data;
    }

    public void setData(ArrayList<PatienTtitle> data) {
        this.data = data;
    }
}
