package com.xywy.askforexpert.model.notice;

/**
 * Created by xugan on 2018/5/29.
 */

public class Notice {
    public String id;   //公告id
    public String title;  //公告标题
    public String intime;  //公告时间
    public String content;  //公告内容
    public int hs_read;  //是否有公告   0代表没有公告(或者公告未读)        1是有公告(或者公告已读)
    public String new_url;
    public int new_read;
}
