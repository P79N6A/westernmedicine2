package com.xywy.askforexpert.module.docotorcirclenew.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseAdapter;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.DensityUtils;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.module.doctorcircle.SeePicActivty;

import java.util.ArrayList;
import java.util.List;

/**
 * 医圈-动态-图片适配器
 * Created by bailiangjin on 2016/10/27.
 */

public class CirclePictureListAdapter extends YMBaseAdapter<String> {

    public CirclePictureListAdapter(Activity activity, List<String> dataList) {
        super(activity, dataList);
    }

    public void clearData(){
        dataList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.item_circle_img_list;
    }

    @Override
    protected BaseViewHolder getViewHolder(View rootView) {
        return new ViewHolder(rootView);
    }

    public class ViewHolder extends BaseViewHolder<String> {


        private final ImageView iv_item;

        public ViewHolder(View rootView) {
            super(rootView);
            this.iv_item = (ImageView) rootView.findViewById(R.id.iv_item);
        }

        @Override
        public void show(int position, String item) {


            int width = AppUtils.getScreenWidth(context);
            int pictureWidth = 4 >= dataList.size() ? DensityUtils.dp2px(130) : (width - DensityUtils.dp2px(80)) / 3;
            ViewGroup.LayoutParams lp = iv_item.getLayoutParams();
            lp.width=pictureWidth;
            lp.height=pictureWidth;
            iv_item.setLayoutParams(lp);

            //填充数据
            fillData(position, item);
            //view 点击事件绑定
            addListener(position, item);
        }

        private void fillData(int position, String item) {
            //加载图片
            ImageLoadUtils.INSTANCE.loadImageView(iv_item, item);

        }

        private void addListener(final int position, final String item) {
            //
            iv_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO:跳转到查看图片详情Activity  可复用 试题模块的查看图片页面
                    //AnswerShowPicActivity.start(context,dataList,position);

                    Intent intent = new Intent(context, SeePicActivty.class);
                    intent.putExtra("type", 1 + "");
                    intent.putExtra("imgs", (ArrayList)dataList);
                    intent.putExtra("curentItem", position + "");
                    context.startActivity(intent);
                }
            });
        }
    }
}
