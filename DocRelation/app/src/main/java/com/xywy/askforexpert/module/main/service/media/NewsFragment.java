package com.xywy.askforexpert.module.main.service.media;

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
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ACache;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.model.ConsultingInfo;
import com.xywy.askforexpert.module.main.news.adapter.Con_NewsAdapter;
import com.xywy.askforexpert.widget.view.MyLoadMoreListView;
import com.xywy.askforexpert.widget.view.MyLoadMoreListView.OnIsOnScrolling;
import com.xywy.askforexpert.widget.view.MyLoadMoreListView.OnLoadMore;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 资讯 信息列表
 *
 * @author 王鹏
 * @2015-5-10下午12:39:18
 */
public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnLoadMore, OnIsOnScrolling {

    private static final String ARG_POSITION = "position";

    private int position;
    /**
     * 一级标题id
     */
    private String[] fragment_id = new String[]{"0", "1", "15", "31", "16",
            "17", "10", "30"};
    /**
     * 二级标题id
     */
    private String[] type_2 = new String[]{"1", "2", "9", "3", "4", "5"};
    private String type_id;
    //	private ListView list_news_con;
    private MyLoadMoreListView list_news_con;
    //	private PullToRefreshListView mPullToRefreshListView;
    private Con_NewsAdapter adapter;
    private LinearLayout lin_seciton;
    private int page = 1;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");

    public ConsultingInfo consult_info;
    public ConsultingInfo consult_info_down;
    private TextView[] tv_type;
    private String type = "2";
    private String id;
    private LinearLayout no_data;
    private TextView tv_nodata_title;
    private ImageView img_nodata;

    private ACache mCache;

    SwipeRefreshLayout swip;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 200:
                    if (consult_info != null && consult_info.getCode().equals("0")) {
                        if (getActivity() != null) {
                            if (consult_info.getList().size() > 0) {
                                no_data.setVisibility(View.GONE);
                            } else {
                                no_data.setVisibility(View.VISIBLE);
                            }
                            adapter = new Con_NewsAdapter(getActivity());
                            adapter.setData(consult_info.getList());
                            list_news_con.setAdapter(adapter);
                        }
                        mCache.put("zx" + type + id, consult_info);
                    } else {
                        no_data.setVisibility(View.VISIBLE);
                    }
                    break;
                case 300:
                    if (consult_info_down != null
                            && consult_info_down.getCode().equals("0")) {
                        consult_info.getList().addAll(consult_info_down.getList());
                        // int position = (page - 1) * 5;
                        // updateList(consult_info.getList(), position);
                        adapter.setData(consult_info.getList());
                        adapter.notifyDataSetChanged();
                        if (consult_info_down.getList().size() == 0) {
//						T.shortToast( "没有更多数据了");
                            page--;
                            list_news_con.LoadingMoreText(getResources().getString(R.string.no_more));
                            list_news_con.setLoading(true);
                        }
                        mCache.put("zx" + type + id, consult_info);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public static NewsFragment newInstance(int position) {
        NewsFragment f = new NewsFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCache = ACache.get(getActivity());
        position = getArguments().getInt(ARG_POSITION);

    }

    public void updateList(List<ConsultingInfo> list, int position) {

        // list_news_con.setSelection(position);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.news_cons, container, false);
        lin_seciton = (LinearLayout) v.findViewById(R.id.lin_seciton);
        no_data = (LinearLayout) v.findViewById(R.id.lin_nodata);
        tv_nodata_title = (TextView) v.findViewById(R.id.tv_nodata_title);
        tv_nodata_title.setText("暂无数据");
        img_nodata = (ImageView) v.findViewById(R.id.img_nodate);
        img_nodata.setBackgroundResource(R.drawable.service_more_none);
//		mPullToRefreshListView = (PullToRefreshListView) v
//				.findViewById(R.id.list_news_con);
//		mPullToRefreshListView.setPullLoadEnabled(false);
//		mPullToRefreshListView.setScrollLoadEnabled(true);
//		list_news_con = new ListView(getActivity());
//		list_news_con = mPullToRefreshListView.getRefreshableView();
        list_news_con = (MyLoadMoreListView) v.findViewById(R.id.list_news_con);
        list_news_con.setFadingEdgeLength(0);
        list_news_con.setLoadMoreListen(this);
        list_news_con.setIsScrolling(this);
        swip = (SwipeRefreshLayout) v.findViewById(R.id.swip_index);
        swip.setColorSchemeResources(R.color.color_scheme_2_1, R.color.color_scheme_2_2,
                R.color.color_scheme_2_3, R.color.color_scheme_2_4);
        swip.setOnRefreshListener(this);
        setLastUpdateTime();
        id = fragment_id[position];
        if (NetworkUtil.isNetWorkConnected()) {
            getData(page, type, id);
        } else {
            consult_info = (ConsultingInfo) mCache
                    .getAsObject("zx" + type + id);
            handler.sendEmptyMessage(200);
            ToastUtils.shortToast( "网络连接失败");
        }

        if (position == 1)

        {
            lin_seciton.setVisibility(View.VISIBLE);
            tv_type = new TextView[6];
            tv_type[0] = (TextView) v.findViewById(R.id.tv_all);

            tv_type[0].setOnClickListener(new ItemOnClick());
            tv_type[1] = (TextView) v.findViewById(R.id.tv_1);
            tv_type[1].setOnClickListener(new ItemOnClick());
            tv_type[2] = (TextView) v.findViewById(R.id.tv_2);
            tv_type[2].setOnClickListener(new ItemOnClick());
            tv_type[3] = (TextView) v.findViewById(R.id.tv_3);
            tv_type[3].setOnClickListener(new ItemOnClick());
            tv_type[4] = (TextView) v.findViewById(R.id.tv_4);
            tv_type[4].setOnClickListener(new ItemOnClick());
            tv_type[5] = (TextView) v.findViewById(R.id.tv_5);
            tv_type[5].setOnClickListener(new ItemOnClick());
            initType();
            tv_type[0]
                    .setTextColor(getResources().getColor(R.color.purse_blue));

        } else {
            lin_seciton.setVisibility(View.GONE);

        }

//		mPullToRefreshListView
//				.setOnRefreshListener(new OnRefreshListener<ListView>()
//				{
//
//					@Override
//					public void onPullDownToRefresh(
//							PullToRefreshBase<ListView> refreshView)
//					{
//						// TODO Auto-generated method stub
//
//						if (NetworkUtil.isNetWorkConnected(getActivity()))
//						{
//							page = 1;
//							getData(page, type, id);
//
//						} else
//						{
//							mPullToRefreshListView.onPullDownRefreshComplete();
//							T.shortToast( "网络连接失败");
//
//						}
//					}
//
//					@Override
//					public void onPullUpToRefresh(
//							PullToRefreshBase<ListView> refreshView)
//					{
//						if (NetworkUtil.isNetWorkConnected(getActivity()))
//						{
//							getData(++page, type, id);
//						} else
//						{
//							mPullToRefreshListView.onPullUpRefreshComplete();
//							T.shortToast( "网络连接失败");
//
//						}
//					}
//				});

        list_news_con.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(getActivity(),
                        InfoDetailActivity.class);
                intent.putExtra("url", consult_info.getList().get(arg2)
                        .getUrl());
                intent.putExtra("ids", consult_info.getList().get(arg2).getId());
                intent.putExtra("title", consult_info.getList().get(arg2)
                        .getTitle());
                intent.putExtra("imageurl", consult_info.getList().get(arg2)
                        .getImage());

                startActivity(intent);

            }
        });

        return v;

    }

    /**
     * 初始化 二级选择科室
     */
    public void initType() {
        for (int i = 0; i < tv_type.length; i++) {
            tv_type[i].setTextColor(getResources().getColor(R.color.gray_text));
        }
    }

    /**
     * 二级 科室 选择
     *
     * @author 王鹏
     * @2015-5-11下午5:47:42
     */
    public class ItemOnClick implements OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_all:
                    initType();
                    tv_type[0].setTextColor(getResources().getColor(
                            R.color.purse_blue));
                    type = "2";
                    id = fragment_id[position];

                    if (NetworkUtil.isNetWorkConnected()) {
                        getData(page, type, id);
                    } else {
                        consult_info = (ConsultingInfo) mCache.getAsObject("zx"
                                + type + id);
                        handler.sendEmptyMessage(200);
                        ToastUtils.shortToast( "网络连接失败");
                    }
                    break;
                case R.id.tv_1:
                    initType();
                    tv_type[1].setTextColor(getResources().getColor(
                            R.color.purse_blue));
                    type = "1";
                    id = type_2[1];
                    if (NetworkUtil.isNetWorkConnected()) {
                        getData(page, type, id);
                    } else {
                        consult_info = (ConsultingInfo) mCache.getAsObject("zx"
                                + type + id);
                        handler.sendEmptyMessage(200);
                        ToastUtils.shortToast( "网络连接失败");
                    }
                    break;
                case R.id.tv_2:
                    initType();
                    tv_type[2].setTextColor(getResources().getColor(
                            R.color.purse_blue));
                    type = "1";
                    id = type_2[2];
                    if (NetworkUtil.isNetWorkConnected()) {
                        getData(page, type, id);
                    } else {
                        consult_info = (ConsultingInfo) mCache.getAsObject("zx"
                                + type + id);
                        handler.sendEmptyMessage(200);
                        ToastUtils.shortToast( "网络连接失败");
                    }
                    break;
                case R.id.tv_3:
                    initType();
                    tv_type[3].setTextColor(getResources().getColor(
                            R.color.purse_blue));
                    type = "1";
                    id = type_2[3];
                    if (NetworkUtil.isNetWorkConnected()) {
                        getData(page, type, id);
                    } else {
                        consult_info = (ConsultingInfo) mCache.getAsObject("zx"
                                + type + id);
                        handler.sendEmptyMessage(200);
                        ToastUtils.shortToast( "网络连接失败");
                    }
                    break;
                case R.id.tv_4:
                    initType();
                    tv_type[4].setTextColor(getResources().getColor(
                            R.color.purse_blue));
                    type = "1";
                    id = type_2[4];
                    if (NetworkUtil.isNetWorkConnected()) {
                        getData(page, type, id);
                    } else {
                        consult_info = (ConsultingInfo) mCache.getAsObject("zx"
                                + type + id);
                        handler.sendEmptyMessage(200);
                        ToastUtils.shortToast( "网络连接失败");
                    }
                    break;
                case R.id.tv_5:
                    initType();
                    tv_type[5].setTextColor(getResources().getColor(
                            R.color.purse_blue));
                    type = "1";
                    id = type_2[5];
                    if (NetworkUtil.isNetWorkConnected()) {
                        getData(page, type, id);
                    } else {
                        consult_info = (ConsultingInfo) mCache.getAsObject("zx"
                                + type + id);
                        handler.sendEmptyMessage(200);
                        ToastUtils.shortToast( "网络连接失败");
                    }
                    break;

                default:
                    break;
            }

        }

    }

    /**
     * 获取网络数据
     *
     * @param page
     * @param type
     * @param id
     */
    public void getData(final int page, String type, String id) {
        // fragment_id[position]
        FinalHttp fh = new FinalHttp();
        String sign = MD5Util.MD5(id + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("c", "article");
        params.put("a", "list");
        params.put("id", id);
        params.put("page", page + "");
        params.put("pagesize", "20");
        params.put("type", type);
        params.put("sign", sign);

        fh.post(CommonUrl.Consulting_Url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                if (page == 1) {
                    consult_info = ResolveJson.R_Consult(t.toString());
                    handler.sendEmptyMessage(200);
                    swip.setRefreshing(false);
                } else {
                    consult_info_down = ResolveJson.R_Consult(t.toString());
                    handler.sendEmptyMessage(300);
                    list_news_con.onLoadComplete();
                }
//				mPullToRefreshListView.onPullDownRefreshComplete();
//				mPullToRefreshListView.onPullUpRefreshComplete();

                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                if (getActivity() != null) {
//					mPullToRefreshListView.onPullDownRefreshComplete();
//					mPullToRefreshListView.onPullUpRefreshComplete();
                    swip.setRefreshing(false);
                    list_news_con.onLoadComplete();
                }

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
//		mPullToRefreshListView.setLastUpdatedLabel(text);
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
            getData(++page, type, id);
        } else {
//			mPullToRefreshListView.onPullUpRefreshComplete();
            ToastUtils.shortToast( "网络连接失败");

        }
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        if (NetworkUtil.isNetWorkConnected()) {
            page = 1;
            getData(page, type, id);

        } else {
//			mPullToRefreshListView.onPullDownRefreshComplete();
            ToastUtils.shortToast( "网络连接失败");

        }

    }

    public void onResume() {
        super.onResume();
        StatisticalTools.fragmentOnResume("NewsFragment");
    }

    public void onPause() {
        super.onPause();
        StatisticalTools.fragmentOnPause("NewsFragment");
    }

    @Override
    public void getIscroll(int scrollState) {
        if (scrollState == 2) {
            adapter.isScrollering = true;

        } else {
            adapter.isScrollering = false;
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}