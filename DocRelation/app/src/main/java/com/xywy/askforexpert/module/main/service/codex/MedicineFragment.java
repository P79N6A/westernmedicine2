package com.xywy.askforexpert.module.main.service.codex;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
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
import com.xywy.askforexpert.widget.view.MyLoadMoreExpandListView;
import com.xywy.askforexpert.widget.view.MyLoadMoreExpandListView.OnLoadMore;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 中药 西药 (药品分类)
 *
 * @author 王鹏
 * @2015-5-10下午3:19:22
 */

public class MedicineFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener,
        OnLoadMore {
    private static final String ARG_POSITION = "position";
    private int position;
    String[] str_position = new String[]{"1", "184"};
    // private ExpandableListView expandlist;
    private MyLoadMoreExpandListView expandlist;
    // private PullToRefreshExpandListView mPullToRefreshListView;
    private MedicinFragAdapter adapter;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");

    private CodexInfo coInfo;
    private CodexInfo coInfo_down;
    private int page = 1;
    private LinearLayout no_data;
    private TextView tv_nodata_title;
    private ImageView img_nodata;
    private SwipeRefreshLayout swip;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 100:
                    if (coInfo == null) {
                        no_data.setVisibility(View.VISIBLE);
                        return;
                    }
                    if (coInfo != null & coInfo.getList().size() > 0) {


                        adapter.setData(coInfo.getList());
                        expandlist.setAdapter(adapter);
                        if (coInfo.getList().size() < 15) {
                            // mPullToRefreshListView.setHasMoreData(false);
                            // mPullToRefreshListView.getFooterLoadingLayout().show(false);
                            expandlist.setLoading(true);
                            expandlist.noMoreLayout();
                        } else {
                            expandlist.setLoading(false);
                            // mPullToRefreshListView.setHasMoreData(true);
                        }
                        no_data.setVisibility(View.GONE);

                    } else {
                        no_data.setVisibility(View.VISIBLE);
                    }
                    break;
                case 200:
                    if (coInfo_down != null && coInfo_down.getList().size() > 0) {

                        coInfo.getList().addAll(coInfo_down.getList());
                        adapter.setData(coInfo.getList());
                        adapter.notifyDataSetChanged();

                    } else {
//					T.shortToast( "没有更多数据了");
                        page--;
                        expandlist.LoadingMoreText(getResources().getString(R.string.no_more));
                        expandlist.setLoading(true);
                    }
                    break;

                default:
                    break;
            }

        }
    };
    private LinearLayout lin_nodata;

    public static MedicineFragment newInstance(int position) {
        MedicineFragment f = new MedicineFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);
        StatisticalTools.eventCount(getActivity(), "WesternMedicine");
        StatisticalTools.eventCount(getActivity(), "ChineseMedicine");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.medicinfrag, container, false);
        // mPullToRefreshListView = (PullToRefreshExpandListView) v
        // .findViewById(R.id.expand_list);
        no_data = (LinearLayout) v.findViewById(R.id.lin_nodata);
        tv_nodata_title = (TextView) v.findViewById(R.id.tv_nodata_title);
        tv_nodata_title.setText("暂无数据");
        img_nodata = (ImageView) v.findViewById(R.id.img_nodate);
        img_nodata.setBackgroundResource(R.drawable.service_more_none);
        adapter = new MedicinFragAdapter(getActivity());
        // expandlist = new ExpandableListView(getActivity());
        // expandlist.setGroupIndicator(null);
        //
        // mPullToRefreshListView.setPullLoadEnabled(true);
        // mPullToRefreshListView.setScrollLoadEnabled(false);
        //
        // expandlist = mPullToRefreshListView.getRefreshableView();

        expandlist = (MyLoadMoreExpandListView) v
                .findViewById(R.id.expand_list);

        expandlist.setFadingEdgeLength(0);
        expandlist.setGroupIndicator(null);

        expandlist.setLoadMoreListen(this);
        swip = (SwipeRefreshLayout) v.findViewById(R.id.swip_index);
        swip.setColorSchemeResources(R.color.color_scheme_2_1,
                R.color.color_scheme_2_2, R.color.color_scheme_2_3,
                R.color.color_scheme_2_4);
        swip.setOnRefreshListener(this);
        setLastUpdateTime();
        if (NetworkUtil.isNetWorkConnected()) {
            getData(page);
        } else {
            ToastUtils.shortToast( "网络连接失败");
            no_data.setVisibility(View.VISIBLE);
            tv_nodata_title.setText("网络连接失败");

        }

        // mPullToRefreshListView
        // .setOnRefreshListener(new OnRefreshListener<ExpandableListView>()
        // {
        //
        // @Override
        // public void onPullDownToRefresh(
        // PullToRefreshBase<ExpandableListView> refreshView)
        // {
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
        //
        // }
        //
        // @Override
        // public void onPullUpToRefresh(
        // PullToRefreshBase<ExpandableListView> refreshView)
        // {
        //
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
        expandlist.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView arg0, View arg1,
                                        int arg2, int arg3, long arg4) {
                Intent intent = new Intent(getActivity(),
                        CodexSecondActivity.class);
                intent.putExtra("ids", coInfo.getList().get(arg2)
                        .getList_second().get(arg3).getId());
                intent.putExtra("title", coInfo.getList().get(arg2)
                        .getList_second().get(arg3).getName());
                intent.putExtra("type", "codex");
                getActivity().startActivity(intent);
                return true;
            }
        });

        expandlist.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView arg0, View arg1,
                                        int arg2, long arg3) {
                if (coInfo.getList().get(arg2).getList_second().size() == 0) {
                    Intent intent = new Intent(getActivity(),
                            CodexSecondActivity.class);
                    intent.putExtra("ids", coInfo.getList().get(arg2).getId());
                    intent.putExtra("title", coInfo.getList().get(arg2)
                            .getName());
                    intent.putExtra("type", "codex");
                    getActivity().startActivity(intent);
                    return true;
                } else {
                    return false;
                }

            }
        });
        return v;
    }

    /**
     * 获取网络数据
     */
    public void getData(final int page) {
        // final ProgressDialog dialog = new ProgressDialog(getActivity(),
        // "正在加载中。。。");
        // dialog.setCanceledOnTouchOutside(false);
        // dialog.showProgersssDialog();
        AjaxParams params = new AjaxParams();
        String sign = MD5Util
                .MD5(str_position[position] + Constants.MD5_KEY);
        params.put("c", "codex");
        params.put("a", "codex");
        params.put("id", str_position[position]);
        params.put("page", page + "");
        params.put("pagesize", 15 + "");
        params.put("sign", sign);
        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.Codex_Url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                // TODO Auto-generated method stub
                try {
                    if (page == 1) {
                        coInfo = ResolveJson.R_codex(t.toString());
                        handler.sendEmptyMessage(100);
                        swip.setRefreshing(false);
                    } else {
                        coInfo_down = ResolveJson.R_codex(t.toString());
                        handler.sendEmptyMessage(200);
                        expandlist.onLoadComplete();
                    }
                } catch (Exception e) {
                    coInfo = null;
                    handler.sendEmptyMessage(100);

                }

                // dialog.dismiss();
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                no_data.setVisibility(View.VISIBLE);
                // dialog.dismiss();
                swip.setRefreshing(false);
                expandlist.onLoadComplete();
                ToastUtils.shortToast( "网络繁忙，请稍后重试");
                super.onFailure(t, errorNo, strMsg);
            }

            @Override
            public void onLoading(long count, long current) {
                // TODO Auto-generated method stub
                super.onLoading(count, current);
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

    public void onResume() {
        super.onResume();
        StatisticalTools.fragmentOnResume("MedicineFragment");
    }

    public void onPause() {
        super.onPause();
        StatisticalTools.fragmentOnPause("MedicineFragment");
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
