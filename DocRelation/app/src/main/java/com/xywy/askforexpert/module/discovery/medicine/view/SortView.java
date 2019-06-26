package com.xywy.askforexpert.module.discovery.medicine.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.module.discovery.medicine.config.OffLineService;

/**
 * Created by bobby on 17/3/24.
 */

public class SortView extends LinearLayout implements View.OnClickListener {

    public interface OnSrotTypeChangeListener {
        void onSortTypeChange(SORTED_TYPE type);
    }

    public enum SORTED_TYPE {
        SORTED_TYPE_DEFAULT,
        SORTED_TYPE_PRICE_UP,
        SORTED_TYPE_PRICE_DOWN,
        SORTED_TYPE_COUNT_UP,
        SORTED_TYPE_COUNT_DOWN,
        SORTED_TYPE_INDEX_UP,
        SORTED_TYPE_INDEX_DOWN
    }

    private SORTED_TYPE mSortedType = SORTED_TYPE.SORTED_TYPE_DEFAULT;

    private View mDefault;
    private View mPrice;
    private View mCount;
    private View mIndex;

    private TextView mDefaultText;
    private TextView mPriceText;
    private TextView mCountText;
    private TextView mIndexText;

    private ImageView mIvPrice;
    private ImageView mIvCount;
    private ImageView mIvIndex;
    private boolean mUp ;

    private OnSrotTypeChangeListener listener;

    public SortView(Context context) {
        this(context, null);

    }

    public SortView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_sort, this);
        initView();
    }



    private void initView() {
        mDefault = findViewById(R.id.type_default);
        mPrice = findViewById(R.id.type_price);
        mCount = findViewById(R.id.type_count);
        mIndex = findViewById(R.id.type_index);
        if(OffLineService.isOffLine()){
            mIndex.setVisibility(GONE);//线下版本，就隐藏指数
        }else {
            mIndex.setVisibility(VISIBLE);//线上版本显示指数
        }
        mDefaultText = (TextView) findViewById(R.id.type_default);
        mPriceText = (TextView) findViewById(R.id.price);
        mCountText = (TextView) findViewById(R.id.count);
        mIndexText = (TextView)findViewById(R.id.index);
        mIvPrice = (ImageView) findViewById(R.id.iv_price);
        mIvCount = (ImageView) findViewById(R.id.iv_count);
        mIvIndex = (ImageView)findViewById(R.id.iv_index);
        mDefault.setOnClickListener(this);
        mPrice.setOnClickListener(this);
        mCount.setOnClickListener(this);
        mIndex.setOnClickListener(this);
    }

    public void setListener(OnSrotTypeChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        dealWithLastType(mSortedType);
        switch (v.getId()) {
            case R.id.type_default:
                if(listener!=null && mSortedType != SORTED_TYPE.SORTED_TYPE_DEFAULT) {
                    mSortedType = SORTED_TYPE.SORTED_TYPE_DEFAULT;
                }
                break;
            case R.id.type_count:
                if(mSortedType == SORTED_TYPE.SORTED_TYPE_COUNT_UP) {
                    mSortedType = SORTED_TYPE.SORTED_TYPE_COUNT_DOWN;
                } else {
                    mSortedType = SORTED_TYPE.SORTED_TYPE_COUNT_UP;
                    }
                break;
            case R.id.type_price:
                if(mSortedType == SORTED_TYPE.SORTED_TYPE_PRICE_UP) {
                    mSortedType = SORTED_TYPE.SORTED_TYPE_PRICE_DOWN;
                } else {
                    mSortedType = SORTED_TYPE.SORTED_TYPE_PRICE_UP;
                }
                break;
            case R.id.type_index:
                if(mSortedType == SORTED_TYPE.SORTED_TYPE_INDEX_UP) {
                    mSortedType = SORTED_TYPE.SORTED_TYPE_INDEX_DOWN;
                } else {
                    mSortedType = SORTED_TYPE.SORTED_TYPE_INDEX_UP;
                }

                break;
            default:
                break;
        }
        dealWithNewType(mSortedType);
    }

    private void dealWithLastType(SORTED_TYPE lastType) {
        TextView lastTextView = getTextViewFromType(lastType);
        if(lastTextView == null) {
            return;
        }
        lastTextView.setTextColor(getResources().getColor(R.color.my_textcolor));
        ImageView newImageView = getImageViewFromType(lastType);
        if(null == newImageView){
            return;
        }
        newImageView.setImageResource(R.drawable.sort_up_down);
    }

    private void dealWithNewType(SORTED_TYPE newType) {
        TextView newTextView = getTextViewFromType(newType);
        if(newTextView == null) {
            return;
        }
        newTextView.setTextColor(getResources().getColor(R.color.sort_view_focuse_color));
        if(listener!=null) {
            listener.onSortTypeChange(mSortedType);
        }
        ImageView newImageView = getImageViewFromType(newType);
        if(null == newImageView){
            return;
        }
        if(mUp){
            newImageView.setImageResource(R.drawable.sort_up);
        }else {
            newImageView.setImageResource(R.drawable.sort_down);
        }

    }

    private TextView getTextViewFromType(SORTED_TYPE type) {
        TextView target = null;
        switch (type) {
            case SORTED_TYPE_DEFAULT:
                target = mDefaultText;
                break;
            case SORTED_TYPE_COUNT_UP:
            case SORTED_TYPE_COUNT_DOWN:
                target = mCountText;
                break;
            case SORTED_TYPE_INDEX_UP:
            case SORTED_TYPE_INDEX_DOWN:
                target = mIndexText;
                break;
            case SORTED_TYPE_PRICE_UP:
            case SORTED_TYPE_PRICE_DOWN:
                target = mPriceText;
                break;
            default:
                break;
        }
        return target;
    }
    private ImageView getImageViewFromType(SORTED_TYPE type) {
        ImageView target = null;
        switch (type) {
            case SORTED_TYPE_DEFAULT:
                break;
            case SORTED_TYPE_COUNT_UP:
                mUp = true;
                target = mIvCount;
                break;
            case SORTED_TYPE_COUNT_DOWN:
                mUp = false;
                target = mIvCount;
                break;
            case SORTED_TYPE_INDEX_UP:
                mUp = true;
                target = mIvIndex;
                break;
            case SORTED_TYPE_INDEX_DOWN:
                mUp = false;
                target = mIvIndex;
                break;
            case SORTED_TYPE_PRICE_UP:
                mUp = true;
                target = mIvPrice;
                break;
            case SORTED_TYPE_PRICE_DOWN:
                mUp = false;
                target = mIvPrice;
                break;
            default:
                break;
        }
        return target;
    }
}
