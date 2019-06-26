package com.xywy.askforexpert.module.doctorcircle.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.adapter.CommonBaseAdapter;
import com.xywy.askforexpert.appcommon.base.adapter.CommonViewHolder;
import com.xywy.askforexpert.model.discussDetail.DiscussCommentList;
import com.xywy.askforexpert.model.doctor.Touser;
import com.xywy.askforexpert.model.doctor.User;
import com.xywy.askforexpert.module.main.service.person.PersonDetailActivity;
import com.xywy.askforexpert.module.message.usermsg.DiscussSettingsActivity;

import java.util.List;

/**
 * 评论列表 适配器
 * <p>
 * Compiler: Android Studio
 * Project: D_Platform
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2015/12/21 16:56
 */
public class CommentListAdapter extends CommonBaseAdapter<DiscussCommentList> {

    private final DisplayImageOptions OPTIONS;

    private List<DiscussCommentList> mDatas;

    public CommentListAdapter(Context mContext, List<DiscussCommentList> mDatas, int mItemLayoutId) {
        super(mContext, mDatas, mItemLayoutId);

        this.mDatas = mDatas;

        OPTIONS = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnFail(R.drawable.icon_photo_def)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.drawable.icon_photo_def)
                .build();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(mItemLayoutId, parent, false);
        }

        // 评论者头像
        ImageView commentAvatar = CommonViewHolder.getView(convertView, R.id.comment_avatar);
        // 评论者姓名
        TextView commentName = CommonViewHolder.getView(convertView, R.id.comment_name);
        // 评论者职称
        TextView commentTitle = CommonViewHolder.getView(convertView, R.id.comment_list_title);
        // 评论者部门
        TextView commentDep = CommonViewHolder.getView(convertView, R.id.comment_list_dep);
        // 评论内容
        TextView commentDetail = CommonViewHolder.getView(convertView, R.id.comment_list_detail);
        // 垂直分割线
        View dividerLine = CommonViewHolder.getView(convertView, R.id.comment_list_portrait_divider);
        // 回复布局
        LinearLayout replyLayout = CommonViewHolder.getView(convertView, R.id.reply_layout);
        TextView replyTo = CommonViewHolder.getView(convertView, R.id.reply_to);
        // 回复给谁
        TextView replyToName = CommonViewHolder.getView(convertView, R.id.reply_to_name);
        // 评论点赞
        TextView commentPraise = CommonViewHolder.getView(convertView, R.id.comment_praise_count);

        commentPraise.setVisibility(View.GONE);

        final User user = mDatas.get(position).getUser();
        final Touser touser = mDatas.get(position).getTouser();

        mImageLoader.displayImage(user.photo == null ? "" : user.photo, commentAvatar, OPTIONS);
        commentName.setText(user.nickname == null ? "我是谁" : user.nickname);
        if (user.job == null || user.job.equals("")) {
            commentTitle.setVisibility(View.GONE);
            dividerLine.setVisibility(View.GONE);
        } else {
            commentTitle.setVisibility(View.VISIBLE);
            dividerLine.setVisibility(View.VISIBLE);
            commentTitle.setText(user.job);
        }
        if (user.department == null || user.department.equals("")) {
            commentDep.setVisibility(View.GONE);
        } else {
            commentDep.setVisibility(View.VISIBLE);
            commentDep.setText(user.department);
        }

        if (touser == null || touser.nickname == null || touser.userid == null || touser.userid.equals("")) {
            replyLayout.setVisibility(View.GONE);
            commentDetail.setText(mDatas.get(position).getContent() == null ? "" : mDatas.get(position).getContent());
        } else {
            replyLayout.setVisibility(View.VISIBLE);
            replyToName.setText(touser.nickname);
            String s = replyTo.getText().toString() + touser.nickname + "：";
            String content = s + mDatas.get(position).getContent();
            SpannableString spannableString = new SpannableString(content);
            spannableString.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, s.length()
                    , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            commentDetail.setText(spannableString);
        }

        commentAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 个人主页
                Intent intent;
                if (user.getUserType() != null && user.getUserType().equals("seminar")) {
                    intent = new Intent(mContext, DiscussSettingsActivity.class);
                } else {
                    intent = new Intent(mContext, PersonDetailActivity.class);
                }
                intent.putExtra("uuid", user.getUserid());
                intent.putExtra("isDoctor", "");
                mContext.startActivity(intent);
            }
        });

        commentName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 个人主页
                Intent intent;
                if (user.getUserType() != null && user.getUserType().equals("seminar")) {
                    intent = new Intent(mContext, DiscussSettingsActivity.class);
                } else {
                    intent = new Intent(mContext, PersonDetailActivity.class);
                }
                intent.putExtra("uuid", user.getUserid());
                intent.putExtra("isDoctor", "");
                mContext.startActivity(intent);
            }
        });

        if (touser != null) {
            replyToName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 个人主页
                    Intent intent;
                    if (user.getUserType() != null && user.getUserType().equals("seminar")) {
                        intent = new Intent(mContext, DiscussSettingsActivity.class);
                    } else {
                        intent = new Intent(mContext, PersonDetailActivity.class);
                    }
                    intent.putExtra("uuid", touser.userid);
                    intent.putExtra("isDoctor", "");
                    mContext.startActivity(intent);
                }
            });
        }

        return convertView;
    }
}
