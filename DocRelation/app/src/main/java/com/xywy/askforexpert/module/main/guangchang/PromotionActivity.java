package com.xywy.askforexpert.module.main.guangchang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.appcommon.utils.ScreenUtils;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.enctools.MD5Util;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.askforexpert.model.doctor.User;
import com.xywy.askforexpert.widget.view.RoundCornerProgressBar;
import com.xywy.base.view.ProgressDialog;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 功能：晋级申请
 * </p>
 *
 * @author liuxuejiao
 * @2015-05-15 下午14:03:00
 */
public class PromotionActivity extends Activity implements OnClickListener,
        OnItemSelectedListener, OnItemClickListener {

    private static final String TAG = "PromotionActivity";

    /**
     * mBtnBack: 返回按钮
     */
    private View mBtnBack;
    /**
     * mTxtTitle: 标题文字
     */
    private TextView mTxtTitle;
    /**
     * mTxtCurJob：当前职位级别
     */
    private TextView mTxtCurJob;
    /**
     * mTxtDaysPer：答题天数百分比
     */
    private TextView mTxtDaysPer;
    /**
     * mTxtActDayPer：活跃天数百分比
     */
    private TextView mTxtActDayPer;
    /**
     * mBtnPro:申请按钮
     */
    private Button mBtnPro;
    /**
     * mGallery:图片选择
     */
    private Gallery mGallery;
    /**
     * mDayBar:答题天数百分比
     */
    private RoundCornerProgressBar mDayBar;
    /**
     * mActDayBar:活跃天数百分比
     */
    private RoundCornerProgressBar mActDayBar;
    private GalleryImageAdapter mAdapter;

    private User user;

    private LinearLayout rlayout;

    private List<String> listString;

    private int code = -1;

    private Handler handle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1) {
                rlayout.setVisibility(View.VISIBLE);
                mTxtCurJob.setText(user.getMedalName());
                mTxtDaysPer.setText(user.getLoseDate() + "/30");
                mDayBar.setMax(30f);
                mDayBar.setProgress(Integer.parseInt(user.getLoseDate()));
                mTxtActDayPer.setText(user.getWork() + "/" + user.getHy());
                mActDayBar.setMax(Float.parseFloat(user.getHy()));
                mActDayBar.setProgress(Float.parseFloat(user.getWork()));

                mAdapter.setType(user.getUserType());
                mGallery.setAdapter(mAdapter);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);

        CommonUtils.initSystemBar(this);
        getHttpRequest();
        initView();
        initUtil();
        initListener();
        mTxtTitle.setText(R.string.pro_txt_pro);
        ScreenUtils.initScreen(this);
    }

    private void getHttpRequest() {
        final ProgressDialog dialog = new ProgressDialog(
                PromotionActivity.this, "全力加载中...");
        dialog.showProgersssDialog();
        AjaxParams params = new AjaxParams();

        params.put("userid", YMApplication.getLoginInfo().getData().getPid());
        params.put(
                "sign",
                MD5Util.MD5(YMApplication.getLoginInfo().getData().getPid()
                        + Constants.MD5_KEY));
        FinalHttp fh = new FinalHttp();

        fh.post(CommonUrl.QUE_PROMOTION, params, new AjaxCallBack() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                super.onFailure(t, errorNo, strMsg);
            }

            @Override
            public void onLoading(long count, long current) {
                // TODO Auto-generated method stub
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    String msg = jsonObject.getString("msg");
                    code = jsonObject.getInt("code");
                    // T.showShort(PromotionActivity.this, msg);
                    JSONObject jsonElement = jsonObject.getJSONObject("data");

                    if (code == 0) {
                        dialog.closeProgersssDialog();
                        user = new User();
                        user.setLoseDate(jsonElement.getString("losedate_tp"));
                        user.setMedalName(jsonElement.getString("medal_name"));
                        user.setNext(jsonElement.getString("next"));
                        user.setHy(jsonElement.getString("hy"));
                        user.setNote(jsonElement.getString("note"));
                        user.setClick(jsonElement.getString("click"));
                        user.setType(jsonElement.getString("type"));
                        user.setSqState(jsonElement.getString("sq_status"));
                        user.setUserType(jsonElement.getString("usertype"));
                        user.setHy(jsonElement.getString("hy"));
                        user.setWork(jsonElement.getString("is_work"));
                        JSONArray array = jsonElement.getJSONArray("jj_where");
                        listString = new ArrayList<String>();
                        for (int i = 0; i < array.length(); i++) {
                            listString.add(array.getString(i));
                        }

                        Message message = Message.obtain();
                        message.what = 1;
                        handle.sendMessage(message);
                    } else {
                        dialog.closeProgersssDialog();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void initListener() {
        mBtnBack.setOnClickListener(this);
        mBtnPro.setOnClickListener(this);
        mGallery.setOnItemSelectedListener(this);
        mGallery.setOnItemClickListener(this);
    }

    private void initUtil() {
        mAdapter = new GalleryImageAdapter(PromotionActivity.this);
    }

    private void initView() {
        rlayout = (LinearLayout) findViewById(R.id.ll_bottom);
        mTxtCurJob = (TextView) findViewById(R.id.textView_prorank);
        mTxtDaysPer = (TextView) findViewById(R.id.textView_pro_dayper);
        mTxtActDayPer = (TextView) findViewById(R.id.textView_activeday_per);
        mTxtTitle = (TextView) findViewById(R.id.tv_title);
        mBtnPro = (Button) findViewById(R.id.button_promotion);
        mBtnBack =  findViewById(R.id.btn1);
        mGallery = (Gallery) findViewById(R.id.gallery_promotion);
        mGallery.setSpacing(5);
        mDayBar = (RoundCornerProgressBar) findViewById(R.id.progress_days);
        mActDayBar = (RoundCornerProgressBar) findViewById(R.id.progress_active_days);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_promotion:

//			DLog.i(TAG,"晋级申请==ADFASDF"+user.getClick());
                if (code == 0) {
                    if (listString == null || listString.size() == 0) {
                        Toast.makeText(PromotionActivity.this, "晋级申请出错,请稍后再试", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(PromotionActivity.this,
                                PromotionDetailActivity.class);
                        intent.putStringArrayListExtra("content", (ArrayList<String>) listString);
                        intent.putExtra("click", user.getClick());
                        intent.putExtra("type", user.getType());
                        intent.putExtra("note", user.getNote());
                        intent.putExtra("next", user.getNext());
                        intent.putExtra("med_name", user.getMedalName());
                        intent.putExtra("sq_status", user.getSqState());
                        startActivityForResult(intent, 5000);
                    }
                }
                break;
            case R.id.btn1:
                this.finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3) {
        mAdapter.setSelectItem(arg2);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        mAdapter.setSelectItem(arg2);
    }

    public void onResume() {
        super.onResume();
        StatisticalTools.onResume(this);
    }

    public void onPause() {
        StatisticalTools.onPause(this);
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5000) {
            if (resultCode == 5000) {
                getHttpRequest();
            }
        }
    }


}
