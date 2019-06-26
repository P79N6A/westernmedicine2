package com.xywy.askforexpert.model.media;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shihao on 16/3/15.
 */
public class MediaList {

    /**
     * code : 10000
     * msg : 成功
     * data : [{"name":"医学论文","id":"92863422","list":[{"title":"75个一致性药品","id":"37662","url":"http://test.zixun_club.xywy.com/d37662.html?xywyfrom=h5","img":"http://xs3.op.xywy.com/club.xywy.com/mpzx/20160315/66771c760e10e26.jpg"},{"title":"医学测试测试测试44","id":"37666","url":"http://test.zixun_club.xywy.com/d37666.html?xywyfrom=h5","img":"http://xs3.op.xywy.com/club.xywy.com/mpzx/20160316/c88a4622dd78010.jpg"}]},{"name":"中国医生的一天","id":"87492994","list":[{"title":"75个一致性药品","id":"37662","url":"http://test.zixun_club.xywy.com/d37662.html?xywyfrom=h5","img":"http://xs3.op.xywy.com/club.xywy.com/mpzx/20160315/66771c760e10e26.jpg"},{"title":"医学测试测试测试44","id":"37666","url":"http://test.zixun_club.xywy.com/d37666.html?xywyfrom=h5","img":"http://xs3.op.xywy.com/club.xywy.com/mpzx/20160316/c88a4622dd78010.jpg"},{"title":"是否撒是否撒发的发","id":"37653","url":"http://test.zixun_club.xywy.com/d37653.html?xywyfrom=h5","img":"http://xs3.op.xywy.com/club.xywy.com/mpzx/20160309/3cd4226e7aa4252.jpg"},{"title":"预测乳腺癌，中性粒细胞","id":"37654","url":"http://test.zixun_club.xywy.com/d37654.html?xywyfrom=h5","img":"http://xs3.op.xywy.com/club.xywy.com/mpzx/20160310/78144da41351435.jpg"},{"title":"八问口服补液盐（ORS）","id":"37661","url":"http://test.zixun_club.xywy.com/d37661.html?xywyfrom=h5","img":"http://xs3.op.xywy.com/club.xywy.com/mpzx/20160315/784f25052c38c38.jpg"}]}]
     * isnew : 1
     * message : {"title":"测试测试测试测试策四","time":1458118162}
     * total : 2
     */

    private int code;
    private String msg;
    private int isnew;
    /**
     * title : 测试测试测试测试策四
     * time : 1458118162
     */

    private MessageEntity message;
    private int total;
    /**
     * name : 医学论文
     * id : 92863422
     * list : [{"title":"75个一致性药品","id":"37662","url":"http://test.zixun_club.xywy.com/d37662.html?xywyfrom=h5","img":"http://xs3.op.xywy.com/club.xywy.com/mpzx/20160315/66771c760e10e26.jpg"},{"title":"医学测试测试测试44","id":"37666","url":"http://test.zixun_club.xywy.com/d37666.html?xywyfrom=h5","img":"http://xs3.op.xywy.com/club.xywy.com/mpzx/20160316/c88a4622dd78010.jpg"}]
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

    public int getIsnew() {
        return isnew;
    }

    public void setIsnew(int isnew) {
        this.isnew = isnew;
    }

    public MessageEntity getMessage() {
        return message;
    }

    public void setMessage(MessageEntity message) {
        this.message = message;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public static class MessageEntity implements Serializable {
        private String title;
        private int time;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }
    }

    public static class DataEntity implements Serializable {
        private String name;
        private String id;
        /**
         * title : 75个一致性药品
         * id : 37662
         * url : http://test.zixun_club.xywy.com/d37662.html?xywyfrom=h5
         * img : http://xs3.op.xywy.com/club.xywy.com/mpzx/20160315/66771c760e10e26.jpg
         */

        private List<ListEntity> list;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<ListEntity> getList() {
            return list;
        }

        public void setList(List<ListEntity> list) {
            this.list = list;
        }

        public static class ListEntity implements Serializable {
            private String title;
            private String id;
            private String url;
            private String img;
            private String uu;
            private String vu;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

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
}
