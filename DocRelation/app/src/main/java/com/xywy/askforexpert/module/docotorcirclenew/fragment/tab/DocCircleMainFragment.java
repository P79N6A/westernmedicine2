package com.xywy.askforexpert.module.docotorcirclenew.fragment.tab;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.base.fragment.CommonListFragment;
import com.xywy.askforexpert.appcommon.medicine.SyncInfoRequest;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.ScreenUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.module.discovery.medicine.SellDrugRequest;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;
import com.xywy.askforexpert.module.docotorcirclenew.adapter.AdapterFactory;
import com.xywy.askforexpert.module.docotorcirclenew.adapter.BaseUltimateRecycleAdapter;
import com.xywy.askforexpert.module.docotorcirclenew.fragment.DynamicMsgFragment;
import com.xywy.askforexpert.module.docotorcirclenew.model.DoctorCircleModelFactory;
import com.xywy.askforexpert.module.docotorcirclenew.model.IRecycleViewModel;
import com.xywy.askforexpert.module.docotorcirclenew.model.PublishType;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.SendDCMsgSuccessBean;
import com.xywy.askforexpert.module.doctorcircle.DoctorCircleSendMessageActivty;
import com.xywy.askforexpert.module.message.friend.AddNewCardHolderActivity;
import com.xywy.askforexpert.module.message.friend.InviteNewFriendMainActivity;
import com.xywy.askforexpert.module.message.friend.MyIdCardActivity;
import com.xywy.askforexpert.module.message.imgroup.activity.ImUserListActivity;
import com.xywy.askforexpert.module.message.imgroup.constants.UserPageShowType;
import com.xywy.askforexpert.module.message.usermsg.MsgFriendCardActivity;
import com.xywy.askforexpert.widget.ActionItem;
import com.xywy.askforexpert.widget.TitlePopup;
import com.xywy.retrofit.RetrofitClient;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.xywy.retrofit.rxbus.Event;
import com.xywy.retrofit.rxbus.EventSubscriber;
import com.xywy.util.T;
import com.zxing.activity.CaptureActivity;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;

import static com.xywy.askforexpert.module.docotorcirclenew.model.PublishType.Anonymous;
import static com.xywy.askforexpert.module.docotorcirclenew.model.PublishType.Realname;

/**
 * A simple {@link Fragment} subclass.
 */
public class DocCircleMainFragment extends DocCircleCommonTabFragment implements TitlePopup.OnItemOnClickListener {
    private static final String TAG = "DocCircleMainFragment";
    private ImageButton mAddFriend,mSendMessge;
    private LinearLayout ll_higth;
    private View view1;
    private LinearLayout ll_send_real;
    private LinearLayout ll_send_notname;

    private static final String ADD_FRIEND = "添加好友";
    private static final String GROUP_CHAT = "发起群聊";
    private static final String INVITE_FRIEND = "邀请医友";
    private static final String FRIEND_LIST = "好友列表";
    private static final String MY_CARD = "我的二维码";
    private static final String ERWEIMA = "扫一扫";
    private SharedPreferences sp;
    private TitlePopup menuPopup;

    protected boolean dismisisDiaglog() {
        if (ll_higth.getVisibility() == View.VISIBLE) {
            topOut();
            return true;
        }
        return false;
    }

    @Override
    protected void initView() {
        super.initView();
        sp = getActivity().getSharedPreferences("save_user", Context.MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.tl_title);
        toolbar.setBackgroundResource(R.drawable.toolbar_bg_no_alpha_new);
//        CommonUtils.setTransparentBar(toolbar, getActivity(), DensityUtils.dp2px(48));
        mAddFriend = (ImageButton) rootView.findViewById(R.id.ib_add_friend);
        mAddFriend.setVisibility(View.VISIBLE);
        mSendMessge = (ImageButton) rootView.findViewById(R.id.ib_sendmessage);
        mSendMessge.setVisibility(View.VISIBLE);
        ll_higth = (LinearLayout) rootView.findViewById(R.id.ll_higth);
        int height = ScreenUtils.getScreenHeight(this.getContext());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        ll_higth.setLayoutParams(lp);
        view1 = rootView.findViewById(R.id.view1);
        ll_send_real = (LinearLayout) rootView.findViewById(R.id.ll_send_real);
        ll_send_notname = (LinearLayout) rootView.findViewById(R.id.ll_send_notname);
        menuPopup = new TitlePopup(getActivity(), LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        menuPopup.setItemOnClickListener(this);

        menuPopup.cleanAction();
//        menuPopup.addAction(new ActionItem(getActivity(), ADD_FRIEND, R.drawable.addfr));
//        menuPopup.addAction(new ActionItem(getActivity(), GROUP_CHAT, R.drawable.fqql));
        menuPopup.addAction(new ActionItem(getActivity(), INVITE_FRIEND, R.drawable.yqyy));
//        menuPopup.addAction(new ActionItem(getActivity(), FRIEND_LIST, R.drawable.friend_list));
        menuPopup.addAction(new ActionItem(getActivity(), MY_CARD, R.drawable.erwima));
//        menuPopup.addAction(new ActionItem(getActivity(), ERWEIMA, R.drawable.sys));

        mAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticalTools.eventCount(getActivity(), "NewTopPlus");
                menuPopup.show_new_left(v);
            }
        });

//        final String userid = YMUserService.isGuest() ? "0" : YMApplication.getPID();
//        DLog.d(TAG, "is msg tutorial showed? " + sp.getBoolean("msg_tutorial_show_" + userid, false));
//        if (!sp.getBoolean("msg_tutorial_show_" + userid, false)) {
//            mAddFriend.post(new Runnable() {
//                @Override
//                public void run() {
//                    menuPopup.show(mAddFriend);
//                    MsgTutorialFragment tutorialDialog = MsgTutorialFragment.newInstance(R.layout.msg_tutorial_layout);
//                    tutorialDialog.show(getActivity().getSupportFragmentManager(), TAG);
//                    sp.edit().putBoolean("msg_tutorial_show_" + userid, true).apply();
//                }
//            });
//        }

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        YmRxBus.registerSendDCMsgSuccessListener(new EventSubscriber<SendDCMsgSuccessBean>() {
            @Override
            public void onNext(Event<SendDCMsgSuccessBean> sendDCMsgSuccessBeanEvent) {
                LogUtils.d("切换刷新UI");
                switchFragment(sendDCMsgSuccessBeanEvent.getData().getType());
                refresh();
            }
        }, getActivity());

    }

    protected void switchToFragment(int page) {
        if (page == 0) {
            mTv_realname.setTextColor(getResources().getColor(R.color.white));
            mRealName.setBackgroundResource(R.drawable.shape_while_bg_left);
            mTv_Noname.setTextColor(getResources().getColor(R.color.c333));
            mNotRealName.setBackgroundResource(R.drawable.shape_while_bg_rigte);
            mContent.setCurrentItem(0);
        } else if (page == 1) {
            mTv_realname.setTextColor(getResources().getColor(R.color.c333));
            mRealName.setBackgroundResource(R.drawable.shape_while_bg_left_normal);
            mTv_Noname.setTextColor(getResources().getColor(R.color.white));
            mNotRealName.setBackgroundResource(R.drawable.shape_while_bg_right_selected);
            mContent.setCurrentItem(1);
        }

    }

    @Override
    protected void initListener() {
        super.initListener();
        mSendMessge.setOnClickListener(this);
        view1.setOnClickListener(this);
        ll_send_real.setOnClickListener(this);
        ll_send_notname.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_sendmessage://发送动态
                if (!NetworkUtil.isNetWorkConnected()) {
                    ToastUtils.shortToast(getString(R.string.no_network));
                    return;
                }
                if (ll_higth.getVisibility() == View.VISIBLE) {
                    topOut();
                } else {
                    topIn();

                }

                break;
            case R.id.view1:
                topOut();
                break;

            case R.id.ll_send_real:
                //添加验证认证状态 stone
                DialogUtil.showUserCenterCertifyDialog(getActivity(), new MyCallBack() {
                    @Override
                    public void onClick(Object data) {
                        topOut();
//                        if (!DCMiddlewareService.isInvalidDCUser(DCMsgType.REAL_NAME, "发表实名动态")) {
//                            DoctorCircleSendMessageActivty.startActivity(getActivity(), "1");
//                        }
                        DoctorCircleSendMessageActivty.startActivity(getActivity(), "1");
                    }
                }, null, null);
                break;

            case R.id.ll_send_notname:
                //添加验证认证状态 stone
                DialogUtil.showUserCenterCertifyDialog(getActivity(), new MyCallBack() {
                    @Override
                    public void onClick(Object data) {
                        topOut();
//                        if (!DCMiddlewareService.isInvalidDCUser(DCMsgType.NO_NAME, "发表匿名动态")) {
//                            DoctorCircleSendMessageActivty.startActivity(getActivity(), "2");
//                        }
                        DoctorCircleSendMessageActivty.startActivity(getActivity(), "2");
                    }
                }, null, null);
                break;
            default:
                super.onClick(v);
                break;
        }
    }

    protected void initSubFragments() {
        addFragment(Realname);
        addFragment(Anonymous);
    }

    private void addFragment(@PublishType String type) {
        CommonListFragment realNameFragment = DynamicMsgFragment.newInstance(type);
        IRecycleViewModel realModel = DoctorCircleModelFactory.newInstance(realNameFragment, this.getActivity(), type, null);
        realNameFragment.setRecycleViewModel(realModel);
        BaseUltimateRecycleAdapter adapter = AdapterFactory.newDynanicMsgAdapter(getContext(), type);
        realNameFragment.setAdapter(adapter);
        subFragmets.add(realNameFragment);
    }

    private void topIn() {
        Animation loadAnimation = AnimationUtils.loadAnimation(YMApplication.getAppContext(), R.anim.top_in);
        loadAnimation.setInterpolator(new BounceInterpolator());
        ll_higth.startAnimation(loadAnimation);
        ll_higth.setVisibility(View.VISIBLE);
        view1.setVisibility(View.VISIBLE);
    }

    private void topOut() {

        Animation loadAnimation = AnimationUtils.loadAnimation(YMApplication.getAppContext(), R.anim.top_out);

        ll_higth.startAnimation(loadAnimation);
        loadAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view1.setVisibility(View.GONE);
                ll_higth.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public String getStatisticalPageName() {
        return "DocCircleMainFragment";
    }

    @Override
    public void onItemClick(ActionItem item, int position) {
        String mTitle = (String) item.mTitle;
        Intent intent;
        switch (mTitle) {
            // 添加好友
            case ADD_FRIEND:
                StatisticalTools.eventCount(getActivity(), "NewAddFriends");
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
                } else {
                    intent = new Intent(getActivity(), AddNewCardHolderActivity.class);
                    startActivity(intent);
                }
                break;
            // 发起群聊
            case GROUP_CHAT:
                //判断认证 stone
                DialogUtil.showUserCenterCertifyDialog(getActivity(), new MyCallBack() {
                    @Override
                    public void onClick(Object data) {
                        ImUserListActivity.start(getActivity(), null, UserPageShowType.CREATE_GROUP);
                    }
                }, null, null);

                break;
            // 邀请好友
            case INVITE_FRIEND:
                StatisticalTools.eventCount(getActivity(), "NewInviteFriends");
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
                } else {
                    intent = new Intent(getActivity(), InviteNewFriendMainActivity.class);
                    startActivity(intent);
                }
                break;
            case FRIEND_LIST:
                //好友列表
                StatisticalTools.eventCount(getActivity(), "NewInviteFriends");
                startActivity(new Intent(YMApplication.getAppContext(), MsgFriendCardActivity.class));
                break;

            // 我的名片
            case MY_CARD:
                StatisticalTools.eventCount(getActivity(), "NewMycard");
                //判断认证 stone
                DialogUtil.showUserCenterCertifyDialog(getActivity(), new MyCallBack() {
                    @Override
                    public void onClick(Object data) {


//                if (YMUserService.isGuest()) {
//                    DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
//                } else {
                        if (YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
                            if (YMApplication.getInstance().getHasSyncInfo()) {
                                //如果已经同步过医生信息
                                if (YMApplication.getInstance().getSyncInfoResult()) {
                                    //医生信息同步成功
                                    if (YMApplication.getInstance().getHasSellDrug()) {
                                        //已经检查过医生是否具有售药的权限
                                        if (YMApplication.getInstance().getSellDrugResult()) {
                                            //当前医生具有售药的权限
                                            com.xywy.util.LogUtils.i("跳转到二维码展示页面");
                                            Intent intent = new Intent(getActivity(), MyIdCardActivity.class);
                                            startActivity(intent);
                                        } else {
                                            ToastUtils.shortToast(YMApplication.getInstance().getSellDrugMsg());
                                        }
                                    } else {
                                        //还未检查医生是否具有售药的权限
                                        sellDrug();
                                    }
                                } else {
                                    //医生信息同步失败
                                    com.xywy.askforexpert.module.discovery.medicine.util.ToastUtils.shortToast(YMApplication.getInstance().getSyncInfoMsg());
                                }
                            } else {
                                //没有同步过医生信息,则同步医生信息
                                syncInfo();
                            }
                        } else {
                            Intent intent = new Intent(getActivity(), MyIdCardActivity.class);
                            startActivity(intent);
                        }

//                }

                    }
                }, null, null);
                break;

            // 扫一扫
            case ERWEIMA:
                StatisticalTools.eventCount(getActivity(), "RichScan");
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            CommonUtils.permissionRequestDialog(getActivity(), "无法启动相机，请授予照相机(Camera)权限", 222);
                        } else {
                            Intent openCameraIntent = new Intent(getActivity(), CaptureActivity.class);
                            startActivityForResult(openCameraIntent, 0);
                        }
                    } else {
                        Intent openCameraIntent = new Intent(getActivity(), CaptureActivity.class);
                        startActivityForResult(openCameraIntent, 0);
                    }
                }
                break;

            default:
                break;
        }
    }

    /**
     * 同步医生信息
     */
    public void syncInfo() {
        long doctroId = Long.parseLong(YMUserService.getCurUserId());
        SyncInfoRequest.getInstance().syncInfo(doctroId).subscribe(new BaseRetrofitResponse<BaseData>() {
            @Override
            public void onStart() {
                super.onStart();
                showProgressDialog("");
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                hideProgressDialog();
                if (e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof HttpException) {
                    //未同步医生信息接口
                    YMApplication.getInstance().setHasSyncInfo(false);
                    Toast.makeText(RetrofitClient.getContext(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else {
                    //已经同步过医生信息接口
                    YMApplication.getInstance().setHasSyncInfo(true);
                    //同步过医生信息接口的返回结果是：同步医生信息失败
                    YMApplication.getInstance().setSyncInfoResult(false, e.getMessage());
                    T.showShort(RetrofitClient.getContext(), e.getMessage());
                }
            }

            @Override
            public void onNext(BaseData entry) {
                super.onNext(entry);
                if (entry != null) {
                    dealwithEntry(entry);
                }
            }
        });
    }

    private void dealwithEntry(BaseData entry) {
        if (null != entry && entry.getCode() == 10000) {
            //已经同步过医生信息接口
            YMApplication.getInstance().setHasSyncInfo(true);
            //同步过医生信息接口的返回结果是：同步医生信息成功
            YMApplication.getInstance().setSyncInfoResult(true, "");
            sellDrug();
        }
    }

    private void sellDrug() {
        SellDrugRequest.getInstance().getSellDrug(Integer.parseInt(YMUserService.getCurUserId())).subscribe(new BaseRetrofitResponse<BaseData>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                hideProgressDialog();

            }

            @Override
            public void onError(Throwable e) {
                hideProgressDialog();
                if (e instanceof UnknownHostException || e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof HttpException) {
                    //还未调用检测是否具有售药权限的接口
                    YMApplication.getInstance().setHasSellDrug(false);
                    Toast.makeText(RetrofitClient.getContext(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else {
                    //已经调用了检测是否具有售药权限的接口
                    YMApplication.getInstance().setHasSellDrug(true);
                    //检测是否具有售药权限的接口返回的结果是：没有售药权限
                    YMApplication.getInstance().setSellDrugResult(false, e.getMessage());
                    T.showShort(RetrofitClient.getContext(), e.getMessage());
                }
            }

            @Override
            public void onNext(BaseData entry) {
                super.onNext(entry);
                if (entry != null) {
                    //已经调用了检测是否具有售药权限的接口
                    YMApplication.getInstance().setHasSellDrug(true);
                    //检测是否具有售药权限的接口返回的结果是：具有售药权限
                    YMApplication.getInstance().setSellDrugResult(true, "");
                    Intent intent = new Intent(getActivity(), MyIdCardActivity.class);
                    startActivity(intent);

                }
            }
        });
    }
}
