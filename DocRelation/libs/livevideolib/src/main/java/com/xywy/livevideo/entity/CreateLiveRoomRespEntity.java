package com.xywy.livevideo.entity;

/**
 * Created by zhangzheng on 2017/3/8.
 */
public class CreateLiveRoomRespEntity {


    /**
     * code : 10000
     * msg : 成功
     * data : {"id":249,"userid":"68256258","name":"太寂寞沙洲冷","rtmp":"rtmp://7958.livepush.myqcloud.com/live/7958_6af7ba12?bizid=7958&txSecret=f721c7b0780d7248a813aecef33f74c9&txTime=58C77A66","vod":"","state":1,"amount":0,"cover":"http://xs3.op.xywy.com/api.app.xywy.com/appimg/20170313/58c628de9f0e5.jpg","chatroomsid":"10468150280194","createtime":"2017-03-13 13:06:46"}
     */

    private int code;
    private String msg;
    /**
     * id : 249
     * userid : 68256258
     * name : 太寂寞沙洲冷
     * rtmp : rtmp://7958.livepush.myqcloud.com/live/7958_6af7ba12?bizid=7958&txSecret=f721c7b0780d7248a813aecef33f74c9&txTime=58C77A66
     * vod :
     * state : 1
     * amount : 0
     * cover : http://xs3.op.xywy.com/api.app.xywy.com/appimg/20170313/58c628de9f0e5.jpg
     * chatroomsid : 10468150280194
     * createtime : 2017-03-13 13:06:46
     */

    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int id;
        private String userid;
        private String name;
        private String rtmp;
        private String vod;
        private int state;
        private int amount;
        private String cover;
        private String chatroomsid;
        private String createtime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRtmp() {
            return rtmp;
        }

        public void setRtmp(String rtmp) {
            this.rtmp = rtmp;
        }

        public String getVod() {
            return vod;
        }

        public void setVod(String vod) {
            this.vod = vod;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getChatroomsid() {
            return chatroomsid;
        }

        public void setChatroomsid(String chatroomsid) {
            this.chatroomsid = chatroomsid;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }
    }
}
