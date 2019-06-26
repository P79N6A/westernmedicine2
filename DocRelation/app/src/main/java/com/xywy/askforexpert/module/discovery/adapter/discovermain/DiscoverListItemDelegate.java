package com.xywy.askforexpert.module.discovery.adapter.discovermain;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.module.discovery.adapter.discovermain.bean.DiscoverItemBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by blj on 16/12/29.
 */
public class DiscoverListItemDelegate implements ItemViewDelegate<DiscoverItemBean>
{

    @Override
    public int getItemViewLayoutId()
    {
        return R.layout.item_discover_fragment_rv_item_horizon;
    }

    @Override
    public boolean isForViewType(DiscoverItemBean item, int position)
    {
        return item.getType()==DiscoverItemBean.TYPE_LIST;
    }

    @Override
    public void convert(ViewHolder holder, DiscoverItemBean itemBean, int position)
    {
//        LogUtils.e("DiscoverListItemBean:"+itemBean.getHorizonItemBean().getItemType());
        holder.setText(R.id.tv_title, itemBean.getHorizonItemBean().getTitle());
        holder.setText(R.id.tv_desc, itemBean.getHorizonItemBean().getDisc());
        holder.setBackgroundRes(R.id.iv_icon,itemBean.getHorizonItemBean().getIconResId());
    }
}
