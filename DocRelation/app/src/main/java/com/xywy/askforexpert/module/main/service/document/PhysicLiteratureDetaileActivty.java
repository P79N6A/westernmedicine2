package com.xywy.askforexpert.module.main.service.document;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.artifex.mupdfdemo.MuPDFActivity;
import com.google.gson.Gson;
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
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.DownFileItemInfo;
import com.xywy.askforexpert.model.wenxian.WXditaileBean;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 文献详情
 *
 * @author apple
 */
public class PhysicLiteratureDetaileActivty extends Activity implements
        OnClickListener {

    protected static final String TAG = "PhysicLiteratureDetaileActivty";
    private ImageView back;
    private Activity context;
    private TextView bt_down, tv_title_down, tv_year, tv_guanjianci,
            tv_come_from, tv_oauzer, tv_title_name, tv_size, tv_abstract,
            tv_Volum, tv_Issue, tv_Page, tv_hy_name;
    private String articleID, DBID;// 论文id
    private WXditaileBean mBean;
    private ImageView iv_connect;
    private String type = "ins";// 收藏
    private RelativeLayout re_down;

    /**
     * 从数据库中获取当前文献是否下载过
     */
    private List<DownFileItemInfo> items = new ArrayList<DownFileItemInfo>();

    private Class<DownFileItemInfo> clazz;

    private FinalDb fb;

    private String userid = "0";
    /**
     * 是否打开
     */
    private boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        context = this;

        articleID = getIntent().getStringExtra("ArticleID");
        DBID = getIntent().getStringExtra("DBID");

        setContentView(R.layout.physic_literature_activity);

        initview();
        initdata();
        initLinsener();

    }

    private void initview() {

        back = (ImageView) findViewById(R.id.iv_back);
        bt_down = (TextView) findViewById(R.id.bt_down);
        re_down = (RelativeLayout) findViewById(R.id.re_down);
        tv_year = (TextView) findViewById(R.id.tv_year);
        tv_title_down = (TextView) findViewById(R.id.tv_title_down);
        tv_guanjianci = (TextView) findViewById(R.id.tv_guanjianci);
        tv_come_from = (TextView) findViewById(R.id.tv_come_from);
        tv_oauzer = (TextView) findViewById(R.id.tv_oauzer);
        tv_title_name = (TextView) findViewById(R.id.tv_title_name);
        tv_size = (TextView) findViewById(R.id.tv_size);
//		tv_Volum = (TextView) findViewById(R.id.tv_Volum);
//		tv_Page = (TextView) findViewById(R.id.tv_Page);
//		tv_Issue = (TextView) findViewById(R.id.tv_Issue);
        tv_hy_name = (TextView) findViewById(R.id.tv_hy_name);
        tv_abstract = (TextView) findViewById(R.id.tv_abstract);
        iv_connect = (ImageView) findViewById(R.id.iv_connect);

    }

    private void initdata() {
        clazz = DownFileItemInfo.class;
        fb = FinalDb.create(this, "coupon.db");

        if (YMUserService.isGuest()) {
            userid = "0";
        } else {
            userid = YMApplication.getLoginInfo().getData().getPid();
        }

        FinalHttp fh = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("a", "literature");
        ajaxParams.put("m", "zpDetails");
        ajaxParams.put("DBID", DBID);
        ajaxParams.put("id", articleID);
        if (!YMUserService.isGuest()) {
            ajaxParams.put("uid", YMApplication.getLoginInfo().getData()
                    .getPid());
        }
        ajaxParams.put("bind", articleID);
        String sign = MD5Util.MD5(articleID + Constants.MD5_KEY);
        ajaxParams.put("sign", sign);
        fh.post(CommonUrl.wenxian, ajaxParams, new AjaxCallBack() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);

                DLog.i(TAG, t.toString());
                getSeverDataSucess(t.toString());

                // 取数据从数据库
                try {
                    items = fb.findAllByWhere(clazz,
                            "movieName='" + mBean.getTitle()
                                    + "'  and userid='" + userid
                                    + "'  and commed = '" + "1" + "'");
                    if (items.size() > 0) {
                        if (items.get(0).getDownloadState() == 6) {
                            bt_down.setText("打开");
                            isOpen = true;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

    }

    /**
     * 获取数据成功
     *
     * @param
     */
    private void getSeverDataSucess(String t) {
        Gson mGson = new Gson();

        try {
            mBean = mGson.fromJson(t, WXditaileBean.class);

        } catch (Exception e) {
            DLog.i(TAG, "数据错误");
            return;
        }
        if (mBean == null) {
            return;
        }
        iv_connect.setVisibility(View.VISIBLE);
        // 判断值是否为空，空就隐藏控件
        isEmpty();

        if ("已收藏".equals(mBean.getInfo())) {
            type = "del";
        } else {
            type = "ins";
        }
        if ("WF_HY".equals(DBID)) {
            tv_hy_name.setVisibility(View.VISIBLE);
        } else {
            tv_hy_name.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(mBean.Conference)) {
            tv_hy_name.setText("会议名称:  " + mBean.Conference);
        }

        if ("WF_NSTL".equals(DBID)) {
            re_down.setVisibility(View.VISIBLE);
            bt_down.setEnabled(false);
            bt_down.setTextColor(getResources().getColor(R.color.gray_text));
            bt_down.setBackgroundResource(R.drawable.blue_long_btn_press);
        } else {
            if ("1".equals(mBean.getHasOriginalDoc())) {// 有全文可以下载
                re_down.setVisibility(View.VISIBLE);

            } else {
                re_down.setVisibility(View.VISIBLE);
                bt_down.setBackgroundResource(R.drawable.blue_long_btn_press);
                bt_down.setEnabled(false);
                bt_down.setTextColor(getResources().getColor(R.color.gray_text));
            }
        }

        setBac();

        // }

    }

    /***
     * 判断值是否为空，空就隐藏控件
     ***/
    private void isEmpty() {

        if (TextUtils.isEmpty(mBean.getYear())) {
            tv_year.setVisibility(View.GONE);
        } else {

            tv_year.setText("发表年份:  " + mBean.getYear());
        }

        if (TextUtils.isEmpty(mBean.getKeyWords())) {
            tv_guanjianci.setVisibility(View.GONE);
        } else {
            tv_guanjianci.setText("关键词:  " + mBean.getKeyWords());
        }

//		if (TextUtils.isEmpty(mBean.getIssue())) {
//			tv_Issue.setVisibility(View.GONE);
//		} else {
//			tv_Issue.setText("期:  " + mBean.getIssue());
//		}

//		if (TextUtils.isEmpty(mBean.getVolum())) {
//			tv_Volum.setVisibility(View.GONE);
//		} else {
//			tv_Volum.setText("卷:  " + mBean.getVolum());
//		}

//		if (TextUtils.isEmpty(mBean.getPage())) {
//			tv_Page.setVisibility(View.GONE);
//		} else {
//			tv_Page.setText("页码:  " + mBean.getPage());
//		}

        if (TextUtils.isEmpty(mBean.getCreator())) {
            tv_oauzer.setVisibility(View.GONE);
        } else {
            tv_oauzer.setText("作者:  " + mBean.getCreator());
        }

        if (TextUtils.isEmpty(mBean.getSource())) {
            tv_come_from.setVisibility(View.GONE);
        } else {
            tv_come_from.setText("来源:  " + mBean.getSource());
        }
        if (!TextUtils.isEmpty(mBean.getTitle())) {
            tv_title_down.setText(mBean.getTitle().length() > 8 ? mBean
                    .getTitle().subSequence(0, 8) + "..." : mBean.getTitle());
        }
        tv_title_name.setText(mBean.getTitle());
        tv_abstract.setText("        " + mBean.getAbstract());
    }

    private void setBac() {
        if ("del".equals(type)) {
            iv_connect.setBackgroundResource(R.drawable.collect_nobtn_sector_);
        } else {
            iv_connect.setBackgroundResource(R.drawable.collected_btn_sector);

        }
    }

    private void initLinsener() {
        back.setOnClickListener(this);
        iv_connect.setOnClickListener(this);
        bt_down.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:

                context.finish();
                break;
            case R.id.bt_down:
                StatisticalTools.eventCount(this, "xiazai");
                if (!NetworkUtil.isNetWorkConnected()) {
                    ToastUtils.shortToast( "网络链接异常");
                    return;
                }

                if (mBean == null) {
                    return;
                }
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog_back(new YMOtherUtils(context).context);
                    return;
                }
                if (isOpen) {
                    String path = items.get(0).getFilePath().trim();
                    DLog.i("PDF ", "path==" + path);
                    try {
                        Uri uri = Uri.parse(path);
                        Intent intent = new Intent(context, MuPDFActivity.class);
                        intent.setAction(Intent.ACTION_VIEW);
                        // intent.putExtra("path",
                        // items.get(arg2).getFilePath().toString().trim());
                        intent.setData(uri);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        DLog.i("PDF ", "e==" + e);
                        DLog.i(TAG, "PDF打开失败原因 " + e);
                        ToastUtils.shortToast( "pdf打开失败");
                    }
                } else {
                    Intent intent = new Intent(context, PhysicLiteraturePayActivity.class);
                    intent.putExtra("ArticleID", articleID);
                    intent.putExtra("DBID", mBean.getDBID());
                    intent.putExtra("fileName", mBean.getTitle());
                    startActivityForResult(intent, 201518);
                }
                break;
            case R.id.iv_connect:
                StatisticalTools.eventCount(this, "shoucang");

                if (!NetworkUtil.isNetWorkConnected()) {
                    ToastUtils.shortToast( "网络链接异常");
                    return;
                }

                if (YMUserService.isGuest()) {// 是游客
                    DialogUtil.LoginDialog_back(new YMOtherUtils(context).context);
                } else {
                    getConnectData();
                }

                break;

        }

    }

    /**
     * 收藏接口
     */
    private void getConnectData() {
        FinalHttp ft = new FinalHttp();

        AjaxParams params = new AjaxParams();
        params.put("a", "literature");
        params.put("m", "zpCollect");
        params.put("lid", articleID);
        params.put("bind", articleID);
        params.put("DBID", DBID);
        params.put("title", mBean.getTitle());

        params.put("uid", YMApplication.getLoginInfo().getData().getPid());
        String sgn = MD5Util.MD5(articleID + Constants.MD5_KEY);
        params.put("sign", sgn);
        params.put("type", type);

        ft.post(CommonUrl.wenxian, params, new AjaxCallBack() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }

            @Override
            public void onStart() {
                super.onStart();

            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);

                try {
                    JSONObject JS = new JSONObject(t.toString());
                    if ("0".equals(JS.getString("code"))) {
                        // T.shortToast( JS.getString("status"));
                        DLog.d("shoucang", JS.getString("msg"));
                        if ("取消收藏成功".equals(JS.getString("msg"))) {
                            type = "ins";
                            ToastUtils.shortToast( "取消收藏");
                        } else if ("收藏成功".equals(JS.getString("msg"))) {
                            type = "del";
                            ToastUtils.shortToast( JS.getString("msg"));
                        } else {
                            ToastUtils.shortToast( JS.getString("msg"));
                        }
                        setBac();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                DLog.i(TAG, "----" + t.toString());
            }
        });

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        fb = null;

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatisticalTools.onResume(this);
        // TODO Auto-generated method stub

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        StatisticalTools.onPause(this);
        super.onPause();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 201518) {
            if (resultCode == 201518) {
                DLog.i(TAG, "iiiiiii");
                initdata();
            }
        }
    }
}
