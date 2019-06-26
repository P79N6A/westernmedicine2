package com.xywy.askforexpert.module.main.media.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.media.MediaList;
import com.xywy.askforexpert.module.main.media.MediaDetailActivity;
import com.xywy.askforexpert.module.main.service.media.InfoDetailActivity;
import com.xywy.askforexpert.module.main.subscribe.video.VideoNewsActivity;
import com.xywy.askforexpert.widget.view.MyListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by shihao on 16/3/16.
 */
public class SubMediaAdapter extends BaseAdapter {

    private Context context;

    private List<MediaList.DataEntity> dataEntityList;

    private List<MediaList.DataEntity.ListEntity> childDataEntityList = new ArrayList<>();

    private DisplayImageOptions displayImageOptions;

    private ImageLoader imageLoader;

    private boolean isScroll = false;

    private boolean removeState = false;

    public SubMediaAdapter(Context context, List<MediaList.DataEntity> dataEntityList) {
        this.context = context;
        this.dataEntityList = dataEntityList;
        displayImageOptions = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .showStubImage(R.drawable.img_default_bg)
                .showImageForEmptyUri(R.drawable.img_default_bg)
                .showImageOnFail(R.drawable.img_default_bg).cacheInMemory(true)
                .cacheOnDisc(true).build();

        imageLoader = ImageLoader.getInstance();
    }

    public void setIsScroll(boolean isScroll) {
        this.isScroll = isScroll;
    }

    public void bindData(List<MediaList.DataEntity> dataEntityList) {
        this.dataEntityList = dataEntityList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataEntityList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataEntityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_sub_media_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        try {
            DLog.i("media", "titile==" + dataEntityList.get(position).getName());
            viewHolder.submediaTitleTv.setText(dataEntityList.get(position).getName());
            childDataEntityList = dataEntityList.get(position).getList();
            if (dataEntityList.get(position).getList().size() > 0) {
                if (dataEntityList.get(position).getList().get(0) != null) {
                    final MediaList.DataEntity.ListEntity firstListEntity = dataEntityList.get(position).getList().get(0);
                    imageLoader.displayImage(firstListEntity.getImg(), viewHolder.submediaImgIv, displayImageOptions);
                    viewHolder.submediaImgTv.setText(firstListEntity.getTitle());
                    viewHolder.submediaImgRl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String uu = firstListEntity.getUu();
                            String vu = firstListEntity.getVu();
                            DLog.d("media uu vu", "media uu = " + uu + ", vu = " + vu);
                            if (uu != null && vu != null && !"".equals(uu) && !"".equals(vu)) {
                                toVideoNews(firstListEntity.getId(), uu, vu, firstListEntity.getTitle());
                            } else {
                                goToInfoDetail(firstListEntity.getUrl(), firstListEntity.getId(),
                                        firstListEntity.getTitle(), firstListEntity.getImg());
                            }
                        }
                    });
                }
                if ((dataEntityList.get(position).getList().size() - 1) == 0) {
                    viewHolder.showHeight.setVisibility(View.VISIBLE);
                    viewHolder.submediaLv.setVisibility(View.GONE);
                } else {
                    viewHolder.submediaLv.setVisibility(View.VISIBLE);
                    viewHolder.showHeight.setVisibility(View.GONE);
                    viewHolder.submediaLv.setAdapter(new ItemChildAdapter(context, childDataEntityList));
                    viewHolder.submediaLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int childPostion, long id) {

                            MediaList.DataEntity.ListEntity item = (MediaList.DataEntity.ListEntity) parent.getAdapter().getItem(childPostion);
                            DLog.i("media ", "child POSITION" + childPostion + item.getUrl());
                            try {
                                String uu = item.getUu();
                                String vu = item.getVu();
                                DLog.d("media uu vu", "media uu = " + uu + ", vu = " + vu);
                                if (uu != null && vu != null && !"".equals(uu) && !"".equals(vu)) {
                                    toVideoNews(item.getId(), uu, vu, item.getTitle());
                                } else {
                                    goToInfoDetail(item.getUrl(), item.getId(), item.getTitle(), item.getImg());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }

            }

            viewHolder.submediaTitleTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MediaDetailActivity.class);
                    intent.putExtra("mediaId", dataEntityList.get(position).getId());
                    context.startActivity(intent);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private void goToInfoDetail(String url, String id, String title, String imageUrl) {
        Intent intent = new Intent(context, InfoDetailActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("ids", id);
        intent.putExtra("title", title);
        intent.putExtra("imageurl", imageUrl);
        intent.putExtra("model", 2);
        context.startActivity(intent);
    }

    private void toVideoNews(String id, String uu, String vu, String title) {
        Intent intent = new Intent(context, VideoNewsActivity.class);
        intent.putExtra(VideoNewsActivity.NEWS_ID_INTENT_KEY, id);
        intent.putExtra(VideoNewsActivity.VIDEO_UUID_INTENT_KEY, uu);
        intent.putExtra(VideoNewsActivity.VIDEO_VUID_INTENT_KEY, vu);
        intent.putExtra(VideoNewsActivity.VIDEO_TITLE_INTENT_KEY, title);
        context.startActivity(intent);
    }

    static class ViewHolder {
        @Bind(R.id.submedia_title_tv)
        TextView submediaTitleTv;
        @Bind(R.id.submedia_img_iv)
        ImageView submediaImgIv;
        @Bind(R.id.submedia_img_tv)
        TextView submediaImgTv;
        @Bind(R.id.submedia_img_rl)
        RelativeLayout submediaImgRl;
        @Bind(R.id.submedia_lv)
        MyListView submediaLv;
        @Bind(R.id.submedia_ll)
        LinearLayout submediaLl;
        @Bind(R.id.show_Height)
        View showHeight;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class ItemChildAdapter extends BaseAdapter {

        private Context context;

        private List<MediaList.DataEntity.ListEntity> listEntities;

        public ItemChildAdapter(Context context, List<MediaList.DataEntity.ListEntity> dataEntityList) {
            this.context = context;

            this.listEntities = dataEntityList;


        }

        @Override
        public int getCount() {
            return this.listEntities.size() - 1;
        }

        @Override
        public MediaList.DataEntity.ListEntity getItem(int position) {
            return this.listEntities.get(position + 1);
        }

        @Override
        public long getItemId(int position) {
            return position + 1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewChildHolder childHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.adapter_child_media_item, null);
                childHolder = new ViewChildHolder(convertView);
                convertView.setTag(childHolder);
            } else {
                childHolder = (ViewChildHolder) convertView.getTag();
            }
            DLog.i("media", "ddddd" + this.listEntities.get(position + 1).getTitle() + "size==" + this.listEntities.size());
            imageLoader.displayImage(this.listEntities.get(position + 1).getImg(), childHolder.childMediaIv, displayImageOptions);
            childHolder.childMediaTv.setText(this.listEntities.get(position + 1).getTitle());

            return convertView;
        }

        class ViewChildHolder {
            @Bind(R.id.child_media_iv)
            ImageView childMediaIv;
            @Bind(R.id.child_media_tv)
            TextView childMediaTv;

            ViewChildHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
