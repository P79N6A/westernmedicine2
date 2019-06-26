package com.xywy.askforexpert.model;

import android.util.SparseBooleanArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 电话医生开通
 *
 * @author 王鹏
 * @2015-5-28下午6:01:50
 */
public class PhoneSettingInfo {

    private String phone;
    private String fee;
    private String phone1;
    private String phone2;
    private String explanation;

    private String phonetime;

    private String key;

    private List<String> time;
    private List<HashMap<String, String>> map = new ArrayList<HashMap<String, String>>();
    private List<Map<String, String>> list_times = new ArrayList<Map<String, String>>();

    private List<String> choose_list = new ArrayList<String>();

    private List<PhoneMoneyInfo> list = new ArrayList<PhoneMoneyInfo>();

    private SparseBooleanArray selectionMap = new SparseBooleanArray();
    private int position;

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }

    public List<Map<String, String>> getList_times() {
        return list_times;
    }

    public void setList_times(List<Map<String, String>> list_times) {
        this.list_times = list_times;
    }

    public SparseBooleanArray getSelectionMap() {
        return selectionMap;
    }

    public void setSelectionMap(SparseBooleanArray selectionMap) {
        this.selectionMap = selectionMap;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<PhoneMoneyInfo> getList() {
        return list;
    }

    public void setList(List<PhoneMoneyInfo> list) {
        this.list = list;
    }

    public List<String> getChoose_list() {
        return choose_list;
    }

    public void setChoose_list(List<String> choose_list) {
        this.choose_list = choose_list;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getPhonetime() {
        return phonetime;
    }

    public void setPhonetime(String phonetime) {
        this.phonetime = phonetime;
    }

    public List<HashMap<String, String>> getMap() {
        return map;
    }

    public void setMap(List<HashMap<String, String>> map) {
        this.map = map;
    }


}
