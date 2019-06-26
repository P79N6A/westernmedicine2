package com.xywy.askforexpert.module.doctorcircle.topic;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.topics.MoreTopicItem;
import com.xywy.askforexpert.model.topics.MyTopic;
import com.xywy.askforexpert.module.docotorcirclenew.activity.NewTopicDetailActivity;
import com.xywy.askforexpert.module.doctorcircle.topic.adapter.MoreItemAdapter;
import com.xywy.askforexpert.module.doctorcircle.topic.adapter.MyTopicAdapter;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 更多话题itemFragment
 *
 * @author shihao
 *         created at 16/4/25 下午3:25
 */
@SuppressLint("ValidFragment")
public class MoreTopicItemFragment extends Fragment {

    private static final String TAG = MoreTopicItemFragment.class.getSimpleName();
    private final int pageSize = 10;
    @Bind(R.id.more_topic_lv)
    ListView moreTopicLv;
    @Bind(R.id.refresh_more_topic)
    SwipeRefreshLayout refreshMoreTopic;
    @Bind(R.id.refresh_my_topic)
    SwipeRefreshLayout refreshMyTopic;
    @Bind(R.id.more_mytopic_lv)
    ListView moreMytopicLv;
    @Bind(R.id.empty_show_rl)
    RelativeLayout emptyShowRl;
    /**
     * 当前位置
     */
    private int mNum;
    /**
     * 普通item Adapter，前3个
     */
    private MoreItemAdapter mAdapter = null;
    /**
     * 我的话题页Adapter
     */
    private MyTopicAdapter myAdapter;
    private List<MoreTopicItem.ListEntity> infoDataList = new ArrayList<>();
    private List<MyTopic.DataEntity> topicEntityList = new ArrayList<>();
    private FinalHttp fh;
    private String userId = "";
    private Gson gson = new Gson();
    private int allPageNum;
    private int page = 1;
    private View footView;
    private boolean isBottom = false;
    private boolean isLoading = false;

    private boolean isFirst = true;

    public MoreTopicItemFragment() {
    }

    public MoreTopicItemFragment(int num) {
        mNum = num;
        DLog.i(TAG, "MoreTopicItemFragment构造方法" + mNum);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DLog.i(TAG, "onCreate");
        fh = new FinalHttp();
        userId = YMApplication.getLoginInfo().getData().getPid();
        if (userId == null) {
            userId = "";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DLog.i(TAG, "onCreateView" + mNum);
        View view = inflater.inflate(R.layout.fragment_more_topic, container, false);
        ButterKnife.bind(this, view);
        if (mNum == 3) {
            DLog.i(TAG, "当前我的页面" + mNum);
            refreshMyTopic.setColorSchemeResources(R.color.color_scheme_2_1, R.color.color_scheme_2_2,
                    R.color.color_scheme_2_3, R.color.color_scheme_2_4);
            refreshMoreTopic.setVisibility(View.GONE);
            refreshMyTopic.setVisibility(View.VISIBLE);
            //go to login
            getMyTopicData(false);
            refreshMyTopic.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getMyTopicData(true);
                }
            });
        } else {
            footView = LayoutInflater.from(getActivity()).inflate(R.layout.loading_more, null);
            if (moreTopicLv.getFooterViewsCount() == 0) {
                moreTopicLv.addFooterView(footView);
            }
            if (footView != null && footView.getVisibility() == View.VISIBLE) {
                footView.setVisibility(View.GONE);
            }
            refreshMoreTopic.setColorSchemeResources(R.color.color_scheme_2_1, R.color.color_scheme_2_2,
                    R.color.color_scheme_2_3, R.color.color_scheme_2_4);
            refreshMoreTopic.setVisibility(View.VISIBLE);
            refreshMyTopic.setVisibility(View.GONE);
            page = 1;
            getMoreTopicData(mNum + 1, false, page);
            moreTopicLv.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    if (isBottom && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && !isLoading) {
                        DLog.i(TAG, "加载更多scroll bottom");
                        isLoading = true;
                        if (moreTopicLv.getFooterViewsCount() == 0) {
                            moreTopicLv.addFooterView(footView);
                        }
                        if (footView.getVisibility() == View.GONE) {
                            footView.setVisibility(View.VISIBLE);
                        }
                        if (NetworkUtil.isNetWorkConnected()) {
                            page = page + 1;
                            getMoreTopicData(mNum + 1, false, page);
                        } else {
                            isBottom = false;
                            Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();
                            if (footView != null && footView.getVisibility() == View.VISIBLE) {
                                footView.setVisibility(View.GONE);
                            }
                        }
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    isBottom = ((firstVisibleItem + visibleItemCount) == totalItemCount);
                }
            });
            moreTopicLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Toast.makeText(getActivity(), "点击了第" + position + "行", Toast.LENGTH_SHORT).show();
                    if (mNum == 0) {
                        StatisticalTools.eventCount(getActivity(), "yqTopicMoreRecomTopic");
                    } else if (mNum == 1) {
                        StatisticalTools.eventCount(getActivity(), "yqTopicMoreHotTopic");
                    } else if (mNum == 2) {
                        StatisticalTools.eventCount(getActivity(), "yqTopicMoreNewTopic");
                    }
                    Intent intent = new Intent(getActivity(), NewTopicDetailActivity.class);
                    intent.putExtra(NewTopicDetailActivity.TOPICID_INTENT_PARAM_KEY, mAdapter.getItem(position).getId());
                    startActivity(intent);
                }
            });
            refreshMoreTopic.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    page = 1;
                    infoDataList.clear();
                    if (moreTopicLv.getFooterViewsCount() == 0) {
                        moreTopicLv.addFooterView(footView);
                    }
                    getMoreTopicData(mNum + 1, true, page);
                }
            });
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DLog.i(TAG, "onActivityCreated");
        //setAdapter
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirst && mNum == 3) {
            getMyTopicData(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isFirst = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (refreshMoreTopic != null) {
            refreshMoreTopic.setRefreshing(false);
        }
        if (refreshMyTopic != null) {
            refreshMyTopic.setRefreshing(false);
        }
    }

    /**
     * 更多 我的话题
     */
    private void getMyTopicData(final boolean isRefresh) {
//        http://test.yimai.api.xywy.com/app/1.2/index.interface.php?m=theme_mylist&a=theme&userid=65173122&debug=1&bind=65173122&sign=7c6c9a80020b701c2dc2b8efefd72517&debug=1
        if (!NetworkUtil.isNetWorkConnected()) {
            Toast.makeText(getActivity(), "请检查网络设置", Toast.LENGTH_SHORT).show();
            return;
        }
        AjaxParams params = new AjaxParams();
        params.put("m", "theme_mylist");
        params.put("a", "theme");
        params.put("userid", userId);
        params.put("bind", userId);
        params.put("sign", MD5Util.MD5(userId + Constants.MD5_KEY));
        DLog.i(TAG, "more my topic url" + CommonUrl.doctor_circo_url + "?" + params.toString());
        fh.post(CommonUrl.doctor_circo_url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                if (refreshMoreTopic != null) {
                    refreshMyTopic.setRefreshing(false);
                }
//                DLog.i(TAG, "更多话题页我的" + s);
                try {
                    MyTopic myTopic = gson.fromJson(s, MyTopic.class);
                    if (myTopic.getCode() == 0) {
                        topicEntityList = myTopic.getData();
                        if (topicEntityList != null && !topicEntityList.isEmpty()) {
                            emptyShowRl.setVisibility(View.GONE);
                            if (isRefresh) {
                                if (myAdapter != null) {
                                    myAdapter.bindData(topicEntityList);
                                }
                                return;
                            }
                            myAdapter = new MyTopicAdapter(getActivity(), topicEntityList);
                            moreMytopicLv.setAdapter(myAdapter);

                        } else {
                            emptyShowRl.setVisibility(View.VISIBLE);
                        }
                    } else {
                        emptyShowRl.setVisibility(View.VISIBLE);
                        topicEntityList.clear();
                        myAdapter.bindData(topicEntityList);
                    }

                } catch (Exception e) {
                    DLog.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (refreshMoreTopic != null) {
                    refreshMyTopic.setRefreshing(false);
                }
                if (emptyShowRl != null) {
                    emptyShowRl.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * 更多话题页
     *
     * @param type 推荐1，热门2，最新3
     */
    private void getMoreTopicData(int type, final boolean isRefresh, final int page) {
        DLog.i(TAG, "page ===" + page);
        if (!NetworkUtil.isNetWorkConnected()) {
            Toast.makeText(getActivity(), "请检查网络设置", Toast.LENGTH_SHORT).show();
            return;
        }
        AjaxParams params = new AjaxParams();
        params.put("m", "theme_list");
        params.put("a", "theme");
        params.put("bind", type + "");
        params.put("type", type + "");
        params.put("page", page + "");
        params.put("sign", MD5Util.MD5(type + Constants.MD5_KEY));
        DLog.i(TAG, "more topic url" + CommonUrl.doctor_circo_url + "?" + params.toString());
        fh.post(CommonUrl.doctor_circo_url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                if (refreshMoreTopic != null) {
                    refreshMoreTopic.setRefreshing(false);
                }
                isLoading = false;
//                DLog.i(TAG, "更多话题页" + s);
                try {
                    MoreTopicItem moreItem = gson.fromJson(s, MoreTopicItem.class);
                    int pageTotal = moreItem.getTotal();

                    allPageNum = (pageTotal + pageSize - 1) / pageSize;      //总条数
                    DLog.i(TAG, "总条数：" + allPageNum + "当前第" + page + "页");
                    if (page >= allPageNum) {
                        if (moreTopicLv.getFooterViewsCount() != 0) {
                            moreTopicLv.removeFooterView(footView);
                        }
                    }
                    List<MoreTopicItem.ListEntity> list = moreItem.getList();
                    if (list != null && !list.isEmpty()) {

                        if (moreItem.getCode() == 0) {
                            //在此页面刷新,mAdapter!=null
                            if (isRefresh && mAdapter != null) {
                                DLog.i(TAG, "在此页面刷新,mAdapter!=null");
                                infoDataList.clear();
                                infoDataList = list;
                                mAdapter.bindData(infoDataList);
                                return;
                            }
                            //进入页面 ，但是没有刷新
                            if (page == 1 && !isRefresh) {
                                DLog.i(TAG, "进入页面 ，但是没有刷新");
                                infoDataList.addAll(list);
                                mAdapter = new MoreItemAdapter(getActivity(), infoDataList);
                                moreTopicLv.setAdapter(mAdapter);
                                return;
                            }
                            //加载更多
                            if (page > 1 && page <= allPageNum) {
                                DLog.i(TAG, "加载更多");
                                infoDataList.addAll(list);
                                mAdapter.bindData(infoDataList);
                            } else {
                                if (footView != null && footView.getVisibility() == View.VISIBLE) {
                                    footView.setVisibility(View.GONE);
                                }
                            }
                        }
                    } else {
                        if (footView != null && footView.getVisibility() == View.VISIBLE) {
                            footView.setVisibility(View.GONE);
                        }
                    }

                } catch (Exception e) {
                    DLog.e(TAG, e.getMessage());
                    if (footView != null && footView.getVisibility() == View.VISIBLE) {
                        footView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                Toast.makeText(YMApplication.getAppContext(), "获取数据失败，请稍后再试", Toast.LENGTH_SHORT).show();
                if (refreshMoreTopic != null) {
                    refreshMoreTopic.setRefreshing(false);
                }
                isLoading = false;
                if (footView != null && footView.getVisibility() == View.VISIBLE) {
                    footView.setVisibility(View.GONE);
                }
            }
        });
    }

}