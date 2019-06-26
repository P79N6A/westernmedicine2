package com.xywy.askforexpert.module.docotorcirclenew.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.socialize.UMShareAPI;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.base.fragment.CommonListFragment;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.old.EventConstant;
import com.xywy.askforexpert.appcommon.utils.DensityUtils;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ViewCordinateUtil;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.topics.TopicDetailData;
import com.xywy.askforexpert.model.topics.TopicType;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;
import com.xywy.askforexpert.module.docotorcirclenew.adapter.CircleTopicAdapter;
import com.xywy.askforexpert.module.docotorcirclenew.model.IRecycleViewModel;
import com.xywy.askforexpert.module.docotorcirclenew.model.RecycleViewTopicModel;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.type.DCMsgType;
import com.xywy.askforexpert.module.docotorcirclenew.service.DCMiddlewareService;
import com.xywy.askforexpert.module.doctorcircle.DoctorCircleSendMessageActivty;
import com.xywy.askforexpert.module.doctorcircle.report.ReportActivity;
import com.xywy.askforexpert.module.doctorcircle.topic.CreateEditTopicActivity;
import com.xywy.askforexpert.module.doctorcircle.topic.FansListActivity;
import com.xywy.askforexpert.module.message.share.ShareUtil;
import com.xywy.askforexpert.widget.ActionItem;
import com.xywy.askforexpert.widget.TitlePopup;
import com.xywy.askforexpert.widget.view.ultimaterecycleview.RecyclerViewPositionHelper;
import com.xywy.askforexpert.widget.view.ultimaterecycleview.UltimateRecyclerView;
import com.xywy.retrofit.rxbus.Event;
import com.xywy.retrofit.rxbus.EventSubscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static com.xywy.askforexpert.appcommon.old.Constants.EDIT_TOPIC;

/**
 * 话题详情
 *
 * @author Jack Fang
 */
public class NewTopicDetailActivity extends YMBaseActivity
        implements View.OnClickListener,
        TitlePopup.OnItemOnClickListener {

    public static final int REQUEST_CODE = 3;
    public static final String TOPICID_INTENT_PARAM_KEY = "topicID";
    private static final String TAG = NewTopicDetailActivity.class.getSimpleName();
    private static final int INFO_CHECK_REQUEST_CODE = 99;
    private static final int EDIT_TOPIC_REQUEST_CODE = 6;
    //    private static final int DEFAULT_PAGE = 1;
    private static final String POUND = "#";

    private static final String MENU_EDIT = "编辑";
    private static final String MENU_FANS = "粉丝";
    private static final String MENU_SHARE = "分享";
    private static final String MENU_REPORT = "举报";


    private static final int HANDLER_UPDATE_HEADER = 0;
    public static int isDoctor = 0;
    @Bind(R.id.topic_detail_toolbar_title)
    TextView topicDetailToolbarTitle;
    @Bind(R.id.topic_detail_menu)
    ImageView topicDetailMenu;
    @Bind(R.id.topic_detail_toolbar)
    Toolbar topicDetailToolbar;

    @Bind(R.id.topic_detail_edit)
    FloatingActionButton topicDetailEdit;
//    RecyclerViewPositionHelper helper;
    TopicDetailData.ListBean headerData;

    // 分享相关参数
    private String shareUrl = "";
    private String shareTitle = "医脉话题";
    private String shareContent = "医脉话题";
    private String ShareImgUrl = "";
    private String userID;
    private TitlePopup menuPopup;
    private int topicId;
    private LinearLayout headerView;
    private CircleTopicAdapter adapter;
    private List<TopicType.TopicTypeBean> editTopicTypes;
    private String editTopicImgUrl;
    private String editIntro;
    private String themeuserid;
//    private int initY;
//    private RecycleViewModel recycleViewModel;

    public static void startActivity(Context con, int topicId) {
        Intent intent = new Intent(con, NewTopicDetailActivity.class);
        intent.putExtra(TOPICID_INTENT_PARAM_KEY, topicId);
        con.startActivity(intent);
    }
    /**
     * 初始化菜单actions
     *
     * @param b 是否是自己创建的话题
     */
    private void initMenuActions(boolean b) {
        menuPopup.cleanAction();
        if (b) {
            menuPopup.addAction(new ActionItem(getResources().getDrawable(R.drawable.topic_detail_menu_edit), MENU_EDIT));
        }
        menuPopup.addAction(new ActionItem(getResources().getDrawable(R.drawable.topic_detail_menu_fans), MENU_FANS));
        menuPopup.addAction(new ActionItem(getResources().getDrawable(R.drawable.topic_detail_menu_share), MENU_SHARE));
        menuPopup.addAction(new ActionItem(getResources().getDrawable(R.drawable.topic_detail_menu_report), MENU_REPORT));

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_new_topic_detail;
    }

    @Override
    protected void beforeViewBind() {
        userID = YMApplication.getUUid();
        topicId = getIntent().getIntExtra(TOPICID_INTENT_PARAM_KEY, -1);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    CommonListFragment topicFragment;
    IRecycleViewModel recycleViewModel;
    protected void initView() {
        hideCommonBaseTitle();
        hideStatusBarHeight();
        CommonUtils.setToolbar(this, topicDetailToolbar);
        // 4.4 以上系统重新设置高度和padding
        DLog.d(TAG, "status bar height = " + CommonUtils.getStatusBarHeight(this));
        CommonUtils.setTransparentBar(topicDetailToolbar, this, DensityUtils.dp2px( 48));
        initTopicFragment();
        registerListeners();
    }

    private void initTopicFragment() {
        setToolBarAlpha(0);
        headerView = (LinearLayout) getLayoutInflater().inflate(R.layout.topic_detail_header_layout, null);

        //         4.4 以上系统重新设置padding
        DLog.d(NewTopicDetailActivity.TAG, "action bar height = " + CommonUtils.getActionBarHeight(this));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            headerView.setPadding(0,
                    CommonUtils.getActionBarHeight(this),
                    0, 0);
        }

        topicFragment =  new CommonListFragment();
        adapter = new CircleTopicAdapter(this);
        recycleViewModel = new RecycleViewTopicModel(topicFragment, this, topicId + "");
        adapter.setUiListener(recycleViewModel.getItemClickListener());
        adapter.setData(recycleViewModel.getData());
        topicFragment.setRecycleViewModel(recycleViewModel);
        topicFragment.setAdapter(adapter);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, topicFragment).commit();
        topicFragment.setCallBack(new CommonListFragment.CallBack() {
            @Override
            public void afterInit(UltimateRecyclerView ultimateRecyclerView) {
                ultimateRecyclerView.setNormalHeader(headerView);
                final RecyclerViewPositionHelper helper = RecyclerViewPositionHelper.createHelper(ultimateRecyclerView.mRecyclerView);
                ultimateRecyclerView.mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);

                                int scrollOffset = -ViewCordinateUtil.getGalobalY(headerView)  + helper.findFirstVisibleItemPosition() * headerView.getHeight();
                                DLog.d(TAG, "scrollOffset = " + scrollOffset);
                                if (headerView.getHeight() > 0) {
                                    float alpha = scrollOffset * 1.0f / headerView.getHeight();
                                    DLog.d(TAG, "alpha = " + alpha);
                                    if (alpha > 1.0f) {
                                        alpha = 1.0f;
                                    }
                                    setToolBarAlpha(alpha);
                                }
                            }
                        });
                YmRxBus.registerTopicHeadMessage(new EventSubscriber<TopicDetailData.ListBean>() {
                    @Override
                    public void onNext(Event<TopicDetailData.ListBean> listBeanEvent) {
                        headerData = listBeanEvent.getData();
                        updateHead();
                    }
                }, NewTopicDetailActivity.this);
            }
        });
    }

    @Override
    protected void initData() {

    }

    private void registerListeners() {
        topicDetailEdit.setOnClickListener(this);
    }

    private void setToolBarAlpha(float alpha) {
        Drawable background = topicDetailToolbar.getBackground();
        background.setAlpha((int) (alpha * 255));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            topicDetailToolbar.setBackground(background);
        } else {
            topicDetailToolbar.setBackgroundDrawable(background);
        }
        topicDetailToolbarTitle.setAlpha(alpha);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        DLog.i(TAG, "onActivityResult" + requestCode);
        DLog.d(TAG, "requestCode = " + requestCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE:
                    int pos = data.getIntExtra("pos", -1);
                    recycleViewModel.deleteItem(adapter.getItem(pos));
                    break;

                case EDIT_TOPIC_REQUEST_CODE:
                case EventConstant.POST_MSG:
                case INFO_CHECK_REQUEST_CODE:
                    topicFragment.refresh();
                    recycleViewModel.onRefresh();
                    break;
            }
        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemClick(ActionItem item, int position) {
        String mTitle = (String) item.mTitle;
        switch (mTitle) {
            case MENU_EDIT:
                // 编辑
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(this).context);
                } else {
                    toEditTopic();
                }
                break;

            case MENU_FANS:
                // 粉丝
                Intent intent = new Intent(NewTopicDetailActivity.this, FansListActivity.class);
                intent.putExtra(FansListActivity.TOPICID_INTENT_KEY, String.valueOf(topicId));
                startActivity(intent);
                break;

            case MENU_SHARE:
                // 分享
                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(this).context);
                } else {
                    if (ShareImgUrl.equals("")) {
                        ShareImgUrl = Constants.COMMON_SHARE_IMAGE_URL;
                    }
                    if (shareContent.length() > 20) {
                        shareContent = shareContent.substring(0, 20);
                    }
                    LogUtils.d("shareUrl = " + shareUrl);
                    LogUtils.d("shareTitle = " + shareTitle);
                    LogUtils.d("shareContent = " + shareContent);
                    LogUtils.d("shareImgUrl = " + ShareImgUrl);

                    new ShareUtil.Builder()
                            .setTitle(shareTitle)
                            .setText(shareContent)
                            .setTargetUrl(shareUrl)
                            .setImageUrl(ShareImgUrl).build(NewTopicDetailActivity.this).outerShare();
                }
                break;

            case MENU_REPORT:
                // 举报

                if (YMUserService.isGuest()) {
                    DialogUtil.LoginDialog(new YMOtherUtils(this).context);
                } else {
//                    ReportActivity.start(this, String.valueOf(topicId), Constants.REPORT_STYLE_TOPIC);
                    DialogUtil.showUserCenterCertifyDialog(NewTopicDetailActivity.this, new MyCallBack() {
                        @Override
                        public void onClick(Object object) {
                            ReportActivity.start(NewTopicDetailActivity.this, String.valueOf(topicId), Constants.REPORT_STYLE_TOPIC);
                        }
                    }, null, null);
                }
                break;
        }

    }

    @OnClick({R.id.topic_detail_menu, R.id.topic_detail_edit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.topic_detail_menu:
                if (menuPopup != null) {
                    menuPopup.show(view);
                }
                break;
            case R.id.topic_detail_edit:
                // 发表动态
                StatisticalTools.eventCount(this, "yqTopicDetailWrite");
//                if (!DCMiddlewareService.isInvalidDCUser(DCMsgType.REAL_NAME, "发表实名动态")) {
//                    // 可以发表话题相关动态
//                    if (headerData!=null&&headerData.getTheme() != null) {
//                        Intent intent = new Intent(this, DoctorCircleSendMessageActivty.class);
//                        intent.putExtra("type", "1");
//                        intent.putExtra("topicName", headerData.getTheme());
//                        intent.putExtra("topicId", String.valueOf(topicId));
//                        intent.putExtra("themeuserid", themeuserid);
////                            startActivity(intent);
//                        NewTopicDetailActivity.this.startActivityForResult(intent, EventConstant.POST_MSG);
//                    } else {
////                        currentPage = DEFAULT_PAGE;
//                        topicFragment.refresh();
//                    }
//                }
                DialogUtil.showUserCenterCertifyDialog(NewTopicDetailActivity.this, new MyCallBack() {
                    @Override
                    public void onClick(Object object) {
                        // 可以发表话题相关动态
                        if (headerData!=null&&headerData.getTheme() != null) {
                            Intent intent = new Intent(NewTopicDetailActivity.this, DoctorCircleSendMessageActivty.class);
                            intent.putExtra("type", "1");
                            intent.putExtra("topicName", headerData.getTheme());
                            intent.putExtra("topicId", String.valueOf(topicId));
                            intent.putExtra("themeuserid", themeuserid);
//                            startActivity(intent);
                            NewTopicDetailActivity.this.startActivityForResult(intent, EventConstant.POST_MSG);
                        } else {
//                        currentPage = DEFAULT_PAGE;
                            topicFragment.refresh();
                        }
                    }
                }, null, null);
                break;
        }
    }


    private void toEditTopic() {
        Intent intent = new Intent(this, CreateEditTopicActivity.class);
        // 编辑 or 创建
        intent.putExtra(CreateEditTopicActivity.MODE_KEY, EDIT_TOPIC);
        // 话题名称
        String s = headerData.getTheme();
        intent.putExtra(CreateEditTopicActivity.TOPIC_NAME_KEY, s);
        // 话题封面url
        intent.putExtra(CreateEditTopicActivity.TOPIC_COVER_KEY, editTopicImgUrl);
        // 话题id
        intent.putExtra(CreateEditTopicActivity.TOPIC_ID_KEY, String.valueOf(topicId));
        // 话题简介
        intent.putExtra(CreateEditTopicActivity.TOPIC_INTRO_KEY, editIntro);
        // 话题分类 ArrayList<TopicType.TopicTypeBean>
        intent.putExtra(CreateEditTopicActivity.TOPIC_TYPE_KEY, (ArrayList) editTopicTypes);
        startActivityForResult(intent, EDIT_TOPIC_REQUEST_CODE);
    }

    private void updateHead() {
        // update header
        if (headerData != null) {
            editTopicTypes = headerData.getCat();
            editTopicImgUrl = headerData.getEditimg();
            editIntro = headerData.getEditdescript();

            topicDetailToolbarTitle.setText(POUND + headerData.getTheme() + POUND);

            if (menuPopup == null) {
                menuPopup = new TitlePopup(NewTopicDetailActivity.this, LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
            }

            if (headerData.getUserid() != null && !headerData.getUserid().equals("")) {
                themeuserid = headerData.getUserid();
            } else {
                themeuserid = "0";
            }

            if (YMUserService.isGuest()) {
                initMenuActions(false);
//                menuPopup.cleanAction();
//                menuPopup.addAction(new ActionItem(getResources().getDrawable(R.drawable.topic_detail_menu_fans), MENU_FANS));
//                menuPopup.addAction(new ActionItem(getResources().getDrawable(R.drawable.topic_detail_menu_share), MENU_SHARE));
//                menuPopup.addAction(new ActionItem(getResources().getDrawable(R.drawable.topic_detail_menu_report), MENU_REPORT));
            } else {
                if (userID.equals(headerData.getUserid())) {
                    initMenuActions(true);
                } else {
                    initMenuActions(false);
                }
            }
            menuPopup.setItemOnClickListener(NewTopicDetailActivity.this);


            LogUtils.d("topic_share_json:" + GsonUtils.toJson(headerData));
            // 设置分享数据
            shareUrl = headerData.getUrl();
            shareTitle = headerData.getTheme();
            shareContent = headerData.getDescription();
            ShareImgUrl = headerData.getImage();

            headerView.setTag(headerData);
            adapter.notifyDataSetChanged();
        }
    }
}
