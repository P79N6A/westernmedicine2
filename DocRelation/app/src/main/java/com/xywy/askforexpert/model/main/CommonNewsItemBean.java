package com.xywy.askforexpert.model.main;

import com.xywy.askforexpert.model.news.MediaRecommendItem;

import java.util.List;

/**
 * Created by bailiangjin on 2017/1/10.
 */

public class CommonNewsItemBean {
    /**
     * id : 53951
     * title : 测测测试测试
     * image :
     * createtime : 2016-11-24
     * praiseNum : 0
     * style :
     * source :
     * catpid : 0
     * model : 0
     * mediaid : 93405000
     * mediaName : 新的一天来了
     * readNum : 3906
     * url : http://test.zixun_club.xywy.com/d53951.html?xywyfrom=h5&cat=6&m=1484028620
     * comNum : 0
     * recommend : [{"name":"中西医执助公开课","img":"http://xs3.op.xywy.com/club.xywy.com/doc/20160704/150_150_02c5d5afd9e72e.jpg","mid":"100741793"},{"name":"西北帕金森中心","img":"http://xs3.op.xywy.com/club.xywy.com/doc/20160705/150_150_2b2db5de714e0f.jpg","mid":"100788892"},{"name":"神经外科刘方军","img":"http://xs3.op.xywy.com/club.xywy.com/doc/20160706/150_150_a5ac29d5cfdbc8.jpg","mid":"100952904"},{"name":"啊撒地方萨芬","img":"http://xs3.op.xywy.com/club.xywy.com/doc/20161216/150_150_15125971a6c976.jpg","mid":"112783837"}]
     * imgs : ["http://xs3.op.xywy.com/club.xywy.com/mpzx/20160818/190b59ff46d86ef.jpg","http://xs3.op.xywy.com/club.xywy.com/mpzx/20160818/8ab2dfcf11384a4.jpg","http://xs3.op.xywy.com/club.xywy.com/mpzx/20160818/6430a5413f95b4b.jpg"]
     * video : {"uu":"","vu":null}
     */


    private int id;
    private String title;
    private String image;
    private String createtime;
    private String praiseNum;
    private String style;
    private String source;
    private String catpid;
    private String vector;
    private String model;
    private String mediaid;
    private String mediaName;
    private int praise;
    private int colle;
    private String readNum;
    private String url;
    private String comNum;
    private VideoBean video;
    private boolean isRead;

    private List<MediaRecommendItem> recommend;
    private List<String> imgs;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(String praiseNum) {
        this.praiseNum = praiseNum;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCatpid() {
        return catpid;
    }

    public void setCatpid(String catpid) {
        this.catpid = catpid;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVector() {
        return vector;
    }

    public void setVector(String vector) {
        this.vector = vector;
    }

    public String getMediaid() {
        return mediaid;
    }

    public void setMediaid(String mediaid) {
        this.mediaid = mediaid;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getReadNum() {
        return readNum;
    }

    public void setReadNum(String readNum) {
        this.readNum = readNum;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getComNum() {
        return comNum;
    }

    public void setComNum(String comNum) {
        this.comNum = comNum;
    }

    public VideoBean getVideo() {
        return video;
    }

    public void setVideo(VideoBean video) {
        this.video = video;
    }

    public List<MediaRecommendItem> getRecommend() {
        return recommend;
    }

    public void setRecommend(List<MediaRecommendItem> recommend) {
        this.recommend = recommend;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public int getPraise() {
        return praise;
    }

    public void setPraise(int praise) {
        this.praise = praise;
    }

    public int getColle() {
        return colle;
    }

    public void setColle(int colle) {
        this.colle = colle;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public static class VideoBean {
        /**
         * uu :
         * vu : null
         */

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

        public void setVu(String vu) {
            this.vu = vu;
        }
    }


}