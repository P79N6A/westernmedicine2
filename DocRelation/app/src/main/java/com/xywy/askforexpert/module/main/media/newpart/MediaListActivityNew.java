package com.xywy.askforexpert.module.main.media.newpart;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.model.media.MediaNumberBean;
import com.xywy.askforexpert.module.docotorcirclenew.interfaze.IViewRender;
import com.xywy.askforexpert.module.docotorcirclenew.model.IRecycleViewModel;
import com.xywy.askforexpert.module.main.media.MediaDetailActivity;
import com.xywy.askforexpert.module.main.media.MediaServiceSearchActivity;
import com.xywy.askforexpert.module.main.media.model.MediaFirstModel;
import com.xywy.askforexpert.module.main.media.model.MediaRightModel;
import com.xywy.uilibrary.recyclerview.adapter.DividerItemDecoration;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import butterknife.Bind;

import static com.xywy.askforexpert.appcommon.old.Constants.TYPE_MEDIA;


/**
 * Created by bailiangjin on 2016/12/20.
 */

public class MediaListActivityNew extends YMBaseActivity {

    @Bind(R.id.et_search)
    EditText et_search;

//    @Bind(R.id.rl_search)
//    RelativeLayout rl_search;

    @Bind(R.id.rv_left)
    RecyclerView rv_left;

    @Bind(R.id.rv_right)
    RecyclerView rv_right;

    IRecycleViewModel leftModel,rightModel;
    private LeftBaseAdapter leftAdapter;

    private RightBaseAdapter rightAdapter;

    private LoadMoreWrapper rightLoadMoreWrapper;

    public static void start(Context activity){
        Intent intent = new Intent(activity,MediaListActivityNew.class);
        activity.startActivity(intent);
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_two_grid_listview;
    }

    @Override
    protected void beforeViewBind() {

    }

    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("订阅更多");
        initLeftRv();
        initRightRv();
        et_search.setFocusable(false);
        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticalTools.eventCount(v.getContext(), "MediaNumberSearchBox");
                MediaServiceSearchActivity.start(v.getContext(),TYPE_MEDIA);
            }
        });
    }

    private void initRightRv() {
        rightModel=new MediaRightModel(rightViewRender);
        rv_right.setLayoutManager(new LinearLayoutManager(this));

        rightAdapter = new RightBaseAdapter(this);
        rightAdapter.setData(rightModel.getData());
        rightLoadMoreWrapper = new LoadMoreWrapper(rightAdapter);
        rightLoadMoreWrapper.setLoadMoreView(LayoutInflater.from(this).inflate(R.layout.progress_dialog, rv_right, false));
        rightLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                rightModel.loadMore();
            }
        });

        rightAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                MediaNumberBean item=rightAdapter.getItem(position);
                MediaDetailActivity.startActivity(view.getContext(),item.getMid());
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        rv_right.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        setRightLoadMoreEnabled(false);
    }
    public  void setRightLoadMoreEnabled(boolean enable){
        if (enable){
            rv_right.setAdapter(rightLoadMoreWrapper);
        }else {
            rv_right.setAdapter(rightAdapter);
        }
    }

    private void initLeftRv() {
        leftModel=new MediaFirstModel(leftViewRender);
        rv_left.setLayoutManager(new LinearLayoutManager(this));
        leftAdapter = new LeftBaseAdapter(this);
        leftAdapter.setData(leftModel.getData());
        leftAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                LogUtils.e("左侧Item点击:" + position);
                leftAdapter.setSelectedItem(position);
                String id = leftAdapter.getDatas().get(position).getId();
                if ("1".equals(id)){
                    setRightLoadMoreEnabled(false);
                }else {
                    setRightLoadMoreEnabled(true);
                }
                rightModel.putExtra("id",id);
                rightModel.onRefresh();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        rv_left.setAdapter(leftAdapter);
    }

    @Override
    protected void initData() {
        leftModel.onRefresh();
    }

    IViewRender leftViewRender=new IViewRender(){

        @Override
        public void handleModelMsg(Message msg) {
            switch (msg.what) {
                case DataChanged:
                    leftAdapter.setData(leftModel.getData());
                    leftAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    IViewRender rightViewRender=new IViewRender(){

        @Override
        public void handleModelMsg(Message msg) {
            switch (msg.what) {
                case DataChanged:
                    rightAdapter.setData(rightModel.getData());
                    rv_right.getAdapter().notifyDataSetChanged();
                    break;
                case NoMore:
                    setRightLoadMoreEnabled(false);
                    break;
            }
        }
    };

}
