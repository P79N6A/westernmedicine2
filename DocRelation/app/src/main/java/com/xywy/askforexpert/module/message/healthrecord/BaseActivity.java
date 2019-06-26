package com.xywy.askforexpert.module.message.healthrecord;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xywy.askforexpert.R;

import java.util.Stack;

import cn.jpush.android.api.JPushInterface;


public class BaseActivity extends AppCompatActivity {

    private static Stack<Activity> activityStack;
    private static Handler mApplicationHandler = new Handler(Looper.getMainLooper());
    protected FrameLayout baseContainer;
    protected View commonTitleView;
    protected RelativeLayout leftCommonImgBtn;
    protected ImageButton rightCommonImgBtn;
    //protected ProgressBar rightCommonProgressBar;
    protected Button rightCommonText;
    protected TextView titleCommonTv;
    protected ImageButton leftImgBtn;
    private Toast toast = null;
    private LayoutInflater layoutInflater;

    /**
     * add Activity 添加Activity到栈
     */
    static public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * get current Activity 获取当前Activity（栈中最后一个压入的）
     */
    static public Activity currentActivity() {
        if (activityStack == null) {
            return null;
        }
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（栈中最后一个压入的）
     */
    static public void finishActivity() {
        if (activityStack == null) {
            return;
        }
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    static public void finishActivity(Activity activity) {
        if (activityStack == null) {
            return;
        }
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    static public void finishActivity(Class<?> cls) {
        if (activityStack == null) {
            return;
        }
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    static public void finishAllActivity() {
        if (activityStack == null) {
            return;
        }
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 按照指定类名找到activity
     *
     * @param cls
     * @return
     */
    static public Activity findActivity(Class<?> cls) {
        Activity act = null;
        if (activityStack != null) {
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    act = activity;
                    break;
                }
            }
        }
        return act;
    }

    /**
     * 退出应用程序
     */
    static public void AppExit() {
        try {
            finishAllActivity();
        } catch (Exception e) {
        }
    }

    public Handler getHandler() {
        return mApplicationHandler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        super.setContentView(R.layout.activity_base_for_healthxml);

        initBaseView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
//        L.e("currentActivity", this.getClass().getCanonicalName());
//        StatisticalTools.onResume(this, getStatisticalId());
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
//        StatisticalTools.onPause(this, getStatisticalId());
    }

    /**
     * 将子activity的view 加入到baseactivity 中
     *
     * @param layoutResID
     */
    @Override
    public void setContentView(int layoutResID) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(BaseActivity.this);

        }
        View ChildView = layoutInflater.inflate(layoutResID, null);
        baseContainer.removeAllViews();
        baseContainer.addView(ChildView);
    }

    /**
     * 隐藏公共title
     */
    protected void hideCommonBaseTitle() {
        if (commonTitleView != null) {
            commonTitleView.setVisibility(View.GONE);
        }
    }

    /**
     * 显示公共的title
     */
    protected void showCommonBaseTitle() {

        if (commonTitleView != null) {
            commonTitleView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 在屏幕上添加一个转动的小菊花（传说中的Loading），默认为隐藏状态
     * 注意：务必保证此方法在setContentView()方法后调用，否则小菊花将会处于最底层，被屏幕其他View给覆盖
     *
     * @param activity 需要添加菊花的Activity
     * @return {ProgressBar}    菊花对象
     */
    @SuppressLint("NewApi")
    protected ProgressBar createProgressBar(Activity activity) {
        // activity根部的ViewGroup，其实是一个FrameLayout
        FrameLayout rootContainer = (FrameLayout) activity.findViewById(android.R.id.content);
        // 给progressbar准备一个FrameLayout的LayoutParams
        LayoutParams lp = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置对其方式为：屏幕居中对其
        lp.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
        ProgressBar progressBar = new ProgressBar(activity);
        progressBar.setVisibility(View.GONE);
        progressBar.setLayoutParams(lp);
        // 自定义小菊花
        progressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.iamge_progress));
        // 将菊花添加到FrameLayout中
        rootContainer.addView(progressBar);
        return progressBar;
    }

    public void showToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setText(msg);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    // 点击空白区域 自动隐藏软键盘
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishActivity(this);
    }

    private void initBaseView() {
        baseContainer = (FrameLayout) findViewById(R.id.baseContainer);
        commonTitleView = findViewById(R.id.commonTitle);
        leftCommonImgBtn = (RelativeLayout) findViewById(R.id.Lback);
        titleCommonTv = (TextView) findViewById(R.id.title_name);
        rightCommonImgBtn = (ImageButton) findViewById(R.id.right_btn);
        //rightCommonProgressBar = (ProgressBar) findViewById(R.id.loading_progressbar);
        rightCommonText = (Button) findViewById(R.id.right_text);
        leftCommonImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        leftImgBtn = (ImageButton) findViewById(R.id.left_btn);
    }

    /**
     * 获取 打点统计Id
     *
     * @return
     */
    protected String getStatisticalId() {
        return null;
    }
}
