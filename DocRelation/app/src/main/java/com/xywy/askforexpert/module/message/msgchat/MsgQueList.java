package com.xywy.askforexpert.module.message.msgchat;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.model.QuestionBean;
import com.xywy.askforexpert.model.QuestionBean.Data.DataList;
import com.xywy.askforexpert.module.message.MsgQueListAdapter;
import com.xywy.base.view.CircleProgressBar;
import com.xywy.base.view.ProgressDialog;
import com.xywy.uilibrary.titlebar.ItemClickListener;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.List;

/**
 * 项目名称：D_Platform 类名称：MsgQueList 类描述：问答消息列表 创建人：shihao 创建时间：2015-6-11 下午2:35:12
 * 修改备注：
 */
public class MsgQueList extends YMBaseActivity {
    private int page = 1;
    private ListView mlistView;

    private Context context;
    private MsgQueListAdapter listAdapter;
    private QuestionBean fromJson;
    private QuestionBean moreDatafromJson;
    private View lin_nodata;
    private ProgressDialog pro;
    private String type;
    private View footview;
    private CircleProgressBar pb;
    private TextView tv_more;

    private boolean isloading = true;
    private String title;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_msg_quelist);
//        CommonUtils.initSystemBar(this);
//        this.context = this;
//
//        type = getIntent().getStringExtra("type");
//        title = getIntent().getStringExtra("title");
//
////        initView();
//        pro = new ProgressDialog(this, "正在加载中...");
//        pro.show();
//        intData();
//        setListner();
//    }

//    private void initView() {
//
//        tv_title = (TextView) findViewById(R.id.title);
//        footview = View.inflate(context, R.layout.loading_more, null);
//        footview.setVisibility(View.INVISIBLE);
//        btn_msg_back = (ImageButton) findViewById(R.id.btn_msg_back);
//        mlistView = (ListView) findViewById(R.id.lv);
//        lin_nodata = findViewById(R.id.lin_nodata);
//        findViewById(R.id.img_nodate).setBackgroundResource(R.drawable.service_more_none);
//        ((TextView) findViewById(R.id.tv_nodata_title)).setText("暂时没有数据哦~");
//        mlistView.setFadingEdgeLength(0);
//        mlistView.setDivider(null);
//        mlistView.setDividerHeight(0);
//        mlistView.setSelector(R.color.transparent);
//        pb = (CircleProgressBar) footview.findViewById(R.id.pb_more);
//        tv_more = (TextView) footview.findViewById(R.id.tv_more);
//        mlistView.addFooterView(footview);
//        footview.setVisibility(View.INVISIBLE);
//        tv_title.setText(title);
//
//    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_msg_quelist;
    }

    @Override
    protected void initView() {
        CommonUtils.initSystemBar(this);
        this.context = this;
        type = getIntent().getStringExtra("type");
        title = getIntent().getStringExtra("title");
        footview = View.inflate(context, R.layout.loading_more, null);
        footview.setVisibility(View.INVISIBLE);
        mlistView = (ListView) findViewById(R.id.lv);
        lin_nodata = findViewById(R.id.lin_nodata);
        findViewById(R.id.img_nodate).setBackgroundResource(R.drawable.service_more_none);
        ((TextView) findViewById(R.id.tv_nodata_title)).setText("暂时没有数据哦~");
        mlistView.setFadingEdgeLength(0);
        mlistView.setDivider(null);
        mlistView.setDividerHeight(0);
        mlistView.setSelector(R.color.transparent);
        pb = (CircleProgressBar) footview.findViewById(R.id.pb_more);
        tv_more = (TextView) footview.findViewById(R.id.tv_more);
        mlistView.addFooterView(footview);
        footview.setVisibility(View.INVISIBLE);
        titleBarBuilder.setTitleText(title).setBackIconClickEvent(new ItemClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(MsgQueList.this, MsgZhuShou.class);
                setResult(6000, intent);
                MsgQueList.this.finish();
            }
        }).build();
        pro = new ProgressDialog(this, "正在加载中...");
        pro.show();
        intData();
        setListner();
    }

    @Override
    protected void initData() {
    }

    private void intData() {
        if (page < 0) {
            page = 1;
        }
        String userid = YMApplication.getLoginInfo().getData().getPid();
        FinalHttp fh = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        String sign = MD5Util.MD5(userid + Constants.MD5_KEY);
        ajaxParams.put("userid", userid);
        ajaxParams.put("page", page + "");
        ajaxParams.put("pagesize", 20 + "");
        ajaxParams.put("sign", sign);
        ajaxParams.put("type", type);
        ajaxParams.put("command", "message");
        fh.post(CommonUrl.DP_COMMON, ajaxParams, new AjaxCallBack() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                isloading = true;
                footview.setVisibility(View.GONE);
                if (page == 1) {
                    mlistView.setVisibility(View.GONE);
                    lin_nodata.setVisibility(View.VISIBLE);
                }
                // if(1 == page )iv.setVisibility(View.VISIBLE);
                page--;
                if (pro != null && pro.isShowing()) {
                    pro.closeProgersssDialog();
                }
            }

            @Override
            public void onSuccess(String t) {
                // TODO Auto-generated method stub
                super.onSuccess(t);
                if (pro != null && pro.isShowing()) {
                    pro.closeProgersssDialog();
                }
                isloading = true;
                getseverData(t.toString());
            }

        });
    }

    private void getseverData(String string) {

        Gson mGson = new Gson();
        if (1 == page) {
            fromJson = mGson.fromJson(string, QuestionBean.class);
            if (fromJson.data.data.size() == 0) {
                mlistView.setVisibility(View.GONE);
                mlistView.setVisibility(View.GONE);
                lin_nodata.setVisibility(View.VISIBLE);
            } else {
                mlistView.setVisibility(View.VISIBLE);
                mlistView.setVisibility(View.VISIBLE);
                lin_nodata.setVisibility(View.GONE);
            }

            if (fromJson.data.data.size() < 10) {
                isloading = false;
            } else if (fromJson.data.data.size() < 20) {
                isloading = false;
                pb.setVisibility(View.GONE);
                tv_more.setText("没有数据了");

            }

        } else {
            moreDatafromJson = mGson.fromJson(string, QuestionBean.class);
            if (moreDatafromJson == null) {
                return;
            }
            if (moreDatafromJson.data.data.size() != 0) {
                fromJson.data.data.addAll(moreDatafromJson.data.data);
            } else {
                isloading = false;
                pb.setVisibility(View.GONE);
                tv_more.setText("没有数据了");
            }
        }
        if (fromJson == null) {
            return;
        }
        if (!fromJson.code.equals("0")) {
            return;
        }
        if (fromJson.data != null && fromJson.data.data.size() > 0) {
            List<DataList> data = fromJson.data.data;
            if (listAdapter == null) {
                listAdapter = new MsgQueListAdapter(context, data);
                mlistView.setAdapter(listAdapter);

            } else {
                listAdapter.addData(data);
            }
        }

    }

    private void setListner() {
        mlistView.setOnScrollListener(new OnScrollListener() {

            private int lastitem;

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
                // TODO Auto-generated method stub
                if (lastitem == listAdapter.getCount()
                        && arg1 == OnScrollListener.SCROLL_STATE_IDLE) {
                    if (isloading) {
                        isloading = false;
                        if (!NetworkUtil.isNetWorkConnected()) {
                            footview.setVisibility(View.GONE);
                            return;
                        }

                        footview.setVisibility(View.VISIBLE);
                        page++;
                        intData();

                    }

                }

            }

            @Override
            public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                lastitem = arg1 + arg2 - 1;
            }
        });
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if (pro != null && pro.isShowing()) {
            pro.closeProgersssDialog();
        }
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        StatisticalTools.onResume(this);

    }

    public void onPause() {
        StatisticalTools.onPause(this);
        super.onPause();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(MsgQueList.this, MsgZhuShou.class);
            setResult(6000, intent);
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
