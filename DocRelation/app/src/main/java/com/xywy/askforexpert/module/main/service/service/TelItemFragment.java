package com.xywy.askforexpert.module.main.service.service;

import android.content.Intent;
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
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.adapter.RecyclerOnScrollListener;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.model.TelItem;
import com.xywy.askforexpert.module.message.adapter.TelDocAdapter;
import com.xywy.askforexpert.module.message.adapter.TelDocAdapter.OnItemClickListener;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TelItemFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView recyclerView;

    private LinearLayoutManager linearLayoutManager;

    private RelativeLayout noDataLayout;

    private ListView telListView;

    private TelDocAdapter telDocAdapter;

    private List<TelItem> telItems = new ArrayList<TelItem>();

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");

    private int state;

    private int page = 1;

    private int pageSize = 10;

    private int pageCount;

    private int code = -1;
    private String msg = "";

    private boolean mIsStart;

    private boolean hasMoreData;

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
                telDocAdapter.bindData(telItems);
                if (telItems.size() == 0) {
//					telPullToRefreshListView.setHasMoreData(false);
                    noDataLayout.setVisibility(View.VISIBLE);
                } else {
                    noDataLayout.setVisibility(View.GONE);
                }

                if (mIsStart) {
                    recyclerView.setAdapter(telDocAdapter);
//					telPullToRefreshListView.onPullDownRefreshComplete();
                } else {
                    telDocAdapter.notifyDataSetChanged();
//					telPullToRefreshListView.onPullDownRefreshComplete();
                }

//				telListView.setOnItemClickListener(new OnItemClickListener() {
//
//					@Override
//					public void onItemClick(AdapterView<?> parent, View view,
//							int position, long id) {
//						Intent intent = new Intent(getActivity(),
//								TelDetailActivity.class);
//						intent.putExtra("orderId", telItems.get(position)
//								.getId());
//						intent.putExtra("state", state+"");
//						startActivity(intent);
//					}
//				});

                telDocAdapter.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(int position, Object object) {
                        Intent intent = new Intent(getActivity(),
                                TelDetailActivity.class);
                        intent.putExtra("orderId", telItems.get(position)
                                .getId());
                        intent.putExtra("state", state + "");
                        startActivity(intent);
                    }
                });
            }
        }
    };

    public TelItemFragment() {
    }

    public static TelItemFragment newInstance(int state) {
        TelItemFragment fragment = new TelItemFragment();
        Bundle args = new Bundle();
        args.putInt("state", state);
        fragment.setArguments(args);

        return fragment;
    }

    //	public TelItemFragment(int state) {
//		this.state = state;
//	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.state = getArguments().getInt("state");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.tel_fragment_item, container,
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


        telDocAdapter = new TelDocAdapter(getActivity());

        mIsStart = true;
        hasMoreData = true;
//		telDocAdapter.setUseFooter(true);
        showListData();

//		telPullToRefreshListView
//				.setOnRefreshListener(new OnRefreshListener<ListView>() {
//
//					@Override
//					public void onPullDownToRefresh(
//							PullToRefreshBase<ListView> refreshView) {
//						// TODO Auto-generated method stub
//						mIsStart = true;
//						showListData();
//					}
//
//					@Override
//					public void onPullUpToRefresh(
//							PullToRefreshBase<ListView> refreshView) {
//						mIsStart = false;
//						showListData();
//					}
//				});
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mIsStart = true;
                        hasMoreData = true;
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
                if (hasMoreData) {
                    telDocAdapter.notifyDataSetChanged();
                }
                telDocAdapter.setCanLoadMore(true);
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
            getData(CommonUrl.QUE_TEL_LIST, page);
        } else {
            page++;
            if ((page * pageSize) > pageCount) {
                hasMoreData = false;
                telDocAdapter.setCanLoadMore(false);
                telDocAdapter.notifyDataSetChanged();
            } else {
                getData(CommonUrl.QUE_TEL_LIST, page);
                telDocAdapter.setCanLoadMore(true);
            }
        }
    }

    public void getData(String url, int page) {
        AjaxParams params = new AjaxParams();
        params.put("userid", YMApplication.getLoginInfo().getData().getPid());
        params.put("state", state + "");
        params.put("page", page + "");
        params.put("pagesize", pageSize + "");
        params.put(
                "sign",
                MD5Util.MD5(YMApplication.getLoginInfo().getData().getPid()
                        + Constants.MD5_KEY));
        FinalHttp fh = new FinalHttp();

        fh.post(url, params, new AjaxCallBack() {

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
                parseTelListJsonData(t);
            }
        });
    }

    private void parseTelListJsonData(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            code = jsonObject.getInt("code");
            msg = jsonObject.getString("msg");
            if (code == 0) {
                if (mIsStart) {
                    telItems.clear();
                    swipeRefreshLayout.setRefreshing(false);
                }
                JSONObject jsonElement = jsonObject.getJSONObject("data");
                pageCount = jsonElement.getInt("total");
                JSONArray jsonArray = jsonElement.getJSONArray("list");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonChild = jsonArray.getJSONObject(i);
                    TelItem item = new TelItem();
                    item.setId(jsonChild.getString("id"));
                    item.setOrderNum(jsonChild.getString("order_num"));
                    item.setFee(jsonChild.getString("fee"));
                    item.setRealname(jsonChild.getString("realname"));
                    item.setState(jsonChild.getString("state"));
                    item.setStateName(jsonChild.getString("state_name"));
                    item.setTalkTime(jsonChild.getString("talk_time"));
                    telItems.add(item);
                }

                if (mIsStart) {
                    if (telItems.size() >= 10) {
                        telDocAdapter.setUseFooter(true);
                    } else {
                        telDocAdapter.setUseFooter(false);
                    }
                }
                Message msg = Message.obtain();
                msg.what = 1;
                handler.sendMessage(msg);
                mIsRefreshing = false;
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void onResume() {
        super.onResume();
        StatisticalTools.fragmentOnResume("TelItemFragment");
    }

    public void onPause() {
        super.onPause();
        StatisticalTools.fragmentOnPause("TelItemFragment");
    }
}
