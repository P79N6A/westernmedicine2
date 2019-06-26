package com.xywy.askforexpert.module.doctorcircle.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.adapter.CommonBaseAdapter;
import com.xywy.askforexpert.appcommon.base.adapter.CommonViewHolder;
import com.xywy.askforexpert.model.discussDetail.DiscussPraiseList;

import java.util.List;

/**
 * 点赞头像 适配器
 * <p>
 * Compiler: Android Studio
 * Project: D_Platform
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2015/12/21 17:03
 */
public class PraiseAvatarAdapter extends CommonBaseAdapter<DiscussPraiseList> {

    private final DisplayImageOptions OPTIONS;

    private List<DiscussPraiseList> mDatas;

    public PraiseAvatarAdapter(Context mContext, List<DiscussPraiseList> mDatas, int mItemLayoutId) {
        super(mContext, mDatas, mItemLayoutId);

        this.mDatas = mDatas;

        OPTIONS = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnFail(R.drawable.icon_photo_def)
                .showImageForEmptyUri(R.drawable.icon_photo_def)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.drawable.icon_photo_def)
                .build();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(mItemLayoutId, parent, false);
        }

        // 点赞者头像
        ImageView praiseAvatar = CommonViewHolder.getView(convertView, R.id.praise_avatar);

        mImageLoader.displayImage(mDatas.get(position).getPhoto() == null ? "" : mDatas.get(position).getPhoto(),
                praiseAvatar, OPTIONS);

        return convertView;
    }
}
