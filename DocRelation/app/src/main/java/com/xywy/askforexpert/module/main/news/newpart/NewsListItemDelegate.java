package com.xywy.askforexpert.module.main.news.newpart;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.model.main.CommonNewsItemBean;
import com.xywy.askforexpert.module.main.news.newpart.bean.MainTabListItemBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by blj on 16/12/29.
 */
public class NewsListItemDelegate implements ItemViewDelegate<MainTabListItemBean> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.news_list_item_layout;
    }

    @Override
    public boolean isForViewType(MainTabListItemBean item, int position) {
        return item.getType() == MainTabListItemBean.TYPE_NEWS;
    }

    @Override
    public void convert(ViewHolder holder, MainTabListItemBean itemBean, int position) {
        CommonNewsItemBean newsListData = itemBean.getNewsItemBean().getCommonNewsItemBean();

        // 不同item相同部分
        TextView newsListTitle = holder.getView(R.id.news_list_title);
        ImageView pic_single = holder.getView(R.id.news_list_only);
        TextView tv_time = holder.getView(R.id.news_list_time);
        TextView tv_read_count = holder.getView(R.id.news_list_read_count);

        TextView tv_summary = holder.getView(R.id.news_list_summary);

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
                ImageLoadUtils.INSTANCE.loadImageView(pic_single, newsListData.getStyle(),R.drawable.top_trangle);
            }


            ImageView iv_image = holder.getView(R.id.news_list_img);
            if (null != iv_image) {
                iv_image.setVisibility(View.VISIBLE);
            }
            tv_summary.setVisibility(View.VISIBLE);

            ImageLoadUtils.INSTANCE.loadImageView(iv_image, newsListData.getImage());
            // 普通item
            tv_summary.setText(newsListData.getVector() == null ? "" : newsListData.getVector());

            String mediaName = TextUtils.isEmpty(newsListData.getMediaName()) ? "" : newsListData.getMediaName();
            TextView tv_org_name = holder.getView(R.id.tv_org_name);
            if (TextUtils.isEmpty(mediaName)) {
                tv_org_name.setVisibility(View.GONE);
            } else {
                tv_org_name.setVisibility(View.VISIBLE);
                tv_org_name.setText(mediaName);
            }
        }
    }


}
