package com.xywy.askforexpert.module.my.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.adapter.CommonBaseAdapter;
import com.xywy.askforexpert.appcommon.base.adapter.CommonViewHolder;
import com.xywy.askforexpert.widget.CircleImageView;

import java.util.List;
import java.util.Map;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2015/11/27 10:47
 */
public class MyInviteFriendAdapter extends CommonBaseAdapter {
    private final DisplayImageOptions options;
    private List<Map<String, Object>> mDatas;

    public MyInviteFriendAdapter(Context mContext, List<Map<String, Object>> mDatas, int mItemLayoutId) {
        super(mContext, mDatas, mItemLayoutId);

        this.mDatas = mDatas;

        options = new DisplayImageOptions.Builder().showImageOnFail(R.drawable.icon_photo_def)
                .showImageOnLoading(R.drawable.icon_photo_def).cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(mItemLayoutId, parent, false);
        }

        String avatarUrl = (String) this.mDatas.get(position).get("avatar");

        CircleImageView friendAvatar = CommonViewHolder.getView(convertView, R.id.friend_avatar);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(avatarUrl, friendAvatar, options);

        return convertView;
    }
}
