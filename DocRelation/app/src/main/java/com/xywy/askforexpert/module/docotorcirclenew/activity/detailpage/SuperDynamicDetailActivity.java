package com.xywy.askforexpert.module.docotorcirclenew.activity.detailpage;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.ImageLoadUtils;
import com.xywy.askforexpert.appcommon.utils.KeyBoardUtils;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.model.api.BaseStringResultBean;
import com.xywy.askforexpert.model.doctor.CommentBean;
import com.xywy.askforexpert.model.doctor.Data;
import com.xywy.askforexpert.model.doctor.DynamicDtaile;
import com.xywy.askforexpert.model.doctor.MoreMessage;
import com.xywy.askforexpert.model.doctor.Share;
import com.xywy.askforexpert.model.doctor.Touser;
import com.xywy.askforexpert.model.doctor.User;
import com.xywy.askforexpert.model.newdoctorcircle.CommentResultBean;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;
import com.xywy.askforexpert.module.docotorcirclenew.adapter.CirclePictureListAdapter;
import com.xywy.askforexpert.module.docotorcirclenew.adapter.DetailCommentListAdapter;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.PraiseParamBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.ShareClickParamBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.ShareParamBean;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.type.DCMsgType;
import com.xywy.askforexpert.module.docotorcirclenew.requestbean.type.ShareDirectionType;
import com.xywy.askforexpert.module.docotorcirclenew.service.DCMiddlewareService;
import com.xywy.askforexpert.module.docotorcirclenew.service.DcCommonClickService;
import com.xywy.askforexpert.module.docotorcirclenew.service.DoctorCircleService;
import com.xywy.askforexpert.module.docotorcirclenew.utils.RichTextUtils;
import com.xywy.askforexpert.module.doctorcircle.adapter.PraiseHeadAdapter;
import com.xywy.askforexpert.widget.PasteEditText;
import com.xywy.askforexpert.widget.view.MyGridView;

import java.util.List;

import butterknife.Bind;
import rx.Subscriber;

/**
 * 动态详情页 抽象类 包括 实名页 和匿名页公有的逻辑 两者区别化的实现在 实名、匿名Activity中
 * Created by bailiangjin on 2016/11/15.
 */
public abstract class SuperDynamicDetailActivity extends YMBaseActivity implements DetailPageLifecycle {

    @Bind(R.id.lv_comment)
    ListView lv_comment;

    @Bind(R.id.ll_bottom)
    LinearLayout ll_bottom;

    @Bind(R.id.pet_comment)
    PasteEditText pet_comment;

    @Bind(R.id.btn_send)
    Button btn_send;

    View headView;
    View footView;

    //医生信息
    ImageView iv_head_icon;

    //分隔线
    View v_divider;

    TextView tv_name;
    TextView tv_post;
    TextView tv_hospital;

    //更多下拉框 实名item用
    ImageView iv_more;

    /**
     * 删除 按钮 匿名item用
     */
    RelativeLayout rl_delete;

    //内容
    TextView tv_title;
    TextView tv_content;

    //图片GridView
    MyGridView gv_pictures;

    //分享
    LinearLayout ll_share;
    ImageView iv_share_photo;
    TextView tv_share_title;

    //底部 时间 点赞数 评论数
    RelativeLayout rl_bottom;
    TextView tv_time;
    TextView tv_praise_number;
    TextView tv_comment_number;
    TextView tv_share;


    //点赞头像 点赞人数
    RelativeLayout rl_praise;
    MyGridView gv_praise_head;
    TextView tv_praise_person_number;

    View praise_divider;

    /**
     * 动态Id
     */
    String msgId;


    DcCommonClickService clickService;

    DetailCommentListAdapter detailCommentListAdapter;

    PraiseHeadAdapter praiseListAdapter;

    CirclePictureListAdapter circlePictureListAdapter;

    boolean isLoadingMore;

    int pageCount = 1;

    DynamicDtaile mDynamicDtaile;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_dynamic_realname_detail;
    }

    @Override
    protected void beforeViewBind() {
        clickService = new DcCommonClickService();
        msgId = getIntent().getStringExtra("msgId");
    }

    @Override
    protected void initView() {
        setTitle();
        headView = View.inflate(SuperDynamicDetailActivity.this, R.layout.item_circle_content_cell, null);
        initHeaderView(headView);

        lv_comment.addHeaderView(headView);

        footView = View.inflate(SuperDynamicDetailActivity.this, R.layout.loading_more, null);
        lv_comment.addFooterView(footView);
        footView.setVisibility(View.GONE);


    }

    @Override
    protected void initData() {

        LogUtils.d("msgId:" + msgId);
        if (TextUtils.isEmpty(msgId)) {
            shortToast("初始参数为空");
            return;
        }

        initPageData();
    }

    //具体实现---------------------------------------------------------

    /**
     * 填充头部数据
     */
    private void initHeaderView(View headView) {

        //医生信息
        iv_head_icon = (ImageView) headView.findViewById(R.id.iv_head_icon);

        tv_name = (TextView) headView.findViewById(R.id.tv_name);
        tv_post = (TextView) headView.findViewById(R.id.tv_post);
        tv_hospital = (TextView) headView.findViewById(R.id.tv_hospital);
        v_divider = headView.findViewById(R.id.v_divider);

        //更多下拉框
        iv_more = (ImageView) headView.findViewById(R.id.iv_more);

        // 匿名 删除按钮
        rl_delete = (RelativeLayout) headView.findViewById(R.id.rl_delete);

        //内容
        tv_title = (TextView) headView.findViewById(R.id.tv_title);
        tv_content = (TextView) headView.findViewById(R.id.tv_content);

        //图片GridView
        gv_pictures = (MyGridView) headView.findViewById(R.id.gv_pictures);


        //分享
        ll_share = (LinearLayout) headView.findViewById(R.id.ll_share);
        iv_share_photo = (ImageView) headView.findViewById(R.id.iv_share_photo);
        tv_share_title = (TextView) headView.findViewById(R.id.tv_share_title);


        //底部 时间 点赞数 评论数
        rl_bottom = (RelativeLayout) headView.findViewById(R.id.rl_bottom);
        tv_time = (TextView) headView.findViewById(R.id.tv_time);
        tv_praise_number = (TextView) headView.findViewById(R.id.tv_praise_number);
        tv_comment_number = (TextView) headView.findViewById(R.id.tv_comment_number);
        tv_share = (TextView) headView.findViewById(R.id.tv_share);


        //点赞头像 点赞人数
        rl_praise = (RelativeLayout) headView.findViewById(R.id.rl_praise);
        gv_praise_head = (MyGridView) headView.findViewById(R.id.gv_praise_head);
        tv_praise_person_number = (TextView) headView.findViewById(R.id.tv_praise_person_number);
        praise_divider = headView.findViewById(R.id.praise_divider);
    }

    /**
     * 初始化页面数据
     */
    private void initPageData() {
        DoctorCircleService.getDynamicDetailPageInfo(msgId, new CommonResponse<DynamicDtaile>() {
            @Override
            public void onNext(DynamicDtaile dynamicDtaile) {
                mDynamicDtaile = dynamicDtaile;
                LogUtils.d("获取成功");

                fillHeaderViewData(mDynamicDtaile);

                initCommentListView(mDynamicDtaile);
            }
        });
    }


    /**
     * 初始化 评论列表
     *
     * @param dynamicDtaile
     */
    private void initCommentListView(DynamicDtaile dynamicDtaile) {
        List<CommentBean> commentBeanList = dynamicDtaile.getData().getCommlist();
        if (null != commentBeanList && !commentBeanList.isEmpty()) {
            fillListViewData(commentBeanList);
            praise_divider.setVisibility(View.VISIBLE);
        } else {
            LogUtils.d("评论列表为空");
            //没有item时 保证Header显示
            lv_comment.setAdapter(null);
            praise_divider.setVisibility(View.GONE);
        }
    }


    /**
     * 填充头部数据
     */
    private void fillHeaderViewData(final DynamicDtaile dynamicDtaile) {

        final Data data = dynamicDtaile.getData();
        ImageLoadUtils.INSTANCE.loadImageView(iv_head_icon, data.getUserphoto());
        tv_name.setText(data.getNickname());

        setHeaderViewVisibility();

        List<String> imgList = data.getImgs();
        if (null != imgList && !imgList.isEmpty()) {
            int numberOneLine = imgList.size() > 4 ? 3 : 2;
            gv_pictures.setNumColumns(numberOneLine);
            if (null == circlePictureListAdapter) {
                circlePictureListAdapter = new CirclePictureListAdapter(SuperDynamicDetailActivity.this, imgList);
                gv_pictures.setAdapter(circlePictureListAdapter);
            } else {
                circlePictureListAdapter.setData(imgList);
                circlePictureListAdapter.notifyDataSetChanged();
            }

        } else {
            if (null != circlePictureListAdapter) {
                circlePictureListAdapter.clearData();
            }
        }

        //tv_title.setText(data.getSubject());

        String content = data.content;

        //填充文本内容内容
        RichTextUtils.parseCircleMsgContent(SuperDynamicDetailActivity.this, tv_content, content);


        tv_time.setText(data.getCreatetime());

        final int praiseNumber = null == data.getPraiselist() ? 0 : data.getPraiselist().size();
        tv_praise_number.setText("" + praiseNumber);
        tv_praise_number.setSelected("1".equals(data.is_praise));
        tv_comment_number.setText(data.getCommentNum());
        if (0 == praiseNumber) {
            rl_praise.setVisibility(View.GONE);
        } else {
            rl_praise.setVisibility(View.VISIBLE);
            tv_praise_person_number.setText(praiseNumber + "人点赞");
        }

        tv_praise_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                clickService.onPraiseClick(new PraiseParamBean(msgId, "", "", getMSGType(), new CommonResponse() {
//                    @Override
//                    public void onNext(Object o) {
//                        if ("1".equals(data.getIs_praise())) {
//                            shortToast("取消点赞成功");
//                        } else {
//                            shortToast("点赞成功");
//                        }
//                        updateHeaderData();
//                    }
//                }));

                //判断认证 stone
                DialogUtil.showUserCenterCertifyDialog(SuperDynamicDetailActivity.this, new MyCallBack() {
                    @Override
                    public void onClick(Object t) {

                        clickService.onPraiseClick(new PraiseParamBean(msgId, "", "", getMSGType(), new CommonResponse() {
                            @Override
                            public void onNext(Object o) {
                                if ("1".equals(data.getIs_praise())) {
                                    shortToast("取消点赞成功");
                                } else {
                                    shortToast("点赞成功");
                                }
                                updateHeaderData();
                            }
                        }));
                    }
                }, null, null);
            }
        });

        final Share shareData = data.getShare();

        if (null == shareData || TextUtils.isEmpty(shareData.getShare_title())) {
            ll_share.setVisibility(View.GONE);
        } else {
            ll_share.setVisibility(View.VISIBLE);
            tv_share_title.setText(shareData.getShare_title());
            ImageLoadUtils.INSTANCE.loadImageView(iv_share_photo, shareData.getShare_img());
            ll_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickService.onShareItemClick(SuperDynamicDetailActivity.this, new ShareClickParamBean(shareData.getShare_source(), shareData.getShare_title(), shareData.getShare_link(), shareData.getShare_img(), shareData.getShare_other()));
                }
            });
        }

        tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String shareImgUrl = null != data.getImgs() && !data.getImgs().isEmpty() ? data.getImgs().get(0) : "";
//                clickService.onShareBtnClick(SuperDynamicDetailActivity.this, new ShareParamBean(data.getContent(), data.getContent(), data.getUrl(), shareImgUrl, data.getId(), ShareDirectionType.OUTER_SHARE));
                DialogUtil.showUserCenterCertifyDialog(SuperDynamicDetailActivity.this, new MyCallBack() {
                    @Override
                    public void onClick(Object object) {
                        String shareImgUrl = null != data.getImgs() && !data.getImgs().isEmpty() ? data.getImgs().get(0) : "";
                        clickService.onShareBtnClick(SuperDynamicDetailActivity.this, new ShareParamBean(data.getContent(), data.getContent(), data.getUrl(), shareImgUrl, data.getId(), ShareDirectionType.OUTER_SHARE));
                    }
                }, null, null);
            }
        });

        tv_comment_number.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        if (DCMiddlewareService.isInvalidDCUser(getMSGType(), "评论实名动态")) {
//                            return;
//                        }
//                        bindCommentMsgEvent(mDynamicDtaile.getUser().getUserid());
//                        KeyBoardUtils.openKeybord(pet_comment);
                        DialogUtil.showUserCenterCertifyDialog(SuperDynamicDetailActivity.this, new MyCallBack() {
                            @Override
                            public void onClick(Object object) {
                                bindCommentMsgEvent(mDynamicDtaile.getUser().getUserid());
                                KeyBoardUtils.openKeybord(pet_comment);
                            }
                        }, null, null);

                    }
                });
        bindCommentMsgEvent(mDynamicDtaile.getUser().getUserid());

        setHeaderViewData();
    }


    /**
     * 刷新Header数据
     */
    private void updateHeaderData() {
        DoctorCircleService.getDynamicDetailPageInfoWithLastPage(msgId, pageCount, new CommonResponse<DynamicDtaile>() {
            @Override
            public void onNext(DynamicDtaile dynamicDtaile) {
                //刷新数据赋值
                mDynamicDtaile = dynamicDtaile;
                LogUtils.d("获取新数据成功");

                fillHeaderViewData(dynamicDtaile);

            }
        });
    }

    /**
     * 给输入框 组件绑定评论时间
     */
    private void bindCommentMsgEvent(String curUserId) {
        initOrUpdateInputView(curUserId, null, null, null, new CommonResponse<CommentResultBean>() {
            @Override
            public void onNext(CommentResultBean commentResultBean) {

                if (null != commentResultBean) {
                    if (BaseStringResultBean.CODE_SUCCESS.equals(commentResultBean.getCode())) {
                        //评论成功
                        LogUtils.d("评论成功");
                        shortToast("评论成功");
                        afterComment(commentResultBean, null);

                    } else {
                        LogUtils.d("评论失败");
                        shortToast("评论失败");

                    }
                }
            }
        });

    }

    /**
     * 评论成功 刷新界面数据
     *
     * @param commentResultBean
     * @param commentBean
     */
    private void afterComment(CommentResultBean commentResultBean, CommentBean commentBean) {
        String curCommentNumber = tv_comment_number.getText().toString().trim();
        if ("0".equals(curCommentNumber)) {
            //初始化 获取页面数据
            initPageData();
        } else {
            ///刷新界面数据
            updateHeaderData();
            User newUser = mDynamicDtaile.getUser();
            CommentBean newCommentBean = generateMyComment(commentResultBean.getCommentId(), commentResultBean.getCommentStr(), newUser, commentBean);
            detailCommentListAdapter.addData(newCommentBean);
        }
    }

    /**
     * 生成 回复数据 添加到评论列表（评论成功后）
     *
     * @param commentId
     * @param commentStr
     * @param newUser
     * @param commentBean
     * @return
     */
    @NonNull
    private CommentBean generateMyComment(String commentId, String commentStr, User newUser, CommentBean commentBean) {
        CommentBean newCommentBean = new CommentBean();

        Touser newToUser = null;

        if (!"0".equals(commentId) && null != commentBean) {// 对评论评论
            newToUser = new Touser();
            newToUser.userid = commentBean.user.userid;
            newToUser.nickname = commentBean.user.nickname;
            newToUser.photo = commentBean.user.photo;
        }
        newCommentBean.id = commentId;
        newCommentBean.content = commentStr;
        newCommentBean.user = newUser;
        newCommentBean.touser = newToUser;
        return newCommentBean;
    }

    /**
     * 填充评论列表数据
     */
    private void fillListViewData(final List<CommentBean> commentBeanList) {

        if (null == commentBeanList || commentBeanList.isEmpty()) {
            LogUtils.d("评论列表为空");
            praise_divider.setVisibility(View.GONE);
            return;
        }
        praise_divider.setVisibility(View.VISIBLE);
        detailCommentListAdapter = new DetailCommentListAdapter(SuperDynamicDetailActivity.this, commentBeanList, msgId, getMSGType() == DCMsgType.REAL_NAME);
        lv_comment.setAdapter(detailCommentListAdapter);
        lv_comment.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final CommentBean commList = detailCommentListAdapter.getItem(position - 1);
                if (YMUserService.getCurUserId().equals(commList.getUser().getUserid())) {

                    String commentId = commList.getId();
                    showDeleteCommentDialog(commentId);
                }
                return true;
            }
        });

        lv_comment.setOnScrollListener(new AbsListView.OnScrollListener() {

            int lastPosition;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE && lastPosition == detailCommentListAdapter.getCount() + 2) {
                    if (isLoadingMore) {
                        return;
                    }
                    isLoadingMore = true;
                    LogUtils.d("加载更多");
                    //shortToast("加载更多");
                    footView.setVisibility(View.VISIBLE);
                    DoctorCircleService.getDynamicDetailMoreData(msgId, ++pageCount, new CommonResponse<MoreMessage>() {
                        @Override
                        public void onNext(MoreMessage moreMessage) {

                            footView.setVisibility(View.GONE);
                            if (null == moreMessage) {
                                pageCount--;
                                shortToast("没有更多数据");
                                return;
                            }
                            if ("0".equals(moreMessage.getCode())) {
                                List<CommentBean> moreCommentList = moreMessage.getList();
                                if (null != moreCommentList && !moreCommentList.isEmpty()) {
                                    addMoreCommentData(moreCommentList);
                                } else {
                                    shortToast("没有更多数据");
                                }
                            } else {
                                pageCount--;
                                shortToast("没有更多数据");
                            }
                            isLoadingMore = false;
                        }
                    });
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastPosition = firstVisibleItem + visibleItemCount;
            }
        });

        lv_comment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final CommentBean commentBean = detailCommentListAdapter.getItem(position - 1);
                if (YMUserService.getCurUserId().equals(commentBean.getUser().getUserid())) {
                    // 自己的评论 删除评论弹框
                    showDeleteCommentDialog(commentBean.getId());
                } else {
                    KeyBoardUtils.openKeybord(pet_comment);
                    //刷洗 更改回复人
                    initOrUpdateInputView(mDynamicDtaile.getUser().getUserid(), commentBean.getId(), commentBean.getUser().getUserid(), commentBean.getUser().getNickname(), new CommonResponse<CommentResultBean>() {
                        @Override
                        public void onNext(CommentResultBean commentResultBean) {

                            if (BaseStringResultBean.CODE_SUCCESS.equals(commentResultBean.getCode())) {
                                //回复成功
                                LogUtils.d("回复成功");
                                shortToast("回复成功");
                                afterComment(commentResultBean, commentBean);
                            } else {
                                LogUtils.d("回复失败");
                                shortToast("回复失败");
                            }

                        }
                    });
                }
            }
        });
    }

    /**
     * 弹出删除评论 弹框
     *
     * @param commentId
     */
    private void showDeleteCommentDialog(final String commentId) {
        clickService.onDeleteCommentClick(SuperDynamicDetailActivity.this, commentId, new CommonResponse() {
            @Override
            public void onNext(Object o) {
                shortToast("删除成功");
                updateHeaderData();
                detailCommentListAdapter.removeData(commentId);
                detailCommentListAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * ListView添加上拉刷新加载的更多评论数据
     *
     * @param moreCommentList
     */
    private void addMoreCommentData(List<CommentBean> moreCommentList) {
        detailCommentListAdapter.addData(moreCommentList);
        detailCommentListAdapter.notifyDataSetChanged();
    }


    /**
     * 初始化或者更新 输入框
     *
     * @param commentId
     * @param toUserId
     * @param toUserName
     * @param subscriber
     */
    private void initOrUpdateInputView(final String curUserId, final String commentId, final String toUserId, final String toUserName, final Subscriber subscriber) {

        pet_comment.setText("");
        String commentToUserName = TextUtils.isEmpty(toUserName) ? toUserId : toUserName;
        String hint = TextUtils.isEmpty(toUserName) ? "请输入您的评论" : "回复 " + commentToUserName;
        pet_comment.setHint(hint);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (DCMiddlewareService.isInvalidDCUser(getMSGType(), "评论实名动态")) {
//                    return;
//                }
//
//                String content = pet_comment.getText().toString().trim();
//                if (TextUtils.isEmpty(content)) {
//                    shortToast("内容不能为空");
//                    return;
//                }
//
//                CommonResponse<CommentResultBean> commonResponse = new CommonResponse<CommentResultBean>() {
//                    @Override
//                    public void onNext(CommentResultBean commentResultBean) {
//
//                        //清空输入框
//                        pet_comment.setText("");
//                        subscriber.onNext(commentResultBean);
//                        KeyBoardUtils.closeKeyboard(pet_comment);
//                    }
//                };
//
//                comment(curUserId, commentId, toUserId, content, commonResponse);

                //添加验证认证状态
                DialogUtil.showUserCenterCertifyDialog(SuperDynamicDetailActivity.this, new MyCallBack() {
                    @Override
                    public void onClick(Object data) {
                        String content = pet_comment.getText().toString().trim();
                        if (TextUtils.isEmpty(content)) {
                            shortToast("内容不能为空");
                            return;
                        }

                        CommonResponse<CommentResultBean> commonResponse = new CommonResponse<CommentResultBean>() {
                            @Override
                            public void onNext(CommentResultBean commentResultBean) {

                                //清空输入框
                                pet_comment.setText("");
                                subscriber.onNext(commentResultBean);
                                KeyBoardUtils.closeKeyboard(pet_comment);
                            }
                        };

                        comment(curUserId, commentId, toUserId, content, commonResponse);
                    }
                }, null, null);

            }
        });
    }

    @Override
    protected void onPause() {
        KeyBoardUtils.closeKeyboard(pet_comment);
        super.onPause();
    }
}
