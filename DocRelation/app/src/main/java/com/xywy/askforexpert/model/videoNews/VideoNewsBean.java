package com.xywy.askforexpert.model.videoNews;

import com.xywy.askforexpert.model.discussDetail.DiscussPraiseList;

import java.util.List;

import javax.annotation.Generated;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/6/22 17:43
 */
@Generated("org.jsonschema2pojo")
public class VideoNewsBean {
    /**
     * comment : [{"userid":"69779769","photo":"http://static.img.xywy.com/club/niming/ys.png","nickname":"马莳","level":"2","createtime":"3个月前","subject":"五官科","hospital":"抚顺市第五医院","content":"dnjsjjsjsiwisk"},{"userid":"20751339","photo":"http://static.img.xywy.com/club/niming/ys.png","nickname":"淳于意","level":"2","createtime":"3个月前","subject":"内科","hospital":"","content":"哈哈哈哈"}]
     * praise : [{"photo":"http://static.img.xywy.com/doc_connection/images/docPC.jpg?v=1466740740","userid":"19088510"},{"photo":"http://i1.wkimg.com/web/Image20.gif?v=1458892596","userid":"64947642"},{"photo":"http://xs3.op.xywy.com/club.xywy.com/doc/20160321/100_100_e976c7d0c755cd.jpg?v=1459413129","userid":"69779769"},{"photo":"http://doctor.club.xywy.com/images/upload/paper/2015072417593962092.jpg?v=1466732727","userid":"18732252"},{"photo":"http://xs3.op.xywy.com/club.xywy.com/doc/20160318/100_100_6faaebb41d49eb.jpg?v=1463736561","userid":"55257233"}]
     * related : [{"id":37689,"click":4134,"title":"测试测试文章排序","url":"http://test.zixun_club.xywy.com/d37689.html?xywyfrom=h5&cat=15&m=1466746288"}]
     * url : http://test.zixun_club.xywy.com/d37654.html?xywyfrom=h5
     * image : http://xs3.op.xywy.com/club.xywy.com/mpzx/20160310/78144da41351435.jpg
     * ismedia : 1
     * media : {"mediaid":"87492994","img":"http://xs3.op.xywy.com/club.xywy.com/doc/20160317/150_150_39772ef92116d2.jpg"}
     * is_subscription : 0
     * photo : http://static.img.xywy.com/club/niming/ys.png
     * nickname : 淳于意
     * iscollection : 0
     * ispraise : 0
     * title : 预测乳腺癌，中性粒细胞
     * vector :
     * source : 医脉媒体号
     * author : 医脉官方
     * createtime : 3个月前
     * commentNum : 2
     * praiseNum : 5
     */

    private String url;
    private String image;
    private int ismedia;
    /**
     * mediaid : 87492994
     * img : http://xs3.op.xywy.com/club.xywy.com/doc/20160317/150_150_39772ef92116d2.jpg
     */

    private MediaBean media;
    private int is_subscription;
    private String photo;
    private String nickname;
    private int iscollection;
    private int ispraise;
    private String title;
    private String vector;
    private String source;
    private String author;
    private String createtime;
    private String commentNum;
    private String praiseNum;
    /**
     * userid : 69779769
     * photo : http://static.img.xywy.com/club/niming/ys.png
     * nickname : 马莳
     * level : 2
     * createtime : 3个月前
     * subject : 五官科
     * hospital : 抚顺市第五医院
     * content : dnjsjjsjsiwisk
     */

    private List<CommentBean> comment;
    /**
     * photo : http://static.img.xywy.com/doc_connection/images/docPC.jpg?v=1466740740
     * userid : 19088510
     */

    private List<DiscussPraiseList> praise;
    /**
     * id : 37689
     * click : 4134
     * title : 测试测试文章排序
     * url : http://test.zixun_club.xywy.com/d37689.html?xywyfrom=h5&cat=15&m=1466746288
     */

    private List<RelatedBean> related;
    /**
     * uu : cfd9191aeb
     * uv : 79ff602f42
     */

    private VideoBean video;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getIsmedia() {
        return ismedia;
    }

    public void setIsmedia(int ismedia) {
        this.ismedia = ismedia;
    }

    public MediaBean getMedia() {
        return media;
    }

    public void setMedia(MediaBean media) {
        this.media = media;
    }

    public int getIs_subscription() {
        return is_subscription;
    }

    public void setIs_subscription(int is_subscription) {
        this.is_subscription = is_subscription;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getIscollection() {
        return iscollection;
    }

    public void setIscollection(int iscollection) {
        this.iscollection = iscollection;
    }

    public int getIspraise() {
        return ispraise;
    }

    public void setIspraise(int ispraise) {
        this.ispraise = ispraise;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVector() {
        return vector;
    }

    public void setVector(String vector) {
        this.vector = vector;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public String getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(String praiseNum) {
        this.praiseNum = praiseNum;
    }

    public List<CommentBean> getComment() {
        return comment;
    }

    public void setComment(List<CommentBean> comment) {
        this.comment = comment;
    }

    public List<DiscussPraiseList> getPraise() {
        return praise;
    }

    public void setPraise(List<DiscussPraiseList> praise) {
        this.praise = praise;
    }

    public List<RelatedBean> getRelated() {
        return related;
    }

    public void setRelated(List<RelatedBean> related) {
        this.related = related;
    }

    public VideoBean getVideo() {
        return video;
    }

    public void setVideo(VideoBean video) {
        this.video = video;
    }

    @Override
    public String toString() {
        return "VideoNewsBean{" +
                "url='" + url + '\'' +
                ", image='" + image + '\'' +
                ", ismedia=" + ismedia +
                ", media=" + media +
                ", is_subscription=" + is_subscription +
                ", photo='" + photo + '\'' +
                ", nickname='" + nickname + '\'' +
                ", iscollection=" + iscollection +
                ", ispraise=" + ispraise +
                ", title='" + title + '\'' +
                ", vector='" + vector + '\'' +
                ", source='" + source + '\'' +
                ", author='" + author + '\'' +
                ", createtime='" + createtime + '\'' +
                ", commentNum='" + commentNum + '\'' +
                ", praiseNum='" + praiseNum + '\'' +
                ", comment=" + comment +
                ", praise=" + praise.toString() +
                ", related=" + related +
                ", video=" + video +
                '}';
    }

    public static class MediaBean {
        private String mediaid;
        private String img;

        public String getMediaid() {
            return mediaid;
        }

        public void setMediaid(String mediaid) {
            this.mediaid = mediaid;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        @Override
        public String toString() {
            return "MediaBean{" +
                    "mediaid='" + mediaid + '\'' +
                    ", img='" + img + '\'' +
                    '}';
        }
    }

    public static class CommentBean {
        private String userid;
        private String photo;
        private String nickname;
        private String level;
        private String createtime;
        private String subject;
        private String hospital;
        private String content;

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getHospital() {
            return hospital;
        }

        public void setHospital(String hospital) {
            this.hospital = hospital;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return "CommentBean{" +
                    "userid='" + userid + '\'' +
                    ", photo='" + photo + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", level='" + level + '\'' +
                    ", createtime='" + createtime + '\'' +
                    ", subject='" + subject + '\'' +
                    ", hospital='" + hospital + '\'' +
                    ", content='" + content + '\'' +
                    '}';
        }
    }

    public static class RelatedBean {
        private String id;
        private int click;
        private String title;
        private String url;
        private int model;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getClick() {
            return click;
        }

        public void setClick(int click) {
            this.click = click;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getModel() {
            return model;
        }

        public void setModel(int model) {
            this.model = model;
        }
    }

    public static class VideoBean {
        private String uu;
        private String vu;

        public String getUu() {
            return uu;
        }

        public void setUu(String uu) {
            this.uu = uu;
        }

        public String getVu() {
            return vu;
        }

        public void setUv(String vu) {
            this.vu = vu;
        }

        @Override
        public String toString() {
            return "VideoBean{" +
                    "uu='" + uu + '\'' +
                    ", vu='" + vu + '\'' +
                    '}';
        }
    }
}
