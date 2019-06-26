package com.xywy.askforexpert.appcommon.old;

import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.YMApplication;

/**
 * 常量 stone
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/2/25 16:14
 */
public class Constants {
    public static final String MD5_KEY = BuildConfig.md5_key;



    /**
     * umeng share
     */
    public static final String WEIXIN_S1 = "wx7adc9ead4e1de7ef";
    public static final String WEIXIN_S2 = "c2958650c92241d801f105a2d1eaa4d9";
    public static final String WEIXIN_S1_YSZS = "wx7603c9db8d1729da";
    public static final String WEIXIN_S2_YSZS = "2bc02c3d953e3f99db7bede0cb0fbf9e";
    public static final String WEIBO_S1 = "3911872859";
    public static final String WEIBO_S2 = "8db2d8d411591fa567153f48e4de88cc";
    //闻康医生助手友微博的App Key
    public static final String WEIBO_S1_YSZS = "374723487";
    //闻康医生助手微博的的App Secret
    public static final String WEIBO_S2_YSZS = "ca1d800e2141468de7cddcdf113ab49f";
    public static final String QZONE_S1 = "1101752729";
    public static final String QZONE_S2 = "LgrXeXE27j0PFDlU";
    //医脉友盟统计的appKey
    public static final String APP_ID = "5563db1e67e58e72a50009a5";
    //闻康医生助手友盟统计的appKey
    public static final String APP_ID_YSZS = "5a0518b68f4a9d0484000091";


    /**
     * 资讯列表 普通
     */
    public static final int NEWS_LIST_NORMAL = 0;

    /**
     * 资讯列表 3张图模式
     */
    public static final int NEWS_LIST_3_PICS = 1;

    /**
     * 话题id匹配
     */
    public static final String TOPIC = "\\$([0-9]+)\\$ ";

    /**
     * 服务器返回的带有id的话题整体匹配
     */
    public static final String TOPIC_REGEX = "#([^#]+?)\\$([0-9]+)\\$ #";

    //医脉的图片url
    public static final String YM_IMG_YRL = "http://static.img.xywy.com/club/ypt_app/app-logo-512-512.jpg";
    //闻康医生助手的图片url
    public static final String YSZS_IMG_YRL = "http://static.img.xywy.com/3g_club_patient/images/logo-512.png";

    //找工作url
//    public static final String FIND_JOB_URL = "http://m.9453job.com";
    public static final String FIND_JOB_URL = "http://m.9453job.com?from=ym"; //5.4.0 新版做的修改

    // TODO: 2018/4/11 测试stone 家庭医生协议url 未给
    public static final String FAMILY_PROTOCOL_URL = "http://yimai.api.xywy.com/app/1.7/club/family.html";

    /**
     * 默认分享图片url
     */
    public static final String COMMON_SHARE_IMAGE_URL = (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) ? YSZS_IMG_YRL : YM_IMG_YRL;

    /**
     * 创建话题
     */
    public static final int CREATE_TOPIC = 0;

    /**
     * 编辑话题
     */
    public static final int EDIT_TOPIC = 1;

    /**
     * 医圈举报
     */
    public static final int REPORT_STYLE_CIRCLE = 1;

    /**
     * 话题举报
     */
    public static final int REPORT_STYLE_TOPIC = 2;

    /**
     * 区域医疗患者环信id标识
     */
    public static final String QXYL_USER_HXID_MARK = "qyylxtid_";

    public static final int MAX_NEWS_COMMENT_INPUT = 150;


    /**
     * 媒体号
     */
    public static final int TYPE_MEDIA = 3;

    /**
     * 服务号
     */
    public static final int TYPE_SERVICE = 1;


    //key值 start
    public final static String KEY_POS = "pos";
    public final static String KEY_VALUE = "value";
    public final static String KEY_ID = "id";
    public final static String KEY_TYPE = "type";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static final String KEY_URL = "url";
    public static final String KEY_PROVINCE = "province";
    public static final String KEY_CITY = "city";
    //key值 end

    //stone 新添加的问诊总结 问题id question_id 总结内容1 sumup_data1 总结内容2 sumup_data2  移植于yimai
    public static final String QUESTION_ID = "question_id";
    public static final String SUMUP_DATA1 = "sumup_data1";
    public static final String SUMUP_DATA2 = "sumup_data2";
    //stone im即时问答快捷回复
    public static final String IM_FAST_REPLY_CONTENT = "im_fast_reply_content";

    //分割线
    public static final String DIVIDERS = "—";

    //stone 请求码 往上变小 往下变大 start
    /**
     * 擅长疾病(申请家庭医生)
     */
    public static final int REQUESTCODE_GOOD_AT_FOR_FAMILY_DOCTOR = 90;
    /**
     * 服务时间
     */
    public static final int REQUESTCODE_SERVICE_PRICE = 91;
    /**
     * 服务时间
     */
    public static final int REQUESTCODE_SERVICE_TIME = 92;
    /**
     * 个人荣誉
     */
    public static final int REQUESTCODE_PERSONAL_HONOR = 93;
    /**
     * 医生寄语
     */
    public static final int REQUESTCODE_DOCTOR_HOPE = 94;
    /**
     * 选择省市
     */
    public static final int REQUESTCODE_GO_CHOOSE_PROVINCE_CITY = 95;
    /**
     * 去上传图片
     */
    public static final int REQUESTCODE_GO_UPLOAD_PHOTO = 96;
    /**
     * 所属科室
     */
    public static final int REQUESTCODE_DEPARTMENT = 97;
    /**
     * 在职医院
     */
    public static final int REQUESTCODE_HOSPITAL = 98;
    /**
     * 临床职称
     */
    public static final int REQUESTCODE_JOBTITLE = 99;
    /**
     * 擅长疾病
     */
    public static final int REQUESTCODE_GOOD_AT = 100;
    /**
     * 个人简介
     */
    public static final int REQUESTCODE_PERSONAL_DESC = 101;
    /**
     * 本地图片选取标志
     */
    public static final int REQUESTCODE_CHOOSE_IMG = 102;
    /**
     * 截取结束标志
     */
    public static final int REQUESTCODE_MODIFY_FINISH = 103;
    /**
     * 相机标志
     */
    public static final int REQUESTCODE_CHOOSE_CAMERA = 104;

    //服务的开通状态
    public static final String FUWU_AUDIT_STATUS_NO = "-1";//未开通
    public static final String FUWU_AUDIT_STATUS_0 = "0";//待审核(审核中)
    public static final String FUWU_AUDIT_STATUS_1 = "1";//已开通
    public static final String FUWU_AUDIT_STATUS_2 = "2";//关闭 (审核未通过)   审核未通过，就是给的2
    public static final String FUWU_AUDIT_STATUS_3 = "3";//暂时关闭

    public static final String SP_KEY_NIGHT_MODE = "night_mode";

    //申请家庭医生 stone
    public static final String APPLY_FOR_FAMILY_DOCTOR = "apply_for_family_doctor";
    //im 发图片 stone
    public static final String IM_CHAT = "im_chat";
    //im 发图片 stone
    public static final String ONLINE_CHAT = "online_chat";
    public static final String PATIENT_CHAT = "patient_chat";
    //荣誉展示
    public static final String HONOR_SHOW = "honor_show";
    //个人风采
    public static final String PERSONAL_STYLE = "personal_style";
    //用户id命名的共享文件数据保存的key
    public static final String STR_SHOW = "str_show";//上传成功后返回的图片地址 用|连接
    public static final String STR_SHOW_PATH = "str_show_path";//本地图片路径 用,连接
    public static final String STR_SHOW_SAVE = "str_show_save";//上传成功后返回的图片地址 用,连接
    public static final String STR_STYLE = "str_style";
    public static final String STR_STYLE_PATH = "str_style_path";
    public static final String STR_STYLE_SAVE = "str_style_save";

    public static final String OPEN = "已开通";
    public static final String UNDER_REVIEW = "审核中";
    public static final String REFUSE = "未通过";

    public static final String CLUB_CN = "问题广场";
    public static final String JSDH_CN = "极速电话";
    public static final String FAMILYDOCTOR_CN = "家庭医生";
    public static final String ZJZIXUN_CN = "专家咨询";
    public static final String JIAHAO_CN = "预约转诊";
    public static final String DHYS_CN = "电话医生";
    public static final String XIANXIA_CN = "患者管理";
    public static final String WKYS_CN = "闻康医生";
    public static final String IMWD_CN = "即时问答";
    public static final String CLUB_ASSIGN_CN = "问题广场(指定)";
    public static final String IMWD_REWARD_CN = "即时问答(悬赏)";
    public static final String IMWD_ASSIGN_CN = "即时问答(指定)";
    public static final String CLUB_REWARD_CN = "问题广场(悬赏)";
    public static final String MYGH_CN = "名医挂号";//名医挂号

    public static final String ZXZHSH_CN = "问诊用药";
    public static final String PATIENT_MANAGE_CN = "诊后患者";


    public static final String INTENT_KEY_ISCONFLICT = "isConflict";
    public static final String INTENT_KEY_ISCURRENTACCOUNTREMOVED = "isCurrentAccountRemoved";


    public static final String REMARK_UNAUTHENTICATION = "备注：问题广场（免费、悬赏），即时问答（免费）在认证通过后自动开通";
    public static final String REMARK_OPEN = "备注：问题广场（免费、悬赏），即时问答（免费）已开通";
    public static final String REMARK_UNOPEN = "备注：问题广场（免费、悬赏），即时问答（免费）有未开通的，请联系客服开通";
    public static final String AUTHENTICATION = "去认证";
    public static final String ANSWER = "去答题";
    public static final String CUSTOMER_SERVICE = "联系客服";
    public static final String APPLY_FOR_OPENING = "申请开通";
    public static final String APPLY = "已申请";
    public static final String CLOSED = "已关闭";


    public static final String CUSTOMER_SERVICE_OPEN = "联系客服开通";
    public static final String ENTER = "进入";
    public static final String REOPEN = "重新申请";
    public static final String PENDING = "审核中";

    public static final String INTENT_KEY_INCOME = "income";
    public static final String INTENT_KEY_BALANCE = "balance";
    public static final String INTENT_KEY_JIXIAO = "jixiao";

    public static final int TYPE_3 = 3;
    public static final int TYPE_2 = 2;

    public static final String ZERO_STR = "0";
    public static final int ZERO = 0;

    public static final String INTENT_KEY_TITLE = "title";

    public static final String CLUB_EN = "club";//问题广场
    public static final String FAMILYDOCTOR_EN = "familyDoctor";//家庭医生
    public static final String ZJZIXUN_EN = "zjzixun";//专家咨询
    public static final String JIAHAO_EN = "jiahao";//预约转诊
    public static final String DHYS_EN = "dhys";//电话医生
    public static final String XIANXIA_EN = "xianxia";//患者管理
    public static final String WKYS_EN = "wkys";//闻康医生
    public static final String IMWD_EN = "imwd";//即时问答
    public static final String CLUB_ASSIGN_EN = "club_assign";//问题广场(指定)
    public static final String IMWD_REWARD_EN = "imwd_reward";//即时问答(悬赏)
    public static final String IMWD_ASSIGN_EN = "imwd_assign";//即时问答(指定)
    public static final String CLUB_REWARD_EN = "club_reward";//问题广场(悬赏)
    public static final String MYGH_EN = "mygh";//名医挂号

    //离线留言 夜间留言的 type stone
    public static final String OFFLINE = "1";
    public static final String NIGHT = "2";
    //留言模式开启与否 stone
    public static final String OPENED = "1";
    public static final String CLOSEED = "0";

    //编辑还是删除 stone
    public static final String EDIT = "0";
    public static final String DELETE = "1";

    //stone 5.4.0新添加事件统计
    public static final String NEWS = "news";//首页-消息
    public static final String SIGNIN = "signin";//首页-签到
    public static final String MORESERVICE = "moreservice";//首页-更多服务
    public static final String FINDMORESERVICE = "findmoreservice";//首页-发现更多诊室
    public static final String APPLYFOROPENINGJSWDXS = "applyforopeningJSWDXS";//我的诊室-即时问答(悬赏)申请开通

    public static final String APPLYFOROPENINGWTGCZD = "applyforopeningWTGCZD";//我的诊室-问题广场(指定)申请开通
    public static final String APPLYFOROPENINGJSWDZD = "applyforopeningJSWDZD";//我的诊室-即时问答(指定)申请开通
    public static final String CERTIFICATION = "certification";//我的诊室-去认证
    public static final String APPLYFOROPENINGJTYS = "applyforopeningJTYS";//我的诊室-家庭医生申请
    public static final String OPENINGBYCUSTOMERSERVICEDHYS = "openingbycustomerserviceDHYS";//我的诊室-电话医生联系客服开通

    public static final String OPENINGBYCUSTOMERSERVICEYYZZ = "openingbycustomerserviceYYZZ";//我的诊室-预约转诊联系客服开通
    public static final String REAPPLY = "reapply";//审核未通过页-重新申请
    public static final String SUBMITANAPPLICATION = "Submitanapplication";//家庭医生服务申请页-提交申请
    public static final String MESSAGESETTING = "messagesetting";//即时问答-留言设置
    public static final String OPENOFFLINEMESSAGE = "openofflinemessage";//留言设置-开启离线留言

    public static final String OPENNIGHTMESSAGE = "opennightmessage";//留言设置-开启夜间留言
    public static final String MODIFYTHEMESSAGE = "modifythemessage";//留言设置-修改
    public static final String CLOSETHEMESSAGE = "closethemessage";//留言设置-关闭
    public static final String VIEWNEWMESSAGES = "viewnewmessages";//及时问答-新消息查看
    public static final String HOTTOPIC = "hottopic";//医圈-热门话题

    public static final String ONEDAYOFCHINESEDOCTOR = "onedayofchinesedoctor";//医圈-中国医生的一天
    public static final String FOCUSONWECHATPUBLIC = "focusonWeChatpublic";//我的-关注微信公共号
    public static final String NIGHTMODO = "nightmodo";//我的-夜间模式
    public static final String IMMEDIATEATTENTION = "Immediateattention";//关注微信公众号-立即关注

    public static final double DOUBLE_0 = 0.00;
    public static final double DOUBLE_1 = 1.00;
    public static final double DOUBLE_2 = 2.00;
    public static final double DOUBLE_3 = 3.00;
    public static final double DOUBLE_4 = 4.00;
    public static final double DOUBLE_5 = 5.00;
    public static final double DOUBLE_6 = 6.00;
    public static final double DOUBLE_7 = 7.00;
    public static final double DOUBLE_8 = 8.00;
    public static final double DOUBLE_9 = 9.00;
    public static final double DOUBLE_10 = 10.00;

    public static final String DOCTORGETREWARD_STR = "医生获得用户";//关注微信公众号-立即关注
    public static final String REWARD_STR = "打赏";//关注微信公众号-立即关注
    public static final String CRAD_NUMBER_STR = "卡卡号";
    public static final String CRAD_INSTRUCTION_ONE_STR = "1、寻医问药网目前只支持中国农业银行借记卡支付工资";
    public static final String CRAD_INSTRUCTION_TWO_STR = "2、请提供医生本人银行卡，非本人银行卡无法发放绩效";
    public static final String CRAD_NUM_MSG_STR = "请填写农业银行卡号";
    public static final String CRAD_NUM_MSG_STR_19 = "请填写19位农行借记卡卡号";
    public static final String CRAD_ADDRESS_MSG_STR = "请填写开户行";
    public static final String CRAD_NAME_MSG_STR = "请填写开户人";
    public static final String CRAD_IDENTIFY_MSG_STR = "请填写身份证";
//    public static final String GET_BANK_CARD_STATE = "getBankCardState";
//    public static final String BIND_BANK_CARD = "bindBankCard";
    public static final String SUCCESS = "success";
    public static final String ERROR = "error";



    public static final String SUCCESS_CN = "成功";
    public static final String CHECK_REFUSE = "审核不通过";
    public static final String COMMIT_NEVER = "未提交过";
    public static final String HS_READ = "hs_read";

    public static final String CONSULTING_ROOM_ONLINE = "问诊用药";
    public static final String EIGHT_STR = "8";
    public static final String OTHER = "其他";
    public static final String INTENT_KEY_MONTH = "month";
    public static final String INTENT_KEY_TYPE = "type";
    public static final String BILL_DETAIL = "绩效明细";

    public static final String LEVEL = "level";

    public static final String INTENT_KEY_NAME = "name";
    public static final String INTENT_KEY_SEX = "sex";
    public static final String INTENT_KEY_YEAR = "year";
    public static final String INTENT_KEY_DAY = "day";
    public static final String INTENT_KEY_DATE = "date";
    public static final String INTENT_KEY_DEPARTMENT  = "department";
    public static final String INTENT_KEY_UID  = "uid";
    public static final String INTENT_KEY_TIME  = "time";
    public static final String INTENT_KEY_QID  = "questionid";
    public static final String INTENT_KEY_SOURCE  = "Source";
    public static final String YWQ_CLIENTID  = BuildConfig.YWQ_CLIENTID;

    public static final String BSID_RTQA  = "rtqa";



}
