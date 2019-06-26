package com.xywy.askforexpert.module.docotorcirclenew.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseAdapter;
import com.xywy.askforexpert.appcommon.base.adapter.CommonViewHolder;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.model.doctor.CommentBean;
import com.xywy.askforexpert.model.doctor.Touser;
import com.xywy.askforexpert.model.doctor.User;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.PraiseParamBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.type.DCMsgType;
import com.xywy.askforexpert.module.docotorcirclenew.service.DcCommonClickService;
import com.xywy.askforexpert.module.main.service.person.PersonDetailActivity;
import com.xywy.askforexpert.module.message.usermsg.DiscussSettingsActivity;

import java.util.List;


/**
 * 动态详情评论适配器
 *
 * @author LG
 * @modified Jack Fang
 * <p>
 * blj 重构
 */
public class DetailCommentListAdapter extends YMBaseAdapter<CommentBean> {

    /**
     * 是否为实名 实名显示 职位 点击头像跳转 匿名 不显示职位 无点击事件
     */
    private boolean isRealName;
    private String msgId;

    DcCommonClickService clickService;


    public DetailCommentListAdapter(Activity activity, List<CommentBean> dataList, String msgId, boolean isRealName) {
        super(activity, dataList);
        this.isRealName = isRealName;
        this.msgId = msgId;
        clickService = new DcCommonClickService();
    }


    @Override
    public int getItemLayoutResId() {
        return R.layout.comment_list_item_layout;
    }

    @Override
    protected BaseViewHolder getViewHolder(View rootView) {
        return new ViewHolder(rootView);
    }

    public void addDataList(List<CommentBean> commlist) {
        addData(commlist);
        notifyDataSetChanged();
    }

    public void addData(CommentBean commentBean) {
        dataList.add(commentBean);
        notifyDataSetChanged();
    }

    public void removeData(CommentBean commentBean) {
        super.removeData(commentBean);
        notifyDataSetChanged();
    }

    public void removeData(String commentId) {
        CommentBean tempItem = null;
        for (CommentBean item : dataList) {
            if (commentId.equals(item.getId())) {
                tempItem = item;
            }
        }

        if (null != tempItem) {
            dataList.remove(tempItem);
            notifyDataSetChanged();
        }

    }


    public class ViewHolder extends BaseViewHolder<CommentBean> {

        // 评论者头像
        final ImageView commentAvatar;
        final TextView commentName;
        // 竖直分割线
        final View comment_list_portrait_divider;
        // 评论者职称
        final TextView commentTitle;
        // 评论点赞数
        final TextView tv_comment_praise_count;
        // 评论者部门
        final TextView commentDep;
        // 回复布局
        final LinearLayout commentReplyLayout;
        // 回复
        final TextView replyTo;
        // 回复谁
        final TextView commentReplyTo;
        // 评论内容
        final TextView commentContent;


        public ViewHolder(View convertView) {
            super(convertView);
            // 评论者头像
            commentAvatar = CommonViewHolder.getView(convertView, R.id.comment_avatar);
            // 评论者名字
            commentName = CommonViewHolder.getView(convertView, R.id.comment_name);
            // 竖直分割线
            comment_list_portrait_divider = CommonViewHolder.getView(convertView, R.id.comment_list_portrait_divider);
            // 评论者职称
            commentTitle = CommonViewHolder.getView(convertView, R.id.comment_list_title);
            // 评论点赞
            // 评论点赞数
            tv_comment_praise_count = CommonViewHolder.getView(convertView, R.id.comment_praise_count);
            // 评论者部门
            commentDep = CommonViewHolder.getView(convertView, R.id.comment_list_dep);
            // 回复布局
            commentReplyLayout = CommonViewHolder.getView(convertView, R.id.reply_layout);
            // 回复
            replyTo = CommonViewHolder.getView(convertView, R.id.reply_to);
            // 回复谁
            commentReplyTo = CommonViewHolder.getView(convertView, R.id.reply_to_name);
            // 评论内容
            commentContent = CommonViewHolder.getView(convertView, R.id.comment_list_detail);

        }

        @Override
        public void show(int position, final CommentBean commentBean) {
            final User user = commentBean.user;
            final Touser toUser = commentBean.touser;

            ImageLoadUtils.INSTANCE.loadImageView(commentAvatar, user.photo == null ? "" : user.photo);
            commentName.setText(user.nickname == null ? "我是谁" : user.nickname);
            if (!isRealName || TextUtils.isEmpty(user.job)) {
                commentTitle.setVisibility(View.GONE);
                comment_list_portrait_divider.setVisibility(View.GONE);
            } else {
                commentTitle.setVisibility(View.VISIBLE);
                comment_list_portrait_divider.setVisibility(View.VISIBLE);
                commentTitle.setText(user.job);
            }

            if (!isRealName || TextUtils.isEmpty(user.department)) {
                commentDep.setVisibility(View.GONE);
            } else {
                commentDep.setText(user.department);
            }
            if (toUser == null || toUser.nickname == null || toUser.userid == null || toUser.userid.equals("")) {
                commentReplyLayout.setVisibility(View.GONE);
                commentContent.setText(commentBean.getContent() == null ? "" : commentBean.getContent());
            } else {
                commentReplyLayout.setVisibility(View.VISIBLE);
                commentReplyTo.setText(toUser.nickname);
                String s = replyTo.getText().toString() + toUser.nickname + "：";
                String content = s + commentBean.getContent();
                SpannableString spannableString = new SpannableString(content);
                spannableString.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                commentContent.setText(spannableString);
            }

            if (isRealName) {
                commentAvatar.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 个人主页
                        Intent intent;
                        if (user.getUserType() != null && user.getUserType().equals("seminar")) {
                            intent = new Intent(context, DiscussSettingsActivity.class);
                        } else {
                            intent = new Intent(context, PersonDetailActivity.class);
                        }
                        intent.putExtra("uuid", user.getUserid());
                        intent.putExtra("isDoctor", user.is_doctor);
                        context.startActivity(intent);
                    }
                });
                commentName.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent;
                        if (user.getUserType() != null && user.getUserType().equals("seminar")) {
                            intent = new Intent(context, DiscussSettingsActivity.class);
                        } else {
                            intent = new Intent(context, PersonDetailActivity.class);
                        }
                        intent.putExtra("uuid", user.getUserid());
                        intent.putExtra("isDoctor", user.is_doctor);
                        context.startActivity(intent);
                    }
                });

                commentReplyTo.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent;
                        if (user.getUserType() != null && user.getUserType().equals("seminar")) {
                            intent = new Intent(context, DiscussSettingsActivity.class);
                        } else {
                            intent = new Intent(context, PersonDetailActivity.class);
                        }
                        intent.putExtra("uuid", toUser.userid);
                        intent.putExtra("isDoctor", "");
                        context.startActivity(intent);
                    }
                });
            } else {
                tv_comment_praise_count.setVisibility(View.VISIBLE);
                tv_comment_praise_count.setText(commentBean.getPraisecount());
                tv_comment_praise_count.setSelected("1".equals(commentBean.is_praise));
                if (!TextUtils.isEmpty(msgId)) {


                    tv_comment_praise_count.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //判断认证 stone
                            DialogUtil.showUserCenterCertifyDialog(context, new MyCallBack() {
                                @Override
                                public void onClick(Object data) {
                                    clickService.onPraiseClick(new PraiseParamBean(msgId, commentBean.getId(), commentBean.getUser().getUserid(), DCMsgType.NO_NAME, new CommonResponse() {
                                        @Override
                                        public void onNext(Object o) {
                                            int curPraiseCount = TextUtils.isEmpty(commentBean.getPraisecount()) ? 0 : Integer.parseInt(commentBean.getPraisecount());
                                            if ("1".equals(commentBean.is_praise)) {
                                                commentBean.setIs_praise("0");
                                                curPraiseCount = curPraiseCount - 1;
                                            } else {
                                                commentBean.setIs_praise("1");
                                                curPraiseCount = curPraiseCount + 1;
                                            }

                                            curPraiseCount = curPraiseCount < 0 ? 0 : curPraiseCount;
                                            commentBean.setPraisecount("" + curPraiseCount);

                                            notifyDataSetChanged();

                                        }
                                    }));
                                }
                            }, null, null);


                        }
                    });

                }
            }

        }
    }
}
