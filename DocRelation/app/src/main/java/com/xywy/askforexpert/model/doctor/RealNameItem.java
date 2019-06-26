package com.xywy.askforexpert.model.doctor;

import com.xywy.askforexpert.model.topics.MoreTopicItem;

import java.util.List;


public class RealNameItem {
    public String id;
    public String userid;
    public String level;
    public String nickname;
    public String content;
    public String userphoto;
    public String relation;
    public String url;
    public String subject;
    public String hospital;
    public String is_praise;
    public User user;
    public String createtime;
    public String type;
    public List<String> imgs;
    public List<String> minimgs;
    public String praiseNum;
    public String commentNum;
    public Share share;
    public String source;
    public List<PraiseBean> praiselist;
    @SuppressWarnings("rawtypes")
    public List<CommentBean> commlist;
    /**
     * 数据类型 userlist：感兴趣的人
     */
    public String listtype;
    /**
     * 感兴趣的人列表
     */
    public List<InterestePersonItemBean> list;

    public List<MoreTopicItem.ListEntity> themelist;

    public Share getShare() {
        return share;
    }

    public void setShare(Share share) {
        this.share = share;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getIs_praise() {
        return is_praise;
    }

    public void setIs_praise(String is_praise) {
        this.is_praise = is_praise;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public List<String> getMinimgs() {
        return minimgs;
    }

    public void setMinimgs(List<String> minimgs) {
        this.minimgs = minimgs;
    }

    public String getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(String praiseNum) {
        this.praiseNum = praiseNum;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public List<PraiseBean> getPraiselist() {
        return praiselist;
    }

    public void setPraiselist(List<PraiseBean> praiselist) {
        this.praiselist = praiselist;
    }

    public List<CommentBean> getCommlist() {
        return commlist;
    }

    public void setCommlist(List<CommentBean> commlist) {
        this.commlist = commlist;
    }


}
