package com.xywy.askforexpert.model.healthrecord;

import com.google.gson.JsonArray;

/**
 * Created by yuwentao on 16/6/3.
 */
public class BloodSugarModel {

    private int code;

    private int total;
    private int msg;

    private JsonArray list_data;


    /**
     * kongfu : 8.5
     * zaocanhou : 8.8
     * wucanqian : 8.9
     * wucanhou : 8.9
     * wancanqian : 8.7
     * wancanhou : 8.6
     * shuiqian : 8.8
     */

    private MaxDataEntity max_data;

    public MaxDataEntity getMax_data() {
        return max_data;
    }

    public void setMax_data(MaxDataEntity max_data) {
        this.max_data = max_data;
    }


    public static class BloodSugarRecordItem {


        private String clsj = "0";

        private String clz = "0";

        private String sjd = "暂无数据";

        public String getClsj() {
            return clsj;
        }

        public void setClsj(String clsj) {
            this.clsj = clsj;
        }

        public String getClz() {
            return clz;
        }

        public void setClz(String clz) {
            this.clz = clz;
        }

        public String getSjd() {
            return sjd;
        }

        public void setSjd(String sjd) {
            this.sjd = sjd;
        }

        public String toString() {
            return "clsj: " + clsj + "    clz: " + clz + "    sjd: " + sjd;
        }
    }


    public static class MaxDataEntity {
        private String kongfu;

        private String zaocanhou;

        private String wucanqian;

        private String wucanhou;

        private String wancanqian;

        private String wancanhou;

        private String shuiqian;

        public String getKongfu() {
            return kongfu;
        }

        public void setKongfu(String kongfu) {
            this.kongfu = kongfu;
        }

        public String getZaocanhou() {
            return zaocanhou;
        }

        public void setZaocanhou(String zaocanhou) {
            this.zaocanhou = zaocanhou;
        }

        public String getWucanqian() {
            return wucanqian;
        }

        public void setWucanqian(String wucanqian) {
            this.wucanqian = wucanqian;
        }

        public String getWucanhou() {
            return wucanhou;
        }

        public void setWucanhou(String wucanhou) {
            this.wucanhou = wucanhou;
        }

        public String getWancanqian() {
            return wancanqian;
        }

        public void setWancanqian(String wancanqian) {
            this.wancanqian = wancanqian;
        }

        public String getWancanhou() {
            return wancanhou;
        }

        public void setWancanhou(String wancanhou) {
            this.wancanhou = wancanhou;
        }

        public String getShuiqian() {
            return shuiqian;
        }

        public void setShuiqian(String shuiqian) {
            this.shuiqian = shuiqian;
        }
    }
}
