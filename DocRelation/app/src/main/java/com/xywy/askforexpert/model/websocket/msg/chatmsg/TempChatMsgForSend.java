package com.xywy.askforexpert.model.websocket.msg.chatmsg;

import com.xywy.askforexpert.model.websocket.msg.BaseSocketMsg;
import com.xywy.askforexpert.model.websocket.type.ActType;

/**
 * 临时发消息类 websocket接口设计问题 收发消息格式不统一 服务端（邢林沱）承诺下个版本统一消息格式
 * 新版本去掉id,转而新添加msg_id,from to类型有变化 stone
 */
public class TempChatMsgForSend extends BaseSocketMsg {


    /**
     * from : 1230
     * to : 12345
     * id : 客户端生成 id
     * act : PUB
     * body : {"content":"这些药物效果比较好。","qid":"5797"}
     */

    private String from;
    private String to;
    private String id;
    private BodyBean body;

    public TempChatMsgForSend(String from, String to, String id, BodyBean body) {
        this.act = ActType.PUB;
        this.from = from;
        this.to = to;
        this.id = id;
        this.body = body;
    }


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class BodyBean {

        /**
         * content : 这些药物效果比较好。
         * qid : 5797
         */
        private String content;
        private String qid;
        private String time;
        private String type;


        public BodyBean(String qid, String content) {
            this.qid = qid;
            this.content = content;
        }

        public BodyBean(String content, String qid, String time, String type) {
            this.content = content;
            this.qid = qid;
            this.time = time;
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getQid() {
            return qid;
        }

        public void setQid(String qid) {
            this.qid = qid;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    // TODO: 2018/5/29 websocket新版本 stone
//    private int from;
//    private int to;
//    private int msg_id;
//    private BodyBean body;
//
//    public int getFrom() {
//        return from;
//    }
//
//    public void setFrom(int from) {
//        this.from = from;
//    }
//
//    public int getTo() {
//        return to;
//    }
//
//    public void setTo(int to) {
//        this.to = to;
//    }
//
//    public int getMsg_id() {
//        return msg_id;
//    }
//
//    public void setMsg_id(int msg_id) {
//        this.msg_id = msg_id;
//    }
//
//    public BodyBean getBody() {
//        return body;
//    }
//
//    public void setBody(BodyBean body) {
//        this.body = body;
//    }
//
//    public TempChatMsgForSend(int from, int to, int msg_id, BodyBean body) {
//        this.act = ActType.PUB;
//        this.from = from;
//        this.to = to;
//        this.msg_id = msg_id;
//        this.body = body;
//    }
//
//
//    public static class BodyBean {
//
//        /**
//         * content : 这些药物效果比较好。
//         * qid : 5797
//         */
//        private String content;
//        private int qid;
//        private String time;
//        private String type;
//
//        public BodyBean(int qid, String content) {
//            this.qid = qid;
//            this.content = content;
//        }
//
//
//        public BodyBean(String content, int qid, String time, String type) {
//            this.content = content;
//            this.qid = qid;
//            this.time = time;
//            this.type = type;
//        }
//
//
//        public String getContent() {
//            return content;
//        }
//
//        public void setContent(String content) {
//            this.content = content;
//        }
//
//        public int getQid() {
//            return qid;
//        }
//
//        public void setQid(int qid) {
//            this.qid = qid;
//        }
//
//        public String getTime() {
//            return time;
//        }
//
//        public void setTime(String time) {
//            this.time = time;
//        }
//
//        public String getType() {
//            return type;
//        }
//
//        public void setType(String type) {
//            this.type = type;
//        }
//    }
}
