package com.xywy.askforexpert.module.main.service.linchuang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.umeng.socialize.UMShareAPI;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.model.BookBaseInfo;
import com.xywy.askforexpert.module.main.service.linchuang.CommentInfoActivity;
import com.xywy.askforexpert.module.message.share.ShareUtil;
import com.xywy.askforexpert.widget.view.SelectBasePopupWindow;
import com.xywy.uilibrary.titlebar.ItemClickListener;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.HashMap;
import java.util.Map;

/**
 * 临床指南 主界面 stone
 *
 * @author 王鹏
 * @2015-5-11上午8:47:03
 */
public class GuideMainFragment extends YMBaseActivity {


    private BookBaseInfo info;
    private String ispraise;

    private TextView tv_collect;
    private TextView tv_praise;
    private View lay1;
    private View lay2;
    private View lay3;
    private View lay4;
    private View popRoot;

    private SelectBasePopupWindow mPopupWindow;

    //    private SlidingMenu mSlidingMenu;
    private FragmentTransaction mTransaction;
    //    private Menu_right_Guide rightFragment;
    private GuideWebViewFragment centerFragment;
    private String url;
    private String id;
    private String title;
    // private String imageUrl;
    private String channel;
    private String iscollection;
    private String filesize;
    private String fileurl;


    private void initview() {
//        ((TextView) findViewById(R.id.con_tv_title)).setText("临床指南详情");

//        mSlidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
//        mSlidingMenu.setCanSliding(false);
//        mSlidingMenu.setRightView(getLayoutInflater().inflate(
//                R.layout.right_frame, null));
//        mSlidingMenu.setCenterView(getLayoutInflater().inflate(
//                R.layout.center_frame, null));
        mTransaction = this.getSupportFragmentManager().beginTransaction();
//        rightFragment = new Menu_right_Guide();
        centerFragment = new GuideWebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("title", title);
        bundle.putString("filesize", filesize);
        bundle.putString("fileurl", fileurl);
        centerFragment.setArguments(bundle);
//        Bundle bundle2 = new Bundle();
//        bundle2.putString("ids", id);
//        bundle2.putString("url", url);
//        bundle2.putString("title", title);
//        bundle2.putString("channel", channel);
//        bundle2.putString("iscollection", iscollection);

        // bundle2.putString("imageurl", imageUrl);
//        rightFragment.setArguments(bundle2);
        mTransaction.replace(R.id.layout_container, centerFragment);
//        mTransaction.replace(R.id.right_frame, rightFragment);
        mTransaction.commit();
    }

//    public void onClickBack(View view) {
//        switch (view.getId()) {
//            case R.id.btn1:
//                if (centerFragment.webview.canGoBack()) {
//                    centerFragment.webview.goBack();
//                } else {
//                    finish();
//                }
//
//                break;
//            case R.id.btn2:
//                showPop();
////                mSlidingMenu.showRightView();
//                break;
//            default:
//                break;
//        }
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (centerFragment.webview.canGoBack()) {
                centerFragment.webview.goBack();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        /** 使用SSO授权必须添加如下代码 */
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void initView() {

        // TODO: 2018/5/14 stone

//        hideCommonBaseTitle();
//        ((TextView) findViewById(R.id.tv_title)).setText("临床指南详情");
//        ((ImageView) findViewById(R.id.btn2)).setImageResource(R.drawable.service_topque_right_btn);
//        findViewById(R.id.btn2).setVisibility(View.VISIBLE);

        titleBarBuilder.addItem("", R.drawable.service_topque_right_btn, new ItemClickListener() {
            @Override
            public void onClick() {
                showPop();
            }
        }).setBackIconClickEvent(new ItemClickListener() {
            @Override
            public void onClick() {
                if (centerFragment.webview.canGoBack()) {
//                    centerFragment.webview.goBack();
                } else {
                    finish();
                }
//
            }
        }).setTitleText("临床指南详情").build();


        CommonUtils.initSystemBar(this);
        url = getIntent().getStringExtra("url");
        id = getIntent().getStringExtra("ids");
        title = getIntent().getStringExtra("title");
        channel = getIntent().getStringExtra("channel");
        iscollection = getIntent().getStringExtra("iscollection");
        filesize = getIntent().getStringExtra("filesize");
        fileurl = getIntent().getStringExtra("fileurl");
        // imageUrl = getIntent().getStringExtra("imageurl");
        initview();
    }

    @Override
    protected void initData() {
        getData();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.consult_main_frag_new;
    }

    private void showPop() {
        if (mPopupWindow == null) {
            mPopupWindow = new SelectBasePopupWindow(true, this);
            popRoot = View.inflate(getBaseContext(), R.layout.pop_layout_guide_detail, null);
            lay1 = popRoot.findViewById(R.id.lay1);
            lay2 = popRoot.findViewById(R.id.lay2);
            lay3 = popRoot.findViewById(R.id.lay3);
            lay4 = popRoot.findViewById(R.id.lay4);
            tv_collect = (TextView) popRoot.findViewById(R.id.tv_collect);
            tv_praise = (TextView) popRoot.findViewById(R.id.tv_praise);
            lay1.setOnClickListener(mPopOnClickListener);
            lay2.setOnClickListener(mPopOnClickListener);
            lay3.setOnClickListener(mPopOnClickListener);
            lay4.setOnClickListener(mPopOnClickListener);

            updateStatus();
        }
        if (!mPopupWindow.isShowing()) {
//            mPopupWindow.init(popRoot).showAtLocation(this.getWindow().getDecorView(), Gravity.TOP | Gravity.RIGHT, (AppUtils.dpToPx(48, getResources()) - 24) / 2 + AppUtils.dpToPx(5, getResources()) - 30 - 27, AppUtils.dpToPx(48, getResources()) + YMApplication.getStatusBarHeight() - 30);
            mPopupWindow.init(popRoot).showAtLocation(this.getWindow().getDecorView(), Gravity.TOP | Gravity.RIGHT, AppUtils.dpToPx(15, getResources()), AppUtils.dpToPx(48 + 5, getResources()) + YMApplication.getStatusBarHeight() - 30);
        }
    }

    /**
     * pop监听器 种族
     */
    private View.OnClickListener mPopOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mPopupWindow != null && mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
            AjaxParams params;
            switch (view.getId()) {
                case R.id.lay1:
                    Intent intent = new Intent(GuideMainFragment.this,
                            CommentInfoActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("type", "guide");

                    startActivity(intent);
                    break;
                case R.id.lay2:
                    if (YMUserService.isGuest()) {
                        DialogUtil.LoginDialog(GuideMainFragment.this);
                    } else {
                        // if (!"已点赞".equals(tv_praise.getText())) {
                        String userid = YMApplication.getLoginInfo().getData().getPid();
                        String sign = MD5Util.MD5(userid + Constants.MD5_KEY);
                        params = new AjaxParams();
                        params.put("a", "praise");
                        params.put("id", id);
                        params.put("userid", userid);
                        params.put("type", "1");
                        params.put("c", "comment");
                        params.put("sign", sign);
                        setData(params, "praise");
                        // }

                    }
                    break;
                case R.id.lay3:
                    StatisticalTools.eventCount(GuideMainFragment.this, "zncollectn");
                    if (YMUserService.isGuest()) {
                        DialogUtil.LoginDialog(GuideMainFragment.this);
                    } else {
                        String sign1 = MD5Util.MD5(id + Constants.MD5_KEY);

                        params = new AjaxParams();
                        params.put("a", "collection");
                        params.put("collecid", id);
                        params.put("channel", 3 + "");
                        params.put("userid", YMApplication.getLoginInfo().getData().getPid());
                        params.put("c", "collection");
                        params.put("sign", sign1);
                        setData(params, "collect");
                    }
                    break;
                case R.id.lay4:
                    StatisticalTools.eventCount(GuideMainFragment.this, "znshare");

                    new ShareUtil.Builder()
                            .setTitle(title)
                            .setText(" ")//这里不能为空字符串，否则会导致分享调不起微博客户端,采用一个空格符来替换空字符串
                            .setTargetUrl(url)
                            .setImageUrl(ShareUtil.DEFAULT_SHARE_IMG_ULR)
                            .build(GuideMainFragment.this).outerShare();
                    break;
                default:
                    break;
            }
        }

    };

    /**
     * 获取收藏 点赞状态
     */
    public void getData() {
        String userid;
        String sign = MD5Util.MD5(id + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("c", "status");
        params.put("a", "status");
        if (YMUserService.isGuest()) {
            userid = "0";
        } else {
            userid = YMApplication.getLoginInfo().getData().getPid();
        }
        params.put("userid", userid);
        params.put("id", id);
        params.put("channel", channel);
        params.put("sign", sign);
        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.Codex_Url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                Gson gson = new Gson();
                info = gson.fromJson(t.toString(), BookBaseInfo.class);
                if ("0".equals(info.getCode())) {
                    iscollection = info.getList().getIscollection();
                    ispraise = info.getList().getIspraise();
                    updateStatus();
                }
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    public void setData(AjaxParams params, final String type) {
        FinalHttp ft = new FinalHttp();
        ft.post(CommonUrl.Codex_Url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                // TODO Auto-generated method stub
                map = ResolveJson.R_Action(t.toString());
                if ("0".equals(map.get("code")) || "2".equals(map.get("code"))
                        || "1".equals(map.get("code"))) {
                    if ("collect".equals(type)) {
                        if ("0".equals(iscollection)) {
                            iscollection = 1 + "";
                            updateStatus();
                        } else {
                            iscollection = 0 + "";
                            updateStatus();
                        }
                    } else if ("praise".equals(type)) {
                        ispraise = 1 + "";
                        updateStatus();
                    }
                }

                handler.sendEmptyMessage(200);
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    private Map<String, String> map = new HashMap<String, String>();
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 200:
                    ToastUtils.shortToast(map.get("msg"));
                    break;

                default:
                    break;
            }

        }
    };

    public void updateStatus() {
        if (mPopupWindow == null) {
            return;
        }

        if ("1".equals(iscollection)) {
            tv_collect.setText("已收藏");
        } else {
            tv_collect.setText("收藏");
        }
        if ("1".equals(ispraise)) {
            tv_praise.setText("已点赞");
        } else {
            tv_praise.setText("点赞");
        }
    }
}
