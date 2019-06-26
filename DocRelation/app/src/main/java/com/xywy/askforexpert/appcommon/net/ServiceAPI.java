package com.xywy.askforexpert.appcommon.net;

import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.retrofitTools.RetrofitServices;
import com.xywy.askforexpert.appcommon.net.retrofitTools.RetrofitUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.base.BaseBean2;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * 类描述:
 * 创建人: shihao on 16/1/4 18:41.
 */
@Deprecated
public final class ServiceAPI {

    /**
     * 服务定制和订阅列表接口http://yimai.api.xywy.com/app/1.2/zixun/index.interface.php?a=list&c=subscribe&userid=18732253
     * 李小松(lixiaosong)  17:16:23
     * 修改服务定制的接口：http://yimai.api.xywy.com/app/1.2/zixun/index.interface.php?a=serve&c=subscribe&userid=18732253
     * 李小松(lixiaosong)  17:16:46
     * 修改订阅的接口：http://yimai.api.xywy.com/app/1.2/zixun/index.interface.php?a=read&c=subscribe&userid=18732253
     * 李小松(lixiaosong)  17:17:37
     * 列表中type为0是没定制的type为1是定制的type为2是固定的
     */

    private final static String url = CommonUrl.Consulting_Url;//"http://yimai.api.xywy.com/app/1.2/zixun/index.interface.php?a=list&c=subscribe&userid=18732253";// CommonUrl.doctor_circo_url;
    private static final String TAG = "ServiceAPI";
    public static ServiceAPI serviceAPI;
    private static FinalHttp fh = new FinalHttp();

    private ServiceAPI() {
    }

    public static ServiceAPI getInstance() {
        if (serviceAPI == null) {
            serviceAPI = new ServiceAPI();
        }

        return serviceAPI;
    }

    /**
     * 服务定制和订阅列表接口
     *
     * @param userid   当前登录用户id
     *                 // @param identityType   当前登录用户类型  0未登陆，1专家，2医学生，3兼职医生
     * @param callBack
     */
    public void getServiceData(String userid, AjaxCallBack callBack) {
        // YMApplication.getLoginInfo().getData().getIsjob()  isjob 1 兼职 2 专家  0未认证
        // 医学生  isdoctor   12|| 13|| 14
        String identityType;
        if (!YMUserService.isGuest()) {
            identityType = YMApplication.getLoginInfo().getData().getIsjob();
            DLog.d(TAG, "user type = " + identityType);
            String isDoctor = YMApplication.getLoginInfo().getData().getIsdoctor();
            if (null == identityType || "".equals(identityType)) {
                identityType = "0";
            } else {
                switch (identityType) {
                    // 为认证：
                    case "0":
                        identityType = "0";
                        if (isDoctor.equals("12") || isDoctor.equals("13") || isDoctor.equals("14")) {
                            identityType = "2";
                        }
                        break;

                    // 兼职医生
                    case "1":
                        identityType = "3";
                        break;

                    // 专家
                    case "2":
                        identityType = "1";
                        break;

                    // 医学生
                    default:
                        if (isDoctor.equals("12") || isDoctor.equals("13") || isDoctor.equals("14")) {
                            identityType = "2";
                        }
                        break;
                }
            }

        } else {
            userid = "0";
            identityType = "0";
        }

        AjaxParams params = new AjaxParams();
        params.put("a", "list");
        params.put("userid", userid);
        params.put("c", "subscribe");
        params.put("type", identityType);
        params.put("sign", MD5Util.MD5(userid + Constants.MD5_KEY));
        DLog.i(TAG, "-服务咨询定制添加接口----" + url + "?" + params.toString());
        fh.post(url, params, callBack);
    }

    /**
     * 修改订阅接口
     *
     * @param userid   当前用户登录id
     * @param callBack
     */
    public void modifySub(String userid, String orderId, AjaxCallBack callBack) {

        if (YMUserService.isGuest()) {
            userid = "0";
        }

        AjaxParams params = new AjaxParams();
        params.put("a", "read");
        params.put("c", "subscribe");
        params.put("userid", userid);
        params.put("id", orderId);
        params.put("sign", MD5Util.MD5(userid + Constants.MD5_KEY));
        DLog.i(TAG, "-修改订阅接口----" + url + "?" + params.toString());
        fh.post(url, params, callBack);
    }

    public void pushMediaSubscribe(String mediaIds, Callback<BaseBean2> callback) {
        DLog.d(TAG, "push media ids = " + mediaIds);
        Map<String, Object> map = new HashMap<>();
        map.put("a", "dcFriend");
        map.put("m", "friend_media");
        map.put("userid", YMApplication.getPID());
        map.put("sign", CommonUtils.computeSign(YMApplication.getPID()));
        map.put("bind", YMApplication.getPID());
        map.put("id", mediaIds);
        RetrofitServices.MediaSubscribePushService service =
                RetrofitUtil.createService(RetrofitServices.MediaSubscribePushService.class);
        Call<BaseBean2> call = service.getData(map);
        call.enqueue(callback);
    }

    /**
     * 修改定制服务接口
     *
     * @param userid   当前用户登录id
     * @param callBack
     */
    public void modifyService(String userid, String orderId, AjaxCallBack callBack) {
        if (YMUserService.isGuest()) {
            userid = "0";
        }
        AjaxParams params = new AjaxParams();
        params.put("a", "serve");
        params.put("c", "subscribe");
        params.put("userid", userid);
        params.put("id", orderId);
        params.put("sign", MD5Util.MD5(userid + Constants.MD5_KEY));
        DLog.i(TAG, "-修改定制服务接口----" + url + params.toString());
        fh.post(url, params, callBack);
    }


}
