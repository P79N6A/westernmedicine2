package com.xywy.askforexpert.module.my.pause;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.UMShareAPI;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.model.inviteMoney.InviteDocList;
import com.xywy.askforexpert.model.inviteMoney.InviteRootData;
import com.xywy.askforexpert.module.main.service.person.PersonDetailActivity;
import com.xywy.askforexpert.module.message.share.ShareUtil;
import com.xywy.askforexpert.module.my.adapter.MyInviteFriendAdapter;
import com.xywy.askforexpert.widget.FloatButtonScrollView;
import com.xywy.askforexpert.widget.view.MyGridView;
import com.xywy.base.view.ProgressDialog;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 邀请发钱啦 页面
 *
 * @author 方琪
 * @version 1.0
 */
@Deprecated
public class InviteForMoney extends AppCompatActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, AdapterView.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String SHARE_IMG = ShareUtil.DEFAULT_SHARE_IMG_ULR;

    private static final int COLUMN_SIZE = 6;

    private static final String A = "invitation";
    private static final String M = "user";
    private static final String PARAM_SIGN = Constants.MD5_KEY;

    /**
     * 我的邀请链接
     */
    private TextView myShareLink;

    /**
     * 分享按钮
     */
    private ImageButton shareMyLink;

    /**
     * 通过邀请获得的钱数
     */
    private TextView moneyGained;

    /**
     * 转跳到 我的钱包 页面
     */
    private TextView gotoMyPurse;

    /**
     * 响应邀请的好友数
     */
    private TextView numOfFriendsInvited;

    /**
     * 展开/收起好友列表
     */
    private CheckBox moreInvitedFriends;

    /**
     * 响应邀请的好友列表
     */
    private MyGridView friendList;

    /**
     * 悬浮分享按钮
     */
    private ImageButton floatShare;

    /**
     * 需要填充到布局中的邀请的好友的头像
     */
    private List<Map<String, Object>> mDatas = new ArrayList<>();

    /**
     * 从网络获取的好友的头像
     */
    private List<Map<String, Object>> avatars = new ArrayList<>();

    /**
     * 好友列表适配器
     */
    private MyInviteFriendAdapter adapter;

    private FloatButtonScrollView content;
    private SwipeRefreshLayout refreshLayout;

    private boolean isShouldShowLoading = true;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                // 更新页面数据
                case 999:
                    InviteRootData rootData = (InviteRootData) msg.obj;
                    myShareLink.setText(rootData.getUrl());
                    moneyGained.setText(String.valueOf(rootData.getReward()));
                    numOfFriendsInvited.setText(String.valueOf(rootData.getDocNum()));
                    break;

                default:
                    break;
            }
        }
    };
    private View dividerLine;
    private ImageButton floatShare2;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);

        setContentView(R.layout.activity_invite_for_money);

        initView();
        CommonUtils.setToolbar(this, toolbar);
        setFloatShareButton();
        getParams();
        CommonUtils.setRefresh(refreshLayout);
        registerListeners();
        setFloatButtonAnim();

        adapter = new MyInviteFriendAdapter(this, mDatas, R.layout.invite_friend_avatar);
        friendList.setAdapter(adapter);
    }

    /**
     * 设置悬浮分享按钮进入/退出动画
     */
    private void setFloatButtonAnim() {
        Animation enterAnim = AnimationUtils.loadAnimation(this, R.anim.float_share_enter_anim);
        Animation exitAnim = AnimationUtils.loadAnimation(this, R.anim.float_share_exit_anim);

        content.setEnterAnim(enterAnim);
        content.setExitAnim(exitAnim);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void setFloatShareButton() {
        content.setFloatView(floatShare);
        content.setPrimaryView(shareMyLink);
        content.setFloatShare2(floatShare2);
    }

    /**
     * 获取医生id
     */
    private void getParams() {
        Intent intent = getIntent();
        String docId = intent.getStringExtra("docId");
        initUrl(docId);
    }

    /**
     * 拼接接口url
     *
     * @param docId 医生id
     */
    private void initUrl(String docId) {
        // 计算接口bind值
        String bind = A + M + docId;
        // 计算接口sign值
        String sign = MD5Util.MD5(bind + PARAM_SIGN);
        // 拼接接口url
        String url = CommonUrl.HOST_URL + "a=" + A + "&m=" + M + "&id=" + docId + "&bind=" + bind +
                "&sign=" + sign;
        requestData(url);
    }

    /**
     * 请求接口数据
     *
     * @param url 接口url
     */
    private void requestData(String url) {
        final ProgressDialog dialog = new ProgressDialog(this, getString(R.string.loading_now));
        dialog.setCanceledOnTouchOutside(false);
        if (isShouldShowLoading) {
            if (content.getVisibility() == View.VISIBLE) {
                content.setVisibility(View.GONE);
            }
            dialog.showProgersssDialog();
            isShouldShowLoading = false;
        }

        final FinalHttp request = new FinalHttp();
        request.get(url, new AjaxCallBack() {
            @Override
            public void onSuccess(String jsonString) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (content.getVisibility() == View.GONE) {
                    content.setVisibility(View.VISIBLE);
                }
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                InviteRootData rootData = new InviteRootData();
                rootData.parseJson(jsonObject);

                if (jsonObject == null || rootData.getCode() != 10000) {
                    Toast.makeText(InviteForMoney.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                    InviteForMoney.this.finish();
                } else {
                    // 更新页面数据
                    Message msg = mHandler.obtainMessage();
                    msg.what = 999;
                    msg.obj = rootData;
                    mHandler.sendMessage(msg);

                    if (null != avatars && avatars.size() > 0) {
                        avatars.clear();
                    }

                    if (rootData.getDocNum() == 0) {
                        // 无好友响应时隐藏分割线和GridView
                        if (dividerLine.getVisibility() == View.VISIBLE) {
                            dividerLine.setVisibility(View.GONE);
                        }
                        if (friendList.getVisibility() == View.VISIBLE) {
                            friendList.setVisibility(View.GONE);
                        }
                    } else {
                        if (dividerLine.getVisibility() == View.GONE) {
                            dividerLine.setVisibility(View.VISIBLE);
                        }
                        if (friendList.getVisibility() == View.GONE) {
                            friendList.setVisibility(View.VISIBLE);
                        }
                        // 获取已邀请的好友列表
                        List<InviteDocList> docLists = rootData.getDocLists();
                        for (InviteDocList docList : docLists) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("avatar", docList.getPhoto());
                            map.put("id", docList.getId());
                            map.put("realname", docList.getRealname());

                            avatars.add(map);
                        }

                        if (mDatas != null && mDatas.size() > 0) {
                            mDatas.clear();
                        }
                        // 默认显示2行好友头像
                        // 邀请的好友列表超过2行时显示下拉展开按钮，否则隐藏
                        if (rootData.getDocNum() > COLUMN_SIZE * 2) {
                            moreInvitedFriends.setVisibility(View.VISIBLE);
                            moreInvitedFriends.setEnabled(true);
                            mDatas.addAll(avatars.subList(0, COLUMN_SIZE * 2));
                        } else {
                            moreInvitedFriends.setVisibility(View.GONE);
                            moreInvitedFriends.setEnabled(false);
                            mDatas.addAll(avatars);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    content.smoothScrollTo(0, 0);
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                if (!NetworkUtil.isNetWorkConnected()) {
                    Toast.makeText(InviteForMoney.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(InviteForMoney.this, R.string.loading_failed, Toast.LENGTH_SHORT).show();
                }
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (content.getVisibility() == View.GONE) {
                    content.setVisibility(View.VISIBLE);
                }
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    /**
     * 注册各类监听
     */
    private void registerListeners() {
        registerForContextMenu(myShareLink);
        shareMyLink.setOnClickListener(this);
        gotoMyPurse.setOnClickListener(this);
        moreInvitedFriends.setOnCheckedChangeListener(this);
        floatShare.setOnClickListener(this);
        friendList.setOnItemClickListener(this);
        refreshLayout.setOnRefreshListener(this);
    }

    /**
     * 找到各个View
     */
    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        content = (FloatButtonScrollView) findViewById(R.id.content_scroll);
        myShareLink = (TextView) findViewById(R.id.my_share_link_text);
        shareMyLink = (ImageButton) findViewById(R.id.share_my_link);
        moneyGained = (TextView) findViewById(R.id.money_gained);
        gotoMyPurse = (TextView) findViewById(R.id.goto_my_purse);
        numOfFriendsInvited = (TextView) findViewById(R.id.num_of_friends_invited);
        moreInvitedFriends = (CheckBox) findViewById(R.id.more_friends);
        friendList = (MyGridView) findViewById(R.id.invited_friends_list);
        floatShare = (ImageButton) findViewById(R.id.float_share);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        dividerLine = findViewById(R.id.divider_line);
        floatShare2 = (ImageButton) findViewById(R.id.float_share_2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 返回上一级页面
            case android.R.id.home:
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.share_link_copy, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 复制我的分享链接
            case R.id.my_share_link_copy:
                copyText(myShareLink.getText().toString());
                break;
        }

        return super.onContextItemSelected(item);
    }

    /**
     * 复制文字
     *
     * @param content 需要复制的内容
     */
    private void copyText(String content) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("text", content.trim());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, R.string.copy_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            // 分享我的链接
            case R.id.share_my_link:
                StatisticalTools.eventCount(this, "ShareMoneyOn");

                new ShareUtil.Builder()
                        .setTitle( getString(R.string.invite_money_share_title))
                        .setText(getString(R.string.invite_money_share_content))
                        .setTargetUrl(myShareLink.getText().toString())
                        .setImageUrl(SHARE_IMG)
                        .build(InviteForMoney.this).outerShare();
                break;

            // 转跳到 我的钱包
            case R.id.goto_my_purse:
                Intent intent = new Intent(this, MyPurseActivity.class);
                startActivity(intent);
                break;

            // 分享我的链接
            case R.id.float_share:
                StatisticalTools.eventCount(this, "ShareMoneyUnder");

                new ShareUtil.Builder()
                        .setTitle( getString(R.string.invite_money_share_title))
                        .setText(getString(R.string.invite_money_share_content))
                        .setTargetUrl(myShareLink.getText().toString())
                        .setImageUrl(SHARE_IMG)
                        .build(InviteForMoney.this).outerShare();
                break;

            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView instanceof CheckBox) {
            if (isChecked) {
                // 展开邀请的好友列表
                mDatas.addAll(avatars.subList(COLUMN_SIZE * 2, avatars.size()));
                adapter.notifyDataSetChanged();
            } else {
                // 收起好友列表
                mDatas.clear();
                mDatas.addAll(avatars.subList(0, COLUMN_SIZE * 2));
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 点击邀请的好友的头像转跳到好友个人主页
        Intent intent = new Intent(this, PersonDetailActivity.class);
        intent.putExtra("uuid", String.valueOf(mDatas.get(position).get("id")));
        intent.putExtra("isDoctor", "");
        startActivity(intent);
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        if (!refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(true);
            getParams();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

}
