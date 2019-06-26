package com.xywy.askforexpert.module.main.subscribe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.media.MediaList;
import com.xywy.askforexpert.module.main.media.MediaSettingActivity;
import com.xywy.askforexpert.module.main.media.adapter.SubMediaAdapter;
import com.xywy.askforexpert.module.main.media.newpart.MediaListActivityNew;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 订阅媒体号
 * <p>
 * create by shihao
 */
public class SubscribeMediactivity extends AppCompatActivity {

    private static final String TAG = SubscribeMediactivity.class.getSimpleName();
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.media_setting_iv)
    ImageView mediaSettingIv;
    @Bind(R.id.media_lv)
    ListView mediaLv;
    @Bind(R.id.media_refresh)
    SwipeRefreshLayout mediaRefresh;
    private String uid = "";
    private int currentIndex = 1;
    private boolean isBottomFlag = false;
    private int allPageNum;
    private int everyPageSize = 10;
    private MediaList mediaList;
    private List<MediaList.DataEntity> messageLists = new ArrayList<>();
    private SubMediaAdapter subMediaAdapter;
    private DisplayImageOptions displayImageOptions;

    public static  void start(Activity activity){
        Intent intent = new Intent(activity, SubscribeMediactivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_media);
        ButterKnife.bind(this);

        CommonUtils.initSystemBar(this);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        if (!YMUserService.isGuest()) {
            if (YMApplication.getLoginInfo() != null) {
                uid = YMApplication.getLoginInfo().getData().getPid();
            }
        }
        mediaList = new MediaList();
        displayImageOptions = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .showImageOnLoading(R.drawable.img_default_bg)
                .showImageForEmptyUri(R.drawable.img_default_bg)
                .showImageOnFail(R.drawable.img_default_bg)
                .cacheInMemory(true).cacheOnDisk(true).build();
        mediaRefresh.setColorSchemeResources(R.color.color_scheme_2_1, R.color.color_scheme_2_2,
                R.color.color_scheme_2_3, R.color.color_scheme_2_4);
        messageLists = YMApplication.getInstance().getMediaList().getData();
        subMediaAdapter = new SubMediaAdapter(this, messageLists);
        mediaRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentIndex = 1;
                getNewMediaNum(currentIndex);
            }
        });
        mediaLv.setAdapter(subMediaAdapter);
        initHead();
        mediaLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                switch (scrollState) {
                    case SCROLL_STATE_FLING:
                        subMediaAdapter.setIsScroll(true);
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        subMediaAdapter.setIsScroll(true);
                        break;
                    case SCROLL_STATE_IDLE:
                        subMediaAdapter.setIsScroll(false);
                        if (isBottomFlag) {
                            loadMore();
                        }
                        break;

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                isBottomFlag = (firstVisibleItem + visibleItemCount == totalItemCount);

            }
        });
    }

    private void initHead() {
        View head= LayoutInflater.from(this).inflate(R.layout.order_more,null);
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MediaListActivityNew.start(SubscribeMediactivity.this);
            }
        });
        mediaLv.addHeaderView(head);
    }

    /**
     * 加载更多
     */
    private void loadMore() {
        currentIndex++;
        DLog.i("media", "加载更多" + currentIndex);
        getNewMediaNum(currentIndex);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

//    @OnItemClick(R.id.media_lv)
//    void onItemClick(int position) {
//
//    }

    @OnClick({R.id.iv_back, R.id.media_setting_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.media_setting_iv:
                Intent intent = new Intent(this, MediaSettingActivity.class);

                startActivity(intent);
                break;
        }
    }

    /**
     * 获取媒体号，save
     */
    public void getNewMediaNum(final int pageNum) {

        String sign = MD5Util.MD5(uid + Constants.MD5_KEY);
        AjaxParams params = new AjaxParams();

        params.put("bind", uid);
        params.put("userid", uid);
        params.put("a", "media");
        params.put("m", "mediaArticleList");
        params.put("sign", sign);
        params.put("page", pageNum + "");

        FinalHttp fh = new FinalHttp();
        DLog.i(TAG, "URL" + CommonUrl.doctor_circo_url + "?" + params.toString());
        fh.post(CommonUrl.doctor_circo_url, params, new AjaxCallBack() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                DLog.i("媒体号返回数据", s);
                try {
                    Gson gson = new Gson();
                    mediaList = gson.fromJson(s, MediaList.class);
                    int total = mediaList.getTotal();

                    allPageNum = (total + everyPageSize - 1) / everyPageSize;      //总条数

                    if (pageNum == 1) {
                        messageLists.clear();
                        mediaRefresh.setRefreshing(false);
                    }

                    if (pageNum > allPageNum) {
                        return;
                    }

                    if (mediaList.getData() == null) {
                        return;
                    }
                    messageLists.addAll(mediaList.getData());
                    DLog.i("media", "当前列表大小" + messageLists.size());
                    subMediaAdapter.bindData(messageLists);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);

                mediaRefresh.setRefreshing(false);
            }
        });
    }
}
