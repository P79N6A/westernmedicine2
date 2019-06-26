package com.xywy.askforexpert.module.main.service.linchuang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.xywy.askforexpert.appcommon.utils.logictools.ResolveJson;
import com.xywy.askforexpert.model.BookBaseInfo;
import com.xywy.askforexpert.module.main.service.linchuang.CommentInfoActivity;
import com.xywy.askforexpert.module.message.share.ShareUtil;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.HashMap;
import java.util.Map;

/**
 * 临床指南 右边菜单
 *
 * @author 王鹏
 * @2015-5-11下午6:07:11 修改 添加分享
 */
public class Menu_right_Guide extends Fragment {

    /**
     * 评论 点赞 收藏 转发
     */
    private LinearLayout lin_comment, lin_praise, lin_collect, lin_transpond;
    private String url;
    private String id;
    private String title;
    private String imageUrl;
    private String channel;
    //    private UMSocialService mController;
    private BookBaseInfo info;
    private String iscollection = "0";
    private String ispraise;
    private TextView tv_praise;
    private TextView tv_collect;
    Map<String, String> map = new HashMap<String, String>();
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 200:
                    ToastUtils.shortToast( map.get("msg"));
                    break;

                default:
                    break;
            }

        }
    };

    public void initview() {
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Bundle budle = getArguments();
        id = budle.getString("ids");
        url = budle.getString("url");
        title = budle.getString("title");
        imageUrl = budle.getString("imageurl");
        channel = budle.getString("channel");
//        // 初始化分享平台
//        initSocialSDK(title, " ", url,
//                ShareUtil.DEFAULT_SHARE_IMG_ULR);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_right_consult, container,
                false);
        lin_comment = (LinearLayout) v.findViewById(R.id.lin_comment);
        lin_praise = (LinearLayout) v.findViewById(R.id.lin_praise);
        lin_collect = (LinearLayout) v.findViewById(R.id.lin_collect);
        lin_transpond = (LinearLayout) v.findViewById(R.id.lin_transpond);
        lin_comment.setOnClickListener(new OnclickItem());
        lin_praise.setOnClickListener(new OnclickItem());
        lin_collect.setOnClickListener(new OnclickItem());
        lin_transpond.setOnClickListener(new OnclickItem());

        tv_praise = (TextView) v.findViewById(R.id.tv_praise);
        tv_collect = (TextView) v.findViewById(R.id.tv_collect);
        if (NetworkUtil.isNetWorkConnected()) {
            getData();
        } else {
            ToastUtils.shortToast( "网络连接失败");
        }
        initview();
        return v;
    }

    public class OnclickItem implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            AjaxParams params;
            switch (arg0.getId()) {
                case R.id.lin_comment:
                    // T.shortToast( "评论");
                    Intent intent = new Intent(getActivity(),
                            CommentInfoActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("type", "guide");

                    startActivity(intent);
                    break;
                case R.id.lin_praise:
                    // T.shortToast( "点赞");
                    if (YMUserService.isGuest()) {
                        DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
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
                case R.id.lin_collect:
                    StatisticalTools.eventCount(getActivity(), "zncollectn");
                    if (YMUserService.isGuest()) {
                        DialogUtil.LoginDialog(new YMOtherUtils(getActivity()).context);
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
                case R.id.lin_transpond:
                    StatisticalTools.eventCount(getActivity(), "znshare");

                    new ShareUtil.Builder()
                            .setTitle( title)
                            .setText(" ")//这里不能为空字符串，否则会导致分享调不起微博客户端,采用一个空格符来替换空字符串
                            .setTargetUrl(url)
                            .setImageUrl(ShareUtil.DEFAULT_SHARE_IMG_ULR)
                            .build(getActivity()).outerShare();

                    break;

            }
        }

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
                            initview();
                        } else {
                            iscollection = 0 + "";
                            initview();
                        }
                    } else if ("praise".equals(type)) {
                        ispraise = 1 + "";
                        initview();
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
                    initview();
                }
                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    public void onResume() {
        super.onResume();
        StatisticalTools.fragmentOnResume("Menu_right_Guide");
    }

    public void onPause() {
        super.onPause();
        StatisticalTools.fragmentOnPause("Menu_right_Guide");
    }

}
