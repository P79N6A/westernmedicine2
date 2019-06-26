package com.xywy.askforexpert.module.consult;


import com.xywy.askforexpert.BuildConfig;

/**
 * Created by zhangzheng on 2017/4/26.
 */

public class ConsultConstants {
    //接口相关常量
    public static final String BASE_URL = BuildConfig.WWS_XYWY_BASE_URL;
    public static final String METHOD_GET_DOCTOR_INFO = "doctor/doctorCommon/info";
    public static final String METHOD_GET_ZXZS_READ_NUM = "imask/getDoctor/noReadnum";
    public static final String METHOD_GET_QUESTION_IN_HANDLE = "rtqa/question/recent";
    public static final String METHOD_GET_QUESTION_POOL = "rtqa/question/pool";
    public static final String METHOD_GET_JSDH_COOKIE_POOL = BuildConfig.JSDH_BASE_DATA_URL;
    public static final String METHOD_GET_JSDH_URL = BuildConfig.JSDH_URL;



    //在线医院聊天记录
    public static final String METHOD_GET_ONLINE_QUESTION_MSG_LIST = "imask/getDoctor/ques_detail";
    public static final String METHOD_GET_JDSH_COOKIE_LIST = "api-doctor/set-doc-cookie";
    public static final String METHOD_GET_PATIENT_QUESTION_MSG_LIST = "imask/getQues/quesCard";

    public static final String METHOD_GET_QUESTION_MSG_LIST = "rtqa/question/getmsg";
    public static final String METHOD_BACK_QUESTION = "rtqa/question/skip";
    public static final String METHOD_ADOPT_QUESTION = "rtqa/question/claim";
    public static final String METHOD_OBTAIN_QUESTION = "rtqa/question/rob";
    public static final String METHOD_READ_QUESTION = "rtqa/question/readmsg";
    public static final String METHOD_TIP_OFF_QUESTION = "rtqa/question/report";
    public static final String METHOD_ANSWERED_QUESTION = "rtqa/question/finish";
    public static final String METHOD_PATIENT_MANAGER_LIST = "club3g/patient/reportPatient";
    public static final String METHOD_PATIENT_GROUPING_LIST = "club3g/patient/patients";
    public static final String METHOD_PATIENT_DELETE = "club3g/patient/patientDel";
    public static final String METHOD_BATCH_NOTICE_LIST = "club3g/notify/getDocTem";
    public static final String METHOD_PATIENT_GROUP_MANAGE_LIST = "club3g/group/getDroup";
    public static final String METHOD_BATCH_NOTICE_CONTENT = "club3g/notify/delTem";
    public static final String METHOD_GET_ADDRESS = "club3g/address/getAddress";
    public static final String METHOD_ADD_ADDRESS = "club3g/address/addOrEdit";
    public static final String METHOD_APPLY = "club3g/address/apply";
    public static final String METHOD_DELETE_GROUP = "club3g/group/delDroup";
    public static final String METHOD_PATIENT_LIST = "club3g/patient/groupPatient";
    public static final String METHOD_ADD_BATCH_NOTICE_CONTENT = "club3g/notify/operTem";
    public static final String METHOD_SEND_BATCH_NOTICE_CONTENT = "club3g/notify/batchSend";
    public static final String METHOD_ADD_GROUP = "club3g/group/addDroup";
    public static final String METHOD_ADD_PATIENT = "club3g/patient/batchAddPatient";
    public static final String METHOD_CHANGE_DEPARTMENT = "rtqa/question/referral";
    public static final String METHOD_UNSUMUP_LIST = "rtqa/question/un_conclusion";
    public static final String METHOD_NEW_MESSAGE_NUM = "rtqa/doctor/no_read_num";//即时问答消息数 stone
    public static final String METHOD_NEW_JSDH_NUM = "jsdh/question/total";//即时问答消息数 stone
    //医院上传图片
    public static final String METHOD_UOLOADIMAGE_PATH = ConsultConstants.BASE_URL+"common/uploadImg/index";

    //stone 添加总结 移植于yimai
    public static final String METHOD_DOCTOR_SUMMARY = "imask/doctor/summary";

    //websocket 收到消息类型常量
//    public static final int RECV_MSG_TYPE_ASK = 101001;    //患者提问
//    public static final int RECV_MSG_TYPE_NORMAL = 101002;    //患者追问
//    public static final int RECV_MSG_TYPE_QUESTION_OVER_TIME = 101003;    //问题超时
//    public static final int RECV_MSG_TYPE_CLOSED = 101004;    //问题已关闭
//    public static final int RECV_MSG_TYPE_ACCEPT_OVER_TIME = 101005;  //医生接诊超时
//    public static final int RECV_MSG_TYPE_UN_ADOPT = 101006;  //未认领
//    public static final int RECV_MSG_TYPE_DOCTOR_FORBID = 101007; //医生被停止服务
//    public static Map<Integer, String> RECV_ERROR_MSG_TIP;
//
//    static {
//        RECV_ERROR_MSG_TIP = new HashMap<>();
//        RECV_ERROR_MSG_TIP.put(Integer.valueOf(RECV_MSG_TYPE_QUESTION_OVER_TIME), "问题超时");
//        RECV_ERROR_MSG_TIP.put(Integer.valueOf(RECV_MSG_TYPE_CLOSED), "问题已关闭");
//        RECV_ERROR_MSG_TIP.put(Integer.valueOf(RECV_MSG_TYPE_ACCEPT_OVER_TIME), "接诊超时");
//        RECV_ERROR_MSG_TIP.put(Integer.valueOf(RECV_MSG_TYPE_UN_ADOPT), "未认领");
//        RECV_ERROR_MSG_TIP.put(Integer.valueOf(RECV_MSG_TYPE_DOCTOR_FORBID), "医生被停止服务");
//    }


    public static final String WEBSOCKET_ADDRESS = BuildConfig.WEBSOCKET_URL;

    public static final String VERSION_1_0 = "1.0";
    public static final int CODE_REQUEST_SUCCESS = 10000;
    public static final int CODE_EMPTY_DATA=30000;

    public static final int CHAT_SEND_FAILED_TIME_LIMITED=2000;

    //打点 stone 新加
    public static final String EVENT_MY_CONSULT="Myconsultingquestion"; //我的咨询
    public static final String EVENT_QUESTION_POOL="consultinglistquestion";    //问题库
    public static final String EVENT_QUESTION_CLAIM="OCclaim";  //认领
    public static final String EVENT_QUESTION_OBTAIN="OCGrabtheconsulting"; //抢题
    public static final String EVENT_QUESTION_SKIP="OCskip";    //跳过
    public static final String EVENT_QUESTION_TIP_OFF="Ocinform";   //举报
    public static final String EVENT_QUESTION_SWITCH="OCreferral";  //转诊
    public static final String EVENT_QUESTION_SUM_UP="OCconclusion";    //总结
    public static final String EVENT_QUESTION_COMMENT="OCevaluation";   //鼓励评价
    public static boolean LOG_OUT_FALG=false;   //鼓励评价

    public static final String EVENT_QUESTION_HISTORY_CONCLUSION="oclishihuifuconclusion";   //历史回复中的问诊总结
    public static final String EVENT_QUESTION_CONCLUSION_FINISHED="Ocyizhufinished";   //问诊总结完成
    public static final String EVENT_YIMAIPUSH="yimaipush";   //推送打开
    public static final String JSDH_URL = "http://m.d.jisudianhua.xywy.com/question/pool";
}
