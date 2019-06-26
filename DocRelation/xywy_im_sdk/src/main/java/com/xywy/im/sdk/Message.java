package com.xywy.im.sdk;

import com.xywy.im.sdk.tool.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;


/**
 * Created by xugan on 2018/8/14.
 */

public class Message implements Serializable {
    public static final int MSGTYPE_TEXT = 2;//消息类型是文字
    public static final int MSGTYPE_IMG = 4;//消息类型是图片
    public static final int MSGTYPE_SYSTEM = 6;//消息类型是系统消息
    public static final int MSGTYPE_TIME_BASE = 8;//消息类型是时间
    public static final int MSGTYPE_SYSTEM_REWARD = 10;//消息类型是系统消息，并且是打赏的消息
    public static final int MSGTYPE_COMMENT = 12;//消息类型是评价消息


    public static final int MSG_OUT = 1; //1 表示发送出去的消息
    public static final int MSG_IN = 0;  //0 表示接受的消息

    private static final String TEXT_STR = "text";
    private static final String IMAGE_STR = "image";
    public static final String REWARD_STR_EN = "reward";
    public static final String COMMENT_STR_EN = "comment";

    private String msgId;

    private Long sender;

    private Long receiver;

    private Long time;

    private String content;

    private int msgType;    // 0 表示 文字，1 表示图片

    private int isOutgoing; //  0 表示接收到的消息，1 表示发送出去的消息

    private int sendState;//消息发送状态

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    private int groupId; //群id

    public int getToUserNum() {
        return toUserNum;
    }

    public void setToUserNum(int toUserNum) {
        this.toUserNum = toUserNum;
    }

    private int toUserNum; //单独发送到群内的用户数

    public HashSet<Integer> getToUids() {
        return toUids;
    }

    public void setToUids(HashSet<Integer> toUids) {
        this.toUids = toUids;
    }

    private HashSet<Integer> toUids ; //目标用户id所存放的数组

    //以下字段未存入数据库
    private int cmd;

    //针对医脉的特殊需求，将qid也存入数据库
    public long getQid() {
        return qid;
    }

    public void setQid(long qid) {
        this.qid = qid;
    }

    private long qid;//问题id

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String userName;//消息所有者的名称

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    private String headUrl;//消息所有者的头像




    /**
     * 将消息对象打包成byte数组
     * 客户端发送数据时所带的数据中的几种cmd
     * CONNECT,	        1	客户端发送到服务器端的第一个包必须是connect包,包中的数据中所带的cmd
     * PUBLISH		    3	客户端发送的数据中所带的cmd
     * GROUP_PUB_ACK	6	客户端收到服务端推送的群消息后，返回给服务端的数据中所带的cmd
     * DISCONNECT		9	客户端主动断开连接时数据中所带的数据的cmd
     * PING_RESP	    8	客户端收到服务端的心跳包后，客户端回复心跳包时，所带的cmd
     * PUB_ACK		    4	客户端收到服务端推送的消息后，返回给服务端的数据中所带的cmd
     * GROUP_PUBLISH    5   客户端发送群消息的数据中所带的cmd
     * GROUP_PART_PUBLISH   11      客户端对群内部分用户发送消息
     * GROUP_PART_PUB_ACK   12      客户端收到服务端对群内部分用户发送的消息后，返回给服务端的数据中所带的cmd
     *
     * @return
     */
    public byte[] pack(){
        if (cmd == 1) {
            byte[] startBytes = CommonUtils.int2Bytes(Constant.CONNECT,1);
            byte[] userNameLengthBytes = CommonUtils.intToByteArray(WebSocketApi.getInStance().UserName().length());
            byte[] pwdLengthBytes = CommonUtils.intToByteArray(WebSocketApi.getInStance().Pwd().length());
            try {
                byte[] userNameBytes = WebSocketApi.getInStance().UserName().getBytes("utf-8");
                byte[] pwdBytes = WebSocketApi.getInStance().Pwd().getBytes("utf-8");
                byte[] connectBytes = CommonUtils.byteMergerAll(startBytes,userNameLengthBytes,
                        userNameBytes,pwdLengthBytes,pwdBytes);
                return connectBytes;
            } catch (UnsupportedEncodingException e) {
                LogUtils .i(""+e.getMessage());
                e.printStackTrace();
                return null;
            }
        } else if (cmd ==3) {
            byte[] startBytes = CommonUtils.int2Bytes(Constant.PUBLISH,1);

            byte[] toUidBytes = CommonUtils.int2Bytes(receiver,4);
            LogUtils.i("toUidBytes="+ Arrays.toString(toUidBytes)+"---toUid=="+ BytePacket.readInt32(toUidBytes, 0));
            try {
                byte[] msgIdBytes = msgId.getBytes("utf-8");
                byte[] payloadBytes = content.getBytes("utf-8");
                byte[] payloadLenBytes = CommonUtils.int2Bytes(payloadBytes.length,2);
                LogUtils.e("payloadBytes.length="+payloadBytes.length);
                return CommonUtils.byteMergerAll(startBytes,toUidBytes, msgIdBytes,payloadLenBytes,
                        payloadBytes);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }else if (cmd ==5) {
            //发群消息
            byte[] startBytes = CommonUtils.int2Bytes(Constant.GROUP_PUBLISH,1);
            byte[] toGroupIdBytes = CommonUtils.int2Bytes(groupId,4);
            try{
                byte[] msgIdBytes = msgId.getBytes("utf-8");
                byte[] payloadBytes = content.getBytes("utf-8");
                byte[] payloadLenBytes = CommonUtils.int2Bytes(payloadBytes.length,2);
                return CommonUtils.byteMergerAll(startBytes,toGroupIdBytes,msgIdBytes,payloadLenBytes,payloadBytes);
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
                return null;
            }
        }else if (cmd ==9) {
            return CommonUtils.int2Bytes(Constant.DISCONNECT,1);
        }else if (cmd ==8) {
            return CommonUtils.int2Bytes(Constant.PING_RESP,1);
        }else if (cmd ==4) {
            byte[] startBytes = CommonUtils.int2Bytes(Constant.PUB_ACK,1);
            try {
                byte[] msgIdBytes = msgId.getBytes("utf-8");
                return CommonUtils.byteMergerAll(startBytes,msgIdBytes);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else if(cmd == 6){
            //客户端收到服务端推送的群消息后，返回给服务端的应答数据
            byte[] startBytes = CommonUtils.int2Bytes(Constant.GROUP_PUB_ACK,1);
            try {
                byte[] msgIdBytes = msgId.getBytes("utf-8");
                return CommonUtils.byteMergerAll(startBytes,msgIdBytes);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else if(cmd == 11){
            //客户端对群内部分用户发送消息
            byte[] startBytes = CommonUtils.int2Bytes(Constant.GROUP_PART_PUBLISH,1);
            byte[] toGroupIdBytes = CommonUtils.int2Bytes(groupId,4);
            try {
                byte[] msgIdBytes = msgId.getBytes("utf-8");
                byte[] toUserNumBytes = CommonUtils.int2Bytes(toUserNum,2);
                if(null == toUids){
                    return null;
                }
                byte[] toUidsBytes = new byte[0];
                for(Integer toUid:toUids){
                    byte[] bytes = CommonUtils.int2Bytes(toUid, 4);
                    toUidsBytes = CommonUtils.byteMergerAll(toUidsBytes,bytes);
                }
                byte[] payloadBytes = content.getBytes("utf-8");
                byte[] payloadBytesLen = CommonUtils.int2Bytes(payloadBytes.length,2);
                return CommonUtils.byteMergerAll(startBytes,toGroupIdBytes,msgIdBytes,
                        toUserNumBytes,toUidsBytes,payloadBytesLen,payloadBytes);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else if(cmd == 12){
            //客户端收到服务端对群内部分用户发送的消息后，返回给服务端的数据
            byte[] startBytes = CommonUtils.int2Bytes(Constant.GROUP_PART_PUB_ACK,1);
            try {
                byte[] msgIdBytes = msgId.getBytes("utf-8");
                return CommonUtils.byteMergerAll(startBytes,msgIdBytes);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 解包如果出错则返回false,否则返回true
     * @param data
     * @return
     *
     * 收到服务端的应答的几种cmd
     * CONNECT_ACK,     2	客户端连接上服务端后，服务端返回的数据中所带的cmd
     * PUB_ACK		    4	客户端消息发送后，服务端返回的数据中所带的cmd
     * GROUP_PUB_ACK	6	客户端群消息发送后，服务端返回的数据中所带的cmd
     * PING		        7	服务端发送的心跳包中所带的数据的cmd
     * PUBLISH		    3	服务端推送消息时，所带的cmd
     * GROUP_PUBLISH    5   服务端推送的群消息,所带的cmd
     * GROUP_PART_PUBLISH  11  服务端推送给群里的部分人的消息，所带的cmd
     * GROUP_PART_PUB_ACK  12  客户端给群里部分人发送消息后，服务端返回的数据中所带的cmd
     * DISCONNECT		9	客户端主动断开连接时数据中所带的数据的cmd
     * INVALID_PACKET		10	服务端收到无效的数据包后通知客户端时数据中所带的数据的cmd
     * LOGOUT		    20	服务端通知客户端连接被挤掉, 其他点登陆挤掉当前连接时数据中所带的cmd
     */
    public boolean unpack(byte[] data){
        int cmd = data[0] & 0xFF;
//        LogUtils.e("cmd= "+cmd);
        if (cmd == 0x02) {  //CONNECT_ACK
            this.cmd = (byte)Constant.CONNECT_ACK;
            //连接上服务端后，服务端返回的应答消息
            int code = data[1] & 0xFF;
            LogUtils.i("code="+code);
            switch (code){
                case 0:
                    //0: Connection accepted, 连接成功

                    return true;
                case 1:
                    //1: Connection dup, 重复建立连接
                    return false;
                case 3:
                    //3: Username or password is error, 用户名或密码错误
                    LogUtils.i("用户名或密码错误");
                    return false;
                default:
                    return false;
            }
        }  else if (cmd == 0x03) {
            //服务器端会推送消息到客户端
            this.cmd = Constant.PUBLISH;
            byte[] msgIdByte = new byte[32];
            System.arraycopy(data,1,msgIdByte,0,32);
            byte[] payLoadLenByteArray = new byte[2];
            System.arraycopy(data,33,payLoadLenByteArray,0,2);
            short payLoadLen = BytePacket.readInt16(payLoadLenByteArray, 0);
//            LogUtils.i("payLoadLen="+payLoadLen);
            byte[] payLoadByteArray = new byte[payLoadLen];
            System.arraycopy(data,35,payLoadByteArray,0,payLoadLen);
//            LogUtils.i(Arrays.toString(data));
//            LogUtils.i(Arrays.toString(payLoadByteArray));
            try {
                String msg_id = new String(msgIdByte,"utf-8");
                String content = new String(payLoadByteArray,"utf-8");
                JSONObject body = new JSONObject(content);
                this.content = content;
                this.msgId = msg_id;
                LogUtils.i("msg_id= "+msg_id+"   content= "+content);
                this.sender = Long.parseLong(body.getString("from"));
                this.receiver = Long.parseLong(body.getString("to"));
                this.isOutgoing = Message.MSG_IN;
                this.time = System.currentTimeMillis();
                this.sendState = MessageSendState.MESSAGE_SEND_SUCCESS;
                //后期如果要接收图片，语音等吗，还要设置消息类型,将消息类型存储到发送的数据的json体中
                String innerContent = body.getString("content");
                JSONObject innerContentJSONObject = new JSONObject(innerContent);
                String type = innerContentJSONObject.getString("type");
                String action = "";
                if(body.has("action")){
                    action = body.getString("action");
                }
                if(Constant.FAILBACK.equals(action)){
                    this.msgId = this.msgId+"_"+new String(UUID.randomUUID().toString().replace("-", "").getBytes(),"utf-8").toString();
                }
                LogUtils.e("action=  "+action+"   "+this.msgId);
                if(TEXT_STR.equals(type)){
                    this.msgType = MSGTYPE_TEXT;
                }else if(IMAGE_STR.equals(type)){
                    this.msgType = MSGTYPE_IMG;
                }else if(REWARD_STR_EN.equals(type)){
                    this.msgType = MSGTYPE_SYSTEM_REWARD;
                }else if(COMMENT_STR_EN.equals(type)){
                    this.msgType = MSGTYPE_COMMENT;
                }
                if(body.has("role")){
                    if("0".equals(body.getString("role"))){
                        this.msgType = MSGTYPE_SYSTEM;
                    }
                }
                if(body.has("qid")){
                    this.qid = body.getLong("qid");
                }else {
                    this.qid = 0;
                }

                return true;
            } catch (UnsupportedEncodingException e) {
                LogUtils.i("UnsupportedEncodingException= "+e.getMessage());
                e.printStackTrace();
                return false;
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        } else if (cmd == 0x04) {
            //服务端单聊消息回复(消息发送后，服务端返回的数据中所带的cmd)
            this.cmd = Constant.PUB_ACK;
            byte[] msgIdByte = new byte[32];
            System.arraycopy(data,1,msgIdByte,0,32);
            try {
                String msg_id = new String(msgIdByte,"utf-8");
                this.msgId = msg_id;
                return true;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return false;
            }
        }else if (cmd == 0x06) {
            //服务端群消息的回复
            this.cmd = Constant.GROUP_PUB_ACK;
            byte[] msgIdByte = new byte[32];
            System.arraycopy(data,1,msgIdByte,0,32);
            try {
                String msg_id = new String(msgIdByte,"utf-8");
                this.msgId = msg_id;
                return true;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return false;
            }
        }else if (cmd == 0x07) {
            //PING      服务器会自动发送心跳包
            this.cmd = Constant.PING;
            return true;
        }else if(cmd == 0x05){
            // 服务端推送的群消息
            int srcPos = 0;
            this.cmd = Constant.GROUP_PUBLISH;
            srcPos = 1;
            byte[] groupIdBytes = new byte[4];
            System.arraycopy(data,srcPos,groupIdBytes,0,groupIdBytes.length);
            int groupId = BytePacket.readInt32(groupIdBytes, 0);
            srcPos +=groupIdBytes.length;
            byte[] msgIdBytes = new byte[32];
            System.arraycopy(data,srcPos,msgIdBytes,0,msgIdBytes.length);
            srcPos+=msgIdBytes.length;
            byte[] payloadLenBytes = new byte[2];
            System.arraycopy(data,srcPos,payloadLenBytes,0,payloadLenBytes.length);
            srcPos+=payloadLenBytes.length;
            short payloadLen = BytePacket.readInt16(payloadLenBytes,0);
            byte[] payloadBytes = new byte[payloadLen];
            System.arraycopy(data,srcPos,payloadBytes,0,payloadBytes.length);
            try {
                String msgId = new String(msgIdBytes,"utf-8");
                String content = new String(payloadBytes,"utf-8");
                this.msgId = msgId;
                this.groupId = groupId;
                JSONObject body = new JSONObject(content);
                this.content = content;
                LogUtils.i("msg_id= "+msgId+"   content= "+content);
                this.sender = Long.parseLong(body.getString("from"));
                this.isOutgoing = Message.MSG_IN;
                this.time = System.currentTimeMillis();
                this.sendState = MessageSendState.MESSAGE_SEND_SUCCESS;
                //后期如果要接收图片，语音等吗，还要设置消息类型,将消息类型存储到发送的数据的json体中
                String innerContent = body.getString("content");
                JSONObject innerContentJSONObject = new JSONObject(innerContent);
                String type = innerContentJSONObject.getString("type");
                String action = "";
                if(body.has("action")){
                    action = body.getString("action");
                }

                LogUtils.e("action=  "+action+"   "+this.msgId);
                if(TEXT_STR.equals(type)){
                    this.msgType = MSGTYPE_TEXT;
                }else if(IMAGE_STR.equals(type)){
                    this.msgType = MSGTYPE_IMG;
                }else if(REWARD_STR_EN.equals(type)){
                    this.msgType = MSGTYPE_SYSTEM_REWARD;
                }else if(COMMENT_STR_EN.equals(type)){
                    this.msgType = MSGTYPE_COMMENT;
                }
                if(body.has("role")){
                    if("0".equals(body.getString("role"))){
                        this.msgType = MSGTYPE_SYSTEM;
                    }
                }
                if(body.has("qid")){
                    this.qid = body.getLong("qid");
                }else {
                    this.qid = 0;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return false;
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }else if(cmd == 0x0b){
            int srcPos = 0;
            this.cmd = Constant.GROUP_PART_PUBLISH;
            srcPos = 1;
            byte[] groupIdBytes = new byte[4];
            System.arraycopy(data,srcPos,groupIdBytes,0,groupIdBytes.length);
            int groupId = BytePacket.readInt32(groupIdBytes, 0);
            srcPos +=groupIdBytes.length;
            byte[] msgIdBytes = new byte[32];
            System.arraycopy(data,srcPos,msgIdBytes,0,msgIdBytes.length);
            srcPos+=msgIdBytes.length;
            byte[] toUserNumBytes = new byte[2];
            System.arraycopy(data,srcPos,toUserNumBytes,0,toUserNumBytes.length);
            srcPos+=toUserNumBytes.length;
            short toUserNum = BytePacket.readInt16(toUserNumBytes,0);
            HashSet<Integer> toUids = new HashSet<Integer>();
            for(int i=1;i<=toUserNum;i++){
                byte[] tempBytes = new byte[4];
                System.arraycopy(data,srcPos,tempBytes,0,tempBytes.length);
                toUids.add(BytePacket.readInt32(tempBytes,0));
                srcPos+=4;
            }
            this.toUids = toUids;//集合中存放了所有被指定了接收群内消息的用户
            byte[] payloadLenBytes = new byte[2];
            System.arraycopy(data,srcPos,payloadLenBytes,0,payloadLenBytes.length);
            srcPos+=payloadLenBytes.length;
            short payloadLen = BytePacket.readInt16(payloadLenBytes,0);
            byte[] payloadBytes = new byte[payloadLen];
            System.arraycopy(data,srcPos,payloadBytes,0,payloadBytes.length);
            try {
                String msgId = new String(msgIdBytes,"utf-8");
                String content = new String(payloadBytes,"utf-8");
                this.msgId = msgId;
                this.groupId = groupId;
                JSONObject body = new JSONObject(content);
                this.content = content;
                LogUtils.i("msg_id= "+msgId+"   content= "+content);
                this.sender = Long.parseLong(body.getString("from"));
                this.isOutgoing = Message.MSG_IN;
                this.time = System.currentTimeMillis();
                this.sendState = MessageSendState.MESSAGE_SEND_SUCCESS;
                //后期如果要接收图片，语音等吗，还要设置消息类型,将消息类型存储到发送的数据的json体中
                String innerContent = body.getString("content");
                JSONObject innerContentJSONObject = new JSONObject(innerContent);
                String type = innerContentJSONObject.getString("type");
                String action = "";
                if(body.has("action")){
                    action = body.getString("action");
                }

                LogUtils.e("action=  "+action+"   "+this.msgId);
                if(TEXT_STR.equals(type)){
                    this.msgType = MSGTYPE_TEXT;
                }else if(IMAGE_STR.equals(type)){
                    this.msgType = MSGTYPE_IMG;
                }else if(REWARD_STR_EN.equals(type)){
                    this.msgType = MSGTYPE_SYSTEM_REWARD;
                }else if(COMMENT_STR_EN.equals(type)){
                    this.msgType = MSGTYPE_COMMENT;
                }
                if(body.has("role")){
                    if("0".equals(body.getString("role"))){
                        this.msgType = MSGTYPE_SYSTEM;
                    }
                }
                if(body.has("qid")){
                    this.qid = body.getLong("qid");
                }else {
                    this.qid = 0;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return false;
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }else if(cmd == 0x0c){
            // 客户端给群里部分人发送消息后，服务端返回的数据
            this.cmd = Constant.GROUP_PART_PUB_ACK;
            byte[] msgIdByte = new byte[32];
            System.arraycopy(data,1,msgIdByte,0,32);
            try {
                String msg_id = new String(msgIdByte,"utf-8");
                this.msgId = msg_id;
                return true;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return false;
            }
        }else if(cmd == 0x09){
            //服务端发送消息，通知客户端断开连接
            this.cmd = Constant.DISCONNECT;
        }else if(cmd == 0x0a){
            this.cmd = Constant.INVALID_PACKET;
        }else if(cmd == 0x14){
            this.cmd = Constant.LOGOUT;
        }
        return true;
    }

    public void setCmd(int cmd){
        this.cmd = cmd;
    }

    public int getCmd(){
        return this.cmd;
    }

    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(
            this);

    public PropertyChangeSupport getPropertyChangeSupport(){
        return changeSupport;
    }




    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }



    public Long getTime() {
        return this.time;
    }

    public void setTime(Long time) {
        this.time = time;
        changeSupport.firePropertyChange("time",null,this.time);
    }


    public int getSendState() {
        return this.sendState;
    }

    public void setSendState(int sendState) {
        int oldSendState = this.sendState;
        this.sendState = sendState;
        changeSupport.firePropertyChange("sendState", oldSendState, this.sendState);
    }

    public String getMsgId() {
        return this.msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public Long getSender() {
        return this.sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public Long getReceiver() {
        return this.receiver;
    }

    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMsgType() {
        return this.msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getIsOutgoing() {
        return this.isOutgoing;
    }

    public void setIsOutgoing(int isOutgoing) {
        this.isOutgoing = isOutgoing;
    }

    public static Message createTxtSendMessage(String content , long sender, long receiver, String userName, String headUrl){
        try {
            Message message = new Message();
            message.setContent(new String(content.getBytes(),"utf-8"));
            message.setSender(sender);
            message.setReceiver(receiver);
            message.setMsgType(Message.MSGTYPE_TEXT);
            message.setTime(System.currentTimeMillis());
            message.setIsOutgoing(Message.MSG_OUT);
            message.setSendState(MessageSendState.MESSAGE_SEND_LISTENED);
            message.setUserName(userName);
            message.setHeadUrl(headUrl);
            message.setCmd(3);
            JSONObject jsonObject = new JSONObject(content);
            long qid = jsonObject.getLong("qid");
            message.setQid(qid);
            message.setMsgId(new String(UUID.randomUUID().toString().replace("-", "").getBytes(),"utf-8"));
            return message;
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Message createImageSendMessage(String content , long sender, long receiver, String userName, String headUrl){
        try {
            Message message = new Message();
            message.setContent(content);
            message.setSender(sender);
            message.setReceiver(receiver);
            message.setMsgType(Message.MSGTYPE_IMG);
            message.setTime(System.currentTimeMillis());
            message.setIsOutgoing(Message.MSG_OUT);
            message.setSendState(MessageSendState.MESSAGE_SEND_LISTENED);
            message.setUserName(userName);
            message.setHeadUrl(headUrl);
            message.setCmd(3);
            JSONObject jsonObject = new JSONObject(content);
            long qid = jsonObject.getLong("qid");
            message.setQid(qid);
            message.setMsgId(new String(UUID.randomUUID().toString().replace("-", "").getBytes(),"utf-8"));
            return message;
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Message createTxtSendSuccessMessage(String content , long sender, long receiver, String userName, String headUrl){
        try {
            Message message = new Message();
            message.setContent(new String(content.getBytes(),"utf-8"));
            message.setSender(sender);
            message.setReceiver(receiver);
            message.setMsgType(Message.MSGTYPE_TEXT);
            message.setTime(System.currentTimeMillis());
            message.setIsOutgoing(Message.MSG_OUT);
            message.setSendState(MessageSendState.MESSAGE_SEND_SUCCESS);
            message.setUserName(userName);
            message.setHeadUrl(headUrl);
            message.setCmd(3);
            JSONObject jsonObject = new JSONObject(content);
            long qid = jsonObject.getLong("qid");
            message.setQid(qid);
            message.setMsgId(new String(UUID.randomUUID().toString().replace("-", "").getBytes(),"utf-8"));
            return message;
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Message createTextReceiveMessage(String content , long sender, long receiver, String userName, String headUrl, String msgId, long time){
        try {
            Message message = new Message();
            message.setContent(content);
            message.setSender(receiver);
            //由于是构建接收到的消息，为了传值的方便，将sender的值一直是当前医生的id,传进来，
            // 但是，由于医生此时是消息接收者，所以，设置消息的message.setReceiver()时，传的值是sender
            message.setReceiver(sender);
            message.setMsgType(Message.MSGTYPE_TEXT);
            message.setTime(time);
            message.setIsOutgoing(Message.MSG_IN);
            message.setSendState(MessageSendState.MESSAGE_SEND_SUCCESS);
            message.setUserName(userName);
            message.setHeadUrl(headUrl);
            message.setCmd(3);
            JSONObject jsonObject = new JSONObject(content);
            long qid = jsonObject.getLong("qid");
            message.setQid(qid);
            message.setMsgId(msgId);
            return message;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Message createImageReceiveMessage(String content , long sender, long receiver, String userName, String headUrl, String msgId, long time){
        try {
            Message message = new Message();
            message.setContent(content);
            message.setSender(receiver);
            message.setReceiver(sender);
            message.setMsgType(Message.MSGTYPE_IMG);
            message.setTime(time);
            message.setIsOutgoing(Message.MSG_IN);
            message.setSendState(MessageSendState.MESSAGE_SEND_SUCCESS);
            message.setUserName(userName);
            message.setHeadUrl(headUrl);
            message.setCmd(3);
            JSONObject jsonObject = new JSONObject(content);
            long qid = jsonObject.getLong("qid");
            message.setQid(qid);
            message.setMsgId(msgId);
            return message;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Message createImageReceiveSubMessage(String content , long sender, long receiver, String msgId, long time){
        try {
            Message message = new Message();
            message.setContent(content);
            message.setSender(receiver);
            message.setReceiver(sender);
            message.setMsgType(Message.MSGTYPE_IMG);
            message.setTime(time);
            message.setIsOutgoing(Message.MSG_IN);
            message.setSendState(MessageSendState.MESSAGE_SEND_SUCCESS);
            message.setCmd(3);
            JSONObject jsonObject = new JSONObject(content);
            long qid = jsonObject.getLong("qid");
            message.setQid(qid);
            message.setMsgId(msgId);
            return message;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Message createQuestionMessage(String content , long sender, long receiver, String userName, String headUrl){
        try {
            Message message = new Message();
            message.setContent(new String(content.getBytes(),"utf-8"));
            message.setSender(receiver);
            message.setReceiver(sender);
            message.setMsgType(Message.MSGTYPE_TEXT);
            message.setTime(System.currentTimeMillis());
            message.setIsOutgoing(Message.MSG_IN);
            message.setSendState(MessageSendState.MESSAGE_SEND_SUCCESS);
            message.setUserName(userName);
            message.setHeadUrl(headUrl);
            message.setCmd(3);
            JSONObject jsonObject = new JSONObject(content);
            long qid = jsonObject.getLong("qid");
            message.setQid(qid);
            message.setMsgId("");
            return message;
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Message createSystemMessage(String content , long sender, long receiver, String userName, String headUrl){
        try {
            Message message = new Message();
            message.setContent(new String(content.getBytes(),"utf-8"));
            message.setSender(sender);
            message.setReceiver(receiver);
            message.setMsgType(Message.MSGTYPE_SYSTEM);
            message.setTime(System.currentTimeMillis());
            message.setIsOutgoing(Message.MSG_OUT);
            message.setSendState(MessageSendState.MESSAGE_SEND_LISTENED);
            message.setUserName(userName);
            message.setHeadUrl(headUrl);
            message.setCmd(3);
            JSONObject jsonObject = new JSONObject(content);
            long qid = jsonObject.getLong("qid");
            message.setQid(qid);
            message.setMsgId(new String(UUID.randomUUID().toString().replace("-", "").getBytes(),"utf-8"));
            return message;
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
