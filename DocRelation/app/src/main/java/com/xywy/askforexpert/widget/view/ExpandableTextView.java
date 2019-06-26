package com.xywy.askforexpert.widget.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.easeWrapper.utils.SmileUtils;

/**
 * 仿微信查看全文
 */

public class ExpandableTextView extends LinearLayout implements View.OnClickListener {

    private static final String TAG = ExpandableTextView.class.getSimpleName();

    /* The default number of lines */
    private static final int MAX_COLLAPSED_LINES = 5;

    /* The default animation duration */
    private static final int DEFAULT_ANIM_DURATION = 300;

    /* The default alpha value when the animation starts */
    private static final float DEFAULT_ANIM_ALPHA_START = 0.7f;

    protected TextView mTv;

    protected TextView mButton; // Button to expand/collapse

    private boolean mRelayout;

    private boolean mCollapsed = true; // Show short version as default.

    private int mCollapsedHeight;

    private int mTextHeightWithMaxLines;

    private int mMaxCollapsedLines;

    private int mMarginBetweenTxtAndBottom;

    private String mExpandDrawable;

    private String mCollapseDrawable;

    private int mAnimationDuration;

    private float mAnimAlphaStart;

//    private boolean mAnimating;

    /* For saving collapsed status when used in ListView */
    private SparseBooleanArray mCollapsedStatus;
    private int mPosition;

    public ExpandableTextView(Context context) {
        super(context);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ExpandableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private static boolean isPostHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    @Override
    public void onClick(View view) {
        if (mButton.getVisibility() != View.VISIBLE) {
            return;
        }

        mCollapsed = !mCollapsed;

        mButton.setText(mCollapsed ? mExpandDrawable : mCollapseDrawable);

        if (mCollapsedStatus != null) {
            mCollapsedStatus.put(mPosition, mCollapsed);
        }
        if (mCollapsed) {
            mTv.setLines(mMaxCollapsedLines);
        } else {
            mTv.setSingleLine(false);

        }
    }

    @Override
    protected void onFinishInflate() {
        findViews();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // If no change, measure and return
        if (!mRelayout || getVisibility() == View.GONE) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        mRelayout = false;

        // Setup with optimistic case
        // i.e. Everything fits. No button needed
        mButton.setVisibility(View.GONE);
        mTv.setMaxLines(Integer.MAX_VALUE);

        // Measure
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // If the text fits in collapsed mode, we are done.
        if (mTv.getLineCount() <= mMaxCollapsedLines) {
            return;
        }

        // Saves the text height w/ max lines
//        mTextHeightWithMaxLines = getRealTextViewHeight(mTv);

        // Doesn't fit in collapsed mode. Collapse text view as needed. Show
        // button.
        if (mCollapsed) {
            mTv.setMaxLines(mMaxCollapsedLines);
        }
        mButton.setVisibility(View.VISIBLE);

        // Re-measure with new setup
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        requestLayout();
//        if (mCollapsed) {
//            // Gets the margin between the TextView's bottom and the ViewGroup's bottom
//            mTv.post(new Runnable() {
//                @Override
//                public void run() {
//                    mMarginBetweenTxtAndBottom = getHeight() - mTv.getHeight();
//                }
//            });
//            // Saves the collapsed height of this ViewGroup
//            mCollapsedHeight = getMeasuredHeight();
//        }
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
        mMaxCollapsedLines = typedArray.getInt(R.styleable.ExpandableTextView_maxCollapsedLines, MAX_COLLAPSED_LINES);
        mAnimationDuration = typedArray.getInt(R.styleable.ExpandableTextView_animDuration, DEFAULT_ANIM_DURATION);
        mAnimAlphaStart = typedArray.getFloat(R.styleable.ExpandableTextView_animAlphaStart, DEFAULT_ANIM_ALPHA_START);
        mExpandDrawable = "全文";
        mCollapseDrawable = "收起";
        typedArray.recycle();
    }

    private void findViews() {
        mTv = (TextView) findViewById(R.id.expandable_text);
//        mTv.setOnClickListener(null);
        mButton = (TextView) findViewById(R.id.expand_collapse);
        mButton.setText(mCollapsed ? mExpandDrawable : mCollapseDrawable);
//        mButton.setOnClickListener(this);
    }

    public void setText(CharSequence text) {
        mRelayout = true;
        Spannable spannable = SmileUtils.getSmiledText(mTv.getContext(), text);
        mTv.setMovementMethod(LinkMovementMethod.getInstance());
        mTv.setText(spannable, TextView.BufferType.SPANNABLE);
        setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
    }

    public void setText(CharSequence text, SparseBooleanArray collapsedStatus, int position) {
        mCollapsedStatus = collapsedStatus;
        mPosition = position;
        //        clearAnimation();
        mCollapsed = collapsedStatus.get(position, true);
        mButton.setText(mCollapsed ? mExpandDrawable : mCollapseDrawable);

        setText(text);
        getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        requestLayout();
    }

    public void setTextContent(CharSequence text, SparseBooleanArray collapsedStatus, int position) {
        mCollapsedStatus = collapsedStatus;
        mPosition = position;
        boolean isCollapsed = collapsedStatus.get(position, false);
//        clearAnimation();
        mCollapsed = isCollapsed;
        mButton.setText(mCollapsed ? mExpandDrawable : mCollapseDrawable);

        setText(text);
        getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        requestLayout();
    }
//  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//  private static void applyAlphaAnimation(View view, float alpha) {
//      if (isPostHoneycomb()) {
//          view.setAlpha(alpha);
//      } else {
//          AlphaAnimation alphaAnimation = new AlphaAnimation(alpha, alpha);
//          // make it instant
//          alphaAnimation.setDuration(0);
//          alphaAnimation.setFillAfter(true);
//          view.startAnimation(alphaAnimation);
//      }
//  }

//    public CharSequence getText() {
//        if (mTv == null) {
//            return "";
//        }
//        return mTv.getText();
//    }

//    private static int getRealTextViewHeight(TextView textView) {
//        int textHeight = textView.getLayout().getLineTop(textView.getLineCount());
//        int padding = textView.getCompoundPaddingTop() + textView.getCompoundPaddingBottom();
//        return textHeight + padding;
//    }

//    protected class ExpandCollapseAnimation extends Animation {
//        private final View mTargetView;
//        private final int mStartHeight;
//        private final int mEndHeight;
//
//        public ExpandCollapseAnimation(View view, int startHeight, int endHeight) {
//            mTargetView = view;
//            mStartHeight = startHeight;
//            mEndHeight = endHeight;
//            setDuration(mAnimationDuration);
//        }
//
//        @Override
//        protected void applyTransformation(float interpolatedTime, Transformation t) {
//            final int newHeight = (int)((mEndHeight - mStartHeight) * interpolatedTime + mStartHeight);
//            mTv.setMaxHeight(newHeight - mMarginBetweenTxtAndBottom);
//            if (Float.compare(mAnimAlphaStart, 1.0f) != 0) {
//                applyAlphaAnimation(mTv, mAnimAlphaStart + interpolatedTime * (1.0f - mAnimAlphaStart));
//            }
//            mTargetView.getLayoutParams().height = newHeight;
//            mTargetView.requestLayout();
//        }
//
//        @Override
//        public void initialize( int width, int height, int parentWidth, int parentHeight ) {
//            super.initialize(width, height, parentWidth, parentHeight);
//        }
//
//        @Override
//        public boolean willChangeBounds( ) {
//            return true;
//        }
//    };

    public boolean ismCollapsed() {
        return mCollapsed;
    }

    public void setmCollapsed(boolean mCollapsed) {
        this.mCollapsed = mCollapsed;
    }
}