package com.xywy.askforexpert.module.consult.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.letv.utils.SharedPreferenceUtil;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.YmRxBus;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.model.consultentity.IMQuestionBean;
import com.xywy.askforexpert.model.consultentity.QuestionsListEntity;
import com.xywy.askforexpert.model.consultentity.QuestionsPoolRspEntity;
import com.xywy.askforexpert.model.websocket.msg.chatmsg.ChatMsg;
import com.xywy.askforexpert.module.consult.ServiceProvider;
import com.xywy.askforexpert.module.consult.fragment.ConsultPagerFragment;
import com.xywy.askforexpert.module.websocket.WebSocketRxBus;
import com.xywy.askforexpert.widget.view.SelectBasePopupWindow;
import com.xywy.retrofit.rxbus.Event;
import com.xywy.retrofit.rxbus.EventSubscriber;
import com.xywy.uilibrary.titlebar.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Subscriber;

/**
 * Created by zhangzheng on 2017/4/26. stone 移植yimai 即时问答
 */

public class ConsultOnlineActivity extends YMBaseActivity implements ConsultPagerFragment.ITip {
    @Bind(R.id.tv_activity_consult_tip)
    TextView tvTip;
    @Bind(R.id.scrap_question_vf)
    ViewFlipper scrap_question_vf;
    @Bind(R.id.tv_zero_msg)
    TextView tv_zero_msg;
    @Bind(R.id.scrap_tv)
    TextView scrap_tv;
    @Bind(R.id.scrap_ll)
    LinearLayout scrap_ll;
    @Bind(R.id.guide_iv)
    ImageView guide_iv;
    @Bind(R.id.not_question_tv)
    TextView not_question_tv;
    @Bind(R.id.scrap_layout)
    LinearLayout scrap_layout;

    private View lay1;
    private View lay2;
    private View popRoot;
    private String doctorId = YMUserService.getCurUserId();

    private int guideFlag = 0;

//    @Bind(R.id.v_history)
//    View v_history;
//    @Bind(R.id.v_setting)
//    View v_setting;


//    @Bind(R.id.main_drawer_layout)
//    DrawerLayout main_drawer_layout;
//    @Bind(R.id.main_right_drawer_layout)
//    View main_right_drawer_layout;

    private ConsultPagerFragment pagerFragment;

    private SelectBasePopupWindow mPopupWindow;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ConsultOnlineActivity.class));
    }

    @Override
    protected void initView() {
//        initTitleBar();
        if (pagerFragment == null) {
            pagerFragment = new ConsultPagerFragment();
            pagerFragment.setiTip(this);
            displayFragment(pagerFragment);
        }
        scrap_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConsultOnlineActivity.this,QuestionListActivity.class));
            }
        });
        toolbar.setVisibility(View.GONE);
        if (SharedPreferenceUtil.getInstance(this).getBoolean("guide",true)){
            SharedPreferenceUtil.getInstance(this).putBoolean("guide",false);
            guide_iv.setVisibility(View.VISIBLE);
            guide_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    guideFlag++;
                    switch (guideFlag){
                        case 1:
                            guide_iv.setBackgroundResource(R.drawable.guide_page_icon1);
                            break;
                        case 2:
                            guide_iv.setBackgroundResource(R.drawable.guide_page_icon2);
                            break;
                        case 3:
                            guide_iv.setBackgroundResource(R.drawable.guide_page_icon3);
                            break;
                    }
                    if (guideFlag>3){
                        guide_iv.setVisibility(View.GONE);
                        toolbar.setVisibility(View.VISIBLE);
                        initTitleBar();
                    }
                }
            });
        }else{
            toolbar.setVisibility(View.VISIBLE);
            initTitleBar();
        }
        WebSocketRxBus.registerWebSocketChatMagListener(new EventSubscriber<ChatMsg>() {
            @Override
            public void onNext(Event<ChatMsg> chatMsgEvent) {
                loadPoolData(doctorId);
            }

        }, this);

        YmRxBus.registerAdoptQuestionListener(new EventSubscriber() {
            @Override
            public void onNext(Object o) {
                //stone 断开自动重连 2018年01月11日13:19:59
                loadPoolData(doctorId);
            }
        },this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPoolData(doctorId);
    }

    private void initViewFlipper(List<IMQuestionBean> data) {
        if (null!=data &&data.size()!=0) {
            scrap_layout.setVisibility(View.VISIBLE);
            not_question_tv.setVisibility(View.GONE);
            scrap_question_vf.setVisibility(View.VISIBLE);
            tv_zero_msg.setVisibility(View.GONE);
            scrap_tv.setText(data.size()+"");
            Animation in = new TranslateAnimation(0, 0, 300, 0);
            in.setDuration(300);
            in.setInterpolator(new AccelerateInterpolator());
            Animation out = new TranslateAnimation(0, 0, 0, -300);
            out.setDuration(300);
            out.setInterpolator(new AccelerateInterpolator());
            scrap_question_vf.setInAnimation(in);
            scrap_question_vf.setOutAnimation(out);
            if (scrap_question_vf.isFlipping()) {
                scrap_question_vf.stopFlipping();
            }
            scrap_question_vf.removeAllViews();
            double priceFlag = 0;
            final ArrayList<IMQuestionBean> scrapQuestionData = new ArrayList<>();
            for (IMQuestionBean iMQuestionBean : data){
                if (scrapQuestionData.size()==5){
                    break;
                }
                if (!TextUtils.isEmpty(iMQuestionBean.getAmount())&&
                        Double.parseDouble(iMQuestionBean.getAmount())>priceFlag){
                    scrapQuestionData.clear();
                    priceFlag = Double.parseDouble(iMQuestionBean.getAmount());
                    scrapQuestionData.add(iMQuestionBean);
                }else if(Double.parseDouble(iMQuestionBean.getAmount())==priceFlag){
                    scrapQuestionData.add(iMQuestionBean);
                }
            }
            if (scrapQuestionData.size()==0){
                tv_zero_msg.setVisibility(View.VISIBLE);
                scrap_question_vf.setVisibility(View.GONE);
            }else {
                for (int i = 0; i < scrapQuestionData.size(); i++) {
                    View v = this.getLayoutInflater().inflate(R.layout.scrap_question_item, null);

                    ImageView iv_patient_head = (ImageView) v.findViewById(R.id.iv_patient_head);
                    Glide.with(this).load(scrapQuestionData.get(i).getUser_photo()).into(iv_patient_head);

                    TextView tv_patient_name = (TextView) v.findViewById(R.id.tv_patient_name);
                    tv_patient_name.setText(scrapQuestionData.get(i).getPatient_name());

                    TextView tv_patient_sex = (TextView) v.findViewById(R.id.tv_patient_sex);
                    if (scrapQuestionData.get(i).getPatient_sex().equals("1")) {
                        tv_patient_sex.setText("男");
                    } else {
                        tv_patient_sex.setText("女");
                    }

                    TextView tv_patient_age = (TextView) v.findViewById(R.id.tv_patient_age);
                    tv_patient_age.setText(scrapQuestionData.get(i).getPatient_age() + "岁");

                    TextView tv_time = (TextView) v.findViewById(R.id.tv_time);
                    tv_time.setText(scrapQuestionData.get(i).getType_tag());

                    TextView tv_question_desc = (TextView) v.findViewById(R.id.tv_question_desc);
                    tv_question_desc.setText(scrapQuestionData.get(i).getContent());

                    TextView tv_scrap = (TextView) v.findViewById(R.id.tv_scrap);
                    final int finalI = i;
                    tv_scrap.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ConsultChatActivity.startActivity(true, ConsultOnlineActivity.this,
                                    scrapQuestionData.get(finalI).getQid(), scrapQuestionData.get(finalI).getUid(),
                                    scrapQuestionData.get(finalI).getPatient_name(), scrapQuestionData.get(finalI).getType().equals(QuestionsListEntity.PAY_TYPE_RMB_SPECIFY)
                            );
                        }
                    });
                    scrap_question_vf.addView(v);
                }
            }
            if(scrapQuestionData.size()>1){
                scrap_question_vf.setFlipInterval(3000);
                scrap_question_vf.startFlipping();
            }

        }else{
            scrap_layout.setVisibility(View.GONE);
            not_question_tv.setVisibility(View.VISIBLE);
            tv_zero_msg.setVisibility(View.VISIBLE);
            scrap_question_vf.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {
        loadPoolData(doctorId);
    }

    public void loadPoolData(String doctorId) {
        ServiceProvider.getQuestionsPool(1,30,doctorId, new Subscriber<QuestionsPoolRspEntity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(QuestionsPoolRspEntity entry) {
                if (null!=entry) {
                    if (entry.getData() != null) {
                        initViewFlipper(entry.getData().getList());
                    }
                }else{
//                    Toast.makeText(ConsultOnlineActivity.this,"数据返回异常",Toast.LENGTH_SHORT).show();
                    Log.e("dataError","数据返回异常");
                }
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_consult_online;
    }

    private void displayFragment(Fragment fragment) {
        if (!fragment.isAdded()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fl_container, fragment);
            transaction.commitAllowingStateLoss();
        }
    }

    private void initTitleBar() {
        titleBarBuilder.setTitleText(getString(R.string.online_consultation));

        titleBarBuilder.addItem("设置",new ItemClickListener() {
            @Override
            public void onClick() {
                showPop();
            }
        }).build();
    }


    @Override
    public void showTip(String text) {
        if(TextUtils.isEmpty(text)){
            tvTip.setVisibility(View.GONE);
            return;
        }
        tvTip.setText(text);
    }

    //    @OnClick({R.id.v_history, R.id.v_setting})
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.v_history:
//                main_drawer_layout.closeDrawer(main_right_drawer_layout);
//                ConsultAnsweredListActivity.startActivity(ConsultOnlineActivity.this);
//                break;
//
//            case R.id.v_setting:
//                main_drawer_layout.closeDrawer(main_right_drawer_layout);
//                startActivity(new Intent(ConsultOnlineActivity.this, MessageSettingActivity.class));
//                break;
//        }
//    }
//


    private void showPop() {
        if (mPopupWindow == null) {
            mPopupWindow = new SelectBasePopupWindow(true, this);
            popRoot = View.inflate(getBaseContext(), R.layout.pop_layout_message_setting, null);
            lay1 = popRoot.findViewById(R.id.lay1);
            lay2 = popRoot.findViewById(R.id.lay2);
            lay1.setOnClickListener(mPopOnClickListener);
            lay2.setOnClickListener(mPopOnClickListener);
        }
        if (!mPopupWindow.isShowing()) {
//            mPopupWindow.init(popRoot).showAtLocation(this.getWindow().getDecorView(), Gravity.TOP | Gravity.RIGHT, (AppUtils.dpToPx(48, getResources()) - 24) / 2 + AppUtils.dpToPx(5, getResources()) - 30 - 27, AppUtils.dpToPx(48, getResources()) + YMApplication.getStatusBarHeight() - 30);
            mPopupWindow.init(popRoot).showAtLocation(this.getWindow().getDecorView(), Gravity.TOP | Gravity.RIGHT, AppUtils.dpToPx(5, getResources()), AppUtils.dpToPx(38, getResources()) + YMApplication.getStatusBarHeight());
        }
    }


    /**
     * pop监听器 种族
     */
    private View.OnClickListener mPopOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mPopupWindow.dismiss();
            switch (view.getId()) {
                case R.id.lay1:
                    ConsultAnsweredListActivity.startActivity(ConsultOnlineActivity.this);
                    break;
                case R.id.lay2:
                    //留言设置 stone
                    StatisticalTools.eventCount(ConsultOnlineActivity.this, Constants.MESSAGESETTING);

                    startActivity(new Intent(ConsultOnlineActivity.this, MessageSettingActivity.class));
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus && YMApplication.getIsFirstShowOnlineNotice()) {
//            YMApplication.setIsFirstShowOnlineNotice(false);
//            if (!SharedPreferenceUtil.getInstance(this).getBoolean("guide",true)) {
//                showPop();
//            }
//            new AddMessageSettingGuideDialogFragment().show(getSupportFragmentManager(), "ConsultOnlineActivity");
//        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scrap_question_vf.stopFlipping();
    }
}
