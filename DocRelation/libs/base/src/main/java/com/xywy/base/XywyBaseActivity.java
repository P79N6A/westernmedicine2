package com.xywy.base;

import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.xywy.base.view.ProgressDialog;
import com.xywy.util.KeyBoardUtils;
import com.xywy.util.UIHandler;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/2/24 15:23
 */

public class XywyBaseActivity extends AppCompatActivity {

    protected  final String TAG = getClass().getSimpleName();
    protected Toolbar toolbar;
    protected RelativeLayout root;
    protected TitleBarBuilder titleBarBuilder;
    protected LayoutInflater layoutInflater;

    protected FrameLayout baseContainer;

    private ProgressDialog progressDialog;

    /**
     * Handler 消息处理
     */
    protected UIHandler uiHandler = new UIHandler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSuperUI();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        baseContainer = (FrameLayout) findViewById(R.id.baseContainer);
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(XywyBaseActivity.this);

        }
        if(0!=layoutResID&&-1!=layoutResID){
            View ChildView = layoutInflater.inflate(layoutResID, null);
            baseContainer.removeAllViews();
            baseContainer.addView(ChildView);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //放置handler 内存泄漏
        hideProgressDialog();
        uiHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    /**
     * 初始化父类UI
     */
    private void initSuperUI() {
        super.setContentView(R.layout.base_activity_base);
        baseContainer = (FrameLayout) findViewById(R.id.baseContainer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titleBarBuilder = new TitleBarBuilder(this, toolbar);
        root = (RelativeLayout) findViewById(R.id.ll_root);
        root.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                KeyBoardUtils.closeKeyboard(XywyBaseActivity.this);
            }
        });
    }


    /**
     * 隐藏公共title
     */
    public void hideCommonBaseTitle() {
        if (toolbar != null) {
            toolbar.setVisibility(View.GONE);
        }
    }
    /**
     * 设置隐藏状态栏高度
     */
    public void hideStatusBarHeight(){
        root.setFitsSystemWindows(false);
    }

    /**
     * 显示公共的title
     */
    public void showCommonBaseTitle() {

        if (toolbar != null) {
            toolbar.setVisibility(View.VISIBLE);
        }
    }

    protected void setToolbarTransparent(final int transparent) {
        toolbar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                toolbar.getBackground().setAlpha(transparent);
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) baseContainer.getLayoutParams();
//                layoutParams.setMargins(0, -DensityUtil.dip2px(48),0,0);
                layoutParams.setMargins(0, -toolbar.getHeight(),0,0);
                baseContainer.setLayoutParams(layoutParams);
                toolbar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

    }




    protected void displayFragment(Fragment fragment, boolean isAddToBackStack){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
        beginTransaction.add(R.id.baseContainer, fragment);    //通过事务去管理Fragment
        if (isAddToBackStack){
            beginTransaction.addToBackStack(null);    //处理返回键
        }

        beginTransaction.commit();  //提交
    }

    /**
     * 显示进度dialog
     *
     * @param content 提示文字内容
     */
    public void showProgressDialog(String content) {
        if (null == progressDialog) {
            progressDialog = new ProgressDialog(this, content);
            progressDialog.setCanceledOnTouchOutside(false);

        } else {
            progressDialog.setTitle(content);
        }
        progressDialog.showProgersssDialog();
    }

    /**
     * 隐藏进度dialog
     */
    public void hideProgressDialog() {
        if (null == progressDialog) {
            return;
        }
        if (progressDialog.isShowing()) {
            progressDialog.closeProgersssDialog();
        }
    }
}
