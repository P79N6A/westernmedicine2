package com.xywy.askforexpert.model.consultentity;

/**
 * Created by bailiangjin on 2017/5/8.
 */

public class DoctorInfoBean {

    /**
     * user_id : 1
     * gender : 0
     * photo :
     * be_good_at :
     * introduce :
     * major_first : 0
     * major_second : 0
     * hospitalid : 0
     * province : 0
     * city : 0
     * details : {"hos_name":"","province":"","city":"","major_first":"","major_second":"","gender":""}
     * work : {"club":-1,"familyDoctor":-1,"zjzixun":-1,"jiahao":-1,"dhys":-1,"xianxia":-1,"wkys":-1,"imwd":-1}
     */

    private int user_id;
    private int gender;
    private String photo;
    private String be_good_at;
    private String introduce;
    private int major_first;
    private int major_second;
    private int hospitalid;
    private int province;
    private int city;
    private DetailsBean details;
    private WorkBean work;
    private String subject_pid;

    public String getSubject_pid() {
        return subject_pid;
    }

    public void setSubject_pid(String subject_pid) {
        this.subject_pid = subject_pid;
    }

    //用药助手新增字段
    private String real_name;
    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    //用药助手新增字段

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public int getMajor_first() {
        return major_first;
    }

    public void setMajor_first(int major_first) {
        this.major_first = major_first;
    }

    public int getMajor_second() {
        return major_second;
    }

    public void setMajor_second(int major_second) {
        this.major_second = major_second;
    }

    public int getHospitalid() {
        return hospitalid;
    }

    public void setHospitalid(int hospitalid) {
        this.hospitalid = hospitalid;
    }

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public DetailsBean getDetails() {
        return details;
    }

    public void setDetails(DetailsBean details) {
        this.details = details;
    }

    public WorkBean getWork() {
        return work;
    }

    public void setWork(WorkBean work) {
        this.work = work;
    }

    public static class DetailsBean {
        /**
         * hos_name :
         * province :
         * city :
         * major_first :
         * major_second :
         * gender :
         */

        private String hos_name;
        private String province;
        private String city;
        private String major_first;
        private String major_second;
        private String gender;



        //用药助手添加
        private String clinic;
        public String getClinic() {
            return clinic;
        }

        public void setClinic(String clinic) {
            this.clinic = clinic;
        }
        //用药助手添加

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

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }
    }

    public static class WorkBean {
        /**
         * club : -1
         * familyDoctor : -1
         * zjzixun : -1
         * jiahao : -1
         * dhys : -1
         * xianxia : -1
         * wkys : -1
         * imwd : -1
         */

        private int club;
        private int familyDoctor;
        private int zjzixun;
        private int jiahao;
        private int dhys;
        private int xianxia;
        private int wkys;
        private int imwd;

        public int getClub() {
            return club;
        }

        public void setClub(int club) {
            this.club = club;
        }

        public int getFamilyDoctor() {
            return familyDoctor;
        }

        public void setFamilyDoctor(int familyDoctor) {
            this.familyDoctor = familyDoctor;
        }

        public int getZjzixun() {
            return zjzixun;
        }

        public void setZjzixun(int zjzixun) {
            this.zjzixun = zjzixun;
        }

        public int getJiahao() {
            return jiahao;
        }

        public void setJiahao(int jiahao) {
            this.jiahao = jiahao;
        }

        public int getDhys() {
            return dhys;
        }

        public void setDhys(int dhys) {
            this.dhys = dhys;
        }

        public int getXianxia() {
            return xianxia;
        }

        public void setXianxia(int xianxia) {
            this.xianxia = xianxia;
        }

        public int getWkys() {
            return wkys;
        }

        public void setWkys(int wkys) {
            this.wkys = wkys;
        }

        public int getImwd() {
            return imwd;
        }

        public void setImwd(int imwd) {
            this.imwd = imwd;
        }
    }
}