package com.xywy.askforexpert.model.topics;

import java.util.List;

/**
 * Created by shihao on 16/4/27.
 */
public class MoreTopicItem {


    /**
     * code : 0
     * msg : 成功
     * list : [{"id":62,"theme":"测试","userid":"20751339","image":"http://xs3.op.xywy.com/club.xywy.com/doc/20160426/72646f5f63b05f.jpg","level":"25","createtime":"1461663982","description":"","recommend":"0","fansNum":0,"dynamicNum":0},{"id":61,"theme":"测测时时","userid":"20751339","image":"http://xs3.op.xywy.com/club.xywy.com/doc/20160426/ee999300789aac.jpg","level":"25","createtime":"1461662881","description":"","recommend":"0","fansNum":0,"dynamicNum":0},{"id":60,"theme":"测试测试","userid":"20751339","image":"http://static.img.xywy.com/doc_connection/images/doc_photo.png","level":"25","createtime":"1461662678","description":"","recommend":"0","fansNum":0,"dynamicNum":0},{"id":59,"theme":"15:03","userid":"65173122","image":"http://xs3.op.xywy.com/club.xywy.com/doc/20160426/efe5c675b6a82c.jpg","level":"25","createtime":"1461654224","description":"15:03YimaiTopicCategoryResultModelYimaiTopicCategoryResultModel","recommend":"0","fansNum":0,"dynamicNum":0},{"id":58,"theme":"14:59","userid":"65173122","image":"http://xs3.op.xywy.com/club.xywy.com/doc/20160426/c506bf087cf5d3.jpg","level":"25","createtime":"1461654015","description":"1111111111111111","recommend":"0","fansNum":0,"dynamicNum":0},{"id":57,"theme":"14:42","userid":"65173122","image":"http://xs3.op.xywy.com/club.xywy.com/doc/20160426/f94fd72f66fe86.jpg","level":"25","createtime":"1461652980","description":"huatifenlei lvxing yule","recommend":"0","fansNum":0,"dynamicNum":0},{"id":56,"theme":"话题名字14:07","userid":"65173122","image":"http://xs3.op.xywy.com/club.xywy.com/doc/20160426/ac7ed1380a5aa3.png","level":"25","createtime":"1461650853","description":"9.25:50","recommend":"0","fansNum":0,"dynamicNum":0},{"id":55,"theme":"话题名字13:43","userid":"65173122","image":"http://xs3.op.xywy.com/club.xywy.com/doc/20160426/ea38efd5622308.jpg","level":"25","createtime":"1461650777","description":"9.25:50","recommend":"0","fansNum":0,"dynamicNum":0},{"id":54,"theme":"美丽","userid":"65173122","image":"http://static.img.xywy.com/doc_connection/images/doc_photo.png","level":"25","createtime":"1461650727","description":"","recommend":"0","fansNum":0,"dynamicNum":0},{"id":53,"theme":"话题名字13:42","userid":"65173122","image":"http://static.img.xywy.com/doc_connection/images/doc_photo.png","level":"25","createtime":"1461649331","description":"9.25:50","recommend":"0","fansNum":0,"dynamicNum":0}]
     * total : 26
     */

    private int code;
    private String msg;
    private int total;
    private String title;
    /**
     * id : 62
     * theme : 测试
     * userid : 20751339
     * image : http://xs3.op.xywy.com/club.xywy.com/doc/20160426/72646f5f63b05f.jpg
     * level : 25
     * createtime : 1461663982
     * description :
     * recommend : 0
     * fansNum : 0
     * dynamicNum : 0
     */

    private List<ListEntity> list;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public static class ListEntity {
        private int id;
        private String theme;
        private String userid;
        /**
         * 添加头信息
         */
        private String header;
        private String image;
        private String level;
        private String createtime;
        private String description;
        private String recommend;
        private int fansNum;
        private int dynamicNum;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTheme() {
            return theme;
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getRecommend() {
            return recommend;
        }

        public void setRecommend(String recommend) {
            this.recommend = recommend;
        }

        public int getFansNum() {
            return fansNum;
        }

        public void setFansNum(int fansNum) {
            this.fansNum = fansNum;
        }

        public int getDynamicNum() {
            return dynamicNum;
        }

        public void setDynamicNum(int dynamicNum) {
            this.dynamicNum = dynamicNum;
        }

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }
    }
}
