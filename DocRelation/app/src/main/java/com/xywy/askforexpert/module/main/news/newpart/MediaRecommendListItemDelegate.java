package com.xywy.askforexpert.module.main.news.newpart;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.model.news.MediaRecommendItem;
import com.xywy.askforexpert.module.main.media.MediaDetailActivity;
import com.xywy.askforexpert.module.main.media.MediaTodayRecommendActivity;
import com.xywy.askforexpert.module.main.news.newpart.bean.MainTabListItemBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by blj on 16/12/29.
 */
public class MediaRecommendListItemDelegate implements ItemViewDelegate<MainTabListItemBean> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_main_media_today_recommend;
    }

    @Override
    public boolean isForViewType(MainTabListItemBean item, int position) {
        return item.getType() == MainTabListItemBean.TYPE_MEDIA_RECOMMEND;
    }

    @Override
    public void convert(ViewHolder holder, MainTabListItemBean itemBean, int position) {

        final List<MediaRecommendItem> mediaRecommendItemList = itemBean.getMediaRecommendItemBean().getMediaRecommendItemList();

        final TextView tv_more = holder.getView(R.id.tv_more);

        TextView tv_item1 = holder.getView(R.id.tv_item1);
        TextView tv_item2 = holder.getView(R.id.tv_item2);
        TextView tv_item3 = holder.getView(R.id.tv_item3);
        TextView tv_item4 = holder.getView(R.id.tv_item4);


        ImageView iv_icon1 = holder.getView(R.id.iv_icon1);
        ImageView iv_icon2 = holder.getView(R.id.iv_icon2);
        ImageView iv_icon3 = holder.getView(R.id.iv_icon3);
        ImageView iv_icon4 = holder.getView(R.id.iv_icon4);

        RelativeLayout rl_my_item1 = holder.getView(R.id.rl_my_item1);
        RelativeLayout rl_my_item2 = holder.getView(R.id.rl_my_item2);
        RelativeLayout rl_my_item3 = holder.getView(R.id.rl_my_item3);
        RelativeLayout rl_my_item4 = holder.getView(R.id.rl_my_item4);


        tv_item1.setText(getItemName(mediaRecommendItemList, 0));
        tv_item2.setText(getItemName(mediaRecommendItemList, 1));
        tv_item3.setText(getItemName(mediaRecommendItemList, 2));
        tv_item4.setText(getItemName(mediaRecommendItemList, 3));

        ImageLoadUtils.INSTANCE.loadImageView(iv_icon1, getItemImgUrl(mediaRecommendItemList, 0));
        ImageLoadUtils.INSTANCE.loadImageView(iv_icon2, getItemImgUrl(mediaRecommendItemList, 1));
        ImageLoadUtils.INSTANCE.loadImageView(iv_icon3, getItemImgUrl(mediaRecommendItemList, 2));
        ImageLoadUtils.INSTANCE.loadImageView(iv_icon4, getItemImgUrl(mediaRecommendItemList, 3));


        tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转更多今日推荐的媒体号列表页
                //ToastUtils.shortToast("跳转更多");
                MediaTodayRecommendActivity.startActivity(tv_more.getContext());
            }
        });


        final Activity context = (Activity) rl_my_item1.getContext();
        rl_my_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRecommendItem(context, mediaRecommendItemList, 0);
            }
        });

        rl_my_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRecommendItem(context, mediaRecommendItemList, 1);
            }
        });

        rl_my_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRecommendItem(context, mediaRecommendItemList, 2);
            }
        });

        rl_my_item4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRecommendItem(context, mediaRecommendItemList, 3);
            }
        });


    }

    private String getItemName(List<MediaRecommendItem> mediaRecommendItemList, int index) {
        if (null == mediaRecommendItemList) {
            return "服务端数据异常";
        }
        if (index >= mediaRecommendItemList.size()) {
            return "服务端推荐条目不足4个";
        }
        MediaRecommendItem mediaRecommendItem = mediaRecommendItemList.get(index);
        String name = mediaRecommendItem.getName();

        return TextUtils.isEmpty(name) ? "服务端Item名为空" : name;
    }

    private String getItemId(List<MediaRecommendItem> mediaRecommendItemList, int index) {
        if (null == mediaRecommendItemList) {
            return null;
        }
        if (index >= mediaRecommendItemList.size()) {
            return null;
        }
        MediaRecommendItem mediaRecommendItem = mediaRecommendItemList.get(index);
        String id = mediaRecommendItem.getMid();

        return id;
    }

    private String getItemImgUrl(List<MediaRecommendItem> mediaRecommendItemList, int index) {
        if (null == mediaRecommendItemList) {
            return null;
        }
        if (index >= mediaRecommendItemList.size()) {
            return null;
        }
        MediaRecommendItem mediaRecommendItem = mediaRecommendItemList.get(index);
        String imgUrl = mediaRecommendItem.getImg();

        return imgUrl;
    }


    public void gotoRecommendItem(Activity activity, List<MediaRecommendItem> mediaRecommendItemList, int index) {

        String recommendItemId = getItemId(mediaRecommendItemList, index);
        if (TextUtils.isEmpty(recommendItemId)) {
            ToastUtils.shortToast("服务端数据错误:推荐ItemId为空 无法跳转");
        } else {
            //跳转到推荐的具体媒体号详情页
            //ToastUtils.shortToast("跳转到推荐的具体媒体号详情页mid:" + recommendItemId);
            MediaDetailActivity.startActivity(activity, recommendItemId);
        }

    }
}
