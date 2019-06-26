package com.xywy.livevideo.chat.interfaces;

import android.app.Activity;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/2/24 16:27
 * 聊天业务接口
 */

public interface IChatModel {

    void sendGiftMsg(String presentId, int count);

    void showMsgInputBox(Activity activity);

    void sendMsg(String content);

    void refreshMsgs();

    void onFinish() ;
}
