package com.xywy.easeWrapper;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMCallStateChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.EMNoActiveCallException;
import com.hyphenate.exceptions.HyphenateException;
import com.xywy.easeWrapper.controller.EaseUI;

import java.util.Map;

/**
 * Created by shijiazi on 16/8/16.
 */
public class EMChatManager {
    //转发chatmanager和callmanager的方法
    //部分contactmanager

    private static EMChatManager instance = null;

    private EMChatManager() {
    }

    public static EMChatManager getInstance() {
        if(instance == null) {
            instance = new EMChatManager();
        }

        return instance;
    }

    public void login(String username, String password, EMCallBack callback) {
        EMClient.getInstance().login(username, password, callback);
    }

    public void addConnectionListener(EMConnectionListener myConnectionListener) {
        EMClient.getInstance().addConnectionListener(myConnectionListener);
    }

    public void removeConnectionListener(EMConnectionListener connectionListener) {
        EMClient.getInstance().removeConnectionListener(connectionListener);
    }

    public int getUnreadMsgsCount() {
        return EMClient.getInstance().chatManager().getUnreadMsgsCount();
    }

    public EMConversation getConversation(String from) {
        return getConversation(from, EMConversation.EMConversationType.Chat);
    }

    public EMConversation getConversation(String from, EMConversation.EMConversationType type) {
        return getConversation(from, type, true);
    }

    public EMConversation getConversation(String from, EMConversation.EMConversationType type, boolean unknowned) {
        return EMClient.getInstance().chatManager().getConversation(from, type, unknowned);
    }

    public void deleteConversation(String userName, boolean deleteMessage, boolean unknowned) {
        EMClient.getInstance().chatManager().deleteConversation(userName, deleteMessage);
    }

    public EMMessage getMessage(String msgId) {
        return EMClient.getInstance().chatManager().getMessage(msgId);
    }

    public void removeCallStateChangeListener(EMCallStateChangeListener callStateListener) {
        EMClient.getInstance().callManager().removeCallStateChangeListener(callStateListener);
    }

    public void rejectCall() throws EMNoActiveCallException {
        EMClient.getInstance().callManager().rejectCall();
    }


    public void activityResumed() {
        EaseUI.getInstance().getNotifier().reset();
    }

    public void saveMessage(EMMessage message, boolean b) {
        EMClient.getInstance().chatManager().saveMessage(message);
    }

    public void clearConversation(String toChatUsername) {
        EMClient.getInstance().chatManager().deleteConversation(toChatUsername, true);
    }

    public void loadAllConversations() {
        EMClient.getInstance().chatManager().loadAllConversations();
    }

    public void ackMessageRead(String from, String msgId) throws HyphenateException {
        EMClient.getInstance().chatManager().ackMessageRead(from, msgId);
    }

    public void addMessageListener(EMMessageListener messageListener) {
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    public Map<String, EMConversation> getAllConversations() {
        return EMClient.getInstance().chatManager().getAllConversations();
    }

    public void setMessageListened(EMMessage messageListened) {
        EMClient.getInstance().chatManager().setMessageListened(messageListened);
    }

    public void removeMessageListener(EMMessageListener messageListener) {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
    }

    public void asyncFetchMessage(EMMessage message) {
        EMClient.getInstance().chatManager().downloadAttachment(message);
    }

    public void sendMessage(EMMessage message, EMCallBack emCallBack) {
        EMClient.getInstance().chatManager().sendMessage(message);
        message.setMessageStatusCallback(emCallBack);
        //// TODO: 16/8/17 shijiazi
    }

    public void acceptInvitation(String hx_usernam) throws HyphenateException {
        EMClient.getInstance().contactManager().acceptInvitation(hx_usernam);
    }

    public void refuseInvitation(String hx_usernam) throws HyphenateException {
        EMClient.getInstance().contactManager().declineInvitation(hx_usernam);
    }

    public boolean isConnected() {
        //// TODO: 16/8/17 shijiazi
        return true;
    }
}
