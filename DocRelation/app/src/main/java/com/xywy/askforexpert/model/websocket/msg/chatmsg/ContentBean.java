package com.xywy.askforexpert.model.websocket.msg.chatmsg;

import com.xywy.askforexpert.model.websocket.type.ContentType;

/**
 * Created by bailiangjin on 2017/4/27.
 */

public class ContentBean {
    /**
     * type : text
     * text : 问题内容
     */

    private ContentType type;
    private String text;
    private String file;

    /**
     * 客户端发送消息时使用的 构造方法
     * @param text
     */
    public ContentBean(String text) {
        this.type = ContentType.text;
        this.text = text;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public ContentType getType() {
        return type;
    }

    public void setType(ContentType type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
