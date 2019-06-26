package com.xywy.askforexpert.module.doctorcircle.report;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.DLog;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static com.xywy.askforexpert.appcommon.old.Constants.REPORT_STYLE_CIRCLE;

/**
 * 举报页面
 *
 * @author Jack Fang
 */
public class ReportActivity extends YMBaseActivity
        implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    public static final String REPORT_ID_INTENT_KEY = "reportID";
    public static final String REPORT_STYLE_INTENT_KEY = "reportStyle";
    private static final String LOG_TAG = ReportActivity.class.getSimpleName();
    private static final int MAX_CUSTOM_REPORT_REASON_EMS = 128;
    @Bind(R.id.report_reason_1)
    RelativeLayout relativeLayout1;
    @Bind(R.id.report_reason_2)
    RelativeLayout relativeLayout2;
    @Bind(R.id.report_reason_3)
    RelativeLayout relativeLayout3;
    @Bind(R.id.report_reason_4)
    RelativeLayout relativeLayout4;
    @Bind(R.id.report_check_box1)
    AppCompatCheckBox reportCheckBox1;
    @Bind(R.id.report_check_box2)
    AppCompatCheckBox reportCheckBox2;
    @Bind(R.id.report_check_box3)
    AppCompatCheckBox reportCheckBox3;
    @Bind(R.id.report_check_box4)
    AppCompatCheckBox reportCheckBox4;
    @Bind(R.id.report_reason)
    EditText reportReason;
    @Bind(R.id.submit_report)
    AppCompatButton submitReport;
    private String userid;

    private List<RelativeLayout> relativeLayouts = new ArrayList<>();
    private List<AppCompatCheckBox> checkBoxes = new ArrayList<>();
    private List<Integer> selectedItems = new ArrayList<>();
    private String reportID;
    private int reportStyle;


    public static void start(Activity activity, String reportID, int reportStyle) {
        Intent intent = new Intent(activity, ReportActivity.class);
        intent.putExtra(ReportActivity.REPORT_ID_INTENT_KEY, TextUtils.isEmpty(reportID) ? "" : reportID);
        intent.putExtra(ReportActivity.REPORT_STYLE_INTENT_KEY, reportStyle);
        activity.startActivity(intent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_report;
    }

    @Override
    protected void beforeViewBind() {
        reportID = getIntent().getStringExtra(REPORT_ID_INTENT_KEY);
        reportStyle = getIntent().getIntExtra(REPORT_STYLE_INTENT_KEY, REPORT_STYLE_CIRCLE);

        if (YMUserService.isGuest()) {
            userid = "0";
        } else {
            userid = YMApplication.getPID();
        }
    }

    @Override
    protected void initView() {
        titleBarBuilder.setTitleText("举报");

        relativeLayouts.add(relativeLayout1);
        relativeLayouts.add(relativeLayout2);
        relativeLayouts.add(relativeLayout3);
        relativeLayouts.add(relativeLayout4);

        checkBoxes.add(reportCheckBox1);
        checkBoxes.add(reportCheckBox2);
        checkBoxes.add(reportCheckBox3);
        checkBoxes.add(reportCheckBox4);

        for (int i = 0; i < relativeLayouts.size(); i++) {
            relativeLayouts.get(i).setOnClickListener(this);
        }

        for (int i = 0; i < checkBoxes.size(); i++) {
            checkBoxes.get(i).setOnCheckedChangeListener(this);
        }

    }

    @Override
    protected void initData() {

    }


    @OnClick(R.id.submit_report)
    public void onClick() {
        // 提交举报
        String customReason = reportReason.getText().toString().trim();
        if (customReason.length() > MAX_CUSTOM_REPORT_REASON_EMS) {
            Toast.makeText(this, "举报原因最多输入" + MAX_CUSTOM_REPORT_REASON_EMS + "个字", Toast.LENGTH_SHORT).show();
            return;
        }

        String type = "";
        if (selectedItems != null && !selectedItems.isEmpty()) {
            for (int i = 0; i < selectedItems.size(); i++) {
                type += selectedItems.get(i) + ",";
            }
        }

        if (customReason.equals("") && type.equals("")) {
            Toast.makeText(this, "请填写举报原因", Toast.LENGTH_SHORT).show();
            return;
        }

        String sign = MD5Util.MD5(userid + reportID + Constants.MD5_KEY);
        String bind = userid + reportID;
        AjaxParams params = new AjaxParams();
        params.put("m", "theme_report");
        params.put("a", "theme");
        params.put("userid", userid);
        params.put("sign", sign);
        params.put("bind", bind);
        params.put("themeid", reportID);
        params.put("content", customReason);
        if (!"".equals(type)) {
            type = type.substring(0, type.length() - 1);
        }
        params.put("type", type);
        params.put("style", String.valueOf(reportStyle));
        DLog.d(LOG_TAG, "report url = " + CommonUrl.FOLLOW_LIST + params.toString());
        new FinalHttp().post(CommonUrl.FOLLOW_LIST, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);

                DLog.d(LOG_TAG, "report s = " + s);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jsonObject == null) {
                    Toast.makeText(ReportActivity.this, getResources().getString(R.string.server_error),
                            Toast.LENGTH_SHORT).show();
                } else {
                    int code = jsonObject.optInt("code");
                    String msg = jsonObject.optString("msg");

                    if (code == 0) {
                        Toast.makeText(ReportActivity.this, "举报成功", Toast.LENGTH_SHORT).show();
                        ReportActivity.this.finish();
                    } else {
                        Toast.makeText(ReportActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);

                Toast.makeText(ReportActivity.this, "举报失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v instanceof RelativeLayout) {
            RelativeLayout relativeLayout = (RelativeLayout) v;
            int index = relativeLayouts.indexOf(relativeLayout);
            AppCompatCheckBox checkBox = checkBoxes.get(index);
            checkBox.setChecked(!checkBox.isChecked());
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView instanceof AppCompatCheckBox) {
            AppCompatCheckBox checkBox = (AppCompatCheckBox) buttonView;
            int index = checkBoxes.indexOf(checkBox);
            if (isChecked) {
                selectedItems.add(index + 1);
            } else if (selectedItems.contains(index + 1)) {
                selectedItems.remove(Integer.valueOf(index + 1));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
