package com.xywy.askforexpert.module.main.media;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.UMShareAPI;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.AddressBook;
import com.xywy.askforexpert.model.followList.IsFollowData;
import com.xywy.askforexpert.model.media.MediaListCacheData;
import com.xywy.askforexpert.model.mediaDetail.MediaDetailData;
import com.xywy.askforexpert.model.subscribe.ServiceTitleEntity;
import com.xywy.askforexpert.model.subscribe.SubscribeMediaBean;
import com.xywy.askforexpert.module.main.media.adapter.MediaDetailAdapter;
import com.xywy.askforexpert.module.main.service.media.InfoDetailActivity;
import com.xywy.askforexpert.module.main.subscribe.video.VideoNewsActivity;
import com.xywy.askforexpert.module.message.share.ShareUtil;
import com.xywy.askforexpert.widget.CircleImageView;
import com.xywy.uilibrary.titlebar.ItemClickListener;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 媒体号详情页 stone
 */
public class MediaDetailActivity extends YMBaseActivity
        implements AdapterView.OnItemClickListener, View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String LOG_TAG = MediaDetailActivity.class.getSimpleName();
    private static final int DEFAULT_PAGE = 1;
    public static boolean isFollowed;
//    @Bind(R.id.toolbar_title)
//    TextView toolbarTitle;
    @Bind(R.id.media_detail_list)
    ListView mediaDetailList;
    private MediaDetailAdapter adapter;
    private CircleImageView mediaIcon;
    private TextView mediaName;
    private TextView mediaType;
    private TextView mediaFollow;
    private TextView mediaFunc;
    private boolean isLoading;
    private int page = DEFAULT_PAGE;
    private String mediaId;
    private String uuid;
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;
    private List<MediaDetailData.ArtlistBean> mDatas = new ArrayList<>();
    private View footerView;
    private boolean isBottom;
    private String shareImageUrl;
    private String shareTitle;
    private String shareContent;
    private SwipeRefreshLayout mediaDetailRefresh;
    private String shareWebUrl;
    private SharedPreferences mediaCache;
    private SharedPreferences mediaListCache;
    private String cacheData;
    private String cacheMediaListData;
    private boolean acting;

    public static void startActivity(Context context,String mediaId){
        Intent intent = new Intent(context, MediaDetailActivity.class);
        intent.putExtra("mediaId", mediaId);
        context.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isFollowed) {
            mediaFollow.setText("取消订阅");
            mediaFollow.setTextColor(getResources().getColor(R.color.c_00c8aa));
            mediaFollow.setBackgroundResource(R.drawable.follow_button_checked);
        } else {
            mediaFollow.setText("+订阅");
            mediaFollow.setTextColor(Color.WHITE);
            mediaFollow.setBackgroundResource(R.drawable.follow_button);
        }
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
//        setContentView(R.layout.activity_media_detail);
//        CommonUtils.initSystemBar(this);

//        ButterKnife.bind(this);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        CommonUtils.setToolbar(this, toolbar);

        mediaCache = getSharedPreferences("saveChannel", Context.MODE_PRIVATE);
        mediaListCache = getSharedPreferences("mediaListCache", MODE_PRIVATE);
        cacheData = mediaCache.getString("channelData", "");
        cacheMediaListData = mediaListCache.getString("media_list_cache", "");

        mImageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnFail(R.drawable.icon_photo_def)
                .showImageOnLoading(R.drawable.icon_photo_def)
                .build();

        mediaId = getIntent().getStringExtra("mediaId");

        uuid = YMApplication.getUUid();

        initList();
        if (NetworkUtil.isNetWorkConnected()) {
            mediaDetailRefresh.setRefreshing(true);
            requestData();
        } else {
            Toast.makeText(this, "网络不给力", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_media_detail;
    }

    private void requestData() {
        if (page == DEFAULT_PAGE) {
            CommonUtils.showLoadingIndicator(mediaDetailRefresh, true);
        }

        DLog.d(LOG_TAG, "media_id = " + mediaId);
        String bind = mediaId;
        String sign = MD5Util.MD5(mediaId + Constants.MD5_KEY);
        final AjaxParams params = new AjaxParams();
        params.put("m", "doctor_row");
        params.put("a", "doctor");
        params.put("bind", bind);
        params.put("userid", mediaId);
        params.put("touserid", uuid);
        params.put("sign", sign);
        params.put("type", "3");
        params.put("page", String.valueOf(page));
        DLog.d(LOG_TAG, "media_detail_url = " + CommonUrl.doctor_circo_url + "?" + params.toString());
        FinalHttp request = new FinalHttp();
        request.post(CommonUrl.doctor_circo_url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);

                isLoading = false;
                if (mediaDetailRefresh != null && mediaDetailRefresh.isRefreshing()) {
                    mediaDetailRefresh.setRefreshing(false);
                }

                try {
                    Gson gson = new Gson();
                    MediaDetailData mediaDetailData = gson.fromJson(s, MediaDetailData.class);
                    if (mediaDetailData == null) {
                        Toast.makeText(MediaDetailActivity.this, getResources().getString(R.string.server_error),
                                Toast.LENGTH_SHORT).show();
                        MediaDetailActivity.this.finish();
                    } else {
                        if (mediaDetailData.getCode().equals("0")) {
                            MediaDetailData.DataBean data = mediaDetailData.getData();
                            shareTitle = data.getRealname();

                            titleBarBuilder.setTitleText(data.getRealname()).addItem("分享", new ItemClickListener() {
                                @Override
                                public void onClick() {
                                    // 分享
                                    if (shareImageUrl == null || shareImageUrl.equals("")) {
                                        shareImageUrl = ShareUtil.DEFAULT_SHARE_IMG_ULR;
                                    }
                                    DLog.d(LOG_TAG, "share_title = " + shareTitle);
                                    DLog.d(LOG_TAG, "share_content = " + shareContent);
                                    DLog.d(LOG_TAG, "share_image_url = " + shareImageUrl);
                                    DLog.d(LOG_TAG, "share_web_url = " + shareWebUrl);

                                    new ShareUtil.Builder()
                                            .setTitle(shareTitle)
                                            .setText(shareContent)
                                            .setTargetUrl(shareWebUrl)
                                            .setImageUrl(shareImageUrl)
                                            .setShareId(mediaId)
                                            .setShareSource("4")
                                            .setFrom("media")
                                            .build(MediaDetailActivity.this).innerShare();
                                }
                            }).build();
//                            toolbarTitle.setText(data.getRealname());

                            mediaName.setText(data.getRealname());
                            mediaType.setText(data.getSubject());
                            shareContent = data.getSubject();
                            shareImageUrl = data.getPhoto();
                            mImageLoader.displayImage(data.getPhoto(), mediaIcon, options);
                            mediaFunc.setText(data.getSynopsis());
                            DLog.d(LOG_TAG, "media_relation = " + data.getRelation() + ", media id = " + mediaId + ", userid = " + uuid);
                            // 0无关系    1我的粉丝    2我关注的    3互相关注    4自己
                            if (data.getRelation() == 2 || data.getRelation() == 3) {
                                isFollowed = true;
                                mediaFollow.setText("取消订阅");
                                mediaFollow.setTextColor(getResources().getColor(R.color.c_00c8aa));
                                mediaFollow.setBackgroundResource(R.drawable.follow_button_checked);
                            } else {
                                isFollowed = false;
                                mediaFollow.setText("+订阅");
                                mediaFollow.setTextColor(Color.WHITE);
                                mediaFollow.setBackgroundResource(R.drawable.follow_button);
                            }
                            shareWebUrl = data.getScrurl();

                            List<MediaDetailData.ArtlistBean> artlist = mediaDetailData.getArtlist();
                            if (artlist != null && !artlist.isEmpty()) {
                                if (mDatas != null) {
                                    if (page == DEFAULT_PAGE) {
                                        if (!mDatas.isEmpty()) {
                                            mDatas.clear();
                                        }

                                        mDatas.addAll(artlist);
                                    } else {
                                        mDatas.addAll(artlist);
                                    }
                                } else {
                                    mDatas = new ArrayList<>();
                                    mDatas.addAll(artlist);
                                }
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(MediaDetailActivity.this, "暂无更多数据", Toast.LENGTH_SHORT).show();
                                if (mediaDetailList.getFooterViewsCount() != 0 && footerView != null) {
                                    mediaDetailList.removeFooterView(footerView);
                                }
                                page--;
                            }
                        } else {
                            Toast.makeText(MediaDetailActivity.this, mediaDetailData.getMsg(),
                                    Toast.LENGTH_SHORT).show();
                            if (footerView != null) {
                                footerView.setVisibility(View.GONE);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);

                Toast.makeText(MediaDetailActivity.this, "加载失败", Toast.LENGTH_SHORT).show();

                if (mediaDetailRefresh != null && mediaDetailRefresh.isRefreshing()) {
                    mediaDetailRefresh.setRefreshing(false);
                }

                if (footerView != null) {
                    footerView.setVisibility(View.GONE);
                }
                isLoading = false;
                page--;
            }
        });
    }

    @SuppressLint("InflateParams")
    private void initList() {
        View headerView = LayoutInflater.from(this).inflate(R.layout.media_detail_header_layout, null);
        mediaIcon = (CircleImageView) headerView.findViewById(R.id.media_header_icon);
        mediaName = (TextView) headerView.findViewById(R.id.media_header_name);
        mediaType = (TextView) headerView.findViewById(R.id.media_header_type);
        mediaFollow = (TextView) headerView.findViewById(R.id.media_header_follow);
        mediaFunc = (TextView) headerView.findViewById(R.id.media_header_func_intro);
        footerView = LayoutInflater.from(this).inflate(R.layout.loading_more, null);
        if (mediaDetailList.getHeaderViewsCount() == 0) {
            mediaDetailList.addHeaderView(headerView, null, false);
        }
//        if (mediaDetailList.getFooterViewsCount() == 0)
//            mediaDetailList.addFooterView(footerView, null, false);
//        footerView.setVisibility(View.GONE);

        mediaIcon.setOnClickListener(this);
        mediaName.setOnClickListener(this);

        mediaFollow.setOnClickListener(this);
        mediaDetailList.setOnItemClickListener(this);
        mediaDetailList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (NetworkUtil.isNetWorkConnected()) {
                    if (!isLoading) {
                        if (scrollState == SCROLL_STATE_FLING) {
                            adapter.getImageLoader().pause();
                        } else {
                            adapter.getImageLoader().resume();
                        }
                        if (scrollState == SCROLL_STATE_IDLE && isBottom) {
                            isLoading = true;
                            if (mediaDetailList.getFooterViewsCount() == 0) {
                                mediaDetailList.addFooterView(footerView, null, false);
                            }
                            if (mediaDetailList.getFooterViewsCount() != 0
                                    && footerView.getVisibility() == View.GONE) {
                                footerView.setVisibility(View.VISIBLE);
                            }
                            // 加载更多
                            page++;
                            requestData();
                        }
                    }
                } else {
                    Toast.makeText(MediaDetailActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                isBottom = firstVisibleItem + visibleItemCount >= totalItemCount;
            }
        });

        adapter = new MediaDetailAdapter(this, mDatas);
        mediaDetailList.setAdapter(adapter);

        mediaDetailRefresh = (SwipeRefreshLayout) findViewById(R.id.media_detail_refresh);
        CommonUtils.setRefresh(mediaDetailRefresh);
        mediaDetailRefresh.setOnRefreshListener(this);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                this.finish();
//                break;
//
//            case R.id.my_site_share:
//                // 分享
//                if (shareImageUrl == null || shareImageUrl.equals("")) {
//                    shareImageUrl = ShareUtil.DEFAULT_SHARE_IMG_ULR;
//                }
//                DLog.d(LOG_TAG, "share_title = " + shareTitle);
//                DLog.d(LOG_TAG, "share_content = " + shareContent);
//                DLog.d(LOG_TAG, "share_image_url = " + shareImageUrl);
//                DLog.d(LOG_TAG, "share_web_url = " + shareWebUrl);
//
//                new ShareUtil.Builder()
//                        .setTitle(shareTitle)
//                        .setText(shareContent)
//                        .setTargetUrl(shareWebUrl)
//                        .setImageUrl(shareImageUrl)
//                        .setShareId(mediaId)
//                        .setShareSource("4")
//                        .setFrom("media")
//                        .build(MediaDetailActivity.this).innerShare();
//                break;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MediaDetailData.ArtlistBean data = (MediaDetailData.ArtlistBean) parent.getAdapter().getItem(position);
        Intent intent = new Intent();
        if (data.getModel().equals("4")) {
            // 原生视频资讯
            MediaDetailData.ArtlistBean.VideoBean video = data.getVideo();
            intent.setClass(this, VideoNewsActivity.class);
            intent.putExtra(VideoNewsActivity.NEWS_ID_INTENT_KEY, String.valueOf(data.getId()));
            intent.putExtra(VideoNewsActivity.VIDEO_TITLE_INTENT_KEY, data.getTitle());
            if (video != null) {
                intent.putExtra(VideoNewsActivity.VIDEO_UUID_INTENT_KEY, video.getUu());
                intent.putExtra(VideoNewsActivity.VIDEO_VUID_INTENT_KEY, video.getVu());
            }
            intent.putExtra(VideoNewsActivity.VIDEO_FROM_INTENT_KEY, VideoNewsActivity.FROM_WHERE);
        } else {
            intent.setClass(this, InfoDetailActivity.class);
            DLog.d(LOG_TAG, "media detail info url = " + data.getUrl());
            intent.putExtra("url", data.getUrl());
            intent.putExtra("ids", String.valueOf(data.getId()));
            intent.putExtra("title", data.getTitle());
            intent.putExtra("imageurl", data.getImage());
            InfoDetailActivity.shouldBack = true;
        }
        startActivity(intent);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.my_site_share, menu);
//        return true;
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.media_header_icon:
            case R.id.media_header_name:
                Intent intent = new Intent(this, MediaSettingActivity.class);
                intent.putExtra("uuid", mediaId);
                intent.putExtra("mediaIcon", shareImageUrl);
                intent.putExtra("mediaFunc", shareContent);
                startActivity(intent);
                break;

            case R.id.media_header_follow:
                StatisticalTools.eventCount(this, "MediaSubscription");
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(this).context);
                } else {
                    if (isFollowed) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("取消关注后将不会再收到该媒体号的消息。");
                        builder.setNegativeButton("取消", null);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!acting) {
                                    acting = true;
                                    removeFollow(uuid, mediaId);
                                }
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.c_00c8aa));
                        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.c_00c8aa));

                    } else {
                        if (!acting) {
                            acting = true;
                            addFollow(uuid, mediaId);
                        }
                    }
                }
                break;
        }
    }

    /**
     * 添加订阅
     */
    private void addFollow(String userid, String touserid) {
        String bind = userid + touserid;
        String sign = MD5Util.MD5(bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("a", "dcFriend");
        params.put("m", "friend_add");
        params.put("userid", userid);
        params.put("touserid", touserid);
        params.put("bind", bind);
        params.put("sign", sign);

        DLog.d(LOG_TAG, "addFollow: " + CommonUrl.doctor_circo_url + "?" + params.toString());
        FinalHttp request = new FinalHttp();
        request.post(CommonUrl.doctor_circo_url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);

                acting = false;
                Gson gson = new Gson();
                IsFollowData data = gson.fromJson(s, IsFollowData.class);

                if (data != null) {
                    if (data.getCode().equals("0")) {
                        Toast.makeText(MediaDetailActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                        YMApplication.isrefresh = true;
                        InfoDetailActivity.isShouldReload = true;
                        isFollowed = true;
                        mediaFollow.setText("取消订阅");
                        mediaFollow.setTextColor(getResources().getColor(R.color.c_00c8aa));
                        mediaFollow.setBackgroundResource(R.drawable.follow_button_checked);
                        // 更新缓存
                        ServiceTitleEntity serviceTitleEntity = new Gson().fromJson(cacheData, ServiceTitleEntity.class);
                        if (serviceTitleEntity != null) {
                            List<SubscribeMediaBean> mediaList = serviceTitleEntity.getMedia();
                            if (mediaList != null && !mediaList.isEmpty()) {
                                SubscribeMediaBean bean = new SubscribeMediaBean();
                                bean.setId(Integer.parseInt(mediaId));
                                bean.setType(1);
                                bean.setName(mediaName.getText().toString().trim());
                                if (mediaList.contains(bean)) {
                                    mediaList.remove(bean);
                                }
                                mediaList.add(bean);
                            }
                            mediaCache.edit().putString("channelData", new Gson().toJson(serviceTitleEntity)).apply();
                        }
                    } else {
                        Toast.makeText(MediaDetailActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MediaDetailActivity.this,
                            MediaDetailActivity.this.getString(R.string.server_error),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);

                acting = false;
                Toast.makeText(MediaDetailActivity.this, "关注失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 取消订阅
     */
    private void removeFollow(String userid, String touserid) {
        String bind = userid + touserid;
        String sign = MD5Util.MD5(bind + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();
        params.put("a", "dcFriend");
        params.put("m", "friend_del");
        params.put("userid", userid);
        params.put("touserid", touserid);
        params.put("bind", bind);
        params.put("sign", sign);

        DLog.d(LOG_TAG, "removeFollow: " + CommonUrl.doctor_circo_url + "?" + params.toString());
        FinalHttp request = new FinalHttp();
        request.post(CommonUrl.doctor_circo_url, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);

                acting = false;
                IsFollowData data = new Gson().fromJson(s, IsFollowData.class);

                if (data != null) {
                    if (data.getCode().equals("0")) {
                        Toast.makeText(MediaDetailActivity.this, "取消订阅成功", Toast.LENGTH_SHORT).show();
                        YMApplication.isrefresh = true;
                        InfoDetailActivity.isShouldReload = true;
                        isFollowed = false;
                        mediaFollow.setText("+订阅");
                        mediaFollow.setTextColor(Color.WHITE);
                        mediaFollow.setBackgroundResource(R.drawable.follow_button);
                        // 更新缓存
                        ServiceTitleEntity serviceTitleEntity = new Gson().fromJson(cacheData, ServiceTitleEntity.class);
                        if (serviceTitleEntity != null) {
                            List<SubscribeMediaBean> mediaList = serviceTitleEntity.getMedia();
                            if (mediaList != null && !mediaList.isEmpty()) {
                                SubscribeMediaBean bean = new SubscribeMediaBean();
                                bean.setId(Integer.parseInt(mediaId));
                                bean.setType(0);
                                bean.setName(mediaName.getText().toString().trim());
                                DLog.d(LOG_TAG, "contains bean = " + mediaList.contains(bean));
                                if (mediaList.contains(bean)) {
                                    mediaList.remove(bean);
                                    mediaList.add(bean);
                                }
                            }
                            mediaCache.edit().putString("channelData", new Gson().toJson(serviceTitleEntity)).apply();
                        }

                        // 更新媒体号列表缓存
                        MediaListCacheData mediaListCacheData = new Gson().fromJson(cacheMediaListData, MediaListCacheData.class);
                        if(null==mediaListCacheData){
                            return;
                        }
                        List<AddressBook> mediacachelist = mediaListCacheData.getMediacachelist();
                        AddressBook addressBook = new AddressBook();
                        addressBook.setId(mediaId);
                        DLog.d(LOG_TAG, "contains addressbook = " + mediacachelist.contains(addressBook));
                        if (mediacachelist.contains(addressBook)) {
                            mediacachelist.remove(addressBook);
                        }
                        mediaListCacheData.setMediacachelist(mediacachelist);
                        cacheMediaListData = new Gson().toJson(mediaListCacheData);
                        mediaListCache.edit().putString("media_list_cache", cacheMediaListData).apply();
                        mediaListCache.edit().putBoolean("changed", true).apply();
                    } else {
                        Toast.makeText(MediaDetailActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MediaDetailActivity.this,
                            MediaDetailActivity.this.getString(R.string.server_error),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);

                acting = false;
                Toast.makeText(MediaDetailActivity.this, "取消订阅失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        if (NetworkUtil.isNetWorkConnected()) {
            if (mediaDetailRefresh != null && !mediaDetailRefresh.isRefreshing()) {
                mediaDetailRefresh.setRefreshing(true);
                page = DEFAULT_PAGE;
                requestData();
            }
        } else {
            Toast.makeText(this, "网络不给力", Toast.LENGTH_SHORT).show();
            mediaDetailRefresh.setRefreshing(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
