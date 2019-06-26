package com.xywy.askforexpert.module.my.photo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.ScreenUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.widget.SDCardImageLoader;

import net.tsz.afinal.FinalBitmap;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;

/**
 * 根据图片地址加载页面
 *
 * @author 王鹏
 * @2015-4-29上午11:06:48
 */
public class ImageDetailFragment extends Fragment {
    private String mImageUrl;
    private PhotoView mImageView;
    private ProgressBar progressBar;
    private SDCardImageLoader loader;
    private PhotoViewAttacher mAttacher;
    private FinalBitmap fb;
    Bitmap bm;

    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();

        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url")
                : null;
        loader = new SDCardImageLoader(ScreenUtils.getScreenW(),
                ScreenUtils.getScreenH());
//		distoryBitmap();
        fb = FinalBitmap.create(getActivity(), false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.image_detail_fragment,
                container, false);
        mImageView = (PhotoView) v.findViewById(R.id.image);
//		mAttacher = new PhotoViewAttacher(mImageView);
        mImageView.setOnPhotoTapListener(new OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2) {
                getActivity().finish();
            }
        });
        progressBar = (ProgressBar) v.findViewById(R.id.loading);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // new VolleyTools(getActivity()).downloadImage(mImageView, mImageUrl);

//		bm = loader.createBitmap(mImageUrl);
//		mImageView.setImageBitmap(bm);
        if (mImageUrl != null) {
            mImageUrl = mImageUrl.replace("100_100_", "");
            fb.display(mImageView, mImageUrl);
            fb.configLoadfailImage(R.drawable.img_default_bg);
            fb.configLoadingImage(R.drawable.img_default_bg);
        } else {
            mImageView.setImageResource(R.drawable.img_default_bg);
        }
    }

    /**
     * 图片内存回收
     */
    public void distoryBitmap() {
        if (null != bm && !bm.isRecycled()) {
            bm.recycle();
        }
//        System.gc();
    }

    @Override
    public void onDestroy() {
        if (null != bm && !bm.isRecycled()) {
            bm.recycle();
//            System.gc();
        }

        super.onDestroy();
    }

    public void onResume() {
        super.onResume();
        StatisticalTools.fragmentOnResume("ImageDetailFragment");
    }

    public void onPause() {
        super.onPause();
        StatisticalTools.fragmentOnPause("ImageDetailFragment");
    }

}
