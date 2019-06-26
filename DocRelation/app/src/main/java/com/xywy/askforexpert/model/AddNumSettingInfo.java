package com.xywy.askforexpert.model;

import android.util.SparseBooleanArray;

import java.util.ArrayList;
import java.util.List;

/**
 * 预约加号
 *
 * @author 王鹏
 * @2015-5-27下午8:12:15
 */
public class AddNumSettingInfo {
    private String istrue;
    private String movephone;
    private String treatlimt;
    private String detail;
    private String plusline;
    private String arealimit;
    private String plus_range;

    private SparseBooleanArray selectionMap = new SparseBooleanArray();

    private int isttrue_position;
    private int treatlimt_position;
    private List<String> list = new ArrayList<String>();

    public SparseBooleanArray getSelectionMap() {
        return selectionMap;
    }

    public void setSelectionMap(SparseBooleanArray selectionMap) {
        this.selectionMap = selectionMap;
    }

    public int getIsttrue_position() {
        return isttrue_position;
    }

    public void setIsttrue_position(int isttrue_position) {
        this.isttrue_position = isttrue_position;
    }

    public int getTreatlimt_position() {
        return treatlimt_position;
    }

    public void setTreatlimt_position(int treatlimt_position) {
        this.treatlimt_position = treatlimt_position;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public String getPlus_range() {
        return plus_range;
    }

    public void setPlus_range(String plus_range) {
        this.plus_range = plus_range;
    }

    public String getIstrue() {
        return istrue;
    }

    public void setIstrue(String istrue) {
        this.istrue = istrue;
    }

    public String getMovephone() {
        return movephone;
    }

    public void setMovephone(String movephone) {
        this.movephone = movephone;
    }

    public String getTreatlimt() {
        return treatlimt;
    }

    public void setTreatlimt(String treatlimt) {
        this.treatlimt = treatlimt;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPlusline() {
        return plusline;
    }

    public void setPlusline(String plusline) {
        this.plusline = plusline;
    }

    public String getArealimit() {
        return arealimit;
    }

    public void setArealimit(String arealimit) {
        this.arealimit = arealimit;
    }

}
