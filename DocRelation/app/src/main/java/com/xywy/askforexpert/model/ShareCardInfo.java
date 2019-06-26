package com.xywy.askforexpert.model;

/**
 * 分享图文信息
 *
 * @author 王鹏
 * @2015-8-4下午4:47:20
 */
public class ShareCardInfo {

    private String commentTxt;
    private String imageUrl;
    private String title;
    private String shareUrl;
    private String posts_id;
    private String friendXywyId;
    private String huanxinId;
    private String friendName;
    private String shareSource;

//    /**
//     * 试题首页  不需要 可以为空
//     * 试题列表页  分类id   classId
//     * 试题答题详情页 试卷id  paperId
//     */
//    private String shareAnswerId;
//    /**
//     * 标题
//     */
//    private String shareAnswerTitle;
//    /**
//     * answer_home;//试题首页
//     * answer_list";//试题列表页
//     * answer_detail";//试题答题详情页
//     */
//    private String shareAnswerType;

    /**
     * 试题版本
     */
    private String answerversion;
    /**
     * 视频id
     */
    private String shareUu;
    private String shareVu;

    /**
     * 职称
     */
    private String fCardTitle;
    /**
     * 所在医院
     */
    private String fCardHospital;
    /**
     * 所属科室
     */
    private String fCardDpart;

    private String fType;


    public String getPosts_id() {
        return posts_id;
    }

    public void setPosts_id(String posts_id) {
        this.posts_id = posts_id;
    }

    public String getCommentTxt() {
        return commentTxt;
    }

    public void setCommentTxt(String commentTxt) {
        this.commentTxt = commentTxt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getFriendXywyId() {
        return friendXywyId;
    }

    public void setFriendXywyId(String friendXywyId) {
        this.friendXywyId = friendXywyId;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getHuanxinId() {
        return huanxinId;
    }

    public void setHuanxinId(String huanxinId) {
        this.huanxinId = huanxinId;
    }

    public String getfCardDpart() {
        return fCardDpart;
    }

    public void setfCardDpart(String fCardDpart) {
        this.fCardDpart = fCardDpart;
    }

    public String getfCardHospital() {
        return fCardHospital;
    }

    public void setfCardHospital(String fCardHospital) {
        this.fCardHospital = fCardHospital;
    }

    public String getfCardTitle() {
        return fCardTitle;
    }

    public void setfCardTitle(String fCardTitle) {
        this.fCardTitle = fCardTitle;
    }

    public String getfType() {
        return fType;
    }

    public void setfType(String fType) {
        this.fType = fType;
    }

    public String getShareSource() {
        return shareSource;
    }

    public void setShareSource(String shareSource) {
        this.shareSource = shareSource;
    }

    public String getShareUu() {
        return shareUu;
    }

    public void setShareUu(String shareUu) {
        this.shareUu = shareUu;
    }

    public String getShareVu() {
        return shareVu;
    }

    public void setShareVu(String shareVu) {
        this.shareVu = shareVu;
    }

//    public String getShareAnswerId() {
//        return shareAnswerId;
//    }
//
//    public void setShareAnswerId(String shareAnswerId) {
//        this.shareAnswerId = shareAnswerId;
//    }
//
//    public String getShareAnswerTitle() {
//        return shareAnswerTitle;
//    }
//
//    public void setShareAnswerTitle(String shareAnswerTitle) {
//        this.shareAnswerTitle = shareAnswerTitle;
//    }
//
//    public String getShareAnswerType() {
//        return shareAnswerType;
//    }
//
//    public void setShareAnswerType(String shareAnswerType) {
//        this.shareAnswerType = shareAnswerType;
//    }

    public String getAnswerversion() {
        return answerversion;
    }

    public void setAnswerversion(String answerversion) {
        this.answerversion = answerversion;
    }
}
