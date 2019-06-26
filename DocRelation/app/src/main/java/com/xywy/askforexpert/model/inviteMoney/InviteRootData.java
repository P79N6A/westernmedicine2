package com.xywy.askforexpert.model.inviteMoney;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 邀请发钱啦 接口返回JSON根数据
 * <p>
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2015/11/27 11:17
 */
public class InviteRootData {
    /**
     * 接口返回码
     */
    private int code;

    /**
     * 接口返回信息
     */
    private String msg;

    /**
     * 已邀请的好友数
     */
    private int docNum;

    /**
     * 通过邀请获得的钱数
     */
    private int reward;

    /**
     * 我的邀请链接
     */
    private String url;

    /**
     * 已邀请的好友列表
     */
    private List<InviteDocList> docLists;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public int getDocNum() {
        return docNum;
    }

    public int getReward() {
        return reward;
    }

    public String getUrl() {
        return url;
    }

    public List<InviteDocList> getDocLists() {
        return docLists;
    }

    @Override
    public String toString() {
        return "InviteRootData{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", docNum=" + docNum +
                ", reward=" + reward +
                ", url='" + url + '\'' +
                ", docLists=" + docLists +
                '}';
    }

    public void parseJson(JSONObject jsonObject) {
        if (jsonObject != null) {
            code = jsonObject.optInt("code");
            msg = jsonObject.optString("msg");
            docNum = jsonObject.optInt("docNum");
            reward = jsonObject.optInt("reward");
            url = jsonObject.optString("url");

            JSONArray array = jsonObject.optJSONArray("doclist");
            docLists = new ArrayList<>();
            if (array != null) {
                for (int i = 0, n = array.length(); i < n; i++) {
                    try {
                        JSONObject object = array.getJSONObject(i);

                        InviteDocList docList = new InviteDocList();
                        docList.parseJson(object);

                        docLists.add(docList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
