package com.xywy.askforexpert.module.main.subscribe;


import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.ChannelItem;
import com.xywy.askforexpert.model.main.SubscribeTitleListBean;
import com.xywy.askforexpert.module.main.home.HomeService;
import com.xywy.askforexpert.module.main.media.newpart.MediaListActivityNew;
import com.xywy.askforexpert.module.main.subscribe.adapter.DragAdapter;
import com.xywy.askforexpert.widget.DragGrid;
import com.xywy.askforexpert.widget.OtherGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页 我的订阅  实际内容页面：频道订阅，媒体号订阅
 *
 * @author Jack Fang
 */
public class SubscribeListFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    private static final String TAG = "SubscribeListFragment";
    private static final String ARGS_KEY = "args_position";
    /**
     * 用户栏目对应的适配器，可以拖动
     */
    DragAdapter userAdapter;
    /**
     * 其它栏目对应的适配器
     */
    OtherAdapter otherAdapter;
    /**
     * 其它栏目列表
     */
    List<ChannelItem> otherChannelList = new ArrayList<>();
    /**
     * 用户栏目列表
     */
    List<ChannelItem> userChannelList = new ArrayList<>();
    /**
     * 是否在移动，由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。
     */
    boolean isMove = false;
    /**
     * 用以区分 频道订阅 和 媒体号订阅
     * 0：频道订阅， 1：媒体号订阅
     */
    /**
     * 用户栏目的GRIDVIEW
     */
    private DragGrid userGridView;
    /**
     * 其它栏目的GRIDVIEW
     */
    private OtherGridView otherGridView;
    private TextView subscribeMore;
    private TextView subscribeMoreM;
    private TextView my_category_tip_text;
    private TextView my_category_tip_text_m;
    private TextView mediaSubscribeSuggest;

    public SubscribeListFragment() {
        // Required empty public constructor
    }

    public static SubscribeListFragment newInstance(int position) {
        SubscribeListFragment fragment = new SubscribeListFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_KEY, position);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_media_subscribe_list, container, false);
        initView(view);

        my_category_tip_text.setText("单击增删频道，长按拖拽排序");
        CommonUtils.showOrHideViews(new View[]{mediaSubscribeSuggest, subscribeMore, subscribeMoreM, my_category_tip_text_m},
                new boolean[]{false, false, false, false});

        mediaSubscribeSuggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SubscribeMediactivity.class);
                startActivity(intent);
            }
        });

        subscribeMore.setOnClickListener(this);
        subscribeMoreM.setOnClickListener(this);

        initData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }


    private void initView(View view) {
        if (view != null) {
            mediaSubscribeSuggest = (TextView) view.findViewById(R.id.media_subscribe_suggest);
            my_category_tip_text = (TextView) view.findViewById(R.id.my_category_tip_text);
            my_category_tip_text_m = (TextView) view.findViewById(R.id.my_category_tip_text_m);
            userGridView = (DragGrid) view.findViewById(R.id.userGridView);
            otherGridView = (OtherGridView) view.findViewById(R.id.otherGridView);
            subscribeMore = (TextView) view.findViewById(R.id.subscribe_more);
            subscribeMoreM = (TextView) view.findViewById(R.id.subscribe_more_m);
            userGridView.setStalePos(1);
        }
    }

    private void initData() {

        initOrUpdateGridViews();

        getNewestDateFromServer();
    }

    /**
     * 从服务端获取最新数据
     */
    private void getNewestDateFromServer() {
        HomeService.getSubscribeTitleListInfo(new CommonResponse<SubscribeTitleListBean>() {
            @Override
            public void onNext(SubscribeTitleListBean subscribeTitleListBean) {
                initOrUpdateGridViews();
            }
        });
    }

    private void initOrUpdateGridViews() {
        userChannelList = ChannelManager.getManager().getUserChannel();
        otherChannelList = ChannelManager.getManager().getOtherChannel();
        LogUtils.d("user channel list = " + userChannelList.toString());
        LogUtils.d("other channel list = " + otherChannelList.toString());

        if (null == userAdapter) {
            userAdapter = new DragAdapter(getActivity(), userChannelList, userGridView);

            userGridView.setAdapter(userAdapter);
        }

        if (null == otherAdapter) {
            otherAdapter = new OtherAdapter(getActivity(), otherChannelList, otherGridView);

            otherGridView.setAdapter(otherAdapter);
        }


        // 设置GRIDVIEW的ITEM的点击监听
        otherGridView.setOnItemClickListener(this);
        userGridView.setOnItemClickListener(this);

        otherAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (otherAdapter.getCount() == 0) {
                    my_category_tip_text.setText("查看更多媒体号>>");
                } else {
                    my_category_tip_text.setText("单击增删媒体号，长按拖拽排序");
                }
            }

            @Override
            public void onInvalidated() {
                super.onInvalidated();
                if (otherAdapter.getCount() == 0) {
                    my_category_tip_text.setText("查看更多媒体号>>");
                } else {
                    my_category_tip_text.setText("单击增删媒体号，长按拖拽排序");
                }
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        // 如果点击的时候，之前动画还没结束，那么就让点击事件无效
        if (isMove) {
            return;
        }
        switch (parent.getId()) {
            case R.id.userGridView:
                // position为 0的不可以进行任何操作
                ChannelItem item = userChannelList.get(position);
                Map<String, String> map = new HashMap<>();
                map.put("channelId", String.valueOf(item.getId()));
                map.put("name", item.getName());
                // UMeng 计算事件
//                    CommonUtils.UMengAnalytics(getActivity(), "AlreadyChannel");
                MobclickAgent.onEventValue(getActivity(), "AlreadyChannel", map, 1);
                if (item.getSelected() != 2) {
                    final ImageView moveImageView = getView(view);
                    if (moveImageView != null) {
                        TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                        final int[] startLocation = new int[2];
                        newTextView.getLocationInWindow(startLocation);
                        final ChannelItem channel = ((DragAdapter) parent.getAdapter())
                                .getItem(position);// 获取点击的频道内容
                        otherAdapter.setVisible(false);
                        // 添加到最后一个
                        otherAdapter.addItem(channel);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    int[] endLocation = new int[2];
                                    // 获取终点的坐标
                                    otherGridView.getChildAt(otherGridView.getLastVisiblePosition())
                                            .getLocationInWindow(endLocation);
                                    MoveAnim(moveImageView, startLocation, endLocation, userGridView);
                                    userAdapter.setRemove(position);
                                } catch (Exception localException) {
                                    localException.printStackTrace();
                                }
                            }
                        }, 50L);
                    }
                }
                break;
            case R.id.otherGridView:
                final ImageView moveImageView1 = getView(view);
                if (moveImageView1 != null) {
                    final ChannelItem channel = ((OtherAdapter) parent.getAdapter()).getItem(position);
                    Map<String, String> map1 = new HashMap<>();
                    map1.put("channelId", String.valueOf(channel.getId()));
                    map1.put("name", channel.getName());
                    // UMeng 计算事件
//                        CommonUtils.UMengAnalytics(getActivity(), "NotChannel");
                    MobclickAgent.onEventValue(getActivity(), "NotChannel", map1, 1);

                    TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                    final int[] startLocation = new int[2];
                    newTextView.getLocationInWindow(startLocation);
                    userAdapter.setVisible(false);
                    // 添加到最后一个
                    userAdapter.addItem(channel);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                int[] endLocation = new int[2];
                                // 获取终点的坐标
                                userGridView.getChildAt(userGridView.getLastVisiblePosition())
                                        .getLocationInWindow(endLocation);
                                MoveAnim(moveImageView1, startLocation, endLocation, otherGridView);
                                otherAdapter.setRemove(position);
                            } catch (Exception localException) {
                                localException.printStackTrace();
                            }
                        }
                    }, 50L);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 点击ITEM移动动画
     *
     * @param moveView
     * @param startLocation
     * @param endLocation
     * @param clickGridView
     */
    private void MoveAnim(View moveView, int[] startLocation, int[] endLocation,
                          final GridView clickGridView) {
        int[] initLocation = new int[2];
        // 获取传递过来的VIEW的坐标
        moveView.getLocationInWindow(initLocation);
        // 得到要移动的VIEW,并放入对应的容器中
        final ViewGroup moveViewGroup = getMoveViewGroup();
        final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
        // 创建移动动画
        TranslateAnimation moveAnimation = new TranslateAnimation(
                startLocation[0], endLocation[0], startLocation[1],
                endLocation[1]);
        moveAnimation.setDuration(300L);// 动画时间
        // 动画配置
        AnimationSet moveAnimationSet = new AnimationSet(true);
        moveAnimationSet.setFillAfter(false);// 动画效果执行完毕后，View对象不保留在终止的位置
        moveAnimationSet.addAnimation(moveAnimation);
        mMoveView.startAnimation(moveAnimationSet);
        moveAnimationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                isMove = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                moveViewGroup.removeView(mMoveView);
                // instanceof 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView
                if (clickGridView instanceof DragGrid) {
                    otherAdapter.setVisible(true);
                    otherAdapter.notifyDataSetChanged();
                    userAdapter.remove();
                } else {
                    userAdapter.setVisible(true);
                    userAdapter.notifyDataSetChanged();
                    otherAdapter.remove();
                }
                isMove = false;
            }
        });
    }

    /**
     * 获取移动的VIEW，放入对应ViewGroup布局容器
     *
     * @param viewGroup
     * @param view
     * @param initLocation
     * @return
     */
    private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
        int x = initLocation[0];
        int y = initLocation[1];
        viewGroup.addView(view);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = x;
        mLayoutParams.topMargin = y;
        view.setLayoutParams(mLayoutParams);
        return view;
    }

    /**
     * 创建移动的ITEM对应的ViewGroup布局容器
     */
    private ViewGroup getMoveViewGroup() {
        ViewGroup moveViewGroup = (ViewGroup) getActivity().getWindow().getDecorView();
        LinearLayout moveLinearLayout = new LinearLayout(getActivity());
        moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        moveViewGroup.addView(moveLinearLayout);
        return moveLinearLayout;
    }

    /**
     * 获取点击的Item的对应View，
     *
     * @param view
     * @return
     */
    private ImageView getView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        ImageView iv = new ImageView(getActivity());
        iv.setImageBitmap(cache);
        return iv;
    }

    /**
     * 退出时候保存选择后数据库的设置
     */
    public void saveChannel() {

        DLog.i(TAG, "订阅资讯列表是否修改" + userAdapter.isListChanged());

        if (userAdapter.isListChanged()) {
            ChannelManager.getManager().deleteAllChannel();
            ChannelManager.getManager().saveUserChannel(userAdapter.getChannnelLst());
            ChannelManager.getManager().saveOtherChannel(otherAdapter.getChannnelLst());
            ChannelManager.getManager().saveDataToSp();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        StatisticalTools.eventCount(getActivity(), "Complete");
        //saveChannel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.subscribe_more:
                // 已订阅媒体号列表
//                StatisticalTools.eventCount(getActivity(), "AlreadyMediaMore");
//                Intent mediaIntent = new Intent(getActivity(), MediaCenterActivity.class);
//                mediaIntent.putExtra("type", 5);
//                startActivity(mediaIntent);
                MediaListActivityNew.start(getActivity());
                break;

            case R.id.subscribe_more_m:
                // 媒体号列表
                StatisticalTools.eventCount(getActivity(), "NotMediaMore");
//                Intent intent = new Intent(getActivity(), MediaServiceListActivity.class);
//                intent.putExtra(MediaServiceListActivity.TYPE_INTENT_KEY, Constants.TYPE_MEDIA);
//                startActivity(intent);
                MediaListActivityNew.start(getActivity());

                break;
        }
    }

}
