package com.xywy.askforexpert.model;

import java.util.ArrayList;

/**
 * Created by jason on 2018/11/8.
 */

public class GroupingData {
    private String gid;
    private String g_name;
    private String nums;
    private boolean itemFlag = false;
    private ArrayList<PatienTtitle>  child;

    public boolean isItemFlag() {
        return itemFlag;
    }

    public void setItemFlag(boolean itemFlag) {
        this.itemFlag = itemFlag;
    }



    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getG_name() {
        return g_name;
    }

    public void setG_name(String g_name) {
        this.g_name = g_name;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public ArrayList<PatienTtitle> getChild() {
        return child;
    }

    public void setChild(ArrayList<PatienTtitle> child) {
        this.child = child;
    }
}
