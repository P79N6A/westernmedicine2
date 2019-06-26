package com.xywy.askforexpert.module.docotorcirclenew.requestbean;

import com.xywy.askforexpert.module.docotorcirclenew.requestbean.type.ShareDirectionType;

/**
 * Created by bailiangjin on 2016/11/9.
 */

public class ShareParamBean {

    private String title;
    private String content;
    private String targetUrl;
    private String imgUrl;
    private String shareId;

    private ShareDirectionType shareDirectionType;

    public ShareParamBean(String title, String content, String targetUrl, String imgUrl, String shareId, ShareDirectionType shareDirectionType) {
        this.title = title;
        this.content = content;
        this.targetUrl = targetUrl;
        this.imgUrl = imgUrl;
        this.shareId = shareId;
        this.shareDirectionType = shareDirectionType;
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

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    public ShareDirectionType getShareDirectionType() {
        return shareDirectionType;
    }

    public void setShareDirectionType(ShareDirectionType shareDirectionType) {
        this.shareDirectionType = shareDirectionType;
    }
}
