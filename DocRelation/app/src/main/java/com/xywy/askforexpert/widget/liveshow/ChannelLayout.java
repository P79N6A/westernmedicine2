package com.xywy.askforexpert.widget.liveshow;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.DensityUtils;

import java.util.List;

/**
 * Created by DongJr on 2017/2/20.
 * <p>
 * 频道页面布局
 */

public class ChannelLayout extends LinearLayout implements Animation.AnimationListener {

    private List<String> mChannelList;
    private DragGridView mDragGridView;
    private DragAdapter mDragAdapter;

    private Context mContext;

    private static final int ANIMATION_TIME = 300;

    private boolean isChannelLayoutOpen;

    private OnDragViewClickListener mOnDragViewClickListener;

    private int mCurrentPosition;

    public ChannelLayout(Context context) {
        this(context, null);
    }

    public ChannelLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChannelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        setOrientation(VERTICAL);
    }

    public void setChannelData(List<String> channelList) {
        mChannelList = channelList;
        layoutViews();
    }

    private void layoutViews() {
        layoutTitle();
        layoutGridView();
    }

    private void layoutTitle() {
        TextView textView = new TextView(mContext);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(params);
        textView.setText("我的频道");
        //textView.setTextColor(getResources().getColor(R.color.c_333));
        textView.setTextColor(Color.parseColor("#666666"));
        textView.setTextSize(14);
        textView.setBackgroundColor(Color.WHITE);
        textView.setClickable(true);
        textView.setPadding(DensityUtils.dp2px(10), DensityUtils.dp2px(10), 0, 0);
        addView(textView);
    }

    private void layoutGridView() {
        if (mChannelList == null) {
            return;
        }

        if (mDragAdapter == null) {

            mDragGridView = new DragGridView(mContext);
            mDragGridView.setNumColumns(4);
            mDragGridView.setHorizontalSpacing(DensityUtils.dp2px(8));
            mDragGridView.setVerticalSpacing(DensityUtils.dp2px(8));
            mDragGridView.setPadding(DensityUtils.dp2px(10), DensityUtils.dp2px(10),
                    DensityUtils.dp2px(10), DensityUtils.dp2px(10));
            mDragGridView.setBackgroundColor(Color.WHITE);
            addView(mDragGridView);
            mDragAdapter = new DragAdapter();
            mDragGridView.setAdapter(mDragAdapter);
        } else {
            mDragAdapter.notifyDataSetChanged();
        }

        onClickEvent();

    }

    private void onClickEvent() {
        mDragGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnDragViewClickListener != null) {
                    mOnDragViewClickListener.onClick(view, position);
                }
            }
        });


    }

    public boolean isLayoutOpen() {
        return isChannelLayoutOpen;
    }


    public void open(int currentPosition) {
        mCurrentPosition = currentPosition;

        setVisibility(VISIBLE);
        TranslateAnimation animation = new TranslateAnimation(0.0f
                , 0f, -getMeasuredHeight(), 0.0f);
        animation.setDuration(ANIMATION_TIME);
        animation.setAnimationListener(this);
        startAnimation(animation);
        mDragAdapter.notifyDataSetChanged();
    }

    public void close() {
        TranslateAnimation animation = new TranslateAnimation(0.0f,
                0f, 0, -getMeasuredHeight());
        animation.setDuration(ANIMATION_TIME);
        animation.setAnimationListener(this);
        startAnimation(animation);
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        isChannelLayoutOpen = !isChannelLayoutOpen;
        setVisibility(isChannelLayoutOpen ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    class DragAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mChannelList.size();
        }

        @Override
        public String getItem(int position) {
            return mChannelList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DragHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext)
                        .inflate(R.layout.item_news_drag_grid_view, parent, false);
                holder = new DragHolder();
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.flItemRoot = (FrameLayout) convertView.findViewById(R.id.fl_item_root);
                convertView.setTag(holder);

            } else {
                holder = (DragHolder) convertView.getTag();
            }
            holder.tvName.setText(getItem(position));

            holder.flItemRoot.setEnabled(position == mCurrentPosition);
            holder.tvName.setEnabled(position == mCurrentPosition);
            return convertView;
        }
    }

    static class DragHolder {
        TextView tvName;
        FrameLayout flItemRoot;
    }


    public interface OnDragViewClickListener {
        void onClick(View view, int positon);
    }

    public void setOnDragViewClickListener(OnDragViewClickListener listener) {
        mOnDragViewClickListener = listener;
    }

}
