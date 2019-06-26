package com.xywy.askforexpert.module.discovery.medicine.module.account.beans;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/3/28 11:09
 */

public class PicCodeBean {
    public String imageUrl;
    private String timestamp;
    public String picIdentify;//图片的标识符
    public String userInput;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
