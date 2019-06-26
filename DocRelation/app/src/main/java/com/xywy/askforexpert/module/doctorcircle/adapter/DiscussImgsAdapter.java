package com.xywy.askforexpert.module.doctorcircle.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.adapter.CommonBaseAdapter;
import com.xywy.askforexpert.appcommon.base.adapter.CommonViewHolder;
import com.xywy.askforexpert.appcommon.utils.AppUtils;

import java.util.List;

/**
 * 病例研讨班 辅助检查 图片集合 适配器
 * <p>
 * Compiler: Android Studio
 * Project: D_Platform
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2015/12/23 14:00
 */
public class DiscussImgsAdapter extends CommonBaseAdapter<String> {

    private final ImageLoader mImageLoader;
    private final DisplayImageOptions options;
    private List<String> mDatas;
    private boolean isShould = true;

    public DiscussImgsAdapter(Context mContext, List<String> mDatas, int mItemLayoutId) {
        super(mContext, mDatas, mItemLayoutId);

        this.mDatas = mDatas;

        mImageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnFail(R.drawable.discuss_imgs)
                .showImageOnLoading(R.drawable.discuss_imgs)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    public void setIsShould(boolean isShould) {
        this.isShould = isShould;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(mItemLayoutId, parent, false);
        }

        RelativeLayout relativeLayout = CommonViewHolder.getView(convertView, R.id.discuss_img_item_rl);
        ImageView discussImgItem = CommonViewHolder.getView(convertView, R.id.discuss_img_item);

        // 默认选中第一张图片
        if (position == 0 && isShould) {
            relativeLayout.setBackgroundResource(R.drawable.discuss_imgs_bg);
        }

        ViewGroup.LayoutParams layoutParams = discussImgItem.getLayoutParams();
        int screenWidth = AppUtils.getScreenWidth((Activity) mContext);
        layoutParams.width = screenWidth / 3;
        layoutParams.height = (screenWidth / 3) * 62 / 105;
        discussImgItem.setLayoutParams(layoutParams);

        mImageLoader.displayImage(mDatas.get(position), discussImgItem, options);

        return convertView;
    }
}
