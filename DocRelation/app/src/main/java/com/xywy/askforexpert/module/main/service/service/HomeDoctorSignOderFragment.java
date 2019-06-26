package com.xywy.askforexpert.module.main.service.service;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.adapter.RecyclerOnScrollListener;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.HttpRequstParamsUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.HomeDoctorSignOderBean;
import com.xywy.askforexpert.model.SignOrderBean;
import com.xywy.askforexpert.module.main.order.SignOdersAdapter;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 功能：服务-家庭医生-签约订单
 * </p>
 *
 * @author liuxuejiao
 * @2015-5-11 下午 17:27:30
 */
public class HomeDoctorSignOderFragment extends Fragment {
    private static final String ARG_POSITION = "position";
    private static final String TAG = "HomeDoctorSignOderFragment";
    /**
     * mListView: 签约订单列表外层view
     */
    private SwipeRefreshLayout swipeRefreshLayout;
    /**
     * mListView: 签约订单列表
     */
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    /**
     * adapter: 签约订单列表 适配器
     */
    private SignOdersAdapter mAdapter;
    /**
     * mBtnInservice: 底部服务中按钮
     */
    private Button mBtnInservice;
    /**
     * mBtnServiceDone: 底部服务完成按钮
     */
    private Button mBtnServiceDone;
    /**
     * mNoDataLayout: 没有数据空布局
     */
    private RelativeLayout mNoDataLayout;
    /**
     * mDataListInService: InService数据
     */
    private List<SignOrderBean> mDataListInService;
    /**
     * mDataListServiceDone: ServiceDone数据
     */
    private List<SignOrderBean> mDataListServiceDone;
    private int mPage = 1;
    private int mPageSize = 10;

    private LinearLayout home_doctor_bottom;

    private boolean hasMore = true;
    //是否在刷新
    private boolean mIsRefreshing = false;

    /**
     * 是否开始
     */
    private boolean mIsStart = true;

    public static HomeDoctorSignOderFragment newInstance(int position) {
        HomeDoctorSignOderFragment f = new HomeDoctorSignOderFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(
                R.layout.fragment_homedoctor_sign_order, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) v
                .findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setClipChildren(true);
        swipeRefreshLayout.setColorSchemeResources(R.color.color_scheme_2_1,
                R.color.color_scheme_2_2, R.color.color_scheme_2_3,
                R.color.color_scheme_2_4);

//		swipeRefreshLayout.post(new Runnable() {
//
//			@Override
//			public void run() {
//				if (!swipeRefreshLayout.isRefreshing()) {
//					swipeRefreshLayout.setRefreshing(true);
//				}
//			}
//		});

        home_doctor_bottom = (LinearLayout) v.findViewById(R.id.home_doctor_bottom);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mIsRefreshing;
            }
        });
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        mNoDataLayout = (RelativeLayout) v.findViewById(R.id.order_rl_no_data);
        mBtnInservice = (Button) v.findViewById(R.id.btn_homedoctor_inservice);
        mBtnServiceDone = (Button) v
                .findViewById(R.id.btn_homedoctor_servicedone);
        initUtil();
        initListener();
        initData();
        return v;

    }

    private void initData() {
        mPage = 1;
        mBtnInservice.setSelected(true);
        mBtnServiceDone.setSelected(false);
        getHttpData(HttpRequstParamsUtil.IN_SERVICE, mPage, true);
    }

    private void initListener() {
        // TODO Auto-generated method stub
        // mListViewLayout.setPullLoadEnabled(false);
        // mListViewLayout.setScrollLoadEnabled(true);
        mBtnInservice.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mPage = 1;
                mBtnInservice.setSelected(true);
                mBtnServiceDone.setSelected(false);
                getHttpData(HttpRequstParamsUtil.IN_SERVICE, mPage, true);
            }
        });
        mBtnServiceDone.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mPage = 1;
                mBtnInservice.setSelected(false);
                mBtnServiceDone.setSelected(true);
                getHttpData(HttpRequstParamsUtil.SERVICE_DONE, mPage, true);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mIsStart = true;
                        hasMore = true;
                        mPage = 1;
                        mIsRefreshing = true;
                        if (mBtnInservice.isSelected()) {
                            getHttpData(HttpRequstParamsUtil.IN_SERVICE, mPage,
                                    true);
                        } else {
                            getHttpData(HttpRequstParamsUtil.SERVICE_DONE,
                                    mPage, true);
                        }
                    }
                }, 1000);
            }
        });

        recyclerView.setOnScrollListener(new RecyclerOnScrollListener(
                linearLayoutManager) {

            @Override
            public void onLoadMore() {
                if (hasMore) {
                    mAdapter.notifyDataSetChanged();
                }
                mAdapter.setCanLoadMore(true);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mIsStart = false;
                        if (hasMore) {
                            if (mBtnInservice.isSelected()) {

                                getHttpData(HttpRequstParamsUtil.IN_SERVICE,
                                        ++mPage, false);
                            } else {
                                getHttpData(HttpRequstParamsUtil.SERVICE_DONE,
                                        ++mPage, false);
                            }
                        } else {
                            mAdapter.setCanLoadMore(false);
                            mAdapter.notifyDataSetChanged();
                        }

                    }
                }, 1000);
            }

            @Override
            public void onScrolling() {
                if (hasMore) {
                    home_doctor_bottom.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initUtil() {
        mDataListInService = new ArrayList<SignOrderBean>();
        mDataListServiceDone = new ArrayList<SignOrderBean>();
        mAdapter = new SignOdersAdapter(getActivity());
        mAdapter.setUseFooter(true);
    }

    private void getHttpData(int serviceState, int page,
                             final boolean downRefresh) {
        DLog.i(TAG, "serviceState=" + serviceState + ";;;;;page=" + page + ";;;;;downRefresh=" + downRefresh);
        AjaxParams params = new AjaxParams();
        // params.put("userid", YMApplication.login.getData().getPid());
        params.put(HttpRequstParamsUtil.ID, YMApplication.getLoginInfo()
                .getData().getPid()
                + "");
        params.put(HttpRequstParamsUtil.PAGE, mPage + "");
        params.put(HttpRequstParamsUtil.PAGE_SIZE, mPageSize + "");
        params.put(HttpRequstParamsUtil.TYPE, serviceState + "");
        params.put(HttpRequstParamsUtil.A, HttpRequstParamsUtil.SERVICE_LIST);

        params.put(
                HttpRequstParamsUtil.SIGN,
                MD5Util.MD5(YMApplication.getLoginInfo().getData().getPid()
                        + Constants.MD5_KEY));
        FinalHttp fh = new FinalHttp();

        fh.get(CommonUrl.HOMEDOCTOR_LIST, params, new AjaxCallBack() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                if ((mDataListInService.size() == 0 && mBtnInservice
                        .isSelected())
                        || (mDataListServiceDone.size() == 0 && mBtnServiceDone
                        .isSelected())) {
                    mNoDataLayout.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                } else {
                    mNoDataLayout.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                    if (mBtnInservice.isSelected()) {
                        mAdapter.setmDataList(mDataListInService, true);
                    } else {
                        mAdapter.setmDataList(mDataListServiceDone, false);
                    }
                    recyclerView.setAdapter(mAdapter);
                }
                swipeRefreshLayout.setRefreshing(false);
                mIsRefreshing = false;
                super.onFailure(t, errorNo, strMsg);
            }

            @Override
            public void onLoading(long count, long current) {
                // TODO Auto-generated method stub
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                parseJsonData(t, downRefresh);
                home_doctor_bottom.setVisibility(View.VISIBLE);
                if ((mDataListInService.size() == 0 && mBtnInservice
                        .isSelected())
                        || (mDataListServiceDone.size() == 0 && mBtnServiceDone
                        .isSelected())) {
                    mNoDataLayout.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                } else {
                    mNoDataLayout.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void parseJsonData(String json, boolean downRefresh) {
        DLog.i(TAG, "json===" + json);
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        HomeDoctorSignOderBean homeDoctorBean = new HomeDoctorSignOderBean();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            int code = ResolveJson.getjsonInt(jsonObject,
                    HttpRequstParamsUtil.CODE);
            homeDoctorBean.setCode(code);
            homeDoctorBean.setMsg(ResolveJson.getjsonString(jsonObject,
                    HttpRequstParamsUtil.MSG));
            homeDoctorBean.setIs_more(ResolveJson.getjsonBoolean(jsonObject,
                    HttpRequstParamsUtil.IS_MORE));
            hasMore = ResolveJson.getjsonBoolean(jsonObject,
                    HttpRequstParamsUtil.IS_MORE);
            if (jsonObject.has(HttpRequstParamsUtil.LIST)) {
                ArrayList<SignOrderBean> list = new ArrayList<SignOrderBean>();
                JSONArray jsonArray = jsonObject
                        .getJSONArray(HttpRequstParamsUtil.LIST);
                for (int i = 0; i < jsonArray.length(); i++) {
                    SignOrderBean signOrderBean = new SignOrderBean();
                    JSONObject data = jsonArray.getJSONObject(i);
                    signOrderBean.setDocname(ResolveJson.getjsonString(data,
                            "docname"));
                    signOrderBean.setDocid(ResolveJson.getjsonString(data,
                            "docid"));
                    signOrderBean.setAmount(ResolveJson.getjsonString(data,
                            "amount"));
                    signOrderBean.setPhoto(ResolveJson.getjsonString(data,
                            "photo"));
                    signOrderBean.setScore(ResolveJson.getjsonString(data,
                            "score"));
                    signOrderBean.setCategory(ResolveJson.getjsonString(data,
                            "category"));
                    signOrderBean.setOrederTime(ResolveJson.getjsonString(data,
                            "ordertime"));
                    list.add(signOrderBean);
                }
                homeDoctorBean.setList(list);
                if (downRefresh) {
                    if (mBtnInservice.isSelected()) {
                        mDataListInService.clear();
                        mDataListInService.addAll(homeDoctorBean.getList());
                    } else {
                        mDataListServiceDone.clear();
                        mDataListServiceDone.addAll(homeDoctorBean.getList());
                    }
                } else {
                    if (mBtnInservice.isSelected()) {
                        mDataListInService.addAll(homeDoctorBean.getList());
                    } else {
                        mDataListServiceDone.addAll(homeDoctorBean.getList());
                    }
                }

                if (mIsStart) {
                    if (mBtnInservice.isSelected()) {
                        if (mDataListInService.size() >= 10) {
                            mAdapter.setUseFooter(true);
                        } else {
                            mAdapter.setUseFooter(false);
                        }
                    } else {
                        if (mDataListServiceDone.size() >= 10) {
                            mAdapter.setUseFooter(true);
                        } else {
                            mAdapter.setUseFooter(false);
                        }
                    }
                }

                if (mBtnInservice.isSelected()) {
                    mAdapter.setmDataList(mDataListInService, true);
                } else {
                    mAdapter.setmDataList(mDataListServiceDone, false);
                }

                if (mIsStart) {
                    recyclerView.setAdapter(mAdapter);
                } else {
                    mAdapter.notifyDataSetChanged();
                }
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                mIsRefreshing = false;
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void onResume() {
        super.onResume();
        StatisticalTools.fragmentOnResume("HomeDoctorSignOderFragment");
    }

    public void onPause() {
        super.onPause();
        StatisticalTools.fragmentOnPause("HomeDoctorSignOderFragment");
    }
}