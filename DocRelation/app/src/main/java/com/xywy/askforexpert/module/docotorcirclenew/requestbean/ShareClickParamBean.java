package com.xywy.askforexpert.module.docotorcirclenew.requestbean;

/**
 *
 * 内容Cell 分享布局 点击事件 跳转请求参数
 * Created by bailiangjin on 2016/11/10.
 */
public class ShareClickParamBean {

    /**
     * 分享源
     */
    private String source;
    /**
     * 分享标题
     */
    private String title;
    /**
     * 分享跳转的WebUrl
     */
    private String url;
    /**
     * 分享图片URL
     */
    private String imageUrl;
    /**
     * 附加数据
     */
    private String extraData;

    public ShareClickParamBean(String source, String title, String url, String imageUrl, String extraData) {
        this.source = source;
        this.title = title;
        this.url = url;
        this.imageUrl = imageUrl;
        this.extraData = extraData;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }
}
