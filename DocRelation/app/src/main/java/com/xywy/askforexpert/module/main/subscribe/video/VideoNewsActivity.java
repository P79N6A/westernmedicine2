package com.xywy.askforexpert.module.main.subscribe.video;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.letv.skin.v4.V4PlaySkin;
import com.letv.utils.LetvNormalVideoHelper;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.fragment.LoadingDialogFragment;
import com.xywy.askforexpert.appcommon.interfaces.IRecyclerView;
import com.xywy.askforexpert.appcommon.interfaces.TextWatcherImpl;
import com.xywy.askforexpert.appcommon.net.retrofitTools.HttpRequestCallBack;
import com.xywy.askforexpert.appcommon.net.retrofitTools.RetrofitServices;
import com.xywy.askforexpert.appcommon.net.retrofitTools.RetrofitUtil;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.base.BaseBean;
import com.xywy.askforexpert.model.base.BaseBean2;
import com.xywy.askforexpert.model.discussDetail.DiscussPraiseList;
import com.xywy.askforexpert.model.videoNews.VideoNewsBean;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;
import com.xywy.askforexpert.module.doctorcircle.adapter.PraiseAvatarAdapter;
import com.xywy.askforexpert.module.main.media.MediaDetailActivity;
import com.xywy.askforexpert.module.main.service.linchuang.CommentInfoActivity1;
import com.xywy.askforexpert.module.main.service.media.InfoDetailActivity;
import com.xywy.askforexpert.module.main.service.person.PersonDetailActivity;
import com.xywy.askforexpert.module.message.share.ShareUtil;
import com.xywy.askforexpert.widget.module.topics.MyClickSpan;
import com.xywy.askforexpert.widget.view.MyListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.xywy.askforexpert.appcommon.old.Constants.MAX_NEWS_COMMENT_INPUT;

/**
 * 视频资讯 原生 stone
 * <p>
 * 放弃视频资讯就页重载，因为直接重载新视频，视频播放会卡住
 *
 * @author Jack Fang
 */
public class VideoNewsActivity extends AppCompatActivity
        implements /*IRecyclerView.OnLoadMoreListener,*/ IRecyclerView.OnItemClickListener {
    public static final String FROM_WHERE = "Media";
    public static final String NEWS_ID_INTENT_KEY = "NEWS_ID";
    public static final String VIDEO_UUID_INTENT_KEY = "video_uuid";
    public static final String VIDEO_VUID_INTENT_KEY = "video_vuid";
    public static final String VIDEO_TITLE_INTENT_KEY = "video_title";
    public static final String VIDEO_FROM_INTENT_KEY = "from";
    private static final String TAG = "VideoNewsActivity";
    private static final String REQUEST_PARAMS_A = "comment";
    private static final String REQUEST_PARAMS_C = "article";
    private static final int PAGE_SIZE = 10;
    private static final int DEFAULT_PAGE = 1;

    private static final String FOLLOW_PARAMS_A = "dcFriend";
    private static final String FOLLOW_ADD_PARAMS_M = "friend_add";
    private static final String FOLLOW_DEL_PARAMS_M = "friend_del";

    @Bind(R.id.video_news_letv_player)
    V4PlaySkin videoNewsLetvPlayer;
    @Bind(R.id.video_news_comment_list)
    RecyclerView videoNewsCommentList;
    @Bind(R.id.video_news_comment_input)
    EditText videoNewsCommentInput;
    @Bind(R.id.video_news_detail_comment)
    ImageView videoNewsDetailComment;
    @Bind(R.id.video_news_detail_praise)
    ImageView videoNewsDetailPraise;
    @Bind(R.id.video_news_detail_share)
    ImageView videoNewsDetailShare;
    @Bind(R.id.comment_layout)
    LinearLayout commentLayout;
    @Bind(R.id.video_news_comment_input_count)
    TextView videoNewsCommentInputCount;
    @Bind(R.id.comment_anonymous)
    AppCompatButton commentAnonymous;
    @Bind(R.id.comment_real_name)
    AppCompatButton commentRealName;
    @Bind(R.id.video_news_detail_comment_send_layout)
    LinearLayout videoNewsDetailCommentSendLayout;

    private Bundle videoNewsBundle;
    private String videoUUid;
    private String videoVUid;
    private String videoTitle;

    private List<VideoNewsBean.CommentBean> mDatas = new ArrayList<>();
    private VideoNewsAdapter adapter;

    private List<VideoNewsBean.RelatedBean> mRelatedDatas = new ArrayList<>();
    private RelatedListAdapter relatedListAdapter;
    private boolean relatedDataLoaded;

    private String url;
    private String title;
    private String imageUrl;
    private String from;

    /**
     * 区分原生视频和H5资讯
     */
    private int model;

    private TextView videoNewsSummaryTitle;
    private TextView videoNewsSummary;
    private TextView videoNewsFollow;
    private TextView videoNewsCommentCount;
    private LinearLayout videoNewsPraiseLayout;
    private GridView videoNewsPraiseList;
    private TextView videoNewsPraiseCount;
    private LoadingDialogFragment loadingDialogFragment;
    private String userid;
    private String newsId;
    private int currentPage = DEFAULT_PAGE;

    private Map<String, String> sMap = new HashMap<>();
    private Map<String, String> commentMap = new HashMap<>();
    private Map<String, String> followMap = new HashMap<>();
    private View headerView;
    private View footerView;
//    private View loadingFooter;

    /**
     * 媒体号id
     */
    private String mediaid;

    /**
     * 标记是否已关注媒体号
     */
    private boolean isFollowed;

    private View relatedFooter;

    /**
     * 相关阅读列表
     */
    private MyListView relatedList;

    /**
     * 匿名名字
     */
    private String anonymousName;

    /**
     * 匿名头像
     */
    private String anonymousPhoto;

    /**
     * 标记是否已点赞
     */
    private boolean isPraised;

    /**
     * 点赞列表
     */
    private List<DiscussPraiseList> praiseBeanLists;
    private RelativeLayout videoNewsSummaryLayout;

    /**
     * 是否为媒体号
     */
    private boolean isMedia;

    private LetvNormalVideoHelper videoHelper;

    /**
     * 标记播放器是否已经初始化过
     */
    private boolean playerInit;
    private int praiseNum;
    private TextView source_;
    private TextView loadingMore;
    private ProgressBar loadingProgress;
    private RelativeLayout loadingRl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_video_news);
        ButterKnife.bind(this);


        if (videoNewsCommentInput != null) {
            videoNewsCommentInput.requestFocus();
        }

        initVideoParams(getIntent());

        loadingDialogFragment = LoadingDialogFragment.newInstance("正在发送评论，请稍后……");

        if (YMUserService.isGuest()) {
            userid = "0";
        } else {
            userid = YMApplication.getPID();
        }

        sMap.put("a", REQUEST_PARAMS_A);
        sMap.put("c", REQUEST_PARAMS_C);
        sMap.put("pagesize", String.valueOf(PAGE_SIZE));
        sMap.put("userid", userid);

        commentMap.put("a", "comment");
        commentMap.put("c", "comment");
        commentMap.put("userid", userid);
        commentMap.put("toUserid", "0");
        commentMap.put("pid", "0");

        followMap.put("a", FOLLOW_PARAMS_A);

        videoNewsCommentList.setLayoutManager(new LinearLayoutManager(this));

        initHeaderView();
        registerListeners();
        initPlayer();

        adapter = new VideoNewsAdapter(this, mDatas, R.layout.video_news_comment_layout, videoNewsCommentList);
        adapter.addHeaderView(headerView);
        adapter.addFooterView(footerView);
        videoNewsCommentList.setAdapter(adapter);
//        adapter.setOnLoadMoreListener(this);
        adapter.setOnItemClickListener(this);

        relatedListAdapter = new RelatedListAdapter(this, mRelatedDatas, R.layout.related_news_item_layout);
        relatedList.setAdapter(relatedListAdapter);

        if (NetworkUtil.isNetWorkConnected()) {
            requestData();
        } else {
            Toast.makeText(this, "请检查您的网络连接", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * init player and start play
     */
    private void initPlayer() {
        try {
            if (this.videoNewsBundle == null) {
                genVideoBundle();
            }

            if (this.videoNewsBundle != null && !playerInit && videoHelper == null) {
                videoHelper = new LetvNormalVideoHelper();
                videoHelper.init(this.getApplicationContext(), videoNewsBundle, videoNewsLetvPlayer);
                playerInit = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "暂时无法播放此视频", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * init letv player data bundle
     */
    private void genVideoBundle() {
        if (videoTitle == null) {
            videoTitle = "";
        }
        DLog.d(TAG, "videoTITLE = " + videoTitle);
        if (videoUUid != null && !videoUUid.equals("") && videoVUid != null && !videoVUid.equals("")) {
            videoNewsBundle = CommonUtils.setVodParams(videoUUid, videoVUid, "", "", videoTitle);
        }
    }

    private void initHeaderView() {
        headerView = LayoutInflater.from(this).inflate(R.layout.video_news_detail_list_header, videoNewsCommentList, false);
        videoNewsSummaryLayout = (RelativeLayout) headerView.findViewById(R.id.video_news_summary_layout);
        videoNewsSummaryTitle = (TextView) headerView.findViewById(R.id.video_news_summary_title);
        videoNewsSummary = (TextView) headerView.findViewById(R.id.video_news_summary);
        source_ = (TextView) headerView.findViewById(R.id.source);
        videoNewsFollow = (TextView) headerView.findViewById(R.id.video_news_follow);
        videoNewsCommentCount = (TextView) headerView.findViewById(R.id.video_news_comment_count);
        videoNewsPraiseLayout = (LinearLayout) headerView.findViewById(R.id.video_news_praise_list_layout);
        videoNewsPraiseList = (GridView) headerView.findViewById(R.id.video_news_praise_list);
        videoNewsPraiseCount = (TextView) headerView.findViewById(R.id.video_news_praise_count);

        footerView = LayoutInflater.from(this).inflate(R.layout.video_news_footer, videoNewsCommentList, false);
        relatedFooter = footerView.findViewById(R.id.related_list);
        loadingRl = (RelativeLayout) footerView.findViewById(R.id.loading_more_rl);
        relatedList = (MyListView) footerView.findViewById(R.id.video_news_related_list);
        loadingMore = (TextView) footerView.findViewById(R.id.video_news_more_comment);
        loadingProgress = (ProgressBar) footerView.findViewById(R.id.videoNewsProgressBar);
//        loadingFooter = footerView.findViewById(R.id.loading_view);
    }

    private void registerListeners() {
        loadingMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticalTools.eventCount(VideoNewsActivity.this, "SeeMoreComments");
                requestData();
            }
        });

        videoNewsFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 订阅/取消订阅
                if (isFollowed) {
                    showConfirmDialog();
                } else {
                    follow();
                }
            }
        });

        videoNewsCommentInput.addTextChangedListener(new TextWatcherImpl() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                videoNewsCommentInputCount.setText(String.valueOf(s.length()));
                if (s.length() > MAX_NEWS_COMMENT_INPUT) {
                    videoNewsCommentInputCount.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                } else {
                    videoNewsCommentInputCount.setTextColor(Color.parseColor("#666666"));
                }
            }
        });


        relatedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 相关阅读点击
                VideoNewsBean.RelatedBean relatedBean = mRelatedDatas.get(position);
                newsId = relatedBean.getId();
                model = relatedBean.getModel();
                DLog.d(TAG, "related model = " + model + "newsid = " + newsId + ", " + relatedBean.getTitle());
                // 加载相关阅读
                Intent intent = new Intent();
                if (model == 4) { // 视频源资讯
                    intent.setClass(VideoNewsActivity.this, VideoNewsActivity.class);
                    intent.putExtra(NEWS_ID_INTENT_KEY, newsId);
                    intent.putExtra(VIDEO_TITLE_INTENT_KEY, relatedBean.getTitle());
                } else { // H5资讯
                    intent.setClass(VideoNewsActivity.this, InfoDetailActivity.class);
                    intent.putExtra("ids", relatedBean.getId());
                    intent.putExtra("url", relatedBean.getUrl());
                    intent.putExtra("title", relatedBean.getTitle());
                }
                startActivity(intent);
//                if (model != 4)
                VideoNewsActivity.this.finish();
            }
        });
    }

    private void showConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("取消关注后将不会再收到该媒体号的消息。");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                follow();
            }
        });
        builder.show();
    }

    @OnClick({R.id.video_news_comment_input, R.id.video_news_detail_comment, R.id.video_news_detail_praise,
            R.id.video_news_detail_share, R.id.comment_anonymous, R.id.comment_real_name})
    public void onClick(View view) {
        switch (view.getId()) {
            // 输入评论
            case R.id.video_news_comment_input:
                DLog.d(TAG, "start input comment");
                StatisticalTools.eventCount(this, "NativeVideoReview");
                CommonUtils.showOrHideViews(new View[]{videoNewsDetailComment, videoNewsDetailPraise,
                        videoNewsDetailShare, videoNewsDetailCommentSendLayout}, new boolean[]{false,
                        false, false, true});
                View focus = getCurrentFocus();
                if (focus != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.showSoftInputFromInputMethod(focus.getWindowToken(), InputMethodManager.SHOW_FORCED);
                }
                break;

            // TODO 去评论列表页
            case R.id.video_news_detail_comment:
                break;

            // 点赞
            case R.id.video_news_detail_praise:
                StatisticalTools.eventCount(this, "NativeVideoPointLike");
                praiseNews();
                break;

            // 分享
            case R.id.video_news_detail_share:
                StatisticalTools.eventCount(this, "NativeVideoSharing");
                if (imageUrl == null || imageUrl.equals("")) {
                    imageUrl = Constants.COMMON_SHARE_IMAGE_URL;
                }
                new ShareUtil.Builder()
                        .setTitle("医学咨询")
                        .setText(title)
                        .setTargetUrl(url)
                        .setImageUrl(imageUrl)
                        .setShareId(newsId)
                        .setShareSource("5")
                        .setVideoData(videoUUid, videoVUid)
                        .build(VideoNewsActivity.this).innerShare();

                break;

            // 匿名评论
            case R.id.comment_anonymous:
                StatisticalTools.eventCount(this, "NativeVideoReviewAnonymousPublication");
                //判断认证 stone
                DialogUtil.showUserCenterCertifyDialog(this, new MyCallBack() {
                    @Override
                    public void onClick(Object data) {
                        commentMap.put("level", "2");
                        sendComment(videoNewsCommentInput.getText().toString().trim());
                    }
                }, null, null);
                break;

            // 实名评论
            case R.id.comment_real_name:
                StatisticalTools.eventCount(this, "NativeVideoRealNameRelease");
                //判断认证 stone
                DialogUtil.showUserCenterCertifyDialog(this, new MyCallBack() {
                    @Override
                    public void onClick(Object data) {
                        commentMap.put("level", "0");
                        sendComment(videoNewsCommentInput.getText().toString().trim());
                    }
                }, null, null);
                break;
        }
    }

    /**
     * 更新页面数据
     *
     * @param data 数据
     */
    private void updatePage(VideoNewsBean data) {
        DLog.d(TAG, "currentPage = " + currentPage);
        // 更新页面数据
        if (currentPage == DEFAULT_PAGE) {
            VideoNewsBean.VideoBean video = data.getVideo();
            if (video != null) {
                videoUUid = video.getUu() == null ? "" : video.getUu();
                videoVUid = video.getVu() == null ? "" : video.getVu();

                DLog.d(TAG, "uuid = " + videoUUid + ", vuid = " + videoVUid);
                initPlayer();
            }

            url = data.getUrl() == null ? "" : data.getUrl();
            title = data.getTitle() == null ? "" : data.getTitle();
            imageUrl = data.getImage() == null ? "" : data.getImage();
            videoTitle = title;

            isPraised = data.getIspraise() == 1;
            isFollowed = data.getIs_subscription() == 1;
            isMedia = data.getIsmedia() == 1;

            if (isMedia) {
                videoNewsFollow.setVisibility(View.VISIBLE);

                VideoNewsBean.MediaBean media = data.getMedia();
                mediaid = media.getMediaid();

                if (isFollowed) {
                    videoNewsFollow.setText("已订阅");
                } else {
                    videoNewsFollow.setText("订阅");
                }
            }

            if (isPraised) {
                videoNewsDetailPraise.setImageResource(R.drawable.info_praised);
            } else {
                videoNewsDetailPraise.setImageResource(R.drawable.info_detail_img);
            }

            DLog.d(TAG, "anonymous name = " + data.getNickname() + ", anonymous photo = " + data.getPhoto());
            anonymousName = data.getNickname() == null ? "华佗" : data.getNickname();
            anonymousPhoto = data.getPhoto() == null ? "" : data.getPhoto();

            if (data.getVector() != null && !"".equals(data.getVector())) {
                String summaryContent = videoNewsSummaryTitle.getText() + data.getVector();
                SpannableString s = new SpannableString(summaryContent);
                s.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, videoNewsSummaryTitle.getText().length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                videoNewsSummary.setText(s);
            } else {
                videoNewsSummaryLayout.setVisibility(View.GONE);
            }
            setSource(data);

            videoNewsCommentCount.setText("实时评论（" + data.getCommentNum() + "）");
        }

        // 点赞头像
        praiseBeanLists = data.getPraise();
        praiseNum = Integer.parseInt(data.getPraiseNum());
        showPraiseAvatar(praiseBeanLists, praiseNum);

        List<VideoNewsBean.CommentBean> commentBeanList = data.getComment();
        final List<VideoNewsBean.RelatedBean> relatedBeanList = data.getRelated();

        if (commentBeanList == null && relatedBeanList == null && currentPage == DEFAULT_PAGE) {
//            loadingFooter.setVisibility(View.GONE);
            footerView.setVisibility(View.GONE);
            return;
        }

        // 相关阅读（随机出现的？）
        if (!relatedDataLoaded) {
            if (relatedBeanList != null && !relatedBeanList.isEmpty()) {
                relatedFooter.setVisibility(View.VISIBLE);
                if (!mRelatedDatas.isEmpty()) {
                    mRelatedDatas.clear();
                }
                mRelatedDatas.addAll(relatedBeanList);
                relatedListAdapter.notifyDataSetChanged();
                relatedDataLoaded = true;
            }
        }

        if (currentPage == DEFAULT_PAGE) { // 第一页，清空旧数据
            if (!mDatas.isEmpty()) {
                mDatas.clear();
            }
        }
        if (commentBeanList != null && !commentBeanList.isEmpty()) {
            if (commentBeanList.size() < 10) {
                loadingRl.setVisibility(View.GONE);
            }
            mDatas.addAll(commentBeanList);
            adapter.notifyDataSetChanged();
            currentPage++;
        } else {
            loadingRl.setVisibility(View.GONE);
        }
    }

    private void setSource(VideoNewsBean data) {
        String source = data.getSource() == null ? "" : data.getSource();
        String time = data.getCreatetime() == null ? "" : data.getCreatetime();
        String creator = data.getAuthor() == null ? "" : data.getAuthor();
        String s;
        if (!source.equals("") && !time.equals("")) {
            s = "来源：" + source + "    时间：" + time;
        } else {
            if (!source.equals("")) {
                s = "来源：" + source;
            } else if (!time.equals("")) {
                s = "时间：" + time;
            } else {
                s = "";
            }
        }
        if (!isMedia && !creator.equals("")) {
            s = s + "    发布者：" + creator;
        }

        DLog.d(TAG, s);
        SpannableString spannableString = new SpannableString(s);
        if (!isMedia) {
            int i = s.indexOf("发布者：");
            if (i != -1) {
                int start = i + "发布者：".length();
                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#0DC3CE")), start,
                        start + creator.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        if (isMedia) {
            source_.setMovementMethod(LinkMovementMethod.getInstance());
            MyClickSpan clickSpan = new MyClickSpan() {
                @Override
                public void onClick(View widget) {
                    DLog.d(TAG, "media name is clicked");
                    Intent intent = new Intent(VideoNewsActivity.this, MediaDetailActivity.class);
                    intent.putExtra("mediaId", mediaid);
                    VideoNewsActivity.this.startActivity(intent);
                    VideoNewsActivity.this.finish();
                }
            };
            int i = s.indexOf("来源：");
            if (i != -1) {
                int start = i + "来源：".length();
                spannableString.setSpan(clickSpan, start, start + source.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        source_.setText(spannableString);
    }

    /**
     * 点赞头像
     */
    private void showPraiseAvatar(List<DiscussPraiseList> praiseBeanList, int praiseNum) {
        final List<DiscussPraiseList> praiseList = new ArrayList<>();
//        List<DiscussPraiseList> praiseBeanList = data.getPraise();
        if (praiseBeanList != null && !praiseBeanList.isEmpty()) {
            DLog.d(TAG, "praise list = " + praiseBeanList.toString());
            videoNewsPraiseLayout.setVisibility(View.VISIBLE);
            DLog.d(TAG, "praise count = " + praiseBeanList.size());
            if (praiseBeanList.size() >= 5) {
                videoNewsPraiseCount.setText("等" + praiseNum + "人点赞");
                praiseList.addAll(praiseBeanList.subList(0, 5));
            } else {
                videoNewsPraiseCount.setText(praiseNum + "人点赞");
                praiseList.addAll(praiseBeanList);
            }
            PraiseAvatarAdapter praiseAdapter = new PraiseAvatarAdapter(this, praiseList, R.layout.praise_avatar_layout);
            videoNewsPraiseList.setAdapter(praiseAdapter);
            videoNewsPraiseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (praiseList.get(position).getUserid() != null) {
                        Intent intent = new Intent(VideoNewsActivity.this, PersonDetailActivity.class);
                        intent.putExtra("uuid", praiseList.get(position).getUserid());
                        intent.putExtra("isDoctor", "");
                        VideoNewsActivity.this.startActivity(intent);
                    } else {
                        Toast.makeText(VideoNewsActivity.this, "用户id错误", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            adapter.notifyDataSetChanged();
        } else {
            videoNewsPraiseLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 请求页面数据
     */
    private void requestData() {
        DLog.d(TAG, "video news id = " + newsId);
        CommonUtils.showOrHideViews(new View[]{loadingMore, loadingProgress}, new boolean[]{false, true});
        DLog.d(TAG, "current page = " + currentPage);
        sMap.put("page", String.valueOf(currentPage));
        sMap.put("id", newsId);
        sMap.put("sign", CommonUtils.computeSign(newsId));
        RetrofitServices.VideoNewsService service = RetrofitUtil.createService(RetrofitServices.VideoNewsService.class);
        Call<BaseBean<VideoNewsBean>> call = service.getData(sMap);
        RetrofitUtil.getInstance().enqueueCall(call, new HttpRequestCallBack<VideoNewsBean>() {
            @Override
            public void onSuccess(BaseBean<VideoNewsBean> baseBean) {
                // 数据请求成功，更新页面数据
                CommonUtils.showOrHideViews(new View[]{loadingMore, loadingProgress}, new boolean[]{true, false});
                VideoNewsBean data = baseBean.getData();
                DLog.d(TAG, "videonewbean = " + data.toString());
                updatePage(data);
                adapter.setIsLoading(false);
            }

            @Override
            public void onFailure(RequestState state, String msg) {
                // 失败
                CommonUtils.showOrHideViews(new View[]{loadingMore, loadingProgress}, new boolean[]{true, false});
                Toast.makeText(VideoNewsActivity.this, msg, Toast.LENGTH_SHORT).show();
                adapter.setIsLoading(false);
            }
        });
    }

    /**
     * 发送评论
     *
     * @param comment 评论内容
     */
    private void sendComment(final String comment) {
        if (YMUserService.isGuest()) {
            DialogUtil.LoginDialog(new YMOtherUtils(this).context);
            return;
        }

        if (comment.length() > MAX_NEWS_COMMENT_INPUT) {
            Toast.makeText(this, "字数限制在" + MAX_NEWS_COMMENT_INPUT + "字以内", Toast.LENGTH_SHORT).show();
            return;
        }
        if (comment.length() == 0 || comment.equals("")) {
            Toast.makeText(this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        commentMap.put("themeid", newsId);
        commentMap.put("content", comment);
        commentMap.put("sign", CommonUtils.computeSign(userid + commentMap.get("toUserid") + newsId));
        // 发送评论
        showLoadingDialog(true);
        RetrofitServices.SendNewsCommentService service = RetrofitUtil.createService(RetrofitServices.SendNewsCommentService.class);
        Call<BaseBean2> call = service.getData(commentMap);
        enqueueCall(call, new RequestInterfaceImpl() {
            @Override
            public void onSuccess(BaseBean2 data) {
                showLoadingDialog(false);
                // 更新页面数据
                VideoNewsBean.CommentBean bean = new VideoNewsBean.CommentBean();
                bean.setContent(comment);
                bean.setCreatetime(new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date(System.currentTimeMillis())));
                DLog.d(TAG, "login info = " + YMApplication.getLoginInfo());
                bean.setUserid(YMApplication.getPID());

                switch (commentMap.get("level")) {
                    // 实名评论
                    case "0":
                        bean.setHospital(YMApplication.getLoginInfo().getData().getHospital());
                        bean.setSubject(YMApplication.getLoginInfo().getData().getSubjectName());
                        bean.setNickname(YMApplication.getLoginInfo().getData().getRealname());
                        bean.setPhoto(YMApplication.getLoginInfo().getData().getPhoto());
                        bean.setLevel("0");
                        break;

                    // 匿名评论
                    case "2":
                        bean.setHospital("");
                        bean.setSubject("");
                        bean.setNickname(anonymousName);
                        bean.setPhoto(anonymousPhoto);
                        bean.setLevel("2");
                        break;
                }

                mDatas.add(0, bean);
                videoNewsCommentCount.setText("实时评论（" + mDatas.size() + "）");
                adapter.notifyDataSetChanged();
                videoNewsCommentList.smoothScrollToPosition(1);
                videoNewsCommentInput.setText("");
                CommonUtils.showOrHideViews(new View[]{videoNewsDetailPraise, videoNewsDetailShare,
                        videoNewsDetailCommentSendLayout}, new boolean[]{true, true, false});
                CommonUtils.showOrHideSoftKeyboard(VideoNewsActivity.this, false, getCurrentFocus());
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                showLoadingDialog(false);
            }
        });
    }

    /**
     * 点赞
     */
    private void praiseNews() {
        if (YMUserService.isGuest()) {
            DialogUtil.LoginDialog(new YMOtherUtils(this).context);
            return;
        }

        if (isPraised) {
            Toast.makeText(this, "已经赞过了", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("a", "actionAdd");
        map.put("c", "praise");
        map.put("type", "1");
        map.put("userid", userid);
        map.put("id", newsId);
        map.put("sign", CommonUtils.computeSign(userid + newsId));
        RetrofitServices.PraiseNewsService service = RetrofitUtil.createService(RetrofitServices.PraiseNewsService.class);
        Call<BaseBean2> call = service.getData(map);
        enqueueCall(call, new RequestInterfaceImpl() {
            @Override
            public void onSuccess(BaseBean2 data) {
                Toast.makeText(VideoNewsActivity.this, "点赞成功", Toast.LENGTH_SHORT).show();
                videoNewsDetailPraise.setImageResource(R.drawable.info_praised);
                DiscussPraiseList praiseList = new DiscussPraiseList();
                praiseList.setPhoto(YMApplication.getLoginInfo().getData().getPhoto());
                praiseList.setUserid(YMApplication.getPID());
                praiseBeanLists.add(0, praiseList);
                showPraiseAvatar(praiseBeanLists, praiseNum + 1);
            }
        });
    }

    /**
     * 订阅/取消订阅
     */
    private void follow() {
        if (YMUserService.isGuest()) {
            DialogUtil.LoginDialog(new YMOtherUtils(this).context);
            return;
        }

        if (isFollowed) {
            followMap.put("m", FOLLOW_DEL_PARAMS_M);
        } else {
            followMap.put("m", FOLLOW_ADD_PARAMS_M);
        }
        followMap.put("userid", userid);
        followMap.put("touserid", mediaid);
        followMap.put("bind", userid + mediaid);
        followMap.put("sign", CommonUtils.computeSign(userid + mediaid));
        RetrofitServices.MediaFollowService service = RetrofitUtil.createService(RetrofitServices.MediaFollowService.class);
        Call<BaseBean2> call = service.getData(followMap);
        enqueueCall(call, new RequestInterfaceImpl() {
            @Override
            public void onSuccess(BaseBean2 data) {
                // 订阅/取消订阅成功
                switch (followMap.get("m")) {
                    case FOLLOW_ADD_PARAMS_M:
                        Toast.makeText(VideoNewsActivity.this, "订阅成功", Toast.LENGTH_SHORT).show();
                        videoNewsFollow.setText("已订阅");
                        isFollowed = true;
                        if (from != null && FROM_WHERE.equals(from)) {
                            MediaDetailActivity.isFollowed = true;
                        }
                        break;

                    case FOLLOW_DEL_PARAMS_M:
                        Toast.makeText(VideoNewsActivity.this, "取消订阅成功", Toast.LENGTH_SHORT).show();
                        videoNewsFollow.setText("订阅");
                        isFollowed = false;
                        if (from != null && FROM_WHERE.equals(from)) {
                            MediaDetailActivity.isFollowed = false;
                        }
                        break;
                }
            }
        });
    }

    /**
     * 发送评论时的进度框
     */
    private void showLoadingDialog(boolean b) {
        if (loadingDialogFragment != null) {
            if (b) {
                loadingDialogFragment.show(getSupportFragmentManager(), TAG);
            } else {
                loadingDialogFragment.dismiss();
            }
        }
    }

    /**
     * 加载更多
     */
//    @Override
//    public void onLoadMore() {
//        DLog.d(TAG, "onLoadMore");
//
//        if (loadingFooter != null) {
//            loadingFooter.setVisibility(View.VISIBLE);
//        }
//
//        if (NetworkUtil.isNetWorkConnected(this)) {
//            requestData();
//        } else {
//            Toast.makeText(this, "网络不给力", Toast.LENGTH_SHORT).show();
//        }
//    }

    /**
     * TODO 评论点击
     */
    @Override
    public void OnItemClick(View view) {
//        int i = videoNewsCommentList.getChildAdapterPosition(view) - adapter.getHeaderViewCount();
        Intent intent = new Intent(this, CommentInfoActivity1.class);
        intent.putExtra("id", newsId);
        intent.putExtra("type", "consult");
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        intent.putExtra("imageUrl", imageUrl);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (videoNewsDetailCommentSendLayout.getVisibility() == View.VISIBLE) {
                CommonUtils.showOrHideViews(new View[]{videoNewsDetailPraise, videoNewsDetailShare,
                        videoNewsDetailCommentSendLayout}, new boolean[]{true, true, false});
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * onResume  onPause 方法分别放在onStart和onStop生命周期中，适配 Android N 分屏模式
     * <p>
     * In multi-window mode, an app can be in the paused state and still be visible to the user.
     * An app might need to continue its activities even while paused. For example, a video-playing
     * app that is in paused mode but is visible should continue showing its video. For this reason,
     * we recommend that activities that play video not pause the video in their onPause() handlers.
     * Instead, they should pause video in onStop(), and resume playback in onStart().
     */
    @Override
    protected void onStart() {
        super.onStart();

        if (videoHelper != null) {
            videoHelper.onResume();
        }

        View focus = getCurrentFocus();
        if (focus != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(focus.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (videoHelper != null) {
            videoHelper.onPause();
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (videoHelper != null) {
            videoHelper.onDestroy();
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        DLog.d(TAG, "onConfigurationChanged");
        CommonUtils.showOrHideSoftKeyboard(this, false, getCurrentFocus());
        CommonUtils.showOrHideViews(new View[]{videoNewsDetailComment, videoNewsDetailPraise,
                videoNewsDetailShare, videoNewsDetailCommentSendLayout}, new boolean[]{false,
                true, true, false});
        if (videoHelper != null) {
            videoHelper.onConfigurationChanged(newConfig);
        }
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        DLog.d(TAG, "onNewIntent");
//        if (videoHelper != null) {
//            videoHelper.onDestroy();
//            videoHelper = null;
//        }
//        relatedDataLoaded = false;
//        playerInit = false;
//        currentPage = DEFAULT_PAGE;
//        initVideoParams(intent);
//        initPlayer();
//        requestData();
//    }

    private void initVideoParams(Intent intent) {
        if (intent == null) {
            return;
        }

        newsId = intent.getStringExtra(NEWS_ID_INTENT_KEY);
        videoUUid = intent.getStringExtra(VIDEO_UUID_INTENT_KEY);
        videoVUid = intent.getStringExtra(VIDEO_VUID_INTENT_KEY);
        videoTitle = intent.getStringExtra(VIDEO_TITLE_INTENT_KEY);
        from = intent.getStringExtra(VIDEO_FROM_INTENT_KEY);
        genVideoBundle();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void enqueueCall(Call<BaseBean2> call, final RequestInterface callBack) {
        if (call != null) {
            call.enqueue(new Callback<BaseBean2>() {
                @Override
                public void onResponse(Call<BaseBean2> call, Response<BaseBean2> response) {
                    if (response.isSuccessful()) {
                        BaseBean2 baseBean2 = response.body();
                        if (baseBean2.getCode().equals("0") || baseBean2.getCode().equals("10000")
                                /*|| baseBean2.getCode().equals("1")*/) {
                            if (callBack != null) {
                                callBack.onSuccess(baseBean2);
                            }
                        } else {
                            if (callBack != null) {
                                callBack.onFailure(baseBean2.getMsg());
                            }
                        }
                    } else {
                        if (callBack != null) {
                            callBack.onFailure("请求失败");
                        }
                    }
                }

                @Override
                public void onFailure(Call<BaseBean2> call, Throwable t) {
                    if (callBack != null) {
                        callBack.onFailure(t.getMessage());
                    }
                }
            });
        }
    }

    interface RequestInterface {
        void onSuccess(BaseBean2 data);

        void onFailure(String msg);
    }

    abstract class RequestInterfaceImpl implements RequestInterface {
        @Override
        public void onFailure(String msg) {
            Toast.makeText(VideoNewsActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
