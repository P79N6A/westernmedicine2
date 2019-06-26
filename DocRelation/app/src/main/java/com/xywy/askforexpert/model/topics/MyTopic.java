package com.xywy.askforexpert.model.topics;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shihao on 16/4/19.
 */
public class MyTopic {
    private static final long serialVersionUID = 1L;


    /**
     * code : 0
     * msg : 成功
     * data : [{"themes":[{"count":0,"image":"http://static.img.xywy.com/doc_connection/images/doc_photo.png","cat":[{"id":3,"name":"旅行","createtime":"1461318160"}],"theme":"阿卡丽","id":71,"level":1},{"count":0,"image":"http://static.img.xywy.com/doc_connection/images/doc_photo.png","cat":[{"id":3,"name":"旅行","createtime":"1461318160"}],"theme":"佛佛也得","id":70,"level":1}],"title":"我的话题"}]
     */

    private int code;
    private String msg;
    /**
     * themes : [{"count":0,"image":"http://static.img.xywy.com/doc_connection/images/doc_photo.png","cat":[{"id":3,"name":"旅行","createtime":"1461318160"}],"theme":"阿卡丽","id":71,"level":1},{"count":0,"image":"http://static.img.xywy.com/doc_connection/images/doc_photo.png","cat":[{"id":3,"name":"旅行","createtime":"1461318160"}],"theme":"佛佛也得","id":70,"level":1}]
     * title : 我的话题
     */

    private List<DataEntity> data;

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

    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public static class DataEntity implements Serializable {
        private String title;
        /**
         * count : 0
         * image : http://static.img.xywy.com/doc_connection/images/doc_photo.png
         * cat : [{"id":3,"name":"旅行","createtime":"1461318160"}]
         * theme : 阿卡丽
         * id : 71
         * level : 1
         */

        private List<ThemesEntity> themes;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<ThemesEntity> getThemes() {
            return themes;
        }

        public void setThemes(List<ThemesEntity> themes) {
            this.themes = themes;
        }

        public static class ThemesEntity implements Serializable {
            private int count;
            private String image;
            private String theme;
            private String description;
            private int id;
            private int level;
            /**
             * id : 3
             * name : 旅行
             * createtime : 1461318160
             */

            private List<TopicType.TopicTypeBean> cat;

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getTheme() {
                return theme;
            }

            public void setTheme(String theme) {
                this.theme = theme;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }

            public List<TopicType.TopicTypeBean> getCat() {
                return cat;
            }

            public void setCat(List<TopicType.TopicTypeBean> cat) {
                this.cat = cat;
            }
        }
    }
}
