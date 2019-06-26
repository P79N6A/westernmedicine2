package com.xywy.askforexpert.module.discovery.medicine.module.chat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.xywy.easeWrapper.controller.EaseUI;
import com.xywy.easeWrapper.domain.EaseUser;
import com.xywy.util.ContextUtil;
import com.xywy.util.SharedPreferencesHelper;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/4/11 10:26
 * 环信用户昵称，头像管理类
 */

public class HxUserHelper {

    public static EaseUI.EaseUserProfileProvider getUserProvider() {
        return UserHolder.userProvider;
    }

    public static void addUser(EMMessage message) {
        if (message == null)
            return;
        if (message.getType() == EMMessage.Type.TXT) {
            try {
                String nickName = message.getStringAttribute("userName");
                String headImage = message.getStringAttribute("userAvatar");
                EaseUser easeUser = new EaseUser(message.getFrom());
//                easeUser.setNickname(nickName);
                easeUser.setNick(nickName);
                easeUser.setAvatar(headImage);
                UserHolder.addUser(easeUser);
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addUser(String hxId, String nickName, String head) {
        EaseUser easeUser = new EaseUser(hxId);
//        easeUser.setNickname(nickName);
        easeUser.setNick(nickName);
        easeUser.setAvatar(head);
        UserHolder.addUser(easeUser);
    }

    static class UserHolder {
        static Gson gson = new Gson();
        private static Map<String, EaseUser> userMap;
        static EaseUI.EaseUserProfileProvider userProvider = new EaseUI.EaseUserProfileProvider() {
            @Override
            public EaseUser getUser(String username) {
                return userMap.get(username);
            }
        };

        //初始化
        static {
            Type type = new TypeToken<HashMap<String, EaseUser>>() {
            }.getType();
            String jsonStr = SharedPreferencesHelper.getSystem(ContextUtil.getAppContext()).getString("HX_USER_MAP");
            userMap = new Gson().fromJson(jsonStr, type);
            if (userMap == null)
                userMap = new HashMap<>();
        }

        private static void addUser(EaseUser user) {
            userMap.put(user.getUsername(), user);

            String jsonStr = gson.toJson(userMap);
            SharedPreferencesHelper.getSystem(ContextUtil.getAppContext()).putString("HX_USER_MAP", jsonStr);
        }
    }
}
