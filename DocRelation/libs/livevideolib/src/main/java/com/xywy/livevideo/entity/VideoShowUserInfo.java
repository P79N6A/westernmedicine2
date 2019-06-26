package com.xywy.livevideo.entity;

/**
 * Created by bailiangjin on 2017/3/1.
 */

public class VideoShowUserInfo {

    /**
     * source 类型 医脉
     */
    public static final String SOURCE_TYPE_YIMAI = "SOURCE_TYPE_YIMAI";
    /**
     * source 类型 寻医问药
     */
    public static final String SOURCE_TYPE_XYWY = "SOURCE_TYPE_XYWY";

    /**
     * 用户id
     */
    private String id;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 用户头像url
     */
    private String headImageUrl;

    /**
     * 直播封面背景图像url
     */
    private String bgImageUrl;

    /**
     * 虚拟货币数量
     */
    private int virtualCoinCount;

    /**
     * 医脉or 寻医问药
     */
    private String source;


    public VideoShowUserInfo(String id, String nickName, String headImageUrl, String bgImageUrl, int virtualCoinCount, String source) {
        this.id = id;
        this.nickName = nickName;
        this.headImageUrl = headImageUrl;
        this.bgImageUrl = bgImageUrl;
        this.virtualCoinCount = virtualCoinCount;
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public String getBgImageUrl() {
        return bgImageUrl;
    }

    public void setBgImageUrl(String bgImageUrl) {
        this.bgImageUrl = bgImageUrl;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getVirtualCoinCount() {
        return virtualCoinCount;
    }

    public void setVirtualCoinCount(int virtualCoinCount) {
        this.virtualCoinCount = virtualCoinCount;
    }
}
