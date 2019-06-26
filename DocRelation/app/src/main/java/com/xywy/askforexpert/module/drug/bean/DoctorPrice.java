package com.xywy.askforexpert.module.drug.bean;

/**
 * Created by jason on 2018/8/6.
 */

public class DoctorPrice {
    public Prescription doctor;

    public Prescription getDoctor() {
        return doctor;
    }

    public void setDoctor(Prescription doctor) {
        this.doctor = doctor;
    }

    public class Prescription{
        private String ask_amount;
        private String ask_hlwyy_amount;

        public String getAsk_hlwyy_amount() {
            return ask_hlwyy_amount;
        }

        public void setAsk_hlwyy_amount(String ask_hlwyy_amount) {
            this.ask_hlwyy_amount = ask_hlwyy_amount;
        }

        public String getAsk_amount() {
            return ask_amount;
        }

        public void setAsk_amount(String ask_amount) {
            this.ask_amount = ask_amount;
        }
    }
}
