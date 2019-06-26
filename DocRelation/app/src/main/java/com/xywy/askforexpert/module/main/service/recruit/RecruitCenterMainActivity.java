package com.xywy.askforexpert.module.main.service.recruit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.model.RecruitSerchInfo;
import com.xywy.askforexpert.model.RecrutiCenterInfo;
import com.xywy.askforexpert.module.main.service.recruit.adapter.BaseRecruitCenterAdapter2;
import com.xywy.askforexpert.widget.view.MyLoadMoreListView;
import com.xywy.askforexpert.widget.view.MyLoadMoreListView.OnLoadMore;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 招聘中心
 *
 * @author 王鹏
 * @2015-5-16上午10:13:02
 */
public class RecruitCenterMainActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener, OnLoadMore {
    //	private PullToRefreshListView mPullToRefreshListView;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
    public static RecruitSerchInfo serchinfo;
    private MyLoadMoreListView list_recruit;
    private int page = 1;
    private RecrutiCenterInfo recinfo;
    private RecrutiCenterInfo recinfo_down;
    private BaseRecruitCenterAdapter2 adapter;
    private TextView tv_serch_content;
    private String type;
    private LinearLayout no_data;
    private TextView tv_nodata_title;
    private ImageView img_nodata;
    //	ProgressDialog dialog;
    SwipeRefreshLayout swip;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 100:
                    if (recinfo != null && recinfo.getCode().equals("0")
                            && recinfo.getLists().getList().size() > 0) {
                        no_data.setVisibility(View.GONE);
                        adapter = new BaseRecruitCenterAdapter2(
                                RecruitCenterMainActivity.this);
                        adapter.setData(recinfo.getLists().getList());
                        list_recruit.setAdapter(adapter);
                        if (recinfo.getLists().getList().size() < 15) {
//						mPullToRefreshListView.setHasMoreData(false);
//						mPullToRefreshListView.getFooterLoadingLayout().show(
//								false);
                            list_recruit.setLoading(true);
                            list_recruit.noMoreLayout();

                        } else {
//						mPullToRefreshListView.setHasMoreData(true);
                            list_recruit.setLoading(false);
                        }
                    } else {
                        no_data.setVisibility(View.VISIBLE);
                    }
                    break;

                case 200:
                    if (recinfo != null & recinfo_down != null && recinfo_down.getCode().equals("0")) {
                        recinfo.getLists().getList()
                                .addAll(recinfo_down.getLists().getList());
                        adapter.setData(recinfo.getLists().getList());
                        adapter.notifyDataSetChanged();
                        if (recinfo_down.getLists().getList().size() == 0) {
//						T.shortToast( "没有更多数据了");

                            page--;
                            list_recruit.LoadingMoreText(getResources().getString(R.string.no_more));
                            list_recruit.setLoading(true);
                        }
                    }
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);

        setContentView(R.layout.recruit_center);

//		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.list_recruit);
//		mPullToRefreshListView.setPullLoadEnabled(false);
//		mPullToRefreshListView.setScrollLoadEnabled(true);
//		list_recruit = new ListView(RecruitCenterMainActivity.this);
        list_recruit = (MyLoadMoreListView) findViewById(R.id.list_recruit);

        tv_serch_content = (TextView) findViewById(R.id.tv_serch_content);
        no_data = (LinearLayout) findViewById(R.id.lin_nodata);
        tv_nodata_title = (TextView) findViewById(R.id.tv_nodata_title);
        tv_nodata_title.setText("暂无数据");
        img_nodata = (ImageView) findViewById(R.id.img_nodate);
        img_nodata.setBackgroundResource(R.drawable.service_more_none);
//		list_recruit = mPullToRefreshListView.getRefreshableView();
        list_recruit.setLoadMoreListen(this);
        swip = (SwipeRefreshLayout) findViewById(R.id.swip_index);
        swip.setColorSchemeResources(R.color.color_scheme_2_1, R.color.color_scheme_2_2,
                R.color.color_scheme_2_3, R.color.color_scheme_2_4);
        swip.setOnRefreshListener(this);

        list_recruit.setFadingEdgeLength(0);
        type = getIntent().getStringExtra("type");
        setLastUpdateTime();
        if (NetworkUtil.isNetWorkConnected()) {
            getData(page);
        } else {
            ToastUtils.shortToast( "网络连接失败");
            no_data.setVisibility(View.VISIBLE);
            tv_nodata_title.setText("网络连接失败");

        }
        getData(page);
        // init();
        tv_serch_content.setText("工作地点/职位类别/薪资/学历/经验/工作性质/企业性质");
        list_recruit.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(RecruitCenterMainActivity.this,
                        RecruitWebMianActivity.class);

                intent.putExtra("url", recinfo.getLists().getList().get(arg2)
                        .getUrl());
                intent.putExtra("deliver",
                        recinfo.getLists().getList().get(arg2).getDeliver());
                intent.putExtra("id", recinfo.getLists().getList().get(arg2)
                        .getId());
                intent.putExtra("coll", recinfo.getLists().getList().get(arg2)
                        .getColl());
                startActivity(intent);

            }
        });

//		mPullToRefreshListView
//				.setOnRefreshListener(new OnRefreshListener<ListView>()
//				{
//
//					@Override
//					public void onPullDownToRefresh(
//							PullToRefreshBase<ListView> refreshView)
//					{
//						// TODO Auto-generated method stub
//						if (NetworkUtil
//								.isNetWorkConnected(RecruitCenterMainActivity.this))
//						{
//							page = 1;
//							getData(page);
//							mPullToRefreshListView.onPullDownRefreshComplete();
//						} else
//						{
//							T.shortToast(
//									"网络连接失败");
//
//						}
//					}
//
//					@Override
//					public void onPullUpToRefresh(
//							PullToRefreshBase<ListView> refreshView)
//					{
//						// TODO Auto-generated method stub
//						if (NetworkUtil
//								.isNetWorkConnected(RecruitCenterMainActivity.this))
//						{
//							getData(++page);
//							mPullToRefreshListView.onPullUpRefreshComplete();
//						} else
//						{
//							T.shortToast(
//									"网络连接失败");
//						}
//
//					}
//				});

    }

    /**
     * 获取网络数据
     *
     * @param page
     */
    public void getData(final int page) {
//		dialog = new ProgressDialog(this, "正在加载中...");
//		dialog.setCanceledOnTouchOutside(false);
//		dialog.showProgersssDialog();
        String sign = MD5Util.MD5(Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        if (YMUserService.isGuest()) {
            params.put("uid", "");
        } else {
            params.put("uid", YMApplication.getLoginInfo().getData().getPid());
        }

        params.put("page", page + "");
        params.put("pagesize", 15 + "");
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.Recruit_Center_Url, params,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        if (page == 1) {

                            recinfo = ResolveJson.R_Recruit_Center(t.toString());
                            if (recinfo != null) {
                                handler.sendEmptyMessage(100);
                            } else {
                                no_data.setVisibility(View.VISIBLE);
                            }
                            swip.setRefreshing(false);

                        } else {
                            recinfo_down = ResolveJson.R_Recruit_Center(t
                                    .toString());
                            if (recinfo_down != null) {
                                handler.sendEmptyMessage(200);
                            }
                            list_recruit.onLoadComplete();
                        }

//						dialog.dismiss();
                        super.onSuccess(t);
                    }

                    @Override
                    public void onLoading(long count, long current) {
                        // TODO Auto-generated method stub
                        super.onLoading(count, current);
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
//						dialog.dismiss();
                        super.onFailure(t, errorNo, strMsg);
                    }
                });
    }

    public void onClick_back(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.btn2:
                // T.showNoRepeatShort(RecruitCenterMainActivity.this, "我的");
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(RecruitCenterMainActivity.this).context);
                } else {
                    intent = new Intent(this, MyResumeActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.re_seach:
                StatisticalTools.eventCount(RecruitCenterMainActivity.this, "zpsearch");
                intent = new Intent(RecruitCenterMainActivity.this,
                        RecruitSerchEditAcitvity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
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
    protected void onDestroy() {
        // TODO Auto-generated method stub
        serchinfo = null;
//		if(dialog!=null)
//		{
//			dialog.dismiss();
//		}

        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    protected void onResume() {

        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        if (serchinfo == null) {
            serchinfo = new RecruitSerchInfo();
        }

        StatisticalTools.onResume(RecruitCenterMainActivity.this);

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub

        StatisticalTools.onPause(RecruitCenterMainActivity.this);
        super.onPause();

    }

    @Override
    public void loadMore() {
        // TODO Auto-generated method stub
        if (NetworkUtil
                .isNetWorkConnected()) {
            getData(++page);
        } else {
            ToastUtils.shortToast(
                    "网络连接失败");
        }
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        if (NetworkUtil
                .isNetWorkConnected()) {
            page = 1;
            getData(page);
        } else {
            ToastUtils.shortToast(
                    "网络连接失败");

        }

    }

}
