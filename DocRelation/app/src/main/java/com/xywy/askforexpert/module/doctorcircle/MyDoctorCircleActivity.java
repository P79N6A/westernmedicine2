package com.xywy.askforexpert.module.doctorcircle;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.utils.NetworkUtil;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.module.docotorcirclenew.activity.MyDynamicNewActivity;
import com.xywy.askforexpert.module.docotorcirclenew.activity.MyUnReadMessagesActivity;
import com.xywy.askforexpert.module.docotorcirclenew.model.PublishType;


/**
 * 我的医圈页
 *
 * @author LG
 */
public class MyDoctorCircleActivity extends YMBaseActivity {
    private ImageView iv_back;
    private RelativeLayout  rl_realname_dynamic,
            rl_realname_history;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_myperson_center;
    }

    @Override
    protected void beforeViewBind() {

    }


    @Override
    protected void initView() {
        hideCommonBaseTitle();
        iv_back = (ImageView) findViewById(R.id.iv_back);
        rl_realname_dynamic = (RelativeLayout) findViewById(R.id.rl_realname_dynamic);
        rl_realname_history = (RelativeLayout) findViewById(R.id.rl_realname_history);

        initListener();

    }

    @Override
    protected void initData() {

    }

    private void initListener() {
        MyOnclick myOnclick = new MyOnclick();
        iv_back.setOnClickListener(myOnclick);
        rl_realname_dynamic.setOnClickListener(myOnclick);
        rl_realname_history.setOnClickListener(myOnclick);
    }

    class MyOnclick implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_back:
                    MyDoctorCircleActivity.this.finish();
                    break;

                case R.id.rl_realname_history:// 实名历史
                    if (!NetworkUtil.isNetWorkConnected()) {
                        ToastUtils.shortToast("网络异常，请检查网络连接");
                        return;
                    }
                    Intent intent3 = new Intent(MyDoctorCircleActivity.this, MyUnReadMessagesActivity.class);
                    intent3.putExtra("type", "1");
                    MyDoctorCircleActivity.this.startActivity(intent3);

                    break;

                case R.id.rl_realname_dynamic:// 实名动态
                    if (!NetworkUtil.isNetWorkConnected()) {
                        ToastUtils.shortToast("网络异常，请检查网络连接");
                        return;
                    }
                    MyDynamicNewActivity.startActivity(MyDoctorCircleActivity.this,null, PublishType.Realname, null);
                    break;

            }
        }

    }


}
