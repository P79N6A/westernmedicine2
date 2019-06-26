package com.xywy.askforexpert.module.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.MainActivity;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.base.YMBaseFragment;
import com.xywy.askforexpert.appcommon.base.webview.CommonWebViewActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.view.CustomToast;
import com.xywy.askforexpert.model.ImageInfo;
import com.xywy.askforexpert.model.Qdinfo;
import com.xywy.askforexpert.model.consultentity.DoctorInfoEntity;
import com.xywy.askforexpert.model.consultentity.JSDHBean;
import com.xywy.askforexpert.model.consultentity.NewMessageNumEntity;
import com.xywy.askforexpert.model.consultentity.ZXZHSHReadBean;
import com.xywy.askforexpert.model.discover.DiscoverNoticeNumberBean;
import com.xywy.askforexpert.model.home.HomeGVItemBean;
import com.xywy.askforexpert.model.home.HomeItemBean;
import com.xywy.askforexpert.model.home.RewardItemBean;
import com.xywy.askforexpert.module.consult.ServiceProvider;
import com.xywy.askforexpert.module.discovery.answer.AnswerMainActivity;
import com.xywy.askforexpert.module.discovery.medicine.view.XYWYLoadMoreWrapper;
import com.xywy.askforexpert.module.discovery.service.DiscoverService;
import com.xywy.askforexpert.module.discovery.service.ServiceOpenStateUtils;
import com.xywy.askforexpert.module.docotorcirclenew.activity.NewTopicDetailActivity;
import com.xywy.askforexpert.module.docotorcirclenew.activity.detailpage.RealNameDetailActivity;
import com.xywy.askforexpert.module.main.home.HomeDataAdapter;
import com.xywy.askforexpert.module.main.home.request.GetRewardListRequest;
import com.xywy.askforexpert.module.main.service.media.InfoDetailActivity;
import com.xywy.askforexpert.module.main.service.que.QuePerActivity;
import com.xywy.askforexpert.module.message.MessageInfoActivity;
import com.xywy.askforexpert.module.my.invite.InviteMoneyActivity;
import com.xywy.askforexpert.widget.view.MyGallery;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;
import com.xywy.retrofit.rxbus.Event;
import com.xywy.retrofit.rxbus.EventSubscriber;
import com.xywy.uilibrary.recyclerview.adapter.HSItemDecoration;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by bailiangjin on 2017/1/6.
 */

public class HomeFragment extends YMBaseFragment implements View.OnClickListener{
    private boolean mIsLoadBanner;

    @Bind(R.id.topline)
    View topline;

    @Bind(R.id.tv_title_center)
    View tv_title_center;

    @Bind(R.id.app_bar_layout)
    AppBarLayout app_bar_layout;

    @Bind(R.id.toolbarCollapse)
    CollapsingToolbarLayout collapsingToolbar;

    @Bind(R.id.rl_checkin)
    RelativeLayout rl_checkin;

    @Bind(R.id.iv_qd)
    ImageView iv_qd;

    @Bind(R.id.tv_notice_dot)
    TextView tv_notice_dot;

    @Bind(R.id.fl_message)
    FrameLayout fl_message;

    @Bind(R.id.banner_gallery)
    MyGallery gallery;

    @Bind(R.id.banner_title)
    TextView bannerTitle;

    @Bind(R.id.ovalLayout)
    LinearLayout bannerIndicator;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private HomeDataAdapter mAdapter;

    protected boolean isDecorationAdded = false;

    /**
     * 轮播图默认图片
     */
    private int[] imageId = new int[]{R.drawable.banner_default, R.drawable.banner_default,
            R.drawable.banner_default, R.drawable.banner_default};

    /**
     * 轮播图图片集合
     */
    private final List<ImageInfo> bannerImgs = new ArrayList<>();

    private ArrayList<HomeGVItemBean> innerItemGrid = new ArrayList<>();
    private ArrayList<HomeItemBean> itemList;
    private SharedPreferences sp;
    /**
     * 接口返回的签到信息
     */
    public static Qdinfo mQdinfo;

    private EmptyWrapper mEmptyWrapper;
    private XYWYLoadMoreWrapper mLoadMoreWrapper;
    private String depa_pid;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        CommonUtils.initSystemBar(getActivity());
        hideStatusBarHight();
        initCustomHeaderView();
        sp = getActivity().getSharedPreferences("save_user", Context.MODE_PRIVATE);
        //非im模块的消息数
        YmRxBus.registerUnReadMsgCount(new EventSubscriber<Integer>() {
            @Override
            public void onNext(Event<Integer> integerEvent) {
                updateShowRedPoint(integerEvent.getData().intValue(),Constants.CLUB_CN);
                //更新消息助手和网站公告的消息小红点的显示状态
                updateRedPoint();
            }
        }, getActivity());

        rl_checkin.setOnClickListener(this);
        fl_message.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (!isDecorationAdded) {
            recyclerView.addItemDecoration(getItemDecoration());
            isDecorationAdded = true;
        }
        mAdapter = new HomeDataAdapter(getActivity());
        itemList = new ArrayList<>();

        initGVData();
        itemList.add(0,new HomeItemBean(innerItemGrid,HomeItemBean.TYPE_GRID));
        itemList.add(1,new HomeItemBean());
        initRewardData();
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                HomeItemBean item = mAdapter.getItem(position);
                switch (item.getType()) {
                    case HomeItemBean.TYPE_LIST:
                        LogUtils.i(""+position);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.setAdapter(mAdapter);

//        mEmptyWrapper = new EmptyWrapper(mAdapter);
//        mLoadMoreWrapper = new XYWYLoadMoreWrapper(mEmptyWrapper, recyclerView);
//        mLoadMoreWrapper.setLoadMoreView(R.layout.loading_more);
//        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
//            @Override
//            public void onLoadMoreRequested() {
////                mPage ++;
////                LogUtils.e("load more .......mPage="+mPage+"---mPagesize="+mPagesize);
//                LogUtils.e("load more .......mPage=");
////                getData(mOrder_by,mStort,mPage,mPagesize, State.LOADMORE.getFlag());
//
//            }
//        });
//        recyclerView.setAdapter(mLoadMoreWrapper);

        //隐藏首页媒体号的提示icon
//        if (HomePageCacheUtils.isMediaGuideShouldShow()) {
//            // TODO: 2017/10/24 stone 此处应为getChildFragmentManager
////            new HomeGuideDialogFragment().show(getFragmentManager(), "HomeFragment");
//            new HomeGuideDialogFragment().show(getChildFragmentManager(), "HomeFragment");
//            HomePageCacheUtils.setediaGuideShouldShow(false);
//        }

        app_bar_layout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {

                if (state == State.EXPANDED) {
                    //展开状态
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    tv_title_center.setVisibility(View.VISIBLE);//展示播放按钮
                    topline.setVisibility(View.VISIBLE);//展示播放按钮
                } else {
                    //中间状态
                    tv_title_center.setVisibility(View.GONE);//隐藏播放按钮
                    topline.setVisibility(View.GONE);//隐藏播放按钮
                }
            }
        });
    }

    private void initRewardData() {
        GetRewardListRequest.getInstance().getRewardList().subscribe(new BaseRetrofitResponse<BaseData<RewardItemBean>>(){
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
            }

            @Override
            public void onNext(BaseData<RewardItemBean> entry) {
                super.onNext(entry);
                if(entry!=null) {
                    RewardItemBean data = entry.getData();
                    List<RewardItemBean.Reward> list = data.list;
                    if(null !=list){
                        HomeItemBean homeItemBean = null;
                        RewardItemBean.Reward reward = null;
                        for (int i = 0; i < list.size(); i++) {
                            reward = list.get(i);
                            homeItemBean = new HomeItemBean(reward.image,reward.username,reward.doctorname,reward.depart,
                                    reward.pay_time,reward.did,reward.amount,reward.note);
                            itemList.add(homeItemBean);
                        }
                        mAdapter.setData(itemList);
                        mAdapter.notifyDataSetChanged();

//                        mAdapter.setData(itemList);
//                        mLoadMoreWrapper.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void initGVData() {

        boolean isImwdOpened = Constants.OPEN.equals(ServiceOpenStateUtils.getImwdOpenState());
        boolean isQuestionSquareOpened = Constants.OPEN.equals(ServiceOpenStateUtils.getQuestionSquareOpenState());
        boolean jsdhOpened = false;
        if (!TextUtils.isEmpty(YMApplication.getLoginInfo().getData().getJsdh())){
            jsdhOpened = "1".equals(YMApplication.getLoginInfo().getData().getJsdh()) ? true : false;
        }
        boolean isHomeDoctorOpened = Constants.OPEN.equals(ServiceOpenStateUtils.getHomeDoctorOpenState());
        boolean isCallDoctorOpened = Constants.OPEN.equals(ServiceOpenStateUtils.getCallDoctorOpenState());
        boolean isTransferTreatmentOpened = Constants.OPEN.equals(ServiceOpenStateUtils.getTransferTreatmentOpenState());
        boolean zxzhshOpened = false;
        if (!TextUtils.isEmpty(YMApplication.getLoginInfo().getData().getZxzhsh())){
            zxzhshOpened = "1".equals(YMApplication.getLoginInfo().getData().getZxzhsh()) ? true : false;
        }
        boolean patientManagerOpened = false;
        if(isImwdOpened
                ||isQuestionSquareOpened
                ||jsdhOpened
                ||YMApplication.getLoginInfo().getData().getXiaozhan().getImwd_assign()==1){
            patientManagerOpened = true;
        }

        int imwdIconResId = isImwdOpened ? R.drawable.imwd_open : R.drawable.imwd_noraml;
        int questionSquareIconResId = isQuestionSquareOpened ? R.drawable.wtgc_open : R.drawable.wtgc_normal;
        int jsdhIconResId = jsdhOpened ? R.drawable.jsdh_open : R.drawable.jsdh_normal;
        int homeDoctorIconResId = isHomeDoctorOpened ? R.drawable.jtys_open : R.drawable.jtys_normal;
        int callDoctorIconResId = isCallDoctorOpened ? R.drawable.dhys_open : R.drawable.dhys_normal;
        int transferTreatmentIconResId = isTransferTreatmentOpened ? R.drawable.yyzz_open : R.drawable.yyzz_normal;
        int zxzshshIconResId = !TextUtils.isEmpty(YMApplication.getLoginInfo().getData().getZxzhsh())?
                    "1".equals(YMApplication.getLoginInfo().getData().getZxzhsh())? R.drawable.online_clinic_open : R.drawable.online_clinic_close
                    :R.drawable.online_clinic_close;
        int patientManagerIconResId = patientManagerOpened ? R.drawable.patient_manager_open : R.drawable.patient_manager_close;
        ArrayList<HomeGVItemBean> opendData = new ArrayList<>();
        ArrayList<HomeGVItemBean> unOpenData = new ArrayList<>();
        String imwdState = null;
        String questionSquareState = null;
        String homeDoctorState = null;
        String callDoctorState = null;
        String transferTreatmentState = null;

        final int type = YMApplication.DoctorApproveType();
        //认证通过-->做自己的跳转,不谈提示框
        if (type == 1) {
            imwdState = ServiceOpenStateUtils.getImwdOpenState();
            questionSquareState = ServiceOpenStateUtils.getQuestionSquareOpenState();
            homeDoctorState = ServiceOpenStateUtils.getHomeDoctorOpenState();
            callDoctorState = ServiceOpenStateUtils.getCallDoctorOpenState();
            transferTreatmentState = ServiceOpenStateUtils.getTransferTreatmentOpenState();
        }else {
            imwdState = Constants.APPLY_FOR_OPENING;
            questionSquareState = Constants.APPLY_FOR_OPENING;
            homeDoctorState = Constants.APPLY_FOR_OPENING;
            callDoctorState = Constants.APPLY_FOR_OPENING;
            transferTreatmentState = Constants.APPLY_FOR_OPENING;
        }
        HomeGVItemBean imwdItemBean = new HomeGVItemBean(Constants.IMWD_CN,imwdState , imwdIconResId, isImwdOpened, 0);
        HomeGVItemBean questionSquareItemBean = new HomeGVItemBean(Constants.CLUB_CN, questionSquareState, questionSquareIconResId, isQuestionSquareOpened, 0);
        HomeGVItemBean jsdhItemBean = new HomeGVItemBean(Constants.JSDH_CN, "未开通", jsdhIconResId, jsdhOpened, 0);
        HomeGVItemBean homeDoctorItemBean = new HomeGVItemBean(Constants.FAMILYDOCTOR_CN,homeDoctorState, homeDoctorIconResId, isHomeDoctorOpened, 0);
        HomeGVItemBean callDoctorItemBean = new HomeGVItemBean(Constants.DHYS_CN, callDoctorState, callDoctorIconResId, isCallDoctorOpened, 0);
        HomeGVItemBean transferTreatmentItemBean = new HomeGVItemBean(Constants.JIAHAO_CN, transferTreatmentState, transferTreatmentIconResId, isTransferTreatmentOpened, 0);
        HomeGVItemBean zxzhshItemBean = new HomeGVItemBean(Constants.ZXZHSH_CN, "", zxzshshIconResId, zxzhshOpened, 0);
        HomeGVItemBean patientManagerBean = new HomeGVItemBean(Constants.PATIENT_MANAGE_CN, "", patientManagerIconResId, patientManagerOpened, 0);

        addItemData(isImwdOpened, opendData, unOpenData, imwdItemBean);
        addItemData(isQuestionSquareOpened, opendData, unOpenData, questionSquareItemBean);
        addItemData(jsdhOpened, opendData, unOpenData, jsdhItemBean);
        addItemData(isHomeDoctorOpened, opendData, unOpenData, homeDoctorItemBean);
        addItemData(isCallDoctorOpened, opendData, unOpenData, callDoctorItemBean);
        addItemData(isTransferTreatmentOpened, opendData, unOpenData, transferTreatmentItemBean);
        addItemData(zxzhshOpened, opendData, unOpenData, zxzhshItemBean);
        addItemData(patientManagerOpened, opendData, unOpenData, patientManagerBean);

        innerItemGrid.clear();
        for (int i = 0; i < opendData.size(); i++) {
            innerItemGrid.add(opendData.get(i));
        }
        for (int i = 0; i < unOpenData.size(); i++) {
            innerItemGrid.add(unOpenData.get(i));
        }
    }

    private void addItemData(boolean isImwdOpened, ArrayList<HomeGVItemBean> opendData, ArrayList<HomeGVItemBean> unOpenData, HomeGVItemBean imwdItemBean) {
        if(isImwdOpened){
            opendData.add(imwdItemBean);
        }else {
            unOpenData.add(imwdItemBean);
        }
    }

    //问题广场中消息数 stone
    private void updateShowRedPoint(int num,String name) {
        HomeItemBean homeItemBean = null;
        if (!YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)) {
            homeItemBean = itemList.get(0);
        } else {
            homeItemBean = itemList.get(1);
        }
        if (null != homeItemBean) {
            List<HomeGVItemBean> homeGVItemDatas = homeItemBean.getHomeGVItemDatas();
            HomeGVItemBean homeGVItemBean = null;
            int index = 0;
            for (int i = 0; i < homeGVItemDatas.size(); i++) {
                homeGVItemBean = homeGVItemDatas.get(i);
                if(name.equals(homeGVItemBean.getName())){
                    homeGVItemBean.setUnreadMsgCount(num);
                    index = i;
                    break;
                }
            }
            innerItemGrid.set(index,homeGVItemBean);
            homeItemBean.setHomeGVItemDatas(innerItemGrid);
            itemList.set(0,homeItemBean);
            mAdapter.setData(itemList);
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        refresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        //之前没加载成功,重新加载 stone
        if (!mIsLoadBanner) {
            requestBannerImgs();
        }
        refresh();
    }

    @Override
    public String getStatisticalPageName() {
        return "HomeFragment";
    }

    //stone 加载过,刷新
    public void refresh() {
        if (YMApplication.getLoginInfo() != null && !YMUserService.isGuest( )) {
            getQD_Data();
        }
        //更新环信的消息提示
//        tv_notice_dot.setVisibility(Constants.ZERO_STR.equals(YMApplication.msgtotal)?View.INVISIBLE:View.VISIBLE);

        YMUserService.refreshUserInfo(getActivity(),null);//当用户认证后，申请了一个业务，每次进入这个页面，就请求下用户信息接口，刷新业务的审核状态
        initGVData();
        itemList.set(0,new HomeItemBean(innerItemGrid,HomeItemBean.TYPE_GRID));
        mAdapter.setData(itemList);
        mAdapter.notifyDataSetChanged();
        //stone 非im即时问答模块消息数
        DiscoverService.getDiscoverNoticeService(new CommonResponse<DiscoverNoticeNumberBean>() {
            @Override
            public void onNext(DiscoverNoticeNumberBean discoverNoticeNumberBean) {
                if(null != discoverNoticeNumberBean && null != discoverNoticeNumberBean.getData()){
                    updateShowRedPoint(discoverNoticeNumberBean.getData().getAsk(),Constants.CLUB_CN);
                }
            }
        });

        //im即时问答模块消息数
        if (Constants.OPEN.equals(ServiceOpenStateUtils.getImwdOpenState())) {
            ServiceProvider.getIMNewMessageNum(YMApplication.getUUid(), new CommonResponse<NewMessageNumEntity>() {
                @Override
                public void onNext(NewMessageNumEntity newMessageNumEntity) {
                    if (newMessageNumEntity != null && newMessageNumEntity.getData() != null) {
                        updateShowRedPoint(newMessageNumEntity.getData().getNum(), Constants.IMWD_CN);
                    }
                }
            });
        }
        if (null != YMApplication.getLoginInfo()
                && null != YMApplication.getLoginInfo().getData()
                && !TextUtils.isEmpty(YMApplication.getLoginInfo().getData().getJsdh())){
            if("1".equals(YMApplication.getLoginInfo().getData().getJsdh())){
                if (TextUtils.isEmpty(depa_pid)) {
                    ServiceProvider.getDoctorInfo(YMUserService.getCurUserId(), new CommonResponse<DoctorInfoEntity>() {
                        @Override
                        public void onNext(DoctorInfoEntity doctorInfoEntity) {
                            if (doctorInfoEntity.getCode() == 10000) {
                                depa_pid = doctorInfoEntity.getData().getSubject_pid();
                                getJSDH();
                            }
                        }
                    });
                }else{
                    getJSDH();
                }

            }
        }

        if (null != YMApplication.getLoginInfo()
                && null != YMApplication.getLoginInfo().getData()
                &&!TextUtils.isEmpty(YMApplication.getLoginInfo().getData().getZxzhsh())
                &&"1".equals(YMApplication.getLoginInfo().getData().getZxzhsh()) ? true : false){
            ServiceProvider.getZxzhsh(YMUserService.getCurUserId(), new CommonResponse<ZXZHSHReadBean>() {
                @Override
                public void onNext(ZXZHSHReadBean entity) {
                    if (entity.getCode() == 10000) {
                      HomeItemBean homeItemBean = itemList.get(0);
                        if (null != homeItemBean) {
                            if(entity.getData().getNum()!=0) {
                                List<HomeGVItemBean> homeGVItemDatas = homeItemBean.getHomeGVItemDatas();
                                HomeGVItemBean homeGVItemBean = null;
                                int index = 0;
                                for (int i = 0; i < homeGVItemDatas.size(); i++) {
                                    homeGVItemBean = homeGVItemDatas.get(i);
                                    if (Constants.ZXZHSH_CN.equals(homeGVItemBean.getName())){
                                        homeGVItemBean.setUnreadMsgCount(entity.getData().getNum());
                                        index = i;
                                        break;
                                    }
                                }
                                innerItemGrid.set(index, homeGVItemBean);
                                homeItemBean.setHomeGVItemDatas(innerItemGrid);
                                itemList.set(0, homeItemBean);
                                mAdapter.setData(itemList);
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            });
        }

    }

    private void getJSDH(){
        ServiceProvider.getJSDH(depa_pid, new CommonResponse<JSDHBean>() {
            @Override
            public void onNext(JSDHBean jSDHBean) {
                HomeItemBean homeItemBean = itemList.get(0);
                if (null != homeItemBean) {
                    List<HomeGVItemBean> homeGVItemDatas = homeItemBean.getHomeGVItemDatas();
                    HomeGVItemBean homeGVItemBean = null;
                    int index = 0;
                    for (int i = 0; i < homeGVItemDatas.size(); i++) {
                        homeGVItemBean = homeGVItemDatas.get(i);
                        if (Constants.JSDH_CN.equals(Constants.JSDH_CN)) {
                            homeGVItemBean.setUnreadMsgCount(jSDHBean.getData().getTotal());
                            index = i;
                            break;

                        }
                    }
                        innerItemGrid.set(index, homeGVItemBean);
                        homeItemBean.setHomeGVItemDatas(innerItemGrid);
                        itemList.set(0, homeItemBean);
                        mAdapter.setData(itemList);
                        mAdapter.notifyDataSetChanged();
                    }
                }
        });
    }

    private void updateRedPoint() {
        if(Constants.ZERO_STR.equals(YMApplication.msgtotal) && !YMApplication.getInstance().hasNewNotice()){
            tv_notice_dot.setVisibility(View.INVISIBLE);
        }else {
            tv_notice_dot.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取是否已经签到信息 stone
     */
    public void getQD_Data() {
        String userid = "";

        AjaxParams params = new AjaxParams();
        if (YMUserService.isGuest()) {
            userid = "0";
        } else {
            if(null != YMApplication.getLoginInfo().getData()){
                userid = YMApplication.getLoginInfo().getData().getPid();
            }
        }
        if(!TextUtils.isEmpty(userid)){
            String sign = CommonUtils.computeSign(userid);

            params.put("uid", userid);
            params.put("command", "qdInfo");
            params.put("sign", sign);
            FinalHttp fh = new FinalHttp();
            fh.post(CommonUrl.QDINFO_URL, params, new AjaxCallBack() {
                @Override
                public void onSuccess(String t) {
                    Gson gson = new Gson();
                    mQdinfo = gson.fromJson(t, Qdinfo.class);
                    setQdView();
                    super.onSuccess(t);
                }

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    super.onFailure(t, errorNo, strMsg);
                }
            });
        }
    }
    /**
     * 设置自定头布局
     */
    private void initCustomHeaderView() {
        if (getActivity() != null) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) gallery.getLayoutParams();
            int width = AppUtils.getScreenWidth(getActivity());
            lp.width = width;
            lp.height = width / 2;
            gallery.setLayoutParams(lp);
        }
    }

    /**
     * 请求顶部轮播图图片
     */
    private void requestBannerImgs() {
        new FinalHttp().get(CommonUrl.IMAGE_FOUCE + "&userid=" + YMUserService.getCurUserId(), new AjaxCallBack() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                mIsLoadBanner = true;
                bannerImgs.clear();

                LogUtils.d("请求轮播图成功：" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonElement = jsonArray.getJSONObject(i);
                            ImageInfo info = new ImageInfo();
                            info.setUrl(jsonElement.getString("url"));
                            info.setImgUrl(jsonElement.getString("imgUrl"));
                            info.setTitle(jsonElement.getString("title"));
                            info.setType(jsonElement.getInt("type"));
                            info.setDescription(jsonElement.getString("description"));
                            info.setArticleImgUrl(jsonElement.getString("articleImageUrl"));
                            info.setId(jsonElement.getString("id"));
                            bannerImgs.add(info);
                        }

                        startBannerCirculation(bannerImgs);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                LogUtils.d("home banner " + errorNo + ", " + strMsg);
            }
        });
    }

    /**
     * 轮播图开始轮转
     *
     * @param bannerImgs
     */
    private void startBannerCirculation(final List<ImageInfo> bannerImgs) {
        FragmentActivity activity = getActivity();
        if(null != activity){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gallery.start(bannerImgs, imageId, 3000, bannerIndicator, bannerTitle,
                            R.drawable.point_focus, R.drawable.point_default);
                    gallery.setMyOnItemClickListener(new MyGallery.MyOnItemClickListener() {
                        public void onItemClick(int curIndex) {
                            onBannerItemCLick(curIndex);
                        }
                    });
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        //点击事件 事件分发处理
        switch (v.getId()) {
            case R.id.rl_checkin:
                // 签到

                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
                } else {
                    sp.edit().putBoolean("main_isfist", true).apply();
                    if (mQdinfo == null) {
                        return;
                    }
                    if ("1".equals(mQdinfo.getData().getQd_flag())) {
                        CustomToast.showAlreadySigned(getActivity(), "您已签到", R.drawable.already_signed);
                    } else {
                        signName();
                    }

                }
                break;
            case R.id.fl_message:
                //消息 stone
                StatisticalTools.eventCount(getActivity(), Constants.NEWS);

                Intent intent = new Intent(getActivity(),MessageInfoActivity.class);
                intent.putExtra(Constants.INTENT_KEY_ISCONFLICT,((MainActivity) getActivity()).isConflict);
                intent.putExtra(Constants.INTENT_KEY_ISCURRENTACCOUNTREMOVED,((MainActivity) getActivity()).getCurrentAccountRemoved());
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void setQdView() {
        if (mQdinfo == null) {
            return;
        }
        Drawable drawableLeft = null;
        if ("1".equals(mQdinfo.getData().getQd_flag())) {
            iv_qd.setImageResource(R.drawable.qd_ok);
        } else {
            iv_qd.setImageResource(R.drawable.qd);
        }
    }

    //签到,打卡
    public void signName() {
        // 签到 stone
        StatisticalTools.eventCount(getActivity(), Constants.SIGNIN);

        String userid;

        AjaxParams params = new AjaxParams();
        if (YMUserService.isGuest()) {
            userid = "0";
        } else {
            userid = YMApplication.getLoginInfo().getData().getPid();
        }
        String sign = MD5Util.MD5(userid + Constants.MD5_KEY);

        params.put("uid", userid);
        params.put("command", "qdSave");
        params.put("sign", sign);
        params.put("score", mQdinfo.getData().getScore());
        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.QDINFO_URL, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                mQdinfo.getData().setPoint(
                        Integer.parseInt(mQdinfo.getData()
                                .getPoint()) + Integer.parseInt(mQdinfo.getData().getScore()) + "");
                mQdinfo.getData().setQd_flag("1");
                setQdView();
                if (BuildConfig.isTestServer) {
                    CustomToast.showSignName(getActivity(), "签到成功!\n+10积分", R.drawable.signname_success);
                } else {
                    CustomToast.showSignName(getActivity(), "签到成功!\n+20积分", R.drawable.signname_success);
                }

                YmRxBus.notifySignName();
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });

    }

    /**
     * 轮播图点击事件
     *
     * @param curIndex
     */
    private void onBannerItemCLick(int curIndex) {
        StatisticalTools.eventCount(getActivity(), "pic" + curIndex);
        LogUtils.d("banner_type = " + bannerImgs.get(curIndex).getType());
        if (bannerImgs.get(curIndex).getType() == 1) {
            Intent intent;
            if (bannerImgs.get(curIndex).getUrl().equals(CommonUrl.INVITE_MONEY)) {
                intent = new Intent(getActivity(), InviteMoneyActivity.class);
            } else {
                intent = new Intent(getActivity(), InfoDetailActivity.class);
                intent.putExtra("title", bannerImgs.get(curIndex).getTitle());
                intent.putExtra("url", bannerImgs.get(curIndex).getUrl());
                intent.putExtra("imageurl", bannerImgs.get(curIndex).getArticleImgUrl());
                intent.putExtra("ids", bannerImgs.get(curIndex).getId());
            }
            startActivity(intent);
        } else if (bannerImgs.get(curIndex).getType() == 2) {

            RealNameDetailActivity.start(getActivity(), bannerImgs.get(curIndex).getId());

        } else if (bannerImgs.get(curIndex).getType() == 3) { // 活动
            Intent intent = new Intent(getActivity(), CommonWebViewActivity.class);
            intent.putExtra("id", bannerImgs.get(curIndex).getId());
            intent.putExtra("imageUrl", bannerImgs.get(curIndex).getArticleImgUrl());
            String title = bannerImgs.get(curIndex).getTitle();
            if (title.contains("\n")) {
                title = title.substring(0, title.indexOf("\n"));
            }
            intent.putExtra("isfrom", title);
            intent.putExtra("content_url", bannerImgs.get(curIndex).getUrl());
            intent.putExtra("description", bannerImgs.get(curIndex).getDescription());
            if (bannerImgs.get(curIndex).getUrl().contains(CommonUrl.DISCOVER_MALL)) {
                intent.putExtra(CommonWebViewActivity.SHARE_VISIBLE_INTENT_KEY, false);
            }
            startActivity(intent);
        } else if (bannerImgs.get(curIndex).getType() == 4) {
            Intent intent = new Intent(getActivity(), NewTopicDetailActivity.class);
            intent.putExtra(NewTopicDetailActivity.TOPICID_INTENT_PARAM_KEY, Integer.parseInt(bannerImgs.get(curIndex).getId()));
            startActivity(intent);
        } else if (bannerImgs.get(curIndex).getType() == 5) {
            //试题
            AnswerMainActivity.start(getActivity());

        } else {
            if (YMUserService.isGuest()) {
                DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
            } else {
                Intent intent = new Intent(getActivity(), QuePerActivity.class);
                intent.putExtra("id", bannerImgs.get(curIndex).getId());
                intent.putExtra("imageUrl", bannerImgs.get(curIndex).getArticleImgUrl());
                intent.putExtra("isfrom", bannerImgs.get(curIndex).getTitle());
                intent.putExtra("content_url", bannerImgs.get(curIndex).getUrl() + "&time=" + System.nanoTime());
                intent.putExtra("description", bannerImgs.get(curIndex).getDescription());
                startActivity(intent);
            }
        }
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    //stone 新添加
    public enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {

        private State mCurrentState = State.IDLE;

        @Override
        public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
            if (i == 0) {
                if (mCurrentState != State.EXPANDED) {
                    onStateChanged(appBarLayout, State.EXPANDED);
                }
                mCurrentState = State.EXPANDED;
            } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
                if (mCurrentState != State.COLLAPSED) {
                    onStateChanged(appBarLayout, State.COLLAPSED);
                }
                mCurrentState = State.COLLAPSED;
            } else {
                if (mCurrentState != State.IDLE) {
                    onStateChanged(appBarLayout, State.IDLE);
                }
                mCurrentState = State.IDLE;
            }
        }

        public abstract void onStateChanged(AppBarLayout appBarLayout, State state);
    }

    /**
     * 获取分割线样式
     *
     * @return
     */
    private HSItemDecoration getItemDecoration() {

        if (getDividerLineWidth() >= 0 && getDividerColorResId() > 0) {
            return new HSItemDecoration(getActivity(), getDividerColorResId(), getDividerLineWidth());
        } else if (getDividerLineWidth() >= 0) {
            return new HSItemDecoration(getActivity(), getDividerLineWidth());
        } else if (getDividerColorResId() > 0) {
            return new HSItemDecoration(getActivity(), getDividerColorResId());
        } else {
            return new HSItemDecoration(getActivity());
        }
    }

    /**
     * 自定义分割线宽度
     *
     * @return
     */
    protected float getDividerLineWidth() {
        return -1;
    }

    /**
     * 自定义分隔线颜色 返回color的resId
     *
     * @return
     */
    protected int getDividerColorResId() {
        return 0;
    }

}
