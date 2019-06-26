package com.xywy.livevideo.chat.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.JsonParser;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.xywy.livevideo.LiveManager;
import com.xywy.livevideo.entity.GiftListRespEntity;
import com.xywy.util.L;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/3/6 16:07
 */

public class ChatMessageHelper {
    final static  String system= "直播通知";
    public static final String LIVE_CHAT_MES_TYPE = "LiveChatMesType";
    public static final String LIVE_CHAT_ALIAS_NAME = "LiveCHatAliasName";
    public static final String LIVE_CHAT_MES_GIFT_ID = "LiveChatMesGiftId";
    public static final String LIVE_CHAT_MES_GIFT_COUNT = "LiveCHatMesGiftCount";
    public static final String LIVE_CHAT_ROOM_ID = "chatroomsid";
    private ChatMessageHelper() {
    }

    @NonNull
    private static EMMessage createMsg(String content, String from, String to) {
        EMMessage msg = EMMessage.createSendMessage(EMMessage.Type.TXT);
        //支持单聊和群聊，默认单聊，如果是群聊添加下面这行
        msg.setChatType(EMMessage.ChatType.ChatRoom);
        msg.setFrom(from);
        msg.setTo(to);
        msg.addBody(new EMTextMessageBody(content));
        return msg;
    }

    public static List<EMMessage>  getMsgs(EMConversation conversation) {
        conversation.markAllMessagesAsRead();
        Long startTime = System.currentTimeMillis();
        if (!TextUtils.isEmpty(conversation.getExtField())){
            startTime=Long.valueOf(conversation.getExtField());
        }
        List<EMMessage> msgs=conversation.getAllMessages();
        Iterator<EMMessage> it=msgs.iterator();
        while (it.hasNext()){
            EMMessage msg= it.next();
            if (msg.getMsgTime()<startTime){
                it.remove();
            }
        }
        return msgs;
    }
    /**
     * 发送本地消息
     * @param content
     * @param from
     * @param conversation
     */
    public static void sendLocalMsg(String content, String from, EMConversation conversation) {
        EMMessage msg = createMsg(content, from,LiveChatClient.getInstance().getHxUserName());
        msg.setLocalTime(System.currentTimeMillis());
        msg.setAttribute(LIVE_CHAT_MES_TYPE,LiveMsgType.Txt);
        msg.setAttribute(LIVE_CHAT_ALIAS_NAME, from);
        conversation.insertMessage(msg);
    }

    /**
     * 发送聊天消息
     *
     * @param content
     * @param toUserId
     */
    protected static void sendMessage(String content, String toUserId) {
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = createMsg(content,LiveChatClient.getInstance().getHxUserName(), toUserId);
        message.setAttribute(LIVE_CHAT_MES_TYPE,LiveMsgType.Txt);
        message.setAttribute(LIVE_CHAT_ALIAS_NAME, LiveManager.getInstance().getConfig().nickName);
        LiveChatClient.getInstance().sendMessage(message);
    }

    public static void sendGiftMsg(String presentId, int count, String toUserId) {
        EMMessage message = createMsg("",LiveChatClient.getInstance().getHxUserName(), toUserId);
        message.setAttribute(LIVE_CHAT_MES_TYPE,LiveMsgType.Gift);
        message.setAttribute(LIVE_CHAT_ALIAS_NAME, LiveManager.getInstance().getConfig().nickName);
        message.setAttribute(LIVE_CHAT_MES_GIFT_ID,presentId+"");
        message.setAttribute(LIVE_CHAT_MES_GIFT_COUNT,count+"");
        LiveChatClient.getInstance().sendMessage(message);
    }
    public static void sendLeaveMsg(String toUserId) {
        EMMessage message = createMsg("",LiveChatClient.getInstance().getHxUserName(), toUserId);
        message.setAttribute(LIVE_CHAT_MES_TYPE,LiveMsgType.LeaveRoom);
        message.setAttribute(LIVE_CHAT_ALIAS_NAME,LiveManager.getInstance().getConfig().nickName);
        LiveChatClient.getInstance().sendMessage(message);
    }
    public static void sendJoinRoomMsg(String toUserId) {
        EMMessage message = createMsg("",LiveChatClient.getInstance().getHxUserName(), toUserId);
        message.setAttribute(LIVE_CHAT_MES_TYPE,LiveMsgType.JoinRoom);
        message.setAttribute(LIVE_CHAT_ALIAS_NAME,LiveManager.getInstance().getConfig().nickName);
        LiveChatClient.getInstance().sendMessage(message);
    }

    public static List<LiveChatContent> resolveMsgs(List<EMMessage> msgs) {
        List<LiveChatContent> liveMsgs=new ArrayList<>();
        for (EMMessage msg:msgs){
            LiveChatContent object = resolveMsg(msg);
            if (object!=null){
                liveMsgs.add(object);
            }else {
                L.e(msg.toString());
            }
        }
        return liveMsgs;
    }

    private static LiveChatContent resolveMsg(EMMessage msg) {
        if (msg.getType()== EMMessage.Type.TXT){
            try {
                int type=msg.getIntAttribute(LIVE_CHAT_MES_TYPE);
                switch (type){
                    case 0:
                        return resolveTxtMsg(msg);
                    case 1:
                        return  resolveGiftMsg(msg);
                    case 2:
                        return resolveLeaveMsg(msg);
                    case 3:
                        return resolveJoinRoomMsg(msg);
                }
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        }
        return new LiveChatContent(msg.getFrom(),msg.getBody().toString());
    }

    public static Set<String> resolveLiveChatCmdMsg(List<EMMessage> msgs) {
        Set<String> chatRoomIds=new HashSet<>();
        for (EMMessage msg:msgs){
            try {
                EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) msg.getBody();
                String action = cmdMsgBody.action();// 获取自定义action
                if ("进入了聊天室".equals(action)){
                    String jsonChatRoomId = msg.getStringAttribute(LIVE_CHAT_ROOM_ID);
                    String chatRoomId=new JsonParser().parse(jsonChatRoomId).getAsJsonObject().get("id").getAsString();
                    chatRoomIds.add(chatRoomId);
                    L.e("收到聊天室id透传");
                }
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        }
        return chatRoomIds;
    }
    private static LiveChatContent resolveTxtMsg(EMMessage msg) {
        try {
            String name=msg.getStringAttribute(LIVE_CHAT_ALIAS_NAME);
            EMTextMessageBody txtBody = (EMTextMessageBody) msg.getBody();
            return new LiveChatContent(name,txtBody.getMessage());
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static LiveChatContent resolveGiftMsg(final EMMessage msg) {
        try {
            String name = msg.getStringAttribute(LIVE_CHAT_ALIAS_NAME);
            final String giftId=msg.getStringAttribute(LIVE_CHAT_MES_GIFT_ID,"");
            String count=msg.getStringAttribute(LIVE_CHAT_MES_GIFT_COUNT);
            for(final GiftListRespEntity.DataBean gift:LiveManager.getInstance().getConfig().giftList.getData()){
                if (giftId.equals(gift.getId())){
                    String content = "送"+count+"个"+gift.getName();
//                    String icon = "icon";
//                    Bitmap b = ImageLoaderUtils.getInstance().loadImageSync(gift.getImg());
//                    ImageSpan imgSpan = new ImageSpan(b);
//                    SpannableString spanString = new SpannableString(content+icon);
//                    spanString.setSpan(imgSpan, content.length(), (content+icon).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    return new LiveChatContent(name,content,gift.getImg());
                }
            }
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        return null;

    }
    private static LiveChatContent resolveLeaveMsg(EMMessage msg) {
        try {
            String name=msg.getStringAttribute(LIVE_CHAT_ALIAS_NAME);
            return new LiveChatContent(ChatMessageHelper.system,name+"离开了房间");
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static LiveChatContent resolveJoinRoomMsg(EMMessage msg) {
        try {
            String name=msg.getStringAttribute(LIVE_CHAT_ALIAS_NAME);
            return new LiveChatContent(ChatMessageHelper.system,name+"进入了房间");
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        return null;
    }
}
