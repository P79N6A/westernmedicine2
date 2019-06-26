package com.xywy.askforexpert.module.main.service.recruit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.model.RecruitSerchMainInfo;
import com.xywy.askforexpert.module.main.service.recruit.adapter.BaseRecruiSerchMainAdapter;
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
public class RecruitSerchMainActivity extends Activity implements
        SwipeRefreshLayout.OnRefreshListener,
        OnLoadMore {
    // private PullToRefreshListView mPullToRefreshListView;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
    public RecruitSerchMainInfo recinfo = new RecruitSerchMainInfo();
    private MyLoadMoreListView list_recruit;
    private int page = 1;
    private RecruitSerchMainInfo recinfo_down;
    private BaseRecruiSerchMainAdapter adapter;
    private TextView tv_serch_content;
    private String type;

    private LinearLayout no_data;
    private TextView tv_nodata_title;
    private ImageView img_nodata;
    private SwipeRefreshLayout swip;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 100:
                    if (recinfo != null && recinfo.getCode().equals("0")) {
                        if (recinfo.getList().getData() != null) {
                            if (recinfo.getList().getData().size() > 0) {
                                adapter = new BaseRecruiSerchMainAdapter(
                                        RecruitSerchMainActivity.this);
                                adapter.setData(recinfo.getList().getData());
                                list_recruit.setAdapter(adapter);
                                no_data.setVisibility(View.GONE);
                            } else {
                                no_data.setVisibility(View.VISIBLE);
                            }

                            if (recinfo.getList().getData().size() < 15) {
                                // mPullToRefreshListView.setHasMoreData(false);
                                // mPullToRefreshListView.getFooterLoadingLayout()
                                // .show(false);
                                list_recruit.setLoading(true);
                                list_recruit.noMoreLayout();

                            } else {
                                // mPullToRefreshListView.setHasMoreData(true);
                                list_recruit.setLoading(false);
                            }

                        } else {
                            no_data.setVisibility(View.VISIBLE);
                        }
                    } else {
                        no_data.setVisibility(View.VISIBLE);
                    }
                    break;

                case 200:
                    if (recinfo != null & recinfo_down != null
                            && recinfo_down.getCode().equals("0")) {

                        if (recinfo_down.getList().getData() == null) {
//						T.shortToast( "没有更多数据了");
                            page--;
                            list_recruit.LoadingMoreText(getResources().getString(R.string.no_more));
                            list_recruit.setLoading(true);
                            return;
                        }
                        if (recinfo_down.getList().getData().size() == 0) {
                            // mPullToRefreshListView.setHasMoreData(false);
                            // mPullToRefreshListView.getFooterLoadingLayout().show(
                            // false);
//						T.shortToast( "没有更多数据了");

                            page--;
                            list_recruit.LoadingMoreText(getResources().getString(R.string.no_more));
                            list_recruit.setLoading(true);

                        }
                        recinfo.getList().getData()
                                .addAll(recinfo_down.getList().getData());
                        adapter.setData(recinfo.getList().getData());
                        adapter.notifyDataSetChanged();
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
        no_data = (LinearLayout) findViewById(R.id.lin_nodata);
        tv_nodata_title = (TextView) findViewById(R.id.tv_nodata_title);
        tv_nodata_title.setText("抱歉，暂时没有这样的职位，请修改条件重新搜索");
        img_nodata = (ImageView) findViewById(R.id.img_nodate);
        img_nodata.setBackgroundResource(R.drawable.service_more_none);

        // mPullToRefreshListView = (PullToRefreshListView)
        // findViewById(R.id.list_recruit);
        // mPullToRefreshListView.setPullLoadEnabled(true);
        // mPullToRefreshListView.setScrollLoadEnabled(true);
        // list_recruit = new ListView(RecruitSerchMainActivity.this);
        list_recruit = (MyLoadMoreListView) findViewById(R.id.list_recruit);
        list_recruit.setFadingEdgeLength(0);

        list_recruit.setLoadMoreListen(this);
        swip = (SwipeRefreshLayout) findViewById(R.id.swip_index);
        swip.setColorSchemeResources(R.color.color_scheme_2_1,
                R.color.color_scheme_2_2, R.color.color_scheme_2_3,
                R.color.color_scheme_2_4);
        swip.setOnRefreshListener(this);

        tv_serch_content = (TextView) findViewById(R.id.tv_serch_content);
        // list_recruit = mPullToRefreshListView.getRefreshableView();
        ((TextView) findViewById(R.id.tv_title)).setText("招聘搜索");
        type = getIntent().getStringExtra("type");
        findViewById(R.id.re_seach).setVisibility(View.GONE);
        findViewById(R.id.btn2).setVisibility(View.GONE);
        setLastUpdateTime();
        if (NetworkUtil.isNetWorkConnected()) {
            getData(page);
        } else {
            ToastUtils.shortToast("网络连接失败");
            no_data.setVisibility(View.VISIBLE);
            tv_nodata_title.setText("网络连接失败");
        }

        // init();
        // tv_serch_content.setText("工作地点/职位类别/薪资/学历/经验/工作性质/企业性质");
        list_recruit.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(RecruitSerchMainActivity.this,
                        RecruitWebMianActivity.class);
                intent.putExtra("id", recinfo.getList().getData().get(arg2)
                        .getId());
                intent.putExtra("url", recinfo.getList().getData().get(arg2)
                        .getUrl());
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
        // if (NetworkUtil
        // .isNetWorkConnected(RecruitSerchMainActivity.this))
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
        // if (NetworkUtil
        // .isNetWorkConnected(RecruitSerchMainActivity.this))
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

    }

    /**
     * 获取网络数据
     *
     * @param page
     */
    public void getData(final int page) {
        // final ProgressDialog dialog = new ProgressDialog(this, "正在搜索中...");
        // dialog.setCanceledOnTouchOutside(false);
        // dialog.showProgersssDialog();
        String sign = MD5Util.MD5("key" + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("page", page + "");
        params.put("pagesize", 15 + "");
        params.put("sign", sign);
        String area1 = "";
        String area2 = "";
        String jobtype = "";
        String money = "";
        String xueli = "";
        String workyear = "";
        String worktype = "";
        String workstat = "";
        String area_2_id = "";
        String area_1_id = "";
        if (RecruitCenterMainActivity.serchinfo != null) {

            area1 = RecruitCenterMainActivity.serchinfo.getArea_1();
            area2 = RecruitCenterMainActivity.serchinfo.getArea_2();
            jobtype = RecruitCenterMainActivity.serchinfo.getJobtype();
            money = RecruitCenterMainActivity.serchinfo.getMoney();
            xueli = RecruitCenterMainActivity.serchinfo.getXueli_id();
            workyear = RecruitCenterMainActivity.serchinfo.getWork_year_id();
            worktype = RecruitCenterMainActivity.serchinfo.getWork_type_id();
            workstat = RecruitCenterMainActivity.serchinfo.getCom_type_id();
            area_2_id = RecruitCenterMainActivity.serchinfo.getArea_2_id();
            area_1_id = RecruitCenterMainActivity.serchinfo.getArea_1_id();
        }

        if (!TextUtils.isEmpty(workstat)) {
            params.put("workstate", workstat);
        }
        if (!TextUtils.isEmpty(xueli)) {
            params.put("study", xueli);
        }
        if (!TextUtils.isEmpty(workyear)) {
            params.put("workyear", workyear);
        }
        if (!TextUtils.isEmpty(money)) {
            params.put("salarymin",
                    RecruitCenterMainActivity.serchinfo.getMoney_low());
            params.put("salarymax",
                    RecruitCenterMainActivity.serchinfo.getMoney_top());
        }
        if (!TextUtils.isEmpty(worktype)) {
            params.put("worktype", worktype);
        }
        if (!TextUtils.isEmpty(jobtype)) {
            params.put("title", jobtype);
        }

        if (!TextUtils.isEmpty(area_1_id)) {
            if (TextUtils.isEmpty(area_2_id)) {
                params.put("workplaceprovince", area_1_id);
            } else {
                params.put("workplacecity", area_2_id);
            }
        }

        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.Recruit_Serch_Url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                Gson gson = new Gson();
                if (page == 1) {
                    recinfo = gson.fromJson(t.toString(),
                            RecruitSerchMainInfo.class);
                    // recinfo = ResolveJson.R_Recruit_Center(t.toString());
                    if (recinfo != null) {
                        handler.sendEmptyMessage(100);
                    }

                    swip.setRefreshing(false);

                } else {
                    recinfo_down = gson.fromJson(t.toString(),
                            RecruitSerchMainInfo.class);
                    // recinfo_down = ResolveJson.R_Recruit_Center(t
                    // .toString());
                    if (recinfo_down != null) {
                        handler.sendEmptyMessage(200);
                    }
                    list_recruit.onLoadComplete();
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
                // dialog.dismiss();
                // no_data.setVisibility(View.VISIBLE);
                list_recruit.onLoadComplete();
                swip.setRefreshing(false);
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
                ToastUtils.shortToast("我的");
                intent = new Intent(RecruitSerchMainActivity.this,
                        MyResumeActivity.class);
                startActivity(intent);
                break;
            case R.id.re_seach:
                intent = new Intent(RecruitSerchMainActivity.this,
                        RecruitSerchEditAcitvity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
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
    protected void onResume() {
        super.onResume();
        StatisticalTools.onResume(RecruitSerchMainActivity.this);

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        StatisticalTools.onPause(RecruitSerchMainActivity.this);
        super.onPause();

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
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
