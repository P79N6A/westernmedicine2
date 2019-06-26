package com.xywy.askforexpert.module.doctorcircle.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hyphenate.util.DensityUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.adapter.CommonViewHolder;
import com.xywy.askforexpert.appcommon.net.DoctorAPI;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.utils.DensityUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.model.doctor.CommentBean;
import com.xywy.askforexpert.model.doctor.Data;
import com.xywy.askforexpert.model.doctor.Touser;
import com.xywy.askforexpert.model.doctor.User;
import com.xywy.askforexpert.model.newdoctorcircle.PraiseResultBean;
import com.xywy.askforexpert.module.docotorcirclenew.service.DoctorCircleService;
import com.xywy.askforexpert.module.doctorcircle.AnonymousNameIntroActivity;

import net.tsz.afinal.FinalBitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * 匿名动态详情评论适配器
 *
 * @author LG
 */
public class DitaileNoNameCommlistAdapter extends BaseAdapter {

    public static String uuid;
    String TAG = "CommlistAdapter";
    private Activity context;
    private PopupWindow pp;
    private List<CommentBean> commlist;
    private Data data;
    private FinalBitmap fb;
    private List<CommentBean> commentids = new ArrayList<CommentBean>();// 评论成功回的id
    private List<String> cs = new ArrayList<String>();// 评论成功回的id
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;

    public DitaileNoNameCommlistAdapter() {

    }

    public DitaileNoNameCommlistAdapter(Activity con) {
        this.context = con;

    }

    public DitaileNoNameCommlistAdapter(Activity context2, Data data) {
        this.context = context2;
        commlist = data.commlist;
        this.data = data;
        uuid = YMApplication.getUUid();
        commentids = data.commlist;
        for (int i = 0; i < data.commlist.size(); i++) {
            cs.add(data.commlist.get(i).id);
        }

        mImageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnFail(R.drawable.icon_photo_def)
                .showImageOnLoading(R.drawable.icon_photo_def)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    public void dataNotify() {
        this.notifyDataSetChanged();
    }

    public void addData(List<CommentBean> commlist) {
        for (int i = 0; i < commlist.size(); i++) {
            if (!cs.contains(commlist.get(i).id)) {
                commentids.add(commlist.get(i));
            }
        }

        this.commlist = commentids;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return commlist.size();
    }

    @Override
    public Object getItem(int arg0) {
        return commlist.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @SuppressWarnings("unused")
    @Override
    public View getView(int position, View connView, ViewGroup arg2) {
        if (connView == null) {
            connView = LayoutInflater.from(context).inflate(R.layout.comment_list_item_layout, arg2, false);
        }

        // 评论者头像
        ImageView commentAvatar = CommonViewHolder.getView(connView, R.id.comment_avatar);
        // 评论者名字
        TextView commentName = CommonViewHolder.getView(connView, R.id.comment_name);
        // 竖直分割线
        View comment_list_portrait_divider = CommonViewHolder.getView(connView, R.id.comment_list_portrait_divider);
        // 评论者职称
        TextView commentTitle = CommonViewHolder.getView(connView, R.id.comment_list_title);
        // 评论点赞
        TextView commentPraise = CommonViewHolder.getView(connView, R.id.comment_praise_count);
        // 评论者部门
        TextView commentDep = CommonViewHolder.getView(connView, R.id.comment_list_dep);
        // 回复
        LinearLayout commentReplyLayout = CommonViewHolder.getView(connView, R.id.reply_layout);
        TextView replyTo = CommonViewHolder.getView(connView, R.id.reply_to);
        // 回复谁
        TextView commentReplyTo = CommonViewHolder.getView(connView, R.id.reply_to_name);
        // 评论内容
        TextView commentContent = CommonViewHolder.getView(connView, R.id.comment_list_detail);

        CommentBean commentBean = commlist.get(position);
        final User user = commentBean.user;
        final Touser touser = commentBean.touser;

        comment_list_portrait_divider.setVisibility(View.GONE);
        commentTitle.setVisibility(View.GONE);
        commentDep.setVisibility(View.GONE);

        mImageLoader.displayImage(user.photo == null ? "" : user.photo, commentAvatar, options);
        commentName.setText(user.nickname == null ? "我是谁" : user.nickname);
        commentName.setPadding(0, DensityUtil.px2dip(context, DensityUtils.dp2px(12)), 0, 0);
        commentPraise.setText(commentBean.getPraisecount());
        if ("1".equals(commentBean.is_praise)) {
            commentPraise.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.icon_priseed),
                    null, null, null);
        } else {
            commentPraise.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.icon_doctor_praise),
                    null, null, null);
        }
        if (touser == null || touser.nickname == null || touser.userid == null || touser.userid.equals("")) {
            commentReplyLayout.setVisibility(View.GONE);
            commentContent.setText(commentBean.getContent() == null ? "" : commentBean.getContent());
        } else {
            commentReplyLayout.setVisibility(View.VISIBLE);
            commentReplyTo.setText(touser.nickname);
            String s = replyTo.getText().toString() + touser.nickname + "：";
            String content = s + commentBean.getContent();
            SpannableString spannableString = new SpannableString(content);
            spannableString.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            commentContent.setText(spannableString);
        }

        commentAvatar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 匿名介绍
                gotoAnonymousIntro(user.nickname);
            }
        });
        commentName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAnonymousIntro(user.nickname);
            }
        });
        commentPraise.setTag(position);
        commentPraise.setOnClickListener(new MyOnclick());
        commentReplyTo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAnonymousIntro(touser.nickname);
            }
        });


        return connView;
    }

    /**
     * 转跳 匿名介绍
     */
    private void gotoAnonymousIntro(String nickname) {
        if (nickname != null) {
            StatisticalTools.eventCount(context, "yqHideDetailStatus");
            Intent intent = new Intent(context, AnonymousNameIntroActivity.class);
            intent.putExtra("anonymousName", nickname);
            context.startActivity(intent);
        }
    }

    class MyOnclick implements OnClickListener {

        private int posionItem;

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.comment_praise_count:
                    if (YMUserService.isGuest()) {
                        DoctorAPI.startLogIn(context);
                        return;
                    }

                    posionItem = (Integer) v.getTag();
                    CommentBean commentBean2 = commlist.get(posionItem);
                    String touserid = commentBean2.user.userid;

                    DoctorCircleService.praiseNoName(data.id, commentBean2.id, touserid,new CommonResponse<PraiseResultBean>() {
                        @Override
                        public void onNext(PraiseResultBean praiseResultBean) {
                            if ("0".equals(praiseResultBean.getCode())) {

                                CommentBean commentBean3 = commlist.get(posionItem);


                                if (commentBean3.is_praise.equals("1")) {//取消
                                    commentBean3.is_praise = "0";
                                    ToastUtils.shortToast("取消点赞成功");
                                    commentBean3.praisecount = "" + (Integer.parseInt(commentBean3.praisecount) - 1);
                                } else {
                                    commentBean3.is_praise = "1";
                                    ToastUtils.shortToast("点赞成功");
                                    commentBean3.praisecount = "" + (Integer.parseInt(commentBean3.praisecount) + 1);
                                }

                                DitaileNoNameCommlistAdapter.this.notifyDataSetChanged();
                            } else {
                                ToastUtils.shortToast("点赞失败");
                            }
                        }
                    });
                    break;

                default:
                    break;

            }
        }

    }

}
