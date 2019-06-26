package com.xywy.askforexpert.appcommon.net;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.module.my.account.LoginActivity;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

/**
 * 医圈接口类
 *
 * @author LG
 *
 * 废弃的接口 新的 api 请在Rx 相应的工具类中添加
 */
@Deprecated
public class DoctorAPI {

    public static final String doctorCicleKey = "doctorCicleKey";

    private final static String url = CommonUrl.doctor_circo_url;
    private static final String TAG = "DoctorAPI";
    private static FinalHttp fh = new FinalHttp();

    /**
     * 动态未读消息
     *
     * @param uuid
     * @param type
     * @param sign
     * @param callBack
     */
    public static void getUnReadIpi(String uuid, String type,
                                    String sign, AjaxCallBack callBack) {
        AjaxParams params = new AjaxParams();
        params.put("m", "message_list");
        params.put("a", "dcMsg");
        params.put("userid", uuid);
        params.put("bind", uuid + type);
        params.put("type", type);
        params.put("sign", sign);
        fh.post(url, params, callBack);
    }

    /**
     * 动态未读消息历史消息
     *
     * @param uuid
     * @param type
     * @param sign
     * @param callBack
     */
    public static void getUnReadHistoryIpi(String page, String uuid,
                                           String type, String sign, AjaxCallBack callBack) {
        AjaxParams params = new AjaxParams();
        params.put("m", "my_message");
        params.put("a", "dcMsg");
        params.put("userid", uuid);
        params.put("page", page);
        params.put("bind", uuid + type);
        params.put("type", type);
        params.put("sign", sign);
        DLog.i(TAG, "-动态未读消息历----" + url + params.toString());
        fh.post(url, params, callBack);
    }

    /**
     * 点赞 已替换 废弃
     *
     * @param uuid
     * @param type
     * @param sign
     * @param bind
     * @param messag_id
     * @param callBack
     */
    @Deprecated
    public static void praiseAPI(String commentid, String uuid,
                                 String touserid, String type, String sign, String bind,
                                 String messag_id, AjaxCallBack callBack) {
        AjaxParams params = new AjaxParams();
        params.put("m", "praise");
        params.put("a", "praise");
        params.put("userid", uuid);
        params.put("dynamicid", messag_id);
        params.put("commentid", commentid);// 评论ID
        params.put("bind", bind);// userid+touserid+commentid+dynamicid
        params.put("touserid", touserid);// 被评论用户ID
        params.put("sign", sign);
        params.put("type", type);
        DLog.d("praise", "praiseUrl=" + url + "?" + params.toString());
        fh.post(url, params, callBack);
    }

    /**
     * 删除动态
     *
     * @param uuid
     * @param bind
     * @param sign
     * @param dynamicid
     * @param callBack
     */
    public static void dynamicDelete(String uuid, String bind, String sign,
                                     String dynamicid, AjaxCallBack callBack) {
        AjaxParams params = new AjaxParams();
        params.put("m", "dynamic_del");
        params.put("a", "dynamic");
        params.put("userid", uuid);
        params.put("bind", bind);
        params.put("dynamicid", dynamicid);
        params.put("sign", sign);
        DLog.d("tag", "DELETE POST URL = " + (url + "?" + params.toString()));
        fh.post(url, params, callBack);
    }

    /**
     * 医圈动态详情
     *
     * @param bind
     * @param sign
     * @param callBack
     */
    public static void dynamicDetaile(String dynamicid, String userid,
                                      String bind, String sign, int page, AjaxCallBack callBack) {
        AjaxParams params = new AjaxParams();
        params.put("m", "dynamic_row");
        params.put("a", "dynamic");
        params.put("bind", bind);
        params.put("userid", userid);
        params.put("dynamicid", dynamicid);
        params.put("sign", sign);
        params.put("page", String.valueOf(page));
        fh.post(url, params, callBack);
        DLog.i("医圈实名动态详情", "Url=" + url + "?" + params.toString());
    }

    /**
     * 医圈我的动态(匿名)
     *
     * @param bind
     * @param sign
     * @param callBack
     */
    public static void getMydynamicNoName(String touserid, String page, String userid,
                                          String type, String bind, String sign,
                                          AjaxCallBack callBack) {
        AjaxParams params = new AjaxParams();
        params.put("m", "my_dynamic");
        params.put("a", "dynamic");
        params.put("userid", userid);
        params.put("touserid", touserid);
        params.put("bind", bind);
        params.put("page", page);
        params.put("type", type);
        params.put("sign", sign);
        DLog.i(TAG, "医圈我的动态匿名 " + url + "?" + params.toString());
        fh.post(url, params, callBack);
    }

    /**
     * 医圈我的动态评论
     *
     * @param bind
     * @param sign
     * @param callBack
     */
    public static void getRealNameConment(String type, String commentid,
                                          String content, String dynamicid, String userid, String touserid,
                                          String bind, String sign, AjaxCallBack callBack) {
        AjaxParams params = new AjaxParams();
        params.put("m", "comment");
        params.put("a", "comment");
        params.put("commentid", commentid);
        params.put("userid", userid);
        params.put("type", type);
        params.put("touserid", touserid);// 被评论用户ID
        params.put("dynamicid", dynamicid);// 动态IDID
        params.put("content", content);// 评论内容
        params.put("bind", bind);// /userid+touserid+dynamicid
        params.put("sign", sign);
        DLog.d(TAG, "comment url = " + url + "?" + params.toString());
        fh.post(url, params, callBack);
    }

    /**
     * 删除动态评论 已替换 废弃
     *
     * @param bind
     * @param sign
     * @param callBack
     */
    @Deprecated
    public static void getDeleteConment(String id, String userid, String bind,
                                        String sign, AjaxCallBack callBack) {
        AjaxParams params = new AjaxParams();
        params.put("m", "comment_del");
        params.put("a", "comment");
        params.put("userid", userid);
        params.put("id", id);// 被评论用户ID
        params.put("bind", bind);// /userid+id
        params.put("sign", sign);
        fh.post(url, params, callBack);
    }

    /**
     * 获取更多评论
     *
     * @param bind
     * @param sign
     * @param callBack
     */
    public static void getMoreConment(String page, String dynamicid,
                                      String bind, String sign, AjaxCallBack callBack) {
        AjaxParams params = new AjaxParams();
        params.put("m", "comment_page");
        params.put("page", page);
        params.put("a", "comment");
        params.put("dynamicid", dynamicid);
        params.put("bind", bind);// /userid+id
        params.put("sign", sign);
        DLog.d(TAG, "more comment url = " + url + "?" + params.toString());
        fh.post(url, params, callBack);
    }

    /**
     * 添加好友
     *
     * @param bind
     * @param sign
     * @param callBack
     */
    public static void getAddFrends(String userid, String touserid,
                                    String bind, String sign, AjaxCallBack callBack) {
        AjaxParams params = new AjaxParams();
        params.put("m", "friend_add");
        params.put("a", "dcFriend");
        params.put("userid", userid);
        params.put("touserid", touserid);
        params.put("bind", bind);// / userid+touserid
        params.put("sign", sign);
        fh.post(url, params, callBack);
    }

    /**
     * 用户详细信息
     *
     * @param userid
     * @param bind
     * @param sign
     * @param callBack
     */
    public static void getDoctorInfos(String touserid, String userid, String bind, String sign, AjaxCallBack callBack) {
        AjaxParams params = new AjaxParams();
        params.put("m", "doctor_row");
        params.put("a", "doctor");
        params.put("userid", userid);
        params.put("bind", bind);
        params.put("sign", sign);
        params.put("touserid", touserid);

        DLog.i(TAG, url + "?" + params.toString());
        fh.post(url, params, callBack);
    }

    /**
     * 病例研讨班
     *
     * @param type     3代表病例研讨班
     * @param userid
     * @param bind
     * @param sign
     * @param callBack
     */
    public static void getYanTaoInfos(String type, String touserid, String userid, String bind, String sign, AjaxCallBack callBack) {
        AjaxParams params = new AjaxParams();
        params.put("m", "doctor_row");
        params.put("a", "doctor");
        params.put("userid", userid);
        params.put("bind", bind);
        params.put("sign", sign);
        params.put("touserid", touserid);
        params.put("type", type);
        DLog.i(TAG, url + "?" + params.toString());
        fh.post(url, params, callBack);
    }

    /**
     * 是否接收消息
     *
     * @param type     1取消接收消息 0 打开接收消息
     * @param touserid
     * @param userid
     * @param bind
     * @param sign
     * @param callBack
     */
    public static void getMsgPushState(String type, String touserid, String userid, String bind, String sign, AjaxCallBack callBack) {
        AjaxParams params = new AjaxParams();
        params.put("m", "blacklist");
        params.put("a", "dcFriend");
        params.put("userid", userid);
        params.put("bind", bind);
        params.put("sign", sign);
        params.put("touserid", touserid);
        params.put("type", type);
        DLog.i(TAG, "getMsgPushState" + url + "?" + params.toString());
        fh.post(url, params, callBack);
    }

    /**
     * 登陆
     *
     * @param context
     */
    public static void startLogIn(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(DoctorAPI.doctorCicleKey, DoctorAPI.doctorCicleKey);
        context.startActivity(intent);
        if (context instanceof Activity){
            ((Activity)context).finish();
        }


    }

}
