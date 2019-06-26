package com.xywy.askforexpert.appcommon.net;

import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.ApiConstants;
import com.xywy.askforexpert.module.main.subscribe.ChannelUtil;

/**
 * 地址管理
 *
 * @author SHR
 * @2015-5-5上午10:12:31
 */
public class CommonUrl {

    /**
     * 基准URL
     */
    public static final String BASE_HOST = (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)?BuildConfig.APP_BASE_URL_YYZS:BuildConfig.APP_BASE_URL_YM);

    public static final String WWS_XYWY_BASE_URL = BuildConfig.WWS_XYWY_BASE_URL;

    public static final String TV_XYWY_BASE_URL = BuildConfig.TV_XYWY_BASE_URL;

    /**
     * 固定版本地址
     */
    public static final String ModleUrl1_4 = BASE_HOST + "/app/"+ 1.4+"/";
    public static final String ModleUrl = BASE_HOST + "/app/"+ ApiConstants.API_VERSION+"/";


    public static final String DISCOVER_MALL = "http://yimai.api.xywy.com/app/other/index.interface.php?a=duiba&m=duibaurl&userid=";

    /**
     * 医学试题 H5 接口(切换线上时务必更改此接口)
     */
    public static final String H5_EXAM = "http://yimai.api.xywy.com/app/other/index.interface.php?a=medicine&m=paperclass&userId=";

    public static final String INVITE_MONEY = BASE_HOST + "/app/other/index.interface.php?a=invitation&m=show&userid=";

    /**
     * 邀请发钱啦 接口域名
     */
    public static final String HOST_URL = "http://yimai.api.xywy.com/app/other/index.interface.php?";

    public static final String DP_COMMON = ModleUrl + "club/doctorApp.interface.php?";

    public static final String DP_COMMON_QUE_NEW = ModleUrl + "club/questionApp.interface.php?";

    public static final String DP_TEL = ModleUrl + "club/phoneApp.interface.php?";

    public static final String DP_MAKE = ModleUrl + "club/yuyueApp.interface.php?";

    public static final String IMAGE_FOUCE = DP_COMMON + "command=banner";

    public static final String FOLLOW_LIST = ModleUrl + "index.interface.php?";

    public static final String ANONYMOUS_INTRO = "http://yimai.api.xywy.com/app/other/famousdoctor.php?name=";

    public static final String NEWS_LIST_URL = ModleUrl + "zixun/index.interface.php";


    /**
     * 400呼叫Url
     */
    public static final String CALL_URL = ModleUrl + "index.interface.php";

    /**
     * 试题部分请求url
     */
    public static final String ANSWER_BASE_URL = ModleUrl + "medicine/index.interface.php";

    /**
     * 签到 信息
     */
    public static final String QDINFO_URL = ModleUrl + "club/qdApp.interface.php?";
    /**
     * 晋级申请上传证件
     */
    public static final String PRO_UPLOAD_IDCARD = DP_COMMON
            + "command=save_smrenz";

    /**
     * 专家医生设置回复区开关
     */
    public static final String QUE_EXPERT_OPEN = DP_COMMON
            + "command=expertOpen";

    /**
     * 纠正科室
     */
    public static final String QUE_EDIT_SUB = DP_COMMON
            + "command=editSub";

    /**
     * 关闭答题服务
     */
    public static final String QUE_CLOSE_DATI = DP_COMMON
            + "command=expertOpen";

    /**
     * 消息小助手
     */
    public static final String MSG_ZHU_SHOU = DP_COMMON
            + "command=msgList";
    /**
     * 钱包接口
     */
    public static final String MY_PURSE = DP_COMMON + "command=bill";
    /**
     * 预约加号
     */
    public static final String MAKE_ADD_NUM_TITLE = DP_MAKE
            + "command=ordermenu";
    /**1628
     * 预约加号列表
     */
    public static final String MAKE_ADD_NUM_LIST = DP_MAKE
            + "command=orderlist";

    /**
     * 预约加号提交
     */
    public static final String MAKE_ADD_NUM_SUB = DP_MAKE + "command=ordersub";
    /**
     * 问题广场title接口
     */

    public static final String QUE_TITLE = DP_COMMON + "command=quesMenu";
    /**
     * 诊所
     */

    public static final String ZHEN_SUO = DP_MAKE + "command=zhensuo";

    /**
     * 模板编辑
     */

    public static final String QUE_EDIT = DP_COMMON + "command=templateList";

    /**
     * 模板删除
     */

    public static final String QUE_EDIT_DELETE = DP_COMMON
            + "command=templateDel";

    /**
     * 模板添加
     */

    public static final String QUE_EDIT_ADD = DP_COMMON + "command=templateAdd";
    /**
     * 晋级申请
     */
    public static final String QUE_PROMOTION = DP_COMMON + "command=promotion";

    /**
     * 问题列表
     */
    public static final String QUE_CONTENT_LIST = DP_COMMON
            + "command=quesList";
    /**
     * 问题解锁
     */
    public static final String QUE_UNLOCK = DP_COMMON_QUE_NEW
            + "command=quesUnlock";
    /**
     * 网站问题
     */
    public static final String QUE_NEW_DETAIL = DP_COMMON_QUE_NEW + "command=quesList";

    /**
     * 电话医生列表
     */
    public static final String QUE_TEL_LIST = DP_TEL + "command=orderlist";
    /**
     * 电话医生是否开通
     */
    public static final String QUE_OPEN_TEL = DP_TEL + "command=phonedoc";
    /**
     * 电话医生列表详情
     */
    public static final String QUE_TEL_DETAIL = DP_TEL + "command=orderdetail";
    /**
     * 投诉帖子
     */
    public static final String QUE_OTHER_REASON = DP_COMMON + "command=tousu";

    /**
     * 找回密码
     */
    public static final String FIND_PWD = DP_COMMON + "command=backPwd";
    /**
     * 绑定手机号
     */
    public static final String BIND_PHONE = DP_COMMON + "command=bindPhone";
    /**
     * 发送验证码
     */
    public static final String SEND_CODE = DP_COMMON + "command=sendCode";
    /**
     * 注册
     */
    public static final String REGISTER_URL = DP_COMMON + "command=register";
    /**
     * 问题详情
     */
    public static final String QUE_CONTENT_DETAIL = DP_COMMON
            + "command=quesDetail";
    /**
     * 跳过
     */
    public static final String QUE_SKIP = DP_COMMON + "command=skip";
    /**
     * 我的回复
     */
    public static final String QUE_MY_REPLY = DP_COMMON + "command=myreply";
    /**
     * 绩效
     */
    public static final String QUE_JIXIAO = DP_COMMON + "command=jixiao";
    /**
     * 发送回复
     */
    public static final String QUE_SEND_REPLY = DP_COMMON + "command=replySave";

    /**
     * 发送追问回复
     */
    public static final String QUE_SEND_REPLY_ZHUI = DP_COMMON
            + "command=replyZhuiwen";


    /**
     * 资讯类
     */
    public static final String Consulting_Url = ModleUrl
            + "zixun/index.interface.php";
    /**
     * 药典
     */
    public static final String Codex_Url = ModleUrl + "shouce/index.interface.php";

    /**
     * 家庭医生服务
     */
    public static final String HOMEDOCTOR_LIST = ModleUrl
            + "family/family.interface.php";
    /**
     * 招聘中心
     */
    public static final String Recruit_Center_Url = ModleUrl
            + "zhaopin/index.interface.php";
    /**
     * 我的投递过的职位
     */
    public static final String MyResume_Send_Url = ModleUrl
            + "zhaopin/returned.interface.php";
    /**
     * 我的招聘收藏过的职位
     */
    public static final String MyResume_Save_Url = ModleUrl
            + "zhaopin/jobcollection.interface.php";
    public static final String Recruit_toudi_Url = ModleUrl + "zhaopin/deliverresume.interface.php";
    public static final String Recruit_Serch_Url = ModleUrl + "zhaopin/searchTitle.interface.php";
    /**
     * 搜长
     */
    public static final String Recruit_Coll_Url = ModleUrl + "zhaopin/shoucang.interface.php";
    public static final String DIRE = "xywy";
    public static final String SUFFIX = ".jpg.xywy";
    public static final String TAG = "Dplatform";

    /**
     * 医圈列表
     ***/
    public static final String doctor_circo_url = ModleUrl + "index.interface.php";
    /**
     * 患者管理
     */
    public static final String Patient_Manager_Url = ModleUrl + "index.interface.php";
    /**
     * 我的诊所
     */
    public static final String MyClinic_State_Url = ModleUrl
            + "club/yuyueApp.interface.php";
    /**
     * 家庭医生开通
     */
    public static final String MyClinic_Fam_Url = ModleUrl
            + "family/open.interface.php";
    /**
     * 电话医生
     */
    public static final String MyClinic_Phone_Url = ModleUrl
            + "club/phoneApp.interface.php";
    /***
     * 医生的一天
     **/
    public static final String doctorOneDay = "http://club.xywy.com/doctorDay/web/";
    /***
     * 收藏
     **/
    public static final String connetUrl = ModleUrl + "shouce/index.interface.php";
    public static final String zixun = ModleUrl + "zixun/index.interface.php";

    /***
     * 版本跟新
     **/
    public static final String TEST_CLUB_URL = "http://test.api.club.xywy.com/doctorApp.interface.php?";
    public static final String CLUB_URL = "http://api.club.xywy.com/doctorApp.interface.php?";
    public static final String UpdataUrl = CLUB_URL + "command=update_versions&from=" + ChannelUtil.getChannel(YMApplication.getAppContext(), "xywypc");

    public static final String UpdataImgUrl = "http://api.club.xywy.com/doctorApp.interface.php";

    public static final String wenxian = ModleUrl + "index.interface.php?";
//	public static final String wenxian ="http://test.yimai.api.xywy.com/app/1.1/" + "index.interface.php?";

    /**
     * 积分详情
     */
    public static final String ScoresPointUrl = ModleUrl + "club/pointApp.interface.php";

    /**
     * 专家认证接口地址
     */
//	public static final String ExpertApprovUrl= "http://test.z.xywy.com/club_doctor.php?action=add_expert_verify&source=club";
    public static final String ExpertApprovUrl = "http://api.zhuanjia.xywy.com/club_doctor.php?action=add_expert_verify&source=club";


}
