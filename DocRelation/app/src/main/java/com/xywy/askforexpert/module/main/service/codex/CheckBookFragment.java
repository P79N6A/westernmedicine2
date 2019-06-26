package com.xywy.askforexpert.module.main.service.codex;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.model.CodexInfo;
import com.xywy.askforexpert.module.main.service.codex.adapter.BaseCheckBookAdapter;
import com.xywy.askforexpert.widget.view.MyLoadMoreListView;
import com.xywy.askforexpert.widget.view.MyLoadMoreListView.OnLoadMore;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

/**
 * 检查手册 展示页面
 *
 * @author 王鹏
 * @2015-5-16下午12:21:27
 */
public class CheckBookFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener,
        OnLoadMore {
    private static final String ARG_POSITION = "position";
    private int position;
    private String[] str = new String[]{"504", "521", "474"};

    private String id;
    private int page = 1;

    private BaseCheckBookAdapter adapter;
    CodexInfo cInfo;
    CodexInfo cInfo_down;
    private MyLoadMoreListView mListview;
    private SwipeRefreshLayout swip;

    private LinearLayout no_data;
    private TextView tv_nodata_title;
    private ImageView img_nodata;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 100:
                    if (cInfo != null) {
                        if (cInfo.getCode().equals("0")
                                & cInfo.getList().size() > 0) {
                            adapter = new BaseCheckBookAdapter(getActivity());
                            adapter.setData(cInfo.getList());
                            mListview.setAdapter(adapter);
                            if (cInfo.getList().size() < 15) {
                                mListview.setLoading(true);
                                mListview.noMoreLayout();
                                // mPullToRefreshListView.setHasMoreData(false);
                                // mPullToRefreshListView.getFooterLoadingLayout()
                                // .show(false);
                            } else {
                                mListview.setLoading(false);
                                // mPullToRefreshListView.setHasMoreData(true);

                            }
                            no_data.setVisibility(View.GONE);
                        } else {
                            no_data.setVisibility(View.VISIBLE);
                        }

                    } else {
                        no_data.setVisibility(View.VISIBLE);
                    }
                    break;
                case 200:
                    if (cInfo_down != null & cInfo_down.getCode().equals("0")) {
                        cInfo.getList().addAll(cInfo_down.getList());
                        adapter.setData(cInfo.getList());
                        adapter.notifyDataSetChanged();
                        if (cInfo_down.getList().size() == 0) {
                            // T.shortToast( "没有更多数据了");
                            page--;
                            mListview.LoadingMoreText("没有数据啦");
                            mListview.setLoading(true);
                            // mPullToRefreshListView.setHasMoreData(false);
                        }
                    }

                    break;
                default:
                    break;
            }
        }
    };

    public static CheckBookFragment newInstance(int position) {
        CheckBookFragment f = new CheckBookFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);
        StatisticalTools.eventCount(getActivity(), "Chemicalexamination");
        StatisticalTools.eventCount(getActivity(), "Physicalexamination");
        StatisticalTools.eventCount(getActivity(), "Laboratorytests");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        final View v = inflater.inflate(R.layout.checkbook_fragment, container,
                false);
        no_data = (LinearLayout) v.findViewById(R.id.lin_nodata);
        tv_nodata_title = (TextView) v.findViewById(R.id.tv_nodata_title);
        tv_nodata_title.setText("暂无数据");
        img_nodata = (ImageView) v.findViewById(R.id.img_nodate);
        img_nodata.setBackgroundResource(R.drawable.service_more_none);

        mListview = (MyLoadMoreListView) v.findViewById(R.id.list_codex);
        mListview.setLoadMoreListen(this);
        swip = (SwipeRefreshLayout) v.findViewById(R.id.swip_index);
        swip.setColorSchemeResources(R.color.color_scheme_2_1,
                R.color.color_scheme_2_2, R.color.color_scheme_2_3,
                R.color.color_scheme_2_4);
        swip.setOnRefreshListener(this);
        // mPullToRefreshListView = (PullToRefreshListView) v
        // .findViewById(R.id.list_codex);
        // mPullToRefreshListView.setPullLoadEnabled(false);
        // mPullToRefreshListView.setScrollLoadEnabled(true);
        // list_codex = new ListView(getActivity());
        // list_codex = mPullToRefreshListView.getRefreshableView();
        // list_codex.setFadingEdgeLength(0);
        // setLastUpdateTime();
        if (NetworkUtil.isNetWorkConnected()) {
            getData(1);
        } else {
            ToastUtils.shortToast( "网络连接失败");
            no_data.setVisibility(View.VISIBLE);
            tv_nodata_title.setText("网络连接失败");

        }
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
        // getData(page);
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
        // getData(++page);
        // mPullToRefreshListView.onPullUpRefreshComplete();
        // } else
        // {
        // T.shortToast( "网络连接失败");
        // }
        //
        // }
        // });
        mListview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(getActivity(),
                        CodexSecondActivity.class);
                intent.putExtra("ids", cInfo.getList().get(arg2).getId());
                intent.putExtra("title", cInfo.getList().get(arg2).getName());
                intent.putExtra("type", "check");
                getActivity().startActivity(intent);

            }
        });
        return v;
    }

    /**
     * 获取网络数据
     *
     * @param page
     */
    public void getData(final int page) {
        // final ProgressDialog dialog = new ProgressDialog(getActivity(),
        // "正在加载中。。。");
        // dialog.setCanceledOnTouchOutside(false);
        // dialog.showProgersssDialog();
        String sign = MD5Util.MD5(str[position] + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("c", "check");
        params.put("a", "check");
        params.put("page", page + "");
        params.put("pagesize", 15 + "");
        params.put("id", str[position]);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.Codex_Url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                if (page == 1) {

                    cInfo = ResolveJson.R_codex(t.toString());
                    handler.sendEmptyMessage(100);
                    swip.setRefreshing(false);
                } else {
                    cInfo_down = ResolveJson.R_codex(t.toString());
                    handler.sendEmptyMessage(200);
                    mListview.onLoadComplete();
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
                ToastUtils.shortToast( "网络繁忙，请稍后重试");
                swip.setRefreshing(false);
                mListview.onLoadComplete();
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    // private void setLastUpdateTime()
    // {
    // String text = formatDateTime(System.currentTimeMillis());
    // mPullToRefreshListView.setLastUpdatedLabel(text);
    // }
    //
    // private String formatDateTime(long time)
    // {
    // if (0 == time)
    // {
    // return "";
    // }
    // return mDateFormat.format(new Date(time));
    // }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        StatisticalTools.fragmentOnResume("CheckBookFragment");

    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        StatisticalTools.fragmentOnPause("CheckBookFragment");
    }

    @Override
    public void loadMore() {
        // TODO Auto-generated method stub
        if (NetworkUtil.isNetWorkConnected()) {
            getData(++page);
        } else {
            ToastUtils.shortToast( "网络连接失败");
        }
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        if (NetworkUtil.isNetWorkConnected()) {
            page = 1;
            getData(page);

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
