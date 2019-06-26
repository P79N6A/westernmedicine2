package com.xywy.modifyjson.entity;

import java.util.List;

/**
 * 电话医生列表页用到的医院的数据
 */
public class HospitalEntity {


    /**
     * province : 上海市
     * province_id : 2
     * city : 南汇
     * city_id : 19
     * hospital : 上海市第六人民医院东院
     * id : 4807
     * level : 三级甲等
     * type : 公立
     * is_yibao : 医保
     */

    private List<DataEntity> data;

    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public static class DataEntity {
        private String province;


        private String city;

        private String city_id;

        private String hospital;

        private String id;

        private String level;

        private String type;

        private String is_yibao;

        private String province_id;

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }


        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCity_id() {
            return city_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }

        public String getHospital() {
            return hospital;
        }

        public void setHospital(String hospital) {
            this.hospital = hospital;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getIs_yibao() {
            return is_yibao;
        }

        public void setIs_yibao(String is_yibao) {
            this.is_yibao = is_yibao;
        }

        public String getProvince_id() {
            return province_id;
        }

        public void setProvince_id(String province_id) {
            this.province_id = province_id;
        }
    }
}
