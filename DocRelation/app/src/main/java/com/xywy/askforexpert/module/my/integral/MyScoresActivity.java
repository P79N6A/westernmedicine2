package com.xywy.askforexpert.module.my.integral;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.model.MyScoresInfo;
import com.xywy.askforexpert.module.main.service.document.IntegralRechargeactivity;
import com.xywy.askforexpert.module.main.service.que.QuePerActivity;
import com.xywy.askforexpert.widget.view.MyLoadMoreListView;
import com.xywy.askforexpert.widget.view.MyLoadMoreListView.OnLoadMore;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的积分
 *
 * @author 王鹏
 * @2015-10-21下午4:11:24
 */
public class MyScoresActivity extends Activity implements
        SwipeRefreshLayout.OnRefreshListener,
        OnLoadMore {

    SwipeRefreshLayout swip;
    private MyLoadMoreListView list_scores;
    int page;
    BaseMyScoresAdapter adapter;
    private View line_chongzhi;
    private LinearLayout no_data, linear_chongzhi, linear_nodate;
    private TextView tv_myscore, tv_guoqi_title;
    private MyScoresInfo scoreinfo;
    private MyScoresInfo scoreinfo_down;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 100:
                    if (scoreinfo != null && scoreinfo.getData() != null) {
                        if (scoreinfo.getData().getDetail() == null) {
                            if (!"0".equals(scoreinfo.getCode())) {
                                ToastUtils.shortToast(scoreinfo.getMsg());
                            }
                            linear_nodate.setVisibility(View.VISIBLE);
                            list_scores.setAdapter(null);
                            list_scores.setLoading(false);
                            list_scores.noMoreLayout();
                            return;
                        }
                        if ("0".equals(scoreinfo.getCode())) {
                            initview(scoreinfo.getData().getPoints()
                                    .getTotal_score(), scoreinfo.getData()
                                    .getPoints().getGone_score());
                            if (scoreinfo.getData().getDetail().size() == 0) {
                                // List<MyScoresInfo> list = new
                                // ArrayList<MyScoresInfo>();
                                // adapter = new BaseMyScoresAdapter(
                                // MyScoresActivity.this,list);
                                // list_scores.setAdapter(adapter);
                                linear_nodate.setVisibility(View.VISIBLE);
                                list_scores.setAdapter(null);
                                list_scores.setLoading(false);
                                list_scores.noMoreLayout();
                                return;
                            }
                            if (scoreinfo.getData().getDetail().size() < 20) {
                                // mPullToRefreshListView.setHasMoreData(false);
                                linear_nodate.setVisibility(View.GONE);

                                list_scores.setLoading(true);
                                list_scores.noMoreLayout();
                            } else {
                                // mPullToRefreshListView.setHasMoreData(true);
                                linear_nodate.setVisibility(View.GONE);
                                list_scores.setLoading(false);
                            }
                            // updateList(codex.getList());
                            adapter = new BaseMyScoresAdapter(
                                    MyScoresActivity.this, scoreinfo.getData()
                                    .getDetail());
                            adapter.setData(scoreinfo.getData().getDetail());
                            list_scores.setAdapter(adapter);
                        } else {
                            ToastUtils.shortToast(scoreinfo.getMsg());
                        }
                    }
                    break;
                case 200:
                    if (scoreinfo_down != null && scoreinfo_down.getData() != null && scoreinfo_down.getData().getDetail() != null
                            && scoreinfo_down.getCode().equals("0")) {
                        if (scoreinfo != null) {
                            if (scoreinfo.getData() != null) {
                                if (scoreinfo.getData().getDetail() != null) {
                                    scoreinfo.getData().getDetail()
                                            .addAll(scoreinfo_down.getData().getDetail());
                                    adapter.setData(scoreinfo.getData().getDetail());
                                    adapter.notifyDataSetChanged();
                                    if (scoreinfo_down.getData().getDetail().size() == 0) {
                                        // T.showNoRepeatShort(CodexSecondActivity.this,
                                        // "没有更多数据了");
                                        page--;
                                        list_scores.LoadingMoreText(getResources().getString(
                                                R.string.no_more));
                                        list_scores.setLoading(true);
                                    }
                                }
                            }
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
        setContentView(R.layout.my_scores_main);

        ((TextView) findViewById(R.id.tv_title)).setText("我的积分");
        list_scores = (MyLoadMoreListView) findViewById(R.id.list_scores);
        list_scores.setLoadMoreListen(this);
        swip = (SwipeRefreshLayout) findViewById(R.id.swip_index);
        swip.setColorSchemeResources(R.color.color_scheme_2_1,
                R.color.color_scheme_2_2, R.color.color_scheme_2_3,
                R.color.color_scheme_2_4);
        swip.setOnRefreshListener(this);

        View layout = LayoutInflater.from(MyScoresActivity.this).inflate(
                R.layout.my_score_header, null);

        linear_chongzhi = (LinearLayout) layout
                .findViewById(R.id.linear_chongzhi);
        tv_myscore = (TextView) layout.findViewById(R.id.tv_myscore);
        tv_guoqi_title = (TextView) layout.findViewById(R.id.tv_guoqi_title);
        linear_nodate = (LinearLayout) layout.findViewById(R.id.linear_nodate);
        line_chongzhi =  layout.findViewById(R.id.line_chongzhi);

        list_scores.addHeaderView(layout, null, false);
        if (NetworkUtil.isNetWorkConnected()) {
            // linear_nodate.setVisibility(View.VISIBLE);
            linear_nodate.setVisibility(View.GONE);
            list_scores.setAdapter(null);
            page = 1;
            swip.post(new Runnable() {
                @Override
                public void run() {
                    swip.setRefreshing(true);
                    getData(page);

                }
            });
        } else {
            ToastUtils.shortToast("网络连接失败");
            List<MyScoresInfo> list = new ArrayList<MyScoresInfo>();
            adapter = new BaseMyScoresAdapter(MyScoresActivity.this, list);
            linear_nodate.setVisibility(View.VISIBLE);
            list_scores.setAdapter(null);
            list_scores.setLoading(false);
            list_scores.noMoreLayout();
        }

        //医脉 去掉积分充值功能 医生助手暂时不用管 stone
        if (!YMApplication.sIsYSZS) {
           linear_chongzhi.setVisibility(View.GONE);
           line_chongzhi.setVisibility(View.GONE);
        }

    }

    public void initview(String total, String gone) {
        tv_myscore.setText(total);
        tv_guoqi_title.setText(gone);
    }

    public void getData(final int page) {
        String userid = YMApplication.getLoginInfo().getData().getPid();
        String sign = MD5Util.MD5(userid + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("command", "detail");
        params.put("pagenum", page + "");
        params.put("pagesize", 20 + "");
        params.put("userid", userid);
        params.put("ismoney", "0");
        params.put("isold", "0");
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();

        fh.post(CommonUrl.ScoresPointUrl, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {

                Gson gson = new Gson();
                if (page == 1) {
                    scoreinfo = gson.fromJson(t.toString(), MyScoresInfo.class);
                    handler.sendEmptyMessage(100);
                    swip.setRefreshing(false);
                } else {
                    scoreinfo_down = gson.fromJson(t.toString(),
                            MyScoresInfo.class);
                    handler.sendEmptyMessage(200);
                    list_scores.onLoadComplete();
                }
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                if (page == 1) {
                    linear_nodate.setVisibility(View.VISIBLE);
                    list_scores.setAdapter(null);
                    list_scores.setLoading(false);
                    list_scores.noMoreLayout();
                }
                swip.setRefreshing(false);
                list_scores.onLoadComplete();
                ToastUtils.shortToast("网络链接超时");
                super.onFailure(t, errorNo, strMsg);
            }
        });

    }

    @Override
    public void loadMore() {

        if (NetworkUtil.isNetWorkConnected()) {
            getData(++page);
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
            swip.setRefreshing(false);
            ToastUtils.shortToast("网络连接失败");
        }

    }

    public void onClick_back(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.linear_chongzhi:
                StatisticalTools.eventCount(MyScoresActivity.this, "scorerecharge");
                if (YMApplication.isDoctor()) {
                    if (YMApplication.isDoctorApprove()) {
                        if (YMApplication.getLoginInfo().getData() != null) {

                            if ("-1".equals(YMApplication.getLoginInfo().getData().getXiaozhan().getDati())) {
                                ToastUtils.shortToast(
                                        "您还未认证，还没有积分充值功能权限");

                            } else {
                                intent = new Intent(this,
                                        IntegralRechargeactivity.class);
                                intent.putExtra("type", "1");
                                startActivity(intent);
                            }

                        }

                    } else {
                        ToastUtils.shortToast(
                                "您还未认证，还没有积分充值功能权限");
                    }
                } else {
                    ToastUtils.shortToast(
                            "您是医学生身份，暂时不能使用积分充值功能");
                }

                break;
            case R.id.linear_guoqi:
                StatisticalTools.eventCount(MyScoresActivity.this, "overduescore");
                if (!"0".equals(tv_guoqi_title.getText())) {
                    intent = new Intent(this, GoneScoresActivity.class);
                    intent.putExtra("userid", YMApplication.getLoginInfo().getData().getPid());
                    startActivity(intent);
                }

                break;
            case R.id.rela_guiz:
                StatisticalTools.eventCount(MyScoresActivity.this, "scorerule");
                intent = new Intent(MyScoresActivity.this, QuePerActivity.class);
                intent.putExtra("isfrom", "积分规则");
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        StatisticalTools.onResume(this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        StatisticalTools.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
