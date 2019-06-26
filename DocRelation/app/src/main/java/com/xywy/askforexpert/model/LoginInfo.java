package com.xywy.askforexpert.model;

/**
 * 登录 模型
 *
 * @author 王鹏
 * @2015-5-15上午11:30:04
 */
public class LoginInfo {
    /**
     * isjob=1为医生 isdoctor 12 13 14为医学生 isdoctor=13&&approveid=0为申请审核中
     * isdoctor=13&&approveid=为申请审失败 isdoctor=14为申请成功
     * <p>
     * <p>
     * isdoctor ！＝12 13 14 医生 isjob ＝0 为认证 approve ＝0 默认 approve ＝1 审核中 审核成功
     * isjob＝ 1 isjob＝2 专家医生
     * <p>
     * isdoctor ＝12 默认 is doctor ＝13 学生认证apprpve＝0 ; is doctor =14 成功
     * <p>
     * 认证失败 doctor＝13 approve＝1
     */

    private String code;
    private String msg;
    private UserData data = new UserData();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LoginInfo{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public class UserData {
        private String pid;//医生id
        private String token;
        private String realname;//昵称
        private String count;
        private String isjob;
        private String isdoctor;
        private String approveid;
        private String hospital;
        private String hosp_level;
        private String subject;
        private String job;
        private String synopsis;
        private String photo;
        private String medal;
        private String cored;

        private String huanxin_username;
        private String huanxin_password;

        private String unreplySubject;
        private String mobileSubject;
        private String stat;
        private String points;
        private String h_num;
        private String phone;

        private String school;
        private String profession;

        private String username;
        private String zxzhsh;
        private String jsdh;
        private String majorfirst;
        private String hos_name;
        private String openid;

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getHos_name() {
            return hos_name;
        }

        public void setHos_name(String hos_name) {
            this.hos_name = hos_name;
        }

        public String getMajorfirst() {
            return majorfirst;
        }

        public void setMajorfirst(String majorfirst) {
            this.majorfirst = majorfirst;
        }

        public String getJsdh() {
            return jsdh;
        }

        public void setJsdh(String jsdh) {
            this.jsdh = jsdh;
        }

        public String getZxzhsh() {
            return zxzhsh;
        }

        public void setZxzhsh(String zxzhsh) {
            this.zxzhsh = zxzhsh;
        }

        public int getAudit_status() {
            return audit_status;
        }

        public void setAudit_status(int audit_status) {
            this.audit_status = audit_status;
        }

        /**认证状态
         *  -1 => '审核中', -2 => '驳回',
         *    0 => '待审核', 1 => '通过', 2 => '不通过',
         *   3 => '待跟踪', 4 => '暂不开通',
         */
        private int audit_status;

        public int getImwd() {
            return imwd;
        }

        public void setImwd(int imwd) {
            this.imwd = imwd;
        }

        private int imwd;   //是否开通IM问答  -1未开通 0 待审核 1开通 2 关闭

        private ClinicStatInfo xiaozhan = new ClinicStatInfo();
        private String subjectName;




        private String jixiao; //昨日收入

        public String getJixiao() {
            return jixiao;
        }

        public void setJixiao(String jixiao) {
            this.jixiao = jixiao;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public double getIncome() {
            return income;
        }

        public void setIncome(double income) {
            this.income = income;
        }

        private String balance; //累计收入
        private double income; //本月收入

        public String getTop() {
            return top;
        }

        public void setTop(String top) {
            this.top = top;
        }

        private String top;//收入排名

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getIsjob() {
            return isjob;
        }

        public void setIsjob(String isjob) {
            this.isjob = isjob;
        }

        public String getIsdoctor() {
            return isdoctor;
        }

        public void setIsdoctor(String isdoctor) {
            this.isdoctor = isdoctor;
        }

        public String getApproveid() {
            return approveid;
        }

        public void setApproveid(String approveid) {
            this.approveid = approveid;
        }

        public String getHospital() {
            return hospital;
        }

        public void setHospital(String hospital) {
            this.hospital = hospital;
        }

        public String getHosp_level() {
            return hosp_level;
        }

        public void setHosp_level(String hosp_level) {
            this.hosp_level = hosp_level;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getSynopsis() {
            return synopsis;
        }

        public void setSynopsis(String synopsis) {
            this.synopsis = synopsis;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getMedal() {
            return medal;
        }

        public void setMedal(String medal) {
            this.medal = medal;
        }

        public String getCored() {
            return cored;
        }

        public void setCored(String cored) {
            this.cored = cored;
        }

        public String getHuanxin_username() {
            return huanxin_username;
        }

        public void setHuanxin_username(String huanxin_username) {
            this.huanxin_username = huanxin_username;
        }

        public String getHuanxin_password() {
            return huanxin_password;
        }

        public void setHuanxin_password(String huanxin_password) {
            this.huanxin_password = huanxin_password;
        }

        public String getUnreplySubject() {
            return unreplySubject;
        }

        public void setUnreplySubject(String unreplySubject) {
            this.unreplySubject = unreplySubject;
        }

        public String getMobileSubject() {
            return mobileSubject;
        }

        public void setMobileSubject(String mobileSubject) {
            this.mobileSubject = mobileSubject;
        }

        public String getStat() {
            return stat;
        }

        public void setStat(String stat) {
            this.stat = stat;
        }

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }

        public String getH_num() {
            return h_num;
        }

        public void setH_num(String h_num) {
            this.h_num = h_num;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public String getProfession() {
            return profession;
        }

        public void setProfession(String profession) {
            this.profession = profession;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public ClinicStatInfo getXiaozhan() {
            return xiaozhan;
        }

        public void setXiaozhan(ClinicStatInfo xiaozhan) {
            this.xiaozhan = xiaozhan;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        @Override
        public String toString() {
            return "UserData{" +
                    "pid='" + pid + '\'' +
                    ", token='" + token + '\'' +
                    ", realname='" + realname + '\'' +
                    ", count='" + count + '\'' +
                    ", isjob='" + isjob + '\'' +
                    ", isdoctor='" + isdoctor + '\'' +
                    ", approveid='" + approveid + '\'' +
                    ", hospital='" + hospital + '\'' +
                    ", hosp_level='" + hosp_level + '\'' +
                    ", subject='" + subject + '\'' +
                    ", job='" + job + '\'' +
                    ", synopsis='" + synopsis + '\'' +
                    ", photo='" + photo + '\'' +
                    ", medal='" + medal + '\'' +
                    ", cored='" + cored + '\'' +
                    ", huanxin_username='" + huanxin_username + '\'' +
                    ", huanxin_password='" + huanxin_password + '\'' +
                    ", unreplySubject='" + unreplySubject + '\'' +
                    ", mobileSubject='" + mobileSubject + '\'' +
                    ", stat='" + stat + '\'' +
                    ", points='" + points + '\'' +
                    ", h_num='" + h_num + '\'' +
                    ", phone='" + phone + '\'' +
                    ", school='" + school + '\'' +
                    ", profession='" + profession + '\'' +
                    ", username='" + username + '\'' +
                    ", xiaozhan=" + xiaozhan +
                    ", subjectName='" + subjectName + '\'' +
                    '}';
        }
    }
}