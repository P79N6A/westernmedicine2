package com.xywy.askforexpert.module.discovery.adapter.discovermain;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.module.discovery.adapter.discovermain.bean.DiscoverItemBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * 直播入口 item
 * Created by blj on 17/12/29.
 */
public class DiscoverLiveShowItemDelegate implements ItemViewDelegate<DiscoverItemBean>
{

    @Override
    public int getItemViewLayoutId()
    {
        return R.layout.item_discover_fragment_rv_item_live_show;
    }

    @Override
    public boolean isForViewType(DiscoverItemBean item, int position)
    {
        return item.getType()==DiscoverItemBean.TYPE_LIVE_SHOW;
    }

    @Override
    public void convert(ViewHolder holder, DiscoverItemBean itemBean, int position)
    {

    }
}
