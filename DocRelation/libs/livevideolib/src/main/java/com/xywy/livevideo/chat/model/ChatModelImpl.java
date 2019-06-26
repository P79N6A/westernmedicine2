package com.xywy.livevideo.chat.model;

import android.app.Activity;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMChatRoomChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.xywy.livevideo.LiveManager;
import com.xywy.livevideo.chat.DoctorCircleCommentPopupWindow;
import com.xywy.livevideo.chat.interfaces.IChatModel;
import com.xywy.livevideo.chat.interfaces.IChatView;
import com.xywy.livevideo.chat.interfaces.SendMsgWindowListener;
import com.xywy.util.L;
import com.xywy.util.T;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/2/27 15:53
 */
public class ChatModelImpl implements IChatModel {
    private String TAG = "ChatModelImpl";
    IChatView view;
    Set<String>  roomIds;
//    private Set<EMConversation> conversations=new HashSet<>();
    private EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息
            refreshMsgs();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
            //收到已读回执
//            refreshMsgs();
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
            //收到已送达回执
//            refreshMsgs();
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
//            refreshMsgs();
        }
    };
    private EMCallBack msgSentCallBack = new EMCallBack() {
        @Override
        public void onSuccess() {
            refreshMsgs();
        }

        @Override
        public void onError(int i, String s) {

        }

        @Override
        public void onProgress(int i, String s) {

        }
    };

    public ChatModelImpl(IChatView view, Set<String> roomIds) {
        this.view = view;
        this.roomIds = roomIds;
    }

    @Override
    public void sendGiftMsg(String presentId, int count) {
        for (String id:roomIds){
            ChatMessageHelper.sendGiftMsg(presentId, count, id);
        }
    }


    private void joinChatRoom(final String roomId) {
        addChatRoomChangeListenr(roomId);
        LiveChatClient.getInstance().registerMessageStatusCallback(msgSentCallBack);
        LiveChatClient.getInstance().joinChatRoom(roomId, new EMValueCallBack<EMChatRoom>() {
            @Override
            public void onSuccess(EMChatRoom emChatRoom) {
                L.e(TAG, "进入聊天室成功:" + roomId);
                joined=true;
                EMConversation emConversation = LiveChatClient.getInstance().getConversation(roomId);
                emConversation.setExtField(System.currentTimeMillis()+"");
                emConversation.clearAllMessages();
                registerMsgChangedListener();
                ChatMessageHelper.sendLocalMsg("我们提倡绿色直播", ChatMessageHelper.system,emConversation );
                ChatMessageHelper.sendJoinRoomMsg(roomId);
                refreshMsgs();
            }

            @Override
            public void onError(int i, String s) {
                L.e(TAG, "进入聊天室失败:" + s);
                T.showShort(view.getActivity(), s);
                view.getActivity().finish();
            }
        });
    }

    private void addChatRoomChangeListenr(final String id) {
        EMChatRoomChangeListener chatRoomChangeListener = new EMChatRoomChangeListener() {

            @Override
            public void onChatRoomDestroyed(String roomId, String roomName) {
                if (roomId.equals(id)) {
                    Log.e(TAG, " room : " + roomId + " with room name : " + roomName + " was destroyed");
                    view.getActivity().finish();
                }
                joined=false;
                LiveManager.getInstance().getConfig().chatRoomIds.remove(roomId);
            }

            @Override
            public void onMemberJoined(String roomId, String participant) {
                if (roomId.equals(id)) {
                    Log.e(TAG, "member : " + participant + " join the room : " + roomId);
//                    helper.sendLocalMsg(participant + "进入了房间 : ", ChatMessageModel.system);
                }
            }

            @Override
            public void onMemberExited(String roomId, String roomName, String participant) {
                if (roomId.equals(id)) {
                    Log.e(TAG, "member : " + participant + " leave the room : " + roomId);
//                    helper.sendLocalMsg(participant + "离开了房间 : ", ChatMessageModel.system);
                }
            }

            @Override
            public void onMemberKicked(String roomId, String roomName, String participant) {

            }
        };
        LiveChatClient.getInstance().addChatRoomChangeListenr(chatRoomChangeListener);
    }
    private DoctorCircleCommentPopupWindow doctorCircleCommentPopupWindow;
    @Override
    public void showMsgInputBox(Activity activity) {
        if (doctorCircleCommentPopupWindow==null){
            doctorCircleCommentPopupWindow = new DoctorCircleCommentPopupWindow(activity, null, true, new SendMsgWindowListener() {
                @Override
                public void onSend(String content) {
                    for (String id : roomIds) {
                        ChatMessageHelper.sendMessage(content, id);
                    }
                }
            });
            doctorCircleCommentPopupWindow.setHint("和大家说点什么");
            doctorCircleCommentPopupWindow.setMaxWord(140);
        }
        doctorCircleCommentPopupWindow.show();
    }

    @Override
    public void sendMsg(String content) {
        for (String id:roomIds){
            ChatMessageHelper.sendMessage(content, id);
        }
    }
    private volatile boolean joined=false;
    private boolean isJoined() {
        return joined;
    }

    @Override
    public void refreshMsgs() {
        if (isJoined()) {
            List<EMMessage> msgs=new ArrayList<>();
            for (String roomId :roomIds){
                EMConversation conv= LiveChatClient.getInstance().getConversation(roomId);
                msgs.addAll(ChatMessageHelper.getMsgs(conv));
            }
            view.refreshChatView(ChatMessageHelper.resolveMsgs(msgs));
        } else {
            for (String id :roomIds) {
                joinChatRoom(id);
            }
        }
    }

    private void registerMsgChangedListener() {
        LiveChatClient.getInstance().registerMessageListener(msgListener);
    }

    private void removeMsgChangedListener() {
        LiveChatClient.getInstance().removeMessageListener(msgListener);
    }

    @Override
    public void onFinish() {
        removeMsgChangedListener();
        for (String id :roomIds) {
            ChatMessageHelper.sendLeaveMsg(id);
            LiveChatClient.getInstance().leaveChatRoom(id);
        }
        LiveManager.getInstance().getConfig().chatRoomIds.removeAll(roomIds);
    }
}
