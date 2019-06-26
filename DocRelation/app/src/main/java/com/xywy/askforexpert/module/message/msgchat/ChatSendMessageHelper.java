package com.xywy.askforexpert.module.message.msgchat;

import android.content.Context;
import android.content.Intent;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ACache;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.AddressBook;
import com.xywy.easeWrapper.EMChatManager;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.UUID;

/**
 * Created by wangpeng on 16/7/13.
 * describ：
 * revise：
 */
public class ChatSendMessageHelper {

    private static String TAG = "sendMessage";

    /**
     * cmd 透传消息发送
     *
     * @param toUser     发送人 id 或者群id
     * @param content    发送内容
     * @param toRealname 发送者名称或者 群名称
     * @param toHeadImge 头像 或者群头像
     */
    public static void sendCMDMessage(String toUser, String content, String toRealname, String toHeadImge) {
        sendCMDMessage(toUser, content, toRealname, toHeadImge, false);
    }

    public static void sendCMDMessage(String toUser, String content, String toRealname, String toHeadImge, boolean isJson) {


        EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);

        //支持单聊和群聊，默认单聊，如果是群聊添加下面这行
//        cmdMsg.setChatType(EMMessage.ChatType.GroupChat);

        String action = "singleCmd";//action可以自定义，在广播接收时可以收到
        EMCmdMessageBody cmdBody = new EMCmdMessageBody(action);
        String toUsername = toUser;//发送给某个人
        cmdMsg.setReceipt(toUsername);
        cmdMsg.setAttribute("command", "needUpdate");//支持自定义扩展
//        cmdMsg.setAttribute("isJson", isJson);
//        cmdMsg.setAttribute("content", content);//支持自定义扩展

        cmdMsg.setAttribute("fromRealName", YMApplication.getLoginInfo().getData().getRealname());
        cmdMsg.setAttribute("fromAvatar", YMApplication.getLoginInfo().getData().getPhoto());
        cmdMsg.setAttribute("toRealName", toRealname);
        cmdMsg.setAttribute("toAvatar", toHeadImge);

        //stone 新添加的扩展字段区别是医脉还是用药助手
        if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
            cmdMsg.setAttribute("source", "selldrug");
        }


        cmdMsg.addBody(cmdBody);
        EMChatManager.getInstance().sendMessage(cmdMsg, new EMCallBack() {
            @Override
            public void onSuccess() {

                DLog.d(TAG, "发送成功");
            }

            @Override
            public void onError(int i, String s) {
                DLog.d(TAG, "发送失败");

            }

            @Override
            public void onProgress(int i, String s) {
                DLog.d(TAG, "发送中");

            }
        });
    }

    /**
     * 保存本地数据
     *
     * @param username   id
     * @param content    内容
     * @param toRealname
     * @param toHeadImge
     */
    public static void saveMessage(String username, String content, String toRealname, String toHeadImge) {

        EMTextMessageBody txtBody = null;
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
        message.setReceipt(username);
        txtBody = new EMTextMessageBody(content);
        message.setStatus(EMMessage.Status.SUCCESS);
        message.setChatType(EMMessage.ChatType.GroupChat);
        message.setAttribute("fromRealName", YMApplication.getLoginInfo().getData().getRealname());
        message.setAttribute("fromAvatar", YMApplication.getLoginInfo().getData().getPhoto());

        message.setAttribute("toRealName", toRealname);
        message.setAttribute("toAvatar", toHeadImge);

        message.setAttribute("newMsgType", "groupCMD");

        //stone 新添加的扩展字段区别是医脉搏还是用药助手
        if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
            message.setAttribute("source", "selldrug");
        }
        message.addBody(txtBody);

        String msgid = UUID.randomUUID().toString();
        message.setMsgId(msgid);
        EMChatManager.getInstance().saveMessage(message, false);
    }


    public static void saveNomalMessage(Context context, String username, String content, String toRealname, String toHeadImge, String msgType) {

        EMTextMessageBody txtBody = null;
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
        message.setReceipt(username);
        txtBody = new EMTextMessageBody(content);
        message.setStatus(EMMessage.Status.SUCCESS);
        //        message.setChatType(EMMessage.ChatType.GroupChat);
        message.setAttribute("fromRealName", YMApplication.getLoginInfo().getData().getRealname());
        message.setAttribute("fromAvatar", YMApplication.getLoginInfo().getData().getPhoto());

        message.setAttribute("toRealName", toRealname);
        message.setAttribute("toAvatar", toHeadImge);

        //stone 新添加的扩展字段区别是医脉搏还是用药助手
        if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
            message.setAttribute("source", "selldrug");
        }

        message.setAttribute("newMsgType", msgType);
        message.addBody(txtBody);

        String msgid = UUID.randomUUID().toString();
        message.setMsgId(msgid);
        EMChatManager.getInstance().saveMessage(message, true);

        Intent intent = new Intent();
        intent.setAction("needUpdate");
        context.sendBroadcast(intent);
    }

    static AddressBook pListInfo = null;

    public static AddressBook getMsgFriendCard(Context context) {
        final String did = YMApplication.getLoginInfo().getData().getPid();
        String bind = YMApplication.getLoginInfo().getData().getHuanxin_username();
        Long st = System.currentTimeMillis();
        final ACache mCache = ACache.get(context);

        String sign = MD5Util.MD5(st + bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();

        params.put("timestamp", st + "");
        params.put("bind", bind);
        params.put("a", "chat");
        params.put("m", "getRelation");
        params.put("username", bind);
        params.put("sign", sign);
        FinalHttp fh = new FinalHttp();
        DLog.d(TAG, "名片夹url = " + CommonUrl.Patient_Manager_Url + "?" + params.toString());
        fh.get(CommonUrl.Patient_Manager_Url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
//                LogUtils.i("名片夹返回数据" + t.toString());
                pListInfo = ResolveJson.R_CardHold(t.toString());
                mCache.put("card" + did, pListInfo);
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
        return pListInfo;
    }

    /**
     * 返回好友信息
     *
     * @param context
     * @param hxid
     * @return
     */
    public static AddressBook getMsgFriendInfo(Context context, String hxid) {
        if (hxid == null) {
            return null;
        }
        final ACache mCache = ACache.get(context);
        AddressBook friendinfo = new AddressBook();
        AddressBook cachePlistInfo = (AddressBook) mCache.getAsObject("card" + YMUserService.getCurUserId());
        if (cachePlistInfo != null && cachePlistInfo.getData() != null && cachePlistInfo.getData().size() > 0) {
            for (int i = 0; i < cachePlistInfo.getData().size(); i++) {
                if (hxid.equals(cachePlistInfo.getData().get(i).getHxusername())) {
                    friendinfo = cachePlistInfo.getData().get(i);
                    break;
                }
            }
        }
        return friendinfo;

    }


}
