package com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity;

import android.text.TextUtils;

/**
 * Created by xgxg on 2017/3/27.
 */

public class PersonCard {

        /**
         * id : 436213
         * old_id : 317740
         * user_id : 68257985
         * expert_id : 0
         * isdomain : 0
         * ispublish : 1
         * audit_status : 1
         * phone : 15172330957
         * real_name : 送什么
         * gender : 男
         * photo : http://xs3.op.xywy.com/api.app.xywy.com/appimg/20170405/58e4cd5d81a4c.png
         * clinic : 1
         * be_good_at : 哦P民如图做头欧诺屠夫多头木头诺妥投木头诺拖
         * introduce : 婆婆婆特勒偷偷目睹特特特区二二二ROM7门女女婆婆红
         * hos_name : 北京协和医院
         * province : 11
         * city : 1101
         * major_first : 外科
         * major_second : 功能神经外科
         * card_image : http://xs3.op.xywy.com/api.app.xywy.com/appimg/20170405/58e4c9f4269de.png|http://xs3.op.xywy.com/api.app.xywy.com/appimg/20170405/58e4ca6a41fef.png|http://xs3.op.xywy.com/api.app.xywy.com/appimg/20170405/58e4ca6e90d49.png|http://xs3.op.xywy.com/api.app.xywy.com/appimg/20170405/58e4cd837dd80.png
         * certificate_image : http://xs3.op.xywy.com/api.app.xywy.com/appimg/20170405/58e4c9f3c7ab9.png|http://xs3.op.xywy.com/api.app.xywy.com/appimg/20170405/58e4ca6a760c0.png|http://xs3.op.xywy.com/api.app.xywy.com/appimg/20170405/58e4ca6c7407d.png|http://xs3.op.xywy.com/api.app.xywy.com/appimg/20170405/58e4cd8368d8a.png
         * create_time : 1490947848
         * club : -1
         * familyDoctor : -1
         * zjzixun : -1
         * jiahao : -1
         * dhys : -1
         * xianxia : 1
         * wkys : 1
         * imwd : 1
         * hx_username : medicine_did_68257985
         * hx_password : 0bb78c76c31b19a519bc1c5f3a05b0a2
         * qrcode : http://xs3.op.xywy.com/club.xywy.com/medicine/7.jpg
         * gold : 0
         */

        private String id;
        private String old_id;
        private String user_id;
        private String expert_id;
        private String isdomain;
        private String ispublish;
        private String audit_status;
        private String phone;
        private String real_name;
        private String gender;
        private String photo;
        private String clinic;
        private String be_good_at="";
        private String introduce="";
        private String hos_name;
        private String province;
        private String city;
        private String major_first;
        private String major_second;
        private String card_image;
        private String certificate_image;
        private String create_time;
        private String club;
        private String familyDoctor;
        private String zjzixun;
        private String jiahao;
        private String dhys;
        private String xianxia;
        private String wkys;
        private String imwd;
        private String hx_username;
        private String hx_password;
        private String qrcode;
        private String gold;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOld_id() {
            return old_id;
        }

        public void setOld_id(String old_id) {
            this.old_id = old_id;
        }

        public String getUser_id() {
            if(TextUtils.isEmpty(user_id)){
                //添加这个判断是否防止，在PatientListActivity类中，
                //mDoctorId = Integer.parseInt(user.getLoginServerBean().getUser_id());
                //解析出user.getLoginServerBean().getUser_id()的值为null,导致Integer.parseInt(null)
                //报错
                return "-1";
            }
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getExpert_id() {
            return expert_id;
        }

        public void setExpert_id(String expert_id) {
            this.expert_id = expert_id;
        }

        public String getIsdomain() {
            return isdomain;
        }

        public void setIsdomain(String isdomain) {
            this.isdomain = isdomain;
        }

        public String getIspublish() {
            return ispublish;
        }

        public void setIspublish(String ispublish) {
            this.ispublish = ispublish;
        }

        public String getAudit_status() {
            return audit_status;
        }

        public void setAudit_status(String audit_status) {
            this.audit_status = audit_status;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getReal_name() {
            return real_name;
        }

        public void setReal_name(String real_name) {
            this.real_name = real_name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getClinic() {
            return clinic;
        }

        public void setClinic(String clinic) {
            this.clinic = clinic;
        }

        public String getBe_good_at() {
            return be_good_at;
        }

        public void setBe_good_at(String be_good_at) {
            this.be_good_at = be_good_at;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getHos_name() {
            return hos_name;
        }

        public void setHos_name(String hos_name) {
            this.hos_name = hos_name;
        }

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

        public String getMajor_first() {
            return major_first;
        }

        public void setMajor_first(String major_first) {
            this.major_first = major_first;
        }

        public String getMajor_second() {
            return major_second;
        }

        public void setMajor_second(String major_second) {
            this.major_second = major_second;
        }

        public String getCard_image() {
            return card_image;
        }

        public void setCard_image(String card_image) {
            this.card_image = card_image;
        }

        public String getCertificate_image() {
            return certificate_image;
        }

        public void setCertificate_image(String certificate_image) {
            this.certificate_image = certificate_image;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getClub() {
            return club;
        }

        public void setClub(String club) {
            this.club = club;
        }

        public String getFamilyDoctor() {
            return familyDoctor;
        }

        public void setFamilyDoctor(String familyDoctor) {
            this.familyDoctor = familyDoctor;
        }

        public String getZjzixun() {
            return zjzixun;
        }

        public void setZjzixun(String zjzixun) {
            this.zjzixun = zjzixun;
        }

        public String getJiahao() {
            return jiahao;
        }

        public void setJiahao(String jiahao) {
            this.jiahao = jiahao;
        }

        public String getDhys() {
            return dhys;
        }

        public void setDhys(String dhys) {
            this.dhys = dhys;
        }

        public String getXianxia() {
            return xianxia;
        }

        public void setXianxia(String xianxia) {
            this.xianxia = xianxia;
        }

        public String getWkys() {
            return wkys;
        }

        public void setWkys(String wkys) {
            this.wkys = wkys;
        }

        public String getImwd() {
            return imwd;
        }

        public void setImwd(String imwd) {
            this.imwd = imwd;
        }

        public String getHx_username() {
            return hx_username;
        }

        public void setHx_username(String hx_username) {
            this.hx_username = hx_username;
        }

        public String getHx_password() {
            return hx_password;
        }

        public void setHx_password(String hx_password) {
            this.hx_password = hx_password;
        }

        public String getQrcode() {
            return qrcode;
        }

        public void setQrcode(String qrcode) {
            this.qrcode = qrcode;
        }

        public String getGold() {
            return gold;
        }

        public void setGold(String gold) {
            this.gold = gold;
        }
}
