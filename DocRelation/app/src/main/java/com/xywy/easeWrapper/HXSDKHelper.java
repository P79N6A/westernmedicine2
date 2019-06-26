package com.xywy.easeWrapper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;
import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.module.message.msgchat.ChatMainActivity;
import com.xywy.askforexpert.module.message.msgchat.VideoCallActivity;
import com.xywy.easeWrapper.controller.EaseUI;
import com.xywy.easeWrapper.db.HXDBManager;
import com.xywy.easeWrapper.db.InviteMessgeDao;
import com.xywy.easeWrapper.db.UserDao;
import com.xywy.easeWrapper.domain.EaseEmojicon;
import com.xywy.easeWrapper.domain.EaseEmojiconGroupEntity;
import com.xywy.easeWrapper.domain.EaseUser;
import com.xywy.easeWrapper.domain.EmojiconExampleGroupData;
import com.xywy.easeWrapper.domain.RobotUser;
import com.xywy.easeWrapper.utils.CallReceiver;
import com.xywy.easeWrapper.utils.EaseAtMessageHelper;
import com.xywy.easeWrapper.utils.EaseCommonUtils;
import com.xywy.easeWrapper.utils.EaseNotifier;
import com.xywy.easeWrapper.utils.PreferenceManager;
import com.xywy.easeWrapper.utils.UserProfileManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by shijiazi on 16/8/16.
 */
public class HXSDKHelper {

    public static final String FU_CADR = "em_apns_ext";
    private String password;

    /**
     * data sync listener
     */
    static public interface DataSyncListener {
        /**
         * sync complete
         *
         * @param success true：data sync successful，false: failed to sync data
         */
        public void onSyncComplete(boolean success);
    }

    protected static final String TAG = "HXSDKHelper";

    private EaseUI easeUI;

    /**
     * EMEventListener
     */
    protected EMMessageListener messageListener = null;

    private Map<String, EaseUser> contactList;

    private Map<String, RobotUser> robotList;

    private UserProfileManager userProManager;

    private static HXSDKHelper instance = null;

    private HXSDKModel hxModel = null;

    /**
     * sync groups status listener
     */
    private List<DataSyncListener> syncGroupsListeners;
    /**
     * sync contacts status listener
     */
    private List<DataSyncListener> syncContactsListeners;
    /**
     * sync blacklist status listener
     */
    private List<DataSyncListener> syncBlackListListeners;

    private boolean isSyncingGroupsWithServer = false;
    private boolean isSyncingContactsWithServer = false;
    private boolean isSyncingBlackListWithServer = false;
    private boolean isGroupsSyncedWithServer = false;
    private boolean isContactsSyncedWithServer = false;
    private boolean isBlackListSyncedWithServer = false;

    public boolean isVoiceCalling;
    public boolean isVideoCalling;

    private String username;

    private Context appContext;

    private CallReceiver callReceiver;

    private EMConnectionListener connectionListener;

    private InviteMessgeDao inviteMessgeDao;
    private UserDao userDao;

    private LocalBroadcastManager broadcastManager;

    private boolean isGroupAndContactListenerRegisted;

    private HXSDKHelper() {
    }

    public synchronized static HXSDKHelper getInstance() {
        if (instance == null) {
            instance = new HXSDKHelper();
        }
        return instance;
    }

    /**
     * init helper
     *
     * @param context application context
     */
    public void onInit(Context context) {
        hxModel = new HXSDKModel(context);
        EMOptions options = initChatOptions();
        //use default options if options is null
        if (EaseUI.getInstance().init(context, options)) {
            appContext = context;

            //debug mode, you'd better set it to false, if you want release your App officially.
            //TODO stone 测试与线上要修改 动态控制
            EMClient.getInstance().setDebugMode(BuildConfig.DEBUG);
            //get easeui instance
            easeUI = EaseUI.getInstance();
            //to set user's profile and avatar
            setEaseUIProviders();
            //initialize preference manager
            PreferenceManager.init(context);
            //initialize profile manager
            getUserProfileManager().init(context);

            EMClient.getInstance().callManager().getVideoCallHelper().setAdaptiveVideoFlag(getModel().isAdaptiveVideoEncode());

            setGlobalListeners();
            broadcastManager = LocalBroadcastManager.getInstance(appContext);
            initDbDao();
        }
    }


    private EMOptions initChatOptions() {
        Log.d(TAG, "init HuanXin Options");

        EMOptions options = new EMOptions();
        // set if accept the invitation automatically
        options.setAcceptInvitationAlways(false);
        // set if you need read ack
        options.setRequireAck(true);
        // set if you need delivery ack
        options.setRequireDeliveryAck(false);

        //stone 添加 环信版本升级3.1.4->3.1.5
        options.setAutoLogin(true);

        //stone 去掉 环信版本升级3.1.4->3.1.5
//        //you need apply & set your own id if you want to use google cloud messaging.
//        options.setGCMNumber("324169311137");
//        //you need apply & set your own id if you want to use Mi push notification
//        options.setMipushConfig("2882303761517426801", "5381742660801");
//        //you need apply & set your own id if you want to use Huawei push notification
//        options.setHuaweiPushAppId("10492024");

        options.allowChatroomOwnerLeave(getModel().isChatroomOwnerLeaveAllowed());
        options.setDeleteMessagesAsExitGroup(getModel().isDeleteMessagesAsExitGroup());
        options.setAutoAcceptGroupInvitation(getModel().isAutoAcceptGroupInvitation());

        return options;
    }

    public void setMsgSoundAllowed(boolean isSoundOn){
        hxModel.setSettingMsgSound(isSoundOn);
    }

    public void setMsgVibrateAllowed(boolean isVibrateOn){
        hxModel.setSettingMsgVibrate(isVibrateOn);
    }


    protected void setEaseUIProviders() {
        // set profile provider if you want easeUI to handle avatar and nickname
        easeUI.setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {

            @Override
            public EaseUser getUser(String username) {
                return getUserInfo(username);
            }
        });

        //set options
        easeUI.setSettingsProvider(new EaseUI.EaseSettingsProvider() {

            @Override
            public boolean isSpeakerOpened() {
                return hxModel.getSettingMsgSpeaker();
            }

            @Override
            public boolean isMsgVibrateAllowed(EMMessage message) {
                return hxModel.getSettingMsgVibrate();
            }

            @Override
            public boolean isMsgSoundAllowed(EMMessage message) {
                return hxModel.getSettingMsgSound();
            }

            @Override
            public boolean isMsgNotifyAllowed(EMMessage message) {
                if (message == null) {
                    return hxModel.getSettingMsgNotification();
                }
                if (!hxModel.getSettingMsgNotification()) {
                    return false;
                } else {
                    String chatUsename = null;
                    List<String> notNotifyIds = null;
                    // get user or group id which was blocked to show message notifications
                    if (message.getChatType() == EMMessage.ChatType.Chat) {
                        chatUsename = message.getFrom();
                        notNotifyIds = hxModel.getDisabledIds();
                    } else if (message.getChatType() == EMMessage.ChatType.ChatRoom) {
                       return false;
                    }
                    else {
                        chatUsename = message.getTo();
                        notNotifyIds = hxModel.getDisabledGroups();
                    }

                    if (notNotifyIds == null || !notNotifyIds.contains(chatUsename)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        });
        //set emoji icon provider
        easeUI.setEmojiconInfoProvider(new EaseUI.EaseEmojiconInfoProvider() {

            @Override
            public EaseEmojicon getEmojiconInfo(String emojiconIdentityCode) {
                EaseEmojiconGroupEntity data = EmojiconExampleGroupData.getData();
                for (EaseEmojicon emojicon : data.getEmojiconList()) {
                    if (emojicon.getIdentityCode().equals(emojiconIdentityCode)) {
                        return emojicon;
                    }
                }
                return null;
            }

            @Override
            public Map<String, Object> getTextEmojiconMapping() {
                return null;
            }
        });

        //set notification options, will use default if you don't set it
        easeUI.getNotifier().setNotificationInfoProvider(new EaseNotifier.EaseNotificationInfoProvider() {

            @Override
            public String getTitle(EMMessage message) {
                //you can update title here
                return null;
            }

            @Override
            public int getSmallIcon(EMMessage message) {
                //you can update icon here
                return 0;
            }

            @Override
            public String getDisplayedText(EMMessage message) {
                // be used on notification bar, different text according the message type.
                String ticker = EaseCommonUtils.getMessageDigest(message, appContext);
                if (message.getType() == EMMessage.Type.TXT) {
                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
                }
                EaseUser user = getUserInfo(message.getFrom());
                if (user != null) {
                    if (EaseAtMessageHelper.get().isAtMeMsg(message)) {
                        return String.format(appContext.getString(R.string.at_your_in_group), user.getNick());
                    }
                    return user.getNick() + ": " + ticker;
                } else {
                    if (EaseAtMessageHelper.get().isAtMeMsg(message)) {
                        return String.format(appContext.getString(R.string.at_your_in_group), message.getFrom());
                    }
                    return message.getFrom() + ": " + ticker;
                }
            }

            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                // here you can customize the text.
                // return fromUsersNum + "contacts send " + messageNum + "messages to you";
                return null;
            }

            @Override
            public Intent getLaunchIntent(EMMessage message) {
                // you can set what activity you want display when user click the notification
                Intent intent = new Intent(appContext, ChatMainActivity.class);
                // open calling activity if there is call
                if (isVideoCalling) {
                    intent = new Intent(appContext, VideoCallActivity.class);
                } else if (isVoiceCalling) {
                    //comment by shijiazi
//                    intent = new Intent(appContext, VoiceCallActivity.class);
                } else {
                    EMMessage.ChatType chatType = message.getChatType();
                    if (chatType == EMMessage.ChatType.Chat) { // single chat message
                        intent.putExtra("userId", message.getFrom());
                        intent.putExtra("chatType", EaseConstant.CHATTYPE_SINGLE);
                    } else { // group chat message
                        // message.getTo() is the group id
                        intent.putExtra("userId", message.getTo());
                        if (chatType == EMMessage.ChatType.GroupChat) {
                            intent.putExtra("chatType", EaseConstant.CHATTYPE_GROUP);
                        } else {
                            intent.putExtra("chatType", EaseConstant.CHATTYPE_CHATROOM);
                        }

                    }
                }
                return intent;
            }
        });
    }

    /**
     * set global listener
     */
    protected void setGlobalListeners() {
        syncGroupsListeners = new ArrayList<DataSyncListener>();
        syncContactsListeners = new ArrayList<DataSyncListener>();
        syncBlackListListeners = new ArrayList<DataSyncListener>();

        isGroupsSyncedWithServer = hxModel.isGroupsSynced();
        isContactsSyncedWithServer = hxModel.isContactSynced();
        isBlackListSyncedWithServer = hxModel.isBacklistSynced();

//        // create the global connection listener
//        connectionListener = new EMConnectionListener() {
//            @Override
//            public void onDisconnected(int error) {
//                if (error == EMError.USER_REMOVED) {
//                    onCurrentAccountRemoved();
//                }else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
//                    onConnectionConflict();
//                }
//            }
//
//            @Override
//            public void onConnected() {
//                // in case group and contact were already synced, we supposed to notify sdk we are ready to receive the events
//                if(isGroupsSyncedWithServer && isContactsSyncedWithServer){
//                    EMLog.d(TAG, "group and contact already synced with servre");
//                }else{
//                    if(!isGroupsSyncedWithServer){
//                        asyncFetchGroupsFromServer(null);
//                    }
//
//                    if(!isContactsSyncedWithServer){
//                        asyncFetchContactsFromServer(null);
//                    }
//
//                    if(!isBlackListSyncedWithServer){
//                        asyncFetchBlackListFromServer(null);
//                    }
//                }
//            }
//        };

        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
        if (callReceiver == null) {
            callReceiver = new CallReceiver();
        }

        //register incoming call receiver
        appContext.registerReceiver(callReceiver, callFilter);


        //comment by shijiazi
        //This is added in MainActivity
//        //register connection listener
//        EMClient.getInstance().addConnectionListener(connectionListener);
//
//        //register group and contact event listener
        registerGroupAndContactListener();
//        //register message event listener
//
//        registerMessageListener();

    }

    private void initDbDao() {
        inviteMessgeDao = new InviteMessgeDao();
        userDao = new UserDao(appContext);
    }

    //    /**
//     * register group and contact listener, you need register when login
//     */
    public void registerGroupAndContactListener() {
        if (!isGroupAndContactListenerRegisted) {
            EMClient.getInstance().groupManager().addGroupChangeListener(new MyGroupChangeListener());
//            EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
            isGroupAndContactListenerRegisted = true;
        }

    }

    class MyGroupChangeListener implements EMGroupChangeListener {

        @Override
        public void onInvitationReceived(String s, String s1, String s2, String s3) {
            Log.d("GROUP",s);
        }

        @Override
        public void onApplicationReceived(String s, String s1, String s2, String s3) {
            Log.d("GROUP",s);
        }

        @Override
        public void onApplicationAccept(String s, String s1, String s2) {
            Log.d("GROUP",s);
        }

        @Override
        public void onApplicationDeclined(String s, String s1, String s2, String s3) {
            Log.d("GROUP",s);
        }

        //stone 替换 环信版本升级3.1.4->3.1.5
        @Override
        public void onInvitationAccepted(String s, String s1, String s2) {
            Log.d("GROUP",s);
        }

        //stone 旧的 环信版本升级3.1.4->3.1.5
//        @Override
//        public void onInvitationAccpted(String s, String s1, String s2) {
//            Log.d("GROUP",s);
//        }

        @Override
        public void onInvitationDeclined(String s, String s1, String s2) {
            Log.d("GROUP",s);
        }

        @Override
        public void onUserRemoved(String groupId, String groupName) {
            //user is removed from group
            YMApplication.deleteGroupId = groupId;
            broadcastManager.sendBroadcast(new Intent(EaseConstant.ACTION_GROUP_CHANAGED));
            EMChatManager.getInstance().deleteConversation(groupId, true, true);

        }
//stone 替换 环信版本升级3.1.4->3.1.5
        @Override
        public void onGroupDestroyed(String s, String s1) {

        }
//stone 旧的 环信版本升级3.1.4->3.1.5
//        @Override
//        public void onGroupDestroy(String s, String s1) {
//            Log.d("GROUP",s);
//        }

        @Override
        public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {
//            Log.d("GROUP",s);
//            EMMessage msg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
//            msg.setChatType(EMMessage.ChatType.GroupChat);
//            msg.setFrom(inviter);
//            msg.setTo(groupId);
//            msg.setMsgId(UUID.randomUUID().toString());
//            msg.addBody(new EMTextMessageBody(inviter + " " +st3));
//            msg.setStatus(EMMessage.Status.SUCCESS);
//            // save invitation as messages
//            EMClient.getInstance().chatManager().saveMessage(msg);
        }
    }
//
//    /**
//     * group change listener
//     */
//    class MyGroupChangeListener implements EMGroupChangeListener {
//
//        @Override
//        public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
//
//            new InviteMessgeDao(appContext).deleteMessage(groupId);
//
//            // user invite you to join group
//            InviteMessage msg = new InviteMessage();
//            msg.setFrom(groupId);
//            msg.setTime(System.currentTimeMillis());
//            msg.setGroupId(groupId);
//            msg.setGroupName(groupName);
//            msg.setReason(reason);
//            msg.setGroupInviter(inviter);
//            Log.d(TAG, "receive invitation to join the group：" + groupName);
//            msg.setStatus(InviteMessage.InviteMesageStatus.GROUPINVITATION);
//            notifyNewInviteMessage(msg);
//            broadcastManager.sendBroadcast(new Intent(EaseConstant.ACTION_GROUP_CHANAGED));
//        }
//
//        @Override
//        public void onInvitationAccpted(String groupId, String invitee, String reason) {
//
//            new InviteMessgeDao(appContext).deleteMessage(groupId);
//
//            //user accept your invitation
//            boolean hasGroup = false;
//            EMGroup _group = null;
//            for (EMGroup group : EMClient.getInstance().groupManager().getAllGroups()) {
//                if (group.getGroupId().equals(groupId)) {
//                    hasGroup = true;
//                    _group = group;
//                    break;
//                }
//            }
//            if (!hasGroup)
//                return;
//
//            InviteMessage msg = new InviteMessage();
//            msg.setFrom(groupId);
//            msg.setTime(System.currentTimeMillis());
//            msg.setGroupId(groupId);
//            msg.setGroupName(_group == null ? groupId : _group.getGroupName());
//            msg.setReason(reason);
//            msg.setGroupInviter(invitee);
//            Log.d(TAG, invitee + "Accept to join the group：" + _group == null ? groupId : _group.getGroupName());
//            msg.setStatus(InviteMessage.InviteMesageStatus.GROUPINVITATION_ACCEPTED);
//            notifyNewInviteMessage(msg);
//            broadcastManager.sendBroadcast(new Intent(EaseConstant.ACTION_GROUP_CHANAGED));
//        }
//
//        @Override
//        public void onInvitationDeclined(String groupId, String invitee, String reason) {
//
//            new InviteMessgeDao(appContext).deleteMessage(groupId);
//
//            //user declined your invitation
//            boolean hasGroup = false;
//            EMGroup group = null;
//            for (EMGroup _group : EMClient.getInstance().groupManager().getAllGroups()) {
//                if (_group.getGroupId().equals(groupId)) {
//                    group = _group;
//                    hasGroup = true;
//                    break;
//                }
//            }
//            if (!hasGroup)
//                return;
//
//            InviteMessage msg = new InviteMessage();
//            msg.setFrom(groupId);
//            msg.setTime(System.currentTimeMillis());
//            msg.setGroupId(groupId);
//            msg.setGroupName(group == null ? groupId : group.getGroupName());
//            msg.setReason(reason);
//            msg.setGroupInviter(invitee);
//            Log.d(TAG, invitee + "Declined to join the group：" + group == null ? groupId : group.getGroupName());
//            msg.setStatus(InviteMessage.InviteMesageStatus.GROUPINVITATION_DECLINED);
//            notifyNewInviteMessage(msg);
//            broadcastManager.sendBroadcast(new Intent(EaseConstant.ACTION_GROUP_CHANAGED));
//        }
//
//        @Override
//        public void onUserRemoved(String groupId, String groupName) {
//            //user is removed from group
//            broadcastManager.sendBroadcast(new Intent(EaseConstant.ACTION_GROUP_CHANAGED));
//        }
//
//        @Override
//        public void onGroupDestroy(String groupId, String groupName) {
//            // group is dismissed,
//            broadcastManager.sendBroadcast(new Intent(EaseConstant.ACTION_GROUP_CHANAGED));
//        }
//
//        @Override
//        public void onApplicationReceived(String groupId, String groupName, String applyer, String reason) {
//
//            // user apply to join group
//            InviteMessage msg = new InviteMessage();
//            msg.setFrom(applyer);
//            msg.setTime(System.currentTimeMillis());
//            msg.setGroupId(groupId);
//            msg.setGroupName(groupName);
//            msg.setReason(reason);
//            Log.d(TAG, applyer + " Apply to join group：" + groupName);
//            msg.setStatus(InviteMessage.InviteMesageStatus.BEAPPLYED);
//            notifyNewInviteMessage(msg);
//            broadcastManager.sendBroadcast(new Intent(EaseConstant.ACTION_GROUP_CHANAGED));
//        }
//
//        @Override
//        public void onApplicationAccept(String groupId, String groupName, String accepter) {
//
//            String st4 = appContext.getString(R.string.Agreed_to_your_group_chat_application);
//            // your application was accepted
//            EMMessage msg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
//            msg.setChatType(EMMessage.ChatType.GroupChat);
//            msg.setFrom(accepter);
//            msg.setTo(groupId);
//            msg.setMsgId(UUID.randomUUID().toString());
//            msg.addBody(new EMTextMessageBody(accepter + " " +st4));
//            msg.setStatus(EMMessage.Status.SUCCESS);
//            // save accept message
//            EMClient.getInstance().chatManager().saveMessage(msg);
//            // notify the accept message
//            getNotifier().vibrateAndPlayTone(msg);
//
//            broadcastManager.sendBroadcast(new Intent(EaseConstant.ACTION_GROUP_CHANAGED));
//        }
//
//        @Override
//        public void onApplicationDeclined(String groupId, String groupName, String decliner, String reason) {
//            // your application was declined, we do nothing here in demo
//        }
//
//        @Override
//        public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {
//            // got an invitation
//            String st3 = appContext.getString(R.string.Invite_you_to_join_a_group_chat);
//            EMMessage msg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
//            msg.setChatType(EMMessage.ChatType.GroupChat);
//            msg.setFrom(inviter);
//            msg.setTo(groupId);
//            msg.setMsgId(UUID.randomUUID().toString());
//            msg.addBody(new EMTextMessageBody(inviter + " " +st3));
//            msg.setStatus(EMMessage.Status.SUCCESS);
//            // save invitation as messages
//            EMClient.getInstance().chatManager().saveMessage(msg);
//            // notify invitation message
//            getNotifier().vibrateAndPlayTone(msg);
//            EMLog.d(TAG, "onAutoAcceptInvitationFromGroup groupId:" + groupId);
//            broadcastManager.sendBroadcast(new Intent(EaseConstant.ACTION_GROUP_CHANAGED));
//        }
//    }
//
//    /***
//     * 好友变化listener
//     *
//     */
//    public class MyContactListener implements EMContactListener {
//
//        @Override
//        public void onContactAdded(String username) {
//            // save contact
//            Map<String, EaseUser> localUsers = getContactList();
//            Map<String, EaseUser> toAddUsers = new HashMap<String, EaseUser>();
//            EaseUser user = new EaseUser(username);
//
//            if (!localUsers.containsKey(username)) {
//                userDao.saveContact(user);
//            }
//            toAddUsers.put(username, user);
//            localUsers.putAll(toAddUsers);
//
//            broadcastManager.sendBroadcast(new Intent(EaseConstant.ACTION_CONTACT_CHANAGED));
//        }
//
//        @Override
//        public void onContactDeleted(String username) {
//            Map<String, EaseUser> localUsers = HXSDKHelper.getInstance().getContactList();
//            localUsers.remove(username);
//            userDao.deleteContact(username);
//            inviteMessgeDao.deleteMessage(username);
//
//            broadcastManager.sendBroadcast(new Intent(EaseConstant.ACTION_CONTACT_CHANAGED));
//        }
//
//        @Override
//        public void onContactInvited(String username, String reason) {
//            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
//
//            for (InviteMessage inviteMessage : msgs) {
//                if (inviteMessage.getGroupId() == null && inviteMessage.getFrom().equals(username)) {
//                    inviteMessgeDao.deleteMessage(username);
//                }
//            }
//            // save invitation as message
//            InviteMessage msg = new InviteMessage();
//            msg.setFrom(username);
//            msg.setTime(System.currentTimeMillis());
//            msg.setReason(reason);
//            Log.d(TAG, username + "apply to be your friend,reason: " + reason);
//            // set invitation status
//            msg.setStatus(InviteMessage.InviteMesageStatus.BEINVITEED);
//            notifyNewInviteMessage(msg);
//            broadcastManager.sendBroadcast(new Intent(EaseConstant.ACTION_CONTACT_CHANAGED));
//        }
//
//        @Override
//        public void onContactAgreed(String username) {
//            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
//            for (InviteMessage inviteMessage : msgs) {
//                if (inviteMessage.getFrom().equals(username)) {
//                    return;
//                }
//            }
//            // save invitation as message
//            InviteMessage msg = new InviteMessage();
//            msg.setFrom(username);
//            msg.setTime(System.currentTimeMillis());
//            Log.d(TAG, username + "accept your request");
//            msg.setStatus(InviteMessage.InviteMesageStatus.BEAGREED);
//            notifyNewInviteMessage(msg);
//            broadcastManager.sendBroadcast(new Intent(EaseConstant.ACTION_CONTACT_CHANAGED));
//        }
//
//        @Override
//        public void onContactRefused(String username) {
//            // your request was refused
//            Log.d(username, username + " refused to your request");
//        }
//    }
//
//    /**
//     * save and notify invitation message
//     * @param msg
//     */
//    private void notifyNewInviteMessage(InviteMessage msg){
//        if(inviteMessgeDao == null){
//            inviteMessgeDao = new InviteMessgeDao(appContext);
//        }
//        inviteMessgeDao.saveMessage(msg);
//        //increase the unread message count
//        inviteMessgeDao.saveUnreadMessageCount(1);
//        // notify there is new message
//        getNotifier().vibrateAndPlayTone(null);
//    }
//
//    /**
//     * user has logged into another device
//     */
//    protected void onConnectionConflict(){
//        Intent intent = new Intent(appContext, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra(EaseConstant.ACCOUNT_CONFLICT, true);
//        appContext.startActivity(intent);
//    }
//
//    /**
//     * account is removed
//     */
//    protected void onCurrentAccountRemoved(){
//        Intent intent = new Intent(appContext, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra(EaseEaseConstant.ACCOUNT_REMOVED, true);
//        appContext.startActivity(intent);
//    }

    private EaseUser getUserInfo(String username) {
        // To get instance of EaseUser, here we get it from the user list in memory
        // You'd better cache it if you get it from your server
        EaseUser user = null;
        if (username.equals(EMClient.getInstance().getCurrentUser())) {
            return getUserProfileManager().getCurrentUserInfo();
        }
        user = getContactList().get(username);
        if (user == null && getRobotList() != null) {
            user = getRobotList().get(username);
        }

        // if user is not in your contacts, set inital letter for him/her
        if (user == null) {
            user = new EaseUser(username);
            EaseCommonUtils.setUserInitialLetter(user);
        }
        return user;
    }

//    /**
//     * Global listener
//     * If this event already handled by an activity, you don't need handle it again
//     * activityList.size() <= 0 means all activities already in background or not in Activity Stack
//     */
//    protected void registerMessageListener() {
//        messageListener = new EMMessageListener() {
//            private BroadcastReceiver broadCastReceiver = null;
//
//            @Override
//            public void onMessageReceived(List<EMMessage> messages) {
//                for (EMMessage message : messages) {
//                    EMLog.d(TAG, "onMessageReceived id : " + message.getMsgId());
//                    // in background, do not refresh UI, notify it in notification bar
//                    if(!easeUI.hasForegroundActivies()){
//                        getNotifier().onNewMsg(message);
//                    }
//                }
//            }
//
//            @Override
//            public void onCmdMessageReceived(List<EMMessage> messages) {
//                for (EMMessage message : messages) {
//                    EMLog.d(TAG, "receive command message");
//                    //get message body
//                    EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
//                    final String action = cmdMsgBody.action();//获取自定义action
//                    //red packet code : 处理红包回执透传消息
//                    if(!easeUI.hasForegroundActivies()){
//                        //TODO COMMENT FOR REDPACKET shijiazi
////                        if (action.equals(RedPacketConstant.REFRESH_GROUP_RED_PACKET_ACTION)){
////                            RedPacketUtil.receiveRedPacketAckMessage(message);
////                            broadcastManager.sendBroadcast(new Intent(RedPacketConstant.REFRESH_GROUP_RED_PACKET_ACTION));
////                        }
//                    }
//                    //end of red packet code
//                    //获取扩展属性 此处省略
//                    //maybe you need get extension of your message
//                    //message.getStringAttribute("");
//                    EMLog.d(TAG, String.format("Command：action:%s,message:%s", action,message.toString()));
//                }
//            }
//
//            @Override
//            public void onMessageReadAckReceived(List<EMMessage> messages) {
//            }
//
//            @Override
//            public void onMessageDeliveryAckReceived(List<EMMessage> message) {
//            }
//
//            @Override
//            public void onMessageChanged(EMMessage message, Object change) {
//
//            }
//        };
//
//        EMClient.getInstance().chatManager().addMessageListener(messageListener);
//    }

    /**
     * if ever logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        return EMClient.getInstance().isLoggedInBefore();
    }

    public void logout(final EMCallBack callback) {
        logout(true, callback);
    }

    /**
     * logout
     *
     * @param unbindDeviceToken whether you need unbind your device token
     * @param callback          callback
     */
    public void logout(boolean unbindDeviceToken, final EMCallBack callback) {
        endCall();
        Log.d(TAG, "logout: " + unbindDeviceToken);
        EMClient.getInstance().logout(unbindDeviceToken, new EMCallBack() {

            @Override
            public void onSuccess() {
                Log.d(TAG, "logout: onSuccess");
                reset();
                if (callback != null) {
                    callback.onSuccess();
                }

            }

            @Override
            public void onProgress(int progress, String status) {
                if (callback != null) {
                    callback.onProgress(progress, status);
                }
            }

            @Override
            public void onError(int code, String error) {
                Log.d(TAG, "logout: onSuccess");
                reset();
                if (callback != null) {
                    callback.onError(code, error);
                }
            }
        });
    }

    /**
     * get instance of EaseNotifier
     *
     * @return
     */
    public EaseNotifier getNotifier() {
        return easeUI.getNotifier();
    }

    public HXSDKModel getModel() {
        return (HXSDKModel) hxModel;
    }

    /**
     * update contact list
     *
     * @param aContactList
     */
    public void setContactList(Map<String, EaseUser> aContactList) {
        if (aContactList == null) {
            if (contactList != null) {
                contactList.clear();
            }
            return;
        }

        contactList = aContactList;
    }

    /**
     * save single contact
     */
    public void saveContact(EaseUser user) {
        contactList.put(user.getUsername(), user);
        hxModel.saveContact(user);
    }

    /**
     * get contact list
     *
     * @return
     */
    public Map<String, EaseUser> getContactList() {
        if (isLoggedIn() && contactList == null) {
            contactList = hxModel.getContactList();
        }

        // return a empty non-null object to avoid app crash
        if (contactList == null) {
            return new Hashtable<String, EaseUser>();
        }

        return contactList;
    }

    /**
     * set current username
     *
     * @param username
     */
    public void setCurrentUserName(String username) {
        this.username = username;
        hxModel.setCurrentUserName(username);
    }

    /**
     * get current user's id
     */
    public String getCurrentUsernName() {
        if (username == null) {
            username = hxModel.getCurrentUsernName();
        }
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRobotList(Map<String, RobotUser> robotList) {
        this.robotList = robotList;
    }

    public Map<String, RobotUser> getRobotList() {
        if (isLoggedIn() && robotList == null) {
            robotList = hxModel.getRobotList();
        }
        return robotList;
    }

    /**
     * update user list to cache and database
     *
     * @param contactInfoList
     */
    public void updateContactList(List<EaseUser> contactInfoList) {
        for (EaseUser u : contactInfoList) {
            contactList.put(u.getUsername(), u);
        }
        ArrayList<EaseUser> mList = new ArrayList<EaseUser>();
        mList.addAll(contactList.values());
        hxModel.saveContactList(mList);
    }

    public UserProfileManager getUserProfileManager() {
        if (userProManager == null) {
            userProManager = new UserProfileManager();
        }
        return userProManager;
    }

    void endCall() {
        try {
            EMClient.getInstance().callManager().endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addSyncGroupListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (!syncGroupsListeners.contains(listener)) {
            syncGroupsListeners.add(listener);
        }
    }

    public void removeSyncGroupListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (syncGroupsListeners.contains(listener)) {
            syncGroupsListeners.remove(listener);
        }
    }

    public void addSyncContactListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (!syncContactsListeners.contains(listener)) {
            syncContactsListeners.add(listener);
        }
    }

    public void removeSyncContactListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (syncContactsListeners.contains(listener)) {
            syncContactsListeners.remove(listener);
        }
    }

    public void addSyncBlackListListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (!syncBlackListListeners.contains(listener)) {
            syncBlackListListeners.add(listener);
        }
    }

    public void removeSyncBlackListListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (syncBlackListListeners.contains(listener)) {
            syncBlackListListeners.remove(listener);
        }
    }

    /**
     * Get group list from server
     * This method will save the sync state
     *
     * @throws HyphenateException
     */
    public synchronized void asyncFetchGroupsFromServer(final EMCallBack callback) {
        if (isSyncingGroupsWithServer) {
            return;
        }

        isSyncingGroupsWithServer = true;

        new Thread() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().groupManager().getJoinedGroupsFromServer();

                    // in case that logout already before server returns, we should return immediately
                    if (!isLoggedIn()) {
                        isGroupsSyncedWithServer = false;
                        isSyncingGroupsWithServer = false;
                        noitifyGroupSyncListeners(false);
                        return;
                    }

                    hxModel.setGroupsSynced(true);

                    isGroupsSyncedWithServer = true;
                    isSyncingGroupsWithServer = false;

                    //notify sync group list success
                    noitifyGroupSyncListeners(true);

                    if (callback != null) {
                        callback.onSuccess();
                    }
                } catch (HyphenateException e) {
                    hxModel.setGroupsSynced(false);
                    isGroupsSyncedWithServer = false;
                    isSyncingGroupsWithServer = false;
                    noitifyGroupSyncListeners(false);
                    if (callback != null) {
                        callback.onError(e.getErrorCode(), e.toString());
                    }
                }

            }
        }.start();
    }

    public void noitifyGroupSyncListeners(boolean success) {
        for (DataSyncListener listener : syncGroupsListeners) {
            listener.onSyncComplete(success);
        }
    }

    public void asyncFetchContactsFromServer(final EMValueCallBack<List<String>> callback) {
        if (isSyncingContactsWithServer) {
            return;
        }

        isSyncingContactsWithServer = true;

        new Thread() {
            @Override
            public void run() {
                List<String> usernames = null;
                try {
                    usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    // in case that logout already before server returns, we should return immediately
                    if (!isLoggedIn()) {
                        isContactsSyncedWithServer = false;
                        isSyncingContactsWithServer = false;
                        notifyContactsSyncListener(false);
                        return;
                    }

                    Map<String, EaseUser> userlist = new HashMap<String, EaseUser>();
                    for (String username : usernames) {
                        EaseUser user = new EaseUser(username);
                        EaseCommonUtils.setUserInitialLetter(user);
                        userlist.put(username, user);
                    }
                    // save the contact list to cache
                    getContactList().clear();
                    getContactList().putAll(userlist);
                    // save the contact list to database
                    UserDao dao = new UserDao(appContext);
                    List<EaseUser> users = new ArrayList<EaseUser>(userlist.values());
                    dao.saveContactList(users);

                    hxModel.setContactSynced(true);
                    EMLog.d(TAG, "set contact syn status to true");

                    isContactsSyncedWithServer = true;
                    isSyncingContactsWithServer = false;

                    //notify sync success
                    notifyContactsSyncListener(true);

                    getUserProfileManager().asyncFetchContactInfosFromServer(usernames, new EMValueCallBack<List<EaseUser>>() {

                        @Override
                        public void onSuccess(List<EaseUser> uList) {
                            updateContactList(uList);
                            getUserProfileManager().notifyContactInfosSyncListener(true);
                        }

                        @Override
                        public void onError(int error, String errorMsg) {
                        }
                    });
                    if (callback != null) {
                        callback.onSuccess(usernames);
                    }
                } catch (HyphenateException e) {
                    hxModel.setContactSynced(false);
                    isContactsSyncedWithServer = false;
                    isSyncingContactsWithServer = false;
                    notifyContactsSyncListener(false);
                    e.printStackTrace();
                    if (callback != null) {
                        callback.onError(e.getErrorCode(), e.toString());
                    }
                }

            }
        }.start();
    }

    public void notifyContactsSyncListener(boolean success) {
        for (DataSyncListener listener : syncContactsListeners) {
            listener.onSyncComplete(success);
        }
    }

    public void asyncFetchBlackListFromServer(final EMValueCallBack<List<String>> callback) {

        if (isSyncingBlackListWithServer) {
            return;
        }

        isSyncingBlackListWithServer = true;

        new Thread() {
            @Override
            public void run() {
                try {
                    List<String> usernames = EMClient.getInstance().contactManager().getBlackListFromServer();

                    // in case that logout already before server returns, we should return immediately
                    if (!isLoggedIn()) {
                        isBlackListSyncedWithServer = false;
                        isSyncingBlackListWithServer = false;
                        notifyBlackListSyncListener(false);
                        return;
                    }

                    hxModel.setBlacklistSynced(true);

                    isBlackListSyncedWithServer = true;
                    isSyncingBlackListWithServer = false;

                    notifyBlackListSyncListener(true);
                    if (callback != null) {
                        callback.onSuccess(usernames);
                    }
                } catch (HyphenateException e) {
                    hxModel.setBlacklistSynced(false);

                    isBlackListSyncedWithServer = false;
                    isSyncingBlackListWithServer = true;
                    e.printStackTrace();

                    if (callback != null) {
                        callback.onError(e.getErrorCode(), e.toString());
                    }
                }

            }
        }.start();
    }

    public void notifyBlackListSyncListener(boolean success) {
        for (DataSyncListener listener : syncBlackListListeners) {
            listener.onSyncComplete(success);
        }
    }

    public boolean isSyncingGroupsWithServer() {
        return isSyncingGroupsWithServer;
    }

    public boolean isSyncingContactsWithServer() {
        return isSyncingContactsWithServer;
    }

    public boolean isSyncingBlackListWithServer() {
        return isSyncingBlackListWithServer;
    }

    public boolean isGroupsSyncedWithServer() {
        return isGroupsSyncedWithServer;
    }

    public boolean isContactsSyncedWithServer() {
        return isContactsSyncedWithServer;
    }

    public boolean isBlackListSyncedWithServer() {
        return isBlackListSyncedWithServer;
    }

    synchronized void reset() {
        isSyncingGroupsWithServer = false;
        isSyncingContactsWithServer = false;
        isSyncingBlackListWithServer = false;

        hxModel.setGroupsSynced(false);
        hxModel.setContactSynced(false);
        hxModel.setBlacklistSynced(false);

        isGroupsSyncedWithServer = false;
        isContactsSyncedWithServer = false;
        isBlackListSyncedWithServer = false;

        isGroupAndContactListenerRegisted = false;

        setContactList(null);
        setRobotList(null);
        getUserProfileManager().reset();
        HXDBManager.getInstance().closeDB();
    }

    public void pushActivity(Activity activity) {
        easeUI.pushActivity(activity);
    }

    public void popActivity(Activity activity) {
        easeUI.popActivity(activity);
    }

    public void getCancleNotification() {
        getNotifier().reset();
    }

    public boolean isLogined() {
        return isLoggedIn();
    }
}