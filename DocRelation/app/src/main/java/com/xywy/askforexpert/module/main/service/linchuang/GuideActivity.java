package com.xywy.askforexpert.module.main.service.linchuang;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.model.CodexSecondInfo;
import com.xywy.askforexpert.module.main.service.codex.BookSerchActivity;
import com.xywy.askforexpert.module.main.service.linchuang.adapter.BaseGuideAdapter;
import com.xywy.askforexpert.module.main.service.linchuang.fragment.GuideMainFragment;
import com.xywy.askforexpert.widget.view.MyLoadMoreListView;
import com.xywy.askforexpert.widget.view.MyLoadMoreListView.OnLoadMore;
import com.xywy.uilibrary.titlebar.ItemClickListener;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 临床指南 stone
 *
 * @author 王鹏
 * @2015-5-13下午4:16:36
 */

public class GuideActivity extends YMBaseActivity implements
        SwipeRefreshLayout.OnRefreshListener,
        OnLoadMore {
    private LinearLayout no_data;
    private TextView tv_nodata_title;
    private ImageView img_nodata;

    SwipeRefreshLayout swip;
    private ListView list_news_con;
    // private PullToRefreshListView mPullToRefreshListView;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");

    private String id = "0";
    // private ListView list_codex;
    private MyLoadMoreListView list_codex;

    private int page = 1;

    private CodexSecondInfo codex = new CodexSecondInfo();
    private CodexSecondInfo codex_down = new CodexSecondInfo();
    private String type;
    private BaseGuideAdapter adapter;
    private LinearLayout lin_serch;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 100:
                    if (codex != null) {
                        if (codex.getCode().equals("0")) {
                            // updateList();
                            if (codex.getList().size() < 15) {
                                // mPullToRefreshListView.setHasMoreData(false);
                                list_codex.setLoading(true);
                                list_codex.noMoreLayout();
                            } else {
                                // mPullToRefreshListView.setHasMoreData(true);
                                list_codex.setLoading(false);
                            }
                            adapter = new BaseGuideAdapter(GuideActivity.this);
                            adapter.setFb(fb);
                            adapter.setData(codex.getList());
                            list_codex.setAdapter(adapter);
                            no_data.setVisibility(View.GONE);
                        }

                    } else {
                        no_data.setVisibility(View.VISIBLE);
                    }

                    break;
                case 200:
                    if (codex_down != null && codex_down.getCode().equals("0")) {
                        codex.getList().addAll(codex_down.getList());
                        adapter.setData(codex.getList());
                        adapter.notifyDataSetChanged();
                        if (codex_down.getList().size() == 0) {
//						T.shortToast("没有更多数据了");
//						page--;
                            page--;
                            list_codex.LoadingMoreText("没有数据啦");
                            list_codex.setLoading(true);

                        }
                    }
                    break;

                default:
                    break;
            }
        }
    };
    private FinalDb fb;


    public void getData(final int page) {
//		final ProgressDialog dialog = new ProgressDialog(GuideActivity.this,
//				"搜索中...");
//		dialog.setCanceledOnTouchOutside(false);
//		dialog.showProgersssDialog();
        String sign = MD5Util.MD5(id + Constants.MD5_KEY);

        AjaxParams params = new AjaxParams();
        params.put("c", "guide");
        params.put("a", "list");
        params.put("page", page + "");
        params.put("pagesize", 15 + "");
        if (!YMUserService.isGuest()) {
            String userid = YMApplication.getLoginInfo().getData().getPid();
            params.put("userid", userid);
        }
        params.put("id", id);
        params.put("sign", sign);
        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.Codex_Url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                if (page == 1) {
                    codex = ResolveJson.R_codexSecond(t.toString());
                    handler.sendEmptyMessage(100);
                    swip.setRefreshing(false);
                } else {
                    codex_down = ResolveJson.R_codexSecond(t.toString());
                    list_codex.onLoadComplete();
                    handler.sendEmptyMessage(200);

                }
//				dialog.dismiss();
                super.onSuccess(t);
            }

            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                swip.setRefreshing(false);
                list_codex.onLoadComplete();
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    private void setLastUpdateTime() {
//		String text = formatDateTime(System.currentTimeMillis());
//		mPullToRefreshListView.setLastUpdatedLabel(text);
    }

    private String formatDateTime(long time) {
        if (0 == time) {
            return "";
        }
        return mDateFormat.format(new Date(time));
    }

    // viewsetOnPageChangeListener
//    public void onClick_back(View view) {
//        switch (view.getId()) {
//            case R.id.btn1:
//                finish();
//                break;
//            case R.id.btn22:
//                Intent intent = new Intent(GuideActivity.this,
//                        SectionTypeActivity.class);
//                startActivity(intent);
//                StatisticalTools.eventCount(GuideActivity.this, "znsearch");
//                break;
//            default:
//                break;
//        }
//    }

    @Override
    protected void onNewIntent(Intent intent) {
        String type = intent.getStringExtra("type");
        id = type;
        if (NetworkUtil.isNetWorkConnected()) {
            getData(1);
            page = 1;
        }
        super.onNewIntent(intent);
    }


    @Override
    protected void initView() {

        titleBarBuilder.setTitleText("临床指南").addItem("科室分类", new ItemClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(GuideActivity.this,
                        SectionTypeActivity.class);
                startActivity(intent);
                StatisticalTools.eventCount(GuideActivity.this, "znsearch");
            }
        }).build();

        fb = FinalDb.create(this, "coupon.db");

        no_data = (LinearLayout) findViewById(R.id.lin_nodata);
        tv_nodata_title = (TextView) findViewById(R.id.tv_nodata_title);
        tv_nodata_title.setText("暂无数据");
        img_nodata = (ImageView) findViewById(R.id.img_nodate);
        img_nodata.setBackgroundResource(R.drawable.service_more_none);
        // mPullToRefreshListView = (PullToRefreshListView)
        // findViewById(R.id.list_codex);
        // mPullToRefreshListView.setPullLoadEnabled(false);
        // mPullToRefreshListView.setScrollLoadEnabled(true);
        // list_codex = new ListView(GuideActivity.this);
        // list_codex = mPullToRefreshListView.getRefreshableView();
        list_codex = (MyLoadMoreListView) findViewById(R.id.list_codex);
        list_codex.setLoadMoreListen(this);
        swip = (SwipeRefreshLayout) findViewById(R.id.swip_index);
        swip.setColorSchemeResources(R.color.color_scheme_2_1,
                R.color.color_scheme_2_2, R.color.color_scheme_2_3,
                R.color.color_scheme_2_4);
        swip.setOnRefreshListener(this);
        View layout = LayoutInflater.from(GuideActivity.this).inflate(
                R.layout.guide_serch_layout, null);
        list_codex.addHeaderView(layout);
        lin_serch = (LinearLayout) layout.findViewById(R.id.lin_serch);
        setLastUpdateTime();
        if (NetworkUtil.isNetWorkConnected()) {
            getData(page);
        } else {
            ToastUtils.shortToast("网络连接失败");
            no_data.setVisibility(View.VISIBLE);
            tv_nodata_title.setText("网络连接失败");
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
//						if (NetworkUtil.isNetWorkConnected(GuideActivity.this))
//						{
//							page = 1;
//							getData(page);
//							mPullToRefreshListView.onPullDownRefreshComplete();
//						} else
//						{
//							T.shortToast("网络连接失败");
//						}
//					}
//
//					@Override
//					public void onPullUpToRefresh(
//							PullToRefreshBase<ListView> refreshView)
//					{
//						// TODO Auto-generated method stub
//						if (NetworkUtil.isNetWorkConnected(GuideActivity.this))
//						{
//							getData(++page);
//							mPullToRefreshListView.onPullUpRefreshComplete();
//						} else
//						{
//							T.shortToast("网络连接失败");
//						}
//					}
//				});

        list_codex.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(GuideActivity.this,
                        GuideMainFragment.class);
                // intent.putExtra("url", codex.getList().get(arg2).getUrl());
                // intent.putExtra("msg", codex.getList().get(arg2).getMsg());
                // intent.putExtra("title",
                // codex.getList().get(arg2).getTitle());
                // intent.putExtra("iscollection",
                // codex.getList().get(arg2).getIscollection());
                // intent.putExtra("channel", "3");
                if (arg2 > 0) {
                    intent.putExtra("url", codex.getList().get(arg2 - 1)
                            .getUrl());
                    intent.putExtra("ids", codex.getList().get(arg2 - 1)
                            .getId());
                    intent.putExtra("title", codex.getList().get(arg2 - 1)
                            .getTitle());
                    intent.putExtra("filesize", codex.getList().get(arg2 - 1)
                            .getFilesize());
                    intent.putExtra("fileurl", codex.getList().get(arg2 - 1)
                            .getDownloadurl());
                    // intent.putExtra("imageurl",
                    // consult_info.getList().get(arg2).getImage());
                    intent.putExtra("channel", "3");
                    intent.putExtra("iscollection",
                            codex.getList().get(arg2 - 1).getIscollection());
                    startActivity(intent);
                }

            }
        });

        lin_serch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(GuideActivity.this,
                        BookSerchActivity.class);
                intent.putExtra("type", 3 + "");
                startActivity(intent);

            }
        });
    }

    @Override
    protected void initData() {

    }


    @Override
    public void loadMore() {
        if (NetworkUtil.isNetWorkConnected()) {
            getData(++page);
//			mPullToRefreshListView.onPullUpRefreshComplete();
        } else {
            ToastUtils.shortToast("网络连接失败");
        }
    }

    @Override
    public void onRefresh() {
        if (NetworkUtil.isNetWorkConnected()) {
            page = 1;
            getData(page);

        } else {
            ToastUtils.shortToast("网络连接失败");
        }
    }

    @Override
    protected void onDestroy() {

        fb = null;
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.guide_main;
    }
}
