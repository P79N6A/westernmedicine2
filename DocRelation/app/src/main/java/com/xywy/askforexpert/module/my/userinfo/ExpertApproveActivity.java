package com.xywy.askforexpert.module.my.userinfo;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.utils.ToastUtils;
import com.xywy.askforexpert.appcommon.utils.YMOtherUtils;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.DLog;
import com.xywy.base.view.ProgressDialog;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 专业认证
 *
 * @author 王鹏
 * @2015-9-10下午2:18:13
 */
public class ExpertApproveActivity extends YMBaseActivity {

    private static final String TAG = "ExpertApproveActivity";
    private SharedPreferences sp;
    EditText edit_names, edit_mobile, edit_telephone;
    private final String reg = "^([a-z]|[A-Z]|[0-9]|[\u2E80-\u9FFF]){1,}|@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?|[wap.]{4}|[www.]{4}|[blog.]{5}|[bbs.]{4}|[.com]{4}|[.cn]{3}|[.net]{4}|[.org]{4}|[http://]{7}|[ftp://]{6}$";
    private final static String REGEX_FIXEDPHONE = "^(010|02\\d|0[3-9]\\d{2})?\\d{6,8}$";

    private static Pattern PATTERN_FIXEDPHONE;


    @Override
    protected int getLayoutResId() {
        return R.layout.expertapprove;
    }

    @Override
    protected void beforeViewBind() {
        sp = getSharedPreferences("save_user", MODE_PRIVATE);
    }

    @Override
    protected void initView() {
        hideCommonBaseTitle();

        ((TextView) findViewById(R.id.tv_title)).setText("专业认证");

        edit_names = (EditText) findViewById(R.id.edit_names);

        edit_mobile = (EditText) findViewById(R.id.edit_mobile);// 手机号
        edit_telephone = (EditText) findViewById(R.id.edit_telephone);

    }

    @Override
    protected void initData() {

    }

    /**
     * @param de_phone 科室
     * @param mo_phone 手机
     */
    public void sendData(String de_phone, String mo_phone) {

        final ProgressDialog dialog = new ProgressDialog(this, "正在提交...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.showProgersssDialog();
        final String userid = YMApplication.getLoginInfo().getData().getPid();
        String signs = "};jB;%bT4YM2J|B4UXGJ" + "club" + "add_expert_verify"
                + userid;
        DLog.i(TAG, "sign " + signs);
        String sign = MD5Util.MD5("};jB;%bT4YM2J|B4UXGJ" + "club"
                + "add_expert_verify" + userid);
        DLog.i(TAG, "转吗：" + sign);
        AjaxParams params = new AjaxParams();

        params.put("depart_phone", de_phone);// 科室
        params.put("mobile_phone", mo_phone);// 手机

        params.put("club_id", userid);
        params.put("sign", sign);

        FinalHttp fh = new FinalHttp();
        fh.post(CommonUrl.ExpertApprovUrl, params, new AjaxCallBack() {
            @Override
            public void onSuccess(String t) {
                // TODO Auto-generated method stub
                DLog.i(TAG, "专家认证。。" + t.toString());
                try {
                    JSONObject json = new JSONObject(t.toString());
                    String state = json.getString("state");
                    String msg = json.getString("msg");
                    if ("1".equals(state)) {
                        sp.edit().putBoolean(userid + "expertapp", true)
                                .commit();
                        dialog.dismiss();
                        finish();
                        ToastUtils.shortToast("提交成功");

                    } else {
                        ToastUtils.shortToast(msg);
                        dialog.dismiss();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                super.onSuccess(t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                ToastUtils.shortToast("链接超时，请重新申请");
                DLog.i(TAG, "专家认证错误日志" + strMsg);
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;
            case R.id.next_btn:
                String mobile_phone = edit_mobile.getText().toString();//
                String depart_phone = edit_telephone.getText().toString();
                String edit_name = edit_names.getText().toString();
                Pattern p2 = Pattern.compile(reg);
                Matcher m2 = p2.matcher(edit_name);
                if (TextUtils.isEmpty(edit_name)) {
                    ToastUtils.shortToast( "请输入姓名为2-4个汉字");
                } else if (edit_name.length() < 2 | edit_name.length() > 4) {
                    ToastUtils.shortToast( "请输入姓名为2-4个汉字");
                } else if (!YMOtherUtils.checkNameChese(edit_name)) {
                    ToastUtils.shortToast( "请输入中文");
                } else if (!YMOtherUtils.checkIsBQZF(edit_name)) {
                    ToastUtils.shortToast( "请输入中文");
                } else if (TextUtils.isEmpty(mobile_phone)) {
                    ToastUtils.shortToast( "手机号不能为空");
                } else if (!isMobileNO(mobile_phone)) {
                    ToastUtils.shortToast( "手机号格式不正确");
                } else if (TextUtils.isEmpty(depart_phone)) {
                    ToastUtils.shortToast( "座机号不能为空");
                } else if (!isFixedPhone(depart_phone)) {
                    ToastUtils.shortToast( "座机号格式不对");
                } else {
                    sendData(depart_phone, mobile_phone);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 判断是否为固定电话号码
     *
     * @param number 固定电话号码
     * @return
     */
    public static boolean isFixedPhone(String number) {
        PATTERN_FIXEDPHONE = Pattern.compile(REGEX_FIXEDPHONE);
        Matcher match = PATTERN_FIXEDPHONE.matcher(number);
        return match.matches();
    }

    /**
     * 判断手机格式是否正确
     */
    public static boolean isMobileNO(String mobiles) {

        Pattern p = Pattern
                .compile(/* "^((13[0-9])|(14[5,7])|(17[6-7])|(15[^4,\\D])|(18[0-9]))\\d{8}$" */"^(1[0-9])\\d{9}$");
        Matcher m = p.matcher(mobiles);

        return m.matches();
    }




}
