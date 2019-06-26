package com.xywy.askforexpert.module.doctorcircle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.UMShareAPI;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.net.DoctorAPI;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.askforexpert.model.api.BaseResultBean;
import com.xywy.askforexpert.model.discussDetail.DataUser;
import com.xywy.askforexpert.model.discussDetail.DiscussCommentList;
import com.xywy.askforexpert.model.discussDetail.DiscussDetailData;
import com.xywy.askforexpert.model.discussDetail.DiscussPraiseList;
import com.xywy.askforexpert.model.discussDetail.RootData;
import com.xywy.askforexpert.model.discussDetail.WinningList;
import com.xywy.askforexpert.model.doctor.DiscussMoreComment;
import com.xywy.askforexpert.model.doctor.Touser;
import com.xywy.askforexpert.model.doctor.User;
import com.xywy.askforexpert.model.newdoctorcircle.PraiseResultBean;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;
import com.xywy.askforexpert.module.docotorcirclenew.service.DoctorCircleService;
import com.xywy.askforexpert.module.doctorcircle.adapter.CommentListAdapter;
import com.xywy.askforexpert.module.doctorcircle.adapter.DiscussImgsAdapter;
import com.xywy.askforexpert.module.doctorcircle.adapter.PraiseAvatarAdapter;
import com.xywy.askforexpert.module.doctorcircle.adapter.RewardListAdapter;
import com.xywy.askforexpert.module.main.service.person.PersonDetailActivity;
import com.xywy.askforexpert.module.message.share.ShareUtil;
import com.xywy.askforexpert.module.message.usermsg.DiscussSettingsActivity;
import com.xywy.askforexpert.module.my.userinfo.PersonInfoActivity;
import com.xywy.askforexpert.widget.view.MyGridView;
import com.xywy.base.view.ProgressDialog;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 病例研讨班详情
 */
public class DiscussDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String LOG_TAG = "DiscussDetailActivity";

    private static final int DEFAULT_PAGE = 1;
    private static final String DISCUSS_STATE = "已结束";
    List<DiscussCommentList> mDatas = new ArrayList<>();
    private int page = DEFAULT_PAGE;
    private Toolbar toolbar;
    /**
     * 答案输入框
     */
    private EditText answerInput;
    /**
     * 提交答案
     */
    private Button sendAnswer;
    /**
     * 病例研讨班头像
     */
    private ImageView discussIcon;
    /**
     * 病例研讨班名称
     */
    private TextView discussName;
    /**
     * 病例研讨班职称
     */
    private TextView discussTitle;
    /**
     * 病例研讨班期数
     */
    private TextView discussPeriod;
    /**
     * 病例研讨班是否已经结束
     */
    private TextView discussOver;
    /**
     * 主诉 详细内容
     */
    private TextView zhusuDetail;
    /**
     * 现病史 详细内容
     */
    private TextView xianbingshiDetail;
    /**
     * 既往史 详细内容
     */
    private TextView jiwangshiDetail;
    /**
     * 体格检查 详细内容
     */
    private TextView tigejianchaDetail;
    /**
     * 辅助检查 详细内容
     */
    private TextView fuzhujianchaDetail;
    /**
     * 大图片
     */
    private ImageView discussBigImg;
    /**
     * 图片集合
     */
    private MyGridView discussImgs;
    /**
     * 奖励规则
     */
    private TextView discussRewardRule;
    /**
     * 答案
     */
    private TextView discussAnswer;
    /**
     * 获奖名单
     */
    private MyGridView discussRewardList;
    /**
     * 动态发布时间
     */
    private TextView postTime;
    /**
     * 点赞
     */
    private LinearLayout praiseButton;
    /**
     * 点赞图标
     */
    private ImageView praiseImg;
    /**
     * 点赞数
     */
    private TextView praiseCount;
    /**
     * 评论
     */
    private LinearLayout commentButton;
    /**
     * 评论数
     */
    private TextView commentCount;
    /**
     * 分享
     */
    private LinearLayout shareButton;
    /**
     * 已点赞用户的头像集合
     */
    private MyGridView praiseAvatar;
    /**
     * 点赞数
     */
    private TextView praiseNum;
    /**
     * 用户id
     */
    private String uuid;
    private ProgressDialog loadingDialog;
    /**
     * 图片加载器
     */
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;
    private String dynamicId;
    private String type = "1";
    /**
     * 评论列表布局
     */
    private LinearLayout commentListLayout;
    /**
     * 点赞列表布局
     */
    private RelativeLayout praiseImgs;
    /**
     * 评论箭头
     */
    private ImageView commentArrow;
    /**
     * 接口返回的数据
     */
    private RootData discussRootData;
    private String msgInfo;
    private boolean onlyOne = true;
    private int sendposition = -1;
    private String sendId = "0";
    /**
     * 诊断
     */
    private TextView zhenduanDetail;
    /**
     * 诊断依据
     */
    private TextView zhenduanyijuDetail;
    /**
     * 进一步检查
     */
    private TextView jinyibujianchaDetail;
    /**
     * 治疗
     */
    private TextView zhiliaoDetail;
    /**
     * 问题讨论
     */
    private TextView wentitaolunDetail;
    /**
     * 主诉布局
     */
    private LinearLayout zhusuLayout;
    /**
     * 现病史布局
     */
    private LinearLayout xianbingshiLayout;
    /**
     * 既往史布局
     */
    private LinearLayout jiwangshiLayout;
    /**
     * 体格检查布局
     */
    private LinearLayout tigejianchaLayout;
    /**
     * 辅助检查布局
     */
    private LinearLayout fuzhujianchaLayout;
    /**
     * 诊断布局
     */
    private LinearLayout zhenduanLayout;
    /**
     * 诊断依据布局
     */
    private LinearLayout zhenduanyijuLayout;
    /**
     * 进一步检查布局
     */
    private LinearLayout jinyibujianchaLayout;
    /**
     * 治疗布局
     */
    private LinearLayout zhiliaoLayout;
    /**
     * 问题讨论布局
     */
    private LinearLayout wentitaolunLayout;
    /**
     * 奖励规则布局
     */
    private LinearLayout rewardLayout;
    /**
     * 答案布局
     */
    private LinearLayout answerLayout;
    /**
     * 奖励名单布局
     */
    private LinearLayout rewardListLayout;
    private View discuss_comment_list_divider_line;
    /**
     * 记录上一次选中的图片位置
     */
    private int prePos = 0;
    private DiscussImgsAdapter adapter;
    private ListView mListView;
    private View footerView;
    private CommentListAdapter commentListAdapter;

    private boolean isLoadingNow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);
        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        sp.edit().putString("mustUpdata", "").apply();

        uuid = YMApplication.getUUid();
        setContentView(R.layout.activity_disucuss_detail);


        initView();
        CommonUtils.setToolbar(this, toolbar);
        getParams();
        registerListener();
        requestData();
    }

    public static void startActivity(Context context, String id, String type) {
        Intent intent = new Intent(context, DiscussDetailActivity.class);
        intent.putExtra("dynamicid", id);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    /**
     * 设置页面数据
     */
    private void refreshPageData(DiscussDetailData data) {
        if (data != null) {
            DataUser user = data.getUser();
            if (user != null) {
                discussName.setText(user.getRealname());
                discussTitle.setText(user.getSubject());
            }

            if (data.getUserphoto() != null) {
                mImageLoader.displayImage(data.getUserphoto(), discussIcon, options);
            } else {
                discussIcon.setImageResource(R.drawable.icon_photo_def);
            }

            discussPeriod.setText(data.getPhase());

            // 是否显示已结束图标
            if (DISCUSS_STATE.equals(data.getState())) {
                discussOver.setVisibility(View.VISIBLE);
                discussOver.setText("已结束");
            } else {
                discussOver.setVisibility(View.VISIBLE);
                discussOver.setText("进行中");
            }

            // 控制布局是否可见
            if (data.getComplain() == null || data.getComplain().equals("")) {
                zhusuLayout.setVisibility(View.GONE);
            } else {
                zhusuLayout.setVisibility(View.VISIBLE);
                zhusuDetail.setText(data.getComplain());
            }
            if (data.getNow_history() == null || data.getNow_history().equals("")) {
                xianbingshiLayout.setVisibility(View.GONE);
            } else {
                xianbingshiLayout.setVisibility(View.VISIBLE);
                xianbingshiDetail.setText(data.getNow_history());
            }
            if (data.getPast_history() == null || data.getPast_history().equals("")) {
                jiwangshiLayout.setVisibility(View.GONE);
            } else {
                jiwangshiLayout.setVisibility(View.VISIBLE);
                jiwangshiDetail.setText(data.getPast_history());
            }
            if (data.getPhysical() == null || data.getPhysical().equals("")) {
                tigejianchaLayout.setVisibility(View.GONE);
            } else {
                tigejianchaLayout.setVisibility(View.VISIBLE);
                tigejianchaDetail.setText(data.getPhysical());
            }
            if ((data.getSup_exa() == null || data.getSup_exa().equals(""))
                    && (data.getMinimgs() == null || data.getImgs() == null
                    || data.getMinimgs().size() == 0 || data.getImgs().size() == 0)) {
                fuzhujianchaLayout.setVisibility(View.GONE);
            } else {
                fuzhujianchaLayout.setVisibility(View.VISIBLE);
                fuzhujianchaDetail.setText(data.getSup_exa());
                if (data.getMinimgs() == null || data.getImgs() == null
                        || data.getMinimgs().size() == 0 || data.getImgs().size() == 0) {
                    discussBigImg.setVisibility(View.GONE);
                    discussImgs.setVisibility(View.GONE);
                } else {
                    discussBigImg.setVisibility(View.VISIBLE);
                    discussImgs.setVisibility(View.VISIBLE);
                    List<String> minimgs = data.getMinimgs();
                    final List<String> imgs = data.getImgs();
                    ViewGroup.LayoutParams layoutParams = discussBigImg.getLayoutParams();
                    int screenWidth = AppUtils.getScreenWidth(DiscussDetailActivity.this);
                    layoutParams.width = screenWidth;
                    layoutParams.height = screenWidth * 201 / 355;
                    discussBigImg.setLayoutParams(layoutParams);
                    final DisplayImageOptions options = new DisplayImageOptions.Builder()
                            .cacheOnDisk(true).cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565)
                            .showImageOnLoading(R.drawable.discuss_big_img)
                            .showImageOnFail(R.drawable.discuss_big_img)
                            .build();
                    if (imgs != null && imgs.size() > 0) {
                        mImageLoader.displayImage(imgs.get(0), discussBigImg, options);
                    }
                    // 记录当前大图显示的图片
//                    final List<String> currentPic = new ArrayList<>();
//                    if (currentPic.size() > 0)
//                        currentPic.clear();
//                    if (imgs != null && imgs.size() > 0)
//                        currentPic.add(imgs.get(0));
                    // 点击查看图片
                    discussBigImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(DiscussDetailActivity.this, SeePicActivty.class);
                            intent.putStringArrayListExtra("imgs", (ArrayList<String>) imgs);
                            intent.putExtra("curentItem", String.valueOf(prePos));
                            DiscussDetailActivity.this.startActivity(intent);
                        }
                    });
                    final List<String> mDatas;
                    if (minimgs != null && minimgs.size() > 0) {
                        if (minimgs.size() > 9) { // 最多显示9张图片
                            mDatas = minimgs.subList(0, 9);
                        } else {
                            mDatas = minimgs;
                        }
                    } else {
                        mDatas = new ArrayList<>();
                    }
                    // 展示图片集合
                    adapter = new DiscussImgsAdapter(this, mDatas, R.layout.discuss_imgs_layout);
                    discussImgs.setAdapter(adapter);
                    discussImgs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            adapter.setIsShould(false);
                            DLog.d("pos", String.valueOf(position) + ", " + prePos);
                            if (position >= 0 && position < mDatas.size() && position != prePos) {
                                // 设置当前选中图片的背景为选中框
                                view.setBackgroundResource(R.drawable.discuss_imgs_bg);

                                // 清除上一次选中图片的背景
                                if (prePos >= 0 && prePos < mDatas.size()) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        discussImgs.getChildAt(prePos).setBackground(null);
                                    }
                                }

                                // 将当前选中位置设置为上一次选中位置
                                prePos = position;

                                // 设置大图片
                                if (imgs != null && imgs.size() > 0 && position >= 0 && position < imgs.size()) {
                                    mImageLoader.displayImage(imgs.get(position), discussBigImg, options);

//                                    if (currentPic.size() > 0)
//                                        currentPic.clear();
//
//                                    currentPic.add(imgs.get(position));
                                }
                            }
                        }
                    });
                }
            }
            if (data.getDiagnosis() == null || data.getDiagnosis().equals("")) {
                zhenduanLayout.setVisibility(View.GONE);
            } else {
                zhenduanLayout.setVisibility(View.VISIBLE);
                zhenduanDetail.setText(data.getDiagnosis());
            }
            if (data.getBasis() == null || data.getBasis().equals("")) {
                zhenduanyijuLayout.setVisibility(View.GONE);
            } else {
                zhenduanyijuLayout.setVisibility(View.VISIBLE);
                zhenduanyijuDetail.setText(data.getBasis());
            }
            if (data.getFurther_exa() == null || data.getFurther_exa().equals("")) {
                jinyibujianchaLayout.setVisibility(View.GONE);
            } else {
                jinyibujianchaLayout.setVisibility(View.VISIBLE);
                jinyibujianchaDetail.setText(data.getFurther_exa());
            }
            if (data.getTreatment() == null || data.getTreatment().equals("")) {
                zhiliaoLayout.setVisibility(View.GONE);
            } else {
                zhiliaoLayout.setVisibility(View.VISIBLE);
                zhiliaoDetail.setText(data.getTreatment());
            }
            if (data.getQuestion() == null || data.getQuestion().equals("")) {
                wentitaolunLayout.setVisibility(View.GONE);
            } else {
                wentitaolunLayout.setVisibility(View.VISIBLE);
                wentitaolunDetail.setText(data.getQuestion());
            }
            if (data.getReward() == null || data.getReward().equals("")) {
                rewardLayout.setVisibility(View.GONE);
            } else {
                rewardLayout.setVisibility(View.VISIBLE);
                discussRewardRule.setText(data.getReward());
            }
            if (data.getAnswer() == null || data.getAnswer().equals("")) {
                answerLayout.setVisibility(View.GONE);
            } else {
                answerLayout.setVisibility(View.VISIBLE);
                discussAnswer.setText(data.getAnswer());
            }
            if (data.getWinning_list() == null || data.getWinning_list().size() == 0) {
                rewardListLayout.setVisibility(View.GONE);
            } else {
                rewardListLayout.setVisibility(View.VISIBLE);
                List<WinningList> mDatas = data.getWinning_list();
//                if (data.getWinning_list().size() > 12) { // 最多显示12个获奖者
//                    mDatas = data.getWinning_list().subList(0, 12);
//                } else {
//                    mDatas = data.getWinning_list();
//                }
                // 展示获奖列表
                RewardListAdapter adapter = new RewardListAdapter(this, mDatas,
                        R.layout.discuss_reward_list_item_layout);
                discussRewardList.setAdapter(adapter);
            }

            postTime.setText(data.getCreatetime());
            praiseCount.setText(data.getPraiseNum());
            commentCount.setText(data.getCommentNum());

            String is_praise = data.getIs_praise();
            if (!TextUtils.isEmpty(is_praise)) {
                if ("1".equals(is_praise)) {
                    praiseImg.setImageResource(R.drawable.icon_priseed);
                } else {
                    praiseImg.setImageResource(R.drawable.icon_doctor_praise);
                }
            }

            if (data.getPraiseNum().equals("0") && data.getCommentNum().equals("0")) {
                commentListLayout.setVisibility(View.GONE);
                commentArrow.setVisibility(View.INVISIBLE);
                discuss_comment_list_divider_line.setVisibility(View.GONE);
            } else {
                commentListLayout.setVisibility(View.VISIBLE);
                if (data.getPraiseNum().equals("0")) {
                    commentListLayout.setVisibility(View.GONE);
                    praiseImgs.setVisibility(View.GONE);
//                    discussPraiseDivider.setVisibility(View.GONE);
                } else {
                    praiseImgs.setVisibility(View.VISIBLE);
//                    discussPraiseDivider.setVisibility(View.VISIBLE);
                    discuss_comment_list_divider_line.setVisibility(View.VISIBLE);
                    // 展示点赞数据
                    final List<DiscussPraiseList> praiselist = new ArrayList<>();
                    if (data.getPraiselist() != null && data.getPraiselist().size() > 0) {
                        if (Integer.parseInt(data.getPraiseNum()) > 6
                                && data.getPraiselist().size() > 6) {
                            praiseNum.setText("等" + data.getPraiseNum() + "人点赞");
                            // 最多显示6个点赞者的头像
                            praiselist.addAll(data.getPraiselist().subList(0, 6));
                        } else {
                            praiseNum.setText(data.getPraiseNum() + "人点赞");
                            praiselist.addAll(data.getPraiselist());
                        }
                    } else {
                        commentListLayout.setVisibility(View.GONE);
                    }
                    PraiseAvatarAdapter adapter = new PraiseAvatarAdapter(this, praiselist, R.layout.praise_avatar_layout);
                    praiseAvatar.setAdapter(adapter);
                    praiseAvatar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(DiscussDetailActivity.this, PersonDetailActivity.class);
                            intent.putExtra("uuid", praiselist.get(position).getUserid());
                            intent.putExtra("isDoctor", "");
                            DiscussDetailActivity.this.startActivity(intent);
                        }
                    });
                }
            }
        }
    }

    private void showDeleteDalog(final int position) {
        if (onlyOne) {
            onlyOne = false;

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("是否删除这条评论?");
            dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    onlyOne = true;
                    arg0.dismiss();
                    deletDynamic(position);
                }
            });

            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    onlyOne = true;
                    arg0.dismiss();
                }
            });
            AlertDialog create = dialog.create();
            create.show();
            create.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface arg0) {
                    onlyOne = true;
                }
            });
        }
    }

    /**
     * 请求接口数据
     */
    private void requestData() {
        if (loadingDialog != null) {
            loadingDialog.showProgersssDialog();
        }

        String sign = MD5Util.MD5(uuid + dynamicId + Constants.MD5_KEY);
        String bind = uuid + dynamicId;
        DoctorAPI.dynamicDetaile(dynamicId, uuid, bind, sign, DEFAULT_PAGE, new AjaxCallBack() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);

                DLog.d(LOG_TAG, "data = " + s);

                if (loadingDialog != null && loadingDialog.isShowing()) {
                    loadingDialog.closeProgersssDialog();
                }

                JSONObject jsonObject = null;
                if (s != null) {
                    try {
                        jsonObject = new JSONObject(s);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (jsonObject == null) { // 接口错误
                    Toast.makeText(DiscussDetailActivity.this, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                    DiscussDetailActivity.this.finish();
                } else {
                    // 解析接口数据
                    Gson gson = new Gson();
                    discussRootData = gson.fromJson(s, RootData.class);
                    DiscussDetailData data = discussRootData.getData();

                    if (page == DEFAULT_PAGE) {
                        refreshPageData(data);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);

                if (mListView.getFooterViewsCount() > 0) {
                    mListView.removeFooterView(footerView);
                }

                if (loadingDialog != null && loadingDialog.isShowing()) {
                    loadingDialog.closeProgersssDialog();
                }

                Toast.makeText(DiscussDetailActivity.this, getString(R.string.loading_failed), Toast.LENGTH_SHORT).show();
            }
        });

        String commentBind = dynamicId;
        String commentSign = MD5Util.MD5(commentBind + Constants.MD5_KEY);
        DoctorAPI.getMoreConment(String.valueOf(page), dynamicId, commentBind, commentSign,
                new AjaxCallBack() {
                    @Override
                    public void onSuccess(String s) {
                        super.onSuccess(s);
                        DLog.d(LOG_TAG, "discuss more comment = " + s);

                        try {
                            DiscussMoreComment discussMoreComment = new Gson().fromJson(s, DiscussMoreComment.class);
                            List<DiscussCommentList> commentLists = discussMoreComment.list;

                            if (null != commentLists && !commentLists.isEmpty()) {
                                commentArrow.setVisibility(View.VISIBLE);
                                discuss_comment_list_divider_line.setVisibility(View.VISIBLE);
                                // 展示评论数据
                                if (commentLists.size() > 0) {
                                    DLog.d(LOG_TAG, "comment size = " + commentLists.size());
                                    if (mDatas != null) {
                                        if (!mDatas.isEmpty()) {
                                            mDatas.clear();
                                        }

                                        mDatas.addAll(commentLists);
                                    }
                                } else {
                                    Toast.makeText(DiscussDetailActivity.this, "暂无更多数据", Toast.LENGTH_SHORT).show();

                                    if (mListView.getFooterViewsCount() > 0) {
                                        mListView.removeFooterView(footerView);
                                    }
                                }
                            } else {
                                commentArrow.setVisibility(View.INVISIBLE);
                                discuss_comment_list_divider_line.setVisibility(View.GONE);
                            }
                            commentListAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            LogUtils.e("服务端json数据格式异常:" + e.getMessage());
                            ToastUtils.shortToast("服务端数据格式异常，请稍后重试");
                            e.printStackTrace();
                            page--;
                        }
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        super.onFailure(t, errorNo, strMsg);

                        if (mDatas != null && mDatas.size() > 0) {
                            Toast.makeText(DiscussDetailActivity.this, "暂无更多数据", Toast.LENGTH_SHORT).show();
                        }

                        if (mListView.getFooterViewsCount() > 0) {
                            mListView.removeFooterView(footerView);
                        }

                        page--;
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null) {
            if (loadingDialog.isShowing()) {
                loadingDialog.closeProgersssDialog();
            }

            loadingDialog = null;
        }

    }

    private void registerListener() {
        discussIcon.setOnClickListener(this);
        discussName.setOnClickListener(this);
        praiseButton.setOnClickListener(this);
        commentButton.setOnClickListener(this);
        shareButton.setOnClickListener(this);
        sendAnswer.setOnClickListener(this);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState != SCROLL_STATE_IDLE) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(answerInput.getWindowToken(), 0);
                }

                if (scrollState == SCROLL_STATE_IDLE
                        && view.getLastVisiblePosition() == view.getCount() - 1) {
                    if (mListView.getFooterViewsCount() == 0) {
                        mListView.addFooterView(footerView, null, false);
                    }

                    if (footerView.getVisibility() == View.GONE) {
                        footerView.setVisibility(View.VISIBLE);
                    }

                    if (!isLoadingNow) {
                        isLoadingNow = true;

                        page++;
                        if (page == 5) {
                            int br = 0;
                        }
                        String bind = dynamicId;
                        String sign = MD5Util.MD5(bind + Constants.MD5_KEY);
                        DoctorAPI.getMoreConment(String.valueOf(page), dynamicId, bind, sign,
                                new AjaxCallBack() {
                                    @Override
                                    public void onSuccess(String s) {
                                        super.onSuccess(s);
                                        DLog.d(LOG_TAG, "more comment s = " + s);

                                        isLoadingNow = false;
                                        try {
                                            DiscussMoreComment discussMoreComment = new Gson().fromJson(s, DiscussMoreComment.class);
                                            List<DiscussCommentList> commentLists = discussMoreComment.list;

                                            if (commentLists != null && commentLists.size() > 0) {
                                                if (page == DEFAULT_PAGE) {
                                                    if (!mDatas.isEmpty()) {
                                                        mDatas.clear();
                                                    }
                                                }
                                                mDatas.addAll(commentLists);
                                            } else {
                                                if (mDatas != null && mDatas.size() > 0) {
                                                    Toast.makeText(DiscussDetailActivity.this, "暂无更多数据", Toast.LENGTH_SHORT).show();
                                                }

                                                if (mListView.getFooterViewsCount() > 0) {
                                                    mListView.removeFooterView(footerView);
                                                }

                                                page--;
                                            }
                                            commentListAdapter.notifyDataSetChanged();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            LogUtils.e("服务端json数据格式异常:" + e.getMessage());
                                            ToastUtils.shortToast("服务端数据格式异常，请稍后重试");
                                            if (mListView.getFooterViewsCount() > 0) {
                                                mListView.removeFooterView(footerView);
                                            }

                                            page--;
                                        }
                                    }

                                    @Override
                                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                                        super.onFailure(t, errorNo, strMsg);

                                        isLoadingNow = false;

                                        if (mDatas != null && mDatas.size() > 0) {
                                            Toast.makeText(DiscussDetailActivity.this, "暂无更多数据", Toast.LENGTH_SHORT).show();
                                        }

                                        if (mListView.getFooterViewsCount() > 0) {
                                            mListView.removeFooterView(footerView);
                                        }

                                        page--;
                                    }
                                });
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (position == 0) {
                    return;
                }

                //判断认证 stone
                DialogUtil.showUserCenterCertifyDialog(DiscussDetailActivity.this, new MyCallBack() {
                    @Override
                    public void onClick(Object data) {
                        User user1 = mDatas.get(position - 1).getUser();
                        if (user1.userid != null && user1.userid.equals(uuid)) {
                            showDeleteDalog(position - 1);
                        } else {
                            answerInput.requestFocus();
                            answerInput.setHint("回复 " + user1.nickname);
                            // 弹出软键盘
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(answerInput, InputMethodManager.SHOW_FORCED);
                            sendposition = position - 1;
                            sendId = mDatas.get(position - 1).getId();
                        }
                    }
                }, null, null);
//stone old
//                if (YMUserService.isGuest()) {// 游客
//                    DoctorAPI.startLogIn(DiscussDetailActivity.this);
//                } else {
//                    if (YMApplication.isDoctor()) {
//                        msgInfo = getString(R.string.doctor_not_all_infors);
//                    } else {
//                        msgInfo = getString(R.string.doctor_student_not_all_infors);
//                    }
//                    if (!"1".equals(discussRootData.getData().getIs_doctor())) {
//                        // 登陆
//                        showDailog();
//                        return;
//                    }
//
//                    User user1 = mDatas.get(position - 1).getUser();
//                    if (user1.userid != null && user1.userid.equals(uuid)) {
//                        showDeleteDalog(position - 1);
//                    } else {
//                        answerInput.requestFocus();
//                        answerInput.setHint("回复 " + user1.nickname);
//                        // 弹出软键盘
//                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.showSoftInput(answerInput, InputMethodManager.SHOW_FORCED);
//                        sendposition = position - 1;
//                        sendId = mDatas.get(position - 1).getId();
//                    }
//                }
            }
        });

        answerInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (YMUserService.isGuest()) {
                    DoctorAPI.startLogIn(DiscussDetailActivity.this);
                    return;
                }
                // 重置hint
                answerInput.setHint("请输入您的评论");
                sendposition = -1;
                sendId = "0";
            }
        });

        answerInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 200) {
                    Toast.makeText(DiscussDetailActivity.this, "最多输入200字", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getParams() {
        dynamicId = getIntent().getStringExtra("dynamicid");
        type = getIntent().getStringExtra("type");
    }

    @SuppressLint("InflateParams")
    private void initView() {
        loadingDialog = new ProgressDialog(this, getString(R.string.loading_now));

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        mListView = (ListView) findViewById(R.id.discuss_detail_list);

        answerInput = (EditText) findViewById(R.id.answer_input);
        sendAnswer = (Button) findViewById(R.id.send_answer);

        @SuppressLint("InflateParams") View headerView = LayoutInflater.from(this).inflate(R.layout.discuss_detail_header, null);

//        contentScroll = (DiscussScrollView) findViewById(R.id.discuss_content_scroll);

        discussIcon = (ImageView) headerView.findViewById(R.id.discuss_img);
        discussName = (TextView) headerView.findViewById(R.id.discuss_name);
        discussTitle = (TextView) headerView.findViewById(R.id.discuss_title);
        discussPeriod = (TextView) headerView.findViewById(R.id.discuss_period);
        discussOver = (TextView) headerView.findViewById(R.id.discuss_over);

        zhusuDetail = (TextView) headerView.findViewById(R.id.discuss_zhusu_detail);
        xianbingshiDetail = (TextView) headerView.findViewById(R.id.discuss_xianbingshi_detail);
        jiwangshiDetail = (TextView) headerView.findViewById(R.id.discuss_jiwangshi_detail);
        tigejianchaDetail = (TextView) headerView.findViewById(R.id.discuss_tigejiancha_detail);
        fuzhujianchaDetail = (TextView) headerView.findViewById(R.id.discuss_fuzhujiancha_detail);
        discussBigImg = (ImageView) headerView.findViewById(R.id.discuss_big_photo);
        discussImgs = (MyGridView) headerView.findViewById(R.id.discuss_photos);
        zhenduanDetail = (TextView) headerView.findViewById(R.id.discuss_zhenduan_detail);
        zhenduanyijuDetail = (TextView) headerView.findViewById(R.id.discuss_zhenduanyiju_detail);
        jinyibujianchaDetail = (TextView) headerView.findViewById(R.id.discuss_jinyibujiancha_detail);
        zhiliaoDetail = (TextView) headerView.findViewById(R.id.discuss_zhiliao_detail);
        wentitaolunDetail = (TextView) headerView.findViewById(R.id.discuss_wentitaolun_detail);

        discussRewardRule = (TextView) headerView.findViewById(R.id.discuss_reward_detail);

        discussAnswer = (TextView) headerView.findViewById(R.id.discuss_answer_detail);

        discussRewardList = (MyGridView) headerView.findViewById(R.id.discuss_reward_list);

        postTime = (TextView) headerView.findViewById(R.id.tv_user_time);
        praiseButton = (LinearLayout) headerView.findViewById(R.id.ll_praise);
        praiseImg = (ImageView) headerView.findViewById(R.id.iv_praise);
        praiseCount = (TextView) headerView.findViewById(R.id.tv_praise_num);
        commentButton = (LinearLayout) headerView.findViewById(R.id.ll_discuss);
        commentCount = (TextView) headerView.findViewById(R.id.tv_discuss_num);
        shareButton = (LinearLayout) headerView.findViewById(R.id.ll_doctor_share);

        praiseAvatar = (MyGridView) headerView.findViewById(R.id.praise_img_list);
        praiseNum = (TextView) headerView.findViewById(R.id.praise_num);
//        commentList = (MyListView) headerView.findViewById(R.id.comment_list);
        praiseImgs = (RelativeLayout) headerView.findViewById(R.id.praise_img);
        commentListLayout = (LinearLayout) headerView.findViewById(R.id.discuss_detail_comment_list_layout);
        commentArrow = (ImageView) headerView.findViewById(R.id.discuss_comment_arrow);

        zhusuLayout = (LinearLayout) headerView.findViewById(R.id.zhushu_ll);
        xianbingshiLayout = (LinearLayout) headerView.findViewById(R.id.xianbingshi_ll);
        jiwangshiLayout = (LinearLayout) headerView.findViewById(R.id.jiwangshi_ll);
        tigejianchaLayout = (LinearLayout) headerView.findViewById(R.id.tigejiancha_ll);
        fuzhujianchaLayout = (LinearLayout) headerView.findViewById(R.id.fuzhujiancha_ll);
        zhenduanLayout = (LinearLayout) headerView.findViewById(R.id.zhenduan_ll);
        zhenduanyijuLayout = (LinearLayout) headerView.findViewById(R.id.zhenduanyiju_ll);
        jinyibujianchaLayout = (LinearLayout) headerView.findViewById(R.id.jinyibujiancha_ll);
        zhiliaoLayout = (LinearLayout) headerView.findViewById(R.id.zhiliao_ll);
        wentitaolunLayout = (LinearLayout) headerView.findViewById(R.id.wentitaolun_ll);
        rewardLayout = (LinearLayout) headerView.findViewById(R.id.reward_ll);
        answerLayout = (LinearLayout) headerView.findViewById(R.id.answer_ll);
        rewardListLayout = (LinearLayout) headerView.findViewById(R.id.reward_list_ll);

        discuss_comment_list_divider_line = headerView.findViewById(R.id.discuss_comment_list_divider_line);
//        discussPraiseDivider = findViewById(R.id.discuss_praise_divider);

        if (mListView.getHeaderViewsCount() == 0) {
            mListView.addHeaderView(headerView, null, false);
        }

        footerView = LayoutInflater.from(this).inflate(R.layout.loading_more, null);
        if (mListView.getFooterViewsCount() == 0) {
            mListView.addFooterView(footerView, null, false);
        }
        footerView.setVisibility(View.GONE);

        commentListAdapter = new CommentListAdapter(this, mDatas, R.layout.comment_list_item_layout);
        mListView.setAdapter(commentListAdapter);

        mImageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.icon_photo_def)
                .showImageOnFail(R.drawable.icon_photo_def)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 病例研讨班 主页
            case R.id.discuss_img:
            case R.id.discuss_name:
                if (discussRootData.getData().getUserId() != null
                        && !discussRootData.getData().getUserId().equals("")) {
                    Intent intent = new Intent(this, DiscussSettingsActivity.class);
                    intent.putExtra("uuid", discussRootData.getData().getUserId());
                    intent.putExtra("isDoctor", discussRootData.getData().getIs_doctor());
                    startActivity(intent);
                }
                break;

            // 点赞
            case R.id.ll_praise:

                //判断认证 stone
                DialogUtil.showUserCenterCertifyDialog(DiscussDetailActivity.this, new MyCallBack() {
                    @Override
                    public void onClick(Object t) {
                        DiscussDetailData data = discussRootData.getData();
                        if ("1".equals(data.getIs_praise())) {
                            ToastUtils.shortToast("取消点赞");
                            data.setIs_praise("0");
                            data.setPraiseNum(String.valueOf(Integer.parseInt(data.getPraiseNum()) - 1));

                            List<DiscussPraiseList> praiselist = discussRootData.getData().getPraiselist();
                            for (int i = 0; i < praiselist.size(); i++) {
                                DiscussPraiseList praiseListitenm = praiselist.get(i);
                                if (praiseListitenm.getUserid().equals(uuid)) {
                                    praiselist.remove(i);
                                }
                            }
                        } else {
                            data.setPraiseNum(String.valueOf(Integer.parseInt(data.getPraiseNum()) + 1));
                            data.setIs_praise("1");
                            ToastUtils.shortToast("点赞成功");

                            List<DiscussPraiseList> praiselis = discussRootData.getData().getPraiselist();
                            DiscussPraiseList praiseList2 = new DiscussPraiseList();
                            praiseList2.setUserid(uuid);
                            praiseList2.setNickname(YMApplication.getLoginInfo().getData().getRealname());
                            praiseList2.setPhoto(YMApplication.getLoginInfo().getData().getPhoto());
                            praiselis.add(0, praiseList2);
                        }

                        refreshPageData(discussRootData.getData());

                        // params.put("bind",bind);//userid+touserid+commentid+dynamicid
                        String pariseBind = uuid + discussRootData.getData().getUserId() + 0 + dynamicId;
                        DLog.d("dynamicid", "dynamicId = " + dynamicId);

                        DoctorCircleService.praiseRealName(discussRootData.getData().getId(), "0", discussRootData.getData().getUserId(), new CommonResponse<PraiseResultBean>() {
                            @Override
                            public void onNext(PraiseResultBean praiseResultBean) {
                                prePos = 0;
                            }
                        });
                    }
                }, null, null);

                //stone old
//                if (YMUserService.isGuest()) {
//                    DoctorAPI.startLogIn(this);
//                } else {
//                    if (YMApplication.isDoctor()) {
//                        msgInfo = "需要完善姓名、职称、医院、科室等信息，才能为实名动态点赞";
//                    } else {
//                        msgInfo = "需要完善姓名、学校、专业等信息，才能为实名动态点赞";
//                    }
//                    if (!"1".equals(discussRootData.getData().getIs_doctor())) {
//                        showDailog();
//                        return;
//                    }
//
//                    DiscussDetailData data = discussRootData.getData();
//                    if ("1".equals(data.getIs_praise())) {
//                        ToastUtils.shortToast("取消点赞");
//                        data.setIs_praise("0");
//                        data.setPraiseNum(String.valueOf(Integer.parseInt(data.getPraiseNum()) - 1));
//
//                        List<DiscussPraiseList> praiselist = discussRootData.getData().getPraiselist();
//                        for (int i = 0; i < praiselist.size(); i++) {
//                            DiscussPraiseList praiseListitenm = praiselist.get(i);
//                            if (praiseListitenm.getUserid().equals(uuid)) {
//                                praiselist.remove(i);
//                            }
//                        }
//                    } else {
//                        data.setPraiseNum(String.valueOf(Integer.parseInt(data.getPraiseNum()) + 1));
//                        data.setIs_praise("1");
//                        ToastUtils.shortToast("点赞成功");
//
//                        List<DiscussPraiseList> praiselis = discussRootData.getData().getPraiselist();
//                        DiscussPraiseList praiseList2 = new DiscussPraiseList();
//                        praiseList2.setUserid(uuid);
//                        praiseList2.setNickname(YMApplication.getLoginInfo().getData().getRealname());
//                        praiseList2.setPhoto(YMApplication.getLoginInfo().getData().getPhoto());
//                        praiselis.add(0, praiseList2);
//                    }
//
//                    refreshPageData(discussRootData.getData());
//
//                    // params.put("bind",bind);//userid+touserid+commentid+dynamicid
//                    String pariseBind = uuid + discussRootData.getData().getUserId() + 0 + dynamicId;
//                    DLog.d("dynamicid", "dynamicId = " + dynamicId);
//
//                    DoctorCircleService.praiseRealName(discussRootData.getData().getId(), "0", discussRootData.getData().getUserId(), new CommonResponse<PraiseResultBean>() {
//                        @Override
//                        public void onNext(PraiseResultBean praiseResultBean) {
//                            prePos = 0;
//                        }
//                    });
//
//                }
                break;

            // 评论
            case R.id.ll_discuss:
                if (YMUserService.isGuest()) {
                    DoctorAPI.startLogIn(this);
                } else {
                    InputMethodManager inputManager = (InputMethodManager) answerInput.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(answerInput, InputMethodManager.SHOW_IMPLICIT);
                }
                break;

            // 提交答案
            case R.id.send_answer:
                //判断认证 stone
                DialogUtil.showUserCenterCertifyDialog(DiscussDetailActivity.this, new MyCallBack() {
                    @Override
                    public void onClick(Object data) {
                        final String content = answerInput.getText().toString().trim();
                        if (TextUtils.isEmpty(content)) {
                            ToastUtils.shortToast("内容不能为空");
                            return;
                        }
                        if (content.length() > 200) {
                            Toast.makeText(DiscussDetailActivity.this, "最多输入200字", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        showpup(sendposition, sendId, content);
                    }
                }, null, null);

//stone old
//                DLog.d(LOG_TAG, "send_andser");
//                if (YMUserService.isGuest()) {
//                    DoctorAPI.startLogIn(this);
//                    return;
//                }
//
//                if (YMApplication.isDoctor()) {
//                    msgInfo = getString(R.string.doctor_not_all_infors);
//                } else {
//                    msgInfo = getString(R.string.doctor_student_not_all_infors);
//                }
//                if (!"1".equals(discussRootData.getData().getIs_doctor())) {
//                    // 登陆
//                    showDailog();
//                    return;
//                }
//
//                final String content = answerInput.getText().toString().trim();
//                if (TextUtils.isEmpty(content)) {
//                    ToastUtils.shortToast("内容不能为空");
//                    return;
//                }
//                if (content.length() > 200) {
//                    Toast.makeText(this, "最多输入200字", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                showpup(sendposition, sendId, content);
                break;

            // 分享
            case R.id.ll_doctor_share:
                //判断认证 stone
                DialogUtil.showUserCenterCertifyDialog(DiscussDetailActivity.this, new MyCallBack() {
                    @Override
                    public void onClick(Object object) {

//stone old
//                if (YMUserService.isGuest()) {
//                    DoctorAPI.startLogIn(this);
//                } else {
                        if (discussRootData != null && discussRootData.getData() != null) {
                            DiscussDetailData data = discussRootData.getData();
                            String title = data.getNickname() == null ? "分享自“医脉”" : data.getNickname();
                            String shareContent = data.getContent() == null ?
                                    "病例研讨班" :
                                    (data.getContent().length() > 20 ? data.getContent().substring(0, 20) : data.getContent());
                            String webUrl = data.getUrl();
                            String imgUrl = ShareUtil.DEFAULT_SHARE_IMG_ULR;
                            if (data.getMinimgs() != null && data.getMinimgs().size() > 0) {
                                imgUrl = data.getMinimgs().get(0);
                            }
                            DLog.d(LOG_TAG, "shareTitle = " + title);
                            DLog.d(LOG_TAG, "shareContent = " + shareContent);
                            DLog.d(LOG_TAG, "webUrl = " + webUrl);
                            DLog.d(LOG_TAG, "imgUrl = " + imgUrl);

                            new ShareUtil.Builder()
                                    .setTitle(title)
                                    .setText(shareContent)
                                    .setTargetUrl(webUrl)
                                    .setImageUrl(imgUrl)
                                    .build(DiscussDetailActivity.this).outerShare();
                        }
//                }
                    }
                }, null, null);
                break;

            default:
                break;
        }
    }

    private void showDailog() {
        if (onlyOne) {
            onlyOne = false;

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(msgInfo);
            dialog.setPositiveButton("去完善", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    onlyOne = true;
                    arg0.dismiss();
                    Intent inten = new Intent(DiscussDetailActivity.this, PersonInfoActivity.class);
                    inten.putExtra("doctorInfo", "doctorInfo");
                    DiscussDetailActivity.this.startActivity(inten);
                }
            });

            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    onlyOne = true;
                    arg0.dismiss();
                }
            });
            AlertDialog create = dialog.create();
            create.show();
            create.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface arg0) {
                    onlyOne = true;
                }
            });

        }
    }

    private void showpup(final int position, final String id, final String content) {
        if (onlyOne) {
            onlyOne = false;
        } else {
            return;
        }
//        List<DiscussCommentList> commlist;
        String bind;
        String touserid;
        if (position != -1) {
//            commlist = mDatas;
            // userid+touserid //被评论用户ID +dynamicid
            bind = uuid + mDatas.get(position).getUser().userid + discussRootData.getData().getId();
            touserid = mDatas.get(position).getUser().userid;
        } else {
            touserid = discussRootData.getData().getUserId();
            bind = uuid + touserid + discussRootData.getData().getId();
        }
        DoctorAPI.getRealNameConment(type, id, content, discussRootData.getData().getId(), uuid,
                touserid, bind, MD5Util.MD5(bind + Constants.MD5_KEY), new AjaxCallBack() {
                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                        ToastUtils.shortToast("评论失败");
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.closeProgersssDialog();
                        }
                        onlyOne = true;
                    }

                    @Override
                    public void onStart() {
                        if (loadingDialog != null) {
                            loadingDialog.showProgersssDialog();
                        }
                        super.onStart();
                    }

                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        onlyOne = true;
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.closeProgersssDialog();
                        }

                        try {
                            JSONObject mJsonObject = new JSONObject(t.toString());
                            String code = mJsonObject.getString("code");
                            String msg = mJsonObject.getString("msg");
                            if (Integer.parseInt(code) == -1) {
                                ToastUtils.shortToast(msg);
                                return;
                            }
                            String commentid = mJsonObject.optString("commentid");
                            if (!TextUtils.isEmpty(code)) {
                                if ("-3".equals(code)) {
                                    CommonUtils.showWarnDialog(DiscussDetailActivity.this, msg);
                                } else if ("0".equals(code)) {
                                    prePos = 0;
                                    ToastUtils.shortToast("评论成功");
                                    answerInput.setText("");
//                                    List<DiscussCommentList> commentLists = discussRootData.getData().getCommlist();
                                    DiscussCommentList newCommList = new DiscussCommentList();
                                    User newUser = new User();
                                    Touser newTOuser = new Touser();
                                    if (!id.equals("0")) {// 对评论评论
                                        DiscussCommentList commentList = mDatas.get(position);
                                        newUser.userid = uuid;
                                        newUser.nickname = YMApplication.getLoginInfo().getData().getRealname();
                                        newUser.job = YMApplication.getLoginInfo().getData().getJob();
                                        newUser.department = YMApplication.getLoginInfo().getData().getSubjectName();
                                        newUser.photo = YMApplication.getLoginInfo().getData().getPhoto();

                                        newTOuser.userid = commentList.getUser().userid;
                                        newTOuser.nickname = commentList.getUser().nickname;
                                        newTOuser.subject = commentList.getUser().subject;
                                        newTOuser.photo = commentList.getUser().photo;
                                    } else {// 消息评论
                                        newUser.userid = uuid;
                                        newUser.nickname = YMApplication.getLoginInfo().getData().getRealname();
                                        newUser.job = YMApplication.getLoginInfo().getData().getJob();
                                        newUser.department = YMApplication.getLoginInfo().getData().getSubjectName();
                                        newUser.photo = YMApplication.getLoginInfo().getData().getPhoto();
                                    }
                                    newCommList.setId(commentid);
                                    newCommList.setContent(content);
                                    newCommList.setUser(newUser);
                                    newCommList.setTouser(newTOuser);

                                    if (mDatas.size() == Integer.parseInt(discussRootData.getData().getCommentNum())) {
                                        if (id.equals("0")) {// 对评论评论position
                                            // + 1,
                                            mDatas.add(newCommList);
                                        } else {
                                            mDatas.add(newCommList);
                                        }
                                        commentListAdapter.notifyDataSetChanged();
                                    }
                                    // 评论数量加一
                                    discussRootData.getData().setCommentNum(String.valueOf(Integer.parseInt(discussRootData.getData().getCommentNum()) + 1));
                                    refreshPageData(discussRootData.getData());
                                } else {
                                    ToastUtils.shortToast("评论失败");
                                }

                            } else {
                                ToastUtils.shortToast("评论失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

    }

    private void deletDynamic(final int position) {
        DiscussCommentList mCommList = mDatas.get(position);
        String bind = mCommList.getUser().userid + mCommList.getId();
        String sign = MD5Util.MD5(bind + Constants.MD5_KEY);

        if (loadingDialog != null) {
            loadingDialog.showProgersssDialog();
        }
        DoctorCircleService.deleteComment(mCommList.getUser().userid, mCommList.getId(), new CommonResponse<BaseResultBean>() {
            @Override
            public void onNext(BaseResultBean baseResultBean) {
                if (loadingDialog != null) {
                    loadingDialog.closeProgersssDialog();
                }

                if (0 == baseResultBean.getCode()) {
                    ToastUtils.shortToast("删除成功");
                    // 删除数据
                    mDatas.remove(position);
                    // 删除评论 条数
                    discussRootData.getData().setCommentNum(String.valueOf(Integer.parseInt(discussRootData.getData().getCommentNum()) - 1));
                    refreshPageData(discussRootData.getData());
                    commentListAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.shortToast("删除失败");
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        StatisticalTools.onResume(this);
    }

    @Override
    protected void onPause() {
        StatisticalTools.onPause(this);

        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /**使用SSO授权必须添加如下代码 */
//        UMSsoHandler ssoHandler = SharedUtils.getInstence().getmController().getConfig().getSsoHandler(requestCode);
//        if (ssoHandler != null) {
//            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
//        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
