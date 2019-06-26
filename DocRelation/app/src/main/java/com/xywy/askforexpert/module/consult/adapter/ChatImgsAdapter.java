package com.xywy.askforexpert.module.consult.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.component.uimodules.photoPicker.PhotoPreviewActivity;
import com.xywy.component.uimodules.photoPicker.model.PhotoInfo;
import com.xywy.uilibrary.recyclerview.adapter.XYWYRVSingleTypeBaseAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzheng on 2017/5/6.
 */

public class ChatImgsAdapter extends XYWYRVSingleTypeBaseAdapter<String> {

    //stone
    private ArrayList<PhotoInfo> photoInfos;
    private boolean mIsFromWTKOrHistory;
    private int msg_type;


    public ChatImgsAdapter(int msg_type, boolean mIsFromWTKOrHistory, Context context) {
        super(context);
        this.mIsFromWTKOrHistory = mIsFromWTKOrHistory;
        this.msg_type = msg_type;
    }

    @Override
    public void setData(List<String> dataList) {
        super.setData(dataList);
        photoInfos = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
            PhotoInfo info = new PhotoInfo();
            info.setNetWorkUrl(mDatas.get(i));
            photoInfos.add(info);
        }
    }

    @Override
    protected int getItemLayoutResId() {
        return R.layout.item_chat_imgs;
    }

    @Override
    protected void convert(ViewHolder holder, final String item, final int position) {
//        ImFileCacheUtil imFileCacheUtil = ImFileCacheUtil.getInstance();
//        File file = new File(item);
//        String fileName = file.getName();
//        boolean imageCache = imFileCacheUtil.imageCache(fileName);
//        if (mIsFromWTKOrHistory) {
//            XYImageLoader.getInstance().displayImage(item, (ImageView) holder.getView(R.id.iv_img));
//        }else{
//            if (imageCache) {
//                File imageFile = new File(imFileCacheUtil.getImageCache(fileName));
//                if (imFileCacheUtil.map.get(fileName)) {
//                    XYImageLoader.getInstance().displayImage(ImageDownloader.Scheme.FILE.wrap(imageFile.getAbsolutePath()),
//                            (ImageView) holder.getView(R.id.iv_img));
//                }else{
//                    XYImageLoader.getInstance().displayImage(item, (ImageView) holder.getView(R.id.iv_img));
//                }
//            } else {
//                XYImageLoader.getInstance().displayImage(item, (ImageView) holder.getView(R.id.iv_img));
//                imFileCacheUtil.downloadImageFile(item, fileName);
//            }
//
//        }
        final ImageView imageView  = (ImageView) holder.getView(R.id.iv_img);
//        if (msg_type == ConsultChatEntity.MSG_TYPE_RECV) {
//            imageView.setScaleType(ImageView.ScaleType.FIT_START);
//        }else{
//            imageView.setScaleType(ImageView.ScaleType.FIT_END);
//        }
        Glide.with(YMApplication.getAppContext()).load(item).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                imageView.setImageDrawable(resource);
                return false;
            }
        }).error(R.drawable.icon_photo_def).
                placeholder(R.drawable.icon_photo_def).into(imageView);
//        XYImageLoader.getInstance().displayImage(item, (SquareImageView) holder.getView(R.id.iv_img));

        holder.getView(R.id.iv_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPreviewActivity.startActivity(mContext, photoInfos, position);
            }
        });
    }

}
