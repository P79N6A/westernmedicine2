package com.xywy.askforexpert.model.doctor.common;

import java.util.List;

/**
 *
 * 医圈共用列表 Item 使用实体
 * Created by bailiangjin on 2016/10/27.
 */

public class CircleCommonListItem {

    /**
     * 医生名
     */
    private String doctorId;

    /**
     * 医生名
     */
    private String doctorName;
    /**
     * 医生职位
     */
    private String doctorPost;
    /**
     * 医生医院
     */
    private String doctorHospital;
    /**
     * 时间
     */
    private String time;

    /**
     * 点赞数
     */
    private String praiseNumber;
    /**
     * 评论数
     */
    private String commentNumber;

    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;

    //TODO:点赞List
    //TODO:评论List



    /**
     * 图片List
     */
    List<String> imgUrlList;


    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorPost() {
        return doctorPost;
    }

    public void setDoctorPost(String doctorPost) {
        this.doctorPost = doctorPost;
    }

    public String getDoctorHospital() {
        return doctorHospital;
    }

    public void setDoctorHospital(String doctorHospital) {
        this.doctorHospital = doctorHospital;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPraiseNumber() {
        return praiseNumber;
    }

    public void setPraiseNumber(String praiseNumber) {
        this.praiseNumber = praiseNumber;
    }

    public String getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(String commentNumber) {
        this.commentNumber = commentNumber;
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

    public List<String> getImgUrlList() {
        return imgUrlList;
    }

    public void setImgUrlList(List<String> imgUrlList) {
        this.imgUrlList = imgUrlList;
    }
}
