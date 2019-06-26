package com.xywy.askforexpert.widget.module.answer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.xywy.askforexpert.R;

/**
 * Created by wangpeng on 16/8/23.
 * describ：
 * revise：
 */
public class SheetTextView extends View {

    float ringWidth;
    /**
     * 控件宽度
     */
    private int mWidth;
    /**
     * 控件高度
     */
    private int mHeight;
    /**
     * 圆的半径
     */
    private float mRadius;
    /**
     * 圆的画笔
     */
    private Paint mPaintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mPaintOutCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 文字画笔
     */
    private Paint mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 内容
     */
    private String mTextStr = "";
    /**
     * 字体颜色
     */
    private int textColor;
    /**
     * 字体大小
     */
    private float textSize;
    /**
     * 圆背景
     */
    private int mCircleBackgroundColor;
    private int mOuterCiclerColor;

    public SheetTextView(Context context) {
        this(context, null);
    }

    public SheetTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {


        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SheetTextView);

        textColor = typedArray.getColor(R.styleable.SheetTextView_sheetTextColor, ContextCompat.getColor(context, android.R.color.black));
        textSize = typedArray.getDimension(R.styleable.SheetTextView_sheetTextSize, 0);
        mCircleBackgroundColor = typedArray.getColor(R.styleable.SheetTextView_sheetCicleColor, 0xffffff);
        ringWidth = typedArray.getDimension(R.styleable.SheetTextView_sheetCircleRingSize, 0);
        mOuterCiclerColor = typedArray.getColor(R.styleable.SheetTextView_sheetCircleOuterColor, ContextCompat.getColor(context, android.R.color.white));

        typedArray.recycle();

    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
        mRadius = Math.min(mWidth / 2, mHeight / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaintCircle.setColor(mCircleBackgroundColor);
        mPaintOutCircle.setColor(mOuterCiclerColor);
        mPaintText.setColor(textColor);
        mPaintText.setTextSize(textSize);

        int mTextWidth = getFontWidth(mPaintText, mTextStr);
        int mTextHeight = getFontHeight(mPaintText, mTextStr);
//        mPaintOutCircle.setStrokeWidth(ringWidth);
//
//        mPaintOutCircle.setStyle(Paint.Style.STROKE);
        //// TODO: 16/8/23 绘制外圈
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mPaintOutCircle);
//        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius-ringWidth, mPaintOutCircle);

        //// TODO: 16/8/23 绘制内圈
        canvas.drawCircle((mWidth) / 2, (mHeight) / 2, mRadius - ringWidth, mPaintCircle);


//        Log.d("Tag", (mWidth - mTextWidth) / 2 + "  ");
//        Log.d("Tag", (mHeight - mTextHeight) + "  ");
//        Log.d("Tag", mWidth + "  " + mHeight + " " + mTextWidth + " " + mTextHeight + " " + ringWidth);

        canvas.drawText(mTextStr, (mWidth - mTextWidth) / 2, (mHeight + mTextHeight) / 2, mPaintText);


    }

    /**
     * 获得字体宽w
     *
     * @param paint
     * @param str
     * @return 输入字符串的宽度
     */
    private int getFontWidth(Paint paint, String str) {
        if (str == null || str.equals("")) {
            return 0;
        }
        Rect rect = new Rect();
        int length = str.length();
        paint.getTextBounds(str, 0, length, rect);
        return rect.width();
    }

    /**
     * 获得字体高度
     *
     * @param paint
     * @return 输入字符串的高度
     */
    private int getFontHeight(Paint paint, String str) {
        if (str == null || str.equals("")) {
            return 0;
        }
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        return rect.height();
    }

    /**
     * 获得由dip数值转化而来的px数值
     *
     * @param context 上下文
     * @param dpValue dip值
     * @return px数值
     */
    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int getTextColor() {
        return textColor;
    }

    /**
     * 设置字体颜色
     *
     * @param textColor
     */
    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    /**
     * 设置字体大小
     *
     * @param textSize
     */
    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public int getmCircleBackgroundColor() {
        return mCircleBackgroundColor;
    }

    /**
     * 绘制圆圈的背景
     *
     * @param mCircleBackgroundColor
     */
    public void setmCircleBackgroundColor(int mCircleBackgroundColor) {
        this.mCircleBackgroundColor = mCircleBackgroundColor;
    }

    public String getmTextStr() {
        return mTextStr;
    }

    /**
     * 设置显示文字
     *
     * @param mTextStr
     */
    public void setmTextStr(String mTextStr) {
        this.mTextStr = mTextStr;
    }

    public int getmOuterCiclerColor() {
        return mOuterCiclerColor;
    }

    /**
     * 设置外圈背景颜色
     *
     * @return
     */
    public void setmOuterCiclerColor(int mOuterCiclerColor) {
        this.mOuterCiclerColor = mOuterCiclerColor;
    }
}