package com.xywy.askforexpert.module.main.service.service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.adapter.RecyclerOnScrollListener;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.QuestionSquareMsgItem;
import com.xywy.askforexpert.module.main.service.MakeAppointAdapter;
import com.xywy.askforexpert.module.main.service.MakeAppointAdapter.OnItemClickListener;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：D_Platform 类名称：MakeAppointFragment 类描述：功能：服务-预约加号item(等待确认、等待就诊、成功就诊、爽约)
 * 创建人：shihao 创建时间：2015-5-23 下午4:04:05 修改备注：
 */

public class MakeAppointFragment extends Fragment {

    private static final String TAG = "MakeAppointFragment";
    private String state;
    private int index;

    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView recyclerView;

    private LinearLayoutManager linearLayoutManager;

    private MakeAppointAdapter mMakeAppointAdapter;

    private int page = 1;

    private int pageSize = 10;

    private int totalSize;

    private List<QuestionSquareMsgItem> mDataList = new ArrayList<QuestionSquareMsgItem>();

    private RelativeLayout noDataLayout;

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");

    /**
     * 是否开始
     */
    private boolean mIsStart;
    /**
     * 是否有更多数据
     */
    private boolean isMore;
    /**
     * 是否在刷新
     */
    private boolean mIsRefreshing = false;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (mDataList.size() == 0) {
                    noDataLayout.setVisibility(View.VISIBLE);
                } else {
                    noDataLayout.setVisibility(View.GONE);
                }

                mMakeAppointAdapter.bindData(mDataList);

                if (mIsStart) {
                    recyclerView.setAdapter(mMakeAppointAdapter);
                } else {
                    mMakeAppointAdapter.notifyDataSetChanged();
                }

                mMakeAppointAdapter.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(int position, Object object) {
                        Intent intent = new Intent(getActivity(),
                                MAppCheckPendingActivity.class);
                        String time = mDataList.get(position).getCreateTime()
                                + "    " + mDataList.get(position).getWeek()
                                + "    " + mDataList.get(position).getHalfday();
                        intent.putExtra("url", mDataList.get(position).getUrl());
                        intent.putExtra("id", mDataList.get(position).getId()
                                + "");
                        intent.putExtra("time", time);
                        intent.putExtra("show", mDataList.get(position)
                                .getIsConfirm());
                        intent.putExtra("audit", mDataList.get(position)
                                .getIsAudit());
                        intent.putExtra("state", state);

                        startActivity(intent);
                    }
                });
            }
        }
    };

    public MakeAppointFragment() {

    }

    public static MakeAppointFragment newInstance(String state, int index) {
        MakeAppointFragment fragment = new MakeAppointFragment();
        Bundle args = new Bundle();
        args.putString("state", state);
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }

//	public MakeAppointFragment(String state, int index) {
//		this.state = state;
//		this.index = index;
//	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        this.state = arguments.getString("state");
        this.index = arguments.getInt("index");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_make_app, container,
                false);

        swipeRefreshLayout = (SwipeRefreshLayout) view
                .findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setClipChildren(true);

        swipeRefreshLayout.setColorSchemeResources(R.color.color_scheme_2_1,
                R.color.color_scheme_2_2, R.color.color_scheme_2_3,
                R.color.color_scheme_2_4);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mIsRefreshing;
            }
        });
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        noDataLayout = (RelativeLayout) view.findViewById(R.id.rl_no_data);

        swipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {
                if (!swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(true);
                }
            }
        });

        mMakeAppointAdapter = new MakeAppointAdapter(getActivity(), state);
        mIsStart = true;
        isMore = true;
//		mMakeAppointAdapter.setUseFooter(true);
        showListData();

        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mIsStart = true;
                        isMore = true;
                        mIsRefreshing = true;
                        showListData();
                    }
                }, 1000);
            }
        });

        recyclerView.setOnScrollListener(new RecyclerOnScrollListener(
                linearLayoutManager) {

            @Override
            public void onLoadMore() {
                if (isMore) {
                    mMakeAppointAdapter.notifyDataSetChanged();
                }
                mMakeAppointAdapter.setCanLoadMore(true);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mIsStart = false;
                        showListData();
                    }
                }, 1000);
            }

            @Override
            public void onScrolling() {
                // TODO Auto-generated method stub

            }
        });
        return view;
    }

    private void showListData() {
        if (mIsStart) {
            page = 1;
            getHttpData(page, state);
        } else {
            page++;
            if ((page * pageSize) > totalSize) {
                isMore = false;
                mMakeAppointAdapter.setCanLoadMore(false);
                mMakeAppointAdapter.notifyDataSetChanged();
            } else {
                getHttpData(page, state);
                mMakeAppointAdapter.setCanLoadMore(true);
            }
        }
    }

    private void getHttpData(int page, String state) {
        AjaxParams params = new AjaxParams();
        params.put("userid", YMApplication.getLoginInfo().getData().getPid());
        params.put("state", state);
        params.put("page", page + "");
        params.put("pagesize", pageSize + "");
        params.put(
                "sign",
                MD5Util.MD5(YMApplication.getLoginInfo().getData().getPid()
                        + Constants.MD5_KEY));
        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.MAKE_ADD_NUM_LIST, params,
                new AjaxCallBack() {

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        // TODO Auto-generated method stub
                        super.onFailure(t, errorNo, strMsg);
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onLoading(long count, long current) {
                        // TODO Auto-generated method stub
                        super.onLoading(count, current);
                    }

                    @Override
                    public void onSuccess(String t) {
                        // TODO Auto-generated method stub
                        super.onSuccess(t);
                        DLog.i(TAG, "JSON==" + t);
                        parseJsonData(t);
                    }
                });
    }

    private void parseJsonData(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            int code = jsonObject.getInt("code");
            String msg = jsonObject.getString("msg");
            if (code == 0) {
                if (mIsStart) {
                    mDataList.clear();
                    swipeRefreshLayout.setRefreshing(false);
                }
                JSONObject jsonElement = jsonObject.getJSONObject("data");
                totalSize = jsonElement.getInt("total");
                JSONArray array = jsonElement.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonChild = array.getJSONObject(i);
                    QuestionSquareMsgItem queData = new QuestionSquareMsgItem();
                    queData.setId(jsonChild.getInt("id"));
                    queData.setSex(jsonChild.getString("sex"));
                    queData.setAge(jsonChild.getString("age"));
                    queData.setBirthday(jsonChild.getString("birthday"));
                    queData.setTodate(jsonChild.getString("todate"));
                    queData.setName(jsonChild.getString("name"));
                    queData.setWeek(jsonChild.getString("week"));
                    queData.setHalfday(jsonChild.getString("halfday"));
                    queData.setCreateTime(jsonChild.getString("date"));
                    queData.setUrl(jsonChild.getString("url"));
                    queData.setIsAudit(jsonChild.getString("is_audit"));
                    queData.setIsConfirm(jsonChild.getString("is_confirm"));
                    mDataList.add(queData);
                }
                if (mIsStart) {
                    if (mDataList.size() >= 10) {
                        mMakeAppointAdapter.setUseFooter(true);
                    } else {
                        mMakeAppointAdapter.setUseFooter(false);
                    }
                }
                Message message = Message.obtain();
                message.what = 1;
                handler.sendMessage(message);
                mIsRefreshing = false;
            } else {
                ToastUtils.shortToast( msg);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        StatisticalTools.fragmentOnResume("MakeAppointFragment");
        SharedPreferences preferences = getActivity().getSharedPreferences(
                "isskip", getActivity().MODE_PRIVATE);
        boolean flag = preferences.getBoolean("skip", false);
        DLog.i(TAG, "onresume = " + flag + "(getString)state==" + preferences.getString("state", 1 + "") + ",state==" + state);
        if (preferences.getString("state", 1 + "").equals(state) && flag == true) {
            mIsStart = true;
            mDataList = null;
            mDataList = new ArrayList<QuestionSquareMsgItem>();
            showListData();
            SharedPreferences.Editor edit = getActivity().getSharedPreferences(
                    "isskip", getActivity().MODE_PRIVATE).edit();
            edit.putBoolean("skip", false);
            edit.commit();
        }

    }

    public void onPause() {
        super.onPause();
        StatisticalTools.fragmentOnPause("MakeAppointFragment");
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
