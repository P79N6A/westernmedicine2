package com.xywy.askforexpert.model.relatedNews;

import org.json.JSONObject;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/3/15 15:20
 */
public class Media {
    private String img;
    private String mediaid;

    public String getImg() {
        return img;
    }

    public String getMediaid() {
        return mediaid;
    }

    public void parseJson(JSONObject jsonObject) {
        if (jsonObject != null) {
            img = jsonObject.optString("img");
            mediaid = jsonObject.optString("mediaid");
        }
    }
}
