package com.xywy.askforexpert.model.healthrecord;

import android.text.TextUtils;

import com.google.gson.JsonArray;

/**
 * Created by yuwentao on 16/6/2.
 */
public class BloodPresureModel {


    private int code;

    private int total;

    private JsonArray list_data;


    private String msg;
    private String ssy = "0";
    private String szy = "0";
    private String xl = "0";

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSsy() {
        return ssy;
    }

    public void setSsy(String ssy) {
        if (TextUtils.isEmpty(ssy)) {
            return;
        }
        this.ssy = ssy;
    }

    public String getSzy() {
        return szy;
    }

    public void setSzy(String szy) {
        if (TextUtils.isEmpty(szy)) {
            return;
        }
        this.szy = szy;
    }

    public String getXl() {
        return xl;
    }

    public void setXl(String xl) {
        if (TextUtils.isEmpty(xl)) {
            return;
        }
        this.xl = xl;
    }


    public static class Everyeasurement {
        private String ssy = "0";

        private String szy = "0";

        private String xl = "0";

        private String jlsj = "";

        public String getSsy() {
            return ssy;
        }

        public void setSsy(final String ssy) {
            this.ssy = ssy;
        }

        public String getSzy() {
            return szy;
        }

        public void setSzy(final String szy) {
            this.szy = szy;
        }

        public String getXl() {
            return xl;
        }

        public void setXl(final String xl) {
            this.xl = xl;
        }

        public String getJlsj() {
            return jlsj;
        }

        public void setJlsj(final String jlsj) {
            this.jlsj = jlsj;
        }


        public boolean hasNoData() {
            if (ssy.equals("0") && szy.equals("0") && xl.equals("0") && jlsj.equals("")) {
                return true;
            }
            return false;
        }
    }

}

