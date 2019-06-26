package com.xywy.askforexpert.model.topics;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/4/15 15:06
 */
public class TopicEntity implements Parcelable, Comparable<TopicEntity> {

    public static final Creator<TopicEntity> CREATOR = new Creator<TopicEntity>() {
        @Override
        public TopicEntity createFromParcel(Parcel in) {
            return new TopicEntity(in);
        }

        @Override
        public TopicEntity[] newArray(int size) {
            return new TopicEntity[size];
        }
    };
    /**
     * 话题名
     */
    private String topicName;
    /**
     * 话题id
     */
    private int topicId;
    private int startIndex;
    private int endIndex;
    /**
     * 添加头信息
     */

    public TopicEntity(String topicName, int topicId) {
        this.topicName = topicName;
        this.topicId = topicId;
    }

    public TopicEntity(String topicName, int topicId, int startIndex, int endIndex) {
        this.topicName = topicName;
        this.topicId = topicId;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    protected TopicEntity(Parcel in) {
        topicName = in.readString();
        topicId = in.readInt();
        startIndex = in.readInt();
        endIndex = in.readInt();
    }

    public static List<TopicEntity> parseJson(JSONArray array) {
        List<TopicEntity> topicEntities = new ArrayList<>();
        if (array != null) {
            int length = array.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = array.optJSONObject(i);
                if (jsonObject != null) {
                    String name = jsonObject.optString("topicName");
                    int id = jsonObject.optInt("topicId");
                    int startIndex = jsonObject.optInt("startIndex");
                    int endIndex = jsonObject.optInt("endIndex");

                    topicEntities.add(new TopicEntity(name, id, startIndex, endIndex));
                }
            }
        }
        return topicEntities;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(topicName);
        dest.writeInt(topicId);
        dest.writeInt(startIndex);
        dest.writeInt(endIndex);
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }



    @Override
    public String toString() {
        return (topicName.substring(0, topicName.length() - 1) +
                "$" + topicId + "$ " +
                topicName.substring(topicName.length() - 1, topicName.length()));
    }

    @Override
    public int compareTo(@NonNull TopicEntity entity) {
        return this.getStartIndex() - entity.getStartIndex();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o instanceof TopicEntity) {
            TopicEntity t = (TopicEntity) o;
            return this.topicName.equals(t.topicName) && this.topicId == t.topicId;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hashCode = 17;
        hashCode = hashCode * 31 + topicId;
        hashCode = hashCode * 31 + topicName.hashCode();
        return hashCode;
    }
}
