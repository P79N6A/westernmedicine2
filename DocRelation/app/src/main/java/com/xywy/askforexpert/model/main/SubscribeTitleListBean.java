package com.xywy.askforexpert.model.main;

import com.xywy.askforexpert.model.api.BaseResultBean;

import java.util.List;

/**
 * 订阅列表 title
 * Created by bailiangjin on 2017/1/10.
 */
public class SubscribeTitleListBean extends BaseResultBean{

    /**
     * subscribe : [{"name":"推荐","id":0,"type":2},{"name":"媒体号","id":6,"type":2},{"id":47,"name":"耳鼻喉科","type":1},{"id":42,"name":"中医科","type":1},{"id":15,"name":"要闻头条","type":1},{"id":35,"name":"神经内科","type":1},{"id":55,"name":"婴幼儿护理","type":1},{"id":5,"name":"其他","type":1},{"id":34,"name":"内分泌科","type":1},{"id":37,"name":"肿瘤科","type":1},{"id":33,"name":"消化内科","type":1},{"id":50,"name":"泌尿外科","type":1},{"id":4,"name":"儿科","type":1},{"id":3,"name":"妇产科","type":0},{"id":10,"name":"生物医药","type":0},{"id":16,"name":"医改政策","type":0},{"id":17,"name":"医者故事","type":0},{"id":30,"name":"考试科研","type":0},{"id":31,"name":"最新会议","type":0},{"id":32,"name":"呼吸内科","type":0},{"id":36,"name":"肾内科","type":0},{"id":38,"name":"血液内科","type":0},{"id":39,"name":"心血管科","type":0},{"id":40,"name":"骨科","type":0},{"id":41,"name":"普外科","type":0},{"id":44,"name":"皮肤性病","type":0},{"id":45,"name":"精神心理","type":0},{"id":46,"name":"生殖孕育","type":0},{"id":48,"name":"口腔科","type":0},{"id":49,"name":"传染科","type":0},{"id":51,"name":"心胸外科","type":0},{"id":52,"name":"整形烧伤科","type":0},{"id":53,"name":"急诊科","type":0},{"id":54,"name":"妇产护理","type":0},{"id":56,"name":"乳腺外科","type":0},{"id":57,"name":"肝胆外科","type":0}]
     * code : 10000
     * msg : 成功
     */

    private List<SubscribeBean> subscribe;



    public List<SubscribeBean> getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(List<SubscribeBean> subscribe) {
        this.subscribe = subscribe;
    }

    public static class SubscribeBean {
        /**
         * name : 推荐
         * id : 0
         * type : 2
         */

        private String name;
        private int id;
        private int type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
