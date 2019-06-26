package com.xywy.livevideo.chat.interfaces;

import android.app.Activity;

import com.xywy.livevideo.chat.model.LiveChatContent;

import java.util.List;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/2/24 16:34
 */

public interface IChatView {
    void refreshChatView(List<LiveChatContent> msgs);

    Activity getActivity();

    void sendGiftMsg(String presentId, int count);

    void showMsgInputBox();
}
