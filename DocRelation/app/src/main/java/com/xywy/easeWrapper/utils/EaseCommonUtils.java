/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xywy.easeWrapper.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.HanziToPinyin;
import com.hyphenate.util.HanziToPinyin.Token;
import com.xywy.askforexpert.R;
import com.xywy.easeWrapper.EaseConstant;
import com.xywy.easeWrapper.domain.EaseUser;

import java.util.ArrayList;
import java.util.List;

public class EaseCommonUtils {
	private static final String TAG = "CommonUtils";
	/**
	 * check if network avalable
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetWorkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable() && mNetworkInfo.isConnected();
			}
		}

		return false;
	}

	/**
	 * check if sdcard exist
	 * 
	 * @return
	 */
	public static boolean isSdcardExist() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}
	
	public static EMMessage createExpressionMessage(String toChatUsername, String expressioName, String identityCode){
	    EMMessage message = EMMessage.createTxtSendMessage("["+expressioName+"]", toChatUsername);
        if(identityCode != null){
            message.setAttribute(EaseConstant.MESSAGE_ATTR_EXPRESSION_ID, identityCode);
        }
        message.setAttribute(EaseConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, true);
        return message;
	}

    /**
     * 根据消息内容和消息类型获取消息内容提示
     *
     * @param message
     * @param context
     * @return
     */
    public static String getMessageDigest(EMMessage message, Context context) {
        String digest = "";
        switch (message.getType()) {
            case LOCATION: // 位置消息
                if (message.direct() == EMMessage.Direct.RECEIVE) {
                    // 从sdk中提到了ui中，使用更简单不犯错的获取string方法
                    // digest = EasyUtils.getAppResourceString(context,
                    // "location_recv");
                    digest = getString(context, R.string.location_recv);
                    digest = String.format(digest, message.getFrom());
                    return digest;
                } else {
                    // digest = EasyUtils.getAppResourceString(context,
                    // "location_prefix");
                    digest = getString(context, R.string.location_prefix);
                }
                break;
            case IMAGE: // 图片消息
                digest = getString(context, R.string.picture);
                break;
            case VOICE:// 语音消息
                digest = getString(context, R.string.voice);
                break;
            case VIDEO: // 视频消息
                digest = getString(context, R.string.video);
                break;
            case TXT: // 文本消息
                if (!message.getBooleanAttribute(
                        EaseConstant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
                    digest = txtBody.getMessage();
                } else {
                    EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
                    digest = getString(context, R.string.voice_call)
                            + txtBody.getMessage();
                }
                break;
            case FILE: // 普通文件消息
                digest = getString(context, R.string.file);
                break;
            default:
                System.err.println("error, unknow type");
                return "";
        }

        return digest;
    }
//
//	/**
//     * Get digest according message type and content
//     *
//     * @param message
//     * @param context
//     * @return
//     */
//    public static String getMessageDigest(EMMessage message, Context context) {
//        String digest = "";
//        switch (message.getType()) {
//        case LOCATION:
//            if (message.direct() == EMMessage.Direct.RECEIVE) {
//                digest = getString(context, R.string.location_recv);
//                digest = String.format(digest, message.getFrom());
//                return digest;
//            } else {
//                digest = getString(context, R.string.location_prefix);
//            }
//            break;
//        case IMAGE:
//            digest = getString(context, R.string.picture);
//            break;
//        case VOICE:
//            digest = getString(context, R.string.voice_prefix);
//            break;
//        case VIDEO:
//            digest = getString(context, R.string.video);
//            break;
//        case TXT:
//            EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
//            if(message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_VOICE_CALL, false)){
//                digest = getString(context, R.string.voice_call) + txtBody.getMessage();
//            }else if(message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_VIDEO_CALL, false)){
//                digest = getString(context, R.string.video_call) + txtBody.getMessage();
//            }else if(message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)){
//                if(!TextUtils.isEmpty(txtBody.getMessage())){
//                    digest = txtBody.getMessage();
//                }else{
//                    digest = getString(context, R.string.dynamic_expression);
//                }
//            }else{
//                digest = txtBody.getMessage();
//            }
//            break;
//        case FILE:
//            digest = getString(context, R.string.file);
//            break;
//        default:
//            EMLog.e(TAG, "error, unknow type");
//            return "";
//        }
//
//        return digest;
//    }
    
    static String getString(Context context, int resId){
        return context.getResources().getString(resId);
    }
	
	/**
	 * get top activity
	 * @param context
	 * @return
	 */
	public static String getTopActivity(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

		if (runningTaskInfos != null)
			return runningTaskInfos.get(0).topActivity.getClassName();
		else
			return "";
	}
	
	/**
     * set initial letter of according user's nickname( username if no nickname)
     * 
     * @param user
     * @param user
     */
    public static void setUserInitialLetter(EaseUser user) {
        final String DefaultLetter = "#";
        String letter = DefaultLetter;
        
        final class GetInitialLetter {
            String getLetter(String name) {
                if (TextUtils.isEmpty(name)) {
                    return DefaultLetter;
                }
                char char0 = name.toLowerCase().charAt(0);
                if (Character.isDigit(char0)) {
                    return DefaultLetter;
                }
                ArrayList<Token> l = HanziToPinyin.getInstance().get(name.substring(0, 1));
                if (l != null && l.size() > 0 && l.get(0).target.length() > 0)
                {
                    Token token = l.get(0);
                    String letter = token.target.substring(0, 1).toUpperCase();
                    char c = letter.charAt(0);
                    if (c < 'A' || c > 'Z') {
                        return DefaultLetter;
                    }
                    return letter;
                }
                return DefaultLetter;
            }
        }
        
        if ( !TextUtils.isEmpty(user.getNick()) ) {
            letter = new GetInitialLetter().getLetter(user.getNick());
            user.setInitialLetter(letter);
            return;
        }
        if (letter == DefaultLetter && !TextUtils.isEmpty(user.getUsername())) {
            letter = new GetInitialLetter().getLetter(user.getUsername());
        }
        user.setInitialLetter(letter);
    }
    
    /**
     * change the chat type to EMConversationType
     * @param chatType
     * @return
     */
    public static EMConversationType getConversationType(int chatType) {
        if (chatType == EaseConstant.CHATTYPE_SINGLE) {
            return EMConversationType.Chat;
        } else if (chatType == EaseConstant.CHATTYPE_GROUP) {
            return EMConversationType.GroupChat;
        } else {
            return EMConversationType.ChatRoom;
        }
    }

}
