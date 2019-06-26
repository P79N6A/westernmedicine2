package com.xywy.askforexpert.module.main.service.que.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.imageutils.DrawableImageLoader;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.appcommon.utils.tv.TVUtils;
import com.xywy.askforexpert.appcommon.utils.tv.bean.ColorText;
import com.xywy.askforexpert.model.QuestionSquareMsgItem;
import com.xywy.askforexpert.module.doctorcircle.SeePicActivty;
import com.xywy.askforexpert.module.main.service.que.model.MedicineBean;
import com.xywy.askforexpert.module.main.service.que.utils.TimeFormatUtils;
import com.xywy.util.LogUtils;

import net.tsz.afinal.FinalBitmap;

import java.util.ArrayList;
import java.util.List;

public class QueDetailAdapter extends BaseAdapter {

    private static final String TAG = "QueDetailAdapter";
    private static final String QUEDETAILADAPTER = "QueDetailAdapter";
    private static final String IS_FROM = "isFrom";
    FinalBitmap fb;
    DrawableImageLoader imageLoader;
    private Context context;
    private List<QuestionSquareMsgItem> queDetailLists;

    public QueDetailAdapter(Context context) {
        this.context = context;
        imageLoader = DrawableImageLoader.getInstance();

        fb = FinalBitmap.create(context, true);
        fb.configLoadfailImage(R.drawable.que_head_icon);
        fb.configLoadingImage(R.drawable.que_head_icon);
    }

    public void bindData(List<QuestionSquareMsgItem> queDetailLists) {
        this.queDetailLists = queDetailLists;
    }

    @Override
    public int getCount() {

        return queDetailLists == null ? -1 : queDetailLists.size();
    }

    @Override
    public Object getItem(int position) {

        return queDetailLists == null ? null : queDetailLists.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.que_detail_item, parent, false);

            holder.typeLayout = (LinearLayout) convertView
                    .findViewById(R.id.ll_user_type);
            holder.typeLayout1 = (LinearLayout) convertView
                    .findViewById(R.id.ll_user_type1);

            holder.tvUContent = (TextView) convertView
                    .findViewById(R.id.tv_detail_ucontent);

            holder.left = (LinearLayout) convertView
                    .findViewById(R.id.ll_left_detail);
            holder.right = (LinearLayout) convertView
                    .findViewById(R.id.ll_right_detail);

            holder.tvQueDpart = (TextView) convertView
                    .findViewById(R.id.tv_detail_dpart);
            holder.tvDesPes = (TextView) convertView
                    .findViewById(R.id.tv_detail_info);
            holder.photo = (ImageView) convertView
                    .findViewById(R.id.iv_detail_icon);
            holder.tvDesDatial = (TextView) convertView
                    .findViewById(R.id.tv_detail_des);
            holder.tvTime = (TextView) convertView
                    .findViewById(R.id.tv_detail_time);
            holder.tvDTime = (TextView) convertView
                    .findViewById(R.id.tv_detail_dtime);

            holder.tvContent = (TextView) convertView
                    .findViewById(R.id.tv_detail_dcontent);

            holder.tv_cash_reward = (TextView) convertView.findViewById(R.id.tv_cash_reward);
            holder.tv_title_content = (TextView) convertView.findViewById(R.id.tv_title_content);

            holder.ivDphoto = (ImageView) convertView
                    .findViewById(R.id.iv_detail_dicon);

            holder.tvDesState = (TextView) convertView
                    .findViewById(R.id.tv_detail_state);
            holder.tvDesHelp = (TextView) convertView
                    .findViewById(R.id.tv_detail_help);

            holder.stateLayout = (LinearLayout) convertView
                    .findViewById(R.id.ll_detail_state);
            holder.helpLayout = (LinearLayout) convertView
                    .findViewById(R.id.ll_detail_help);

            holder.leftImg = (LinearLayout) convertView
                    .findViewById(R.id.ll_user_img);
            holder.ivLeftShow = (ImageView) convertView
                    .findViewById(R.id.iv_detail_zimg);

            holder.llDpart = (LinearLayout) convertView
                    .findViewById(R.id.ll_detail_dpatr);
            // holder.rightImg = convertView.findViewById(R.id.ll)

            holder.rl_detail_dcontent = (RelativeLayout) convertView.findViewById(R.id.rl_detail_dcontent);
            holder.rl_medicine = (RelativeLayout) convertView.findViewById(R.id.rl_medicine);
            holder.tv_medicine_name = (TextView) convertView.findViewById(R.id.tv_medicine_name);
            holder.iv_medicine = (ImageView) convertView.findViewById(R.id.iv_medicine);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        QuestionSquareMsgItem item = queDetailLists.get(position);
        int type = item.getType();
        LogUtils.e("type=" + type);
        //药品item可见性
        holder.rl_medicine.setVisibility(4==type?View.VISIBLE:View.GONE);
        if (type == 0) {
            holder.typeLayout.setVisibility(View.GONE);
            holder.typeLayout1.setVisibility(View.VISIBLE);
            holder.leftImg.setVisibility(View.GONE);
            holder.left.setVisibility(View.VISIBLE);
            holder.right.setVisibility(View.GONE);

            holder.photo.setImageDrawable(context.getResources().getDrawable(R.drawable.que_head_icon));
            DLog.d("TAG", "my reply detail age = " + item.getAge());
            holder.tvDesPes.setText(item.getSex() + "    " + (TextUtils.isEmpty(item.getAge())?"":item.getAge()));
            holder.tvDesDatial.setText(item.getDetail());
            if (position > 0) {
                if (queDetailLists.get(position - 1).getCreateTime().equals(item.getCreateTime())) {
                    holder.tvTime.setVisibility(View.GONE);
                } else {
                    holder.tvTime.setVisibility(View.VISIBLE);
                    holder.tvTime.setText(TimeFormatUtils.formatMsgTime(item.getCreateTime()));
                }
            } else {
                holder.tvTime.setText(TimeFormatUtils.formatMsgTime(item.getCreateTime()));
            }

            String state = item.getState();
            String help = item.getHelp();
            String pidName = item.getSubject_pidName();
            String subName = item.getSubjectName();

            if ("".equals(pidName) || "".equals(subName)) {
                holder.tvQueDpart.setVisibility(View.GONE);
                holder.llDpart.setVisibility(View.GONE);
            } else {
                holder.llDpart.setVisibility(View.VISIBLE);
                holder.tvQueDpart.setVisibility(View.VISIBLE);
                holder.tvQueDpart.setText(item.getSubject_pidName() + "-" + item.getSubjectName());
            }

            if ("".equals(state) || state == null) {
                holder.stateLayout.setVisibility(View.GONE);
            } else {
                holder.stateLayout.setVisibility(View.VISIBLE);
                holder.tvDesState.setText(state);
            }

            if ("".equals(help) || help == null) {
                holder.helpLayout.setVisibility(View.GONE);
            } else {
                holder.helpLayout.setVisibility(View.VISIBLE);
                holder.tvDesHelp.setText(help);
            }
        } else if (type == 1) {
            holder.left.setVisibility(View.GONE);
            holder.right.setVisibility(View.VISIBLE);
            holder.leftImg.setVisibility(View.GONE);
            DLog.i(TAG, "url" + item.getPhoto());
            fb.display(holder.ivDphoto, item.getPhoto());

            holder.tvContent.setText(item.getDetail());
//            holder.tvContent.setText(item.getDetail()+"\n"+item.getDetail());

            if (position > 0) {
                if(null != queDetailLists.get(position - 1).getCreateTime()){
                    if (queDetailLists.get(position - 1).getCreateTime().equals(item.getCreateTime())) {
                        holder.tvDTime.setVisibility(View.GONE);
                    } else {
                        holder.tvDTime.setVisibility(View.VISIBLE);
                        holder.tvDTime.setText(TimeFormatUtils.formatMsgTime(item.getCreateTime()));
                    }
                }
            }
        } else if (type == 2) {
            //自己发出的消息
            holder.left.setVisibility(View.GONE);
            holder.right.setVisibility(View.VISIBLE);
            holder.leftImg.setVisibility(View.GONE);
            if(holder.rl_detail_dcontent.getVisibility()==View.GONE){
                holder.rl_detail_dcontent.setVisibility(View.VISIBLE);
            }
            if(holder.ivDphoto.getVisibility()==View.GONE){
                holder.ivDphoto.setVisibility(View.VISIBLE);
            }

//            fb.display(holder.ivDphoto, queDetailLists.get(1).getPhoto());
            fb.display(holder.ivDphoto, queDetailLists.get(position).getPhoto());

            holder.tvContent.setText(item.getDetail());

            if (position > 0) {
                if (queDetailLists.get(position - 1).getCreateTime().equals(item.getCreateTime())) {
                    holder.tvDTime.setVisibility(View.GONE);
                } else {
                    holder.tvDTime.setVisibility(View.VISIBLE);
                    holder.tvDTime.setText(TimeFormatUtils.formatMsgTime(item.getCreateTime()));
                }
            } else {
                holder.tvDTime.setText(TimeFormatUtils.formatMsgTime(item.getCreateTime()));
            }
        } else if (type == 3) {
            //收到的消息
            final String content = item.getDetail();
            if ("".equals(content)) {
                holder.left.setVisibility(View.VISIBLE);
                holder.right.setVisibility(View.GONE);
                holder.typeLayout.setVisibility(View.GONE);
                holder.typeLayout1.setVisibility(View.GONE);
                holder.leftImg.setVisibility(View.VISIBLE);

                // 使用ImageLoader加载网络图片
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.drawable.img_default_bg)
                        .showImageOnFail(R.drawable.img_default_bg) // 设置加载失败的默认图片
                        .cacheInMemory(true) // 内存缓存
                        .cacheOnDisc(true) // sdcard缓存
                        .bitmapConfig(Config.RGB_565)// 设置最低配置
                        .imageScaleType(ImageScaleType.EXACTLY) //
                        .build();//
                ImageLoader.getInstance().displayImage(item.getPicture(), holder.ivLeftShow, options);

                final ArrayList<String> urls2 = new ArrayList<String>();
                urls2.add(item.getPicture());
                holder.ivLeftShow
                        .setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                StatisticalTools.eventCount(context,"userspicture");
                                imageBrower(urls2, "0");
                            }
                        });

            } else {
                holder.left.setVisibility(View.GONE);
                holder.leftImg.setVisibility(View.GONE);
                holder.typeLayout.setVisibility(View.VISIBLE);
                holder.typeLayout1.setVisibility(View.GONE);
                holder.left.setVisibility(View.VISIBLE);
                holder.right.setVisibility(View.GONE);
                holder.stateLayout.setVisibility(View.GONE);
                holder.helpLayout.setVisibility(View.GONE);
                holder.tvUContent.setText(content);
            }

            holder.photo.setImageDrawable(context.getResources().getDrawable(
                    R.drawable.que_head_icon));
            if (position > 0) {
                if (item.getCreateTime().equals(queDetailLists.get(position - 1).getCreateTime())) {
                    holder.tvTime.setVisibility(View.GONE);
                } else {
                    holder.tvTime.setVisibility(View.VISIBLE);
                    holder.tvTime.setText(TimeFormatUtils.formatMsgTime(item.getCreateTime()));
                }
            } else {
                holder.tvTime.setText(TimeFormatUtils.formatMsgTime(item.getCreateTime()));
            }

        } else if (type == 4) {
            LogUtils.e("药品item");
            holder.left.setVisibility(View.GONE);
            holder.right.setVisibility(View.VISIBLE);
            holder.rl_medicine.setVisibility(View.VISIBLE);
            holder.rl_detail_dcontent.setVisibility(View.GONE);
            holder.ivDphoto.setVisibility(View.GONE);
            holder.tvDTime.setVisibility(View.GONE);
            final MedicineBean medicineBean = item.getMedicineBean();
            if (null != medicineBean) {
                holder.tv_medicine_name.setText(TextUtils.isEmpty(medicineBean.getName()) ? "药品名称" : medicineBean.getName());
                ImageLoadUtils.INSTANCE.loadImageView(holder.iv_medicine, medicineBean.getImageUrl());
                //按产品要求 去掉点击事件
//                holder.rl_medicine.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        WebViewActivity.start((Activity) context, "药品详情", medicineBean.getH5Url());
//                    }
//                });
            }

        }

        holder.tv_title_content.setText(TextUtils.isEmpty(item.getTitle()) ? "无" : item.getTitle());


//        holder.tv_cash_reward.setVisibility(isAwardShow(item) ? View.VISIBLE : View.GONE);
        if (isAwardShow(item)) {
            String str = "";
            if("pay_ques".equals(item.getQues_from())){
                str = "指定";
            }else if("award".equals(item.getQues_from())){
                str = "现金悬赏";
            }else {
                str = "绩效";
            }
            if("0".equals(item.getAwardMoney())){
                holder.tv_cash_reward.setCompoundDrawables(null,null,null,null);
                TVUtils.setContentWithColor(holder.tv_cash_reward, new ColorText(str, R.color.c_f1c270));
            }else {
                Drawable drawable= context.getResources().getDrawable(R.drawable.icon_award);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                holder.tv_cash_reward.setCompoundDrawables(drawable,null,null,null);
                TVUtils.setContentWithColor(holder.tv_cash_reward, new ColorText(str, R.color.color_00c8aa), new ColorText(":" + item.getAwardMoney() + "元", R.color.gray_text));
            }
        }else {
            holder.tv_cash_reward.setCompoundDrawables(null,null,null,null);
            TVUtils.setContentWithColor(holder.tv_cash_reward, new ColorText("绩效", R.color.c_f1c270));
        }
        return convertView;
    }

    private boolean isAwardShow(QuestionSquareMsgItem item) {
       return  "award".equals(item.getQues_from())||"pay_ques".equals(item.getQues_from());
    }

    /**
     * 打开图片查看器
     *
     * @param urls2
     */
    protected void imageBrower(List urls2, String curentItem) {
        Intent intent = new Intent(context, SeePicActivty.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra("imgs", (ArrayList) urls2);
        intent.putExtra("curentItem", curentItem);
        intent.putExtra(IS_FROM, QUEDETAILADAPTER);
        context.startActivity(intent);
    }

    class ViewHolder {

        private LinearLayout left, right, llDpart;

        private ImageView photo, ivDphoto, ivLeftShow;

        private TextView tvQueDpart, tvDesPes, tvDesDatial, tvDTime,
                tvDesState, tvDesHelp, tvTime, tvContent, tvUContent, tv_cash_reward, tv_title_content;

        private LinearLayout stateLayout, helpLayout, typeLayout, typeLayout1,
                leftImg, rightImg;

        //药品相关
        private RelativeLayout rl_detail_dcontent;
        private RelativeLayout rl_medicine;
        private TextView tv_medicine_name;
        private ImageView iv_medicine;

    }
}
