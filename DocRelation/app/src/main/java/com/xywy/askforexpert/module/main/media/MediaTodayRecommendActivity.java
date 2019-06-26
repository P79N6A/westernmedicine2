package com.xywy.askforexpert.module.main.media;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.activity.CommonListBaseActivity;
import com.xywy.askforexpert.appcommon.base.fragment.CommonListFragment;
import com.xywy.askforexpert.module.docotorcirclenew.adapter.BaseUltimateRecycleAdapter;
import com.xywy.askforexpert.module.docotorcirclenew.model.IRecycleViewModel;
import com.xywy.askforexpert.module.main.media.adapter.MediaTodayRecommendAdapter;
import com.xywy.askforexpert.module.main.media.model.TodayRecommendMediaModel;
import com.xywy.askforexpert.widget.view.ultimaterecycleview.UltimateRecyclerView;
import com.xywy.uilibrary.recyclerview.adapter.DividerItemDecoration;


public class MediaTodayRecommendActivity extends CommonListBaseActivity {
    public  static void startActivity(Context context){
        Intent intent = new Intent(context, MediaTodayRecommendActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void initView() {
        super.initView();
        titleBarBuilder.setTitleText("今日订阅推荐");
        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                MediaTodayRecommendActivity.this, LinearLayoutManager.VERTICAL);
        itemDecoration.setDividerDrawble(getResources().getDrawable(R.drawable.item_divider_10dp));
        listFragment.setDivider(itemDecoration);
        listFragment.setCallBack(new CommonListFragment.CallBack() {
            @Override
            public void afterInit(UltimateRecyclerView ultimateRecyclerView) {
                ultimateRecyclerView.enableDefaultSwipeRefresh(false);
                ultimateRecyclerView.getState().setLoadmoreEnabled(false);
            }
        });
    }

    @Override
    protected IRecycleViewModel getRecycleViewModel() {
        return new TodayRecommendMediaModel(listFragment);
    }

    @Override
    protected BaseUltimateRecycleAdapter getRecycleViewAdapter() {
        return new MediaTodayRecommendAdapter(this);
    }
}
