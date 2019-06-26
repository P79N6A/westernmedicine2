package com.xywy.askforexpert.module.discovery.medicine;

import android.content.Intent;
import android.net.Uri;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.module.discovery.medicine.common.MyBaseActivity;
import com.xywy.askforexpert.module.discovery.medicine.module.account.UserManager;
import com.xywy.askforexpert.module.discovery.medicine.module.account.beans.UserBean;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.activity.PersonInfoActivity;
import com.xywy.util.TextViewUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

//import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.activity.PersonInfoActivity;
//import com.xywy.askforexpert.module.my.userinfo.PersonInfoActivity;

/**
 * Created by xgxg on 2017/10/20.
 * 实名认证页面
 */

public class VerifyActivity extends MyBaseActivity {
    @Bind(R.id.m_iv_state)
    ImageView mIvState;
    @Bind(R.id.m_btn_verify)
    Button mBtnVerify;
    @Bind(R.id.m_tv_fail_info)
    TextView mTvFailInfo;
    @Bind(R.id.m_rl_fail)
    LinearLayout mRlFail;
    @Bind(R.id.m_ll_checking)
    LinearLayout m_ll_checking;
    @Override
    protected void initView() {
        String state = UserManager.getInstance().getCurrentLoginUser().getState().getId();
//        LogUtils.i("UserManager.getInstance()="+UserManager.getInstance());
//        LogUtils.i("getCurrentLoginUser()="+UserManager.getInstance().getCurrentLoginUser());
//        LogUtils.i("getState()="+UserManager.getInstance().getCurrentLoginUser().getState());
//        LogUtils.i("getId()="+UserManager.getInstance().getCurrentLoginUser().getState().getId());
        if (state.equals(UserBean.UserState.checking)) {
            mRlFail.setVisibility(View.GONE);
            mBtnVerify.setVisibility(View.GONE);
            m_ll_checking.setVisibility(View.VISIBLE);
        } else if (state.equals(UserBean.UserState.failed)) {
            mIvState.setImageResource(R.drawable.verify_failed);
            mRlFail.setVisibility(View.VISIBLE);
            mBtnVerify.setVisibility(View.GONE);
            m_ll_checking.setVisibility(View.GONE);
            initFaileInfo();
        } else{
            mRlFail.setVisibility(View.GONE);
            mBtnVerify.setVisibility(View.VISIBLE);
            m_ll_checking.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {

    }

    private void initFaileInfo() {
        String s1 = "如有疑问请联系客服\n客服电话";
        String s2 = "010-56136496";
        String s = s1 + s2;
        int start = s1.length();
        int end = (s1 + s2).length();
        final String content1 = s2;
        TextViewUtil.setTextViewLink(mTvFailInfo, s, start, end, new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + content1));
                widget.getContext().startActivity(intent);
            }
        });
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_verify;
    }

    @OnClick(R.id.m_btn_verify)
    public void onViewClicked() {
        //未认证用户直接启动认证页面
//        LogUtils.i("UserManager.getInstance().isPerInfoUpdateable(VerifyActivity.this, false)="+UserManager.getInstance().isPerInfoUpdateable(VerifyActivity.this, false));
        if (UserManager.getInstance().isPerInfoUpdateable(VerifyActivity.this, false)) {
            PersonInfoActivity.start(VerifyActivity.this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
