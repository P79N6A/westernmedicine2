package com.xywy.askforexpert.appcommon;

import com.xywy.askforexpert.model.doctor.Messages;
import com.xywy.askforexpert.model.media.MediaNumberBean;
import com.xywy.askforexpert.model.topics.TopicDetailData;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.SendDCMsgSuccessBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.rxevent.DCDeleteMsgEventBean;
import com.xywy.askforexpert.module.main.service.que.model.MedicineBean;
import com.xywy.retrofit.rxbus.Event;
import com.xywy.retrofit.rxbus.EventSubscriber;
import com.xywy.retrofit.rxbus.RxBus;

import java.util.List;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/11/2 11:59
 * tag标签最好选择activity，方便在全局activity 的ondestroy回调中解注册
 */

public class YmRxBus {

    /**
     * notify stone
     */
    public static void notifyNightModeChanged(boolean isOpen){
        postEvent("NightModeChanged",isOpen);
    }


    /**
     * 夜间留言模式改变 stone
     * @param subscriber
     * @param tag
     */
    public static void registerNightModeChangedListener(EventSubscriber subscriber, Object tag){
        registerEvent("NightModeChanged", subscriber, tag);
    }

    /**
     * notify 问题广场 刷新问题列表 推送进入问题详情再退出 stone
     */
    public static void notifyPushEnterExitSuccess(){
        postEvent("PushEnterExit",null);
    }


    /**
     * 问题广场 刷新问题列表 推送进入问题详情再退出 stone
     * @param subscriber
     * @param tag
     */
    public static void registerPushEnterExitListener(EventSubscriber subscriber, Object tag){
        registerEvent("PushEnterExit", subscriber, tag);
    }


    /**
     * notify 即时问答 问题列表中认领成功(处理中),刷新列表 stone
     */
    public static void notifyAdoptQuestionSuccess(){
        postEvent("adoptQuestionSuccess",null);
    }


    /**
     * 即时问答 问题列表中认领成功(处理中),刷新列表 stone
     * @param subscriber
     * @param tag
     */
    public static void registerAdoptQuestionListener(EventSubscriber subscriber, Object tag){
        registerEvent("adoptQuestionSuccess", subscriber, tag);
    }



    /**
     * notify 发表选择药品成功
     */
    public static void notifySelectMedicineSuccess(MedicineBean medicineBean){
        postEvent("selectMedicineSuccess", medicineBean);
    }


    /**
     * 注册 选择药品成功 监听
     * @param subscriber
     * @param tag
     * @return
     */
    public static void registerSelectMedicineListener(EventSubscriber<MedicineBean> subscriber, Object tag){
        registerEvent("selectMedicineSuccess", subscriber, tag);
    }

    /**
     * notify 发表医圈消息成功
     */
    public static void notifySendDCMsgSuccess(SendDCMsgSuccessBean sendDCMsgSuccessBean){
        postEvent("sendDCMsgSuccess", sendDCMsgSuccessBean);
    }

    /**
     * 注册 发表医圈消息成功监听
     * @param subscriber
     * @param tag
     * @return
     */
    public static void registerSendDCMsgSuccessListener(EventSubscriber<SendDCMsgSuccessBean> subscriber, Object tag){
        registerEvent("sendDCMsgSuccess", subscriber, tag);
    }

    /**
     * notify 删除医圈消息
     */
    public static void notifyDeleteDoctorCircleMsg(DCDeleteMsgEventBean deleteMsgEventBean){
        postEvent("deleteDCMsg", deleteMsgEventBean);
    }

    /**
     * 注册 删除医圈消息监听
     * @param subscriber
     * @param tag
     * @return
     */
    public static void registerDeleteDoctorCircleMsgListener(EventSubscriber subscriber, Object tag){
         registerEvent("deleteDCMsg", subscriber, tag);
    }

    /**
     * 消息数量变更事件
     */
    public static void notifyMessageCountChanged(Messages message){
        postEvent("MessageCountChanged", message);
    }

    public static void registerMessageCountChanged(EventSubscriber subscriber, Object tag){
        registerEvent("MessageCountChanged", subscriber, tag);
    }

    /**
     * 刷新实名医圈消息
     */
    public static void notifyRefreshRealCircleMessage(){
        postEvent("RefreshRealCircleMessage", null);
    }
    public static void registerRefreshRealCircleMessage(EventSubscriber subscriber, Object tag){
         registerEvent("RefreshRealCircleMessage", subscriber, tag);
    }

    /**
     * 刷新实名医圈消息
     */
    public static void notifyTopicHeadMessage(TopicDetailData.ListBean data){
        postEvent("TopicHead", data);
    }

    public static void registerTopicHeadMessage(EventSubscriber subscriber, Object tag){
         registerEvent("TopicHead", subscriber, tag);
    }

    /**
     * 刷新匿名医圈消息
     */
    public static void notifyRefreshAnonyCircleMessage(){
        postEvent("RefreshAnonyCircleMessage", null);
    }

    public static void registerRefreshAnonyCircleMessage(EventSubscriber subscriber, Object tag){
         registerEvent("RefreshAnonyCircleMessage", subscriber, tag);
    }

    /**
     * 签到成功
     */
    public static void notifySignName(){
        postEvent("SignName", null);
    }

    public static void registerSignName(EventSubscriber subscriber, Object tag){
        registerEvent("SignName", subscriber, tag);
    }

    /**
     * 订阅更多公众号推荐列表
     */
    public static void notifyMediaRecommendData(List<MediaNumberBean> recommend){
        postEvent("MediaRecommend", recommend);
    }

    public static void registerMediaRecommendData(EventSubscriber subscriber, Object tag){
        registerEvent("MediaRecommend", subscriber, tag);
    }

    /**
     * 未处理完成的帖子的数量
     */
    public static void notifyUnReadMsgCount(Integer num){
        postEvent("UnReadMsgCount", num);
    }

    public static void registerUnReadMsgCount(EventSubscriber<Integer> subscriber, Object tag){
        registerEvent("UnReadMsgCount", subscriber, tag);
    }

    /**
     * 抢到的帖子回复超时
     */
    public static void notifyTimeOut(){
        postEvent("TimeOut",null);
    }

    public static void registerTimeOut(EventSubscriber subscriber, Object tag){
        registerEvent("TimeOut", subscriber, tag);
    }

    public static void notifyUpdateUnreadLabel(){
        postEvent("updateUnreadLabel",null);
    }

    public static void registerUpdateUnreadLabel(EventSubscriber subscriber, Object tag){
        registerEvent("updateUnreadLabel", subscriber, tag);
    }

    /**
     * webview加载页面成功
     */
    public static <T> void notifyWebViewLoadingStateSuccess(T t){
        postEvent("WebViewLoadingStateSuccess", t);
    }

    public static void registerWebViewLoadingStateSuccess(EventSubscriber subscriber, Object tag){
        registerEvent("WebViewLoadingStateSuccess", subscriber, tag);
    }

    /**
     * 医生已经填写完绑定银行卡的信息
     */
    public static void notifyFinshBandCardInfo(){
        postEvent("FinshBandCardInfo",null);
    }

    public static void registerFinshBandCardInfo(EventSubscriber subscriber, Object tag){
        registerEvent("FinshBandCardInfo", subscriber, tag);
    }

    /**
     * 历史恢复中，需要修改的问题，修改成功
     */
    public static void notifyHistoryReplyUpdateSucess(String qid){
        postEvent("HistoryReplyUpdateSucess", qid);
    }

    public static void registerHistoryReplyUpdateSucess(EventSubscriber<String> subscriber, Object tag){
        registerEvent("HistoryReplyUpdateSucess", subscriber, tag);
    }

    public static void postEvent(String eventId,Object data){
        RxBus.getDefault().post(new Event<>(eventId,data));
    }

    public static void registerEvent(String eventId, EventSubscriber subscriber, Object tag){
         RxBus.getDefault().registerEvent(eventId,subscriber, tag);
    }
}
