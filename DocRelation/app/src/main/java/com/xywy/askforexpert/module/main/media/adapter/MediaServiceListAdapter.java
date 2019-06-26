package com.xywy.askforexpert.module.main.media.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.adapter.BaseRecyclerViewAdapter;
import com.xywy.askforexpert.appcommon.utils.DensityUtils;
import com.xywy.askforexpert.model.media.ServicesMediasListBean;

import java.util.List;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/7/5 10:04
 */

/**
 * 媒体号/服务号 列表 adapter
 */
public class MediaServiceListAdapter extends BaseRecyclerViewAdapter<ServicesMediasListBean> {
    private static final String TAG = "MediaServiceListAdapter";
    private final DisplayImageOptions mOptions;

    public MediaServiceListAdapter(Context mContext, List<ServicesMediasListBean> mDatas,
                                   int mItemLayoutId, RecyclerView recyclerView) {
        super(mContext, mDatas, mItemLayoutId, recyclerView);

        mOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(R.drawable.icon_photo_def)
                .showImageOnFail(R.drawable.icon_photo_def)
                .showImageForEmptyUri(R.drawable.icon_photo_def)
                .displayer(new RoundedBitmapDisplayer(DensityUtils.dp2px(2)))
                .build();
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(mItemLayoutId, parent, false);
        return new Holder(view);
    }

    @Override
    protected void onBind(RecyclerView.ViewHolder holder, int pos, ServicesMediasListBean data) {
        if (holder instanceof Holder) {
            Holder h = (Holder) holder;
            if (data != null) {
                ImageLoader.getInstance().displayImage(data.getImg() == null ? "" : data.getImg(),
                        h.avatar, mOptions);
                h.name.setText(data.getName() == null ? "" : data.getName());
            }
        }
    }

    class Holder extends RecyclerView.ViewHolder {

        protected ImageView avatar;
        protected TextView name;

        public Holder(View itemView) {
            super(itemView);

            avatar = (ImageView) itemView.findViewById(R.id.media_service_list_avatar);
            name = (TextView) itemView.findViewById(R.id.media_service_list_name);
        }
    }
}
