package com.xywy.livevideo.player;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xywy.base.XywyBaseActivity;
import com.xywy.livevideo.adapter.MultiVideoListAdapter;
import com.xywy.livevideo.adapter.VideoListLoadMoreAdapter;
import com.xywy.livevideo.common_interface.CommonLiveResponse;
import com.xywy.livevideo.common_interface.Constant;
import com.xywy.livevideo.common_interface.LiveRequest;
import com.xywy.livevideo.entity.HostInfoWithListRespEntity;
import com.xywy.livevideo.entity.VideoListEntity;
import com.xywy.livevideolib.R;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzheng on 2017/2/28.
 */
public class HostMainPageActivity extends XywyBaseActivity implements View.OnClickListener {

    private ImageView ivBack;

    private ImageView ivHead;

    private TextView tvDocName;

    //简介
    private TextView tvDocInfo;

    private TextView tvFollowNumber;

    private TextView tvFansNumber;

    private RecyclerView rlvVideoList;

    //private VideoListAdapter adapter;

    private MultiVideoListAdapter multiVideoListAdapter;

    private VideoListLoadMoreAdapter loadMoreWrapper;

    private IntentParams params;

    public static final String HOSTMAINPAGE_PARAMS_TAG = "HOSTMAINPAGE_PARAMS_TAG";


    private int page = 1;

    public static void startHostMainPageActivity(Activity activity, String hostId, String userId) {
        Intent intent = new Intent(activity, HostMainPageActivity.class);
        IntentParams params = new IntentParams();
        params.setHostId(hostId);
        params.setUserId(userId);
        intent.putExtra(HOSTMAINPAGE_PARAMS_TAG, params);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_activity_host_main_page);
        params = getIntent().getParcelableExtra(HOSTMAINPAGE_PARAMS_TAG);
        hideCommonBaseTitle();
        initView();
        initData();
    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.iv_host_page_back);
        ivBack.setOnClickListener(this);
        ivHead = (ImageView) findViewById(R.id.iv_host_page_head);
        tvDocName = (TextView) findViewById(R.id.tv_host_page_doc_name);
        tvDocInfo = (TextView) findViewById(R.id.tv_host_page_doc_info);
        tvFollowNumber = (TextView) findViewById(R.id.tv_host_page_attend_number);
        tvFansNumber = (TextView) findViewById(R.id.tv_host_page_fans_number);
        rlvVideoList = (RecyclerView) findViewById(R.id.rlv_host_page_video_list);

//        adapter = new VideoListAdapter(this);
//        adapter.setData(new ArrayList<HostInfoWithListRespEntity.DataBean.ListBean>());
//        adapter.setiOnItemClickListener(new VideoListAdapter.IOnItemClickListener() {
//            @Override
//            public void onItemClickListener(HostInfoWithListRespEntity.DataBean.ListBean videoListEntity) {
//                // TODO: 2017/3/6  进入直播或回放
//            }
//        });

        multiVideoListAdapter = new MultiVideoListAdapter(this);
        multiVideoListAdapter.setData(new ArrayList<VideoListEntity>());
        multiVideoListAdapter.setiOnItemClickListener(new MultiVideoListAdapter.IOnItemClickListener() {
            @Override
            public void onItemClickListener(VideoListEntity videoListEntity) {
                if (videoListEntity.getItemType() == VideoListEntity.ITEM_TYPE_NORMAL) {
                    if (videoListEntity.getData() instanceof HostInfoWithListRespEntity.DataBean.ListBean) {
                        HostInfoWithListRespEntity.DataBean.ListBean bean = (HostInfoWithListRespEntity.DataBean.ListBean) videoListEntity.getData();
                        if (bean.getState() == Constant.CONSTANT_LIVE_STATE_LIVE) {
                            XYPlayerActivity.startPlayerActivity(HostMainPageActivity.this, bean.getId() + "", bean.getRtmp(), Constant.CONSTANT_LIVE_STATE_LIVE, bean.getChatroomsid(), null);
                        } else {
                            XYPlayerActivity.startPlayerActivity(HostMainPageActivity.this, bean.getId() + "", bean.getVod(), Constant.CONSTANT_LIVE_STATE_END, bean.getChatroomsid(), bean.getVod_list());
                        }
                    }
                }
            }
        });
        //rlvVideoList.setAdapter(adapter);
        //rlvVideoList.setAdapter(multiVideoListAdapter);
        rlvVideoList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        loadMoreWrapper = new VideoListLoadMoreAdapter(multiVideoListAdapter, rlvVideoList);
        loadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                initData();
            }
        });
        rlvVideoList.setAdapter(loadMoreWrapper);
    }

    private void initData() {
        int isList = 1;
        LiveRequest.getHostInfoList(params.getHostId(), params.getUserId(), page, isList, new CommonLiveResponse<HostInfoWithListRespEntity>() {
            @Override
            public void onReceived(HostInfoWithListRespEntity hostInfoWithListRespEntity) {
                if (hostInfoWithListRespEntity.getCode() == 10000) {
                    if (multiVideoListAdapter.getItemCount() == 0) {
                        VideoListEntity entity = new VideoListEntity();
                        entity.setItemType(VideoListEntity.ITEM_TYPE_HEAD);
                        entity.setData(hostInfoWithListRespEntity.getData());
                        multiVideoListAdapter.addData(entity);
                    }
                    if (hostInfoWithListRespEntity.getData().getList() != null && !hostInfoWithListRespEntity.getData().getList().isEmpty()) {
                        List<VideoListEntity> videoListEntityList = new ArrayList<VideoListEntity>();
                        for (int i = 0; i < hostInfoWithListRespEntity.getData().getList().size(); i++) {
                            VideoListEntity entity = new VideoListEntity();
                            entity.setItemType(VideoListEntity.ITEM_TYPE_NORMAL);
                            entity.setData(hostInfoWithListRespEntity.getData().getList().get(i));
                            videoListEntityList.add(entity);
                        }
                        multiVideoListAdapter.addData(videoListEntityList);
                        //multiVideoListAdapter.notifyDataSetChanged();
                        loadMoreWrapper.notifyDataSetChanged();
                        page++;
                    } else {
                        loadMoreWrapper.setLoadingState(false);
                    }
                    //Toast.makeText(HostMainPageActivity.this, "获取主播信息列表成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HostMainPageActivity.this, "获取主播信息列表失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(HostMainPageActivity.this, "获取主播信息列表失败", Toast.LENGTH_SHORT).show();
            }


        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_host_page_back) {
            //后退
            finish();
        }
    }

    public static class IntentParams implements Parcelable {
        private String hostId;
        private String userId;

        public IntentParams() {

        }

        public static final Creator<IntentParams> CREATOR = new Creator<IntentParams>() {
            @Override
            public IntentParams createFromParcel(Parcel in) {
                IntentParams params = new IntentParams();
                params.setHostId(in.readString());
                params.setUserId(in.readString());
                return params;
            }

            @Override
            public IntentParams[] newArray(int size) {
                return new IntentParams[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(hostId);
            dest.writeString(userId);
        }

        public String getHostId() {
            return hostId;
        }

        public void setHostId(String hostId) {
            this.hostId = hostId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
