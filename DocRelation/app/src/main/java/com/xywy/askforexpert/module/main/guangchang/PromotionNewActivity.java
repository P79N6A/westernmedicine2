package com.xywy.askforexpert.module.main.guangchang;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.retrofitWrapper.CommonResponse;
import com.xywy.askforexpert.model.main.promotion.PromotionBean;
import com.xywy.askforexpert.model.main.promotion.PromotionPageBean;
import com.xywy.askforexpert.module.main.prelaunch.follow.FollowDialog;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by bailiangjin on 2016/10/24.
 */

public class PromotionNewActivity extends YMBaseActivity {


    @Bind(R.id.iv_top_line)
    ImageView iv_top_line;

    @Bind(R.id.tv_cur_state)
    TextView tv_cur_state;


    @Bind(R.id.tv_upper_state)
    TextView tv_upper_state;


    @Bind(R.id.tv_desc)
    TextView tv_desc;

    @Bind(R.id.tv_answer_days)
    TextView tv_answer_days;

    @Bind(R.id.tv_dynamic_rate)
    TextView tv_dynamic_rate;

    @Bind(R.id.tv_pass_rate)
    TextView tv_pass_rate;

    @Bind(R.id.tv_punishment)
    TextView tv_punishment;

    @Bind(R.id.tv_adopt_rate)
    TextView tv_adopt_rate;


    @Bind(R.id.ll_content)
    LinearLayout ll_content;


    @Bind(R.id.tv_top_level_desc)
    TextView tv_top_level_desc;


    public static final int JOIN_GROUP_TAG=100;

    public static void start(Context context) {

        Intent intent = new Intent(context, PromotionNewActivity.class);
        context.startActivity(intent);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //获取最新数据 刷新界面
        getCurrentData();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_promotion_new;
    }

    @Override
    protected void beforeViewBind() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        titleBarBuilder.setTitleText("晋级");
        getCurrentData();

    }

    private void getCurrentData() {
        SquareService.getPromotionInfo(new CommonResponse<PromotionPageBean>(YMApplication.getAppContext()) {
            @Override
            public void onNext(PromotionPageBean promotionPageBean) {

                if (null != promotionPageBean && null != promotionPageBean.getData()) {
                    //shortToast(GsonUtils.INSTANCE.toJson(promotionPageBean));
                    PromotionBean promotionBean = promotionPageBean.getData();
                    refreshData(promotionBean);
                }
            }
        });
    }

    /**
     * 刷新数据
     * @param promotionBean
     */
    private void refreshData(PromotionBean promotionBean) {
        tv_cur_state.setText(promotionBean.getCurLevel());

        tv_desc.setText(promotionBean.isTopLevel()?"最高级别":"本阶段答题情况");

        if(promotionBean.isTopLevel()){
            ll_content.setVisibility(View.GONE);
            iv_top_line.setVisibility(View.GONE);
            tv_upper_state.setVisibility(View.GONE);
            tv_top_level_desc.setVisibility(View.VISIBLE);
        }else {
            ll_content.setVisibility(View.VISIBLE);
            iv_top_line.setVisibility(View.VISIBLE);
            tv_upper_state.setVisibility(View.VISIBLE);
            tv_top_level_desc.setVisibility(View.GONE);

            tv_upper_state.setText(promotionBean.getNextLevel());

            tv_answer_days.setText("" + promotionBean.getAnswerDays());
            tv_dynamic_rate.setText("" + promotionBean.getDynamicRate());
            tv_adopt_rate.setText(promotionBean.getAdoptRate());
            tv_pass_rate.setText(promotionBean.getPassRate());
            tv_punishment.setText(promotionBean.getPunishTime());
        }

    }


    @OnClick({R.id.btn_certification, R.id.tv_promotion_standard})
    public void onClick(View v) {
        //点击事件 事件分发处理
        switch (v.getId()) {

            case R.id.btn_certification:
                shortToast("点击了认证");
                break;
            case R.id.tv_promotion_standard:
                shortToast("点击了晋级标准");
//                new DemoDialog(PromotionNewActivity.this).setTitleText("测试").show();
                new FollowDialog(PromotionNewActivity.this,uiHandler).hideTitle().show();

                break;

            default:
                break;
        }
    }


    @Override
    protected void handleMsg(Message msg) {
        super.handleMsg(msg);

        switch (msg.what){
            case JOIN_GROUP_TAG:
                break;
            default:
                break;
        }
    }
}
