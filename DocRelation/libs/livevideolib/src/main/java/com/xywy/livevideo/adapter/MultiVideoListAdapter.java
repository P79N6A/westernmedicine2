package com.xywy.livevideo.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.xywy.livevideo.LiveManager;
import com.xywy.livevideo.entity.HostInfoWithListRespEntity;
import com.xywy.livevideo.entity.VideoListEntity;
import com.xywy.livevideolib.R;
import com.xywy.uilibrary.recyclerview.adapter.XYWYRVMultiTypeBaseAdapter;
import com.xywy.util.ImageLoaderUtils;
import com.xywy.util.XYImageLoader;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by zhangzheng on 2017/3/8.
 */
public class MultiVideoListAdapter extends XYWYRVMultiTypeBaseAdapter<VideoListEntity> {

    private IOnItemClickListener iOnItemClickListener;

    public MultiVideoListAdapter(Context context) {
        super(context);
        addItemViewDelegate(new VideoListNormalItem());
        addItemViewDelegate(new VideoListHeadItem());
    }

    public class VideoListHeadItem implements ItemViewDelegate<VideoListEntity> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.video_list_head_item;
        }

        @Override
        public boolean isForViewType(VideoListEntity item, int position) {
            return item.getItemType() == VideoListEntity.ITEM_TYPE_HEAD;
        }

        @Override
        public void convert(ViewHolder holder, VideoListEntity videoListEntity, int position) {
            if (videoListEntity.getData() instanceof HostInfoWithListRespEntity.DataBean) {
                HostInfoWithListRespEntity.DataBean dataBean = (HostInfoWithListRespEntity.DataBean) videoListEntity.getData();
                holder.setText(R.id.tv_host_page_doc_name, dataBean.getName());
                holder.setText(R.id.tv_host_page_doc_info, dataBean.getSynopsis());
                holder.setText(R.id.tv_host_page_attend_number, dataBean.getFollowNum());
                holder.setText(R.id.tv_host_page_fans_number, dataBean.getFansNum());
                holder.setOnClickListener(R.id.iv_host_page_back, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (context instanceof Activity) {
                            ((Activity) context).finish();
                        }
                    }
                });
                ImageLoaderUtils.getInstance().init(context);
                XYImageLoader.getInstance().displayUserHeadImage(dataBean.getPortrait(), (ImageView) holder.getView(R.id.iv_host_page_head));
            }
        }
    }

    public class VideoListNormalItem implements ItemViewDelegate<VideoListEntity> {


        @Override
        public int getItemViewLayoutId() {
            return R.layout.live_video_list_item;
        }

        @Override
        public boolean isForViewType(VideoListEntity item, int position) {
            return item.getItemType() == VideoListEntity.ITEM_TYPE_NORMAL;
        }

        @Override
        public void convert(ViewHolder holder, VideoListEntity videoListEntity, int position) {
            if (videoListEntity.getData() instanceof HostInfoWithListRespEntity.DataBean.ListBean) {
                HostInfoWithListRespEntity.DataBean.ListBean item = (HostInfoWithListRespEntity.DataBean.ListBean) videoListEntity.getData();
                holder.setText(R.id.tv_video_item_title, item.getName());
                holder.setText(R.id.tv_video_item_user_name, item.getUser().getName());
                XYImageLoader.getInstance().displayUserHeadImage(item.getUser().getPortrait(), (ImageView) holder.getView(R.id.iv_video_item_user_head));
                holder.setText(R.id.tv_video_item_number, item.getAmount() + "");
                XYImageLoader.getInstance().displayImage(item.getCover(), (ImageView) holder.getView(R.id.iv_video_item_bkg));

                if (item.getState() == LiveManager.TYPE_REALTIME) {
                    holder.setText(R.id.btn_video_item_play, "直播");
                    holder.getView(R.id.btn_video_item_play).setBackgroundResource(R.drawable.gift_send_bkg);
                } else {
                    holder.setText(R.id.btn_video_item_play, "回放");
                    holder.getView(R.id.btn_video_item_play).setBackgroundResource(R.drawable.btn_replay_bkg);
                }
                final int p = position;
                holder.getView(R.id.ll_host_main_page_list_item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iOnItemClickListener.onItemClickListener(getItem(p));
                    }
                });
            }
        }
    }

    public void setiOnItemClickListener(IOnItemClickListener iOnItemClickListener) {
        this.iOnItemClickListener = iOnItemClickListener;
    }

    public static interface IOnItemClickListener {
        void onItemClickListener(VideoListEntity videoListEntity);
    }

}
