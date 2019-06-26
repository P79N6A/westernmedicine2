package com.xywy.askforexpert.module.my.gzh;

import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.DialogUtil;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.module.discovery.medicine.common.MyCallBack;

/**
 * 微信公众号
 * @author xg
 */
public class WeChat_Official_Accounts_Activity extends YMBaseActivity {
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_wechat_offical_account;
    }

    @Override
    protected void beforeViewBind() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("关注公众号");
        findViewById(R.id.btn_attention).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.showCustomDialog(WeChat_Official_Accounts_Activity.this,R.layout.custom_dialogue,"系统已复制医脉(寻医问药医生)","微信公众号：xywy医脉","在微信搜索栏中黏贴搜索即可关注",
                        "确定","取消",new MyCallBack<Object>(){

                            @Override
                            public void onClick(Object data) {
                                //立即关注 stone
                                StatisticalTools.eventCount(WeChat_Official_Accounts_Activity.this, Constants.IMMEDIATEATTENTION);

                                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                cm.setText("xyxy医脉");
                            }
                        });
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
