package com.xywy.askforexpert.model.consultentity;

import java.util.List;

/**
 * Created by zhangzheng on 2017/5/3.
 */

public class QuestionMsgListRspEntity {


    /**
     * code : 10000
     * msg : success
     * data : {"status":1,"list":[{"qid":"16025","content":[{"type":"0","text":"澶т究鍛�"}],"q_a":"1","status":"1","created_time":"1495440595","chat_id":"212438","content_type":"text","user_photo":"http://d.xywy.com/im-static/images/18-40-2.png"},{"qid":"16025","content":[{"type":"0","text":"鏈堢粡姝ｅ悧"}],"q_a":"1","status":"1","created_time":"1495440582","chat_id":"212435","content_type":"text","user_photo":"http://d.xywy.com/im-static/images/18-40-2.png"},{"qid":"16025","content":[{"type":"0","text":"璇︾粏鎻忚堪涓\u20ac涓嬪彂鐥呰鍐�"}],"q_a":"1","status":"1","created_time":"1495440555","chat_id":"212431","content_type":"text","user_photo":"http://d.xywy.com/im-static/images/18-40-2.png"},{"qid":"16025","content":[{"type":"0","text":"杩樻湁鍏朵粬鐥呭彶鍚�"}],"q_a":"1","status":"1","created_time":"1495440502","chat_id":"212425","content_type":"text","user_photo":"http://d.xywy.com/im-static/images/18-40-2.png"},{"qid":"16025","content":[{"type":"0","text":"鑲氬瓙涓嬩晶鐤肩殑鍦版柟瀵硅繋鐫\u20ac鑵扮殑浣嶇疆涔熺柤"}],"q_a":"2","status":"1","created_time":"1495440323","chat_id":"212405","content_type":"text","user_photo":"http://d.xywy.com/im-static/images/18-40-2.png"},{"qid":"16025","content":[{"type":"0","text":"鑲氬瓙宸︿笅渚х柤 璧拌矾鑵块兘鐤笺\u20ac�"}],"q_a":"2","status":"1","created_time":"1495440235","chat_id":"212399","content_type":"text","user_photo":"http://d.xywy.com/im-static/images/18-40-2.png"},{"qid":"16025","content":[{"type":"0","text":"浠\u20ac涔堜笉鑸掓湇"}],"q_a":"1","status":"1","created_time":"1495439976","chat_id":"212390","content_type":"text","user_photo":"http://d.xywy.com/im-static/images/18-40-2.png"},{"qid":"16025","content":[{"type":"0","text":"鎮ㄥソ"}],"q_a":"1","status":"1","created_time":"1495439954","chat_id":"212388","content_type":"text","user_photo":"http://d.xywy.com/im-static/images/18-40-2.png"},{"qid":"16025","content":[{"type":"1","url":"http://xs3.op.xywy.com/d.xywy.com/20170522/34f229938f76e835455141f145487b2e.jpg"}],"q_a":"2","status":"1","created_time":"1495439697","chat_id":"212375","content_type":"media","user_photo":"http://d.xywy.com/im-static/images/18-40-2.png"},{"qid":"16025","content":[{"type":"0","text":"鎴戝師鏉ュ仛杩囦汉娴佹墜鏈� "}],"q_a":"2","status":"1","created_time":"1495439697","chat_id":"212374","content_type":"text","user_photo":"http://d.xywy.com/im-static/images/18-40-2.png"}]}
     */

    private int code;
    private String msg;
    /**
     * status : 1
     * list : [{"qid":"16025","content":[{"type":"0","text":"澶т究鍛�"}],"q_a":"1","status":"1","created_time":"1495440595","chat_id":"212438","content_type":"text","user_photo":"http://d.xywy.com/im-static/images/18-40-2.png"},{"qid":"16025","content":[{"type":"0","text":"鏈堢粡姝ｅ悧"}],"q_a":"1","status":"1","created_time":"1495440582","chat_id":"212435","content_type":"text","user_photo":"http://d.xywy.com/im-static/images/18-40-2.png"},{"qid":"16025","content":[{"type":"0","text":"璇︾粏鎻忚堪涓\u20ac涓嬪彂鐥呰鍐�"}],"q_a":"1","status":"1","created_time":"1495440555","chat_id":"212431","content_type":"text","user_photo":"http://d.xywy.com/im-static/images/18-40-2.png"},{"qid":"16025","content":[{"type":"0","text":"杩樻湁鍏朵粬鐥呭彶鍚�"}],"q_a":"1","status":"1","created_time":"1495440502","chat_id":"212425","content_type":"text","user_photo":"http://d.xywy.com/im-static/images/18-40-2.png"},{"qid":"16025","content":[{"type":"0","text":"鑲氬瓙涓嬩晶鐤肩殑鍦版柟瀵硅繋鐫\u20ac鑵扮殑浣嶇疆涔熺柤"}],"q_a":"2","status":"1","created_time":"1495440323","chat_id":"212405","content_type":"text","user_photo":"http://d.xywy.com/im-static/images/18-40-2.png"},{"qid":"16025","content":[{"type":"0","text":"鑲氬瓙宸︿笅渚х柤 璧拌矾鑵块兘鐤笺\u20ac�"}],"q_a":"2","status":"1","created_time":"1495440235","chat_id":"212399","content_type":"text","user_photo":"http://d.xywy.com/im-static/images/18-40-2.png"},{"qid":"16025","content":[{"type":"0","text":"浠\u20ac涔堜笉鑸掓湇"}],"q_a":"1","status":"1","created_time":"1495439976","chat_id":"212390","content_type":"text","user_photo":"http://d.xywy.com/im-static/images/18-40-2.png"},{"qid":"16025","content":[{"type":"0","text":"鎮ㄥソ"}],"q_a":"1","status":"1","created_time":"1495439954","chat_id":"212388","content_type":"text","user_photo":"http://d.xywy.com/im-static/images/18-40-2.png"},{"qid":"16025","content":[{"type":"1","url":"http://xs3.op.xywy.com/d.xywy.com/20170522/34f229938f76e835455141f145487b2e.jpg"}],"q_a":"2","status":"1","created_time":"1495439697","chat_id":"212375","content_type":"media","user_photo":"http://d.xywy.com/im-static/images/18-40-2.png"},{"qid":"16025","content":[{"type":"0","text":"鎴戝師鏉ュ仛杩囦汉娴佹墜鏈� "}],"q_a":"2","status":"1","created_time":"1495439697","chat_id":"212374","content_type":"text","user_photo":"http://d.xywy.com/im-static/images/18-40-2.png"}]
     */

    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {


        private int closed;//1 关闭了 0 未关闭 stone
        private int report;//1 举报 0 未举报 stone

        private int status;//0认领 1聊天  stone

        private int allow_summary_count;//多少条可以问诊总结  stone

        private int is_conclusion;

        public int getReport() {
            return report;
        }

        public void setReport(int report) {
            this.report = report;
        }

        public int getIs_conclusion() {
            return is_conclusion;
        }

        public void setIs_conclusion(int is_conclusion) {
            this.is_conclusion = is_conclusion;
        }

        /**
         * qid : 16025
         * content : [{"type":"0","text":"澶т究鍛�"}]
         * q_a : 1
         * status : 1
         * created_time : 1495440595
         * chat_id : 212438
         * content_type : text
         * user_photo : http://d.xywy.com/im-static/images/18-40-2.png
         */

        private List<ListBean> list;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public int getAllow_summary_count() {
            return allow_summary_count;
        }

        public void setAllow_summary_count(int allow_summary_count) {
            this.allow_summary_count = allow_summary_count;
        }

        public int getClosed() {
            return closed;
        }

        public void setClosed(int closed) {
            this.closed = closed;
        }

        public static class ListBean {
            private String qid;
            private String q_a;
            private String status;
            private String created_time;
            private String chat_id;
            private String content_type;
            private String user_photo;
            /**
             * type : 0
             * text : 澶т究鍛�
             */

            private List<ContentBean> content;

            public String getQid() {
                return qid;
            }

            public void setQid(String qid) {
                this.qid = qid;
            }

            public String getQ_a() {
                return q_a;
            }

            public void setQ_a(String q_a) {
                this.q_a = q_a;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getCreated_time() {
                return created_time;
            }

            public void setCreated_time(String created_time) {
                this.created_time = created_time;
            }

            public String getChat_id() {
                return chat_id;
            }

            public void setChat_id(String chat_id) {
                this.chat_id = chat_id;
            }

            public String getContent_type() {
                return content_type;
            }

            public void setContent_type(String content_type) {
                this.content_type = content_type;
            }

            public String getUser_photo() {
                return user_photo;
            }

            public void setUser_photo(String user_photo) {
                this.user_photo = user_photo;
            }

            public List<ContentBean> getContent() {
                return content;
            }

            public void setContent(List<ContentBean> content) {
                this.content = content;
            }

            public static class ContentBean {
                private String type;
                private String text;
                private String url;
                private String amount;//打赏金额 stone
                private int width;
                private int height;

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }


                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getAmount() {
                    return amount;
                }

                public void setAmount(String amount) {
                    this.amount = amount;
                }
            }
        }
    }
}
