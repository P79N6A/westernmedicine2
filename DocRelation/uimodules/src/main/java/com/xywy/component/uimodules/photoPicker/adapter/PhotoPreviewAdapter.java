package com.xywy.component.uimodules.photoPicker.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.xywy.component.R;
import com.xywy.component.uimodules.PPImageLoader;
import com.xywy.component.uimodules.photoPicker.model.PhotoInfo;
import com.xywy.component.uimodules.photoPicker.view.PPZoomImageView;

import java.util.List;

/**
 * 预览 adpater
 */
public class PhotoPreviewAdapter extends ViewHolderRecyclingPagerAdapter<PhotoPreviewAdapter.PreviewViewHolder, PhotoInfo> {

    private int angle = 0;

    public PhotoPreviewAdapter(Context context, List<PhotoInfo> list) {
        super(context, list);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public PreviewViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = getLayoutInflater().inflate(R.layout.pp_adapter_preview_viewpgaer_item, null);
        return new PreviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PreviewViewHolder holder, int position) {
        PhotoInfo photoInfo = getDatas().get(position);
        String path = "";
        if (photoInfo != null) {
            if (photoInfo.getNetWorkUrl() != null) {
                path = photoInfo.getNetWorkUrl();
            } else {
                path = "file:///" + photoInfo.getPhotoPath();
            }

        }

        PPImageLoader.getInstance().displayAlbumImage(path, holder.mImageView);
        holder.mImageView.setRotation(angle);
    }

    public void setAngle(int angle) {
        this.angle = angle;
        notifyDataSetChanged();
    }

    static class PreviewViewHolder extends ViewHolderRecyclingPagerAdapter.ViewHolder {
        PPZoomImageView mImageView;

        public PreviewViewHolder(View view) {
            super(view);
            mImageView = (PPZoomImageView) view;
        }
    }
}
