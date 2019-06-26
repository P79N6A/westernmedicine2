package com.xywy.askforexpert.model;

import java.io.Serializable;
import java.util.List;

/**
 * 动态 详情model
 *
 * @author LG
 */
public class DynamicDtaileBean implements Serializable {

    public String code;
    public String msg;
    public Data data;

    public class Data implements Serializable {

        /**
         * "id": "229", "userid": "57145967", "level": "0", "nickname": "你猜",
         * "content": "很婆婆婆婆里面模棱两可", "userphoto":
         * "http://doctor.club.xywy.com/images/upload/paper/2015052210172735013.jpg"
         * , "subject": "皮肤科", "hospital": null, "createtime": "19小时前", "type":
         * "1", "imgs": [], "praiseNum": "1", "commentNum": "0", "praiselist": [
         * { "userid": "57145967", "nickname": "你猜" } ], "commlist": []
         */
        public String id;
        public String userid;
        public String level;
        public String nickname;
        public String content;
        public String userphoto;
        public String subject;
        public String hospital;
        public String createtime;
        public String is_praise;
        public String type;
        public List<String> imgs;
        public String praiseNum;
        public String commentNum;

        public List<PraiseList> praiselist;
        public List<CommList> commlist;

        public class PraiseList implements Serializable {

            public String userid;
            public String nickname;
        }

        public class CommList implements Serializable {
            /**
             * "commlist": [
             * {
             * "user": {
             * "nickname": "郭瑞",
             * "photo": "http://i0.wkimg.com/source/c2/54490688.jpg",
             * "subject": "内科",
             * "hospital": ""
             * },
             * "touser": {
             * "nickname": "你猜",
             * "photo": "http://doctor.club.xywy.com/images/upload/paper/2015052210172735013.jpg",
             * "subject": "皮肤科",
             * "hospital": ""
             * },
             * "content": "啊啊",
             * "id": "43"
             * },
             */
            public User user;
            public Touser touser;
            public String content;
            public String id;
            public CommList() {

            }

            public class User implements Serializable {
                public String userid;
                public String nickname;
                public String photo;
                public String subject;
                public String hospital;
            }

            public class Touser implements Serializable {

                public String nickname;
                public String photo;
                public String subject;
                public String hospital;
            }

        }

    }


}
