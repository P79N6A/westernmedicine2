package com.xywy.askforexpert.model.websocket.msg.chatmsg;

import java.util.List;

/**
 * 新版本qid类型变化 stone
 */

public class BodyBean {

    /**
     * qid : 1
     * name : aaa
     * sex : 1
     * age : 20
     * user_photo : /im-static/images/18-40-2.png
     * pic : ["http://cp01-ocean-400.epc.baidu.com:8082/661fc.jpg","http://cp01-ocean-400.epc.baidu.com:8082/661fc.jpg"]
     * content : {"type":"text","text":"问题内容"}
     * time : 4568255
     * type : 101001
     */

    private String qid;
    private String name;
    private int sex;
    private int age;
    private String user_photo;
    private ContentBean content;
    private int time;
    private int type;
    private String bsid;
    private List<String> pic;

    public String getBsid() {
        return bsid;
    }

    public void setBsid(String bsid) {
        this.bsid = bsid;
    }

    /**
     * 客户端发送消息时使用的构造方法
     *
     * @param qid
     * @param content
     */
    public BodyBean(String qid, ContentBean content) {
        this.qid = qid;
        this.content = content;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getPic() {
        return pic;
    }

    public void setPic(List<String> pic) {
        this.pic = pic;
    }


// TODO: 2018/5/29 websocket新版本 stone
//    /**
//     * qid : 1
//     * name : aaa
//     * sex : 1
//     * age : 20
//     * user_photo : /im-static/images/18-40-2.png
//     * pic : ["http://cp01-ocean-400.epc.baidu.com:8082/661fc.jpg","http://cp01-ocean-400.epc.baidu.com:8082/661fc.jpg"]
//     * content : {"type":"text","text":"问题内容"}
//     * time : 4568255
//     * type : 101001
//     */
//
//    private int qid;
//    private String name;
//    private int sex;
//    private int age;
//    private String user_photo;
//    private ContentBean content;
//    private int time;
//    private int type;
//    private List<String> pic;
//
//    /**
//     * 客户端发送消息时使用的构造方法
//     * @param qid
//     * @param content
//     */
//    public BodyBean(int qid, ContentBean content) {
//        this.qid = qid;
//        this.content = content;
//    }
//
//    public int getQid() {
//        return qid;
//    }
//
//    public void setQid(int qid) {
//        this.qid = qid;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public int getSex() {
//        return sex;
//    }
//
//    public void setSex(int sex) {
//        this.sex = sex;
//    }
//
//    public int getAge() {
//        return age;
//    }
//
//    public void setAge(int age) {
//        this.age = age;
//    }
//
//    public String getUser_photo() {
//        return user_photo;
//    }
//
//    public void setUser_photo(String user_photo) {
//        this.user_photo = user_photo;
//    }
//
//    public ContentBean getContent() {
//        return content;
//    }
//
//    public void setContent(ContentBean content) {
//        this.content = content;
//    }
//
//    public int getTime() {
//        return time;
//    }
//
//    public void setTime(int time) {
//        this.time = time;
//    }
//
//    public int getType() {
//        return type;
//    }
//
//    public void setType(int type) {
//        this.type = type;
//    }
//
//    public List<String> getPic() {
//        return pic;
//    }
//
//    public void setPic(List<String> pic) {
//        this.pic = pic;
//    }


}
