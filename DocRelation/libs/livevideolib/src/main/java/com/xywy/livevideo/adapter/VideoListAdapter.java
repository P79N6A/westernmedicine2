package com.xywy.livevideo.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.xywy.livevideo.entity.HostInfoWithListRespEntity;
import com.xywy.livevideolib.R;
import com.xywy.uilibrary.recyclerview.adapter.XYWYRVSingleTypeBaseAdapter;
import com.xywy.util.ImageLoaderUtils;
import com.xywy.util.XYImageLoader;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by zhangzheng on 2017/2/28.
 */
public class VideoListAdapter extends XYWYRVSingleTypeBaseAdapter<HostInfoWithListRespEntity.DataBean.ListBean> {
    private IOnItemClickListener iOnItemClickListener;

    public VideoListAdapter(Context context) {
        super(context);
        ImageLoaderUtils.getInstance().init(context);
    }

    @Override
    protected int getItemLayoutResId() {
        return R.layout.live_video_list_item;
    }

    @Override
    protected void convert(ViewHolder holder, HostInfoWithListRespEntity.DataBean.ListBean item, final int position) {
        holder.setText(R.id.tv_video_item_title, item.getName());
        holder.setText(R.id.tv_video_item_user_name, item.getUser().getName());
        XYImageLoader.getInstance().displayUserHeadImage(item.getUser().getPortrait(), (ImageView) holder.getView(R.id.iv_video_item_user_head));
        holder.setText(R.id.tv_video_item_number, item.getAmount() + "");
        XYImageLoader.getInstance().displayImage(item.getCover(), (ImageView) holder.getView(R.id.iv_video_item_bkg));
        holder.getView(R.id.ll_host_main_page_list_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iOnItemClickListener.onItemClickListener(getItem(position));
            }
        });
    }

    public void setiOnItemClickListener(IOnItemClickListener iOnItemClickListener) {
        this.iOnItemClickListener = iOnItemClickListener;
    }

    public static interface IOnItemClickListener {
        void onItemClickListener(HostInfoWithListRespEntity.DataBean.ListBean videoListEntity);
    }
}
