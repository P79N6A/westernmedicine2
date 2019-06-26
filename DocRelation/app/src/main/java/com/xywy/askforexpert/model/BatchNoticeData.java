package com.xywy.askforexpert.model;

import java.io.Serializable;

/**
 * Created by jason on 2018/11/14.
 */

public class BatchNoticeData implements Serializable{
    private String id;
    private String did;
    private String title;
    private String content;
    private String create_time;
    private boolean selectedFlag = false;

    public boolean isSelectedFlag() {
        return selectedFlag;
    }

    public void setSelectedFlag(boolean selectedFlag) {
        this.selectedFlag = selectedFlag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
