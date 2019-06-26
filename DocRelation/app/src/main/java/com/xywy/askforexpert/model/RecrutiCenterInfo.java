package com.xywy.askforexpert.model;

import java.util.List;

/**
 * 招聘中心列表
 *
 * @author 王鹏
 * @2015-5-16上午11:15:15
 */
public class RecrutiCenterInfo {

    private String code;
    private String msg;
    private RecrutiCenterInfo lists;
    private String count;
    private List<RecrutiCenterInfo> list;

    private String id;
    private String url;
    private String deliver;
    private String coll;
    private String title;
    private String updatetime;
    private String logo;
    private String entename;

    private String collectiontime;
    private RecrutiCenterInfo list_first;
    private String total;
    private String is_show;
    private String createtime;

    private String returnedtime;


    public String getReturnedtime() {
        return returnedtime;
    }

    public void setReturnedtime(String returnedtime) {
        this.returnedtime = returnedtime;
    }

    public String getCollectiontime() {
        return collectiontime;
    }

    public void setCollectiontime(String collectiontime) {
        this.collectiontime = collectiontime;
    }

    public RecrutiCenterInfo getLists() {
        return lists;
    }

    public void setLists(RecrutiCenterInfo lists) {
        this.lists = lists;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public RecrutiCenterInfo getList_first() {
        return list_first;
    }

    public void setList_first(RecrutiCenterInfo list_first) {
        this.list_first = list_first;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getIs_show() {
        return is_show;
    }

    public void setIs_show(String is_show) {
        this.is_show = is_show;
    }

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

    public List<RecrutiCenterInfo> getList() {
        return list;
    }

    public void setList(List<RecrutiCenterInfo> list) {
        this.list = list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDeliver() {
        return deliver;
    }

    public void setDeliver(String deliver) {
        this.deliver = deliver;
    }

    public String getColl() {
        return coll;
    }

    public void setColl(String coll) {
        this.coll = coll;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getEntename() {
        return entename;
    }

    public void setEntename(String entename) {
        this.entename = entename;
    }


}
