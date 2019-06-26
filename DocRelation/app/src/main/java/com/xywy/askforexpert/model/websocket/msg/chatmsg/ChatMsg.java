package com.xywy.askforexpert.model.websocket.msg.chatmsg;

import com.xywy.askforexpert.model.websocket.msg.BaseSocketMsg;
import com.xywy.askforexpert.model.websocket.type.ActType;

import java.util.HashMap;
import java.util.Map;

/**
 * 新版本msg_id替换id from to msg_id类型变化 stone
 */

public class ChatMsg extends BaseSocketMsg {


//    {
//	    "act":"PUB",		//消息传输控制类型
//                "from":"123",
//                "to":"123",
//                "id":"id1234",		//消息ID
//                "seq":"连续序列",
//                "body":{
//        "qid":1,		//问题ID
//                "name":"aaa",		//患者姓名
//                "sex":1,		//患者性别
//                "age":20,		//患者年龄
//                "user_photo":"/im-static/images/18-40-2.png",   //头像
//                "pic":[
//        "http://cp01-ocean-400.epc.baidu.com:8082/661fc.jpg",
//                "http://cp01-ocean-400.epc.baidu.com:8082/661fc.jpg"
//		],
//        "content":{
//            "type":"text",
//                    "text":"问题内容"
//        },
//        "time":4568255,		//时间戳，单位秒
//                "type":101001		//提问
//    }
//}

    public static final int RECV_MSG_TYPE_NEW_NOTICE = 101010;    //stone 忙线新消息提醒 新添加的 5.4

    public static final int RECV_MSG_TYPE_ASK = 101001;    //患者提问
    public static final int RECV_MSG_TYPE_NORMAL = 101002;    //患者追问
    public static final int RECV_MSG_TYPE_QUESTION_OVER_TIME = 101003;    //问题超时
    public static final int RECV_MSG_TYPE_CLOSED = 101004;    //问题已关闭
    public static final int RECV_MSG_TYPE_ACCEPT_OVER_TIME = 101005;  //医生接诊超时
    public static final int RECV_MSG_TYPE_UN_ADOPT = 101006;  //未认领
    public static final int RECV_MSG_TYPE_DOCTOR_FORBID = 101007; //医生被停止服务
    public static final int RECV_MSG_TYPE_NIGHT_OFFLINE = 101010; //夜间或者离线留言
    public static Map<Integer, String> RECV_ERROR_MSG_TIP;

    static {
        RECV_ERROR_MSG_TIP = new HashMap<>();
        RECV_ERROR_MSG_TIP.put(Integer.valueOf(RECV_MSG_TYPE_QUESTION_OVER_TIME), "问题超时");
        RECV_ERROR_MSG_TIP.put(Integer.valueOf(RECV_MSG_TYPE_CLOSED), "问题已关闭");
        RECV_ERROR_MSG_TIP.put(Integer.valueOf(RECV_MSG_TYPE_ACCEPT_OVER_TIME), "接诊超时");
        RECV_ERROR_MSG_TIP.put(Integer.valueOf(RECV_MSG_TYPE_UN_ADOPT), "未认领");
        RECV_ERROR_MSG_TIP.put(Integer.valueOf(RECV_MSG_TYPE_DOCTOR_FORBID), "医生被停止服务");
    }


    private String from;
    private String to;
    private String id;
    private String seq;
    private BodyBean body;

    /**
     * 客户端 发送消息时使用的构造方法
     * @param from
     * @param to
     * @param id
     * @param body
     */
    public ChatMsg(String from, String to, String id, BodyBean body) {
        this.act= ActType.PUB;
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

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    // TODO: 2018/5/29 websocket新版本 stone
//    private int from;
//    private int to;
//    private int msg_id;
//    private String seq;
//    private BodyBean body;
//
//    /**
//     * 客户端 发送消息时使用的构造方法
//     * @param from
//     * @param to
//     * @param msg_id
//     * @param body
//     */
//    public ChatMsg(int from, int to, int msg_id, BodyBean body) {
//        this.act= ActType.PUB;
//        this.from = from;
//        this.to = to;
//        this.msg_id = msg_id;
//        this.body = body;
//    }
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
//    public String getSeq() {
//        return seq;
//    }
//
//    public void setSeq(String seq) {
//        this.seq = seq;
//    }
//
//    public BodyBean getBody() {
//        return body;
//    }
//
//    public void setBody(BodyBean body) {
//        this.body = body;
//    }
}
