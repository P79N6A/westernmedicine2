package com.xywy.modifyjson.entity;

import java.util.List;

/**
 * 专家网的科室信息
 */
public class DepartmentEntity {


    /**
     * pname : 中医学
     * pid : 15
     * subs : [{"sname":"中医妇产科","sid":"125"},{"sname":"中医儿科","sid":"126"},{"sname":"中医骨科","sid":"127"},
     * {"sname":"中医皮肤科","sid":"128"},{"sname":"中医内分泌","sid":"129"},{"sname":"中医消化科","sid":"130"},{"sname":"中医呼吸科",
     * "sid":"131"},{"sname":"中医肾病内科","sid":"132"},{"sname":"中医免疫内科","sid":"133"},{"sname":"中医心内科","sid":"134"},
     * {"sname":"中医神经内科","sid":"135"},{"sname":"中医肿瘤科","sid":"136"},{"sname":"中医血液科","sid":"137"},{"sname":"中医感染内科",
     * "sid":"138"},{"sname":"中医肝病科","sid":"139"},{"sname":"中医五官科","sid":"140"},{"sname":"中医泌尿、男科","sid":"141"},
     * {"sname":"针灸科","sid":"142"},{"sname":"中医按摩科","sid":"143"},{"sname":"中医乳腺外科","sid":"144"},{"sname":"中医外科",
     * "sid":"145"},{"sname":"中医肛肠科","sid":"146"},{"sname":"中医老年病科","sid":"147"},{"sname":"中医科","sid":"148"},
     * {"sname":"中医眼科","sid":"149"},{"sname":"中医内科","sid":"204"}]
     */

    private List<DataEntity> data;

    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public static class DataEntity {
        private String pname;

        private String pid;

        /**
         * sname : 中医妇产科
         * sid : 125
         */

        private List<SubsEntity> subs;

        public String getPname() {
            return pname;
        }

        public void setPname(String pname) {
            this.pname = pname;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public List<SubsEntity> getSubs() {
            return subs;
        }

        public void setSubs(List<SubsEntity> subs) {
            this.subs = subs;
        }

        public static class SubsEntity {
            private String sname;

            private String sid;

            public String getSname() {
                return sname;
            }

            public void setSname(String sname) {
                this.sname = sname;
            }

            public String getSid() {
                return sid;
            }

            public void setSid(String sid) {
                this.sid = sid;
            }
        }
    }
}
