package com.xywy.askforexpert.model;

import java.util.List;

/**
 * 科室分类
 *
 * @author 王鹏
 * @2015-6-22下午12:34:28
 */
public class SectionSoftInfo {
    List<SectionSoftInfo> group_list;
    private String code;
    private String msg;
    private String name;
    private String id;

    private List<SectionSoftInfo> child_list;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<SectionSoftInfo> getGroup_list() {
        return group_list;
    }

    public void setGroup_list(List<SectionSoftInfo> group_list) {
        this.group_list = group_list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<SectionSoftInfo> getChild_list() {
        return child_list;
    }

    public void setChild_list(List<SectionSoftInfo> child_list) {
        this.child_list = child_list;
    }


}
