package com.xywy.askforexpert.model.certification;

import java.io.Serializable;

/**
 * 留言板 stone
 */
public class MessageBoardBean implements Serializable {

    /**
     * did : 13996218
     * type : 1
     * message : 等会要开会，稍等啊
     * isopen : 1
     * created_time : 1521514142
     * bg_time : 1521514173
     */

    private String did;
    private String type;
    private String message;
    private String isopen;
    private String created_time;
    private String bg_time;
    private String left_time;

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIsopen() {
        return isopen;
    }

    public void setIsopen(String isopen) {
        this.isopen = isopen;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getBg_time() {
        return bg_time;
    }

    public void setBg_time(String bg_time) {
        this.bg_time = bg_time;
    }

    public String getLeft_time() {
        return left_time;
    }

    public void setLeft_time(String left_time) {
        this.left_time = left_time;
    }
}
