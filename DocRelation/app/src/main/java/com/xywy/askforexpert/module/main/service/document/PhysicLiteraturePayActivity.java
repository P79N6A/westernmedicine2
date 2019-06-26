package com.xywy.askforexpert.module.main.service.document;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.artifex.mupdfdemo.MuPDFActivity;
import com.umeng.socialize.UMShareAPI;
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
import com.xywy.askforexpert.appcommon.utils.enctools.RSATools;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.DownFileItemInfo;
import com.xywy.askforexpert.module.main.service.downFile.BaseDownActivity;
import com.xywy.askforexpert.module.main.service.downFile.ContentValue;
import com.xywy.askforexpert.module.main.service.downFile.DownloadService;
import com.xywy.askforexpert.module.message.share.ShareUtil;
import com.xywy.base.view.ProgressDialog;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *  文献支付 adjustPan
 *
 * @author apple
 */
public class PhysicLiteraturePayActivity extends BaseDownActivity implements
        OnClickListener, ContentValue {
    private static final String TAG = "PhysicLiteraturePayActivity";
    final String uid = YMApplication.getLoginInfo().getData().getPid();
    @Bind(R.id.free_ll)
    LinearLayout freeLl;
    @Bind(R.id.free_iv_select)
    ImageView freeIvSelect;
    @Bind(R.id.share_doc_layout)
    RelativeLayout shareDocLayout;

    private TextView freeTvNum;
    private String koufen = "";// 下载需要扣的积分
    private String point = "";// 账号总共积分
    private int typePoint = -1;// 是否扣积分，0不扣积分
    private Activity context;
    private ImageView back;
    private ProgressDialog pro;
    private TextView tv_bind; // 绑定万方
    private String mArticleID, DBID, fileName;// 文章id,类型,文件名字
    private TextView tv_wx_username, tv_wx_integral;// 用户名，可用积分
    private EditText et_wx_username, et_wx_passwored;// 万方用户名，密码
    private LinearLayout ll_uername, ll_password;// 万方用户名，密码
    private TextView tv_wx_dowm_pay_jifen, tv_get_more_jifen, tv_balance,
            tv_donw_wangfang;// 下载需要的积分,获取更多积分,万方余额下载
    private TextView tv_bind_ok, tv_downfile_name, tv_start_down;// 确定绑定,下载文件的名字,开始下载

    private boolean isWF_XW = false;

    public static Handler mHandler = null;
    private TextView tv_wanfang_vip;
    private RelativeLayout free_selrl;
    private ImageView iv_xywy_select, iv_wanf_select;// 选择支付方式按钮
    private boolean bl_xywy_select = false; // xywy支付变量
    private boolean bl_wanfang_select = false;// wanfang支付变量
    private boolean bl_free_select = false;   //活动免费次数

    private boolean isOpen = false; // 是否下载完整

    private boolean downing = true;// 正在下载中

    private String userid;
    private List<DownFileItemInfo> items = new ArrayList<DownFileItemInfo>();
    private DownFileItemInfo item;
    private List<DownFileItemInfo> file;
    private Handler handler = new Handler();
    private Class<DownFileItemInfo> clazz;
    private FinalDb fb;
    private boolean isGo;
    private LinearLayout rela_main;
    private RelativeLayout re_bo, re_user;

    private String shareUrl = "";

    private int freeTimes = 0;
    private int freeTagOne = 0;
    private int freeTagTwo = 0;
    private Handler handler2 = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    handler.removeCallbacks(runnable);
                    break;
                case 200:
                    if (tv_start_down != null) {
                        tv_start_down.setText(msg.obj.toString());
                    }

                    break;
                case 300:
                    handler.removeCallbacks(runnable);

                    if (tv_start_down != null) {
                        tv_start_down.setText("打开");
                    }
                    isOpen = true;
                    tv_start_down.setOnClickListener(new MyOnclick(tv_start_down,
                            2, msg.obj.toString()));
                    break;
                default:
                    break;
            }

        }
    };

    private Runnable runnable = new Runnable() {

        public void run() {
            file = getMyApp().getDownloadItems();
            Message msg = Message.obtain();
            if (item == null) {
                for (int i = 0; i < file.size(); i++) {
                    if (file.get(i).getMovieName().trim().equals(fileName)) {
                        item = file.get(i);
                        break;
                    }

                }
                if (!isGo) {
                    handler.postDelayed(runnable, 0);
                }
            } else {
                if (item.getDownloadState() == DOWNLOAD_STATE_DOWNLOADING) {
                    msg.what = 200;
                    msg.obj = item.getPercentage();
                    DLog.i(TAG, "数据下载中。。。" + msg.obj);
                    handler2.sendMessage(msg);
                    if (!isGo) {
                        handler.postDelayed(runnable, 0);
                    }
                } else if (item.getDownloadState() == DOWNLOAD_STATE_SUCCESS) {
                    msg.what = 300;
                    msg.obj = item.getFilePath().trim();
                    DLog.i(TAG, "数据成功。。。" + msg.obj);
                    handler2.sendMessage(msg);
                } else {
                    if (tv_start_down != null) {
                        tv_start_down.setText("下载失败");
                    }
                }
            }
//            DLog.i(TAG, "下载查询。。。");
        }

    };
    private View free_selrl_div;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);

        if (YMUserService.isGuest()) {
            userid = "0";
        } else {
            userid = YMApplication.getLoginInfo().getData().getPid();
        }
        handler.postDelayed(runnable, 1500);
        mArticleID = getIntent().getStringExtra("ArticleID");
        fileName = getIntent().getStringExtra("fileName");
        DBID = getIntent().getStringExtra("DBID");
        isWF_XW = "WF_XW".equals(DBID);
        pro = new ProgressDialog(context, "正在加载中...");
        pro.setCanceledOnTouchOutside(false);
        setContentView(R.layout.physic_pay_activty);
        ButterKnife.bind(this);
        // initView();

    }

    @Override
    public void initView() {
        super.initView();
        clazz = DownFileItemInfo.class;
        fb = FinalDb.create(this, "coupon.db");
        rela_main = (LinearLayout) findViewById(R.id.rela_main);
        re_bo = (RelativeLayout) findViewById(R.id.re_bo);
        re_user = (RelativeLayout) findViewById(R.id.re_user);
        back = (ImageView) findViewById(R.id.iv_back);
        tv_bind = (TextView) findViewById(R.id.tv_bind);
        tv_balance = (TextView) findViewById(R.id.tv_balance);
        tv_donw_wangfang = (TextView) findViewById(R.id.tv_donw_wangfang);
        freeTvNum = (TextView) findViewById(R.id.free_num_tv);
        tv_bind_ok = (TextView) findViewById(R.id.tv_bind_ok);
        ll_uername = (LinearLayout) findViewById(R.id.ll_uername);
        tv_wanfang_vip = (TextView) findViewById(R.id.tv_wanfang_vip);
        tv_start_down = (TextView) findViewById(R.id.tv_start_down);
        ll_password = (LinearLayout) findViewById(R.id.ll_password);
        tv_wx_integral = (TextView) findViewById(R.id.tv_wx_integral);
        iv_xywy_select = (ImageView) findViewById(R.id.iv_xywy_select);
        free_selrl = (RelativeLayout) findViewById(R.id.free_selrl);
        free_selrl_div = findViewById(R.id.free_selrl_divider_line);
        iv_wanf_select = (ImageView) findViewById(R.id.iv_wanf_select);
        tv_wx_username = (TextView) findViewById(R.id.tv_wx_username);
        et_wx_username = (EditText) findViewById(R.id.et_wx_username);
        et_wx_passwored = (EditText) findViewById(R.id.et_wx_passwored);
        tv_downfile_name = (TextView) findViewById(R.id.tv_downfile_name);
        tv_get_more_jifen = (TextView) findViewById(R.id.tv_get_more_jifen);
        tv_wx_dowm_pay_jifen = (TextView) findViewById(R.id.tv_wx_dowm_pay_jifen);

        if (fileName.length() > 8) {
            String name = fileName;
            tv_downfile_name.setText(name.substring(0, 8) + "...");
        } else {

            tv_downfile_name.setText(fileName);
        }

        try {

            items = fb.findAllByWhere(clazz, "movieName='" + fileName
                    + "'  and userid='" + userid + "'  and commed = '" + "1"
                    + "'");
            String fileName = items.get(0).getMovieName();
            String filePath = items.get(0).getFilePath();
            if (!getFile(fileName, filePath)) {
                items.clear();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        if (items.size() > 0) {
            switch (items.get(0).getDownloadState()) {
                case DOWNLOAD_STATE_DOWNLOADING:
                    tv_start_down.setText(items.get(0).getPercentage());
                    break;
                case DOWNLOAD_STATE_SUSPEND:
                    tv_start_down.setText("暂停");
                    break;
                case DOWNLOAD_STATE_WATTING:
                    tv_start_down.setText("等待");
                    break;
                case DOWNLOAD_STATE_FAIL:
                    tv_start_down.setText("下载失败");
                    break;
                case DOWNLOAD_STATE_SUCCESS:
                    tv_start_down.setText("打开");
                    isOpen = true;
                    tv_start_down.setOnClickListener(new MyOnclick(tv_start_down,
                            2, items.get(0).getFilePath().toString().trim()));
                    break;
                case DOWNLOAD_STATE_DELETE:
                    tv_start_down.setText("gg");
                    break;
                case DOWNLOAD_STATE_NONE:
                    tv_start_down.setText("确认下载");
                    break;
                case DOWNLOAD_STATE_START:
                    tv_start_down.setText("确认下载");
                    break;
                case DOWNLOAD_STATE_EXCLOUDDOWNLOAD:
                    tv_start_down.setText("等待中");
                    break;

                default:
                    tv_start_down.setText("确认下载");
                    tv_start_down.setOnClickListener(new MyOnclick(tv_start_down, 1));
                    break;
            }

        } else {
            tv_start_down.setText("确认下载");
            tv_start_down.setOnClickListener(new MyOnclick(tv_start_down, 1));

        }

        initlisener();
        inidata();
    }

    private void inidata() {
        if (pro != null && !pro.isShowing()) {
            pro.show();
        }

        tv_wx_username.setText(YMApplication.getLoginInfo().getData()
                .getRealname());// 用户名

        FinalHttp fh = new FinalHttp();
        AjaxParams params = new AjaxParams();
        String sign = MD5Util.MD5(uid + Constants.MD5_KEY);

        params.put("a", "literature");
        params.put("m", "zpDown_detail");
        params.put("uid", uid);
        params.put("sign", sign);
        params.put("bind", uid);
        params.put("id", mArticleID);
        params.put("DBID", DBID);

        DLog.i("url", "url" + CommonUrl.wenxian + params);
        fh.post(CommonUrl.wenxian, params, new AjaxCallBack() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (pro.isShowing()) {
                    pro.closeProgersssDialog();
                }

                ToastUtils.shortToast( "连接网络超时");
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(String t) {
                if (pro.isShowing()) {
                    pro.closeProgersssDialog();
                }
                super.onSuccess(t);
                DLog.i(TAG, "url==" + t.toString());
                try {
                    JSONObject js = new JSONObject(t.toString());
                    String code = js.getString("code");
                    point = js.getString("point");
                    String msg = js.getString("msg");
                    koufen = js.getString("koufen");
                    typePoint = js.getInt("type");
                    freeTimes = js.getInt("times");
                    shareUrl = js.getString("srcurl");
                    freeTagOne = js.getInt("is_activity");
                    freeTagTwo = js.getInt("is_activity2");
                    if ("0".equals(code)) {// 成功
                        tv_wx_integral.setText(point);// 可用积分

                        ll_uername.setVisibility(View.GONE);
                        tv_bind_ok.setVisibility(View.GONE);
                        ll_password.setVisibility(View.GONE);
                        freeTvNum.setVisibility(View.INVISIBLE);
                        if (freeTagOne == 1) {
                            freeTvNum.setVisibility(View.VISIBLE);
                            freeTvNum.setText("免费次数：" + freeTimes + "次");
                        }
                        DLog.i("wenxian", "isWF_XW" + isWF_XW + "freeTagOne=" + freeTagOne);
                        if ((freeTagOne == 1 || freeTagTwo == 1) && !isWF_XW) {
                            free_selrl.setVisibility(View.VISIBLE);
                            free_selrl_div.setVisibility(View.VISIBLE);
                            if (freeTagOne == 1) {
                                freeTvNum.setVisibility(View.VISIBLE);
                                if (freeTimes > 0) {
                                    DLog.i("wenxian", "bl_free_selectDBID" + DBID);
                                    bl_free_select = true;

                                    bl_wanfang_select = false;
                                    bl_xywy_select = false;

                                    iv_wanf_select.setSelected(bl_wanfang_select);
                                    iv_xywy_select.setSelected(bl_xywy_select);
                                    freeIvSelect.setSelected(bl_free_select);
                                    freeTvNum.setText("免费次数：" + freeTimes + "次");
                                } else {
                                    freeTvNum.setText("免费次数：" + 0 + "次");
                                    if (Integer.parseInt(point) > Integer.parseInt(koufen)) {//&& Integer.parseInt(point) >= 2000
                                        DLog.i(TAG, "XYWY选中");
                                        bl_wanfang_select = false;
                                        bl_xywy_select = true;
                                        bl_free_select = false;
                                        iv_wanf_select.setSelected(bl_wanfang_select);
                                        iv_xywy_select.setSelected(bl_xywy_select);

                                    } else {
                                        DLog.i(TAG, "XYWY不选中");
                                        bl_xywy_select = false;
                                        bl_free_select = false;
                                        iv_xywy_select.setSelected(bl_xywy_select);
                                    }
                                }
                            } else {
                                freeTvNum.setVisibility(View.INVISIBLE);
                            }
                            if ("已绑定".equals(msg)) {// 已绑定
                                iv_wanf_select.setVisibility(View.VISIBLE);
                                tv_balance.setText("");
                                tv_donw_wangfang.setVisibility(View.VISIBLE);
                                tv_wanfang_vip.setText("");
                                tv_bind.setVisibility(View.GONE);
                                tv_wanfang_vip.setTextColor(getResources()
                                        .getColor(R.color.my_textcolor));
                                // tv_bind.setText("已绑定");
                                // tv_bind.setEnabled(false);

                            } else {

                                tv_balance.setVisibility(View.VISIBLE);
                                tv_wanfang_vip.setText("若您是万方会员,也可绑定帐号");
                                tv_bind.setVisibility(View.VISIBLE);
                                tv_balance.setText("使用万方余额下载");
                            }
                            if (0 == typePoint) {
                                tv_wx_dowm_pay_jifen.setText("5分钟内再次下载本篇文献，不扣积分");// 扣积分
                            } else {
                                tv_wx_dowm_pay_jifen.setText("本次下载需扣除" + koufen
                                        + "积分");// 扣积分
                            }
                        } else {
                            if (Integer.parseInt(point) > Integer.parseInt(koufen)) {//&& Integer.parseInt(point) >= 2000
                                DLog.i(TAG, "XYWY选中");
                                bl_wanfang_select = false;
                                bl_xywy_select = true;
                                bl_free_select = false;
                                iv_wanf_select.setSelected(bl_wanfang_select);
                                iv_xywy_select.setSelected(bl_xywy_select);

                            } else {
                                DLog.i(TAG, "XYWY不选中");
                                bl_xywy_select = false;
                                bl_free_select = false;
                                iv_xywy_select.setSelected(bl_xywy_select);
                                // if ("0".equals(typePoint)) {// 不扣积分
                                // bl_wanfang_select = false;
                                // bl_xywy_select = true;
                                // iv_wanf_select.setSelected(bl_wanfang_select);
                                // iv_xywy_select.setSelected(bl_xywy_select);
                                // }
                            }
                            if ("已绑定".equals(msg)) {// 已绑定
                                iv_wanf_select.setVisibility(View.VISIBLE);
                                tv_balance.setText("");
                                tv_donw_wangfang.setVisibility(View.VISIBLE);
                                tv_wanfang_vip.setText("");
                                tv_bind.setVisibility(View.GONE);
                                tv_wanfang_vip.setTextColor(getResources()
                                        .getColor(R.color.my_textcolor));
                                // tv_bind.setText("已绑定");
                                // tv_bind.setEnabled(false);

                            } else {

                                tv_balance.setVisibility(View.VISIBLE);
                                tv_wanfang_vip.setText("若您是万方会员,也可绑定帐号");
                                tv_bind.setVisibility(View.VISIBLE);
                                tv_balance.setText("使用万方余额下载");
                            }

                            DLog.i(TAG, "typePoint" + typePoint);
                            if (0 == typePoint) {// 不扣积分
                                bl_wanfang_select = false;
                                bl_free_select = false;
                                bl_xywy_select = false;
                                iv_wanf_select.setSelected(bl_wanfang_select);
                                iv_xywy_select.setSelected(bl_xywy_select);
                                tv_wx_dowm_pay_jifen.setText("5分钟内再次下载本篇文献，不扣积分");// 扣积分
                                // iv_xywy_select.setVisibility(View.GONE);
                                // iv_wanf_select.setVisibility(View.GONE);
                            } else {
                                tv_wx_dowm_pay_jifen.setText("本次下载需扣除" + koufen
                                        + "积分");// 扣积分
                            }

                        }


                    }

                } catch (Exception e) {
                }
                DLog.i(TAG, t.toString());
            }
        });
    }

    private void initlisener() {
        back.setOnClickListener(this);
        tv_bind.setOnClickListener(this);
        tv_bind_ok.setOnClickListener(this);
        freeIvSelect.setOnClickListener(this);
        iv_xywy_select.setOnClickListener(this);
        iv_wanf_select.setOnClickListener(this);
        tv_get_more_jifen.setOnClickListener(this);
        shareDocLayout.setOnClickListener(this);

//        rela_main.getViewTreeObserver().addOnGlobalLayoutListener(
//                new OnGlobalLayoutListener() {
//
//                    @Override
//                    public void onGlobalLayout() {
//
//                        int heightDiff = rela_main.getRootView().getHeight()
//                                - rela_main.getHeight();
//                        if (heightDiff > 100) {
//
//                            // 显示
//                            re_bo.setVisibility(View.GONE);
//                        } else {
//                            new Handler().postDelayed(new Runnable() {
//
//                                @Override
//                                public void run() {
//                                    // TODO Auto-generated method stub
//
//                                    re_bo.setVisibility(View.VISIBLE);
//                                }
//                            }, 400);
//                            // 关闭
//
//                        }
//                    }
//                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (isOpen) {
                    setResult(201518);
                }
                context.finish();
                break;
            case R.id.share_doc_layout:

                new ShareUtil.Builder()
                        .setTitle("医学文献")
                        .setText( "医脉送豪礼，万方文献免费领")
                        .setTargetUrl(shareUrl)
                        .setImageUrl(ShareUtil.DEFAULT_SHARE_IMG_ULR)
                        .build(PhysicLiteraturePayActivity.this).innerShare();


                break;
            case R.id.free_iv_select:

                if (freeTimes > 0) {
                    if (isWF_XW) {
                        Toast.makeText(this, "对不起，此文献不再活动范围内，请使用积分或万方账户下载", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    iv_wanf_select.setSelected(false);
                    bl_xywy_select = false;
                    bl_wanfang_select = false;
                    bl_free_select = true;
                    freeIvSelect.setSelected(bl_free_select);
                } else {
                    Toast.makeText(this, "您未获得免费下载机会，请点击上方分享或选择其他兑换方式下载", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_xywy_select:// 选者xywy积分

                if (!TextUtils.isEmpty(point) && !TextUtils.isEmpty(koufen)) {

                    if (typePoint == 1) {// 扣积分

                        if (Integer.parseInt(point) < Integer.parseInt(koufen)//|| Integer.parseInt(point) < 2000
                                ) {// ||
                            // Integer.parseInt(point)
                            // < 20
                            ToastUtils.shortToast( "您的积分不足，请点击下方链接进行兑换");
                            return;
                        }
                    }
                    iv_wanf_select.setSelected(false);

                    bl_xywy_select = true;
                    bl_wanfang_select = false;
                    bl_free_select = false;
                    iv_xywy_select.setSelected(bl_xywy_select);

                }

                break;

            case R.id.iv_wanf_select:// 万方
                freeIvSelect.setSelected(false);
                iv_xywy_select.setSelected(false);

                bl_xywy_select = false;
                bl_wanfang_select = true;
                bl_free_select = false;

                iv_wanf_select.setSelected(bl_wanfang_select);

                break;

            case R.id.tv_bind:// 绑定
                if ("已绑定".equals(tv_bind.getText())) {
                    return;
                }
                tv_bind.setVisibility(View.GONE);
                tv_balance.setVisibility(View.GONE);
                ll_uername.setVisibility(View.VISIBLE);
                tv_bind_ok.setVisibility(View.VISIBLE);
                ll_password.setVisibility(View.VISIBLE);

                break;

            case R.id.tv_bind_ok: // 确定绑定

                if (!NetworkUtil.isNetWorkConnected()) {
                    ToastUtils.shortToast( "网络链接异常");
                    return;
                }

                if (TextUtils.isEmpty(et_wx_username.getText().toString().trim())) {
                    ToastUtils.shortToast( "请输入用户名！");
                    return;
                }
                if (TextUtils.isEmpty(et_wx_passwored.getText().toString().trim())) {
                    ToastUtils.shortToast( "请输入密码！");
                    return;
                }

                LoginWanF();

                break;

            case R.id.tv_get_more_jifen:
                StatisticalTools.eventCount(context, "lianjie");
                if (YMApplication.isDoctor()) {
                    if (YMApplication.isDoctorApprove()) {
                        if (YMApplication.getLoginInfo().getData() != null) {

                            if ("-1".equals(YMApplication.getLoginInfo().getData()
                                    .getXiaozhan().getDati())) {
                                ToastUtils.shortToast( "您还未认证，还没有积分充值功能权限");

                            } else {
                                Intent intent = new Intent(this,
                                        IntegralRechargeactivity.class);
                                intent.putExtra("type", "0");
                                startActivity(intent);
                            }

                        }

                    } else {
                        ToastUtils.shortToast( "您还未认证，还没有积分充值功能权限");
                    }
                } else {
                    ToastUtils.shortToast( "您是医学生身份，暂时不能使用积分充值功能");
                }

                break;
        }

    }

    /**
     * 确定下载
     */
    private void getDownLoad(int type) {
        if (pro != null && !pro.isShowing()) {
            pro.show();
        }
        if (!bl_wanfang_select && !bl_xywy_select && !bl_free_select) {
            Toast.makeText(context, "请先选择下载账户类别", Toast.LENGTH_SHORT).show();
            return;
        }

        String sign = MD5Util.MD5(mArticleID + Constants.MD5_KEY);

        FinalHttp fh = new FinalHttp();
        AjaxParams params = new AjaxParams();

        //默认是活动 有活动走活动
        if (bl_wanfang_select) {    //万方
            params.put("a", "literature");
            params.put("m", "zpDown");
            params.put("id", mArticleID);
            params.put("uid", uid);
            params.put("DBID", DBID);
            params.put("bind", mArticleID);
            params.put("sign", sign);
        } else if (bl_free_select) {
            params.put("a", "literature");
            params.put("m", "zpDown");
            params.put("id", mArticleID);
            params.put("uid", uid);
            params.put("DBID", DBID);
            params.put("type", "times");
            params.put("bind", mArticleID);
            params.put("sign", sign);
        } else if (bl_xywy_select) {                //默认寻医问药
            params.put("a", "literature");
            params.put("m", "zpDown");
            params.put("id", mArticleID);
            params.put("uid", uid);
            params.put("DBID", DBID);
            if (typePoint == 1) {
                params.put("point", point);
                params.put("free", "1");
                params.put("type", "xywy");
            }
            params.put("bind", mArticleID);
            params.put("sign", sign);
        } else {
            //默认有活动
            if ((freeTagOne == 1 || freeTagTwo == 1) && !isWF_XW) {
                params.put("a", "literature");
                params.put("m", "zpDown");
                params.put("id", mArticleID);
                params.put("uid", uid);
                params.put("DBID", DBID);
                params.put("type", "times");
                params.put("bind", mArticleID);
                params.put("sign", sign);
            } else {
                params.put("a", "literature");
                params.put("m", "zpDown");
                params.put("id", mArticleID);
                params.put("uid", uid);
                params.put("DBID", DBID);
                if (typePoint == 1) {
                    params.put("point", point);
                    params.put("free", "1");
                    params.put("type", "xywy");
                }
                params.put("bind", mArticleID);
                params.put("sign", sign);
            }

        }

        DLog.i(TAG, "getDownLoad" + CommonUrl.wenxian + params);

        fh.post(CommonUrl.wenxian, params, new AjaxCallBack() {
            private String xurl;
            private String wurl;

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (pro != null && pro.isShowing()) {
                    pro.closeProgersssDialog();

                }
                downing = true;
                tv_start_down.setText("确认下载");
                tv_start_down.setOnClickListener(new MyOnclick(tv_start_down, 1));
            }

            @Override
            public void onStart() {
                super.onStart();

            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                // bl_wanfang_select = true 万方下载
                // bl_xywy_select = true xywy下载

                try {
                    JSONObject js = new JSONObject(t.toString());
                    String code = js.getString("code");

                    if ("0".equals(code)) {
                        if (bl_xywy_select) {
                            xurl = js.getString("xurl");
                            if (typePoint == 1) {// 扣积分
                                point = (Integer.parseInt(point) - Integer.parseInt(koufen)) + "";
                                tv_wx_integral.setText(point);// 可用积分
                            }
                        }
                        if (bl_free_select) {
                            xurl = js.getString("xurl");
                            freeTimes--;
                            freeTvNum.setText("免费次数：" + freeTimes + "次");
                        }
                        if (bl_wanfang_select) {
                            wurl = js.getString("wurl");
                        }
                        // TODO 开始下载
                        if (bl_xywy_select || bl_free_select) {
                            wurl = xurl;
                        }
                        checkIsDown(wurl);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 判断万方账号能否下载
     *
     * @param url
     */
    private void checkIsDown(final String url) {
        FinalHttp fh = new FinalHttp();
        fh.post(url, null, new AjaxCallBack() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (pro != null && pro.isShowing()) {
                    pro.closeProgersssDialog();

                }
                downing = true;
                tv_start_down.setText("确认下载");
                tv_start_down.setOnClickListener(new MyOnclick(tv_start_down, 1));
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                if (pro != null && pro.isShowing()) {
                    pro.closeProgersssDialog();

                }
                if (bl_xywy_select) {
                    tv_wx_dowm_pay_jifen.setText("5分钟内再次下载本篇文献，不扣积分");
                }
                downing = true;
                if ("交易失败".equals(t.toString())) {
//                    tv_wx_dowm_pay_jifen.setText("本次下载需扣除2000积分");
                    ToastUtils.shortToast( "对不起，您的万方账号余额不足");
                    return;
                }
                Intent i = new Intent(context, DownloadService.class);
                DownFileItemInfo d = new DownFileItemInfo();

                d.setDownloadUrl(url);
                d.setFileSize("");
                d.setMovieName(fileName);
                d.setUserid(userid);
                d.setCommed("1");
                d.setDownloadState(DOWNLOAD_STATE_WATTING);
                i.putExtra(SERVICE_TYPE_NAME, START_DOWNLOAD_MOVIE);
                // i.putExtra(DOWNLOAD_TAG_BY_INTENT, d);
                i.putExtra(DOWNLOAD_TAG_BY_INTENT, d);
                context.startService(i);

            }
        });
    }

    /**
     * 登录万方
     */
    private void LoginWanF() {

        String wname = et_wx_username.getText().toString().trim();
        String wKey = et_wx_passwored.getText().toString();
        String sign = MD5Util.MD5(uid + Constants.MD5_KEY);
        String strRSA = RSATools.strRSA(wKey);
        FinalHttp fh = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("uid", uid);
        ajaxParams.put("bind", uid);
        ajaxParams.put("wkey", strRSA);
        ajaxParams.put("sign", sign);
        ajaxParams.put("wname", wname);
        ajaxParams.put("m", "zpBound");
        ajaxParams.put("a", "literature");

        fh.post(CommonUrl.wenxian, ajaxParams, new AjaxCallBack() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (pro.isShowing()) {
                    pro.closeProgersssDialog();
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                if (!pro.isShowing()) {
                    pro.showProgersssDialog();
                }
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                if (pro.isShowing()) {
                    pro.closeProgersssDialog();
                }
                DLog.i(TAG, t.toString());
                try {
                    JSONObject js = new JSONObject(t.toString());
                    String code = js.getString("code");
                    if ("0".equals(code)) {// 成功
                        String messges = js.getString("massags");

                        if ("绑定成功".equals(messges))
                        // if(true)
                        {
                            ToastUtils.shortToast( messges);
                            iv_wanf_select.setVisibility(View.VISIBLE);
                            tv_balance.setText("");
                            tv_wanfang_vip.setText("");
                            tv_donw_wangfang.setText("万方账户");
                            tv_donw_wangfang.setVisibility(View.VISIBLE);
                            tv_wanfang_vip.setTextColor(getResources()
                                    .getColor(R.color.my_textcolor));
                            tv_bind.setVisibility(View.GONE);
                            tv_wanfang_vip.setVisibility(View.VISIBLE);
                            tv_balance.setVisibility(View.VISIBLE);
                            ll_uername.setVisibility(View.GONE);
                            tv_bind_ok.setVisibility(View.GONE);
                            ll_password.setVisibility(View.GONE);

                            tv_bind.setText("已绑定");
                            tv_bind.setEnabled(false);
                            tv_bind.setVisibility(View.GONE);

                            bl_wanfang_select = true;
                            bl_xywy_select = false;
                            bl_free_select = false;
                            freeIvSelect.setSelected(bl_free_select);
                            iv_xywy_select.setSelected(bl_xywy_select);
                            iv_wanf_select.setSelected(bl_wanfang_select);
                        } else {

                            showBindFaile();

                        }

                    }

                } catch (Exception e) {
                }
            }

        });

    }

    /**
     * 绑定失败
     */
    private void showBindFaile() {

        ToastUtils.shortToast( "对不起，您的身份验证失败");

        // bl_wanfang_select = false;
        // bl_xywy_select = true;
        // iv_xywy_select.setSelected(bl_xywy_select);

        tv_balance.setText("使用万方余额下载");
        tv_wanfang_vip.setText("若您是万方会员,也可绑定帐号");
        tv_bind.setVisibility(View.VISIBLE);
        tv_balance.setVisibility(View.VISIBLE);
        tv_wanfang_vip.setVisibility(View.VISIBLE);

        tv_donw_wangfang.setVisibility(View.GONE);
        ll_uername.setVisibility(View.GONE);
        tv_bind_ok.setVisibility(View.GONE);
        ll_password.setVisibility(View.GONE);

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub

        if (pro != null && pro.isShowing()) {
            pro.closeProgersssDialog();
        }
        isGo = true;
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
        handler.removeCallbacksAndMessages(null);
        handler2.removeCallbacksAndMessages(null);
        fb = null;
        super.onDestroy();


    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        StatisticalTools.onResume(this);

        initView();

    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        StatisticalTools.onPause(this);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

//	private long isCanTouch;

    //1.点击下载请求接口特别慢 2.

    class MyOnclick implements OnClickListener {

        // int position;
        TextView tv_text;
        int type;
        String url;

        public MyOnclick(TextView tv_text, int type, String url) {
            // this.position = position;
            this.tv_text = tv_text;
            this.type = type;
            this.url = url;
        }

        public MyOnclick(TextView tv_text, int type) {
            // this.position = position;
            this.tv_text = tv_text;
            this.type = type;
        }

        @Override
        public void onClick(View arg0) {

            // MobclickAgent.onEvent(context, "download");
            // MobileAgent.onEvent(getActivity(), "download");

            DLog.i(TAG, "type.." + type);
            if (YMUserService.isGuest()) {
                DialogUtil.LoginDialog(new YMOtherUtils(context).context);
            } else {
                // 是否考虑当万方积分不足情况???
                // tv_wx_dowm_pay_jifen.setText("5分钟内再次下载本篇文献，不扣积分");
                if (type == 1) {
                    items = fb.findAllByWhere(clazz, "movieName='" + fileName
                            + "'  and userid='" + userid + "'and commed = '"
                            + "1" + "'");
                    if (items != null & items.size() > 0) {
                        // T.shortToast( "别戳了！再戳就破啦...");
                    } else {
                        handler.postDelayed(runnable, 0);
                        if (freeTimes > 0) {
                            if (downing) {
                                downing = false;
                                getDownLoad(type);
                            } else {
                                ToastUtils.shortToast( "正在处理下载中...");
                            }
                        } else {
                            if (freeTagOne == 1 || freeTagTwo == 1) {
                                if (!bl_wanfang_select && !bl_xywy_select && !bl_free_select) {
                                    Toast.makeText(context, "请先选择下载账户类别", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } else {
                                if (!bl_wanfang_select && !bl_xywy_select) {

                                    ToastUtils.shortToast( "您的积分不足，请点击链接兑换积分");// 请选择一种支付方式
                                    return;
                                }
                            }

                            DLog.i(TAG, "downing=" + downing);
                            if (downing) {
                                downing = false;
                                getDownLoad(type);
                            } else {
                                ToastUtils.shortToast( "正在处理下载中...");
                            }
                        }
                    }
                } else if (type == 2) {
                    try {
                        Uri uri = Uri.parse(url);
                        Intent intent = new Intent(context, MuPDFActivity.class);
                        intent.setAction(Intent.ACTION_VIEW);
                        // intent.putExtra("path",
                        // items.get(arg2).getFilePath().toString().trim());
                        intent.setData(uri);
                        startActivity(intent);
                    } catch (Exception e) {
                        DLog.i(TAG, "PDF打开失败原因 " + e);
                        ToastUtils.shortToast( "pdf打开失败");
                    }

                }
            }

        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {

            int[] leftTop = {0, 0};

            View view = re_user;
            // 获取输入框当前的location位置
            view.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + view.getHeight();
            int right = left + view.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    public boolean getFile(String title, String url) {
        try {
            File f = new File(url);
            if (!f.exists()) {
                DLog.i(TAG, "文件地址没有");
                fb.deleteByWhere(clazz, "movieName='" + title + "'");
                return false;
            }

        } catch (Exception e) {
            // TODO: handle exception
            DLog.i(TAG, "数据操作错误日志" + e);
            return false;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isOpen) {
                setResult(201518);
            }
            context.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}