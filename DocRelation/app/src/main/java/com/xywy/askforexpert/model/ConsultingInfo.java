package com.xywy.askforexpert.model;

import java.io.Serializable;
import java.util.List;

/**
 * 资讯 模型类
 *
 * @author 王鹏
 * @2015-5-11下午3:28:06
 */
public class ConsultingInfo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String code;
    private List<ConsultingInfo> list;
    private String id;
    private String title;
    private String image;
    private String createtime;
    private String praiseNum;
    private String url;
    private String msg;
    private String colle;//收藏标记
    private String praise;//点赞标记


    public String getColle() {
        return colle;
    }

    public void setColle(String colle) {
        this.colle = colle;
    }

    public String getPraise() {
        return praise;
    }

    public void setPraise(String praise) {
        this.praise = praise;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<ConsultingInfo> getList() {
        return list;
    }

    public void setList(List<ConsultingInfo> list) {
        this.list = list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(String praiseNum) {
        this.praiseNum = praiseNum;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
