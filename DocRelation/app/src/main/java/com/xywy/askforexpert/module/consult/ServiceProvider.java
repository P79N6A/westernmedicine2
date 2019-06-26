package com.xywy.askforexpert.module.consult;

import com.xywy.askforexpert.model.AddressBean;
import com.xywy.askforexpert.model.BatchNoticeContent;
import com.xywy.askforexpert.model.DeleteBatchNotice;
import com.xywy.askforexpert.model.DeleteGroup;
import com.xywy.askforexpert.model.GrouManageData;
import com.xywy.askforexpert.model.PatientGroupingData;
import com.xywy.askforexpert.model.PatientManagerData;
import com.xywy.askforexpert.model.consultentity.BackQuestionRspEntity;
import com.xywy.askforexpert.model.consultentity.CommonRspEntity;
import com.xywy.askforexpert.model.consultentity.DoctorInfoEntity;
import com.xywy.askforexpert.model.consultentity.JSDHBean;
import com.xywy.askforexpert.model.consultentity.NewMessageNumEntity;
import com.xywy.askforexpert.model.consultentity.OnlineQuestionMsgListRspEntity;
import com.xywy.askforexpert.model.consultentity.QuestionAnsweredLIstRspEntity;
import com.xywy.askforexpert.model.consultentity.QuestionInHandleRspEntity;
import com.xywy.askforexpert.model.consultentity.QuestionMsgListRspEntity;
import com.xywy.askforexpert.model.consultentity.QuestionsPoolRspEntity;
import com.xywy.askforexpert.model.consultentity.ZXZHSHReadBean;
import com.xywy.askforexpert.model.home.JSDHCookieBean;
import com.xywy.datarequestlibrary.XywyDataRequestApi;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by zhangzheng on 2017/4/26.
 */

public class ServiceProvider {


    //即时问答消息数量 发现页中使用 stone
    public static void getIMNewMessageNum(String doctorId, Subscriber<NewMessageNumEntity> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("did", doctorId);
        XywyDataRequestApi.getInstance().getRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_NEW_MESSAGE_NUM,
                getParams, "1778", ConsultConstants.VERSION_1_0, NewMessageNumEntity.class, subscriber);
    }

    public static void getJSDH(String depa_pid, Subscriber<JSDHBean> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("depa_pid", depa_pid);
        XywyDataRequestApi.getInstance().getRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_NEW_JSDH_NUM,
                getParams, "1961", ConsultConstants.VERSION_1_0, JSDHBean.class, subscriber);
    }

    //1742 . 医生提交总结 v 1.1 stone 移植于yimai
    public static void doctorSummary(String did, String qid,
                                     String advice,
                                     String symptoms,
                                     String diagnose,
                                     Subscriber<CommonRspEntity> subscriber) {
        Map<String, String> getParams = new HashMap<>();

        Map<String, String> postParam = new HashMap<>();
        postParam.put("did", did);
        postParam.put("qid", qid);
        postParam.put("advice", advice);//建议
        postParam.put("symptoms", symptoms);//症状
//        postParam.put("diagnose", diagnose);//诊断 stone 去掉

        XywyDataRequestApi.getInstance().postRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_DOCTOR_SUMMARY,
                getParams, postParam, "1742", "1.1", CommonRspEntity.class, subscriber);
    }

    /**
     * 获取医生信息
     *
     * @param doctorId   当前用户id
     * @param subscriber
     */
    public static void getDoctorInfo(String doctorId, Subscriber<DoctorInfoEntity> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("doctor_id", doctorId);
        XywyDataRequestApi.getInstance().getRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_GET_DOCTOR_INFO,
                getParams, "1628", ConsultConstants.VERSION_1_0, DoctorInfoEntity.class, subscriber);
    }


    public static void getZxzhsh(String doctorId, Subscriber<ZXZHSHReadBean> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("did", doctorId);
        XywyDataRequestApi.getInstance().getRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_GET_ZXZS_READ_NUM,
                getParams, "2071", ConsultConstants.VERSION_1_0, ZXZHSHReadBean.class, subscriber);
    }

    //处理中的问题
    public static void getQuestionsInHandle(String doctorId, Subscriber<QuestionInHandleRspEntity> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("did", doctorId);
        XywyDataRequestApi.getInstance().getRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_GET_QUESTION_IN_HANDLE,
                getParams, "1630", ConsultConstants.VERSION_1_0, QuestionInHandleRspEntity.class, subscriber);
    }

    //处理中的问题
    public static void getQuestionsList(String doctorId, Subscriber<QuestionInHandleRspEntity> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("did", doctorId);
        getParams.put("type", "hlwyy");
        XywyDataRequestApi.getInstance().getRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_GET_QUESTION_IN_HANDLE,
                getParams, "1630", ConsultConstants.VERSION_1_0, QuestionInHandleRspEntity.class, subscriber);
    }

    //问题库
    public static void getQuestionsPool(int page,int size,String doctorId, Subscriber<QuestionsPoolRspEntity> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("did", doctorId);
        getParams.put("page", page+"");
        getParams.put("size", size+"");
        XywyDataRequestApi.getInstance().getRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_GET_QUESTION_POOL,
                getParams, "1632", ConsultConstants.VERSION_1_0, QuestionsPoolRspEntity.class, subscriber);
    }

    //患者报道
    public static void getPatientManagerList(int page,int size,String doctorId, Subscriber<PatientManagerData> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("did", doctorId);
        getParams.put("page", page+"");
        getParams.put("pagesize", size+"");
        XywyDataRequestApi.getInstance().getRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_PATIENT_MANAGER_LIST,
                getParams, "1984", ConsultConstants.VERSION_1_0, PatientManagerData.class, subscriber);
    }

    //患者分组
    public static void getPatientGroupingList(String doctorId, Subscriber<PatientGroupingData> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("did", doctorId);
        XywyDataRequestApi.getInstance().getRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_PATIENT_GROUPING_LIST,
                getParams, "1894", ConsultConstants.VERSION_1_0, PatientGroupingData.class, subscriber);
    }

    //组管理
    public static void getPatientGrouManageList(String doctorId, Subscriber<GrouManageData> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("did", doctorId);
        XywyDataRequestApi.getInstance().getRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_PATIENT_GROUP_MANAGE_LIST,
                getParams, "1909", ConsultConstants.VERSION_1_0, GrouManageData.class, subscriber);
    }
    //添加分组
    public static void addGroup(String doctorId,String g_name, Subscriber<QuestionMsgListRspEntity> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("did", doctorId);
        getParams.put("g_name", g_name);
        XywyDataRequestApi.getInstance().getRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_ADD_GROUP,
                getParams, "1910", ConsultConstants.VERSION_1_0, QuestionMsgListRspEntity.class, subscriber);
    }

    //删除分组
    public static void deleteGroupManageList(String doctorId,String gid, Subscriber<DeleteGroup> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("did", doctorId);
        getParams.put("gid", gid);
        XywyDataRequestApi.getInstance().getRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_DELETE_GROUP,
                getParams, "1911", ConsultConstants.VERSION_1_0, DeleteGroup.class, subscriber);
    }
    public static void deletePatient(String doctorId,String id, Subscriber<QuestionMsgListRspEntity> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("did", doctorId);
        getParams.put("id", id);
        XywyDataRequestApi.getInstance().getRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_PATIENT_DELETE,
                getParams, "1896", ConsultConstants.VERSION_1_0, QuestionMsgListRspEntity.class, subscriber);
    }
    //获取添加分组患者列表
    public static void getPatientList(String doctorId,Subscriber<PatientEntity> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("did", doctorId);
        XywyDataRequestApi.getInstance().getRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_PATIENT_LIST,
                getParams, "1912", ConsultConstants.VERSION_1_0, PatientEntity.class, subscriber);
    }


    //添加分组患者
    public static void addPatient(String doctorId,String gid ,String id,Subscriber<DeleteBatchNotice> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        Map<String, String> postParams = new HashMap<>();
        postParams.put("did", doctorId);
        postParams.put("gid", gid);
        postParams.put("id", id);
        XywyDataRequestApi.getInstance().postRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_ADD_PATIENT,
                getParams,postParams, "1913", ConsultConstants.VERSION_1_0, DeleteBatchNotice.class, subscriber);
    }

    //批量通知消息内容列表
    public static void getBatchNoticeContentList(String doctorId,Subscriber<BatchNoticeContent> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("did", doctorId);
        XywyDataRequestApi.getInstance().getRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_BATCH_NOTICE_LIST,
                getParams, "1917", ConsultConstants.VERSION_1_0, BatchNoticeContent.class, subscriber);
    }
    //删除消息通知内容
    public static void deleteBatchNoticeContent(String id,Subscriber<DeleteBatchNotice> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("id", id);
        XywyDataRequestApi.getInstance().getRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_BATCH_NOTICE_CONTENT,
                getParams, "1918", ConsultConstants.VERSION_1_0, DeleteBatchNotice.class, subscriber);
    }

    //获取物料寄送地址
    public static void getAddress(String did,Subscriber<AddressBean> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("did", did);
        getParams.put("act", "1");
        XywyDataRequestApi.getInstance().getRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_GET_ADDRESS,
                getParams, "1898", ConsultConstants.VERSION_1_0, AddressBean.class, subscriber);
    }

    public static void editAddress(String did,String uname,String mobile,String province,
                                   String city,String address,String act,
                                   String id,Subscriber<AddressBean> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("did", did);
        getParams.put("uname", uname);
        getParams.put("mobile", mobile);
        getParams.put("province", province);
        getParams.put("city", city);
        getParams.put("address", address);
        getParams.put("act", act);
        getParams.put("id", id);
        XywyDataRequestApi.getInstance().getRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_ADD_ADDRESS,
                getParams, "1897", ConsultConstants.VERSION_1_0, AddressBean.class, subscriber);
    }

    public static void apply(String did,String id,Subscriber<AddressBean> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("did", did);
        getParams.put("id", id);
        XywyDataRequestApi.getInstance().getRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_APPLY,
                getParams, "1900", ConsultConstants.VERSION_1_0, AddressBean.class, subscriber);
    }

    //添加消息通知内容
    public static void addBatchNoticeContent(String did,String title,String content,
                                             String id,String act,Subscriber<QuestionMsgListRspEntity> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("did", did);
        getParams.put("title", title);
        getParams.put("content", content);
        getParams.put("id", id);
        getParams.put("act", act);
        XywyDataRequestApi.getInstance().getRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_ADD_BATCH_NOTICE_CONTENT,
                getParams, "1915", ConsultConstants.VERSION_1_0, QuestionMsgListRspEntity.class, subscriber);
    }
    //添加消息通知内容
    public static void sendBatchNoticeContent(String did,String tid,String uid,Subscriber<QuestionMsgListRspEntity> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("did", did);
        getParams.put("tid", tid);
        getParams.put("uid", uid);
        XywyDataRequestApi.getInstance().getRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_SEND_BATCH_NOTICE_CONTENT,
                getParams, "1931", ConsultConstants.VERSION_1_0, QuestionMsgListRspEntity.class, subscriber);
    }

    //已回复的咨询列表 stone 添加page
    public static void getAnsweredList(String page,String doctorId, Subscriber<QuestionAnsweredLIstRspEntity> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("page", page);
        getParams.put("did", doctorId);
        XywyDataRequestApi.getInstance().getRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_ANSWERED_QUESTION,
                getParams, "1631", ConsultConstants.VERSION_1_0, QuestionAnsweredLIstRspEntity.class, subscriber);
    }
    //未总结的咨询列表-历史回复模块 stone
    public static void getNotSumupList(String page,String doctorId, Subscriber<QuestionAnsweredLIstRspEntity> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("page", page);
        getParams.put("did", doctorId);
        XywyDataRequestApi.getInstance().getRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_UNSUMUP_LIST,
                getParams, "1777", ConsultConstants.VERSION_1_0, QuestionAnsweredLIstRspEntity.class, subscriber);
    }

    //问题消息列表
    public static void getQuestionMsgList(String doctorId, String questionId, Subscriber<QuestionMsgListRspEntity> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("did", doctorId);
        getParams.put("qid", questionId);
        XywyDataRequestApi.getInstance().getRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_GET_QUESTION_MSG_LIST,
                getParams, "1639", ConsultConstants.VERSION_1_0, QuestionMsgListRspEntity.class, subscriber);
    }

    //在线诊室聊天列表
    public static void getOnlineQuestionMsgList(String doctorId, String questionId, Subscriber<OnlineQuestionMsgListRspEntity> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("did", doctorId);
        getParams.put("qid", questionId);
        XywyDataRequestApi.getInstance().getRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_GET_ONLINE_QUESTION_MSG_LIST,
                getParams, "1889", ConsultConstants.VERSION_1_0, OnlineQuestionMsgListRspEntity.class, subscriber);
    }

    //诊后管理聊天列表
    public static void getPatientQuestionMsgList(String doctorId, String questionId, Subscriber<OnlineQuestionMsgListRspEntity> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("did", doctorId);
        getParams.put("qid", questionId);
        XywyDataRequestApi.getInstance().getRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_GET_PATIENT_QUESTION_MSG_LIST,
                getParams, "1983", ConsultConstants.VERSION_1_0, OnlineQuestionMsgListRspEntity.class, subscriber);
    }

    //极速电话cookie获取
    public static void getJsdhCookie(String doctorId,Subscriber<JSDHCookieBean> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("c_uid", doctorId);
        XywyDataRequestApi.getInstance().getRequest(ConsultConstants.METHOD_GET_JSDH_COOKIE_POOL, ConsultConstants.METHOD_GET_JDSH_COOKIE_LIST,
                getParams, "", "", JSDHCookieBean.class, subscriber);
    }

    //跳过问题
    public static void backQuestion(String doctorId, String questionId, Subscriber<BackQuestionRspEntity> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("did", doctorId);
        getParams.put("qid", questionId);
        XywyDataRequestApi.getInstance().getRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_BACK_QUESTION,
                getParams, "1636", ConsultConstants.VERSION_1_0, BackQuestionRspEntity.class, subscriber);
    }

    //认领题目
    public static void adoptQuestion(String doctorId, String questionId, Subscriber<CommonRspEntity> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("did", doctorId);
        getParams.put("qid", questionId);
        XywyDataRequestApi.getInstance().getRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_ADOPT_QUESTION,
                getParams, "1635", ConsultConstants.VERSION_1_0, CommonRspEntity.class, subscriber);
    }

    //抢题
    public static void obtainQuestion(String doctorId, String questionId, Subscriber<CommonRspEntity> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("did", doctorId);
        getParams.put("qid", questionId);
        XywyDataRequestApi.getInstance().getRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_OBTAIN_QUESTION,
                getParams, "1633", ConsultConstants.VERSION_1_0, CommonRspEntity.class, subscriber);
    }


    //阅读消息
    public static void readQuestion(String doctorId, String questionId, Subscriber<CommonRspEntity> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("did", doctorId);
        getParams.put("qid", questionId);
        XywyDataRequestApi.getInstance().getRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_READ_QUESTION,
                getParams, "1638", ConsultConstants.VERSION_1_0, CommonRspEntity.class, subscriber);
    }

    //举报
    public static void tipOff(String doctorId, String questionId, String content, Subscriber<CommonRspEntity> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("did", doctorId);
        Map<String, String> postParam = new HashMap<>();
        postParam.put("qid", questionId);
        postParam.put("content", content);
        postParam.put("type", "1");
        XywyDataRequestApi.getInstance().postRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_TIP_OFF_QUESTION,
                getParams, postParam, "1637", ConsultConstants.VERSION_1_0, CommonRspEntity.class, subscriber);
    }

    public static void tipOff(String doctorId, String questionId,String report_type, String content, Subscriber<CommonRspEntity> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("did", doctorId);
        Map<String, String> postParam = new HashMap<>();
        postParam.put("qid", questionId);
        postParam.put("report_type", report_type);
        postParam.put("content", content);
        postParam.put("type", "1");
        XywyDataRequestApi.getInstance().postRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_TIP_OFF_QUESTION,
                getParams, postParam, "1637", ConsultConstants.VERSION_1_0, CommonRspEntity.class, subscriber);
    }

    //转诊
    public static void changeDepartment(String doctorId, String questionId, String sub, String minorSub, Subscriber<CommonRspEntity> subscriber) {
        Map<String, String> getParams = new HashMap<>();
        getParams.put("did", doctorId);
        getParams.put("qid", questionId);
        getParams.put("depart1", sub);
        getParams.put("depart2", minorSub);
        XywyDataRequestApi.getInstance().getRequest(ConsultConstants.BASE_URL, ConsultConstants.METHOD_CHANGE_DEPARTMENT,
                getParams, "1634", ConsultConstants.VERSION_1_0, CommonRspEntity.class, subscriber);
    }

}
