package com.xywy.askforexpert.module.doctorcircle.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.adapter.RefreshBaseAdapter;
import com.xywy.askforexpert.model.UserCommentBean;
import com.xywy.askforexpert.module.main.service.que.utils.TimeFormatUtils;

import java.util.List;

/**
 * <p>
 * 功能：用户评价列表适配器
 * </p>
 *
 * @author liuxuejiao
 * @2015-5-11下午19:05:57
 */
@SuppressLint("ResourceAsColor")
public class UserCommentAdapter extends RefreshBaseAdapter {

    /**
     * mInflater: 布局加载器
     */
    LayoutInflater mInflater;
    /**
     * mDataList: 用户评价列表
     */
    private List<UserCommentBean> mDataList;
    /**
     * mContext: 上下文
     */
    private Context mContext;
    private boolean useFooter;

    public UserCommentAdapter(Context context, List<UserCommentBean> list) {
        this.mContext = context;
        this.mDataList = list;
        mInflater = LayoutInflater.from(mContext);
    }

    public List<UserCommentBean> getmDataList() {
        return mDataList;
    }

    public void setmDataList(List<UserCommentBean> mDataList) {
        this.mDataList = mDataList;
        notifyDataSetChanged();
    }

    @Override
    public boolean useFooter() {
        // TODO Auto-generated method stub
        return useFooter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateContentItemViewHolder(
            ViewGroup parent, int viewType) {
        View view = mInflater.inflate(
                R.layout.home_doctor_commentlist_item, null);
        CommentViewHolder holder = new CommentViewHolder(view);
        return holder;
    }

    @Override
    public void onBindContentItemView(
            RecyclerView.ViewHolder holder,
            int position) {
        CommentViewHolder commentViewHolder = (CommentViewHolder) holder;
        UserCommentBean bean = mDataList.get(position);

        if (bean != null) {
            commentViewHolder.mTextViewName.setText(bean.getG_uname());
            commentViewHolder.mRatingBar.setRating(Float.parseFloat(bean.getG_stat()));
            commentViewHolder.mTextViewContent.setText(bean.getG_cons());
            commentViewHolder.mTextViewScore
                    .setText(Html.fromHtml("<font color=\"#ffa247\">"
                            + (Float.parseFloat(bean.getG_stat()) % 1 == 0 ? Math
                            .round(Math.round(Float.parseFloat(bean
                                    .getG_stat()))) : Float
                            .parseFloat(bean.getG_stat()))
                            + "</font><font color=\"#999999\">分</font> "));
            commentViewHolder.mTextViewTime.setText(TimeFormatUtils.formatShowTime(Long
                    .parseLong(bean.getG_date())));
        }
    }

    @Override
    public int getContentItemCount() {
        // TODO Auto-generated method stub
        return mDataList.size();
    }

    @Override
    public int getContentItemType(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setUseFooter(boolean useFooter) {
        this.useFooter = useFooter;
    }

    private class CommentViewHolder extends RecyclerView.ViewHolder {

        private View parent;
        /**
         * mTextViewName: 姓名
         */
        private TextView mTextViewName;
        /**
         * mTextViewContent: 评价内容
         */
        private TextView mTextViewContent;
        /**
         * mTextViewScore: 分数
         */
        private TextView mTextViewScore;
        /**
         * mRatingBar: 评价分数
         */
        private RatingBar mRatingBar;
        /**
         * mTextViewTime: 时间
         */
        private TextView mTextViewTime;

        public CommentViewHolder(View itemView) {
            super(itemView);
            parent = itemView;

            mRatingBar = (RatingBar) itemView
                    .findViewById(R.id.commentlist_item_ratingbar);
            mTextViewName = (TextView) itemView
                    .findViewById(R.id.commentlist_item_tv_name);
            mTextViewScore = (TextView) itemView
                    .findViewById(R.id.commentlist_item_score);
            mTextViewTime = (TextView) itemView
                    .findViewById(R.id.commentlist_item_tv_time);
            mTextViewContent = (TextView) itemView
                    .findViewById(R.id.commentlist_item_tv_commcontent);
        }

    }
}
