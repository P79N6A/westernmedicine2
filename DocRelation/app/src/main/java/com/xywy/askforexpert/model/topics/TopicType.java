package com.xywy.askforexpert.model.topics;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/4/21 13:57
 */
public class TopicType implements Parcelable {
    public static final Creator<TopicType> CREATOR = new Creator<TopicType>() {
        @Override
        public TopicType createFromParcel(Parcel in) {
            return new TopicType(in);
        }

        @Override
        public TopicType[] newArray(int size) {
            return new TopicType[size];
        }
    };
    /**
     * code : 0
     * msg : 成功
     * list : [{"id":"2","name":"0","createtime":"1461061181"},{"id":"1","name":"生活","createtime":"1460966851"}]
     */

    private String code;
    private String msg;
    /**
     * id : 2
     * name : 0
     * createtime : 1461061181
     */

    private List<TopicTypeBean> list;

    protected TopicType(Parcel in) {
        code = in.readString();
        msg = in.readString();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<TopicTypeBean> getList() {
        return list;
    }

    public void setList(List<TopicTypeBean> list) {
        this.list = list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(msg);
    }

    public static class TopicTypeBean implements Parcelable {
        public static final Creator<TopicTypeBean> CREATOR = new Creator<TopicTypeBean>() {
            @Override
            public TopicTypeBean createFromParcel(Parcel in) {
                return new TopicTypeBean(in);
            }

            @Override
            public TopicTypeBean[] newArray(int size) {
                return new TopicTypeBean[size];
            }
        };
        private String id;
        private String name;
        private String createtime;

        protected TopicTypeBean(Parcel in) {
            id = in.readString();
            name = in.readString();
            createtime = in.readString();
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(name);
            dest.writeString(createtime);
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }

            if (o instanceof TopicTypeBean) {
                TopicTypeBean bean = (TopicTypeBean) o;
                return this.name.equals(bean.getName())
                        && this.id.equals(bean.getId())
                        && this.createtime.equals(bean.getCreatetime());
            }

            return false;
        }

        @Override
        public int hashCode() {
            return Integer.parseInt(id) + 31;
        }
    }
}
