package com.xywy.askforexpert.module.doctorcircle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.model.doctor.InterestePersonBean;
import com.xywy.askforexpert.model.doctor.InterestePersonItemBean;
import com.xywy.askforexpert.module.doctorcircle.adapter.InteresteAdapter;
import com.xywy.base.view.ProgressDialog;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.List;

/**
 * 更多感兴趣的人
 *
 * @autapple
 *
 *
 */
public class InterestPersonActivity extends YMBaseActivity{

    private RecyclerView mRecyclerView;
    private String uid = "0";
//    private ImageView back;
    private Activity context;
    private ProgressDialog pb;
    private String type;
    private View lin_nodata;
    private TextView tv_nodata_title;
//    private TextView in_tv_title;
    private ImageView img_nodate;

    private SharedPreferences sp;

    private int unVisitCount;

    public  static void startActivity(Context context, String type){
        startActivity(context,type,0);
    }
    public  static void startActivity(Context context, String type,int unvisitCount){
        Intent morePerson = new Intent(context, InterestPersonActivity.class);
        morePerson.putExtra("type", type);
        morePerson.putExtra("unVisitCount", unvisitCount);
        context.startActivity(morePerson);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_interest;
    }

    @Override
    protected void beforeViewBind() {
        context = this;
        if (!YMUserService.isGuest()) {
            uid = YMApplication.getLoginInfo().getData().getPid();
        }

        sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        sp.edit().putString("mustUpdata", "").apply();
        pb = new ProgressDialog(this, "正在加载中...");
        //type = 1  推荐好友 3 访问用户列表
        type = getIntent().getStringExtra("type");
        unVisitCount= getIntent().getIntExtra("unVisitCount",0);
    }


    protected void initView() {
//        in_tv_title = (TextView) findViewById(R.id.in_tv_title);
//        if ("2".equals(type)) {
//            in_tv_title.setText("我的访客");
//            img_nodate.setVisibility(View.GONE);
//        }
//        back = (ImageView) findViewById(R.id.iv_back);
        img_nodate = (ImageView) findViewById(R.id.img_nodate);
        if ("2".equals(type)) {
            titleBarBuilder.setTitleText("我的访客");
            img_nodate.setVisibility(View.GONE);
        }else {
            titleBarBuilder.setTitleText("您可能感兴趣的人");
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        lin_nodata = findViewById(R.id.lin_nodata);
        tv_nodata_title = (TextView) findViewById(R.id.tv_nodata_title);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


    }

    protected void initData() {
        if (pb != null && !pb.isShowing()) {
            pb.show();
        }

        String bind = uid + type;
        String sign = MD5Util.MD5(bind + Constants.MD5_KEY);
        FinalHttp fh = new FinalHttp();
        AjaxParams params = new AjaxParams();
        params.put("a", "doctor");
        params.put("m", "doctor_list_all");
        params.put("userid", uid);
        params.put("bind", bind);
        params.put("type", type);
        params.put("sign", sign);
        fh.post(CommonUrl.doctor_circo_url, params, new AjaxCallBack() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (pb.isShowing()) {
                    pb.closeProgersssDialog();
                }
                ToastUtils.shortToast( "网络繁忙，请稍后再试");

                mRecyclerView.setVisibility(View.GONE);
                lin_nodata.setVisibility(View.VISIBLE);
                tv_nodata_title.setText("暂无数据");
            }


            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                if (pb.isShowing()) {
                    pb.closeProgersssDialog();
                }
                if (TextUtils.isEmpty(t.toString())) {
                    return;
                }
                getDataSucess(t.toString());

            }
        });

//        setListener();

    }

    private void getDataSucess(String t) {
        InterestePersonBean mBean = null;
        try {
            mBean = new Gson().fromJson(t, InterestePersonBean.class);
        } catch (Exception e) {
            ToastUtils.shortToast( "网络繁忙，请稍后再试");
            return;
        }
        if (mBean == null || mBean.getData() == null || mBean.getData().size() == 0) {

            mRecyclerView.setVisibility(View.GONE);
            lin_nodata.setVisibility(View.VISIBLE);
            tv_nodata_title.setText("暂无数据");
            return;
        }
        if (mBean.getCode() == 0) {//成功

            List<InterestePersonItemBean> data = mBean.getData();
            for (int i = 0; i < data.size(); i++) {//  遍历用户名 空移除
                if (TextUtils.isEmpty(data.get(i).getNickname())) {
                    data.remove(i);
                }
            }
            InteresteAdapter myAdapter = new InteresteAdapter(context, data, type);
            myAdapter.setUnVisitCount(unVisitCount);
            mRecyclerView.setAdapter(myAdapter);


        }

    }


//    private void setListener() {
//        back.setOnClickListener(this);
//    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.iv_back:
//                this.finish();
//                break;
//
//            default:
//                break;
//        }
//    }


    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }

    @Override
    protected void onDestroy() {
        if (pb != null && pb.isShowing()) {
            pb.closeProgersssDialog();
        }
        super.onDestroy();
    }


}
