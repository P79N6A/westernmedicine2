package com.xywy.askforexpert.module.message.msgchat;

import com.hyphenate.EMGroupChangeListener;

/**
 * group change listener
 */
public abstract class EaseGroupRemoveListener implements EMGroupChangeListener {

    @Override
    public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onApplicationReceived(String groupId, String groupName, String applyer, String reason) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onApplicationAccept(String groupId, String groupName, String accepter) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onApplicationDeclined(String groupId, String groupName, String decliner, String reason) {
        // TODO Auto-generated method stub

    }

    //stone 替换 环信版本升级3.1.4->3.1.5
    @Override
    public void onInvitationAccepted(String s, String s1, String s2) {

    }
//stone 旧的 环信版本升级3.1.4->3.1.5
    //    @Override
//    public void onInvitationAccpted(String groupId, String inviter, String reason) {
//        // TODO Auto-generated method stub
//
//    }

    @Override
    public void onInvitationDeclined(String groupId, String invitee, String reason) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {
        // TODO Auto-generated method stub
    }
}
