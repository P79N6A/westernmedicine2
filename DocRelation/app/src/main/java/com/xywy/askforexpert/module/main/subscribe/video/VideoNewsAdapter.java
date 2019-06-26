package com.xywy.askforexpert.module.main.subscribe.video;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.adapter.BaseRecyclerViewAdapter;
import com.xywy.askforexpert.model.videoNews.VideoNewsBean;
import com.xywy.askforexpert.module.doctorcircle.AnonymousNameIntroActivity;
import com.xywy.askforexpert.module.main.service.person.PersonDetailActivity;

import java.util.List;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/6/23 10:10
 */
public class VideoNewsAdapter extends BaseRecyclerViewAdapter<VideoNewsBean.CommentBean> implements View.OnClickListener {
    private static final String TAG = "VideoNewsAdapter";
    private final DisplayImageOptions mOptions;

    public VideoNewsAdapter(Context mContext, List<VideoNewsBean.CommentBean> mDatas,
                            int mItemLayoutId, RecyclerView recyclerView) {
        super(mContext, mDatas, mItemLayoutId, recyclerView);

        mOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true)
                .showImageOnLoading(R.drawable.icon_photo_def)
                .showImageOnFail(R.drawable.icon_photo_def)
                .showImageForEmptyUri(R.drawable.icon_photo_def)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(mItemLayoutId, parent, false);
        return new Holder(view);
    }

    @Override
    protected void onBind(RecyclerView.ViewHolder holder, int pos, VideoNewsBean.CommentBean data) {
        if (holder instanceof Holder) {
            if (data != null) {
                // 绑定数据
                Holder h = (Holder) holder;
                ImageLoader.getInstance().displayImage(data.getPhoto() == null ? "" : data.getPhoto(),
                        h.videoNewsCommentAvatar, mOptions);
                h.videoNewsCommentName.setText(data.getNickname() == null ? "" : data.getNickname());
                h.videoNewsCommentHos.setText(data.getHospital() == null ? "" : data.getHospital());
                h.videoNewsCommentDept.setText(data.getSubject() == null ? "" : data.getSubject());
                h.videoNewsCommentTime.setText(data.getCreatetime() == null ? "" : data.getCreatetime());
                h.videoNewsCommentContent.setText(data.getContent() == null ? "" : data.getContent());

                h.videoNewsCommentAvatar.setTag(pos);
                h.videoNewsCommentName.setTag(pos);
                h.videoNewsCommentAvatar.setOnClickListener(this);
                h.videoNewsCommentName.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag();
        switch (v.getId()) {
            case R.id.video_news_comment_item_avatar:
            case R.id.video_news_comment_item_name:
                VideoNewsBean.CommentBean bean = mDatas.get(pos);
                String level = bean.getLevel();
                Intent intent = new Intent();
                if (level.equals("0")) {
                    intent.setClass(mContext, PersonDetailActivity.class);
                    intent.putExtra("uuid", bean.getUserid());
                } else if (level.equals("2")) {
                    intent.setClass(mContext, AnonymousNameIntroActivity.class);
                    intent.putExtra("anonymousName", bean.getNickname());
                    intent.putExtra("type", 1);
                }
                mContext.startActivity(intent);
                break;
        }
    }

    class Holder extends RecyclerView.ViewHolder {

        ImageView videoNewsCommentAvatar;
        TextView videoNewsCommentName;
        TextView videoNewsCommentHos;
        TextView videoNewsCommentDept;
        TextView videoNewsCommentTime;
        TextView videoNewsCommentContent;

        public Holder(View itemView) {
            super(itemView);

            videoNewsCommentAvatar = (ImageView) itemView.findViewById(R.id.video_news_comment_item_avatar);
            videoNewsCommentName = (TextView) itemView.findViewById(R.id.video_news_comment_item_name);
            videoNewsCommentHos = (TextView) itemView.findViewById(R.id.video_news_comment_item_hospital);
            videoNewsCommentDept = (TextView) itemView.findViewById(R.id.video_news_comment_item_dept);
            videoNewsCommentTime = (TextView) itemView.findViewById(R.id.video_news_comment_item_time);
            videoNewsCommentContent = (TextView) itemView.findViewById(R.id.video_news_comment_item_content);
        }
    }
}
