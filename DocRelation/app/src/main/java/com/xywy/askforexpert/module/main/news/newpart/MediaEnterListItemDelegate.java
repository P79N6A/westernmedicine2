package com.xywy.askforexpert.module.main.news.newpart;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.module.main.media.newpart.MediaListActivityNew;
import com.xywy.askforexpert.module.main.news.newpart.bean.MainTabListItemBean;
import com.xywy.askforexpert.module.main.subscribe.SubscribeMediactivity;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by blj on 16/12/29.
 */
public class MediaEnterListItemDelegate implements ItemViewDelegate<MainTabListItemBean> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_main_media_enter;
    }

    @Override
    public boolean isForViewType(MainTabListItemBean item, int position) {
        return item.getType() == MainTabListItemBean.TYPE_MEDIA_ENTER;
    }

    @Override
    public void convert(ViewHolder holder, MainTabListItemBean itemBean, int position) {

        final RelativeLayout rl_my_subscribe = holder.getView(R.id.rl_my_subscribe);
        RelativeLayout rl_subscribe_more = holder.getView(R.id.rl_subscribe_more);

        rl_my_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到我的订阅
                //ToastUtils.shortToast("我的订阅");
               if (YMUserService.isLoginUser((Activity) rl_my_subscribe.getContext())){
                   SubscribeMediactivity.start((Activity) rl_my_subscribe.getContext());
               }
            }
        });

        rl_subscribe_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到订阅更多
                //ToastUtils.shortToast("订阅更多");
                StatisticalTools.eventCount(rl_my_subscribe.getContext(), "NotMediaMore");
                MediaListActivityNew.start((Activity) rl_my_subscribe.getContext());
            }
        });


    }
}
