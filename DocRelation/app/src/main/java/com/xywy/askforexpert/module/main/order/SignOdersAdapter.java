package com.xywy.askforexpert.module.main.order;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.adapter.RefreshBaseAdapter;
import com.xywy.askforexpert.model.SignOrderBean;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

/**
 * <p>
 * 功能：签约订单列表适配器
 * </p>
 *
 * @author liuxuejiao
 * @2015-5-11下午17:05:57
 */
public class SignOdersAdapter extends RefreshBaseAdapter {
    public FinalBitmap fb;
    /**
     * mInflater: 布局加载器
     */
    LayoutInflater mInflater;
    /**
     * mDataList: 用户评价列表
     */
    private List<SignOrderBean> mDataList;
    /**
     * mContext: 上下文
     */
    private Context mContext;
    /**
     * mRes: 资源对象
     */
    private Resources mRes;
    /**
     * mIsInService: 服务状态
     */
    private Boolean mIsInService = true;
    private boolean useFooter;

    public SignOdersAdapter(Context context) {
        this.mContext = context;
        this.mRes = context.getResources();
        this.mInflater = LayoutInflater.from(context);
        fb = FinalBitmap.create(context, false);
    }

    public List<SignOrderBean> getmDataList() {
        return mDataList;
    }

    public void setmDataList(List<SignOrderBean> mDataList,
                             boolean mIsInService) {
        this.mDataList = mDataList;
        this.mIsInService = mIsInService;
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
                R.layout.home_doctor_sign_orderllist_item, null);
        SignOrderViewHolder holder = new SignOrderViewHolder(view);
        return holder;
    }

    @Override
    public void onBindContentItemView(
            RecyclerView.ViewHolder holder,
            int position) {
        SignOrderViewHolder signOrderViewHolder = (SignOrderViewHolder) holder;
        SignOrderBean bean = mDataList.get(position);
        if (bean != null) {

            String category = bean.getCategory();
            // 1 包周 2，包月3，限免 4，折扣 5，不开放
            if (category.equals("1")) {
                signOrderViewHolder.mImageViewPhoto.setImageDrawable(mContext.getResources()
                        .getDrawable(R.drawable.bao_zhou_icon));
                signOrderViewHolder.mTextViewService
                        .setText("包周");
            } else if (category.equals("2")) {
                signOrderViewHolder.mImageViewPhoto.setImageDrawable(mContext.getResources()
                        .getDrawable(R.drawable.bao_yue_icon));
                signOrderViewHolder.mTextViewService
                        .setText("包月");
            } else if (category.equals("3")) {
                signOrderViewHolder.mImageViewPhoto.setImageDrawable(mContext.getResources()
                        .getDrawable(R.drawable.xian_mian_icon));
                signOrderViewHolder.mTextViewService
                        .setText("限免");
            } else if (category.equals("4")) {
                signOrderViewHolder.mImageViewPhoto.setImageDrawable(mContext.getResources()
                        .getDrawable(R.drawable.zhe_kou_icon));
                signOrderViewHolder.mTextViewService
                        .setText("折扣");
            } else if (category.equals("5")) {
                signOrderViewHolder.mImageViewPhoto.setImageDrawable(mContext.getResources()
                        .getDrawable(R.drawable.dan_ci_icon));
                signOrderViewHolder.mTextViewService
                        .setText("单次");
            }

            // fb.display(holder.mImageViewPhoto, bean.getPhoto());
            signOrderViewHolder.mTextViewName.setText(bean.getDocname());
//			holder.mTextViewService
//					.setText((HttpRequstParamsUtil.SERVICE_CATEGORY_WEEK + "")
//							.equals(bean.getCategory()) ? R.string.home_doctor_txt_service_week
//							: (HttpRequstParamsUtil.SERVICE_CATEGORY_MONTH + "")
//									.equals(bean.getCategory()) ? R.string.home_doctor_txt_service_month
//									: (HttpRequstParamsUtil.SERVICE_CATEGORY_YEAR + "")
//											.equals(bean.getCategory()) ? R.string.home_doctor_txt_service_year
//											: R.string.home_doctor_txt_service_onsale);
            signOrderViewHolder.mTextViewPay.setText(String.format(mRes
                            .getString(R.string.home_doctor_sign_orders_txt_realpay),
                    bean.getAmount()));
            signOrderViewHolder.orderTime.setText(bean.getOrederTime());
            // if (mIsInService) {
            // holder.mImageViewCall.setVisibility(View.VISIBLE);
            // } else {
            // holder.mImageViewCall.setVisibility(View.GONE);
            // }

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

    private class SignOrderViewHolder extends RecyclerView.ViewHolder {

        /**
         * mTextViewName: 姓名
         */
        TextView mTextViewName;
        /**
         * mTextViewService: 服务内容
         */
        TextView mTextViewService;
        /**
         * mTextViewPay: 支付金额
         */
        TextView mTextViewPay;
        /**
         * mImageViewPhoto: 头像
         */
        ImageView mImageViewPhoto;
        /**
         * 签约订单时间
         */
        TextView orderTime;
        /** mImageViewCall: 电话 */
        // ImageView mImageViewCall;
        private View parent;

        public SignOrderViewHolder(View itemView) {
            super(itemView);
            parent = itemView;

            mImageViewPhoto = (ImageView) itemView
                    .findViewById(R.id.sign_order_item_photo);
            // holder.mImageViewCall = (ImageView) convertView
            // .findViewById(R.id.sign_order_item_call);
            mTextViewName = (TextView) itemView
                    .findViewById(R.id.sign_order_item_name);
            mTextViewService = (TextView) itemView
                    .findViewById(R.id.sign_order_item_service);
            mTextViewPay = (TextView) itemView
                    .findViewById(R.id.sign_order_item_pay);
            orderTime = (TextView) itemView
                    .findViewById(R.id.iv_sign_order_time);
        }

    }
}
