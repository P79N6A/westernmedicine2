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
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.adapter.RecyclerOnScrollListener;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.HttpRequstParamsUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.model.HomeDoctorUserComBean;
import com.xywy.askforexpert.model.UserCommentBean;
import com.xywy.askforexpert.module.doctorcircle.adapter.UserCommentAdapter;

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
 * <p>
 * 功能：服务-家庭医生-用户评价
 * </p>
 *
 * @author liuxuejiao
 * @2015-5-11 下午 18:27:30
 */
public class HomeDoctorCommentFragment extends Fragment {
    private static final String ARG_POSITION = "position";
    /**
     * mListView: 用户评价列表
     */
    private RecyclerView recyclerView;
    /**
     * mListView: 用户评价列表外层view
     */
    private SwipeRefreshLayout swipeRefreshLayout;
    /**
     * adapter: 用户评价列表 适配器
     */
    private UserCommentAdapter mAdapter;
    /**
     * mDataList: 数据
     */
    private List<UserCommentBean> mDataList;
    /**
     * mNoDataLayout: 没有数据空布局
     */
    private RelativeLayout mNoDataLayout;
    /**
     * mTxtTotalScore: 每页平均分
     */
    private TextView mTxtTotalScore;
    /**
     * mTxtRank: 总排名
     */
    private TextView mTxtRank;
    /**
     * mRatingBar: 平均分
     */
    private RatingBar mRatingBar;

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");

    private int mPage = 1;
    private int mPageSize = 10;
    /**
     * 是否开始
     */
    private boolean mIsStart;
    private boolean isMore;
    private LinearLayoutManager linearLayoutManager;
    /**
     * 是否在刷新
     */
    private boolean mIsRefreshing = false;

    public static HomeDoctorCommentFragment newInstance(int position) {
        HomeDoctorCommentFragment f = new HomeDoctorCommentFragment();
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

        final View v = inflater.inflate(
                R.layout.fragment_homedoctor_usercomment, container, false);
        initView(v);
        initUtil();
        initListener();
        initData();

        return v;

    }

    private void initData() {
        // TODO Auto-generated method stub
        recyclerView.setAdapter(mAdapter);
        mIsStart = true;
        isMore = true;
        getData();
    }

    private void getData() {
        if (mIsStart) {
            mPage = 1;
            getHttpData(mPage, mIsStart);
        } else {
            mPage++;
            if (isMore) {
                getHttpData(mPage, mIsStart);
                mAdapter.setCanLoadMore(true);
            } else {
                mAdapter.setCanLoadMore(false);
                mAdapter.notifyDataSetChanged();
            }

        }
    }

    private void initListener() {
        // TODO Auto-generated method stub
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mIsStart = true;
                        isMore = true;
                        mIsRefreshing = true;
                        getData();
                    }
                }, 1000);
            }
        });

        recyclerView.setOnScrollListener(new RecyclerOnScrollListener(
                linearLayoutManager) {

            @Override
            public void onLoadMore() {
                if (!swipeRefreshLayout.isRefreshing()) {
                    if (isMore) {
                        mAdapter.notifyDataSetChanged();
                    }
                    mAdapter.setCanLoadMore(true);
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            mIsStart = false;
                            getData();
                        }
                    }, 1000);
                }
            }

            @Override
            public void onScrolling() {
                // TODO Auto-generated method stub

            }
        });
    }

    private void initUtil() {
        // TODO Auto-generated method stub
        mDataList = new ArrayList<UserCommentBean>();
        mAdapter = new UserCommentAdapter(getActivity(), mDataList);
        mAdapter.setUseFooter(true);
    }

    private void initView(View view) {
        // TODO Auto-generated method stub
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
        mRatingBar = (RatingBar) view
                .findViewById(R.id.commentlist_top_ratingbar);
        mTxtTotalScore = (TextView) view.findViewById(R.id.comm_totalscore);
        mTxtRank = (TextView) view.findViewById(R.id.commentlist_top_percent);

        mNoDataLayout = (RelativeLayout) view.findViewById(R.id.rl_no_data);

//		swipeRefreshLayout.post(new Runnable() {
//
//			@Override
//			public void run() {
//				if (!swipeRefreshLayout.isRefreshing()) {
//					swipeRefreshLayout.setRefreshing(true);
//				}
//			}
//		});
    }

    private void getHttpData(int page, final boolean downRefresh) {

        AjaxParams params = new AjaxParams();
        // params.put("userid", YMApplication.login.getData().getPid());
        params.put(HttpRequstParamsUtil.ID, YMApplication.getLoginInfo()
                .getData().getPid()
                + "");
        params.put(HttpRequstParamsUtil.PAGE, mPage + "");
        params.put(HttpRequstParamsUtil.PAGE_SIZE, mPageSize + "");
        params.put(HttpRequstParamsUtil.A, HttpRequstParamsUtil.DOCTOR_COMMENT);

        params.put(
                HttpRequstParamsUtil.SIGN,
                MD5Util.MD5(YMApplication.getLoginInfo().getData().getPid()
                        + Constants.MD5_KEY));
        FinalHttp fh = new FinalHttp();

        fh.get(CommonUrl.HOMEDOCTOR_LIST, params, new AjaxCallBack() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
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

                if (mDataList.size() == 0) {
                    mNoDataLayout.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                    mTxtTotalScore.setText(String.format(getResources()
                                    .getString(R.string.home_doctor_txt_comment_score),
                            0));
                    mRatingBar.setClickable(false);
                    mRatingBar.setRating(0f);
                    // mTxtRank.setText(String.format(
                    // getResources().getString(
                    // R.string.home_doctor_txt_comment_percent),
                    // "0"));
                    mTxtRank.setText("打败0个医生");
                } else {
                    mNoDataLayout.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void parseJsonData(String json, boolean downRefresh) {
        HomeDoctorUserComBean homeDoctorBean = new HomeDoctorUserComBean();
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
            isMore = ResolveJson.getjsonBoolean(jsonObject,
                    HttpRequstParamsUtil.IS_MORE);
            homeDoctorBean.setRank(ResolveJson.getjsonString(jsonObject,
                    HttpRequstParamsUtil.RANK));
            homeDoctorBean.setTotalscore(ResolveJson.getjsonFloat(jsonObject,
                    HttpRequstParamsUtil.TOTALSCORE));
            if (jsonObject.has(HttpRequstParamsUtil.LIST)) {
                ArrayList<UserCommentBean> list = new ArrayList<UserCommentBean>();
                JSONArray jsonArray = jsonObject
                        .getJSONArray(HttpRequstParamsUtil.LIST);
                for (int i = 0; i < jsonArray.length(); i++) {
                    UserCommentBean userCommentBean = new UserCommentBean();
                    JSONObject data = jsonArray.getJSONObject(i);
                    userCommentBean.setG_uname(ResolveJson.getjsonString(data,
                            "g_uname"));
                    userCommentBean.setG_ruid(ResolveJson.getjsonString(data,
                            "g_ruid"));
                    userCommentBean.setG_stat(ResolveJson.getjsonString(data,
                            "g_stat"));
                    userCommentBean.setG_date(ResolveJson.getjsonString(data,
                            "g_date"));
                    userCommentBean.setG_cons(ResolveJson.getjsonString(data,
                            "g_cons"));

                    list.add(userCommentBean);
                }
                homeDoctorBean.setList(list);
                if (downRefresh) {
                    mDataList.clear();
                }
                mDataList.addAll(homeDoctorBean.getList());
                mAdapter.setmDataList(mDataList);
                mIsRefreshing = false;
                if (isAdded()) {
                    mTxtTotalScore.setText(String.format(
                            getResources().getString(R.string.home_doctor_txt_comment_score),
                            String.valueOf(homeDoctorBean.getTotalscore())));
                }
                mRatingBar.setRating(homeDoctorBean.getTotalscore());
                mTxtRank.setText(String.format(
                        getResources().getString(
                                R.string.home_doctor_txt_comment_percent),
                        homeDoctorBean.getRank(), "%"));
                swipeRefreshLayout.setRefreshing(false);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void onResume() {
        super.onResume();
        StatisticalTools.fragmentOnResume("HomeDoctorCommentFragment");
    }

    public void onPause() {
        StatisticalTools.fragmentOnPause("HomeDoctorCommentFragment");
        super.onPause();
    }
}
