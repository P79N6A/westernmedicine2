package com.xywy.askforexpert.module.docotorcirclenew.adapter.viewholder;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.DensityUtils;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMDebugOnClickListener;
import com.xywy.askforexpert.model.doctor.CommentBean;
import com.xywy.askforexpert.model.doctor.PraiseBean;
import com.xywy.askforexpert.model.doctor.RealNameItem;
import com.xywy.askforexpert.module.docotorcirclenew.model.PublishType;
import com.xywy.askforexpert.module.docotorcirclenew.utils.RichTextUtils;
import com.xywy.askforexpert.module.doctorcircle.SeePicActivty;
import com.xywy.askforexpert.module.doctorcircle.adapter.CommlistAdapter;
import com.xywy.askforexpert.module.doctorcircle.adapter.DoctorPicsAdapter;
import com.xywy.askforexpert.module.main.service.person.PersonDetailActivity;
import com.xywy.askforexpert.widget.ExpandableTextView;
import com.xywy.askforexpert.widget.view.FlowRadioGroup;
import com.xywy.askforexpert.widget.view.MyGridView;
import com.xywy.askforexpert.widget.view.MyListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/12/1 16:37
 */
public class ContentRecycleViewHolder extends BaseRecycleViewHolder<RealNameItem> {
    private View.OnClickListener uiListener;
    ImageView iv_usrer_pic, iv_praise, iv_discuss_pic, iv_doctor_share_pic, iv_share_poto;
    TextView tv_username, tv_user_time, tv_read_morme_content, tv_doctor, subject,
            tv_praise_num, tv_discuss_num, tv_all_discusss,
            tv_doctor_share_num;//tv_add_friend;
    LinearLayout ll_content_ditaile, ll_user_message_detail, ll_share;
    LinearLayout ll_praise, ll_discuss, ll_doctor_share;
    MyGridView gv_pics;
    View line, line_v;
    MyListView lv_praiselist;
    FlowRadioGroup FlowLayout;
    //        RelativeLayout all_discusss_rl;
    RelativeLayout re_praise_do;
    ExpandableTextView mExpandableTextView;
    TextView fullText;
    TextView expandable_text, tv_share_title;
    ViewGroup expandView;
    RelativeLayout disscuss_arrow_rl, iv_add;

    public ContentRecycleViewHolder(View.OnClickListener uiListener, View rootView) {
        super(rootView);
        this.uiListener = uiListener;
        mExpandableTextView = (ExpandableTextView) rootView.findViewById(R.id.post_content);
        expandable_text = (TextView) rootView.findViewById(R.id.expandable_text);
        expandView = (ViewGroup) rootView.findViewById(R.id.expand_collapse_view_group);
        fullText = (TextView) rootView.findViewById(R.id.full_text);
        iv_usrer_pic = (ImageView) rootView.findViewById(R.id.iv_usrer_pic);
        tv_username = (TextView) rootView.findViewById(R.id.tv_username);
        tv_user_time = (TextView) rootView.findViewById(R.id.tv_user_time);
        re_praise_do = (RelativeLayout) rootView.findViewById(R.id.re_praise_do);
        iv_add = (RelativeLayout) rootView.findViewById(R.id.iv_add);
        ll_praise = (LinearLayout) rootView.findViewById(R.id.ll_praise);
        iv_praise = (ImageView) rootView.findViewById(R.id.iv_praise);
        tv_praise_num = (TextView) rootView.findViewById(R.id.tv_praise_num);

        tv_doctor = (TextView) rootView.findViewById(R.id.tv_doctor);
        ll_discuss = (LinearLayout) rootView.findViewById(R.id.ll_discuss);
        iv_discuss_pic = (ImageView) rootView.findViewById(R.id.iv_discuss_pic);
        tv_discuss_num = (TextView) rootView.findViewById(R.id.tv_discuss_num);
        tv_all_discusss = (TextView) rootView.findViewById(R.id.tv_all_discusss);
//            ll_content_ditaile = (LinearLayout) rootView.findViewById(R.id.ll_content_ditaile);
        ll_doctor_share = (LinearLayout) rootView.findViewById(R.id.ll_doctor_share);
        iv_doctor_share_pic = (ImageView) rootView.findViewById(R.id.iv_doctor_share_pic);
        tv_doctor_share_num = (TextView) rootView.findViewById(R.id.tv_doctor_share_num);
//            iv_delete = (RelativeLayout) rootView.findViewById(R.id.iv_delete);
        iv_add = (RelativeLayout) rootView.findViewById(R.id.iv_add);
        FlowLayout = (FlowRadioGroup) rootView.findViewById(R.id.FlowLayout);
        line = rootView.findViewById(R.id.line);
        lv_praiselist = (MyListView) rootView.findViewById(R.id.lv_praiselist);
        subject = (TextView) rootView.findViewById(R.id.subject);
        gv_pics = (MyGridView) rootView.findViewById(R.id.gv_pics);
//            tv_add_friend = (TextView) rootView.findViewById(R.id.tv_add_friend);
        //分享
        tv_share_title = (TextView) rootView.findViewById(R.id.tv_share_title);
        iv_share_poto = (ImageView) rootView.findViewById(R.id.iv_share_poto);
        ll_share = (LinearLayout) rootView.findViewById(R.id.ll_share);
        disscuss_arrow_rl = (RelativeLayout) rootView.findViewById(R.id.disscuss_arrow_rl);

        line_v = rootView.findViewById(R.id.line_v);
    }

    public void updateView(final Context context, final RealNameItem mRealNameItem) {
        super.updateView(context, mRealNameItem);
        //分享过来
        if ("3".equals(mRealNameItem.source)) {
            ll_share.setVisibility(View.VISIBLE);
            if (mRealNameItem.getShare() != null) {
                tv_share_title.setText(mRealNameItem.getShare().share_title);
                ImageLoadUtils.INSTANCE.loadImageView(iv_share_poto, mRealNameItem.getShare().share_img);
            }
        } else {
            ll_share.setVisibility(View.GONE);
        }

        if (mRealNameItem.user != null && mRealNameItem.user.job.equals("")) {
            line_v.setVisibility(View.GONE);
        } else {
            line_v.setVisibility(View.VISIBLE);
        }

        if (mRealNameItem.minimgs != null) {
            // // gridview 显示几列
            if (mRealNameItem.minimgs.size() == 1) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(DensityUtils.dp2px(130), ViewGroup.LayoutParams.WRAP_CONTENT));
                lp.leftMargin = DensityUtils.dp2px(10);
                lp.rightMargin = DensityUtils.dp2px(16);
                lp.topMargin = DensityUtils.dp2px(5);
                gv_pics.setLayoutParams(lp);
                gv_pics.setNumColumns(1);
                gv_pics.setColumnWidth(DensityUtils.dp2px(130));
            } else if (mRealNameItem.minimgs.size() == 4 || mRealNameItem.minimgs.size() == 2) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(DensityUtils.dp2px(260), ViewGroup.LayoutParams.WRAP_CONTENT));
                lp.leftMargin = DensityUtils.dp2px(10);
                lp.rightMargin = DensityUtils.dp2px(16);
                lp.topMargin = DensityUtils.dp2px(5);
                gv_pics.setLayoutParams(lp);
                gv_pics.setColumnWidth(DensityUtils.dp2px(130));
                gv_pics.setNumColumns(2);

            } else {
                int width = AppUtils.getScreenWidth(context);

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width - DensityUtils.dp2px(80), ViewGroup.LayoutParams.WRAP_CONTENT));
                lp.leftMargin = DensityUtils.dp2px(10);
                lp.rightMargin = DensityUtils.dp2px(16);
                lp.topMargin = DensityUtils.dp2px(5);
                gv_pics.setLayoutParams(lp);
                gv_pics.setColumnWidth((width - 170) / 3);
                gv_pics.setNumColumns(3);

            }
        } else {
            gv_pics.setVisibility(View.GONE);
        }
        ImageLoadUtils.INSTANCE.loadImageView(iv_usrer_pic, mRealNameItem.userphoto);
        List<PraiseBean> praiselist = mRealNameItem.praiselist;// 点赞
        FlowLayout.removeAllViews();//将点赞人名变得可点击
        if (praiselist != null && praiselist.size() > 0) {
            for (int i = 0; i < praiselist.size(); i++) {
                if (praiselist.size() >= 5) {
                    if (i < 5) {
                        newPariseTV(FlowLayout, praiselist, i);
                        if (i != 4) {
                            TextView vv1 = new TextView(context);
                            vv1.setText(" 、");
                            vv1.setTextColor(context.getResources().getColor(R.color.c_00c8aa));
                            FlowLayout.addView(vv1);
                        } else {
                            if (Integer.parseInt(mRealNameItem.praiseNum) > 5) {
                                TextView vv1 = new TextView(context);
                                vv1.setText("等" + mRealNameItem.praiseNum + "人");
                                vv1.setTextSize(14);
                                vv1.setTextColor(context.getResources().getColor(R.color.gray_normal));
                                FlowLayout.addView(vv1);

                            }
                        }
                    }

                } else {
                    newPariseTV(FlowLayout, praiselist, i);
                    if (i != praiselist.size() - 1) {
                        TextView vv1 = new TextView(context);
                        vv1.setText(" 、");
                        vv1.setTextColor(context.getResources().getColor(R.color.c_00c8aa));
                        FlowLayout.addView(vv1);
                    }
                }
            }
        }
        isShowView(mRealNameItem, iv_praise, re_praise_do, tv_all_discusss
                , lv_praiselist, line);

        tv_all_discusss.setText("查看全部" + mRealNameItem.commentNum + "条评论");
        tv_username.setText(mRealNameItem.nickname);
        tv_user_time.setText(mRealNameItem.createtime);
        LogUtils.i("实名动态内容:" + mRealNameItem.content);

        if (!TextUtils.isEmpty(mRealNameItem.content)) {
            mExpandableTextView.setVisibility(View.VISIBLE);
            String text=RichTextUtils.parseCircleMsgContent((Activity) context, mExpandableTextView.getmTv(), mRealNameItem.content);
            mExpandableTextView.setText(text);
        } else {
            mExpandableTextView.setVisibility(View.GONE);
        }

        if (mRealNameItem.user != null) {
            tv_doctor.setText(mRealNameItem.user.job);
        }
        subject.setText(mRealNameItem.hospital);
        String pariseShowNum = mRealNameItem.praiseNum.equals("0") ? "0" : mRealNameItem.praiseNum;
        tv_praise_num.setText(pariseShowNum);
        String commentShowNum = mRealNameItem.commentNum.equals("0") ? "0" : mRealNameItem.commentNum;
        tv_discuss_num.setText(commentShowNum);

        tv_username.setOnClickListener(uiListener);
        mExpandableTextView.setOnClickListener(uiListener);
        expandable_text.setOnClickListener(uiListener);
        expandView.setOnClickListener(uiListener);
        ll_praise.setOnClickListener(uiListener);
        ll_doctor_share.setOnClickListener(uiListener);
        ll_discuss.setOnClickListener(uiListener);
        iv_usrer_pic.setOnClickListener(uiListener);
        tv_all_discusss.setOnClickListener(uiListener);
        //逻辑调整
        iv_add.setOnClickListener(uiListener);
        ll_share.setOnClickListener(uiListener);

        DoctorPicsAdapter mAdapter;
        List<String> minimgs = mRealNameItem.minimgs;//小图片
        if (minimgs != null && minimgs.size() > 0) {
            mAdapter = new DoctorPicsAdapter(context, minimgs, mRealNameItem.imgs);
            gv_pics.setVisibility(View.VISIBLE);
            gv_pics.setAdapter(mAdapter);
        } else {
            gv_pics.setVisibility(View.GONE);
        }
        // 关于评论
        List<CommentBean> commlist = mRealNameItem.commlist;

        if (commlist != null && commlist.size() > 0) {
            if (commlist.size() > 5) {// 多余五条
                commlist.remove(0);
            }
            CommlistAdapter mCommlistAdapter = new CommlistAdapter(context, mRealNameItem);
            disscuss_arrow_rl.setVisibility(View.VISIBLE);
            lv_praiselist.setVisibility(View.VISIBLE);          // lv_praiselist 为评论列表
            lv_praiselist.setAdapter(mCommlistAdapter);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = DensityUtils.dp2px(5);
            re_praise_do.setLayoutParams(lp);

        } else {      //没有评论时候
            disscuss_arrow_rl.setVisibility(View.GONE);
            lv_praiselist.setVisibility(View.GONE);


            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.bottomMargin = DensityUtils.dp2px(5);
            lp.topMargin = DensityUtils.dp2px(5);
            lp.leftMargin = 0;//DensityUtils.dp2px(context, 16);
            lp.rightMargin = 0;//DensityUtils.dp2px(context, 10);
            re_praise_do.setLayoutParams(lp);

        }
        //查看大图
        gv_pics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<String> pics = (ArrayList<String>) ((DoctorPicsAdapter) parent.getAdapter()).getPics();
                SeePicActivty.startActivity(context, pics, position, PublishType.Realname);
            }
        });
        //对评论进行评论
        lv_praiselist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CommentBean commentBean = mRealNameItem.commlist.get(position);
                view.setTag(R.string.first_tag, mRealNameItem);
                view.setTag(R.string.second_tag, commentBean);
                view.setOnClickListener(uiListener);
                view.callOnClick();
            }
        });
        itemView.setOnClickListener(uiListener);

    }

    /**
     * 组件是否显示
     *
     * @param mRealNameItem
     * @param iv_praise
     * @param re_praise_do
     * @param tv_all_discusss
     * @param lv_praiselist
     * @param line
     */
    private void isShowView(RealNameItem mRealNameItem, ImageView iv_praise, RelativeLayout re_praise_do, TextView tv_all_discusss
            , MyListView lv_praiselist, View line) {
        if ("1".equals(mRealNameItem.is_praise)) {// 是否点赞
            iv_praise.setBackgroundResource(R.drawable.icon_priseed);
        } else {
            iv_praise.setBackgroundResource(R.drawable.icon_doctor_praise);
        }
        if (mRealNameItem.praiseNum != null && Integer.parseInt(mRealNameItem.praiseNum) > 0) {// 点赞是否显示
            re_praise_do.setVisibility(View.VISIBLE);
        } else {
            re_praise_do.setVisibility(View.GONE);
        }
        if (mRealNameItem.commentNum != null && Integer.parseInt(mRealNameItem.commentNum) > 5) {// 判断查看全部是否显示
            tv_all_discusss.setVisibility(View.VISIBLE);

            if (re_praise_do.getVisibility() == View.VISIBLE) {//判断点赞是否显示动态控制评论top
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.topMargin = 0;
                lp.bottomMargin = 0;
                lp.rightMargin = DensityUtils.dp2px(10);
                lv_praiselist.setLayoutParams(lp);

            } else {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.bottomMargin = 0;
                lp.topMargin = 0;//DynamicDtaileNoNameActivity.dip2px(context, 10);
                lp.rightMargin = DensityUtils.dp2px(10);
                lv_praiselist.setLayoutParams(lp);
            }

        } else {
            tv_all_discusss.setVisibility(View.GONE);

            if (re_praise_do.getVisibility() == View.VISIBLE) {//判断点赞是否显示动态控制评论top

            } else {
            }

        }

        // 判断横线是否显示
        if (mRealNameItem.commlist != null && mRealNameItem.commlist.size() == 0 && mRealNameItem.praiselist.size() == 0) {
            line.setVisibility(View.GONE);
        } else {
            line.setVisibility(View.GONE);
        }
    }

    /**
     * 新建一个点赞用户textview
     *
     * @param praiselist
     * @param i
     */
    private void newPariseTV(final FlowRadioGroup rootView, List<PraiseBean> praiselist, int i) {
        TextView vv = new TextView(rootView.getContext());
        vv.setTextSize(14);
        vv.setTag(praiselist.get(i).userid);
        vv.setTextColor(rootView.getContext().getResources().getColor(R.color.c_00c8aa));
        vv.setText(praiselist.get(i).nickname);
        rootView.addView(vv);
        vv.setOnClickListener(new YMDebugOnClickListener() {
            @Override
            public void onClick(View v) {
                super.onClick(v);
                if (!NetworkUtil.isNetWorkConnected()) {
                    ToastUtils.shortToast("网络异常，请检查网络连接");
                    return;
                }
                StatisticalTools.eventCount(rootView.getContext(), "yqListPraiseName");
                String userid = (String) v.getTag();
                PersonDetailActivity.start(rootView.getContext(), userid, null);
            }
        });
    }
}
