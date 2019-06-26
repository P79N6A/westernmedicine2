package com.xywy.askforexpert.model.relatedNews;

import org.json.JSONObject;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/1/15 15:17
 */
public class RelatedNewsRootData {
    private int code;
    private RelatedNewsItem item;
    private String msg;

    public int getCode() {
        return code;
    }

    public RelatedNewsItem getItem() {
        return item;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "RelatedNewsRootData{" +
                "code=" + code +
                ", item=" + item +
                ", msg='" + msg + '\'' +
                '}';
    }

    public void parseJson(JSONObject jsonObject) {
        if (jsonObject != null) {
            code = jsonObject.optInt("code");
            msg = jsonObject.optString("msg");

            JSONObject object = jsonObject.optJSONObject("list");
            item = new RelatedNewsItem();
            item.parseJson(object);
        }
    }
}
