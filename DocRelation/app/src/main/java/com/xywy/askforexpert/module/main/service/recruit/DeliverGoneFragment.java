package com.xywy.askforexpert.module.main.service.recruit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.model.RecrutiCenterInfo;
import com.xywy.askforexpert.module.main.service.recruit.adapter.BaseRecruitCenterAdapter;
import com.xywy.askforexpert.widget.view.MyLoadMoreListView;
import com.xywy.askforexpert.widget.view.MyLoadMoreListView.OnLoadMore;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 简历被查看
 *
 * @author 王鹏
 * @2015-6-18上午8:52:41
 */
public class DeliverGoneFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener,
        OnLoadMore {

    // private ListView list_news_con;
    // private PullToRefreshListView mPullToRefreshListView;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");

    private String id;
    // private ListView list_codex;
    private MyLoadMoreListView list_codex;
    private int page = 1;
    private String type;
    private RecrutiCenterInfo recrut_info = new RecrutiCenterInfo();
    private RecrutiCenterInfo recrut_down = new RecrutiCenterInfo();
    private BaseRecruitCenterAdapter adapter;
    private LinearLayout no_data;
    private TextView tv_nodata_title;
    private ImageView img_nodata;
    SwipeRefreshLayout swip;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 100:
                    if (recrut_info == null) {
                        no_data.setVisibility(View.VISIBLE);
                        return;
                    }
                    if (recrut_info != null & recrut_info.getCode().equals("0")
                            & recrut_info.getList_first().getList().size() > 0) {
                        // updateList(codex.getList());
                        no_data.setVisibility(View.GONE);
                        adapter = new BaseRecruitCenterAdapter(getActivity());
                        adapter.setData(recrut_info.getList_first().getList());
                        list_codex.setAdapter(adapter);
                        if (recrut_info.getList_first().getList().size() < 10) {
                            // mPullToRefreshListView.setHasMoreData(false);
                            // mPullToRefreshListView.getFooterLoadingLayout().show(
                            // false);
                            list_codex.setLoading(true);
                            list_codex.noMoreLayout();
                        } else {
                            // mPullToRefreshListView.setHasMoreData(true);
                            list_codex.setLoading(false);
                        }
                        if (recrut_info.getList_first().getList().size() > 0) {
                            ((TextView) getActivity().findViewById(R.id.tv_total2))
                                    .setText("共"
                                            + recrut_info.getList_first()
                                            .getCount() + "个职位");
                        }

                    } else {
                        no_data.setVisibility(View.VISIBLE);
                    }
                    break;
                case 200:
                    if (recrut_down != null & "0".equals(recrut_down.getCode())) {
                        recrut_info.getList_first().getList()
                                .addAll(recrut_down.getList_first().getList());
                        adapter.setData(recrut_info.getList_first().getList());
                        adapter.notifyDataSetChanged();
                        if (recrut_down.getList_first().getList().size() == 0) {
//						T.shortToast( "没有更多数据了");
                            page--;
                            list_codex.LoadingMoreText(getResources().getString(R.string.no_more));
                            list_codex.setLoading(true);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.myresume_second3, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Bundle bunder = getArguments();
        // type = bunder.getString("type");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        no_data = (LinearLayout) getActivity().findViewById(R.id.nodata);
        tv_nodata_title = (TextView) getActivity().findViewById(
                R.id.tv_nodata_title_3);
        tv_nodata_title.setText("暂无被查看的职位");
        img_nodata = (ImageView) getActivity().findViewById(R.id.img_nodate_3);
        img_nodata.setBackgroundResource(R.drawable.service_more_none);
        // mPullToRefreshListView = (PullToRefreshListView) getActivity()
        // .findViewById(R.id.list_resume33);
        // mPullToRefreshListView.setPullLoadEnabled(false);
        // mPullToRefreshListView.setScrollLoadEnabled(true);
        // list_codex = new ListView(getActivity());
        // list_codex = mPullToRefreshListView.getRefreshableView();
        list_codex = (MyLoadMoreListView) getActivity().findViewById(
                R.id.list_resume33);
        list_codex.setLoadMoreListen(this);

        swip = (SwipeRefreshLayout) getActivity().findViewById(R.id.swip_index0);
        swip.setColorSchemeResources(R.color.color_scheme_2_1,
                R.color.color_scheme_2_2, R.color.color_scheme_2_3,
                R.color.color_scheme_2_4);
        swip.setOnRefreshListener(this);

        list_codex.setFadingEdgeLength(0);
        setLastUpdateTime();
        // String title = getIntent().getStringExtra("title");
        // type = getIntent().getStringExtra("type");
        // ((TextView) findViewById(R.id.tv_title)).setText(title);
        getData(page, "show");

        list_codex.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(),
                        RecruitWebMianActivity.class);

                intent.putExtra("url", recrut_info.getList_first().getList()
                        .get(arg2).getUrl());
                intent.putExtra("id", recrut_info.getList_first().getList()
                        .get(arg2).getId());
                startActivity(intent);
            }
        });
        // mPullToRefreshListView
        // .setOnRefreshListener(new OnRefreshListener<ListView>()
        // {
        //
        // @Override
        // public void onPullDownToRefresh(
        // PullToRefreshBase<ListView> refreshView)
        // {
        // // TODO Auto-generated method stub
        // if (NetworkUtil.isNetWorkConnected(getActivity()))
        // {
        // page = 1;
        // getData(page, "show");
        // mPullToRefreshListView.onPullDownRefreshComplete();
        // } else
        // {
        // T.shortToast( "网络连接失败");
        //
        // }
        // }
        //
        // @Override
        // public void onPullUpToRefresh(
        // PullToRefreshBase<ListView> refreshView)
        // {
        // // TODO Auto-generated method stub
        // if (NetworkUtil.isNetWorkConnected(getActivity()))
        // {
        // getData(++page, "show");
        // mPullToRefreshListView.onPullUpRefreshComplete();
        // } else
        // {
        // T.shortToast( "网络连接失败");
        // }
        //
        // }
        // });

        getActivity().findViewById(R.id.btn3).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        getActivity().finish();

                    }
                });

    }

    public void getData(final int page, String type) {
        // final ProgressDialog dialog = new ProgressDialog(getActivity(),
        // "正在加载中...");
        // dialog.setCanceledOnTouchOutside(false);
        // dialog.showProgersssDialog();

        String id = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(id + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("uid", id);
        params.put("sign", sign);
        params.put("page", page + "");
        params.put("type", type);
        params.put("pagesize", 10 + "");
        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.MyResume_Send_Url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                if (page == 1) {
                    recrut_info = ResolveJson.R_Mysume(t.toString());
                    handler.sendEmptyMessage(100);
                    swip.setRefreshing(false);
                } else {
                    recrut_down = ResolveJson.R_Mysume(t.toString());
                    handler.sendEmptyMessage(200);
                    list_codex.onLoadComplete();
                }
                // dialog.dismiss();
                super.onSuccess(t);
            }

            @Override
            public void onLoading(long count, long current) {
                // TODO Auto-generated method stub
                super.onLoading(count, current);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                // dialog.dismiss();
                list_codex.onLoadComplete();
                swip.setRefreshing(false);
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    private void setLastUpdateTime() {
        String text = formatDateTime(System.currentTimeMillis());
        // mPullToRefreshListView.setLastUpdatedLabel(text);
    }

    private String formatDateTime(long time) {
        if (0 == time) {
            return "";
        }
        return mDateFormat.format(new Date(time));
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
//		MobclickAgent.onPause(getActivity());
//		MobileAgent.onPause(getActivity());
        StatisticalTools.fragmentOnPause("DeliverGoneFragment");

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//		MobclickAgent.onResume(getActivity());
//		MobileAgent.onResume(getActivity());
        StatisticalTools.fragmentOnResume("DeliverGoneFragment");
    }


    @Override
    public void loadMore() {
        // TODO Auto-generated method stub
        if (NetworkUtil.isNetWorkConnected()) {
            getData(++page, "show");
        } else {
            ToastUtils.shortToast( "网络连接失败");
        }
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        if (NetworkUtil.isNetWorkConnected()) {
            page = 1;
            getData(page, "show");
        } else {
            ToastUtils.shortToast( "网络连接失败");

        }
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
