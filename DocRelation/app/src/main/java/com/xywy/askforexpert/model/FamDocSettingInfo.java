package com.xywy.askforexpert.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 王鹏
 * @2015-5-27上午11:56:29
 */
public class FamDocSettingInfo {


    private String words;//医生寄语
    private String month;
    private String week;
    private String special;//擅长疾病
    private String time;
    private String honor;//个人荣誉

    private String honorPic;//荣誉展示图片 用|隔开
    private String stylePic;//个人风采图片

    private List<String> day = new ArrayList<String>();

    private int moth_position;
    private int week_position;

    private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    private List<String> date_list = new ArrayList<String>();
    private List<List<String>> choolse_list = new ArrayList<List<String>>();

    public List<Map<String, String>> getList() {
        return list;
    }

    public void setList(List<Map<String, String>> list) {
        this.list = list;
    }

    public int getMoth_position() {
        return moth_position;
    }

    public void setMoth_position(int moth_position) {
        this.moth_position = moth_position;
    }

    public int getWeek_position() {
        return week_position;
    }

    public void setWeek_position(int week_position) {
        this.week_position = week_position;
    }

    public List<String> getDay() {
        return day;
    }

    public void setDay(List<String> day) {
        this.day = day;
    }

    public List<String> getDate_list() {
        return date_list;
    }

    public void setDate_list(List<String> date_list) {
        this.date_list = date_list;
    }

    public List<List<String>> getChoolse_list() {
        return choolse_list;
    }

    public void setChoolse_list(List<List<String>> choolse_list) {
        this.choolse_list = choolse_list;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHonor() {
        return honor;
    }

    public void setHonor(String honor) {
        this.honor = honor;
    }

    public String getHonorPic() {
        return honorPic;
    }

    public void setHonorPic(String honorPic) {
        this.honorPic = honorPic;
    }

    public String getStylePic() {
        return stylePic;
    }

    public void setStylePic(String stylePic) {
        this.stylePic = stylePic;
    }
}
