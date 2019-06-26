package com.xywy.askforexpert.model.relatedNews;

import org.json.JSONObject;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/1/15 15:17
 */
public class RelatedNewsItem {
    private String id;
    private String title;
    private String image;
    private String createtime;
    private String praiseNum;
    private String url;
    private String comNum;
    private int colle;
    private int praise;
    private int model;
    private int type;
    private long mediaid;
    private int is_subscription;
    private Media media;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getCreatetime() {
        return createtime;
    }

    public String getPraiseNum() {
        return praiseNum;
    }

    public String getUrl() {
        return url;
    }

    public String getComNum() {
        return comNum;
    }

    public int getColle() {
        return colle;
    }

    public int getPraise() {
        return praise;
    }

    public int getModel() {
        return model;
    }

    public int getType() {
        return type;
    }

    public long getMediaid() {
        return mediaid;
    }

    public int getIs_subscription() {
        return is_subscription;
    }

    public Media getMedia() {
        return media;
    }

    public void parseJson(JSONObject jsonObject) {
        if (jsonObject != null) {
            id = jsonObject.optString("id");
            title = jsonObject.optString("title");
            image = jsonObject.optString("image");
            createtime = jsonObject.optString("createtime");
            praiseNum = jsonObject.optString("praiseNum");
            url = jsonObject.optString("url");
            comNum = jsonObject.optString("comNum");
            colle = jsonObject.optInt("colle");
            praise = jsonObject.optInt("praise");
            mediaid = jsonObject.optLong("mediaid");
            model = jsonObject.optInt("model");
            type = jsonObject.optInt("type");
            is_subscription = jsonObject.optInt("is_subscription");

            JSONObject object = jsonObject.optJSONObject("media");
            media = new Media();
            media.parseJson(object);
        }
    }
}
