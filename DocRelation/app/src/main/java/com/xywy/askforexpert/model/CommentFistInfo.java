package com.xywy.askforexpert.model;

import java.util.List;

/**
 * 评论 列表 模型类
 *
 * @author 王鹏
 * @2015-5-12上午9:06:04
 */
public class CommentFistInfo {

    private String code;
    private String msg;
    private List<CommentFistInfo> list;
    private String id;
    private String userid;
    private String toUserid;
    private String themeid;
    private String createtime;
    private String pid;
    private String praiseNum;
    private String content;
    private String commentNum;
    private CommentFistInfo doc;

    private String name;
    private String photo;

    private String nomaldate;
    private List<CommentFistInfo> second_list;

    private CommentFistInfo todoc;


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

    public List<CommentFistInfo> getList() {
        return list;
    }

    public void setList(List<CommentFistInfo> list) {
        this.list = list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getToUserid() {
        return toUserid;
    }

    public void setToUserid(String toUserid) {
        this.toUserid = toUserid;
    }

    public String getThemeid() {
        return themeid;
    }

    public void setThemeid(String themeid) {
        this.themeid = themeid;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(String praiseNum) {
        this.praiseNum = praiseNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public CommentFistInfo getDoc() {
        return doc;
    }

    public void setDoc(CommentFistInfo doc) {
        this.doc = doc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getNomaldate() {
        return nomaldate;
    }

    public void setNomaldate(String nomaldate) {
        this.nomaldate = nomaldate;
    }

    public List<CommentFistInfo> getSecond_list() {
        return second_list;
    }

    public void setSecond_list(List<CommentFistInfo> second_list) {
        this.second_list = second_list;
    }

    public CommentFistInfo getTodoc() {
        return todoc;
    }

    public void setTodoc(CommentFistInfo todoc) {
        this.todoc = todoc;
    }


}
