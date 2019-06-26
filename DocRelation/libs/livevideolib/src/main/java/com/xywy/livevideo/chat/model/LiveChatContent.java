package com.xywy.livevideo.chat.model;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/3/16 15:39
 */

public class LiveChatContent {
    public LiveChatContent(CharSequence name, CharSequence content) {
        this.name = name;
        this.content = content;
    }

    public CharSequence name;
    public CharSequence content;

    public LiveChatContent(CharSequence name, CharSequence content, String imageurl) {
        this.name = name;
        this.content = content;
        this.imageurl = imageurl;
    }

    public String imageurl;
}
