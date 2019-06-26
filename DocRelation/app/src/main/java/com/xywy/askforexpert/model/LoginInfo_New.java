package com.xywy.askforexpert.model;

import java.io.Serializable;
import java.util.List;

/**
 * 登录成功后返回的数据/个人信息接口返回的数据 stone
 */
public class LoginInfo_New implements Serializable {
    /**
     * id : 436365
     * old_id : 0
     * user_id : 68258701
     * expert_id : 0
     * isdomain : 0
     * ispublish : 0
     * isauto : 1
     * audit_status : -1
     * status : 51
     * phone : 18600784173
     * real_name : 刘念
     * gender : 女
     * photo : http://xs3.op.xywy.com/api.iu1.xywy.com/yishengku/20171108/39717f9f3d1a80a342ab820dec6b4f2467662.png
     * clinic : 3
     * be_good_at : 在于我们个人认为自己做一点
     * introduce : 看看
     * hospitalid : 2
     * hos_name : 北京协和医院
     * province : 11
     * city : 1101
     * rank : 0
     * major_first : 0
     * major_second : 0
     * details : {"user_type":1,"doctor_type":"","gender":"女","clinic":"主治医师","hos_name":"北京协和医院","rank":"","province":"北京市","city":"东城区","major_first":"","major_second":"","subject_first":"内科","subject_second":"内分泌","subject_phone":"010-7220720","hospital_type":0,"job_card":0,"zhy_image":"http://xs3.op.xywy.com/club.xywy.com/doc/20180206/b001908e9363bc.jpg|http://xs3.op.xywy.com/club.xywy.com/doc/20180206/04e4196cd0840b.jpg|http://xs3.op.xywy.com/club.xywy.com/doc/20180206/098e53fd859d60.jpg","zhch_image":"http://xs3.op.xywy.com/club.xywy.com/doc/20180206/e52d50c5b70445.jpg|http://xs3.op.xywy.com/club.xywy.com/doc/20180206/cbadbb4f47c795.jpg","zg_image":"","shf_image":"http://xs3.op.xywy.com/club.xywy.com/doc/20180206/9825cd219d23c1.jpg|http://xs3.op.xywy.com/club.xywy.com/doc/20180206/f6840207efb04a.jpg","remark":""}
     * card_image :
     * certificate_image :
     * create_time : 1510128268
     * hx_username : did_68258701
     * hx_password : 5ee2d18c10041163128f51f70f01e2eb
     * hx_token : 4492cf611aa0c79e594991c6f74d6d61
     * qrcode :
     * gold : 0
     */
    //接口请求多出来的部分 stone
    public int gold;

    //原来文档上的
    public int id;              //资料id
    public int old_id;          //原数据id
    public int user_id;              //医生id
    public int expert_id;            //专家id
    public int isdomain;            //是否有个人域名 0 无 1有
    public int ispublish;           //是否发布 0未发布 1已发布
    public int isauto;              //是否自动保存
    public int audit_status;        //审核状态 1.通过
    public int status;              //合作状态 51正常 -1停止合作
    public long phone;              //手机号
    public String real_name;       //真实姓名
    public String gender;       //性别
    public String photo;        //头像
    public int clinic;          //临床职称id
    public String be_good_at;       //擅长主治
    public String introduce;        //简介
    public int hospitalid;          //医院id
    public String hos_name;         //医院名称
    public int province;            //省id
    public int city;                //市id
    public int rank;                //医院等级id

    //解析错误 后台传的"" 而不是整数 stone
//    public int major_first;         //一级专业方向
//    public int major_second;        //二级专业方向
    public String major_first;         //一级专业方向
    public String major_second;        //二级专业方向

    public String card_image;       //身份证
    public String certificate_image;       //执业证
    public int create_time;       //注册时间
    public int club;                //咨询 -1未开通 0待审核 1已开通 2关闭 3暂时关闭
    public int familyDoctor;        //家庭医生-1未开通 0待审核 1已开通 2关闭 3暂时关闭
    public int zjzixun;             //专家咨询-1未开通 0待审核 1已开通 2关闭 3暂时关闭
    public int jiahao;              //预约加号-1未开通 0待审核 1已开通 2关闭 3暂时关闭
    public int dhys;                //电话医生-1未开通 0待审核 1已开通 2关闭 3暂时关闭
    public int xianxia;             //患者管理-1未开通 0待审核 1已开通 2关闭 3暂时关闭
    public int wkys;                //在线医生售药-1未开通 0待审核 1已开通 2关闭 3暂时关闭
    public int imwd;                //是否开通IM问答 -1未开通 0 待审核 1开通 2 关闭
    public int club_assign;         //图文咨询-指定付费  -1未开通  0 待审核 1开通 2 关闭
    public int imwd_reward;         //IM问答-悬赏  -1未开通0 待审核 1开通 2 关闭
    public int imwd_assign;         //IM问答-指定付费  -1未开通0 待审核 1开通 2 关闭
    public int club_reward;         //图文咨询-悬赏    -1未开通   0 待审核 1开通 2 关闭
    public int service_id;                //售药服务id
    public String hx_username;      //环信用户名
    public String hx_password;      //环信密码
    public String hx_token;         //token
    public String qrcode;           //二维码
    public Details details;         //详情\
    public List<Service> services;

    public String jixiao; //昨日收入
    public String balance; //累计收入
    public double income; //本月收入
    public String top;//收入排名
    public String zxzhsh;//问诊用药
    public String jsdh;//极速电话
    public String openid;//处方SDK ID

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getZxzhsh() {
        return zxzhsh;
    }

    public void setZxzhsh(String zxzhsh) {
        this.zxzhsh = zxzhsh;
    }

    public class Details implements Serializable {
//认证过的
//                "user_type": 1,
//                "doctor_type": "",
//                "gender": "男",
//                "clinic": "主任医师",
//                "hos_name": "黑龙江中医药大学附属第二医院",
//                "rank": "三级甲等",
//                "province": "黑龙江省",
//                "city": "哈尔滨市",
//                "major_first": "皮肤性病科",
//                "major_second": "皮肤科",
//                "subject_name": "皮肤科",
//                "subject_first": "中医学",
//                "subject_second": "皮肤科"

        //未认证过的
        /**
         * user_type : 1
         * doctor_type :
         * gender : 女
         * clinic : 主治医师
         * hos_name : 北京协和医院
         * rank :
         * province : 北京市
         * city : 东城区
         * major_first :
         * major_second :
         * subject_first : 内科
         * subject_second : 内分泌
         * subject_phone : 010-7220720
         * hospital_type : 0
         * job_card : 0
         * zhy_image : http://xs3.op.xywy.com/club.xywy.com/doc/20180206/b001908e9363bc.jpg|http://xs3.op.xywy.com/club.xywy.com/doc/20180206/04e4196cd0840b.jpg|http://xs3.op.xywy.com/club.xywy.com/doc/20180206/098e53fd859d60.jpg
         * zhch_image : http://xs3.op.xywy.com/club.xywy.com/doc/20180206/e52d50c5b70445.jpg|http://xs3.op.xywy.com/club.xywy.com/doc/20180206/cbadbb4f47c795.jpg
         * zg_image :
         * shf_image : http://xs3.op.xywy.com/club.xywy.com/doc/20180206/9825cd219d23c1.jpg|http://xs3.op.xywy.com/club.xywy.com/doc/20180206/f6840207efb04a.jpg
         * remark :
         */
        //接口请求数据多出来的部分 stone
        public int user_type;
        public String subject_first;
        public String subject_second;
        public String subject_phone;
        public int hospital_type;
        public int job_card;
        public String zhy_image;
        public String zhch_image;
        public String zg_image;
        public String shf_image;
        public String remark;//被拒原因

        //原来文档上的
        public String doctor_type;  //医生类型
        public String gender;       //性别
        public String clinic;       //临床职称
        public String hos_name;       //医院名称
        public String rank;       //医院等级
        public String province;       //省
        public String city;       //市
        public String major_first;       //一级专业方向
        public String major_second;       //二级专业方向
        public String subject_name;       //实名科室

    }

}