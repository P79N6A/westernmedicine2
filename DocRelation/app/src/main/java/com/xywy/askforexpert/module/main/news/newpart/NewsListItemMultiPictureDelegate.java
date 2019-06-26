package com.xywy.askforexpert.module.main.news.newpart;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.model.main.CommonNewsItemBean;
import com.xywy.askforexpert.module.main.news.newpart.bean.MainTabListItemBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by bailiangjin on 2017/2/4.
 */

public class NewsListItemMultiPictureDelegate implements ItemViewDelegate<MainTabListItemBean> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.news_list_item_layout_multipic;
    }

    @Override
    public boolean isForViewType(MainTabListItemBean item, int position) {
        return item.getType() == MainTabListItemBean.TYPE_NEWS_MULTI_PICTURE;
    }

    @Override
    public void convert(ViewHolder holder, MainTabListItemBean itemBean, int position) {
        LinearLayout ll_pictures = holder.getView(R.id.pics_layout);
        ImageView pic0 = holder.getView(R.id.pic_0);
        ImageView pic1 = holder.getView(R.id.pic_1);
        ImageView pic2 = holder.getView(R.id.pic_2);
        ll_pictures.setVisibility(View.VISIBLE);

        CommonNewsItemBean newsListData = itemBean.getNewsItemBean().getCommonNewsItemBean();

        // 不同item相同部分
        TextView newsListTitle = holder.getView(R.id.news_list_title);
        ImageView pic_single = holder.getView(R.id.news_list_only);
        TextView tv_time = holder.getView(R.id.news_list_time);
        TextView tv_read_count = holder.getView(R.id.news_list_read_count);


        if (newsListData != null) {
            newsListTitle.setText(newsListData.getTitle() == null ? "" : newsListData.getTitle());

            if (YMApplication.getNewsReadDataSource().getAllReadNews().contains(String.valueOf(newsListData.getId()))) {
                newsListTitle.setTextColor(Color.parseColor("#999999"));
            } else {
                newsListTitle.setTextColor(Color.parseColor("#333333"));
            }

            tv_time.setText(newsListData.getCreatetime() == null ? "" : newsListData.getCreatetime());
            tv_read_count.setText(String.valueOf(newsListData.getReadNum()));

            if (newsListData.getStyle() == null || "".equals(newsListData.getStyle())) {
                pic_single.setVisibility(View.GONE);
            } else {
                pic_single.setVisibility(View.VISIBLE);
                ImageLoadUtils.INSTANCE.loadImageView(pic_single, newsListData.getStyle());
            }

            // 3图item
            List<String> imgs = newsListData.getImgs();

            if (imgs != null && !imgs.isEmpty()) {
                if (imgs.get(0) != null) {
                    ImageLoadUtils.INSTANCE.loadImageView(pic0, imgs.get(0));
                }
                if (imgs.size() >= 2 && imgs.get(1) != null) {
                    ImageLoadUtils.INSTANCE.loadImageView(pic1, imgs.get(1));
                }
                if (imgs.size() >= 3 && imgs.get(2) != null) {
                    ImageLoadUtils.INSTANCE.loadImageView(pic2, imgs.get(2));
                }
            }
        }
    }
}
