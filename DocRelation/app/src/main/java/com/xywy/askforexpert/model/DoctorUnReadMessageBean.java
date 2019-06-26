package com.xywy.askforexpert.model;

import java.io.Serializable;
import java.util.List;

/**
 * 未读信息
 *
 * @author LG
 */
public class DoctorUnReadMessageBean implements Serializable {

    public int code;
    public String msg;
    public List<ListItem> list;

    @Override
    public String toString() {
        return "DoctorUnReadMessageBean [code=" + code + ", msg=" + msg
                + ", list=" + list + "]";
    }

    public class ListItem implements Serializable {
        /**
         * "num": 2,
         * "content": "等2人赞了我的动态",
         * "nomalDate": "42分钟前",
         * "userrow": {
         * "userid": "57145967",
         * "nickname": "你猜",
         * "userphoto": "http://doctor.club.xywy.com/images/upload/paper/2015052210172735013.jpg"
         * },
         * "dynamic": "给你无聊",
         * "dynamicid": "207",
         * "type": 1
         */
        public String id;
        public String num;
        public String content;
        public String nomalDate;
        public Userrow userrow;
        public String source;
        public String dynamic;
        public String dynamicid;
        public String type;

        public class Userrow implements Serializable {
            public String userid;
            public String nickname;
            public String userphoto;
        }

    }

}
