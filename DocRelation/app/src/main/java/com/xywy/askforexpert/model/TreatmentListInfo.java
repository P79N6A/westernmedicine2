package com.xywy.askforexpert.model;

import java.util.List;

/**
 * 指定医生的患者诊疗记录列表
 *
 * @author 王鹏
 * @2015-5-22下午2:20:32
 */
public class TreatmentListInfo {
    private String code;
    private String msg;
    private TreatmentListInfo data;
    private String id;
    private String did;
    private String slickname;
    private String slickinfo;
    private List<TreatmentListInfo> list;
    private String content;
    private String addtime;
    private List<String> imgs;

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

    public TreatmentListInfo getData() {
        return data;
    }

    public void setData(TreatmentListInfo data) {
        this.data = data;
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

    public String getSlickname() {
        return slickname;
    }

    public void setSlickname(String slickname) {
        this.slickname = slickname;
    }

    public String getSlickinfo() {
        return slickinfo;
    }

    public void setSlickinfo(String slickinfo) {
        this.slickinfo = slickinfo;
    }

    public List<TreatmentListInfo> getList() {
        return list;
    }

    public void setList(List<TreatmentListInfo> list) {
        this.list = list;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }


}
