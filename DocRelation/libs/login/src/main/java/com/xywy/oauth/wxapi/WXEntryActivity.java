package com.xywy.oauth.wxapi;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.xywy.component.datarequest.neworkWrapper.BaseData;
import com.xywy.component.datarequest.neworkWrapper.IDataResponse;
import com.xywy.component.datarequest.tool.DataRequestTool;
import com.xywy.oauth.R;
import com.xywy.oauth.login.DatabaseRequestType;
import com.xywy.oauth.login.MineConstants;
import com.xywy.oauth.model.LoginModel;
import com.xywy.oauth.model.UserInfoCenter;
import com.xywy.oauth.model.entity.UserThird;
import com.xywy.oauth.service.ApiParams;
import com.xywy.oauth.service.LoginServiceProvider;
import com.xywy.oauth.service.NetworkConstants;
import com.xywy.oauth.ui.ThirdLoginFragment;
import com.xywy.oauth.utils.CommonUtils;
import com.xywy.oauth.utils.TimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by DongJr on 2016/3/21.
 * <p>
 * 使用微信登录时,需要将此类连同文件夹放入主项目,包名同级目录下,否则接收不到回调
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {


    private String mOpenid;
    private IWXAPI wxApi = ThirdLoginFragment.wxApi;
    //    public static boolean isGo = false ;
    public Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.weixin_login);
        loadingDialog = new Dialog(this, R.style.loading_dialog);
        super.onCreate(savedInstanceState);
        if (wxApi != null) {
            wxApi.handleIntent(getIntent(), this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        wxApi.handleIntent(intent, this);
    }


    @Override
    protected void onResume() {
        super.onResume();
//        if (isGo){
//            finish();
//        }
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
            loginCallback(baseResp);
        } else if (baseResp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
            shareCallback(baseResp);
        }
//        isGo = false;
    }

    private void loginCallback(BaseResp baseResp) {
        int result;
        //微信登录回调
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                showDialog();
                result = R.string.third_login_success;
                String code = ((SendAuth.Resp) baseResp).code;
                getToken(code);
                CommonUtils.showToast(result);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.third_login_cancle;
                CommonUtils.showToast(result);
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.third_login_denied;
                CommonUtils.showToast(result);
                finish();
                break;
            default:
                result = R.string.third_login_fail;
                CommonUtils.showToast(result);
                finish();
                break;
        }
    }

    private void shareCallback(BaseResp baseResp) {
        int result;
        //微信分享回调
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = R.string.share_success;
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.share_cancle;
                break;
            default:
                result = R.string.share_fail;
                break;
        }
        CommonUtils.showToast(result);
        finish();
    }

    private void getToken(String code) {
        final String getTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?"
                + "appid=" + MineConstants.WXAPP_ID
                + "&secret=" + MineConstants.WXAPP_SECRET
                + "&code=" + code
                + "&grant_type=" + "authorization_code";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //获取token和openid
                    String json = requetHttp(getTokenUrl);
                    JSONObject jsonObject = new JSONObject(json);
                    String access_token = jsonObject.getString("access_token");
                    mOpenid = jsonObject.getString("openid");
                    //通过token和openid获取用户信息
                    getUserInfo(access_token, mOpenid);
                } catch (JSONException e) {
                    CommonUtils.showToast(R.string.third_login_fail);
                    finish();
                }
            }
        }).start();
    }

    private void getUserInfo(String access_token, String openid) {
        String infoUrl = "https://api.weixin.qq.com/sns/userinfo?"
                + "access_token=" + access_token
                + "&openid=" + openid;

        String userInfoJson = requetHttp(infoUrl);
        try {
            JSONObject jsonObject = new JSONObject(userInfoJson);
            String nickname = jsonObject.getString("nickname");
            String headimgurl = jsonObject.getString("headimgurl");

            initApi(nickname.trim(), headimgurl);

        } catch (JSONException e) {
            CommonUtils.showToast(R.string.third_login_fail);
            finish();
        }
    }

    private void initApi(String nickname, String headimgurl) {

        ApiParams apiParams = new ApiParams()
                .with(NetworkConstants.act_key, NetworkConstants.act_third_value)
                .with(NetworkConstants.openid_key, mOpenid)
                .with(NetworkConstants.channelnum_key, "weixin")
                .with(NetworkConstants.usersource_key, NetworkConstants.source_value)
                .with(NetworkConstants.nickname_key, nickname)
                .with(NetworkConstants.photo_key, headimgurl)
                .with("phone", "");
        LoginServiceProvider.loginThird(apiParams, new ThirdResponse(this), DatabaseRequestType.Userthird);

    }

    private String requetHttp(String getTokenUrl) {
        try {
            URL url = new URL(getTokenUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            conn.setConnectTimeout(5 * 1000);
            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] bytes = new byte[1024];
                int len = 0;
                if ((len = is.read(bytes)) != -1) {
                    bos.write(bytes, 0, len);
                }
                return bos.toString();
            }
            conn.disconnect();
        } catch (Exception e) {
            CommonUtils.showToast(R.string.third_login_fail);
            finish();
        }
        return "";
    }

    class ThirdResponse implements IDataResponse {

        private Context mContext;

        public ThirdResponse(Context context) {
            mContext = context;
        }

        @Override
        public void onResponse(BaseData obj) {
            if (obj != null) {
                boolean isSucess = DataRequestTool.noError(obj);
                if (isSucess) {
                    UserThird data = (UserThird) obj.getData();
                    UserThird.DataEntity info = data.getData();
                    LoginModel loginModel = new LoginModel();
                    loginModel.setPhoto(info.getPhoto());
                    loginModel.setBirthday(info.getBirthday());
                    loginModel.setAge(info.getAge());
                    loginModel.setBlood_pressure(info.getBlood_pressure());
                    loginModel.setBlood_type(info.getBlood_type());
                    loginModel.setDevid(info.getDevid());
                    loginModel.setFirst_login_time(info.getFirst_login_time());
                    loginModel.setHeight(info.getHeight());
                    loginModel.setIdcard(info.getIdcard());
                    loginModel.setInsert_data(info.getInsert_data());
                    loginModel.setNickname(info.getNickname());
                    loginModel.setPoints(info.getPoints());
                    loginModel.setRealname(info.getRealname());
                    loginModel.setUserid(info.getUserid());
                    loginModel.setSex(info.getSex());
                    loginModel.setUserphone(info.getUserphone());
                    loginModel.setUsersource(info.getUsersource());
                    loginModel.setWeight(info.getWeight());
                    loginModel.setUseremail(info.getUseremail());
                    loginModel.setType(info.getType());
                    loginModel.setLasttime(info.getLasttime());
                    loginModel.setUsername(info.getUsername());
                    loginModel.setQz_login_time(TimeUtils.getSecondTime());
                    //保存登录状态
                    UserInfoCenter.getInstance().setLoginModel(loginModel);
                    CommonUtils.showToast(R.string.login_success);
                    //暂时这样写。发送广播finish，loginactivity，和当前activiy。该fragemnt可以属于其他activity
                    Intent intent = new Intent();
                    intent.setAction("LOGIN_FINISH");
                    mContext.sendBroadcast(intent);
                    mContext.sendBroadcast(new Intent().setAction("REGISTER_FINISH"));
                    mContext.sendBroadcast(new Intent().setAction("FIND_FINISH"));

                } else {
                    if ("network error".equals(obj.getMsg())) {
                        CommonUtils.showToast(R.string.login_net_work_error_msg);
                    } else {
                        CommonUtils.showToast(R.string.login_fail);
                    }
                }
                finish();
            }
        }

    }

    /**
     * dialog show
     */
    public void showDialog() {
        LayoutInflater inflater2 = LayoutInflater.from(this);
        View v = inflater2.inflate(R.layout.dialog_common_layout, null);// 得到加载view
        RelativeLayout layout2 = (RelativeLayout) v.findViewById(R.id.layout_control);// 加载布局
        loadingDialog.setCancelable(false);//true 点击空白处或返回键消失   false 不消失
        loadingDialog.setContentView(layout2, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
        loadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    loadingDialog.dismiss();
                }
                return false;
            }
        });
        loadingDialog.show();
    }

    public void hideDialog() {
        if (loadingDialog != null) {
            loadingDialog.cancel();
        }
    }
}