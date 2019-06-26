package com.xywy.oauth.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.xywy.component.datarequest.neworkWrapper.BaseData;
import com.xywy.component.datarequest.neworkWrapper.IDataResponse;
import com.xywy.oauth.R;
import com.xywy.oauth.login.DatabaseRequestType;
import com.xywy.oauth.login.LoginCallBack;
import com.xywy.oauth.login.MineConstants;
import com.xywy.oauth.login.WeiBo.WBAuthListener;
import com.xywy.oauth.login.qq.BaseUiListener;
import com.xywy.oauth.model.LoginModel;
import com.xywy.oauth.model.UserInfoCenter;
import com.xywy.oauth.model.entity.UserThird;
import com.xywy.oauth.service.ApiParams;
import com.xywy.oauth.service.DataRequestTool;
import com.xywy.oauth.service.LoginServiceProvider;
import com.xywy.oauth.utils.CommonUtils;
import com.xywy.oauth.utils.TimeUtils;


/**
 * Created by DongJr on 2016/3/16.
 */
public class ThirdLoginFragment extends Fragment implements View.OnClickListener {

    public static BaseUiListener mTencentListener;
    public static IWXAPI wxApi;
    public SsoHandler mSsoHandler;
    private ImageButton loginWeibo;
    private ImageButton loginWeixin;
    private ImageButton loginQq;
    private Tencent mTencent;
    private AuthInfo mAuthInfo;
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.third_party_login, container, false);
        bindView();
        return mView;
    }

    private void bindView() {
        loginWeibo = (ImageButton) mView.findViewById(R.id.login_weibo);
        loginWeixin = (ImageButton) mView.findViewById(R.id.login_weixin);
        loginQq = (ImageButton) mView.findViewById(R.id.login_qq);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        loginWeibo.setOnClickListener(this);
        loginQq.setOnClickListener(this);
        loginWeixin.setOnClickListener(this);
        initThirdLogin();
    }

    private void initThirdLogin() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        wxApi = WXAPIFactory.createWXAPI(getContext(), MineConstants.WXAPP_ID, false);
        wxApi.registerApp(MineConstants.WXAPP_ID);

        //初始化QQ
        if (mTencent == null) {
            mTencent = Tencent.createInstance(MineConstants.QQAPP_ID, getContext());
        }
        mTencentListener = new BaseUiListener(getActivity(), mTencent, new ThirdLoginCallBack());

        //初始化微博
        mAuthInfo = new AuthInfo(getContext(), MineConstants.WBAPP_ID,
                MineConstants.REDIRECT_URL, MineConstants.WB_SCOPE);
        mSsoHandler = new SsoHandler(getActivity(), mAuthInfo);

    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.login_weibo) {//微博登录
            mSsoHandler.authorize(new WBAuthListener(getContext(), new ThirdLoginCallBack()));

        } else if (i == R.id.login_weixin) {//微信登录
            if (wxApi.isWXAppInstalled()) {
                SendWXAuth();
            } else {
                CommonUtils.showToast("请先安装微信客户端");
            }

        } else if (i == R.id.login_qq) {//QQ登录
            if (!mTencent.isSessionValid()) {
                mTencent.login(getActivity(), "all", mTencentListener);
                ((BaseActivity) getActivity()).showDialog();
            }
        }
    }

    private void SendWXAuth() {
        final SendAuth.Req req = new SendAuth.Req();
        req.transaction = MineConstants.WXAPP_ID;
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        wxApi.sendReq(req);
    }


    class ThirdLoginCallBack implements LoginCallBack {

        @Override
        public void success(Object o) {
            ApiParams postApiParams = (ApiParams) o;
            LoginServiceProvider.loginThird(postApiParams, new ThirdResponse(getContext()), DatabaseRequestType.Userthird);
        }

        @Override
        public void fail(String msg) {
            if (getActivity() != null) {
                ((BaseActivity) getActivity()).hideDialog();
            }
            CommonUtils.showToast(msg);
        }
    }

    class ThirdResponse implements IDataResponse {

        private Context mContext;

        public ThirdResponse(Context context) {
            mContext = context;
        }

        @Override
        public void onResponse(BaseData obj) {
            if (obj != null) {
                boolean isSucess = DataRequestTool.noError(mContext, obj, false);
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
                    if (getActivity() != null) {
                        ((BaseActivity) getActivity()).hideDialog();
                    }

                    if (mTencent.isSessionValid()) {
                        mTencent.logout(mContext);
                    }
                    //暂时这样写。发送广播finish，loginactivity，和当前activiy。该fragemnt可以属于其他activity
                    Intent intent = new Intent();
                    intent.setAction("LOGIN_FINISH");
                    mContext.sendBroadcast(intent);
                    getActivity().finish();

                } else {
                    ((BaseActivity) getActivity()).hideDialog();
                    if ("network error".equals(obj.getMsg())) {
                        CommonUtils.showToast(R.string.login_net_work_error_msg);
                    } else {
                        CommonUtils.showToast(R.string.login_fail);
                    }
                }

            }
        }

    }

}
