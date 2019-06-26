package com.xywy.askforexpert.module.my.setting;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.xywy.askforexpert.BuildConfig;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.utils.AppUtils;
import com.xywy.askforexpert.module.main.service.que.QuePerActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 项目名称：D_Platform 类名称：AboutActivity 类描述：关于医脉 创建人：shihao 创建时间：2015-6-17
 * 上午10:47:22 修改备注：
 * <p/>
 * 2016.09.2 重构人：王烨明
 */
public class AboutActivity extends YMBaseActivity {

    @Bind(R.id.ym_setting_about_ym)
    TextView vAboutYM;
    @Bind(R.id.ym_setting_about_protocol)
    TextView vAboutProtocol;
    @Bind(R.id.ym_setting_about_version)
    TextView vAboutVersion;

    @Override
    protected int getLayoutResId() {
        return R.layout.ym_activity_about;
    }

    @Override
    protected void beforeViewBind() {

    }

    @Override
    protected void initView() {
        vAboutVersion.setText(getString(R.string.ym_setting_about_version, AppUtils.getAppVersionName(this)));
        if(YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)){
            titleBarBuilder.setTitleText("关于闻康医生助手");
            vAboutYM.setText("关于闻康医生助手");
            Drawable top = getResources().getDrawable(R.mipmap.dp_icon);
            vAboutVersion.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
        }else {
            titleBarBuilder.setTitleText(getString(R.string.ym_setting_about_ym));
        }

    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.ym_setting_about_ym)
    public void routeToAboutYM() {
        Intent about = new Intent(this, QuePerActivity.class);
        if(YMApplication.APPLICATIONID.equals(BuildConfig.APPLICATION_ID)){
            about.putExtra("isfrom", "关于闻康医生助手");
        }else {
            about.putExtra("isfrom", "关于医脉");
        }

        startActivity(about);
    }

    @OnClick(R.id.ym_setting_about_protocol)
    public void routeToAboutProtocol() {
        Intent intent = new Intent(this, QuePerActivity.class);
        intent.putExtra("isfrom", "服务协议");
        startActivity(intent);
    }

}