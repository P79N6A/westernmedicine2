package com.xywy.askforexpert.module.main.media.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.adapter.CommonBaseAdapter;
import com.xywy.askforexpert.appcommon.base.adapter.CommonViewHolder;
import com.xywy.askforexpert.model.mediaDetail.MediaDetailData;

import java.util.List;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/3/7 13:33
 */
public class MediaDetailAdapter extends CommonBaseAdapter<MediaDetailData.ArtlistBean> {
    private static final int NORMAL_TYPE = 0;
    private static final int THREE_PICS_TYPE = 1;
    private final DisplayImageOptions options;

    public MediaDetailAdapter(Context mContext, List<MediaDetailData.ArtlistBean> mDatas) {
        super(mContext, mDatas);

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnFail(R.drawable.discuss_imgs)
                .showImageOnLoading(R.drawable.discuss_imgs)
                .build();
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        MediaDetailData.ArtlistBean artlistBean = mDatas.get(position);
        if (artlistBean.getModel().equals("1")) {
            return THREE_PICS_TYPE;
        } else {
            return NORMAL_TYPE;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MediaDetailData.ArtlistBean artlistBean = (MediaDetailData.ArtlistBean) this.getItem(position);

        if (convertView == null) {
            switch (getItemViewType(position)) {
                case NORMAL_TYPE:
                    convertView = mLayoutInflater.inflate(R.layout.media_item_layout, parent, false);
                    break;

                case THREE_PICS_TYPE:
                    convertView = mLayoutInflater.inflate(R.layout.media_item_3_pics_item, parent, false);
                    break;

                default:
                    convertView = mLayoutInflater.inflate(R.layout.media_item_layout, parent, false);
                    break;
            }
        }

        TextView mediaItemTitle = CommonViewHolder.getView(convertView, R.id.media_item_title);
        TextView mediaItemTime = CommonViewHolder.getView(convertView, R.id.media_item_time);
        TextView mediaItemPraiseCount = CommonViewHolder.getView(convertView, R.id.media_item_praise_count);
        TextView mediaItemReadCount = CommonViewHolder.getView(convertView, R.id.media_item_click_count);

        ImageView mediaItemImg = CommonViewHolder.getView(convertView, R.id.media_item_img);
        TextView mediaItemSummary = CommonViewHolder.getView(convertView, R.id.media_item_summary);

        ImageView mediaItemPic0 = CommonViewHolder.getView(convertView, R.id.media_pic_0);
        ImageView mediaItemPic1 = CommonViewHolder.getView(convertView, R.id.media_pic_1);
        ImageView mediaItemPic2 = CommonViewHolder.getView(convertView, R.id.media_pic_2);

        mediaItemTitle.setText(artlistBean.getTitle());
        mediaItemTime.setText(artlistBean.getCreatetime());
        mediaItemPraiseCount.setText(artlistBean.getPraiseNum());
        mediaItemReadCount.setText(String.valueOf(artlistBean.getReadNum()));
        if (artlistBean.getModel().equals("1")) {
            List<String> imgs = artlistBean.getImgs();
            if (imgs != null && !imgs.isEmpty()) {
                if (imgs.get(0) != null) {
                    mImageLoader.displayImage(imgs.get(0), mediaItemPic0, options);
                }
                if (imgs.size() >= 2 && imgs.get(1) != null) {
                    mImageLoader.displayImage(imgs.get(1), mediaItemPic1, options);
                }
                if (imgs.size() >= 3 && imgs.get(2) != null) {
                    mImageLoader.displayImage(imgs.get(2), mediaItemPic2, options);
                }
            }
        } else {
            mImageLoader.displayImage(artlistBean.getImage(), mediaItemImg, options);
            mediaItemSummary.setText(artlistBean.getVector());
        }

        return convertView;
    }
}
