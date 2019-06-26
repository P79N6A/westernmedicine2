package com.xywy.askforexpert.model.topics;

import java.util.List;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/5/4 11:17
 */
public class FansData {
    /**
     * code : 0
     * msg : 成功
     * data : [{"nickname":"廖阁","userid":65173122,"photo":"http://doctor.club.xywy.com/images/upload/paper/20150907162230879785e.jpg?v=1462333402","hospital":"北京口腔医院","profess_job":"医生","job":"医师","relation":0}]
     * total : 1
     */

    private int code;
    private String msg;
    private String total;
    /**
     * nickname : 廖阁
     * userid : 65173122
     * photo : http://doctor.club.xywy.com/images/upload/paper/20150907162230879785e.jpg?v=1462333402
     * hospital : 北京口腔医院
     * profess_job : 医生
     * job : 医师
     * relation : 0
     */

    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String nickname;
        private int userid;
        private String photo;
        private String hospital;
        private String profess_job;
        private String job;
        private int relation;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getHospital() {
            return hospital;
        }

        public void setHospital(String hospital) {
            this.hospital = hospital;
        }

        public String getProfess_job() {
            return profess_job;
        }

        public void setProfess_job(String profess_job) {
            this.profess_job = profess_job;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public int getRelation() {
            return relation;
        }

        public void setRelation(int relation) {
            this.relation = relation;
        }
    }
}
