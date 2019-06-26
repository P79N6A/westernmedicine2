package com.xywy.askforexpert.model.doctor;

import java.util.List;

/**
 * 医圈医生信息
 *
 * @author LG
 */
public class doctorInfosBean {

    public String code;
    public String msg;
    public InfoData data;

    public class InfoData {
        /**
         * {
         * "code": "0",
         * "msg": "\u6210\u529f",
         * "data": {
         * "photo": "http:\/\/doctor.club.xywy.com\/images\/images\/userface\/Image2.gif",
         * "userid": "8923937",
         * "nickname": "\u8096\u51ef\u4f0a",
         * "subject": "\u5176\u4ed6",
         * "hospital": "\u89e3\u653e\u519b305\u533b\u9662",
         * "synopsis": "\u7ea2\u8272",
         * "job": "\u4e94\u7ea7\u8425\u517b\u5e08",
         * "sex": "",
         * "dynamiccount": "10",
         * "relation": 0
         * }
         * }
         */

        public String photo;
        public String userid;
        public String nickname;
        public String subject;
        public String hospital;
        public String synopsis;
        public String job;
        public String sex;
        public String dynamiccount;
        public String relation;

        public List<FocusedUser> watchedlist;
    }
}
