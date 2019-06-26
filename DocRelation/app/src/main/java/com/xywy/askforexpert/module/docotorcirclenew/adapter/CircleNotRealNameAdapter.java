package com.xywy.askforexpert.module.docotorcirclenew.adapter;

/**
 * Created by Marshal Chen on 3/8/2016.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.DensityUtils;
import com.xywy.askforexpert.model.doctor.NotRealNameItem;
import com.xywy.askforexpert.module.docotorcirclenew.adapter.viewholder.BaseRecycleViewHolder;
import com.xywy.askforexpert.module.docotorcirclenew.model.PublishType;
import com.xywy.askforexpert.module.doctorcircle.SeePicActivty;
import com.xywy.askforexpert.module.doctorcircle.adapter.DoctorPicsAdapter;
import com.xywy.askforexpert.widget.view.MyGridView;
import com.xywy.askforexpert.widget.view.ultimaterecycleview.UltimateRecyclerviewViewHolder;
import com.xywy.easeWrapper.utils.SmileUtils;

import java.util.ArrayList;
import java.util.List;

//// TODO: 2016/11/15  卡顿问题待解决
public class CircleNotRealNameAdapter extends BaseUltimateRecycleAdapter<NotRealNameItem> {

    public CircleNotRealNameAdapter(Context context) {
        super(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_not_name, parent, false);
        RecyclerView.ViewHolder vh = new NotRealNameRecycleViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder mViewHolder, int position) {
        ((UltimateRecyclerviewViewHolder) mViewHolder).onBindView(getItem(position));
    }

    class NotRealNameRecycleViewHolder extends BaseRecycleViewHolder<NotRealNameItem> {
        //话题
        ImageView iv_praise, iv_discuss_pic, iv_doctor_share_pic;
        TextView tv_user_content, tv_praise_num, tv_discuss_num,
                tv_doctor_share_num, tv_user_time;
        LinearLayout ll_pics;
        MyGridView gv_notname_pic;
        LinearLayout ll_praise, ll_discuss, ll_doctor_share;

        public NotRealNameRecycleViewHolder(View convertView) {
            super(convertView);
            //话题列表
            tv_user_content = (TextView) convertView
                    .findViewById(R.id.tv_user_content);

            ll_praise = (LinearLayout) convertView
                    .findViewById(R.id.ll_praise);
            iv_praise = (ImageView) convertView
                    .findViewById(R.id.iv_praise);
            tv_praise_num = (TextView) convertView
                    .findViewById(R.id.tv_praise_num);
            ll_discuss = (LinearLayout) convertView
                    .findViewById(R.id.ll_discuss);
            iv_discuss_pic = (ImageView) convertView
                    .findViewById(R.id.iv_discuss_pic);
            tv_discuss_num = (TextView) convertView
                    .findViewById(R.id.tv_discuss_num);

            ll_pics = (LinearLayout) convertView
                    .findViewById(R.id.ll_pics);
            ll_doctor_share = (LinearLayout) convertView
                    .findViewById(R.id.ll_doctor_share);
            iv_doctor_share_pic = (ImageView) convertView
                    .findViewById(R.id.iv_doctor_share_pic);
            tv_doctor_share_num = (TextView) convertView
                    .findViewById(R.id.tv_doctor_share_num);
            gv_notname_pic = (MyGridView) convertView
                    .findViewById(R.id.gv_notname_pic);

            tv_user_time = (TextView) convertView.findViewById(R.id.tv_user_time);

        }

        public void updateView(final Context context, NotRealNameItem item) {
            super.updateView(context, item);
            if (item != null) {
                tv_user_time.setText(item.createtime);

                String is_praise = item.is_praise;
                if ("1".equals(is_praise)) {
                    iv_praise
                            .setBackgroundResource(R.drawable.icon_priseed);
                } else {
                    iv_praise
                            .setBackgroundResource(R.drawable.icon_doctor_praise);
                }

                tv_discuss_num.setText(item.commentNum.equals("0") ? "0" : item.commentNum);
                Spannable spannable = SmileUtils.getSmiledText(context, item.content);
                tv_user_content.setText(spannable, TextView.BufferType.SPANNABLE);
                tv_praise_num.setText(item.praiseNum.equals("0") ? "0" : item.praiseNum);
                ll_praise.setOnClickListener(uiListener);
                ll_discuss.setOnClickListener(uiListener);
                tv_user_content.setOnClickListener(uiListener);
                ll_doctor_share.setOnClickListener(uiListener);

                if (item.minimgs != null) {
                    List<String> minimgs = item.minimgs;
                    // // gridview 显示几列
                    if (minimgs.size() == 1) {
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(DensityUtils.dp2px( 130), ViewGroup.LayoutParams.WRAP_CONTENT));
                        lp.leftMargin = DensityUtils.dp2px(10);
                        lp.rightMargin = DensityUtils.dp2px(16);
                        lp.topMargin = DensityUtils.dp2px(5);

                        gv_notname_pic.setLayoutParams(lp);
                        gv_notname_pic.setColumnWidth(DensityUtils.dp2px(130));
                        gv_notname_pic.setNumColumns(1);
                    } else if (minimgs.size() == 4 || minimgs.size() == 2) {
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(DensityUtils.dp2px( 260), ViewGroup.LayoutParams.WRAP_CONTENT));
                        lp.leftMargin = DensityUtils.dp2px(10);
                        lp.rightMargin = DensityUtils.dp2px(16);
                        lp.topMargin = DensityUtils.dp2px(5);
                        gv_notname_pic.setLayoutParams(lp);
                        gv_notname_pic.setColumnWidth(DensityUtils.dp2px(130));
                        gv_notname_pic.setNumColumns(2);

                    } else {
                        int width = AppUtils.getScreenWidth(context);

                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width - DensityUtils.dp2px(80), ViewGroup.LayoutParams.WRAP_CONTENT));
                        lp.leftMargin = DensityUtils.dp2px(10);
                        lp.rightMargin = DensityUtils.dp2px(16);
                        lp.topMargin = DensityUtils.dp2px(5);
                        gv_notname_pic.setLayoutParams(lp);
                        gv_notname_pic
                                .setColumnWidth((width - 170) / 3);
                        gv_notname_pic.setNumColumns(3);

                    }

                    DoctorPicsAdapter mnoAdapter = null;
                    if (minimgs != null && minimgs.size() > 0) {
                        if (mnoAdapter == null) {
                            mnoAdapter = new DoctorPicsAdapter(context, minimgs, item.imgs);
                        }
                        gv_notname_pic.setAdapter(mnoAdapter);
                        gv_notname_pic.setVisibility(View.VISIBLE);
                        ll_pics.setVisibility(View.VISIBLE);
                        tv_user_content.setMinLines(2);
                    } else {
                        tv_user_content.setMinLines(5);
                        ll_pics.setVisibility(View.GONE);
                        gv_notname_pic.setVisibility(View.GONE);
                    }
                } else {
                    gv_notname_pic.setVisibility(View.GONE);
                }
                gv_notname_pic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ArrayList<String> pics = (ArrayList<String>) ((DoctorPicsAdapter) parent.getAdapter()).getPics();
                        SeePicActivty.startActivity(context, pics, position, PublishType.Anonymous);
                    }
                });

                itemView.setOnClickListener(uiListener);
            }
        }
    }
//
//    private class ItemViewHolder extends UltimateRecyclerviewViewHolder<NotRealNameItem> {
//        BaseRecycleViewHolder<NotRealNameItem> binder;
//
//        ItemViewHolder(View rootView) {
//            super(rootView);
//            binder = new NotRealNameRecycleViewHolder(rootView);
//        }
//
//        protected void updateView(Context context, NotRealNameItem item) {
//            binder.updateView(context, item);
//        }
//    }
}