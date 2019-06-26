package com.xywy.askforexpert.model.subscribe;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/7/7 14:46
 */

/**
 * 订阅媒体号列表
 */
public class SubscribeMediaBean implements Parcelable {
    public static final Creator<SubscribeMediaBean> CREATOR = new Creator<SubscribeMediaBean>() {
        @Override
        public SubscribeMediaBean createFromParcel(Parcel in) {
            return new SubscribeMediaBean(in);
        }

        @Override
        public SubscribeMediaBean[] newArray(int size) {
            return new SubscribeMediaBean[size];
        }
    };
    /**
     * id : 87492994
     * name : 中国医生的一天
     * type : 1
     */

    private int id;
    private String name;
    private int type;

    public SubscribeMediaBean() {
    }

    protected SubscribeMediaBean(Parcel in) {
        id = in.readInt();
        name = in.readString();
        type = in.readInt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(type);
    }

    @Override
    public String toString() {
        return "SubscribeMediaBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o != null) {
            if (o == this) {
                return true;
            }

            if (o instanceof SubscribeMediaBean) {
                SubscribeMediaBean another = (SubscribeMediaBean) o;
                return this.id == another.id && this.name.equals(another.name);
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
