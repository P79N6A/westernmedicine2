package com.xywy.askforexpert.module.discovery.answer.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;

import java.util.LinkedList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 查看图片 适配器
 * Created by bailiangjin on 16/4/18.
 */
public class ShowPicturePagerAdapter extends PagerAdapter {


    //显示的数据
    private List<String> dataList = null;

    private List<View> mViewCache = null;

    private LayoutInflater mLayoutInflater = null;
    private Activity activity;


    public ShowPicturePagerAdapter(Activity activity, List<String> dataList) {
        this.activity = activity;
        this.dataList = dataList;
        this.mLayoutInflater = LayoutInflater.from(activity);
        this.mViewCache = new LinkedList<>();
    }

    public void setDataList(List<String> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return null == dataList ? 0 : dataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View contentView = (View) object;
        container.removeView(contentView);
        this.mViewCache.add(contentView);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final String picurl = dataList.get(position);
        ViewHolder viewHolder = null;
        View convertView = null;
        if (mViewCache.size() == 0) {
            convertView = mLayoutInflater.inflate(R.layout.item_for_show_picture_viewpager, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            convertView = (View) ((LinkedList)mViewCache).removeFirst();
            viewHolder = (ViewHolder) convertView.getTag();
        }

        updateData(picurl, viewHolder, convertView);

        container.addView(convertView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return convertView;
    }

    /**
     * 更新数据
     *
     * @param picUrl
     * @param viewHolder
     * @param convertView
     */
    private void updateData(final String picUrl, final ViewHolder viewHolder, View convertView) {
        ImageLoadUtils.INSTANCE.loadImageView(viewHolder.pv_picture, picUrl,new ImageLoadingListener(){

            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (loadedImage.getWidth() < 600 && loadedImage.getHeight() > 600) {// 窄长的图片  放大
                    ((PhotoView) view).setScaleType(ImageView.ScaleType.CENTER_CROP);
                } else {//正常显示
                    ((PhotoView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public final class ViewHolder {

        public final View root;
        public final PhotoView pv_picture;

        public ViewHolder(View root) {
            this.root = root;
            pv_picture = (PhotoView) root.findViewById(R.id.pv_picture);
            pv_picture.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                @Override
                public void onViewTap(View view, float v, float v1) {
                    activity.finish();
                }
            });
        }
    }


}
