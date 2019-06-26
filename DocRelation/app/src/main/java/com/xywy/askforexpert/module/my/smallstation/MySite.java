package com.xywy.askforexpert.module.my.smallstation;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.model.Service;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;
import com.xywy.askforexpert.module.discovery.service.ServiceOpenStateUtils;
import com.xywy.askforexpert.module.main.home.request.ApplyClubOrImwdStateRequest;
import com.xywy.askforexpert.module.main.service.ApplyBusinessResultActivity;
import com.xywy.askforexpert.module.my.userinfo.ApproveInfoActivity;
import com.xywy.askforexpert.module.my.userinfo.CheckStateActivity;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.retrofit.net.BaseData;

import java.util.Map;


/**
 * 4.2.2版 我的小站/我的诊室
 *
 * @author Jack Fang
 *         <p>
 *         5.4.0全新改版
 */
public class MySite extends YMBaseActivity implements View.OnClickListener {

    private TextView tv_club_assign_state, tv_club_assign, tv_imwd_reward_state,
            tv_imwd_reward, tv_imwd_assign_state, tv_imwd_assign, tv_remark;
    private ImageView iv_club_assign, iv_imwd_reward, iv_imwd_assign;
    private TextView tv_family_doctor_state, tv_phone_doctor_state, tv_transferTreatment_state;
    private TextView tv_family_doctor, tv_phone_doctor, tv_transferTreatment;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_my_site;
    }

    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("我的诊室");
        initQuestionSquareAndImwdView();
        initRemartTextView();


    }

    /**
     * 初始化问题广场和即时问答的业务开通情况的view
     */
    private void initQuestionSquareAndImwdView() {
        iv_club_assign = (ImageView) findViewById(R.id.iv_club_assign);
        tv_club_assign_state = (TextView) findViewById(R.id.tv_club_assign_state);
        tv_club_assign = (TextView) findViewById(R.id.tv_club_assign);
        tv_club_assign.setOnClickListener(this);
        iv_imwd_reward = (ImageView) findViewById(R.id.iv_imwd_reward);
        tv_imwd_reward_state = (TextView) findViewById(R.id.tv_imwd_reward_state);
        tv_imwd_reward = (TextView) findViewById(R.id.tv_imwd_reward);
        tv_imwd_reward.setOnClickListener(this);
        iv_imwd_assign = (ImageView) findViewById(R.id.iv_imwd_assign);
        tv_imwd_assign_state = (TextView) findViewById(R.id.tv_imwd_assign_state);
        tv_imwd_assign = (TextView) findViewById(R.id.tv_imwd_assign);
        tv_imwd_assign.setOnClickListener(this);
        tv_family_doctor_state = (TextView) findViewById(R.id.tv_family_doctor_state);
        tv_family_doctor = (TextView) findViewById(R.id.tv_family_doctor);
        tv_family_doctor.setOnClickListener(this);
        tv_phone_doctor_state = (TextView) findViewById(R.id.tv_phone_doctor_state);
        tv_phone_doctor = (TextView) findViewById(R.id.tv_phone_doctor);
        tv_phone_doctor.setOnClickListener(this);
        tv_transferTreatment_state = (TextView) findViewById(R.id.tv_transferTreatment_state);
        tv_transferTreatment = (TextView) findViewById(R.id.tv_transferTreatment);
        tv_transferTreatment.setOnClickListener(this);
        setState();
    }

    private void setState() {
        if (!YMApplication.isDoctorApprove()) {
            //未认证
            iv_club_assign.setImageResource(R.drawable.wtgc_normal);
            iv_imwd_reward.setImageResource(R.drawable.imwd_noraml);
            iv_imwd_assign.setImageResource(R.drawable.imwd_assing_normal);
            tv_club_assign_state.setVisibility(View.GONE);
            tv_imwd_reward_state.setVisibility(View.GONE);
            tv_imwd_assign_state.setVisibility(View.GONE);
            tv_club_assign.setText(Constants.APPLY_FOR_OPENING);
            tv_imwd_reward.setText(Constants.APPLY_FOR_OPENING);
            tv_imwd_assign.setText(Constants.APPLY_FOR_OPENING);
            tv_club_assign.setBackgroundResource(R.drawable.activity_my_site_item_normal_bg);
            tv_imwd_reward.setBackgroundResource(R.drawable.activity_my_site_item_normal_bg);
            tv_imwd_assign.setBackgroundResource(R.drawable.activity_my_site_item_normal_bg);
            tv_family_doctor_state.setVisibility(View.GONE);
            tv_phone_doctor_state.setVisibility(View.GONE);
            tv_transferTreatment_state.setVisibility(View.GONE);
            tv_family_doctor.setText(Constants.APPLY_FOR_OPENING);
            tv_phone_doctor.setText(Constants.CUSTOMER_SERVICE_OPEN);
            tv_transferTreatment.setText(Constants.CUSTOMER_SERVICE_OPEN);
        } else {
            //已认证
            tv_club_assign_state.setVisibility(View.VISIBLE);
            tv_imwd_reward_state.setVisibility(View.VISIBLE);
            tv_imwd_assign_state.setVisibility(View.VISIBLE);
            String questionSquareOpenStateAssign = ServiceOpenStateUtils.getQuestionSquareOpenStateAssign();
            Map<String, Service> serviceMark = YMApplication.getServiceMark();
            Service service = serviceMark.get(Constants.CLUB_ASSIGN_CN);
            String status = service.status+"";
            if(Constants.FUWU_AUDIT_STATUS_3.equals(status)){
                tv_club_assign.setText(Constants.CLOSED);
                tv_club_assign.setTextColor(getResources().getColor(R.color.c999));
                tv_club_assign.setBackgroundResource(R.drawable.activity_my_site_item_applyed_bg);
                tv_club_assign_state.setVisibility(View.VISIBLE);
                tv_club_assign_state.setText(Constants.CLOSED);
                tv_club_assign_state.setTextColor(getResources().getColor(R.color.c999));
                tv_club_assign_state.setBackgroundResource(R.drawable.activity_my_site_item_applyed_bg);
            }else{
                setBusinessState(questionSquareOpenStateAssign, iv_club_assign, R.drawable.wtgc_open, R.drawable.wtgc_normal);
                setBusinessState(questionSquareOpenStateAssign, tv_club_assign, tv_club_assign_state);
            }


            String imwdOpenStateAssign = ServiceOpenStateUtils.getImwdOpenStateAssign();
            setBusinessState(imwdOpenStateAssign, iv_imwd_assign, R.drawable.imwd_assign_open, R.drawable.imwd_assing_normal);
            setBusinessState(imwdOpenStateAssign, tv_imwd_assign, tv_imwd_assign_state);

            String imwdOpenStateReward = ServiceOpenStateUtils.getImwdOpenStateReward();
            setBusinessState(imwdOpenStateReward, iv_imwd_reward, R.drawable.imwd_open, R.drawable.imwd_noraml);
            setBusinessState(imwdOpenStateReward, tv_imwd_reward, tv_imwd_reward_state);

            String homeDoctorOpenState = ServiceOpenStateUtils.getHomeDoctorOpenState();
            setBusinessState(homeDoctorOpenState, tv_family_doctor, tv_family_doctor_state);
            String callDoctorOpenState = ServiceOpenStateUtils.getCallDoctorOpenState();
            if (Constants.OPEN.equals(callDoctorOpenState)) {
                tv_phone_doctor.setText(Constants.ENTER);
                tv_phone_doctor_state.setVisibility(View.VISIBLE);
                tv_phone_doctor_state.setText(Constants.OPEN);
                tv_phone_doctor_state.setBackgroundResource(R.drawable.activity_my_site_item_open_bg);
            } else {
                tv_phone_doctor_state.setVisibility(View.GONE);
                tv_phone_doctor.setText(Constants.CUSTOMER_SERVICE_OPEN);
            }
            String transferTreatmentOpenState = ServiceOpenStateUtils.getTransferTreatmentOpenState();
            if (Constants.OPEN.equals(transferTreatmentOpenState)) {
                tv_transferTreatment_state.setVisibility(View.VISIBLE);
                tv_transferTreatment_state.setText(Constants.OPEN);
                tv_transferTreatment_state.setBackgroundResource(R.drawable.activity_my_site_item_open_bg);
                tv_transferTreatment.setText(Constants.ENTER);
            } else {
                tv_transferTreatment_state.setVisibility(View.GONE);
                tv_transferTreatment.setText(Constants.CUSTOMER_SERVICE_OPEN);
            }
        }
    }

    private void setBusinessState(String state, TextView tv_bottom, TextView tv_state) {
        if (Constants.OPEN.equals(state)) { //已开通 状态栏为已开通,按钮为进入，点击后进入具体业务页面
            tv_bottom.setText(Constants.ENTER);
            tv_bottom.setEnabled(true);
            tv_bottom.setTextColor(getResources().getColor(R.color.white));
            tv_bottom.setBackgroundResource(R.drawable.activity_my_site_item_normal_bg);
            tv_state.setVisibility(View.VISIBLE);
            tv_state.setText(Constants.OPEN);
            tv_state.setBackgroundResource(R.drawable.activity_my_site_item_open_bg);
        }else if(Constants.APPLY_FOR_OPENING.equals(state)){  //未开通 状态栏隐藏
            tv_bottom.setText(Constants.APPLY_FOR_OPENING);
            tv_bottom.setTextColor(getResources().getColor(R.color.white));
            tv_bottom.setBackgroundResource(R.drawable.activity_my_site_item_normal_bg);
            tv_state.setVisibility(View.GONE);
        } else if (Constants.UNDER_REVIEW.equals(state)) { //状态栏为审核中，按钮为已申请，按钮置灰不可点击
            tv_bottom.setText(Constants.APPLY);
            tv_bottom.setTextColor(getResources().getColor(R.color.c999));
            tv_bottom.setBackgroundResource(R.drawable.activity_my_site_item_applyed_bg);
            tv_state.setVisibility(View.VISIBLE);
            tv_state.setText(Constants.PENDING);
            tv_state.setBackgroundResource(R.drawable.activity_my_site_item_under_review_bg);
        } else if (Constants.REFUSE.equals(state)) { //未通过：状态栏为未通过，按钮为重新申请，点击后进入未通过原因页面
            tv_bottom.setText(Constants.REOPEN);
            tv_bottom.setTextColor(getResources().getColor(R.color.white));
            tv_bottom.setBackgroundResource(R.drawable.activity_my_site_item_normal_bg);
            tv_state.setVisibility(View.VISIBLE);
            tv_state.setText(Constants.REFUSE);
            tv_state.setBackgroundResource(R.drawable.activity_my_site_item_refuse_bg);
        } else {  //已关闭：运营后台关闭的服务。图标及按钮置灰。按钮文案未已关闭
//            tv_bottom.setText(Constants.CLOSED);
//            tv_bottom.setEnabled(false);
//            tv_bottom.setBackgroundResource(R.drawable.activity_my_site_item_applyed_bg);
//            tv_state.setVisibility(View.VISIBLE);
//            tv_state.setText(Constants.CLOSED);
//            tv_state.setBackgroundResource(R.drawable.activity_my_site_item_closed_bg);

            //暂时的补坑方法，目前将未通过和关闭都按照未未通过处理，fuck
            tv_bottom.setText(Constants.REOPEN);
            tv_bottom.setTextColor(getResources().getColor(R.color.white));
            tv_bottom.setBackgroundResource(R.drawable.activity_my_site_item_normal_bg);
            tv_state.setVisibility(View.VISIBLE);
            tv_state.setText(Constants.REFUSE);
            tv_state.setBackgroundResource(R.drawable.activity_my_site_item_refuse_bg);
        }
    }

    private void setBusinessState(String state, ImageView iv, int resIdOpen, int resIdNormal) {
        if (Constants.OPEN.equals(state)) { //已开通 状态栏为已开通,按钮为进入，点击后进入具体业务页面
            iv.setImageResource(resIdOpen);
        } else {  //除了未开通的其他状态
            iv.setImageResource(resIdNormal);
        }
    }

    private void initRemartTextView() {
        tv_remark = (TextView) findViewById(R.id.tv_remark);
        String text = "";
        String endText = "";
        if (!YMApplication.isDoctorApprove()) {  //未认证
            endText = Constants.AUTHENTICATION;
            text = Constants.REMARK_UNAUTHENTICATION + " "+endText;
        } else {  //已认证
            if (!YMUserService.isQuestionSquareOpen() || !YMUserService.isImwdOpen()) {//问题广场或者即时问答未开通
                endText = Constants.CUSTOMER_SERVICE;
                text = Constants.REMARK_UNOPEN + " "+endText;
            } else { //问题广场和即时问答都开通了,，则显示答题
                endText = Constants.ANSWER;
                text = Constants.REMARK_OPEN + " "+endText;
            }
        }

        SpannableString spannableString = new SpannableString(text + ".");
        final String finalText = text;
        spannableString.setSpan(new ClickableSpan() {

            @Override
            public void onClick(View arg0) {
                if (finalText.contains(Constants.AUTHENTICATION)) {
                    //去认证 stone
                    StatisticalTools.eventCount(MySite.this, Constants.CERTIFICATION);

                    gotoApprove();
                } else if (finalText.contains(Constants.ANSWER)) {
                    finish();
                } else {
                    DialogUtil.showCustomDialog(MySite.this, R.layout.custom_dialogue, "", "请联系寻医问药客服帮您开通此服务：4008591200", "",
                            "确定", "取消", new MyCallBack<Object>() {

                                @Override
                                public void onClick(Object data) {
                                    Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4008591200"));//跳转到拨号界面，同时传递电话号码
                                    startActivity(dialIntent);
                                }
                            });
                }
            }
        }, 0, spannableString.length()-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.c999)), 0, spannableString.length()-endText.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.c_00c9ab)), spannableString.length()-endText.length()-1, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //设置最后几个字的颜色
        spannableString.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), spannableString.length()-1, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //设置前景色为透明色
        tv_remark.append(spannableString);
        //为了响应点击
        tv_remark.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected void initData() {

    }

    public void onResume() {
        super.onResume();
        StatisticalTools.onResume(this);
    }

    public void onPause() {
        super.onPause();
        StatisticalTools.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_club_assign:
                //问题广场(指定)
                handleAction(Constants.CLUB_ASSIGN_CN);
                break;
            case R.id.tv_imwd_reward:
                //即时问答(悬赏)
                handleAction(Constants.IMWD_REWARD_CN);
                break;
            case R.id.tv_imwd_assign:
                //即时问答(指定)
                handleAction(Constants.IMWD_ASSIGN_CN);
                break;
            case R.id.tv_family_doctor:
                //家庭医生申请开通 stone
                StatisticalTools.eventCount(MySite.this, Constants.APPLYFOROPENINGJTYS);

                CommonUtils.gotoService(MySite.this, 5);
//                String homeDoctorOpenState = ServiceOpenStateUtils.getHomeDoctorOpenState();
//                if(Constants.OPEN.equals(homeDoctorOpenState)){
//                    //进入家庭医生页面
//                    CommonUtils.gotoService(MySite.this, 5);
//                }else if(Constants.UNOPEN.equals(homeDoctorOpenState)){
//                    //跳转到申请开通家庭医生页面
//                    CommonUtils.gotoService(MySite.this, 5);
//                }else {
//                    //跳转到申请开通家庭医生页面
//                    CommonUtils.gotoService(MySite.this, 5);
//                }
                break;
            case R.id.tv_phone_doctor:
                if (!YMApplication.isDoctorApprove()) {
                    //未认证
                    CommonUtils.gotoService(MySite.this, 7);
                } else {
                    String callDoctorOpenState = ServiceOpenStateUtils.getCallDoctorOpenState();
                    if (Constants.OPEN.equals(callDoctorOpenState)) {
                        //进入电话医生页面
                        CommonUtils.gotoService(MySite.this, 7);
                    }else {
                        //电话医生联系客服开通 stone
                        StatisticalTools.eventCount(MySite.this, Constants.OPENINGBYCUSTOMERSERVICEDHYS);
                        //联系客服开通电话医生
                        showDialogue("请联系寻医问药客服帮您开通此服务：4008591200", "呼叫", "取消");
                    }
//                    else if(Constants.APPLY_FOR_OPENING.equals(callDoctorOpenState)){
//                        //电话医生联系客服开通 stone
//                        StatisticalTools.eventCount(MySite.this, Constants.OPENINGBYCUSTOMERSERVICEDHYS);
//                        //联系客服开通电话医生
//                        showDialogue("请联系寻医问药客服帮您开通此服务：4008591200", "呼叫", "取消");
//                    }
                }

                break;
            case R.id.tv_transferTreatment:
                if (!YMApplication.isDoctorApprove()) {  //未认证
                    CommonUtils.gotoService(MySite.this, 10);
                } else {  //已认证
                    String transferTreatmentOpenState = ServiceOpenStateUtils.getTransferTreatmentOpenState();
                    if (Constants.OPEN.equals(transferTreatmentOpenState)) {
                        //进入预约转诊页面
                        CommonUtils.gotoService(MySite.this, 10);
                    }else {
                        //预约转诊联系客服开通 stone
                        StatisticalTools.eventCount(MySite.this, Constants.OPENINGBYCUSTOMERSERVICEYYZZ);
                        //联系客服开通预约转诊
                        showDialogue("请联系寻医问药客服帮您开通此服务：4008591200", "呼叫", "取消");
                    }

//                    else if(Constants.APPLY_FOR_OPENING.equals(transferTreatmentOpenState)){
//                        //预约转诊联系客服开通 stone
//                        StatisticalTools.eventCount(MySite.this, Constants.OPENINGBYCUSTOMERSERVICEYYZZ);
//                        //联系客服开通预约转诊
//                        showDialogue("请联系寻医问药客服帮您开通此服务：4008591200", "呼叫", "取消");
//                    }
                }

                break;
            default:
                break;
        }
    }

    private void showDialogue(String content, String btnOKText, String btnCancelText) {
        DialogUtil.showCustomDialog(MySite.this, R.layout.custom_dialogue, "", content, "",
                btnOKText, btnCancelText, new MyCallBack<Object>() {

                    @Override
                    public void onClick(Object data) {
                        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4008591200"));//跳转到拨号界面，同时传递电话号码
                        startActivity(dialIntent);
                    }
                });
    }

    //去认证 stone
    private void gotoApprove() {
        if (YMUserService.isGuest()) {
            DialogUtil.LoginDialog(MySite.this);
            return;
        }

//        int ran = new Random().nextInt(7) - 2;

        switch (YMApplication.DoctorApproveType()) {
//        switch (ran) {
            case -2:
                CheckStateActivity.startActivity(MySite.this, "check_reject", "被驳回");
                break;
            case -1:
                CheckStateActivity.startActivity(MySite.this, "checking", "审核中");
                break;
            case 0:
                if (YMApplication.getUserInfo() != null) {
                    Intent intent = new Intent(MySite.this, ApproveInfoActivity.class);
                    startActivity(intent);
                }
//                else {
//                    mSkip = true;
//                    // 获取用户信息 stone
//                    YMUserService.refreshUserInfo(MySite.this, mRefreshUserInfoCallback);
//                }
                break;
            case 1:
                ToastUtils.shortToast("已认证");
                break;
            case 2:
                CheckStateActivity.startActivity(MySite.this, "check_err", "不通过");
                break;
            case 3:
                ToastUtils.shortToast("待跟踪");
                break;
            case 4:
                ToastUtils.shortToast("暂不开通");
                break;

            default:
                break;
        }
    }

    private void handleAction(final String serviceName) {
        DialogUtil.showUserCenterCertifyDialog(MySite.this, new MyCallBack() {
            @Override
            public void onClick(Object data) {
//                StatisticalTools.eventCount(MySite.this, "Onlineconsulting");
                Map<String, Service> serviceMark = YMApplication.getServiceMark();
                Service service = serviceMark.get(serviceName);
                handleState(service,serviceName);
            }
        }, null, serviceName);
    }

    private void handleState(Service service,String serviceName) {
        String status = service.status+"";
        if(Constants.FUWU_AUDIT_STATUS_NO.equals(status)){
            if(Constants.CLUB_ASSIGN_CN.equals(serviceName)){
                //问题广场的指定
                applyClubAssignState();
            }else if(Constants.IMWD_REWARD_CN.equals(serviceName)){
                //即时问答(悬赏)
                applyImwdState(Constants.TYPE_2);
            }else if(Constants.IMWD_ASSIGN_CN.equals(serviceName)){
                //即时问答(指定)
                applyImwdState(Constants.TYPE_3);
            }
        }else if(Constants.FUWU_AUDIT_STATUS_2.equals(status)
                || Constants.FUWU_AUDIT_STATUS_0.equals(status)){
            //未通过或者审核中
            Intent intent = new Intent(MySite.this,ApplyBusinessResultActivity.class);
            intent.putExtra(Constants.INTENT_KEY_TITLE,serviceName);
            startActivity(intent);
        }else if(Constants.FUWU_AUDIT_STATUS_1.equals(status)){
            //已开通
            if(Constants.CLUB_ASSIGN_CN.equals(serviceName)){
                //问题广场的指定
                CommonUtils.gotoService(MySite.this, 1);
            }else if(Constants.IMWD_REWARD_CN.equals(serviceName)){
                //即时问答(悬赏)
                CommonUtils.gotoService(MySite.this, 888);
            }else if(Constants.IMWD_ASSIGN_CN.equals(serviceName)){
                //即时问答(指定)
                CommonUtils.gotoService(MySite.this, 888);
            }
        }
    }

    //医生即时问答(悬赏，指定)提交审核
    private void applyImwdState(final int type) {
        if (Constants.TYPE_2 == type) {  // 2悬赏 3指定
            //即时问答悬赏 stone
            StatisticalTools.eventCount(MySite.this, Constants.APPLYFOROPENINGJSWDXS);
        } else {
            //即时问答指定 stone
            StatisticalTools.eventCount(MySite.this, Constants.APPLYFOROPENINGJSWDZD);
        }
        String curUserId = YMUserService.getCurUserId();
        ApplyClubOrImwdStateRequest.getInstance().getImwdState(curUserId, type).subscribe(new BaseRetrofitResponse<BaseData>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onNext(BaseData baseData) {
                super.onNext(baseData);
                if (Constants.TYPE_2 == type) {  // 2悬赏 3指定
                    setBusinessState(Constants.UNDER_REVIEW, tv_imwd_reward, tv_imwd_reward_state);
                } else {
                    setBusinessState(Constants.UNDER_REVIEW, tv_imwd_assign, tv_imwd_assign_state);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    //医生问题广场指定付费提交审核
    private void applyClubAssignState() {
        //问题广场指定 stone
        StatisticalTools.eventCount(MySite.this, Constants.APPLYFOROPENINGWTGCZD);

        String curUserId = YMUserService.getCurUserId();
        ApplyClubOrImwdStateRequest.getInstance().getClubAssignState(curUserId).subscribe(new BaseRetrofitResponse<BaseData>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onNext(BaseData baseData) {
                super.onNext(baseData);
                setBusinessState(Constants.UNDER_REVIEW, tv_club_assign, tv_club_assign_state);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }
}
