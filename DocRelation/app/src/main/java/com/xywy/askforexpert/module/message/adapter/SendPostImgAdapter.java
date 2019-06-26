package com.xywy.askforexpert.module.message.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.adapter.CommonBaseAdapter;
import com.xywy.askforexpert.appcommon.base.adapter.CommonViewHolder;
import com.xywy.askforexpert.appcommon.utils.others.DLog;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

/**
 * Compiler: Android Studio
 * Project: D_Platform
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2015/12/16 16:47
 */
public class SendPostImgAdapter extends CommonBaseAdapter<Bitmap> {
    public static boolean isAddShouldShow = true;
    private List<Bitmap> mDatas;
    private int maxSize = 9;
    private FinalBitmap fb;

    public SendPostImgAdapter(Context mContext, List<Bitmap> mDatas, int mItemLayoutId) {
        super(mContext, mDatas, mItemLayoutId);

        this.mDatas = mDatas;
        if (mContext != null) {
            fb = FinalBitmap.create(mContext, false);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(mItemLayoutId, parent, false);
        }

        ImageView img = CommonViewHolder.getView(convertView, R.id.idcard_gridView_item_photo);

        DLog.d("img_size", String.valueOf(getCount()));
        if (position == getCount()) {
            if (isAddShouldShow) {
                img.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.card_add));
                if (position == maxSize) {
                    img.setVisibility(View.GONE);
                }
            } else {
                img.setVisibility(View.GONE);
            }
        } else {
            img.setImageBitmap(mDatas.get(position));
        }

        return convertView;
    }
}
